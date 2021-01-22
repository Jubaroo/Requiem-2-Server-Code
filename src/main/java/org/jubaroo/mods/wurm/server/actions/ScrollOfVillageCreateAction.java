package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.Items;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.villages.Village;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.tools.RandomUtils;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;

public class ScrollOfVillageCreateAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final ActionEntry actionEntry;
    private final String actStrVerb = "reading";
    private final int item = CustomItems.scrollOfVillageCreation.getTemplateId();
    private static final NumberFormat nf = NumberFormat.getInstance();

    public ScrollOfVillageCreateAction() {
        String actStr = "Read scroll";
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
    public List<ActionEntry> getBehavioursFor(final Creature performer, final Item source, final Item target) {
        if (performer instanceof Player && target != null && source != null && (target.getTemplate().getTemplateId() == item && !target.isTraded()))
            return Collections.singletonList(actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {
        try {
            final Village v = performer.getCitizenVillage();

            if (target.getTopParentOrNull() != performer.getInventory()) {
                performer.getCommunicator().sendNormalServerMessage("You must have the scroll in your inventory to use it.");
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (target.getTemplate().getTemplateId() != item) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You cannot use a %s without a %s!", target.getName(), target.getName()));
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (counter == 1f) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You start to use the scroll on the %s.", target.getName()));
                int timeLeft = 100;
                action.setTimeLeft(timeLeft);
                performer.sendActionControl(actStrVerb, true, timeLeft);
            } else if (counter * 10f > action.getTimeLeft()) {
                float bonus = v.getFaithCreateValue() + RandomUtils.getRandomIntegerInRange(1, 10);
                v.setFaithCreate(bonus);
                Items.destroyItem(source.getWurmId());
                performer.getCommunicator().sendNormalServerMessage("The scroll crumbles to dust as you read the last word. You have added a faith bonus to  your settlement: Enchanting faith bonus is now (" + nf.format(v.getFaithCreateValue()) + "): " + nf.format(v.getFaithCreateBonus()) + "%, Rarity window: " + (int) Math.min(10.0f, v.getFaithCreateValue()) + " bonus seconds.");
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            return propagate(action, ActionPropagation.CONTINUE_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        } catch (Exception e) {
            e.printStackTrace();
            performer.getCommunicator().sendNormalServerMessage(String.format("Action error in class (%s). Error message: %s", ScrollOfVillageCreateAction.class.getName(), e.toString()));
            performer.getCommunicator().sendNormalServerMessage("Please inform a GM by opening a ticket (/support) and provide the message above. Thank you.");
            return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        }
    }

}




