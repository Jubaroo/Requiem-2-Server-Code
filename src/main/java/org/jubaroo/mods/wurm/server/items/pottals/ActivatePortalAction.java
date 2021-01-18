package org.jubaroo.mods.wurm.server.items.pottals;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemTypes;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.questions.ActivatePortalQuestion;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;
import org.gotti.wurmunlimited.modsupport.ModSupportDb;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class ActivatePortalAction implements WurmServerMod, ItemTypes, MiscConstants, ModAction, BehaviourProvider, ActionPerformer {
    public static short actionId;
    static ActionEntry actionEntry;

    public ActivatePortalAction() {
        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(actionId, "Activate Portal", "Activating Portal", new int[]{
        });
        ModActions.registerAction(actionEntry);
    }

    @Override
    public BehaviourProvider getBehaviourProvider() {
        return this;
    }

    @Override
    public ActionPerformer getActionPerformer() {
        return this;
    }

    @Override
    public short getActionId() {
        return actionId;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
        return getBehavioursFor(performer, target);
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
        if (performer instanceof Player) {
            if (PortalMod.checkAction(target)) {

                Connection dbcon;
                PreparedStatement ps;
                ResultSet rs;
                boolean found = false;
                try {
                    dbcon = ModSupportDb.getModSupportDb();
                    ps = dbcon.prepareStatement("SELECT * FROM ColdiePortals");
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        if (target.getWurmId() == rs.getLong("itemid")) {
                            found = true;
                        }
                    }
                    rs.close();
                    ps.close();
                    dbcon.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                Connection dbcon2;
                PreparedStatement ps2;
                ResultSet rs2;
                try {
                    dbcon2 = ModSupportDb.getModSupportDb();
                    ps2 = dbcon2.prepareStatement("SELECT * FROM ColdieGMPortals");
                    rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        if (target.getWurmId() == rs2.getLong("itemid")) {
                            found = true;
                        }
                    }
                    rs2.close();
                    ps2.close();
                    dbcon2.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


                if (!found) {
                    PortalMod.myMapPortal.clear();
                    PortalMod.myMapPortal.put("0", target.getWurmId());
                    return Collections.singletonList(actionEntry);
                }

            }
        }
        return null;
    }

    @Override
    public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
        return action(act, performer, target, action, counter);
    }

    @Override
    public boolean action(Action act, Creature performer, Item target, short action, float counter) {
        if (!PortalMod.checkAction(target)) {
            return false;
        }
        try {
            ActivatePortalQuestion aq = new ActivatePortalQuestion(
                    performer,
                    "Portal Activation",
                    "Would you like to activate this portal??\n\n",
                    performer.getWurmId());

            aq.sendQuestion();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}