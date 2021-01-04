package org.jubaroo.mods.wurm.server.actions.creatures.npc;

import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.actions.creatures.BaseManagementAction;

import static org.gotti.wurmunlimited.modsupport.actions.ActionPropagation.*;

public class FaceMeAction extends BaseManagementAction {
    public FaceMeAction() {
        super(ActionEntry.createEntry((short) ModActions.getNextActionId(), "Face me", "managing", new int[]{
                ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS,
                ActionTypesProxy.ACTION_TYPE_NEVER_USE_ACTIVE_ITEM
        }));
    }

    @Override
    public boolean action(Action action, Creature performer, Creature target, short num, float counter) {
        if (canUse(performer, target)) {
            target.turnTowardsCreature(performer);
        }
        return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
    }
}
