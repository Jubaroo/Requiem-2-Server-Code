package org.jubaroo.mods.wurm.server.actions.machinaOfFortune;

import com.wurmonline.communication.MessageColors;
import com.wurmonline.server.*;
import com.wurmonline.server.behaviours.Methods;
import com.wurmonline.server.bodys.Wound;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.NoSuchCreatureException;
import com.wurmonline.server.economy.Change;
import com.wurmonline.server.economy.Economy;
import com.wurmonline.server.economy.MonetaryConstants;
import com.wurmonline.server.items.*;
import com.wurmonline.server.skills.SkillSystem;
import com.wurmonline.server.zones.NoSuchZoneException;
import com.wurmonline.server.zones.VirtualZone;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zones;
import com.wurmonline.shared.constants.SoundNames;
import com.wurmonline.shared.util.MulticolorLineSegment;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.tools.RandomUtils;
import org.jubaroo.mods.wurm.server.tools.RequiemTools;
import org.jubaroo.mods.wurm.server.tools.SpellTools;

import java.util.ArrayList;

class MachinaOfFortuneHelpers {

    //TODO redo the roll system because its stupid!

    public static void sendColoredNoFortune(final Communicator comm) {
        final ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
        segments.add(new MulticolorLineSegment("Your fortune is... ", MessageColors.COLOR_WHITE_ID));
        segments.add(new MulticolorLineSegment("nothing", MessageColors.COLOR_RED_ID));
        segments.add(new MulticolorLineSegment(". Better luck next time!", MessageColors.COLOR_WHITE_ID));
        comm.sendNormalServerMessage("The machine makes a strange noise and then stops. You notice a fortune appear.");
        comm.sendColoredMessageEvent(segments, (byte) 3);
    }

    public static void sendColoredNegativeFortune(final String msg, final Communicator comm) {
        final ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
        segments.add(new MulticolorLineSegment("Your fortune is... ", MessageColors.COLOR_WHITE_ID));
        segments.add(new MulticolorLineSegment(msg, MessageColors.COLOR_RED_ID));
        segments.add(new MulticolorLineSegment("!", MessageColors.COLOR_WHITE_ID));
        comm.sendNormalServerMessage("The machine makes a strange noise and then stops. You notice a fortune appear.");
        comm.sendColoredMessageEvent(segments, (byte) 3);
    }

    public static void sendColoredGenericFortune(final String msg, final Communicator comm) {
        final ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
        segments.add(new MulticolorLineSegment("Your fortune is... ", MessageColors.COLOR_WHITE_ID));
        segments.add(new MulticolorLineSegment(msg, MessageColors.COLOR_GREEN_ID));
        segments.add(new MulticolorLineSegment("!", MessageColors.COLOR_WHITE_ID));
        comm.sendNormalServerMessage("The machine makes a strange noise and then stops. You notice a fortune appear.");
        comm.sendColoredMessageEvent(segments, (byte) 3);
    }

    public static void sendColoredSingleItemFortune(Item item, Communicator comm) {
        final ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
        segments.add(new MulticolorLineSegment(String.format("Your fortune is... %s ", RequiemTools.a_an(item.getName())), MessageColors.COLOR_WHITE_ID));
        if (item.getTemplateId() == CustomItems.essenceOfWoodId || item.getTemplateId() == ItemList.handMirror || item.isCoin()) {
            segments.add(new MulticolorLineSegment(String.format("%s (%s)", item.getName(), Materials.convertMaterialByteIntoString(item.getMaterial())), MessageColors.COLOR_GREEN_ID));
        } else {
            segments.add(new MulticolorLineSegment(item.getName(), MessageColors.COLOR_GREEN_ID));
        }
        segments.add(new MulticolorLineSegment("!", MessageColors.COLOR_WHITE_ID));
        comm.sendNormalServerMessage("The machine makes a strange noise and then stops. You notice a fortune appear.");
        comm.sendColoredMessageEvent(segments, (byte) 3);
    }

    public static void sendColoredMultipleItemFortune(Item item, Communicator comm) {
        final ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
        segments.add(new MulticolorLineSegment("Your fortune is... some ", MessageColors.COLOR_WHITE_ID));
        segments.add(new MulticolorLineSegment(String.format("%s's", item.getName()), MessageColors.COLOR_GREEN_ID));
        segments.add(new MulticolorLineSegment("!", MessageColors.COLOR_WHITE_ID));
        comm.sendNormalServerMessage("The machine makes a strange noise and then stops. You notice a fortune appear.");
        comm.sendColoredMessageEvent(segments, (byte) 3);
    }

    public static void sendColoredMultipleCoinFortune(int coinAmount, Communicator comm) {
        final Change newch = Economy.getEconomy().getChangeFor(coinAmount);
        final ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
        segments.add(new MulticolorLineSegment("Your fortune is... ", MessageColors.COLOR_WHITE_ID));
        segments.add(new MulticolorLineSegment(String.format("%s", newch.getChangeString()), MessageColors.COLOR_GREEN_ID));
        segments.add(new MulticolorLineSegment("!", MessageColors.COLOR_WHITE_ID));
        comm.sendNormalServerMessage("The machine makes a strange noise and then stops. You notice a fortune appear.");
        comm.sendColoredMessageEvent(segments, (byte) 3);
    }

    public static void sendColoredMultipleSkillFortune(String skillString, Communicator comm) {
        final ArrayList<MulticolorLineSegment> segments = new ArrayList<>();
        segments.add(new MulticolorLineSegment("Your fortune is... ", MessageColors.COLOR_WHITE_ID));
        segments.add(new MulticolorLineSegment(String.format("%s ", skillString), MessageColors.COLOR_FROST_ID));
        segments.add(new MulticolorLineSegment("skill points", MessageColors.COLOR_GREEN_ID));
        segments.add(new MulticolorLineSegment("!", MessageColors.COLOR_WHITE_ID));
        comm.sendNormalServerMessage("The machine makes a strange noise and then stops. You notice a fortune appear.");
        comm.sendColoredMessageEvent(segments, (byte) 3);
    }

    // ==============================================================================================================================

    public static void noFortune(Communicator comm, Item source) {
        sendColoredNoFortune(comm);
        Items.destroyItem(source.getWurmId());
    }

    public static void painFortune(Creature performer, Communicator comm, Item source) {
        Methods.sendSound(performer, "sound.magicTurret.attack");
        VolaTile vt = Zones.getOrCreateTile(source.getTilePos(), source.isOnSurface());
        for (VirtualZone vz : vt.getWatchers()) {
            if (vz.getWatcher().isPlayer() && vz.getWatcher().hasLink())
                vz.getWatcher().getCommunicator().sendAddEffect(source.getWurmId() + 2, source.getWurmId(), (short) 27, source.getPosX(), source.getPosY(), source.getPosZ(), (byte) 0, "karmaFireball", 10f, 0f);
        }
        SpellTools.addMagicDamage(null, performer, 1 + Server.rand.nextInt(33), 10000, Wound.TYPE_BURN, false);
        sendColoredNegativeFortune("PAIN", comm);
        Items.destroyItem(source.getWurmId());
    }

    // ==============================================================================================================================
    public static void singleItemOneRollInsert(int itemId, float minQ, float maxQ, byte material, byte rarity, float weightMultiplier, Item source, Creature performer, Communicator comm) throws NoSuchTemplateException, FailedException {
        Item item = ItemFactory.createItem(itemId, (float) RandomUtils.generateRandomDoubleInRange(minQ, maxQ), material, rarity, null);
        item.setWeight((int) (item.getWeightGrams() * weightMultiplier), true);
        performer.getInventory().insertItem(item, true);
        sendColoredSingleItemFortune(item, comm);
        Items.destroyItem(source.getWurmId());
        Methods.sendSound(performer, SoundNames.HUMM_SND);
    }

    public static void singleItemOneRollInsert(int itemId, float minQ, float maxQ, byte rarity, float weightMultiplier, Item source, Creature performer, Communicator comm) throws NoSuchTemplateException, FailedException {
        Item item = ItemFactory.createItem(itemId, (float) RandomUtils.generateRandomDoubleInRange(minQ, maxQ), rarity, null);
        item.setWeight((int) (item.getWeightGrams() * weightMultiplier), true);
        performer.getInventory().insertItem(item, true);
        sendColoredSingleItemFortune(item, comm);
        Items.destroyItem(source.getWurmId());
        Methods.sendSound(performer, SoundNames.HUMM_SND);
    }

    public static void singleItemOneRollInsert(int itemId, float minQ, float maxQ, float weightMultiplier, Item source, Creature performer, Communicator comm) throws NoSuchTemplateException, FailedException {
        Item item = ItemFactory.createItem(itemId, (float) RandomUtils.generateRandomDoubleInRange(minQ, maxQ), null);
        item.setWeight((int) (item.getWeightGrams() * weightMultiplier), true);
        performer.getInventory().insertItem(item, true);
        sendColoredSingleItemFortune(item, comm);
        Items.destroyItem(source.getWurmId());
        Methods.sendSound(performer, SoundNames.HUMM_SND);
    }

    public static void singleItemOneRollInsert(int itemId, float minQ, float maxQ, Item source, Creature performer, Communicator comm) throws NoSuchTemplateException, FailedException {
        Item item = ItemFactory.createItem(itemId, (float) RandomUtils.generateRandomDoubleInRange(minQ, maxQ), null);
        performer.getInventory().insertItem(item, true);
        sendColoredSingleItemFortune(item, comm);
        Items.destroyItem(source.getWurmId());
        Methods.sendSound(performer, SoundNames.HUMM_SND);
    }

    public static void singleItemOneRollInsert(int itemId, float quality, Item source, Creature performer, Communicator comm) throws NoSuchTemplateException, FailedException {
        Item item = ItemFactory.createItem(itemId, quality, null);
        performer.getInventory().insertItem(item, true);
        sendColoredSingleItemFortune(item, comm);
        Items.destroyItem(source.getWurmId());
        Methods.sendSound(performer, SoundNames.HUMM_SND);
    }

    // ==============================================================================================================================

    public static void multipleItemOneRollInsert(int maxItemCount, int itemId, float minQ, float maxQ, byte material, byte rarity, float weightMultiplier, Item source, Creature performer, Communicator comm) throws NoSuchTemplateException, FailedException {
        Item item = ItemFactory.createItem(itemId, (float) RandomUtils.generateRandomDoubleInRange(minQ, maxQ), material, rarity, null);
        for (int i = 0; i < Server.rand.nextInt(maxItemCount) + 1; i++) {
            item.setWeight((int) (item.getWeightGrams() * weightMultiplier), true);
            performer.getInventory().insertItem(item, true);
        }
        Methods.sendSound(performer, SoundNames.HUMM_SND);
        sendColoredMultipleItemFortune(item, comm);
        Items.destroyItem(source.getWurmId());
    }

    public static void multipleItemOneRollInsert(int maxItemCount, int itemId, float minQ, float maxQ, byte rarity, float weightMultiplier, Item source, Creature performer, Communicator comm) throws NoSuchTemplateException, FailedException {
        Item item = ItemFactory.createItem(itemId, (float) RandomUtils.generateRandomDoubleInRange(minQ, maxQ), rarity, null);
        for (int i = 0; i < Server.rand.nextInt(maxItemCount) + 1; i++) {
            item.setWeight((int) (item.getWeightGrams() * weightMultiplier), true);
            performer.getInventory().insertItem(item, true);
        }
        Methods.sendSound(performer, SoundNames.HUMM_SND);
        sendColoredMultipleItemFortune(item, comm);
        Items.destroyItem(source.getWurmId());
    }

    public static void multipleItemOneRollInsert(int maxItemCount, int itemId, float minQ, float maxQ, float weightMultiplier, Item source, Creature performer, Communicator comm) throws NoSuchTemplateException, FailedException {
        Item item = ItemFactory.createItem(itemId, (float) RandomUtils.generateRandomDoubleInRange(minQ, maxQ), null);
        for (int i = 0; i < Server.rand.nextInt(maxItemCount) + 1; i++) {
            item.setWeight((int) (item.getWeightGrams() * weightMultiplier), true);
            performer.getInventory().insertItem(item, true);
        }
        Methods.sendSound(performer, SoundNames.HUMM_SND);
        sendColoredMultipleItemFortune(item, comm);
        Items.destroyItem(source.getWurmId());
    }

    public static void multipleItemOneRollInsert(int maxItemCount, int itemId, float minQ, float maxQ, Item source, Creature performer, Communicator comm) throws NoSuchTemplateException, FailedException {
        Item item = ItemFactory.createItem(itemId, (float) RandomUtils.generateRandomDoubleInRange(minQ, maxQ), null);
        for (int i = 0; i < Server.rand.nextInt(maxItemCount) + 1; i++) {
            performer.getInventory().insertItem(item, true);
        }
        Methods.sendSound(performer, SoundNames.HUMM_SND);
        sendColoredMultipleItemFortune(item, comm);
        Items.destroyItem(source.getWurmId());
    }

    public static void multipleItemOneRollInsert(int maxItemCount, int itemId, float quality, Item source, Creature performer, Communicator comm) throws NoSuchTemplateException, FailedException {
        Item item = ItemFactory.createItem(itemId, quality, null);
        for (int i = 0; i < Server.rand.nextInt(maxItemCount) + 1; i++) {
            performer.getInventory().insertItem(item, true);
        }
        Methods.sendSound(performer, SoundNames.HUMM_SND);
        sendColoredMultipleItemFortune(item, comm);
        Items.destroyItem(source.getWurmId());
    }

    // ==============================================================================================================================

    public static void multipleItemTwoRollInsert(int maxSecondRoll, float rollMultiplier, int maxItemCount, int itemId, float minQ, float maxQ, Item source, Creature performer, Communicator comm) throws NoSuchTemplateException, FailedException {
        if (rollMultiplier * Server.rand.nextInt(maxSecondRoll - 1) == 0) {
            Item item = ItemFactory.createItem(itemId, (float) RandomUtils.generateRandomDoubleInRange(minQ, maxQ), null);
            for (int i = 0; i < Server.rand.nextInt(maxItemCount) + 1; i++) {
                performer.getInventory().insertItem(item, true);
            }
            Methods.sendSound(performer, SoundNames.HUMM_SND);
            sendColoredMultipleItemFortune(item, comm);
            Items.destroyItem(source.getWurmId());
        } else {
            noFortune(comm, source);
        }
    }

    // ==============================================================================================================================

    public static void singleItemTwoRollContainerInsert(int maxSecondRoll, float rollMultiplier, int containerId, int resourceId, float minQContainer, float maxQContainer, float minQResource, float maxQResource, int resourceWeightInGrams, Item source, Creature performer, Communicator comm) throws NoSuchTemplateException, FailedException {
        try {
            if (rollMultiplier * Server.rand.nextInt(maxSecondRoll - 1) == 0) {
                Item container = ItemFactory.createItem(containerId, (float) RandomUtils.generateRandomDoubleInRange(minQContainer, maxQContainer), null);
                Item filler = ItemFactory.createItem(resourceId, (float) RandomUtils.generateRandomDoubleInRange(minQResource, maxQResource), null);
                filler.setWeight(resourceWeightInGrams, true);
                container.insertItem(filler, true);
                if (container.getWeightGrams() > 20000) {
                    container.putItemInfrontof(performer, 0f);
                } else {
                    performer.getInventory().insertItem(container, true);
                }
                sendColoredSingleItemFortune(container, comm);
                Items.destroyItem(source.getWurmId());
                Methods.sendSound(performer, SoundNames.HUMM_SND);
            } else {
                noFortune(comm, source);
            }
        } catch (NoSuchCreatureException | NoSuchItemException | NoSuchPlayerException | NoSuchZoneException e) {
            RequiemLogging.logException("[Error] in singleItemTwoRollContainerInsert in MachinaOfFortunesHelpers", e);
        }
    }

    public static void multipleItemTwoRollContainerInsert(int maxSecondRoll, float rollMultiplier, int itemCount, int containerId, int resourceId, float minQContainer, float maxQContainer, float minQResource, float maxQResource, Item source, Creature performer, Communicator comm) throws NoSuchTemplateException, FailedException {
        try {
            if (rollMultiplier * Server.rand.nextInt(maxSecondRoll - 1) == 0) {
                Item container = ItemFactory.createItem(containerId, (float) RandomUtils.generateRandomDoubleInRange(minQContainer, maxQContainer), null);
                for (int i = 0; i < itemCount; i++) {
                    container.insertItem(ItemFactory.createItem(resourceId, (float) RandomUtils.generateRandomDoubleInRange(minQResource, maxQResource), null), true);
                }
                if (container.getWeightGrams() > 20000) {
                    container.putItemInfrontof(performer, 0f);
                } else {
                    performer.getInventory().insertItem(container, true);
                }
                sendColoredSingleItemFortune(container, comm);
                Items.destroyItem(source.getWurmId());
                Methods.sendSound(performer, SoundNames.HUMM_SND);
            } else {
                noFortune(comm, source);
            }
        } catch (NoSuchCreatureException | NoSuchItemException | NoSuchPlayerException | NoSuchZoneException e) {
            RequiemLogging.logException("[Error] in multipleItemTwoRollContainerInsert in MachinaOfFortunesHelpers", e);
        }
    }

    // ==============================================================================================================================

    public static void multipleSilverCoinInsert(int maxSecondRoll, float rollMultiplier, int coinAmount, Item source, Creature performer, Communicator comm) {
        int money = MonetaryConstants.COIN_SILVER * coinAmount;
        if (rollMultiplier * Server.rand.nextInt(maxSecondRoll - 1) == 0) {
            Item[] coins = Economy.getEconomy().getCoinsFor(money);
            for (Item coin : coins) {
                performer.getInventory().insertItem(coin, true);
            }
            sendColoredMultipleCoinFortune(money, comm);
            Items.destroyItem(source.getWurmId());
            Methods.sendSound(performer, SoundNames.HUMM_SND);
        } else {
            noFortune(comm, source);
        }
    }

    // ==============================================================================================================================
    public static void singleItemTwoRollInsert(int maxSecondRoll, float rollMultiplier, int itemId, float minQ, float maxQ, Item source, Creature performer, Communicator comm) throws NoSuchTemplateException, FailedException {
        if (rollMultiplier * Server.rand.nextInt(maxSecondRoll - 1) == 0) {
            Item item = ItemFactory.createItem(itemId, (float) RandomUtils.generateRandomDoubleInRange(minQ, maxQ), null);
            performer.getInventory().insertItem(item, true);
            sendColoredSingleItemFortune(item, comm);
            Items.destroyItem(source.getWurmId());
            Methods.sendSound(performer, SoundNames.HUMM_SND);
        } else {
            noFortune(comm, source);
        }
    }

    public static void singleItemTwoRollInsert(int maxSecondRoll, float rollMultiplier, int itemId, float minQ, float maxQ, byte rarity, float weightMultiplier, Item source, Creature performer, Communicator comm) throws NoSuchTemplateException, FailedException {
        if (rollMultiplier * Server.rand.nextInt(maxSecondRoll - 1) == 0) {
            Item item = ItemFactory.createItem(itemId, (float) RandomUtils.generateRandomDoubleInRange(minQ, maxQ), rarity, null);
            item.setWeight((int) (item.getWeightGrams() * weightMultiplier), true);
            performer.getInventory().insertItem(item, true);
            sendColoredSingleItemFortune(item, comm);
            Items.destroyItem(source.getWurmId());
            Methods.sendSound(performer, SoundNames.HUMM_SND);
        } else {
            noFortune(comm, source);
        }
    }

    public static void singleItemTwoRollInsert(int maxSecondRoll, float rollMultiplier, int itemId, float minQ, float maxQ, float weightMultiplier, Item source, Creature performer, Communicator comm) throws NoSuchTemplateException, FailedException {
        if (rollMultiplier * Server.rand.nextInt(maxSecondRoll - 1) == 0) {
            Item item = ItemFactory.createItem(itemId, (float) RandomUtils.generateRandomDoubleInRange(minQ, maxQ), null);
            item.setWeight((int) (item.getWeightGrams() * weightMultiplier), true);
            performer.getInventory().insertItem(item, true);
            sendColoredSingleItemFortune(item, comm);
            Items.destroyItem(source.getWurmId());
            Methods.sendSound(performer, SoundNames.HUMM_SND);
        } else {
            noFortune(comm, source);
        }
    }

    public static void singleItemTwoRollInsert(int maxSecondRoll, float rollMultiplier, int itemId, float quality, Item source, Creature performer, Communicator comm) throws NoSuchTemplateException, FailedException {
        if (rollMultiplier * Server.rand.nextInt(maxSecondRoll - 1) == 0) {
            Item item = ItemFactory.createItem(itemId, quality, null);
            performer.getInventory().insertItem(item, true);
            sendColoredSingleItemFortune(item, comm);
            Items.destroyItem(source.getWurmId());
            Methods.sendSound(performer, SoundNames.HUMM_SND);
        } else {
            noFortune(comm, source);
        }
    }

    public static void singleItemTwoRollInsert(int maxSecondRoll, float rollMultiplier, int itemId, float minQ, float maxQ, byte material, byte rarity, Item source, Creature performer, Communicator comm) throws NoSuchTemplateException, FailedException {
        if (rollMultiplier * Server.rand.nextInt(maxSecondRoll - 1) == 0) {
            Item item = ItemFactory.createItem(itemId, (float) RandomUtils.generateRandomDoubleInRange(minQ, maxQ), material, rarity, null);
            performer.getInventory().insertItem(item, true);
            sendColoredSingleItemFortune(item, comm);
            Items.destroyItem(source.getWurmId());
            Methods.sendSound(performer, SoundNames.HUMM_SND);
        } else {
            noFortune(comm, source);
        }
    }

    // ==============================================================================================================================

    public static void singleItemTwoRollDataChangeInsert(int maxSecondRoll, float rollMultiplier, int itemId, float quality, int data1, int data2, byte auxData, Item source, Creature performer, Communicator comm) throws NoSuchTemplateException, FailedException {
        if (rollMultiplier * Server.rand.nextInt(maxSecondRoll - 1) == 0) {
            final Item item = ItemFactory.createItem(itemId, quality, null);
            if (data1 != -1) {
                item.setData1(data1);
            }
            if (data2 != -1) {
                item.setData2(data2);
            }
            if (auxData != -1) {
                item.setAuxData(auxData);
            }
            performer.getInventory().insertItem(item);
            sendColoredSingleItemFortune(item, comm);
            Items.destroyItem(source.getWurmId());
            Methods.sendSound(performer, SoundNames.HUMM_SND);
        } else {
            noFortune(comm, source);
        }
    }

    // ==============================================================================================================================

    public static void addRandomSkillTwoRoll(int maxSecondRoll, float rollMultiplier, int skillNumber, Item source, Creature performer, Communicator comm) {
        if (rollMultiplier * Server.rand.nextInt(maxSecondRoll - 1) == 0) {
            String skillString = SkillSystem.getNameFor(skillNumber);
            performer.getSkills().getSkillOrLearn(skillNumber).skillCheck(1.0, 0.0, false, 1f);
            sendColoredMultipleSkillFortune(skillString, comm);
            Items.destroyItem(source.getWurmId());
            Methods.sendSound(performer, SoundNames.HUMM_SND);
        } else {
            noFortune(comm, source);
        }
    }

}
