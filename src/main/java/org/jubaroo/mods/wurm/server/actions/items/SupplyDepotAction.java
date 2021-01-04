package org.jubaroo.mods.wurm.server.actions.items;

import com.wurmonline.server.Items;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.behaviours.CreatureBehaviour;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.items.behaviours.SupplyDepotBehaviour;
import org.jubaroo.mods.wurm.server.tools.CreatureTools;

import java.util.Collections;
import java.util.List;

public class SupplyDepotAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public SupplyDepotAction() {
        // Get the action id
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Capture depot", "capturing", new int[]{ActionTypesProxy.ACTION_TYPE_NOMOVE}).range(8).build();
        // Register the action entry
        ModActions.registerAction(this.actionEntry);
    }

    @Override
    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            // Menu with activated object
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item object) {
                return this.getBehavioursFor(performer, object);
            }
            // Menu without activated object
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item object) {
                if (performer instanceof Player && object != null && object.getTemplateId() == CustomItems.requiemDepotId) {
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

            // Without activated object
            @Override
            public boolean action(Action act, Creature performer, Item item, short action, float counter) {
                try {
                    Communicator comm = performer.getCommunicator();
                    if (performer instanceof Player) {
                        if (item.getTemplate().getTemplateId() != CustomItems.requiemDepotId) {
                            comm.sendNormalServerMessage(String.format("That is not a %s.", item.getName()));
                            return true;
                        }
                        if (!Items.exists(item)) {
                            comm.sendNormalServerMessage("The supply depot has already been captured.");
                            return true;
                        }
                        if (performer.getFightingSkill().getKnowledge() < 25f) {
                            comm.sendNormalServerMessage("You must have at least 25 fighting skill to capture a depot.");
                            return true;
                        }
                        if (counter == 1f) {
                            comm.sendNormalServerMessage("You begin to capture the depot.");
                            Server.getInstance().broadCastAction(String.format("%s begins capturing the depot.", performer.getName()), performer, 50);
                            if (performer.getPower() >= MiscConstants.POWER_HIGH_GOD) {
                                act.setTimeLeft(30);
                            } else {
                                act.setTimeLeft(2400);
                            }
                            performer.sendActionControl("Capturing", true, act.getTimeLeft());
                            SupplyDepotBehaviour.maybeBroadcastOpen(performer);
                            int newCreature = RequiemUtilities.getRandArrayInt(CreatureTools.randomDepotCreature);
                            if (item.getAuxData() == 0) {
                                for (int i = 0; i < (int) RequiemUtilities.generateRandomDouble(3, 6); i++) {
                                    Creature creature = Creature.doNew(newCreature, (byte) RequiemUtilities.getRandArrayInt(CreatureTools.randomCreatureType), item.getPosX() - 5f + Server.rand.nextFloat() * 10, item.getPosY() - 5f + Server.rand.nextFloat() * 10, Server.rand.nextFloat() * 360f, performer.getLayer(), "", Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE);
                                    final boolean done = performer.getCombatHandler().attack(creature, Server.getCombatCounter(), false, counter, act);
                                    CreatureBehaviour.setOpponent(creature, performer, done, act);
                                    //creature.setOpponent(performer);
                                }
                                item.setAuxData((byte) 1);
                                comm.sendAlertServerMessage("Some aggressive creatures are drawn to the depot beam and begin attacking you!");
                            }
                        } else if (counter * 10f > performer.getCurrentAction().getTimeLeft()) {
                            Item inv = performer.getInventory();
                            inv.insertItem(ItemFactory.createItem(CustomItems.requiemDepotCacheId, (float) RequiemUtilities.generateRandomDouble(99, 99.9), ""), true);
                            inv.insertItem(ItemFactory.createItem(CustomItems.sorceryFragmentId, (float) RequiemUtilities.generateRandomDouble(99, 99.9), ""), true);
                            //ItemTool.lumps.setWeight(20000, true);
                            //inv.insertItem(ItemFactory.createItem(RequiemUtilities.getRandArrayInt(ItemTool.lumpTemplates), (float) RequiemUtilities.generateRandomDouble(90, 99), ""), true);
                            comm.sendSafeServerMessage("You have successfully captured the depot!");
                            Server.getInstance().broadCastAction(String.format("%s successfully captures the depot!", performer.getName()), performer, 50);
                            SupplyDepotBehaviour.broadcastCapture(performer);
                            SupplyDepotBehaviour.removeSupplyDepot(item);
                            Items.destroyItem(item.getWurmId());
                            SupplyDepotBehaviour.addPlayerStatsDepot(performer.getName());
                            return true;
                        }
                    } else {
                        RequiemLogging.debug("Somehow a non-player activated a Supply Depot...");
                    }
                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                    return true;
                }
            }

            @Override
            public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
                return this.action(act, performer, target, action, counter);
            }
        };
    }
}