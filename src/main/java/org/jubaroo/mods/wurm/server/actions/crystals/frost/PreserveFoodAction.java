package org.jubaroo.mods.wurm.server.actions.crystals.frost;

import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.utils.Cooldowns;

import java.util.Collections;
import java.util.List;

import static org.gotti.wurmunlimited.modsupport.actions.ActionPropagation.*;

public class PreserveFoodAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final short actionId;
    private final ActionEntry actionEntry;

    public PreserveFoodAction() {
        // Get the action id
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Preserve food", "preserving", new int[]{
                ActionTypesProxy.ACTION_TYPE_NOMOVE,
                ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS,
                ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM
        }).range(8).build();
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

    public boolean canUse(Creature performer, Item source, Item target) {
        if (performer.isPlayer() && source != null && target != null &&
                source.getTemplateId() == CustomItems.frostCrystal.getTemplateId() &&
                source.getTopParent() == performer.getInventory().getWurmId() &&
                (target.getTopParent() == performer.getInventory().getWurmId() || target.getLastOwnerId() == performer.getWurmId()/*|| performer.getPower() > 0*/)) {
            return target.isFood();
        } else return false;
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
        try {
            final String playerEffect = performer.getName() + "crystalPreserveFoodAction";
            int cooldown = 5; // minutes
            if (Cooldowns.isOnCooldown(playerEffect, cooldown)) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You will need to wait %s before you can do that again.", Cooldowns.timeRemaining(playerEffect, cooldown)));
                return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (!canUse(performer, source, target)) {
                performer.getCommunicator().sendAlertServerMessage("Do you have the correct item activated and/or targeting the correct item/creature? Please try again or use /support to open a ticket with as much info as possible, and the staff will get to it as soon as possible.");
                return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (target.isFood()) {
                target.setHasNoDecay(true);
                target.savePermissions();
                performer.getCommunicator().sendNormalServerMessage(String.format("You touch the %s with the %s and it freezes it in time stopping the decay!", target.getName(), source.getName()));
            }
            return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
        } catch (Exception e) {
            RequiemLogging.logWarning(e.getMessage() + e);
            return true;
        }

    }
}