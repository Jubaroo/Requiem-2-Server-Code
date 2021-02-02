package org.jubaroo.mods.wurm.server.actions.crystals.death;

import com.wurmonline.server.behaviours.*;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.CreaturesProxy;
import com.wurmonline.server.items.Item;
import com.wurmonline.shared.constants.EffectConstants;
import com.wurmonline.shared.constants.SoundNames;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.misc.MiscChanges;
import org.jubaroo.mods.wurm.server.utils.Cooldowns;

import java.util.Collections;
import java.util.List;

public class CrystalAbortAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final ActionEntry actionEntry;

    public CrystalAbortAction() {
        actionEntry = ActionEntry.createEntry((short) ModActions.getNextActionId(), "Abort pregnancy", "aborting", new int[]{
                ActionTypesProxy.ACTION_TYPE_NOMOVE,
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

    private boolean canUse(Creature performer, Item source, Creature creature) {
        if (performer.isPlayer() && source != null && creature != null &&
                source.getTemplateId() == CustomItems.deathCrystal.getTemplateId() &&
                source.getTopParent() == performer.getInventory().getWurmId()) {

            return creature.isPregnant();
        } else return false;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Creature creature) {
        if (canUse(performer, source, creature))
            return Collections.singletonList(actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Creature creature, short num, float counter) {
        try {
            final String playerEffect = String.format("%scrystalAbortAction", performer.getName());
            int cooldown = 60; // minutes
            if (Cooldowns.isOnCooldown(playerEffect, cooldown)) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You will need to wait %s before you can do that again.", Cooldowns.timeRemaining(playerEffect, cooldown)));
                return true;
            }
            if (!canUse(performer, source, creature)) {
                performer.getCommunicator().sendAlertServerMessage("Do you have the correct item activated and/or targeting the correct item/creature? Please try again or use /support to open a ticket with as much info as possible, and the staff will get to it as soon as possible.");
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (performer.getVillageId() <= 0 || !creature.isBranded() || !creature.isBrandedBy(performer.getVillageId())) {
                performer.getCommunicator().sendNormalServerMessage(String.format("%s is not branded by your settlement.", creature.getName()));
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (canUse(performer, source, creature)) {
                if (counter == 1f) {
                    performer.getCommunicator().sendNormalServerMessage(String.format("You touch the crystal to the %s and the fetus begins to glow within the creature.", creature.getName()));
                    performer.getCurrentAction().setTimeLeft(70);
                    performer.sendActionControl("Aborting offspring", true, 70);
                    performer.getCommunicator().sendAddEffect(creature.getWurmId(), creature.getWurmId(), EffectConstants.EFFECT_GENERIC, creature.getPosX(), creature.getPosY(), 0, (byte) 0, "traitor", 7, 0f);
                } else {
                    if (counter * 10f > action.getTimeLeft()) {
                        CreaturesProxy.deleteOffspringSettings(creature.getWurmId());
                        Methods.sendSound(performer, SoundNames.CONCH);
                        MiscChanges.actionNotify(performer, String.format("You hear Libila whispering as the glowing fetus disappears and the %s looks sad as it somehow knows what happened.", creature.getName()), String.format("%%NAME touches a strange crystal to the %s and the pregnancy is aborted.", creature.getName()), String.format("A shadowy form touches a strange crystal to the %s and the pregnancy is aborted.", creature.getName()));
                        Cooldowns.setUsed(playerEffect);
                        return true;
                    }
                }
            }
            return false;
        } catch (NoSuchActionException e) {
            RequiemLogging.logException("[Error] in action in CrystalAbortAction", e);
        }
        return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
    }
}



