package org.jubaroo.mods.wurm.server.actions.items;

import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.questions.DestroyCreatureQuestion;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.util.Collections;
import java.util.List;

public class HealingFountainAction implements ModAction, ActionPerformer, BehaviourProvider {
    private static short actionId;
    private final ActionEntry actionEntry;

    public static short getActId() {
        return actionId;
    }

    public HealingFountainAction() {
        // Get the action id
        actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(actionId, "Heal your wounds", "healing", new int[] {ActionTypesProxy.ACTION_TYPE_NOMOVE}).range(4).build();
        // Register the action entry
        ModActions.registerAction(this.actionEntry);
    }

    @Override
    public short getActionId() {
        return actionId;
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
    public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
        if (performer instanceof Player && target != null && target.getTemplate().getTemplateId() == CustomItems.healingFountainId)
            return Collections.singletonList(this.actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item target, short num, float counter) {
        try {
            final DestroyCreatureQuestion aq = new DestroyCreatureQuestion(performer, "Destroy that mean Beast", "Ok tell me the wurmId and consider it gone.\n\n", target.getWurmId());
            aq.sendQuestion();

            return propagate(action, ActionPropagation.CONTINUE_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        } catch (Exception e) {
            e.printStackTrace();
            performer.getCommunicator().sendNormalServerMessage(String.format("Action error in class (%s). Error message: " +
                    "%s", HealingFountainAction.class.getName(), e.toString()));
            performer.getCommunicator().sendNormalServerMessage("Please inform a GM by opening a ticket (/support) and provide the message above. Thank you.");
            return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        }
    }

}




