package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.*;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.*;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.Requiem;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import static org.gotti.wurmunlimited.modsupport.actions.ActionPropagation.*;

public class GemSmashAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final short actionId;
    private final ActionEntry actionEntry;

    public GemSmashAction() {
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Smash gem", "smashing", new int[]{ActionTypesProxy.ACTION_TYPE_NOMOVE}).build();
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
                source.getTemplateId() == ItemList.maulLarge &&
                (source.getMaterial() == Materials.MATERIAL_GLIMMERSTEEL ||
                        source.getMaterial() == Materials.MATERIAL_ADAMANTINE ||
                        source.getRarity() >= MiscConstants.RARE) &&
                source.getTopParent() == performer.getInventory().getWurmId() &&
                (target.getTopParent() == performer.getInventory().getWurmId() || target.getLastOwnerId() == performer.getWurmId())) {

            return isNormalGem(target.getTemplateId());
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
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {

        if (!canUse(performer, source, target)) {
            performer.getCommunicator().sendAlertServerMessage("You can't do that.");
            return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
        }

        if (target.getTemplateId() == ItemList.ruby) {
            try {
                Item parent = target.getParent();
                for (int i = 0; i < (1 + Server.rand.nextInt(9)); i++) {
                    parent.insertItem(ItemFactory.createItem(CustomItems.rubyFragment.getTemplateId(), target.getQualityLevel() * (Server.rand.nextFloat()), target.getRarity(), null), true);
                }
                parent.dropItem(target.getWurmId(), false);
                Items.destroyItem(target.getWurmId());
                if (performer.getSkills().getSkillOrLearn(11005).getKnowledge() <= 40) {
                    performer.getSkills().getSkillOrLearn(11005).skillCheck(1.0, 0.0, false, 1f);
                }
                performer.getCommunicator().sendNormalServerMessage(String.format("You smash the %s with the %s and it crumbles into several fragments.", target.getName(), source.getName()));
            } catch (FailedException | NoSuchTemplateException | NoSuchItemException e) {
                Requiem.logger.log(Level.SEVERE, (String.format("Error on %s: %s", GemSmashAction.class.getName(), e)));
            }
        } else if (target.getTemplateId() == ItemList.emerald) {
            try {
                Item parent = target.getParent();
                for (int i = 0; i < (1 + Server.rand.nextInt(9)); i++) {
                    parent.insertItem(ItemFactory.createItem(CustomItems.emeraldFragment.getTemplateId(), target.getQualityLevel() * (Server.rand.nextFloat()), target.getRarity(), null), true);
                }
                parent.dropItem(target.getWurmId(), false);
                Items.destroyItem(target.getWurmId());
                if (performer.getSkills().getSkillOrLearn(11005).getKnowledge() <= 40) {
                    performer.getSkills().getSkillOrLearn(11005).skillCheck(1.0, 0.0, false, 1f);
                }
                performer.getCommunicator().sendNormalServerMessage(String.format("You smash the %s with the %s and it crumbles into several fragments.", target.getName(), source.getName()));
            } catch (FailedException | NoSuchTemplateException | NoSuchItemException e) {
                Requiem.logger.log(Level.SEVERE, (String.format("Error on %s: %s", GemSmashAction.class.getName(), e)));
            }
        } else if (target.getTemplateId() == ItemList.sapphire) {
            try {
                Item parent = target.getParent();
                for (int i = 0; i < (1 + Server.rand.nextInt(9)); i++) {
                    parent.insertItem(ItemFactory.createItem(CustomItems.sapphireFragment.getTemplateId(), target.getQualityLevel() * (Server.rand.nextFloat()), target.getRarity(), null), true);
                }
                parent.dropItem(target.getWurmId(), false);
                Items.destroyItem(target.getWurmId());
                if (performer.getSkills().getSkillOrLearn(11005).getKnowledge() <= 40) {
                    performer.getSkills().getSkillOrLearn(11005).skillCheck(1.0, 0.0, false, 1f);
                }
                performer.getCommunicator().sendNormalServerMessage(String.format("You smash the %s with the %s and it crumbles into several fragments.", target.getName(), source.getName()));
            } catch (FailedException | NoSuchTemplateException | NoSuchItemException e) {
                Requiem.logger.log(Level.SEVERE, (String.format("Error on %s: %s", GemSmashAction.class.getName(), e)));
            }
        } else if (target.getTemplateId() == ItemList.opal) {
            try {
                Item parent = target.getParent();
                for (int i = 0; i < (1 + Server.rand.nextInt(9)); i++) {
                    parent.insertItem(ItemFactory.createItem(CustomItems.opalFragment.getTemplateId(), target.getQualityLevel() * (Server.rand.nextFloat()), target.getRarity(), null), true);
                }
                parent.dropItem(target.getWurmId(), false);
                Items.destroyItem(target.getWurmId());
                if (performer.getSkills().getSkillOrLearn(11005).getKnowledge() <= 40) {
                    performer.getSkills().getSkillOrLearn(11005).skillCheck(1.0, 0.0, false, 1f);
                }
                performer.getCommunicator().sendNormalServerMessage(String.format("You smash the %s with the %s and it crumbles into several fragments.", target.getName(), source.getName()));
            } catch (FailedException | NoSuchTemplateException | NoSuchItemException e) {
                Requiem.logger.log(Level.SEVERE, (String.format("Error on %s: %s", GemSmashAction.class.getName(), e)));
            }
        } else if (target.getTemplateId() == ItemList.diamond) {
            try {
                Item parent = target.getParent();
                for (int i = 0; i < (1 + Server.rand.nextInt(9)); i++) {
                    parent.insertItem(ItemFactory.createItem(CustomItems.diamondFragment.getTemplateId(), target.getQualityLevel() * (Server.rand.nextFloat()), target.getRarity(), null), true);
                }
                parent.dropItem(target.getWurmId(), false);
                Items.destroyItem(target.getWurmId());
                if (performer.getSkills().getSkillOrLearn(11005).getKnowledge() <= 40) {
                    performer.getSkills().getSkillOrLearn(11005).skillCheck(1.0, 0.0, false, 1f);
                }
                performer.getCommunicator().sendNormalServerMessage(String.format("You smash the %s with the %s and it crumbles into several fragments.", target.getName(), source.getName()));
            } catch (FailedException | NoSuchTemplateException | NoSuchItemException e) {
                Requiem.logger.log(Level.SEVERE, (String.format("Error on %s: %s", GemSmashAction.class.getName(), e)));
            }
        }

        return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);

    }

    private boolean isNormalGem(int itemTemplateID) {
        switch (itemTemplateID) {
            case ItemList.ruby:
            case ItemList.emerald:
            case ItemList.sapphire:
            case ItemList.opal:
            case ItemList.diamond:
                return true;
            default:
                return false;
        }
    }
}