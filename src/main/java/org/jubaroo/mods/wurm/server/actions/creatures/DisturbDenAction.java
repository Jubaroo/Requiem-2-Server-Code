package org.jubaroo.mods.wurm.server.actions.creatures;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.behaviours.Methods;
import com.wurmonline.server.creatures.*;
import com.wurmonline.server.economy.Economy;
import com.wurmonline.server.items.*;
import com.wurmonline.server.players.Player;
import com.wurmonline.shared.constants.CreatureTypes;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.ItemTools;

import java.util.Collections;
import java.util.List;

public class DisturbDenAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final ActionEntry actionEntry;

    public DisturbDenAction() {
        actionEntry = ActionEntry.createEntry((short) ModActions.getNextActionId(), "Look for treasure", "searching", new int[]{
                ActionTypesProxy.ACTION_TYPE_NEED_FOOD,
                ActionTypesProxy.ACTION_TYPE_FATIGUE,
                ActionTypesProxy.ACTION_TYPE_NOMOVE,
                ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS,
                ActionTypesProxy.ACTION_TYPE_NEVER_USE_ACTIVE_ITEM
        });
        try {
            ReflectionUtil.setPrivateField(actionEntry, ReflectionUtil.getField(ActionEntry.class, "maxRange"), 4);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        ModActions.registerAction(actionEntry);
    }

    @Override
    public short getActionId() {
        return actionEntry.getNumber();
    }

    @Override
    public BehaviourProvider getBehaviourProvider() {
        return this;
    }

    @Override
    public ActionPerformer getActionPerformer() {
        return this;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
        return getBehavioursFor(performer, null, target);
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
        if (performer instanceof Player && target != null && target.getTemplateId() == ItemList.creatureSpawn)
            return Collections.singletonList(actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {
        return action(action, performer, target, num, counter);
    }


    @Override
    public boolean action(Action action, Creature performer, Item target, short num, float counter) {
        try {
            Communicator comm = performer.getCommunicator();
            int creatureType = target.getData1();
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(creatureType);
            if (target.getData2() > 0) {
                comm.sendNormalServerMessage("Doesn't look like there's anything here, maybe return later.");
                return true;
            }
            if (counter == 1f) {
                comm.sendNormalServerMessage(String.format("You start searching the %s for some treasure.", target.getName()));
                comm.sendAlertServerMessage(String.format("Beware of angry %ss!", template.getName().toLowerCase()), (byte) 1);
                action.setTimeLeft(60);
                performer.sendActionControl("searching", true, 60);
                performer.playAnimation("forage", false);
                Methods.sendSound(performer, "sound.work.foragebotanize");
            } else {
                if (action.justTickedSecond() && Server.rand.nextBoolean()) {
                    Methods.sendSound(performer, template.getHitSound(MiscConstants.SEX_MALE));
                }
                if (counter * 10f > action.getTimeLeft()) {
                    target.setData2(35 + Server.rand.nextInt(35));
                    giveLootFromDen(performer, comm);
                    spawnDefenders(performer, creatureType, template, comm, target);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            RequiemLogging.logWarning( "search action error" + e);
            return true;
        }
    }

    private void giveLootFromDen(Creature performer, Communicator comm) {
        Item inv = performer.getInventory();
        try {
            int animalParts = Server.rand.nextInt(4);
            switch (animalParts) {
                case 0:
                    Item eyes = ItemFactory.createItem(ItemList.eye, 1f + (Server.rand.nextFloat() * 98.9f), Materials.MATERIAL_MEAT, MiscConstants.COMMON, null);
                    inv.insertItem(eyes);
                    comm.sendNormalServerMessage("You find some eyes.");
                    break;
                case 1:
                    Item fat = ItemFactory.createItem(ItemList.tallow, 1f + (Server.rand.nextFloat() * 98.9f), Materials.MATERIAL_FAT, MiscConstants.COMMON, null);
                    fat.setWeight(1000 + Server.rand.nextInt(9000), true);
                    inv.insertItem(fat);
                    comm.sendNormalServerMessage("You find a pile of fat.");
                    break;
                case 2:
                    Item eggs = ItemFactory.createItem(ItemList.eggSmall, 1f + (Server.rand.nextFloat() * 98.9f), Materials.MATERIAL_DAIRY, MiscConstants.COMMON, null);
                    inv.insertItem(eggs);
                    comm.sendNormalServerMessage("You find some eggs.");
                    break;
                case 3:
                    ItemTools.createRandomMaterialsConstruction(1f, 99f, null);
                    ItemTools.createRandomMaterialsLumps(1f, 99f, null);
                    break;
                case 4:
                    return;
                case 5:
                    return;
                case 6:
                    return;
                case 7:
                    return;
                case 8:
                    return;
                case 9:
                    return;
            }

            if (Server.rand.nextInt(50) == 0) {
                long iron = 10000; // 1 silver
                iron += Server.rand.nextInt(20000); // add up to 2 silver extra
                Item[] coins = Economy.getEconomy().getCoinsFor(iron);
                for (Item coin : coins) {
                    inv.insertItem(coin, true);
                }
                comm.sendNormalServerMessage("You find some coin!");
            }

            if (Server.rand.nextInt(150) == 0) {
                Item dragonEgg = ItemFactory.createItem(ItemList.eggLarge, 99f, Materials.MATERIAL_DAIRY, MiscConstants.COMMON, null);
                dragonEgg.setData1(CreatureTemplateCreator.getRandomDragonOrDrakeId());
                inv.insertItem(dragonEgg);
                comm.sendNormalServerMessage("You find a dragon egg!");
            }

            if (Server.rand.nextInt(200) == 0) {
                Item treasureChest = ItemFactory.createItem(ItemList.treasureChest, 99f, Materials.MATERIAL_GOLD, MiscConstants.COMMON, null);
                inv.insertItem(treasureChest);
                comm.sendNormalServerMessage("You find a treasure Chest!");
            }

        } catch (NoSuchTemplateException | FailedException e) {
            e.printStackTrace();
        }

    }

    private void spawnDefenders(Creature performer, int creatureType, CreatureTemplate template, Communicator comm, Item lair) {
        boolean attack = template.isAggHuman() || template.isMonster();

        for (int i = 0; i < 5; i++) {
            try {
                byte sex = template.getSex();
                if (sex == 0 && !template.keepSex && Server.rand.nextInt(2) == 0) {
                    sex = 1;
                }
                Creature spawned = Creature.doNew(creatureType, i == 0 ? CreatureTypes.C_MOD_CHAMPION : CreatureTypes.C_MOD_NONE, lair.getPosX() - 5f + Server.rand.nextFloat() * 10, lair.getPosY() - 5f + Server.rand.nextFloat() * 10, Server.rand.nextInt(360), lair.isOnSurface() ? 0 : -1, "", sex);
                if (attack)
                    spawned.setOpponent(performer);
            } catch (Exception e) {
                RequiemLogging.logWarning(String.format("Error spawning defenders for searching lair type %d", creatureType) + e);
            }
        }

        if (attack) {
            comm.sendAlertServerMessage(String.format("Angry %ss from the %s rush to defend their home!", template.getName().toLowerCase(), lair.getName()), (byte) 1);
        } else {
            comm.sendNormalServerMessage(String.format("Some %ss come out of the %sand and throw you angry looks for disturbing their home but do not attack.", template.getName().toLowerCase(), lair.getName()));
        }
    }

}