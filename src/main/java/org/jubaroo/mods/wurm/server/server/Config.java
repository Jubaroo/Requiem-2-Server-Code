package org.jubaroo.mods.wurm.server.server;

import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.util.Properties;

public class Config {

    private static void ASCIIDragon() {
        RequiemLogging.logInfo("                                  /   \\                                        ");
        RequiemLogging.logInfo(" _                        )      ((   ))     (                                  ");
        RequiemLogging.logInfo("(@)                      /|\\      ))_((     /|\\                               ");
        RequiemLogging.logInfo("|-|                     / | \\    (/\\|/\\)   / | \\                      (@)   ");
        RequiemLogging.logInfo("| |--------------------/--|-voV---\\`|'/--Vov-|--\\---------------------|-|     ");
        RequiemLogging.logInfo("|-|                         '^`   (o o)  '^`                          | |       ");
        RequiemLogging.logInfo("| |                               `\\Y/'                               |-|      ");
        RequiemLogging.logInfo("|-|                                                                   | |       ");
        RequiemLogging.logInfo("| |                       Configuring Requiem Mod                     |-|       ");
        RequiemLogging.logInfo("|-|                                                                   | |       ");
        RequiemLogging.logInfo("| |                                                                   |-|       ");
        RequiemLogging.logInfo("|_|___________________________________________________________________| |       ");
        RequiemLogging.logInfo("(@)                 l  /\\ /       ( (       \\ /\\  l                 `\\|-|   ");
        RequiemLogging.logInfo("                    l /  V         \\ \\       V  \\ l                   (@)    ");
        RequiemLogging.logInfo("                    l/             _) )_         \\I                            ");
        RequiemLogging.logInfo("                                   `\\ /'                                       ");
        RequiemLogging.logInfo("                                     `                                          ");
    }

    public static void doConfig(Properties properties) {
        RequiemLogging.logInfo("configure called");
        ASCIIDragon();
        // ========================== AddActions ===========================
        Constants.addGmProtect = Boolean.parseBoolean(properties.getProperty("addGmProtect", String.valueOf(Constants.addGmProtect)));
        Constants.addGmUnprotect = Boolean.parseBoolean(properties.getProperty("addGmUnprotect", String.valueOf(Constants.addGmUnprotect)));
        Constants.addSpawnGuard = Boolean.parseBoolean(properties.getProperty("addSpawnGuard", String.valueOf(Constants.addSpawnGuard)));
        Constants.addLabyrinth = Boolean.parseBoolean(properties.getProperty("addLabyrinth", String.valueOf(Constants.addLabyrinth)));
        Constants.searchDens = Boolean.parseBoolean(properties.getProperty("searchDens", String.valueOf(Constants.searchDens)));
        Constants.terrainSmoothing = Boolean.parseBoolean(properties.getProperty("terrainSmoothing", String.valueOf(Constants.terrainSmoothing)));
        Constants.changeToMycelium = Boolean.parseBoolean(properties.getProperty("changeToMycelium", String.valueOf(Constants.changeToMycelium)));
        // ========================== GM Changes ===========================
        Constants.gmFullFavor = Boolean.parseBoolean(properties.getProperty("gmFullFavor", String.valueOf(Constants.searchDens)));
        Constants.gmFullStamina = Boolean.parseBoolean(properties.getProperty("gmFullStamina", String.valueOf(Constants.searchDens)));
        // ========================== Items ===========================
        Constants.itemCrystalCreationDamage = Integer.parseInt(properties.getProperty("crystalCreationDamage", Integer.toString(Constants.itemCrystalCreationDamage)));
        Constants.itemHolyBook = Boolean.parseBoolean(properties.getProperty("holyBook", String.valueOf(Constants.itemHolyBook)));
        Constants.itemNymphPortal = Boolean.parseBoolean(properties.getProperty("nymphPortal", String.valueOf(Constants.itemNymphPortal)));
        Constants.itemDemonPortal = Boolean.parseBoolean(properties.getProperty("demonPortal", String.valueOf(Constants.itemDemonPortal)));
        Constants.bulkCreationEntries = Boolean.parseBoolean(properties.getProperty("oldCreationEntries", String.valueOf(Constants.bulkCreationEntries)));
        // ========================== Creatures ===========================
        Constants.stfuNpcs = Boolean.parseBoolean(properties.getProperty("stfuNpcs", String.valueOf(Constants.stfuNpcs)));
        Constants.setUnicornIsHorse = Boolean.parseBoolean(properties.getProperty("setUnicornIsHorse", String.valueOf(Constants.setUnicornIsHorse)));
        Constants.makeNonAggro = properties.getProperty("makeNonAggressive", String.valueOf(Constants.makeNonAggro));
        Constants.enableDepots = Boolean.parseBoolean(properties.getProperty("enable_depots", String.valueOf(Constants.enableDepots)));
        Constants.uniques = Boolean.parseBoolean(properties.getProperty("uniques", String.valueOf(Constants.uniques)));
        Constants.treasureGoblin = Boolean.parseBoolean(properties.getProperty("treasure_goblin", String.valueOf(Constants.treasureGoblin)));
        Constants.customMounts = Boolean.parseBoolean(properties.getProperty("custom_mounts", String.valueOf(Constants.customMounts)));
        Constants.undead = Boolean.parseBoolean(properties.getProperty("undead", String.valueOf(Constants.undead)));
        Constants.farmAnimals = Boolean.parseBoolean(properties.getProperty("farm_animals", String.valueOf(Constants.farmAnimals)));
        Constants.monsters = Boolean.parseBoolean(properties.getProperty("monsters", String.valueOf(Constants.monsters)));
        Constants.npc = Boolean.parseBoolean(properties.getProperty("npc", String.valueOf(Constants.npc)));
        Constants.titans = Boolean.parseBoolean(properties.getProperty("titans", String.valueOf(Constants.titans)));
        Constants.humans = Boolean.parseBoolean(properties.getProperty("humans", String.valueOf(Constants.humans)));
        Constants.animals = Boolean.parseBoolean(properties.getProperty("animals", "false"));
        Constants.wyverns = Boolean.parseBoolean(properties.getProperty("wyverns", "false"));
        Constants.milkWhales = Boolean.parseBoolean(properties.getProperty("milk_whales", "false"));
        Constants.christmasMobs = Boolean.parseBoolean(properties.getProperty("christmasMobs", "false"));
        //Constants.halloweenMobs = Boolean.parseBoolean(properties.getProperty("halloweenMobs", String.valueOf(Constants.halloweenMobs)));
        // ========================== Spells ===========================
        Constants.noCooldownSpells = properties.getProperty("noCooldownSpells", String.valueOf(Constants.noCooldownSpells));
        // ========================== Other ===========================
        Constants.enableMyceliumSpread = Boolean.parseBoolean(properties.getProperty("enableMyceliumSpread", "false"));
        Constants.addCommands = Boolean.parseBoolean(properties.getProperty("addCommands", "false"));
        Constants.loadFullContainers = Boolean.parseBoolean(properties.getProperty("loadFullContainers", "false"));
        Constants.noMineDrift = Boolean.parseBoolean(properties.getProperty("noMineDrift", "false"));
        Constants.lampsAutoLight = Boolean.parseBoolean(properties.getProperty("lampsAutoLight", "false"));
        Constants.allowTentsOnDeed = Boolean.parseBoolean(properties.getProperty("allowTentsOnDeed", "false"));
        Constants.allSurfaceMine = Boolean.parseBoolean(properties.getProperty("allSurfaceMine", "false"));
        Constants.hidePlayerGodInscriptions = Boolean.parseBoolean(properties.getProperty("hidePlayerGodInscriptions", "false"));
        Constants.cashPerCorpse = Long.parseLong(properties.getProperty("cashPerCorpse", Long.toString(Constants.cashPerCorpse)));
        Constants.newsletterImage = String.valueOf(properties.getProperty("newsletterImage", String.valueOf(Constants.newsletterImage)));
        Constants.mailboxEnableEnchant = Boolean.parseBoolean(properties.getProperty("enableEnchant", "false"));
        Constants.mailboxEnchantPower = Float.parseFloat(properties.getProperty("enchantPower", String.valueOf(Constants.mailboxEnchantPower)));
        if (Constants.mailboxEnchantPower < 1.0f || Constants.mailboxEnchantPower > 101.0f) {
            Constants.mailboxEnchantPower = 30.0f;
        }
        // ========================== Labyrinth ===========================
        Constants.logExecutionCost = Constants.executionCostLogging;
        Constants.executionCostLogging = Boolean.parseBoolean(properties.getProperty("executionCostLogging", "false"));
        // ========================== Mission Items ===========================
        Constants.addMissionItems = Boolean.parseBoolean(properties.getProperty("addMissionItems", "false"));
        Constants.coins = Boolean.parseBoolean(properties.getProperty("coins", "false"));
        Constants.riftItems = Boolean.parseBoolean(properties.getProperty("riftItems", "false"));
        Constants.miscItems = Boolean.parseBoolean(properties.getProperty("miscItems", "false"));
        Constants.metalLumps = Boolean.parseBoolean(properties.getProperty("metalLumps", "false"));
        Constants.metalOres = Boolean.parseBoolean(properties.getProperty("metalOres", "false"));
        Constants.gems = Boolean.parseBoolean(properties.getProperty("gems", "false"));
        Constants.potionsSalvesOils = Boolean.parseBoolean(properties.getProperty("potionsSalvesOils", "false"));
        Constants.mineDoors = Boolean.parseBoolean(properties.getProperty("mineDoors", "false"));
        Constants.mirrors = Boolean.parseBoolean(properties.getProperty("mirrors", "false"));
        Constants.masks = Boolean.parseBoolean(properties.getProperty("masks", "false"));
        Constants.magicItems = Boolean.parseBoolean(properties.getProperty("magicItems", "false"));
        Constants.wands = Boolean.parseBoolean(properties.getProperty("wands", "false"));
        // ========================== MountSettings ===========================
        Constants.wagons = Boolean.parseBoolean(properties.getProperty("wagons", "false"));
        //Constants.wagonKits = Boolean.parseBoolean(properties.getProperty("wagonKits", "false"));
        Constants.sailingBoats = Boolean.parseBoolean(properties.getProperty("sailingBoats", "false"));
        Constants.cogs = Boolean.parseBoolean(properties.getProperty("cogs", "false"));
        Constants.knarrs = Boolean.parseBoolean(properties.getProperty("knarrs", "false"));
        Constants.caravels = Boolean.parseBoolean(properties.getProperty("caravels", "false"));
        Constants.corbitas = Boolean.parseBoolean(properties.getProperty("corbitas", "false"));
        Constants.magicCarpet = Boolean.parseBoolean(properties.getProperty("magicCarpets", "false"));
        Constants.vehicleCreationEntries = Boolean.parseBoolean(properties.getProperty("vehicleCreationEntries", "false"));
        // ========================== Conquest ============================
        Constants.delayFogGoblins = Long.parseLong(properties.getProperty("delayFogGoblins", Long.toString(Constants.delayFogGoblins)));
        Constants.delayTradeTents = Long.parseLong(properties.getProperty("delayTradeTents", Long.toString(Constants.delayTradeTents)));
        Constants.delayResourcePoints = Long.parseLong(properties.getProperty("delayResourcePoints", Long.toString(Constants.delayResourcePoints)));
        Constants.delayLootCarpets = Long.parseLong(properties.getProperty("delayLootCarpets", Long.toString(Constants.delayLootCarpets)));
        Constants.delayMobSpawners = Long.parseLong(properties.getProperty("delayMobSpawners", Long.toString(Constants.delayMobSpawners)));
        Constants.delayAthanorMechanism = Long.parseLong(properties.getProperty("delayAthanorMechanism", Long.toString(Constants.delayAthanorMechanism)));
        Constants.delayWallRepair = Long.parseLong(properties.getProperty("delayWallRepair", Long.toString(Constants.delayWallRepair)));
        Constants.initialGoblinCensus = Boolean.parseBoolean(properties.getProperty("initialGoblinCensus", "false"));
        Constants.maxFogGoblins = Integer.parseInt(properties.getProperty("maxFogGoblins", Integer.toString(Constants.maxFogGoblins)));
        Constants.tradeTentsNorthZoneName = String.valueOf(properties.getProperty("tradeTentsNorthZoneName", String.valueOf(Constants.tradeTentsNorthZoneName)));
        Constants.tradeTentsSouthZoneName = String.valueOf(properties.getProperty("tradeTentsSouthZoneName", String.valueOf(Constants.tradeTentsSouthZoneName)));
        Constants.tradeTentCoinReward = Long.parseLong(properties.getProperty("tradeTentCoinReward", Long.toString(Constants.tradeTentCoinReward)));
        // ========================== World of Wonders ===========================
        Constants.damageToTake = Integer.parseInt(properties.getProperty("damageToTake", Float.toString(Constants.damageToTake)));
        Constants.skullLocateUnique = Boolean.parseBoolean(properties.getProperty("skullLocateUnique", "false"));
        //Constants.skullImpFactor = Float.parseFloat(properties.getProperty("skullImpFactor", Float.toString(Constants.skullImpFactor)));
        Constants.reloadSkull = Boolean.parseBoolean(properties.getProperty("reloadSkull", "false"));
        Constants.bossids = properties.getProperty("bossIds").split(";");
        //Constants.scrollids = properties.getProperty("scrollids").split(";");
        // ========================== Jousting ===========================
        Constants.LanceDamage = Integer.parseInt(properties.getProperty("LanceDamage", "10"));
        Constants.BonusLanceDamage = Integer.parseInt(properties.getProperty("BonusLanceDamage", "5"));
        Constants.BaseHitChance = Integer.parseInt(properties.getProperty("BaseHitChance", "20"));
        Constants.SpearSkillRange = Integer.parseInt(properties.getProperty("SkillRange", "5"));
        Constants.LoseHelmetChance = Integer.parseInt(properties.getProperty("LoseHelmetChance", "5"));
        Constants.PerKMDamageBoost = Float.parseFloat(properties.getProperty("PerKMDamageBonus", "10"));
        Constants.AllowSkillGain = Boolean.parseBoolean(properties.getProperty("AllowSkillGain", "true"));
        Constants.AllowCraftingLance = Boolean.parseBoolean(properties.getProperty("AllowCraftingLance", "true"));
        Constants.LanceRange = Float.parseFloat(properties.getProperty("LanceRange", "5"));
        // ========================= Logging ==============================
        Constants.debug = Boolean.parseBoolean(properties.getProperty("debug", "false"));
        Constants.creatureDeathLogging = Boolean.parseBoolean(properties.getProperty("CreatureDeathLogging", "false"));
        Constants.creatureCreateLogging = Boolean.parseBoolean(properties.getProperty("CreatureCreateLogging", "false"));
        Constants.itemCreateLogging = Boolean.parseBoolean(properties.getProperty("ItemCreateLogging", "false"));
        Constants.itemRemoveLogging = Boolean.parseBoolean(properties.getProperty("itemRemoveLogging", "false"));
        // ========================= Mod Toggles ==============================
        Constants.disableEntireMod = Boolean.parseBoolean(properties.getProperty("disableEntireMod", "false"));
        Constants.disableCreatureLoot = Boolean.parseBoolean(properties.getProperty("disableCreatureLoot", "false"));
        Constants.disableHolidayCreatures = Boolean.parseBoolean(properties.getProperty("disableHolidayCreatures", "false"));
        Constants.disableCreatureMods = Boolean.parseBoolean(properties.getProperty("disableCreatureMods", "false"));
        Constants.disableTradeShips = Boolean.parseBoolean(properties.getProperty("disableTradeShips", "false"));
        Constants.disableItemMods = Boolean.parseBoolean(properties.getProperty("disableItemMods", "false"));
        Constants.disableVehicleMods = Boolean.parseBoolean(properties.getProperty("disableVehicleMods", "false"));
        Constants.disableBytecodeMods = Boolean.parseBoolean(properties.getProperty("disableBytecodeMods", "false"));
        Constants.disableMiscMods = Boolean.parseBoolean(properties.getProperty("disableMiscMods", "false"));
        Constants.disableMiscMods = Boolean.parseBoolean(properties.getProperty("disableMiscMods", "false"));
        Constants.enableAthanorMechanism = Boolean.parseBoolean(properties.getProperty("enableAthanorMechanism", "false"));
        Constants.disableDiscordReliance = Boolean.parseBoolean(properties.getProperty("disableDiscordReliance", "false"));
        Constants.disableScrollGearBinding = Boolean.parseBoolean(properties.getProperty("disableScrollGearBinding", "false"));
        RequiemLogging.logInfo("all configure completed");
    }
}
