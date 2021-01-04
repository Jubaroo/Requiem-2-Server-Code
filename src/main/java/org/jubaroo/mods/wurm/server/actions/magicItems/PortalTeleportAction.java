package org.jubaroo.mods.wurm.server.actions.magicItems;

import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.items.ItemMod;
import org.jubaroo.mods.wurm.server.utils.GoTo;

import java.util.Collections;
import java.util.List;

public class PortalTeleportAction implements ModAction, BehaviourProvider, ActionPerformer {
    public final short actionId;
    public final ActionEntry actionEntry;

    public PortalTeleportAction() {

        actionId = (short) ModActions.getNextActionId();
        int[] types = {
                ActionTypesProxy.ACTION_TYPE_FATIGUE,
                ActionTypesProxy.ACTION_TYPE_MISSION,
                ActionTypesProxy.ACTION_TYPE_SHOW_ON_SELECT_BAR,
        };

        actionEntry = ActionEntry.createEntry(actionId, "Use Portal", "Teleporting", types);
        try {
            ReflectionUtil.setPrivateField(actionEntry, ReflectionUtil.getField(ActionEntry.class, "maxRange"), 5);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        ModActions.registerAction(actionEntry);
    }

    @Override
    public short getActionId() {
        return actionId;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature actor, Item source, Item target) {

        if (target == null) {
            return null;
        }

        if (ItemMod.isNotPortalItem(target)) {
            return null;
        }

        return Collections.singletonList(actionEntry);
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature actor, Item target) {
        return getBehavioursFor(actor, null, target);
    }

    @Override
    public boolean action(Action action, Creature actor, Item target, short num, float counter) {
        return action(action, actor, null, target, num, counter);
    }

    @Override
    public boolean action(Action action, Creature actor, Item source, Item target, short num, float counter) {

        Communicator comm = actor.getCommunicator();

        if (!(actor instanceof Player)) {
            return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        }

        Player player = (Player) actor;

        if (ItemMod.isHomePortalItem(target)) {

            String name = player.getVillageName();
            if (name.length() == 0) {
                comm.sendNormalServerMessage("You are not a member of a deed.  You have no home to go to...");
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }

            GoTo.sendToVillage(actor, name);
            return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        }

        int x = target.getData1();
        int y = target.getData2();

        int floor = 0;
        int layer = target.getAuxData();
        //int floor = target.getTemperature();

        if (layer > 0) {
            floor = layer;
            layer = 1;
        }

        if (ItemMod.isNotPortalItem(target)) {
            return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        }

        GoTo.sendToXy(actor, x, y, layer, floor);
        return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
    }

}
