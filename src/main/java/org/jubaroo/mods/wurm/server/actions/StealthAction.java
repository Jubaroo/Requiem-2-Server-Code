package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.Players;
import com.wurmonline.server.Servers;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.behaviours.NoSuchActionException;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.utils.Cooldowns;

import java.util.Collections;
import java.util.List;

public class StealthAction implements ModAction {
    private static short actionId;
    private final ActionEntry actionEntry;
    private final int castTime = 5; // 0.5 seconds
    private final String effectName = "instantstealth";
    private final int cooldown = 900000; // 15 minutes

    public StealthAction() {
        RequiemLogging.logInfo("StealthAction()");
        StealthAction.actionId = (short) ModActions.getNextActionId();
        ModActions.registerAction(this.actionEntry = ActionEntry.createEntry(StealthAction.actionId, "Stealth", "stealthing", new int[]{
                ActionTypesProxy.ACTION_TYPE_NOMOVE
        }));
    }

    public static short getActionId() {
        return StealthAction.actionId;
    }

    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            public List<ActionEntry> getBehavioursFor(final Creature performer, final Item source, final Item object) {
                return this.getBehavioursFor(performer, object);
            }

            public List<ActionEntry> getBehavioursFor(final Creature performer, final Item object) {
                if (performer.isPlayer() && object != null && object.getTemplateId() == ItemList.bodyBody) {
                    return Collections.singletonList(StealthAction.this.actionEntry);
                }
                return null;
            }
        };
    }

    public ActionPerformer getActionPerformer() {
        return new ActionPerformer() {
            public short getActionId() {
                return StealthAction.actionId;
            }

            public boolean action(final Action act, final Creature performer, final Item target, final short action, final float counter) {
                try {
                    if (counter == 1f) {
                        performer.getCurrentAction().setTimeLeft(5);
                        performer.sendActionControl("Stealthing", true, castTime);
                        return false;
                    }
                    if (counter * 10f <= act.getTimeLeft()) {
                        return false;
                    }
                } catch (NoSuchActionException e) {
                    return true;
                }
                if (this.isWithinDistanceToOthers(performer)) {
                    performer.getCommunicator().sendNormalServerMessage("You are too close to someone to be able to stealth.");
                    return true;
                }
                final String playerEffect = performer.getName() + effectName;
                if (Cooldowns.isOnCooldown(playerEffect, cooldown)) {
                    performer.getCommunicator().sendNormalServerMessage(String.format("You will need to wait %s before you can do that again.", Cooldowns.timeRemaining(playerEffect, cooldown)));
                    if (!Servers.isThisATestServer()) {
                        return true;
                    }
                    performer.getCommunicator().sendNormalServerMessage("... but you are on a test server, so allowing it!");
                }
                performer.setStealth(true);
                Cooldowns.setUsed(playerEffect);
                return true;
            }

            private boolean isWithinDistanceToOthers(final Creature stealther) {
                final Player[] players = Players.getInstance().getPlayers();
                Player[] array;
                for (int length = (array = players).length, i = 0; i < length; ++i) {
                    final Player p = array[i];
                    if (!p.isStealth() && p.getPower() <= 0) {
                        if (p.getWurmId() != stealther.getWurmId()) {
                            if (p.isWithinDistanceTo(stealther, 30f)) {
                                RequiemLogging.logInfo(String.format("%s is too close to %s to be able to insta-stealth", p.getName(), stealther.getName()));
                                return true;
                            }
                        }
                    }
                }
                return false;
            }

            public boolean action(final Action act, final Creature performer, final Item source, final Item target, final short action, final float counter) {
                return this.action(act, performer, target, action, counter);
            }
        };
    }
}
