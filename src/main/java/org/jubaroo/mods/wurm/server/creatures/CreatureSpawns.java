package org.jubaroo.mods.wurm.server.creatures;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.FailedException;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Server;
import com.wurmonline.server.bodys.BodyTemplate;
import com.wurmonline.server.creatures.*;
import com.wurmonline.server.items.*;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.sounds.SoundPlayer;
import com.wurmonline.server.zones.EncounterType;
import com.wurmonline.shared.constants.CreatureTypes;
import com.wurmonline.shared.constants.Enchants;
import com.wurmonline.shared.constants.ItemMaterials;
import com.wurmonline.shared.constants.SoundNames;
import org.gotti.wurmunlimited.modsupport.creatures.EncounterBuilder;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.communication.discord.ChatHandler;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.creatures.traitedCreatures.Zebra;
import org.jubaroo.mods.wurm.server.misc.database.holidays.Holidays;
import org.jubaroo.mods.wurm.server.server.Constants;
import org.jubaroo.mods.wurm.server.tools.ItemTools;

import java.io.IOException;

public class CreatureSpawns {

    private void spawnAttackers(Creature performer, int template, int num, boolean reborn) throws
            NoSuchCreatureTemplateException {
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
        if (Constants.uniques) {
            CreatureSpawns.spawnOnMostTerrain(CustomCreatures.spiritStagId);
            CreatureSpawns.spawnOnMostTerrain(CustomCreatures.kongId);
            CreatureSpawns.spawnOnMostTerrain(CustomCreatures.facebreykerId);
            CreatureSpawns.spawnOnMostTerrain(CustomCreatures.whiteBuffaloId);
        }
        if (Constants.undead) {
            CreatureSpawns.spawnOnMostTerrain(CustomCreatures.zombieWalkerId);
            CreatureSpawns.spawnOnMostTerrain(CustomCreatures.zombieLeaderId);
            new EncounterBuilder(Tiles.Tile.TILE_SNOW.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.spiritTrollId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_TREE.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.spiritTrollId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.reanimatedSkeletonId, 1).build(1);
        }
        if (Constants.treasureGoblin) {
            CreatureSpawns.spawnOnMostTerrain(CustomCreatures.treasureGoblinId);
            CreatureSpawns.spawnOnMostTerrain(CustomCreatures.treasureGoblinMenageristGoblinId);
            //CreatureSpawns.spawnOnMostTerrain(CustomCreatures.treasureGoblinRainbowGoblinId);
            //CreatureSpawns.spawnOnMostTerrain(CustomCreatures.treasureGoblinOdiousCollectorId);
            //CreatureSpawns.spawnOnMostTerrain(CustomCreatures.treasureGoblinMalevolentTormentorId);
            //CreatureSpawns.spawnOnMostTerrain(CustomCreatures.treasureGoblinGemHoarderId);
            //CreatureSpawns.spawnOnMostTerrain(CustomCreatures.treasureGoblinBloodThiefId);
        }
        if (Constants.monsters) {
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.snakeVineId, 3).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_BUSH_THORN.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.snakeVineId, 3).build(3);
            new EncounterBuilder(Tiles.Tile.TILE_TREE.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.prismaticBlobId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_MOSS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.prismaticBlobId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_PEAT.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.prismaticBlobId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.prismaticBlobId, 1).build(2);
            new EncounterBuilder(Tiles.Tile.TILE_TREE.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.mimicChestId, 2).build(2);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.mimicChestId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_ROCK.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.golemId, 2).build(2);
            new EncounterBuilder(Tiles.Tile.TILE_MARSH.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.golemId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_CLAY.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.golemId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.giantId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_TREE.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.giantId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_TREE.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.forestSpiderId, 3).build(2);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.fireSpiderId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_SAND.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.fireSpiderId, 1).build(2);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.fireGiantId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_SAND.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.fireGiantId, 1).build(3);
            new EncounterBuilder(Tiles.Tile.TILE_ROCK.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.cyclopsId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_MARSH.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.cyclopsId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_CLAY.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.cyclopsId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_TREE.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.blobId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_MOSS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.blobId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_PEAT.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.blobId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_TAR.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.blobId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.avengerId, 1).build(1);
            // register vanilla creature templates that normally don't spawn
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CreatureTemplateIds.DEATHCRAWLER_MINION_CID, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_SAND.id, EncounterType.ELEVATION_GROUND).addCreatures(CreatureTemplateIds.GOBLIN_CID, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_SAND.id, EncounterType.ELEVATION_GROUND).addCreatures(CreatureTemplateIds.LAVA_CREATURE_CID, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CreatureTemplateIds.WORG_CID, 2).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_STEPPE.id, EncounterType.ELEVATION_GROUND).addCreatures(CreatureTemplateIds.WORG_CID, 2).build(2);
            new EncounterBuilder(Tiles.Tile.TILE_SAND.id, EncounterType.ELEVATION_GROUND).addCreatures(CreatureTemplateIds.DEMON_SOL_CID, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_MYCELIUM.id, EncounterType.ELEVATION_GROUND).addCreatures(CreatureTemplateIds.DEMON_SOL_CID, 1).build(3);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CreatureTemplateIds.SPAWN_UTTACHA_CID, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_DIRT.id, EncounterType.ELEVATION_WATER).addCreatures(CreatureTemplateIds.SEA_SERPENT_CID, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_DIRT.id, EncounterType.ELEVATION_DEEP_WATER).addCreatures(CreatureTemplateIds.SEA_SERPENT_CID, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_WATER).addCreatures(CreatureTemplateIds.SEA_SERPENT_CID, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_DEEP_WATER).addCreatures(CreatureTemplateIds.SEA_SERPENT_CID, 1).build(1);
            //new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CreatureTemplateIds.DRAKESPIRIT_CID, 1).build(1);
            //new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CreatureTemplateIds.EAGLESPIRIT_CID, 1).build(1);
        }
        if (Constants.humans) {
            CreatureSpawns.spawnOnMostTerrain(CustomCreatures.tombRaiderId);
            new EncounterBuilder(Tiles.Tile.TILE_COBBLESTONE.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.banditId, 1).build(3);
            new EncounterBuilder(Tiles.Tile.TILE_COBBLESTONE_ROUGH.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.banditId, 1).build(3);
            new EncounterBuilder(Tiles.Tile.TILE_COBBLESTONE_ROUND.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.banditId, 1).build(3);
        }
        if (Holidays.isRequiemHalloween() && !Constants.disableHolidayCreatures) {
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.ominousTreeId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_TREE.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.ominousTreeId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_DIRT.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.ominousTreeId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.scaryPumpkinId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_TREE.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.scaryPumpkinId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_DIRT.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.scaryPumpkinId, 1).build(1);
        }
        if (Constants.wyverns) {
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.whiteWyvernId, 1).build(4);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.redWyvernId, 1).build(4);
            new EncounterBuilder(Tiles.Tile.TILE_TREE.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.greenWyvernId, 1).build(4);
            new EncounterBuilder(Tiles.Tile.TILE_MARSH.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.blueWyvernId, 1).build(3);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.blueWyvernId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.blackWyvernId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_PEAT.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.blackWyvernId, 1).build(4);
        }
        if (Constants.customMounts) {
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.rainbowZebraId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.prismaticHellHorseId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.hornedPonyId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.chargerId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.whiteRainbowUnicornId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.purpleRainbowUnicornId, 1).build(1);
        }
        if (Holidays.isRequiemChristmas() && !Constants.disableHolidayCreatures) {
            new EncounterBuilder(Tiles.Tile.TILE_TUNDRA.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.snowmanId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_BUSH_LINGONBERRY.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.snowmanId, 1).build(3);
            new EncounterBuilder(Tiles.Tile.TILE_SNOW.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.snowmanId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.sinisterSantaId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_SNOW.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.sinisterSantaId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.rudolphId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_SNOW.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.rudolphId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_TUNDRA.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.reindeerId, 2).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_SNOW.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.reindeerId, 2).build(2);
            CreatureSpawns.spawnOnMostTerrain(CustomCreatures.grinchId);
            new EncounterBuilder(Tiles.Tile.TILE_TUNDRA.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.frostyId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_BUSH_LINGONBERRY.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.frostyId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_SNOW.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.frostyId, 1).build(5);
        }
        if (Constants.animals) {
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.wolfPackmasterId, 1).build(2);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.warHoundId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_SNOW.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.polarBearId, 1).build(2);
            new EncounterBuilder(Tiles.Tile.TILE_TUNDRA.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.polarBearId, 1).build(2);
            new EncounterBuilder(Tiles.Tile.TILE_TREE.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.pantherId, 3).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_TREE.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.pandaId, 2).build(2);
            new EncounterBuilder(Tiles.Tile.TILE_TREE.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.ocelotId, 2).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_CAVE.id, EncounterType.ELEVATION_CAVES).addCreatures(CustomCreatures.ocelotId, 2).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_SAND.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.largeBoarId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.largeBoarId, 1).build(3);
            new EncounterBuilder(Tiles.Tile.TILE_SNOW.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.iceCatId, 2).build(3);
            new EncounterBuilder(Tiles.Tile.TILE_TUNDRA.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.iceCatId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.hyenaId, 2).build(2);
            new EncounterBuilder(Tiles.Tile.TILE_SAND.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.hyenaId, 2).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.gorillaId, 2).build(2);
            new EncounterBuilder(Tiles.Tile.TILE_SAND.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.gorillaId, 2).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.fireCrabId, 3).build(2);
            new EncounterBuilder(Tiles.Tile.TILE_SAND.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.fireCrabId, 3).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_TREE.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.blackWidowId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_MOSS.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.blackWidowId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_PEAT.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.blackWidowId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_STEPPE.id, EncounterType.ELEVATION_GROUND).addCreatures(Zebra.templateId, 1).build(1);
        }
        new EncounterBuilder(Tiles.Tile.TILE_MARSH.id, EncounterType.ELEVATION_GROUND).addCreatures(CustomCreatures.willOWispId, 1).build(1);
        if (!Constants.disableTradeShips) {
            new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_DEEP_WATER).addCreatures(CustomCreatures.npcMerchantShipId, 1).build(1);
            new EncounterBuilder(Tiles.Tile.TILE_DIRT.id, EncounterType.ELEVATION_DEEP_WATER).addCreatures(CustomCreatures.npcMerchantShipId, 1).build(1);
        }
    }

    private static void spawnGolemlings(Creature creature) {
        try {
            float x = creature.getPosX();
            float y = creature.getPosY();
            if (creature.getAttackers() != 0) {
                RequiemLogging.logInfo(String.format("Spawning x2 %s.", creature.getNameWithoutPrefixes()));
                CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(CustomCreatures.golemlingId);
                Creature.doNew(template.getTemplateId(), true, x + 1, y, Server.rand.nextFloat() * 360f, creature.getLayer(), template.getName(), MiscConstants.SEX_MALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) 2);
                Creature.doNew(template.getTemplateId(), true, x, y + 1, Server.rand.nextFloat() * 360f, creature.getLayer(), template.getName(), MiscConstants.SEX_MALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) 2);
                Server.getInstance().broadCastAction(String.format("The %s splits into two!", creature.getTemplate().getName()), creature, 20);
            }
        } catch (Exception e) {
            e.printStackTrace();
            RequiemLogging.logWarning("Error in Spawning Golemlings.");
        }
    }

    public static void spawnWolves(Creature creature) {
        try {
            float x = creature.getPosX();
            float y = creature.getPosY();
            if (creature.getAttackers() != 0) {
                RequiemLogging.logInfo(String.format("Spawning x2 %s.", creature.getNameWithoutPrefixes()));
                CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(CreatureTemplateIds.WOLF_BLACK_CID);
                for (int i = 0; i < 8; i++) {
                    Creature.doNew(template.getTemplateId(), true, x, y, Server.rand.nextFloat() * 360f, creature.getLayer(), template.getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE, creature.getKingdomId(), BodyTemplate.TYPE_DOG, false, /*age*/(byte) 20);
                }
                Server.getInstance().broadCastAction("A wolf packmaster and its pack have just appeared near you! It would be wise to avoid it unless prepared to fight.", creature, 20);
            }
        } catch (Exception e) {
            e.printStackTrace();
            RequiemLogging.logWarning("Error in Spawning Wolves.");
        }
    }

    private static void spawnBloblings(Creature creature) {
        try {
            float x = creature.getPosX();
            float y = creature.getPosY();
            if (creature.getAttackers() != 0) {
                RequiemLogging.logInfo(String.format("Spawning x2 %s.", creature.getNameWithoutPrefixes()));
                CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(CustomCreatures.bloblingId);
                Creature.doNew(template.getTemplateId(), true, x, y + 1, Server.rand.nextFloat() * 360f, creature.getLayer(), creature.getTemplate().getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) 2);
                Creature.doNew(template.getTemplateId(), true, x + 1, y, Server.rand.nextFloat() * 360f, creature.getLayer(), creature.getTemplate().getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) 2);
                Server.getInstance().broadCastAction(String.format("The %s splits into two!", creature.getTemplate().getName()), creature, 20);
            }
        } catch (Exception e) {
            e.printStackTrace();
            RequiemLogging.logWarning("Error in Spawning Bloblings.");
        }
    }

    private static void spawnPrismaticBloblings(Creature creature) {
        try {
            float x = creature.getPosX();
            float y = creature.getPosY();
            if (creature.getAttackers() != 0) {
                RequiemLogging.logInfo(String.format("Spawning x2 %s.", creature.getNameWithoutPrefixes()));
                CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(CustomCreatures.prismaticBloblingId);
                Creature.doNew(template.getTemplateId(), true, x, y + 1, Server.rand.nextFloat() * 360f, creature.getLayer(), creature.getTemplate().getName(), MiscConstants.SEX_MALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) 2);
                Creature.doNew(template.getTemplateId(), true, x + 1, y, Server.rand.nextFloat() * 360f, creature.getLayer(), creature.getTemplate().getName(), MiscConstants.SEX_MALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) 2);
                Server.getInstance().broadCastAction(String.format("The %s splits into two!", creature.getTemplate().getName()), creature, 20);
            }
        } catch (Exception e) {
            e.printStackTrace();
            RequiemLogging.logWarning("Error in Spawning Prismatic Bloblings.");
        }
    }

    private static void spawnBuffaloSpirit(Creature creature) {
        try {
            float x = creature.getPosX();
            float y = creature.getPosY();
            if (creature.getAttackers() != 0) {
                RequiemLogging.logInfo(String.format("Spawning a %s.", creature.getNameWithoutPrefixes()));
                CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(CustomCreatures.whiteBuffaloSpiritId);
                Creature.doNew(template.getTemplateId(), true, x, y, Server.rand.nextFloat() * 360f, creature.getLayer(), template.getName(), MiscConstants.SEX_MALE, creature.getKingdomId(), BodyTemplate.TYPE_HORSE, false, /*age*/(byte) 25);
                Server.getInstance().broadCastAction(String.format("The spirit of the %s is released into the world!", creature.getTemplate().getName()), creature, 20);
                Server.getInstance().broadCastAlert(String.format("The %s is released from the soul of the %s, seeking vengeance for its physical form!", creature.getTemplate().getName(), creature.getTemplate().getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            RequiemLogging.logWarning("Error in Spawning Buffalo Spirit.");
        }
    }

    public static void spawnZombieHorde(Creature creature) {
        try {
            float x = creature.getPosX();
            float y = creature.getPosY();
            RequiemLogging.logInfo("Spawning Zombie Horde!");
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(CustomCreatures.zombieWalkerId);
            CreatureTemplate hulk = CreatureTemplateFactory.getInstance().getTemplate(CustomCreatures.zombieHulkId);
            for (int i = 0; i < 8; i++) {
                Creature.doNew(template.getTemplateId(), true, x, y, Server.rand.nextFloat() * 360f, creature.getLayer(), template.getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) 5);
            }
            for (int i = 0; i < 2; i++) {
                Creature.doNew(hulk.getTemplateId(), true, x, y, Server.rand.nextFloat() * 360f, creature.getLayer(), hulk.getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) 5);
            }
            SoundPlayer.playSound("sound.combat.crocodile.aggressive", creature, 0f);
            Server.getInstance().broadCastAction("A horde of zombies has just formed near you, LOOK OUT!!!", creature, 15);
            Server.getInstance().broadCastAlert("A zombie horde has just formed somewhere in the world! It would be best to avoid it until prepared.", true, Constants.displayOnScreen);
        } catch (Exception e) {
            e.printStackTrace();
            RequiemLogging.logWarning("Error in Spawning Zombie Horde!");
        }
    }

    private static void spawnMimic(Creature creature) {
        try {
            float x = creature.getPosX();
            float y = creature.getPosY();
            if (creature.getAttackers() != 0) {
                RequiemLogging.logInfo(String.format("Spawning a %s.", creature.getNameWithoutPrefixes()));
                CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(CustomCreatures.mimicId);
                Creature.doNew(template.getTemplateId(), true, x, y, Server.rand.nextFloat() * 360f, creature.getLayer(), template.getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) (10 + Server.rand.nextInt(50)));
                Server.getInstance().broadCastAction(String.format("The %s busts apart as you hit it and a Mimic jumps out ready to fight!", creature.getTemplate().getName()), creature, 20);
            }
        } catch (Exception e) {
            e.printStackTrace();
            RequiemLogging.logWarning("Error in Spawning Mimic.");
        }
    }

    private static void spawnHorsemanWar(Creature creature) {
        try {
            float x = creature.getPosX();
            float y = creature.getPosY();
            if (creature.getAttackers() != 0) {
                RequiemLogging.logInfo(String.format("Spawning a horseman of the apocalypse: %s.", creature.getNameWithoutPrefixes()));
                CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(CustomCreatures.horsemanWarId);
                Creature.doNew(template.getTemplateId(), true, x, y, Server.rand.nextFloat() * 360f, creature.getLayer(), template.getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) (10 + Server.rand.nextInt(50)));
                SoundPlayer.playSound(SoundNames.HIT_HORSE_SND, creature, 0f);
                Server.getInstance().broadCastAction(String.format("The horseman %s appears from the void ready to fight!", creature.getTemplate().getName()), creature, 20);
            }
        } catch (Exception e) {
            e.printStackTrace();
            RequiemLogging.logWarning("Error in Spawning Horseman Conquest.");
        }
    }

    private static void spawnHorsemanFamine(Creature creature) {
        try {
            float x = creature.getPosX();
            float y = creature.getPosY();
            if (creature.getAttackers() != 0) {
                RequiemLogging.logInfo(String.format("Spawning a horseman of the apocalypse: %s.", creature.getNameWithoutPrefixes()));
                CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(CustomCreatures.horsemanFamineId);
                Creature.doNew(template.getTemplateId(), true, x, y, Server.rand.nextFloat() * 360f, creature.getLayer(), template.getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) (10 + Server.rand.nextInt(50)));
                SoundPlayer.playSound(SoundNames.HIT_HORSE_SND, creature, 0f);
                Server.getInstance().broadCastAction(String.format("The horseman %s appears from the void ready to fight!", creature.getTemplate().getName()), creature, 20);
            }
        } catch (Exception e) {
            e.printStackTrace();
            RequiemLogging.logWarning("Error in Spawning Horseman Conquest.");
        }
    }

    private static void spawnHorsemanDeath(Creature creature) {
        try {
            float x = creature.getPosX();
            float y = creature.getPosY();
            if (creature.getAttackers() != 0) {
                RequiemLogging.logInfo(String.format("Spawning a horseman of the apocalypse: %s.", creature.getNameWithoutPrefixes()));
                CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(CustomCreatures.horsemanDeathId);
                Creature.doNew(template.getTemplateId(), true, x, y, Server.rand.nextFloat() * 360f, creature.getLayer(), template.getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE, creature.getKingdomId(), BodyTemplate.TYPE_HUMAN, false, /*age*/(byte) (10 + Server.rand.nextInt(50)));
                SoundPlayer.playSound(SoundNames.HIT_HORSE_SND, creature, 0f);
                Server.getInstance().broadCastAction(String.format("The horseman %s appears from the void ready to fight!", creature.getTemplate().getName()), creature, 20);
            }
        } catch (Exception e) {
            e.printStackTrace();
            RequiemLogging.logWarning("Error in Spawning Horseman Conquest.");
        }
    }

    /**
     * Spawn a creature when the original creature dies
     *
     * @param creature The creature used to determine the creature(s) spawned when it dies
     */
    static void creatureSpawnOnDeath(Creature creature) {
        int templateId = creature.getTemplate().getTemplateId();

        if (templateId == CustomCreatures.golemId) {
            // Spawn 2 Golemlings when a golem dies
            CreatureSpawns.spawnGolemlings(creature);
        }
        if (templateId == CustomCreatures.blobId) {
            // Spawn 2 Bloblings when a blob dies
            CreatureSpawns.spawnBloblings(creature);
        }
        if (templateId == CustomCreatures.prismaticBlobId) {
            // Spawn 2 Prismatic Bloblings when a prismatic blob dies
            CreatureSpawns.spawnPrismaticBloblings(creature);
        }
        if (templateId == CustomCreatures.whiteBuffaloId) {
            // Spawn Buffalo Spirit when a white buffalo dies
            CreatureSpawns.spawnBuffaloSpirit(creature);
        }
        if (templateId == CustomCreatures.mimicChestId) {
            // Spawn 1 Mimic when a treasure chest dies
            CreatureSpawns.spawnMimic(creature);
        }
        if (templateId == CustomCreatures.horsemanConquestId) {
            // Spawn the horseman war when conquest dies
            CreatureSpawns.spawnHorsemanWar(creature);
        }
        if (templateId == CustomCreatures.horsemanWarId) {
            // Spawn the horseman famine when war dies
            CreatureSpawns.spawnHorsemanFamine(creature);
        }
        if (templateId == CustomCreatures.horsemanFamineId) {
            // Spawn the horseman death when famine dies
            CreatureSpawns.spawnHorsemanDeath(creature);
        }
    }

    /**
     * @param itemId Id of the item to be used in the template for the Black Knight
     * @return Item template to be used for spawning Black Knight items
     */
    private static Item blackKnightItemSpawn(int itemId) throws NoSuchTemplateException, FailedException {
        return ItemFactory.createItem(itemId, 50f + (Server.rand.nextFloat() * 30f), ItemMaterials.MATERIAL_STEEL, MiscConstants.COMMON, "Black Knight");
    }

    /**
     * Spawn items and make the Black Knight equip them
     *
     * @param creature Black Knight
     */
    public static void blackKnightSpawn(Creature creature) {
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

            creature.setSex(MiscConstants.SEX_MALE, true);
            creature.setAlignment(-100f);
            creature.setKingdomId(MiscConstants.KINGDOM_HOTS, true, true, true);
            //SoundPlayer.playSound(dramatic_sound, creature, 0f);// play dramatic sound
            // Sword
            int roll = Server.rand.nextInt(100);
            if (roll >= 0) {
                twoHandSword.setName("Ashbringer");
                twoHandSword.enchant((byte) 12);
                ItemTools.applyEnchant(twoHandSword, Enchants.BUFF_NIMBLENESS, 75);
                ItemTools.applyEnchant(twoHandSword, Enchants.BUFF_FLAMING_AURA, 90);
                twoHandSword.setColor(0);
                twoHandSword.setColor2(0);
            }
            if (roll >= 40) {
                twoHandSword.setName("Zulfiqar");
                twoHandSword.enchant((byte) 11);
                ItemTools.applyEnchant(twoHandSword, Enchants.BUFF_NIMBLENESS, 80);
                ItemTools.applyEnchant(twoHandSword, Enchants.BUFF_FROSTBRAND, 100);
                ItemTools.applyEnchant(twoHandSword, Enchants.BUFF_CIRCLE_CUNNING, 90);
                twoHandSword.setColor(0);
                twoHandSword.setColor2(0);
            }
            if (roll >= 75) {
                twoHandSword.setName("Apocalypse");
                twoHandSword.enchant((byte) 9);
                ItemTools.applyEnchant(twoHandSword, Enchants.BUFF_NIMBLENESS, 104);
                ItemTools.applyEnchant(twoHandSword, Enchants.BUFF_ROTTING_TOUCH, 104);
                ItemTools.applyEnchant(twoHandSword, Enchants.BUFF_MINDSTEALER, 104);
                ItemTools.applyEnchant(twoHandSword, Enchants.BUFF_CIRCLE_CUNNING, 104);
                twoHandSword.setColor(0);
                twoHandSword.setColor2(0);
                twoHandSword.setMaterial(ItemMaterials.MATERIAL_ADAMANTINE);
            }
            if (roll >= 90) {
                twoHandSword.setName("World-Destroyer");
                twoHandSword.enchant((byte) 10);
                ItemTools.applyEnchant(twoHandSword, Enchants.BUFF_BLOODTHIRST, 10000);
                ItemTools.applyEnchant(twoHandSword, Enchants.BUFF_ROTTING_TOUCH, 104);
                ItemTools.applyEnchant(twoHandSword, Enchants.BUFF_NIMBLENESS, 104);
                ItemTools.applyEnchant(twoHandSword, Enchants.BUFF_CIRCLE_CUNNING, 104);
                ItemTools.applyEnchant(twoHandSword, Enchants.BUFF_BLOODTHIRST, 104);
                twoHandSword.setColor(0);
                twoHandSword.setColor2(0);
                twoHandSword.setMaterial(ItemMaterials.MATERIAL_GLIMMERSTEEL);
            }
            // Chest
            ItemTools.applyEnchant(steelChest, Enchants.BUFF_SHARED_PAIN, 104);
            steelChest.setColor(0);
            steelChest.setColor2(0);
            // Legs
            ItemTools.applyEnchant(steelLegs, Enchants.BUFF_SHARED_PAIN, 104);
            steelLegs.setColor(0);
            steelLegs.setColor2(0);
            // Sleeves
            ItemTools.applyEnchant(steelLeftSleeve, Enchants.BUFF_SHARED_PAIN, 104);
            ItemTools.applyEnchant(steelRightSleeve, Enchants.BUFF_SHARED_PAIN, 104);
            steelLeftSleeve.setColor(0);
            steelLeftSleeve.setColor2(0);
            steelRightSleeve.setColor(0);
            steelRightSleeve.setColor2(0);
            // Gloves
            ItemTools.applyEnchant(steelRightGlove, Enchants.BUFF_SHARED_PAIN, 104);
            ItemTools.applyEnchant(steelLeftGlove, Enchants.BUFF_SHARED_PAIN, 104);
            steelLeftGlove.setColor(0);
            steelLeftGlove.setColor2(0);
            steelRightGlove.setColor(0);
            steelRightGlove.setColor2(0);
            // Helm
            ItemTools.applyEnchant(greatHelm, Enchants.BUFF_SHARED_PAIN, 104);
            greatHelm.setColor(0);
            greatHelm.setColor2(0);
            // Boots
            ItemTools.applyEnchant(steelLeftBoot, Enchants.BUFF_SHARED_PAIN, 104);
            ItemTools.applyEnchant(steelRightBoot, Enchants.BUFF_SHARED_PAIN, 104);
            steelLeftBoot.setColor(0);
            steelLeftBoot.setColor2(0);
            steelRightBoot.setColor(0);
            steelRightBoot.setColor2(0);
            // Shoulders
            steelRightShoulder.setColor(0);
            steelRightShoulder.setColor2(0);
            steelLeftShoulder.setColor(0);
            steelLeftShoulder.setColor2(0);
            // Add armor and sword to black knight
            creature.getBody().getBodyPart(BodyTemplate.rightHand).insertItem(twoHandSword, true);
            creature.getBody().getBodyPart(BodyTemplate.rightHand).insertItem(steelRightGlove, true);
            creature.getBody().getBodyPart(BodyTemplate.leftHand).insertItem(steelLeftGlove, true);
            creature.getBody().getBodyPart(BodyTemplate.head).insertItem(greatHelm, true);
            creature.getBody().getBodyPart(BodyTemplate.leftArm).insertItem(steelLeftSleeve, true);
            creature.getBody().getBodyPart(BodyTemplate.rightArm).insertItem(steelRightSleeve, true);
            creature.getBody().getBodyPart(BodyTemplate.legs).insertItem(steelLegs, true);
            creature.getBody().getBodyPart(BodyTemplate.leftFoot).insertItem(steelLeftBoot, true);
            creature.getBody().getBodyPart(BodyTemplate.rightFoot).insertItem(steelRightBoot, true);
            creature.getBody().getBodyPart(BodyTemplate.torso).insertItem(steelChest, true);
            creature.getBody().getBodyPart(BodyTemplate.leftShoulder).insertItem(steelLeftShoulder, true);
            creature.getBody().getBodyPart(BodyTemplate.rightShoulder).insertItem(steelRightShoulder, true);
            // Send message on spawn
            ChatHandler.systemMessage((Player) creature, CustomChannel.EVENTS, String.format("The %s has departed across the lands to seek out worthy challengers. Seek %s out to claim honor and glory by defeating %s in single combat.", creature.getNameWithoutPrefixes(), creature.getHimHerItString(), creature.getHimHerItString()), 255, 104, 0);
        } catch (NoSpaceException | FailedException | NoSuchTemplateException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void spawnOnMostTerrain(int templateId) {
        new EncounterBuilder(Tiles.Tile.TILE_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_DIRT.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_SAND.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_STEPPE.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_TREE.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_TAR.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_TUNDRA.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_MOSS.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_PEAT.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_SNOW.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_ROCK.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_CLAY.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_BUSH.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_MYCELIUM.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_MYCELIUM_LAWN.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_MARSH.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_CLIFF.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_ENCHANTED_TREE.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_ENCHANTED_GRASS.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_DIRT_PACKED.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_ENCHANTED_BUSH.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_CAVE.id, EncounterType.ELEVATION_CAVES).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_FIELD.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_FIELD2.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_GRAVEL.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_STONE_SLABS.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_COBBLESTONE.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_MARBLE_BRICKS.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_MARBLE_SLABS.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_POTTERY_BRICKS.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_SANDSTONE_BRICKS.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_SANDSTONE_SLABS.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_SLATE_SLABS.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
        new EncounterBuilder(Tiles.Tile.TILE_SLATE_BRICKS.id, EncounterType.ELEVATION_GROUND).addCreatures(templateId, 1).build(1);
    }

}
