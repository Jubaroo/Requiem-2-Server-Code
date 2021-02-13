package org.jubaroo.mods.wurm.server.tools.database;

import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.players.Player;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by bdew on 2/12/2021
 */

public class DbHelper {
    static final long PRESENT_COOLDOWN = TimeConstants.WEEK_MILLIS * 50;
    static final long PATREON_COOLDOWN = TimeConstants.MONTH_MILLIS;
    static String user = "root";
    static String pass = "Bvhfu4!b5y4u!";

    private static Connection mysqlConnection;

    private static void ensureDBOpen() {
        if (mysqlConnection == null) {
            try {
                mysqlConnection = DriverManager.getConnection(String.format("jdbc:mysql://localhost:3306/test?user=%s&password=%s", user, pass));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean canReceiveHolidayPresent(Player p, String holidayName) {
        ensureDBOpen();
        try (
                PreparedStatement psCreate = mysqlConnection.prepareStatement("insert ignore into RequiemHolidays (STEAMID,HOLIDAY,TIMER) values (?,?,?)");
                PreparedStatement psUpdate = mysqlConnection.prepareStatement("update RequiemHolidays set TIMER=? where STEAMID=? and HOLIDAY=? and TIMER+?<?")
        ) {
            psCreate.setLong(1, p.getSteamId().getSteamID64());
            psCreate.setString(2, holidayName);
            psCreate.setLong(3, Long.MIN_VALUE);
            psCreate.execute();
            psUpdate.setLong(1, System.currentTimeMillis());
            psUpdate.setLong(2, p.getSteamId().getSteamID64());
            psUpdate.setString(3, holidayName);
            psUpdate.setLong(4, PRESENT_COOLDOWN);
            psUpdate.setLong(5, System.currentTimeMillis());
            return psUpdate.executeUpdate() > 0;
        } catch (SQLException e) {
            RequiemLogging.logException("[Error] in canReceiveHolidayPresent in DbHelper", e);
            throw new RuntimeException(e);
        }
    }

    public static boolean canReceiveMonthlyGift(Player p, String giftName) {
        ensureDBOpen();
        try (
                PreparedStatement psCreate = mysqlConnection.prepareStatement("insert ignore into RequiemHolidays (STEAMID,HOLIDAY,TIMER) values (?,?,?)");
                PreparedStatement psUpdate = mysqlConnection.prepareStatement("update RequiemHolidays set TIMER=? where STEAMID=? and HOLIDAY=? and TIMER+?<?")
        ) {
            psCreate.setLong(1, p.getSteamId().getSteamID64());
            psCreate.setString(2, giftName);
            psCreate.setLong(3, Long.MIN_VALUE);
            psCreate.execute();
            psUpdate.setLong(1, System.currentTimeMillis());
            psUpdate.setLong(2, p.getSteamId().getSteamID64());
            psUpdate.setString(3, giftName);
            psUpdate.setLong(4, PATREON_COOLDOWN);
            psUpdate.setLong(5, System.currentTimeMillis());
            return psUpdate.executeUpdate() > 0;
        } catch (SQLException e) {
            RequiemLogging.logException("[Error] in canReceiveMonthlyGift in DbHelper", e);
            throw new RuntimeException(e);
        }
    }

}
