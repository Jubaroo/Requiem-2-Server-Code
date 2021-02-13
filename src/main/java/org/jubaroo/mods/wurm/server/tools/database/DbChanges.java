package org.jubaroo.mods.wurm.server.tools.database;

import com.wurmonline.server.DbConnector;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.ModSupportDb;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbChanges {

    public static void onPlayerLogin(Player p) {
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
            RequiemLogging.logException("[Error] querying LeaderboardOpt in DbChanges", e);
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
            RequiemLogging.logException("[Error] querying PlayerStats in DbChanges", e);
            throw new RuntimeException(e);
        }

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

    public static void addPlayerStat(String playerName, String stat) {
        try (Connection con = ModSupportDb.getModSupportDb()) {
            if (!ModSupportDb.hasTable(con, "PlayerStats")) {
                try (PreparedStatement ps = con.prepareStatement(String.format("UPDATE PlayerStats SET %s = %s + 1 WHERE NAME = %s", stat, stat, playerName))) {
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            RequiemLogging.logException("[Error] in addPlayerStat in DbChanges", e);
            throw new RuntimeException(e);
        }

    }

    public static void addFiveBankSlots(long wurmId) {
        try (Connection con = DbConnector.getEconomyDbCon()) {
            try (PreparedStatement ps = con.prepareStatement(String.format("UPDATE BANKS SET SIZE = SIZE + 5 WHERE OWNER = %s", wurmId))) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            RequiemLogging.logException("[Error] in addFiveBankSlots in DbChanges", e);
            throw new RuntimeException(e);
        }
    }

}
