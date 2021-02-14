package org.jubaroo.mods.wurm.server;

import com.wurmonline.server.TimeConstants;

public class ModConfig {
    public static String botToken;
    public static String serverName;
    public static String user;
    public static String pass;
    public static String name;
    public static boolean terrainSmoothing = false;
    public static boolean changeToMycelium = false;
    public static boolean addGmProtect = false;
    public static boolean addGmUnprotect = false;
    public static boolean addSpawnGuard = false;
    public static boolean addLabyrinth = false;
    public static boolean searchDens = false;
    public static boolean gmFullFavor = false;
    public static boolean gmFullStamina = false;
    public static int itemCrystalCreationDamage;
    public static boolean itemHolyBook = false;
    public static boolean itemNymphPortal = false;
    public static boolean itemDemonPortal = false;
    public static boolean stfuNpcs = false;
    public static boolean setUnicornIsHorse = false;
    public static String makeNonAggro = "";
    public static boolean enableDepots = false;
    public static boolean uniques = false;
    public static boolean treasureGoblin = false;
    public static boolean customMounts = false;
    public static boolean undead = false;
    public static boolean farmAnimals = false;
    public static boolean monsters = false;
    public static boolean npc = false;
    public static boolean titans = false;
    public static boolean humans = false;
    public static boolean animals = false;
    public static boolean wyverns = false;
    public static boolean milkWhales = false;
    public static String noCooldownSpells = "";
    public static boolean enableMyceliumSpread = false;
    public static boolean addCommands = false;
    public static boolean loadFullContainers = false;
    public static boolean noMineDrift = false;
    public static boolean lampsAutoLight = false;
    public static boolean allowTentsOnDeed = false;
    public static boolean allSurfaceMine = false;
    public static boolean hidePlayerGodInscriptions = false;
    public static Long cashPerCorpse = 0L;
    public static String newsletterImage = "http://filterbubbles.com/img/wu/half-vamp-clue2.png";
    public static boolean mailboxEnableEnchant = true;
    public static float mailboxEnchantPower = 30f;

    public static int costPerMin = 1; //1 iron per min default
    public static int costToActivate = 10000; //1s default
    public static int activateBankAmount = 5000; //50c default
    public static boolean craftPortals = false;

    public static boolean addMissionItems = false;
    public static boolean coins = false;
    public static boolean riftItems = false;
    public static boolean miscItems = false;
    public static boolean metalLumps = false;
    public static boolean metalOres = false;
    public static boolean gems = false;
    public static boolean potionsSalvesOils = false;
    public static boolean mineDoors = false;
    public static boolean mirrors = false;
    public static boolean masks = false;
    public static boolean magicItems = false;
    public static boolean wands = false;

    public static boolean wagons = false;
    public static boolean sailingBoats = false;
    public static boolean cogs = false;
    public static boolean caravels = false;
    public static boolean knarrs = false;
    public static boolean corbitas = false;
    public static boolean magicCarpet = false;
    public static boolean vehicleCreationEntries = false;

    public static long delayFogGoblins = TimeConstants.MINUTE_MILLIS * 5;
    public static long delayTradeTents = TimeConstants.MINUTE_MILLIS * 10;
    public static long delayResourcePoints = TimeConstants.MINUTE_MILLIS;
    public static long delayLootCarpets = TimeConstants.HOUR_MILLIS * 10;
    public static long delayMobSpawners = TimeConstants.MINUTE_MILLIS * 10;
    public static long delayAthanorMechanism = TimeConstants.HOUR_MILLIS * 4;
    public static long delayWallRepair = TimeConstants.MINUTE_MILLIS * 5;

    public static boolean initialGoblinCensus = false;
    public static int maxFogGoblins = 100;

    public static String tradeTentsNorthZoneName = "Northport";
    public static String tradeTentsSouthZoneName = "Southport";
    public static long tradeTentCoinReward = 2000L;

    public static int damageToTake = 25;
    public static boolean skullLocateUnique = false;
    public static boolean reloadSkull = false;
    public static String[] bossIds = {""};

    public static int LanceDamage;
    public static int BonusLanceDamage;
    public static int BaseHitChance;
    public static int SpearSkillRange;
    public static int LoseHelmetChance;
    public static float PerKMDamageBoost;
    public static boolean AllowSkillGain;
    public static boolean AllowCraftingLance;
    public static float LanceRange;

    public static boolean creatureDeathLogging = false;
    public static boolean creatureCreateLogging = false;
    public static boolean itemCreateLogging = false;
    public static boolean itemRemoveLogging = false;

    public static boolean enableNewPlayerQuestion = false;
    public static boolean enableAthanorMechanism = false;
    public static boolean disableEntireMod = false;
    public static boolean disableCreatureLoot = false;
    public static boolean disableHolidayCreatures = false;
    public static boolean disableTradeShips = false;
    public static boolean disableCreatureMods = false;
    public static boolean disableItemMods = false;
    public static boolean disableVehicleMods = false;
    public static boolean disableBytecodeMods = false;
    public static boolean disableMiscMods = false;
    public static boolean disablePollingMods = false;
    public static boolean disableDiscordReliance = false;
    public static boolean disableScrollGearBinding = false;
    public static boolean disablePreInit = false;
    public static boolean disableInit = false;
    public static boolean disableOnPlayerLogin = false;
    public static boolean disableOnPlayerLogout = false;
    public static boolean disableOnServerStarted = false;
    public static boolean disableOnKingdomMessage = false;
    public static boolean disableOnPlayerMessage = false;
    public static boolean disableFogGoblins = false;
    public static boolean disableColoredUnicorns = false;
    public static boolean disableDatabaseChanges = false;
    public static boolean disableMissionChanges = false;

    public static int tileX;
    public static int tileY;
    public static int tileZ;

}
