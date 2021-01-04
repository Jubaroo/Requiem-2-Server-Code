package org.jubaroo.mods.wurm.server;

import com.wurmonline.server.NoSuchPlayerException;
import com.wurmonline.server.Players;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.PlayerInfoFactory;
import net.bdew.wurm.tools.server.ServerThreadExecutor;
import org.jubaroo.mods.wurm.server.creatures.CreatureTools;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.items.ItemTools;
import org.jubaroo.mods.wurm.server.server.Constants;

import java.io.IOException;
import java.util.logging.Level;

public class RequiemLogging {

    public static void logException(String msg, Throwable e) {
        if (Requiem.logger != null)
            Requiem.logger.log(Level.SEVERE, msg, e);
    }

    public static void logWarning(String msg) {
        if (Requiem.logger != null)
            Requiem.logger.log(Level.WARNING, msg);
    }

    public static void logInfo(String msg) {
        if (Requiem.logger != null)
            Requiem.logger.log(Level.INFO, msg);
    }

    public static void debug(String msg) {
        if (Requiem.logger != null) {
            if (Constants.debug) {
                Requiem.logger.info(msg);
            }
        }
    }

    public static void RequiemLoggingMessages() {
        // ========================= Logger Messages ==============================
        // ============================ Actions ================================
        if (Constants.addGmProtect) {
            debug("Add GM Protect Action: Enabled");
        } else {
            debug("Add GM Protect Action: Disabled");
        }
        if (Constants.addGmUnprotect) {
            debug("Add GM Unprotect Action: Enabled");
        } else {
            debug("Add GM Unprotect Action: Disabled");
        }
        if (Constants.addSpawnGuard) {
            debug("Add Spawn Guard Action: Enabled");
        } else {
            debug("Add Spawn Guard Action: Disabled");
        }
        if (Constants.searchDens) {
            debug("Searching Dens/Lairs Action: Enabled");
        } else {
            debug("Searching Dens/Lairs Action: Disabled");
        }
        if (Constants.addLabyrinth) {
            debug("Add Labyrinth Action: Enabled");
        } else {
            debug("Add Labyrinth Action: Disabled");
        }
        if (Constants.terrainSmoothing) {
            debug("Automatic Terrain Smoothing Action: Enabled");
        } else {
            debug("Automatic Terrain Smoothing Action: Disabled");
        }
        // ========================== GM Changes ===========================
        if (Constants.gmFullFavor) {
            debug("GM Full Favor: Enabled");
        } else {
            debug("GM Full Favor: Disabled");
        }
        if (Constants.gmFullStamina) {
            debug("GM Full Stamina: Enabled");
        } else {
            debug("GM Full Stamina: Disabled");
        }
        if (Constants.addCommands) {
            debug("Add Commands: Enabled");
        } else {
            debug("Add Commands: Disabled");
        }
        // ========================== Items ===========================
        if (Constants.itemHolyBook) {
            debug("Holy Book: Enabled");
            debug(String.format("Holy Book ID: %d", CustomItems.prayerBookId));
        } else {
            debug("Holy Book: Disabled");
        }
        if (Constants.itemNymphPortal) {
            debug("NymphPortal: Enabled");
            debug(String.format("Nymph Portal ID: %d", CustomItems.nymphPortal.getTemplateId()));
            debug(String.format("Nymph Portal Home ID: %d", CustomItems.nymphHomePortal.getTemplateId()));
        } else {
            debug("NymphPortal: Disabled");
        }
        if (Constants.itemDemonPortal) {
            debug("DemonPortal: Enabled");
            debug(String.format("Demon Portal ID: %d", CustomItems.demonPortal.getTemplateId()));
            debug(String.format("Demon Portal Home ID: %d", CustomItems.demonHomePortal.getTemplateId()));
        } else {
            debug("DemonPortal: Disabled");
        }
        if (Constants.enableDepots) {
            debug("Depots: Enabled");
        } else {
            debug("Depots: Disabled");
        }
        debug(String.format("Elemental Crystal Creation Damage: %d", Constants.itemCrystalCreationDamage));
        // ========================== Creatures ===========================
        if (Constants.setUnicornIsHorse) {
            debug("set Unicorn Is Horse: Enabled");
        } else {
            debug("set Unicorn Is Horse: Disabled");
        }
        if (Constants.animals) {
            debug("Animals: Enabled");
        } else {
            debug("Animals: Disabled");
        }
        if (Constants.customMounts) {
            debug("Custom Mounts: Enabled");
        } else {
            debug("Custom Mounts: Disabled");
        }
        if (Constants.farmAnimals) {
            debug("Farm Animals: Enabled");
        } else {
            debug("Farm Animals: Disabled");
        }
        //if (Misc.isRequiemChristmas() | Misc.isRequiemChristmasEnd()()) {
        //    Requiem.debug("Christmas Mobs And Loot: Enabled");
        //} else {
        //    Requiem.debug("Christmas Mobs And Loot: Disabled");
        //}
        //if (Misc.isRequiemHalloween()) {
        //    Requiem.debug("Halloween Mobs And Loot: Enabled");
        //} else {
        //    Requiem.debug("Halloween Mobs And Loot: Disabled");
        //}
        if (Constants.milkWhales) {
            debug("Domesticate Whales For Milking: Enabled");
        } else {
            debug("Domesticate Whales For Milking: Disabled");
        }
        if (Constants.monsters) {
            debug("Monsters: Enabled");
        } else {
            debug("Monsters: Disabled");
        }
        if (Constants.npc) {
            debug("NPCs: Enabled");
        } else {
            debug("NPCs: Disabled");
        }
        if (Constants.titans) {
            debug("Titans: Enabled");
        } else {
            debug("Titans: Disabled");
        }
        if (Constants.humans) {
            debug("Humans: Enabled");
        } else {
            debug("Humans: Disabled");
        }
        if (Constants.uniques) {
            debug("Uniques: Enabled");
        } else {
            debug("Uniques: Disabled");
        }
        if (Constants.undead) {
            debug("Undead: Enabled");
        } else {
            debug("Undead: Disabled");
        }
        if (Constants.wyverns) {
            debug("Wyverns: Enabled");
        } else {
            debug("Wyverns: Disabled");
        }
        // ========================== Halloween ===========================
        //if (Constants.halloweenMobs) {
        //    Requiem.debug("Halloween Mobs: Enabled");
        //} else {
        //    Requiem.debug("Halloween Mobs: Disabled");
        //}
        // ========================== Christmas ===========================
        if (Constants.christmasMobs) {
            debug("Christmas Mobs: Enabled");
        } else {
            debug("Christmas Mobs: Disabled");
        }
        // ========================== Spells ===========================
        debug(String.format("No Cooldown On These Spells: %s", Constants.noCooldownSpells));
        // ========================== Other ===========================
        if (Constants.stfuNpcs) {
            debug("Stfu Npcs: Enabled");
        } else {
            debug("Stfu Npcs: Disabled");
        }
        if (Constants.loadFullContainers) {
            debug("Load Full Containers: Enabled");
        } else {
            debug("Load Full Containers: Disabled");
        }
        if (Constants.noMineDrift) {
            debug("No Mine Drift: Enabled");
        } else {
            debug("No Mine Drift: Disabled");
        }
        if (Constants.lampsAutoLight) {
            debug("Lamps Auto Light: Enabled");
        } else {
            debug("Lamps Auto Light: Disabled");
        }
        if (Constants.allowTentsOnDeed) {
            debug("Allow Tents On Deed: Enabled");
        } else {
            debug("Allow Tents On Deed: Disabled");
        }
        if (Constants.allSurfaceMine) {
            debug("All Surface Mine: Enabled");
        } else {
            debug("All Surface Mine: Disabled");
        }
        if (Constants.hidePlayerGodInscriptions) {
            debug("Hide Player God Inscriptions: Enabled");
        } else {
            debug("Hide Player God Inscriptions: Disabled");
        }
        // ========================== Mission Items ===========================
        if (Constants.addMissionItems) {
            debug("Add Mission Items: Enabled");
        } else {
            debug("Add Mission Items: Disabled");
        }
        if (Constants.coins) {
            debug("Coins: Enabled");
        } else {
            debug("Coins: Disabled");
        }
        if (Constants.riftItems) {
            debug("Rift Items: Enabled");
        } else {
            debug("Rift Items: Disabled");
        }
        if (Constants.miscItems) {
            debug("Misc Items: Enabled");
        } else {
            debug("Misc Items: Disabled");
        }
        if (Constants.metalLumps) {
            debug("Metal Lumps: Enabled");
        } else {
            debug("Metal Lumps: Disabled");
        }
        if (Constants.metalOres) {
            debug("Metal Ores: Enabled");
        } else {
            debug("Metal Ores: Disabled");
        }
        if (Constants.gems) {
            debug("Gems: Enabled");
        } else {
            debug("Gems: Disabled");
        }
        if (Constants.potionsSalvesOils) {
            debug("Potions, Salves, And Oils: Enabled");
        } else {
            debug("Potions, Salves, And Oils: Disabled");
        }
        if (Constants.mineDoors) {
            debug("Mine Doors: Enabled");
        } else {
            debug("Mine Doors: Disabled");
        }
        if (Constants.mirrors) {
            debug("Mirrors: Enabled");
        } else {
            debug("Mirrors: Disabled");
        }
        if (Constants.masks) {
            debug("Masks: Enabled");
        } else {
            debug("Masks: Disabled");
        }
        if (Constants.magicItems) {
            debug("Magic Items: Enabled");
        } else {
            debug("Magic Items: Disabled");
        }
        if (Constants.wands) {
            debug("Wands: Enabled");
        } else {
            debug("Wands: Disabled");
        }
        // ====================== Athanor Mechanism =======================
        debug(String.format("delayFogGoblins: %d", Constants.delayFogGoblins));
        debug(String.format("delayTradeTents: %d", Constants.delayTradeTents));
        debug(String.format("delayResourcePoints: %d", Constants.delayResourcePoints));
        debug(String.format("delayLootCarpets: %d", Constants.delayLootCarpets));
        debug(String.format("delayMobSpawners: %d", Constants.delayMobSpawners));
        debug(String.format("delayAthanorMechanism: %d", Constants.delayAthanorMechanism));
        // ========================== debugging ===========================
        if (Constants.executionCostLogging) {
            debug("Execution Cost Logging: Enabled");
        } else {
            debug("Execution Cost Logging: Disabled");
        }
        if (Constants.debug) {
            debug("Debug: Enabled");
        } else {
            debug("Debug: Disabled");
        }
        if (Constants.creatureCreateLogging) {
            debug("New Creature Logging: Enabled");
        } else {
            debug("New Creature Logging: Disabled");
        }
        if (Constants.creatureDeathLogging) {
            debug("Creature Death Logging: Enabled");
        } else {
            debug("Creature Death Logging: Disabled");
        }
        if (Constants.itemCreateLogging) {
            debug("Item Creation Logging: Enabled");
        } else {
            debug("Item Creation Logging: Disabled");
        }
        // ========================== disable modules ===========================
        if (Constants.disableEntireMod) {
            debug("Requiem Module: Disabled");
        } else {
            debug("Requiem Module: Enabled");
        }
        if (Constants.disableCreatureLoot) {
            debug("Creature Loot Module: Disabled");
        } else {
            debug("Creature Loot Module: Enabled");
        }
        if (Constants.disableCreatureMods) {
            debug("Creature Module: Disabled");
        } else {
            debug("Creature Module: Enabled");
        }
        if (Constants.disableItemMods) {
            debug("Item Module: Disabled");
        } else {
            debug("Item Module: Enabled");
        }
        if (Constants.disableVehicleMods) {
            debug("Vehicle Module: Disabled");
        } else {
            debug("Vehicle Module: Enabled");
        }
        if (Constants.disableBytecodeMods) {
            debug("Bytecode Module: Disabled");
        } else {
            debug("Bytecode Module: Enabled");
        }
        if (Constants.disableMiscMods) {
            debug("Misc Module: Disabled");
        } else {
            debug("Misc Module: Enabled");
        }

        logInfo(String.format("Newly created mailboxes will%s get %f power Courier enchants.", (!Constants.mailboxEnableEnchant ? " not" : ""), Constants.mailboxEnchantPower));

    }

    public static void CreatureSpawnLogging(Creature creature) {
        ServerThreadExecutor.INSTANCE.execute(() -> {
            if (creature.getTemplate().getTemplateId() != 0) {
                logInfo("----------> Creature Spawned <----------");
                logInfo(String.format("Name: %s", creature.getNameWithoutPrefixes()));
                logInfo(String.format("TemplateId: %d", creature.getTemplate().getTemplateId()));
                logInfo(String.format("WID: %d", creature.getWurmId()));
                logInfo(String.format("Sex: %s", CreatureTools.getSexString(creature.getStatus().getSex())));
                logInfo(String.format("Condition: %s", CreatureTools.getTypeString(creature)));
                if (creature.getStatus().disease != 0) {
                    logInfo(String.format("Disease: %s", creature.getStatus().disease));
                } else {
                    logInfo("Disease: None");
                }
                    if (creature.isReborn()) {
                    logInfo("Creature has been reborn after death");
                }
                logInfo(String.format("Location: X= %s Y= %s", creature.getTileX(), creature.getTileY()));
                if (creature.getCurrentVillage() != null) {
                    logInfo(String.format("Deed name: %s", creature.getCurrentVillage().getName()));
                }
                logInfo(String.format("Zone Id: %d", creature.getStatus().getZoneId()));
                logInfo(String.format("Kingdom: %s", creature.getKingdomName()));
                if (creature.isOnSurface()) {
                    logInfo("Creature is above ground");
                } else logInfo("Creature is below ground");
                logInfo(String.format("Age: %d", creature.getStatus().age));
                logInfo(String.format("Age string: %s", creature.getStatus().getAgeString()));
                if (creature.numattackers != 0) {
                    debug(String.format("Number of attackers: %d", creature.numattackers));
                }
                logInfo(String.format("Hunger: %d", creature.getStatus().getHunger()));
                logInfo(String.format("Thirst: %d", creature.getStatus().getThirst()));
                logInfo(String.format("Fat: %s", creature.getStatus().getFats()));
            }
            logInfo("----------------------------------------");
        });
    }

    public static void CreatureDeathLogging(Creature creature) {
        try {
            // show inventory
            int templateId = creature.getTemplate().getTemplateId();
            if (templateId != 0) {
                logInfo("----------> Creature Death <----------");
                logInfo(String.format("Name: %s", creature.getNameWithoutPrefixes()));
                logInfo(String.format("TemplateId: %d", templateId));
                logInfo(String.format("WID: %d", creature.getWurmId()));
                logInfo(String.format("Sex: %s", CreatureTools.getSexString(creature.getStatus().getSex())));
                logInfo(String.format("Condition: %s", CreatureTools.getTypeString(creature)));
                logInfo(String.format("Location: X= %s Y= %s", creature.getTileX(), creature.getTileY()));
                if (creature.getCurrentVillage() != null) {
                    logInfo(String.format("Deed name: %s", creature.getCurrentVillage().getName()));
                }
                logInfo(String.format("Zone Id: %d", creature.getStatus().getZoneId()));
                logInfo(String.format("Surface?: %s", creature.isOnSurface()));
                //Requiem.debug("Creature was killed by: " + attackers);
                logInfo(String.format("Age: %d", creature.getStatus().age));
                logInfo(String.format("Age string: %s", creature.getStatus().getAgeString()));
                logInfo(String.format("Number of attackers: %d", creature.numattackers));
                logInfo(String.format("Is a player?: %s", creature.isPlayer()));
                logInfo(String.format("Hunger: %d", creature.getStatus().getHunger()));
                logInfo(String.format("Thirst: %d", creature.getStatus().getThirst()));
                if (creature.getNumberOfFollowers() != 0) {
                    logInfo(String.format("Followers: %s", creature.getNumberOfFollowers()));
                }
                if (creature.getLeader() != null) {
                    logInfo(String.format("Leader: %s", creature.getLeader().getNameWithoutPrefixes()));
                }
                if (creature.getDominator() != null) {
                    logInfo(String.format("Dominated by: %s", Players.getInstance().getNameFor(creature.dominator)));
                }
                if (creature.getHitched() != null) {
                    RequiemLogging.logInfo(String.format("Hitched Vehicle name: %s", creature.getHitched().name));
                }
                logInfo("----------------------------------------");
            }
        } catch (NoSuchPlayerException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void ItemCreationLogging(Item item) {
        ServerThreadExecutor.INSTANCE.execute(() -> {
            if (item.getTemplate().getTemplateId() != 0) {
                logInfo("----------> Item Created <----------");
                logInfo(String.format("Name: %s", item.getName()));
                logInfo(String.format("TemplateId: %s", item.getTemplate().getTemplateId()));
                logInfo(String.format("WID: %s", item.getWurmId()));
                if (item.getParentOrNull() != null && !item.getParentOrNull().getName().equals(item.getName())) {
                    logInfo(String.format("Parent Name: %s", item.getParentOrNull().getName()));
                }
                //if (item.getTopParentOrNull() != null && !item.getTopParentOrNull().getName().equals(item.getParentOrNull().getName())) {
                //    logInfo(String.format("Top Parent Name: %s", item.getTopParentOrNull().getName()));
                //}
                if (item.isHollow()) {
                    logInfo(String.format("Container Size: X= %s, Y= %s, Z= %s", item.getContainerSizeX(), item.getContainerSizeY(), item.getContainerSizeZ()));
                } else if (item.isBulkContainer()) {
                    logInfo(String.format("Container Volume: %s", item.getContainerVolume()));
                }
                if (item.getOwnerId() == -10) {
                    logInfo(String.format("Created by: Server [%s]", item.getOwnerId()));
                } else {
                    logInfo(String.format("Created by: %s [%s]", PlayerInfoFactory.getPlayerName(item.lastOwner), item.getOwnerId()));
                }
                logInfo(String.format("Coordinates: X= %s Y= %s", item.getTileX(), item.getTileY()));
                logInfo(String.format("Raw Coordinates: X= %s Y= %s", item.getPosXRaw(), item.getPosYRaw()));
                logInfo(String.format("Quality: %s", item.getCurrentQualityLevel()));
                logInfo(String.format("Damage: %s", item.getDamage()));
                logInfo(String.format("Weight: %s grams", item.getWeightGrams()));
                logInfo(String.format("Rarity: %s", ItemTools.getRarityString(item.getRarity())));
                logInfo(String.format("Data1: %s", item.getData1()));
                logInfo(String.format("Data2: %s", item.getData2()));
                logInfo(String.format("AuxData: %s", item.getAuxData()));
                logInfo(String.format("Extra1: %s", item.getExtra1()));
                logInfo(String.format("Extra2: %s", item.getExtra2()));
                logInfo("----------------------------------------");
            }
        });
    }

    public static void ItemRemovalLogging(Item item) {
        if (item.getTemplate().getTemplateId() != 0) {
            logInfo("----------> Item Destroyed <----------");
            logInfo(String.format("Name: %s", item.getName()));
            logInfo(String.format("TemplateId: %s", item.getTemplate().getTemplateId()));
            logInfo(String.format("WID: %s", item.getWurmId()));
            if (item.getParentOrNull() != null && !item.getParentOrNull().getName().equals(item.getName())) {
                logInfo(String.format("Parent Name: %s", item.getParentOrNull().getName()));
            }
            //if (item.getTopParentOrNull() != null && !item.getTopParentOrNull().getName().equals(item.getParentOrNull().getName())) {
            //    logInfo(String.format("Top Parent Name: %s", item.getTopParentOrNull().getName()));
            //}
            if (item.isHollow()) {
                logInfo(String.format("Container Size: X= %s, Y= %s, Z= %s", item.getContainerSizeX(), item.getContainerSizeY(), item.getContainerSizeZ()));
            } else if (item.isBulkContainer()) {
                logInfo(String.format("Container Volume: %s", item.getContainerVolume()));
            }
            logInfo(String.format("Created by: %s [%s]", PlayerInfoFactory.getPlayerName(item.lastOwner), item.getOwnerId()));
            logInfo(String.format("Coordinates: X= %s Y= %s", item.getTileX(), item.getTileY()));
            logInfo(String.format("Quality: %s", item.getCurrentQualityLevel()));
            logInfo(String.format("Damage: %s", item.getDamage()));
            logInfo(String.format("Weight: %s grams", item.getWeightGrams()));
            logInfo(String.format("Rarity: %s", ItemTools.getRarityString(item.getRarity())));
            logInfo(String.format("Data1: %s", item.getData1()));
            logInfo(String.format("Data2: %s", item.getData2()));
            logInfo(String.format("AuxData: %s", item.getAuxData()));
            logInfo(String.format("Extra1: %s", item.getExtra1()));
            logInfo(String.format("Extra2: %s", item.getExtra2()));
            logInfo("----------------------------------------");
        }
    }

}
