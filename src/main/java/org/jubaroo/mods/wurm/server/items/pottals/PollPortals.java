package org.jubaroo.mods.wurm.server.items.pottals;

import org.gotti.wurmunlimited.modsupport.ModSupportDb;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@SuppressWarnings("SpellCheckingInspection")
public class PollPortals {
    static long lastPoll = 0L;
    static int pollFrequency = (60 * 60 * 1000);//1 hour

    public static void pollportal() {
        if (lastPoll + pollFrequency > System.currentTimeMillis()) {
            return;
        }
        lastPoll = System.currentTimeMillis();
        Connection dbcon2;
        PreparedStatement ps2;
        try {
            dbcon2 = ModSupportDb.getModSupportDb();
            ps2 = dbcon2.prepareStatement("UPDATE ColdiePortals SET bank = bank - ? WHERE bank >= ?");
            ps2.setInt(1, PortalMod.costPerMin * 60);//updates every hour
            ps2.setInt(2, PortalMod.costPerMin * 60);
            ps2.executeUpdate();
            ps2.close();
            dbcon2.close();
            RequiemLogging.logInfo("Updated player portal upkeeps");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}