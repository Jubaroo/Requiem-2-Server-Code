package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.questions.TownPortalQuestion;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.util.Collections;
import java.util.List;

public class TownScrollAction implements ModAction, BehaviourProvider, ActionPerformer {
    private final short actionId;
    private final ActionEntry actionEntry;

    public TownScrollAction() {
        this.actionId = (short) ModActions.getNextActionId();
        final int[] types = {};
        ModActions.registerAction(this.actionEntry = ActionEntry.createEntry(this.actionId, "Read Scroll", "teleporting", types));
    }

    public short getActionId() {
        return this.actionId;
    }

    public List<ActionEntry> getBehavioursFor(final Creature performer, final Item source, final Item target) {
        if (target == null) {
            return null;
        }
        if (target.getTemplateId() != CustomItems.scrollOfTownPortal.getTemplateId()) {
            return null;
        }
        if (performer.getCitizenVillage() == null) {
            return null;
        }
        if (target.isTraded()) {
            return null;
        }
        if (target.getTopParent() == target.getWurmId()) {
            return null;
        }
        return Collections.singletonList(this.actionEntry);
    }

    public List<ActionEntry> getBehavioursFor(final Creature performer, final Item target) {
        return this.getBehavioursFor(performer, null, target);
    }

    public boolean action(final Action action, final Creature performer, final Item target, final short num, final float counter) {
        return this.action(action, performer, null, target, num, counter);
    }

    public boolean action(final Action action, final Creature performer, final Item source, final Item target, final short num, final float counter) {
        final TownPortalQuestion tq = new TownPortalQuestion(performer);
        tq.sendQuestion();
        return true;
    }
}
