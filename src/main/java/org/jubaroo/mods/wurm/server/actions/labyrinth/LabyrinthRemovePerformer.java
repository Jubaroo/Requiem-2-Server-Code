package org.jubaroo.mods.wurm.server.actions.labyrinth;

import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.shared.constants.StructureConstantsEnum;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.misc.labyrinth.Maze;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LabyrinthRemovePerformer implements ModAction {
    private static Logger logger;
    private static short actionId;

    static {
        LabyrinthRemovePerformer.logger = Logger.getLogger(LabyrinthRemovePerformer.class.getName());
    }

    public final ActionEntry actionEntry;

    public LabyrinthRemovePerformer() {
        LabyrinthRemovePerformer.logger.log(Level.INFO, "LabyrinthRemovePerformer()");
        LabyrinthRemovePerformer.actionId = (short) ModActions.getNextActionId();
        ModActions.registerAction(this.actionEntry = ActionEntry.createEntry(LabyrinthRemovePerformer.actionId, "Remove Labyrinth", "removing", new int[]{23, 29}));
        try {
            ReflectionUtil.setPrivateField(actionEntry, ReflectionUtil.getField(ActionEntry.class, "maxRange"), 20);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static short getActionId() {
        return LabyrinthRemovePerformer.actionId;
    }

    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            public List<ActionEntry> getBehavioursFor(final Creature performer, final int tilex, final int tiley, final boolean onSurface, final int tile, final int dir) {
                if (performer.getPower() >= LabyrinthPerformer.requiredGMlevel) {
                    return Collections.singletonList(LabyrinthRemovePerformer.this.actionEntry);
                }
                return null;
            }

            public List<ActionEntry> getBehavioursFor(final Creature performer, final Item subject, final int tilex, final int tiley, final boolean onSurface, final int tile) {
                if (performer.getPower() >= LabyrinthPerformer.requiredGMlevel) {
                    return Collections.singletonList(LabyrinthRemovePerformer.this.actionEntry);
                }
                return null;
            }

            public List<ActionEntry> getBehavioursFor(final Creature performer, final int tilex, final int tiley, final boolean onSurface, final int tile) {
                if (performer.getPower() >= LabyrinthPerformer.requiredGMlevel) {
                    return Collections.singletonList(LabyrinthRemovePerformer.this.actionEntry);
                }
                return null;
            }
        };
    }

    public ActionPerformer getActionPerformer() {
        return new ActionPerformer() {
            public short getActionId() {
                return LabyrinthRemovePerformer.actionId;
            }

            public boolean action(final Action act, final Creature performer, final Item source, final int tilex, final int tiley, final boolean onSurface, final int heightOffset, final int tile, final short action, final float counter) {
                this.action(act, performer, tilex, tiley, onSurface, tile, action, counter);
                return true;
            }

            public boolean action(final Action act, final Creature performer, final int tilex, final int tiley, final boolean onSurface, final int tile, final short action, final float counter) {
                if (performer.getPower() < LabyrinthPerformer.requiredGMlevel) {
                    return true;
                }
                performer.getCommunicator().sendNormalServerMessage("Remove Labyrinth!");
                final Maze m = new Maze(tilex, tiley, 60, StructureConstantsEnum.FENCE_MAGIC_STONE);
                m.clear();
                return true;
            }
        };
    }
}
