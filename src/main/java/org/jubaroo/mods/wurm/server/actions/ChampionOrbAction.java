package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.Items;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.util.Collections;
import java.util.List;

public class ChampionOrbAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public ChampionOrbAction() {
        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Absorb champion points",
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
                return this.getBehavioursFor(performer, object);
            }

            // Menu without activated object
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item object) {
                if (performer instanceof Player && object != null && object.getTemplateId() == CustomItems.championOrbId) {
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
                    Communicator comm = performer.getCommunicator();
                    String orb = target.getName().toLowerCase();

                    if (performer instanceof Player) {
                        Player player = (Player) performer;
                        if (target.getTemplate().getTemplateId() != CustomItems.championOrbId) {
                            player.getCommunicator().sendSafeServerMessage(String.format("This is not a %s!?", orb));
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        if (!player.isChampion()) {
                            performer.getCommunicator().sendSafeServerMessage(String.format("Only a champion of the gods would know what to do with a %s.", orb));
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        if (player.getChampionPoints() > 98) {
                            performer.getCommunicator().sendSafeServerMessage(String.format("You already have 99 champion points and would not benefit from using the %s.", orb));
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        if (target.getLastOwnerId() != performer.getWurmId() && target.getOwnerId() != performer.getWurmId()) {
                            performer.getCommunicator().sendNormalServerMessage(String.format("You must own the %s to harness its power.", orb));
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        if (counter == 1f) {
                            performer.getCurrentAction().setTimeLeft(150);
                            performer.sendActionControl("extracting energy", true, performer.getCurrentAction().getTimeLeft());
                            comm.sendNormalServerMessage(String.format("You start to draw energy from the %s.", orb));
                            Server.getInstance().broadCastAction(String.format("%s begins to draw energy from %s %s.", performer.getName(), performer.getHisHerItsString(), orb), performer, 5);
                        } else if (counter * 10f > performer.getCurrentAction().getTimeLeft()) {
                            player.modifyChampionPoints(99);
                            Items.destroyItem(target.getWurmId());
                            player.getCommunicator().sendSafeServerMessage(String.format("The %s exhausts the last of its energy and crumbles to dust in your hand.", orb));
                            Server.getInstance().broadCastAction(String.format("%s extracts energy from %s %s.", performer.getName(), performer.getHisHerItsString(), orb), performer, 5);
                        }
                    } else {
                        RequiemLogging.logWarning(String.format("Somehow a non-player activated a %s...", orb));
                    }
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                } catch (Exception e) {
                    RequiemLogging.logException("[ERROR] in action in ChampionOrbAction", e);
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