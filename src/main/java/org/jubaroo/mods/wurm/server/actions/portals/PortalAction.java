package org.jubaroo.mods.wurm.server.actions.portals;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemTypes;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.questions.PortalQuestion;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.items.pottals.PortalMod;

import java.util.Collections;
import java.util.List;

public class PortalAction implements WurmServerMod, ItemTypes, MiscConstants, ModAction, BehaviourProvider, ActionPerformer {
    public static short actionId;
    static ActionEntry actionEntry;

    public PortalAction() {
        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(actionId, "Portal", "teleporting", new int[]{
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
                return Collections.singletonList(actionEntry);
            } else {
                return null;
            }
        } else {
            return null;
        }
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
            PortalQuestion aq = new PortalQuestion(
                    performer,
                    "Portal Menu",
                    "Where would you like to portal to?\n\n",
                    performer.getWurmId());

            aq.sendQuestion();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}