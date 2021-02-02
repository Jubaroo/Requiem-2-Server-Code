package com.wurmonline.server.questions;

import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.NoSuchActionException;
import com.wurmonline.server.creatures.Creature;
import net.coldie.tools.BmlForm;
import org.gotti.wurmunlimited.modsupport.ModSupportDb;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.actions.portals.PortalAction;
import org.jubaroo.mods.wurm.server.items.pottals.PortalMod;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static org.jubaroo.mods.wurm.server.ModConfig.costPerMin;

public class PortalQuestion extends Question {
    private boolean properlySent = false;

    PortalQuestion(Creature aResponder, String aTitle, String aQuestion, int aType, long aTarget) {
        super(aResponder, aTitle, aQuestion, aType, aTarget);
    }

    public PortalQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
        super(aResponder, aTitle, aQuestion, LOCATEQUESTION, aTarget);
    }

    public void answer(Properties answer) {
        if (!properlySent) {
            return;
        }
        if (Integer.parseInt(answer.getProperty("portalchoice")) == 0) {
            return;
        }

        // check drop down and accepted
        boolean accepted = (answer.containsKey("accept")) && (answer.get("accept") == "true");
        float tx = 500;
        float ty = 500;
        String name = "";

        if (accepted) {

            String mynumber = answer.getProperty("portalchoice");
            Connection dbcon2;
            PreparedStatement ps2;
            ResultSet rs2;
            try {
                dbcon2 = ModSupportDb.getModSupportDb();
                ps2 = dbcon2.prepareStatement("SELECT * FROM RequiemGMPortals");
                rs2 = ps2.executeQuery();

                while (rs2.next()) {
                    if (rs2.getString("name").equals(PortalMod.myMap.get(mynumber))) {
                        name = PortalMod.myMap.get(mynumber);
                        tx = rs2.getFloat("posx");
                        ty = rs2.getFloat("posy");
                    }
                }
                rs2.close();
                ps2.close();
                dbcon2.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            Connection dbcon;
            PreparedStatement ps;
            ResultSet rs;
            try {
                dbcon = ModSupportDb.getModSupportDb();
                ps = dbcon.prepareStatement("SELECT * FROM RequiemPortals");
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getString("name").equals(PortalMod.myMap.get(mynumber))) {
                        name = PortalMod.myMap.get(mynumber);
                        tx = rs.getFloat("posx");
                        ty = rs.getFloat("posy");
                    }
                }
                rs.close();
                ps.close();
                dbcon.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (name.equals("")) {
                return;
            }
            tx = (tx * 4);
            ty = (ty * 4);
            int layer = 0;
            int floorLevel = getResponder().getFloorLevel();
            getResponder().getCommunicator().sendNormalServerMessage(String.format("Teleporting to: %s %s, %s", name, tx / 4, ty / 4));
            getResponder().setTeleportPoints(tx, ty, layer, floorLevel);
            getResponder().startTeleporting();
            getResponder().getCommunicator().sendNormalServerMessage("You feel a slight tingle in your spine.");
            getResponder().getCommunicator().sendTeleport(false);
            getResponder().teleport(true);
            getResponder().stopTeleporting();
        }

    }

    public void sendQuestion() {
        boolean ok;
        try {
            ok = false;
            Action act = getResponder().getCurrentAction();
            if (act.getNumber() == PortalAction.actionId) {
                ok = true;
            }
        } catch (NoSuchActionException act) {
            throw new RuntimeException("No such action", act);
        }

        if (ok) {
            properlySent = true;

            BmlForm f = new BmlForm("");
            f.addHidden("id", String.format("%d", id));
            f.addBoldText(getQuestion());
            f.addText("\n ");
            f.addRaw("harray{label{text='Current portal location choices'}dropdown{id='portalchoice';options=\"");
            // use table data
            Connection dbcon2;
            PreparedStatement ps2;
            ResultSet rs2;
            PortalMod.myMap.clear();
            int i = 1;
            try {
                dbcon2 = ModSupportDb.getModSupportDb();
                ps2 = dbcon2.prepareStatement("SELECT * FROM RequiemGMPortals ORDER BY name");
                rs2 = ps2.executeQuery();
                f.addRaw("--Choose Destination--,");

                while (rs2.next()) {
                    PortalMod.myMap.put(String.format("%d", i), rs2.getString("name"));
                    f.addRaw(String.format("*%s*", rs2.getString("name")));
                    f.addRaw(",");
                    i = i + 1;
                }
                rs2.close();
                ps2.close();
                dbcon2.close();
            } catch (SQLException e) {
                RequiemLogging.logException("[Error] in sendQuestion in PortalAction", e);
                throw new RuntimeException(e);
            }

            Connection dbcon;
            PreparedStatement ps;
            ResultSet rs;
            try {
                dbcon = ModSupportDb.getModSupportDb();
                ps = dbcon.prepareStatement("SELECT * FROM RequiemPortals WHERE bank >= ? ORDER BY name");
                ps.setInt(1, costPerMin * 60);//updates every hour
                rs = ps.executeQuery();
                while (rs.next()) {
                    PortalMod.myMap.put(String.format("%d", i), rs.getString("name"));
                    f.addRaw(rs.getString("name"));
                    f.addRaw(",");
                    i = i + 1;
                }
                rs.close();
                ps.close();
                dbcon.close();
            } catch (SQLException e) {

                throw new RuntimeException(e);
            }

            f.addRaw("\"}}");

            f.addText("\n\n\n\n ");
            f.beginHorizontalFlow();
            f.addButton("Portal now", "accept");
            f.addText("               ");
            f.addButton("Cancel", "decline");

            f.endHorizontalFlow();
            f.addText(" \n");
            f.addText(" \n");

            getResponder().getCommunicator().sendBml(
                    300,
                    250,
                    true,
                    true,
                    f.toString(),
                    150,
                    150,
                    200,
                    title);
        }
    }

}