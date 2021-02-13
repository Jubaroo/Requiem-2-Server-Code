package org.jubaroo.mods.wurm.server.creatures;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.FailedException;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Server;
import com.wurmonline.server.bodys.BodyTemplate;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.CreatureTemplate;
import com.wurmonline.server.creatures.CreatureTemplateFactory;
import com.wurmonline.server.creatures.NoSuchCreatureTemplateException;
import com.wurmonline.server.items.*;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.sounds.SoundPlayer;
import com.wurmonline.shared.constants.CreatureTypes;
import com.wurmonline.shared.constants.ItemMaterials;
import com.wurmonline.shared.constants.SoundNames;
import org.gotti.wurmunlimited.modsupport.creatures.EncounterBuilder;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.communication.discord.ChatHandler;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.creatures.traitedCreatures.Zebra;
import org.jubaroo.mods.wurm.server.tools.ItemTools;
import org.jubaroo.mods.wurm.server.tools.RandomUtils;
import org.jubaroo.mods.wurm.server.misc.Holidays;

import java.io.IOException;

import static com.wurmonline.mesh.Tiles.Tile.*;
import static com.wurmonline.server.creatures.CreatureTemplateIds.*;
import static com.wurmonline.server.zones.EncounterType.*;
import static com.wurmonline.shared.constants.Enchants.*;
import static com.wurmonline.shared.constants.ItemMaterials.MATERIAL_ADAMANTINE;
import static com.wurmonline.shared.constants.ItemMaterials.MATERIAL_GLIMMERSTEEL;
import static org.jubaroo.mods.wurm.server.ModConfig.*;
import static org.jubaroo.mods.wurm.server.creatures.CustomCreatures.*;
import static org.jubaroo.mods.wurm.server.server.constants.MessageConstants.displayOnScreen;

public class CreatureSpawns {

    private void spawnAttackers(Creature performer, int template, int num, boolean reborn) throws NoSuchCreatureTemplateException {
        CreatureTemplate tpl = CreatureTemplateFactory.getInstance().getTemplate(template);
        for (int i = 0; i < num; i++) {
            try {
                byte sex = tpl.getSex();
                if (sex == 0 && !tpl.keepSex && Server.rand.nextInt(2) == 0) {
                    sex = 1;
                }
                Creature spawned = Creature.doNew(template, true, performer.getPosX() - 5f + Server.rand.nextFloat() * 10, performer.getPosY() - 5f + Server.rand.nextFloat() * 10, Server.rand.nextInt(360), performer.isOnSurface() ? 0 : -1, "", sex, (byte) 0, Server.rand.nextInt(5) == 0 ? CreatureTypes.C_MOD_CHAMPION : CreatureTypes.C_MOD_NONE, reborn);
                spawned.setOpponent(performer);
            } catch (Exception e) {
                RequiemLogging.logException("Error spawning attackers", e);
            }
        }
    }

    //TODO put all spawns in each creature unless vanilla
    public static void spawnTable() {
        if (uniques) {
            CreatureSpawns.spawnOnMostTerrain(spiritStagId);
            CreatureSpawns.spawnOnMostTerrain(kongId);
            CreatureSpawns.spawnOnMostTerrain(facebreykerId);
            CreatureSpawns.spawnOnMostTerrain(whiteBuffaloId);
        }
        if (undead) {
            CreatureSpawns.spawnOnMostTerrain(zombieWalkerId);
            CreatureSpawns.spawnOnMostTerrain(zombieLeaderId);
            spawnOnTerrain(TILE_SNOW, ELEVATION_GROUND, spiritTrollId, 1, 1);
            spawnOnTerrain(TILE_TREE, ELEVATION_GROUND, spiritTrollId, 1, 1);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, reanimatedSkeletonId, 1, 1);
        }
        if (treasureGoblin) {
            CreatureSpawns.spawnOnMostTerrain(treasureGoblinId);
            CreatureSpawns.spawnOnMostTerrain(treasureGoblinMenageristGoblinId);
            //CreatureSpawns.spawnOnMostTerrain(CustomCreatures.treasureGoblinRainbowGoblinId);
            //CreatureSpawns.spawnOnMostTerrain(CustomCreatures.treasureGoblinOdiousCollectorId);
            //CreatureSpawns.spawnOnMostTerrain(CustomCreatures.treasureGoblinMalevolentTormentorId);
            //CreatureSpawns.spawnOnMostTerrain(CustomCreatures.treasureGoblinGemHoarderId);
            //CreatureSpawns.spawnOnMostTerrain(CustomCreatures.treasureGoblinBloodThiefId);
        }
        if (monsters) {
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, snakeVineId, 3, 1);
            spawnOnTerrain(TILE_BUSH_THORN, ELEVATION_GROUND, snakeVineId, 3, 3);
            spawnOnTerrain(TILE_TREE, ELEVATION_GROUND, prismaticBlobId, 1, 1);
            spawnOnTerrain(TILE_MOSS, ELEVATION_GROUND, prismaticBlobId, 1, 1);
            spawnOnTerrain(TILE_PEAT, ELEVATION_GROUND, prismaticBlobId, 1, 1);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, prismaticBlobId, 1, 2);
            spawnOnTerrain(TILE_TREE, ELEVATION_GROUND, mimicChestId, 2, 2);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, mimicChestId, 1, 1);
            spawnOnTerrain(TILE_ROCK, ELEVATION_GROUND, golemId, 2, 2);
            spawnOnTerrain(TILE_MARSH, ELEVATION_GROUND, golemId, 1, 1);
            spawnOnTerrain(TILE_CLAY, ELEVATION_GROUND, golemId, 1, 1);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, giantId, 1, 1);
            spawnOnTerrain(TILE_TREE, ELEVATION_GROUND, giantId, 1, 1);
            spawnOnTerrain(TILE_TREE, ELEVATION_GROUND, forestSpiderId, 3, 2);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, fireSpiderId, 1, 1);
            spawnOnTerrain(TILE_SAND, ELEVATION_GROUND, fireSpiderId, 1, 2);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, fireGiantId, 1, 1);
            spawnOnTerrain(TILE_SAND, ELEVATION_GROUND, fireGiantId, 1, 3);
            spawnOnTerrain(TILE_ROCK, ELEVATION_GROUND, cyclopsId, 1, 1);
            spawnOnTerrain(TILE_MARSH, ELEVATION_GROUND, cyclopsId, 1, 1);
            spawnOnTerrain(TILE_CLAY, ELEVATION_GROUND, cyclopsId, 1, 1);
            spawnOnTerrain(TILE_TREE, ELEVATION_GROUND, blobId, 1, 1);
            spawnOnTerrain(TILE_MOSS, ELEVATION_GROUND, blobId, 1, 1);
            spawnOnTerrain(TILE_PEAT, ELEVATION_GROUND, blobId, 1, 1);
            spawnOnTerrain(TILE_TAR, ELEVATION_GROUND, blobId, 1, 1);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, avengerId, 1, 1);
            // register vanilla creature templates that normally don't spawn
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, DEATHCRAWLER_MINION_CID, 1, 1);
            spawnOnTerrain(TILE_SAND, ELEVATION_GROUND, GOBLIN_CID, 1, 1);
            spawnOnTerrain(TILE_SAND, ELEVATION_GROUND, LAVA_CREATURE_CID, 1, 1);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, WORG_CID, 2, 1);
            spawnOnTerrain(TILE_STEPPE, ELEVATION_GROUND, WORG_CID, 2, 2);
            spawnOnTerrain(TILE_SAND, ELEVATION_GROUND, DEMON_SOL_CID, 1, 1);
            spawnOnTerrain(TILE_MYCELIUM, ELEVATION_GROUND, DEMON_SOL_CID, 1, 3);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, SPAWN_UTTACHA_CID, 1, 1);
            spawnOnTerrain(TILE_DIRT, ELEVATION_WATER, SEA_SERPENT_CID, 1, 1);
            spawnOnTerrain(TILE_DIRT, ELEVATION_DEEP_WATER, SEA_SERPENT_CID, 1, 1);
            spawnOnTerrain(TILE_GRASS, ELEVATION_WATER, SEA_SERPENT_CID, 1, 1);
            spawnOnTerrain(TILE_GRASS, ELEVATION_DEEP_WATER, SEA_SERPENT_CID, 1, 1);
        }
        if (humans) {
            CreatureSpawns.spawnOnMostTerrain(tombRaiderId);
            spawnOnTerrain(TILE_COBBLESTONE, ELEVATION_GROUND, banditId, 1, 3);
            spawnOnTerrain(TILE_COBBLESTONE_ROUGH, ELEVATION_GROUND, banditId, 1, 3);
            spawnOnTerrain(TILE_COBBLESTONE_ROUND, ELEVATION_GROUND, banditId, 1, 3);
        }
        if (Holidays.isRequiemHalloween() && !disableHolidayCreatures) {
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, ominousTreeId, 1, 1);
            spawnOnTerrain(TILE_TREE, ELEVATION_GROUND, ominousTreeId, 1, 1);
            spawnOnTerrain(TILE_DIRT, ELEVATION_GROUND, ominousTreeId, 1, 1);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, scaryPumpkinId, 1, 1);
            spawnOnTerrain(TILE_TREE, ELEVATION_GROUND, scaryPumpkinId, 1, 1);
            spawnOnTerrain(TILE_DIRT, ELEVATION_GROUND, scaryPumpkinId, 1, 1);
        }
        if (wyverns) {
            spawnOnTerrain(TILE_SNOW, ELEVATION_GROUND, whiteWyvernId, 1, 3);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, redWyvernId, 1, 3);
            spawnOnTerrain(TILE_TREE, ELEVATION_GROUND, greenWyvernId, 1, 3);
            spawnOnTerrain(TILE_MARSH, ELEVATION_GROUND, blueWyvernId, 1, 3);
            spawnOnTerrain(TILE_PEAT, ELEVATION_GROUND, blackWyvernId, 1, 3);
        }
        if (customMounts) {
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, rainbowZebraId, 1, 1);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, prismaticHellHorseId, 1, 1);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, hornedPonyId, 1, 1);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, chargerId, 1, 1);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, whiteRainbowUnicornId, 1, 1);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, purpleRainbowUnicornId, 1, 1);
        }
        if (Holidays.isRequiemChristmas() && !disableHolidayCreatures) {
            spawnOnTerrain(TILE_TUNDRA, ELEVATION_GROUND, snowmanId, 1, 2);
            spawnOnTerrain(TILE_BUSH_LINGONBERRY, ELEVATION_GROUND, snowmanId, 1, 1);
            spawnOnTerrain(TILE_SNOW, ELEVATION_GROUND, snowmanId, 1, 5);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, sinisterSantaId, 1, 1);
            spawnOnTerrain(TILE_SNOW, ELEVATION_GROUND, sinisterSantaId, 1, 5);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, rudolphId, 1, 1);
            spawnOnTerrain(TILE_SNOW, ELEVATION_GROUND, rudolphId, 1, 5);
            spawnOnTerrain(TILE_TUNDRA, ELEVATION_GROUND, reindeerId, 1, 1);
            spawnOnTerrain(TILE_SNOW, ELEVATION_GROUND, reindeerId, 1, 5);
            spawnOnTerrain(TILE_TUNDRA, ELEVATION_GROUND, frostyId, 1, 1);
            spawnOnTerrain(TILE_BUSH_LINGONBERRY, ELEVATION_GROUND, frostyId, 1, 1);
            spawnOnTerrain(TILE_SNOW, ELEVATION_GROUND, frostyId, 1, 5);
            CreatureSpawns.spawnOnMostTerrain(grinchId);
        }
        if (animals) {
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, wolfPackmasterId, 1, 2);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, warHoundId, 1, 1);
            spawnOnTerrain(TILE_SNOW, ELEVATION_GROUND, polarBearId, 1, 5);
            spawnOnTerrain(TILE_TUNDRA, ELEVATION_GROUND, polarBearId, 1, 1);
            spawnOnTerrain(TILE_TREE, ELEVATION_GROUND, pantherId, 3, 1);
            spawnOnTerrain(TILE_TREE, ELEVATION_GROUND, pandaId, 2, 2);
            spawnOnTerrain(TILE_TREE, ELEVATION_GROUND, ocelotId, 2, 3);
            spawnOnTerrain(TILE_CAVE, ELEVATION_GROUND, ocelotId, 2, 1);
            spawnOnTerrain(TILE_SAND, ELEVATION_GROUND, largeBoarId, 1, 1);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, largeBoarId, 1, 3);
            spawnOnTerrain(TILE_SNOW, ELEVATION_GROUND, iceCatId, 3, 5);
            spawnOnTerrain(TILE_TUNDRA, ELEVATION_GROUND, iceCatId, 1, 1);
            spawnOnTerrain(TILE_STEPPE, ELEVATION_GROUND, hyenaId, 2, 2);
            spawnOnTerrain(TILE_SAND, ELEVATION_GROUND, hyenaId, 2, 1);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, gorillaId, 2, 2);
            spawnOnTerrain(TILE_SAND, ELEVATION_GROUND, gorillaId, 2, 1);
            spawnOnTerrain(TILE_GRASS, ELEVATION_BEACH, fireCrabId, 1, 1);
            spawnOnTerrain(TILE_SAND, ELEVATION_BEACH, fireCrabId, 3, 3);
            spawnOnTerrain(TILE_TREE, ELEVATION_GROUND, blackWidowId, 1, 1);
            spawnOnTerrain(TILE_MOSS, ELEVATION_GROUND, blackWidowId, 1, 2);
            spawnOnTerrain(TILE_PEAT, ELEVATION_GROUND, blackWidowId, 2, 1);
            spawnOnTerrain(TILE_STEPPE, ELEVATION_GROUND, Zebra.templateId, 1, 1);
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, Zebra.templateId, 1, 2);
        }
        spawnOnTerrain(TILE_MARSH, ELEVATION_GROUND, willOWispId, 1, 5);
        spawnOnTerrain(TILE_MARSH, ELEVATION_BEACH, willOWispId, 1, 1);
        if (!disableTradeShips) {
            spawnOnTerrain(TILE_GRASS, ELEVATION_DEEP_WATER, npcMerchantShipId, 1, 1);
            spawnOnTerrain(TILE_DIRT, ELEVATION_DEEP_WATER, npcMerchantShipId, 1, 1);
        }
    }

    public static void spawnGolemlings(Creature creature) {
        if (creature.getTemplateId() > 0) {
            try {
                float x = creature.getPosX();
                float y = creature.getPosY();
                if (creature.getAttackers() != 0) {
                    RequiemLogging.logInfo(String.format("Spawning x2 %s.", creature.getNameWithoutPrefixes()));
                    CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(golemlingId);
                    Creature.doNew(template.getTemplateId(), false, x + 1, y, Server.rand.nextFloat() * 360f, creature.getLayer(), template.getName(), MiscConstants.SEX_MALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) 2);
                    Creature.doNew(template.getTemplateId(), false, x, y + 1, Server.rand.nextFloat() * 360f, creature.getLayer(), template.getName(), MiscConstants.SEX_MALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) 2);
                    Server.getInstance().broadCastAction(String.format("The %s splits into two!", creature.getTemplate().getName()), creature, 20);
                }
            } catch (Exception e) {
                RequiemLogging.logException("[Error] in spawnGolemlings in CreatureSpawns", e);
            }
        }
    }

    public static void spawnWolves(Creature creature) {
        if (creature.getTemplateId() > 0) {
            try {
                float x = creature.getPosX();
                float y = creature.getPosY();
                if (creature.getAttackers() != 0) {
                    RequiemLogging.logInfo(String.format("Spawning x2 %s.", creature.getNameWithoutPrefixes()));
                    CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(WOLF_BLACK_CID);
                    for (int i = 0; i < 8; i++) {
                        Creature.doNew(template.getTemplateId(), false, x, y, Server.rand.nextFloat() * 360f, creature.getLayer(), template.getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE, creature.getKingdomId(), BodyTemplate.TYPE_DOG, false, /*age*/(byte) 20);
                    }
                    Server.getInstance().broadCastAction("A wolf packmaster and its pack have just appeared near you! It would be wise to avoid it unless prepared to fight.", creature, 20);
                }
            } catch (Exception e) {
                RequiemLogging.logException("[Error] in spawnWolves in CreatureSpawns", e);
            }
        }
    }

    public static void spawnBloblings(Creature creature) {
        if (creature.getTemplateId() > 0) {
            try {
                float x = creature.getPosX();
                float y = creature.getPosY();
                if (creature.getAttackers() != 0) {
                    RequiemLogging.logInfo(String.format("Spawning x2 %s.", creature.getNameWithoutPrefixes()));
                    CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(bloblingId);
                    Creature.doNew(template.getTemplateId(), false, x, y + 1, Server.rand.nextFloat() * 360f, creature.getLayer(), creature.getTemplate().getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) 2);
                    Creature.doNew(template.getTemplateId(), false, x + 1, y, Server.rand.nextFloat() * 360f, creature.getLayer(), creature.getTemplate().getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) 2);
                    Server.getInstance().broadCastAction(String.format("The %s splits into two!", creature.getTemplate().getName()), creature, 20);
                }
            } catch (Exception e) {
                RequiemLogging.logException("[Error] in spawnBloblings in CreatureSpawns", e);
            }
        }
    }

    public static void spawnPrismaticBloblings(Creature creature) {
        if (creature.getTemplateId() > 0) {
            try {
                float x = creature.getPosX();
                float y = creature.getPosY();
                if (creature.getAttackers() != 0) {
                    RequiemLogging.logInfo(String.format("Spawning x2 %s.", creature.getNameWithoutPrefixes()));
                    CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(prismaticBloblingId);
                    Creature.doNew(template.getTemplateId(), false, x, y + 1, Server.rand.nextFloat() * 360f, creature.getLayer(), creature.getTemplate().getName(), MiscConstants.SEX_MALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) 2);
                    Creature.doNew(template.getTemplateId(), false, x + 1, y, Server.rand.nextFloat() * 360f, creature.getLayer(), creature.getTemplate().getName(), MiscConstants.SEX_MALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) 2);
                    Server.getInstance().broadCastAction(String.format("The %s splits into two!", creature.getTemplate().getName()), creature, 20);
                }
            } catch (Exception e) {
                RequiemLogging.logException("[Error] in spawnPrismaticBloblings in CreatureSpawns", e);
            }
        }
    }

    public static void spawnBuffaloSpirit(Creature creature) {
        if (creature.getTemplateId() > 0) {
            try {
                float x = creature.getPosX();
                float y = creature.getPosY();
                if (creature.getAttackers() != 0) {
                    RequiemLogging.logInfo(String.format("Spawning a %s.", creature.getNameWithoutPrefixes()));
                    CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(whiteBuffaloSpiritId);
                    Creature.doNew(template.getTemplateId(), false, x, y, Server.rand.nextFloat() * 360f, creature.getLayer(), template.getName(), MiscConstants.SEX_MALE, creature.getKingdomId(), BodyTemplate.TYPE_HORSE, false, /*age*/(byte) 25);
                    Server.getInstance().broadCastAction(String.format("The spirit of the %s is released into the world!", creature.getTemplate().getName()), creature, 20);
                    Server.getInstance().broadCastAlert(String.format("The %s is released from the soul of the %s, seeking vengeance for its physical form!", creature.getTemplate().getName(), creature.getTemplate().getName()));
                }
            } catch (Exception e) {
                RequiemLogging.logException("[Error] in spawnBuffaloSpirit in CreatureSpawns", e);
            }
        }
    }

    public static void spawnZombieHorde(Creature creature) {
        if (creature.getTemplateId() > 0) {
            try {
                float x = creature.getPosX();
                float y = creature.getPosY();
                RequiemLogging.logInfo("Spawning Zombie Horde!");
                CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(zombieWalkerId);
                CreatureTemplate hulk = CreatureTemplateFactory.getInstance().getTemplate(zombieHulkId);
                for (int i = 0; i < 8; i++) {
                    Creature.doNew(template.getTemplateId(), false, x, y, Server.rand.nextFloat() * 360f, creature.getLayer(), template.getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) 5);
                }
                for (int i = 0; i < 2; i++) {
                    Creature.doNew(hulk.getTemplateId(), false, x, y, Server.rand.nextFloat() * 360f, creature.getLayer(), hulk.getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) 5);
                }
                SoundPlayer.playSound("sound.combat.crocodile.aggressive", creature, 0f);
                Server.getInstance().broadCastAction("A horde of zombies has just formed near you, LOOK OUT!!!", creature, 15);
                Server.getInstance().broadCastAlert("A zombie horde has just formed somewhere in the world! It would be best to avoid it until prepared.", true, displayOnScreen);
            } catch (Exception e) {
                RequiemLogging.logException("[Error] in spawnZombieHorde in CreatureSpawns", e);
            }
        }
    }

    public static void spawnMimic(Creature creature) {
        if (creature.getTemplateId() > 0) {
            try {
                float x = creature.getPosX();
                float y = creature.getPosY();
                if (creature.getAttackers() != 0) {
                    RequiemLogging.logInfo(String.format("Spawning a %s.", creature.getNameWithoutPrefixes()));
                    CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(mimicId);
                    Creature.doNew(template.getTemplateId(), false, x, y, Server.rand.nextFloat() * 360f, creature.getLayer(), template.getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) (10 + Server.rand.nextInt(50)));
                    Server.getInstance().broadCastAction(String.format("The %s busts apart as you hit it and a Mimic jumps out ready to fight!", creature.getTemplate().getName()), creature, 20);
                }
            } catch (Exception e) {
                RequiemLogging.logException("[Error] in spawnMimic in CreatureSpawns", e);
            }
        }
    }

    public static void spawnHorsemanWar(Creature creature) {
        if (creature.getTemplateId() > 0) {
            try {
                float x = creature.getPosX();
                float y = creature.getPosY();
                if (creature.getAttackers() != 0) {
                    RequiemLogging.logInfo(String.format("Spawning a horseman of the apocalypse: %s.", creature.getNameWithoutPrefixes()));
                    CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(horsemanWarId);
                    Creature.doNew(template.getTemplateId(), true, x, y, Server.rand.nextFloat() * 360f, creature.getLayer(), template.getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) (10 + Server.rand.nextInt(50)));
                    SoundPlayer.playSound(SoundNames.HIT_HORSE_SND, creature, 0f);
                    Server.getInstance().broadCastAction(String.format("The horseman %s appears from the void ready to fight!", creature.getTemplate().getName()), creature, 20);
                }
            } catch (Exception e) {
                RequiemLogging.logException("[Error] in spawnHorsemanWar in CreatureSpawns", e);
            }
        }
    }

    public static void spawnHorsemanFamine(Creature creature) {
        if (creature.getTemplateId() > 0) {
            try {
                float x = creature.getPosX();
                float y = creature.getPosY();
                if (creature.getAttackers() != 0) {
                    RequiemLogging.logInfo(String.format("Spawning a horseman of the apocalypse: %s.", creature.getNameWithoutPrefixes()));
                    CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(horsemanFamineId);
                    Creature.doNew(template.getTemplateId(), true, x, y, Server.rand.nextFloat() * 360f, creature.getLayer(), template.getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) (10 + Server.rand.nextInt(50)));
                    SoundPlayer.playSound(SoundNames.HIT_HORSE_SND, creature, 0f);
                    Server.getInstance().broadCastAction(String.format("The horseman %s appears from the void ready to fight!", creature.getTemplate().getName()), creature, 20);
                }
            } catch (Exception e) {
                RequiemLogging.logException("[Error] in spawnHorsemanFamine in CreatureSpawns", e);
            }
        }
    }

    public static void spawnHorsemanDeath(Creature creature) {
        if (creature.getTemplateId() > 0) {
            try {
                float x = creature.getPosX();
                float y = creature.getPosY();
                if (creature.getAttackers() != 0) {
                    RequiemLogging.logInfo(String.format("Spawning a horseman of the apocalypse: %s.", creature.getNameWithoutPrefixes()));
                    CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(horsemanDeathId);
                    Creature.doNew(template.getTemplateId(), true, x, y, Server.rand.nextFloat() * 360f, creature.getLayer(), template.getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) (10 + Server.rand.nextInt(50)));
                    SoundPlayer.playSound(SoundNames.HIT_HORSE_SND, creature, 0f);
                    Server.getInstance().broadCastAction(String.format("The horseman %s appears from the void ready to fight!", creature.getTemplate().getName()), creature, 20);
                }
            } catch (Exception e) {
                RequiemLogging.logException("[Error] in spawnHorsemanDeath in CreatureSpawns", e);
            }
        }
    }

    /**
     * @param itemId Id of the item to be used in the template for the Black Knight
     * @return Item template to be used for spawning Black Knight items
     */
    private static Item blackKnightItemSpawn(int itemId) throws NoSuchTemplateException, FailedException {
        try {
            return ItemFactory.createItem(itemId, RandomUtils.getRandomQl(50, 99), ItemMaterials.MATERIAL_STEEL, MiscConstants.COMMON, "Black Knight");
        } catch (FailedException | NoSuchTemplateException e) {
            RequiemLogging.logException(String.format("Error creating Black Knight item for ID: %d", itemId), e);
        }
        return null;
    }

    /**
     * Spawn items and make the Black Knight equip them
     *
     * @param creature Black Knight
     */
    public static void blackKnightSpawn(Creature creature) {
        if (creature.getTemplateId() > 0) {
            try {
                Item twoHandSword = blackKnightItemSpawn(ItemList.swordTwoHander);
                Item greatHelm = blackKnightItemSpawn(ItemList.helmetGreat);
                Item steelChest = blackKnightItemSpawn(ItemList.plateJacket);
                Item steelLegs = blackKnightItemSpawn(ItemList.plateHose);
                Item steelLeftGlove = blackKnightItemSpawn(ItemList.plateGauntlet);
                Item steelRightGlove = blackKnightItemSpawn(ItemList.plateGauntlet);
                Item steelRightBoot = blackKnightItemSpawn(ItemList.plateBoot);
                Item steelLeftBoot = blackKnightItemSpawn(ItemList.plateBoot);
                Item steelRightSleeve = blackKnightItemSpawn(ItemList.plateSleeve);
                Item steelLeftSleeve = blackKnightItemSpawn(ItemList.plateSleeve);
                Item steelRightShoulder = blackKnightItemSpawn(ItemList.shoulderPads11);
                Item steelLeftShoulder = blackKnightItemSpawn(ItemList.shoulderPads11);

                creature.setKingdomId(MiscConstants.KINGDOM_HOTS, true, true, true);
                //SoundPlayer.playSound(dramatic_sound, creature, 0f);// play dramatic sound
                // Sword
                if (twoHandSword != null) {
                    if (Server.rand.nextInt(90) == 0) {
                        twoHandSword.setName("Ashbringer");
                        twoHandSword.enchant((byte) 12);
                        ItemTools.applyEnchant(twoHandSword, BUFF_NIMBLENESS, 75);
                        ItemTools.applyEnchant(twoHandSword, BUFF_FLAMING_AURA, 90);
                        twoHandSword.setColor(0);
                        twoHandSword.setColor2(0);
                    } else if (Server.rand.nextInt(75) == 0) {
                        twoHandSword.setName("Zulfiqar");
                        twoHandSword.enchant((byte) 11);
                        ItemTools.applyEnchant(twoHandSword, BUFF_NIMBLENESS, 80);
                        ItemTools.applyEnchant(twoHandSword, BUFF_FROSTBRAND, 100);
                        ItemTools.applyEnchant(twoHandSword, BUFF_CIRCLE_CUNNING, 90);
                        twoHandSword.setColor(0);
                        twoHandSword.setColor2(0);
                    } else if (Server.rand.nextInt(40) == 0) {
                        twoHandSword.setName("Apocalypse");
                        twoHandSword.enchant((byte) 9);
                        ItemTools.applyEnchant(twoHandSword, BUFF_NIMBLENESS, 104);
                        ItemTools.applyEnchant(twoHandSword, BUFF_ROTTING_TOUCH, 104);
                        ItemTools.applyEnchant(twoHandSword, BUFF_MINDSTEALER, 104);
                        ItemTools.applyEnchant(twoHandSword, BUFF_CIRCLE_CUNNING, 104);
                        twoHandSword.setColor(0);
                        twoHandSword.setColor2(0);
                        twoHandSword.setMaterial(MATERIAL_ADAMANTINE);
                    } else if (Server.rand.nextInt(10) == 0) {
                        twoHandSword.setName("World-Destroyer");
                        twoHandSword.enchant((byte) 10);
                        ItemTools.applyEnchant(twoHandSword, BUFF_BLOODTHIRST, 10000);
                        ItemTools.applyEnchant(twoHandSword, BUFF_ROTTING_TOUCH, 104);
                        ItemTools.applyEnchant(twoHandSword, BUFF_NIMBLENESS, 104);
                        ItemTools.applyEnchant(twoHandSword, BUFF_CIRCLE_CUNNING, 104);
                        ItemTools.applyEnchant(twoHandSword, BUFF_BLOODTHIRST, 104);
                        twoHandSword.setColor(0);
                        twoHandSword.setColor2(0);
                        twoHandSword.setMaterial(MATERIAL_GLIMMERSTEEL);
                    }
                }
                // Chest
                if (steelChest != null) {
                    ItemTools.applyEnchant(steelChest, BUFF_SHARED_PAIN, 104);
                    steelChest.setColor(0);
                    steelChest.setColor2(0);
                }
                // Legs
                if (steelLegs != null) {
                    ItemTools.applyEnchant(steelLegs, BUFF_SHARED_PAIN, 104);
                    steelLegs.setColor(0);
                    steelLegs.setColor2(0);
                }
                // Sleeves
                if (steelLeftSleeve != null && steelRightSleeve != null) {
                    ItemTools.applyEnchant(steelLeftSleeve, BUFF_SHARED_PAIN, 104);
                    ItemTools.applyEnchant(steelRightSleeve, BUFF_SHARED_PAIN, 104);
                    steelLeftSleeve.setColor(0);
                    steelLeftSleeve.setColor2(0);
                    steelRightSleeve.setColor(0);
                    steelRightSleeve.setColor2(0);
                }
                // Gloves
                if (steelRightGlove != null && steelLeftGlove != null) {
                    ItemTools.applyEnchant(steelRightGlove, BUFF_SHARED_PAIN, 104);
                    ItemTools.applyEnchant(steelLeftGlove, BUFF_SHARED_PAIN, 104);
                    steelLeftGlove.setColor(0);
                    steelLeftGlove.setColor2(0);
                    steelRightGlove.setColor(0);
                    steelRightGlove.setColor2(0);
                }
                // Helm
                if (greatHelm != null) {
                    ItemTools.applyEnchant(greatHelm, BUFF_SHARED_PAIN, 104);
                    greatHelm.setColor(0);
                    greatHelm.setColor2(0);
                }
                // Boots
                if (steelLeftBoot != null && steelRightBoot != null) {
                    ItemTools.applyEnchant(steelLeftBoot, BUFF_SHARED_PAIN, 104);
                    ItemTools.applyEnchant(steelRightBoot, BUFF_SHARED_PAIN, 104);
                    steelLeftBoot.setColor(0);
                    steelLeftBoot.setColor2(0);
                    steelRightBoot.setColor(0);
                    steelRightBoot.setColor2(0);
                }
                // Shoulders
                if (steelRightShoulder != null && steelLeftShoulder != null) {
                    steelRightShoulder.setColor(0);
                    steelRightShoulder.setColor2(0);
                    steelLeftShoulder.setColor(0);
                    steelLeftShoulder.setColor2(0);
                }
                // Add armor and sword to black knight
                if (twoHandSword != null) {
                    creature.getBody().getBodyPart(BodyTemplate.rightHand).insertItem(twoHandSword, true);
                }
                if (steelRightGlove != null) {
                    creature.getBody().getBodyPart(BodyTemplate.rightHand).insertItem(steelRightGlove, true);
                }
                if (steelLeftGlove != null) {
                    creature.getBody().getBodyPart(BodyTemplate.leftHand).insertItem(steelLeftGlove, true);
                }
                if (greatHelm != null) {
                    creature.getBody().getBodyPart(BodyTemplate.head).insertItem(greatHelm, true);
                }
                if (steelLeftSleeve != null) {
                    creature.getBody().getBodyPart(BodyTemplate.leftArm).insertItem(steelLeftSleeve, true);
                }
                if (steelRightSleeve != null) {
                    creature.getBody().getBodyPart(BodyTemplate.rightArm).insertItem(steelRightSleeve, true);
                }
                if (steelLegs != null) {
                    creature.getBody().getBodyPart(BodyTemplate.legs).insertItem(steelLegs, true);
                }
                if (steelLeftBoot != null) {
                    creature.getBody().getBodyPart(BodyTemplate.leftFoot).insertItem(steelLeftBoot, true);
                }
                if (steelRightBoot != null) {
                    creature.getBody().getBodyPart(BodyTemplate.rightFoot).insertItem(steelRightBoot, true);
                }
                if (steelChest != null) {
                    creature.getBody().getBodyPart(BodyTemplate.torso).insertItem(steelChest, true);
                }
                if (steelLeftShoulder != null) {
                    creature.getBody().getBodyPart(BodyTemplate.leftShoulder).insertItem(steelLeftShoulder, true);
                }
                if (steelRightShoulder != null) {
                    creature.getBody().getBodyPart(BodyTemplate.rightShoulder).insertItem(steelRightShoulder, true);
                }
                // Send message on spawn
                ChatHandler.systemMessage((Player) creature, CustomChannel.EVENTS, String.format("The %s has departed across the lands to seek out worthy challengers. Seek %s out to claim honor and glory by defeating %s in single combat.", creature.getNameWithoutPrefixes(), creature.getHimHerItString(), creature.getHimHerItString()), 255, 104, 0);
            } catch (NoSpaceException | FailedException | NoSuchTemplateException | IOException e) {
                RequiemLogging.logException("[Error] in blackKnightSpawn in CreatureSpawns", e);
            }
        }
    }

    public static void spawnOnTerrain(Tiles.Tile tile, byte elevation, int creatureId, int count, int chance) {
        if (creatureId > 0)
            try {
                new EncounterBuilder(tile.id, elevation).addCreatures(creatureId, count).build(chance);
                RequiemLogging.logInfo(String.format("Added new encounter for %d on tile %s", creatureId, tile));
            } catch (Exception e) {
                RequiemLogging.logException(String.format("Error spawning creature for ID: %d", creatureId), e);
            }
    }

    public static void spawnOnMostTerrain(int templateId) {
        try {
            spawnOnTerrain(TILE_GRASS, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_DIRT, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_SAND, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_STEPPE, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_TREE, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_TAR, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_TUNDRA, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_MOSS, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_PEAT, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_SNOW, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_ROCK, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_CLAY, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_BUSH, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_MYCELIUM, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_MYCELIUM_LAWN, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_MARSH, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_CLIFF, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_ENCHANTED_TREE, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_ENCHANTED_GRASS, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_DIRT_PACKED, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_ENCHANTED_BUSH, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_CAVE, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_FIELD, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_FIELD2, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_GRAVEL, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_STONE_SLABS, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_COBBLESTONE, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_MARBLE_BRICKS, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_MARBLE_SLABS, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_POTTERY_BRICKS, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_SANDSTONE_BRICKS, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_SANDSTONE_SLABS, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_SLATE_SLABS, ELEVATION_GROUND, templateId, 1, 1);
            spawnOnTerrain(TILE_SLATE_BRICKS, ELEVATION_GROUND, templateId, 1, 1);
        } catch (Exception e) {
            RequiemLogging.logException("[Error] in spawnOnMostTerrain in CreatureSpawns", e);
        }
    }

}
