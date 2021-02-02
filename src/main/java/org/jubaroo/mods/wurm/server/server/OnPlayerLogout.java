package org.jubaroo.mods.wurm.server.server;

import com.wurmonline.server.Servers;
import com.wurmonline.server.players.Player;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.communication.discord.DiscordHandler;

import static org.jubaroo.mods.wurm.server.ModConfig.disableDiscordReliance;

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
        if (player.getPower() < 3)
            if (!disableDiscordReliance) {
                DiscordHandler.sendToDiscord(CustomChannel.LOGINS, String.format("[%s] Player %s has logged in.", Servers.getLocalServerName(), player.getName()));
                RequiemLogging.logInfo(String.format("[%s] Player %s has logged out.", Servers.getLocalServerName(), player.getName()));
            }
    }

}
