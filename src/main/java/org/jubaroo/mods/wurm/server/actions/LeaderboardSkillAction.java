package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.questions.LeaderboardSkillQuestion;
import com.wurmonline.server.skills.Skill;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.util.Collections;
import java.util.List;

public class LeaderboardSkillAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public LeaderboardSkillAction() {
        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Open leaderboard",
                "opening",
                new int[]{0 /* ACTION_TYPE_NOMOVE */}    // 6 /* ACTION_TYPE_NOMOVE */, 48 /* ACTION_TYPE_ENEMY_ALWAYS */, 36 /* ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM */
        );
        ModActions.registerAction(actionEntry);
    }

    @Override
    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Skill skill) {
                if (performer instanceof Player && skill.getNumber() > 0) {
                    return Collections.singletonList(actionEntry);
                }

                return null;
            }

            // Never called
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Skill skill) {
                if (performer instanceof Player && skill.getNumber() > 0) {
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

            @Override
            public boolean action(Action act, Creature performer, Skill skill, short action, float counter) {
                LeaderboardSkillQuestion lbsq = new LeaderboardSkillQuestion(performer, "Leaderboard", skill.getName(), performer.getWurmId(), skill.getNumber());
                lbsq.sendQuestion();
                return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }

            // Without activated object
            @Override
            public boolean action(Action act, Creature performer, Item source, Skill skill, short action, float counter) {
                if (performer instanceof Player) {
                    this.action(act, performer, skill, action, counter);
                } else {
                    RequiemLogging.logInfo("Somehow a non-player opened a leaderboard...");
                }
                return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }

            @Override
            public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
                return this.action(act, performer, target, action, counter);
            }


        };
    }
}