package org.jubaroo.mods.wurm.server.server;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.Players;
import com.wurmonline.server.Server;
import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.structures.Fence;
import com.wurmonline.server.villages.Villages;
import com.wurmonline.server.zones.FocusZone;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zones;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.classhooks.InvocationHandlerFactory;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.actions.terrain.SmoothTerrainAction;
import org.jubaroo.mods.wurm.server.creatures.RareSpawns;
import org.jubaroo.mods.wurm.server.creatures.Titans;
import org.jubaroo.mods.wurm.server.items.behaviours.AthanorMechanismBehaviour;
import org.jubaroo.mods.wurm.server.items.behaviours.SupplyDepotBehaviour;
import org.jubaroo.mods.wurm.server.misc.Misc;
import org.jubaroo.mods.wurm.server.server.constants.EffectsConstants;
import org.jubaroo.mods.wurm.server.server.constants.PollingConstants;
import org.jubaroo.mods.wurm.server.tools.CreatureTools;
import org.jubaroo.mods.wurm.server.tools.EffectsTools;
import org.jubaroo.mods.wurm.server.tools.RequiemTools;
import org.jubaroo.mods.wurm.server.utils.MissionCreator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.jubaroo.mods.wurm.server.server.constants.ItemConstants.terrainSmoothing;
import static org.jubaroo.mods.wurm.server.server.constants.ToggleConstants.enableAthanorMechanism;

public class OnServerPoll {

    public static void init() {

        RequiemLogging.logInfo("========= Initializing OnServerPoll.init =========");
        HookManager.getInstance().registerHook("com.wurmonline.server.Server", "run", "()V", new InvocationHandlerFactory() {
            @Override
            public InvocationHandler createInvocationHandler() {
                return new InvocationHandler() {
                    @Override
                    public Object invoke(Object object, Method method, Object[] args) throws Throwable {
                        long now = System.currentTimeMillis();
                        //if (!RequiemTools.isPrivateTestServer()) {
                        if (now - PollingConstants.lastPolledMyceliumChange > PollingConstants.delayMyceliumChange) {
                            RequiemLogging.debug(String.format("OnServerPoll change to mycelium at %d, time since last poll : %d minute(s)", PollingConstants.lastPolledMyceliumChange, (now - PollingConstants.lastPolledMyceliumChange) / TimeConstants.MINUTE_MILLIS));
                            PollingConstants.lastPolledMyceliumChange = now;
                            //changeToMyceliumZone();
                            changeToMyceliumDeed();
                        }
                        //if (now - PollingConstants.lastPolledHolidayMessage > PollingConstants.delayHolidayMessage) {
                        //    RequiemLogging.debug(String.format("OnServerPoll holiday message at %d, time since last poll : %d minute(s)", PollingConstants.lastPolledHolidayMessage, (now - PollingConstants.lastPolledHolidayMessage) / TimeConstants.MINUTE_MILLIS));
                        //    PollingConstants.lastPolledHolidayMessage = now;
                        //    Holidays.holidayDiscordMessage();
                        //}
                        //if (now - PollingConstants.lastPolledCullHotA > PollingConstants.delayCullHotA) {
                        //    RequiemLogging.debug(String.format("OnServerPoll cull HotA at %d, time since last poll : %d minute(s)", PollingConstants.lastPolledCullHotA, (now - PollingConstants.lastPolledCullHotA) / TimeConstants.MINUTE_MILLIS));
                        //    PollingConstants.lastPolledCullHotA = now;
                        //    cullCreaturesHotA();
                        //}
                        if (now - PollingConstants.lastPolledTradeTents > PollingConstants.delayTradeTents) {
                            RequiemLogging.debug(String.format("OnServerPoll trade tents at %d, time since last poll : %d minute(s)", PollingConstants.lastPolledTradeTents, (now - PollingConstants.lastPolledTradeTents) / TimeConstants.MINUTE_MILLIS));
                            PollingConstants.lastPolledTradeTents = now;
                            Misc.pollTradeTents();
                        }
                        if (now - PollingConstants.lastPolledResourcePoints > PollingConstants.delayResourcePoints) {
                            RequiemLogging.debug(String.format("OnServerPoll resource points at %d, time since last poll : %d minute(s)", PollingConstants.lastPolledResourcePoints, (now - PollingConstants.lastPolledResourcePoints) / TimeConstants.MINUTE_MILLIS));
                            PollingConstants.lastPolledResourcePoints = now;
                            Misc.pollResourcePoints();
                        }
                        //if (now - PollingConstants.lastPolledLootCarpets > PollingConstants.delayLootCarpets) {
                        //    RequiemLogging.debug(String.format("OnServerPoll loot carpets at %d, time since last poll : %d minute(s)", PollingConstants.lastPolledLootCarpets, (now - PollingConstants.lastPolledLootCarpets) / TimeConstants.MINUTE_MILLIS));
                        //    PollingConstants.lastPolledLootCarpets = now;
                        //    Misc.pollLootCarpets();
                        //}
                        if (now - PollingConstants.lastPolledMobSpawners > PollingConstants.delayMobSpawners) {
                            RequiemLogging.debug(String.format("OnServerPoll mob spawners at %d, time since last poll : %d minute(s)", PollingConstants.lastPolledMobSpawners, (now - PollingConstants.lastPolledMobSpawners) / TimeConstants.MINUTE_MILLIS));
                            PollingConstants.lastPolledMobSpawners = now;
                            Misc.pollMobSpawners();
                        }
                        if (enableAthanorMechanism) {
                            if (now - PollingConstants.lastPolledAthanorMechanism > PollingConstants.delayAthanorMechanism) {
                                RequiemLogging.debug(String.format("OnServerPoll Athanor Mechanism at %d, time since last poll : %d minute(s)", PollingConstants.lastPolledAthanorMechanism, (now - PollingConstants.lastPolledAthanorMechanism) / TimeConstants.MINUTE_MILLIS));
                                PollingConstants.lastPolledAthanorMechanism = now;
                                AthanorMechanismBehaviour.phaseShiftAthanorMechanism();
                            }
                            if (now - PollingConstants.lastPolledAthanorMechanismPoll > PollingConstants.delayAthanorMechanismPoll) {
                                RequiemLogging.debug(String.format("OnServerPoll Athanor Mechanism at %d, time since last poll : %d minute(s)", PollingConstants.lastPolledAthanorMechanismPoll, (now - PollingConstants.lastPolledAthanorMechanismPoll) / TimeConstants.MINUTE_MILLIS));
                                PollingConstants.lastPolledAthanorMechanismPoll = now;
                                AthanorMechanismBehaviour.pollAthanorMechanism();
                            }
                        }
                        if (now - PollingConstants.lastPolledFogGoblins > PollingConstants.delayFogGoblins) {
                            RequiemLogging.debug(String.format("OnServerPoll fog goblins at %d, time since last poll : %d minute(s), fog is: %s", PollingConstants.lastPolledFogGoblins, (now - PollingConstants.lastPolledFogGoblins) / TimeConstants.MINUTE_MILLIS, RequiemTools.toPercentage(Server.getWeather().getFog(), 2)));
                            PollingConstants.lastPolledFogGoblins = now;
                            Misc.pollFogGoblins();
                        }
                        //if (now - PollingConstants.lastPolledWallRepair > PollingConstants.delayWallRepair) {
                        //    RequiemLogging.debug(String.format("OnServerPoll auto wall repair at %d, time since last poll : %d minute(s)", PollingConstants.lastPolledWallRepair, (now - PollingConstants.lastPolledWallRepair) / TimeConstants.MINUTE_MILLIS));
                        //    PollingConstants.lastPolledWallRepair = now;
                        //    repairWallsHotA();
                        //}
                        if (now - PollingConstants.lastPolledRepairingNpcs > PollingConstants.delayRepairingNpcs) {
                            RequiemLogging.debug(String.format("OnServerPoll repairing NPC's sound & animation at %d, time since last poll : %d minute(s) %d second(s)", PollingConstants.lastPolledRepairingNpcs, (now - PollingConstants.lastPolledRepairingNpcs) / TimeConstants.MINUTE_MILLIS, (now - PollingConstants.lastPolledRepairingNpcs) / TimeConstants.SECOND_MILLIS));
                            PollingConstants.lastPolledRepairingNpcs = now;
                            Misc.pollRepairingNPCs();
                        }
                        // ===================================== TESTING =====================================
                        //} else {
                        //    if (now - PollingConstants.lastPolledMyceliumChange > PollingConstants.testDelayMyceliumChange) {
                        //        RequiemLogging.debug(String.format("=== TESTING === OnServerPoll change to mycelium at %d, time since last poll : %d minute(s)", PollingConstants.lastPolledMyceliumChange, (now - PollingConstants.lastPolledMyceliumChange) / TimeConstants.MINUTE_MILLIS));
                        //        PollingConstants.lastPolledMyceliumChange = now;
                        //        //changeToMyceliumZone();
                        //        changeToMyceliumDeed();
                        //    }
                        //    //if (now - PollingConstants.lastPolledHolidayMessage > PollingConstants.testDelayHolidayMessage) {
                        //    //    RequiemLogging.debug(String.format("=== TESTING === OnServerPoll holiday message at %d, time since last poll : %d minute(s)", PollingConstants.lastPolledHolidayMessage, (now - PollingConstants.lastPolledHolidayMessage) / TimeConstants.MINUTE_MILLIS));
                        //    //    PollingConstants.lastPolledHolidayMessage = now;
                        //    //    Holidays.holidayDiscordMessage();
                        //    //}
                        //    //if (now - PollingConstants.lastPolledCullHotA > PollingConstants.testDelayCullHotA) {
                        //    //    RequiemLogging.debug(String.format("=== TESTING === OnServerPoll cull HotA at %d, time since last poll : %d minute(s)", PollingConstants.lastPolledCullHotA, (now - PollingConstants.lastPolledCullHotA) / TimeConstants.MINUTE_MILLIS));
                        //    //    PollingConstants.lastPolledCullHotA = now;
                        //    //    cullCreaturesHotA();
                        //    //}
                        //    if (now - PollingConstants.lastPolledTradeTents > PollingConstants.testDelayTradeTents) {
                        //        RequiemLogging.debug(String.format("=== TESTING === OnServerPoll trade tents at %d, time since last poll : %d minute(s)", PollingConstants.lastPolledTradeTents, (now - PollingConstants.lastPolledTradeTents) / TimeConstants.MINUTE_MILLIS));
                        //        PollingConstants.lastPolledTradeTents = now;
                        //        Misc.pollTradeTents();
                        //    }
                        //    if (now - PollingConstants.lastPolledResourcePoints > PollingConstants.testDelayResourcePoints) {
                        //        RequiemLogging.debug(String.format("=== TESTING === OnServerPoll resource points at %d, time since last poll : %d minute(s)", PollingConstants.lastPolledResourcePoints, (now - PollingConstants.lastPolledResourcePoints) / TimeConstants.MINUTE_MILLIS));
                        //        PollingConstants.lastPolledResourcePoints = now;
                        //        Misc.pollResourcePoints();
                        //    }
                        //    //if (now - PollingConstants.lastPolledLootCarpets > PollingConstants.testDelayLootCarpets) {
                        //    //    RequiemLogging.debug(String.format("=== TESTING === OnServerPoll loot carpets at %d, time since last poll : %d minute(s)", PollingConstants.lastPolledLootCarpets, (now - PollingConstants.lastPolledLootCarpets) / TimeConstants.MINUTE_MILLIS));
                        //    //    PollingConstants.lastPolledLootCarpets = now;
                        //    //    Misc.pollLootCarpets();
                        //    //}
                        //    if (now - PollingConstants.lastPolledMobSpawners > PollingConstants.testDelayMobSpawners) {
                        //        RequiemLogging.debug(String.format("=== TESTING === OnServerPoll mob spawners at %d, time since last poll : %d minute(s)", PollingConstants.lastPolledMobSpawners, (now - PollingConstants.lastPolledMobSpawners) / TimeConstants.MINUTE_MILLIS));
                        //        PollingConstants.lastPolledMobSpawners = now;
                        //        Misc.pollMobSpawners();
                        //    }
                        //    if (PollingConstants.enableAthanorMechanism) {
                        //        if (now - PollingConstants.lastPolledAthanorMechanism > PollingConstants.testDelayAthanorMechanism) {
                        //            RequiemLogging.debug(String.format("=== TESTING === OnServerPoll athanor mechanism at %d, time since last poll : %d minute(s)", PollingConstants.lastPolledAthanorMechanism, (now - PollingConstants.lastPolledAthanorMechanism) / TimeConstants.MINUTE_MILLIS));
                        //            PollingConstants.lastPolledAthanorMechanism = now;
                        //            AthanorMechanismBehaviour.phaseShiftAthanorMechanism();
                        //        }
                        //        if (now - PollingConstants.lastPolledAthanorMechanismPoll > PollingConstants.testDelayAthanorMechanismPoll) {
                        //            RequiemLogging.debug(String.format("=== TESTING === OnServerPoll athanor mechanism poll at %d, time since last poll : %d minute(s)", PollingConstants.lastPolledAthanorMechanismPoll, (now - PollingConstants.lastPolledAthanorMechanismPoll) / TimeConstants.MINUTE_MILLIS));
                        //            PollingConstants.lastPolledAthanorMechanismPoll = now;
                        //            AthanorMechanismBehaviour.pollAthanorMechanism();
                        //        }
                        //    }
                        //    if (now - PollingConstants.lastPolledFogGoblins > PollingConstants.testDelayFogGoblins) {
                        //        RequiemLogging.debug(String.format("=== TESTING ===  OnServerPoll fog goblins at %d, time since last poll : %d minute(s), fog is: %s", PollingConstants.lastPolledFogGoblins, (now - PollingConstants.lastPolledFogGoblins) / TimeConstants.MINUTE_MILLIS, RequiemTools.toPercentage(Server.getWeather().getFog(), 2)));
                        //        PollingConstants.lastPolledFogGoblins = now;
                        //        Misc.pollFogGoblins();
                        //    }
                        //    if (now - PollingConstants.lastPolledWallRepair > PollingConstants.testDelayWallRepair) {
                        //        RequiemLogging.debug(String.format("=== TESTING === OnServerPoll auto wall repair at %d, time since last poll : %d minute(s)", PollingConstants.lastPolledWallRepair, (now - PollingConstants.lastPolledWallRepair) / TimeConstants.MINUTE_MILLIS));
                        //        PollingConstants.lastPolledWallRepair = now;
                        //        repairWallsHotA();
                        //    }
                        //    if (now - PollingConstants.lastPolledRepairingNpcs > PollingConstants.testDelayRepairingNpcs) {
                        //        RequiemLogging.debug(String.format("=== TESTING ===  OnServerPoll repairing NPC's sound & animation at %d, time since last poll : %d minute(s) %d second(s)", PollingConstants.lastPolledRepairingNpcs, (now - PollingConstants.lastPolledRepairingNpcs) / TimeConstants.MINUTE_MILLIS, (now - PollingConstants.lastPolledRepairingNpcs) / TimeConstants.SECOND_MILLIS));
                        //        PollingConstants.lastPolledRepairingNpcs = now;
                        //        Misc.pollRepairingNPCs();
                        //    }
                        //}
                        return method.invoke(object, args);
                    }
                };
            }
        });
    }

    public static void onServerPoll() {
        if ((PollingConstants.lastSecondPolled + TimeConstants.SECOND_MILLIS) < System.currentTimeMillis()) {
            if (PollingConstants.lastPolledDepots + PollingConstants.pollDepotTime < System.currentTimeMillis()) {
                SupplyDepotBehaviour.pollDepotSpawn();
                PollingConstants.lastPolledDepots += PollingConstants.pollDepotTime;
            }
            if (PollingConstants.lastPolledTitanSpawn + PollingConstants.pollTitanSpawnTime < System.currentTimeMillis()) {
                Titans.pollTitanSpawn();
                PollingConstants.lastPolledTitanSpawn += PollingConstants.pollTitanSpawnTime;
            }
            if (PollingConstants.lastPolledTitans + PollingConstants.pollTitanTime < System.currentTimeMillis()) {
                Titans.pollTitans();
                PollingConstants.lastPolledTitans += PollingConstants.pollTitanTime;
            }
            if (PollingConstants.lastPolledRareSpawns + PollingConstants.pollRareSpawnTime < System.currentTimeMillis()) {
                RareSpawns.pollRareSpawns();
                PollingConstants.lastPolledRareSpawns += PollingConstants.pollRareSpawnTime;
            }
            if (PollingConstants.lastPolledMissionCreator + PollingConstants.pollMissionCreatorTime < System.currentTimeMillis()) {
                MissionCreator.pollMissions();
                PollingConstants.lastPolledMissionCreator += PollingConstants.pollMissionCreatorTime;
            }
            //if (PollingConstants.lastPolledBloodlust + PollingConstants.pollBloodlustTime < System.currentTimeMillis()) {
            //    Bloodlust.pollLusts();
            //    PollingConstants.lastPolledBloodlust += PollingConstants.pollBloodlustTime;
            //}
            if (terrainSmoothing) {
                if (PollingConstants.lastPolledTerrainSmooth + PollingConstants.pollTerrainSmoothTime < System.currentTimeMillis()) {
                    SmoothTerrainAction.onServerPoll();
                    PollingConstants.lastPolledTerrainSmooth += PollingConstants.pollTerrainSmoothTime;
                }
            }
            if (PollingConstants.lastPolledLightningStrike + PollingConstants.pollLightningStrikeTime < System.currentTimeMillis()) {
                EffectsTools.tickRandomLightning(EffectsConstants.tileX, EffectsConstants.tileY);
                PollingConstants.lastPolledLightningStrike += PollingConstants.pollLightningStrikeTime;
            }
            //if (PollingConstants.lastPolledCluckster + PollingConstants.pollClucksterTime < System.currentTimeMillis()) {
            //    Cluckster.pollClucksters();
            //    PollingConstants.lastPolledCluckster += PollingConstants.pollClucksterTime;
            //}
            // Update counter
            if (PollingConstants.lastSecondPolled + TimeConstants.SECOND_MILLIS * 10 > System.currentTimeMillis()) {
                PollingConstants.lastSecondPolled += TimeConstants.SECOND_MILLIS;
            } else {
                RequiemLogging.debug("Time between last poll was greater than 10 seconds. Resetting all poll counters...");
                PollingConstants.lastSecondPolled = System.currentTimeMillis();
                PollingConstants.lastPolledTitanSpawn = System.currentTimeMillis();
                PollingConstants.lastPolledTitans = System.currentTimeMillis();
                PollingConstants.lastPolledDepots = System.currentTimeMillis();
                PollingConstants.lastPolledRareSpawns = System.currentTimeMillis();
                PollingConstants.lastPolledEternalReservoirs = System.currentTimeMillis();
                PollingConstants.lastPolledMissionCreator = System.currentTimeMillis();
                //PollingConstants.lastPolledBloodlust = System.currentTimeMillis();
                PollingConstants.lastPolledUniqueRegeneration = System.currentTimeMillis();
                PollingConstants.lastPolledUniqueCollection = System.currentTimeMillis();
                PollingConstants.lastPolledTerrainSmooth = System.currentTimeMillis();
                //PollingConstants.lastPolledCluckster = System.currentTimeMillis();
            }
        }
    }

    private static void repairWallsHotA() {
        // Search for island of death zone and repair all walls. Thanks bdew!
        Arrays.stream(FocusZone.getAllZones()).filter(focusZone -> focusZone.getName().equals("Island of Death") || focusZone.getName().equals("Island Of Death")).findAny().ifPresent(zone -> {
            for (int startX = zone.getStartX(); startX <= zone.getEndX(); startX++) {
                for (int startY = zone.getStartY(); startY <= zone.getEndY(); startY++) {
                    VolaTile volaTile = Zones.getTileOrNull(startX, startY, true);
                    if (volaTile != null)
                        for (Fence fence : volaTile.getFences()) {
                            if (fence.getDamage() > 0.0f || fence.getQualityLevel() > 100.0f) {
                                fence.setHasNoDecay(false);
                                fence.setIsIndestructible(false);
                                fence.setIsNoPut(false);
                                fence.setIsNoDrag(false);
                                fence.setIsNoDrop(false);
                                fence.setIsNoMove(false);
                                fence.setIsNoTake(false);
                                fence.setIsPlanted(false);
                                fence.setIsNoRepair(false);
                                fence.setIsNoImprove(false);
                                fence.setIsNotLockable(false);
                                fence.setIsNotTurnable(false);
                                fence.setIsNotPaintable(false);
                                fence.setIsNotSpellTarget(false);
                                fence.setIsNotLockpickable(false);
                                fence.savePermissions();
                                fence.setDamage(0.0f);
                                fence.setQualityLevel(100.0f);
                                fence.setHasNoDecay(true);
                                fence.setIsIndestructible(true);
                                fence.savePermissions();
                                RequiemLogging.debug(String.format("Repairing fence (WurmId: %d) at damage %s in %s", fence.getId(), fence.getDamage(), zone.getName()));
                            }
                        }
                }
            }
        });
    }

    private static void cullCreaturesHotA() {
        // Search for island of death zone and cull creatures until total count in zone is what I want. Thanks bdew!
        Arrays.stream(FocusZone.getAllZones()).filter(FocusZone::isPvPHota).findAny().ifPresent(zone -> {
            List<Creature> allCreatures = new ArrayList<>();
            for (int startX = zone.getStartX(); startX <= zone.getEndX(); startX++) {
                for (int startY = zone.getStartY(); startY <= zone.getEndY(); startY++) {
                    VolaTile volaTile = Zones.getTileOrNull(startX, startY, true);
                    if (volaTile != null) allCreatures.addAll(Arrays.asList(volaTile.getCreatures()));
                }
            }
            while (allCreatures.size() > 1000) {
                Creature creature = allCreatures.remove(Server.rand.nextInt(allCreatures.size()));
                if (!creature.isPlayer() && CreatureTools.isOkToDestroy(creature)) {
                    creature.destroy();
                    RequiemLogging.debug(String.format("Culling %s %s (WurmId: %s) in %s", RequiemTools.a_an(creature.getNameWithoutPrefixes()), creature.getNameWithoutPrefixes(), creature.getWurmId(), zone.getName()));
                }
            }
        });
    }

    private static void changeToMyceliumZone() {
        Arrays.stream(FocusZone.getAllZones()).filter(z -> z.getName().contains("mycelium") || z.getName().contains("Mycelium")).findAny().ifPresent(zone -> {
            for (int x = zone.getStartX(); x <= zone.getEndX(); x++) {
                for (int y = zone.getStartY(); y <= zone.getEndY(); y++) {
                    int tile = Server.surfaceMesh.getTile(x, y);
                    byte type = Tiles.decodeType(tile);
                    Tiles.Tile theTile = Tiles.getTile(type);
                    byte data = Tiles.decodeData(tile);
                    if (theTile.isTree())
                        Server.setSurfaceTile(x, y, Tiles.decodeHeight(tile), theTile.getTreeType(data).asMyceliumTree(), data);
                    else if (theTile.isBush())
                        Server.setSurfaceTile(x, y, Tiles.decodeHeight(tile), theTile.getBushType(data).asMyceliumBush(), data);
                    else if (theTile.isRoad() || theTile.getId() == Tiles.TILE_TYPE_DIRT_PACKED || theTile.isEnchanted() || theTile.isCaveDoor() || theTile.getId() == Tiles.TILE_TYPE_ROCK)
                        continue;
                    else
                        Server.setSurfaceTile(x, y, Tiles.decodeHeight(tile), Tiles.Tile.TILE_MYCELIUM.id, (byte) 0);
                }
            }
        });
    }

    private static void changeToMyceliumDeed() {
        Arrays.stream(Villages.getVillages()).filter(village -> village.getName().equals("Tartarus")).findAny().ifPresent(village -> {
            for (int x = village.getStartX(); x <= village.getEndX(); x++) {
                for (int y = village.getStartY(); y <= village.getEndY(); y++) {
                    int tile = Server.surfaceMesh.getTile(x, y);
                    byte type = Tiles.decodeType(tile);
                    Tiles.Tile theTile = Tiles.getTile(type);
                    byte data = Tiles.decodeData(tile);
                    if (theTile.isTree())
                        Server.setSurfaceTile(x, y, Tiles.decodeHeight(tile), theTile.getTreeType(data).asMyceliumTree(), data);
                    else if (theTile.isBush())
                        Server.setSurfaceTile(x, y, Tiles.decodeHeight(tile), theTile.getBushType(data).asMyceliumBush(), data);
                    else if (theTile.isRoad() || RequiemTools.isMineDoor(theTile) || theTile.getId() == Tiles.TILE_TYPE_DIRT_PACKED || theTile.isEnchanted() || Tiles.isMineDoor(theTile) || theTile.getId() == Tiles.TILE_TYPE_ROCK || theTile.getId() == Tiles.TILE_TYPE_CLAY || theTile.getId() == Tiles.TILE_TYPE_MOSS || theTile.getId() == Tiles.TILE_TYPE_PEAT || theTile.getId() == Tiles.TILE_TYPE_SAND)
                        continue;
                    else
                        Server.setSurfaceTile(x, y, Tiles.decodeHeight(tile), Tiles.Tile.TILE_MYCELIUM.id, (byte) 0);
                }
            }
        });
    }

    public static void changeToGrass() {
        // Search entire server for "Unknown" grass tiles (they all seem to have flowers) and change them to normal grass or flower tiles. Thanks bdew!
        Arrays.stream(FocusZone.getAllZones()).filter(focusZone -> focusZone.getName().contains("unknown")).findAny().ifPresent(zone -> {
            for (int startX = zone.getStartX(); startX <= zone.getEndX(); startX++) {
                for (int startY = zone.getStartY(); startY <= zone.getEndY(); startY++) {
                    int tile = Server.surfaceMesh.getTile(startX, startY);
                    byte type = Tiles.decodeType(tile);
                    Tiles.Tile theTile = Tiles.getTile(type);
                    if (!theTile.isMycelium()) {
                        Server.setSurfaceTile(startX, startY, Tiles.decodeHeight(tile), Tiles.Tile.TILE_GRASS.id, (byte) 0);
                        Players.getInstance().sendChangedTile(startX, startY, true, false);
                    }
                }
            }
        });
    }

}
