package org.jubaroo.mods.wurm.server.tools.database.holidays;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.items.*;
import com.wurmonline.server.players.Player;
import com.wurmonline.shared.util.MaterialUtilities;
import org.gotti.wurmunlimited.modsupport.ModSupportDb;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.misc.Misc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class RequiemAnniversaryGift {
    //private static final int serverBirthYear = 2016;
    private static final int serverBirthYear = 2021;
    private static final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    public static String formattedYear = Misc.formatYear(currentYear - serverBirthYear);

    public static void onPlayerLogin(Player player) {
        Connection dbcon;
        PreparedStatement ps;
        boolean steamIdGifted = false;
        boolean thisYear = false;
        try {
            long steamId = Long.parseLong(String.valueOf(player.getSteamId()));
            dbcon = ModSupportDb.getModSupportDb();
            //ps = dbcon.prepareStatement("SELECT * FROM RequiemAnniversary WHERE steamid = ?");
            //ps.setLong(1, currentYear);
            ps = dbcon.prepareStatement("SELECT * FROM RequiemAnniversary");
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
                RequiemLogging.debug(String.format("Player %s has not been gifted this year, giving RequiemAnniversaryGift now...", player.getName()));
                dbcon = ModSupportDb.getModSupportDb();
                ps = dbcon.prepareStatement("INSERT INTO RequiemAnniversary (NAME,STEAMID,GIFTEDYEAR) VALUES(?,?,?)");
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
            String tableName = "RequiemAnniversary";
            if (!ModSupportDb.hasTable(con, tableName)) {
                RequiemLogging.debug(String.format("%s table not found in ModSupport, creating it now.", tableName));
                sql = String.format("CREATE TABLE %s (NAME VARCHAR(30) NOT NULL DEFAULT 'Unknown', STEAMID LONG NOT NULL DEFAULT 0, GIFTEDYEAR INT NOT NULL DEFAULT 0)", tableName);
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
            // Pouch
            final Item pouch = ItemFactory.createItem(ItemList.satchel, 99f, MaterialUtilities.RARE, player.getName());
            pouch.setName(String.format("Requiem's %s Anniversary satchel", formattedYear));
            pouch.setDescription(String.format("Happy %s Anniversary! This satchel will never decay.", formattedYear));
            pouch.setHasNoDecay(true);
            pouch.setIsNoDrop(false);
            pouch.setSizes(3, 10, 20);
            pouch.setColor(WurmColor.createColor(210, 0, 0));
            // Essence of wood
            final int wood = CustomItems.essenceOfWoodId;
            pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_LINGONBERRY, MaterialUtilities.COMMON, player.getName()), true);
            pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_BLUEBERRY, MaterialUtilities.COMMON, player.getName()), true);
            pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_RASPBERRY, MaterialUtilities.COMMON, player.getName()), true);
            pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_APPLE, MaterialUtilities.COMMON, player.getName()), true);
            pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_ORANGE, MaterialUtilities.COMMON, player.getName()), true);
            pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_CHERRY, MaterialUtilities.COMMON, player.getName()), true);
            pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_GRAPE, MaterialUtilities.COMMON, player.getName()), true);
            pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_APPLE, MaterialUtilities.COMMON, player.getName()), true);
            // Other items
            pouch.insertItem(ItemFactory.createItem(ItemList.boneCollar, 99f, Materials.MATERIAL_BONE, MaterialUtilities.SUPREME, player.getName()), true);
            pouch.insertItem(ItemFactory.createItem(CustomItems.gemCache.getTemplateId(), 99f, Materials.MATERIAL_GOLD, MaterialUtilities.COMMON, player.getName()), true);
            // Champagne
            final Item champagne = ItemFactory.createItem(CustomItems.champagneId, 99f, Materials.MATERIAL_GLASS, MaterialUtilities.RARE, player.getName());
            champagne.setName("Requiem Vintage " + currentYear);
            pouch.insertItem(champagne, true);
            // Note
            final Item papyrus = ItemFactory.createItem(ItemList.papyrusSheet, 10f, MaterialUtilities.COMMON, player.getName());
            final String str = "\";maxlines=\"0\"}text{text=\"We want to thank you for logging into the server today.\n\nFrom December 1st through December 14th, we are celebrating Requiem of Wurm's " + formattedYear + " birthday!\n\nPlayers like you are what help make us a great server to play on with a great community since 2016.\n\nPlease enjoy your time and some special items that Zeus made just for you. \n\nHave a great day, and see you around the server!\n\n \n";
            papyrus.setInscription(str, "Requiem Staff");
            pouch.insertItem(papyrus, true);
            // Insert items into players inventory
            player.getInventory().insertItem(ItemFactory.createItem(ItemList.fireworks, 99f, Materials.MATERIAL_WOOD_BIRCH, MaterialUtilities.COMMON, null), true);
            player.getInventory().insertItem(pouch, true);
            player.getCommunicator().sendServerMessage("Some items have been placed in your inventory to help celebrate our server. Thank you for playing.", 255, 192, 203);
        } catch (FailedException | NoSuchTemplateException e) {
            RequiemLogging.logException("Error in giveItems()", e);
            throw new RuntimeException(e);
        }
    }

}