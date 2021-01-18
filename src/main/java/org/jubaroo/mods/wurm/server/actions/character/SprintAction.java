package org.jubaroo.mods.wurm.server.actions.character;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.utils.Cooldowns;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class SprintAction implements ModAction {
    private static Logger logger;
    private static short actionId;
    private static String effectName;
    private static long cooldown; // 30 minutes

    static {
        SprintAction.logger = Logger.getLogger(SprintAction.class.getName());
        SprintAction.effectName = SprintAction.class.getName();
        SprintAction.cooldown = 1800000L;
    }

    private final ActionEntry actionEntry;

    public SprintAction() {
        RequiemLogging.logInfo("SprintAction()");
        SprintAction.actionId = (short) ModActions.getNextActionId();
        ModActions.registerAction(this.actionEntry = ActionEntry.createEntry(SprintAction.actionId, "Sprint", "sprinting",
                new int[]{
                        ActionTypesProxy.ACTION_TYPE_IGNORERANGE,
                        ActionTypesProxy.ACTION_TYPE_QUICK
                }));
    }

    public static short getActionId() {
        return SprintAction.actionId;
    }

    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            public List<ActionEntry> getBehavioursFor(final Creature performer, final int tilex, final int tiley, final boolean onSurface, final int tile, final int dir) {
                return Collections.singletonList(SprintAction.this.actionEntry);
            }

            public List<ActionEntry> getBehavioursFor(final Creature performer, final Item subject, final int tilex, final int tiley, final boolean onSurface, final int tile) {
                return Collections.singletonList(SprintAction.this.actionEntry);
            }

            public List<ActionEntry> getBehavioursFor(final Creature performer, final int tilex, final int tiley, final boolean onSurface, final int tile) {
                return Collections.singletonList(SprintAction.this.actionEntry);
            }
        };
    }

    public ActionPerformer getActionPerformer() {
        return new ActionPerformer() {
            public short getActionId() {
                return SprintAction.actionId;
            }

            public boolean action(final Action act, final Creature performer, final Item source, final int tilex, final int tiley, final boolean onSurface, final int heightOffset, final int tile, final short action, final float counter) {
                this.action(act, performer, tilex, tiley, onSurface, tile, action, counter);
                return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }

            public boolean action(final Action act, final Creature performer, final int tilex, final int tiley, final boolean onSurface, final int tile, final short action, final float counter) {
                if (!performer.isPlayer() || performer.getVehicle() != MiscConstants.NOID) {
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
                final String playerEffect = performer.getName() + SprintAction.effectName;
                if (Cooldowns.isOnCooldown(playerEffect, cooldown)) {
                    performer.getCommunicator().sendNormalServerMessage(String.format("You're still exhausted. You will need to wait %s before you can do that again.", Cooldowns.timeRemaining(playerEffect, cooldown)));
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
                Cooldowns.setUsed(playerEffect);
                ((Player) performer).setFarwalkerSeconds((byte) 60);
                performer.getMovementScheme().setFarwalkerMoveMod(true);
                performer.getStatus().sendStateString();
                performer.getCommunicator().sendNormalServerMessage("Your legs tingle and you feel fantastic!");
                return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
        };
    }
}
