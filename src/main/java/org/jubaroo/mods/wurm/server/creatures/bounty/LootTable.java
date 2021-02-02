package org.jubaroo.mods.wurm.server.creatures.bounty;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.FailedException;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Server;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.CreatureTemplate;
import com.wurmonline.server.creatures.CreatureTemplateFactory;
import com.wurmonline.server.creatures.CreatureTemplateIds;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.NoSuchTemplateException;
import com.wurmonline.server.players.Titles;
import com.wurmonline.server.sounds.SoundPlayer;
import com.wurmonline.shared.constants.SoundNames;
import net.bdew.wurm.tools.server.loot.LootDrop;
import net.bdew.wurm.tools.server.loot.LootManager;
import net.bdew.wurm.tools.server.loot.LootRule;
import org.gotti.wurmunlimited.modsupport.IdFactory;
import org.gotti.wurmunlimited.modsupport.IdType;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.communication.discord.DiscordHandler;
import org.jubaroo.mods.wurm.server.creatures.CreatureSpawns;
import org.jubaroo.mods.wurm.server.creatures.CreatureTweaks;
import org.jubaroo.mods.wurm.server.creatures.CustomCreatures;
import org.jubaroo.mods.wurm.server.creatures.Titans;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.misc.CustomTitles;
import org.jubaroo.mods.wurm.server.server.constants.CreatureConstants;
import org.jubaroo.mods.wurm.server.tools.CreatureTools;
import org.jubaroo.mods.wurm.server.tools.ItemTools;
import org.jubaroo.mods.wurm.server.tools.RandomUtils;
import org.jubaroo.mods.wurm.server.tools.database.DatabaseHelper;
import org.jubaroo.mods.wurm.server.tools.database.holidays.Holidays;

import static org.jubaroo.mods.wurm.server.ModConfig.creatureDeathLogging;
import static org.jubaroo.mods.wurm.server.ModConfig.disableHolidayCreatures;
import static org.jubaroo.mods.wurm.server.creatures.CustomCreatures.*;
import static org.jubaroo.mods.wurm.server.tools.ItemTools.makeRarity;

public class LootTable {

    private static int riftLootChance(Creature c) {
        switch (c.getTemplateId()) {
            case CreatureTemplateIds.RIFT_JACKAL_ONE_CID:
                return 100;
            case CreatureTemplateIds.RIFT_JACKAL_TWO_CID:
                return 30;
            case CreatureTemplateIds.RIFT_JACKAL_THREE_CID:
            case CreatureTemplateIds.RIFT_JACKAL_CASTER_CID:
            case CreatureTemplateIds.RIFT_OGRE_MAGE_CID:
            case CreatureTemplateIds.RIFT_JACKAL_SUMMONER_CID:
            case CreatureTemplateIds.SPAWN_UTTACHA_CID:
            case CreatureTemplateIds.DEMON_SOL_CID:
            case CreatureTemplateIds.DEATHCRAWLER_MINION_CID:
                return 10;
            case CreatureTemplateIds.RIFT_JACKAL_FOUR_CID:
                return 2;
            case CreatureTemplateIds.DRAKESPIRIT_CID:
            case CreatureTemplateIds.EAGLESPIRIT_CID:
            case CreatureTemplateIds.SON_OF_NOGUMP_CID:
                return 5;
            default:
                return -1;
        }
    }

    public static void spawnFriyanTablets(int min, int max) {
        //TODO prevent them from spawning in villages or buildings
        int i = Server.rand.nextInt((max - min) + 1) + min;
        while (i > 0) {
            int x = Server.rand.nextInt(Server.surfaceMesh.getSize());
            int y = Server.rand.nextInt(Server.surfaceMesh.getSize());
            short height = Tiles.decodeHeight(Server.surfaceMesh.getTile(x, y));
            if (height > 0 && height < 1000 && Creature.getTileSteepness(x, y, true)[1] < 30) {
                try {
                    ItemFactory.createItem(CustomItems.friyanTabletId, 80f + Server.rand.nextInt(20), (float) x * 4, (float) y * 4, Server.rand.nextFloat() * 360f, true, (byte) 0, -10, "Friyanouce");
                    RequiemLogging.logInfo(String.format("Created a Tablet of Friyan at %d, %d.", x, y));
                } catch (NoSuchTemplateException | FailedException e) {
                    RequiemLogging.logException("[Error] in spawnFriyanTablets in LootTable", e);
                }
                i--;
            }
        }
        String message = String.format("%d Tablets of Friyan were created somewhere in the world!", i);
        Server.getInstance().broadCastAlert(message);
        DiscordHandler.sendToDiscord(CustomChannel.EVENTS, message);
    }

    /**
     * Here we decide what happens when a creature dies
     */
    public static void creatureDied() {
        // Uniques
        LootManager.add(LootRule.create()
                        .requireUnique()
                        .addTrigger((c, k) -> DatabaseHelper.addPlayerStat(k.getName(), "UNIQUES"))
                        // Spawn Undead Creature
                        .addSubRule(LootRule.create()
                                .chance(c -> c.getStatus().isChampion() ? 0.5f : 0.1f)
                                .addTriggerOnce((c, k) -> {
                                    try {
                                        CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(RandomUtils.getRandArrayInt(CreatureTools.randomUndead));
                                        byte ctype = (byte) Math.max(0, Server.rand.nextInt(17) - 5);
                                        Creature.doNew(template.getTemplateId(), true, c.getPosX(), c.getPosY(), Server.rand.nextFloat() * 360f, c.getLayer(), template.getName(), (byte) 0, c.getKingdomId(), ctype, false, (byte) 60);
                                    } catch (Exception e) {
                                        RequiemLogging.logException("[Error] in spawning undead in creatureDied in LootTable", e);
                                    }
                                    Server.getInstance().broadCastAlert("An undead creature is released from the underworld to claim the soul of a powerful creature that was just slain!");
                                })
                                .commTrigger((c, comm) -> comm.sendNormalServerMessage("An undead creature is released from the underworld to claim the soul of a powerful creature that was just slain!"))
                        )
                        // Tomes
                        .addSubRule(LootRule.create()
                                .chance(c -> c.getStatus().isChampion() ? 0.33f : 0.1f)
                                .addDrop(LootDrop.create((c, k) -> ItemList.bloodAngels + Server.rand.nextInt(16))
                                        .rarity(MiscConstants.RARE)
                                        .aux((byte) 2)
                                )
                        )
                        // Pet eggs
                        .addSubRule(LootRule.create()
                                .chance(0.1f)
                                .addDrop(LootDrop.create(IdFactory.getIdFor("bdew.pets.egg", IdType.ITEMTEMPLATE))
                                        .rarity((c, k) -> Server.rand.nextInt(3) == 0 ? MiscConstants.FANTASTIC : MiscConstants.RARE)
                                )
                        )
                        // Friyan Tablets
                        .addSubRule(LootRule.create()
                                .chance(0.1f)
                                .addTriggerOnce((c, k) -> spawnFriyanTablets(1, 10))
                        )
                        // dragon slayer trophy
                        .addSubRule(LootRule.create()
                                .requireUnique()
                                .requireCreature(Creature::isDragon)
                                .addDrop(LootDrop.create(ItemList.goldChallengeStatue)
                                        .rarity(MiscConstants.RARE)
                                        .weight(1500)
                                        .name((c, k) -> String.format("%s slayer trophy", c.getTemplate().getName().toLowerCase()))
                                )
                        )
                //.addTrigger((c, k) -> k.achievement(MyAchievementIds.FRESH_DRAGON_SLAYER))
        );

        // fire crystal fragments
        LootManager.add(LootRule.create()
                .requireTemplateIds(CustomCreatures.fireCrabId, CustomCreatures.fireGiantId, CreatureTemplateIds.LAVA_CREATURE_CID, CreatureTemplateIds.LAVA_SPIDER_CID, CreatureTemplateIds.HELL_HOUND_CID, CreatureTemplateIds.HELL_SCORPION_CID)
                .chance((c -> c.getStatus().isChampion() ? 1f : 0.2f))
                .addDrop(LootDrop.create(ItemList.itemFragment)
                        .data1(1)
                        .data2(0)
                        .aux((byte) 1)
                        .weight(CustomItems.lesserFireCrystal.getWeightGrams() / CustomItems.lesserFireCrystal.getFragmentAmount())
                )
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You grab some kind of fragment from the corpse of %s.", c.getName())))
        );

        // Champions
        LootManager.add(
                LootRule.create()
                        .requireCreature(Creature::isChampion)
                        .addSubRule(LootRule.create()
                                .chance(0.20f)
                                .addDrop(LootDrop.create(Server.rand.nextBoolean() ? ItemList.adamantineBar : ItemList.glimmerSteelBar)
                                        .ql((c, k) -> Server.rand.nextFloat() * 100)
                                )
                        )
                        .addSubRule(LootRule.create()
                                .chance(0.05f)
                                .addDrop(LootDrop.create(ItemList.boneCollar)
                                        .rarity((c, k) -> makeRarity(50, true))
                                )
                        )
                        .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You find something useful on the corpse of %s.", c.getNameWithoutPrefixes())))
        );

        // Treasure Goblins
        LootManager.add(
                LootRule.create()
                        .requireTemplateIds(CustomCreatures.treasureGoblinId, CustomCreatures.treasureGoblinBloodThiefId, CustomCreatures.treasureGoblinGemHoarderId, CustomCreatures.treasureGoblinBloodThiefId, CustomCreatures.treasureGoblinMenageristGoblinId, CustomCreatures.treasureGoblinRainbowGoblinId, CustomCreatures.treasureGoblinMalevolentTormentorId, CustomCreatures.treasureGoblinOdiousCollectorId)
                        .addSubRule(LootRule.create()
                                .chance(0.5f)
                                .addDrop(LootDrop.create(ItemList.boneCollar)
                                        .rarity((c, k) -> makeRarity(50, true))
                                )
                        )
                        .addSubRule(LootRule.create()
                                .chance(0.5f)
                                .addDrop(LootDrop.create(CustomItems.affinityOrbId))
                        )
                        .addSubRule(LootRule.create()
                                .chance(0.5f)
                                .addDrop(LootDrop.create(CustomItems.gemCache.getTemplateId()))
                        )
                        .addSubRule(LootRule.create()
                                .requireTemplateIds(CustomCreatures.treasureGoblinId)
                                .addDrop(LootDrop.create(CustomItems.treasureBoxId)
                                        .rarity((c, k) -> makeRarity(75, true)))
                        )
                        .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You find some useful things on the corpse of %s.", c.getNameWithoutPrefixes())))
                        .addTrigger((c, k) -> {
                            DiscordHandler.sendToDiscord(CustomChannel.EVENTS, String.format("A %s has been killed by %s.", c.getNameWithoutPrefixes(), k.getNameWithoutPrefixes()));
                            k.addTitle(Titles.Title.getTitle(CustomTitles.TREASURE_GOBLIN));
                            DatabaseHelper.addPlayerStat(k.getName(), "GOBLINS");
                        })
                        .addSubRule(LootRule.create()
                                .requireTemplateIds(CustomCreatures.treasureGoblinMenageristGoblinId)
                                .addDrop(LootDrop.create(IdFactory.getIdFor("bdew.pets.egg", IdType.ITEMTEMPLATE))
                                        .rarity((c, k) -> Server.rand.nextInt(3) == 0 ? MiscConstants.FANTASTIC : MiscConstants.RARE))
                        )
                        .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You find some useful things on the corpse of %s.", c.getNameWithoutPrefixes())))
                        .addTrigger((c, k) -> {
                            DiscordHandler.sendToDiscord(CustomChannel.EVENTS, String.format("A %s has been killed by %s.", c.getNameWithoutPrefixes(), k.getNameWithoutPrefixes()));
                            k.addTitle(Titles.Title.getTitle(CustomTitles.TREASURE_GOBLIN));
                            DatabaseHelper.addPlayerStat(k.getName(), "GOBLINS");
                        })
        );

        // Rift loot
        LootManager.add(LootRule.create()
                .requireCreature(c -> riftLootChance(c) > 0)
                .addTrigger((c, k) -> {
                    //MyAchievements.triggerAchievement(k, MyAchievements.riftSlayer);
                    if (c.getTemplateId() == CreatureTemplateIds.RIFT_JACKAL_FOUR_CID)
                        k.addTitle(Titles.Title.getTitle(CustomTitles.RIFT_SLAYER));
                })
                .chance((c) -> 1f / riftLootChance(c))
                .addSubRule(LootRule.create()
                        .addDrop(LootDrop.create(ItemList.sleepPowder)
                                .repeat(2)
                        )
                        //.addTrigger((c, k) -> MyAchievements.triggerAchievement(k, MyAchievements.othRiftShard))
                        .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You grab some sleep powder from the corpse of %s.", c.getName())))
                )
                .chance((c) -> 3f / riftLootChance(c))
                .addSubRule(LootRule.create()
                        .addDrop(LootDrop.create(ItemList.seryllBar)
                                .ql((c, k) -> 90f + Server.rand.nextFloat() * 10)
                                .rarity((c, k) -> makeRarity(25, false))
                        )
                        .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You grab a lump of strange metal from the corpse of %s.", c.getName())))
                )
        );

        // fire crystal fragments
        LootManager.add(LootRule.create()
                .requireTemplateIds(CustomCreatures.fireCrabId, CustomCreatures.fireGiantId, CreatureTemplateIds.LAVA_CREATURE_CID, CreatureTemplateIds.LAVA_SPIDER_CID, CreatureTemplateIds.HELL_HOUND_CID, CreatureTemplateIds.HELL_SCORPION_CID)
                .chance((c -> c.getStatus().isChampion() ? 1f : 0.2f))
                .addDrop(LootDrop.create(ItemList.itemFragment)
                        .data1(1)
                        .data2(0)
                        .aux((byte) 1)
                        .weight(CustomItems.lesserFireCrystal.getWeightGrams() / CustomItems.lesserFireCrystal.getFragmentAmount()))
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You grab some kind of fragment from the corpse of %s.", c.getName())))
        );

        // Black Knight
        LootManager.add(
                LootRule.create()
                        .requireTemplateIds(CustomCreatures.blackKnightId)
                        .addTrigger((c, k) -> ItemTools.createEnchantOrb(110))
                        .addTrigger((c, k) -> ItemTools.createRandomSorcery((byte) 1))
                        .addDrop(LootDrop.create(CustomItems.treasureBoxId)
                                .rarity((c, k) -> makeRarity(25, true))
                        )
                        .addDrop(LootDrop.create(RandomUtils.randomToolWeaponTemplates())
                                .rarity((c, k) -> makeRarity(65, true))
                        )
                        .addDrop(LootDrop.create(RandomUtils.randomGem(true))
                                .rarity((c, k) -> makeRarity(33, true))
                        )
                        .addDrop(LootDrop.create(CustomItems.artifactCache.getTemplateId())
                                .rarity((c, k) -> makeRarity(33, true))
                        )
                        .addDrop(LootDrop.create(RandomUtils.randomMaterialConstructionTemplates())
                                .rarity((c, k) -> makeRarity(33, false))
                                .repeat(6)
                                .ql((c, k) -> Server.rand.nextFloat() * 100)
                        )
                        .addDrop(LootDrop.create(CustomItems.artifactCache.getTemplateId())
                        )
                        .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You find some useful things on the corpse of %s.", c.getNameWithoutPrefixes())))
                        .addTrigger((c, k) -> {
                            k.addTitle(Titles.Title.getTitle(CustomTitles.TITAN_SLAYER));
                            DiscordHandler.sendToDiscord(CustomChannel.TITAN, String.format("The Titan %s has been defeated!", c.getNameWithoutPrefixes()));
                        })
                        .addSubRule(LootRule.create()
                                .requireCreature(Creature::isOnFire)
                                .chance((c -> 0.01f))
                                .addDrop(LootDrop.create(CustomItems.lesserFireCrystal.getTemplateId()))
                                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You grab some kind of crystal from the corpse of %s.", c.getName())))
                        )
        );

        // Ice Cat
        LootManager.add(LootRule.create()
                .requireTemplateIds(CustomCreatures.iceCatId)
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("The frozen body of %s shatters into pieces with the final blow.", c.getName())))
        );

        // Dock Worker
        LootManager.add(LootRule.create()
                .requireTemplateIds(CustomCreatures.npcDockWorkerId)
                .addTrigger((c, k) -> CreatureConstants.soundEmissionNpcs.remove(c))
        );

        // Skeleton
        LootManager.add(LootRule.create()
                .requireTemplateIds(CustomCreatures.reanimatedSkeletonId)
                .chance((c -> 0.01f))
                .addDrop(LootDrop.create(ItemList.boneCollar)
                        .rarity((c, k) -> makeRarity(50, true))
                )
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You grab some kind of bone from the corpse of %s.", c.getName())))
        );

        // Zombie Leader
        LootManager.add(LootRule.create()
                .requireTemplateIds(CustomCreatures.zombieLeaderId)
                .chance((c -> 0.2f))
                .addDrop(LootDrop.create(ItemList.boneCollar)
                        .rarity((c, k) -> makeRarity(50, true))
                )
                .addDrop(LootDrop.create(ItemList.blood))
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You grab some kind of bone from the corpse of %s.", c.getName())))
        );

        // ================================================================================
        // =================================== Uniques ==================================
        // ================================================================================

        // White Buffalo Spirit
        LootManager.add(LootRule.create()
                .requireTemplateIds(CustomCreatures.whiteBuffaloSpiritId)
                .addDrop(LootDrop.create(ItemList.rodTransmutation))
                .addDrop(LootDrop.create(ItemList.boneCollar)
                        .rarity((c, k) -> makeRarity(80, true))
                )
                .addDrop(LootDrop.create(CustomItems.treasureMapCache.getTemplateId()))
                .addDrop(LootDrop.create(RandomUtils.randomGem(true)))
                .addTrigger((c, k) -> {
                    k.addTitle(Titles.Title.getTitle(CustomTitles.WHITE_BUFFALO));
                    DiscordHandler.sendToDiscord(CustomChannel.EVENTS, String.format("The %s has been calmed. Rest in peace at last.", c.getNameWithoutPrefixes()));
                })
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You find some useful things on the corpse of %s.", c.getNameWithoutPrefixes())))
        );

        // Facebreyker
        LootManager.add(LootRule.create()
                .requireTemplateIds(CustomCreatures.whiteBuffaloSpiritId)
                .addDrop(LootDrop.create(CustomItems.moonCache.getTemplateId()))
                .addDrop(LootDrop.create(CustomItems.riftCache.getTemplateId()))
                .addDrop(LootDrop.create(CustomItems.treasureMapCache.getTemplateId()))
                .addTrigger((c, k) -> k.addTitle(Titles.Title.getTitle(CustomTitles.FACEBREYKER)))
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You find some useful things on the corpse of %s.", c.getNameWithoutPrefixes())))
        );

        // Kong
        LootManager.add(LootRule.create()
                .requireTemplateIds(CustomCreatures.whiteBuffaloSpiritId)
                .addDrop(LootDrop.create(CustomItems.moonCache.getTemplateId()))
                .addDrop(LootDrop.create(CustomItems.riftCache.getTemplateId()))
                .addDrop(LootDrop.create(CustomItems.treasureMapCache.getTemplateId()))
                .addTrigger((c, k) -> k.addTitle(Titles.Title.getTitle(CustomTitles.KING_KONG)))
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You find some useful things on the corpse of %s.", c.getNameWithoutPrefixes())))
        );

        // Spirit Stag
        LootManager.add(LootRule.create()
                .requireTemplateIds(CustomCreatures.spiritStagId)
                .addDrop(LootDrop.create(ItemList.rodTransmutation))
                .addDrop(LootDrop.create(ItemList.boneCollar)
                        .rarity((c, k) -> makeRarity(80, true))
                )
                .addDrop(LootDrop.create(CustomItems.treasureMapCache.getTemplateId()))
                .addDrop(LootDrop.create(RandomUtils.randomGem(true)))
                .addTrigger((c, k) -> DiscordHandler.sendToDiscord(CustomChannel.EVENTS, String.format("The %s has been calmed. Rest in peace at last.", c.getNameWithoutPrefixes())))
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You find some useful things on the corpse of %s.", c.getNameWithoutPrefixes())))
        );

        // White Buffalo Spirit
        LootManager.add(LootRule.create()
                .requireTemplateIds(CustomCreatures.tombRaiderId)
                .addDrop(LootDrop.create(CustomItems.treasureMapCache.getTemplateId()))
                .addDrop(LootDrop.create(RandomUtils.randomGem(false)))
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You find some useful things on the corpse of %s.", c.getNameWithoutPrefixes())))
        );

        // ================================================================================
        // ====================================== Rares ===================================
        // ================================================================================

        // Rare Creatures
        LootManager.add(
                LootRule.create()
                        .requireTemplateIds(CustomCreatures.spectralDragonHatchlingId, CustomCreatures.reaperId)
                        .addTrigger((c, k) -> {
                            if (c.getTemplateId() == CustomCreatures.spectralDragonHatchlingId)
                                k.addTitle(Titles.Title.getTitle(CustomTitles.SPECTRAL));
                        })
                        .chance(0.5f)
                        .addDrop(LootDrop.create(RandomUtils.randomMaterialConstructionTemplates())
                                .ql(Server.rand.nextFloat() * 100)
                                .repeat(3)
                        )
                        .chance(0.5f)
                        .addDrop(LootDrop.create(RandomUtils.randomLumpTemplates())
                                .repeat(5)
                                .ql((c, k) -> (Server.rand.nextFloat() * 100))
                        )
                        .chance(0.5f)
                        .addDrop(LootDrop.create(CustomItems.riftCache.getTemplateId())
                        )
                        .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You grab some kind of fragment from the corpse of %s.", c.getName())))
        );

        // ================================================================================
        // =================================== Taxidermy ==================================
        // ================================================================================

        // Black Bear
        LootManager.add(LootRule.create()
                .requireTemplateIds(CreatureTemplateIds.BEAR_BLACK_CID)
                .chance((c -> 0.075f))
                .addDrop(LootDrop.create(CustomItems.blackBearTaxidermyHead.getTemplateId()))
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You manage to salvage an undamaged head from the corpse of %s.", c.getName())))
                .chance((c -> 0.01f))
                .addDrop(LootDrop.create(CustomItems.blackBearTaxidermyBody.getTemplateId()))
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You manage to salvage an undamaged body from the corpse of %s.", c.getName())))
        );

        // Brown Bear
        LootManager.add(LootRule.create()
                .requireTemplateIds(CreatureTemplateIds.BEAR_BLACK_CID)
                .chance((c -> 0.075f))
                .addDrop(LootDrop.create(CustomItems.brownBearTaxidermyHead.getTemplateId()))
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You manage to salvage an undamaged head from the corpse of %s.", c.getName())))
        );

        // Bison
        LootManager.add(LootRule.create()
                .requireTemplateIds(CreatureTemplateIds.BISON_CID)
                .chance((c -> 0.075f))
                .addDrop(LootDrop.create(CustomItems.bisonTaxidermyHead.getTemplateId()))
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You manage to salvage an undamaged head from the corpse of %s.", c.getName())))
        );

        // Deer
        LootManager.add(LootRule.create()
                .requireTemplateIds(CreatureTemplateIds.DEER_CID)
                .requireCreature(Creature::isNotFemale)
                .chance((c -> 0.075f))
                .addDrop(LootDrop.create(CustomItems.stagTaxidermyHead.getTemplateId()))
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You manage to salvage an undamaged head from the corpse of %s.", c.getName())))
        );

        // Wild Cat
        LootManager.add(LootRule.create()
                .requireTemplateIds(CreatureTemplateIds.CAT_WILD_CID)
                .chance((c -> 0.075f))
                .addDrop(LootDrop.create(CustomItems.wildcatTaxidermyHead.getTemplateId()))
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You manage to salvage an undamaged head from the corpse of %s.", c.getName())))
        );

        // Hell Hound
        LootManager.add(LootRule.create()
                .requireTemplateIds(CreatureTemplateIds.HELL_HOUND_CID)
                .chance((c -> 0.075f))
                .addDrop(LootDrop.create(CustomItems.hellHoundTaxidermyHead.getTemplateId()))
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You manage to salvage an undamaged head from the corpse of %s.", c.getName())))
        );

        // Mountain Lion
        LootManager.add(LootRule.create()
                .requireTemplateIds(CreatureTemplateIds.LION_MOUNTAIN_CID)
                .chance((c -> 0.075f))
                .addDrop(LootDrop.create(CustomItems.lionTaxidermyHead.getTemplateId()))
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You manage to salvage an undamaged head from the corpse of %s.", c.getName())))
        );

        // Hyena
        LootManager.add(LootRule.create()
                .requireTemplateIds(CustomCreatures.hyenaId)
                .chance((c -> 0.075f))
                .addDrop(LootDrop.create(CustomItems.hyenaTaxidermyHead.getTemplateId()))
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You manage to salvage an undamaged head from the corpse of %s.", c.getName())))
        );

        // Large Boar
        LootManager.add(LootRule.create()
                .requireTemplateIds(CustomCreatures.largeBoarId)
                .chance((c -> 0.075f))
                .addDrop(LootDrop.create(CustomItems.boarTaxidermyHead.getTemplateId()))
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You manage to salvage an undamaged head from the corpse of %s.", c.getName())))
        );

        // Large Boar
        LootManager.add(LootRule.create()
                .requireTemplateIds(CustomCreatures.largeBoarId)
                .chance((c -> 0.075f))
                .addDrop(LootDrop.create(CustomItems.boarTaxidermyHead.getTemplateId()))
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You manage to salvage an undamaged head from the corpse of %s.", c.getName())))
        );

        // Black Wolf
        LootManager.add(LootRule.create()
                .requireTemplateIds(CreatureTemplateIds.WOLF_BLACK_CID)
                .chance((c -> 0.075f))
                .addDrop(LootDrop.create(CustomItems.wolfTaxidermyHead.getTemplateId()))
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You manage to salvage an undamaged head from the corpse of %s.", c.getName())))
        );

        // Worg
        LootManager.add(LootRule.create()
                .requireTemplateIds(CreatureTemplateIds.WORG_CID)
                .chance((c -> 0.075f))
                .addDrop(LootDrop.create(CustomItems.worgTaxidermyHead.getTemplateId()))
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You manage to salvage an undamaged head from the corpse of %s.", c.getName())))
        );

        // ================================================================================
        // =================================== Halloween ==================================
        // ================================================================================

        // All Monsters and Undead During Halloween
        if (Holidays.isRequiemHalloween() && !disableHolidayCreatures) {
            LootManager.add(LootRule.create()
                    .requireCreature((c -> c.isUndead() || c.isMonster()))
                    .chance((c -> 0.15f))
                    .addDrop(LootDrop.create(ItemList.sweet)
                            .ql(RandomUtils.getRandomQl(1f, 99f)))
                    .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You find a sweet on the corpse of %s.", c.getName())))
            );
        }

        // =======================================================================================
        // ===================================== Christmas =======================================
        // =======================================================================================

        // All Christmas Creatures During Christmas
        if (Holidays.isRequiemChristmas() && !disableHolidayCreatures) {
            LootManager.add(LootRule.create()
                    .requireCreature((CreatureTweaks::isChristmasMob))
                    .chance((c -> 0.15f))
                    .addDrop(LootDrop.create(ItemList.sweet)
                            .ql(RandomUtils.getRandomQl(1f, 99f)))
                    .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You find a sweet on the corpse of %s.", c.getName())))
                    .chance((c -> 0.01f))
                    .addDrop(LootDrop.create(ItemList.saddleBagsXmas)
                            .ql(RandomUtils.getRandomQl(1f, 99f))
                            .rarity(RandomUtils.randomRarity(25, false)))
                    .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You find a sweet on the corpse of %s.", c.getName())))
            );
        }

        // Grinch
        LootManager.add(LootRule.create()
                .requireTemplateIds(CustomCreatures.grinchId)
                .addDrop(LootDrop.create(ItemList.snowLantern))
                .addDrop(LootDrop.create(ItemList.boneCollar)
                        .rarity((c, k) -> makeRarity(75, true))
                )
                .addDrop(LootDrop.create(CustomItems.treasureMapCache.getTemplateId()))
                .addDrop(LootDrop.create(ItemList.snowLantern))
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You find some useful things on the corpse of %s.", c.getNameWithoutPrefixes())))
        );

        // Snowman
        LootManager.add(LootRule.create()
                .requireTemplateIds(CustomCreatures.snowmanId)
                .chance((c -> 0.3f))
                .addDrop(LootDrop.create(ItemList.carrot)
                        .ql(RandomUtils.getRandomQl(1f, 99f))
                )
                .chance((c -> 0.6f))
                .addDrop(LootDrop.create(ItemList.branch)
                        .ql(RandomUtils.getRandomQl(1f, 99f))
                )
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You find a carrot and branch in the puddle that is left from the %s.", c.getNameWithoutPrefixes())))
        );

        // =======================================================================================
        // ================================== Rare Creatures =====================================
        // =======================================================================================

        // Reaper
        LootManager.add(LootRule.create()
                .requireCreature(CreatureTweaks::isRareCreature)
                .addTrigger((c, k) -> Server.getInstance().broadCastAlert(String.format("The rare %s has been slain. A new rare creature may enter the realm soon.", c.getNameWithoutPrefixes())))
        );

        // =======================================================================================
        // ====================================== Titans =========================================
        // =======================================================================================

        // Titans
        LootManager.add(
                LootRule.create()
                        .requireTemplateIds(CustomCreatures.lilithId, CustomCreatures.ifritId)
                        .addTrigger((c, k) -> Titans.removeTitan(c))
                        .addTrigger((c, k) -> ItemTools.createEnchantOrb(110))
                        .addTrigger((c, k) -> ItemTools.createRandomSorcery((byte) 1))
                        .addDrop(LootDrop.create(CustomItems.treasureBoxId)
                                .rarity((c, k) -> makeRarity(25, true))
                        )
                        .addDrop(LootDrop.create(RandomUtils.randomToolWeaponTemplates())
                                .rarity((c, k) -> makeRarity(65, true))
                        )
                        .addDrop(LootDrop.create(RandomUtils.randomGem(true))
                                .rarity((c, k) -> makeRarity(33, true))
                        )
                        .addDrop(LootDrop.create(RandomUtils.randomMaterialConstructionTemplates())
                                .rarity((c, k) -> makeRarity(33, false))
                                .repeat(6)
                                .ql((c, k) -> Server.rand.nextFloat() * 100)
                        )
                        .addDrop(LootDrop.create(CustomItems.artifactCache.getTemplateId())
                        )
                        .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You find some useful things on the corpse of %s.", c.getNameWithoutPrefixes())))
                        .addTrigger((c, k) -> {
                            k.addTitle(Titles.Title.getTitle(CustomTitles.TITAN_SLAYER));
                            DiscordHandler.sendToDiscord(CustomChannel.TITAN, String.format("The Titan %s has been defeated!", c.getNameWithoutPrefixes()));
                        })
        );

        // =======================================================================================
        // ====================================== Events =========================================
        // =======================================================================================

        // Injured Pirate
        LootManager.add(LootRule.create()
                .requireTemplateIds(CustomCreatures.injuredPirateId)
                .addDrop(LootDrop.create(CustomItems.treasureHuntChestId))
                .addTrigger((c, k) -> SoundPlayer.playSound(SoundNames.EMOTE_INSULT_SND, c, 0f))
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You find a a treasure chest hidden near the corpse of the %s.", c.getNameWithoutPrefixes())))
        );

        // =======================================================================================
        // =================================== Spawn on Death ====================================
        // =======================================================================================

        // Golem
        LootManager.add(LootRule.create()
                .requireTemplateIds(golemId)
                .addSubRule(LootRule.create()
                        .addTriggerOnce((c, k) -> {
                            try {
                                CreatureSpawns.spawnGolemlings(c);
                            } catch (Exception e) {
                                RequiemLogging.logException("[Error] in spawning golem in LootTable", e);
                            }
                        })
                )
        );

        // Blob
        LootManager.add(LootRule.create()
                .requireTemplateIds(blobId)
                .addSubRule(LootRule.create()
                        .addTriggerOnce((c, k) -> {
                            try {
                                CreatureSpawns.spawnBloblings(c);
                            } catch (Exception e) {
                                RequiemLogging.logException("[Error] in spawning blob in LootTable", e);
                            }
                        })
                )
        );

        // Prismatic Blob
        LootManager.add(LootRule.create()
                .requireTemplateIds(prismaticBlobId)
                .addSubRule(LootRule.create()
                        .addTriggerOnce((c, k) -> {
                            try {
                                CreatureSpawns.spawnPrismaticBloblings(c);
                            } catch (Exception e) {
                                RequiemLogging.logException("[Error] in spawning prismatic blob in LootTable", e);
                            }
                        })
                )
        );

        // Great White Buffalo
        LootManager.add(LootRule.create()
                .requireTemplateIds(whiteBuffaloId)
                .addSubRule(LootRule.create()
                        .addTriggerOnce((c, k) -> {
                            try {
                                CreatureSpawns.spawnBuffaloSpirit(c);
                            } catch (Exception e) {
                                RequiemLogging.logException("[Error] in spawning Great White Buffalo in LootTable", e);
                            }
                        })
                )
        );

        // Mimic
        LootManager.add(LootRule.create()
                .requireTemplateIds(mimicChestId)
                .addSubRule(LootRule.create()
                        .addTriggerOnce((c, k) -> {
                            try {
                                CreatureSpawns.spawnMimic(c);
                            } catch (Exception e) {
                                RequiemLogging.logException("[Error] in spawning Mimic in LootTable", e);
                            }
                        })
                )
        );

        // Horseman Conquest
        LootManager.add(LootRule.create()
                .requireTemplateIds(horsemanConquestId)
                .addSubRule(LootRule.create()
                        .addTriggerOnce((c, k) -> {
                            try {
                                CreatureSpawns.spawnHorsemanWar(c);
                            } catch (Exception e) {
                                RequiemLogging.logException("[Error] in spawning Horseman Conquest in LootTable", e);
                            }
                        })
                )
        );

        // Horseman War
        LootManager.add(LootRule.create()
                .requireTemplateIds(horsemanWarId)
                .addSubRule(LootRule.create()
                        .addTriggerOnce((c, k) -> {
                            try {
                                CreatureSpawns.spawnHorsemanFamine(c);
                            } catch (Exception e) {
                                RequiemLogging.logException("[Error] in spawning Horseman War in LootTable", e);
                            }
                        })
                )
        );

        // Horseman Famine
        LootManager.add(LootRule.create()
                .requireTemplateIds(horsemanFamineId)
                .addSubRule(LootRule.create()
                        .addTriggerOnce((c, k) -> {
                            try {
                                CreatureSpawns.spawnHorsemanDeath(c);
                            } catch (Exception e) {
                                RequiemLogging.logException("[Error] in spawning Horseman Famine in LootTable", e);
                            }
                        })
                )
        );

        // =======================================================================================
        // ====================================== All Creatures ==================================
        // =======================================================================================

        LootManager.add(
                LootRule.create()
                        .addTrigger((c, k) -> {
                            if (creatureDeathLogging) {
                                RequiemLogging.CreatureDeathLogging(c);
                            }
                            RequiemLogging.logInfo(String.format("%s killed a %s", k.getName(), c.getName()));
                        })
                        .chance(0.01f)
                        .addSubRule(LootRule.create()
                                .addDrop(LootDrop.create(CustomItems.scrollOfVillageCreation.getTemplateId())))
                        .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You grab some kind of scroll from the corpse of %s.", c.getName())))
                        .chance(0.01f)
                        .addSubRule(LootRule.create()
                                .addDrop(LootDrop.create(CustomItems.scrollOfVillageWar.getTemplateId())))
                        .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You grab some kind of scroll from the corpse of %s.", c.getName())))
                        .chance(0.01f)
                        .addSubRule(LootRule.create()
                                .addDrop(LootDrop.create(CustomItems.scrollOfVillageHeal.getTemplateId())))
                        .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You grab some kind of scroll from the corpse of %s.", c.getName())))
        );
    }

}