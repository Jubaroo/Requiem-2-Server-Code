package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.Items;
import com.wurmonline.server.NoSuchItemException;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.WurmMail;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.util.*;

public class ReceiveMailAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public ReceiveMailAction() {
        // Get the action id
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Receive all mail", "receiving", new int[]{ActionTypesProxy.ACTION_TYPE_NOMOVE}).range(8).build();
        // Register the action entry
        ModActions.registerAction(this.actionEntry);
    }

    @Override
    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            // Menu with activated object
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item object) {
                return this.getBehavioursFor(performer, object);
            }

            // Menu without activated object
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item object) {
                if (performer instanceof Player && object != null && object.isMailBox() && object.getSpellCourierBonus() > 0f) {
                    return Collections.singletonList(actionEntry);
                }
                return null;
            }
        };
    }

    @Override
    public ActionPerformer getActionPerformer() {
        return new ActionPerformer() {

            @Override
            public short getActionId() {
                return actionId;
            }

            // Without activated object
            @Override
            public boolean action(Action act, Creature performer, Item target, short action, float counter) {
                if (performer instanceof Player) {
                    Player player = (Player) performer;
                    if (!target.isMailBox()) {
                        player.getCommunicator().sendSafeServerMessage("You can only receive mail at a mailbox.");
                        return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                    }
                    if (target.getSpellCourierBonus() <= 0f) {
                        player.getCommunicator().sendSafeServerMessage("The mailbox must be enchanted before receiving mail.");
                        return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                    }
                    if (!target.isEmpty(false)) {
                        player.getCommunicator().sendSafeServerMessage("Empty the mailbox first.");
                        return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                    }
                    Set<WurmMail> mailset = WurmMail.getSentMailsFor(performer.getWurmId(), 100);
                    if (mailset.isEmpty()) {
                        player.getCommunicator().sendSafeServerMessage("You have no mail to collect.");
                        return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                    }
                    Iterator<WurmMail> it = mailset.iterator();
                    WurmMail m;
                    HashSet<Item> itemset = new HashSet<>();
                    while (it.hasNext()) {
                        m = it.next();
                        if (m.rejected || m.price > 0) {
                            return propagate(act, ActionPropagation.CONTINUE_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        try {
                            itemset.add(Items.getItem(m.itemId));
                        } catch (NoSuchItemException e) {
                            e.printStackTrace();
                        }
                    }
                    if (!itemset.isEmpty()) {
                        player.getCommunicator().sendSafeServerMessage(String.format("The %d items that were sent via mail are now available.", itemset.size()));
                        for (Item item : itemset) {
                            Item[] contained4 = item.getAllItems(true);
                            for (Item value : contained4) {
                                value.setMailed(false);
                                value.setLastOwnerId(performer.getWurmId());
                            }
                            WurmMail.removeMail(item.getWurmId());
                            target.insertItem(item, true);
                            item.setLastOwnerId(performer.getWurmId());
                            item.setMailed(false);
                            RequiemLogging.logInfo(String.format("%s received %s %d", performer.getName(), item.getName(), item.getWurmId()));
                        }
                    }
                } else {
                    RequiemLogging.logInfo(String.format("Somehow a non-player activated action ID %d...", actionEntry.getNumber()));
                }
                return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }

            @Override
            public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
                return this.action(act, performer, target, action, counter);
            }
        }; // ActionPerformer
    }

}