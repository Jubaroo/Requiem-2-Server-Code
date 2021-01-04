package org.jubaroo.mods.wurm.server.actions.gm.labyrinth;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.Actions;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.shared.constants.StructureConstantsEnum;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.misc.maze.Maze;
import org.jubaroo.mods.wurm.server.tools.RequiemTools;
import org.jubaroo.mods.wurm.server.utils.Cooldowns;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LabyrinthPerformer implements ModAction {
    public static boolean isGMonly;
    public static int requiredGMlevel;
    public static Logger logger;
    public static short actionId;
    public static String effectName;
    public static long cooldown;

    static {
        LabyrinthPerformer.logger = Logger.getLogger(LabyrinthPerformer.class.getName());
        LabyrinthPerformer.effectName = "labyrinth";
        LabyrinthPerformer.cooldown = 0L;
        LabyrinthPerformer.isGMonly = true;
        LabyrinthPerformer.requiredGMlevel = 5;
    }

    public final ActionEntry actionEntry;

    public LabyrinthPerformer() {
        LabyrinthPerformer.logger.log(Level.INFO, "LabyrinthPerformer()");
        LabyrinthPerformer.actionId = (short) ModActions.getNextActionId();
        ModActions.registerAction(this.actionEntry = ActionEntry.createEntry(LabyrinthPerformer.actionId, "Create Labyrinth", "creating", new int[]{
                Actions.ABILITY_MAGICIAN,
                Actions.ABILITY_NORN
        }));
        try {
            ReflectionUtil.setPrivateField(actionEntry, ReflectionUtil.getField(ActionEntry.class, "maxRange"), 20);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static short getActionId() {
        return LabyrinthPerformer.actionId;
    }

    //static boolean isAllowed(final Creature performer) {
    //    return (LabyrinthPerformer.isGMonly && performer.getPower() < LabyrinthPerformer.requiredGMlevel) || (performer.getPower() < LabyrinthPerformer.requiredGMlevel);
    //}

    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            public List<ActionEntry> getBehavioursFor(final Creature performer, final Creature target) {
                return this.getBehavioursFor(performer, target.getTileX(), target.getTileY(), target.isOnSurface(), target.getCurrentTileNum());
            }

            public List<ActionEntry> getBehavioursFor(final Creature performer, final Item subject, final Creature target) {
                return this.getBehavioursFor(performer, target.getTileX(), target.getTileY(), target.isOnSurface(), target.getCurrentTileNum());
            }

            public List<ActionEntry> getBehavioursFor(final Creature performer, final int tilex, final int tiley, final boolean onSurface, final int tile, final int dir) {
                if (LabyrinthMenuProvider.isAllowed(performer)) {
                    return null;
                }
                return Collections.singletonList(LabyrinthPerformer.this.actionEntry);
            }

            public List<ActionEntry> getBehavioursFor(final Creature performer, final Item subject, final int tilex, final int tiley, final boolean onSurface, final int tile) {
                if (LabyrinthMenuProvider.isAllowed(performer)) {
                    return null;
                }
                return Collections.singletonList(LabyrinthPerformer.this.actionEntry);
            }

            public List<ActionEntry> getBehavioursFor(final Creature performer, final int tilex, final int tiley, final boolean onSurface, final int tile) {
                if (LabyrinthMenuProvider.isAllowed(performer)) {
                    return null;
                }
                return Collections.singletonList(LabyrinthPerformer.this.actionEntry);
            }
        };
    }

    public ActionPerformer getActionPerformer() {
        return new ActionPerformer() {
            public short getActionId() {
                return LabyrinthPerformer.actionId;
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
                if (LabyrinthMenuProvider.isAllowed(performer)) {
                    return true;
                }
                if (!performer.isPlayer() || performer.getVehicle() != MiscConstants.NOID) {
                    return true;
                }
                if (!performer.isOnSurface()) {
                    performer.getCommunicator().sendNormalServerMessage("You can only do this above ground.");
                    return true;
                }
                final String playerEffect = performer.getName() + LabyrinthPerformer.effectName;
                if (!RequiemTools.isPrivateTestServer() && performer.getPower() < LabyrinthPerformer.requiredGMlevel && Cooldowns.isOnCooldown(playerEffect, LabyrinthPerformer.cooldown)) {
                    performer.getCommunicator().sendNormalServerMessage(String.format("You will need to wait %s before you can do that again.", Cooldowns.timeRemaining(playerEffect, cooldown)));
                    return true;
                }
                Cooldowns.setUsed(playerEffect);
                performer.getCommunicator().sendNormalServerMessage("Labyrinth created!");
                final Maze m = new Maze(tilex, tiley, 21, StructureConstantsEnum.FENCE_STONEWALL_HIGH);//StructureConstantsEnum.HEDGE_FLOWER3_HIGH
                m.create(true, false);
                return true;
            }
        };
    }
}
