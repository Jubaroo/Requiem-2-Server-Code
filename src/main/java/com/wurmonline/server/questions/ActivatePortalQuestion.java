package com.wurmonline.server.questions;


import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.NoSuchActionException;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.villages.Village;
import com.wurmonline.server.zones.Zones;
import net.coldie.tools.BmlForm;
import org.gotti.wurmunlimited.modsupport.ModSupportDb;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.actions.portals.ActivatePortalAction;
import org.jubaroo.mods.wurm.server.items.pottals.PortalMod;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import static org.jubaroo.mods.wurm.server.ModConfig.*;


public class ActivatePortalQuestion extends Question {
    private boolean properlySent = false;

    ActivatePortalQuestion(Creature aResponder, String aTitle, String aQuestion, int aType, long aTarget) {
        super(aResponder, aTitle, aQuestion, aType, aTarget);
    }

    public ActivatePortalQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
        super(aResponder, aTitle, aQuestion, LOCATEQUESTION, aTarget);
    }


    public void answer(Properties answer) {
        int coins = costToActivate;
        if (!properlySent) {
            return;
        }

        // check drop down and accepted
        boolean accepted = (answer.containsKey("accept")) && (answer.get("accept") == "true");

        if (accepted) {
            if (coins > getResponder().getMoney()) {
                getResponder().getCommunicator().sendNormalServerMessage("you don't have enough coins in bank for that");
                return;
            }

            Connection dbcon;
            PreparedStatement ps;
            Village v = Zones.getVillage(getResponder().getTileX(), getResponder().getTileY(), true);
            if (v != null) {

                String duplicatevillage = "";

                try {
                    getResponder().chargeMoney(coins);
                    logger.info(String.format("Removing %d iron from %s for activating portal. portal wurmid %d", coins, getResponder().getName(), PortalMod.myMapPortal.get("0")));
                    getResponder().getCommunicator().sendNormalServerMessage(String.format("Removed %d iron from your bank to activate portal at %s", coins, v.getName()));

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    RequiemLogging.logException("[Error] in action in TreasureHuntChestAction", e);
                }

                Connection dbcon3;
                PreparedStatement ps3;
                ResultSet rs3;
                try {
                    dbcon3 = ModSupportDb.getModSupportDb();
                    ps3 = dbcon3.prepareStatement("SELECT * FROM RequiemGMPortals ORDER BY name");
                    rs3 = ps3.executeQuery();

                    while (rs3.next()) {
                        if (rs3.getString("name").equals(v.getName())) {

                            duplicatevillage = " 2";
                        }
                        if (rs3.getString("name").equals(v.getName() + " 2")) {

                            duplicatevillage = " 3";
                        }
                    }
                    rs3.close();
                    ps3.close();
                    dbcon3.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                Connection dbcon2;
                PreparedStatement ps2;
                ResultSet rs2;
                try {
                    dbcon2 = ModSupportDb.getModSupportDb();
                    ps2 = dbcon2.prepareStatement("SELECT * FROM RequiemPortals ORDER BY name");
                    rs2 = ps2.executeQuery();

                    while (rs2.next()) {
                        if (rs2.getString("name").equals(v.getName())) {

                            duplicatevillage = " 2";
                        }
                        if (rs2.getString("name").equals(v.getName() + " 2")) {

                            duplicatevillage = " 3";
                        }
                    }
                    rs2.close();
                    ps2.close();
                    dbcon2.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try {
                    dbcon = ModSupportDb.getModSupportDb();
                    ps = dbcon.prepareStatement("INSERT INTO RequiemPortals (name,posx,posy,itemid,bank) VALUES(?,?,?,?,?)");
                    ps.setString(1, v.getName() + duplicatevillage);
                    ps.setFloat(2, getResponder().getPosX() / 4);
                    ps.setFloat(3, getResponder().getPosY() / 4);
                    ps.setLong(4, PortalMod.myMapPortal.get("0"));
                    ps.setInt(5, activateBankAmount);
                    ps.executeUpdate();
                    ps.close();
                    dbcon.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {

                getResponder().getCommunicator().sendNormalServerMessage("The portal must be on a deed");
            }
        }

    }

    public void sendQuestion() {
        boolean ok;

        try {
            ok = false;
            Action act = getResponder().getCurrentAction();
            if (act.getNumber() == ActivatePortalAction.actionId) {
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
            f.addText(String.format("\nIt costs %d iron per minute to keep portals active\n ", costPerMin));
            f.addText(String.format("\n Activating this portal will cost you %d iron from your bank, this will pay for activation and also %d iron into this portals bank", costToActivate, activateBankAmount));

            f.addText("\n\n\n\n ");
            f.beginHorizontalFlow();
            f.addButton("Activate portal now", "accept");
            f.addText("               ");
            f.addButton("Cancel", "decline");

            f.endHorizontalFlow();
            f.addText(" \n");
            f.addText(" \n");

            getResponder().getCommunicator().sendBml(
                    300,
                    350,
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