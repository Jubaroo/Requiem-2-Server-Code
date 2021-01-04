package org.jubaroo.mods.wurm.server.actions.crystals.life;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.Players;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.utils.logging.TileEvent;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.tools.RequiemTools;
import org.jubaroo.mods.wurm.server.utils.Cooldowns;

import java.util.Collections;
import java.util.List;


public class EnchantGroundAction implements ModAction, BehaviourProvider, ActionPerformer {
    private static byte newType;
    private static byte oldType;
    private final short actionId;
    private final ActionEntry actionEntry;

    public EnchantGroundAction() {
        // Get the action id
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Enchant vegetation", "enchanting", new int[]{
                ActionTypesProxy.ACTION_TYPE_NOMOVE,
                ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS,
                ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM
        }).range(8).build();
        // Register the action entry
        ModActions.registerAction(this.actionEntry);
    }

    private boolean canUse(final Creature performer, final Item source, final int tile) {
        if (performer.isPlayer() && source != null && source.getTemplateId() == CustomItems.lifeCrystal.getTemplateId() &&
                source.getTopParent() == performer.getInventory().getWurmId()) {
            return RequiemTools.isTreeGrassBushTile(Tiles.decodeType(tile));
        } else return false;
    }

    public List<ActionEntry> getBehavioursFor(final Creature performer, final Item source, final int tilex, final int tiley, final boolean onSurface, final int tile) {
        if (canUse(performer, source, tile)) {
            return Collections.singletonList(this.actionEntry);
        }
        return null;
    }

    public short getActionId() {
        return this.actionId;
    }

    public boolean action(final Action action, final Creature performer, final Item source, final int tilex, final int tiley, final boolean onSurface, final int heightOffset, final int tile, final short num, final float counter) {
        try {
            EnchantGroundAction.oldType = Tiles.decodeType(tile);
            final Tiles.Tile oldTile = Tiles.getTile(EnchantGroundAction.oldType);
            EnchantGroundAction.newType = Tiles.Tile.TILE_ENCHANTED_GRASS.id;
            final byte oldData = Tiles.decodeData(tile);
            final String playerEffect = String.format("%scrystalEnchantGroundAction", performer.getName());
            int cooldown = 90; // minutes
            if (Cooldowns.isOnCooldown(playerEffect, cooldown * 60000)) {
                performer.getCommunicator().sendNormalServerMessage(String.format("The %s cannot access that power right now, you will need to wait before you can do that again.", source.getName()));
                return true;
            }
            if (!RequiemTools.isTreeGrassBushTile(Tiles.decodeType(tile))) {
                performer.getCommunicator().sendNormalServerMessage("The area refuses to accept the crystals power.");
                return true;
            }
            if (canUse(performer, source, tile)) {
                if (counter == 1f) {
                    final int time = 70;
                    performer.getCommunicator().sendNormalServerMessage("You touch the crystal to the ground and you begin to feel its energy flow into the ground.");
                    Server.getInstance().broadCastAction(String.format("%s touches a crystal of life to the ground in front of %s and starts to transform the ground.", performer.getName(), performer.getHisHerItsString()), performer, 5);
                    performer.getCurrentAction().setTimeLeft(time);
                    performer.sendActionControl("Enchanting", true, time);
                } else {
                    int time = 0;
                    time = performer.getCurrentAction().getTimeLeft();
                    if (counter * 10f > time) {
                        performer.getCommunicator().sendNormalServerMessage("The crystals energy penetrates the ground and enchants the ground with its magic.");
                        Server.getInstance().broadCastAction(String.format("%s uses a crystal of life to enchant the ground in front of %s.", performer.getName(), performer.getHimHerItString()), performer, 10);
                        TileEvent.log(tilex, tilex, 0, performer.getWurmId(), action.getNumber());
                        if (oldTile.isNormalTree()) {
                            EnchantGroundAction.newType = oldTile.getTreeType(oldData).asEnchantedTree();
                        } else if (oldTile.isNormalBush()) {
                            EnchantGroundAction.newType = oldTile.getBushType(oldData).asEnchantedBush();
                        }
                        Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), EnchantGroundAction.newType, oldData);
                        performer.getMovementScheme().touchFreeMoveCounter();
                        Players.getInstance().sendChangedTile(tilex, tiley, onSurface, false);
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
