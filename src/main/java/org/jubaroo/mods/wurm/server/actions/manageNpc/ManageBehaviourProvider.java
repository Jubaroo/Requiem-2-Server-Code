package org.jubaroo.mods.wurm.server.actions.manageNpc;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;

import java.util.LinkedList;
import java.util.List;

public class ManageBehaviourProvider implements BehaviourProvider {
    private final List<BaseManagementAction> manageActions/*, movementActions*/;

    public ManageBehaviourProvider() {
        manageActions = new LinkedList<>();
        manageActions.add(new FaceMeAction());
    }

    public static boolean canManage(Creature performer, Creature target) {
        if (performer.getPower() >= MiscConstants.POWER_DEMIGOD) {
            target.getTemplate().getTemplateId();
        }
        return false;
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
