package org.jubaroo.mods.wurm.server.actions.special.events;

import com.wurmonline.server.*;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.economy.Economy;
import com.wurmonline.server.economy.MonetaryConstants;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.NoSuchTemplateException;
import com.wurmonline.server.players.Titles;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.server.skills.SkillSystem;
import com.wurmonline.server.tutorial.MissionPerformed;
import com.wurmonline.server.tutorial.MissionPerformer;
import com.wurmonline.shared.constants.EffectConstants;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.communication.discord.DiscordHandler;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.misc.database.holidays.Holidays;
import org.jubaroo.mods.wurm.server.misc.CustomTitles;
import org.jubaroo.mods.wurm.server.tools.RandomUtils;
import org.jubaroo.mods.wurm.server.utils.Cooldowns;

import java.util.Collections;
import java.util.List;

import static org.gotti.wurmunlimited.modsupport.actions.ActionPropagation.*;
import static org.jubaroo.mods.wurm.server.server.constants.ToggleConstants.disableDiscordReliance;

public class TreasureHuntChestAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;
    // Mission Ids
    private static final int CaptainAvery = 1878;
    private static final int Tower = 1879;
    private static final int PirateScout = 1880;
    private static final int ZombieAttack = 1904;
    private static final int Tartarus = 1886;
    private static final int Digsite = 1881;
    private static final int DockWorker = 1887;
    private static final int NylanMine = 1889;
    private static final int MarzisLanding = 1888;
    private static final int Kingston = 1877;
    private static final int BattleCampAlpha = 1893;
    private static final int CaveInfo = 1903;
    private static final int CavePirateBoss = 1916;

    public TreasureHuntChestAction() {
        // Get the action id
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Open", "looting the chest", new int[]{ActionTypesProxy.ACTION_TYPE_NOMOVE}).range(8).build();
        // Register the action entry
        ModActions.registerAction(this.actionEntry);
    }

    public boolean canUse(Creature performer, Item target) {
        return performer.isPlayer() && target != null && target.getTemplateId() == CustomItems.treasureHuntChestId;
    }

    @Override
    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            // Menu with activated object
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
                return this.getBehavioursFor(performer, target);
            }

            // Menu without activated object
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
                if (canUse(performer, target)) {
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

            // Without activated object
            @Override
            public boolean action(Action act, Creature performer, Item target, short action, float counter) {
                try {
                    Communicator comm = performer.getCommunicator();
                    int sk = RandomUtils.getRandArrayInt(SkillList.skillArray);
                    String skillString = SkillSystem.getNameFor(sk);
                    final String playerEffect = String.format("%s%s", performer.getName(), TreasureHuntChestAction.class.getName());
                    long cooldown = TimeConstants.YEAR_MILLIS;

                    if (!Items.exists(target)) {
                        comm.sendNormalServerMessage("The treasure chest was here a second ago!?.");
                        return propagate(act, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
                    }
                    if (target.getTemplate().getTemplateId() != CustomItems.treasureHuntChestId) {
                        comm.sendNormalServerMessage("That is not a treasure chest.");
                        return propagate(act, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
                    }
                    if (!Servers.isThisATestServer()) {
                        if (performer.getPower() != 5) {
                            if (!allMissionsCompleted(performer)) {
                                performer.getCommunicator().sendNormalServerMessage("The chest will not open no matter what you try. Maybe you should check out Marnbourne for clues. (You must complete all parts of the treasure hunt before you can open the chest)");
                                return propagate(act, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
                            }
                            if (!Holidays.isRequiemTreasureHunt()) {
                                performer.getCommunicator().sendNormalServerMessage("The Treasure Hunt is not currently running.");
                                return propagate(act, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
                            }
                            if (Cooldowns.isOnCooldown(playerEffect, cooldown)) {
                                performer.getCommunicator().sendNormalServerMessage("The chest is empty, there is nothing left to take.");
                                return propagate(act, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
                            }
                        }
                    }
                    if (counter == 1f) {
                        performer.getCommunicator().sendNormalServerMessage("You begin to remove the lock and open the chest.");
                        Server.getInstance().broadCastAction(String.format("%s starts to open the %s", performer.getNameWithoutPrefixes(), target.getName()), performer, 5);
                        act.setTimeLeft(70);
                        performer.sendActionControl("Opening lock and looting the pirate chest", true, act.getTimeLeft());
                    } else if (counter * 10f > performer.getCurrentAction().getTimeLeft()) {
                        itemsInsert(performer);
                        performer.getSkills().getSkillOrLearn(sk).skillCheck(1.0, 0.0, false, 2f);
                        performer.addTitle(Titles.Title.getTitle(CustomTitles.TREASURE_HUNT));
                        if (performer.getPower() < 3 || !Servers.isThisATestServer()) {
                            Cooldowns.setUsed(playerEffect);
                        }
                        performer.getCommunicator().sendNormalServerMessage(String.format("You pry open the chest and look inside. You find that the chest contained 2 star gems, 3 rift stones, 3 rift crystals, 3 rift wood, 4 silver coins, 1 affinity orb, an old skull mask, 3 potions, 2 sleep powder, and some skill in %s", skillString));
                        Server.getInstance().broadCastAction(String.format("Opens the %s and claims the treasure!", target.getName()), performer, 10);
                        if (Holidays.isRequiemTreasureHunt()) {
                            Server.getInstance().broadCastNormal(String.format("%s has finished the Treasure Hunt and claimed the treasure! Congratulations to you!", performer.getNameWithoutPrefixes()), true);
                            if (!disableDiscordReliance)
                                DiscordHandler.sendToDiscord(CustomChannel.EVENTS, String.format("%s has finished the Treasure Hunt and claimed the treasure! :treasure_chest: Congratulations to you! :fireworks:", performer.getNameWithoutPrefixes()));
                        }
                        comm.sendAddEffect(target.getWurmId(), target.getWurmId(), EffectConstants.EFFECT_GENERIC, target.getPosX(), target.getPosY(), target.getPosZ(), (byte) 0, "FGDeathSparkle", 5, 0f);
                        Items.destroyItem(target.getWurmId());
                        //if (volaTile != null) {
                        //    volaTile.sendAnimation(performer, target, "close", false, false);
                        //}
                        return propagate(act, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
                    }
                    return propagate(act, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
                } catch (Exception e) {
                    e.printStackTrace();
                    return propagate(act, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
                }
            }

            @Override
            public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
                return this.action(act, performer, target, action, counter);
            }
        };
    }

    private static void coinInsert(Creature performer) {
        int money = MonetaryConstants.COIN_SILVER * 4;
        Item[] coins = Economy.getEconomy().getCoinsFor(money);
        for (Item coin : coins) {
            performer.getInventory().insertItem(coin, true);
        }
    }

    private static void itemsInsert(Creature performer) throws NoSuchTemplateException, FailedException {
        for (int i = 0; i < 2; i++) {
            performer.getInventory().insertItem(ItemFactory.createItem(RandomUtils.randomGem(true), (float) RandomUtils.generateRandomDoubleInRange(50, 99), null), true);
        }
        for (int i = 0; i < 3; i++) {
            performer.getInventory().insertItem(ItemFactory.createItem(ItemList.riftStone, (float) RandomUtils.generateRandomDoubleInRange(50, 99), null), true);
            performer.getInventory().insertItem(ItemFactory.createItem(ItemList.riftWood, (float) RandomUtils.generateRandomDoubleInRange(50, 99), null), true);
            performer.getInventory().insertItem(ItemFactory.createItem(ItemList.riftCrystal, (float) RandomUtils.generateRandomDoubleInRange(50, 99), null), true);
        }
        for (int i = 0; i < 3; i++) {
            performer.getInventory().insertItem(ItemFactory.createItem(RandomUtils.randomPotionTemplates(), 99f, null), true);
        }
        for (int i = 0; i < 2; i++) {
            performer.getInventory().insertItem(ItemFactory.createItem(ItemList.sleepPowder, 99f, null), true);
        }
        coinInsert(performer);
        performer.getInventory().insertItem(ItemFactory.createItem(CustomItems.affinityOrbId, 99f, null), true);
        performer.getInventory().insertItem(ItemFactory.createItem(CustomItems.skullMaskId, 99f, MiscConstants.RARE, "Tarbeard the Terrible"), true);
    }

    private static boolean allMissionsCompleted(Creature performer) {
        MissionPerformer missionPerformer = MissionPerformed.getMissionPerformer(performer.getWurmId());
        MissionPerformed one = missionPerformer.getMission(CaptainAvery);
        MissionPerformed two = missionPerformer.getMission(Tower);
        MissionPerformed thee = missionPerformer.getMission(PirateScout);
        MissionPerformed four = missionPerformer.getMission(ZombieAttack);
        MissionPerformed five = missionPerformer.getMission(Tartarus);
        MissionPerformed six = missionPerformer.getMission(Digsite);
        MissionPerformed seven = missionPerformer.getMission(DockWorker);
        MissionPerformed eight = missionPerformer.getMission(NylanMine);
        MissionPerformed nine = missionPerformer.getMission(MarzisLanding);
        MissionPerformed ten = missionPerformer.getMission(Kingston);
        MissionPerformed eleven = missionPerformer.getMission(BattleCampAlpha);
        MissionPerformed twelve = missionPerformer.getMission(CaveInfo);
        MissionPerformed thirteen = missionPerformer.getMission(CavePirateBoss);
        return one.isCompleted() && two.isCompleted() && thee.isCompleted() && four.isCompleted() && five.isCompleted() && six.isCompleted() && seven.isCompleted() && eight.isCompleted() && nine.isCompleted() && ten.isCompleted() && eleven.isCompleted() && twelve.isCompleted() && thirteen.isCompleted();
    }
}