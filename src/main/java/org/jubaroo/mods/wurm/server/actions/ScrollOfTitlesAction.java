package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.Items;
import com.wurmonline.server.Players;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.players.Titles;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.util.Collections;
import java.util.List;

public class ScrollOfTitlesAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final short actionId;
    private final ActionEntry actionEntry;

    public ScrollOfTitlesAction() {
        // Get the action id
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Read the scroll", "reading", new int[]{ActionTypesProxy.ACTION_TYPE_NOMOVE}).build();
        // Register the action entry
        ModActions.registerAction(this.actionEntry);
    }

    @Override
    public short getActionId() {
        return this.actionId;
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
    public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
        return getBehavioursFor(performer, null, target);
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
        if (performer instanceof Player && target != null && target.getTemplateId() == CustomItems.scrollOfTitles.getTemplateId())
            return Collections.singletonList(this.actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {
        return action(action, performer, target, num, counter);
    }

    @Override
    public boolean action(Action action, Creature performer, Item target, short num, float counter) {
        try {
            Communicator comm = performer.getCommunicator();
            String targetName = target.getName();

            if (target.getTemplate().getTemplateId() != CustomItems.scrollOfTitles.getTemplateId()) {
                comm.sendSafeServerMessage(String.format("This is not a %s!?", targetName));
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (target.getData1() == -1) {
                comm.sendSafeServerMessage("This scroll does not have a title assigned to it. Please open a ticket using /support and give as many details as possible.");
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (target.getLastOwnerId() != performer.getWurmId() && target.getOwnerId() != performer.getWurmId()) {
                comm.sendNormalServerMessage(String.format("You must own the %s to read it.", targetName));
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (counter == 1f) {
                comm.sendNormalServerMessage(String.format("You start to read from the %s.", targetName));
                Server.getInstance().broadCastAction(String.format("%s begins to read from %s %s.", performer.getName(), performer.getHisHerItsString(), targetName), performer, 5);
                action.setTimeLeft(50);
                performer.sendActionControl("reading", true, 50);
            } else {
                if (counter * 10f > action.getTimeLeft()) {
                    final Player p = Players.getInstance().getPlayer(performer.getNameWithoutPrefixes());
                    Titles.Title title = Titles.Title.getTitle(target.getData1());
                    if (title != null) {
                        p.addTitle(title);
                        comm.sendNormalServerMessage(String.format("You gain the title %s from the %s.", title.getName(), targetName));
                    }
                    Items.destroyItem(target.getWurmId());
                    comm.sendSafeServerMessage(String.format("The %s crumbles to dust in your hand as you read the last word.", targetName));
                    Server.getInstance().broadCastAction(String.format("%s finishes reading %s %s.", performer.getName(), performer.getHisHerItsString(), targetName), performer, 5);
                    return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
            }
            return propagate(action, ActionPropagation.CONTINUE_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        } catch (Exception e) {
            RequiemLogging.logWarning(String.format("search action error%s", e));
            return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        }
    }
}