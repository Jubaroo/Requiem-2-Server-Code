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

public class RequiemCanadaDayGift {
    private static final int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    public static void onPlayerLogin(Player player) {
        Connection dbcon;
        PreparedStatement ps;
        boolean steamIdGifted = false;
        boolean thisYear = false;
        try {
            long steamId = Long.parseLong(String.valueOf(player.getSteamId()));
            dbcon = ModSupportDb.getModSupportDb();
            ps = dbcon.prepareStatement("SELECT * FROM RequiemCanadaDayGift");
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
                RequiemLogging.debug("Player " + player.getName() + " has not been gifted this year, giving RequiemCanadaDayGift now...");
                dbcon = ModSupportDb.getModSupportDb();
                ps = dbcon.prepareStatement("INSERT INTO RequiemCanadaDayGift (NAME,STEAMID,GIFTEDYEAR) VALUES(?,?,?)");
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
            String tableName = "RequiemCanadaDayGift";
            if (!ModSupportDb.hasTable(con, tableName)) {
                RequiemLogging.debug(tableName + " table not found in ModSupport, creating it now.");
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
            final Item mug = ItemFactory.createItem(ItemList.jarPottery, 90f, Materials.MATERIAL_POTTERY, MaterialUtilities.COMMON, null);
            final Item mapleSyrup = ItemFactory.createItem(ItemList.syrupMaple, 99f, Materials.MATERIAL_WATER, MaterialUtilities.COMMON, null);
            mapleSyrup.setWeight(2000, true);
            mug.insertItem(mapleSyrup);
            player.getInventory().insertItem(mug);
            player.getInventory().insertItem(ItemFactory.createItem(ItemList.sleepPowder, 99f, Materials.MATERIAL_WHEAT, MaterialUtilities.COMMON, null), true);
            player.getCommunicator().sendServerMessage("A shadowy figure appears behind you and your pocket gets heavier. You quickly turn around to see what is going on only to find that nobody is there.", 255, 192, 203);
        } catch (FailedException | NoSuchTemplateException e) {
            RequiemLogging.logException("Error in giveItems()", e);
            throw new RuntimeException(e);
        }
    }
}