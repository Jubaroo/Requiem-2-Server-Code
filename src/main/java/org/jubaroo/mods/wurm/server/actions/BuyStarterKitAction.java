package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.Items;
import com.wurmonline.server.NoSuchItemException;
import com.wurmonline.server.NoSuchPlayerException;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.NoSuchCreatureException;
import com.wurmonline.server.items.*;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.zones.NoSuchZoneException;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.util.Collections;
import java.util.List;

public class BuyStarterKitAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;
    private final String npcName = "Guide";

    public BuyStarterKitAction() {
        RequiemLogging.logInfo(BuyStarterKitAction.class.getName());
        this.actionId = (short) ModActions.getNextActionId();
        ModActions.registerAction(this.actionEntry = ActionEntry.createEntry(this.actionId, "Buy a starter kit (1 silver)", "buying a starter kit", new int[]{
                ActionEntry.ACTION_TYPE_NOMOVE
        }));
    }

    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            public List<ActionEntry> getBehavioursFor(final Creature performer, final Creature object) {
                return this.getBehavioursFor(performer, null, object);
            }

            public List<ActionEntry> getBehavioursFor(final Creature performer, final Item subject, final Creature target) {
                if (performer instanceof Player && target != null && target.getName().equals(npcName)) {
                    return Collections.singletonList(BuyStarterKitAction.this.actionEntry);
                }
                return null;
            }
        };
    }

    public ActionPerformer getActionPerformer() {
        return new ActionPerformer() {
            public short getActionId() {
                return BuyStarterKitAction.this.actionId;
            }

            public boolean action(final Action act, final Creature performer, final Creature target, final short action, final float counter) {
                if (performer instanceof Player && target != null && target.getName().equals(npcName)) {
                    performer.getCommunicator().sendNormalServerMessage(String.format("%s says, \"Buy a kit with what? Think this is charity? Activate a coin of correct value and talk to me again.\"", npcName));
                }
                return true;
            }

            public boolean action(final Action act, final Creature performer, final Item source, final Creature target, final short action, final float counter) {
                try {
                    if (!performer.isPlayer() || target == null || !target.getName().equals(npcName)) {
                        return true;
                    }
                    if (source.getTemplateId() != ItemList.coinSilver) {
                        performer.getCommunicator().sendNormalServerMessage(npcName + " says, \"You need to pay me and I don't have any change!\"");
                        return true;
                    }
                    final String creator = npcName;
                    final Item pouch = ItemFactory.createItem(ItemList.satchel, 5f, (byte) 0, creator);
                    pouch.setColor(WurmColor.createColor(190, 1, 1));
                    pouch.isNoImprove();
                    final Item hammer = ItemFactory.createItem(ItemList.hammerMetal, 35f, (byte) 0, creator);
                    pouch.insertItem(hammer);
                    final Item pickAxe = ItemFactory.createItem(ItemList.pickAxe, 50f, (byte) 0, creator);
                    pouch.insertItem(pickAxe);
                    final Item shaft = ItemFactory.createItem(ItemList.shaft, 50f, (byte) 0, creator);
                    pouch.insertItem(shaft);
                    final Item tabard = ItemFactory.createItem(CustomItems.requiemTabard.getTemplateId(), 90f, (byte) 0, creator);
                    tabard.setHasNoDecay(true);
                    pouch.insertItem(tabard, true);
                    final Item chisel = ItemFactory.createItem(ItemList.stoneChisel, 35f, (byte) 0, creator);
                    pouch.insertItem(chisel);
                    final Item ironLump = ItemFactory.createItem(ItemList.ironBar, 60f, (byte) 0, creator);
                    ironLump.setWeight(20000, true);
                    pouch.insertItem(ironLump);
                    final Item pendulum = ItemFactory.createItem(ItemList.pendulum, 50f, (byte) 0, creator);
                    pouch.insertItem(pendulum);
                    final Item papyrus = ItemFactory.createItem(ItemList.papyrusSheet, 20f, (byte) 0, creator);
                    final String str = "\";maxlines=\"0\"}text{text=\"Enjoy your starter kit, we hope you find it useful!\n\nMore help can be found by doing small missions for the Guide.\n\nYou can also ask for help from others.\n\nVisit our website for the most information you can get.\n\nDiscord is integrated into the server for easy communication between players and staff.\n\nUse the items provided to go out and find yourself a place to call home.\n\nThank you for choosing this server and have fun!\n\n \n";
                    papyrus.setInscription(str, creator);
                    pouch.insertItem(papyrus);
                    final Item waterSkin = ItemFactory.createItem(ItemList.skinWater, 35f, (byte) 0, creator);
                    final Item water = ItemFactory.createItem(ItemList.water, 99f, (byte) 0, creator);
                    water.setWeight(2500, true);
                    waterSkin.insertItem(water);
                    pouch.insertItem(waterSkin);
                    RequiemLogging.logInfo(String.format("DESTROYING %s (material: %s) because they bought a starter kit with it", source.getName(), source.getMaterial()));
                    Items.destroyItem(source.getWurmId());
                    if (performer.getInventory().getNumItemsNotCoins() < 100) {
                        performer.getInventory().insertItem(pouch, true);
                        performer.getCommunicator().sendNormalServerMessage("You buy a starter kit.");
                    } else {
                        pouch.putItemInfrontof(performer);
                        performer.getCommunicator().sendNormalServerMessage("Your starter kit has been placed on the ground in front of you because you have no room in your inventory.");
                    }
                } catch (FailedException | NoSuchTemplateException | NoSuchCreatureException | NoSuchItemException | NoSuchPlayerException | NoSuchZoneException ex2) {
                    RequiemLogging.logException("Problem selling starter kit", ex2);
                    throw new RuntimeException(ex2);
                }
                return true;
            }
        };
    }

}
