package org.jubaroo.mods.wurm.server.actions.creatures.npc;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.jubaroo.mods.wurm.server.actions.creatures.BaseManagementAction;

import java.util.LinkedList;
import java.util.List;

public class ManageBehaviourProvider implements BehaviourProvider {
    private final List<BaseManagementAction> manageActions/*, movementActions*/;

    public ManageBehaviourProvider() {
        manageActions = new LinkedList<>();
        //manageActions.add(new CustomizeFaceAction());
        //manageActions.add(new EquipmentAction());
        manageActions.add(new FaceMeAction());
        //movementActions = new LinkedList<>();
    }

    public static boolean canManage(Creature performer, Creature target) {
        return performer.getPower() >= MiscConstants.POWER_DEMIGOD && /* target.isNpc() || target.isNpc() || MethodsBestiary.isRequiemNPC(target) || target.isBartender() || target.isTrader() ||
                target.isNpcTrader() || */ target.getTemplate().getTemplateId() > 0;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Creature target) {
        if (!canManage(performer, target)) return null;

        LinkedList<ActionEntry> manage = new LinkedList<>();

        for (BaseManagementAction act : manageActions) {
            if (act.canUse(performer, target))
                manage.add(act.getActionEntry());
        }

        int manageEntries = manage.size();

        //LinkedList<ActionEntry> movement = new LinkedList<>();

        //for (BaseManagementAction act : movementActions) {
        //    if (act.canUse(performer, target))
        //        movement.add(act.getActionEntry());
        //}

        //if (movement.size() > 0) {
        //    manage.add(new ActionEntry((short) -movement.size(), "Movement", ""));
        //    manage.addAll(movement);
        //    manageEntries += 1;
        //}

        if (manageEntries > 0) {
            manage.add(0, new ActionEntry((short) -manageEntries, "Manage NPC", ""));
        }

        return manage;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item subject, Creature target) {
        return getBehavioursFor(performer, target);
    }
}
