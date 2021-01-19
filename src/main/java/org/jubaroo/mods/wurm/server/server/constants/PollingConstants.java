package org.jubaroo.mods.wurm.server.server.constants;

import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.creatures.Creature;

import java.util.ArrayList;

public class PollingConstants {
    public static String tradeTentsNorthZoneName = "Northport";
    public static String tradeTentsSouthZoneName = "Southport";
    public static long tradeTentCoinReward = 2000L;
    public static boolean initialGoblinCensus = false;
    public static int maxFogGoblins = 100;
    public static ArrayList<Creature> fogGoblins = new ArrayList<>();
    public static final long pollTitanSpawnTime = TimeConstants.MINUTE_MILLIS * 2;
    public static final long pollLightningStrikeTime = TimeConstants.SECOND_MILLIS * 5;
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
    public static long lastPolledLightningStrike = 0;
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
}
