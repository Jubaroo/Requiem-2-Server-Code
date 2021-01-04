package org.jubaroo.mods.wurm.server.actions.creatures;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemTypes;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.questions.DestroyCreatureQuestion;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

import java.util.Collections;
import java.util.List;

public class KillCreatureAction implements WurmServerMod, ItemTypes, MiscConstants, ModAction, BehaviourProvider, ActionPerformer {
    public static short actionId;
    private static ActionEntry actionEntry;

    public KillCreatureAction() {
        KillCreatureAction.actionId = (short) ModActions.getNextActionId();
        ModActions.registerAction(KillCreatureAction.actionEntry = ActionEntry.createEntry(KillCreatureAction.actionId, "Destroy Creature", "Destroying Creature", new int[0]));
    }

    public BehaviourProvider getBehaviourProvider() {
        return this;
    }

    public ActionPerformer getActionPerformer() {
        return this;
    }

    public short getActionId() {
        return KillCreatureAction.actionId;
    }

    public List<ActionEntry> getBehavioursFor(final Creature performer, final Item source, final Item target) {
        if (!(performer instanceof Player)) {
            return null;
        }
        if (performer.getPower() > MiscConstants.POWER_HIGH_GOD) {
            return Collections.singletonList(KillCreatureAction.actionEntry);
        }
        return null;
    }

    public boolean action(final Action act, final Creature performer, final Item source, final Item target, final short action, final float counter) {
        if (performer instanceof Player) {
            if (performer.getPower() > MiscConstants.POWER_HIGH_GOD) {
                try {
                    final DestroyCreatureQuestion aq = new DestroyCreatureQuestion(performer, "Destroy that mean Beast", "Ok tell me the wurmId and consider it gone.\n\n", target.getWurmId());
                    aq.sendQuestion();
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
