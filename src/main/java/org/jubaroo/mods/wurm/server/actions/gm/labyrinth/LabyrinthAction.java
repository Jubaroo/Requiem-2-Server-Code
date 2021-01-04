package org.jubaroo.mods.wurm.server.actions.gm.labyrinth;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.shared.constants.StructureConstantsEnum;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.misc.maze.Maze;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LabyrinthAction implements ModAction {
    static int requiredGMlevel;
    private static boolean isGMonly;
    private static Logger logger;
    private static short actionId;
    private static String effectName;
    private static long cooldown;

    static {
        LabyrinthAction.logger = Logger.getLogger(LabyrinthAction.class.getName());
        LabyrinthAction.effectName = "labyrinth";
        LabyrinthAction.cooldown = 0L;
        LabyrinthAction.isGMonly = true;
        LabyrinthAction.requiredGMlevel = 5;
    }

    private final ActionEntry actionEntry;

    public LabyrinthAction() {
        LabyrinthAction.logger.log(Level.INFO, "LabyrinthAction()");
        LabyrinthAction.actionId = (short) ModActions.getNextActionId();
        ModActions.registerAction(this.actionEntry = ActionEntry.createEntry(LabyrinthAction.actionId,
                "GM Labyrinth", "creating", new int[]{
                        ActionTypesProxy.ACTION_TYPE_IGNORERANGE,
                        ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM
                }));
    }

    public static short getActionId() {
        return LabyrinthAction.actionId;
    }

    private boolean isAllowed(final Creature performer) {
        return (LabyrinthAction.isGMonly && performer.getPower() < LabyrinthAction.requiredGMlevel) || (performer.getPower() < LabyrinthAction.requiredGMlevel);
    }

    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            public List<ActionEntry> getBehavioursFor(final Creature performer, final Creature target) {
                return this.getBehavioursFor(performer, target.getTileX(), target.getTileY(), target.isOnSurface(), target.getCurrentTileNum());
            }

            public List<ActionEntry> getBehavioursFor(final Creature performer, final Item subject, final Creature target) {
                return this.getBehavioursFor(performer, target.getTileX(), target.getTileY(), target.isOnSurface(), target.getCurrentTileNum());
            }

            public List<ActionEntry> getBehavioursFor(final Creature performer, final int tilex, final int tiley, final boolean onSurface, final int tile, final int dir) {
                if (LabyrinthAction.this.isAllowed(performer)) {
                    return null;
                }
                return Collections.singletonList(LabyrinthAction.this.actionEntry);
            }

            public List<ActionEntry> getBehavioursFor(final Creature performer, final Item subject, final int tilex, final int tiley, final boolean onSurface, final int tile) {
                if (LabyrinthAction.this.isAllowed(performer)) {
                    return null;
                }
                return Collections.singletonList(LabyrinthAction.this.actionEntry);
            }

            public List<ActionEntry> getBehavioursFor(final Creature performer, final int tilex, final int tiley, final boolean onSurface, final int tile) {
                if (LabyrinthAction.this.isAllowed(performer)) {
                    return null;
                }
                return Collections.singletonList(LabyrinthAction.this.actionEntry);
            }
        };
    }

    public ActionPerformer getActionPerformer() {
        return new ActionPerformer() {
            public short getActionId() {
                return LabyrinthAction.actionId;
            }

            public boolean action(final Action act, final Creature performer, final Creature target, final short action, final float counter) {
                if (!performer.isOnSurface() || !target.isOnSurface()) {
                    performer.getCommunicator().sendNormalServerMessage("You can only do this above ground.");
                    return true;
                }
                return this.action(act, performer, target.getTileX(), target.getTileY(), target.isOnSurface(), target.getCurrentTileNum(), action, counter);
            }

            public boolean action(final Action act, final Creature performer, final Item source, final Creature target, final short action, final float counter) {
                if (!performer.isOnSurface() || !target.isOnSurface()) {
                    performer.getCommunicator().sendNormalServerMessage("You can only do this above ground.");
                    return true;
                }
                return this.action(act, performer, target.getTileX(), target.getTileY(), target.isOnSurface(), target.getCurrentTileNum(), action, counter);
            }

            public boolean action(final Action act, final Creature performer, final Item source, final int tilex, final int tiley, final boolean onSurface, final int heightOffset, final int tile, final short action, final float counter) {
                this.action(act, performer, tilex, tiley, onSurface, tile, action, counter);
                return true;
            }

            public boolean action(final Action act, final Creature performer, final int tilex, final int tiley, final boolean onSurface, final int tile, final short action, final float counter) {
                if (LabyrinthAction.this.isAllowed(performer)) {
                    return true;
                }
                if (!performer.isPlayer() || performer.getVehicle() != MiscConstants.NOID) {
                    return true;
                }
                if (!performer.isOnSurface()) {
                    performer.getCommunicator().sendNormalServerMessage("You can only do this above ground.");
                    return true;
                }
                //final String playerEffect = performer.getName() + LabyrinthAction.effectName;
                //if (!RequiemUtilities.isPrivateTestServer() && performer.getPower() < LabyrinthAction.requiredGMlevel && Cooldowns.isOnCooldown(playerEffect, LabyrinthAction.cooldown)) {
                //    performer.getCommunicator().sendNormalServerMessage(String.format("You will need to wait %s before you can do that again.", Cooldowns.timeRemaining(playerEffect, cooldown)));
                //    return true;
                //}
                //Cooldowns.setUsed(playerEffect);
                performer.getCommunicator().sendNormalServerMessage("Labyrinth created!");
                final Maze maze = new Maze(tilex, tiley, 60, StructureConstantsEnum.HEDGE_FLOWER3_HIGH);
                maze.create(true, true);
                return true;
            }
        };
    }
}
