package org.jubaroo.mods.wurm.server.communication;

import com.wurmonline.server.Message;
import com.wurmonline.server.Players;
import com.wurmonline.server.players.Player;
import javassist.*;
import mod.sin.lib.Util;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
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

    public static void preInit() {
        try {
            // Show custom slash commands in /help readout
            ClassPool classpool = HookManager.getInstance().getClassPool();
            Util.setReason("Show Custom Slash Commands In /help Readout");
            CtClass clazz = classpool.get("com.wurmonline.server.creatures.Communicator");
            CtMethod method = clazz.getDeclaredMethod("sendHelp");
            method.insertAfter("this.sendHelpMessage(\"/roll - randomly rolls a pair of dice and displays it in the chat window.\");");
            method.insertAfter("this.sendHelpMessage(\"/gps or /location or /coordinates - tells you your location on the server.\");");
            method.insertAfter("this.sendHelpMessage(\"/stime or /servertime - tells you the current server time and date.\");");

        } catch (CannotCompileException | NotFoundException | IllegalArgumentException | ClassCastException e) {
            throw new HookException(e);
        }
    }

}
