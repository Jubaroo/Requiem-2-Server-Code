package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.Items;
import com.wurmonline.server.Players;
import com.wurmonline.server.Server;
import com.wurmonline.server.Servers;
import com.wurmonline.server.behaviours.*;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.villages.Village;
import com.wurmonline.server.villages.VillageRole;
import com.wurmonline.server.villages.Villages;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.util.Collections;
import java.util.List;

public class DisintegrationRodAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public DisintegrationRodAction() {
        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Use",
                "disintegrating",
                new int[]{
                        ActionTypesProxy.ACTION_TYPE_SHOW_ON_SELECT_BAR,
                        ActionTypesProxy.ACTION_TYPE_IGNORERANGE
                }
        );
        ModActions.registerAction(actionEntry);
    }

    @Override
    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            // Menu with activated object
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item subject, int tilex, int tiley, boolean onSurface, int tile, int dir) {
                if (performer instanceof Player && subject != null && subject.getTemplateId() == CustomItems.disintegrationRodId && Tiles.isSolidCave(Tiles.decodeType(tile))) {
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

            // With activated object
            @Override
            public boolean action(Action act, Creature performer, Item source, int tilex, int tiley, boolean onSurface, int heightOffset, int tile, short action, float counter) {
                try {
                    if (performer instanceof Player) {
                        int newTile = Server.caveMesh.getTile(tilex, tiley);
                        if (source.getTemplate().getTemplateId() != CustomItems.disintegrationRodId) {
                            performer.getCommunicator().sendSafeServerMessage("You must use a Disintegration Rod to do this.");
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        Village v = Villages.getVillage(tilex, tiley, true);
                        if (v != null) {
                            boolean ok = false;
                            VillageRole r = v.getRoleFor(performer);
                            if (r == null || !r.mayMineRock() || !r.mayReinforce()) {
                                if (Servers.localServer.PVPSERVER && System.currentTimeMillis() - v.plan.getLastDrained() > 7200000) {
                                    performer.getCommunicator().sendNormalServerMessage("The settlement has not been drained during the last two hours and the wall still stands this time.", (byte) 3);
                                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                                } else if (!Servers.localServer.PVPSERVER) {
                                    performer.getCommunicator().sendNormalServerMessage("You do not have permission to use that here.");
                                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                                }
                            }
                        }
                        byte type = Tiles.decodeType(newTile);
                        if (Tiles.isSolidCave(type)) {
                            int resource = Server.getCaveResource(tilex, tiley);
                            int dir = (int) (act.getTarget() >> 48) & 255;
                            boolean destroyRod = true;
                            int depleteAmount = 10000;

                            // Don't allow disintegration of reinforced caves
                            if ((type & 0xFF) == Tiles.TILE_TYPE_CAVE_WALL_REINFORCED) {
                                destroyRod = false;
                                performer.getCommunicator().sendSafeServerMessage("The reinforced wall is too strong and doesn't get affected by the rod.");
                            } else if (resource <= depleteAmount && TileRockBehaviour.createInsideTunnel(tilex, tiley, newTile, performer, Actions.MINE, dir, true, act)) {
                                //Server.caveMesh.setTile(tilex, tiley, Tiles.encode(Tiles.decodeHeight(tile), Tiles.Tile.TILE_CAVE.id, Tiles.decodeData(tile)));
                                Players.getInstance().sendChangedTile(tilex, tiley, false, false);
                                performer.getCommunicator().sendSafeServerMessage(String.format("You use the %s on the %s and it breaks!", source.getTemplate().getName(), Tiles.getTile(Tiles.decodeType(newTile)).tiledesc.toLowerCase()));
                            } else if ((type & 0xFF) != Tiles.TILE_TYPE_CAVE_WALL && resource > depleteAmount) {
                                Server.setCaveResource(tilex, tiley, resource - depleteAmount);
                                Players.getInstance().sendChangedTile(tilex, tiley, false, false);
                                performer.getCommunicator().sendSafeServerMessage(String.format("You use the %s on the %s, but it is only weakened!", source.getTemplate().getName(), Tiles.getTile(Tiles.decodeType(newTile)).tiledesc.toLowerCase()));
                            } else if ((type & 0xFF) == Tiles.TILE_TYPE_CAVE_WALL && TileRockBehaviour.createInsideTunnel(tilex, tiley, newTile, performer, Actions.MINE, dir, true, act)) {
                                Players.getInstance().sendChangedTile(tilex, tiley, false, false);
                                performer.getCommunicator().sendSafeServerMessage(String.format("The rock is powerless against the %s!", source.getTemplate().getName()));
                            } else if (!TileRockBehaviour.createInsideTunnel(tilex, tiley, newTile, performer, Actions.MINE, dir, true, act)) {
                                performer.getCommunicator().sendSafeServerMessage("The wall is not stable enough.");
                                destroyRod = false;
                            } // else if
                            if (destroyRod) {
                                Items.destroyItem(source.getWurmId());
                            }
                        } else {
                            RequiemLogging.logInfo(String.format("Disintegration rod attempted to be used on non-solid cave at %d, %d", tilex, tiley));
                        }
                    } else {
                        RequiemLogging.logWarning(String.format("Somehow a non-player activated a %s.", source.getTemplate().getName()));
                    }
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                } catch (Exception e) {
                    RequiemLogging.logException("[Error] in action in DisintegrationRodAction", e);
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
            }
        };
    }
}