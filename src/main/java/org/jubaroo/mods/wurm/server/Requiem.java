package org.jubaroo.mods.wurm.server;

import com.wurmonline.server.Message;
import com.wurmonline.server.Servers;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.interfaces.*;
import org.jubaroo.mods.wurm.server.communication.commands.ArgumentTokenizer;
import org.jubaroo.mods.wurm.server.communication.discord.ChatHandler;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.communication.discord.DiscordHandler;
import org.jubaroo.mods.wurm.server.creatures.*;
import org.jubaroo.mods.wurm.server.items.CustomItemCreationEntries;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.items.ItemMod;
import org.jubaroo.mods.wurm.server.misc.Misc;
import org.jubaroo.mods.wurm.server.misc.QualityOfLife;
import org.jubaroo.mods.wurm.server.server.*;
import org.jubaroo.mods.wurm.server.vehicles.CustomVehicleCreationEntries;
import org.jubaroo.mods.wurm.server.vehicles.CustomVehicles;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class Requiem implements WurmServerMod, ServerStartedListener, ServerShutdownListener, PlayerLoginListener, ItemTemplatesCreatedListener, Configurable, PreInitable, ServerPollListener, Initable, PlayerMessageListener, ChannelMessageListener {
    public static Logger logger = Logger.getLogger(String.format("%s %s", Requiem.class.getName(), Constants.VERSION));

    @Override
    public void onItemTemplatesCreated() {
        RequiemLogging.logInfo("onItemTemplatesCreated called");
        try {
            CustomItems.registerCustomItems();
            CustomVehicles.registerCustomVehicles();
            RequiemLogging.logInfo("======= Adding Creatures & Mount Settings for Creatures=======");
            CustomCreatures.registerCustomCreatures();
            CreatureSpawns.spawnTable();
            CustomMountSettings.registerCustomMountSettings();
            RequiemLogging.logInfo("======= Creating Item Mod items =======");
            RequiemLogging.logInfo("======= Creating Cache items =======");
            if (!Constants.disableItemMods) {
                CustomItemCreationEntries.registerCustomItemCreationEntries();
            }
            if (!Constants.disableEntireMod) {
                RequiemLogging.debug("======= Editing existing item templates =======");
                ItemMod.modifyItemsOnCreated();
            }
            CustomVehicleCreationEntries.registerCustomVehicleCreationEntries();
            new OnItemTemplateCreated();
        } catch (IllegalArgumentException | ClassCastException | IOException e) {
            e.printStackTrace();
            RequiemLogging.logException("Error in onItemTemplatesCreated()", e);
        }
        RequiemLogging.logInfo("all onItemTemplatesCreated completed");
    }

    @Override
    public void configure(final Properties properties) {
        Config.doConfig(properties);
        // Discord
        Constants.botToken = properties.getProperty("botToken");
        Constants.serverName = properties.getProperty("serverName");
        CustomChannel.GLOBAL.discordName = properties.getProperty("globalName");
        CustomChannel.HELP.discordName = properties.getProperty("helpName");
        CustomChannel.TICKETS.discordName = properties.getProperty("ticketName");
        CustomChannel.TITLES.discordName = properties.getProperty("titlesName");
        CustomChannel.EVENTS.discordName = properties.getProperty("eventsName");
        CustomChannel.TITAN.discordName = properties.getProperty("titanName");
        CustomChannel.TRADE.discordName = properties.getProperty("tradeName");
        CustomChannel.LOGINS.discordName = properties.getProperty("loginsName");
        CustomChannel.SERVER_STATUS.discordName = properties.getProperty("serverStatusName");
    }

    @Override
    public void preInit() {
        RequiemLogging.logInfo("preInit called");
        try {
            if (!Constants.disableEntireMod) {
                PreInit.preInit();
            }
        } catch (IllegalArgumentException | ClassCastException e) {
            throw new HookException(e);
        }
        RequiemLogging.logInfo("all preInit completed");
    }

    @Override
    public void init() {
        RequiemLogging.logInfo("init called");
        try {
            if (!Constants.disableEntireMod) {
                RequiemLogging.logInfo("Started Init.init()");
                try {
                    ItemMod.init();
                    Bounty.init();
                    MethodsBestiary.init();
                    Misc.init();
                    //OnServerPoll.init();
                    QualityOfLife.init();
                    //MiscHooks.init();
                } catch (Throwable e) {
                    RequiemLogging.logException("Error in init()", e);
                }
            }
        } catch (Throwable e) {
            RequiemLogging.logException("Error in init()", e);
        }
        RequiemLogging.logInfo("all init completed");
    }

    @Override
    public void onServerStarted() {
        RequiemLogging.logInfo("onServerStarted called");
        try {
            if (!Constants.disableEntireMod) {
                OnServerStarted.onServerStarted();
            }
            ChatHandler.serverStarted();
        } catch (IllegalArgumentException | ClassCastException e) {
            RequiemLogging.logException("Error in modifyItemsOnServerStarted()", e);
        }
        RequiemLogging.logInfo("all onServerStarted completed");
    }

    @Override
    public void onServerShutdown() {
    }

    @Deprecated
    @Override
    public boolean onPlayerMessage(Communicator communicator, String message) {
        return false;
    }

    @Override
    public MessagePolicy onPlayerMessage(Communicator communicator, String message, String title) {
        final Player player = communicator.getPlayer();
        String[] argv = ArgumentTokenizer.tokenize(message).toArray(new String[0]);
        if (communicator.player.getPower() >= 4 && message.startsWith("#discordreconnect")) {
            DiscordHandler.initJda();
            return MessagePolicy.DISCARD;
        } else if (communicator.player.getPower() >= 1 && message.startsWith("#eventmsg")) {
            String msg = message.replace("#eventmsg", "").trim();
            ChatHandler.setUpcomingEvent(msg);
            if (msg.length() > 0) {
                communicator.sendNormalServerMessage("Set event line: " + msg);
            } else {
                communicator.sendNormalServerMessage("Cleared event line.");
            }
            return MessagePolicy.DISCARD;
        } else if (!message.startsWith("#") && !message.startsWith("/")) {
            CustomChannel chan = CustomChannel.findByIngameName(title);
            if (chan != null) {
                if (chan.canPlayersSend) {
                    ChatHandler.handleGlobalMessage(chan, communicator, message);
                }
                return MessagePolicy.DISCARD;
            } else return MessagePolicy.PASS;
        } else if (OnServerStarted.cmdtool.runWurmCmd(player, argv)) {
            return MessagePolicy.PASS;
        } else if (!message.startsWith("/me ") && (message.equals("help") || message.contains("guard!") || message.contains("guards!") || message.contains("help guard") || message.contains("help guards"))) {
            player.getCommunicator().sendNormalServerMessage("Guards have been called!");
            player.callGuards();
            return MessagePolicy.DISCARD;
        } else {
            return MessagePolicy.PASS;
        }
    }

    @Override
    public MessagePolicy onKingdomMessage(Message message) {
        return OnKingdomMessage.onKingdomMessage(message);
    }

    @Override
    public void onPlayerLogin(Player player) {
        try {
            if (!Constants.disableEntireMod) {
                OnPlayerLogin.onPlayerLogin(player);
            }
        } catch (IllegalArgumentException |
                ClassCastException e) {
            RequiemLogging.logException("Error in onPlayerLogin()", e);
        }
    }

    @Override
    public void onPlayerLogout(Player player) {
        try {
            if (!Constants.disableEntireMod) {
                OnPlayerLogout.onPlayerLogout(player);
            }
        } catch (IllegalArgumentException |
                ClassCastException e) {
            RequiemLogging.logException("Error in onPlayerLogin()", e);
        }
    }

    @Override
    public void onServerPoll() {
        if (!Constants.disableEntireMod) {
            OnServerPoll.onServerPoll();
            Misc.noobTips();
            //Titans.locateTitans();
        }
        if (Servers.localServer.LOGINSERVER)
            DiscordHandler.poll();
    }

    @Override
    public String getVersion() {
        return Constants.VERSION;
    }

    //TODO
    // [18:45:54] Tilex: 1191, tiley=1037
    // *** redo all items to be in one file ***
    // action for patreons to use a mailbox to order more blueprints
    // labyrinthia deed that has a maze that changes once a week. the maze has a chest in the middle with random loot each time
    // change the descriptions of the animals that relate to real life
    // Need to make a healing fountain action and item for the arena. new model also!!!
    // Make an action/question to reset ALL skills to 1 and apply a certain permanent "buff" that will increase each time you reset all skills. Example req. would be to have x amount of skills at 99 or above
    // Hook into Player.spawn and add monthly buffs
    // Add treasure goblins and such to the leaderboard mod?
    // Make factions with reputation that can be gained
    // tablets of fryian spawn inside buildings, stop that!
    // Item to summon a dragon. If players fail to kill it after a set amount of time, the dragon disappears. Possibly make it a question as to what to summon?
    // make noob tips display once in a while unless the player types a certain command to turn it off? Database function required?
    // golden drake with treasure goblin effect
    // reindeer corpse
    // fix logging wagon
    // hook into MethodsCreatures.milkMethodsCreatures.milk
    // add deed create and remove to discord
    // zombie doing a rummage action looping
    // create new sword model from butchering knife and sword handle
    // invisible item as a smoke emitter
    // Make HotA creatures (in HotA zone) have a chance to give out a token or something to exchange for goods at a special vendor
    // Storage units seem to be messed up as far as volume of items inside
    // Make eyes able to be put into bulk storage
    // Make different types of beds give different sleep bonus rates
    // Make help channel in game and Discord. Remove the CA help channel
    // Make capes work!!!
    // ----------> Setup gradle build for Requiem mod Integrate all server.jar code<----------
    // Portable bank or another item to be used as one
    // Horse traits do not show up in GM trait set list after trait 40
    // Make an action/question to trade in a HotA statue for a different kind?
    // Make weak "cannibal tribes" that roam globally like The Forest?
    // Make new undead creature that only spawns at night and leaves no body when it dies so they can mass de-spawn at dawn like a wraith
    // Make a headless horseman with an invisible helm so that it looks like he has no head? Or figure out the pumpkin helm
    // Auto turn unknown grass tiles into normal grass tiles
    // Fix rainbow unicorns foal to adult transition
    // White hell horse spawns a black one now and then
    // Create invisible, immortal animal(s) to hitch to the carpet to emulate it being pulled. or make an animal invisible when hitched to the carpet Look in VehicleBehaviour.actionHitch
    // Alchemy overhaul. Make health potions, favor potions, etc?
    // Players with high Proteins value will receive a bonus to Body skill gain and it's sub-skills?
    // Make enchant grass ability from crystal apply the effect to a 3x3 area
    // Wand Of Transmutation for Ground Types, not ore
    // Scrolls of summoning for each creature
    // Necromancer that equips black clothes, a staff and cast spells and has skeleton followers
    // Custom naming tool that will change the names of animals to whatever the player types in
    // Mark HotA zone with light beams when HotA is active?
    // A ring that works as a portal. Super hard to make and very expensive
    // player appreciation day on a certain day each year
    // Add new things to TempStates in com.wurmonline.server.items.TempStates or Item.tempMod
    // Merchant ships that travel the seas selling items. Salesman with a cog model that goes in the water. Or have it teleport around the map like athanor mechanism
    // make combat rating based on soul skill for DeathAction
    // keep the same color trait after rebirth
    // make the sub menu for both labyrinth actions work!!!
    // Make an action to pack up a wagon into a kit using Karma or some other "currency"
    // damage dealt is causing issues with phantom creatures. After an animal dies, it keeps fighting you even though you cannot see it
    // make The Cluckster spawn special chicks to attack the player when at half health
    // Fix supply depot spawn times <--------
    // absorb health through a corpse with death crystal?
    // Update Dyno bot to give an awesome greeting like the discord channel for the game Raft.
    // Make an action/item to increase bank slots for players. Database edits needed most likely
    // Make an NPC that sells animals
    // Make an NPC that sells titles
    // Make backpack of steel tools disappear when opened like treasure chest - currently does not work 100%
    // Make treasure chest a rare container of some kind. Maybe a new bank item that is portable?
    // Fix Cluckster and minions!!!
    // Make Life Crystal Rebirth action spawn new creature and delete the corpse of the old one
    // Make Eternal Reservoirs use less fuel
    // Make bark harvest-able from any over-aged tree
    // Bag that only holds tools. Able to hold tools a normal backpack cannot
    // Make hitching post turned the correct way
    // Copy/paste action for terrain data to auxData on ebony wand
}
