package org.jubaroo.mods.wurm.server.actions.items;

import com.wurmonline.server.Items;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.behaviours.AthanorMechanismBehaviour;
import org.jubaroo.mods.wurm.server.misc.templates.EnchantScrollTemplate;

import java.util.Collections;
import java.util.List;

public class PlaceGemAction implements ModAction {

    private final short actionId;
    private final ActionEntry actionEntry;
    private final int mechanismID;
    private final EnchantScrollTemplate[] enchantScrollTemplates;

    public PlaceGemAction(int id, EnchantScrollTemplate[] est) {
        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(actionId, "Place Gem", "placing", new int[]{
                ActionTypesProxy.ACTION_TYPE_NOMOVE,
                ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS
        });
        ModActions.registerAction(actionEntry);
        mechanismID = id;
        enchantScrollTemplates = est;
    }

    private void giveItems(Item mechanism, Item gem, Creature player, EnchantScrollTemplate[] enchantScrollTemplates) {
        if (isMechanism(mechanism.getTemplateId()) && player.isPlayer()) {
            try {
                int itemID = enchantScrollTemplates[Server.rand.nextInt(enchantScrollTemplates.length)].templateID;
                Item newItem = ItemFactory.createItem(itemID, 55.0F + (Server.rand.nextFloat() * 40.0F), null);
                newItem.setRarity((byte) 1);
                //Insert Items
                for (int i = 0; i < 2; i++) {
                    player.getInventory().insertItem(ItemFactory.createItem(ItemList.riftCrystal, 50.0F + (Server.rand.nextFloat() * 40.0F), null));
                    player.getInventory().insertItem(ItemFactory.createItem(ItemList.riftStone, 50.0F + (Server.rand.nextFloat() * 40.0F), null));
                    player.getInventory().insertItem(ItemFactory.createItem(ItemList.riftWood, 50.0F + (Server.rand.nextFloat() * 40.0F), null));
                }
                for (int i = 0; i < 2; i++) {
                    player.getInventory().insertItem(ItemFactory.createItem(ItemList.sleepPowder, 50.0F + (Server.rand.nextFloat() * 40.0F), null));
                }
                for (int i = 0; i < 1; i++) {
                    player.getInventory().insertItem(newItem);
                }
                    player.getCommunicator().sendSafeServerMessage(String.format("A %s and some other items emerge from the mechanism.", newItem.getName().toLowerCase()));
                Items.destroyItem(gem.getWurmId());
            } catch (Exception ex) {
                RequiemLogging.logWarning( ex.getMessage() + ex);
            }
        }
    }

    private boolean isMechanism(int itemTemplateID) {
        return itemTemplateID == mechanismID;
    }

    private boolean isStarGem(int itemTemplateID) {
        switch (itemTemplateID) {
            case ItemList.rubyStar:
            case ItemList.emeraldStar:
            case ItemList.sapphireStar:
            case ItemList.opalBlack:
            case ItemList.diamondStar:
                return true;
            default:
                return false;
        }
    }

    @Override
    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
                if (performer instanceof Player && isMechanism(target.getTemplateId()) && isStarGem(source.getTemplateId())) {
                    return Collections.singletonList(actionEntry);
                } else {
                    return null;
                }
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

            @Override
            public boolean action(Action action, Creature performer, Item source, Item mechanism, short act, float counter) {
                try {
                    if (!performer.isWithinDistanceTo(mechanism, 5)) {
                        performer.getCommunicator().sendNormalServerMessage("You need to be closer to the mechanism.");
                        return true;
                    }
                    if (counter == 1.0f) {
                        performer.getCommunicator().sendNormalServerMessage("You start to place the gem into the mechanism.");
                        final int time = 120; // 10th of seconds. 50 means 5 seconds
                        performer.getCurrentAction().setTimeLeft(time);
                        performer.sendActionControl("Placing Gem", true, time);
                    } else if (performer.getCurrentAction().currentSecond() % 7 == 0) {
                        performer.getCommunicator().sendNormalServerMessage("The mechanism makes a strange sound and some parchment violently judders from a crevice.");
                    } else {
                        int time = performer.getCurrentAction().getTimeLeft();
                        if (counter * 10.0f > time) {
                            if (performer instanceof Player && isMechanism(mechanism.getTemplateId())) {
                                if (performer.getDeity() == null) {
                                    performer.getCommunicator().sendNormalServerMessage("You have no deity, causing the parchment to crumple and tear.");
                                    return true;
                                } else {
                                    giveItems(mechanism, source, performer, enchantScrollTemplates);
                                    //Mechanism.removeAthanorMechanism(mechanism);
                                    AthanorMechanismBehaviour.removeAthanorMechanism(mechanism);
                                    Items.destroyItem(mechanism.getWurmId());
                                }
                                return true;
                            } else {
                                performer.getCommunicator().sendNormalServerMessage("There is nowhere to place a gem in this.");
                                return true;
                            }
                        }
                    }
                } catch (Exception e) {
                    RequiemLogging.logWarning( e.getMessage() + e);
                    return true;
                }
                return false;
            }
        };
    }
}
