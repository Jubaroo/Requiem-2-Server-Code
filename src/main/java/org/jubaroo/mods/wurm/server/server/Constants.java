package org.jubaroo.mods.wurm.server.server;

import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.CreatureTemplateIds;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.shared.constants.Enchants;
import com.wurmonline.shared.constants.ModelConstants;
import com.wurmonline.shared.constants.SoundNames;
import org.jubaroo.mods.wurm.server.creatures.CustomCreatures;
import org.jubaroo.mods.wurm.server.misc.templates.*;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Constants {
    //===================================================================
    //====================== Discord Constants ==========================
    //===================================================================
    public static String botToken;
    public static String serverName;
    //===================================================================
    //====================== Message Constants ==========================
    //===================================================================
    public static byte displayOnScreen = 1;
    //===================================================================
    //============================ OTHER ================================
    //===================================================================
    public static String dayOneBuffMessage = "You regenerate health faster on the first day of each month.";
    public static String daySevenBuffMessage = "Your defensive combat rating is increased on the seventh day of each month.";
    public static String dayFifteenBuffMessage = "You require food less on the fifteenth day of each month.";
    public static String dayTwentyThreeBuffMessage = "Your offensive combat rating is increased on the twenty third day of each month.";
    //===================================================================
    //============================ TITLES ================================
    //===================================================================
    //public static boolean enableCustomTitlesModule = true;
    //public static ArrayList<Misc.CustomTitle> customTitles = new ArrayList<>();
    //public static HashMap<Integer,ArrayList<String>> awardTitles = new HashMap<>();
    //===================================================================
    //============================ ITEMS ================================
    //===================================================================
    public static boolean mailboxEnableEnchant = true;
    public static float mailboxEnchantPower = 30f;
    public static boolean enableAthanorMechanism = false;
    public static boolean enableDepots = false;
    public static boolean terrainSmoothing = false;
    public static boolean changeToMycelium = false;
    public static boolean hidePlayerGodInscriptions = false;
    public static Long cashPerCorpse = 0L;
    // Mission Items
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
    public static boolean bulkCreationEntries = false;
    public static int itemCrystalCreationDamage = 1000;
    public static boolean itemHolyBook = false;
    public static boolean searchDens = false;
    public static int damageToTake = 25;
    public static boolean skullLocateUnique = false;
    public static boolean reloadSkull = false;
    public static String[] scrollids;
    public static boolean itemNymphPortal = false;
    public static boolean itemDemonPortal = false;
    public static String newsletterImage = "http://filterbubbles.com/img/wu/half-vamp-clue2.png";
    public static String[] createItemDescs = {
            "(IFBBJLjava/lang/String;)Lcom/wurmonline/server/items/Item;",
            "(IFFFFZBBJLjava/lang/String;B)Lcom/wurmonline/server/items/Item;",
    };
    public static ArrayList<Item> mobSpawners = new ArrayList<>();
    public static ArrayList<Item> resourcePoints = new ArrayList<>();
    public static ArrayList<Item> tradeTents = new ArrayList<>();
    public static ArrayList<Creature> soundEmissionNpcs = new ArrayList<>();
    public static int tradeGoodsID;
    public static int tradeTentID;
    public static String tradeTentsNorthZoneName = "Northport";
    public static String tradeTentsSouthZoneName = "Southport";
    public static long tradeTentCoinReward = 2000L;
    public static int lootBoxID;
    public static ArrayList<Item> lootCarpets = new ArrayList<>();
    public static int smallLootBoxID;
    public static int lootFlagID;
    public static int smallLootFlagID;
    private static final byte DEPO_NONE = 0;
    private static final byte DEPO_WEAPON = 1;
    private static final byte DEPO_PLATE = 2;
    private static final byte DEPO_CHAIN = 3;
    private static final byte DEPO_CLAY = 4;
    private static final byte DEPO_SAND = 5;
    private static final byte DEPO_BRICKS = 6;
    private static final byte DEPO_MORTAR = 7;
    private static final byte DEPO_ROCKS = 8;
    private static final byte DEPO_IRON = 9;
    private static final byte DEPO_STEEL = 10;
    private static final byte DEPO_LUMBER = 11;
    private static final byte DEPO_FORESTRY = 12;
    public static Location[] mechanismLocations = {
            new Location(881, 1276, DEPO_CHAIN),
            new Location(949, 1439, DEPO_CLAY),
            new Location(635, 1315, DEPO_SAND),
            new Location(652, 1112, DEPO_BRICKS),
            new Location(587, 973, DEPO_LUMBER),
            new Location(614, 851, DEPO_MORTAR),
            new Location(690, 645, DEPO_ROCKS),
            new Location(754, 568, DEPO_FORESTRY),
            new Location(810, 820, DEPO_STEEL),
            new Location(856, 859, DEPO_WEAPON),
            new Location(954, 774, DEPO_CHAIN),
            new Location(955, 602, DEPO_CLAY),
            new Location(1257, 717, DEPO_IRON),
            new Location(1323, 590, DEPO_SAND),
            new Location(1404, 712, DEPO_BRICKS),
            new Location(1336, 869, DEPO_MORTAR),
            new Location(1126, 803, DEPO_PLATE),
            new Location(1440, 1079, DEPO_LUMBER),
            new Location(1260, 1260, DEPO_STEEL),
            new Location(1413, 1434, DEPO_ROCKS),
            new Location(1198, 1349, DEPO_FORESTRY),
            new Location(1123, 1117, DEPO_WEAPON),
            new Location(879, 1193, DEPO_PLATE),
            new Location(791, 1430, DEPO_IRON)
    };

    public static StructureTemplate[] structureTemplates = {
            new StructureTemplate("Brick Factory", SoundNames.STONECUTTING_SND, ModelConstants.MODEL_SUPPLYDEPOT2, ItemList.stoneBrick, ItemList.rock, 0, 20, 1000, TimeConstants.MINUTE_MILLIS),
            new StructureTemplate("Lumber Mill", SoundNames.CARPENTRY_SAW_SND, ModelConstants.MODEL_SUPPLYDEPOT2, ItemList.plank, ItemList.log, 0, 20, 1000, TimeConstants.MINUTE_MILLIS),
            new StructureTemplate("Forestry", SoundNames.WOODCUTTING1_SND, ModelConstants.MODEL_SUPPLYDEPOT2, ItemList.log, 0, 0, 20, 1000, TimeConstants.MINUTE_MILLIS),
            new StructureTemplate("Quarry", SoundNames.MINING1_SND, ModelConstants.MODEL_SUPPLYDEPOT2, ItemList.rock, 0, 0, 20, 1000, TimeConstants.MINUTE_MILLIS),
            new StructureTemplate("Clay Pit", SoundNames.DIGGING1_SND, ModelConstants.MODEL_SUPPLYDEPOT2, ItemList.clay, 0, 0, 20, 1000, TimeConstants.MINUTE_MILLIS),
            new StructureTemplate("Sand Pit", SoundNames.DIGGING1_SND, ModelConstants.MODEL_SUPPLYDEPOT2, ItemList.sand, 0, 0, 20, 1000, TimeConstants.MINUTE_MILLIS),
            new StructureTemplate("Steel Mill", SoundNames.FLINTSTEEL_SND, ModelConstants.MODEL_SUPPLYDEPOT2, ItemList.steelBar, ItemList.ironBar, 0, 20, 1000, TimeConstants.MINUTE_MILLIS),
            new StructureTemplate("Iron Mine", SoundNames.MINING1_SND, ModelConstants.MODEL_SUPPLYDEPOT2, ItemList.ironBar, 0, 0, 20, 1000, TimeConstants.MINUTE_MILLIS),
            new StructureTemplate("Mortar Factory", SoundNames.MINING1_SND, ModelConstants.MODEL_SUPPLYDEPOT2, ItemList.mortar, ItemList.clay, ItemList.sand, 20, 1000, TimeConstants.MINUTE_MILLIS),
            new StructureTemplate("Chain Smithy", SoundNames.HAMMERONMETAL_SND, ModelConstants.MODEL_SUPPLYDEPOT2, ItemList.chainBoot, ItemList.ironBar, 0, 1, 100, TimeConstants.MINUTE_MILLIS),
            new StructureTemplate("Plate Smithy", SoundNames.HAMMERONMETAL_SND, ModelConstants.MODEL_SUPPLYDEPOT2, ItemList.plateBoot, ItemList.steelBar, 0, 1, 100, TimeConstants.MINUTE_MILLIS),
            new StructureTemplate("Weapon Smithy", SoundNames.HAMMERONMETAL_SND, ModelConstants.MODEL_SUPPLYDEPOT2, ItemList.swordLong, ItemList.ironBar, 0, 1, 100, TimeConstants.MINUTE_MILLIS),
    };

    public static SpawnerTemplate[] spawnerTemplates = {
            // Ungrouped mobs
            new SpawnerTemplate("Bandit Totem", SoundNames.HUMM_SND, ModelConstants.MODEL_FIREPLACE_CAMPFIRE, CustomCreatures.banditId, 5, TimeConstants.MINUTE_MILLIS * 10),
            // Snake mob group
            new SpawnerTemplate("Cobra Totem", SoundNames.HUMM_SND, "model.decoration.amphora.large.pottery.", CustomCreatures.cobraId, 4, TimeConstants.MINUTE_MILLIS * 10),
            new SpawnerTemplate("Viper Totem", SoundNames.HUMM_SND, "model.decoration.amphora.large.pottery.", CustomCreatures.viperId, 4, TimeConstants.MINUTE_MILLIS * 10), // add
            new SpawnerTemplate("Snake Cultist Totem", SoundNames.HUMM_SND, "model.decoration.amphora.small.pottery.", CustomCreatures.snakeCultistId, 4, TimeConstants.MINUTE_MILLIS * 10), // add
            new SpawnerTemplate("Giant Serpent Totem", SoundNames.HUMM_SND, "model.decoration.altar.silver.", CustomCreatures.giantSerpentId, 1, TimeConstants.HALF_HOUR_MILLIS * 3), // add
            // Pyramid mob group
            new SpawnerTemplate("Scorpion Totem", SoundNames.HUMM_SND, "model.decoration.altar.stone.", CreatureTemplateIds.SCORPION_CID, 2, TimeConstants.FIFTEEN_MINUTES_MILLIS),
            new SpawnerTemplate("Cave Bug Totem", SoundNames.HUMM_SND, "model.decoration.altar.stone.", CreatureTemplateIds.CAVE_BUG_CID, 2, TimeConstants.FIFTEEN_MINUTES_MILLIS),
            new SpawnerTemplate("Temple Guardian Totem", SoundNames.HUMM_SND, "model.decoration.altar.stone.", CustomCreatures.templeGuardianId, 1, TimeConstants.FIFTEEN_MINUTES_MILLIS),
            new SpawnerTemplate("Temple Priest Totem", SoundNames.HUMM_SND, "model.decoration.altar.stone.", CustomCreatures.templePriestId, 1, TimeConstants.FIFTEEN_MINUTES_MILLIS),
            new SpawnerTemplate("Temple Patriarch Totem", SoundNames.HUMM_SND, "model.decoration.altar.stone.", CustomCreatures.templePatriarchId, 1, TimeConstants.FIFTEEN_MINUTES_MILLIS),
            new SpawnerTemplate("Rift Beast Totem", SoundNames.HUMM_SND, "model.decoration.altar.stone.", CreatureTemplateIds.RIFT_JACKAL_ONE_CID, 1, TimeConstants.FIFTEEN_MINUTES_MILLIS),
            // Goblin mob group
            new SpawnerTemplate("Goblin Totem", SoundNames.HUMM_SND, ModelConstants.MODEL_FIREPLACE_CAMPFIRE, CreatureTemplateIds.GOBLIN_CID, 4, TimeConstants.MINUTE_MILLIS * 10),
            new SpawnerTemplate("Rat Totem", SoundNames.HUMM_SND, ModelConstants.MODEL_CONTAINER_TRASHBIN, CreatureTemplateIds.RAT_LARGE_CID, 2, TimeConstants.MINUTE_MILLIS * 10),
            new SpawnerTemplate("Goblin Warlord Totem", SoundNames.HUMM_SND, ModelConstants.MODEL_FIREPLACE_CAMPFIRE, CustomCreatures.goblinWarlordId, 1, TimeConstants.MINUTE_MILLIS * 20), // add
            new SpawnerTemplate("Troll Totem", SoundNames.HUMM_SND, ModelConstants.MODEL_FIREPLACE_CAMPFIRE, CreatureTemplateIds.TROLL_CID, 1, TimeConstants.MINUTE_MILLIS * 20),
            // Undead mob group
            new SpawnerTemplate("Wraith Totem", SoundNames.HUMM_SND, "model.decoration.altar.stone.", CustomCreatures.wraithId, 1, TimeConstants.MINUTE_MILLIS * 10), // add
            new SpawnerTemplate("Zombie Totem", SoundNames.HUMM_SND, "model.decoration.altar.stone.", CustomCreatures.fleshwalkerId, 4, TimeConstants.MINUTE_MILLIS * 10),
            new SpawnerTemplate("Skeleton Totem", SoundNames.HUMM_SND, "model.furniture.coffin.stone.", CustomCreatures.bonewalkerId, 4, TimeConstants.MINUTE_MILLIS * 10),
            new SpawnerTemplate("Necromancer Totem", SoundNames.HUMM_SND, "model.decoration.altar.silver.", CustomCreatures.necromancerId, 1, TimeConstants.HALF_HOUR_MILLIS * 3), // add
            // Fire mob group
            new SpawnerTemplate("Lavafiend Totem", SoundNames.HUMM_SND, "model.resource.boulder.adamantine.", CreatureTemplateIds.LAVA_CREATURE_CID, 2, TimeConstants.MINUTE_MILLIS * 20),
            new SpawnerTemplate("Fire Spider Totem", SoundNames.HUMM_SND, "model.resource.boulder.adamantine.", CustomCreatures.fireSpiderId, 2, TimeConstants.MINUTE_MILLIS * 20),
            new SpawnerTemplate("Hell hound Totem", SoundNames.HUMM_SND, "model.resource.ore.adamantine.", CreatureTemplateIds.HELL_HOUND_CID, 2, TimeConstants.MINUTE_MILLIS * 20),
            new SpawnerTemplate("Hell scorpious Totem", SoundNames.HUMM_SND, "model.resource.boulder.adamantine.", CreatureTemplateIds.HELL_SCORPION_CID, 2, TimeConstants.MINUTE_MILLIS * 20),
            new SpawnerTemplate("Sol demon Totem", SoundNames.HUMM_SND, "model.resource.boulder.adamantine.", CreatureTemplateIds.DEMON_SOL_CID, 2, TimeConstants.MINUTE_MILLIS * 20),
            // HotA group
            new SpawnerTemplate("Troll HotA Totem", SoundNames.HUMM_SND, "model.resource.boulder.adamantine.", CreatureTemplateIds.TROLL_CID, 5, TimeConstants.MINUTE_MILLIS * 10),
            new SpawnerTemplate("Cyclops HotA Totem", SoundNames.HUMM_SND, "model.resource.boulder.adamantine.", CustomCreatures.cyclopsId, 5, TimeConstants.MINUTE_MILLIS * 10),
            new SpawnerTemplate("Fire Giant HotA Totem", SoundNames.HUMM_SND, "model.resource.boulder.adamantine.", CustomCreatures.fireGiantId, 5, TimeConstants.MINUTE_MILLIS * 10),
            new SpawnerTemplate("Gorilla HotA Totem", SoundNames.HUMM_SND, "model.resource.boulder.adamantine.", CustomCreatures.gorillaId, 5, TimeConstants.MINUTE_MILLIS * 10),
            new SpawnerTemplate("Snakevine HotA Totem", SoundNames.HUMM_SND, "model.resource.boulder.adamantine.", CustomCreatures.snakeVineId, 5, TimeConstants.MINUTE_MILLIS * 10),
            new SpawnerTemplate("Tomb Raider HotA Totem", SoundNames.HUMM_SND, "model.resource.boulder.adamantine.", CustomCreatures.tombRaiderId, 5, TimeConstants.MINUTE_MILLIS * 10),
            new SpawnerTemplate("Large Boar HotA Totem", SoundNames.HUMM_SND, "model.resource.boulder.adamantine.", CustomCreatures.largeBoarId, 5, TimeConstants.MINUTE_MILLIS * 10),
            new SpawnerTemplate("Blob HotA Totem", SoundNames.HUMM_SND, "model.resource.boulder.adamantine.", CustomCreatures.blobId, 5, TimeConstants.MINUTE_MILLIS * 10),
            new SpawnerTemplate("Spirit Troll HotA Totem", SoundNames.HUMM_SND, "model.resource.boulder.adamantine.", CustomCreatures.spiritTrollId, 5, TimeConstants.MINUTE_MILLIS * 10),
            new SpawnerTemplate("Hyena HotA Totem", SoundNames.HUMM_SND, "model.resource.boulder.adamantine.", CustomCreatures.hyenaId, 5, TimeConstants.MINUTE_MILLIS * 10),
            new SpawnerTemplate("Deathcrawler HotA Totem", SoundNames.HUMM_SND, "model.resource.boulder.adamantine.", CreatureTemplateIds.DEATHCRAWLER_MINION_CID, 5, TimeConstants.MINUTE_MILLIS * 10),
            new SpawnerTemplate("Rift Ogre Magi HotA Totem", SoundNames.HUMM_SND, "model.resource.boulder.adamantine.", CreatureTemplateIds.RIFT_OGRE_MAGE_CID, 5, TimeConstants.MINUTE_MILLIS * 10),
            new SpawnerTemplate("Rift Summoner HotA Totem", SoundNames.HUMM_SND, "model.resource.boulder.adamantine.", CreatureTemplateIds.RIFT_JACKAL_SUMMONER_CID, 5, TimeConstants.MINUTE_MILLIS * 10),
    };

    public static ScrollTemplate[] scrollTemplates = {
            new ScrollTemplate("faith scroll", "A scroll that grants faith.", ModelConstants.MODEL_WRIT, SkillList.FAITH, 10.0f),
            new ScrollTemplate("body strength scroll", "A scroll that grants body strength.", ModelConstants.MODEL_WRIT, SkillList.BODY_STRENGTH, 1.0f),
            new ScrollTemplate("body stamina scroll", "A scroll that grants body stamina.", ModelConstants.MODEL_WRIT, SkillList.BODY_STAMINA, 1.0f),
            new ScrollTemplate("body control scroll", "A scroll that grants body control", ModelConstants.MODEL_WRIT, SkillList.BODY_CONTROL, 1.0f),
            new ScrollTemplate("soul strength scroll", "A scroll that grants soul strength.", ModelConstants.MODEL_WRIT, SkillList.SOUL_STRENGTH, 1.0f),
            new ScrollTemplate("soul depth scroll", "A scroll that grants soul depth.", ModelConstants.MODEL_WRIT, SkillList.SOUL_DEPTH, 1.0f),
            new ScrollTemplate("mind logic scroll", "A scroll that grants mind logic.", ModelConstants.MODEL_WRIT, SkillList.MIND_LOGICAL, 1.0f),
            new ScrollTemplate("mind speed scroll", "A scroll that grants mind speed.", ModelConstants.MODEL_WRIT, SkillList.MIND_SPEED, 1.0f),
            new ScrollTemplate("affinity scroll", "A scroll that grants a random affinity.", ModelConstants.MODEL_WRIT, 0, 0.2f),
    };

    public static EnchantScrollTemplate[] enchantScrollTemplates = {
            new EnchantScrollTemplate("aura of shared pain scroll", "A scroll on strange green and white parchment.", ModelConstants.MODEL_WRIT, Enchants.BUFF_SHARED_PAIN),
            new EnchantScrollTemplate("life transfer scroll", "A scroll on strange green and white parchment.", ModelConstants.MODEL_WRIT, Enchants.BUFF_LIFETRANSFER),
            new EnchantScrollTemplate("nimbleness scroll", "A scroll on strange green and white parchment.", ModelConstants.MODEL_WRIT, Enchants.BUFF_NIMBLENESS),
            new EnchantScrollTemplate("wind of ages scroll", "A scroll on strange green and white parchment.", ModelConstants.MODEL_WRIT, Enchants.BUFF_WIND_OF_AGES),
            new EnchantScrollTemplate("frostbrand scroll", "A scroll on strange green and white parchment.", ModelConstants.MODEL_WRIT, Enchants.BUFF_FROSTBRAND),
            new EnchantScrollTemplate("mindstealer scroll", "A scroll on strange green and white parchment.", ModelConstants.MODEL_WRIT, Enchants.BUFF_MINDSTEALER),
            new EnchantScrollTemplate("venom scroll", "A scroll on strange green and white parchment.", ModelConstants.MODEL_WRIT, Enchants.BUFF_VENOM),
            new EnchantScrollTemplate("flaming aura scroll", "A scroll on strange green and white parchment.", ModelConstants.MODEL_WRIT, Enchants.BUFF_FLAMING_AURA),
            new EnchantScrollTemplate("circle of cunning scroll", "A scroll on strange green and white parchment.", ModelConstants.MODEL_WRIT, Enchants.BUFF_CIRCLE_CUNNING),
            new EnchantScrollTemplate("web armour scroll", "A scroll on strange green and white parchment.", ModelConstants.MODEL_WRIT, Enchants.BUFF_WEBARMOUR),
            new EnchantScrollTemplate("rotting touch scroll", "A scroll on strange green and white parchment.", ModelConstants.MODEL_WRIT, Enchants.BUFF_ROTTING_TOUCH),
            new EnchantScrollTemplate("bloodthirst scroll", "A scroll on strange green and white parchment.", ModelConstants.MODEL_WRIT, Enchants.BUFF_BLOODTHIRST),
            new EnchantScrollTemplate("blessings of the dark scroll", "A scroll on strange green and white parchment.", ModelConstants.MODEL_WRIT, Enchants.BUFF_BLESSINGDARK),
    };

    //===================================================================
    //=========================== POLLING ===============================
    //===================================================================
    public static final long pollTitanSpawnTime = TimeConstants.MINUTE_MILLIS * 2;
    public static final long pollTitanTime = TimeConstants.SECOND_MILLIS;
    //static final long pollClucksterTime = TimeConstants.SECOND_MILLIS;
    public static final long pollDepotTime = TimeConstants.MINUTE_MILLIS;
    public static final long pollRareSpawnTime = TimeConstants.MINUTE_MILLIS * 5;
    public static final long pollMissionCreatorTime = TimeConstants.HOUR_MILLIS * 4;
    public static final long pollBloodlustTime = TimeConstants.MINUTE_MILLIS;
    public static final long pollUniqueRegenerationTime = TimeConstants.SECOND_MILLIS;
    public static final long pollUniqueCollectionTime = TimeConstants.MINUTE_MILLIS * 5;
    public static final long pollTerrainSmoothTime = TimeConstants.SECOND_MILLIS * 5;
    public static final long pollTerrainChangeToMyceliumTime = TimeConstants.SECOND_MILLIS * 5;
    public static long lastSecondPolled = 0;
    public static long lastPolledTitanSpawn = 0;
    public static long lastPolledTitans = 0;
    public static long lastPolledDepots = 0;
    public static long lastPolledRareSpawns = 0;
    public static long lastPolledEternalReservoirs = 0;
    public static long lastPolledMissionCreator = 0;
    public static long lastPolledBloodlust = 0;
    public static long lastPolledUniqueRegeneration = 0;
    public static long lastPolledUniqueCollection = 0;
    public static long lastPolledTerrainSmooth = 0;
    public static long lastPolledChangeToMycelium = 0;
    // ========= Testing =========
    public static long testDelayMyceliumChange = TimeConstants.MINUTE_MILLIS;
    public static long testDelayWallRepair = TimeConstants.MINUTE_MILLIS;
    public static long testDelayCullHotA = TimeConstants.MINUTE_MILLIS;
    public static long testDelayHolidayMessage = TimeConstants.MINUTE_MILLIS * 5;
    public static long testDelayFogGoblins = TimeConstants.MINUTE_MILLIS;
    public static long testDelayTradeTents = TimeConstants.MINUTE_MILLIS;
    public static long testDelayResourcePoints = TimeConstants.MINUTE_MILLIS;
    public static long testDelayLootCarpets = TimeConstants.MINUTE_MILLIS;
    public static long testDelayMobSpawners = TimeConstants.MINUTE_MILLIS;
    public static long testDelayAthanorMechanism = TimeConstants.MINUTE_MILLIS * 2;
    public static long testDelayAthanorMechanismPoll = TimeConstants.SECOND_MILLIS * 10;
    public static long testDelayRepairingNpcs = TimeConstants.SECOND_MILLIS * 10;
    // ======== Live =========
    public static long delayWallRepair = TimeConstants.MINUTE_MILLIS * 5;
    public static long delayCullHotA = TimeConstants.DAY_MILLIS;
    public static long delayMyceliumChange = TimeConstants.HOUR_MILLIS * 5;
    public static long delayHolidayMessage = TimeConstants.DAY_MILLIS;
    public static long delayFogGoblins = TimeConstants.MINUTE_MILLIS * 5;
    public static long delayTradeTents = TimeConstants.MINUTE_MILLIS * 10;
    public static long delayResourcePoints = TimeConstants.MINUTE_MILLIS;
    public static long delayLootCarpets = TimeConstants.HOUR_MILLIS * 10;
    public static long delayMobSpawners = TimeConstants.MINUTE_MILLIS * 10;
    public static long delayAthanorMechanism = TimeConstants.HOUR_MILLIS * 4;
    public static long delayAthanorMechanismPoll = TimeConstants.MINUTE_MILLIS * 2;
    public static long delayRepairingNpcs = TimeConstants.MINUTE_MILLIS;
    public static long lastPolledWallRepair = 0;
    public static long lastPolledCullHotA = 0;
    public static long lastPolledMyceliumChange = 0;
    public static long lastPolledHolidayMessage = 0;
    public static long lastPolledFogGoblins = 0;
    public static long lastPolledTradeTents = 0;
    public static long lastPolledResourcePoints = 0;
    public static long lastPolledLootCarpets = 0;
    public static long lastPolledMobSpawners = 0;
    public static long lastPolledAthanorMechanism = 0;
    public static long lastPolledAthanorMechanismPoll = 0;
    public static long lastPolledRepairingNpcs = 0;
    //===================================================================
    //========================== CREATURES ==============================
    //===================================================================
    public static int HUNDRED_PERCENT_HEALTH = 65535;
    public static int EIGHTY_EIGHT_PERCENT_HEALTH = 57344;
    public static int SEVENTY_FIVE_PERCENT_HEALTH = 49152;
    public static int SIXTY_PERCENT_HEALTH = 39321;
    public static int FIFTY_PERCENT_HEALTH = 32767;
    public static int FORTY_PERCENT_HEALTH = 26214;
    public static int THIRTY_PERCENT_HEALTH = 19661;
    public static int TWENTY_PERCENT_HEALTH = 13017;
    public static int TEN_PERCENT_HEALTH = 6554;
    public static int EIGHTY_EIGHT_PERCENT_DAMAGE = 8191;
    public static int SEVENTY_FIVE_PERCENT_DAMAGE = 16383;
    public static int SIXTY_PERCENT_DAMAGE = 26214;
    public static int FIFTY_PERCENT_DAMAGE = 32767;
    public static int FORTY_PERCENT_DAMAGE = 39321;
    public static int THIRTY_PERCENT_DAMAGE = 45874;
    public static int TWENTY_PERCENT_DAMAGE = 52428;
    public static int TEN_PERCENT_DAMAGE = 58981;
    public static float uniquex = 0f;
    public static float uniquey = 0f;
    public static String uniquename = "Noname";
    public static String[] bossids;
    public static boolean treasureGoblin = false;
    public static boolean uniques = false;
    public static boolean customMounts = false;
    public static boolean undead = false;
    public static boolean farmAnimals = false;
    public static boolean monsters = false;
    public static boolean npc = false;
    public static boolean titans = false;
    public static boolean humans = false;
    public static boolean animals = false;
    public static boolean wyverns = false;
    public static boolean halloweenMobs = false;
    public static boolean christmasMobs = false;
    public static boolean milkWhales = false;
    //static long lastPolledCluckster = 0;
    // Conquest stuff
    public static boolean initialGoblinCensus = false;
    public static int maxFogGoblins = 100;
    public static ArrayList<Creature> fogGoblins = new ArrayList<>();
    //===================================================================
    //=========================== JOUSTING ==============================
    //===================================================================
    public static int LanceDamage;
    public static int BaseHitChance;
    public static int SpearSkillRange;
    public static int BonusLanceDamage;
    public static int LoseHelmetChance;
    public static float PerKMDamageBoost;
    public static boolean AllowSkillGain;
    public static boolean AllowCraftingLance;
    public static float LanceRange;
    //===================================================================
    //============================= SKILLS ==============================
    //===================================================================
    public static int skillTaxidermy = 11000;
    public static int skillStuffing = 11001;
    public static int skillTanning = 11002;
    public static int skillMounting = 11003;
    public static int skillGrooming = 11004;
    public static int skillGemCrafting = 11005;
    //===================================================================
    //=========================== VEHICLES ==============================
    //===================================================================
    public static boolean wagons = false;
    //public static boolean wagonKits = false;
    public static boolean sailingBoats = false;
    public static boolean cogs = false;
    public static boolean caravels = false;
    public static boolean knarrs = false;
    public static boolean corbitas = false;
    public static boolean magicCarpet = false;
    public static boolean vehicleCreationEntries = false;

    public static final String[] CARPET_LIST = {
            "model.decoration.colorful.carpet.medium"
    };

    public static final String[] CARPET_NAMES = {
            "Hovering"
    };

    //===================================================================
    //============================ OTHER ================================
    //===================================================================
    public static boolean addMissionItems = false;
    public static boolean addGmProtect = false;
    public static boolean addGmUnprotect = false;
    public static boolean addSpawnGuard = false;
    public static boolean gmFullFavor = false;
    public static boolean gmFullStamina = false;
    public static boolean setUnicornIsHorse = false;
    public static boolean addCommands = false;
    public static boolean enableMyceliumSpread = false;
    public static boolean addLabyrinth = false;
    public static boolean stfuNpcs = false;
    public static boolean noMineDrift = false;
    public static boolean allSurfaceMine = false;
    public static boolean lampsAutoLight = false;
    public static boolean allowTentsOnDeed = false;
    public static boolean loadFullContainers = false;
    public static long tmpExecutionStartTime = 0L;
    public static boolean actPortalDone = false;
    public static DecimalFormat df = new DecimalFormat("###,###,###,###");
    public static DecimalFormat executionLogDf = new DecimalFormat("#.#########");
    public static String makeNonAggro = "";
    public static String noCooldownSpells = "";
    public static final String[] randomServerGreetings = {
            "Put that :cookie: down, the server is back online!",
            "I come in peace! :peace: *hides a shod club behind his back* :smiling_imp:",
            "What’s kickin’, little chicken? :chicken: Server is back to pumping out those eggs! :egg:",
            "Hey, howdy, hi! Lets all eat some pie... Server is back to baking. :pizza:",
            "Hello, sunshine! Come play, the server is waiting for you. :sun_with_face:",
            "Welcome back to the server!",
            "My name is Inigo Montoya. You killed my father. Prepare to die. :crossed_swords~1:",
            "What's cookin', good lookin'? Get in the game and show me! :shallow_pan_of_food:",
            "Hamsters are now well fed. :hamster:", // Oluf
            "HEY YOU!!! THE SERVER IS BACK ONLINE!!! THE SERVER IS BACK ONLINE!!!",
            "Psst! The server is back up, go play.",
            "Welcome back to another great day in Requiem!",
            "Time for more Requiem of Wurm!!!"
    };

    public static final String[] randomServerGoodbyes = {
            "Saying goodbye to someone, just means that there’s a hello waiting around the corner.",
            "Goodbyes always make my throat hurt? I need more hellos. -Charlie Brown",
            "I will not say goodbye to you! I’ll say see you soon! :wave:",
            "How lucky I am to have something that makes saying Goodbye so hard. -Winnie the Pooh",
            "So long, and thanks for all the fish! :dolphin:",
            "Smoke me a kipper, Ill be back for breakfast.",
            "Catch you on the flippity-flip.",
            "Hasta la vista, baby.",
            "Parting is such sweet sorrow, that I shall say good night till it be tomorrow. :zzz:",
            "Someone forgot to feed the hamsters :hamster:, server is shutting down to fix that. :cheese:", // Oluf
            "We shall return after a brief message from our sponsors...",
            "Don't touch that remote, we will be right back!",
            "Don't even think about going and doing anything in the real world, wait in that chair until I am back online!!!"
    };
    //===================================================================
    //=========================== LOGGING ===============================
    //===================================================================
    public static String VERSION = "2.1";
    public static boolean executionCostLogging = true;
    public static boolean debug = false;
    public static boolean logExecutionCost = true;
    public static boolean creatureDeathLogging = false;
    public static boolean creatureCreateLogging = false;
    public static boolean itemCreateLogging = false;
    public static boolean itemRemoveLogging = false;
    //==========================================================================
    //=========================== ENABLE/DISABLE ===============================
    //==========================================================================
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

}
