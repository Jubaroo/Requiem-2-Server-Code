package org.jubaroo.mods.wurm.server.actions.special.events;

import com.wurmonline.server.Items;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Server;
import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.behaviours.Actions;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.CreatureTemplateIds;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.ItemTemplateFactory;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.shared.constants.CreatureTypes;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.tools.RequiemTools;
import org.jubaroo.mods.wurm.server.utils.Cooldowns;

import java.util.Collections;
import java.util.List;

public class SummoningStoneAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final short actionId;
    private final ActionEntry actionEntry;
    private static final int hand = ItemList.bodyHand;
    private static final int redDragonSummonItem = ItemList.rubyStar;
    private static final int blueDragonSummonItem = ItemList.sapphireStar;
    private static final int whiteDragonSummonItem = ItemList.diamondStar;
    private static final int greenDragonSummonItem = ItemList.emeraldStar;
    private static final int blackDragonSummonItem = ItemList.opalBlack;
    private static final int redDragonHatchlingSummonItem = ItemList.ruby;
    private static final int blueDragonHatchlingSummonItem = ItemList.sapphire;
    private static final int whiteDragonHatchlingSummonItem = ItemList.diamond;
    private static final int greenDragonHatchlingSummonItem = ItemList.emerald;
    private static final int blackDragonHatchlingSummonItem = ItemList.opal;
    private static final int trollKingSummonItem = 7520; // Fake Crown of Might from More Craft-ables mod - https://forum.wurmonline.com/index.php?/topic/144675-released-craft-more-things/
    private static final int goblinLeaderSummonItem = ItemList.coinCopperTwenty;
    private static final int kyklopsSummonItem = ItemList.haggis;
    private static final int forestGiantSummonItem = ItemList.hogRoast;
    private static final float redDragonSummonItemQuality = 10f;
    private static final float blueDragonSummonItemQuality = 10f;
    private static final float whiteDragonSummonItemQuality = 10f;
    private static final float greenDragonSummonItemQuality = 10f;
    private static final float blackDragonSummonItemQuality = 10f;
    private static final float redDragonHatchlingSummonItemQuality = 50f;
    private static final float blueDragonHatchlingSummonItemQuality = 50f;
    private static final float whiteDragonHatchlingSummonItemQuality = 50f;
    private static final float greenDragonHatchlingSummonItemQuality = 50f;
    private static final float blackDragonHatchlingSummonItemQuality = 50f;
    private static final float trollKingSummonItemQuality = 50f;
    private static final float goblinLeaderSummonItemQuality = 1f;
    private static final float kyklopsSummonItemQuality = 85f;
    private static final float forestGiantSummonItemQuality = 85f;

    public boolean canUse(Creature performer, Item source, Item target) {
        return performer.isPlayer() && source != null && target != null && target.getTemplateId() == CustomItems.summoningStoneId && isSummonItem(source.getTemplateId());
    }

    public SummoningStoneAction() {
        // Get the action id
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Activate stone", "summoning", new int[]{ActionTypesProxy.ACTION_TYPE_NOMOVE}).range(8).build();
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
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
        if (canUse(performer, source, target))
            return Collections.singletonList(this.actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {
        try {
            if (performer instanceof Player) {
                final Communicator comm = performer.getCommunicator();
                final long cooldown = TimeConstants.FIFTEEN_MINUTES_MILLIS / 3;
                final String cooldownString = SummoningStoneAction.class.getName();
                if (Cooldowns.isOnCooldown(cooldownString, cooldown)) {
                    performer.getCommunicator().sendNormalServerMessage(String.format("The %s cannot locate any unique creatures right now, you will need to wait %s before you can do that again.", target.getName(), Cooldowns.timeRemaining(cooldownString, cooldown)));
                    return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
                if (!canUse(performer, source, target)) {
                    comm.sendAlertServerMessage("Do you have the correct item activated and/or targeting the correct item/creature? Please try again or use /support to open a ticket with as much info as possible, and the staff will get to it as soon as possible.");
                    return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
                // generate spawn list
                if (source.getTemplateId() == ItemList.bodyHand) {
                    comm.sendNormalServerMessage("You hear whispers when you touch the stone with your hand...");
                    comm.sendNormalServerMessage("This stone can call forth powerful creatures from beyond the void. To summon these creatures, you must offer the desired item and begin the ritual.");
                    comm.sendServerMessage(String.format("Red Dragon's desire %s %s at %s quality.", RequiemTools.a_an_FromTemplateId(redDragonSummonItem), ItemTemplateFactory.getInstance().getTemplate(redDragonSummonItem).getName(), redDragonSummonItemQuality), 0, 255, 0);
                    comm.sendServerMessage(String.format("Blue Dragon's desire %s %s at %s quality.", RequiemTools.a_an_FromTemplateId(blueDragonSummonItem), ItemTemplateFactory.getInstance().getTemplate(blueDragonSummonItem).getName(), blueDragonSummonItemQuality), 0, 255, 0);
                    comm.sendServerMessage(String.format("White Dragon's desire %s %s at %s quality.", RequiemTools.a_an_FromTemplateId(whiteDragonSummonItem), ItemTemplateFactory.getInstance().getTemplate(whiteDragonSummonItem).getName(), whiteDragonSummonItemQuality), 0, 255, 0);
                    comm.sendServerMessage(String.format("Green Dragon's desire %s %s at %s quality.", RequiemTools.a_an_FromTemplateId(greenDragonSummonItem), ItemTemplateFactory.getInstance().getTemplate(greenDragonSummonItem).getName(), greenDragonSummonItemQuality), 0, 255, 0);
                    comm.sendServerMessage(String.format("Black Dragon's desire %s %s at %s quality.", RequiemTools.a_an_FromTemplateId(blackDragonSummonItem), ItemTemplateFactory.getInstance().getTemplate(blackDragonSummonItem).getName(), blackDragonSummonItemQuality), 0, 255, 0);
                    comm.sendServerMessage(String.format("Red Dragon Hatchling's desire %s %s at %s quality.", RequiemTools.a_an_FromTemplateId(redDragonHatchlingSummonItem), ItemTemplateFactory.getInstance().getTemplate(redDragonHatchlingSummonItem).getName(), redDragonHatchlingSummonItemQuality), 0, 255, 0);
                    comm.sendServerMessage(String.format("Blue Dragon Hatchling's desire %s %s at %s quality.", RequiemTools.a_an_FromTemplateId(blueDragonHatchlingSummonItem), ItemTemplateFactory.getInstance().getTemplate(blueDragonHatchlingSummonItem).getName(), blueDragonHatchlingSummonItemQuality), 0, 255, 0);
                    comm.sendServerMessage(String.format("White Dragon Hatchling's desire %s %s at %s quality.", RequiemTools.a_an_FromTemplateId(whiteDragonHatchlingSummonItem), ItemTemplateFactory.getInstance().getTemplate(whiteDragonHatchlingSummonItem).getName(), whiteDragonHatchlingSummonItemQuality), 0, 255, 0);
                    comm.sendServerMessage(String.format("Green Dragon Hatchling's desire %s %s at %s quality.", RequiemTools.a_an_FromTemplateId(greenDragonHatchlingSummonItem), ItemTemplateFactory.getInstance().getTemplate(greenDragonHatchlingSummonItem).getName(), greenDragonHatchlingSummonItemQuality), 0, 255, 0);
                    comm.sendServerMessage(String.format("Black Dragon Hatchling's desire %s %s at %s quality.", RequiemTools.a_an_FromTemplateId(blackDragonHatchlingSummonItem), ItemTemplateFactory.getInstance().getTemplate(blackDragonHatchlingSummonItem).getName(), blackDragonHatchlingSummonItemQuality), 0, 255, 0);
                    comm.sendServerMessage(String.format("Troll King's desire %s %s at %s quality.", RequiemTools.a_an_FromTemplateId(trollKingSummonItem), ItemTemplateFactory.getInstance().getTemplate(trollKingSummonItem).getName(), trollKingSummonItemQuality), 0, 255, 0);
                    comm.sendServerMessage(String.format("Goblin Leader's desire %s copper %s.", RequiemTools.a_an_FromTemplateId(goblinLeaderSummonItem), ItemTemplateFactory.getInstance().getTemplate(goblinLeaderSummonItem).getName()), 0, 255, 0);
                    comm.sendServerMessage(String.format("Kyklops's desire %s %s at %s quality.", RequiemTools.a_an_FromTemplateId(kyklopsSummonItem), ItemTemplateFactory.getInstance().getTemplate(kyklopsSummonItem).getName(), kyklopsSummonItemQuality), 0, 255, 0);
                    comm.sendServerMessage(String.format("Forest Giant's desire %s %s at %s quality.", RequiemTools.a_an_FromTemplateId(forestGiantSummonItem), ItemTemplateFactory.getInstance().getTemplate(forestGiantSummonItem).getName(), forestGiantSummonItemQuality), 0, 255, 0);
                    return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
                if (!itemIsOfValidQuality(source)) {
                    comm.sendNormalServerMessage(String.format("The %s is not high enough quality. Check the required quality level by using your hand on the %s.", source.getName(), target.getName()));
                    return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
                if (counter == 1f) {
                    comm.sendNormalServerMessage(String.format("You start to summon a %s from beyond the void.", getSummonedCreatureName(source.getTemplateId())));
                    Server.getInstance().broadCastAction(String.format("%s starts to summon a %s from beyond the void.", performer.getName(), getSummonedCreatureName(source.getTemplateId())), performer, 5);
                    performer.getCurrentAction().setTimeLeft(Actions.getSlowActionTime(performer, performer.getSkills().getSkill(SkillList.SOUL_DEPTH), source, 0));
                    performer.sendActionControl("Summoning", true, performer.getCurrentAction().getTimeLeft());
                } else if (counter * 10f > action.getTimeLeft()) {
                    //summon red dragon
                    summonArenaCreature(performer, source, redDragonSummonItem, CreatureTemplateIds.DRAGON_RED_CID, redDragonSummonItemQuality, cooldownString);
                    //summon blue dragon
                    summonArenaCreature(performer, source, blueDragonSummonItem, CreatureTemplateIds.DRAGON_BLUE_CID, blueDragonSummonItemQuality, cooldownString);
                    //summon white dragon
                    summonArenaCreature(performer, source, whiteDragonSummonItem, CreatureTemplateIds.DRAGON_WHITE_CID, whiteDragonSummonItemQuality, cooldownString);
                    //summon green dragon
                    summonArenaCreature(performer, source, greenDragonSummonItem, CreatureTemplateIds.DRAGON_GREEN_CID, greenDragonSummonItemQuality, cooldownString);
                    //summon black dragon
                    summonArenaCreature(performer, source, blackDragonSummonItem, CreatureTemplateIds.DRAGON_BLACK_CID, blackDragonSummonItemQuality, cooldownString);
                    //summon red dragon hatchling
                    summonArenaCreature(performer, source, redDragonHatchlingSummonItem, CreatureTemplateIds.DRAKE_RED_CID, redDragonHatchlingSummonItemQuality, cooldownString);
                    //summon blue dragon hatchling
                    summonArenaCreature(performer, source, blueDragonHatchlingSummonItem, CreatureTemplateIds.DRAKE_BLUE_CID, blueDragonHatchlingSummonItemQuality, cooldownString);
                    //summon white dragon hatchling
                    summonArenaCreature(performer, source, whiteDragonHatchlingSummonItem, CreatureTemplateIds.DRAKE_WHITE_CID, whiteDragonHatchlingSummonItemQuality, cooldownString);
                    //summon green dragon hatchling
                    summonArenaCreature(performer, source, greenDragonHatchlingSummonItem, CreatureTemplateIds.DRAKE_GREEN_CID, greenDragonHatchlingSummonItemQuality, cooldownString);
                    //summon black dragon hatchling
                    summonArenaCreature(performer, source, blackDragonHatchlingSummonItem, CreatureTemplateIds.DRAKE_BLACK_CID, blackDragonHatchlingSummonItemQuality, cooldownString);
                    //summon Troll King
                    summonArenaCreature(performer, source, trollKingSummonItem, CreatureTemplateIds.TROLL_KING_CID, trollKingSummonItemQuality, cooldownString);
                    //summon Goblin leader
                    summonArenaCreature(performer, source, goblinLeaderSummonItem, CreatureTemplateIds.GOBLIN_LEADER_CID, goblinLeaderSummonItemQuality, cooldownString);
                    //summon Kyklops
                    summonArenaCreature(performer, source, kyklopsSummonItem, CreatureTemplateIds.CYCLOPS_CID, kyklopsSummonItemQuality, cooldownString);
                    //summon Forest Giant
                    summonArenaCreature(performer, source, forestGiantSummonItem, CreatureTemplateIds.FOREST_GIANT_CID, forestGiantSummonItemQuality, cooldownString);
                    return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            performer.getCommunicator().sendNormalServerMessage(String.format("Summoning Stone action error: %s", e.toString()));
            performer.getCommunicator().sendNormalServerMessage("Please inform a GM by opening a ticket (/support) and provide the message above. Thank you.");
            return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        }
        return propagate(action, ActionPropagation.CONTINUE_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
    }

    private static String getSummonedCreatureName(int sourceId) {
        if (sourceId == redDragonSummonItem) {
            return "Red Dragon";
        } else if (sourceId == blueDragonSummonItem) {
            return "Blue Dragon";
        } else if (sourceId == whiteDragonSummonItem) {
            return "White Dragon";
        } else if (sourceId == greenDragonSummonItem) {
            return "Green Dragon";
        } else if (sourceId == blackDragonSummonItem) {
            return "Black Dragon";
        } else if (sourceId == redDragonHatchlingSummonItem) {
            return "Red Dragon Hatchling";
        } else if (sourceId == blueDragonHatchlingSummonItem) {
            return "Blue Dragon Hatchling";
        } else if (sourceId == whiteDragonHatchlingSummonItem) {
            return "White Dragon Hatchling";
        } else if (sourceId == greenDragonHatchlingSummonItem) {
            return "Green Dragon Hatchling";
        } else if (sourceId == blackDragonHatchlingSummonItem) {
            return "Black Dragon Hatchling";
        } else if (sourceId == trollKingSummonItem) {
            return "Troll King";
        } else if (sourceId == goblinLeaderSummonItem) {
            return "Goblin Leader";
        } else if (sourceId == kyklopsSummonItem) {
            return "Kyklops";
        } else if (sourceId == forestGiantSummonItem) {
            return "Forest Giant";
        }
        return "NO_VALID_CREATURE";
    }

    private static boolean itemIsOfValidQuality(Item source) {
        float qty = source.getCurrentQualityLevel();
        return qty >= redDragonSummonItemQuality || qty >= blueDragonSummonItemQuality || qty >= whiteDragonSummonItemQuality ||
                qty >= greenDragonSummonItemQuality || qty >= blackDragonSummonItemQuality || qty >= redDragonHatchlingSummonItemQuality ||
                qty >= blueDragonHatchlingSummonItemQuality || qty >= whiteDragonHatchlingSummonItemQuality || qty >= greenDragonHatchlingSummonItemQuality ||
                qty >= blackDragonHatchlingSummonItemQuality || qty >= trollKingSummonItemQuality || qty >= goblinLeaderSummonItemQuality ||
                qty >= kyklopsSummonItemQuality || qty >= forestGiantSummonItemQuality;
    }

    private boolean isSummonItem(int itemTemplateID) {
        switch (itemTemplateID) {
            case hand:
            case redDragonSummonItem:
            case blueDragonSummonItem:
            case whiteDragonSummonItem:
            case greenDragonSummonItem:
            case blackDragonSummonItem:
            case redDragonHatchlingSummonItem:
            case blueDragonHatchlingSummonItem:
            case whiteDragonHatchlingSummonItem:
            case greenDragonHatchlingSummonItem:
            case blackDragonHatchlingSummonItem:
            case trollKingSummonItem:
            case goblinLeaderSummonItem:
            case kyklopsSummonItem:
            case forestGiantSummonItem:
                return true;
            default:
                return false;
        }
    }

    private void summonArenaCreature(Creature performer, Item source, int summonItem, int creatureId, float summonItemQuality, String playerEffect) throws Exception {
        final float arenaX = 2830 * 4;
        final float arenaY = 3840 * 4;
        if (source.getTemplateId() == summonItem && source.getCurrentQualityLevel() >= summonItemQuality) {
            Cooldowns.setUsed(playerEffect);
            Items.destroyItem(source.getWurmId());
            final Creature creature = Creature.doNew(creatureId, false, arenaX, arenaY, 360f * Server.rand.nextFloat(), performer.getLayer(), "", MiscConstants.SEX_MALE, performer.getKingdomId(), CreatureTypes.C_MOD_NONE, false);
            //ChatTabs.sendLocalChat(performer, String.format("%s, I challenge YOU!", creature.getNameWithoutPrefixes()), 255, 120, 0);
            performer.getCommunicator().sendNormalServerMessage(String.format("You summon a %s from beyond the void.", creature.getNameWithoutPrefixes()));
            Server.getInstance().broadCastAction(String.format("%s summons a %s from beyond the void.", performer.getName(), creature.getNameWithoutPrefixes()), performer, 5);
        }
    }
}