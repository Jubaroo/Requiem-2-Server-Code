package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.NoSuchTemplateException;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.questions.TextInputQuestion;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.util.Collections;
import java.util.List;

public class AddSubGroupAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public AddSubGroupAction() {
        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Add Subgroup",
                "adding group",
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
                if (performer instanceof Player && object != null && object.getTemplateId() == ItemList.inventoryGroup) {
                    return Collections.singletonList(actionEntry);
                }

                return null;
            }
        };
    }

    private static void renameGroup(Item group, Creature performer) {
        TextInputQuestion tiq = new TextInputQuestion(performer, "Setting name for group.", "Set the new name:", 1, group.getWurmId(), 20, false);
        tiq.setOldtext(group.getName());
        tiq.sendQuestion();
    }

    private static void addGroup(Item group, Creature performer) {
        if (group.getOwnerId() != performer.getWurmId()) {
            performer.getCommunicator().sendNormalServerMessage("You must use this on an inventory group you own.");
            return;
        }
        Item[] items = performer.getInventory().getItemsAsArray();
        int groupCount = 0;
        for (Item item : items) {
            if (item.getTemplateId() == ItemList.inventoryGroup) {
                ++groupCount;
            }
            if (groupCount == 20) break;
        }
        if (groupCount >= 20) {
            performer.getCommunicator().sendNormalServerMessage("You can only have 20 groups.");
            return;
        }
        try {
            Item newGroup = ItemFactory.createItem(ItemList.inventoryGroup, 100.0f, "");
            newGroup.setName("Group");
            group.insertItem(newGroup, true);
            renameGroup(newGroup, performer);
        } catch (NoSuchTemplateException | FailedException nst) {
            RequiemLogging.logException(nst.getMessage(), nst);
        }
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
                    if (performer instanceof Player) {
                        if (target.getTemplateId() != ItemList.inventoryGroup) {
                            performer.getCommunicator().sendNormalServerMessage("You can only use this on an inventory group.");
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        addGroup(target, performer);
                    }
                    return propagate(act, ActionPropagation.CONTINUE_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                } catch (Exception e) {
                    e.printStackTrace();
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
            }

            @Override
            public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
                return this.action(act, performer, target, action, counter);
            }
        }; // ActionPerformer
    }

}