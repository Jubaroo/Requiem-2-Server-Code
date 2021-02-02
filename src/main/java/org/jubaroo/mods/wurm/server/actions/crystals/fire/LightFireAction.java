package org.jubaroo.mods.wurm.server.actions.crystals.fire;

import com.wurmonline.server.Items;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.behaviours.BehaviourList;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.effects.Effect;
import com.wurmonline.server.effects.EffectFactory;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import static org.gotti.wurmunlimited.modsupport.actions.ActionPropagation.*;

public class LightFireAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final ActionEntry actionEntry;

    public LightFireAction() {
        actionEntry = ActionEntry.createEntry((short) ModActions.getNextActionId(), "Light", "lighting", new int[]{
                ActionTypesProxy.ACTION_TYPE_NOMOVE,
                ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS,
                ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM
        });
        ModActions.registerAction(actionEntry);
    }

    public static boolean canUse(Creature performer, Item source, Item target) {
        if (performer.isPlayer() && source != null && target != null &&
                (source.getTemplateId() == CustomItems.fireCrystal.getTemplateId() || source.getTemplateId() == CustomItems.lesserFireCrystal.getTemplateId()) &&
                source.getTopParent() == performer.getInventory().getWurmId() &&
                (target.isLight() || target.isFire() || target.getTemplate().getBehaviourType() == 18) && !target.isOnFire()) {

            if (target.getTemplate().isTransportable() && target.getTopParent() != target.getWurmId()) return false;
            return !target.isStreetLamp() || target.isPlanted();
        } else return false;
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

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
        if (canUse(performer, source, target))
            return Collections.singletonList(actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {
        if (!canUse(performer, source, target)) {
            performer.getCommunicator().sendAlertServerMessage("Do you have the correct item activated and/or targeting the correct item/creature? Please try again or use /support to open a ticket with as much info as possible, and the staff will get to it as soon as possible.");
            return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
        }

        //Communicator comm = performer.getCommunicator();

        if (target.isOilConsuming() || target.isCandleHolder() || target.getTemplateId() == ItemList.beeSmoker || target.getTemplateId() == ItemList.openFireplace)
            target.setAuxData((byte) 127);

        performer.getCommunicator().sendNormalServerMessage(String.format("You light the %s.", target.getName()));
        Server.getInstance().broadCastAction(String.format("%s lights %s.", performer.getName(), target.getNameWithGenus()), performer, 5);
        target.setTemperature(Short.MAX_VALUE);
        if (source.getTemplate().getTemplateId() == CustomItems.lesserFireCrystal.getTemplateId()) {
            Items.destroyItem(source.getWurmId());
        }
        try {
            ReflectionUtil.callPrivateMethod(target, Item.class.getDeclaredMethod("notifyWatchersTempChange"));
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            RequiemLogging.logException("[ERROR] heating item: ", e);
        }

        if (target.getTemplate().getBehaviourType() == BehaviourList.fireBehaviour && target.getTemplateId() != ItemList.beeSmoker) {
            final Effect effect = EffectFactory.getInstance().createFire(target.getWurmId(), target.getPosX(), target.getPosY(), target.getPosZ(), performer.isOnSurface());
            target.addEffect(effect);
        }

        return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
    }
}