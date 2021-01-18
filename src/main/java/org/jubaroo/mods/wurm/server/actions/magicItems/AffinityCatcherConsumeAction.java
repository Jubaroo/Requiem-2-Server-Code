package org.jubaroo.mods.wurm.server.actions.magicItems;

import com.wurmonline.server.Items;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.skills.Affinities;
import com.wurmonline.server.skills.Affinity;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.util.Collections;
import java.util.List;

public class AffinityCatcherConsumeAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public AffinityCatcherConsumeAction() {
        RequiemLogging.logWarning("AffinityCatcherConsumeAction()");

        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Gain affinity",
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
                if (performer instanceof Player && object != null && object.getTemplateId() == CustomItems.affinityCatcherId && object.getData() > 0) {
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
                    if (target.getTemplate().getTemplateId() != CustomItems.affinityCatcherId) {
                        player.getCommunicator().sendSafeServerMessage("You must use a captured affinity.");
                        return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                    }
                    if (target.getData() <= 0) {
                        player.getCommunicator().sendSafeServerMessage("The catcher needs to have an affinity captured before being consumed.");
                        return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                    }
                    int skillNum = (int) target.getData();
                    Affinity[] affs = Affinities.getAffinities(performer.getWurmId());
                    for (Affinity affinity : affs) {
                        if (affinity.getSkillNumber() != skillNum) continue;
                        if (affinity.getNumber() >= 5) {
                            player.getCommunicator().sendSafeServerMessage("You already have the maximum amount of affinities for that skill.");
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        Affinities.setAffinity(player.getWurmId(), skillNum, affinity.getNumber() + 1, false);
                        player.getCommunicator().sendSafeServerMessage("Your affinity grows stronger.");
                        Items.destroyItem(target.getWurmId());
                        return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                    }
                    // Has no affinity in this, so should give them one.
                    Affinities.setAffinity(player.getWurmId(), skillNum, 1, false);
                    player.getCommunicator().sendSafeServerMessage("You obtain a new affinity.");
                    Items.destroyItem(target.getWurmId());
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                } else {
                    RequiemLogging.debug(String.format("Somehow a non-player activated action ID %d...", actionEntry.getNumber()));
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