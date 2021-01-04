package org.jubaroo.mods.wurm.server.actions.potions;

import com.wurmonline.server.Items;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zones;
import com.wurmonline.shared.constants.CreatureTypes;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.creatures.CreatureTools;
import org.jubaroo.mods.wurm.server.tools.RequiemTools;

import java.util.Collections;
import java.util.List;

public class DiseasePotionAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final short actionId;
    private final ActionEntry actionEntry;

    public DiseasePotionAction() {
        // Get the action id
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Cure disease", "summoning", new int[]{ActionTypesProxy.ACTION_TYPE_NOMOVE}).range(8).build();
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

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Creature creature) {
        if (performer instanceof Player && creature != null && source.getTemplate().getTemplateId() == CustomItems.diseasePotionId)
            return Collections.singletonList(this.actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Creature creature, short num, float counter) {
        int timeLeft = (int) (50 / (performer.getSkills().getSkillOrLearn(SkillList.ALCHEMY_NATURAL).getKnowledge() / 5) * 10);
        VolaTile tile = Zones.getTileOrNull(creature.getTilePos(), creature.isOnSurface());
        try {
            if (creature.getStatus().getModType() != CreatureTypes.C_MOD_DISEASED && creature.getDisease() == 0) {
                performer.getCommunicator().sendNormalServerMessage("There is no disease to cure.");
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (source.getTemplate().getTemplateId() != CustomItems.diseasePotionId) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You cannot use a %s without a %s!", source.getName(), source.getName()));
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (counter == 1f) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You pull the %s from your pocket and feed it to %s. The end of the wand begins to faintly glow...", source.getName(), creature.getName()));
                Server.getInstance().broadCastAction(String.format("%s pulls %s %s from %s pocket and points it at the %s.", performer.getName(), RequiemTools.a_an(source.getName()), source.getName(), performer.getHisHerItsString(), creature.getName()), performer, 5);
                action.setTimeLeft(timeLeft);
                performer.sendActionControl("curing", true, timeLeft);
            } else if (counter * 10f > action.getTimeLeft()) {
                if (creature.getDisease() > 0) {
                    creature.setDisease((byte) 0);
                }
                if (creature.getStatus().getModType() == CreatureTypes.C_MOD_DISEASED) {
                    CreatureTools.changeCreatureModtype(creature, CreatureTypes.C_MOD_NONE);
                }
                if (tile != null) {
                    tile.makeInvisible(creature);
                    tile.makeVisible(creature);
                }
                Items.destroyItem(source.getWurmId());
                performer.getCommunicator().sendNormalServerMessage(String.format("The %s is now cured of all diseases.", creature.getName()));
                Server.getInstance().broadCastAction(String.format("%s used %s %s on the creature. The %s is now free of diseases.", performer.getName(), performer.getHisHerItsString(), source.getName(), creature.getName()), performer, 5);
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            return propagate(action, ActionPropagation.CONTINUE_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        } catch (Exception e) {
            e.printStackTrace();
            performer.getCommunicator().sendNormalServerMessage(String.format("%s error: %s", DiseasePotionAction.class.getName(), e.toString()));
            performer.getCommunicator().sendNormalServerMessage("Please inform a GM by opening a ticket (/support) and provide the message above. Thank you.");
            return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        }
    }

}




