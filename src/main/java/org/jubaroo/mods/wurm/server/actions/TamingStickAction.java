package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.Items;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.behaviours.Actions;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.DbCreatureStatus;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.players.PermissionsByPlayer;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.server.spells.Dominate;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.creatures.CreatureTweaks;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.tools.RequiemTools;

import java.util.Collections;
import java.util.List;

public class TamingStickAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final ActionEntry actionEntry;

    public TamingStickAction() {
        actionEntry = ActionEntry.createEntry((short) ModActions.getNextActionId(), "Control creature", "bending will", new int[]{
                ActionTypesProxy.ACTION_TYPE_NOMOVE
        });
        ModActions.registerAction(actionEntry);
    }

    @Override
    public short getActionId() {
        return actionEntry.getNumber();
    }

    @Override
    public BehaviourProvider getBehaviourProvider() {
        return this;
    }

    @Override
    public ActionPerformer getActionPerformer() {
        return this;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Creature creature) {
        if (performer instanceof Player && creature != null && (source.getTemplate().getTemplateId() == CustomItems.tamingStickId || source.getTemplate().getTemplateId() == ItemList.wandDeity))
            return Collections.singletonList(actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Creature creature, short num, float counter) {
        int timeLeft = (int) (50 - (performer.getSkills().getSkillOrLearn(SkillList.TAMEANIMAL).getKnowledge() / 3));
        try {
            if (!performer.isWithinDistanceTo(creature, 12.0f)) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You must be closer to the %s.", creature.getName()), (byte) 3);
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (source.getTopParentOrNull() != performer.getInventory()) {
                performer.getCommunicator().sendNormalServerMessage("You must have the stick in your inventory to use it.", (byte) 3);
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (performer.getPet() != null && DbCreatureStatus.getIsLoaded(performer.getPet().getWurmId()) == 1) {
                performer.getCommunicator().sendNormalServerMessage("You have a pet in a cage, remove it first, to dominate this one.", (byte) 3);
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (source.getTemplate().getTemplateId() != CustomItems.tamingStickId && source.getTemplate().getTemplateId() != ItemList.wandDeity) {
                performer.getCommunicator().sendNormalServerMessage("You cannot use a taming stick without a taming stick!", (byte) 3);
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (creature.isCaredFor((Player) performer) && creature.getCareTakerId() != performer.getWurmId()) {
                performer.getCommunicator().sendNormalServerMessage(String.format("The %s is cared for by someone else.", creature.getName()), (byte) 3);
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (creature.isBranded() && creature.getBrandVillage() != performer.getCitizenVillage()) {
                performer.getCommunicator().sendNormalServerMessage(String.format("The %s is branded by another village.", creature.getName()), (byte) 3);
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (performer.getCurrentVillage() != null && (!performer.getCurrentVillage().isActionAllowed(Actions.BRAND, performer) || !performer.getCurrentVillage().isActionAllowed(Actions.TAME, performer) || !performer.getCurrentVillage().isActionAllowed(Actions.ATTACK, performer) || !performer.getCurrentVillage().isActionAllowed(Actions.LEAD, performer))) {
                performer.getCommunicator().sendNormalServerMessage("The current village permissions do not allow that.", (byte) 3);
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (creature.isDominated() && creature.getDominator() != performer) {
                performer.getCommunicator().sendNormalServerMessage(String.format("The %s is dominated by someone else.", creature.getName()), (byte) 3);
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (creature.isDominated() && creature.getDominator() == performer) {
                performer.getCommunicator().sendNormalServerMessage(String.format("The %s is dominated by someone you already.", creature.getName()), (byte) 3);
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (creature.isUnique() || CreatureTweaks.isRareCreature(creature)) {
                performer.getCommunicator().sendNormalServerMessage(String.format("The %s is too strong.", creature.getName()), (byte) 3);
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (CreatureTweaks.isChristmasMob(creature) || CreatureTweaks.isHalloweenMob(creature)) {
                performer.getCommunicator().sendNormalServerMessage("You cannot control holiday creatures.", (byte) 3);
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (CreatureTweaks.isRequiemNPC(creature) || creature.isNpc() || creature.isTrader() || creature.isNpcTrader() || creature.isBartender() || creature.isGuide()) {
                performer.getCommunicator().sendNormalServerMessage("You cannot control NPC's.", (byte) 3);
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (creature.isPlayer()) {
                performer.getCommunicator().sendNormalServerMessage("You cannot control players.", (byte) 3);
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            //if (performer.hasPet()) {
            //    performer.getCommunicator().sendNormalServerMessage("You cannot control any more creatures.", (byte)3);
            //    return true;
            //}
            if (creature.isGuard()) {
                performer.getCommunicator().sendNormalServerMessage("You cannot control guards.", (byte) 3);
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (creature.isRidden()) {
                performer.getCommunicator().sendNormalServerMessage(String.format("%s is currently riding that creature. Dismount and try again.", PermissionsByPlayer.getPlayerOrGroupName(creature.getHitched().pilotId)), (byte) 3);
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (creature.isHitched()) {
                performer.getCommunicator().sendNormalServerMessage(String.format("That creature is currently hitched to the %s.", creature.getHitched().name), (byte) 3);
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (counter == 1f) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You pull the %s from your pocket and point it at the %s. The end of the stick begins to faintly glow...", source.getName(), creature.getName()), (byte) 3);
                Server.getInstance().broadCastAction(String.format("%s pulls %s %s from %s pocket and points it at the %s.", performer.getName(), RequiemTools.a_an(source.getName()), source.getName(), performer.getHisHerItsString(), creature.getName()), performer, 5);
                action.setTimeLeft(timeLeft);
                performer.sendActionControl("controlling", true, timeLeft);
            } else if (counter * 10f > action.getTimeLeft()) {
                Dominate.dominate(1000, performer, creature);
                if (source.getTemplate().getTemplateId() != ItemList.wandDeity) {
                    Items.destroyItem(source.getWurmId());
                }
                //performer.getCommunicator().sendNormalServerMessage(String.format("The %s is now fully loyal to you and will defend you until death if taken into battle.", creature.getName()));
                //Server.getInstance().broadCastAction(String.format("%s used %s %s to control the creature. The %s is now obeying %s", performer.getName(), performer.getHisHerItsString(), source.getName(), creature.getName(), performer.getHisHerItsString()), performer, 5);
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            return propagate(action, ActionPropagation.CONTINUE_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        } catch (Exception e) {
            e.printStackTrace();
            performer.getCommunicator().sendNormalServerMessage(String.format("Controlling Rod action error: %s", e.toString()), (byte) 3);
            performer.getCommunicator().sendNormalServerMessage("Please inform a GM by opening a ticket (/support) and provide the message above. Thank you.", (byte) 3);
            return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        }
    }

}




