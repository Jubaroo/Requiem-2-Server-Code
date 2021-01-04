package org.jubaroo.mods.wurm.server.server;

import com.wurmonline.server.Message;
import org.gotti.wurmunlimited.modloader.interfaces.MessagePolicy;
import org.jubaroo.mods.wurm.server.communication.KeyEvent;

public class OnKingdomMessage {

    public static MessagePolicy onKingdomMessage(Message message) {
        String window = message.getWindow();

        if (window.startsWith("Global") && KeyEvent.isActive()) {
            KeyEvent.handlePlayerMessage(message);
        }
        return MessagePolicy.PASS;
    }
}
