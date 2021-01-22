package org.jubaroo.mods.wurm.server.actions.labyrinth;

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
import org.jubaroo.mods.wurm.server.misc.labyrinth.Maze;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LabyrinthRemoveAction implements ModAction {
    private static Logger logger;
    private static short actionId;

    static {
        LabyrinthRemoveAction.logger = Logger.getLogger(LabyrinthRemoveAction.class.getName());
    }

    private final ActionEntry actionEntry;

    public LabyrinthRemoveAction() {
        LabyrinthRemoveAction.logger.log(Level.INFO, "SprintAction()");
        LabyrinthRemoveAction.actionId = (short) ModActions.getNextActionId();
        ModActions.registerAction(this.actionEntry = ActionEntry.createEntry(LabyrinthRemoveAction.actionId, "Remove GM Labyrinth", "removing", new int[]{
                ActionTypesProxy.ACTION_TYPE_IGNORERANGE,
                ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM
        }));
    }

    public static short getActionId() {
        return LabyrinthRemoveAction.actionId;
    }

    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            public List<ActionEntry> getBehavioursFor(final Creature performer, final int tilex, final int tiley, final boolean onSurface, final int tile, final int dir) {
                if (performer.getPower() >= LabyrinthAction.requiredGMlevel) {
                    return Collections.singletonList(LabyrinthRemoveAction.this.actionEntry);
                }
                return null;
            }

            public List<ActionEntry> getBehavioursFor(final Creature performer, final Item subject, final int tilex, final int tiley, final boolean onSurface, final int tile) {
                if (performer.getPower() >= LabyrinthAction.requiredGMlevel) {
                    return Collections.singletonList(LabyrinthRemoveAction.this.actionEntry);
                }
                return null;
            }

            public List<ActionEntry> getBehavioursFor(final Creature performer, final int tilex, final int tiley, final boolean onSurface, final int tile) {
                if (performer.getPower() >= LabyrinthAction.requiredGMlevel) {
                    return Collections.singletonList(LabyrinthRemoveAction.this.actionEntry);
                }
                return null;
            }
        };
    }

    public ActionPerformer getActionPerformer() {
        return new ActionPerformer() {
            public short getActionId() {
                return LabyrinthRemoveAction.actionId;
            }

            public boolean action(final Action act, final Creature performer, final Item source, final int tilex, final int tiley, final boolean onSurface, final int heightOffset, final int tile, final short action, final float counter) {
                this.action(act, performer, tilex, tiley, onSurface, tile, action, counter);
                return true;
            }

            public boolean action(final Action act, final Creature performer, final int tilex, final int tiley, final boolean onSurface, final int tile, final short action, final float counter) {
                if (performer.getPower() < LabyrinthAction.requiredGMlevel) {
                    return true;
                }
                performer.getCommunicator().sendNormalServerMessage("Removed the Labyrinth!");
                final Maze maze = new Maze(tilex, tiley, 60, StructureConstantsEnum.HEDGE_FLOWER3_HIGH);
                maze.clear();
                return true;
            }
        };
    }
}
