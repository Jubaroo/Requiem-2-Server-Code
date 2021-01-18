package org.jubaroo.mods.wurm.server.actions.magicItems;

import com.wurmonline.server.Items;
import com.wurmonline.server.NoSuchItemException;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.Materials;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zones;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.util.Collections;
import java.util.List;

import static org.gotti.wurmunlimited.modsupport.actions.ActionPropagation.*;

public class ConvertWoodTypeAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final ActionEntry actionEntry;

    public ConvertWoodTypeAction() {
        actionEntry = ActionEntry.createEntry((short) ModActions.getNextActionId(), "Change wood type", "changing wood type", new int[]{
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

    public boolean canUse(Creature performer, Item source, Item target) {
        if (performer.isPlayer() && source != null && target != null &&
                source.getTemplateId() == CustomItems.essenceOfWoodId &&
                source.getTopParent() == performer.getInventory().getWurmId() &&
                (target.getTopParent() == performer.getInventory().getWurmId() || target.getLastOwnerId() == performer.getWurmId()/*|| performer.getPower() > 0*/)) {

            return target.isWood();
        } else return false;
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

        final String mat = Materials.convertMaterialByteIntoString(source.getMaterial());

        if (!canUse(performer, source, target)) {
            performer.getCommunicator().sendAlertServerMessage("Do you have the correct item activated and/or targeting the correct item/creature? Please try again or use /support to open a ticket with as much info as possible, and the staff will get to it as soon as possible.");
            return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
        }
        if (!performer.isWithinDistanceTo(target, 8f)) {
            performer.getCommunicator().sendNormalServerMessage(String.format("You must be closer to the %s.", target.getName()));
            return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
        }
        if (target.getMaterial() == source.getMaterial()) {
            performer.getCommunicator().sendNormalServerMessage(String.format("The %s is already the same wood type as the %s.", source.getName(), target.getName()));
            return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
        } else {
            target.setMaterial(source.getMaterial());

            if (target.getParentId() == -10) {
                VolaTile tile = Zones.getTileOrNull(target.getTilePos(), target.isOnSurface());
                if (tile != null) {
                    tile.makeInvisible(target);
                    tile.makeVisible(target);
                }
            } else {
                Item parent = target.getParentOrNull();
                if (parent != null) {
                    try {
                        parent.dropItem(target.getWurmId(), false);
                        parent.insertItem(target, true);
                    } catch (NoSuchItemException e) {
                        RequiemLogging.logWarning("Error setting wood material: " + e);
                    }
                }
            }
            Items.destroyItem(source.getWurmId());
            performer.getCommunicator().sendNormalServerMessage(String.format("You touch the %s with the %s and it magically changes the wood type to %s", target.getName(), source.getName(), mat));
        }

        return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
    }
}