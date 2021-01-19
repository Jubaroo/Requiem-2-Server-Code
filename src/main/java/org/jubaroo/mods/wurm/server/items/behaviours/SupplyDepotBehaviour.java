package org.jubaroo.mods.wurm.server.items.behaviours;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.*;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.economy.Economy;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.NoSuchTemplateException;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.villages.Village;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zones;
import com.wurmonline.shared.constants.EffectConstants;
import org.gotti.wurmunlimited.modsupport.ModSupportDb;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.communication.discord.DiscordHandler;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import org.jubaroo.mods.wurm.server.server.constants.ToggleConstants;
import org.jubaroo.mods.wurm.server.tools.ItemTools;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplyDepotBehaviour {
    public static ArrayList<Item> depots = new ArrayList<>();
    public static final long depotRespawnTime = TimeConstants.HOUR_MILLIS * 11L;
    public static long lastSpawnedDepot = 0;
    protected static boolean initalizedSupplyDepot = false;

    public static void updateLastSpawnedDepot() {
        Connection dbcon;
        PreparedStatement ps;
        try {
            dbcon = ModSupportDb.getModSupportDb();
            ps = dbcon.prepareStatement(String.format("UPDATE ObjectiveTimers SET TIMER = %d WHERE ID = \"DEPOT\"", System.currentTimeMillis()));
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initializeDepotTimer() {
        Connection dbcon;
        PreparedStatement ps;
        boolean foundLeaderboardOpt = false;
        try {
            dbcon = ModSupportDb.getModSupportDb();
            ps = dbcon.prepareStatement("SELECT * FROM ObjectiveTimers WHERE ID = \"DEPOT\"");
            ResultSet rs = ps.executeQuery();
            lastSpawnedDepot = rs.getLong("TIMER");
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        RequiemLogging.logInfo(String.format("Initialized Supply Depot timer: %d", lastSpawnedDepot));
        initalizedSupplyDepot = true;
    }

    public static void addPlayerStatsDepot(String playerName) {
        Connection dbcon;
        PreparedStatement ps;
        try {
            dbcon = ModSupportDb.getModSupportDb();
            ps = dbcon.prepareStatement(String.format("UPDATE PlayerStats SET DEPOTS = DEPOTS + 1 WHERE NAME = \"%s\"", playerName));
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendDepotEffect(Player player, Item depot) {
        //player.getCommunicator().sendAddEffect(depot.getWurmId(), EffectsConstants.RIFT_SPAWN, depot.getPosX(), depot.getPosY(), depot.getPosZ(), (byte) 0);
        player.getCommunicator().sendAddEffect(depot.getWurmId(), EffectConstants.EFFECT_ERUPTION, depot.getPosX(), depot.getPosY(), depot.getPosZ(), (byte) 0);
    }

    public static void sendDepotEffectsToPlayer(Player player) {
        RequiemLogging.logInfo(String.format("Sending depot effects to player %s", player.getName()));
        for (Item depot : depots) {
            sendDepotEffect(player, depot);
        }
    }

    public static void sendDepotEffectsToPlayers(Item depot) {
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
        depots.remove(depot);
        removeDepotEffect(depot);
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
            }
        }
        for (Item item : Items.getAllItems()) {
            if (isSupplyDepot(item) && !depots.contains(item)) {
                RequiemLogging.logInfo("Found existing supply depots, adding to list and sending data to players.");
                depots.add(item);
                sendDepotEffectsToPlayers(item);
            }
        }
        if (!ToggleConstants.enableDepots) {
            return;
        }
        if (!initalizedSupplyDepot) {
            return;
        }
        if (depots.isEmpty()) {
            if (System.currentTimeMillis() > lastSpawnedDepot + depotRespawnTime) {
                RequiemLogging.logInfo("No Depots were found, and the timer has expired. Spawning a new one.");
                boolean spawned = false;
                int i = 0;
                int spawnAttempts = 250;
                while (!spawned && i < spawnAttempts) {
                    float worldSizeX = Zones.worldTileSizeX;
                    float worldSizeY = Zones.worldTileSizeY;
                    float minX = worldSizeX * 0.2f;
                    float minY = worldSizeY * 0.2f;
                    int tilex = (int) (minX + (minX * 3 * Server.rand.nextFloat()));
                    int tiley = (int) (minY + (minY * 3 * Server.rand.nextFloat()));
                    int tile = Server.surfaceMesh.getTile(tilex, tiley);
                    try {
                        // find if the tile is in a village
                        final VolaTile vtile = Zones.getOrCreateTile(tilex, tiley, true);
                        Village village = vtile.getVillage();
                        boolean inVillage = false;
                        if (village != null) {
                            inVillage = true;
                        }
                        final MeshTile meshTile = new MeshTile(Server.surfaceMesh, tilex, tiley);
                        // check to see if the tile is above water
                        if (Tiles.decodeHeight(tile) > 0) {
                            // check to see if the tile is flat
                            if (!meshTile.isFlat()) {
                                // check to see if the tile is in a village
                                if (!inVillage) {
                                    Item depot = ItemFactory.createItem(CustomItems.requiemDepotId, 50 + Server.rand.nextFloat() * 40f, (float) (tilex << 2) + 2.0f, (float) (tiley << 2) + 2.0f, Server.rand.nextFloat() * 360.0f, true, (byte) 0, -10, null);
                                    depots.add(depot);
                                    sendDepotEffectsToPlayers(depot);
                                    RequiemLogging.logInfo(String.format("New supply depot being placed at %d, %d", tilex, tiley));
                                    spawned = true;
                                    String mess = "A new Requiem depot has appeared!";
                                    DiscordHandler.sendToDiscord(CustomChannel.EVENTS, mess);
                                    lastSpawnedDepot = System.currentTimeMillis();
                                    updateLastSpawnedDepot();
                                } else {
                                    RequiemLogging.logInfo(String.format("Position %d, %d was invalid, it is in a village, attempting another spawn...", tilex, tiley));
                                    i++;
                                }
                            } else {
                                RequiemLogging.logInfo(String.format("Position %d, %d was invalid, it is not flat, attempting another spawn...", tilex, tiley));
                                i++;
                            }
                        } else {
                            RequiemLogging.logInfo(String.format("Position %d, %d was invalid, it is not above water, attempting another spawn...", tilex, tiley));
                            i++;
                        }
                    } catch (Exception e) {
                        RequiemLogging.logException("Failed to create Requiem Depot.", e);
                        e.printStackTrace();
                    }
                }
                if (i >= spawnAttempts) {
                    RequiemLogging.logWarning(String.format("Could not find a valid location within %d tries for a Requiem depot.", spawnAttempts));
                }
            }
            long timeLeft = (lastSpawnedDepot + depotRespawnTime) - System.currentTimeMillis();
            long minutesLeft = timeLeft / TimeConstants.MINUTE_MILLIS;
            if (minutesLeft > 0) {
                if (minutesLeft == 4) {
                    DiscordHandler.sendToDiscord(CustomChannel.EVENTS, String.format("The next Requiem depot will appear in %d minutes!", minutesLeft + 1));
                } else if (minutesLeft == 19) {
                    DiscordHandler.sendToDiscord(CustomChannel.EVENTS, String.format("The next Requiem depot will appear in %d minutes!", minutesLeft + 1));
                } else if (minutesLeft == 59) {
                    DiscordHandler.sendToDiscord(CustomChannel.EVENTS, String.format("The next Requiem depot will appear in %d minutes!", minutesLeft + 1));
                }
            }
        }
    }

    public static long lastAttemptedDepotCapture = 0;
    public static final long captureMessageInterval = TimeConstants.MINUTE_MILLIS * 3;

    public static void maybeBroadcastOpen(Creature performer) {
        if (System.currentTimeMillis() > lastAttemptedDepotCapture + captureMessageInterval) {
            DiscordHandler.sendToDiscord(CustomChannel.EVENTS, String.format("%s is beginning to claim a requiem depot", performer.getName()));
            lastAttemptedDepotCapture = System.currentTimeMillis();
        }
    }

    public static void broadcastCapture(Creature performer) {
        DiscordHandler.sendToDiscord(CustomChannel.EVENTS, String.format("%s has claimed a requiem depot!", performer.getName()));
    }

    public static void giveCacheReward(Creature performer) {
        Item inv = performer.getInventory();
        Item enchantOrb = ItemTools.createEnchantOrb(60f + (Math.min(Server.rand.nextFloat() * 60f, Server.rand.nextFloat() * 60f)));
        if (enchantOrb != null) {
            inv.insertItem(enchantOrb);
        }
        try {
            // Add a special caches as a reward.
            int[] cacheIds = {
                    CustomItems.armourCache.getTemplateId(),
                    CustomItems.crystalCache.getTemplateId(),
                    CustomItems.gemCache.getTemplateId(), CustomItems.gemCache.getTemplateId(),
                    CustomItems.riftCache.getTemplateId(),
                    CustomItems.treasureMapCache.getTemplateId()
            };
            int i = 3 + Server.rand.nextInt(2); // 2-3 caches.
            while (i > 0) {
                Item cache = ItemFactory.createItem(cacheIds[Server.rand.nextInt(cacheIds.length)], 40f + (50f * Server.rand.nextFloat()), "");
                inv.insertItem(cache, true);
                i--;
            }
            // High quality seryll
            //Item seryll = ItemFactory.createItem(ItemList.seryllBar, 80 + (20 * Server.rand.nextFloat()), null);
            //inv.insertItem(seryll, true);
            // Sleep powder
            Item sleepPowder = ItemFactory.createItem(ItemList.sleepPowder, 99f, null);
            inv.insertItem(sleepPowder, true);
            // Very low chance for a HotA statue.
            if (Server.rand.nextFloat() * 100f <= 1f) {
                Item hotaStatue = ItemFactory.createItem(ItemList.statueHota, 80f + (20f * Server.rand.nextFloat()), "");
                hotaStatue.setAuxData((byte) Server.rand.nextInt(10));
                hotaStatue.setWeight(50000, true);
                inv.insertItem(hotaStatue, true);
            }
            // Add 10-30 copper
            long iron = 1000; // 10 copper
            iron += Server.rand.nextInt(2000); // add up to 20 copper
            Item[] coins = Economy.getEconomy().getCoinsFor(iron);
            for (Item coin : coins) {
                inv.insertItem(coin, true);
            }
			/*if(Server.rand.nextFloat()*100f <= 3f){
				Item sorcery = ItemFactory.createItem(ItemUtil.sorceryIds[Server.rand.nextInt(ItemUtil.sorceryIds.length)], 80f+(20f*Server.rand.nextFloat()), "");
				sorcery.setAuxData((byte)2);
				inv.insertItem(sorcery, true);
			}*/
        } catch (FailedException | NoSuchTemplateException e) {
            e.printStackTrace();
        }
    }
}
