package org.jubaroo.mods.wurm.server.actions.items;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.ItemTypes;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.questions.TentSleepQuestion;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

import java.util.Collections;
import java.util.List;

public class TentSleepAction implements WurmServerMod, ItemTypes, MiscConstants, ModAction, BehaviourProvider, ActionPerformer {
    public static short actionId;
    static ActionEntry actionEntry;

    public TentSleepAction() {
        TentSleepAction.actionId = (short) ModActions.getNextActionId();
        ModActions.registerAction(TentSleepAction.actionEntry = ActionEntry.createEntry(TentSleepAction.actionId, "Sleep", "Sleeping", new int[
                ActionTypesProxy.ACTION_TYPE_NOMOVE
                ]));
    }

    public BehaviourProvider getBehaviourProvider() {
        return this;
    }

    public ActionPerformer getActionPerformer() {
        return this;
    }

    public short getActionId() {
        return TentSleepAction.actionId;
    }

    public List<ActionEntry> getBehavioursFor(final Creature performer, final Item source, final Item target) {
        if (!(performer instanceof Player)) {
            return null;
        }
        if (target.isOwner(performer) && target.getParentId() == MiscConstants.NOID && (target.isTent() || target.getTemplateId() == ItemList.strawBed)) {
            return Collections.singletonList(TentSleepAction.actionEntry);
        }
        return null;
    }

    public boolean action(final Action act, final Creature performer, final Item source, final Item target, final short action, final float counter) {
        if (performer instanceof Player) {
            if (target.isOwner(performer) && target.getParentId() == MiscConstants.NOID) {
                if (!target.isTent()) {
                    if (target.getTemplateId() != ItemList.strawBed) {
                        return true;
                    }
                }
                try {
                    final TentSleepQuestion aq = new TentSleepQuestion(performer, "Sleep Activation", "You seriously want to sleep here??\n\n", target.getWurmId());
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
