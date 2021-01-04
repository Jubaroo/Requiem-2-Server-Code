package org.jubaroo.mods.wurm.server.actions.crystals;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.Items;
import com.wurmonline.server.NoSuchItemException;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.NoSuchTemplateException;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.Requiem;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.server.Constants;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import static org.gotti.wurmunlimited.modsupport.actions.ActionPropagation.*;

public class ArcaneShardAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final short actionId;
    private final ActionEntry actionEntry;

    public ArcaneShardAction() {
        // Get the action id
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Convert Shard", "convert", new int[]{
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

    private boolean canUse(Creature performer, Item source, Item target) {
        if (performer.isPlayer() && source != null && target != null &&
                source.getTemplateId() == CustomItems.arcaneShardId &&
                source.getTopParent() == performer.getInventory().getWurmId() &&
                (target.getTopParent() == performer.getInventory().getWurmId() || target.getLastOwnerId() == performer.getWurmId()/*|| performer.getPower() > 0*/)) {

            return target.getTemplateId() == ItemList.colossusOfMagranon || target.getTemplateId() == ItemList.colossusOfVynora || target.getTemplateId() == ItemList.colossusOfLibila || target.getTemplateId() == ItemList.colossusOfFo;
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
        if (!canUse(performer, source, target)) {
            performer.getCommunicator().sendAlertServerMessage("Do you have the correct item activated and/or targeting the correct item/creature? Please try again or use /support to open a ticket with as much info as possible, and the staff will get to it as soon as possible.");
            return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
        }
        if (target.getTemplateId() == ItemList.colossusOfMagranon) {
            shardAction(CustomItems.fireCrystal.getTemplateId(), performer, source, target);
        } else if (target.getTemplateId() == ItemList.colossusOfVynora) {
            shardAction(CustomItems.frostCrystal.getTemplateId(), performer, source, target);
        } else if (target.getTemplateId() == ItemList.colossusOfFo) {
            shardAction(CustomItems.lifeCrystal.getTemplateId(), performer, source, target);
        } else if (target.getTemplateId() == ItemList.colossusOfLibila) {
            shardAction(CustomItems.deathCrystal.getTemplateId(), performer, source, target);
        }
        return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
    }

    private static void shardAction(int id, Creature performer, Item source, Item target) {
        try {
            Communicator comm = performer.getCommunicator();
            Item crystal = ItemFactory.createItem(id, 99f, performer.getName());
            Item parent = source.getParent();
            parent.dropItem(source.getWurmId(), false);
            parent.insertItem(crystal, true, false);
            Items.destroyItem(source.getWurmId());
            comm.sendCombatAlertMessage(String.format("Your life force is drained, as it is used to power the creation of the %s!", crystal.getName()));
            performer.addWoundOfType(null, (byte) 9, 1, false, 1.0f, false, Constants.itemCrystalCreationDamage, 0f, 0f, true, false);
            comm.sendNormalServerMessage(String.format("You touch the %s with the %s and it transforms into a %s!", target.getName(), source.getName(), crystal.getName()));
        } catch (FailedException | NoSuchTemplateException | NoSuchItemException e) {
            Requiem.logger.log(Level.SEVERE, (String.format("Error on arcane shard action: %s", e)));
        }
    }
}