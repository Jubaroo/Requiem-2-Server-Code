package org.jubaroo.mods.wurm.server.creatures;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Server;
import com.wurmonline.server.Servers;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.CreatureTemplate;
import com.wurmonline.server.creatures.CreatureTemplateFactory;
import com.wurmonline.server.creatures.CreatureTemplateIds;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.Materials;
import com.wurmonline.server.players.Titles;
import net.bdew.wurm.tools.server.loot.LootDrop;
import net.bdew.wurm.tools.server.loot.LootManager;
import net.bdew.wurm.tools.server.loot.LootRule;
import org.gotti.wurmunlimited.modsupport.IdFactory;
import org.gotti.wurmunlimited.modsupport.IdType;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.communication.discord.DiscordHandler;
import org.jubaroo.mods.wurm.server.creatures.bounty.LootBounty;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.items.ItemTools;
import org.jubaroo.mods.wurm.server.misc.CustomTitles;
import org.jubaroo.mods.wurm.server.tools.RandomUtils;
import org.jubaroo.mods.wurm.server.tools.RequiemTools;

import static org.jubaroo.mods.wurm.server.items.ItemTools.makeRarity;

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

    /**
     * Here we decide what happens when a creature dies
     */
    public static void creatureDied() {
        // Uniques
        LootManager.add(LootRule.create()
                .requireUnique()

                // Spawn Undead Creature
                .addSubRule(LootRule.create()
                        .chance(c -> c.getStatus().isChampion() ? 0.33f : 0.1f)
                        .addTriggerOnce((c, k) -> {
                            try {
                                CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(RequiemTools.getRandArrayInt(CreatureTools.randomUndead));
                                byte ctype = (byte) Math.max(0, Server.rand.nextInt(17) - 5);
                                Creature.doNew(template.getTemplateId(), true, c.getPosX(), c.getPosY(), Server.rand.nextFloat() * 360f, c.getLayer(), template.getName(), (byte) 0, c.getKingdomId(), ctype, false, (byte) 60);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Server.getInstance().broadCastAlert("An undead creature is released from the underworld, seeking the soul of a powerful creature!");
                            LootBounty.spawnFriyanTablets(5, 10);
                        })
                        .commTrigger((c, comm) -> comm.sendNormalServerMessage("An undead creature is released from the underworld, seeking the soul of a powerful creature!"))

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
                                .chance(0.5f)
                                .addDrop(LootDrop.create(IdFactory.getIdFor("bdew.pets.egg", IdType.ITEMTEMPLATE))
                                        .rarity((c, k) -> Server.rand.nextInt(3) == 0 ? MiscConstants.FANTASTIC : MiscConstants.RARE)
                                )
                        )

                        // dragon slayer trophy
                        .addSubRule(LootRule.create()
                                .requireUnique()
                                .requireCreature(Creature::isDragon))
                                .addDrop(LootDrop.create(ItemList.goldChallengeStatue)
                                        .rarity(MiscConstants.RARE)
                                        .weight(1000)
                                        .name((c, k) -> String.format("%s slayer trophy", c.getTemplate().getName().toLowerCase()))
                                )
                                //.addTrigger((c, k) -> k.achievement(MyAchievementIds.FRESH_DRAGON_SLAYER))
                        )
        );

        // fire crystal fragments
        LootManager.add(LootRule.create()
                .requireTemplateIds(CustomCreatures.fireCrabId, CustomCreatures.fireGiantId, CreatureTemplateIds.LAVA_CREATURE_CID, CreatureTemplateIds.LAVA_SPIDER_CID, CreatureTemplateIds.HELL_HOUND_CID, CreatureTemplateIds.HELL_SCORPION_CID)
                .chance((c -> c.getStatus().isChampion() ? 1f : 0.05f))
                .addDrop(LootDrop.create(ItemList.itemFragment)
                        .rarity((c, k) -> MiscConstants.COMMON)
                        .data1(1)
                        .data2(0)
                        .aux((byte) 1)
                        .weight(CustomItems.lesserFireCrystal.getWeightGrams() / CustomItems.lesserFireCrystal.getFragmentAmount())
                )
                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You grab some kind of fragment from the corpse of %s.", c.getName())))
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

        // Rare Creatures
        LootManager.add(
                LootRule.create()
                        .requireTemplateIds(CustomCreatures.spectralDragonHatchlingId, CustomCreatures.reaperId)
                        .chance(0.5f)
                        .addDrop(LootDrop.create(RandomUtils.randomMaterialConstructionTemplates())
                                .ql(Server.rand.nextFloat() * 100)
                                .repeat(3)
                        )
                        .addDrop(LootDrop.create(RandomUtils.randomLumpTemplates())
                                .repeat(5)
                                .ql((c, k) -> (Server.rand.nextFloat() * 100))
                        )
                                .addDrop(LootDrop.create(CustomItems.riftCache.getTemplateId())
                                )
                        .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You grab some kind of fragment from the corpse of %s.", c.getName())))
        );

        // Titans
        LootManager.add(
                LootRule.create()
                        .requireTemplateIds(CustomCreatures.lilithId, CustomCreatures.ifritId)
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
                                .addSubRule(LootRule.create()
                                        .chance(0.20f)
                                        .addDrop((LootDrop) RandomUtils.maskTemplates)
                                )
                                        .addSubRule(LootRule.create()
                                                .chance(0.05f)
                                                .addDrop(LootDrop.create(ItemList.boneCollar)
                                                        .rarity((c, k) -> makeRarity(50, true))
                                                )
                                                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You find something useful on the corpse of %s.", c.getNameWithoutPrefixes())))
                                        )
                        )
        );

        // Treasure Goblins
        LootManager.add(
                LootRule.create()
                        .requireTemplateIds(CustomCreatures.treasureGoblinId, CustomCreatures.treasureGoblinBloodThiefId, CustomCreatures.treasureGoblinGemHoarderId, CustomCreatures.treasureGoblinBloodThiefId, CustomCreatures.treasureGoblinMenageristGoblinId, CustomCreatures.treasureGoblinRainbowGoblinId, CustomCreatures.treasureGoblinMalevolentTormentorId, CustomCreatures.treasureGoblinOdiousCollectorId)
                        .addSubRule(LootRule.create()
                                .chance(0.05f)
                                .addDrop(LootDrop.create(ItemList.boneCollar)
                                        .rarity((c, k) -> makeRarity(50, true))
                                )
                                .addDrop(LootDrop.create(CustomItems.affinityOrbId))
                                .addDrop(LootDrop.create(CustomItems.gemCache.getTemplateId())
                                )
                        )
                        .addSubRule(LootRule.create()
                                        .requireTemplateIds(CustomCreatures.treasureGoblinId)
                                .addDrop(LootDrop.create(CustomItems.treasureBoxId)
                                        .rarity((c, k) -> makeRarity(75, true))
                                )
                                        .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You find some useful things on the corpse of %s.", c.getNameWithoutPrefixes())))
                                        .addTrigger((c, k) -> DiscordHandler.sendToDiscord(CustomChannel.EVENTS, String.format("A %s has been killed by %s.", c.getNameWithoutPrefixes(), k.getNameWithoutPrefixes())))
                                .addSubRule(LootRule.create()
                                                .requireTemplateIds(CustomCreatures.treasureGoblinMenageristGoblinId)
                                                .addDrop(LootDrop.create(IdFactory.getIdFor("bdew.pets.egg", IdType.ITEMTEMPLATE))
                                                        .rarity((c, k) -> Server.rand.nextInt(3) == 0 ? MiscConstants.FANTASTIC : MiscConstants.RARE)
                                                )
                                                .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You find some useful things on the corpse of %s.", c.getNameWithoutPrefixes())))
                                                .addTrigger((c, k) -> DiscordHandler.sendToDiscord(CustomChannel.EVENTS, String.format("A %s has been killed by %s.", c.getNameWithoutPrefixes(), k.getNameWithoutPrefixes())))))
        );

        // Rift loot
        LootManager.add(LootRule.create()
                .requireCreature(c -> riftLootChance(c) > 0)
                .addTrigger((c, k) -> {
                    //MyAchievements.triggerAchievement(k, MyAchievements.riftSlayer);
                    if (c.getTemplateId() == CreatureTemplateIds.RIFT_JACKAL_FOUR_CID)
                        //MyAchievements.triggerAchievement(k, MyAchievements.freshRiftSlayer);
                })
                .addSubRule(LootRule.create()
                        .chance((c) -> 1f / riftLootChance(c))
                        .addDrop(LootDrop.create(JewelItemsCrafting.arcaneShardId)
                                .rarity((c, k) -> MyTools.makeRarity(25, false))
                        )
                        //.addTrigger((c, k) -> MyAchievements.triggerAchievement(k, MyAchievements.othRiftShard))
                        .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You grab a magical shard from the corpse of %s.", c.getName())))
                )
                .addSubRule(LootRule.create()
                        .chance((c) -> 3f / riftLootChance(c))
                        .addDrop(LootDrop.create(ItemList.seryllBar)
                                .ql((c, k) -> 90f + Server.rand.nextFloat() * 10)
                                .rarity((c, k) -> MyTools.makeRarity(25, false))
                        )
                        .commTrigger((c, comm) -> comm.sendNormalServerMessage(String.format("You grab a lump of strange metal from the corpse of %s.", c.getName())))
                )
        );

        // All creatures
        LootManager.add(
                LootRule.create()
                        .chance(3f)
                        .addDrop(LootDrop.staticDrop(CustomItems.scrollOfVillageCreation.getTemplateId())
                                .addSubRule(LootRule.create()
                                .chance(3f)
                                .addDrop(CustomItems.scrollOfVillageWar.getTemplateId(), 99f, Materials.MATERIAL_PAPER, MiscConstants.COMMON)
                                .triggerChance(3f)
                                .addDrop(CustomItems.scrollOfVillageHeal.getTemplateId(), 99f, Materials.MATERIAL_PAPER, MiscConstants.COMMON)
                                .triggerChance(100f)
                                .addDrop(ItemList.skull, 99f, Materials.MATERIAL_BONE, MiscConstants.COMMON)
                                .addTrigger((c, p) -> {
                                    if (p.hasLink()) {
                                        p.getCommunicator().sendNormalServerMessage(String.format("You find something useful on the corpse of %s.", c.getNameWithoutPrefixes()));
                                        LootBounty.sendLootAssist(c, p);
                                    }
                                })
                        );

    }

    //TODO move 'All creatures' to the bottom when done


    /*
    public static void creatureDied(Creature creature, Map<Long, Long> attackers) {
        long now = System.currentTimeMillis();
        Set<Player> killers = attackers.entrySet().stream()
                .filter(e -> now - e.getValue() < (TimeConstants.MINUTE_MILLIS * 10) && WurmId.getType(e.getKey()) == 0)
                .flatMap(e -> streamOfNullable(Players.getInstance().getPlayerOrNull(e.getKey())))
                .collect(Collectors.toSet());

        if (creature.getName().equals("Dock Worker") || creature.getName().equals("Dock worker")) {
            Constants.soundEmissionNpcs.remove(creature);
        }

        if (!Constants.disableCreatureLoot) {
            try {

                if (templateId == CustomCreatures.treasureGoblinId) {
                    killers.forEach(killer -> {
                        Item inv = killer.getInventory();
                        try {
                            inv.insertItem(ItemFactory.createItem(CustomItems.treasureBoxId, 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_GOLD, (byte) (Server.rand.nextInt(3)), null), true);
                            inv.insertItem(ItemFactory.createItem(CustomItems.affinityOrbId, 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_MAGIC, (byte) (Server.rand.nextInt(3)), null), true);
                            inv.insertItem(ItemFactory.createItem(CustomItems.gemCache.getTemplateId(), 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_GOLD, (byte) (Server.rand.nextInt(3)), creature.getNameWithoutPrefixes()), true);
                            inv.insertItem(ItemFactory.createItem(ItemList.boneCollar, 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_BONE, (byte) 1, creature.getNameWithoutPrefixes()), true);
                        } catch (FailedException | NoSuchTemplateException e) {
                            RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                        }
                        if (killer.hasLink()) {
                            killer.getCommunicator().sendNormalServerMessage(String.format("You find a box of treasure, a strange bone, affinity orb, and a valuable cache of gems on the corpse of %s.", creature.getNameWithoutPrefixes()));
                        }
                        DiscordHandler.sendToDiscord(CustomChannel.EVENTS, String.format("A %s has been killed by %s.", creature.getNameWithoutPrefixes(), killer.getNameWithoutPrefixes()));
                    });
                }

                if (templateId == CustomCreatures.treasureGoblinMenageristGoblinId) {
                    killers.forEach(killer -> {
                        Item inv = killer.getInventory();
                        try {
                            for (int i = 0; i < 4; i++) {
                                Item item;
                                item = ItemFactory.createItem(IdFactory.getIdFor("bdew.pets.egg", IdType.ITEMTEMPLATE), 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_MAGIC, Server.rand.nextBoolean() ? (byte) 1 : (byte) 3, creature.getNameWithoutPrefixes());
                                item.setAuxData((byte) Server.rand.nextInt(102));
                                inv.insertItem(item, true);
                            }
                            inv.insertItem(ItemFactory.createItem(CustomItems.affinityOrbId, 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_MAGIC, (byte) (Server.rand.nextInt(3)), null), true);
                            inv.insertItem(ItemFactory.createItem(CustomItems.gemCache.getTemplateId(), 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_GOLD, (byte) (Server.rand.nextInt(3)), creature.getNameWithoutPrefixes()), true);
                            inv.insertItem(ItemFactory.createItem(ItemList.boneCollar, 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_BONE, (byte) 1, creature.getNameWithoutPrefixes()), true);
                        } catch (FailedException | NoSuchTemplateException e) {
                            RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                        }
                        if (killer.hasLink()) {
                            killer.getCommunicator().sendNormalServerMessage(String.format("You find some interesting eggs and some other goodies from the corpse of %s.", creature.getNameWithoutPrefixes()));
                        }
                        DiscordHandler.sendToDiscord(CustomChannel.EVENTS, String.format("A %s has been killed by %s.", creature.getNameWithoutPrefixes(), killer.getNameWithoutPrefixes()));
                    });
                }

                if (templateId == CustomCreatures.reanimatedSkeletonId) {
                    killers.forEach(killer -> {
                        Item inv = killer.getInventory();
                        if (Server.rand.nextInt(150) == 0) {
                            try {
                                inv.insertItem(ItemFactory.createItem(ItemList.boneCollar, 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_BONE, (byte) 1, creature.getNameWithoutPrefixes()), true);
                            } catch (FailedException | NoSuchTemplateException e) {
                                RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                            }
                            if (killer.hasLink()) {
                                killer.getCommunicator().sendNormalServerMessage(String.format("You grab a strange bone from the corpse of the %s.", creature.getNameWithoutPrefixes()));
                            }
                        }
                    });
                }

                if (templateId == CustomCreatures.blackKnightId) {
                    killers.forEach(killer -> {
                        Item inv = killer.getInventory();
                        try {
                            inv.insertItem(ItemFactory.createItem(CustomItems.lesserFireCrystal.getTemplateId(), 99f, ItemMaterials.MATERIAL_CRYSTAL, MiscConstants.COMMON, creature.getNameWithoutPrefixes()), true);
                        } catch (FailedException | NoSuchTemplateException e) {
                            RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                        }
                        if (killer.hasLink()) {
                            killer.getCommunicator().sendNormalServerMessage(String.format("You grab some sort of crystal from the corpse of %s.", creature.getNameWithoutPrefixes()));
                        }
                    });
                }

                if (creature.isOnFire()) {
                    killers.forEach(killer -> {
                        Item inv = killer.getInventory();
                        try {
                            if (Server.rand.nextInt(50) == 11) {
                                inv.insertItem(ItemFactory.createItem(CustomItems.lesserFireCrystal.getTemplateId(), 99f, ItemMaterials.MATERIAL_CRYSTAL, MiscConstants.COMMON, creature.getNameWithoutPrefixes()), true);
                                killer.getCommunicator().sendNormalServerMessage(String.format("You grab some sort of crystal from the corpse of %s.", creature.getNameWithoutPrefixes()));
                            }
                        } catch (FailedException | NoSuchTemplateException e) {
                            RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                        }
                        if (killer.hasLink()) {
                            killer.getCommunicator().sendNormalServerMessage(String.format("You grab some sort of crystal from the corpse of %s.", creature.getNameWithoutPrefixes()));
                        }
                    });
                }

                if (templateId == CustomCreatures.whiteBuffaloSpiritId) {
                    killers.forEach(killer -> {
                        Item inv = killer.getInventory();
                        try {
                            inv.insertItem(ItemFactory.createItem(ItemList.rodTransmutation, 99f, MiscConstants.COMMON, creature.getNameWithoutPrefixes()), true);
                            inv.insertItem(ItemFactory.createItem(ItemList.boneCollar, 99f, Materials.MATERIAL_BONE, (byte) 1, creature.getNameWithoutPrefixes()), true);
                            inv.insertItem(ItemFactory.createItem(ItemList.blood, 99f, null), true);
                            inv.insertItem(ItemFactory.createItem(CustomItems.treasureBoxId, 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_GOLD, (byte) (Server.rand.nextInt(3)), null), true);
                            killer.addTitle(Titles.Title.getTitle(CustomTitles.WHITE_BUFFALO));
                        } catch (FailedException | NoSuchTemplateException e) {
                            RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                        }
                        if (killer.hasLink()) {
                            killer.getCommunicator().sendNormalServerMessage(String.format("You grab some sort of crystal from the corpse of %s.", creature.getNameWithoutPrefixes()));
                        }
                    });
                }

                if (templateId == CustomCreatures.facebreykerId) {
                    killers.forEach(killer -> {
                        Item inv = killer.getInventory();
                        try {
                            inv.insertItem(ItemFactory.createItem(CustomItems.treasureMapCache.getTemplateId(), 99f, MiscConstants.COMMON, creature.getNameWithoutPrefixes()), true);
                            inv.insertItem(ItemFactory.createItem(CustomItems.moonCache.getTemplateId(), 99f, MiscConstants.COMMON, creature.getNameWithoutPrefixes()), true);
                            inv.insertItem(ItemFactory.createItem(CustomItems.riftCache.getTemplateId(), 99f, MiscConstants.COMMON, creature.getNameWithoutPrefixes()), true);
                        } catch (FailedException | NoSuchTemplateException e) {
                            RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                        }
                        if (killer.hasLink()) {
                            killer.getCommunicator().sendNormalServerMessage(String.format("You grab some shiny caches from the corpse of %s.", creature.getNameWithoutPrefixes()));
                        }
                    });
                }

                if (templateId == CustomCreatures.kongId) {
                    killers.forEach(killer -> {
                        Item inv = killer.getInventory();
                        try {
                            inv.insertItem(ItemFactory.createItem(CustomItems.affinityOrbId, 99f, MiscConstants.COMMON, creature.getNameWithoutPrefixes()), true);
                            killer.addTitle(Titles.Title.getTitle(CustomTitles.KING_KONG));
                        } catch (FailedException | NoSuchTemplateException e) {
                            RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                        }
                        if (killer.hasLink()) {
                            killer.getCommunicator().sendNormalServerMessage(String.format("You find an affinity orb the corpse of %s!", creature.getNameWithoutPrefixes()));
                        }
                    });
                }

                if (templateId == CustomCreatures.zombieLeaderId) {
                    killers.forEach(killer -> {
                        Item inv = killer.getInventory();
                        try {
                            inv.insertItem(ItemFactory.createItem(ItemList.boneCollar, 99f, Materials.MATERIAL_BONE, (byte) 1, creature.getNameWithoutPrefixes()), true);
                            inv.insertItem(ItemFactory.createItem(ItemList.blood, 99f, null), true);
                        } catch (FailedException | NoSuchTemplateException e) {
                            RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                        }
                        if (killer.hasLink()) {
                            killer.getCommunicator().sendNormalServerMessage(String.format("You grab some loot from the corpse of the %s.", creature.getNameWithoutPrefixes()));
                        }
                    });
                }

                if (templateId == CustomCreatures.spiritStagId) {
                    killers.forEach(killer -> {
                        Item inv = killer.getInventory();
                        try {
                            inv.insertItem(ItemFactory.createItem(ItemList.boneCollar, 99f, Materials.MATERIAL_BONE, (byte) 1, creature.getNameWithoutPrefixes()), true);
                            inv.insertItem(ItemFactory.createItem(ItemList.blood, 99f, null), true);
                            inv.insertItem(ItemFactory.createItem(CustomItems.treasureBoxId, 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_GOLD, (byte) (Server.rand.nextInt(3)), null), true);
                        } catch (FailedException | NoSuchTemplateException e) {
                            RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                        }
                        if (killer.hasLink()) {
                            killer.getCommunicator().sendNormalServerMessage(String.format("You grab some loot from the corpse of the %s.", creature.getNameWithoutPrefixes()));
                        }
                    });
                }

                if (templateId == CustomCreatures.tombRaiderId) {
                    killers.forEach(killer -> {
                        Item inv = killer.getInventory();
                        try {
                            inv.insertItem(ItemFactory.createItem(CustomItems.gemCache.getTemplateId(), 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_GOLD, (byte) (Server.rand.nextInt(3)), creature.getNameWithoutPrefixes()), true);
                            inv.insertItem(ItemFactory.createItem(CustomItems.treasureMapCache.getTemplateId(), 99f, MiscConstants.COMMON, creature.getNameWithoutPrefixes()), true);
                        } catch (FailedException | NoSuchTemplateException e) {
                            RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                        }
                        if (killer.hasLink()) {
                            killer.getCommunicator().sendNormalServerMessage(String.format("You grab some loot from the corpse of the %s.", creature.getNameWithoutPrefixes()));
                        }
                    });
                }

                //if (templateId == CustomCreatures.spectralDragonHatchlingId) {
                //    killers.forEach(killer -> {
                //        killer.addTitle(Titles.Title.SPECTRAL);
                //    });
                //}
                // ================================================================================
                // =================================== Taxidermy ==================================
                // ================================================================================
                if (templateId == CreatureTemplateIds.BEAR_BLACK_CID) {
                    killers.forEach(killer -> {
                        Item inv = killer.getInventory();
                        switch (Server.rand.nextInt(taxidermyChance)) {
                            case 0:
                                try {
                                    inv.insertItem(ItemFactory.createItem(CustomItems.blackBearTaxidermyHead.getTemplateId(), 1f + ((98f) * Server.rand.nextFloat()), Materials.MATERIAL_ANIMAL, (byte) 0, null), true);
                                } catch (FailedException | NoSuchTemplateException e) {
                                    RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                                }
                                break;
                            case 1:
                                try {
                                    inv.insertItem(ItemFactory.createItem(CustomItems.blackBearTaxidermyBody.getTemplateId(), 1f + ((98f) * Server.rand.nextFloat()), Materials.MATERIAL_ANIMAL, (byte) 0, null), true);
                                } catch (FailedException | NoSuchTemplateException e) {
                                    RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                                }
                                break;
                        }
                    });
                }

                if (templateId == CreatureTemplateIds.BEAR_BROWN_CID) {
                    killers.forEach(killer -> {
                        Item inv = killer.getInventory();
                        if (Server.rand.nextInt(taxidermyChance) == 0) {
                            try {
                                inv.insertItem(ItemFactory.createItem(CustomItems.brownBearTaxidermyHead.getTemplateId(), 1f + ((98f) * Server.rand.nextFloat()), Materials.MATERIAL_ANIMAL, (byte) 0, null), true);
                            } catch (FailedException | NoSuchTemplateException e) {
                                RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                            }
                        }
                    });
                }

                if (templateId == CreatureTemplateIds.BISON_CID) {
                    killers.forEach(killer -> {
                        Item inv = killer.getInventory();
                        if (Server.rand.nextInt(taxidermyChance) == 0) {
                            try {
                                inv.insertItem(ItemFactory.createItem(CustomItems.bisonTaxidermyHead.getTemplateId(), 1f + ((98f) * Server.rand.nextFloat()), Materials.MATERIAL_ANIMAL, (byte) 0, null), true);
                            } catch (FailedException | NoSuchTemplateException e) {
                                RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                            }
                        }
                    });
                }

                if (templateId == CreatureTemplateIds.DEER_CID) {
                    killers.forEach(killer -> {
                        if (Server.rand.nextInt(taxidermyChance) == 0) {
                            if (creature.isNotFemale()) {
                                Item inv = killer.getInventory();
                                try {
                                    inv.insertItem(ItemFactory.createItem(CustomItems.stagTaxidermyHead.getTemplateId(), 1f + ((98f) * Server.rand.nextFloat()), Materials.MATERIAL_ANIMAL, (byte) 0, null), true);
                                } catch (FailedException | NoSuchTemplateException e) {
                                    RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                                }
                            }
                        }
                    });
                }

                if (templateId == CreatureTemplateIds.CAT_WILD_CID) {
                    killers.forEach(killer -> {
                        if (Server.rand.nextInt(taxidermyChance) == 0) {
                            try {
                                Item inv = killer.getInventory();
                                inv.insertItem(ItemFactory.createItem(CustomItems.wildcatTaxidermyHead.getTemplateId(), 1f + ((98f) * Server.rand.nextFloat()), Materials.MATERIAL_ANIMAL, (byte) 0, null), true);
                            } catch (FailedException | NoSuchTemplateException e) {
                                RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                            }
                        }
                    });
                }

                if (templateId == CreatureTemplateIds.HELL_HOUND_CID) {
                    killers.forEach(killer -> {
                        if (Server.rand.nextInt(taxidermyChance) == 0) {
                            try {
                                Item inv = killer.getInventory();
                                inv.insertItem(ItemFactory.createItem(CustomItems.hellHoundTaxidermyHead.getTemplateId(), 1f + ((98f) * Server.rand.nextFloat()), Materials.MATERIAL_ANIMAL, (byte) 0, null), true);
                            } catch (FailedException | NoSuchTemplateException e) {
                                RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                            }
                        }
                    });
                }

                if (templateId == CreatureTemplateIds.LION_MOUNTAIN_CID) {
                    killers.forEach(killer -> {
                        Item inv = killer.getInventory();
                        if (Server.rand.nextInt(taxidermyChance) == 0) {
                            try {
                                inv.insertItem(ItemFactory.createItem(CustomItems.lionTaxidermyHead.getTemplateId(), 1f + ((98f) * Server.rand.nextFloat()), Materials.MATERIAL_ANIMAL, (byte) 0, null), true);
                            } catch (FailedException | NoSuchTemplateException e) {
                                RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                            }
                        }
                    });
                }

                if (templateId == CustomCreatures.hyenaId) {
                    killers.forEach(killer -> {
                        Item inv = killer.getInventory();
                        if (Server.rand.nextInt(taxidermyChance) == 0) {
                            try {
                                inv.insertItem(ItemFactory.createItem(CustomItems.hyenaTaxidermyHead.getTemplateId(), 1f + ((98f) * Server.rand.nextFloat()), Materials.MATERIAL_ANIMAL, (byte) 0, null), true);
                            } catch (FailedException | NoSuchTemplateException e) {
                                RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                            }
                        }
                    });
                }

                if (templateId == CustomCreatures.largeBoarId) {
                    killers.forEach(killer -> {
                        Item inv = killer.getInventory();
                        if (Server.rand.nextInt(taxidermyChance) == 0) {
                            try {
                                inv.insertItem(ItemFactory.createItem(CustomItems.boarTaxidermyHead.getTemplateId(), 1f + ((98f) * Server.rand.nextFloat()), Materials.MATERIAL_ANIMAL, (byte) 0, null), true);
                            } catch (FailedException | NoSuchTemplateException e) {
                                RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                            }
                        }
                    });
                }

                if (templateId == CreatureTemplateIds.WOLF_BLACK_CID) {
                    killers.forEach(killer -> {
                        Item inv = killer.getInventory();
                        if (Server.rand.nextInt(taxidermyChance) == 0) {
                            try {
                                inv.insertItem(ItemFactory.createItem(CustomItems.wolfTaxidermyHead.getTemplateId(), 1f + ((98f) * Server.rand.nextFloat()), Materials.MATERIAL_ANIMAL, (byte) 0, null), true);
                            } catch (FailedException | NoSuchTemplateException e) {
                                RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                            }
                        }
                    });
                }

                if (templateId == CreatureTemplateIds.WORG_CID) {
                    killers.forEach(killer -> {
                        Item inv = killer.getInventory();
                        if (Server.rand.nextInt(taxidermyChance) == 0) {
                            try {
                                inv.insertItem(ItemFactory.createItem(CustomItems.worgTaxidermyHead.getTemplateId(), 1f + ((98f) * Server.rand.nextFloat()), Materials.MATERIAL_ANIMAL, (byte) 0, null), true);
                            } catch (FailedException | NoSuchTemplateException e) {
                                RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                            }
                        }
                    });
                }

                // =======================================================================================
                // =================================== Halloween Stuff ===================================
                // =======================================================================================
                int partsChance = 10;
                if (Holidays.isRequiemHalloween()) {
                    if (creature.isUndead() || creature.isMonster()) {
                        killers.forEach(killer -> {
                            Item inv = killer.getInventory();
                            if (Server.rand.nextInt(candyChance) == 0) {
                                try {
                                    inv.insertItem(ItemFactory.createItem(ItemList.sweet, 10f + (Server.rand.nextFloat() * 89.9f), Materials.MATERIAL_VEGETARIAN, (byte) 0, null), true);
                                } catch (FailedException | NoSuchTemplateException e) {
                                    RequiemLogging.logException(String.format("Error in %s loot", creature.getNameWithoutPrefixes()), e);
                                }
                            }
                        });
                    }

                    if (templateId == CustomCreatures.scaryPumpkinId) {
                        killers.forEach(killer -> {
                            Item inv = killer.getInventory();
                            switch (Server.rand.nextInt(partsChance)) {
                                case 0:
                                    try {
                                        inv.insertItem(ItemFactory.createItem(ItemList.pumpkin, 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_LEATHER, (byte) (Server.rand.nextInt(3)), null), true);
                                        inv.insertItem(ItemFactory.createItem(ItemList.sweet, 10f + (Server.rand.nextFloat() * 89.9f), Materials.MATERIAL_VEGETARIAN, (byte) 0, null), true);
                                    } catch (FailedException | NoSuchTemplateException e) {
                                        RequiemLogging.logException("Error in scary pumpkin loot, pumpkin", e);
                                    }
                                    break;
                                case 1:
                                    try {
                                        inv.insertItem(ItemFactory.createItem(ItemList.pumpkinSeed, 1f + ((99.9f) * Server.rand.nextFloat()), Materials.MATERIAL_VEGETARIAN, (byte) 0, null), true);
                                        inv.insertItem(ItemFactory.createItem(ItemList.sweet, 10f + (Server.rand.nextFloat() * 89.9f), Materials.MATERIAL_VEGETARIAN, (byte) 0, null), true);
                                    } catch (FailedException | NoSuchTemplateException e) {
                                        RequiemLogging.logException("Error in scary pumpkin loot, seed", e);
                                    }
                                    break;
                                case 2:
                                    try {
                                        inv.insertItem(ItemFactory.createItem(ItemList.pumpkinHalloween, 20f + ((79.9f) * Server.rand.nextFloat()), Materials.MATERIAL_VEGETARIAN, (byte) 0, null), true);
                                        inv.insertItem(ItemFactory.createItem(ItemList.sweet, 10f + (Server.rand.nextFloat() * 89.9f), Materials.MATERIAL_VEGETARIAN, (byte) 0, null), true);
                                    } catch (FailedException | NoSuchTemplateException e) {
                                        RequiemLogging.logException("Error in pumpkin loot, carved", e);
                                    }
                                    break;
                            }
                            if (Server.rand.nextInt(boneChance) == 0) {
                                try {
                                    inv.insertItem(ItemFactory.createItem(ItemList.boneCollar, 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_BONE, (byte) 1, creature.getNameWithoutPrefixes()), true);
                                } catch (FailedException | NoSuchTemplateException e) {
                                    RequiemLogging.logException("Error in halloween loot, bone collar, pumpkin", e);
                                }
                            }
                            if (Server.rand.nextInt(shoulderChance) == 0) {
                                try {
                                    inv.insertItem(ItemFactory.createItem(ItemList.shoulderPumpkinHalloween, 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_LEATHER, (byte) (Server.rand.nextInt(3)), null), true);
                                } catch (FailedException | NoSuchTemplateException e) {
                                    RequiemLogging.logException("Error in halloween loot, shoulder", e);
                                }
                            }
                            if (Server.rand.nextInt(maskChance) == 0) {
                                try {
                                    inv.insertItem(ItemFactory.createItem(ItemList.maskTrollHalloween, 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_LEATHER, (byte) (Server.rand.nextInt(3)), null), true);
                                } catch (FailedException | NoSuchTemplateException e) {
                                    RequiemLogging.logException("Error in halloween loot, mask", e);
                                }
                            }
                        });
                    }

                    if (templateId == CustomCreatures.ominousTreeId) {
                        killers.forEach(killer -> {
                            Item inv = killer.getInventory();
                            switch (Server.rand.nextInt(partsChance)) {
                                case 0:
                                    try {
                                        inv.insertItem(ItemFactory.createItem(ItemList.branch, 1f + ((99.9f) * Server.rand.nextFloat()), Materials.MATERIAL_WOOD_BIRCH, (byte) 0, null), true);
                                        inv.insertItem(ItemFactory.createItem(ItemList.sweet, 10f + (Server.rand.nextFloat() * 89.9f), Materials.MATERIAL_VEGETARIAN, (byte) 0, null), true);
                                    } catch (FailedException | NoSuchTemplateException e) {
                                        RequiemLogging.logException("Error in halloween loot, tree branch", e);
                                    }
                                    break;
                                case 1:
                                    try {
                                        inv.insertItem(ItemFactory.createItem(ItemList.log, 1f + ((99.9f) * Server.rand.nextFloat()), Materials.MATERIAL_WOOD_BIRCH, (byte) 0, null), true);
                                        inv.insertItem(ItemFactory.createItem(ItemList.sweet, 10f + (Server.rand.nextFloat() * 89.9f), Materials.MATERIAL_VEGETARIAN, (byte) 0, null), true);
                                    } catch (FailedException | NoSuchTemplateException e) {
                                        RequiemLogging.logException("Error in halloween loot, log", e);
                                    }
                                    break;
                                case 2:
                                    try {
                                        inv.insertItem(ItemFactory.createItem(ItemList.acorn, 1f + ((99.9f) * Server.rand.nextFloat()), Materials.MATERIAL_WOOD_BIRCH, (byte) 0, null), true);
                                        inv.insertItem(ItemFactory.createItem(ItemList.sweet, 10f + (Server.rand.nextFloat() * 89.9f), Materials.MATERIAL_VEGETARIAN, (byte) 0, null), true);
                                    } catch (FailedException | NoSuchTemplateException e) {
                                        RequiemLogging.logException("Error in halloween loot, acorn", e);
                                    }
                                    break;
                            }
                            if (Server.rand.nextInt(boneChance) == 0) {
                                try {
                                    inv.insertItem(ItemFactory.createItem(ItemList.boneCollar, 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_BONE, (byte) 1, creature.getNameWithoutPrefixes()), true);
                                } catch (FailedException | NoSuchTemplateException e) {
                                    RequiemLogging.logException("Error in halloween loot, bone collar, tree", e);
                                }
                            }
                            if (Server.rand.nextInt(shoulderChance) == 0) {
                                try {
                                    inv.insertItem(ItemFactory.createItem(ItemList.shoulderPumpkinHalloween, 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_LEATHER, (byte) (Server.rand.nextInt(3)), null), true);
                                } catch (FailedException | NoSuchTemplateException e) {
                                    RequiemLogging.logException("Error in halloween loot, shoulder", e);
                                }
                            }
                            if (Server.rand.nextInt(maskChance) == 0) {
                                try {
                                    inv.insertItem(ItemFactory.createItem(ItemList.maskTrollHalloween, 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_LEATHER, (byte) (Server.rand.nextInt(3)), null), true);
                                } catch (FailedException | NoSuchTemplateException e) {
                                    RequiemLogging.logException("Error in halloween loot, mask", e);
                                }
                            }
                        });
                    }
                }

                // =======================================================================================
                // =================================== Christmas Stuff ===================================
                // =======================================================================================
                if (Holidays.isRequiemChristmas()) {
                    if (MethodsBestiary.isChristmasMob(creature)) {
                        killers.forEach(killer -> {
                            Item inv = killer.getInventory();
                            if (Server.rand.nextInt(xmasChance) == 0) {
                                try {
                                    inv.insertItem(ItemFactory.createItem(ItemList.saddleBagsXmas, 99f, Materials.MATERIAL_LEATHER, (byte) (Server.rand.nextInt(3)), null), true);
                                    inv.insertItem(ItemFactory.createItem(ItemList.sweet, 10f + (Server.rand.nextFloat() * 89.9f), Materials.MATERIAL_VEGETARIAN, (byte) 0, null), true);
                                } catch (FailedException | NoSuchTemplateException e) {
                                    RequiemLogging.logException("Error in christmas loot, saddlebags", e);
                                }
                            }
                        });
                    }

                    if (templateId == CustomCreatures.sinisterSantaId) {
                        killers.forEach(killer -> {
                            Item inv = killer.getInventory();
                            if (Server.rand.nextInt(xmasHat) == 0) {
                                try {
                                    Item santaHat = ItemFactory.createItem(ItemList.santaHat, 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_COTTON, MiscConstants.COMMON, null);
                                    santaHat.setColor(WurmColor.createColor(255, 0, 0));
                                    santaHat.setColor2(WurmColor.createColor(0, 0, 0));
                                    inv.insertItem(santaHat, true);
                                    inv.insertItem(ItemFactory.createItem(ItemList.sweet, 10f + (Server.rand.nextFloat() * 89.9f), Materials.MATERIAL_VEGETARIAN, (byte) 0, null), true);
                                } catch (FailedException | NoSuchTemplateException e) {
                                    RequiemLogging.logException("Error in christmas loot, evil santa", e);
                                }
                            }
                        });
                    }

                    if (templateId == CustomCreatures.grinchId) {
                        killers.forEach(killer -> {
                            Item inv = killer.getInventory();
                            switch (Server.rand.nextInt(partsChance)) {
                                case 0:
                                    try {
                                        inv.insertItem(ItemFactory.createItem(ItemList.snowLantern, 99f, Materials.MATERIAL_WOOD_BIRCH, (byte) 0, null), true);
                                    } catch (FailedException | NoSuchTemplateException e) {
                                        RequiemLogging.logException("Error in christmas loot, grinch, lantern", e);
                                    }
                                    break;
                                case 1:
                                    try {
                                        inv.insertItem(ItemFactory.createItem(ItemList.boneCollar, 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_BONE, (byte) 1, creature.getNameWithoutPrefixes()), true);
                                    } catch (FailedException | NoSuchTemplateException e) {
                                        RequiemLogging.logException("Error in christmas loot, grinch, log", e);
                                    }
                                    break;
                                case 2:
                                    try {
                                        inv.insertItem(ItemFactory.createItem(CustomItems.treasureBoxId, 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_GOLD, (byte) (Server.rand.nextInt(3)), null), true);
                                    } catch (FailedException | NoSuchTemplateException e) {
                                        RequiemLogging.logException("Error in christmas loot, grinch, acorn", e);
                                    }
                                    break;
                            }
                        });
                    }

                    if (templateId == CustomCreatures.snowmanId) {
                        killers.forEach(killer -> {
                            Item inv = killer.getInventory();
                            if (Server.rand.nextInt(partsChance) == 0) {
                                try {
                                    inv.insertItem(ItemFactory.createItem(ItemList.carrot, 1f + ((99.9f) * Server.rand.nextFloat()), Materials.MATERIAL_VEGETARIAN, (byte) 0, null), true);
                                    inv.insertItem(ItemFactory.createItem(ItemList.branch, 1f + ((99.9f) * Server.rand.nextFloat()), Materials.MATERIAL_WOOD_BIRCH, (byte) 0, null), true);
                                } catch (FailedException | NoSuchTemplateException e) {
                                    RequiemLogging.logException("Error in christmas loot, snowman", e);
                                }
                            }
                        });
                    }
                }
                if (templateId == CustomCreatures.reaperId || templateId == CustomCreatures.spectralDragonHatchlingId) {
                    Server.getInstance().broadCastAlert(String.format("The %s has been slain. A new creature shall enter the realm shortly.", creature.getNameWithoutPrefixes()));
                    sendLootHelp = true;
                } else if (Titans.isTitan(creature)) {
                    DiscordHandler.sendToDiscord(CustomChannel.TITAN, String.format("The Titan %s has been defeated!", creature.getNameWithoutPrefixes()));
                    Item armour = ItemTools.createRandomPlateChain(50f, 80f, Materials.MATERIAL_SERYLL, creature.getNameWithoutPrefixes());
                    if (armour != null) {
                        killers.forEach(killer -> {
                            Item inv = killer.getInventory();
                            if (Server.rand.nextInt(partsChance) == 0) {
                                ItemTools.applyEnchant(armour, (byte) 110, 80f + (Server.rand.nextInt(40))); // Harden
                                inv.insertItem(armour, true);
                                Titans.removeTitan(creature);
                            }
                        });
                        sendLootHelp = true;
                    }
                }

                if (templateId == CustomCreatures.reaperId || templateId == CustomCreatures.spectralDragonHatchlingId) {
                    Server.getInstance().broadCastAlert(String.format("The %s has been slain. A new creature shall enter the realm shortly.", creature.getNameWithoutPrefixes()));
                    sendLootHelp = true;
                }

                if (templateId == CustomCreatures.injuredPirateId) {
                    try {
                        ItemFactory.createItem(CustomItems.treasureHuntChestId, 99f, (float) 1478 * 4, (float) 928 * 4, Server.rand.nextFloat() * 360f, false, (byte) 3, -10, "Tarbeard the Terrible");
                        //ChatTabs.sendLocalChat(creature, "Curse ye to Hell and back again for taking my treasure!", 1, 220, 1);
                        SoundPlayer.playSound(SoundNames.EMOTE_INSULT_SND, creature, 0f);
                    } catch (NoSuchTemplateException | FailedException e) {
                        e.printStackTrace();
                    }
                }

                CreatureSpawns.creatureSpawnOnDeath(creature);

                if (Constants.creatureDeathLogging) {
                    RequiemLogging.CreatureDeathLogging(creature);
                }
            } catch (Exception e) {
                RequiemLogging.logException(String.format("Error in %s", LootTable.class.getName()), e);
            }
        }
    }
*/
}