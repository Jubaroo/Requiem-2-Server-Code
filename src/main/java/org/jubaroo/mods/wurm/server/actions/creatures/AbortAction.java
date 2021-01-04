package org.jubaroo.mods.wurm.server.actions.creatures;

import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.behaviours.NoSuchActionException;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.CreaturesProxy;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.skills.Skill;
import com.wurmonline.server.skills.SkillList;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.misc.MiscChanges;
import org.jubaroo.mods.wurm.server.utils.Cooldowns;

import java.util.Collections;
import java.util.List;

public class AbortAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;
    private final int cooldown = (int) TimeConstants.HOUR_MILLIS;
    private final int skillReq = 50;

    public AbortAction() {
        // Get the action id
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Abort offspring", "aborts offspring", new int[]{ActionTypesProxy.ACTION_TYPE_NOMOVE}).range(5).build();
        // Register the action entry
        ModActions.registerAction(this.actionEntry);
    }

    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            public List<ActionEntry> getBehavioursFor(final Creature performer, final Creature object) {
                return this.getBehavioursFor(performer, null, object);
            }

            public List<ActionEntry> getBehavioursFor(final Creature performer, final Item subject, final Creature target) {
                if (performer instanceof Player && target != null && target.isAnimal() && target.isPregnant()) {
                    return Collections.singletonList(AbortAction.this.actionEntry);
                }
                return null;
            }
        };
    }

    public ActionPerformer getActionPerformer() {
        return new ActionPerformer() {
            public short getActionId() {
                return AbortAction.this.actionId;
            }

            public boolean action(final Action act, final Creature performer, final Creature target, final short action, final float counter) {
                return this.action(act, performer, null, target, action, counter);
            }

            public boolean action(final Action act, final Creature performer, final Item source, final Creature target, final short action, final float counter) {
                final int castTime = 100; // 10 seconds
                final String playerEffect = String.format("%sabortoffspring", performer.getName());
                if (!(performer instanceof Player) || target == null || !target.isAnimal() || !target.isPregnant()) {
                    return true;
                }
                //if (performer.getPower() < 1) {
                //    return true;
                //}
                final Skill animalHusbandry = performer.getSkills().getSkillOrLearn(SkillList.BREEDING);
                if (animalHusbandry.getKnowledge() < skillReq) {
                    performer.getCommunicator().sendNormalServerMessage("You need a bit more skill in animal husbandry for that...");
                    return true;
                }
                if (Cooldowns.isOnCooldown(playerEffect, cooldown)) {
                    performer.getCommunicator().sendNormalServerMessage(String.format("You will need to wait %s before you can do that again.", Cooldowns.timeRemaining(playerEffect, cooldown)));
                    return true;
                }
                try {
                    if (counter == 1f) {
                        performer.getCommunicator().sendNormalServerMessage("You close your eyes and focus your senses the offspring of " + target.getName() + "...");
                        performer.getCurrentAction().setTimeLeft(castTime);
                        performer.sendActionControl("Aborting offspring", true, castTime);
                        return false;
                    }
                    if (counter * 10f <= act.getTimeLeft()) {
                        return false;
                    }
                } catch (NoSuchActionException e) {
                    return true;
                }
                if (performer.getVillageId() <= 0 || !target.isBranded() || !target.isBrandedBy(performer.getVillageId())) {
                    performer.getCommunicator().sendNormalServerMessage(target.getName() + " is not branded by your settlement.");
                    return true;
                }
                CreaturesProxy.deleteOffspringSettings(target.getWurmId());
                MiscChanges.actionNotify(performer, String.format("You skillfully and swiftly remove the offspring of %s using an ancient, and secret technique.", target.getName()), String.format("%%NAME mumbles some incoherent phrases and %s seems healthier.", target.getName()), String.format("A shadowy form mumbles some incoherent phrases and %s seems healthier.", target.getName()));
                Cooldowns.setUsed(playerEffect);
                return true;
            }
        };
    }

}
