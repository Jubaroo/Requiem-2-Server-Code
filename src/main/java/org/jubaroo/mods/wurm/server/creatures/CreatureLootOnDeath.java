package org.jubaroo.mods.wurm.server.creatures;

import com.wurmonline.server.*;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.CreatureTemplate;
import com.wurmonline.server.creatures.CreatureTemplateFactory;
import com.wurmonline.server.creatures.CreatureTemplateIds;
import com.wurmonline.server.items.*;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.players.Titles;
import com.wurmonline.server.sounds.SoundPlayer;
import com.wurmonline.server.villages.Village;
import com.wurmonline.server.villages.Villages;
import com.wurmonline.shared.constants.ItemMaterials;
import com.wurmonline.shared.constants.SoundNames;
import org.gotti.wurmunlimited.modsupport.IdFactory;
import org.gotti.wurmunlimited.modsupport.IdType;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.communication.discord.DiscordHandler;
import org.jubaroo.mods.wurm.server.creatures.bounty.LootBounty;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.items.ItemTools;
import org.jubaroo.mods.wurm.server.misc.CustomTitles;
import org.jubaroo.mods.wurm.server.misc.database.holidays.Holidays;
import org.jubaroo.mods.wurm.server.server.Constants;
import org.jubaroo.mods.wurm.server.tools.CreatureTools;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreatureLootOnDeath {

    private static <T> Stream<T> streamOfNullable(T v) {
        return v == null ? Stream.empty() : Stream.of(v);
    }

    public static void creatureDied(Creature creature, Map<Long, Long>attackers) {
        int templateId = creature.getTemplate().getTemplateId();
        int partsChance = 10;
        int boneChance = 99;
        int shoulderChance = 50;
        int maskChance = 50;
        int candyChance = 5;
        int taxidermyChance = 45;
        int xmasChance = 50;
        int xmasHat = 40;

        if (creature.getName().equals("Dock Worker") || creature.getName().equals("Dock worker")) {
            Constants.soundEmissionNpcs.remove(creature);
        }

        if (!Constants.disableCreatureLoot) {
            try {
                if (attackers == null || attackers.isEmpty()) return;
                long now = System.currentTimeMillis();
                Set<Player> killers = attackers.entrySet().stream()
                        .filter(e -> now - e.getValue() < TimeConstants.MINUTE_MILLIS * 10 && WurmId.getType(e.getKey()) == 0)
                        .flatMap(e -> streamOfNullable(Players.getInstance().getPlayerOrNull(e.getKey())))
                        .collect(Collectors.toSet());
                
                if (Titans.isTitan(creature)) {
                    DiscordHandler.sendToDiscord(CustomChannel.TITAN, String.format("The Titan %s has been defeated!", creature.getNameWithoutPrefixes()));
                    killers.forEach(killer -> {
                        Item inv = killer.getInventory();
                        for (int i = 0; i < 2; i++) {
                            inv.insertItem(Objects.requireNonNull(ItemTools.createRandomToolWeapon(1f, 99f, "")));
                        }
                        for (int i = 0; i < 4; i++) {
                            try {
                                inv.insertItem(ItemFactory.createItem(RandomUtils.randomGem(true), RandomUtils.getRandomQl(1, 99), ""));
                            } catch (FailedException | NoSuchTemplateException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            inv.insertItem(ItemFactory.createItem(CustomItems.treasureBoxId, 50f + (Server.rand.nextFloat() * 49.9f), Materials.MATERIAL_GOLD, (byte) (Server.rand.nextInt(3)), null), true);
                        } catch (FailedException | NoSuchTemplateException e) {
                            e.printStackTrace();
                        }
                        inv.insertItem(Objects.requireNonNull(ItemTools.createEnchantOrb(110)));
                        Titans.addTitanLoot(killer);
                        killer.addTitle(Titles.Title.getTitle(CustomTitles.TITAN_SLAYER));
                        if (killer.hasLink()) {
                            killer.getCommunicator().sendNormalServerMessage(String.format("You grab some gems, an enchant orb, and some other things from the corpse of the titan %s.", creature.getNameWithoutPrefixes()));
                        }
                    });
                }

                if (MethodsBestiary.isRareCreature(creature)) {
                    killers.forEach(killer -> {
                        try {
                            Item inv = killer.getInventory();
                            inv.insertItem(Objects.requireNonNull(ItemTools.createRandomMaterialsConstruction(30f, 99f, "")));
                            inv.insertItem(Objects.requireNonNull(ItemTools.createRandomMaterialsConstruction(30f, 99f, "")));
                            inv.insertItem(Objects.requireNonNull(ItemTools.createRandomMaterialsConstruction(30f, 99f, "")));
                            inv.insertItem(Objects.requireNonNull(ItemTools.createRandomMaterialsLumps(30f, 99f, "")));
                            inv.insertItem(Objects.requireNonNull(ItemTools.createRandomMaterialsLumps(30f, 99f, "")));
                            inv.insertItem(Objects.requireNonNull(ItemTools.createRandomMaterialsLumps(30f, 99f, "")));
                            inv.insertItem(Objects.requireNonNull(ItemTools.createRandomMaterialsLumps(30f, 99f, "")));
                            inv.insertItem(Objects.requireNonNull(ItemTools.createRandomMaterialsLumps(30f, 99f, "")));
                            inv.insertItem(Objects.requireNonNull(ItemTools.createRandomToolWeapon(30f, 99f, "")));
                            inv.insertItem(ItemFactory.createItem(CustomItems.riftCache.getTemplateId(), 50f + (30f * Server.rand.nextFloat()), creature.getName()), true);
                        } catch (FailedException | NoSuchTemplateException e) {
                            e.printStackTrace();
                        }

                        if (killer.hasLink()) {
                            killer.getCommunicator().sendNormalServerMessage(String.format("You find some useful things on the corpse of %s.", creature.getNameWithoutPrefixes()));
                        }
                    });
                }

                if (templateId > 0) {
                    killers.forEach(killer -> {
                        try {
                            int roll = Server.rand.nextInt(500);
                            if (roll == 0) {
                                Item inv = killer.getInventory();
                                inv.insertItem(ItemFactory.createItem(CustomItems.scrollOfVillageCreation.getTemplateId(), 99f, Materials.MATERIAL_PAPER, MiscConstants.COMMON, null), true);
                            }
                            else if (roll == 1) {
                                Item inv = killer.getInventory();
                                inv.insertItem(ItemFactory.createItem(CustomItems.scrollOfVillageWar.getTemplateId(), 99f, Materials.MATERIAL_PAPER, MiscConstants.COMMON, null), true);
                            }
                            else if (roll == 2) {
                                Item inv = killer.getInventory();
                                inv.insertItem(ItemFactory.createItem(CustomItems.scrollOfVillageHeal.getTemplateId(), 99f, Materials.MATERIAL_PAPER, MiscConstants.COMMON, null), true);
                            }

                        } catch (FailedException | NoSuchTemplateException e) {
                            e.printStackTrace();
                        }
                        if (killer.hasLink()) {
                            killer.getCommunicator().sendNormalServerMessage(String.format("You found a scroll on the corpse of %s.", creature.getNameWithoutPrefixes()));
                        }
                    });
                }

                if (creature.getStatus().isChampion()) {
                    killers.forEach(killer -> {
                        handleChampionLoot(killer);
                        if (killer.hasLink()) {
                            killer.getCommunicator().sendNormalServerMessage(String.format("You find some useful things on the corpse of %s.", creature.getNameWithoutPrefixes()));
                        }
                    });
                }

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
            } catch (Exception e) {
                RequiemLogging.logException("Error in CreatureLootOnDeath.creatureDied()", e);
            }
        }
    }

    private static void handleChampionLoot(Player killer) {
        Item inv = killer.getInventory();
        if (!Constants.disableCreatureLoot) {
            try {
                Item tool = ItemTools.createRandomLootTool();
                if (tool != null) {
                    inv.insertItem(tool, true);
                }
                if (Server.rand.nextInt(100) < 75) {
                    inv.insertItem(ItemFactory.createItem((Server.rand.nextBoolean() ? ItemList.adamantineBar : ItemList.glimmerSteelBar), 30 + (30 * Server.rand.nextFloat()), ""));
                }
                if (Server.rand.nextInt(100) < 5) {
                    inv.insertItem(ItemFactory.createItem(RandomUtils.randomMaskTemplates(), 90 + (9 * Server.rand.nextFloat()), ""));
                }
                if (Server.rand.nextInt(100) < 1) {
                    Item bone = ItemFactory.createItem(ItemList.boneCollar, 90 + (10 * Server.rand.nextFloat()), "");
                    bone.setRarity((byte) 1);
                    if (Server.rand.nextInt(100) < 1) {
                        bone.setRarity((byte) 2);
                    }
                    inv.insertItem(bone);
                }
            } catch (FailedException | NoSuchTemplateException e) {
                RequiemLogging.logException("Error in CreatureLootOnDeath.handleChampionLoot()", e);
            }
        }
    }

    public static void checkLootTable(Creature creature, Item corpse) {
        if (Constants.creatureDeathLogging) {
            RequiemLogging.CreatureDeathLogging(creature);
        }

        CreatureSpawns.creatureSpawnOnDeath(creature);

        if (!Constants.disableCreatureLoot) {
            int templateId = creature.getTemplate().getTemplateId();

            int randomChance0 = Server.rand.nextInt(3);
            int randomChance1 = Server.rand.nextInt(2);

            if (templateId == CustomCreatures.injuredPirateId) {
                try {
                    ItemFactory.createItem(CustomItems.treasureHuntChestId, 99f, (float) 1478 * 4, (float) 928 * 4, Server.rand.nextFloat() * 360f, false, (byte) 3, -10, "Tarbeard the Terrible");
                    //ChatTabs.sendLocalChat(creature, "Curse ye to Hell and back again for taking my treasure!", 1, 220, 1);
                    SoundPlayer.playSound(SoundNames.EMOTE_INSULT_SND, creature, 0f);
                } catch (NoSuchTemplateException | FailedException e) {
                    e.printStackTrace();
                }
            }

            if (creature.isReborn() || creature.isBred()) {
                return;
            }
            double cretStr = Bounty.getCreatureStrength(creature);
            int numCrystals = 0;
            double crystalStr = cretStr;
            if (creature.isUnique()) { // Uniques will drop 3x as many, and have special properties to enable dropping rare and possibly supreme versions as well.
                crystalStr *= 3;
            } else if (Servers.localServer.PVPSERVER) { // Arena gives double the amount of crystals.
                crystalStr *= 1.5;
            }
            // Award chaos crystals if the strength is high enough:
            if (crystalStr > 3000) { // 30 copper
                numCrystals += LootBounty.doRollingCrystalReward(creature, corpse, crystalStr, CustomItems.chaosCrystalId, 4, 5000);
            }
            if (crystalStr > 10000) { // 1 silver
                numCrystals += LootBounty.doRollingCrystalReward(creature, corpse, crystalStr, CustomItems.enchantersCrystalId, 5, 20000);
            }
            boolean sendLootHelp = false;
            // Begin loot table drops
            //int templateId = mob.getTemplate().getTemplateId();
            if (Servers.localServer.PVPSERVER && creature.isPlayer()) {
                if (creature.isDeathProtected()) {
                    RequiemLogging.logInfo(String.format("Death protection was active for %s. Inserting silver coin reward.", creature.getNameWithoutPrefixes()));
                    try {
                        Item silver = ItemFactory.createItem(ItemList.coinSilver, 99f, null);
                        corpse.insertItem(silver, true);
                    } catch (FailedException | NoSuchTemplateException e) {
                        e.printStackTrace();
                    }
                }
                Item[] items = creature.getAllItems();
                for (Item item : items) {
                    if (item.isRepairable()) {
                        item.setDamage(Math.min(99f, item.getDamage() + Math.max(10f + (Server.rand.nextFloat() * 5f), 10f * item.getDamageModifier(false))));
                    }
                }
            }
            if (templateId == CustomCreatures.reaperId || templateId == CustomCreatures.spectralDragonHatchlingId) {
                Server.getInstance().broadCastAlert(String.format("The %s has been slain. A new creature shall enter the realm shortly.", creature.getNameWithoutPrefixes()));
                sendLootHelp = true;
            } else if (Titans.isTitan(creature)) {
                DiscordHandler.sendToDiscord(CustomChannel.TITAN, String.format("The Titan %s has been defeated!", creature.getNameWithoutPrefixes()));
                Item armour = ItemTools.createRandomPlateChain(50f, 80f, Materials.MATERIAL_SERYLL, creature.getNameWithoutPrefixes());
                if (armour != null) {
                    ItemTools.applyEnchant(armour, (byte) 110, 80f + (Server.rand.nextInt(40))); // Harden
                    corpse.insertItem(armour, true);
                }
                Titans.removeTitan(creature);
                sendLootHelp = true;
            }
            if (creature.getTemplate().getTemplateId() == CreatureTemplateFactory.GOBLIN_CID) {
                // Random lump of metal from goblins.
                try {
                    int[] lumpIds = {
                            ItemList.adamantineBar,
                            ItemList.brassBar,
                            ItemList.bronzeBar,
                            ItemList.copperBar,
                            ItemList.glimmerSteelBar,
                            ItemList.goldBar,
                            ItemList.ironBar,
                            ItemList.leadBar,
                            ItemList.silverBar,
                            ItemList.steelBar,
                            ItemList.zincBar
                    };
                    Item randomLump = ItemFactory.createItem(lumpIds[Server.rand.nextInt(lumpIds.length)], 20 + (60 * Server.rand.nextFloat()), "");
                    corpse.insertItem(randomLump);
                } catch (FailedException | NoSuchTemplateException e) {
                    e.printStackTrace();
                }
            }
            if (creature.isUnique() && !MethodsBestiary.isRequiemGhost(creature)) {
                // Added in new test if statement to prevent spawning stuff from arena kills
                Village v = Villages.getVillage(creature.getTileX(), creature.getTileY(), creature.isOnSurface());
                if (!v.getName().equals("Event Center")) {
                    // Spawn random addy/glimmer veins throughout the world
                    LootBounty.blessWorldWithMoonVeins(creature);
                }
                // Spawn 5-10 friyan tablets throughout the world.
                LootBounty.spawnFriyanTablets();
                // Add unique loot
                LootBounty.insertUniqueLoot(creature, corpse);
                // Spawn Spectral Drake
                if (randomChance0 == 0) {
                    if (creature.isDragon()) {
                        // Spawn the spectral drake and add extra hide/scale
                        LootBounty.handleDragonLoot(creature, corpse);
                    }
                } else {
                    // Spawn undead
                    try {
                        if (randomChance1 == 1) {
                            byte ctype = (byte) Math.max(0, Server.rand.nextInt(17) - 5);
                            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(RequiemUtilities.getRandArrayInt(CreatureTools.randomUndead));
                            Creature.doNew(template.getTemplateId(), true, creature.getPosX(), creature.getPosY(), Server.rand.nextFloat() * 360f, creature.getLayer(), template.getName(), (byte) 0, creature.getKingdomId(), ctype, false, (byte) 150);
                            Server.getInstance().broadCastAction(String.format("The death of the %s attracts a powerful being from below, seeking to claim it's soul.", creature.getNameWithoutPrefixes()), creature, 20);
                            Server.getInstance().broadCastAlert("An undead creature is released from the underworld, seeking the soul of a powerful creature!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                sendLootHelp = true;
            }
            if (sendLootHelp) {
                RequiemLogging.logInfo("Beginning loot assistance message generation...");
                LootBounty.displayLootAssistance(creature);
            }
            if (numCrystals > 0) {
                Server.getInstance().broadCastAction(String.format("%s had something of interest...", creature.getNameWithoutPrefixes()), creature, 5);
            }
        }
    }

}