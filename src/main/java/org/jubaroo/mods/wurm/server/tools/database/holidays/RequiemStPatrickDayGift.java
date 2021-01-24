package org.jubaroo.mods.wurm.server.tools.database.holidays;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.items.*;
import com.wurmonline.server.players.Player;
import com.wurmonline.shared.util.MaterialUtilities;
import org.gotti.wurmunlimited.modsupport.ModSupportDb;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class RequiemStPatrickDayGift {
    private static final int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    public static void onPlayerLogin(Player player) {
        Connection dbcon;
        PreparedStatement ps;
        boolean steamIdGifted = false;
        boolean thisYear = false;
        try {
            long steamId = Long.parseLong(String.valueOf(player.getSteamId()));
            dbcon = ModSupportDb.getModSupportDb();
            ps = dbcon.prepareStatement("SELECT * FROM RequiemStPatrickDayGift");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getLong("STEAMID") == steamId & rs.getInt("GIFTEDYEAR") == currentYear) {
                    steamIdGifted = true;
                    thisYear = true;
                    break;
                }
            }
            rs.close();
            ps.close();
            if (!steamIdGifted & !thisYear) {
                RequiemLogging.logInfo("Player " + player.getName() + " has not been gifted this year, giving RequiemStPatrickDayGift now...");
                dbcon = ModSupportDb.getModSupportDb();
                ps = dbcon.prepareStatement("INSERT INTO RequiemStPatrickDayGift (NAME,STEAMID,GIFTEDYEAR) VALUES(?,?,?)");
                ps.setString(1, player.getName());
                ps.setLong(2, steamId);
                ps.setInt(3, currentYear);
                ps.executeUpdate();
                ps.close();
                // Give items
                giveItems(player);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void onServerStarted() {
        try {
            Connection con = ModSupportDb.getModSupportDb();
            String sql;
            String tableName = "RequiemStPatrickDayGift";
            if (!ModSupportDb.hasTable(con, tableName)) {
                RequiemLogging.logInfo(tableName + " table not found in ModSupport, creating it now.");
                sql = "CREATE TABLE " + tableName + " (NAME VARCHAR(30) NOT NULL DEFAULT 'Unknown', STEAMID LONG NOT NULL DEFAULT 0, GIFTEDYEAR INT NOT NULL DEFAULT 0)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.execute();
                ps.close();
            }
        } catch (SQLException e) {
            RequiemLogging.logException("Error in onServerStarted()", e);
            throw new RuntimeException(e);
        }
    }

    private static void giveItems(Player player) {
        try {
            final Item waterSkin = ItemFactory.createItem(ItemList.skinWater, 20.0f, null);
            final Item whisky = ItemFactory.createItem(ItemList.whisky, 99.0f, null);
            whisky.setWeight(2500, true);
            waterSkin.insertItem(whisky);
            player.getInventory().insertItem(waterSkin, true);
            player.getInventory().insertItem(ItemFactory.createItem(ItemList.sleepPowder, 99f, Materials.MATERIAL_WHEAT, MaterialUtilities.COMMON, null), true);
            player.getCommunicator().sendServerMessage("A shadowy figure appears behind you and your pocket gets heavier. You quickly turn around to see what is going on only to find that nobody is there.", 255, 192, 203);
        } catch (FailedException | NoSuchTemplateException e) {
            RequiemLogging.logException("Error in giveItems()", e);
            throw new RuntimeException(e);
        }
    }
}