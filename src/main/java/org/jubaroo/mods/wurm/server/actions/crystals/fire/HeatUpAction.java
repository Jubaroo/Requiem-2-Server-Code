package org.jubaroo.mods.wurm.server.actions.crystals.fire;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.Items;
import com.wurmonline.server.NoSuchItemException;
import com.wurmonline.server.NoSuchPlayerException;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.NoSuchCreatureException;
import com.wurmonline.server.items.*;
import com.wurmonline.server.skills.NoSuchSkillException;
import com.wurmonline.server.zones.NoSuchZoneException;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.gotti.wurmunlimited.modsupport.actions.ActionPropagation.*;

public class HeatUpAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final ActionEntry actionEntry;
    private Map<Integer, TempState> tempStates;

    public HeatUpAction() {
        actionEntry = ActionEntry.createEntry((short) ModActions.getNextActionId(), "Heat", "heating", new int[]{
                ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS,
                ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM
        });
        ModActions.registerAction(actionEntry);
        initStatesMap();
    }

    private void initStatesMap() {
        try {
            tempStates = new HashMap<>();
            Method mGetOrigItemTemplateId = ReflectionUtil.getMethod(TempState.class, "getOrigItemTemplateId");

            Set<TempState> statesSet = ReflectionUtil.getPrivateField(null, ReflectionUtil.getField(TempStates.class, "tempStates"));
            for (final TempState tempState : statesSet) {
                int orig = ReflectionUtil.callPrivateMethod(tempState, mGetOrigItemTemplateId);
                tempStates.put(orig, tempState);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            throw new RuntimeException("Error getting temp states", e);
        }
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
        if (LightFireAction.canUse(performer, source, target)) return false;
        return performer.isPlayer() && source != null && target != null && !target.isBulkItem() &&
                (source.getTemplateId() == CustomItems.fireCrystal.getTemplateId() || source.getTemplateId() == CustomItems.lesserFireCrystal.getTemplateId()) &&
                source.getTopParent() == performer.getInventory().getWurmId() && !target.isLight() && !target.isAlwaysLit() &&
                (target.getTopParent() == performer.getInventory().getWurmId() || (target.getLastOwnerId() == performer.getWurmId()));
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
        if (canUse(performer, source, target))
            return Collections.singletonList(actionEntry);
        else
            return null;
    }

    private Item transformItem(Item item, TempState state, Creature performer) throws NoSuchFieldException, IllegalAccessException, NoSuchTemplateException, FailedException, NoSuchItemException, NoSuchPlayerException, NoSuchZoneException, NoSuchCreatureException, NoSuchSkillException {
        Item newItem;
        int newItemTemplateId = ReflectionUtil.getPrivateField(state, ReflectionUtil.getField(TempState.class, "newItemTemplateId"));
        if (ReflectionUtil.getPrivateField(state, ReflectionUtil.getField(TempState.class, "keepMaterial"))) {
            newItem = ItemFactory.createItem(newItemTemplateId, item.getCurrentQualityLevel(), item.getMaterial(), item.getRarity(), item.creator);
        } else {
            newItem = ItemFactory.createItem(newItemTemplateId, item.getCurrentQualityLevel(), (byte) 0, item.getRarity(), item.creator);
        }

        newItem.setDescription(item.getDescription());

        for (Item sub : item.getItems()) {
            try {
                item.dropItem(sub.getWurmId(), false);
                newItem.insertItem(sub, true);
            } catch (NoSuchItemException e) {
                RequiemLogging.logException("[ERROR] in transformItem in HeatUpAction]", e);
            }
        }

        if (ReflectionUtil.getPrivateField(item.getTemplate(), ReflectionUtil.getField(ItemTemplate.class, "passFullData")))
            newItem.setData(item.getData());

        newItem.setLastOwnerId(item.getLastOwnerId());

        if (ReflectionUtil.getPrivateField(state, ReflectionUtil.getField(TempState.class, "keepWeight"))) {
            newItem.setWeight(item.getWeightGrams(), false);
        } else {
            float mod = (float) item.getWeightGrams() / (float) item.getTemplate().getWeightGrams();
            if (item.getTemplateId() == 684)
                mod *= 0.8F;
            int newWeight = (int) ((float) newItem.getTemplate().getWeightGrams() * mod);
            newItem.setWeight(newWeight, false);
        }

        if (newItem.getWeightGrams() > 0) {
            newItem.setTemperature(Short.MAX_VALUE);

            Item parent = item.getParentOrNull();
            if (parent == null)
                newItem.putItemInfrontof(performer);
            else if (!parent.insertItem(newItem, true, false))
                newItem.putItemInfrontof(performer);

        } else {
            Items.decay(newItem.getWurmId(), newItem.getDbStrings());
        }

        if (newItem.hasPrimarySkill())
            performer.getSkills().getSkillOrLearn(newItem.getPrimarySkill()).skillCheck(newItem.getTemplate().getDifficulty(), newItem, 0.0D, false, 1f);

        Items.destroyItem(item.getWurmId());

        return newItem;
    }


    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {

        Item parent = null;
        try {
            parent = target.getParent();
        } catch (NoSuchItemException e) {
            RequiemLogging.logException("[Error] in action in HeatUpAction", e);
        }

        if (!canUse(performer, source, target)) {
            performer.getCommunicator().sendAlertServerMessage("Do you have the correct item activated and/or targeting the correct item/creature? Please try again or use /support to open a ticket with as much info as possible, and the staff will get to it as soon as possible.");
            return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
        }

        target.setTemperature(Short.MAX_VALUE);
        if (source.getTemplate().getTemplateId() == CustomItems.lesserFireCrystal.getTemplateId()) {
            Items.destroyItem(source.getWurmId());
        }

        if (tempStates.containsKey(target.getTemplateId())) {
            try {
                Item res = transformItem(target, tempStates.get(target.getTemplateId()), performer);

                performer.getCommunicator().sendNormalServerMessage(String.format("You touch the %s with the crystal and it turns into %s!", target.getName(), res.getNameWithGenus()));
            } catch (Exception e) {
                performer.getCommunicator().sendAlertServerMessage("Something went wrong. Try again later or open a support ticket.");
            }
        }
        if (parent != null && target.getTemplateId() == ItemList.snowball && parent.isContainerLiquid()) {
            try {
                Item water = ItemFactory.createItem(ItemList.water, target.getQualityLevel(), target.getRarity(), null);
                water.setWeight(target.getWeightGrams(), false);
                parent.dropItem(target.getWurmId(), false);
                parent.insertItem(water, true, false);
                Items.destroyItem(target.getWurmId());
                if (source.getTemplate().getTemplateId() == CustomItems.lesserFireCrystal.getTemplateId()) {
                    Items.destroyItem(source.getWurmId());
                }
                performer.getCommunicator().sendNormalServerMessage(String.format("You touch the snowball with the %s and it melts into the %s.", source.getName(), parent.getName()));
            } catch (FailedException | NoSuchTemplateException | NoSuchItemException e) {
                RequiemLogging.logException("[Error] on cool action: ", e);
            }
        }
        if (target.getTemplateId() == ItemList.snowman) {
            Items.destroyItem(target.getWurmId());
            if (source.getTemplate().getTemplateId() == CustomItems.lesserFireCrystal.getTemplateId()) {
                Items.destroyItem(source.getWurmId());
            }
            performer.getCommunicator().sendNormalServerMessage(String.format("You touch the %s with the %s and it melts away into nothing.", target.getName(), source.getName()));
        } else if (target.getTemplateId() != ItemList.snowball) {
            if (source.getTemplate().getTemplateId() == CustomItems.lesserFireCrystal.getTemplateId()) {
                Items.destroyItem(source.getWurmId());
            }
            performer.getCommunicator().sendNormalServerMessage(String.format("You touch the %s with the crystal and it heats up instantly!", target.getName()));
            try {
                ReflectionUtil.callPrivateMethod(target, Item.class.getDeclaredMethod("notifyWatchersTempChange"));
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                RequiemLogging.logException("[ERROR] in action in HeatUpAction]", e);
            }
        }
        return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
    }
}
