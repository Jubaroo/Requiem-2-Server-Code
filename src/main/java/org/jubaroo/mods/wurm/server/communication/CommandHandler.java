package org.jubaroo.mods.wurm.server.communication;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.creatures.Communicator;
import org.gotti.wurmunlimited.modloader.interfaces.MessagePolicy;

public class CommandHandler {
    public static MessagePolicy handleCommand(Communicator communicator, String message) {
        if (message.startsWith("#spawnGravestone") && communicator.player.getPower() >= MiscConstants.POWER_DEMIGOD) {
            if (!communicator.player.isOnSurface()) {
                communicator.sendAlertServerMessage("Gravestones can't be spawned underground");
            }
            return MessagePolicy.DISCARD;
        }
        return MessagePolicy.PASS;
    }
}
