package org.jubaroo.mods.wurm.server.actions.items;

import com.wurmonline.server.Items;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.util.Collections;
import java.util.List;

public class WagonKitPackAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final ActionEntry actionEntry;
    private final String actStr = "Pack up wagon";
    private final String actStrVerb = "packing";
    private final int sourceItem = CustomItems.wagonKitId;

    public WagonKitPackAction() {
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
        if (performer instanceof Player && target != null && source.getTemplate().getTemplateId() == sourceItem && target.isVehicle())
            return Collections.singletonList(actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {
        try {
            int timeLeft = 300;

            if (!performer.isWithinDistanceTo(target, 8f)) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You must be closer to the %s.", target.getName()));
                return true;
            }
            if (source.getTopParentOrNull() != performer.getInventory()) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You must have the %s in your inventory.", source.getName()));
                return true;
            }
            if (!target.isVehicle() && target.getOwnerId() != performer.getWurmId() && source.getTemplate().getTemplateId() != sourceItem ) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You cannot do that without a %s activated and a wagon targeted.", source.getName()));
                return true;
            }
            if (target.isTraded()) {
                performer.getCommunicator().sendNormalServerMessage("You cannot do that while trading.");
                return true;
            }
            if (counter == 1f) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You begin to carefully disassemble and pack up the %s into a large crate.", target.getName()));
                action.setTimeLeft(timeLeft);
                performer.sendActionControl(actStrVerb, true, timeLeft);
            } else if (counter * 10f > action.getTimeLeft()) {
                // insert action here

                Items.destroyItem(target.getWurmId());
                performer.getCommunicator().sendNormalServerMessage(String.format("You successfully pack up the %s.", target.getName()));
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            performer.getCommunicator().sendNormalServerMessage(String.format("Action error in class (%s). Error message: %s", actStr, e.toString()));
            performer.getCommunicator().sendNormalServerMessage("Please inform the staff by opening a ticket (/support) and provide the message above. Thank you.");
            return true;
        }
    }

}




