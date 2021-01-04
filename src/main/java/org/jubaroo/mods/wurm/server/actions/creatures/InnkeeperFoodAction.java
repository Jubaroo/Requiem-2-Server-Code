package org.jubaroo.mods.wurm.server.actions.creatures;

import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.util.Collections;
import java.util.List;

public class InnkeeperFoodAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;
    private final String npcName = "Innkeeper";

    public InnkeeperFoodAction() {
        RequiemLogging.logInfo("GiveFoodAction()");
        this.actionId = (short) ModActions.getNextActionId();
        ModActions.registerAction(this.actionEntry = ActionEntry.createEntry(this.actionId, "Ask for food", "ask", new int[]{
                ActionTypesProxy.ACTION_TYPE_NOMOVE
        }));
    }

    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            public List<ActionEntry> getBehavioursFor(final Creature performer, final Creature object) {
                return this.getBehavioursFor(performer, null, object);
            }

            public List<ActionEntry> getBehavioursFor(final Creature performer, final Item subject, final Creature target) {
                if (performer instanceof Player && target != null && target.getName().contains(npcName)) {
                    return Collections.singletonList(InnkeeperFoodAction.this.actionEntry);
                }
                return null;
            }
        };
    }

    public ActionPerformer getActionPerformer() {
        return new ActionPerformer() {
            public short getActionId() {
                return InnkeeperFoodAction.this.actionId;
            }

            public boolean action(final Action act, final Creature performer, final Creature target, final short action, final float counter) {
                if (performer instanceof Player && target != null && target.getName().contains(npcName)) {
                    performer.getCommunicator().sendNormalServerMessage(npcName + " says, \"You need to hold out your hand for me to give you anything. No soup for you!\"");
                }
                return true;
            }

            public boolean action(final Action act, final Creature performer, final Item source, final Creature target, final short action, final float counter) {
                Communicator comm = performer.getCommunicator();

                if (!performer.isPlayer() || target == null || !target.getName().contains(npcName)) {
                    return true;
                }
                if (source.getTemplateId() != ItemList.bodyHand) {
                    performer.getCommunicator().sendNormalServerMessage(npcName + " says, \"You need to hold out your hand for me to give you anything.\"");
                    return true;
                }
                performer.getStatus().refresh(0.99f, true);
                performer.getStatus().setMaxCCFP();
                comm.sendNormalServerMessage(target.getName() + " feeds you some delicious steak and cool water.");
                target.playAnimation("give", false);
                return true;
            }
        };
    }
}
