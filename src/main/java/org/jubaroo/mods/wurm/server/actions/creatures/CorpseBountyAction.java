package org.jubaroo.mods.wurm.server.actions.creatures;

import com.wurmonline.server.Items;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.behaviours.MethodsItems;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.economy.Economy;
import com.wurmonline.server.items.Item;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.server.Constants;

import java.util.Collections;
import java.util.List;

public class CorpseBountyAction implements ModAction, BehaviourProvider, ActionPerformer {
    private final short actionId;
    private final ActionEntry actionEntry;

    public CorpseBountyAction() {
        // Get the action id
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Redeem Bounty", "Redeeming", new int[]{
                ActionTypesProxy.ACTION_TYPE_NOMOVE,
                ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS,
                ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM}
        ).range(10).build();
        // Register the action entry
        ModActions.registerAction(this.actionEntry);
    }

    @Override
    public short getActionId() {
        return this.actionId;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature actor, Item source, Item target) {

        if (target == null) {
            return null;
        }

        if (Constants.cashPerCorpse == 0) {
            return null;
        }

        if (!isRedeemCorpse(actor, target)) {
            return null;
        }

        return Collections.singletonList(this.actionEntry);
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature actor, Item target) {
        return getBehavioursFor(actor, null, target);
    }

    @Override
    public boolean action(Action action, Creature actor, Item target, short num, float counter) {
        return action(action, actor, null, target, num, counter);
    }

    public boolean isRedeemCorpse(Creature actor, Item item) {
        if (!item.isCorpse()) {
            return false;
        }
        return MethodsItems.isLootableBy(actor, item);
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {

        Communicator comm = performer.getCommunicator();

        if (!isRedeemCorpse(performer, target)) {
            comm.sendNormalServerMessage("This is not a redeemable corpse");
            return true;
        }
        Items.destroyItem(target.getWurmId());

        Item[] coins = Economy.getEconomy().getCoinsFor(Constants.cashPerCorpse);
        for (Item coin : coins) {
            performer.getInventory().insertItem(coin, true);
        }
        //performer.addMoney(Constants.cashPerCorpse);
        comm.sendNormalServerMessage(String.format("Bounty Awarded: %d iron", Constants.cashPerCorpse));
        return true;
    }
}
