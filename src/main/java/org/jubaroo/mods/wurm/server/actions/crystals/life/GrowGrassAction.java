package org.jubaroo.mods.wurm.server.actions.crystals.life;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.Players;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.utils.Cooldowns;

import java.util.Collections;
import java.util.List;

public class GrowGrassAction implements ModAction, BehaviourProvider, ActionPerformer {
    private final short actionId;
    private final ActionEntry actionEntry;

    public GrowGrassAction() {
        // Get the action id
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Grow grass", "growing", new int[]{
                ActionTypesProxy.ACTION_TYPE_NOMOVE,
                ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS,
                ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM
        }).range(8).build();
        // Register the action entry
        ModActions.registerAction(this.actionEntry);
    }

    public List<ActionEntry> getBehavioursFor(final Creature performer, final Item object, final int tilex, final int tiley, final boolean onSurface, final int tile) {
        if (performer instanceof Player && object != null && object.getTemplateId() == CustomItems.lifeCrystal.getTemplateId() && Tiles.decodeType(tile) == Tiles.Tile.TILE_DIRT.id) {
            return Collections.singletonList(actionEntry);
        }
        return null;
    }

    public short getActionId() {
        return this.actionId;
    }

    public boolean action(final Action action, final Creature performer, final Item source, final int tilex, final int tiley, final boolean onSurface, final int heightOffset, final int tile, final short num, final float counter) {
        try {
            final String playerEffect = String.valueOf(performer.getName()) + "crystalGrowGrassAction";
            int cooldown = 20; // minutes
            if (Cooldowns.isOnCooldown(playerEffect, cooldown)) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You will need to wait %s before you can do that again.", Cooldowns.timeRemaining(playerEffect, cooldown)));
                return true;
            }
            if (Tiles.decodeType(tile) != Tiles.Tile.TILE_DIRT.id) {
                performer.getCommunicator().sendSafeServerMessage("You cannot grow grass here.");
                return true;
            }
            if (Tiles.decodeType(tile) == Tiles.Tile.TILE_DIRT.id) {
                if (counter == 1f) {
                    final int time = 70;
                    performer.getCommunicator().sendNormalServerMessage("You touch the crystal to the dirt.");
                    Server.getInstance().broadCastAction(performer.getName() + " touches a crystal of life to the dirt in front of " + performer.getHimHerItString() + ".", performer, 10);
                    performer.getCurrentAction().setTimeLeft(50);
                    performer.sendActionControl("Growing grass", true, 50);
                } else {
                    int time = 0;
                    time = performer.getCurrentAction().getTimeLeft();
                    if (counter * 10f > time) {
                        Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), Tiles.Tile.TILE_GRASS.id, (byte) 0);
                        Players.getInstance().sendChangedTile(tilex, tiley, true, false);
                        performer.getCommunicator().sendNormalServerMessage("The crystals energy penetrates the ground and grass shoots out of the ground.");
                        Server.getInstance().broadCastAction("Grass instantly grows in front of " + performer.getName() + ".", performer, 10);
                        Cooldowns.setUsed(playerEffect);
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            RequiemLogging.logWarning(e.getMessage() + e);
            return true;
        }
    }

}
