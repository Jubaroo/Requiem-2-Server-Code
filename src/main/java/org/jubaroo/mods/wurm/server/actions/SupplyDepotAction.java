package org.jubaroo.mods.wurm.server.actions;

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
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.items.behaviours.SupplyDepots;

import java.util.Collections;
import java.util.List;

public class SupplyDepotAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public SupplyDepotAction() {
        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Claim Supply Cache",
                "claiming",
                new int[]{
                        ActionTypesProxy.ACTION_TYPE_NOMOVE
                }
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

            @Override
            public boolean action(Action act, Creature performer, Item target, short action, float counter) {
                try {
                    if (performer instanceof Player) {
                        if (target.getTemplate().getTemplateId() != CustomItems.requiemDepotId) {
                            performer.getCommunicator().sendNormalServerMessage("That is not a supply cache.");
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        if (!performer.isWithinDistanceTo(target, 5)) {
                            performer.getCommunicator().sendNormalServerMessage("You must be closer to claim the supply cache.");
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        if (!Items.exists(target)) {
                            performer.getCommunicator().sendNormalServerMessage("The supply cache has already been captured.");
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        if (counter == 1.0f) {
                            performer.getCommunicator().sendNormalServerMessage("You begin to claim the cache.");
                            Server.getInstance().broadCastAction(String.format("%s begins claiming the cache.", performer.getName()), performer, 50);
                            act.setTimeLeft(3600);
                            performer.sendActionControl("Claiming", true, act.getTimeLeft());
                            SupplyDepots.maybeBroadcastOpen(performer);
                        } else if (counter * 10f > performer.getCurrentAction().getTimeLeft()) {

                            SupplyDepots.giveCacheReward(performer);

                            performer.getCommunicator().sendSafeServerMessage("You have successfully claimed the cache!");
                            Server.getInstance().broadCastAction(String.format("%s successfully claims the cache!", performer.getName()), performer, 50);
                            Server.getInstance().broadCastAlert(String.format("%s has claimed a supply cache!", performer.getName()));
                            SupplyDepots.removeSupplyDepot(target);
                            Items.destroyItem(target.getWurmId());
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                    } else {
                        RequiemLogging.logWarning("Somehow a non-player activated a Supply Depot...");
                    }
                    return propagate(act, ActionPropagation.CONTINUE_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                } catch (Exception e) {
                    RequiemLogging.logException("Error in SupplyDepotAction", e.getCause());
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