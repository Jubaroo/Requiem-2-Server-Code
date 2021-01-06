package org.jubaroo.mods.wurm.server.actions.special.machinaOfFortune;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Server;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.NoSuchTemplateException;
import com.wurmonline.server.skills.SkillList;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.items.ItemTools;
import org.jubaroo.mods.wurm.server.tools.RandomUtils;

import static org.jubaroo.mods.wurm.server.actions.special.machinaOfFortune.MachinaOfFortuneHelpers.singleItemTwoRollInsert;
import static org.jubaroo.mods.wurm.server.tools.RequiemTools.*;


class CoinInsertion {
    private static final int ROLL_0 = 0;
    private static final int ROLL_1 = 1;
    private static final int ROLL_2 = 2;
    private static final int ROLL_3 = 3;
    private static final int ROLL_4 = 4;
    private static final int ROLL_5 = 5;
    private static final int ROLL_6 = 6;
    private static final int ROLL_7 = 7;
    private static final int ROLL_8 = 8;
    private static final int ROLL_9 = 9;
    private static final int ROLL_10 = 10;
    private static final int ROLL_11 = 11;
    private static final int ROLL_12 = 12;
    private static final int ROLL_13 = 13;
    private static final int ROLL_14 = 14;
    private static final int ROLL_15 = 15;
    private static final int ROLL_16 = 16;
    private static final int ROLL_17 = 17;
    private static final int ROLL_18 = 18;
    private static final int ROLL_19 = 19;
    private static final int ROLL_20 = 20;
    private static final int ROLL_21 = 21;
    private static final int ROLL_22 = 22;
    private static final int ROLL_23 = 23;
    private static final int ROLL_24 = 24;
    private static final int ROLL_25 = 25;
    private static final int ROLL_26 = 26;
    private static final int ROLL_27 = 27;
    private static final int ROLL_28 = 28;
    private static final int ROLL_29 = 29;
    private static final int ROLL_30 = 30;
    private static final int ROLL_31 = 31;
    private static final int ROLL_32 = 32;
    private static final int ROLL_33 = 33;
    private static final int ROLL_34 = 34;
    private static final int ROLL_35 = 35;
    private static final int ROLL_36 = 36;
    private static final int ROLL_37 = 37;
    private static final int ROLL_38 = 38;
    private static final int ROLL_39 = 39;
    private static final int ROLL_40 = 40;
    private static final int ROLL_41 = 41;
    private static final int ROLL_42 = 42;
    private static final int ROLL_43 = 43;
    private static final int ROLL_44 = 44;
    private static final int ROLL_45 = 45;
    private static final int ROLL_46 = 46;
    private static final int ROLL_47 = 47;
    private static final int ROLL_48 = 48;
    private static final int ROLL_49 = 49;
    private static final int ROLL_50 = 50;
    private static final int ROLL_51 = 51;
    private static final int ROLL_52 = 52;
    private static final int ROLL_53 = 53;
    private static final int ROLL_54 = 54;
    private static final int ROLL_55 = 55;
    private static final int ROLL_56 = 56;
    private static final int ROLL_57 = 57;
    private static final int ROLL_58 = 58;
    private static final int ROLL_59 = 59;
    private static final int ROLL_60 = 60;

    static void coinInsert(Creature performer, Communicator comm, Item source, float rollMultiplier) {
        try {
            switch (Server.rand.nextInt(61)) {
                case ROLL_0:
                default:
                    MachinaOfFortuneHelpers.noFortune(comm, source);
                    break;
                case ROLL_1:
                    MachinaOfFortuneHelpers.singleItemOneRollInsert(ItemList.coinIron, 1f, 100f, source, performer, comm);
                    break;
                case ROLL_2:
                    MachinaOfFortuneHelpers.multipleItemOneRollInsert(5, ItemList.eggSmall, 1f, 100f, source, performer, comm);
                    break;
                case ROLL_3:
                    MachinaOfFortuneHelpers.painFortune(performer, comm, source);
                    break;
                case ROLL_4:
                    MachinaOfFortuneHelpers.multipleItemOneRollInsert(5, ItemList.eye, 1f, 100f, source, performer, comm);
                    break;
                case ROLL_5:
                    singleItemTwoRollInsert(60, rollMultiplier, ItemList.sleepPowder, 100f, source, performer, comm);
                    break;
                case ROLL_6:
                    MachinaOfFortuneHelpers.multipleSilverCoinInsert(1000, rollMultiplier, 50, source, performer, comm);
                    break;
                case ROLL_7:
                    singleItemTwoRollInsert(150, rollMultiplier, ItemList.adamantineBar, 100f, source, performer, comm);
                    break;
                case ROLL_8:
                    singleItemTwoRollInsert(150, rollMultiplier, ItemList.glimmerSteelBar, 100f, source, performer, comm);
                    break;
                case ROLL_9:
                    singleItemTwoRollInsert(100, rollMultiplier, ItemList.riftCrystal, 100f, source, performer, comm);
                    break;
                case ROLL_10:
                    singleItemTwoRollInsert(100, rollMultiplier, ItemList.riftWood, 100f, source, performer, comm);
                    break;
                case ROLL_11:
                    singleItemTwoRollInsert(100, rollMultiplier, ItemList.riftStone, 100f, source, performer, comm);
                    break;
                case ROLL_12:
                    singleItemTwoRollInsert(20, rollMultiplier, ItemList.fireworks, 1f, 100f, source, performer, comm);
                    break;
                case ROLL_13:
                    MachinaOfFortuneHelpers.multipleItemOneRollInsert(10, RandomUtils.randomFishTemplates(), 1f, 100f, source, performer, comm);
                    break;
                case ROLL_14:
                    singleItemTwoRollInsert(50, rollMultiplier, RandomUtils.randomGem(false), 10f, 100f, source, performer, comm);
                    break;
                case ROLL_15:
                    MachinaOfFortuneHelpers.addRandomSkillTwoRoll(200, rollMultiplier, getRandArrayInt(SkillList.skillArray), source, performer, comm);
                    break;
                case ROLL_16:
                    if (rollMultiplier * Server.rand.nextInt(100 - 1) == 0) {
                        Item item = ItemTools.createEnchantOrb((float) generateRandomDoubleInRange(1f, 100f));
                        if (item != null) {
                            performer.getInventory().insertItem(item);
                            MachinaOfFortuneHelpers.sendColoredSingleItemFortune(item, comm);
                        } else
                            MachinaOfFortuneHelpers.noFortune(comm, source);
                    } else {
                        MachinaOfFortuneHelpers.noFortune(comm, source);
                    }
                    break;
                case ROLL_17:
                    singleItemTwoRollInsert(100, rollMultiplier, RandomUtils.randomMineDoorTemplates(), 1f, 100f, source, performer, comm);
                    break;
                case ROLL_18:
                    MachinaOfFortuneHelpers.singleItemOneRollInsert(RandomUtils.randomFlowerTemplates(), 1f, 100f, source, performer, comm);
                    break;
                case ROLL_19:
                    singleItemTwoRollInsert(10, rollMultiplier, ItemList.steak, 1f, 100f, (float) generateRandomDoubleInRange(1f, 5f), source, performer, comm);
                    break;
                case ROLL_20:
                    singleItemTwoRollInsert(100, rollMultiplier, ItemList.teleportationTwig, 100f, source, performer, comm);
                    break;
                case ROLL_21:
                    singleItemTwoRollInsert(100, rollMultiplier, ItemList.teleportationStone, 100f, source, performer, comm);
                    break;
                case ROLL_22:
                    singleItemTwoRollInsert(50, rollMultiplier, ItemList.joists, 1f, 100f, source, performer, comm);
                    break;
                case ROLL_23:
                    singleItemTwoRollInsert(1500, rollMultiplier, 10500/*Server Portal*/, 100f, source, performer, comm);
                    break;
                case ROLL_24:
                    singleItemTwoRollInsert(40, rollMultiplier, Server.rand.nextBoolean() ? ItemList.gardenGnomeGreen : ItemList.gardenGnome, 1f, 100f, source, performer, comm);
                    break;
                case ROLL_25:
                    singleItemTwoRollInsert(1250, rollMultiplier, ItemList.chestNoDecaySmall, 100f, source, performer, comm);
                    break;
                case ROLL_26:
                    singleItemTwoRollInsert(1250, rollMultiplier, ItemList.chestNoDecayLarge, 100f, source, performer, comm);
                    break;
                case ROLL_27:
                    MachinaOfFortuneHelpers.singleItemTwoRollContainerInsert(75, rollMultiplier, ItemList.flaskPottery, ItemList.potionTransmutation, 1f, 100f, 1f, 100f, 250, source, performer, comm);
                    break;
                case ROLL_28:
                    singleItemTwoRollInsert(100, rollMultiplier, ItemList.tuningFork, 1f, 100f, source, performer, comm);
                    break;
                case ROLL_29:
                    if (rollMultiplier * Server.rand.nextInt(150 - 1) == 0) {
                        int karma = getRandomIntegerInRange(100, 300);
                        performer.setKarma(performer.getKarma() + karma);
                        MachinaOfFortuneHelpers.sendColoredGenericFortune(String.format("%d karma points", karma), comm);
                    } else {
                        MachinaOfFortuneHelpers.noFortune(comm, source);
                    }
                    break;
                case ROLL_30:
                    singleItemTwoRollInsert(100, rollMultiplier, ItemList.handMirror, 100f, source, performer, comm);
                    break;
                case ROLL_31:
                    singleItemTwoRollInsert(150, rollMultiplier, ItemList.goldenMirror, 100f, source, performer, comm);
                    break;
                case ROLL_32:
                    singleItemTwoRollInsert(100, rollMultiplier, RandomUtils.randomPotionTemplates(), 1f, 100f, source, performer, comm);
                    break;
                case ROLL_33:
                    singleItemTwoRollInsert(200, rollMultiplier, ItemList.drakeHide, 1f, 100f, (float) generateRandomDoubleInRange(1f, 2f), source, performer, comm);
                    break;
                case ROLL_34:
                    singleItemTwoRollInsert(150, rollMultiplier, RandomUtils.randomGem(true), 1f, 100f, source, performer, comm);
                    break;
                case ROLL_35:
                    singleItemTwoRollInsert(70, rollMultiplier, ItemList.coinCopper, 1f, 100f, source, performer, comm);
                    break;
                case ROLL_36:
                    singleItemTwoRollInsert(40, rollMultiplier, ItemList.coinIronTwenty, 1f, 100f, source, performer, comm);
                    break;
                case ROLL_37:
                    singleItemTwoRollInsert(200, rollMultiplier, ItemList.wandSculpting, 100f, source, performer, comm);
                    break;
                case ROLL_38:
                    singleItemTwoRollInsert(50, rollMultiplier, ItemList.shakerOrb, 100f, source, performer, comm);
                    break;
                case ROLL_39:
                    singleItemTwoRollInsert(200, rollMultiplier, ItemList.rodTransmutation, 100f, source, performer, comm);
                    break;
                case ROLL_40:
                    singleItemTwoRollInsert(75, rollMultiplier, ItemList.resurrectionStone, 100f, source, performer, comm);
                    break;
                case ROLL_41:
                    singleItemTwoRollInsert(25, rollMultiplier, CustomItems.essenceOfWoodId, 1f, 100f, RandomUtils.randomWoodMaterialIds(), MiscConstants.COMMON, source, performer, comm);
                    break;
                case ROLL_42:
                    singleItemTwoRollInsert(150, rollMultiplier, CustomItems.treasureBoxId, 10f, 100f, ItemTools.randomRarity(), 1f, source, performer, comm);
                    break;
                case ROLL_43:
                    singleItemTwoRollInsert(150, rollMultiplier, CustomItems.affinityOrbId, 100f, source, performer, comm);
                    break;
                case ROLL_44:
                    singleItemTwoRollInsert(50, rollMultiplier, CustomItems.gemCache.getTemplateId(), 100f, source, performer, comm);
                    break;
                case ROLL_45:
                    singleItemTwoRollInsert(50, rollMultiplier, CustomItems.moonCache.getTemplateId(), 100f, source, performer, comm);
                    break;
                case ROLL_46:
                    singleItemTwoRollInsert(50, rollMultiplier, CustomItems.potionCache.getTemplateId(), 100f, source, performer, comm);
                    break;
                case ROLL_47:
                    singleItemTwoRollInsert(50, rollMultiplier, CustomItems.treasureMapCache.getTemplateId(), 1f, 100f, source, performer, comm);
                    break;
                case ROLL_48:
                    singleItemTwoRollInsert(10, rollMultiplier, CustomItems.heraldicCertificateId, 100f, source, performer, comm);
                    break;
                case ROLL_49:
                    singleItemTwoRollInsert(1500, rollMultiplier, CustomItems.scrollOfTownPortal.getTemplateId(), 100f, source, performer, comm);
                    break;
                case ROLL_50:
                    MachinaOfFortuneHelpers.singleItemTwoRollDataChangeInsert(100, rollMultiplier, ItemList.blood, 100f, -1, 3 + Server.rand.nextInt(115), (byte) -1, source, performer, comm);
                    break;
                case ROLL_51:
                    MachinaOfFortuneHelpers.multipleItemOneRollInsert(10, ItemList.baitWurm, 1f, 100f, source, performer, comm);
                    break;
                case ROLL_52:
                    singleItemTwoRollInsert(50, rollMultiplier, RandomUtils.randomMaskTemplates(), 1f, 100f, source, performer, comm);
                    break;
                case ROLL_53:
                    singleItemTwoRollInsert(50, rollMultiplier, RandomUtils.randomHorseEquipmentTemplates(), 1f, 100f, source, performer, comm);
                    break;
                case ROLL_54:
                    if (rollMultiplier * Server.rand.nextInt(100 - 1) == 0) {
                        Item item = ItemFactory.createItem(RandomUtils.randomRuneMaterialIds(), (float) generateRandomDoubleInRange(10f, 100f), "");
                        item.setMaterial(RandomUtils.randomRuneMaterialIds());
                        item.setRealTemplate(RandomUtils.randomRiftItemTemplates());
                        performer.getInventory().insertItem(item, true);
                        MachinaOfFortuneHelpers.sendColoredSingleItemFortune(item, comm);
                    } else {
                        MachinaOfFortuneHelpers.noFortune(comm, source);
                    }
                    break;
                case ROLL_55:
                    MachinaOfFortuneHelpers.multipleItemOneRollInsert(10, ItemList.charcoal, 1f, 100f, source, performer, comm);
                    break;
                case ROLL_56:
                    MachinaOfFortuneHelpers.multipleItemTwoRollInsert(10, rollMultiplier, 20, ItemList.catseye, 1f, 100f, source, performer, comm);
                    break;
                case ROLL_57:
                    MachinaOfFortuneHelpers.multipleItemTwoRollContainerInsert(150, rollMultiplier, 250, ItemList.crateSmall, ItemList.dirtPile, 1f, 100f, 1f, 100f, source, performer, comm);
                    break;
                case ROLL_58:
                    MachinaOfFortuneHelpers.multipleItemTwoRollContainerInsert(150, rollMultiplier, 250, ItemList.crateSmall, ItemList.sand, 1f, 100f, 1f, 100f, source, performer, comm);
                    break;
                case ROLL_59:
                    MachinaOfFortuneHelpers.multipleItemTwoRollContainerInsert(250, rollMultiplier, 250, ItemList.crateSmall, ItemList.concrete, 1f, 100f, 1f, 100f, source, performer, comm);
                    break;
                case ROLL_60:
                    MachinaOfFortuneHelpers.multipleItemTwoRollContainerInsert(150, rollMultiplier, 250, ItemList.crateSmall, ItemList.log, 1f, 100f, 1f, 100f, source, performer, comm);
                    break;
            }
        } catch (NoSuchTemplateException | FailedException e) {
            e.printStackTrace();
            comm.sendNormalServerMessage(String.format("Machina Of Fortune action error: %s", e.toString()));
            comm.sendNormalServerMessage("Please inform a GM by opening a ticket (/support) and provide the message above. Thank you.");
        }
    }

}
