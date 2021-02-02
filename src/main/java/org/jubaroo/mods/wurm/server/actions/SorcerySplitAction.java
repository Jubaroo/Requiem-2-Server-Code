package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.NoSuchTemplateException;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.tools.RandomUtils;

import java.util.Collections;
import java.util.List;

public class SorcerySplitAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public SorcerySplitAction() {
        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Split sorcery",
                "splitting",
                new int[]{0}
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
                if (performer instanceof Player && object != null && RandomUtils.isSorcery(object) && object.getAuxData() < 2) {
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
                if (performer instanceof Player) {
                    Player player = (Player) performer;
                    if (!RandomUtils.isSorcery(target)) {
                        player.getCommunicator().sendNormalServerMessage("You can only split a sorcery.");
                        return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                    }
                    if (target.getAuxData() >= 2) {
                        player.getCommunicator().sendNormalServerMessage("The sorcery must have at least two charges to split.");
                        return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                    }
                    if (target.getOwnerId() != player.getWurmId()) {
                        player.getCommunicator().sendNormalServerMessage("You must be holding the sorcery to split it.");
                        return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                    }
                    while (target.getAuxData() < 2) {
                        try {
                            Item newSorcery = ItemFactory.createItem(target.getTemplateId(), target.getCurrentQualityLevel(), null);
                            newSorcery.setAuxData((byte) 2);
                            player.getInventory().insertItem(newSorcery, true);
                            target.setAuxData((byte) (target.getAuxData() + 1));
                        } catch (FailedException | NoSuchTemplateException e) {
                            RequiemLogging.logException("[ERROR] in action in SorcerySplitAction", e);
                            break;
                        }
                    }
                } else {
                    RequiemLogging.logWarning(String.format("Somehow a non-player activated a %s...", target.getName()));
                }
                return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }

            @Override
            public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
                return this.action(act, performer, target, action, counter);
            }


        }; // ActionPerformer
    }
}