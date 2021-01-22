package org.jubaroo.mods.wurm.server.actions.manageNpc;

import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import org.gotti.wurmunlimited.modsupport.actions.ActionPropagation;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

public class EquipmentAction extends BaseManagementAction {
    public EquipmentAction() {
        super(ActionEntry.createEntry((short) ModActions.getNextActionId(), "Equipment", "managing", new int[]{
                ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS,
                ActionTypesProxy.ACTION_TYPE_NEVER_USE_ACTIVE_ITEM
        }));
    }

    @Override
    public boolean action(Action action, Creature performer, Creature target, short num, float counter) {
        if (canUse(performer, target)) {
            Item bodyItem = target.getBody().getBodyItem();
            if (performer.addItemWatched(bodyItem)) {
                performer.getCommunicator().sendOpenInventoryWindow(bodyItem.getWurmId(), target.getName());
                bodyItem.addWatcher(bodyItem.getWurmId(), performer);
            }
        }
        return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
    }
}
