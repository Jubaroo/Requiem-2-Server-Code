package org.jubaroo.mods.wurm.server.actions.magicItems;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.MethodsCreatures;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.endgames.EndGameItems;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.ItemTypes;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;
import org.gotti.wurmunlimited.modsupport.ModSupportDb;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.misc.database.DatabaseHelper;
import org.jubaroo.mods.wurm.server.server.Constants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class SkullAction implements WurmServerMod, ItemTypes, MiscConstants, ModAction, BehaviourProvider, ActionPerformer {
    public static short actionId;
    static ActionEntry actionEntry;

    public SkullAction() {
        SkullAction.actionId = (short) ModActions.getNextActionId();
        ModActions.registerAction(SkullAction.actionEntry = ActionEntry.createEntry(SkullAction.actionId, "Locate Closest Unique", "Locating Closest Unique", new int[0]));
    }

    public BehaviourProvider getBehaviourProvider() {
        return this;
    }

    public ActionPerformer getActionPerformer() {
        return this;
    }

    public short getActionId() {
        return SkullAction.actionId;
    }

    public List<ActionEntry> getBehavioursFor(final Creature performer, final Item source, final Item target) {
        if (!(performer instanceof Player)) {
            return null;
        }
        if (Constants.skullLocateUnique && source.getTemplateId() == ItemList.skull && target.getTemplateId() == ItemList.skull) {
            return Collections.singletonList(SkullAction.actionEntry);
        }
        return null;
    }

    public boolean action(final Action act, final Creature performer, final Item source, final Item target, final short action, final float counter) {
        if (!(performer instanceof Player)) {
            return false;
        }
        if (Constants.skullLocateUnique && source.getTemplateId() == ItemList.skull && target.getTemplateId() == ItemList.skull) {
            if (Constants.reloadSkull) {
                DatabaseHelper.setUniques();
            }
            Connection dbcon2;
            PreparedStatement ps2;
            ResultSet rs2;
            int smallestdistance = 100000000;
            try {
                dbcon2 = ModSupportDb.getModSupportDb();
                ps2 = dbcon2.prepareStatement("SELECT * FROM UniqueLocations");
                rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    if (performer.getPower() > 4) {
                        performer.getCommunicator().sendNormalServerMessage(String.format("%s %s %s", rs2.getString("name"), rs2.getFloat("X"), rs2.getFloat("Y")));
                    }
                    final int xDistance = (int) Math.abs(performer.getTileX() - rs2.getFloat("X"));
                    final int yDistance = (int) Math.abs(performer.getTileY() - rs2.getFloat("Y"));
                    final int distance = (int) Math.sqrt(xDistance * xDistance + yDistance * yDistance);
                    if (distance < smallestdistance) {
                        Constants.uniquex = rs2.getFloat("X");
                        Constants.uniquey = rs2.getFloat("Y");
                        Constants.uniquename = rs2.getString("name");
                        smallestdistance = distance;
                    }
                }
                rs2.close();
                ps2.close();
                dbcon2.close();
                if (smallestdistance != 100000000) {
                    final float newdamage = source.getDamage() + Constants.damageToTake;
                    performer.getCommunicator().sendNormalServerMessage(String.format("The %s took %d damage.", source.getName(), Constants.damageToTake));
                    source.setDamage(newdamage);
                    final int xDistance2 = (int) Math.abs(performer.getTileX() - Constants.uniquex);
                    final int yDistance2 = (int) Math.abs(performer.getTileY() - Constants.uniquey);
                    final int distance2 = (int) Math.sqrt(xDistance2 * xDistance2 + yDistance2 * yDistance2);
                    final int direction = MethodsCreatures.getDir(performer, (int) Constants.uniquex, (int) Constants.uniquey);
                    performer.getCommunicator().sendNormalServerMessage(EndGameItems.getDistanceString(distance2, String.format("%s ", Constants.uniquename), MethodsCreatures.getLocationStringFor(performer.getStatus().getRotation(), direction, "you"), true));
                } else {
                    performer.getCommunicator().sendNormalServerMessage("no uniques on map");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return true;
    }
}
