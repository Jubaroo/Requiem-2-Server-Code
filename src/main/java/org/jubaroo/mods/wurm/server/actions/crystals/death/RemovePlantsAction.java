package org.jubaroo.mods.wurm.server.actions.crystals.death;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.Players;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.behaviours.Methods;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.tools.RequiemTools;
import org.jubaroo.mods.wurm.server.utils.Cooldowns;

import java.util.Collections;
import java.util.List;

public class RemovePlantsAction implements ModAction, BehaviourProvider, ActionPerformer {
    private final short actionId;
    private final ActionEntry actionEntry;

    public RemovePlantsAction() {
        this.actionId = (short) ModActions.getNextActionId();
        ModActions.registerAction(this.actionEntry = new ActionEntryBuilder(this.actionId, "Decay vegetation", "decaying", new int[]{
                ActionTypesProxy.ACTION_TYPE_NOMOVE,
                ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS,
                ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM
        }).build());
    }

    private boolean canUse(final Creature performer, final Item object, final int tile) {
        if (performer.isPlayer() && object != null && object.getTemplateId() == CustomItems.deathCrystal.getTemplateId() &&
                object.getTopParent() == performer.getInventory().getWurmId()) {

            return RequiemTools.isTreeGrassBushTile(Tiles.decodeType(tile)) || RequiemTools.isEnchantedTile(Tiles.decodeType(tile));

        } else return false;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(final Creature performer, final Item object, final int tilex, final int tiley, final boolean onSurface, final int tile) {
        if (canUse(performer, object, tile))
            return Collections.singletonList(actionEntry);
        else
            return null;
    }

    @Override
    public short getActionId() {
        return this.actionId;
    }

    @Override
    public boolean action(final Action action, final Creature performer, final Item object, final int tilex, final int tiley, final boolean onSurface, final int heightOffset, final int tile, final short num, final float counter) {
        try {
            final String playerEffect = String.valueOf(performer.getName()) + "crystalKillPlantsAction";
            int actionTime = 70;
            int cooldown = 5; // minutes
            if (Cooldowns.isOnCooldown(playerEffect, cooldown)) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You will need to wait %s before you can do that again.", Cooldowns.timeRemaining(playerEffect, cooldown)));
                return true;
            }
            if (!canUse(performer, object, tile)) {
                performer.getCommunicator().sendSafeServerMessage("There is no live vegetation here.");
                return true;
            }
            if (canUse(performer, object, tile)) {
                if (counter == 1f) {
                    performer.playAnimation("place", false);
                    performer.getCommunicator().sendNormalServerMessage("You touch the crystal to the ground and watch as the vegetation starts to die.");
                    performer.getCurrentAction().setTimeLeft(actionTime);
                    Methods.sendSound(performer, "sound.spawn.item.central");
                    performer.sendActionControl("decaying vegetation", true, actionTime);
                } else {
                    if (counter * 10f > action.getTimeLeft()) {
                        performer.getCommunicator().sendNormalServerMessage("The last of the vegetation turns to dust and blows away in the wind.");
                        Server.setSurfaceTile(tilex, tiley, Tiles.decodeHeight(tile), Tiles.Tile.TILE_DIRT.id, (byte) 0);
                        Players.getInstance().sendChangedTile(tilex, tiley, true, false);
                        RequiemLogging.logInfo(String.format("%s uses a %s to turn the tile to dirt at %s X-%s Y-%s", performer.getName(), object.getName(), tile, tilex, tiley));
                        Cooldowns.setUsed(playerEffect);
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            RequiemLogging.logWarning(e.getMessage());
            return true;
        }
    }

}
