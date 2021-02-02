package org.jubaroo.mods.wurm.server.spells;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.FailedException;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.CreatureTemplateIds;
import com.wurmonline.server.creatures.Traits;
import com.wurmonline.server.items.*;
import com.wurmonline.server.skills.Skill;
import com.wurmonline.server.spells.ModReligiousSpell;
import com.wurmonline.server.spells.Spell;
import com.wurmonline.server.spells.SpellEffect;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zones;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modsupport.actions.ActionEntryBuilder;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.util.logging.Level;

public class FeedingHand extends ModReligiousSpell {

    public FeedingHand() {
        super("Feeding Hand", ModActions.getNextActionId(), 30, 30, 30, 20, 1000L);
        this.targetItem = true;

        try {
            ReflectionUtil.setPrivateField(this, ReflectionUtil.getField(Spell.class, "description"), "Periodically feeds nearby animals");
        } catch (Exception e) {
            logger.log(Level.WARNING, null, e);
        }

        ActionEntry actionEntry = new ActionEntryBuilder((short) number, name, "casting",
                new int[]{
                        ActionTypesProxy.ACTION_TYPE_SPELL,
                        ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM,
                        ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS
                }).build();

        ModActions.registerAction(actionEntry);
    }

    public static boolean isValidTarget(Item target) {
        return target.getTemplateId() == ItemList.hopper;
    }

    @Override
    public boolean precondition(final Skill castSkill, final Creature performer, final Item target) {
        if (!isValidTarget(target)) {
            performer.getCommunicator().sendNormalServerMessage("This spell only works on food storage bins.");
            return false;
        }
        return true;
    }

    @Override
    public boolean precondition(final Skill castSkill, final Creature performer, final Creature target) {
        return false;
    }

    @Override
    public void doEffect(final Skill castSkill, final double power, final Creature performer, final Item target) {
        if (!isValidTarget(target)) {
            performer.getCommunicator().sendNormalServerMessage("The spell fizzles.");
            return;
        }

        ItemSpellEffects effs = target.getSpellEffects();
        if (effs == null) {
            effs = new ItemSpellEffects(target.getWurmId());
        }
        SpellEffect eff = effs.getSpellEffect(BUFF_DARKMESSENGER);
        if (eff == null) {
            performer.getCommunicator().sendNormalServerMessage(String.format("The %s will periodically feed animals around it.", target.getName()), (byte) 2);
            eff = new SpellEffect(target.getWurmId(), BUFF_DARKMESSENGER, (float) power, 20000000);
            effs.addSpellEffect(eff);
            Server.getInstance().broadCastAction(String.format("%s looks pleased.", performer.getName()), performer, 5);
        } else if ((double) eff.getPower() > power) {
            performer.getCommunicator().sendNormalServerMessage("You frown as you fail to improve the power.", (byte) 3);
            Server.getInstance().broadCastAction(String.format("%s frowns.", performer.getName()), performer, 5);
        } else {
            performer.getCommunicator().sendNormalServerMessage(String.format("You succeed in improving the power of the %s.", this.name), (byte) 2);
            eff.improvePower(performer, (float) power);
            Server.getInstance().broadCastAction(String.format("%s looks pleased.", performer.getName()), performer, 5);
        }
    }

    private static Item findFood(Item container, Creature creature) {
        for (Item food : container.getItems()) {
            if (food.getTemplateId() != ItemList.bulkItem) continue;
            ItemTemplate rt = food.getRealTemplate();
            if (rt == null) continue;
            if (creature.getTemplate().getTemplateId() == CreatureTemplateIds.SEAL_CID) {
                if (rt.isFish())
                    return food;
                else
                    continue;
            }
            if (rt.isMeat() || rt.isFish()) {
                if (creature.isCarnivore() || creature.isOmnivore())
                    return food;
                else
                    continue;
            }
            if (rt.isSeed() || rt.isVegetable() || rt.isHerb()) {
                if (creature.isHerbivore() || creature.isOmnivore())
                    return food;
                else
                    continue;
            }
            if (rt.isFood() && creature.isOmnivore())
                return food;
        }
        return null;
    }

    private static Item findFoodForCoop(Item container, int minNum) {
        for (Item food : container.getItems()) {
            if (food.getTemplateId() != ItemList.bulkItem) continue;
            ItemTemplate rt = food.getRealTemplate();
            if (rt != null && rt.isSeed() && food.getBulkNums() >= minNum) return food;
        }
        return null;
    }

    private static boolean canGrazeOnTile(Creature creature, byte tile) {
        if (tile == Tiles.Tile.TILE_FIELD.id || tile == Tiles.Tile.TILE_FIELD2.id) return true;
        if (creature.hasTrait(Traits.TRAIT_CORRUPT))
            return tile == Tiles.Tile.TILE_MYCELIUM.id;
        else
            return tile == Tiles.Tile.TILE_GRASS.id || tile == Tiles.Tile.TILE_STEPPE.id || tile == Tiles.Tile.TILE_ENCHANTED_GRASS.id;
    }

    public static void poll(Item container, float power) {
        long start = System.currentTimeMillis();

        int tileX = container.getTileX();
        int tileY = container.getTileY();
        int radius = (int) (power / 20f);

        int creatures = 0, fed = 0, coops = 0;

        for (int x = tileX - radius; x <= tileX + radius; x++) {
            for (int y = tileY - radius; y <= tileY + radius; y++) {
                VolaTile vt = Zones.getTileOrNull(x, y, container.isOnSurface());
                if (vt == null) continue;
                for (Creature creature : vt.getCreatures()) {
                    if (!creature.isNeedFood() || !creature.canEat()) continue;

                    if (creature.isGrazer() && creature.isOnSurface() && canGrazeOnTile(creature, Tiles.decodeType(Server.surfaceMesh.getTile(x, y))))
                        continue;

                    creatures++;

                    Item food = findFood(container, creature);
                    if (food == null) continue;

                    ItemTemplate foodTemplate = food.getRealTemplate();

                    int baseVolume = foodTemplate.getVolume();
                    int volumeToRemove = food.getWeightGrams();
                    float partial = 1.0f;
                    float fishMod = 1;

                    if (foodTemplate.isFish() && foodTemplate.getTemplateId() != 369)
                        fishMod = food.getCurrentQualityLevel() / 100.0f;

                    if (volumeToRemove < baseVolume)
                        partial = volumeToRemove / baseVolume;
                    else
                        volumeToRemove = baseVolume;

                    food.setWeight(food.getWeightGrams() - volumeToRemove, true);

                    float hungerStilled = (float) foodTemplate.getWeightGrams() * partial * fishMod * food.getCurrentQualityLevel();

                    if (creature.getSize() == 5) {
                        hungerStilled *= 0.5F;
                    } else if (creature.getSize() == 4) {
                        hungerStilled *= 0.7F;
                    } else if (creature.getSize() == 2) {
                        hungerStilled *= 5.0F;
                    } else if (creature.getSize() == 1) {
                        hungerStilled *= 10.0F;
                    }

                    float nutrition;

                    if (foodTemplate.isHighNutrition()) {
                        nutrition = 0.56F + food.getQualityLevel() / 300.0F;
                    } else if (foodTemplate.isGoodNutrition()) {
                        nutrition = 0.4F + food.getQualityLevel() / 500.0F;
                    } else if (foodTemplate.isMediumNutrition()) {
                        nutrition = 0.3F + food.getQualityLevel() / 1000.0F;
                    } else {
                        nutrition = 0.1F + food.getQualityLevel() / 1000.0F;
                    }

                    creature.getStatus().modifyHunger((int) -hungerStilled, nutrition);

                    fed++;
                }
                for (Item coop : vt.getItems()) {
                    if (coop.getTemplateId() != ItemList.chickenCoop) continue;
                    coops++;
                    int henCount = 0, foodCount = 0;
                    Item feeder = null;
                    for (Item i : coop.getItems()) {
                        if (i.getTemplateId() == ItemList.nestingBox) {
                            henCount = i.getItems().size();
                        }
                        if (i.getTemplateId() == ItemList.feeder) {
                            foodCount = i.getItems().size();
                            feeder = i;
                        }
                    }
                    int wantFood = henCount * 2 - foodCount;
                    if (wantFood <= 0 || feeder == null) continue;
                    Item food = findFoodForCoop(container, wantFood);
                    if (food == null) continue;
                    int rtId = food.getRealTemplateId();
                    float ql = food.getQualityLevel();
                    byte mat = food.getMaterial();
                    byte aux = food.getAuxData();
                    food.setWeight(food.getWeightGrams() - food.getRealTemplate().getVolume() * wantFood, true);
                    while (wantFood-- > 0) {
                        try {
                            Item newItem = ItemFactory.createItem(rtId, ql, mat, (byte) 0, null);
                            newItem.setAuxData(aux);
                            feeder.insertItem(newItem, true, false);
                        } catch (FailedException | NoSuchTemplateException e) {
                            RequiemLogging.logException("Failed to make food for coop", e);
                        }
                    }
                }
            }
        }

        RequiemLogging.logInfo(String.format("Poll feeding hand - %d at (%d,%d) p=%d r=%d - fed %d/%d - coops %d - took %d ms", container.getWurmId(), container.getTileX(), container.getTileY(), (int) power, radius, fed, creatures, coops, System.currentTimeMillis() - start));
    }

}