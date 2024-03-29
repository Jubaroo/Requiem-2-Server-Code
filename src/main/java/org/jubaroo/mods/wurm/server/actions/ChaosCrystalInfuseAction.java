package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.Items;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.Materials;
import com.wurmonline.server.items.WurmColor;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.skills.SkillList;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.misc.Crystals;

import java.util.Collections;
import java.util.List;

public class ChaosCrystalInfuseAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public ChaosCrystalInfuseAction() {
        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Infuse",
                "infusing",
                new int[]{ActionTypesProxy.ACTION_TYPE_NOMOVE}
        );
        ModActions.registerAction(actionEntry);
    }


    @Override
    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            // Menu with activated object
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item object) {
                if (performer instanceof Player && source != null && object != null && source.getTemplateId() == CustomItems.chaosCrystalId && object.isRepairable() && object.getTemplateId() != ItemList.unfinishedItem) {
                    return Collections.singletonList(actionEntry);
                }
                return null;
            }
        };
    }

    @Override
    public ActionPerformer getActionPerformer() {
        return new ActionPerformer() {

            @Override
            public short getActionId() {
                return actionId;
            }

            // With activated object
            @Override
            public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
                try {
                    if (performer instanceof Player) {
                        if (source.getTemplate().getTemplateId() != CustomItems.chaosCrystalId) {
                            performer.getCommunicator().sendNormalServerMessage("You must use a chaos crystal to infuse an item.");
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        if (!target.isRepairable()) {
                            performer.getCommunicator().sendNormalServerMessage("You cannot infuse that item.");
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        if (target.getTemplateId() == ItemList.unfinishedItem) {
                            performer.getCommunicator().sendNormalServerMessage("You cannot infuse an unfinished item.");
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        if (Crystals.shouldCancelInfusion(performer, source, target)) {
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        if (counter == 1f) {
                            performer.getCommunicator().sendNormalServerMessage("You begin to infuse the " + target.getName() + " with the " + source.getName() + ".");
                            Server.getInstance().broadCastAction(performer.getName() + " begins infusing with a " + source.getName() + ".", performer, 5);
                            act.setTimeLeft(300);
                            performer.sendActionControl("Infusing", true, act.getTimeLeft());
                        } else if (counter * 10f > performer.getCurrentAction().getTimeLeft()) {
                            double diff = Crystals.getInfusionDifficulty(performer, source, target);
                            double power = performer.getSkills().getSkill(SkillList.SOUL).skillCheck(diff, source, 0d, false, 1);
                            if (power > 90) {
                                performer.getCommunicator().sendNormalServerMessage("You handle the crystals expertly and infuse the " + target.getName() + ", increasing its rarity!");
                                target.setRarity(source.getRarity());
                                Items.destroyItem(source.getWurmId());
                            } else if (power > 60) {
                                if (target.isMetal() && Server.rand.nextBoolean()) {
                                    performer.getCommunicator().sendNormalServerMessage("You carefully infuse the metal " + target.getName() + ", changing its material!");
                                    byte[] mats = {Materials.MATERIAL_ADAMANTINE, Materials.MATERIAL_BRASS, Materials.MATERIAL_BRONZE, Materials.MATERIAL_COPPER,
                                            Materials.MATERIAL_GLIMMERSTEEL, Materials.MATERIAL_GOLD, Materials.MATERIAL_IRON, Materials.MATERIAL_IRON, Materials.MATERIAL_LEAD,
                                            Materials.MATERIAL_SERYLL, Materials.MATERIAL_SILVER, Materials.MATERIAL_STEEL, Materials.MATERIAL_TIN, Materials.MATERIAL_ZINC};
                                    target.setMaterial(mats[Server.rand.nextInt(mats.length)]);
                                } else if (target.isWood() && Server.rand.nextBoolean()) {
                                    performer.getCommunicator().sendNormalServerMessage("You carefully infuse the wooden " + target.getName() + ", changing its material!");
                                    byte[] mats = {Materials.MATERIAL_WOOD_APPLE, Materials.MATERIAL_WOOD_BIRCH, Materials.MATERIAL_WOOD_BLUEBERRY, Materials.MATERIAL_WOOD_CAMELLIA,
                                            Materials.MATERIAL_WOOD_CEDAR, Materials.MATERIAL_WOOD_CHERRY, Materials.MATERIAL_WOOD_CHESTNUT, Materials.MATERIAL_WOOD_FIR,
                                            Materials.MATERIAL_WOOD_GRAPE, Materials.MATERIAL_WOOD_HAZELNUT, Materials.MATERIAL_WOOD_IVY, Materials.MATERIAL_WOOD_LAVENDER,
                                            Materials.MATERIAL_WOOD_LEMON, Materials.MATERIAL_WOOD_LINDEN, Materials.MATERIAL_WOOD_LINGONBERRY, Materials.MATERIAL_WOOD_MAPLE,
                                            Materials.MATERIAL_WOOD_OAK, Materials.MATERIAL_WOOD_OLEANDER, Materials.MATERIAL_WOOD_OLIVE, Materials.MATERIAL_WOOD_ORANGE,
                                            Materials.MATERIAL_WOOD_PINE, Materials.MATERIAL_WOOD_RASPBERRY, Materials.MATERIAL_WOOD_ROSE, Materials.MATERIAL_WOOD_THORN,
                                            Materials.MATERIAL_WOOD_WALNUT, Materials.MATERIAL_WOOD_WILLOW};
                                    target.setMaterial(mats[Server.rand.nextInt(mats.length)]);
                                } else {
                                    performer.getCommunicator().sendNormalServerMessage("You carefully infuse the " + target.getName() + ", changing its color.");
                                    target.setColor(WurmColor.createColor(Server.rand.nextInt(255), Server.rand.nextInt(255), Server.rand.nextInt(255)));
                                }
                                Items.destroyItem(source.getWurmId());
                            } else if (power > 30) {
                                performer.getCommunicator().sendNormalServerMessage("You safely infuse the " + target.getName() + ", chaotically changing it its weight.");
                                target.setWeight((int) ((target.getWeightGrams() * 0.5f) + target.getWeightGrams() * Server.rand.nextFloat() * 1.5f), false);
                                Items.destroyItem(source.getWurmId());
                            } else if (power > 0) {
                                performer.getCommunicator().sendNormalServerMessage("You barely manage to infuse the " + target.getName() + ", chaotically changing its quality.");
                                target.setQualityLevel(Server.rand.nextFloat() * 100f);
                                Items.destroyItem(source.getWurmId());
                            } else if (power > -20) {
                                performer.getCommunicator().sendNormalServerMessage("You fail to infuse the " + target.getName() + ", damaging to the " + source.getName() + " in the process.");
                                source.setDamage((float) (source.getDamage() - (power)));
                            } else if (power > -40) {
                                performer.getCommunicator().sendNormalServerMessage("You horribly fail to infuse the " + target.getName() + ", destroying the " + source.getName() + " and heavily damaging the " + target.getName() + ".");
                                target.setDamage((float) (target.getDamage() - (power)));
                                Items.destroyItem(source.getWurmId());
                            } else {
                                if (target.getMaterial() == Materials.MATERIAL_SERYLL) {
                                    performer.getCommunicator().sendNormalServerMessage("The infusion fails catastrophically, destroying the " + source.getName() + "! However, the " + target.getName() + "'s material prevents its utter destruction.");
                                    target.setDamage((float) (target.getDamage() - power));
                                    Items.destroyItem(source.getWurmId());
                                } else {
                                    performer.getCommunicator().sendNormalServerMessage("The infusion fails catastrophically, destroying the " + source.getName() + " and " + target.getName() + "!");
                                    Items.destroyItem(source.getWurmId());
                                    Items.destroyItem(target.getWurmId());
                                }
                            }
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                    } else {
                        RequiemLogging.logWarning(String.format("Somehow a non-player activated a custom item (%d)...", source.getTemplateId()));
                    }
                    return propagate(act, ActionPropagation.CONTINUE_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                } catch (Exception e) {
                    RequiemLogging.logException("[Error] in action in ChaosCrystalInfuseAction", e);
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
            }
        };
    }
}