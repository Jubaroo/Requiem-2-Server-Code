package org.jubaroo.mods.wurm.server.tools.database.patreon;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.Materials;
import com.wurmonline.server.items.NoSuchTemplateException;
import com.wurmonline.server.players.Player;
import com.wurmonline.shared.util.MaterialUtilities;
import org.gotti.wurmunlimited.modsupport.ModSupportDb;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.misc.Misc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class PatreonSleepPowderGift {
    private static int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
    private static int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    public static void onServerStarted() {
        try {
            Connection con = ModSupportDb.getModSupportDb();
            String sql;
            String tableName = "RequiemPatreonGift";
            if (!ModSupportDb.hasTable(con, tableName)) {
                RequiemLogging.debug(String.format("%s table not found in ModSupport, creating it now.", tableName));
                sql = String.format("CREATE TABLE %s (NAME VARCHAR(30) NOT NULL DEFAULT 'Unknown', STEAMID LONG NOT NULL DEFAULT 0, GIFTED_MONTH INT NOT NULL DEFAULT 0, GIFTED_YEAR INT NOT NULL DEFAULT 0)", tableName);
                PreparedStatement ps = con.prepareStatement(sql);
                ps.execute();
                ps.close();
            }
        } catch (SQLException e) {
            RequiemLogging.logException("Error in onServerStarted()", e);
            throw new RuntimeException(e);
        }
    }

    public static void onPlayerLogin(Player player) {
        Connection dbcon;
        PreparedStatement ps;
        boolean steamIdGifted = false;
        boolean thisMonth = false;
        boolean thisYear = false;
        try {
            long steamId = Long.parseLong(String.valueOf(player.getSteamId()));
            dbcon = ModSupportDb.getModSupportDb();
            ps = dbcon.prepareStatement("SELECT * FROM RequiemPatreonGift");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getLong("STEAMID") == steamId & rs.getInt("GIFTED_MONTH") == currentMonth & rs.getInt("GIFTED_YEAR") == currentYear) {
                    steamIdGifted = true;
                    thisMonth = true;
                    thisYear = true;
                    break;
                }
            }
            rs.close();
            ps.close();
            if (!steamIdGifted && !thisMonth && !thisYear) {
                RequiemLogging.debug(String.format("Player %s has not been gifted this month, giving sleep powder x5 now...", player.getName()));
                dbcon = ModSupportDb.getModSupportDb();
                ps = dbcon.prepareStatement("INSERT INTO RequiemPatreonGift (NAME,STEAMID,GIFTED_MONTH,GIFTED_YEAR) VALUES(?,?,?,?)");
                ps.setString(1, player.getName());
                ps.setLong(2, steamId);
                ps.setInt(3, currentMonth);
                ps.setInt(4, currentYear);
                ps.executeUpdate();
                ps.close();
                // Give items to Patreons
                giveItems(player);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void giveItems(Player player) {
        try {
            if (Misc.isRequiemPatreon(player)) {
                for (int i = 0; i < 5; i++) {
                    player.getInventory().insertItem(ItemFactory.createItem(ItemList.sleepPowder, 99f, Materials.MATERIAL_WHEAT, MaterialUtilities.COMMON, null), true);
                }
                player.getCommunicator().sendServerMessage("A shadowy figure appears behind you and your pocket gets heavier. You quickly turn around to see what is going on only to find that nobody is there. Your monthly sleep powder had been deposited into your inventory!", 255, 192, 203);
            }
        } catch (FailedException | NoSuchTemplateException e) {
            RequiemLogging.logException("Error in PatreonSleepPowderGift.giveItems()", e);
            throw new RuntimeException(e);
        }
    }
}