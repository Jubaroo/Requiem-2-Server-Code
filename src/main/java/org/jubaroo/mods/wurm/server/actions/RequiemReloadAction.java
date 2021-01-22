package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemTypes;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.server.Config;
import org.jubaroo.mods.wurm.server.tools.database.DatabaseHelper;

import java.io.InputStream;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class RequiemReloadAction implements WurmServerMod, ItemTypes, MiscConstants, ModAction, BehaviourProvider, ActionPerformer {
    public static short actionId;
    static ActionEntry actionEntry;

    public RequiemReloadAction() {
        RequiemReloadAction.actionId = (short) ModActions.getNextActionId();
        ModActions.registerAction(RequiemReloadAction.actionEntry = ActionEntry.createEntry(RequiemReloadAction.actionId, "Reload Requiem properties", "Reloading Requiem properties", new int[0]));
    }

    public BehaviourProvider getBehaviourProvider() {
        return (BehaviourProvider) this;
    }

    public ActionPerformer getActionPerformer() {
        return (ActionPerformer) this;
    }

    public short getActionId() {
        return RequiemReloadAction.actionId;
    }

    public List<ActionEntry> getBehavioursFor(final Creature performer, final Item source, final Item target) {
        return this.getBehavioursFor(performer, target);
    }

    public List<ActionEntry> getBehavioursFor(final Creature performer, final Item target) {
        if ((performer instanceof Player && performer.getPower() > 4) || performer.getName().equals("Jubaroo")) {
            return Arrays.asList(RequiemReloadAction.actionEntry);
        }
        return null;
    }

    public boolean action(final Action act, final Creature performer, final Item source, final Item target, final short action, final float counter) {
        return this.action(act, performer, target, action, counter);
    }

    public boolean action(final Action act, final Creature performer, final Item target, final short action, final float counter) {
        if ((performer instanceof Player && performer.getPower() == MiscConstants.POWER_IMPLEMENTOR) || performer.getName().equals("Jubaroo")) {
            final Path path = Paths.get("mods/Requiem.properties", new String[0]);
            if (!Files.exists(path, new LinkOption[0])) {
                performer.getCommunicator().sendAlertServerMessage("The config file seems to be missing.");
                return true;
            }
            InputStream stream = null;
            DatabaseHelper.setUniques();
            try {
                performer.getCommunicator().sendAlertServerMessage("Opening the config file.");
                stream = Files.newInputStream(path, new OpenOption[0]);
                final Properties properties = new Properties();
                performer.getCommunicator().sendAlertServerMessage("Reading from the config file.");
                properties.load(stream);
                Config.doConfig(properties);
                performer.getCommunicator().sendAlertServerMessage("Loading all options.");
            } catch (Exception ex) {
                performer.getCommunicator().sendAlertServerMessage("Error reloading the config file, check the server log.");
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (Exception ex2) {
                    performer.getCommunicator().sendAlertServerMessage("Error closing the config file, possible file lock.");
                }
                return true;
            } finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }
                } catch (Exception ex2) {
                    performer.getCommunicator().sendAlertServerMessage("Error closing the config file, possible file lock.");
                }
            }
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception ex2) {
                performer.getCommunicator().sendAlertServerMessage("Error closing the config file, possible file lock.");
            }
        }
        return true;
    }
}
