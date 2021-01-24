package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.Items;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.items.behaviours.TreasureChestsBehaviour;

import java.util.Collections;
import java.util.List;

public class TreasureBoxAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public TreasureBoxAction() {
        RequiemLogging.logWarning("TreasureBoxAction()");

        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Open treasure",
                "opening",
                new int[]{ActionTypesProxy.ACTION_TYPE_NOMOVE
                }
        );
        ModActions.registerAction(actionEntry);
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
                if (performer instanceof Player && object != null && object.getTemplateId() == CustomItems.treasureBoxId) {
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
                try {
                    if (performer instanceof Player) {
                        if (target.getTemplate().getTemplateId() != CustomItems.treasureBoxId) {
                            performer.getCommunicator().sendNormalServerMessage("That is not a treasure box.");
                            return true;
                        }
                        if (target.getLastOwnerId() != performer.getWurmId() && target.getOwnerId() != performer.getWurmId()) {
                            performer.getCommunicator().sendNormalServerMessage("You must own the treasure box to open it.");
                            return true;
                        }
                        if (counter == 1f) {
                            performer.getCommunicator().sendNormalServerMessage("You begin to open a treasure box.");
                            Server.getInstance().broadCastAction(String.format("%s begins opening %s treasure.", performer.getName(), performer.getHisHerItsString()), performer, 5);
                            act.setTimeLeft(50);
                            performer.sendActionControl("Opening", true, act.getTimeLeft());
                        } else if (counter * 10f > performer.getCurrentAction().getTimeLeft()) {
                            int aux;
                            if (target.getRarity() >= 3) {
                                aux = (int) (Server.rand.nextFloat() * target.getQualityLevel() * 0.1f) + 90;
                            } else if (target.getRarity() >= 2) {
                                aux = (int) (Server.rand.nextFloat() * target.getQualityLevel() * 0.3f) + 70;
                            } else if (target.getRarity() >= 1) {
                                aux = (int) (Server.rand.nextFloat() * target.getQualityLevel() * 0.5f) + 50;
                            } else {
                                aux = (int) (Server.rand.nextFloat() * target.getQualityLevel());
                            }
                            if (aux >= 90) {
                                performer.getCommunicator().sendNormalServerMessage("You open your treasure box containing fantastic treasures!");
                                Server.getInstance().broadCastAction(String.format("%s opens %s treasure box, containing fantastic treasures!", performer.getName(), performer.getHisHerItsString()), performer, 5);
                            } else if (aux >= 60) {
                                performer.getCommunicator().sendNormalServerMessage("You open your treasure box containing supreme treasures!");
                                Server.getInstance().broadCastAction(String.format("%s opens %s treasure box, containing supreme treasures!", performer.getName(), performer.getHisHerItsString()), performer, 5);
                            } else {
                                performer.getCommunicator().sendNormalServerMessage("You open your treasure box containing rare treasures.");
                                Server.getInstance().broadCastAction(String.format("%s opens %s treasure box, containing rare treasures.", performer.getName(), performer.getHisHerItsString()), performer, 5);
                            }
                            RequiemLogging.logInfo(String.format("Player %s opened treasure box with power %d.", performer.getName(), aux));
                            TreasureChestsBehaviour.newFillTreasureChest(performer.getInventory(), aux);
                            Items.destroyItem(target.getWurmId());
                            return true;
                        }
                    } else {
                        RequiemLogging.logInfo("Somehow a non-player activated a Treasure Box...");
                    }
                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                    return true;
                }
            }

            @Override
            public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
                return this.action(act, performer, target, action, counter);
            }


        }; // ActionPerformer
    }
}