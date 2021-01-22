package org.jubaroo.mods.wurm.server.actions;

import com.pveplands.treasurehunting.Treasuremap;
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
import org.jubaroo.mods.wurm.server.tools.ItemTools;

import java.util.Collections;
import java.util.List;

public class SealedMapAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public SealedMapAction() {
        RequiemLogging.logWarning("TreasureCacheOpenAction()");

        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Unseal",
                "unsealing",
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
                if (performer instanceof Player && object != null && object.getTemplateId() == CustomItems.sealedMapId) {
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
                        if (target.getTemplateId() != CustomItems.sealedMapId) {
                            performer.getCommunicator().sendNormalServerMessage("That is not a sealed map.");
                            return true;
                        }
                        if (target.getLastOwnerId() != performer.getWurmId() && target.getOwnerId() != performer.getWurmId()) {
                            performer.getCommunicator().sendNormalServerMessage("You must own the " + target.getName() + " to open it.");
                            return true;
                        }
                        if (counter == 1f) {
                            performer.getCommunicator().sendNormalServerMessage("You begin to unseal the " + target.getName() + ".");
                            Server.getInstance().broadCastAction(performer.getName() + " begins unsealing " + performer.getHisHerItsString() + " " + target.getName() + ".", performer, 5);
                            act.setTimeLeft(50);
                            performer.sendActionControl("Unsealing", true, act.getTimeLeft());
                        } else if (counter * 10f > performer.getCurrentAction().getTimeLeft()) {
                            performer.getCommunicator().sendNormalServerMessage("You unseal your " + target.getName() + ".");
                            Server.getInstance().broadCastAction(performer.getName() + " unseals " + performer.getHisHerItsString() + " " + target.getName() + ".", performer, 5);
                            RequiemLogging.logInfo("Player " + performer.getName() + " unsealed " + target.getName() + " at quality " + target.getCurrentQualityLevel() + " and rarity " + ItemTools.getRarityString(target.getRarity()));
                            target.setData1(100);
                            Item map = Treasuremap.CreateTreasuremap(performer, target, null, null, true);
                            performer.getInventory().insertItem(map, true);
                            Items.destroyItem(target.getWurmId());
                            return true;
                        }
                    } else {
                        RequiemLogging.logWarning("Somehow a non-player activated a Treasure Box...");
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


        };
    }
}