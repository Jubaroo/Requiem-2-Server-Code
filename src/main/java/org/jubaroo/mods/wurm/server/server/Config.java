package org.jubaroo.mods.wurm.server.server;

import org.jubaroo.mods.wurm.server.ModConfig;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;

import java.util.Properties;

import static org.jubaroo.mods.wurm.server.ModConfig.*;

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
        RequiemLogging.logInfo("| |                      Configuring Requiem 2 Mod                    |-|       ");
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
        // ========================== Discord ===========================
        ModConfig.botToken = properties.getProperty("botToken");
        ModConfig.serverName = properties.getProperty("serverName");
        CustomChannel.GLOBAL.discordName = properties.getProperty("globalName");
        CustomChannel.HELP.discordName = properties.getProperty("helpName");
        CustomChannel.TICKETS.discordName = properties.getProperty("ticketName");
        CustomChannel.TITLES.discordName = properties.getProperty("titlesName");
        CustomChannel.EVENTS.discordName = properties.getProperty("eventsName");
        CustomChannel.TITAN.discordName = properties.getProperty("titanName");
        CustomChannel.TRADE.discordName = properties.getProperty("tradeName");
        CustomChannel.LOGINS.discordName = properties.getProperty("loginsName");
        CustomChannel.SERVER_STATUS.discordName = properties.getProperty("serverStatusName");
        // ========================== Database ===========================
        user = properties.getProperty("user");
        pass = properties.getProperty("pass");
        name = properties.getProperty("name");
        // ========================== AddActions ===========================
        addGmProtect = Boolean.parseBoolean(properties.getProperty("addGmProtect", String.valueOf(addGmProtect)));
        addGmUnprotect = Boolean.parseBoolean(properties.getProperty("addGmUnprotect", String.valueOf(addGmUnprotect)));
        addSpawnGuard = Boolean.parseBoolean(properties.getProperty("addSpawnGuard", String.valueOf(addSpawnGuard)));
        addLabyrinth = Boolean.parseBoolean(properties.getProperty("addLabyrinth", String.valueOf(addLabyrinth)));
        searchDens = Boolean.parseBoolean(properties.getProperty("searchDens", String.valueOf(searchDens)));
        terrainSmoothing = Boolean.parseBoolean(properties.getProperty("terrainSmoothing", String.valueOf(terrainSmoothing)));
        changeToMycelium = Boolean.parseBoolean(properties.getProperty("changeToMycelium", String.valueOf(changeToMycelium)));
        // ========================== GM Changes ===========================
        gmFullFavor = Boolean.parseBoolean(properties.getProperty("gmFullFavor", String.valueOf(searchDens)));
        gmFullStamina = Boolean.parseBoolean(properties.getProperty("gmFullStamina", String.valueOf(searchDens)));
        // ========================== Items ===========================
        itemCrystalCreationDamage = Integer.parseInt(properties.getProperty("crystalCreationDamage", Integer.toString(itemCrystalCreationDamage)));
        itemHolyBook = Boolean.parseBoolean(properties.getProperty("holyBook", String.valueOf(itemHolyBook)));
        itemNymphPortal = Boolean.parseBoolean(properties.getProperty("nymphPortal", String.valueOf(itemNymphPortal)));
        itemDemonPortal = Boolean.parseBoolean(properties.getProperty("demonPortal", String.valueOf(itemDemonPortal)));
        // ========================== Creatures ===========================
        stfuNpcs = Boolean.parseBoolean(properties.getProperty("stfuNpcs", String.valueOf(stfuNpcs)));
        setUnicornIsHorse = Boolean.parseBoolean(properties.getProperty("setUnicornIsHorse", String.valueOf(setUnicornIsHorse)));
        makeNonAggro = properties.getProperty("makeNonAggressive", String.valueOf(makeNonAggro));
        enableDepots = Boolean.parseBoolean(properties.getProperty("enable_depots", String.valueOf(enableDepots)));
        uniques = Boolean.parseBoolean(properties.getProperty("uniques", String.valueOf(uniques)));
        treasureGoblin = Boolean.parseBoolean(properties.getProperty("treasure_goblin", String.valueOf(treasureGoblin)));
        customMounts = Boolean.parseBoolean(properties.getProperty("custom_mounts", String.valueOf(customMounts)));
        undead = Boolean.parseBoolean(properties.getProperty("undead", String.valueOf(undead)));
        farmAnimals = Boolean.parseBoolean(properties.getProperty("farm_animals", String.valueOf(farmAnimals)));
        monsters = Boolean.parseBoolean(properties.getProperty("monsters", String.valueOf(monsters)));
        npc = Boolean.parseBoolean(properties.getProperty("npc", String.valueOf(npc)));
        titans = Boolean.parseBoolean(properties.getProperty("titans", String.valueOf(titans)));
        humans = Boolean.parseBoolean(properties.getProperty("humans", String.valueOf(humans)));
        animals = Boolean.parseBoolean(properties.getProperty("animals", "false"));
        wyverns = Boolean.parseBoolean(properties.getProperty("wyverns", "false"));
        milkWhales = Boolean.parseBoolean(properties.getProperty("milk_whales", "false"));
        // ========================== Spells ===========================
        noCooldownSpells = properties.getProperty("noCooldownSpells", String.valueOf(noCooldownSpells));
        // ========================== Other ===========================
        tileX = Integer.parseInt(properties.getProperty("LoginServerEffectX"));
        tileY = Integer.parseInt(properties.getProperty("LoginServerEffectY"));
        tileZ = Integer.parseInt(properties.getProperty("LoginServerEffectZ"));
        enableMyceliumSpread = Boolean.parseBoolean(properties.getProperty("enableMyceliumSpread", "false"));
        addCommands = Boolean.parseBoolean(properties.getProperty("addCommands", "false"));
        loadFullContainers = Boolean.parseBoolean(properties.getProperty("loadFullContainers", "false"));
        noMineDrift = Boolean.parseBoolean(properties.getProperty("noMineDrift", "false"));
        lampsAutoLight = Boolean.parseBoolean(properties.getProperty("lampsAutoLight", "false"));
        allowTentsOnDeed = Boolean.parseBoolean(properties.getProperty("allowTentsOnDeed", "false"));
        allSurfaceMine = Boolean.parseBoolean(properties.getProperty("allSurfaceMine", "false"));
        hidePlayerGodInscriptions = Boolean.parseBoolean(properties.getProperty("hidePlayerGodInscriptions", "false"));
        cashPerCorpse = Long.parseLong(properties.getProperty("cashPerCorpse", Long.toString(cashPerCorpse)));
        newsletterImage = String.valueOf(properties.getProperty("newsletterImage", String.valueOf(newsletterImage)));
        mailboxEnableEnchant = Boolean.parseBoolean(properties.getProperty("enableEnchant", "false"));
        mailboxEnchantPower = Float.parseFloat(properties.getProperty("enchantPower", String.valueOf(mailboxEnchantPower)));
        if (mailboxEnchantPower < 1.0f || mailboxEnchantPower > 101.0f) {
            mailboxEnchantPower = 10.0f;
        }
        // ========================== Portals ===========================
        costPerMin = Integer.parseInt(properties.getProperty("costPerMin", String.valueOf(costPerMin)));
        costToActivate = Integer.parseInt(properties.getProperty("costToActivate", String.valueOf(costToActivate)));
        activateBankAmount = Integer.parseInt(properties.getProperty("activateBankAmount", String.valueOf(activateBankAmount)));
        craftPortals = Boolean.parseBoolean(properties.getProperty("craftPortals", Boolean.toString(craftPortals)));
        // ========================== Mission Items ===========================
        addMissionItems = Boolean.parseBoolean(properties.getProperty("addMissionItems", "false"));
        coins = Boolean.parseBoolean(properties.getProperty("coins", "false"));
        riftItems = Boolean.parseBoolean(properties.getProperty("riftItems", "false"));
        miscItems = Boolean.parseBoolean(properties.getProperty("miscItems", "false"));
        metalLumps = Boolean.parseBoolean(properties.getProperty("metalLumps", "false"));
        metalOres = Boolean.parseBoolean(properties.getProperty("metalOres", "false"));
        gems = Boolean.parseBoolean(properties.getProperty("gems", "false"));
        potionsSalvesOils = Boolean.parseBoolean(properties.getProperty("potionsSalvesOils", "false"));
        mineDoors = Boolean.parseBoolean(properties.getProperty("mineDoors", "false"));
        mirrors = Boolean.parseBoolean(properties.getProperty("mirrors", "false"));
        masks = Boolean.parseBoolean(properties.getProperty("masks", "false"));
        magicItems = Boolean.parseBoolean(properties.getProperty("magicItems", "false"));
        wands = Boolean.parseBoolean(properties.getProperty("wands", "false"));
        // ========================== MountSettings ===========================
        wagons = Boolean.parseBoolean(properties.getProperty("wagons", "false"));
        //wagonKits = Boolean.parseBoolean(properties.getProperty("wagonKits", "false"));
        sailingBoats = Boolean.parseBoolean(properties.getProperty("sailingBoats", "false"));
        cogs = Boolean.parseBoolean(properties.getProperty("cogs", "false"));
        knarrs = Boolean.parseBoolean(properties.getProperty("knarrs", "false"));
        caravels = Boolean.parseBoolean(properties.getProperty("caravels", "false"));
        corbitas = Boolean.parseBoolean(properties.getProperty("corbitas", "false"));
        magicCarpet = Boolean.parseBoolean(properties.getProperty("magicCarpets", "false"));
        vehicleCreationEntries = Boolean.parseBoolean(properties.getProperty("vehicleCreationEntries", "false"));
        // ========================== Conquest ============================
        delayFogGoblins = Long.parseLong(properties.getProperty("delayFogGoblins", Long.toString(delayFogGoblins)));
        delayTradeTents = Long.parseLong(properties.getProperty("delayTradeTents", Long.toString(delayTradeTents)));
        delayResourcePoints = Long.parseLong(properties.getProperty("delayResourcePoints", Long.toString(delayResourcePoints)));
        delayLootCarpets = Long.parseLong(properties.getProperty("delayLootCarpets", Long.toString(delayLootCarpets)));
        delayMobSpawners = Long.parseLong(properties.getProperty("delayMobSpawners", Long.toString(delayMobSpawners)));
        delayAthanorMechanism = Long.parseLong(properties.getProperty("delayAthanorMechanism", Long.toString(delayAthanorMechanism)));
        delayWallRepair = Long.parseLong(properties.getProperty("delayWallRepair", Long.toString(delayWallRepair)));
        initialGoblinCensus = Boolean.parseBoolean(properties.getProperty("initialGoblinCensus", "false"));
        maxFogGoblins = Integer.parseInt(properties.getProperty("maxFogGoblins", Integer.toString(maxFogGoblins)));
        tradeTentsNorthZoneName = String.valueOf(properties.getProperty("tradeTentsNorthZoneName", String.valueOf(tradeTentsNorthZoneName)));
        tradeTentsSouthZoneName = String.valueOf(properties.getProperty("tradeTentsSouthZoneName", String.valueOf(tradeTentsSouthZoneName)));
        tradeTentCoinReward = Long.parseLong(properties.getProperty("tradeTentCoinReward", Long.toString(tradeTentCoinReward)));
        // ========================== World of Wonders ===========================
        damageToTake = Integer.parseInt(properties.getProperty("damageToTake", Float.toString(damageToTake)));
        skullLocateUnique = Boolean.parseBoolean(properties.getProperty("skullLocateUnique", "false"));
        //skullImpFactor = Float.parseFloat(properties.getProperty("skullImpFactor", Float.toString(skullImpFactor)));
        reloadSkull = Boolean.parseBoolean(properties.getProperty("reloadSkull", "false"));
        bossIds = properties.getProperty("bossIds").split(";");
        //scrollids = properties.getProperty("scrollids").split(";");
        // ========================== Jousting ===========================
        LanceDamage = Integer.parseInt(properties.getProperty("LanceDamage", "10"));
        BonusLanceDamage = Integer.parseInt(properties.getProperty("BonusLanceDamage", "5"));
        BaseHitChance = Integer.parseInt(properties.getProperty("BaseHitChance", "20"));
        SpearSkillRange = Integer.parseInt(properties.getProperty("SkillRange", "5"));
        LoseHelmetChance = Integer.parseInt(properties.getProperty("LoseHelmetChance", "5"));
        PerKMDamageBoost = Float.parseFloat(properties.getProperty("PerKMDamageBonus", "10"));
        AllowSkillGain = Boolean.parseBoolean(properties.getProperty("AllowSkillGain", "true"));
        AllowCraftingLance = Boolean.parseBoolean(properties.getProperty("AllowCraftingLance", "true"));
        LanceRange = Float.parseFloat(properties.getProperty("LanceRange", "5"));
        // ========================= Logging ==============================
        creatureDeathLogging = Boolean.parseBoolean(properties.getProperty("CreatureDeathLogging", "false"));
        creatureCreateLogging = Boolean.parseBoolean(properties.getProperty("CreatureCreateLogging", "false"));
        itemCreateLogging = Boolean.parseBoolean(properties.getProperty("ItemCreateLogging", "false"));
        itemRemoveLogging = Boolean.parseBoolean(properties.getProperty("itemRemoveLogging", "false"));
        // ========================= Mod Toggles ==============================
        disableEntireMod = Boolean.parseBoolean(properties.getProperty("disableEntireMod", "false"));
        disableCreatureLoot = Boolean.parseBoolean(properties.getProperty("disableCreatureLoot", "false"));
        disableHolidayCreatures = Boolean.parseBoolean(properties.getProperty("disableHolidayCreatures", "false"));
        disableCreatureMods = Boolean.parseBoolean(properties.getProperty("disableCreatureMods", "false"));
        disableTradeShips = Boolean.parseBoolean(properties.getProperty("disableTradeShips", "false"));
        disableItemMods = Boolean.parseBoolean(properties.getProperty("disableItemMods", "false"));
        disableVehicleMods = Boolean.parseBoolean(properties.getProperty("disableVehicleMods", "false"));
        disableBytecodeMods = Boolean.parseBoolean(properties.getProperty("disableBytecodeMods", "false"));
        disableMiscMods = Boolean.parseBoolean(properties.getProperty("disableMiscMods", "false"));
        disableMiscMods = Boolean.parseBoolean(properties.getProperty("disableMiscMods", "false"));
        enableAthanorMechanism = Boolean.parseBoolean(properties.getProperty("enableAthanorMechanism", "false"));
        disableDiscordReliance = Boolean.parseBoolean(properties.getProperty("disableDiscordReliance", "false"));
        disableScrollGearBinding = Boolean.parseBoolean(properties.getProperty("disableScrollGearBinding", "false"));
        disablePreInit = Boolean.parseBoolean(properties.getProperty("disablePreInit", "false"));
        disableInit = Boolean.parseBoolean(properties.getProperty("disableInit", "false"));
        disableOnServerStarted = Boolean.parseBoolean(properties.getProperty("disableOnServerStarted", "false"));
        disableFogGoblins = Boolean.parseBoolean(properties.getProperty("disableFogGoblins", "false"));
        disableColoredUnicorns = Boolean.parseBoolean(properties.getProperty("disableColoredUnicorns", "false"));
        disableDatabaseChanges = Boolean.parseBoolean(properties.getProperty("disableDatabaseChanges", "false"));
        disableInit = Boolean.parseBoolean(properties.getProperty("disableInit", "false"));
        disableOnPlayerLogin = Boolean.parseBoolean(properties.getProperty("disableOnPlayerLogin", "false"));
        disableOnPlayerLogout = Boolean.parseBoolean(properties.getProperty("disableOnPlayerLogout", "false"));
        disableOnServerStarted = Boolean.parseBoolean(properties.getProperty("disableOnServerStarted", "false"));
        disableOnKingdomMessage = Boolean.parseBoolean(properties.getProperty("disableOnKingdomMessage", "false"));
        disableOnPlayerMessage = Boolean.parseBoolean(properties.getProperty("disableOnPlayerMessage", "false"));
        disableMissionChanges = Boolean.parseBoolean(properties.getProperty("disableMissionChanges", "false"));
        RequiemLogging.logInfo("all configure completed");
    }

}
