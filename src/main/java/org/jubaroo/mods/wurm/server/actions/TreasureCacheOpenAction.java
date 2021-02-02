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
import org.jubaroo.mods.wurm.server.misc.Caches;
import org.jubaroo.mods.wurm.server.tools.ItemTools;

import java.util.Arrays;
import java.util.List;

public class TreasureCacheOpenAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public TreasureCacheOpenAction() {
        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Open cache",
                "opening",
                new int[]{ActionTypesProxy.ACTION_TYPE_NOMOVE}    // 6 /* ACTION_TYPE_NOMOVE */, 48 /* ACTION_TYPE_ENEMY_ALWAYS */, 36 /* ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM */
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
                if (performer instanceof Player && object != null && Caches.isTreasureCache(object)) {
                    return Arrays.asList(actionEntry);
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
                        if (!Caches.isTreasureCache(target)) {
                            performer.getCommunicator().sendNormalServerMessage("That is not a treasure cache.");
                            return true;
                        }
                        if (target.getLastOwnerId() != performer.getWurmId() && target.getOwnerId() != performer.getWurmId()) {
                            performer.getCommunicator().sendNormalServerMessage("You must own the " + target.getName() + " to open it.");
                            return true;
                        }
                        if (counter == 1f) {
                            performer.getCommunicator().sendNormalServerMessage("You begin to open the " + target.getName() + ".");
                            Server.getInstance().broadCastAction(performer.getName() + " begins opening " + performer.getHisHerItsString() + " " + target.getName() + ".", performer, 5);
                            act.setTimeLeft(50);
                            performer.sendActionControl("Opening", true, act.getTimeLeft());
                        } else if (counter * 10f > performer.getCurrentAction().getTimeLeft()) {
                            performer.getCommunicator().sendNormalServerMessage("You open your " + target.getName() + ".");
                            Server.getInstance().broadCastAction(performer.getName() + " opens " + performer.getHisHerItsString() + " " + target.getName() + ".", performer, 5);
                            RequiemLogging.logInfo("Player " + performer.getName() + " opened " + target.getName() + " at quality " + target.getCurrentQualityLevel() + " and rarity " + ItemTools.getRarityString(target.getRarity()));
                            Caches.openCache(performer, target);
                            Items.destroyItem(target.getWurmId());
                            return true;
                        }
                    } else {
                        RequiemLogging.logInfo("Somehow a non-player activated a Treasure Box...");
                    }
                    return false;
                } catch (Exception e) {
                    RequiemLogging.logException("[Error] in action in TreasureCacheOpenAction", e);
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