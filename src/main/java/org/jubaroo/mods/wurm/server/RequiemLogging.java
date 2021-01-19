package org.jubaroo.mods.wurm.server;

import com.wurmonline.server.NoSuchPlayerException;
import com.wurmonline.server.Players;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.PlayerInfoFactory;
import net.bdew.wurm.tools.server.ServerThreadExecutor;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.server.constants.LoggingConstants;
import org.jubaroo.mods.wurm.server.tools.CreatureTools;
import org.jubaroo.mods.wurm.server.tools.ItemTools;

import java.io.IOException;
import java.util.logging.Level;

import static org.jubaroo.mods.wurm.server.server.constants.CreatureConstants.*;
import static org.jubaroo.mods.wurm.server.server.constants.ItemConstants.*;
import static org.jubaroo.mods.wurm.server.server.constants.LoggingConstants.*;
import static org.jubaroo.mods.wurm.server.server.constants.OtherConstants.*;
import static org.jubaroo.mods.wurm.server.server.constants.PollingConstants.*;
import static org.jubaroo.mods.wurm.server.server.constants.ToggleConstants.*;

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
            if (LoggingConstants.debug) {
                logInfo(msg);
            }
        }
    }

    public static void RequiemLoggingMessages() {
        // ========================= Logger Messages ==============================
        // ============================ Actions ================================
        if (addGmProtect) {
            RequiemLogging.logInfo("Add GM Protect Action: Enabled");
        } else {
            RequiemLogging.logInfo("Add GM Protect Action: Disabled");
        }
        if (addGmUnprotect) {
            RequiemLogging.logInfo("Add GM Unprotect Action: Enabled");
        } else {
            RequiemLogging.logInfo("Add GM Unprotect Action: Disabled");
        }
        if (addSpawnGuard) {
            RequiemLogging.logInfo("Add Spawn Guard Action: Enabled");
        } else {
            RequiemLogging.logInfo("Add Spawn Guard Action: Disabled");
        }
        if (searchDens) {
            RequiemLogging.logInfo("Searching Dens/Lairs Action: Enabled");
        } else {
            RequiemLogging.logInfo("Searching Dens/Lairs Action: Disabled");
        }
        if (addLabyrinth) {
            RequiemLogging.logInfo("Add Labyrinth Action: Enabled");
        } else {
            RequiemLogging.logInfo("Add Labyrinth Action: Disabled");
        }
        if (terrainSmoothing) {
            RequiemLogging.logInfo("Automatic Terrain Smoothing Action: Enabled");
        } else {
            RequiemLogging.logInfo("Automatic Terrain Smoothing Action: Disabled");
        }
        // ========================== GM Changes ===========================
        if (gmFullFavor) {
            RequiemLogging.logInfo("GM Full Favor: Enabled");
        } else {
            RequiemLogging.logInfo("GM Full Favor: Disabled");
        }
        if (gmFullStamina) {
            RequiemLogging.logInfo("GM Full Stamina: Enabled");
        } else {
            RequiemLogging.logInfo("GM Full Stamina: Disabled");
        }
        if (addCommands) {
            RequiemLogging.logInfo("Add Commands: Enabled");
        } else {
            RequiemLogging.logInfo("Add Commands: Disabled");
        }
        // ========================== Items ===========================
        if (itemHolyBook) {
            RequiemLogging.logInfo("Holy Book: Enabled");
            RequiemLogging.logInfo(String.format("Holy Book ID: %d", CustomItems.prayerBookId));
        } else {
            RequiemLogging.logInfo("Holy Book: Disabled");
        }
        if (itemNymphPortal) {
            RequiemLogging.logInfo("NymphPortal: Enabled");
            RequiemLogging.logInfo(String.format("Nymph Portal ID: %d", CustomItems.nymphPortal.getTemplateId()));
            RequiemLogging.logInfo(String.format("Nymph Portal Home ID: %d", CustomItems.nymphHomePortal.getTemplateId()));
        } else {
            RequiemLogging.logInfo("NymphPortal: Disabled");
        }
        if (itemDemonPortal) {
            RequiemLogging.logInfo("DemonPortal: Enabled");
            RequiemLogging.logInfo(String.format("Demon Portal ID: %d", CustomItems.demonPortal.getTemplateId()));
            RequiemLogging.logInfo(String.format("Demon Portal Home ID: %d", CustomItems.demonHomePortal.getTemplateId()));
        } else {
            RequiemLogging.logInfo("DemonPortal: Disabled");
        }
        if (enableDepots) {
            RequiemLogging.logInfo("Depots: Enabled");
        } else {
            RequiemLogging.logInfo("Depots: Disabled");
        }
        RequiemLogging.logInfo(String.format("Elemental Crystal Creation Damage: %d", itemCrystalCreationDamage));
        // ========================== Creatures ===========================
        if (setUnicornIsHorse) {
            RequiemLogging.logInfo("set Unicorn Is Horse: Enabled");
        } else {
            RequiemLogging.logInfo("set Unicorn Is Horse: Disabled");
        }
        if (animals) {
            RequiemLogging.logInfo("Animals: Enabled");
        } else {
            RequiemLogging.logInfo("Animals: Disabled");
        }
        if (customMounts) {
            RequiemLogging.logInfo("Custom Mounts: Enabled");
        } else {
            RequiemLogging.logInfo("Custom Mounts: Disabled");
        }
        if (farmAnimals) {
            RequiemLogging.logInfo("Farm Animals: Enabled");
        } else {
            RequiemLogging.logInfo("Farm Animals: Disabled");
        }
        //if (Misc.isRequiemChristmas() | Misc.isRequiemChristmasEnd()()) {
        //    Requiem.RequiemLogging.logInfo("Christmas Mobs And Loot: Enabled");
        //} else {
        //    Requiem.RequiemLogging.logInfo("Christmas Mobs And Loot: Disabled");
        //}
        //if (Misc.isRequiemHalloween()) {
        //    Requiem.RequiemLogging.logInfo("Halloween Mobs And Loot: Enabled");
        //} else {
        //    Requiem.RequiemLogging.logInfo("Halloween Mobs And Loot: Disabled");
        //}
        if (milkWhales) {
            RequiemLogging.logInfo("Domesticate Whales For Milking: Enabled");
        } else {
            RequiemLogging.logInfo("Domesticate Whales For Milking: Disabled");
        }
        if (monsters) {
            RequiemLogging.logInfo("Monsters: Enabled");
        } else {
            RequiemLogging.logInfo("Monsters: Disabled");
        }
        if (npc) {
            RequiemLogging.logInfo("NPCs: Enabled");
        } else {
            RequiemLogging.logInfo("NPCs: Disabled");
        }
        if (titans) {
            RequiemLogging.logInfo("Titans: Enabled");
        } else {
            RequiemLogging.logInfo("Titans: Disabled");
        }
        if (humans) {
            RequiemLogging.logInfo("Humans: Enabled");
        } else {
            RequiemLogging.logInfo("Humans: Disabled");
        }
        if (uniques) {
            RequiemLogging.logInfo("Uniques: Enabled");
        } else {
            RequiemLogging.logInfo("Uniques: Disabled");
        }
        if (undead) {
            RequiemLogging.logInfo("Undead: Enabled");
        } else {
            RequiemLogging.logInfo("Undead: Disabled");
        }
        if (wyverns) {
            RequiemLogging.logInfo("Wyverns: Enabled");
        } else {
            RequiemLogging.logInfo("Wyverns: Disabled");
        }
        // ========================== Halloween ===========================
        if (halloweenMobs) {
            RequiemLogging.logInfo("Halloween Mobs: Enabled");
        } else {
            RequiemLogging.logInfo("Halloween Mobs: Disabled");
        }
        // ========================== Christmas ===========================
        if (christmasMobs) {
            RequiemLogging.logInfo("Christmas Mobs: Enabled");
        } else {
            RequiemLogging.logInfo("Christmas Mobs: Disabled");
        }
        // ========================== Spells ===========================
        RequiemLogging.logInfo(String.format("No Cooldown On These Spells: %s", noCooldownSpells));
        // ========================== Other ===========================
        if (stfuNpcs) {
            RequiemLogging.logInfo("Stfu Npcs: Enabled");
        } else {
            RequiemLogging.logInfo("Stfu Npcs: Disabled");
        }
        if (loadFullContainers) {
            RequiemLogging.logInfo("Load Full Containers: Enabled");
        } else {
            RequiemLogging.logInfo("Load Full Containers: Disabled");
        }
        if (noMineDrift) {
            RequiemLogging.logInfo("No Mine Drift: Enabled");
        } else {
            RequiemLogging.logInfo("No Mine Drift: Disabled");
        }
        if (lampsAutoLight) {
            RequiemLogging.logInfo("Lamps Auto Light: Enabled");
        } else {
            RequiemLogging.logInfo("Lamps Auto Light: Disabled");
        }
        if (allowTentsOnDeed) {
            RequiemLogging.logInfo("Allow Tents On Deed: Enabled");
        } else {
            RequiemLogging.logInfo("Allow Tents On Deed: Disabled");
        }
        if (allSurfaceMine) {
            RequiemLogging.logInfo("All Surface Mine: Enabled");
        } else {
            RequiemLogging.logInfo("All Surface Mine: Disabled");
        }
        if (hidePlayerGodInscriptions) {
            RequiemLogging.logInfo("Hide Player God Inscriptions: Enabled");
        } else {
            RequiemLogging.logInfo("Hide Player God Inscriptions: Disabled");
        }
        // ========================== Mission Items ===========================
        if (addMissionItems) {
            RequiemLogging.logInfo("Add Mission Items: Enabled");
        } else {
            RequiemLogging.logInfo("Add Mission Items: Disabled");
        }
        if (coins) {
            RequiemLogging.logInfo("Coins: Enabled");
        } else {
            RequiemLogging.logInfo("Coins: Disabled");
        }
        if (riftItems) {
            RequiemLogging.logInfo("Rift Items: Enabled");
        } else {
            RequiemLogging.logInfo("Rift Items: Disabled");
        }
        if (miscItems) {
            RequiemLogging.logInfo("Misc Items: Enabled");
        } else {
            RequiemLogging.logInfo("Misc Items: Disabled");
        }
        if (metalLumps) {
            RequiemLogging.logInfo("Metal Lumps: Enabled");
        } else {
            RequiemLogging.logInfo("Metal Lumps: Disabled");
        }
        if (metalOres) {
            RequiemLogging.logInfo("Metal Ores: Enabled");
        } else {
            RequiemLogging.logInfo("Metal Ores: Disabled");
        }
        if (gems) {
            RequiemLogging.logInfo("Gems: Enabled");
        } else {
            RequiemLogging.logInfo("Gems: Disabled");
        }
        if (potionsSalvesOils) {
            RequiemLogging.logInfo("Potions, Salves, And Oils: Enabled");
        } else {
            RequiemLogging.logInfo("Potions, Salves, And Oils: Disabled");
        }
        if (mineDoors) {
            RequiemLogging.logInfo("Mine Doors: Enabled");
        } else {
            RequiemLogging.logInfo("Mine Doors: Disabled");
        }
        if (mirrors) {
            RequiemLogging.logInfo("Mirrors: Enabled");
        } else {
            RequiemLogging.logInfo("Mirrors: Disabled");
        }
        if (masks) {
            RequiemLogging.logInfo("Masks: Enabled");
        } else {
            RequiemLogging.logInfo("Masks: Disabled");
        }
        if (magicItems) {
            RequiemLogging.logInfo("Magic Items: Enabled");
        } else {
            RequiemLogging.logInfo("Magic Items: Disabled");
        }
        if (wands) {
            RequiemLogging.logInfo("Wands: Enabled");
        } else {
            RequiemLogging.logInfo("Wands: Disabled");
        }
        // ====================== Athanor Mechanism =======================
        RequiemLogging.logInfo(String.format("delayFogGoblins: %d", delayFogGoblins));
        RequiemLogging.logInfo(String.format("delayTradeTents: %d", delayTradeTents));
        RequiemLogging.logInfo(String.format("delayResourcePoints: %d", delayResourcePoints));
        RequiemLogging.logInfo(String.format("delayLootCarpets: %d", delayLootCarpets));
        RequiemLogging.logInfo(String.format("delayMobSpawners: %d", delayMobSpawners));
        RequiemLogging.logInfo(String.format("delayAthanorMechanism: %d", delayAthanorMechanism));
        // ========================== debugging ===========================
        if (executionCostLogging) {
            RequiemLogging.logInfo("Execution Cost Logging: Enabled");
        } else {
            RequiemLogging.logInfo("Execution Cost Logging: Disabled");
        }
        if (debug) {
            RequiemLogging.logInfo("Debug: Enabled");
        } else {
            RequiemLogging.logInfo("Debug: Disabled");
        }
        if (creatureCreateLogging) {
            RequiemLogging.logInfo("New Creature Logging: Enabled");
        } else {
            RequiemLogging.logInfo("New Creature Logging: Disabled");
        }
        if (creatureDeathLogging) {
            RequiemLogging.logInfo("Creature Death Logging: Enabled");
        } else {
            RequiemLogging.logInfo("Creature Death Logging: Disabled");
        }
        if (itemCreateLogging) {
            RequiemLogging.logInfo("Item Creation Logging: Enabled");
        } else {
            RequiemLogging.logInfo("Item Creation Logging: Disabled");
        }
        // ========================== disable modules ===========================
        if (disableEntireMod) {
            RequiemLogging.logInfo("Requiem Module: Disabled");
        } else {
            RequiemLogging.logInfo("Requiem Module: Enabled");
        }
        if (disableCreatureLoot) {
            RequiemLogging.logInfo("Creature Loot Module: Disabled");
        } else {
            RequiemLogging.logInfo("Creature Loot Module: Enabled");
        }
        if (disableCreatureMods) {
            RequiemLogging.logInfo("Creature Module: Disabled");
        } else {
            RequiemLogging.logInfo("Creature Module: Enabled");
        }
        if (disableItemMods) {
            RequiemLogging.logInfo("Item Module: Disabled");
        } else {
            RequiemLogging.logInfo("Item Module: Enabled");
        }
        if (disableVehicleMods) {
            RequiemLogging.logInfo("Vehicle Module: Disabled");
        } else {
            RequiemLogging.logInfo("Vehicle Module: Enabled");
        }
        if (disableBytecodeMods) {
            RequiemLogging.logInfo("Bytecode Module: Disabled");
        } else {
            RequiemLogging.logInfo("Bytecode Module: Enabled");
        }
        if (disableMiscMods) {
            RequiemLogging.logInfo("Misc Module: Disabled");
        } else {
            RequiemLogging.logInfo("Misc Module: Enabled");
        }

        logInfo(String.format("Newly created mailboxes will%s get %f power Courier enchants.", (!mailboxEnableEnchant ? " not" : ""), mailboxEnchantPower));

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
                    RequiemLogging.logInfo(String.format("Number of attackers: %d", creature.numattackers));
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
                //Requiem.RequiemLogging.logInfo("Creature was killed by: " + attackers);
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
