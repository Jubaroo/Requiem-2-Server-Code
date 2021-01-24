package org.jubaroo.mods.wurm.server.items.behaviours;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.*;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.NoSuchTemplateException;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.zones.Zones;
import com.wurmonline.shared.constants.EffectConstants;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.tools.RandomUtils;

import java.util.ArrayList;

public class SupplyDepots {
    private static boolean isLight = false;
    public static ArrayList<Item> depots = new ArrayList<>();
    public static Creature host = null;
    public static long depotRespawnTime = TimeConstants.HOUR_MILLIS * 3L;
    public static long lastSpawnedDepot = 0;

    public static void sendDepotEffect(Player player, Item depot) {
        player.getCommunicator().sendAddEffect(depot.getWurmId(), EffectConstants.RIFT_SPAWN, depot.getPosX(), depot.getPosY(), depot.getPosZ(), (byte) 0);
    }

    public static void sendDepotEffectsToPlayer(Player player) {
        RequiemLogging.logInfo(String.format("Sending depot effects to player %s", player.getName()));
        for (Item depot : depots) {
            sendDepotEffect(player, depot);
        }
    }

    public static void sendDepotEffectsToPlayers(Item depot) {
        removeDepotEffect(depot);
        for (Player p : Players.getInstance().getPlayers()) {
            sendDepotEffect(p, depot);
        }
    }

    public static void removeDepotEffect(Item depot) {
        for (Player player : Players.getInstance().getPlayers()) {
            player.getCommunicator().sendRemoveEffect(depot.getWurmId());
        }
    }

    public static void removeSupplyDepot(Item depot) {
        if (depots.contains(depot)) {
            depots.remove(depot);
        }
        removeDepotEffect(depot);
        isLight = false;
    }

    private static boolean isSupplyDepot(Item item) {
        return item.getTemplateId() == CustomItems.requiemDepotId;
    }

    public static void pollDepotSpawn() {
        for (int i = 0; i < depots.size(); i++) {
            Item depot = depots.get(i);
            if (!Items.exists(depot)) {
                RequiemLogging.logInfo("Supply depot was destroyed, removing from list.");
                depots.remove(depot);
                removeDepotEffect(depot);
                isLight = false;
            }
        }
        for (Item item : Items.getAllItems()) {
            if (!isLight && isSupplyDepot(item) && !depots.contains(item)) {
                RequiemLogging.logInfo("Found existing supply depots, adding to list and sending data to players. item name: " + item.getName() + ", id: " + item.getWurmId());
                depots.add(item);
                sendDepotEffectsToPlayers(item);
                isLight = true;
            }
        }
        if (depots.isEmpty()) {
            if (System.currentTimeMillis() > lastSpawnedDepot + depotRespawnTime) {
                RequiemLogging.logInfo("No Depots were found, and the timer has expired. Spawning a new one.");
                boolean spawned = false;
                int i = 0;
                while (!spawned && i < 20) {
                    float worldSizeX = Zones.worldTileSizeX;
                    float worldSizeY = Zones.worldTileSizeY;
                    float minX = worldSizeX * 0.25f;
                    float minY = worldSizeY * 0.25f;
                    int tilex = (int) (minX + (minX * 2 * Server.rand.nextFloat()));
                    int tiley = (int) (minY + (minY * 2 * Server.rand.nextFloat()));
                    int tile = Server.surfaceMesh.getTile(tilex, tiley);
                    try {
                        if (Tiles.decodeHeight(tile) > 0) {

                            Item depot = ItemFactory.createItem(CustomItems.requiemDepotId, 50 + Server.rand.nextFloat() * 40f, (float) (tilex << 2) + 2.0f, (float) (tiley << 2) + 2.0f, RandomUtils.getRandomRotation(), true, (byte) 0, -10, null);

                            if (isLight) {
                                removeDepotEffect(depot);
                            }

                            sendDepotEffectsToPlayers(depot);

                            isLight = true;

                            Server.getInstance().broadCastAlert("A supply cache has been delivered by the gods of Valrei.");

                            RequiemLogging.logInfo(String.format("New supply depot being placed at %d, %d", tilex, tiley));
                            spawned = true;
                            host = null;
                            lastSpawnedDepot = System.currentTimeMillis();
                            depotRespawnTime = (TimeConstants.HOUR_MILLIS * 3L) + (TimeConstants.MINUTE_MILLIS * (long) Server.rand.nextFloat() * 60L);
                            RequiemLogging.logInfo(String.format("setting depotRespawnTime: %d", depotRespawnTime));
                        } else {
                            RequiemLogging.logInfo(String.format("Position %d, %d was invalid, attempting another spawn...", tilex, tiley));
                            i++;
                        }
                    } catch (Exception e) {
                        RequiemLogging.logException("Failed to create Arena Depot.", e.getCause());
                        e.printStackTrace();
                    }
                }
                if (i >= 20) {
                    RequiemLogging.logWarning("Could not find a valid location within 20 tries for a supply depot.");
                }
            }
        }
    }

    public static long lastAttemptedDepotCapture = 0;
    public static final long captureMessageInterval = TimeConstants.MINUTE_MILLIS * 3L;

    public static void maybeBroadcastOpen(Creature performer) {
        if (System.currentTimeMillis() > lastAttemptedDepotCapture + captureMessageInterval) {
            Server.getInstance().broadCastAlert(String.format("%s is beginning to claim the supply cache!", performer.getName()));
            lastAttemptedDepotCapture = System.currentTimeMillis();
        }
    }

    public static void giveCacheReward(Creature performer) {
        try {
            Item inv = performer.getInventory();
            int chance = Server.rand.nextInt(10);

            switch (chance) {
                case 5:
                    Item bone = ItemFactory.createItem(ItemList.boneCollar, 55.0F + (Server.rand.nextFloat() * 40.0F), null);
                    bone.setRarity((byte) 1);
                    inv.insertItem(bone);
                    break;

                case 6:
                    Item basket = ItemFactory.createItem(ItemList.xmasLunchbox, 55.0F + (Server.rand.nextFloat() * 40.0F), null);
                    inv.insertItem(basket);
                    break;

                default:
                    int[] starGems = {ItemList.emeraldStar, ItemList.rubyStar, ItemList.opalBlack, ItemList.diamondStar, ItemList.sapphireStar};
                    int gemID = starGems[Server.rand.nextInt(starGems.length)];
                    Item gem = ItemFactory.createItem(gemID, 55.0F + (Server.rand.nextFloat() * 40.0F), null);
                    inv.insertItem(gem);
                    break;
            }

            for (int i = 0; i <= 3; i++) {
                Item newItem = ItemFactory.createItem(ItemList.sleepPowder, 55.0F + (Server.rand.nextFloat() * 40.0F), null);
                inv.insertItem(newItem);
            }

            for (int i = 0; i <= 5; i++) {
                Item newItem = ItemFactory.createItem(ItemList.arrowWar, 55.0F + (Server.rand.nextFloat() * 40.0F), null);
                inv.insertItem(newItem);
            }

        } catch (FailedException | NoSuchTemplateException e) {
            e.printStackTrace();
        }
    }

}
