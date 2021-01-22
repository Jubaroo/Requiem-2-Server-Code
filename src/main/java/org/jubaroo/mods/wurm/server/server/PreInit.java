package org.jubaroo.mods.wurm.server.server;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.gotti.wurmunlimited.modsupport.creatures.ModCreatures;
import org.gotti.wurmunlimited.modsupport.vehicles.ModVehicleBehaviours;
import org.jubaroo.mods.wurm.server.communication.CustomChat;
import org.jubaroo.mods.wurm.server.communication.discord.ChatHandler;
import org.jubaroo.mods.wurm.server.communication.discord.TicketHandler;
import org.jubaroo.mods.wurm.server.creatures.*;
import org.jubaroo.mods.wurm.server.items.ItemRemoval;
import org.jubaroo.mods.wurm.server.items.behaviours.AthanorMechanismBehaviour;
import org.jubaroo.mods.wurm.server.misc.*;
import org.jubaroo.mods.wurm.server.utils.MissionCreator;

import static org.jubaroo.mods.wurm.server.ModConfig.enableAthanorMechanism;
import static org.jubaroo.mods.wurm.server.ModConfig.mailboxEnableEnchant;

public class PreInit {

    public static void preInit() {
        try {
            ModActions.init();
            ModVehicleBehaviours.init();
            ModCreatures.init();
            MiscChanges.SpawnTowerGuards();
            MissionCreator.preInit();
            //TreasureChestsBehaviour.preInit();
            CustomChat.preInit();
            //CustomTitles.announceTitles();
            MiscChanges.preInit();
            Misc.preInit();
            Titans.preInit();
            RareSpawns.preInit();
            MethodsBestiary.preInit();
            CustomMountSettings.preInit();
            QualityOfLife.preInit();
            ItemRemoval.preInit();
            Fishing.preInit();
            Spawn.preInit();
            MiscHooks.newPlayerQuestion();
            //ItemMod.preInit();
            //PlayerTitles.preInit();
            //EconomicChanges.preInit();
            //SkillChanges.preInit();
            //Bloodlust.preInit();
            if (mailboxEnableEnchant) {
                Misc.EnableEnchant();
            }
            if (enableAthanorMechanism) {
                AthanorMechanismBehaviour.preInit();
            }
            ClassPool classPool = HookManager.getInstance().getClassPool();

            // Discord Stuff
            CtClass ctPlayers = classPool.getCtClass("com.wurmonline.server.Players");
            ctPlayers.getMethod("sendGlobalKingdomMessage", "(Lcom/wurmonline/server/creatures/Creature;JLjava/lang/String;Ljava/lang/String;ZBIII)V")
                    .insertBefore(String.format(" if (kingdom < 0) {%s.sendMessage($$); return;}", ChatHandler.class.getName()));

            ctPlayers.getMethod("sendStartKingdomChat", "(Lcom/wurmonline/server/players/Player;)V").setBody("return;");
            ctPlayers.getMethod("sendStartGlobalKingdomChat", "(Lcom/wurmonline/server/players/Player;)V").setBody("return;");

            CtClass ctLoginHandler = classPool.getCtClass("com.wurmonline.server.LoginHandler");
            ctLoginHandler.getMethod("sendLoggedInPeople", "(Lcom/wurmonline/server/players/Player;)V")
                    .insertBefore(String.format("%s.sendBanner(player);", ChatHandler.class.getName()));

            CtClass ctPlayer = classPool.getCtClass("com.wurmonline.server.players.Player");

            ctPlayer.getMethod("isKingdomChat", "()Z").setBody("return false;");
            ctPlayer.getMethod("isGlobalChat", "()Z").setBody("return false;");
            ctPlayer.getMethod("seesPlayerAssistantWindow", "()Z").setBody("return false;");

            CtClass CtServer = classPool.getCtClass("com.wurmonline.server.Server");

            CtServer.getMethod("shutDown", "()V").insertBefore(String.format("%s.serverStopped();", ChatHandler.class.getName()));
            CtServer.getMethod("broadCastNormal", "(Ljava/lang/String;Z)V").insertBefore(String.format("%s.handleBroadcast($1);", ChatHandler.class.getName()));
            CtServer.getMethod("broadCastSafe", "(Ljava/lang/String;ZB)V").insertBefore(String.format("%s.handleBroadcast($1);", ChatHandler.class.getName()));
            CtServer.getMethod("broadCastAlert", "(Ljava/lang/String;ZB)V").insertBefore(String.format("%s.handleBroadcast($1);", ChatHandler.class.getName()));

            classPool.getCtClass("com.wurmonline.server.ServerEntry").getMethod("setAvailable", "(ZZIIII)V")
                    .insertBefore(String.format("if (this.isAvailable != $1) %s.serverAvailable(this, $1);", ChatHandler.class.getName()));

            classPool.getCtClass("com.wurmonline.server.support.Tickets").getMethod("addTicket", "(Lcom/wurmonline/server/support/Ticket;Z)Lcom/wurmonline/server/support/Ticket;")
                    .insertBefore(String.format("%s.updateTicket($1);", TicketHandler.class.getName()));

            classPool.getCtClass("com.wurmonline.server.support.Ticket").getMethod("addTicketAction", "(Lcom/wurmonline/server/support/TicketAction;)V")
                    .insertBefore(String.format("%s.addTicketAction(this, $1);", TicketHandler.class.getName()));

        } catch (IllegalArgumentException | ClassCastException | NotFoundException | CannotCompileException e) {
            throw new HookException(e);
        }
    }

}
