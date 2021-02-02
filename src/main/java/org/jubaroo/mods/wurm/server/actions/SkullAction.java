package org.jubaroo.mods.wurm.server.actions;

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
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.tools.database.DatabaseHelper;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.jubaroo.mods.wurm.server.ModConfig.*;
import static org.jubaroo.mods.wurm.server.server.constants.CreatureConstants.*;

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
        if (skullLocateUnique && source.getTemplateId() == ItemList.skull && target.getTemplateId() == ItemList.skull) {
            return Collections.singletonList(SkullAction.actionEntry);
        }
        return null;
    }

    public boolean action(final Action act, final Creature performer, final Item source, final Item target, final short action, final float counter) {
        if (!(performer instanceof Player)) {
            return false;
        }
        if (skullLocateUnique && source.getTemplateId() == ItemList.skull && target.getTemplateId() == ItemList.skull) {
            if (reloadSkull) {
                //DatabaseHelper.setUniques();
            }
            try {
                if (DatabaseHelper.skullActionDatabaseGetter(performer) != 100000000) {
                    final float newdamage = source.getDamage() + damageToTake;
                    performer.getCommunicator().sendNormalServerMessage(String.format("The %s took %d damage.", source.getName(), damageToTake));
                    source.setDamage(newdamage);
                    final int xDistance2 = (int) Math.abs(performer.getTileX() - uniqueX);
                    final int yDistance2 = (int) Math.abs(performer.getTileY() - uniqueY);
                    final int distance2 = (int) Math.sqrt(xDistance2 * xDistance2 + yDistance2 * yDistance2);
                    final int direction = MethodsCreatures.getDir(performer, (int) uniqueX, (int) uniqueY);
                    performer.getCommunicator().sendNormalServerMessage(EndGameItems.getDistanceString(distance2, String.format("%s ", uniqueName), MethodsCreatures.getLocationStringFor(performer.getStatus().getRotation(), direction, "you"), true));
                } else {
                    performer.getCommunicator().sendNormalServerMessage("No uniques are on the map.");
                }
            } catch (SQLException e) {
                RequiemLogging.logException("[Error] in action in SkullAction", e);
            }
            return true;
        }
        return true;
    }
}
