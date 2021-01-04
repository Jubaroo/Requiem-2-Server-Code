package org.jubaroo.mods.wurm.server.actions.crystals.frost;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.Items;
import com.wurmonline.server.NoSuchItemException;
import com.wurmonline.server.behaviours.*;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.NoSuchTemplateException;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.Requiem;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import static org.gotti.wurmunlimited.modsupport.actions.ActionPropagation.*;

public class CoolAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final short actionId;
    private final ActionEntry actionEntry;

    public CoolAction() {
        // Get the action id
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Cool", "cooling", new int[]{
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
                (target.getTopParent() == performer.getInventory().getWurmId() || target.getLastOwnerId() == performer.getWurmId() || target.getTemplateId() == ItemList.snowman/*|| performer.getPower() > 0*/)) {

            if (target.getTemplateId() == ItemList.snowman) {
                return Methods.isActionAllowed(performer, Actions.REPAIR, target);
            }

            return target.getTemplateId() == ItemList.water || target.getTemperature() >= 200 && !target.isLight();
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
        Communicator comm = performer.getCommunicator();

        if (!canUse(performer, source, target)) {
            performer.getCommunicator().sendAlertServerMessage("Do you have the correct item activated and/or targeting the correct item/creature? Please try again or use /support to open a ticket with as much info as possible, and the staff will get to it as soon as possible.");
            return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
        }
        if (target.getTemplateId() == ItemList.water) {
            try {
                Item snow = ItemFactory.createItem(ItemList.snowball, target.getQualityLevel(), target.getRarity(), null);
                snow.setWeight(target.getWeightGrams(), false);
                Item parent = target.getParent();
                parent.dropItem(target.getWurmId(), false);
                parent.insertItem(snow, true, false);
                Items.destroyItem(target.getWurmId());
                comm.sendNormalServerMessage(String.format("You touch the water with the %s and it turns into a snowball!", source.getName()));
            } catch (FailedException | NoSuchTemplateException | NoSuchItemException e) {
                Requiem.logger.log(Level.SEVERE, ("Error on cool action: " + e));
            }
        } else if (target.getTemplateId() == ItemList.snowman) {
            target.setDamage(0);
            target.setQualityLevel(99f);
            comm.sendNormalServerMessage(String.format("You touch the crystal with the %s and it suddenly looks much better!", source.getName()));
        }
        //else if (target.isFood()) {
        //    target.setHasNoDecay(true);
        //    comm.sendNormalServerMessage(String.format("You touch the %s with the %s and it freezes it in time stopping the decay!", target.getName(), source.getName()));
        //}
        else {
            target.setTemperature((short) 200);
            try {
                ReflectionUtil.callPrivateMethod(target, Item.class.getDeclaredMethod("notifyWatchersTempChange"));
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                Requiem.logger.log(Level.SEVERE, ("Error on cool action: " + e));
            }
            comm.sendNormalServerMessage(String.format("You touch the %s with the %s and it cools down immediately!", target.getName(), source.getName()));
        }
        return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
    }
}