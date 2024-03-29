package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.Items;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.behaviours.Actions;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.skills.Skill;
import com.wurmonline.server.skills.SkillList;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.misc.Crystals;

import java.util.Collections;
import java.util.List;

public class CrystalCombineAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public CrystalCombineAction() {
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(actionId, "Combine", "combining", new int[]{ActionTypesProxy.ACTION_TYPE_NOMOVE}).build();
        // Register the action entry
        ModActions.registerAction(actionEntry);
    }


    @Override
    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            // Menu with activated object
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item object) {
                if (performer instanceof Player && source != null && object != null && Crystals.isCrystal(source) && Crystals.isCrystal(object) && source != object && source.getTemplateId() == object.getTemplateId()) {
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

            // With activated object
            @Override
            public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
                try {
                    if (performer instanceof Player) {
                        if (Crystals.shouldCancelCombine(performer, source, target)) {
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        if (counter == 1f) {
                            performer.getCommunicator().sendNormalServerMessage("You begin to combine the crystals together.");
                            Server.getInstance().broadCastAction(performer.getName() + " begins combining crystals.", performer, 5);
                            Skill combineSkill = performer.getSkills().getSkill(SkillList.MIND_LOGICAL);
                            int time = Actions.getStandardActionTime(performer, combineSkill, source, 0d);
                            act.setTimeLeft(time);
                            performer.sendActionControl("Combining", true, act.getTimeLeft());
                        } else if (counter * 10f > performer.getCurrentAction().getTimeLeft()) {
                            double diff = (source.getCurrentQualityLevel() + target.getCurrentQualityLevel()) * 0.3d;
                            diff += source.getRarity() * 20;
                            diff -= performer.getSkills().getSkill(SkillList.MIND).getKnowledge();
                            double power = performer.getSkills().getSkill(SkillList.SOUL).skillCheck(diff, source, 0d, false, 1);
                            if (power > 0) {
                                performer.getCommunicator().sendNormalServerMessage("You successfully combine the crystals!");
                                Server.getInstance().broadCastAction(performer.getName() + " successfully combines the crystals!", performer, 5);
                                if (source.getCurrentQualityLevel() + target.getCurrentQualityLevel() > 100f) {
                                    float newQuality = (source.getCurrentQualityLevel() + target.getCurrentQualityLevel()) - 100f;
                                    performer.getCommunicator().sendNormalServerMessage("The crystals combine and change, creating a more powerful component.");
                                    Items.destroyItem(source.getWurmId());
                                    target.setQualityLevel(newQuality);
                                    target.setDamage(0);
                                    target.setRarity((byte) (target.getRarity() + 1));
                                } else {
                                    performer.getCommunicator().sendNormalServerMessage("The crystals combine together and reinforce.");
                                    target.setQualityLevel(source.getCurrentQualityLevel() + target.getCurrentQualityLevel());
                                    target.setDamage(0);
                                    Items.destroyItem(source.getWurmId());
                                }
                                return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                            } else {
                                performer.getCommunicator().sendNormalServerMessage("You fail to combine the crystals, damaging them both.");
                                float dam1 = (float) (Server.rand.nextFloat() * power * 0.2f);
                                float dam2 = (float) (Server.rand.nextFloat() * power * 0.2f);
                                source.setDamage(source.getDamage() - dam1);
                                target.setDamage(target.getDamage() - dam2);
                            }
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                    } else {
                        RequiemLogging.logWarning(String.format("Somehow a non-player activated a custom item (%d)...", source.getTemplateId()));
                    }
                    return propagate(act, ActionPropagation.CONTINUE_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                } catch (Exception e) {
                    RequiemLogging.logException("[Error] in action in CrystalCombineAction", e);
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
            }
        };
    }
}