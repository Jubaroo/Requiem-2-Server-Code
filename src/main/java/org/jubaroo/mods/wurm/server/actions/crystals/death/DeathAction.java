package org.jubaroo.mods.wurm.server.actions.crystals.death;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.behaviours.Methods;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.NoSuchTemplateException;
import com.wurmonline.server.zones.Zones;
import com.wurmonline.shared.constants.EffectConstants;
import com.wurmonline.shared.constants.SoundNames;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.tools.CreatureTools;
import org.jubaroo.mods.wurm.server.utils.Cooldowns;

import java.util.Collections;
import java.util.List;

import static org.gotti.wurmunlimited.modsupport.actions.ActionPropagation.*;

public class DeathAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final short actionId;
    private final ActionEntry actionEntry;

    public DeathAction() {
        // Get the action id
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Steal life", "stealing", new int[]{
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

    private boolean canUse(Creature performer, Item source, Creature creature) {
        if (performer.isPlayer() && source != null && creature != null &&
                source.getTemplateId() == CustomItems.deathCrystal.getTemplateId() &&
                source.getTopParent() == performer.getInventory().getWurmId()) {

            return CreatureTools.isOkToDestroy(creature);
        } else return false;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Creature creature) {
        if (canUse(performer, source, creature))
            return Collections.singletonList(this.actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Creature creature, short num, float counter) {
        final String playerEffect = performer.getName() + "crystalDeathAction";
        Communicator perfComm = performer.getCommunicator();
        Communicator tgtComm = creature.getCommunicator();
        final int tileX = creature.getTileX();
        final int tileY = creature.getTileY();
        int cooldown = 120; // minutes
        if (Cooldowns.isOnCooldown(playerEffect, cooldown)) {
            performer.getCommunicator().sendNormalServerMessage(String.format("You will need to wait %s before you can do that again.", Cooldowns.timeRemaining(playerEffect, cooldown)));
            return true;
        }
        if (!canUse(performer, source, creature)) {
            performer.getCommunicator().sendAlertServerMessage("Do you have the correct item activated and/or targeting the correct item/creature? Please try again or use /support to open a ticket with as much info as possible, and the staff will get to it as soon as possible.");
            return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
        }
        if (creature.isReborn()) {
            perfComm.sendNormalServerMessage("You cannot steal life from a reborn creature.");
            return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
        }
        if (creature.isUnique()) {
            perfComm.sendNormalServerMessage("You cannot steal life from a unique creature.");
            return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
        }
        if (creature.getBaseCombatRating() == performer.getSoulStrengthVal() * 1.2) {
            perfComm.sendNormalServerMessage("This creature resists the crystals power. Perhaps it is too powerful.");
            return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
        } else {
            try {
                RequiemLogging.logInfo(String.format("%s kills the %s with a %s, creature wurmId: %s, is cared for by player with wurmId: %s", performer.getName(), creature.getName(), source.getName(), creature.getWurmId(), creature.getCareTakerId()));
                perfComm.sendNormalServerMessage(String.format("%s looks surprised as the hand of Libila herself reaches from the void and quickly rips the heart out from %s body.", creature.getNameWithGenus(), creature.getHisHerItsString()));
                tgtComm.sendAlertServerMessage("You look surprised as the hand of Libila herself reaches from the void and suddenly rips the heart out of your body.");
                tgtComm.sendAlertServerMessage("You see it throb one last time.");
                Server.getInstance().broadCastAction(String.format("%s looks very surprised as the hand of Libila herself reaches from the void and suddenly rips the heart out of %s body.", creature.getNameWithGenus(), creature.getHisHerItsString()), performer, creature, 5);
                final Item heart = ItemFactory.createItem(ItemList.heart, 99f, performer.getName());
                heart.setData1(creature.getTemplate().getTemplateId());
                heart.setName(creature.getName().toLowerCase() + " heart");
                heart.setWeight(heart.getWeightGrams() * Math.max(20, creature.getSize()), true);
                heart.setButchered();
                performer.getInventory().insertItem(heart, true);
                Methods.sendSound(performer, SoundNames.CONCH);
                performer.getCommunicator().sendAddEffect(creature.getWurmId(), creature.getWurmId(), EffectConstants.EFFECT_GENERIC, creature.getPosX(), creature.getPosY(), source.getPosZ(), (byte) 0, "lightningBall01", 10, 0f);
                Server.getWeather().setRainAdd(40f);
                Server.getWeather().setCloudTarget(40f);
                Zones.flashSpell(tileX, tileY, 0, performer);
                creature.die(true, "Death crystal kill");
                Cooldowns.setUsed(playerEffect);
            } catch (NoSuchTemplateException | FailedException ex3) {
                RequiemLogging.logWarning(String.format("Error on death action: %s", ex3.getMessage()));
            }
        }
        return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
    }
}