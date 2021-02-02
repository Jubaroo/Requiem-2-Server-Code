package org.jubaroo.mods.wurm.server.actions.crystals.frost;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.behaviours.Terraforming;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.utils.Cooldowns;

import java.util.Collections;
import java.util.List;

public class FreezeLavaAction implements ModAction, BehaviourProvider, ActionPerformer {
    private final short actionId;
    private final ActionEntry actionEntry;

    public FreezeLavaAction() {
        this.actionId = (short) ModActions.getNextActionId();
        ModActions.registerAction(this.actionEntry = new ActionEntryBuilder(this.actionId, "Freeze lava", "freezing", new int[]{
                ActionTypesProxy.ACTION_TYPE_NOMOVE,
                ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS,
                ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM
        }).build());
    }

    public List<ActionEntry> getBehavioursFor(final Creature performer, final Item object, final int tilex, final int tiley, final boolean onSurface, final int tile) {
        if (performer instanceof Player && object != null && object.getTemplateId() == CustomItems.frostCrystal.getTemplateId() && Tiles.decodeType(tile) == Tiles.Tile.TILE_LAVA.id) {
            return Collections.singletonList(actionEntry);
        }
        return null;
    }

    public short getActionId() {
        return this.actionId;
    }

    public boolean action(final Action action, final Creature performer, final Item source, final int tilex, final int tiley, final boolean onSurface, final int heightOffset, final int tile, final short num, final float counter) {
        try {
            final String playerEffect = performer.getName() + "crystalFreezeLavaAction";
            int cooldown = 5; // minutes
            if (Cooldowns.isOnCooldown(playerEffect, cooldown)) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You will need to wait %s before you can do that again.", Cooldowns.timeRemaining(playerEffect, cooldown)));
                return true;
            }
            if (Tiles.decodeType(tile) != Tiles.Tile.TILE_LAVA.id) {
                performer.getCommunicator().sendSafeServerMessage("There is no lava here.");
                return true;
            }
            if (Tiles.decodeType(tile) == Tiles.Tile.TILE_LAVA.id) {
                if (counter == 1f) {
                    final int time = 70;
                    performer.getCommunicator().sendNormalServerMessage("You touch the crystal to the lava and it begins to hiss and smoke.");
                    performer.getCurrentAction().setTimeLeft(time);
                    performer.sendActionControl("Freezing lava", true, time);
                } else {
                    if (counter * 10f > performer.getCurrentAction().getTimeLeft()) {
                        Terraforming.freezeLava(performer, tilex, tiley, onSurface, tile, 0, false);
                        performer.getCommunicator().sendNormalServerMessage("The lava hisses and turns to solid rock as the crystals energy penetrates the ground.");
                        Server.getInstance().broadCastAction(String.format("%s uses a crystal of permafrost to freeze the lava in front of %s.", performer.getName(), performer.getHimHerItString()), performer, 10);
                        Cooldowns.setUsed(playerEffect);
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            RequiemLogging.logException("[ERROR] in action in FreezeLavaAction", e);
            return true;
        }
    }

}
