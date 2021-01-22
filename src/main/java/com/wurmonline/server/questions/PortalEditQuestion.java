package com.wurmonline.server.questions;

import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.NoSuchActionException;
import com.wurmonline.server.creatures.Creature;
import net.coldie.tools.BmlForm;
import org.gotti.wurmunlimited.modsupport.ModSupportDb;
import org.jubaroo.mods.wurm.server.actions.portals.PortalEditAction;
import org.jubaroo.mods.wurm.server.items.pottals.PortalMod;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class PortalEditQuestion extends Question {
    private boolean properlySent = false;

    PortalEditQuestion(Creature aResponder, String aTitle, String aQuestion, int aType, long aTarget) {
        super(aResponder, aTitle, aQuestion, aType, aTarget);
    }

    public PortalEditQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
        super(aResponder, aTitle, aQuestion, 79, aTarget);
    }

    public void answer(Properties answer) {
        if (!properlySent) {
            return;
        }
        String mynumberGM = answer.getProperty("portalchoiceGM");
        String mynumber = answer.getProperty("portalchoice");
        String name = "";
        String dbname = "";
        if (mynumberGM.equals("0") && mynumber.equals("0")) {
            getResponder().getCommunicator().sendNormalServerMessage("you didn't pick a portal");
            return;
        }
        if (mynumberGM.equals("0")) {
            name = PortalMod.myMapGM1.get(mynumber);
            dbname = "RequiemPortals";
        }
        if (mynumber.equals("0")) {
            name = PortalMod.myMapGM2.get(mynumberGM);
            dbname = "RequiemGMPortals";
        }
        if (dbname.equals("")) {
            getResponder().getCommunicator().sendNormalServerMessage("Just pick 1 portal, not 2");
            return;
        }

        // check drop down and accepted
        boolean accepted = (answer.containsKey("accept")) && (answer.get("accept") == "true");
        if (accepted) {
            //edit selection

            Connection dbcon2 = null;
            PreparedStatement ps2 = null;
            try {
                dbcon2 = ModSupportDb.getModSupportDb();
                ps2 = dbcon2.prepareStatement("UPDATE ? SET posx = ? , posy = ? WHERE name = '" + name + "'");
                ps2.setString(1, dbname);
                ps2.setInt(2, (int) answer.get("xcoords"));
                ps2.setInt(3, (int) answer.get("ycoords"));
                //ps2.setString(4, name);
                ps2.executeUpdate();
                ps2.close();
                dbcon2.close();
                getResponder().getCommunicator().sendNormalServerMessage("Edited portal " + name);
                logger.info(getResponder().getName() + " edited portal " + name);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else {
            //delete selection

            Connection dbcon2 = null;
            PreparedStatement ps2 = null;
            try {
                //getResponder().getCommunicator().sendNormalServerMessage("Deleted portal "+ name + "from db "+dbname);
                dbcon2 = ModSupportDb.getModSupportDb();
                ps2 = dbcon2.prepareStatement("DELETE FROM " + dbname + " WHERE name = '" + name + "'");
                //ps2.setString(1, dbname);
                //ps2.setString(2, name);
                ps2.executeUpdate();
                ps2.close();
                dbcon2.close();
                getResponder().getCommunicator().sendNormalServerMessage("Deleted portal " + name);
                logger.info(getResponder().getName() + " deleted portal " + name);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void sendQuestion() {
        boolean ok = true;
        try {
            ok = false;
            Action act = getResponder().getCurrentAction();
            if (act.getNumber() == PortalEditAction.actionId) {
                ok = true;
            }
        } catch (NoSuchActionException act) {
            throw new RuntimeException("No such action", act);
        }

        if (ok) {
            properlySent = true;

            BmlForm f = new BmlForm("");

            f.addHidden("id", id + "");
            f.addBoldText(getQuestion(), new String[0]);
            f.addText("\n ", new String[0]);

            //drop down option
            f.addRaw("harray{label{text='Current GM portal location choices'}dropdown{id='portalchoiceGM';options=\"");
            // use table data

            Connection dbcon;
            PreparedStatement ps;
            ResultSet rs;
            PortalMod.myMapGM2.clear();
            try {
                dbcon = ModSupportDb.getModSupportDb();
                ps = dbcon.prepareStatement("SELECT * FROM RequiemGMPortals ORDER BY name");
                rs = ps.executeQuery();
                int i = 1;
                f.addRaw("--Choose Portal--,");
                while (rs.next()) {
                    PortalMod.myMapGM2.put(i + "", rs.getString("name"));
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
            f.addText("\n");

            f.addRaw("harray{label{text='Current portal location choices'}dropdown{id='portalchoice';options=\"");
            // use table data

            Connection dbcon2;
            PreparedStatement ps2;
            ResultSet rs2;
            PortalMod.myMapGM1.clear();
            try {
                dbcon2 = ModSupportDb.getModSupportDb();
                ps2 = dbcon2.prepareStatement("SELECT * FROM RequiemPortals ORDER BY name");
                rs2 = ps2.executeQuery();
                int i = 1;
                f.addRaw("--Choose Portal--,");
                while (rs2.next()) {
                    PortalMod.myMapGM1.put(String.format("%d", i), rs2.getString("name"));
                    f.addRaw(rs2.getString("name"));
                    f.addRaw(",");
                    i = i + 1;
                }
                rs2.close();
                ps2.close();
                dbcon2.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            f.addRaw("\"}}");
            f.addText("\n");
            f.addText("Insert new x coords here\t");
            f.addInput("xcoords", 10, String.format("%s", getResponder().getPosX() / 4));
            f.addText("\n");
            f.addText("Insert new y coords here\t");
            f.addInput("ycoords", 10, String.format("%s", getResponder().getPosY() / 4));
            f.addText("\n");

            f.addText("\n");
            f.beginHorizontalFlow();
            f.addButton("Edit Selection", "accept");
            f.addText("               ");
            f.addButton("Delete Selection", "decline");

            f.endHorizontalFlow();
            f.addText(" \n");
            f.addText(" \n");

            getResponder().getCommunicator().sendBml(
                    250,
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