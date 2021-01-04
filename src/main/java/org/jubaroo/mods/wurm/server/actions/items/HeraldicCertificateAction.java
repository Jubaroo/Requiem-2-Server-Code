package org.jubaroo.mods.wurm.server.actions.items;

import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.questions.HeraldicQuestion;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.actions.special.decorative.DecorativeKingdoms;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.util.Collections;
import java.util.List;

public class HeraldicCertificateAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final ActionEntry actionEntry;

    public HeraldicCertificateAction() {
        actionEntry = ActionEntry.createEntry((short) ModActions.getNextActionId(), "Redeem", "redeeming", new int[]{
                ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS,
                ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM
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

    public boolean canUse(Creature performer, Item source, Item target) {
        if (performer.isPlayer() && source != null && target != null &&
                source.getTopParent() == performer.getInventory().getWurmId() &&
                ((target.canHavePermissions() && target.mayManage(performer)) || target.lastOwner == performer.getWurmId() || target.getTopParentOrNull() == performer.getInventory())) {
            if (source.getTemplateId() == CustomItems.heraldicCertificateId)
                return DecorativeKingdoms.canSetAnyKingdom(target);
        }
        return false;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
        if (canUse(performer, source, target))
            return Collections.singletonList(actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {
        if (!canUse(performer, source, target)) {
            performer.getCommunicator().sendAlertServerMessage("Do you have the correct item activated and/or targeting the correct item/creature? Please try again or use /support to open a ticket with as much info as possible, and the staff will get to it as soon as possible.");
            return true;
        }

        if (source.getTemplateId() == CustomItems.heraldicCertificateId) {
            HeraldicQuestion.send(performer, source, target);
        }

        return true;
    }
}
