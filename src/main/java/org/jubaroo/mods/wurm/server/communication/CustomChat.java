package org.jubaroo.mods.wurm.server.communication;

import com.wurmonline.server.Message;
import com.wurmonline.server.Players;
import com.wurmonline.server.players.Player;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.communication.discord.DiscordHandler;

public class CustomChat {

    public static void sendTitleTabMessage(String channel, final String message, final int red, final int green, final int blue) {
        DiscordHandler.sendToDiscord(CustomChannel.TITLES, message);
        Runnable r = new Runnable() {
            public void run() {
                Message mess;
                for (Player rec : Players.getInstance().getPlayers()) {
                    mess = new Message(rec, Message.EVENT, "Titles", message, red, green, blue);
                    rec.getCommunicator().sendMessage(mess);
                }
            }
        };
        r.run();
    }

}
