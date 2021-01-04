package org.jubaroo.mods.wurm.server.actions.special.events;

import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.tools.RequiemTools;
import org.jubaroo.mods.wurm.server.utils.Cooldowns;

import java.util.Collections;
import java.util.List;

import static org.gotti.wurmunlimited.modsupport.actions.ActionPropagation.*;

public class ReplacementChristmasGiftAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public ReplacementChristmasGiftAction() {
        // Get the action id
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Receive gift", "receiving gift", new int[]{
                ActionTypesProxy.ACTION_TYPE_FATIGUE,
                ActionTypesProxy.ACTION_TYPE_SHOW_ON_SELECT_BAR
        }).range(4).build();
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
                if (performer instanceof Player && object != null && object.getTemplateId() == ItemList.villageToken) {
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
                    if (performer instanceof Player) {
                        final String playerEffect = String.format("%s%s", performer.getName(), ReplacementChristmasGiftAction.class.getName());
                        long cooldown = TimeConstants.YEAR_MILLIS;

                        if (Cooldowns.isOnCooldown(playerEffect, cooldown)) {
                            performer.getCommunicator().sendNormalServerMessage("You already got your gift this year.");
                            return propagate(act, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        performer.getInventory().insertItem(ItemFactory.createItem(10000, 99f, performer.getNameWithoutPrefixes()), true);
                        if (performer.getPower() < 3 || !RequiemTools.isPrivateTestServer()) {
                            Cooldowns.setUsed(playerEffect);
                        }
                        performer.getCommunicator().sendNormalServerMessage("You are given a Rune of Recall!");
                        return propagate(act, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
                    } else {
                        RequiemLogging.logWarning( String.format("Somehow a non-player activated %s...", ReplacementChristmasGiftAction.class.getName()));
                    }
                    return propagate(act, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
                } catch (Exception e) {
                    e.printStackTrace();
                    return propagate(act, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
                }
            }

            @Override
            public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
                return this.action(act, performer, target, action, counter);
            }
        };
    }
}
