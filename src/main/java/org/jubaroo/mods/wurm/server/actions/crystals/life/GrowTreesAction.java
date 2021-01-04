package org.jubaroo.mods.wurm.server.actions.crystals.life;

import com.wurmonline.mesh.BushData;
import com.wurmonline.mesh.FoliageAge;
import com.wurmonline.mesh.Tiles;
import com.wurmonline.mesh.TreeData;
import com.wurmonline.server.Players;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.Requiem;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.utils.Cooldowns;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GrowTreesAction implements ModAction, BehaviourProvider, ActionPerformer {
    private static Logger logger;
    private static byte newType;

    static {
        Requiem.logger = Logger.getLogger(Requiem.class.getName());
    }

    private final short actionId;
    private final ActionEntry actionEntry;

    public GrowTreesAction() {
        this.actionId = (short) ModActions.getNextActionId();
        ModActions.registerAction(this.actionEntry = new ActionEntryBuilder(this.actionId, "Grow trees", "growing", new int[]{
                ActionTypesProxy.ACTION_TYPE_NOMOVE,
                ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS,
                ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM
        }).build());
    }

    private static void rampantGrowth(final Creature performer, final int tilex, final int tiley) {
        Requiem.logger.log(Level.INFO, performer.getName() + " creates trees and bushes at " + tilex + ", " + tiley);
        if (performer.getLogger() != null) {
            performer.getLogger().log(Level.INFO, "Creates trees and bushes at " + tilex + ", " + tiley);
        }
        for (int x = tilex - 5; x < tilex + 5; ++x) {
            for (int y = tiley - 5; y < tiley + 5; ++y) {
                final int t = Server.surfaceMesh.getTile(x, y);
                if (Tiles.decodeHeight(t) > 0 && (Tiles.decodeType(t) == Tiles.Tile.TILE_DIRT.id || Tiles.decodeType(t) == Tiles.Tile.TILE_GRASS.id || Tiles.decodeType(t) == Tiles.Tile.TILE_SAND.id) && Server.rand.nextInt(3) == 0) {
                    int type = 0;
                    if (Server.rand.nextBoolean()) {
                        type = Server.rand.nextInt(BushData.BushType.getLength());
                        GrowTreesAction.newType = BushData.BushType.fromInt(type).asNormalBush();
                    } else {
                        type = Server.rand.nextInt(TreeData.TreeType.getLength());
                        GrowTreesAction.newType = TreeData.TreeType.fromInt(type).asNormalTree();
                    }
                    final byte tage = (byte) Server.rand.nextInt(FoliageAge.OVERAGED.getAgeId());
                    final byte grasslen = (byte) (Server.rand.nextInt(3) + 1);
                    Server.setSurfaceTile(x, y, Tiles.decodeHeight(t), GrowTreesAction.newType, Tiles.encodeTreeData(tage, false, false, grasslen));
                    Players.getInstance().sendChangedTile(x, y, true, false);
                }
            }
        }
    }

    public List<ActionEntry> getBehavioursFor(final Creature performer, final Item object, final int tilex, final int tiley, final boolean onSurface, final int tile) {
        if (performer instanceof Player && object != null && object.getTemplateId() == CustomItems.lifeCrystal.getTemplateId() && Tiles.decodeType(tile) == Tiles.Tile.TILE_GRASS.id) {
            return Collections.singletonList(actionEntry);
        }
        return null;
    }

    public short getActionId() {
        return this.actionId;
    }

    public boolean action(final Action action, final Creature performer, final Item source, final int tilex, final int tiley, final boolean onSurface, final int heightOffset, final int tile, final short num, final float counter) {
        try {
            final String playerEffect = String.valueOf(performer.getName()) + "crystalGrowTreesAction";
            int cooldown = 60; // minutes
            if (Cooldowns.isOnCooldown(playerEffect, cooldown)) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You will need to wait %s before you can do that again.", Cooldowns.timeRemaining(playerEffect, cooldown)));
                return true;
            }
            if (Tiles.decodeType(tile) != Tiles.Tile.TILE_GRASS.id) {
                performer.getCommunicator().sendSafeServerMessage("The trees would not survive here.");
                return true;
            }
            if (!performer.isWithinDistanceTo(tilex, tiley, Tiles.decodeHeight(tile), 5f)) {
                performer.getCommunicator().sendNormalServerMessage("You must be closer.");
                return true;
            }
            if (Tiles.decodeType(tile) == Tiles.Tile.TILE_GRASS.id) {
                if (counter == 1f) {
                    final int time = 70;
                    performer.getCommunicator().sendNormalServerMessage("You touch the crystal to the ground and you begin to feel its energy flow into the ground.");
                    performer.getCurrentAction().setTimeLeft(70);
                    performer.sendActionControl("Growing trees", true, 70);
                } else {
                    int time = 0;
                    time = performer.getCurrentAction().getTimeLeft();
                    if (counter * 10f > time) {
                        GrowTreesAction.rampantGrowth(performer, tilex, tiley);
                        performer.getCommunicator().sendNormalServerMessage("The crystals energy penetrates the ground and grows new trees instantly.");
                        Server.getInstance().broadCastAction(performer.getName() + " uses a crystal of life to grow some random trees around " + performer.getHimHerItString() + ".", performer, 10);
                        Cooldowns.setUsed(playerEffect);
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            RequiemLogging.logWarning( e.getMessage() + e);
            return true;
        }
    }

}
