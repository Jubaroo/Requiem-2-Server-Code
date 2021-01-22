package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.Items;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.skills.SkillList;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.Requiem;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class CreateAshAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final short actionId;
    private final ActionEntry actionEntry;

    public CreateAshAction() {
        // Get the action id
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Create ash", "burning", new int[]{
                ActionTypesProxy.ACTION_TYPE_NOMOVE,
                ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS,
                ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM
        }).range(8).build();
        // Register the action entry
        ModActions.registerAction(this.actionEntry);
    }

    @Override
    public short getActionId() {
        return this.actionId;
    }

    @Override
    public BehaviourProvider getBehaviourProvider() {
        return this;
    }

    @Override
    public ActionPerformer getActionPerformer() {
        return this;
    }

    private boolean canUse(Creature performer, Item source, Item target) {
        if (performer.isPlayer() && source != null && target != null &&
                source.getTemplateId() == ItemList.log &&
                source.getTopParent() == performer.getInventory().getWurmId() &&
                (target.getTopParent() != performer.getInventory().getWurmId())) {
            return target.isOnFire() && nonOilFire(target);
        } else return false;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
        if (canUse(performer, source, target))
            return Collections.singletonList(this.actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature player, Item source, Item target, short num, float counter) {
        try {
            int time = (int) (150 - (player.getSkills().getSkillOrLearn(SkillList.FIREMAKING).getKnowledge() / 2));
            Communicator comm = player.getCommunicator();

            if (nonOilFire(target)) {
                if (counter == 1f) {
                    player.getCurrentAction().setTimeLeft(time);
                    player.sendActionControl("burning", true, time);
                    comm.sendNormalServerMessage(String.format("You place the %s into the %s and it begins to burn.", source.getName(), target.getName()));
                } else {
                    if (counter * 10f > action.getTimeLeft()) {
                        if (source.getWeightGrams() >= 1 && source.getWeightGrams() <= 6000) {
                            player.getInventory().insertItem(ItemFactory.createItem(ItemList.ash, source.getQualityLevel() * Server.rand.nextFloat(), source.getRarity(), null), true);
                        } else if (source.getWeightGrams() >= 6001 && source.getWeightGrams() <= 12000) {
                            for (int i = 0; i < 2; i++) {
                                player.getInventory().insertItem(ItemFactory.createItem(ItemList.ash, source.getQualityLevel() * Server.rand.nextFloat(), source.getRarity(), null), true);
                            }
                        } else if (source.getWeightGrams() >= 12001 && source.getWeightGrams() <= 16000) {
                            for (int i = 0; i < 3; i++) {
                                player.getInventory().insertItem(ItemFactory.createItem(ItemList.ash, source.getQualityLevel() * Server.rand.nextFloat(), source.getRarity(), null), true);
                            }
                        } else if (source.getWeightGrams() >= 16001 && source.getWeightGrams() <= 24000) {
                            for (int i = 0; i < 4; i++) {
                                player.getInventory().insertItem(ItemFactory.createItem(ItemList.ash, source.getQualityLevel() * Server.rand.nextFloat(), source.getRarity(), null), true);
                            }
                        }
                        Items.destroyItem(source.getWurmId());
                        comm.sendNormalServerMessage(String.format("You collect the ash that remains from what used to be a %s.", source.getName()));
                        return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                    }
                }
            }
            return propagate(action, ActionPropagation.CONTINUE_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        } catch (Exception e) {
            Requiem.logger.log(Level.SEVERE, (String.format("Error on %s: %s", CreateAshAction.class.getName(), e)));
            return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        }
    }

    private static boolean nonOilFire(Item target) {
        int templateId = target.getTemplate().getTemplateId();
        if (templateId == ItemList.forge) {
            return true;
        } else if (templateId == ItemList.stoneOven) {
            return true;
        } else if (templateId == ItemList.kiln) {
            return true;
        } else if (templateId == ItemList.smelter) {
            return true;
        } else if (templateId == ItemList.campfire) {
            return true;
        } else return templateId == ItemList.openFireplace;
    }

}