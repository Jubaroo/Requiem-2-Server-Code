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
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.util.Collections;
import java.util.List;

public class LabelAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public LabelAction() {
        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Rename",
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
                if (performer instanceof Player && object != null && object.getTemplateId() == CustomItems.labelId) {
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
                        if (target.getTemplate().getTemplateId() != CustomItems.labelId) {
                            player.getCommunicator().sendSafeServerMessage(String.format("This is not a %s!?", orb));
                            return true;
                        }
                        if (!player.isChampion()) {
                            performer.getCommunicator().sendSafeServerMessage(String.format("Only a champion of the gods would know what to do with a %s.", orb));
                            return true;
                        }
                        if (player.getChampionPoints() >= 98) {
                            performer.getCommunicator().sendSafeServerMessage(String.format("You already have 99 champion points and would not benefit from using the %s.", orb));
                            return true;
                        }
                        if (target.getLastOwnerId() != performer.getWurmId() && target.getOwnerId() != performer.getWurmId()) {
                            performer.getCommunicator().sendNormalServerMessage(String.format("You must own the %s to harness its power.", orb));
                            return true;
                        }
                        if (counter == 1f) {
                            performer.getCurrentAction().setTimeLeft(150);
                            performer.sendActionControl("extracting energy", true, 150);
                            comm.sendNormalServerMessage(String.format("You start to draw energy from the %s.", orb));
                            Server.getInstance().broadCastAction(performer.getName() + " begins to draw energy from " + performer.getHisHerItsString() + " " + orb + ".", performer, 5);
                        } else if (counter * 10f > performer.getCurrentAction().getTimeLeft()) {
                            player.modifyChampionPoints(99);
                            Items.destroyItem(target.getWurmId());
                            player.getCommunicator().sendSafeServerMessage(String.format("The %s exhausts the last of its energy and crumbles to dust in your hand.", orb));
                            Server.getInstance().broadCastAction(performer.getName() + " extracts energy from " + performer.getHisHerItsString() + " " + orb + ".", performer, 5);
                        }
                    } else {
                        RequiemLogging.logWarning(String.format("Somehow a non-player activated a %s...", orb));
                    }
                    return true;
                } catch (Exception e) {
                    RequiemLogging.logException("[Error] in action in LabelAction", e);
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