package org.jubaroo.mods.wurm.server.items.behaviours;

import com.wurmonline.server.Items;
import com.wurmonline.server.Players;
import com.wurmonline.server.Server;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.zones.Zone;
import com.wurmonline.server.zones.Zones;
import com.wurmonline.shared.constants.EffectConstants;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.misc.templates.Location;
import org.jubaroo.mods.wurm.server.server.constants.ItemConstants;

import java.util.ArrayList;

import static org.jubaroo.mods.wurm.server.items.CustomItems.athanorMechanismId;

public class AthanorMechanismBehaviour {
    private static final ArrayList<Item> athanorMechanismArray = new ArrayList<>();
    private static Item athanorMechanism;

    private static void sendMechanismEffect(Player player, Item mechanism) {
        player.getCommunicator().sendAddEffect(mechanism.getWurmId(), EffectConstants.EFFECT_XMAS_LIGHTS, mechanism.getPosX(), mechanism.getPosY(), mechanism.getPosZ(), (byte) 0);
    }

    public static void sendMechanismEffectsToPlayer(Player player) {
        for (Item mechanism : athanorMechanismArray) {
            RequiemLogging.logInfo(String.format("sendMechanismEffectsToPlayer %s", player.getName()));
            sendMechanismEffect(player, mechanism);
        }
    }

    private static void sendMechanismEffectsToPlayers(Item mechanism) {
        for (Player player : Players.getInstance().getPlayers()) {
            RequiemLogging.logInfo(String.format("sendMechanismEffectsToPlayers %s", player.getName()));
            sendMechanismEffect(player, mechanism);
        }
    }

    private static void removeMechanismEffect(Item mechanism) {
        for (Player player : Players.getInstance().getPlayers()) {
            RequiemLogging.logInfo(String.format("removeMechanismEffect to player %s", player.getName()));
            player.getCommunicator().sendRemoveEffect(mechanism.getWurmId());
        }
    }

    public static void removeAthanorMechanism(Item mechanism) {
        RequiemLogging.logInfo("removeAthanorMechanism");
        athanorMechanismArray.remove(mechanism);
        removeMechanismEffect(mechanism);
    }

    public static void pollAthanorMechanism() {
        if (athanorMechanism == null) {
            for (Item item : Items.getAllItems()) {
                if (item.getTemplateId() == athanorMechanismId) {
                    RequiemLogging.logInfo(String.format("Athanor Mechanism located and remembered, with name: %s, and wurmId: %d", item.getName(), item.getWurmId()));
                    athanorMechanism = item;
                    sendMechanismEffectsToPlayers(item);
                    break;
                }
            }
        }
    }

    public static void phaseShiftAthanorMechanism() {
        if (athanorMechanism == null) {
            for (Item item : Items.getAllItems()) {
                if (item.getTemplateId() == athanorMechanismId) {
                    RequiemLogging.logInfo(String.format("Athanor Mechanism located and remembered, with name: %s, and wurmId: %d", item.getName(), item.getWurmId()));
                    athanorMechanism = item;
                    break;
                }
            }
        }
        Location targetLocation = ItemConstants.mechanismLocations[Server.rand.nextInt(ItemConstants.mechanismLocations.length)];
        try {
            removeAthanorMechanism(athanorMechanism);
            if (targetLocation != null) {
                Zone zone = Zones.getZone((int) targetLocation.x, (int) targetLocation.y, true);
                //TODO FIX
                athanorMechanism.setPosXY(targetLocation.x * 4.0F, targetLocation.y * 4.0F);
                zone.addItem(athanorMechanism);
                sendMechanismEffectsToPlayers(athanorMechanism);
                athanorMechanismArray.add(athanorMechanism);
                Players.getInstance().weatherFlash(athanorMechanism.getTileX(), athanorMechanism.getTileY(), athanorMechanism.getPosZ());
                Server.getInstance().broadCastAlert("The Athanor Mechanism has phase shifted to a new location!");
                RequiemLogging.logInfo(String.format("Athanor Mechanism shifted to x: %s, y: %s", targetLocation.x, targetLocation.y));
            }
        } catch (Exception e) {
            RequiemLogging.logException("[ERROR] in phaseShiftAthanorMechanism in AthanorMechanismBehaviour", e);
        }
    }

}
