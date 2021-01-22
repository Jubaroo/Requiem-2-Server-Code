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
import org.jubaroo.mods.wurm.server.items.behaviours.SupplyDepotBehaviour;

import java.util.Collections;
import java.util.List;

public class RequiemCacheOpenAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public RequiemCacheOpenAction() {
        RequiemLogging.logWarning("RequiemCacheOpenAction()");

        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Open cache",
                "opening",
                new int[]{ActionTypesProxy.ACTION_TYPE_NOMOVE}
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
                if (performer instanceof Player && object != null && object.getTemplateId() == CustomItems.requiemDepotCacheId) {
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
                        if (target.getTemplate().getTemplateId() != CustomItems.requiemDepotCacheId) {
                            performer.getCommunicator().sendNormalServerMessage("That is not a cache.");
                            return true;
                        }
                        if (target.getLastOwnerId() != performer.getWurmId() && target.getOwnerId() != performer.getWurmId()) {
                            performer.getCommunicator().sendNormalServerMessage(String.format("You must own the %s to open it.", target.getName()));
                            return true;
                        }
                        if (counter == 1f) {
                            performer.getCommunicator().sendNormalServerMessage(String.format("You begin to open a %s.", target.getName()));
                            Server.getInstance().broadCastAction(String.format("%s begins opening %s %s.", performer.getName(), performer.getHisHerItsString(), target.getName()), performer, 5);
                            act.setTimeLeft(50);
                            performer.sendActionControl("Opening", true, act.getTimeLeft());
                        } else if (counter * 10f > performer.getCurrentAction().getTimeLeft()) {
                            performer.getCommunicator().sendNormalServerMessage(String.format("You open your %s.", target.getName()));
                            Server.getInstance().broadCastAction(String.format("%s opens %s %s.", performer.getName(), performer.getHisHerItsString(), target.getName()), performer, 5);
                            RequiemLogging.debug(String.format("Player %s opened Requiem cache.", performer.getName()));
                            // Sorcery fragment.
							/*Item sorceryFragment = ItemFactory.createItem(SorceryFragment.getId(), 90f, null);
							performer.getInventory().insertItem(sorceryFragment, true);*/
                            SupplyDepotBehaviour.giveCacheReward(performer);
                            Items.destroyItem(target.getWurmId());
                            return true;
                        }
                    } else {
                        RequiemLogging.debug("Somehow a non-player activated a Requiem cache...");
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