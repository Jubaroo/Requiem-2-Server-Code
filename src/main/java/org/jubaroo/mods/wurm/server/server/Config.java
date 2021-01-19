package org.jubaroo.mods.wurm.server.server;

import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.actions.character.JoustAction;
import org.jubaroo.mods.wurm.server.items.pottals.PortalMod;
import org.jubaroo.mods.wurm.server.server.constants.*;

import java.util.Properties;

import static org.jubaroo.mods.wurm.server.server.constants.CreatureConstants.*;
import static org.jubaroo.mods.wurm.server.server.constants.ItemConstants.*;
import static org.jubaroo.mods.wurm.server.server.constants.LoggingConstants.executionCostLogging;
import static org.jubaroo.mods.wurm.server.server.constants.OtherConstants.*;
import static org.jubaroo.mods.wurm.server.server.constants.ToggleConstants.*;

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
        bulkCreationEntries = Boolean.parseBoolean(properties.getProperty("oldCreationEntries", String.valueOf(bulkCreationEntries)));
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
            mailboxEnchantPower = 30.0f;
        }
        // ========================== Portals ===========================
        PortalMod.costPerMin = Integer.parseInt(properties.getProperty("costPerMin", String.valueOf(PortalMod.costPerMin)));
        PortalMod.costToActivate = Integer.parseInt(properties.getProperty("costToActivate", String.valueOf(PortalMod.costToActivate)));
        PortalMod.activateBankAmount = Integer.parseInt(properties.getProperty("activateBankAmount", String.valueOf(PortalMod.activateBankAmount)));
        PortalMod.craftPortals = Boolean.parseBoolean(properties.getProperty("craftPortals", Boolean.toString(PortalMod.craftPortals)));
        // ========================== Labyrinth ===========================
        LoggingConstants.logExecutionCost = executionCostLogging;
        executionCostLogging = Boolean.parseBoolean(properties.getProperty("executionCostLogging", "false"));
        // ========================== Mission Items ===========================
        OtherConstants.addMissionItems = Boolean.parseBoolean(properties.getProperty("addMissionItems", "false"));
        ItemConstants.coins = Boolean.parseBoolean(properties.getProperty("coins", "false"));
        ItemConstants.riftItems = Boolean.parseBoolean(properties.getProperty("riftItems", "false"));
        ItemConstants.miscItems = Boolean.parseBoolean(properties.getProperty("miscItems", "false"));
        ItemConstants.metalLumps = Boolean.parseBoolean(properties.getProperty("metalLumps", "false"));
        ItemConstants.metalOres = Boolean.parseBoolean(properties.getProperty("metalOres", "false"));
        ItemConstants.gems = Boolean.parseBoolean(properties.getProperty("gems", "false"));
        ItemConstants.potionsSalvesOils = Boolean.parseBoolean(properties.getProperty("potionsSalvesOils", "false"));
        ItemConstants.mineDoors = Boolean.parseBoolean(properties.getProperty("mineDoors", "false"));
        ItemConstants.mirrors = Boolean.parseBoolean(properties.getProperty("mirrors", "false"));
        ItemConstants.masks = Boolean.parseBoolean(properties.getProperty("masks", "false"));
        ItemConstants.magicItems = Boolean.parseBoolean(properties.getProperty("magicItems", "false"));
        ItemConstants.wands = Boolean.parseBoolean(properties.getProperty("wands", "false"));
        // ========================== MountSettings ===========================
        VehicleConstants.wagons = Boolean.parseBoolean(properties.getProperty("wagons", "false"));
        //wagonKits = Boolean.parseBoolean(properties.getProperty("wagonKits", "false"));
        VehicleConstants.sailingBoats = Boolean.parseBoolean(properties.getProperty("sailingBoats", "false"));
        VehicleConstants.cogs = Boolean.parseBoolean(properties.getProperty("cogs", "false"));
        VehicleConstants.knarrs = Boolean.parseBoolean(properties.getProperty("knarrs", "false"));
        VehicleConstants.caravels = Boolean.parseBoolean(properties.getProperty("caravels", "false"));
        VehicleConstants.corbitas = Boolean.parseBoolean(properties.getProperty("corbitas", "false"));
        VehicleConstants.magicCarpet = Boolean.parseBoolean(properties.getProperty("magicCarpets", "false"));
        VehicleConstants.vehicleCreationEntries = Boolean.parseBoolean(properties.getProperty("vehicleCreationEntries", "false"));
        // ========================== Conquest ============================
        PollingConstants.delayFogGoblins = Long.parseLong(properties.getProperty("delayFogGoblins", Long.toString(PollingConstants.delayFogGoblins)));
        PollingConstants.delayTradeTents = Long.parseLong(properties.getProperty("delayTradeTents", Long.toString(PollingConstants.delayTradeTents)));
        PollingConstants.delayResourcePoints = Long.parseLong(properties.getProperty("delayResourcePoints", Long.toString(PollingConstants.delayResourcePoints)));
        PollingConstants.delayLootCarpets = Long.parseLong(properties.getProperty("delayLootCarpets", Long.toString(PollingConstants.delayLootCarpets)));
        PollingConstants.delayMobSpawners = Long.parseLong(properties.getProperty("delayMobSpawners", Long.toString(PollingConstants.delayMobSpawners)));
        PollingConstants.delayAthanorMechanism = Long.parseLong(properties.getProperty("delayAthanorMechanism", Long.toString(PollingConstants.delayAthanorMechanism)));
        PollingConstants.delayWallRepair = Long.parseLong(properties.getProperty("delayWallRepair", Long.toString(PollingConstants.delayWallRepair)));
        PollingConstants.initialGoblinCensus = Boolean.parseBoolean(properties.getProperty("initialGoblinCensus", "false"));
        PollingConstants.maxFogGoblins = Integer.parseInt(properties.getProperty("maxFogGoblins", Integer.toString(PollingConstants.maxFogGoblins)));
        PollingConstants.tradeTentsNorthZoneName = String.valueOf(properties.getProperty("tradeTentsNorthZoneName", String.valueOf(PollingConstants.tradeTentsNorthZoneName)));
        PollingConstants.tradeTentsSouthZoneName = String.valueOf(properties.getProperty("tradeTentsSouthZoneName", String.valueOf(PollingConstants.tradeTentsSouthZoneName)));
        PollingConstants.tradeTentCoinReward = Long.parseLong(properties.getProperty("tradeTentCoinReward", Long.toString(PollingConstants.tradeTentCoinReward)));
        // ========================== World of Wonders ===========================
        ItemConstants.damageToTake = Integer.parseInt(properties.getProperty("damageToTake", Float.toString(ItemConstants.damageToTake)));
        ItemConstants.skullLocateUnique = Boolean.parseBoolean(properties.getProperty("skullLocateUnique", "false"));
        //skullImpFactor = Float.parseFloat(properties.getProperty("skullImpFactor", Float.toString(skullImpFactor)));
        ItemConstants.reloadSkull = Boolean.parseBoolean(properties.getProperty("reloadSkull", "false"));
        CreatureConstants.bossIds = properties.getProperty("bossIds").split(";");
        //scrollids = properties.getProperty("scrollids").split(";");
        // ========================== Jousting ===========================
        JoustAction.LanceDamage = Integer.parseInt(properties.getProperty("LanceDamage", "10"));
        JoustAction.BonusLanceDamage = Integer.parseInt(properties.getProperty("BonusLanceDamage", "5"));
        JoustAction.BaseHitChance = Integer.parseInt(properties.getProperty("BaseHitChance", "20"));
        JoustAction.SpearSkillRange = Integer.parseInt(properties.getProperty("SkillRange", "5"));
        JoustAction.LoseHelmetChance = Integer.parseInt(properties.getProperty("LoseHelmetChance", "5"));
        JoustAction.PerKMDamageBoost = Float.parseFloat(properties.getProperty("PerKMDamageBonus", "10"));
        JoustAction.AllowSkillGain = Boolean.parseBoolean(properties.getProperty("AllowSkillGain", "true"));
        JoustAction.AllowCraftingLance = Boolean.parseBoolean(properties.getProperty("AllowCraftingLance", "true"));
        JoustAction.LanceRange = Float.parseFloat(properties.getProperty("LanceRange", "5"));
        // ========================= Logging ==============================
        LoggingConstants.debug = Boolean.parseBoolean(properties.getProperty("debug", "false"));
        LoggingConstants.creatureDeathLogging = Boolean.parseBoolean(properties.getProperty("CreatureDeathLogging", "false"));
        LoggingConstants.creatureCreateLogging = Boolean.parseBoolean(properties.getProperty("CreatureCreateLogging", "false"));
        LoggingConstants.itemCreateLogging = Boolean.parseBoolean(properties.getProperty("ItemCreateLogging", "false"));
        LoggingConstants.itemRemoveLogging = Boolean.parseBoolean(properties.getProperty("itemRemoveLogging", "false"));
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
        EffectsConstants.tileX = Integer.parseInt(properties.getProperty("LoginServerEffectX"));
        EffectsConstants.tileY = Integer.parseInt(properties.getProperty("LoginServerEffectY"));
        EffectsConstants.tileZ = Integer.parseInt(properties.getProperty("LoginServerEffectZ"));
        RequiemLogging.logInfo("all configure completed");
    }

}
