package org.jubaroo.mods.wurm.server.actions.items;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.skills.Skill;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.server.skills.Skills;
import com.wurmonline.server.sounds.SoundPlayer;
import com.wurmonline.shared.constants.SoundNames;
import org.gotti.wurmunlimited.modsupport.actions.*;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateSnowballAction implements ModAction, BehaviourProvider, ActionPerformer {
    private static Logger logger;

    static {
        CreateSnowballAction.logger = Logger.getLogger(CreateSnowballAction.class.getName());
    }

    private final short actionId;
    private final ActionEntry actionEntry;

    public CreateSnowballAction() {
        this.actionId = (short) ModActions.getNextActionId();
        ModActions.registerAction(this.actionEntry = new ActionEntryBuilder(this.actionId, "Create a snowball", "creating a snowball", new int[]{
                ActionTypesProxy.ACTION_TYPE_NOMOVE,
                ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS,
                ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM
        }).build());
    }

    public List<ActionEntry> getBehavioursFor(final Creature performer, final Item object, final int tilex, final int tiley, final boolean onSurface, final int tile) {
        if (performer instanceof Player && object != null && object.getTemplateId() == ItemList.shovel && Tiles.decodeType(tile) == Tiles.Tile.TILE_SNOW.id) {
            return Collections.singletonList(actionEntry);
        }
        return null;
    }

    public short getActionId() {
        return this.actionId;
    }

    public boolean action(final Action action, final Creature performer, final Item source, final int tilex, final int tiley, final boolean onSurface, final int heightOffset, final int tile, final short num, final float counter) {
        try {
            final Skills skills = performer.getSkills();
            final Skill digging = skills.getSkillOrLearn(SkillList.DIGGING);
            float snowQuality = (float) Server.rand.nextInt((int) (performer.getSkills().getSkill(SkillList.DIGGING).getKnowledge(0.0) * 100f)) / 100f;
            final Item snowball = ItemFactory.createItem(ItemList.snowball, snowQuality, performer.getName());
            if (!performer.isWithinDistanceTo(tilex, tiley, (int) 6f)) {
                performer.getCommunicator().sendNormalServerMessage("You must be closer to the snow.");
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (Tiles.decodeType(tile) != Tiles.Tile.TILE_SNOW.id) {
                performer.getCommunicator().sendSafeServerMessage("There is no snow here.");
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (Tiles.decodeType(tile) == Tiles.Tile.TILE_SNOW.id) {
                if (counter == 1f) {
                    String sstring = SoundNames.DIGGING1_SND;
                    final int sound = Server.rand.nextInt(2);
                    if (sound == 0) {
                        sstring = SoundNames.DIGGING2_SND;
                    } else {
                        sstring = SoundNames.DIGGING3_SND;
                    }
                    SoundPlayer.playSound(sstring, performer, 0f);
                    performer.playAnimation("dig", false);
                    performer.getCommunicator().sendNormalServerMessage("You start to dig up snow and form it into a ball.");
                    performer.getCurrentAction().setTimeLeft(60);
                    performer.sendActionControl("Digging up snow and forming into a ball", true, 60);
                } else {
                    int time;
                    time = performer.getCurrentAction().getTimeLeft();
                    if (counter * 10f > time) {
                        digging.skillCheck(40.0, source, 0.0, false, counter);
                        if (!performer.getInventory().mayCreatureInsertItem()) {
                            snowball.setWeight(100, true);
                            snowball.putItemInfrontof(performer);
                            performer.getStatus().modifyStamina(-50000f);
                            source.setDamage(source.getDamage() + 0.0005f * source.getDamageModifier());
                            performer.getSkills().getSkillOrLearn(SkillList.DIGGING);
                            performer.getCommunicator().sendNormalServerMessage("Your inventory is full so you put the snowball on the ground in front of you.");
                            Server.getInstance().broadCastAction(performer.getName() + " creates a snowball from a shovelful of snow.", performer, 10);
                            return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        } else {
                            snowball.setWeight(100, true);
                            performer.getInventory().insertItem(snowball);
                            performer.getStatus().modifyStamina(-50000f);
                            source.setDamage(source.getDamage() + 0.0005f * source.getDamageModifier());
                            performer.getSkills().getSkillOrLearn(SkillList.DIGGING);
                            performer.getCommunicator().sendNormalServerMessage("You create a snowball in your hands with the snow from the shovel.");
                            Server.getInstance().broadCastAction(performer.getName() + " creates a snowball from a shovelful of snow.", performer, 10);
                            return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                    }
                }
            }
            return propagate(action, ActionPropagation.CONTINUE_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        } catch (Exception e) {
            CreateSnowballAction.logger.log(Level.WARNING, e.getMessage(), e);
            return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        }
    }

}
