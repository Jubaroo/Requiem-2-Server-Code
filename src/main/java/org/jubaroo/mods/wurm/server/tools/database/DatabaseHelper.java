package org.jubaroo.mods.wurm.server.tools.database;

import com.wurmonline.server.DbConnector;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.Creatures;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.ModSupportDb;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.creatures.Titans;
import org.jubaroo.mods.wurm.server.items.behaviours.SupplyDepotBehaviour;
import org.jubaroo.mods.wurm.server.misc.Misc;

import java.sql.*;

public class DatabaseHelper {

    public static void steamIdDatabase(Player player) {
        boolean founded = false;
        boolean failedsteamid = false;
        try {
            final Connection dbcon = ModSupportDb.getModSupportDb();
            final PreparedStatement ps = dbcon.prepareStatement("SELECT * FROM SteamID");
            final ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (player.getSteamId().equals("")) {
                    failedsteamid = true;
                }
                if (rs.getString("name").equals(player.getName())) {
                    founded = true;
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (!founded && !failedsteamid) {
            try {
                final Connection dbcon = ModSupportDb.getModSupportDb();
                final PreparedStatement ps = dbcon.prepareStatement(String.format("INSERT INTO SteamID (name,steamid) VALUES(\"%s\",\"%s\")", player.getName(), player.getSteamId()));
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static void onPlayerLogin(Player p) {
        Connection dbcon;
        PreparedStatement ps;
        boolean foundLeaderboardOpt = false;
        try {
            dbcon = ModSupportDb.getModSupportDb();
            ps = dbcon.prepareStatement("SELECT * FROM LeaderboardOpt");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (!rs.getString("name").equals(p.getName())) continue;
                foundLeaderboardOpt = true;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (!foundLeaderboardOpt) {
            RequiemLogging.debug(String.format("No leaderboard entry for %s. Creating one.", p.getName()));
            try {
                dbcon = ModSupportDb.getModSupportDb();
                ps = dbcon.prepareStatement("INSERT INTO LeaderboardOpt (name) VALUES(?)");
                ps.setString(1, p.getName());
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        boolean foundPlayerStats = false;
        try {
            dbcon = ModSupportDb.getModSupportDb();
            ps = dbcon.prepareStatement("SELECT * FROM PlayerStats");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (!rs.getString("NAME").equals(p.getName())) continue;
                foundPlayerStats = true;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (!foundPlayerStats) {
            RequiemLogging.debug(String.format("No player stats entry for %s. Creating one.", p.getName()));
            try {
                dbcon = ModSupportDb.getModSupportDb();
                ps = dbcon.prepareStatement(String.format("INSERT INTO PlayerStats (NAME) VALUES(\"%s\")", p.getName()));
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void onServerStarted() {
        try {
            Connection con = ModSupportDb.getModSupportDb();
            String sql;
            // LeaderboardOpt
            String tableName = "LeaderboardOpt";
            if (!ModSupportDb.hasTable(con, tableName)) {
                RequiemLogging.debug(String.format("%s table not found in ModSupport. Creating table now.", tableName));
                sql = String.format("CREATE TABLE %s (name VARCHAR(30) NOT NULL DEFAULT 'Unknown', OPTIN INT NOT NULL DEFAULT 0)", tableName);
                PreparedStatement ps = con.prepareStatement(sql);
                ps.execute();
                ps.close();
            }
            // SteamIdMap
            tableName = "SteamIdMap";
            if (!ModSupportDb.hasTable(con, tableName)) {
                RequiemLogging.debug(String.format("%s table not found in ModSupport. Creating table now.", tableName));
                sql = String.format("CREATE TABLE %s (NAME VARCHAR(30) NOT NULL DEFAULT 'Unknown', STEAMID LONG NOT NULL DEFAULT 0)", tableName);
                PreparedStatement ps = con.prepareStatement(sql);
                ps.execute();
                ps.close();
            }
            // PlayerStats
            tableName = "PlayerStats";
            if (!ModSupportDb.hasTable(con, tableName)) {
                RequiemLogging.debug(String.format("%s table not found in ModSupport. Creating table now.", tableName));
                sql = String.format("CREATE TABLE %s (NAME VARCHAR(30) NOT NULL DEFAULT 'Unknown', KILLS INT NOT NULL DEFAULT 0, DEATHS INT NOT NULL DEFAULT 0, DEPOTS INT NOT NULL DEFAULT 0, HOTAS INT NOT NULL DEFAULT 0, TITANS INT NOT NULL DEFAULT 0, UNIQUES INT NOT NULL DEFAULT 0, GOBLINS INT NOT NULL DEFAULT 0)", tableName);
                PreparedStatement ps = con.prepareStatement(sql);
                ps.execute();
                ps.close();
            } else {
                RequiemLogging.debug(String.format("Found %s. Checking if it has a unique column.", tableName));
                ResultSet rs = con.getMetaData().getColumns(null, null, tableName, "UNIQUES");
                if (rs.next()) {
                    RequiemLogging.debug(String.format("%s already has a uniques column.", tableName));
                } else {
                    RequiemLogging.debug(String.format("Detected no uniques column in %s. Adding now", tableName));
                    sql = String.format("ALTER TABLE %s ADD COLUMN UNIQUES INT NOT NULL DEFAULT 0", tableName);
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.execute();
                    ps.close();
                }
            }
            // add a column for treasure goblins if it does not exist
            if (ModSupportDb.hasTable(con, tableName)) {
                RequiemLogging.debug(String.format("Found %s. Checking if it has a goblins column.", tableName));
                ResultSet rs = con.getMetaData().getColumns(null, null, tableName, "GOBLINS");
                if (rs.next()) {
                    RequiemLogging.debug(String.format("%s already has a goblins column.", tableName));
                } else {
                    RequiemLogging.debug(String.format("Detected no goblins column in %s. Adding now", tableName));
                    sql = String.format("ALTER TABLE %s ADD COLUMN GOBLINS INT NOT NULL DEFAULT 0", tableName);
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.execute();
                    ps.close();
                }
            }
            // ObjectiveTimers
            tableName = "ObjectiveTimers";
            if (!ModSupportDb.hasTable(con, tableName)) {
                RequiemLogging.debug(String.format("%s table not found in ModSupport. Creating table now.", tableName));
                sql = String.format("CREATE TABLE %s (ID VARCHAR(30) NOT NULL DEFAULT 'Unknown', TIMER LONG NOT NULL DEFAULT 0)", tableName);
                PreparedStatement ps = con.prepareStatement(sql);
                ps.execute();
                ps.close();
                try {
                    Connection dbcon;
                    dbcon = ModSupportDb.getModSupportDb();
                    ps = dbcon.prepareStatement("INSERT INTO ObjectiveTimers (ID, TIMER) VALUES(\"DEPOT\", 0)");
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException((Throwable) e);
                }
                try {
                    Connection dbcon;
                    dbcon = ModSupportDb.getModSupportDb();
                    ps = dbcon.prepareStatement("INSERT INTO ObjectiveTimers (ID, TIMER) VALUES(\"TITAN\", 0)");
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException((Throwable) e);
                }
            }
            // SteamID
            tableName = "SteamID";
            if (!ModSupportDb.hasTable(con, tableName)) {
                sql = String.format("CREATE TABLE %s (\t\tname\t\t\t\tVARCHAR(30)\t\t\tNOT NULL DEFAULT 'Unknown',\t\tsteamid\t\t\t\t\tVARCHAR(30)\t\tNOT NULL DEFAULT 'Unknown')", tableName);
                final PreparedStatement ps = con.prepareStatement(sql);
                ps.execute();
                ps.close();
            }
            SupplyDepotBehaviour.initializeDepotTimer();
            Titans.initializeTitanTimer();
        } catch (SQLException e) {
            throw new RuntimeException((Throwable) e);
        }
    }

    public static void setUniques() {
        Connection dbcon;
        PreparedStatement ps;
        Label_0102:
        {
            try {
                final Connection con = ModSupportDb.getModSupportDb();
                String sql;
                if (ModSupportDb.hasTable(con, "UniqueLocations")) {
                    try {
                        dbcon = ModSupportDb.getModSupportDb();
                        ps = dbcon.prepareStatement("DELETE FROM UniqueLocations");
                        ps.executeUpdate();
                        ps.close();
                        break Label_0102;
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                sql = "CREATE TABLE UniqueLocations (\t\tname\t\t\t\t\tVARCHAR(30)\t\t\tNOT NULL DEFAULT 'Unknown',\t\twurmid\t\t\t\t\tLONG\t\tNOT NULL DEFAULT 0,\t\tX\t\t\t\t\tFLOAT\t\tNOT NULL DEFAULT 0,\t\tY\t\t\t\t\tFLOAT\t\tNOT NULL DEFAULT 0)";
                ps = con.prepareStatement(sql);
                ps.execute();
                ps.close();
            } catch (SQLException e2) {
                throw new RuntimeException(e2);
            }
        }
        final Creature[] crets = Creatures.getInstance().getCreatures();
        for (Creature cret : crets) {
            if (cret.isUnique() || Misc.isBoss(cret)) {
                dbcon = null;
                ps = null;
                try {
                    dbcon = ModSupportDb.getModSupportDb();
                    ps = dbcon.prepareStatement("INSERT INTO UniqueLocations (name,wurmid,X,Y) VALUES(?,?,?,?)");
                    ps.setString(1, cret.getName());
                    ps.setLong(2, cret.getWurmId());
                    ps.setFloat(3, cret.getTileX());
                    ps.setFloat(4, cret.getTileY());
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void addPlayerStat(String playerName, String stat) {
        Connection dbcon;
        PreparedStatement ps;
        try {
            dbcon = ModSupportDb.getModSupportDb();
            ps = dbcon.prepareStatement(String.format("UPDATE PlayerStats SET %s = %s + 1 WHERE NAME = \"%s\"", stat, stat, playerName));
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException((Throwable) e);
        }
    }

    public static void addPlayerStatsDeath(String playerName) {
        Connection dbcon;
        PreparedStatement ps;
        try {
            dbcon = ModSupportDb.getModSupportDb();
            ps = dbcon.prepareStatement(String.format("UPDATE PlayerStats SET DEATHS = DEATHS + 1 WHERE NAME = \"%s\"", playerName));
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException((Throwable) e);
        }
    }

    public static void addPlayerStatsKill(String slayers) {
        String[] slayerNames = slayers.split(" ");
        Connection dbcon;
        PreparedStatement ps;
        try {
            dbcon = ModSupportDb.getModSupportDb();
            for (String slayer : slayerNames) {
                if (slayer.length() < 2) continue;
                ps = dbcon.prepareStatement(String.format("UPDATE PlayerStats SET KILLS = KILLS + 1 WHERE NAME = \"%s\"", slayer));
                ps.executeUpdate();
                ps.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException((Throwable) e);
        }
    }

    public static void addFiveBankSlots(long wurmId) {
        Connection dbcon;
        PreparedStatement ps;
        try {
            dbcon = DbConnector.getEconomyDbCon();
            ps = dbcon.prepareStatement(String.format("UPDATE BANKS SET SIZE = SIZE + 5 WHERE OWNER = \"%s\"", wurmId));
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException((Throwable) e);
        }
    }

    public static void createRequiemDatabase(String fileName) {
        String url = "jdbc:sqlite:F:\\Wurm Unlimited Dedicated Server\\row_release\\sqlite/" + fileName;
        //String url2 = "jdbc:sqlite:F:\Wurm Unlimited Dedicated Server\row_release\sqlite/" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                RequiemLogging.debug(String.format("The driver name is %s", meta.getDriverName()));
                RequiemLogging.debug("A new database has been created.");
            }

        } catch (SQLException e) {
            RequiemLogging.debug(e.getMessage());
        }
    }

    public static void createRequiemDatabase() {
        createRequiemDatabase("requiem.db");
    }
}
