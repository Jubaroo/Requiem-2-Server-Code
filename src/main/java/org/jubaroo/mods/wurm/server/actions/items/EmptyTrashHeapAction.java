package org.jubaroo.mods.wurm.server.actions.items;

import com.wurmonline.server.Items;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.behaviours.Actions;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

import java.util.Collections;
import java.util.List;

public class EmptyTrashHeapAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;
    static int actionTime = 100;

    public EmptyTrashHeapAction() {
        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Empty trash heap",
                "emptying",
                new int[]{
                        ActionTypesProxy.ACTION_TYPE_NOMOVE,
                        ActionTypesProxy.ACTION_TYPE_BLOCKED_ALL_BUT_OPEN}
        );
        ModActions.registerAction(actionEntry);
    }

    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            // Menu with activated object
            public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item object) {
                return this.getBehavioursFor(performer, object);
            }

            // Menu without activated object
            public List<ActionEntry> getBehavioursFor(Creature performer, Item object) {
                if (performer instanceof Player && object != null && object.getTemplateId() == ItemList.trashBin && object.getItemCount() > 0) {
                    return Collections.singletonList(actionEntry);
                }
                return null;
            }
        };
    }

    public ActionPerformer getActionPerformer() {
        return new ActionPerformer() {

            public short getActionId() {
                return actionId;
            }

            public boolean action(final Action act, final Creature performer, final Item target, final short action, final float counter) {
                try {
                    if (counter == 1.0f) {
                        performer.getCurrentAction().setTimeLeft(actionTime);
                        performer.sendActionControl(Actions.actionEntrys[actionId].getVerbString(), true, actionTime);
                    } else if (counter * 10.0f > performer.getCurrentAction().getTimeLeft()) {
                        final Item[] iarr = target.getItemsAsArray();
                        if (iarr.length > 0) {
                            for (final Item i : iarr) {
                                Items.destroyItem(i.getWurmId());
                            }
                        }
                        return true;
                    }
                } catch (Exception e) {
                    return false;
                }
                return false;
            }

            public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
                return this.action(act, performer, target, action, counter);
            }
        };
    }

}
