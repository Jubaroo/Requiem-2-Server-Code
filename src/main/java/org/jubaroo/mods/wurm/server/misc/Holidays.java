package org.jubaroo.mods.wurm.server.misc;

import com.wurmonline.server.EasterCalculator;
import com.wurmonline.server.FailedException;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.WurmCalendar;
import com.wurmonline.server.items.*;
import com.wurmonline.server.players.Player;
import com.wurmonline.shared.util.MaterialUtilities;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.server.constants.Constants;
import org.jubaroo.mods.wurm.server.tools.RandomUtils;
import org.jubaroo.mods.wurm.server.tools.database.DbHelper;

import java.time.Year;
import java.util.Calendar;

public class Holidays {
    public static String requiemAnniversary = "REQUIEMANNIVERSARY";
    public static String canadaDay = "REQUIEMCANADADAY";
    public static String christmas = "REQUIEMCHRISTMAS";
    public static String independenceDay = "REQUIEMINDEPENDENCEDAY";
    public static String newYear = "REQUIEMNEWYEAR";
    public static String stPatrickDay = "REQUIEMSTPATRICKDAY";
    public static String thanksgiving = "REQUIEMTHANKSGIVING";
    public static String valentinesDay = "REQUIEMVALENTINES";
    public static String victoriaDay = "REQUIEMVICTORIADAY";
    public static String mardiGras = "REQUIEMMARDIGRAS";
    public static String storeBededag = "REQUIEMSTOREBEDEDAG";
    public static String requiemHoliday = "REQUIEMHOLIDAY";
    public static String midsummer = "REQUIEMMIDSUMMER";
    public static String oktoberfest = "REQUIEMOKTOBERFEST";
    public static String easter = "REQUIEMEASTER";
    public static String test = "REQUIEMTEST";

    // ============>>>>>> Months start at 0 <<<<<<============

    public static boolean isRequiemTreasureHunt() { // Oct 31 - Nov 4 8am-8am
        return WurmCalendar.nowIsBetween(8, 0, 31, 9, Year.now().getValue(), 8, 0, 4, 10, Year.now().getValue());
    }

    public static boolean isRequiemNewYear() { // Jan 1 - Jan 14 @Midnight
        return WurmCalendar.nowIsBetween(0, 1, 1, 0, Year.now().getValue(), 23, 59, 14, 0, Year.now().getValue());
    }

    public static boolean isRequiemMardiGras() { // Ash Wednesday is always 46 days before Easter, and Fat Tuesday is always the day before Ash Wednesday.
        Calendar c = EasterCalculator.findHolyDay(Calendar.getInstance().get(Calendar.YEAR));
        return WurmCalendar.nowIsAfter(10, 0, c.get(Calendar.DATE) - 47, c.get(Calendar.MONTH), c.get(Calendar.YEAR));
    }

    public static boolean isRequiemValentinesDay() { // Feb 14
        return WurmCalendar.nowIsBetween(0, 1, 14, 1, Year.now().getValue(), 23, 59, 14, 1, Year.now().getValue());
    }

    // Store bededag, translated literally as Great Prayer Day or more loosely as General Prayer Day, "All Prayers" Day, Great Day of Prayers or Common Prayer Day, is a Danish holiday.
    public static boolean isRequiemStoreBededag() { //  Celebrated on the 4th Friday after Easter.
        return WurmCalendar.nowIsBetween(0, 1, 14, 1, Year.now().getValue(), 23, 59, 14, 1, Year.now().getValue());
    }

    public static boolean isRequiemStPatricksDay() { // March 17
        return WurmCalendar.nowIsBetween(0, 1, 17, 2, Year.now().getValue(), 23, 59, 17, 2, Year.now().getValue());
    }

    // 2019 date - Thursday, 21 March
    // 2020 date - Monday, 9 March
    // https://en.wikipedia.org/wiki/Holi
    public static boolean isRequiemHoliday() {
        return WurmCalendar.nowIsBetween(0, 1, 14, 1, Year.now().getValue(), 23, 59, 14, 1, Year.now().getValue());
    }

    //Victoria Day is a federal Canadian public holiday celebrated on the last Monday preceding May 25, in honour of Queen Victoria's birthday. As such, it is the Monday between the 18th to the 24th inclusive, and thus is always the penultimate Monday of May.
    public static boolean isRequiemVictoriaDay() { // may 24
        return WurmCalendar.nowIsBetween(0, 1, 24, 4, Year.now().getValue(), 23, 59, 20, 4, Year.now().getValue());
    }

    public static boolean isRequiemMidsummer() { // June 22
        return WurmCalendar.nowIsBetween(0, 1, 4, 6, Year.now().getValue(), 23, 59, 4, 6, Year.now().getValue());
    }

    // Canada Day is the national day of Canada. A federal statutory holiday, it celebrates the anniversary of July 1, 1867, the effective date of the Constitution Act, 1867 (then called the British North America Act, 1867), which united the three separate colonies of the Province of Canada, Nova Scotia, and New Brunswick into a single Dominion within the British Empire called Canada. Originally called Dominion Day (French: Le Jour de la Confédération), the holiday was renamed in 1982, the year the Canada Act was passed. Canada Day celebrations take place throughout the country, as well as in various locations around the world, attended by Canadians living abroad.
    public static boolean isRequiemCanadaDay() { // July 1
        return WurmCalendar.nowIsBetween(0, 1, 1, 6, Year.now().getValue(), 23, 59, 1, 6, Year.now().getValue());
    }

    public static boolean isRequiemIndependenceDay() { // July 4
        return WurmCalendar.nowIsBetween(0, 1, 4, 6, Year.now().getValue(), 23, 59, 4, 6, Year.now().getValue());
    }

    public static boolean isRequiemOktoberfest() { // Sept. 22 - Oct 7 @Midnight
        return WurmCalendar.nowIsBetween(0, 1, 4, 6, Year.now().getValue(), 23, 59, 4, 6, Year.now().getValue());
    }

    public static boolean isRequiemHalloween() { // Oct 23 - Nov 8 @Midnight
        return WurmCalendar.nowIsBetween(0, 1, 23, 9, Year.now().getValue(), 23, 59, 8, 10, Year.now().getValue());
    }

    public static boolean isRequiemThanksgiving() { // Nov 20 - Nov 30 @Midnight
        return WurmCalendar.nowIsBetween(0, 1, 20, 10, Year.now().getValue(), 23, 59, 30, 10, Year.now().getValue());
    }

    public static boolean isRequiemAnniversary() { // Dec 1 - Dec 13 @Midnight
        return WurmCalendar.nowIsBetween(0, 1, 1, 11, Year.now().getValue(), 23, 59, 13, 11, Year.now().getValue());
    }

    public static boolean isRequiemChristmas() { // Dec 20 - Dec 31 @Midnight
        return WurmCalendar.nowIsBetween(0, 1, 20, 11, Year.now().getValue(), 23, 59, 31, 11, Year.now().getValue());
    }

    public static void holidayLogin(Player p) {

        if (WurmCalendar.isEaster()) {
            String message = "Happy Easter! Be on the lookout for the Easter Bunny and its eggs!";
            p.getCommunicator().sendServerMessage(message, 0, 255, 0);
            if (DbHelper.canReceiveHolidayPresent(p, easter)) {
                try {
                    for (int i = 0; i < RandomUtils.getRandomIntegerInRange(4, 12); i++) {
                        p.getInventory().insertItem(ItemFactory.createItem(ItemList.valentines, 99f, Materials.MATERIAL_POTTERY, MaterialUtilities.COMMON, null), true);
                        p.getInventory().insertItem(ItemFactory.createItem(ItemList.sleepPowder, 99f, Materials.MATERIAL_WHEAT, MaterialUtilities.COMMON, null), true);
                        p.getCommunicator().sendServerMessage("A shadowy figure appears behind you and your pocket gets heavier. You quickly turn around to see what is going on only to find that nobody is there.", 255, 192, 203);
                    }
                } catch (FailedException | NoSuchTemplateException e) {
                    RequiemLogging.logException("Error in giveItems()", e);
                    throw new RuntimeException(e);
                }
            }
        }

        if (Holidays.isRequiemValentinesDay()) {
            String message = "Happy Valentines Day!";
            p.getCommunicator().sendServerMessage(message, 0, 255, 0);
            if (DbHelper.canReceiveHolidayPresent(p, valentinesDay)) {
                try {
                    p.getInventory().insertItem(ItemFactory.createItem(ItemList.valentines, 99f, Materials.MATERIAL_POTTERY, MaterialUtilities.COMMON, null), true);
                    p.getInventory().insertItem(ItemFactory.createItem(ItemList.sleepPowder, 99f, Materials.MATERIAL_WHEAT, MaterialUtilities.COMMON, null), true);
                    p.getCommunicator().sendServerMessage("A shadowy figure appears behind you and your pocket gets heavier. You quickly turn around to see what is going on only to find that nobody is there.", 255, 192, 203);
                } catch (FailedException | NoSuchTemplateException e) {
                    RequiemLogging.logException("Error in giveItems()", e);
                    throw new RuntimeException(e);
                }
            }
        }

        if (Holidays.isRequiemIndependenceDay()) {
            String message = "Happy Independence Day!";
            p.getCommunicator().sendServerMessage(message, 0, 255, 0);
            if (DbHelper.canReceiveHolidayPresent(p, independenceDay)) {
                try {
                    p.getInventory().insertItem(ItemFactory.createItem(ItemList.fireworks, 99f, Materials.MATERIAL_WOOD_BIRCH, MaterialUtilities.COMMON, null), true);
                    p.getInventory().insertItem(ItemFactory.createItem(ItemList.sleepPowder, 99f, Materials.MATERIAL_WHEAT, MaterialUtilities.COMMON, null), true);
                    p.getCommunicator().sendServerMessage("A shadowy figure appears behind you and your pocket gets heavier. You quickly turn around to see what is going on only to find that nobody is there.", 255, 192, 203);
                } catch (FailedException | NoSuchTemplateException e) {
                    RequiemLogging.logException("Error in giveItems()", e);
                    throw new RuntimeException(e);
                }
            }
        }

        if (Holidays.isRequiemCanadaDay()) {
            String message = "Happy Canada Day!";
            p.getCommunicator().sendServerMessage(message, 0, 255, 0);
            if (DbHelper.canReceiveHolidayPresent(p, canadaDay)) {
                try {
                    final Item mug = ItemFactory.createItem(ItemList.jarPottery, 90f, Materials.MATERIAL_POTTERY, MaterialUtilities.COMMON, null);
                    final Item mapleSyrup = ItemFactory.createItem(ItemList.syrupMaple, 99f, Materials.MATERIAL_WATER, MaterialUtilities.COMMON, null);
                    mapleSyrup.setWeight(2000, true);
                    mug.insertItem(mapleSyrup, true);
                    p.getInventory().insertItem(mug, true);
                    p.getInventory().insertItem(ItemFactory.createItem(ItemList.sleepPowder, 99f, Materials.MATERIAL_WHEAT, MaterialUtilities.COMMON, null), true);
                    p.getCommunicator().sendServerMessage("A shadowy figure appears behind you and your pocket gets heavier. You quickly turn around to see what is going on only to find that nobody is there.", 255, 192, 203);
                } catch (FailedException | NoSuchTemplateException e) {
                    RequiemLogging.logException("Error in giveItems()", e);
                    throw new RuntimeException(e);
                }
            }
        }

        if (Holidays.isRequiemVictoriaDay()) {
            String message = "Happy Victoria Day!";
            p.getCommunicator().sendServerMessage(message, 0, 255, 0);
            if (DbHelper.canReceiveHolidayPresent(p, victoriaDay)) {
                try {
                    final Item mug = ItemFactory.createItem(ItemList.beerSteinPottery, 90f, Materials.MATERIAL_POTTERY, MaterialUtilities.COMMON, null);
                    mug.setName("coffee mug");
                    mug.setDescription("I Love Victoria");
                    p.getInventory().insertItem(mug);
                    p.getInventory().insertItem(ItemFactory.createItem(ItemList.sleepPowder, 99f, Materials.MATERIAL_WHEAT, MaterialUtilities.COMMON, null), true);
                    p.getCommunicator().sendServerMessage("A shadowy figure appears behind you and your pocket gets heavier. You quickly turn around to see what is going on only to find that nobody is there.", 255, 192, 203);
                } catch (FailedException | NoSuchTemplateException e) {
                    RequiemLogging.logException("Error in giveItems()", e);
                    throw new RuntimeException(e);
                }
            }
        }

        if (Holidays.isRequiemThanksgiving()) {
            String message = "Happy Thanksgiving Day!";
            p.getCommunicator().sendServerMessage(message, 0, 255, 0);
            if (DbHelper.canReceiveHolidayPresent(p, thanksgiving)) {
                try {
                    final Item turkey = ItemFactory.createItem(ItemList.meat, 99f, Materials.MATERIAL_MEAT_FOWL, MaterialUtilities.COMMON, null);
                    turkey.setWeight(15000, true);
                    turkey.setName("Turkey");
                    p.getInventory().insertItem(turkey, true);
                    p.getInventory().insertItem(ItemFactory.createItem(ItemList.sleepPowder, 99f, Materials.MATERIAL_WHEAT, MaterialUtilities.COMMON, null), true);
                    p.getCommunicator().sendServerMessage("A shadowy figure appears behind you and your pocket gets heavier. You quickly turn around to see what is going on only to find that nobody is there.", 255, 192, 203);
                } catch (FailedException | NoSuchTemplateException e) {
                    RequiemLogging.logException("Error in giveItems()", e);
                    throw new RuntimeException(e);
                }
            }
        }

        if (Holidays.isRequiemHalloween()) {
            String message = "Happy Halloween! The creepy crawly creatures are out and about. Go out and find some Halloween fun, but don't eat too many sweets!";
            p.getCommunicator().sendServerMessage(message, 0, 255, 0);
        }

        if (Holidays.isRequiemAnniversary()) {
            String message = String.format("It is now Requiem of Wurm's %s anniversary! December 1st through December 14th, we are celebrating our %s year here at Requiem of Wurm. Thank you for your support and for choosing this server. See you next year!", Constants.formattedYear, Constants.formattedYear);
            p.getCommunicator().sendServerMessage(message, 0, 255, 0);
            if (DbHelper.canReceiveHolidayPresent(p, requiemAnniversary)) {
                try {
                    // Pouch
                    final Item pouch = ItemFactory.createItem(ItemList.satchel, 99f, MaterialUtilities.RARE, p.getName());
                    pouch.setName(String.format("Requiem's %s Anniversary satchel", Constants.formattedYear));
                    pouch.setDescription(String.format("Happy %s Anniversary! This satchel will never decay.", Constants.formattedYear));
                    pouch.setHasNoDecay(true);
                    pouch.setIsNoDrop(false);
                    pouch.setSizes(3, 10, 20);
                    pouch.setColor(WurmColor.createColor(210, 0, 0));
                    // Essence of wood
                    final int wood = CustomItems.essenceOfWoodId;
                    pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_LINGONBERRY, MaterialUtilities.COMMON, p.getName()), true);
                    pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_BLUEBERRY, MaterialUtilities.COMMON, p.getName()), true);
                    pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_RASPBERRY, MaterialUtilities.COMMON, p.getName()), true);
                    pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_APPLE, MaterialUtilities.COMMON, p.getName()), true);
                    pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_ORANGE, MaterialUtilities.COMMON, p.getName()), true);
                    pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_CHERRY, MaterialUtilities.COMMON, p.getName()), true);
                    pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_GRAPE, MaterialUtilities.COMMON, p.getName()), true);
                    pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_APPLE, MaterialUtilities.COMMON, p.getName()), true);
                    // Other items
                    pouch.insertItem(ItemFactory.createItem(ItemList.boneCollar, 99f, Materials.MATERIAL_BONE, MaterialUtilities.SUPREME, p.getName()), true);
                    pouch.insertItem(ItemFactory.createItem(CustomItems.gemCache.getTemplateId(), 99f, Materials.MATERIAL_GOLD, MaterialUtilities.COMMON, p.getName()), true);
                    // Champagne
                    final Item champagne = ItemFactory.createItem(CustomItems.champagneId, 99f, Materials.MATERIAL_GLASS, MaterialUtilities.RARE, p.getName());
                    champagne.setName(String.format("Requiem Vintage %d", Constants.currentYear));
                    pouch.insertItem(champagne, true);
                    // Note
                    final Item papyrus = ItemFactory.createItem(ItemList.papyrusSheet, 10f, MaterialUtilities.COMMON, p.getName());
                    final String str = String.format("\";maxlines=\"0\"}text{text=\"We want to thank you for logging into the server today.\n\nFrom December 1st through December 14th, we are celebrating Requiem of Wurm's %s birthday!\n\nPlayers like you are what help make us a great server to play on with a great community since 2016.\n\nPlease enjoy your time and some special items that Zeus made just for you. \n\nHave a great day, and see you around the server!\n\n \n", Constants.formattedYear);
                    papyrus.setInscription(str, "Requiem Staff");
                    pouch.insertItem(papyrus, true);
                    // Insert items into players inventory
                    p.getInventory().insertItem(ItemFactory.createItem(ItemList.fireworks, 99f, Materials.MATERIAL_WOOD_BIRCH, MaterialUtilities.COMMON, null), true);
                    p.getInventory().insertItem(pouch, true);
                    p.getCommunicator().sendServerMessage("Some items have been placed in your inventory to help celebrate our server. Thank you for playing.", 255, 192, 203);
                } catch (FailedException | NoSuchTemplateException e) {
                    RequiemLogging.logException("[Error] giving items for RequiemAnniversary", e);
                    throw new RuntimeException(e);
                }
            }
        }

        if (Holidays.isRequiemChristmas()) {
            String message = "Merry Christmas to you! Enjoy your presents from Santa and be careful that the grinch does not pay you a visit.";
            p.getCommunicator().sendServerMessage(message, 0, 255, 0);
            if (DbHelper.canReceiveHolidayPresent(p, christmas)) {
                try {
                    // Satchel
                    final Item satchel = ItemFactory.createItem(ItemList.satchel, 10f, MaterialUtilities.COMMON, null);
                    satchel.setColor(WurmColor.createColor(0, 200, 0));
                    // Heraldic Certificate
                    for (int i = 0; i < 10; ++i)
                        satchel.insertItem(ItemFactory.createItem(CustomItems.heraldicCertificateId, 99f, Materials.MATERIAL_PAPER, MaterialUtilities.COMMON, null), true);
                    // Gem cache
                    satchel.insertItem(ItemFactory.createItem(CustomItems.gemCache.getTemplateId(), 99f, Materials.MATERIAL_GOLD, MaterialUtilities.COMMON, null), true);
                    // Note
                    final Item papyrus = ItemFactory.createItem(ItemList.papyrusSheet, 10f, MaterialUtilities.COMMON, null);
                    final String str = "\";maxlines=\"0\"}text{text=\"Merry Christmas!.\n\nFrom December 20 to January 4 at midnight, we are celebrating Requiem Christmas by giving away special items.\n\nWishing you and your loved ones the best of peace, health, and prosperity in this joyful Christmas season.\n\nWe hope you have a great New Year also!\n\nSee you around the server :-)\n\n \n";
                    papyrus.setInscription(str, "Requiem Staff");
                    satchel.insertItem(papyrus, true);
                    // Insert items into players inventory
                    final Item lantern = ItemFactory.createItem(ItemList.snowLantern, 99f, MaterialUtilities.RARE, "Santa Claus");
                    p.getInventory().insertItem(ItemFactory.createItem(ItemList.sleepPowder, 99f, Materials.MATERIAL_WHEAT, MaterialUtilities.COMMON, null), true);
                    lantern.setHasNoDecay(true);
                    lantern.setDescription("Christmas" + Constants.currentYear);
                    lantern.setAuxData((byte) 127);
                    satchel.insertItem(lantern);
                    p.getInventory().insertItem(satchel, true);
                    p.getCommunicator().sendServerMessage("A shadowy figure appears behind you and you start to feel your pockets getting heavier. You quickly turn around to see what is going on only to find that nobody is there. As you wonder what could have happened just now, you pick up the smell of fresh baked cookies and hear the merry laughter of a christmas goblin as you notice something fading into the distance.", 255, 192, 203);
                } catch (FailedException | NoSuchTemplateException e) {
                    RequiemLogging.logException("Error in giveItems()", e);
                    throw new RuntimeException(e);
                }
            }
        }

        if (Holidays.isRequiemNewYear()) {
            String message = "Happy New Year!";
            p.getCommunicator().sendServerMessage(message, 0, 255, 0);
            if (DbHelper.canReceiveHolidayPresent(p, newYear)) {
                try {
                    p.getInventory().insertItem(ItemFactory.createItem(ItemList.fireworks, 99f, Materials.MATERIAL_WOOD_BIRCH, MaterialUtilities.COMMON, null), true);
                    p.getInventory().insertItem(ItemFactory.createItem(ItemList.sleepPowder, 99f, Materials.MATERIAL_WHEAT, MaterialUtilities.COMMON, null), true);
                    p.getCommunicator().sendServerMessage("A shadowy figure appears behind you and your pocket gets heavier. You quickly turn around to see what is going on only to find that nobody is there.", 255, 192, 203);
                } catch (FailedException | NoSuchTemplateException e) {
                    RequiemLogging.logException("Error in giveItems()", e);
                    throw new RuntimeException(e);
                }
            }
        }

        if (Holidays.isRequiemStPatricksDay()) {
            String message = "Happy St. Patrick's Day!";
            p.getCommunicator().sendServerMessage(message, 0, 255, 0);
            if (DbHelper.canReceiveHolidayPresent(p, stPatrickDay)) {
                try {
                    final Item waterSkin = ItemFactory.createItem(ItemList.skinWater, 20.0f, null);
                    final Item whisky = ItemFactory.createItem(ItemList.whisky, 99.0f, null);
                    whisky.setWeight(2500, true);
                    waterSkin.insertItem(whisky);
                    p.getInventory().insertItem(waterSkin, true);
                    p.getInventory().insertItem(ItemFactory.createItem(ItemList.sleepPowder, 99f, Materials.MATERIAL_WHEAT, MaterialUtilities.COMMON, null), true);
                    p.getCommunicator().sendServerMessage("A shadowy figure appears behind you and your pocket gets heavier. You quickly turn around to see what is going on only to find that nobody is there.", 255, 192, 203);
                } catch (FailedException | NoSuchTemplateException e) {
                    RequiemLogging.logException("Error in giveItems()", e);
                    throw new RuntimeException(e);
                }
            }
        }

        if (p.getPower() == MiscConstants.POWER_IMPLEMENTOR) {
            if (DbHelper.canReceiveHolidayPresent(p, test)) {
                try {
                    final Item waterSkin = ItemFactory.createItem(ItemList.skinWater, 20.0f, null);
                    final Item whisky = ItemFactory.createItem(ItemList.whisky, 99.0f, null);
                    whisky.setWeight(2500, true);
                    waterSkin.insertItem(whisky);
                    p.getInventory().insertItem(waterSkin, true);
                    p.getInventory().insertItem(ItemFactory.createItem(ItemList.sleepPowder, 99f, Materials.MATERIAL_WHEAT, MaterialUtilities.COMMON, null), true);
                    p.getCommunicator().sendServerMessage("A shadowy figure appears behind you and your pocket gets heavier. You quickly turn around to see what is going on only to find that nobody is there.", 255, 192, 203);
                } catch (FailedException | NoSuchTemplateException e) {
                    RequiemLogging.logException("Error in giveItems()", e);
                    throw new RuntimeException(e);
                }
            }
        }

    }

}
