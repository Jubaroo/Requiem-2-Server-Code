package org.jubaroo.mods.wurm.server.tools.database.holidays;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.items.*;
import com.wurmonline.server.players.Player;
import com.wurmonline.shared.util.MaterialUtilities;
import org.gotti.wurmunlimited.modsupport.ModSupportDb;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class RequiemChristmasGift {
    private static final int currentYear = Calendar.getInstance().get(Calendar.YEAR);

    public static void onPlayerLogin(Player player) {
        Connection dbcon;
        PreparedStatement ps;
        boolean steamIdGifted = false;
        boolean thisYear = false;
        try {
            long steamId = Long.parseLong(String.valueOf(player.getSteamId()));
            dbcon = ModSupportDb.getModSupportDb();
            ps = dbcon.prepareStatement("SELECT * FROM RequiemChristmasGift");
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
                RequiemLogging.logInfo("Player " + player.getName() + " has not been gifted this year, giving RequiemChristmasGift now...");
                dbcon = ModSupportDb.getModSupportDb();
                ps = dbcon.prepareStatement("INSERT INTO RequiemChristmasGift (NAME,STEAMID,GIFTEDYEAR) VALUES(?,?,?)");
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
            String tableName = "RequiemChristmasGift";
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
            // Satchel
            final Item satchel = ItemFactory.createItem(ItemList.satchel, 10f, MaterialUtilities.COMMON, null);
            satchel.setColor(WurmColor.createColor(0, 200, 0));
            // Heraldic Certificate
            for (int i = 0; i < 10; ++i)
                satchel.insertItem(ItemFactory.createItem(CustomItems.heraldicCertificateId, 99f, Materials.MATERIAL_PAPER, MaterialUtilities.COMMON, null), true);
            // Gem cache
            satchel.insertItem(ItemFactory.createItem(CustomItems.gemCache.getTemplateId(), 99f, Materials.MATERIAL_GOLD, MaterialUtilities.COMMON, null), true);
            // Note
            final Item papyrus = ItemFactory.createItem(ItemList.papyrusSheet, 10f, MaterialUtilities.COMMON, null);
            final String str = "\";maxlines=\"0\"}text{text=\"Merry Christmas!.\n\nFrom December 20 to January 4 at midnight, we are celebrating Requiem Christmas by giving away special items.\n\nWishing you and your loved ones the best of peace, health, and prosperity in this joyful Christmas season.\n\nWe hope you have a great New Year also!\n\nSee you around the server :-)\n\n \n";
            papyrus.setInscription(str, "Requiem Staff");
            satchel.insertItem(papyrus, true);
            // Insert items into players inventory
            final Item lantern = ItemFactory.createItem(ItemList.snowLantern, 99f, MaterialUtilities.RARE, "Santa Claus");
            player.getInventory().insertItem(ItemFactory.createItem(ItemList.sleepPowder, 99f, Materials.MATERIAL_WHEAT, MaterialUtilities.COMMON, null), true);
            lantern.setHasNoDecay(true);
            lantern.setDescription("Christmas" + currentYear);
            lantern.setAuxData((byte) 127);
            satchel.insertItem(lantern);
            player.getInventory().insertItem(satchel, true);
            player.getCommunicator().sendServerMessage("A shadowy figure appears behind you and you start to feel your pockets getting heavier. You quickly turn around to see what is going on only to find that nobody is there. As you wonder what could have happened just now, you pick up the smell of fresh baked cookies and hear the merry laughter of a christmas goblin as you notice something fading into the distance.", 255, 192, 203);
        } catch (FailedException | NoSuchTemplateException e) {
            RequiemLogging.logException("Error in giveItems()", e);
            throw new RuntimeException(e);
        }
    }
}