package org.jubaroo.mods.wurm.server.actions.items;

import com.wurmonline.server.Items;
import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.economy.Change;
import com.wurmonline.server.economy.Economy;
import com.wurmonline.server.economy.MonetaryConstants;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.villages.Village;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.utils.Cooldowns;

import java.util.Collections;
import java.util.List;

public class GuardTokenAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final short actionId;
    private final ActionEntry actionEntry;

    public GuardTokenAction() {
        // Get the action id
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Add guard upkeep", "adding upkeep", new int[]{ActionTypesProxy.ACTION_TYPE_NOMOVE}).range(8).build();
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

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
        if (performer instanceof Player && target != null && source.getTemplate().getTemplateId() == CustomItems.guardTokenId && target.getTemplate().getTemplateId() == ItemList.villageToken)
            return Collections.singletonList(this.actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {
        try {
            final long money = MonetaryConstants.COIN_SILVER * 20;
            final Change newch = Economy.getEconomy().getChangeFor(money);
            final Village village = performer.citizenVillage;
            int timeLeft = 50;
            final String playerEffect = String.format("%s%s", performer.getName(), GuardTokenAction.class.getName());
            int cooldown = (int) TimeConstants.WEEK_MILLIS;

            if (source.getTopParentOrNull() != performer.getInventory()) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You must have the %s in your inventory.", source.getName()));
                return true;
            }
            if (target.getTemplate().getTemplateId() != ItemList.villageToken || source.getTemplate().getTemplateId() != CustomItems.guardTokenId) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You cannot do that without a %s activated and a %s targeted.", source.getName(), target.getName()));
                return true;
            }
            if (target.isTraded()) {
                performer.getCommunicator().sendNormalServerMessage("You cannot do that while trading.");
                return true;
            }
            if (Cooldowns.isOnCooldown(playerEffect, cooldown) && performer.getPower() == 0) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You will need to wait %s before you can do that again.", Cooldowns.timeRemaining(playerEffect, cooldown)));
                return true;
            }
            if (counter == 1f) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You begin to place the token on the %s.", target.getName()));
                action.setTimeLeft(timeLeft);
                performer.sendActionControl("placing token", true, timeLeft);
            } else if (counter * 10f > action.getTimeLeft()) {
                if (village.plan != null) {
                    village.plan.addMoney(money);
                    village.plan.addPayment(performer.getName(), performer.getWurmId(), money);
                }
                Items.destroyItem(source.getWurmId());
                Cooldowns.setUsed(playerEffect);
                performer.getCommunicator().sendNormalServerMessage(String.format("You pay %s to the upkeep fund of %s.", newch.getChangeString(), village.getName()));
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            performer.getCommunicator().sendNormalServerMessage(String.format("Action error in class (%s). Error message: %s", GuardTokenAction.class.getName(), e.toString()));
            performer.getCommunicator().sendNormalServerMessage("Please inform a GM by opening a ticket (/support) and provide the message above. Thank you.");
            return true;
        }
    }

}




