package org.jubaroo.mods.wurm.server.actions.character;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Server;
import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.behaviours.*;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.skills.Skill;
import com.wurmonline.server.skills.SkillList;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.misc.MiscChanges;
import org.jubaroo.mods.wurm.server.utils.Cooldowns;

import java.util.Collections;
import java.util.List;

public class SmashAction implements ModAction, ActionTypesProxy {
    private static short actionId;

    private final ActionEntry actionEntry;
    private final String effectName = "smash";
    private final int cooldown = (int) TimeConstants.FIFTEEN_MINUTES_MILLIS;
    private final int staminaCost = 15000;
    private final int skillReq = (int) 25.0;
    private final int castTime;
    private final boolean canMiss;

    public SmashAction() {
        this.castTime = 30; // 3 seconds
        this.canMiss = false;
        SmashAction.actionId = (short) ModActions.getNextActionId();
        ModActions.registerAction(this.actionEntry = ActionEntry.createEntry(SmashAction.actionId, "Smash", "smashing",
                new int[]{
                        ActionTypesProxy.ACTION_TYPE_NOMOVE,
                        ActionTypesProxy.ACTION_TYPE_ATTACK,
                        ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS}));
    }

    public static short getActionId() {
        return SmashAction.actionId;
    }

    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            public List<ActionEntry> getBehavioursFor(final Creature performer, final Creature object) {
                return this.getBehavioursFor(performer, null, object);
            }

            public List<ActionEntry> getBehavioursFor(final Creature performer, final Item subject, final Creature target) {
                if (performer.isPlayer() && !target.isPlayer()) {
                    return Collections.singletonList(SmashAction.this.actionEntry);
                }
                return null;
            }
        };
    }

    public ActionPerformer getActionPerformer() {
        return new ActionPerformer() {
            public short getActionId() {
                return SmashAction.actionId;
            }

            public boolean action(final Action act, final Creature performer, final Creature target, final short action, final float counter) {
                return this.action(act, performer, null, target, action, counter);
            }

            public boolean action(final Action act, final Creature performer, final Item source, final Creature target, final short action, final float counter) {
                final String playerEffect = performer.getName() + effectName;
                if (!performer.isPlayer() || target.isPlayer()) {
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
                if (target.getWurmId() == performer.getWurmId() || target.getKingdomId() == performer.getKingdomId() || target.getPower() >= MiscConstants.POWER_DEMIGOD) {
                    performer.getCommunicator().sendNormalServerMessage("That would be frowned upon.");
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
                if (Cooldowns.isOnCooldown(playerEffect, cooldown)) {
                    performer.getCommunicator().sendNormalServerMessage(String.format("You will need to wait %s before you can do that again.", Cooldowns.timeRemaining(playerEffect, cooldown)));
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
                //if (performer.isFighting()) {
                //    performer.getCommunicator().sendNormalServerMessage("You can't use smash while fighting.");
                //    return true;
                //}
                try {
                    if (counter == 1f) {
                        performer.getCurrentAction().setTimeLeft(SmashAction.this.castTime);
                        performer.sendActionControl("Smashing", true, SmashAction.this.castTime);
                        return propagate(act, ActionPropagation.CONTINUE_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                    }
                    if (counter * 10f <= act.getTimeLeft()) {
                        return propagate(act, ActionPropagation.CONTINUE_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                    }
                } catch (NoSuchActionException e2) {
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
                Cooldowns.setUsed(playerEffect);
                if (!performer.mayAttack(target)) {
                    performer.getCommunicator().sendNormalServerMessage("You may not attack that.");
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
                final double strength = performer.getSkills().getSkillOrLearn(SkillList.BODY_STRENGTH).getKnowledge();
                if (strength < skillReq) {
                    performer.getCommunicator().sendNormalServerMessage("You still lack the body strength to do that efficiently (25 BS).");
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
                if (performer.getStatus().getStamina() > staminaCost) {
                    performer.getStatus().modifyStamina((float) (performer.getStatus().getStamina() - staminaCost));
                    if (SmashAction.this.canMiss) {
                        final double strCheck = Math.min(95.0, 30.0 + strength * 0.7);
                        final int rndCheck = Server.rand.nextInt(100);
                        RequiemLogging.debug(String.format("smash(): rnd %d > str %s == %smiss!", rndCheck, strCheck, (rndCheck > strCheck) ? "" : "not "));
                        if (rndCheck > strCheck) {
                            MiscChanges.actionNotify(performer, "You swing viciously, but miss.", "%NAME swings viciously, but miss.", "A shadowy form swings viciously, but miss");
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                    }
                    try {
                        final double bodyControl = performer.getSkills().getSkill(SkillList.BODY_CONTROL).getKnowledge();
                        final double power = 23.0 + bodyControl * 0.7699999809265137;
                        double damage = 20000.0 + 13000.0 * (power / 100.0);
                        damage *= (performer.isStealth() ? 1.1f : 1f);
                        final byte pos = target.getBody().getRandomWoundPos();
                        target.addWoundOfType(performer, (byte) 0, pos, false, 1f, true, damage, 0f, 0f, true, false);
                        MiscChanges.actionNotify(performer, String.format("You smash the %s viciously with preternatural strength!", target.getName()), String.format("%%NAME smashes %s viciously!", target.getName()), String.format("A shadowy form smashes %s viciously!", target.getName()));
                        target.setStealth(false);
                        final boolean done = performer.getCombatHandler().attack(target, Server.getCombatCounter(), false, counter, act);
                        CreatureBehaviour.setOpponent(performer, target, done, act);
                        final Skill s = target.getSkills().getSkillOrLearn(SkillList.BODY_STRENGTH);
                        s.skillCheck(1.0, 0.0, false, 1f);
                    } catch (Exception e) {
                        RequiemLogging.logException( "failed to smash", e);
                        return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                    }
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
                performer.getCommunicator().sendNormalServerMessage("You are too tired.");
                return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
        };
    }
}
