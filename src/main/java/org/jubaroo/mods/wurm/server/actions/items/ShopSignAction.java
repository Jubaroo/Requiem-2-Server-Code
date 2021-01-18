package org.jubaroo.mods.wurm.server.actions.items;

import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.questions.ShopSignQuestion;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.actions.special.decorative.ShopSigns;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ShopSignAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final short actionId;
    private final ActionEntry actionEntry;

    public ShopSignAction() {
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Additional sign designs", "changing", new int[]{
                ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS,
                ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM})
                .range(4).build();
        // Register the action entry
        ModActions.registerAction(this.actionEntry);
    }

    @Override
    public short getActionId() {
        return this.actionId;
    }

    @Override
    public BehaviourProvider getBehaviourProvider() {
        return this;
    }

    @Override
    public ActionPerformer getActionPerformer() {
        return this;
    }

    private boolean canUse(Creature performer, Item source, Item target) {
        try {
            if (performer.isPlayer() && source != null && target != null &&
                    (target.canHavePermissions() && target.mayManage(performer)) || Objects.requireNonNull(target).lastOwner == performer.getWurmId() || target.getTopParentOrNull() == performer.getInventory()) {
                if (target.getTemplateId() == ItemList.signShop)
                    return ShopSigns.canSetAnyDesign(target);
            }
        } catch (NullPointerException e) {
            RequiemLogging.logException("Shop sign error", e);
            performer.getCommunicator().sendAlertServerMessage("Something went wrong. Try again or notify staff using /support.");
        }
        return false;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
        if (canUse(performer, source, target))
            return Collections.singletonList(this.actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {
        if (!canUse(performer, source, target)) {
            performer.getCommunicator().sendAlertServerMessage("Do you have the correct item activated and/or targeting the correct item/creature? Please try again or use /support to open a ticket with as much info as possible, and the staff will get to it as soon as possible.");
            return true;
        }
        if (target.getTemplateId() == ItemList.signShop) {
            ShopSignQuestion.send(performer, target);
        }
        return true;
    }
}
