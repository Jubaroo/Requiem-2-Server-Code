package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.Items;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.misc.Misc;

import java.util.Collections;
import java.util.List;

public class ScrollOfGearBindingAction implements ModAction, ActionPerformer, BehaviourProvider {
    // TODO make into a question like scroll of town portal so they can confirm that they indeed want to bind the item
    private final ActionEntry actionEntry;
    private final String actStr = "Bind equipment";
    private final String actStrVerb = "binding";
    private final int sourceItem = CustomItems.scrollOfGearBinding.getTemplateId();

    public ScrollOfGearBindingAction() {
        actionEntry = ActionEntry.createEntry((short) ModActions.getNextActionId(), actStr, actStrVerb, new int[]{
                ActionTypesProxy.ACTION_TYPE_NOMOVE
        });
        ModActions.registerAction(actionEntry);
    }

    @Override
    public short getActionId() {
        return actionEntry.getNumber();
    }

    @Override
    public BehaviourProvider getBehaviourProvider() {
        return this;
    }

    @Override
    public ActionPerformer getActionPerformer() {
        return this;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
        if (performer instanceof Player && target != null && source.getTemplate().getTemplateId() == sourceItem && Misc.isWeaponOrArmor(target) && !target.isBulkItem())
            return Collections.singletonList(actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {
        try {
            if (source.getTopParentOrNull() != performer.getInventory()) {
                performer.getCommunicator().sendNormalServerMessage("You must have the item in your inventory to bind it to you.");
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (target.getName().startsWith("Soulbound")) {
                performer.getCommunicator().sendNormalServerMessage("That item is already bound to your soul.");
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (target.getTopParentOrNull() != performer.getInventory()) {
                performer.getCommunicator().sendNormalServerMessage("You must have the item in your inventory to bind it to you.");
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (!Misc.isWeaponOrArmor(target) && !target.isBulkItem()) {
                performer.getCommunicator().sendNormalServerMessage("You can only bind armor and weapons to your soul.");
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (counter == 1f) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You start to use the scroll on the %s.", target.getName()));
                int timeLeft = 50;
                action.setTimeLeft(timeLeft);
                performer.sendActionControl(actStrVerb, true, timeLeft);
            } else if (counter * 10f > action.getTimeLeft()) {
                target.setHasNoDecay(true);
                target.setIsIndestructible(true);
                target.setIsNoDrop(true);
                // TODO set a data field to indicate is is soulbound for use with hammer of unbinding
                target.savePermissions();
                target.setName(String.format("%s (soulbound)", target.getName()));
                Items.destroyItem(source.getWurmId());
                performer.getCommunicator().sendNormalServerMessage(String.format("The scroll crumbles to dust as you read the last word. and the %s is now bound to your soul.", target.getName()));
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            return propagate(action, ActionPropagation.CONTINUE_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        } catch (Exception e) {
            RequiemLogging.logException("[Error] in action in DisintegrationRodAction", e);
            performer.getCommunicator().sendNormalServerMessage(String.format("Action error in class (%s). Error message: %s", actStr, e.toString()));
            performer.getCommunicator().sendNormalServerMessage("Please inform a GM by opening a ticket (/support) and provide the message above. Thank you.");
            return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        }
    }

}




