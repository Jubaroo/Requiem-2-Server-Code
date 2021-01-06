package org.jubaroo.mods.wurm.server.communication.discord;

import com.wurmonline.server.*;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.webinterface.WcKingdomChat;
import net.bdew.wurm.tools.server.ServerThreadExecutor;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatHandler {
    static Logger chatlogger = Logger.getLogger("Chat");
    static String eventsMsg = "";

    public static void handleGlobalMessage(CustomChannel channel, Communicator communicator, String message) {
        if (communicator.isInvulnerable()) {
            communicator.sendAlertServerMessage("You may not use global chat until you have moved and lost invulnerability.");
            return;
        }
        if (communicator.player.isMute()) {
            communicator.sendNormalServerMessage("You are muted.");
            return;
        }
        chatlogger.log(Level.INFO, channel.ingameName + "-" + "<" + communicator.player.getName() + "> " + message);

        sendToPlayers(channel, communicator.player.getName(), message, communicator.player.getWurmId(),
                communicator.player.hasColoredChat() ? communicator.player.getCustomRedChat() : -1,
                communicator.player.hasColoredChat() ? communicator.player.getCustomGreenChat() : -1,
                communicator.player.hasColoredChat() ? communicator.player.getCustomBlueChat() : -1
        );
        sendToServers(channel,
                communicator.player.getName(), message, communicator.player.getWurmId(),
                communicator.player.hasColoredChat() ? communicator.player.getCustomRedChat() : -1,
                communicator.player.hasColoredChat() ? communicator.player.getCustomGreenChat() : -1,
                communicator.player.hasColoredChat() ? communicator.player.getCustomBlueChat() : -1
        );

        if (Servers.localServer.LOGINSERVER)
            DiscordHandler.sendToDiscord(channel, "<" + communicator.player.getName() + "> " + message);

        communicator.player.chatted();
    }

    public static void sendMessage(final Creature sender, final long senderId, final String playerName, final String message, final boolean emote, final byte kingdom, final int r, final int g, final int b) {
        CustomChannel chan = CustomChannel.findByKingdom(kingdom);
        if (chan != null && !chan.discordOnly) {
            if (chan == CustomChannel.INFO) {
                eventsMsg = message.trim();
                if (eventsMsg.length() > 0)
                    sendToPlayers(CustomChannel.INFO, "", eventsMsg, MiscConstants.NOID, 255, 140, 0);
            } else {
                if (Servers.localServer.LOGINSERVER)
                    DiscordHandler.sendToDiscord(chan, "<" + playerName + "> " + message);
                chatlogger.log(Level.INFO, chan.ingameName + "-" + "<" + playerName + "> " + message);
                sendToPlayers(chan, playerName, message, senderId, r, g, b);
            }
        }
    }

    public static void setUpcomingEvent(String msg) {
        eventsMsg = msg.trim();
        if (eventsMsg.length() > 0)
            sendToPlayers(CustomChannel.INFO, "", eventsMsg, MiscConstants.NOID, 255, 140, 0);
        sendToServers(CustomChannel.INFO, "", eventsMsg, MiscConstants.NOID, 0, 0, 0);
    }

    public static void systemMessage(Player player, CustomChannel channel, String msg) {
        systemMessage(player, channel, msg, 250, 150, 250);
    }

    public static void systemMessage(Player player, CustomChannel channel, String msg, int r, int g, int b) {
        Message mess = new Message(player, (byte) 16, channel.ingameName, msg, r, g, b);
        player.getCommunicator().sendMessage(mess);
    }

    public static void sendBanner(final Player player) {
        systemMessage(player, CustomChannel.GLOBAL, "This is the Global channel, you can use it to communicate with players across all servers and kingdoms.");
        systemMessage(player, CustomChannel.HELP, "This is the Help channel, if you have any questions about the game ask them here and we'll do our best to answer them.");
        systemMessage(player, CustomChannel.HELP, "Please refrain from general chatter in this channel.");
        systemMessage(player, CustomChannel.INFO, "Welcome to Requiem of Wurm!");
        systemMessage(player, CustomChannel.INFO, "Check out our website for all info on the servers and maps: https://requiemofwurm.wixsite.com/main");
        systemMessage(player, CustomChannel.INFO, "Join us in discord - https://discord.gg/3CPJKXp");
        systemMessage(player, CustomChannel.INFO, "This is a HEAVILY MODDED server with a lot of custom content. To be able to enjoy all the server has to offer, it is STRONGLY RECOMMENDED to visit the Mod loader installer link below.");
        systemMessage(player, CustomChannel.INFO, "Right click on and choose open in browser to go to the websites below.");
        systemMessage(player, CustomChannel.INFO, "If you expience any crashes while playing, please install this mod to prevent future crashing. https://github.com/bdew-wurm/modelcrashfix/releases. NOTE: It is included with the installer linked below");
        systemMessage(player, CustomChannel.INFO, "Mod loader installer:");
        systemMessage(player, CustomChannel.INFO, "https://requiemofwurm.wixsite.com/main/client-modloader-installer");
        systemMessage(player, CustomChannel.INFO, "Requiem of Wurm custom Wiki:");
        systemMessage(player, CustomChannel.INFO, "http://requiemofwurm.wikidot.com/start");
        systemMessage(player, CustomChannel.INFO, "Vote for the server as often as you can!");
        systemMessage(player, CustomChannel.INFO, "https://wurm-unlimited.com/server/2701/vote/");
        systemMessage(player, CustomChannel.INFO, "SCROLL UP to see all of the information on the server.");
        systemMessage(player, CustomChannel.INFO, "Enjoy your time on Requiem of Wurm!");
        if (eventsMsg.length() > 0)
            systemMessage(player, CustomChannel.INFO, eventsMsg, 255, 140, 0);
    }

    public static void sendToPlayersAndServers(CustomChannel channel, String author, String msg, long wurmId, int r, int g, int b) {
        sendToPlayers(channel, author, msg, wurmId, r, g, b);
        sendToServers(channel, author, msg, wurmId, r, g, b);
    }

    static void sendToPlayers(CustomChannel channel, String author, String msg, long wurmId, int r, int g, int b) {
        ServerThreadExecutor.INSTANCE.execute(() -> {
            Message mess = new Message(null, (byte) 16, channel.ingameName, (author.length() == 0 ? "" : "<" + author + "> ") + msg);
            mess.setColorR(r);
            mess.setColorG(g);
            mess.setColorB(b);
            final Player[] playarr = Players.getInstance().getPlayers();
            for (Player player : playarr) {
                if (!player.isIgnored(wurmId)) {
                    player.getCommunicator().sendMessage(mess);
                }
            }
        });
    }

    static void sendToServers(CustomChannel channel, String author, String msg, long wurmId, int r, int g, int b) {
        final WcKingdomChat wc = new WcKingdomChat(WurmId.getNextWCCommandId(), wurmId, author, msg, false, channel.kingdom, r, g, b);
        if (!Servers.isThisLoginServer()) {
            wc.sendToLoginServer();
        } else {
            wc.sendFromLoginServer();
        }
    }

    public static void serverStarted() {
        if (Servers.localServer.LOGINSERVER) {
            RequiemLogging.logInfo("Server started, connecting to discord");
            DiscordHandler.initJda();
            DiscordHandler.sendToDiscord(CustomChannel.SERVER_STATUS, "**:green_circle: Servers are starting up... :green_circle:**");
        }
    }

    public static void serverStopped() {
        if (Servers.localServer.LOGINSERVER) {
            RequiemLogging.logInfo("Sending shutdown notice");
            DiscordHandler.sendToDiscord(CustomChannel.SERVER_STATUS, "**:octagonal_sign: Servers are shutting down :octagonal_sign:**");
        }
    }

    public static void serverAvailable(ServerEntry ent, boolean available) {
        if (Servers.localServer.LOGINSERVER) {
            RequiemLogging.logInfo(String.format("Notifying available change - %s %s", ent.getName(), available));
            DiscordHandler.sendToDiscord(CustomChannel.SERVER_STATUS, String.format("**%s is %s**", ent.getName(), available ? "now online!" : "shutting down."));
        }
    }

    public static void handleBroadcast(String msg) {
        if (msg.startsWith("The settlement of") || msg.startsWith("Rumours of") || msg.endsWith("has been slain.")) {
            ChatHandler.sendToPlayersAndServers(CustomChannel.EVENTS, String.format("[%s]", Servers.getLocalServerName()), msg, MiscConstants.NOID, 255, 140, 0);
        }
    }
}
