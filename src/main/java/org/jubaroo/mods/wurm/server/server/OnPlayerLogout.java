package org.jubaroo.mods.wurm.server.server;

import com.wurmonline.server.players.Player;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.communication.discord.DiscordHandler;

public class OnPlayerLogout {

    public static void onPlayerLogout(Player player) {
        try {
            logoutAnnouncement(player);
        } catch (IllegalArgumentException |
                ClassCastException e) {
            RequiemLogging.logException("Error in onPlayerLogout()", e);
        }
    }

    private static void logoutAnnouncement(Player player) {
        if (player.getPower() < 5)
            if (!Constants.disableDiscordReliance) {
                DiscordHandler.sendToDiscord(CustomChannel.EVENTS, String.format("%s has logged out.", player.getNameWithoutPrefixes()));
            }
    }

}