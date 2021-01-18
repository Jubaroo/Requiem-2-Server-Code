package org.jubaroo.mods.wurm.server.actions.magicItems;

import com.wurmonline.server.Items;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.communication.discord.ChatHandler;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.tools.ItemTools;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SorceryCombineAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public SorceryCombineAction() {
        RequiemLogging.logWarning("SorceryCombineAction()");

        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Combine sorcery",
                "combining sorcery",
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
                return this.getBehavioursFor(performer, object);
            }

            // Menu without activated object
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item object) {
                if (performer instanceof Player && object != null && object.isHugeAltar()) {
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
            public boolean action(Action act, Creature performer, Item target, short action, float counter) {
                try {
                    if (performer instanceof Player) {
                        if (!target.isHugeAltar()) {
                            performer.getCommunicator().sendNormalServerMessage("You must combine at a huge altar.");
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        int fragments = 0;
                        Set<Item> inventory = performer.getInventory().getItems();
                        for (Item i : inventory) {
                            if (i.getTemplateId() == CustomItems.sorceryFragmentId) {
                                fragments++;
                            }
                        }
                        if (fragments < 2) {
                            performer.getCommunicator().sendNormalServerMessage("You must have at least two sorcery fragments to combine.");
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        if (counter == 1f) {
                            performer.getCommunicator().sendSafeServerMessage("You begin to combine sorcery fragments.");
                            ChatHandler.systemMessage((Player) performer, CustomChannel.EVENTS, String.format("%s is beginning to combine sorcery fragments at the %s!", performer.getName(), target.getName()), 52, 152, 219);
                            act.setTimeLeft(3000);
                            performer.sendActionControl("Combining sorcery", true, act.getTimeLeft());
                        } else if (counter * 10f > performer.getCurrentAction().getTimeLeft()) {
                            Set<Item> items = performer.getInventory().getItems();
                            Item first = null;
                            Item second = null;
                            for (Item i : items) {
                                if (i.getTemplateId() == CustomItems.sorceryFragmentId) {
                                    if (first == null) {
                                        first = i;
                                    } else if (second == null) {
                                        second = i;
                                    }
                                }
                            }
                            if (first == null || second == null) {
                                performer.getCommunicator().sendNormalServerMessage("Something went wrong with the combination.");
                                return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                            }
                            byte firstAux = first.getAuxData();
                            byte secondAux = (byte) (second.getAuxData() + 1);
                            if (firstAux + secondAux >= 9) {
                                byte newAux = (byte) (firstAux + secondAux - 10);
                                performer.getCommunicator().sendSafeServerMessage("A new sorcery has been created!");
                                ChatHandler.systemMessage((Player) performer, CustomChannel.EVENTS, String.format("%s has created a new sorcery!", performer.getName()), 52, 152, 219);
                                Item sorcery = ItemTools.createRandomSorcery((byte) (2 + Server.rand.nextInt(1)));
                                if (sorcery != null) {
                                    RequiemLogging.debug(String.format("Player %s created a %s with %d charges.", performer.getName(), sorcery.getName(), 3 - sorcery.getAuxData()));
                                    performer.getInventory().insertItem(sorcery, true);
                                }
                                if (newAux >= 0) {
                                    first.setAuxData(newAux);
                                    first.setName(String.format("sorcery fragment [%d/10]", first.getAuxData() + 1));
                                } else {
                                    Items.destroyItem(first.getWurmId());
                                }
                            } else {
                                performer.getCommunicator().sendSafeServerMessage("You combine fragments.");
                                ChatHandler.systemMessage((Player) performer, CustomChannel.EVENTS, String.format("%s has combined sorcery fragments, and is closer to creating a new sorcery.", performer.getName()), 52, 152, 219);
                                first.setAuxData((byte) (firstAux + secondAux));
                                first.setName(String.format("sorcery fragment [%d/10]", first.getAuxData() + 1));
                            }
                            Items.destroyItem(second.getWurmId());
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                    }
                    return propagate(act, ActionPropagation.CONTINUE_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                } catch (Exception e) {
                    e.printStackTrace();
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
            }

            @Override
            public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
                return this.action(act, performer, target, action, counter);
            }
        };
    }
}