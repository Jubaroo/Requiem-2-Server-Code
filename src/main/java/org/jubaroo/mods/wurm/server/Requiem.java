package org.jubaroo.mods.wurm.server;

import com.wurmonline.server.Message;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.interfaces.*;
import org.jubaroo.mods.wurm.server.communication.commands.ArgumentTokenizer;
import org.jubaroo.mods.wurm.server.communication.discord.ChatHandler;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.communication.discord.DiscordHandler;
import org.jubaroo.mods.wurm.server.creatures.CustomCreatures;
import org.jubaroo.mods.wurm.server.creatures.CustomMountSettings;
import org.jubaroo.mods.wurm.server.items.CustomItemCreationEntries;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.items.ItemMod;
import org.jubaroo.mods.wurm.server.items.pottals.PollPortals;
import org.jubaroo.mods.wurm.server.misc.Misc;
import org.jubaroo.mods.wurm.server.server.*;
import org.jubaroo.mods.wurm.server.vehicles.CustomVehicleCreationEntries;
import org.jubaroo.mods.wurm.server.vehicles.CustomVehicles;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import static org.jubaroo.mods.wurm.server.ModConfig.*;

public class Requiem implements WurmServerMod, ServerStartedListener, ServerShutdownListener, PlayerLoginListener, ItemTemplatesCreatedListener, Configurable, PreInitable, ServerPollListener, Initable, PlayerMessageListener, ChannelMessageListener {
    public static String VERSION = "3.0";
    public static Logger logger = Logger.getLogger(String.format("%s %s", Requiem.class.getName(), VERSION));

    @Override
    public void onItemTemplatesCreated() {
        RequiemLogging.logInfo("onItemTemplatesCreated called");
        try {
            RequiemLogging.logInfo("======= Registering Custom Items And Vehicles=======");
            CustomItems.registerCustomItems();
            CustomVehicles.registerCustomVehicles();
            RequiemLogging.logInfo("======= Registering Custom Creatures & Mount Settings For Creatures=======");
            CustomCreatures.registerCustomCreatures();
            if (!disableVehicleMods) {
                CustomMountSettings.registerCustomMountSettings();
            }
            if (!disableItemMods) {
                RequiemLogging.logInfo("======= Registering Custom Items Creation Entries =======");
                CustomItemCreationEntries.registerCustomItemCreationEntries();
            }
            if (!disableVehicleMods) {
                RequiemLogging.logInfo("======= Registering Custom Vehicle Creation Entries =======");
                CustomVehicleCreationEntries.registerCustomVehicleCreationEntries();
            }
            if (!disableEntireMod) {
                RequiemLogging.logInfo("======= Registering Modifications Of New Items =======");
                ItemMod.modifyItemsOnCreated();
            }
        } catch (IllegalArgumentException | ClassCastException | IOException e) {
            RequiemLogging.logException("Error in onItemTemplatesCreated()", e);
        }
        RequiemLogging.logInfo("all onItemTemplatesCreated completed");
    }

    @Override
    public void configure(final Properties properties) {
        Config.doConfig(properties);
    }

    @Override
    public void preInit() {
        RequiemLogging.logInfo("preInit called");
        try {
            if (!disableEntireMod) {
                if (!disablePreInit) {
                    PreInitialize.preInit();
                }
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
            if (!disableEntireMod) {
                if (!disableInit) {
                    try {
                        RequiemLogging.logInfo("Started Init.init()");
                        Initialize.init();
                    } catch (Throwable e) {
                        RequiemLogging.logException("Error in init()", e);
                    }
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
            if (!disableEntireMod) {
                if (!disableOnServerStarted) {
                    OnServerStarted.onServerStarted();
                    ChatHandler.serverStarted();
                }
            }
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
        if (!disableOnPlayerMessage) {
            String[] argv = ArgumentTokenizer.tokenize(message).toArray(new String[0]);
            if (communicator.player.getPower() >= 4 && message.startsWith("#discordreconnect")) {
                DiscordHandler.initJda();
                communicator.sendNormalServerMessage("Discord reconnected command sent");
                return MessagePolicy.DISCARD;
            } else if (communicator.player.getPower() >= MiscConstants.POWER_HERO && message.startsWith("#eventmsg")) {
                String msg = message.replace("#eventmsg", "").trim();
                ChatHandler.setUpcomingEvent(msg);
                if (msg.length() > 0)
                    communicator.sendNormalServerMessage(String.format("Set event line: %s", msg));
                else
                    communicator.sendNormalServerMessage("Cleared event line.");
                return MessagePolicy.DISCARD;
            } else if (!message.startsWith("#") && !message.startsWith("/")) {
                CustomChannel chan = CustomChannel.findByIngameName(title);
                if (chan != null) {
                    if (chan.canPlayersSend)
                        ChatHandler.handleGlobalMessage(chan, communicator, message);
                    return MessagePolicy.DISCARD;
                } else return MessagePolicy.PASS;
            }

            //TODO
            // fix the help commands not calling guards in the new chat system
            else if (message.equals("/help me ") || (message.equals("help") || message.equals("guard!") || message.equals("guard") || message.equals("guards") || message.equals("guards!") || message.equals("help guard") || message.equals("help guards"))) {
                communicator.getPlayer().getCommunicator().sendNormalServerMessage("Guards have been called!");
                communicator.getPlayer().callGuards();
                return MessagePolicy.DISCARD;
            }

            //else if (communicator.player.getPower() > 0 && message.startsWith("#")) {
            //    return CommandHandler.handleCommand(communicator, message);
            //}

            else if (OnServerStarted.cmdtool.runWurmCmd(communicator.getPlayer(), argv)) {
                return MessagePolicy.PASS;
            } else {
                return MessagePolicy.PASS;
            }
        } else {
            return null;
        }

    }

    @Override
    public MessagePolicy onKingdomMessage(Message message) {
        if (!disableOnKingdomMessage) {
            return OnKingdomMessage.onKingdomMessage(message);
        } else {
            return null;
        }
    }

    @Override
    public void onPlayerLogin(Player player) {
        try {
            if (!disableEntireMod) {
                if (!disableOnPlayerLogin) {
                    OnPlayerLogin.onPlayerLogin(player);
                }
            }
        } catch (IllegalArgumentException |
                ClassCastException e) {
            RequiemLogging.logException("Error in onPlayerLogin()", e);
        }
    }

    @Override
    public void onPlayerLogout(Player player) {
        try {
            if (!disableEntireMod) {
                if (!disableOnPlayerLogout) {
                    OnPlayerLogout.onPlayerLogout(player);
                }
            }
        } catch (IllegalArgumentException |
                ClassCastException e) {
            RequiemLogging.logException("Error in onPlayerLogin()", e);
        }
    }

    @Override
    public void onServerPoll() {
        if (!disableEntireMod) {
            if (!disablePollingMods) {
                DiscordHandler.poll();
                OnServerPoll.onServerPoll();
                Misc.noobTips();
                PollPortals.pollPortal();
            }
        }
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    //TODO
    // Action for guide to tell all server features
    // Add portal action into an action performer and make them a list instead of 4 different actions in the main right click menu
    // So just noticed in the ship building menu, knarr's don't have custom sail options.  Also, Caravels are listed as "...caravels caravel," and caravels and cogs are listed twice for each custom sail model.
    // add lava/fire creatures to Creature.doLavaDamage()
    // Login server is the Volcano Island map and the starter village is trying to flee and you have to choose a portal to leave through
    // Make special login banners for the test server
    // I wish more enchants could be put on shields..seems like shared pain should be allowed
    // Make lesser fire crystal have 3 charges
    // ---> Horse traits do not show up in GM trait set list after trait 40 <--- important
    // action for patreons to use a mailbox to order more blueprints
    // Labyrinthia deed that has a maze that changes once a week. the maze has a chest in the middle with random loot each time
    // change the descriptions of the animals that relate to real life
    // Need to make a healing fountain action and item for the arena. new model also!!!
    // Make an action/question to reset ALL skills to 1 and apply a certain permanent "paragon buff" that will increase each time you reset all skills. Example req. would be to have x amount of skills at 99 or above
    // Hook into Player.spawn? and add monthly buffs
    // Add treasure goblins and such to the leaderboard mod? -> check if done already haha <-
    // Make factions with reputation that can be gained?
    // tablets of fryian spawn inside buildings, stop that!
    // Item to summon a creature. If players fail to kill it after a set amount of time, the dragon disappears. Possibly make it a question as to what to summon?
    // make noob tips display once in a while unless the player types a certain command to turn it off? Database function required?
    // golden drake with treasure goblin effect
    // reindeer corpse
    // make a new action for milking whales
    // zombie doing a rummage action looping
    // Make HotA creatures (in HotA zone) have a chance to give out a token or something to exchange for goods at a special vendor
    // Make eyes able to be put into bulk storage
    // Make different types of beds give different sleep bonus rates
    // Make capes work!!!
    // Portable bank or another item to be used as one
    // Redo the mimic creature to be a real chest with a custom action that turns it into a monster
    // Make an action/question to trade in a HotA statue for a different kind?
    // Make weak "cannibal tribes" that roam globally like The Forest?
    // Make new undead creature that only spawns at night and leaves no body when it dies so they can mass de-spawn at dawn like a wraith
    // Make a headless horseman with an invisible helm so that it looks like he has no head? Or figure out the pumpkin helm
    // Auto turn unknown grass tiles into normal grass tiles <--- hopefully wont need on new map
    // Fix rainbow unicorns foal to adult transition
    // White hell horse spawns a black one now and then
    // Copy bdews code from halloween broom to make carpet work
    // Alchemy overhaul. Make health potions, favor potions, etc?
    // Players with high Proteins value will receive a bonus to Body skill gain and it's sub-skills?
    // Make enchant grass ability from crystal apply the effect to a 3x3 area
    // Wand Of Transmutation for Ground Types, not ore
    // Scrolls of summoning for each creature
    // Necromancer that equips black clothes, a staff and cast spells and has skeleton followers
    // Custom naming tool that will change the names of animals to whatever the player types in
    // Mark HotA zone with light beams when HotA is active?
    // A ring that works as a portal. Super hard to make and very expensive?
    // ---> player appreciation day on a certain day each year <---
    // Add new things to TempStates in com.wurmonline.server.items.TempStates or Item.tempMod <--- reference
    // make combat rating based on soul skill for DeathAction
    // keep the same color trait after rebirth
    // make the sub menu for both labyrinth actions work!!!
    // Make an action to pack up a wagon into a kit using Karma or some other "currency"
    // make The Cluckster spawn special chicks to attack the player when at half health
    // Fix supply depot spawn times <--------
    // absorb health through a corpse with death crystal?
    // Update Dyno bot to give an awesome greeting like the discord channel for the game Raft.
    // Make an NPC that sells animals
    // Make an NPC that sells titles
    // Make backpack of steel tools disappear when opened like treasure chest - currently does not work 100%
    // Make treasure chest a rare container of some kind. Maybe a new bank item that is portable?
    // Fix Cluckster and minions!!!
    // Make Life Crystal Rebirth action delete the corpse of the old one and show a cooldown message
    // Make Eternal Reservoirs use less fuel
    // Make bark harvest-able from any over-aged tree
    // Bag that only holds tools. Able to hold tools a normal backpack cannot
    // Make hitching post turned the correct way
    // Copy/paste action for terrain data to auxData on ebony wand
}
