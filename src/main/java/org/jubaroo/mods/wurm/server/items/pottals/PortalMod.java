package org.jubaroo.mods.wurm.server.items.pottals;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import org.gotti.wurmunlimited.modsupport.ModSupportDb;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class PortalMod {
    public static HashMap<String, String> myMap = new HashMap<>();
    public static HashMap<String, String> myMapBank = new HashMap<>();
    public static HashMap<String, String> myMapGM1 = new HashMap<>();
    public static HashMap<String, String> myMapGM2 = new HashMap<>();
    public static HashMap<String, Long> myMapPortal = new HashMap<>();

    public static boolean checkAction(Item target) {
        int clicked = target.getTemplateId();
        return clicked == CustomItems.serverPortal.getTemplateId() || clicked == CustomItems.hugeServerPortal.getTemplateId() ||
                clicked == CustomItems.steelServerPortal.getTemplateId() || clicked == CustomItems.darkCrystalServerPortal.getTemplateId() ||
                clicked == CustomItems.crystalServerPortal.getTemplateId();
    }

    public static boolean checkGM(Creature performer) {
        return performer.getPower() >= MiscConstants.POWER_HIGH_GOD;
    }

    public static void onServerStarted() {
        try(Connection con = ModSupportDb.getModSupportDb()) {
            if (!ModSupportDb.hasTable(con, "RequiemPortals")) {
                String sql = "CREATE TABLE RequiemPortals (\t\tname\t\t\t\t\tVARCHAR(20)\t\tNOT NULL DEFAULT 'Unknown',"
                        + "\t\tposx\t\t\t\t\tINT\t\tNOT NULL DEFAULT 100,"
                        + "\t\tposy\t\t\t\t\tINT\t\tNOT NULL DEFAULT 100,"
                        + "\t\tbank\t\t\t\t\tINT\t\tNOT NULL DEFAULT 0,"
                        + "\t\titemid\t\t\t\t\tLONG\t\tNOT NULL DEFAULT 0)";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.execute();
                }
            }
        } catch (SQLException e) {
            RequiemLogging.logException("Error creating RequiemPortals table in ModSupport.db", e.getCause());
            throw new RuntimeException(e);
        }

        try {
            Connection con2 = ModSupportDb.getModSupportDb();
            String sql2;
            if (!ModSupportDb.hasTable(con2, "RequiemGMPortals")) {
                sql2 = "CREATE TABLE RequiemGMPortals (\t\tname\t\t\t\t\tVARCHAR(20)\t\tNOT NULL DEFAULT 'Unknown',"
                        + "\t\tposx\t\t\t\t\tINT\t\tNOT NULL DEFAULT 100,"
                        + "\t\tposy\t\t\t\t\tINT\t\tNOT NULL DEFAULT 100,"
                        + "\t\tbank\t\t\t\t\tINT\t\tNOT NULL DEFAULT 0,"
                        + "\t\titemid\t\t\t\t\tLONG\t\tNOT NULL DEFAULT 0)";
                PreparedStatement ps2 = con2.prepareStatement(sql2);
                ps2.execute();
                ps2.close();
                con2.close();
            }
        } catch (SQLException e) {
            RequiemLogging.logException("Error creating RequiemGMPortals table in ModSupport.db", e.getCause());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}