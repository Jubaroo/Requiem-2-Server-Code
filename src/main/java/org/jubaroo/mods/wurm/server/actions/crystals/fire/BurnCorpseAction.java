package org.jubaroo.mods.wurm.server.actions.crystals.fire;

import com.wurmonline.server.Items;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.behaviours.MethodsItems;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.economy.Economy;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.shared.constants.EffectConstants;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.util.Collections;
import java.util.List;

public class BurnCorpseAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final short actionId;
    private final ActionEntry actionEntry;

    public BurnCorpseAction() {
        // Get the action id
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Burn body", "burning", new int[]{
                ActionTypesProxy.ACTION_TYPE_NOMOVE,
                ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS,
                ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM
        }).range(12).build();
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
        if (performer.isPlayer() && source != null && target != null &&
                source.getTemplateId() == CustomItems.fireCrystal.getTemplateId() && source.getTemplateId() == CustomItems.lesserFireCrystal.getTemplateId() &&
                source.getTopParent() == performer.getInventory().getWurmId() &&
                (target.getTopParent() != performer.getInventory().getWurmId())) {
            return target.getTemplateId() == ItemList.corpse;
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
        int karma = 50;
        int money = 60;
        try {
            Communicator comm = performer.getCommunicator();
            if (target.getName().contains("dragon")) {
                comm.sendNormalServerMessage("Dragon corpses are naturally immune to fire.");
                return true;
            }
            if (target.isUnique()) {
                comm.sendNormalServerMessage("This unique creature does not seem to be affected by the crystal.");
                return true;
            }
            if (target.getTopParentOrNull() == performer.getInventory()) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You can only burn the corpse of the %s while it is on the ground.", target.getName()));
                return true;
            }
            if (!MethodsItems.isLootableBy(performer, target)) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You are not allowed to burn the %s.", target.getName()));
                return true;
            }
            if (target.getTemplateId() == ItemList.corpse) {
                if (counter == 1f) {
                    performer.getCurrentAction().setTimeLeft(30);
                    performer.sendActionControl("burning", true, 30);
                    comm.sendNormalServerMessage(String.format("You touch the crystal to the %s and it bursts into flames!", source.getName()));
                    Server.getInstance().broadCastAction(String.format("%s touches the %s to the %s and it bursts into flames!", performer.getName(), source.getName(), target.getName()), performer, 5);
                    performer.playAnimation("place", false);
                    comm.sendAddEffect(target.getWurmId(), target.getWurmId(), EffectConstants.EFFECT_GENERIC, target.getPosX(), target.getPosY(), target.getPosZ(), (byte) 0, "magicfire", 7, 0f);
                } else {
                    if (counter * 10f > action.getTimeLeft()) {
                        Items.destroyItem(target.getWurmId());
                        if (source.getTemplate().getTemplateId() == CustomItems.lesserFireCrystal.getTemplateId()) {
                            Items.destroyItem(source.getWurmId());
                        }
                        Item[] coins = Economy.getEconomy().getCoinsFor(money);
                        for (Item coin : coins) {
                            performer.getInventory().insertItem(coin, true);
                        }
                        //performer.addMoney(money);
                        performer.setKarma(performer.getKarma() + karma);
                        comm.sendNormalServerMessage(String.format("As the %s burns away, the gods give you %s iron coins and bestowed %s karma for sacrificing the animals spirit to them!", target.getName(), money, karma));
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            RequiemLogging.logException("[ERROR] in action in BurnCorpseAction]", e);
            return true;
        }
    }

}


