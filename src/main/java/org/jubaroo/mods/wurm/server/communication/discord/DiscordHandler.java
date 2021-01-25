package org.jubaroo.mods.wurm.server.communication.discord;

import com.wurmonline.server.Players;
import com.wurmonline.server.Servers;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DiscordHandler extends ListenerAdapter {
    public static String botToken;
    public static String serverName;
    private static JDA jda;

    private static Map<CustomChannel, ConcurrentLinkedQueue<String>> sendQueues =
            Arrays.stream(CustomChannel.values()).collect(Collectors.toMap(Function.identity(), x -> new ConcurrentLinkedQueue<>()));

    private static Map<String, String> emojis = new HashMap<>();

    private static long lastStatusUpdate = Long.MIN_VALUE;
    private static int lastPlayers = -1;

    static {
        emojis.put("\uD83D\uDE1B", ":P");
        emojis.put("\uD83D\uDE03", ":)");
        emojis.put("\uD83D\uDE04", ":D");
        emojis.put("\uD83D\uDE26", ":(");
        emojis.put("\uD83D\uDE22", ":`(");
        emojis.put("\uD83D\uDE17", ":*");
        emojis.put("\uD83D\uDC94", "</3");
        emojis.put("\u2764", "<3");
    }

    public static void initJda() {
        if (!Servers.localServer.LOGINSERVER) return;

        if (jda != null) {
            jda.shutdown();
            jda = null;
        }

        try {
            jda = JDABuilder.create(DiscordHandler.botToken, GatewayIntent.GUILD_MESSAGES).addEventListeners(new DiscordHandler()).build();
        } catch (LoginException e) {
            RequiemLogging.logException("Error connecting to discord", e);
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.TEXT) && !event.getAuthor().isBot()) {
            CustomChannel channel = CustomChannel.findByDiscordName(event.getTextChannel().getName());
            if (channel != null && !channel.discordOnly && event.getMember() != null) {
                String name = event.getMember().getEffectiveName();
                for (Message.Attachment att : event.getMessage().getAttachments()) {
                    String url = att.getUrl();
                    ChatHandler.sendToPlayersAndServers(channel, "@" + name, url, -10L, -1, -1, -1);
                }
                String msg = event.getMessage().getContentDisplay().trim();
                for (Map.Entry<String, String> p : emojis.entrySet()) {
                    msg = msg.replace(p.getKey(), p.getValue());
                }
                for (String part : msg.split("\n")) {
                    part = part.trim();
                    if (part.length() > 0) {
                        ChatHandler.sendToPlayersAndServers(channel, "@" + name, part, -10L, -1, -1, -1);
                    }
                }
            }
        }
    }

    private static void sendSafe(CustomChannel channel, String msg) {
        MessageBuilder builder = new MessageBuilder();
        builder.append(msg);

        List<Guild> guilds = jda.getGuildsByName(DiscordHandler.serverName, true);
        if (guilds.size() < 1) {
            RequiemLogging.logWarning(String.format("Server '%s' not found on discord, unable to send messages", DiscordHandler.serverName));
            return;
        }

        List<TextChannel> channels = guilds.get(0).getTextChannelsByName(channel.discordName, true);
        if (channels.size() < 1) {
            RequiemLogging.logWarning(String.format("Channel '%s' not found on discord (for %s), unable to send messages", channel.discordName, channel));
            return;
        }

        channels.get(0).sendMessage(builder.build())
                .queue(null, e -> RequiemLogging.logException("Error sending to discord", e));

    }

    public static void sendToDiscord(CustomChannel channel, String msg) {
        try {
            //if (Servers.isThisATestServer()) return;
            if (jda == null || channel.discordName == null) return;
            if (jda.getStatus() != JDA.Status.CONNECTED) {
                RequiemLogging.logInfo(String.format("Discord not ready, queueing: [%s] %s", channel, msg));
                sendQueues.get(channel).add(msg);
            } else {
                sendSafe(channel, msg);
            }
        } catch (Exception e) {
            RequiemLogging.logException("Error sending to discord", e);
        }
    }

    @Override
    public void onStatusChange(StatusChangeEvent event) {
        RequiemLogging.logInfo(String.format("Discord status is now %s", event.getNewStatus()));
        try {
            if (event.getNewStatus() == JDA.Status.CONNECTED) {
                lastPlayers = -1;
                lastStatusUpdate = Long.MIN_VALUE;
                poll();
                for (CustomChannel channel : CustomChannel.values()) {
                    ConcurrentLinkedQueue<String> sendQueue = sendQueues.get(channel);
                    if (!sendQueue.isEmpty()) {
                        while (!sendQueue.isEmpty()) {
                            String msg = sendQueue.poll();
                            RequiemLogging.logInfo(String.format("Sending queued: [%s] %s", channel, msg));
                            sendSafe(channel, msg);
                        }
                    }
                }
            }
        } catch (Exception e) {
            RequiemLogging.logException("Error sending to discord", e);
        }
    }

    public static void poll() {
        if (System.currentTimeMillis() > lastStatusUpdate + 30000 && jda != null && jda.getStatus() == JDA.Status.CONNECTED) {
            lastStatusUpdate = System.currentTimeMillis();

            int players = Arrays.stream(com.wurmonline.server.Servers.getAllServers())
                    .filter(i -> i.id != Servers.localServer.id)
                    .mapToInt(i -> i.currentPlayers)
                    .sum() + Players.getInstance().getNumberOfPlayers();

            if (players != lastPlayers) {
                jda.getPresence().setActivity(Activity.watching(String.format("%d player%s online", players, players == 1 ? "" : "s")));
                RequiemLogging.logInfo(String.format("Sent status update (%d) players", players));
                lastPlayers = players;
            }
        }
    }

}
