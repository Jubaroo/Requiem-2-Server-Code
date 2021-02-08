package org.jubaroo.mods.wurm.server.tools.database;

import com.wurmonline.server.DbConnector;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.Creatures;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.ModSupportDb;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.misc.Misc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import static org.jubaroo.mods.wurm.server.server.constants.CreatureConstants.*;

public class DatabaseHelper {
    public static int serverBirthYear = 2021;
    public static int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    public static String formattedYear = Misc.formatYear(currentYear - serverBirthYear);

    public static void onPlayerLogin(Player p) {
        long steamId = Long.parseLong(String.valueOf(p.getSteamId()));

        // ============= Leaderboard =================

        // Look for the players name in LeaderboardOpt table
        String tableName = "LeaderboardOpt";
        // Try to establish a connection with the ModSupport database and automatically close it when finished or an exception occurs
        try (Connection con = ModSupportDb.getModSupportDb()) {
            // Prepare our Statement by collecting the name of the player using a parameter
            boolean foundLeaderboardOpt = false;
            try (PreparedStatement ps = con.prepareStatement(String.format("SELECT * FROM %s WHERE NAME = ?", tableName))) {
                ps.setString(1, p.getName());
                // Use our PreparedStatement to query the table
                if (ps.executeQuery().next()) {
                    foundLeaderboardOpt = true;
                }
            }
            // If no name matching the player is found, create a new entry in the table
            if (!foundLeaderboardOpt) {
                RequiemLogging.logInfo(String.format("No player stats entry for %s. Creating one.", p.getName()));
                // Prepare our Statement by collecting the name of the player using a parameter
                try (PreparedStatement ps = con.prepareStatement(String.format("INSERT INTO %s (NAME) VALUES(?)", tableName))) {
                    ps.setString(1, p.getName());
                    // Update the table by adding a row with the players name
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            RequiemLogging.logException("[Error] querying LeaderboardOpt in DatabaseHelper", e);
            throw new RuntimeException(e);
        }

        // Look for the players name in PlayerStats table
        tableName = "PlayerStats";
        // Try to establish a connection with the ModSupport database and automatically close it when finished or an exception occurs
        try (Connection con = ModSupportDb.getModSupportDb()) {
            // Prepare our Statement by collecting the name of the player using a parameter
            boolean foundPlayerStats = false;
            try (PreparedStatement ps = con.prepareStatement(String.format("SELECT * FROM %s WHERE NAME = ?", tableName))) {
                ps.setString(1, p.getName());
                // Use our PreparedStatement to query the table
                if (ps.executeQuery().next()) {
                    foundPlayerStats = true;
                }
            }
            // If no name matching the player is found, create a new entry in the table
            if (!foundPlayerStats) {
                RequiemLogging.logInfo(String.format("No player stats entry for %s. Creating one.", p.getName()));
                // Prepare our Statement by collecting the name of the player using a parameter
                try (PreparedStatement ps = con.prepareStatement(String.format("INSERT INTO %s (NAME) VALUES(?)", tableName))) {
                    ps.setString(1, p.getName());
                    // Update the table by adding a row with the players name
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            RequiemLogging.logException("[Error] querying PlayerStats in DatabaseHelper", e);
            throw new RuntimeException(e);
        }

        // ============= Requiem Gifts =================
/*
        //TODO finish me
        boolean steamIdGifted = false;
        boolean thisYear = false;

        // Look for the players steamId and giftedYear in RequiemAnniversary table
        tableName = "RequiemAnniversary";
        // Try to establish a connection with the ModSupport database and automatically close it when finished or an exception occurs
        try (Connection con = ModSupportDb.getModSupportDb()) {
            boolean foundPlayerStats = false;
            // Prepare our Statement by collecting the name and steamID of the player and the current time using parameters
            try (PreparedStatement ps = con.prepareStatement(String.format("SELECT * FROM %s WHERE NAME = ?", tableName))) {
                ps.setString(1, p.getName());
                ps.setLong(2, steamId);
                ps.setLong(3, steamId);
                ps.setLong(4, System.currentTimeMillis());
                // Use our PreparedStatement to query the table
                if (ps.executeQuery().next()) {
                    foundPlayerStats = true;
                }
            }
            // If no name matching the player is found, create a new entry in the table
            if (!foundPlayerStats) {
                RequiemLogging.logInfo(String.format("No player stats entry for %s. Creating one.", p.getName()));
                // Prepare our Statement by collecting the name of the player using a parameter
                try (PreparedStatement ps = con.prepareStatement(String.format("INSERT INTO %s (NAME) VALUES(?)", tableName))) {
                    ps.setString(1, p.getName());
                    // Update the table by adding a row with the players name
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            RequiemLogging.logException("[Error] querying PlayerStats in DatabaseHelper", e);
            throw new RuntimeException(e);
        }


        tableName = "RequiemAnniversary";
        try (Connection con = ModSupportDb.getModSupportDb()) {
            try (PreparedStatement ps = con.prepareStatement(String.format("SELECT * FROM %s", tableName))) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getLong("STEAMID") == steamId & rs.getInt("GIFTEDYEAR") == currentYear) {
                        steamIdGifted = true;
                        thisYear = true;
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            RequiemLogging.logException(String.format("[Error] querying %s", tableName), e);
            throw new RuntimeException(e);
        }
        if (!steamIdGifted & !thisYear) {
            try (Connection con = ModSupportDb.getModSupportDb()) {
                RequiemLogging.logInfo(String.format("Player %s has not been gifted this year, giving RequiemAnniversaryGift now...", p.getName()));
                try (PreparedStatement ps = con.prepareStatement(String.format("INSERT INTO %s (NAME,STEAMID,GIFTEDYEAR) VALUES(?,?,?)", tableName))) {
                    ps.setString(1, p.getName());
                    ps.setLong(2, steamId);
                    ps.setInt(3, currentYear);
                    ps.executeUpdate();
                    try {
                        // Pouch
                        final Item pouch = ItemFactory.createItem(ItemList.satchel, 99f, MaterialUtilities.RARE, p.getName());
                        pouch.setName(String.format("Requiem's %s Anniversary satchel", formattedYear));
                        pouch.setDescription(String.format("Happy %s Anniversary! This satchel will never decay.", formattedYear));
                        pouch.setHasNoDecay(true);
                        pouch.setIsNoDrop(false);
                        pouch.setSizes(3, 10, 20);
                        pouch.setColor(WurmColor.createColor(210, 0, 0));
                        // Essence of wood
                        final int wood = CustomItems.essenceOfWoodId;
                        pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_LINGONBERRY, MaterialUtilities.COMMON, p.getName()), true);
                        pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_BLUEBERRY, MaterialUtilities.COMMON, p.getName()), true);
                        pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_RASPBERRY, MaterialUtilities.COMMON, p.getName()), true);
                        pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_APPLE, MaterialUtilities.COMMON, p.getName()), true);
                        pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_ORANGE, MaterialUtilities.COMMON, p.getName()), true);
                        pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_CHERRY, MaterialUtilities.COMMON, p.getName()), true);
                        pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_GRAPE, MaterialUtilities.COMMON, p.getName()), true);
                        pouch.insertItem(ItemFactory.createItem(wood, 99f, Materials.MATERIAL_WOOD_APPLE, MaterialUtilities.COMMON, p.getName()), true);
                        // Other items
                        pouch.insertItem(ItemFactory.createItem(ItemList.boneCollar, 99f, Materials.MATERIAL_BONE, MaterialUtilities.SUPREME, p.getName()), true);
                        pouch.insertItem(ItemFactory.createItem(CustomItems.gemCache.getTemplateId(), 99f, Materials.MATERIAL_GOLD, MaterialUtilities.COMMON, p.getName()), true);
                        // Champagne
                        final Item champagne = ItemFactory.createItem(CustomItems.champagneId, 99f, Materials.MATERIAL_GLASS, MaterialUtilities.RARE, p.getName());
                        champagne.setName(String.format("Requiem Vintage %d", currentYear));
                        pouch.insertItem(champagne, true);
                        // Note
                        final Item papyrus = ItemFactory.createItem(ItemList.papyrusSheet, 10f, MaterialUtilities.COMMON, p.getName());
                        final String str = String.format("\";maxlines=\"0\"}text{text=\"We want to thank you for logging into the server today.\n\nFrom December 1st through December 14th, we are celebrating Requiem of Wurm's %s birthday!\n\nPlayers like you are what help make us a great server to play on with a great community since 2016.\n\nPlease enjoy your time and some special items that Zeus made just for you. \n\nHave a great day, and see you around the server!\n\n \n", formattedYear);
                        papyrus.setInscription(str, "Requiem Staff");
                        pouch.insertItem(papyrus, true);
                        // Insert items into players inventory
                        p.getInventory().insertItem(ItemFactory.createItem(ItemList.fireworks, 99f, Materials.MATERIAL_WOOD_BIRCH, MaterialUtilities.COMMON, null), true);
                        p.getInventory().insertItem(pouch, true);
                        p.getCommunicator().sendServerMessage("Some items have been placed in your inventory to help celebrate our server. Thank you for playing.", 255, 192, 203);
                    } catch (FailedException | NoSuchTemplateException e) {
                        RequiemLogging.logException("[Error] giving items for RequiemAnniversary", e);
                        throw new RuntimeException(e);
                    }
                }
            } catch (SQLException e) {
                RequiemLogging.logException("[Error] querying RequiemAnniversary", e);
                throw new RuntimeException(e);
            }
        }

        steamIdGifted = false;
        thisYear = false;

        tableName = "RequiemCanadaDayGift";
        try (Connection con = ModSupportDb.getModSupportDb()) {
            try (PreparedStatement ps = con.prepareStatement(String.format("SELECT * FROM %s", tableName))) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getLong("STEAMID") == steamId & rs.getInt("GIFTEDYEAR") == currentYear) {
                        steamIdGifted = true;
                        thisYear = true;
                        break;
                    }
                }
            }
            if (!steamIdGifted & !thisYear) {
                RequiemLogging.logInfo(String.format("Player %s has not been gifted this year, giving RequiemAnniversaryGift now...", p.getName()));
                try (PreparedStatement ps = con.prepareStatement(String.format("INSERT INTO %s (NAME,STEAMID,GIFTEDYEAR) VALUES(?,?,?)", tableName))) {
                    ps.setString(1, p.getName());
                    ps.setLong(2, steamId);
                    ps.setInt(3, currentYear);
                    ps.executeUpdate();
                    try {
                        final Item mug = ItemFactory.createItem(ItemList.jarPottery, 90f, Materials.MATERIAL_POTTERY, MaterialUtilities.COMMON, null);
                        final Item mapleSyrup = ItemFactory.createItem(ItemList.syrupMaple, 99f, Materials.MATERIAL_WATER, MaterialUtilities.COMMON, null);
                        mapleSyrup.setWeight(2000, true);
                        mug.insertItem(mapleSyrup);
                        p.getInventory().insertItem(mug);
                        p.getInventory().insertItem(ItemFactory.createItem(ItemList.sleepPowder, 99f, Materials.MATERIAL_WHEAT, MaterialUtilities.COMMON, null), true);
                        p.getCommunicator().sendServerMessage("A shadowy figure appears behind you and your pocket gets heavier. You quickly turn around to see what is going on only to find that nobody is there.", 255, 192, 203);
                    } catch (FailedException | NoSuchTemplateException e) {
                        RequiemLogging.logException(String.format("[Error] in giving items for %s", tableName), e);
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (SQLException e) {
            RequiemLogging.logException("[Error] in action in ScrollOfVillageCreateAction", e);
        }
 */
    }

    public static void onServerStarted() {

        // Add LeaderboardOpt table
        String tableName = "LeaderboardOpt";
        try (Connection con = ModSupportDb.getModSupportDb()) {
            if (!ModSupportDb.hasTable(con, tableName)) {
                RequiemLogging.logInfo(String.format("%s table not found in ModSupport. Creating table now.", tableName));
                try (PreparedStatement ps = con.prepareStatement(String.format("CREATE TABLE %s (name VARCHAR(30) NOT NULL DEFAULT 'Unknown', OPTIN INT NOT NULL DEFAULT 0)", tableName))) {
                    ps.execute();
                }
            }
        } catch (SQLException e) {
            RequiemLogging.logException(String.format("[Error] creating %s table in ModSupport.db", tableName), e);
            throw new RuntimeException(e);
        }

        // Add SteamIdMap table
        tableName = "SteamIdMap";
        try (Connection con = ModSupportDb.getModSupportDb()) {
            if (!ModSupportDb.hasTable(con, tableName)) {
                RequiemLogging.logInfo(String.format("%s table not found in ModSupport. Creating table now.", tableName));
                try (PreparedStatement ps = con.prepareStatement(String.format("CREATE TABLE %s (NAME VARCHAR(30) NOT NULL DEFAULT 'Unknown', STEAMID LONG NOT NULL DEFAULT 0)", tableName))) {
                    ps.execute();
                }
            }
        } catch (SQLException e) {
            RequiemLogging.logException(String.format("[Error] creating %s table in ModSupport.db", tableName), e);
            throw new RuntimeException(e);
        }

        // Add PlayerStats table
        tableName = "PlayerStats";
        try (Connection con = ModSupportDb.getModSupportDb()) {
            if (!ModSupportDb.hasTable(con, tableName)) {
                RequiemLogging.logInfo(String.format("%s table not found in ModSupport. Creating table now.", tableName));
                try (PreparedStatement ps = con.prepareStatement(String.format("CREATE TABLE %s (NAME VARCHAR(30) NOT NULL DEFAULT 'Unknown', KILLS INT NOT NULL DEFAULT 0, DEATHS INT NOT NULL DEFAULT 0, DEPOTS INT NOT NULL DEFAULT 0, HOTAS INT NOT NULL DEFAULT 0, TITANS INT NOT NULL DEFAULT 0, UNIQUES INT NOT NULL DEFAULT 0, GOBLINS INT NOT NULL DEFAULT 0)", tableName))) {
                    ps.execute();
                }
            }
        } catch (SQLException e) {
            RequiemLogging.logException(String.format("[Error] creating %s table in ModSupport.db", tableName), e);
            throw new RuntimeException(e);
        }

        // Add ObjectiveTimers table
        tableName = "ObjectiveTimers";
        try (Connection con = ModSupportDb.getModSupportDb()) {
            if (!ModSupportDb.hasTable(con, tableName)) {
                RequiemLogging.logInfo(String.format("%s table not found in ModSupport. Creating table now.", tableName));
                try (PreparedStatement ps = con.prepareStatement(String.format("CREATE TABLE %s (ID VARCHAR(30) NOT NULL DEFAULT 'Unknown', TIMER LONG NOT NULL DEFAULT 0)", tableName))) {
                    ps.execute();
                }
                try (PreparedStatement ps = con.prepareStatement("INSERT INTO ObjectiveTimers (ID, TIMER) VALUES(DEPOT, 0)")) {
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement("INSERT INTO ObjectiveTimers (ID, TIMER) VALUES(TITAN, 0)")) {
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            RequiemLogging.logException(String.format("[Error] creating %s table in ModSupport.db", tableName), e);
            throw new RuntimeException(e);
        }

        // Add SteamID table
        tableName = "SteamID";
        try (Connection con = ModSupportDb.getModSupportDb()) {
            if (!ModSupportDb.hasTable(con, tableName)) {
                RequiemLogging.logInfo(String.format("%s table not found in ModSupport. Creating table now.", tableName));
                try (PreparedStatement ps = con.prepareStatement(String.format("CREATE TABLE %s (name VARCHAR(30) NOT NULL DEFAULT 'Unknown', steamid VARCHAR(30) NOT NULL DEFAULT 'Unknown')", tableName))) {
                    ps.execute();
                }
            }
        } catch (SQLException e) {
            RequiemLogging.logException(String.format("[Error] creating %s table in ModSupport.db", tableName), e);
            throw new RuntimeException(e);
        }

    }

    // Create the RequiemHolidays Table
    private static void holidayDatabaseTableCreator() {
        String tableName = "RequiemHolidays";
        try (Connection con = ModSupportDb.getModSupportDb()) {
            if (!ModSupportDb.hasTable(con, tableName)) {
                RequiemLogging.logInfo(String.format("%s table not found in ModSupport. Creating table now.", tableName));
                try (PreparedStatement ps = con.prepareStatement(String.format("CREATE TABLE %s (NAME VARCHAR(30) NOT NULL DEFAULT 'Unknown', STEAMID LONG NOT NULL DEFAULT 0, ANNIVERSARY LONG NOT NULL DEFAULT 0, CANADADAY LONG NOT NULL DEFAULT 0, CHRISTMAS LONG NOT NULL DEFAULT 0, INDEPENDENCEDAY LONG NOT NULL DEFAULT 0, NEWYEAR LONG NOT NULL DEFAULT 0, STPATRICKSDAY LONG NOT NULL DEFAULT 0, THANKSGIVING LONG NOT NULL DEFAULT 0, VALENTINESDAY LONG NOT NULL DEFAULT 0, VICTORIADAY LONG NOT NULL DEFAULT 0)", tableName))) {
                    ps.execute();
                }
            }
        } catch (SQLException e) {
            RequiemLogging.logException(String.format("[Error] creating %s table in ModSupport.db", tableName), e);
            throw new RuntimeException(e);
        }

    }

    public static void setUniques() {
        String tableName = "UniqueLocations";
        String sql = String.format("CREATE TABLE %s (name VARCHAR(30) NOT NULL DEFAULT 'Unknown', wurmid LONG NOT NULL DEFAULT 0, X FLOAT NOT NULL DEFAULT 0, Y FLOAT NOT NULL DEFAULT 0)", tableName);
        final Creature[] crets = Creatures.getInstance().getCreatures();
        try (Connection con = ModSupportDb.getModSupportDb()) {
            if (ModSupportDb.hasTable(con, tableName)) {
                try (PreparedStatement ps = con.prepareStatement(String.format("DELETE FROM %s)", tableName))) {
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.execute();
                }
                for (Creature cret : crets) {
                    if (cret.isUnique()/* || Misc.isBoss(cret)*/) {
                        try (PreparedStatement ps = con.prepareStatement(String.format("INSERT INTO %s (name,wurmid,X,Y) VALUES(?,?,?,?)", tableName))) {
                            ps.execute();
                        }
                    }
                }
            } else {
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.execute();
                }
                for (Creature cret : crets) {
                    if (cret.isUnique()/* || Misc.isBoss(cret)*/) {
                        try (PreparedStatement ps = con.prepareStatement(String.format("INSERT INTO %s (name,wurmid,X,Y) VALUES(?,?,?,?)", tableName))) {
                            ps.execute();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            RequiemLogging.logException("[Error] in setUniques in DatabaseHelper", e);
            throw new RuntimeException(e);
        }

    }

    public static int skullActionDatabaseGetter(Creature performer) throws SQLException {
        int smallestDistance = 100000000;
        try (Connection con = ModSupportDb.getModSupportDb()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM UniqueLocations")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if (performer.getPower() > MiscConstants.POWER_HIGH_GOD) {
                        performer.getCommunicator().sendNormalServerMessage(String.format("%s %s %s", rs.getString("name"), rs.getFloat("X"), rs.getFloat("Y")));
                    }
                    final int xDistance = (int) Math.abs(performer.getTileX() - rs.getFloat("X"));
                    final int yDistance = (int) Math.abs(performer.getTileY() - rs.getFloat("Y"));
                    final int distance = (int) Math.sqrt(xDistance * xDistance + yDistance * yDistance);
                    if (distance < smallestDistance) {
                        uniqueX = rs.getFloat("X");
                        uniqueY = rs.getFloat("Y");
                        uniqueName = rs.getString("name");
                        smallestDistance = distance;
                    }
                }
            }
        } catch (SQLException e) {
            RequiemLogging.logException("[Error] in skullActionDatabaseGetter in DatabaseHelper", e);
            throw new RuntimeException(e);
        }

        return smallestDistance;
    }

    public static void addPlayerStat(String playerName, String stat) {
        try (Connection con = ModSupportDb.getModSupportDb()) {
            if (!ModSupportDb.hasTable(con, "PlayerStats")) {
                try (PreparedStatement ps = con.prepareStatement(String.format("UPDATE PlayerStats SET %s = %s + 1 WHERE NAME = %s", stat, stat, playerName))) {
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            RequiemLogging.logException("[Error] in addPlayerStat in DatabaseHelper", e);
            throw new RuntimeException(e);
        }

    }

    public static void addFiveBankSlots(long wurmId) {
        try (Connection con = DbConnector.getEconomyDbCon()) {
            try (PreparedStatement ps = con.prepareStatement(String.format("UPDATE BANKS SET SIZE = SIZE + 5 WHERE OWNER = %s", wurmId))) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            RequiemLogging.logException("[Error] in addFiveBankSlots in DatabaseHelper", e);
            throw new RuntimeException(e);
        }
    }

}
