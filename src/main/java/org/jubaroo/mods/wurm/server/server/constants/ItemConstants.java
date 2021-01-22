package org.jubaroo.mods.wurm.server.server.constants;

import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.creatures.CreatureTemplateIds;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.shared.constants.Enchants;
import com.wurmonline.shared.constants.ModelConstants;
import com.wurmonline.shared.constants.SoundNames;
import org.jubaroo.mods.wurm.server.creatures.CustomCreatures;
import org.jubaroo.mods.wurm.server.misc.templates.*;

import java.util.ArrayList;

public class ItemConstants {
    // Mission Items
    public static String[] scrollids;
    public static String[] createItemDescs = {
            "(IFBBJLjava/lang/String;)Lcom/wurmonline/server/items/Item;",
            "(IFFFFZBBJLjava/lang/String;B)Lcom/wurmonline/server/items/Item;",
    };
    public static ArrayList<Item> mobSpawners = new ArrayList<>();
    public static ArrayList<Item> resourcePoints = new ArrayList<>();
    public static ArrayList<Item> tradeTents = new ArrayList<>();
    public static int tradeGoodsID;
    public static int tradeTentID;
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

}
