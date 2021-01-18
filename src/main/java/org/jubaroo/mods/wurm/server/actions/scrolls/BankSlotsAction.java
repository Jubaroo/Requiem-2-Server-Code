package org.jubaroo.mods.wurm.server.actions.scrolls;

import com.wurmonline.server.Items;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.misc.database.DatabaseHelper;

import java.util.Collections;
import java.util.List;

public class BankSlotsAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public BankSlotsAction() {
        // Get the action id
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Add 5 bank slots", "adding bank slots", new int[]{
                ActionTypesProxy.ACTION_TYPE_FATIGUE,
                ActionTypesProxy.ACTION_TYPE_SHOW_ON_SELECT_BAR
        }).build();
        // Register the action entry
        ModActions.registerAction(this.actionEntry);
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
                if (performer instanceof Player && object != null && object.getTemplateId() == CustomItems.scrollOfBankSlots.getTemplateId()) {
                    return Collections.singletonList(actionEntry);
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
                try {
                    String itemName = target.getName().toLowerCase();
                    if (performer instanceof Player) {
                        Player player = (Player) performer;
                        if (target.getTemplate().getTemplateId() != CustomItems.scrollOfBankSlots.getTemplateId()) {
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        DatabaseHelper.addFiveBankSlots(player.getWurmId());
                        Items.destroyItem(target.getWurmId());
                        player.getCommunicator().sendSafeServerMessage(String.format("The %s exhausts the last of its energy from creating space in your bank and crumbles to dust in your hand.", itemName));
                        player.getCommunicator().sendSafeServerMessage("5 additional slots have been added to your bank account.");
                        Server.getInstance().broadCastAction(String.format("%s uses a magical scroll.", performer.getName()), performer, 5);
                        RequiemLogging.logInfo(String.format("%s has just used a %s!", performer.getName(), target.getName()));
                    } else {
                        RequiemLogging.logWarning(String.format("Somehow a non-player activated a %s...", itemName));
                    }
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                } catch (Exception e) {
                    e.printStackTrace();
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
            }

            @Override
            public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
                return this.action(act, performer, target, action, counter);
            }
        };
    }

}