package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.NoSuchItemException;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.AutoEquipMethods;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.util.Collections;
import java.util.List;

public class UnequipAllAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public UnequipAllAction() {
        RequiemLogging.logWarning("UnequipAllAction()");

        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Unequip all armour",
                "unequipping",
                new int[]{0}
                //new int[] { 6 /* ACTION_TYPE_NOMOVE */ }	// 6 /* ACTION_TYPE_NOMOVE */, 48 /* ACTION_TYPE_ENEMY_ALWAYS */, 36 /* ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM */
        );
        ModActions.registerAction(actionEntry);
    }


    @Override
    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            // Menu with activated object
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item object) {
                return this.getBehavioursFor(performer, object);
            }

            // Menu without activated object
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item object) {
                try {
                    if (performer instanceof Player && object != null && object.getParentOrNull() != null && object.getParent().isBodyPart() && object.getParent().getOwnerId() == performer.getWurmId()) {
                        return Collections.singletonList(actionEntry);
                    }
                } catch (NoSuchItemException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };
    }

    @Override
    public ActionPerformer getActionPerformer() {
        return new ActionPerformer() {

            @Override
            public short getActionId() {
                return actionId;
            }

            // Without activated object
            @Override
            public boolean action(Action act, Creature performer, Item target, short action, float counter) {
                if (performer instanceof Player) {
                    try {
                        Player player = (Player) performer;
                        if (target.getParent() == null) {
                            player.getCommunicator().sendSafeServerMessage("You cannot unequip an item that isn't equipped.");
                            return true;
                        }
                        if (target.getParent().getOwnerId() != player.getWurmId()) {
                            player.getCommunicator().sendSafeServerMessage("You cannot unequip an item that you do not own.");
                            return true;
                        }
                        for (Item equip : player.getBody().getAllItems()) {
                            if (equip.isArmour() && equip.getParent().getWurmId() != player.getBody().getId()) {
                                AutoEquipMethods.unequip(equip, player);
                            }
                        }
                    } catch (NoSuchItemException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    RequiemLogging.logInfo(String.format("Somehow a non-player activated action ID %d...", actionEntry.getNumber()));
                }
                return true;
            }

            @Override
            public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
                return this.action(act, performer, target, action, counter);
            }


        }; // ActionPerformer
    }
}