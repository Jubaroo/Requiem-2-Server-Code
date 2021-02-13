package org.jubaroo.mods.wurm.server.server;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Servers;
import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.Materials;
import com.wurmonline.server.items.NoSuchTemplateException;
import com.wurmonline.server.players.Player;
import com.wurmonline.shared.constants.Enchants;
import com.wurmonline.shared.util.MaterialUtilities;
import org.jubaroo.mods.wurm.server.ModConfig;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.communication.discord.DiscordHandler;
import org.jubaroo.mods.wurm.server.misc.CustomTitles;
import org.jubaroo.mods.wurm.server.misc.Holidays;
import org.jubaroo.mods.wurm.server.misc.Misc;
import org.jubaroo.mods.wurm.server.misc.MiscHooks;
import org.jubaroo.mods.wurm.server.server.constants.MessageConstants;
import org.jubaroo.mods.wurm.server.tools.CreatureTools;
import org.jubaroo.mods.wurm.server.tools.database.DbChanges;
import org.jubaroo.mods.wurm.server.tools.database.DbHelper;

import static org.jubaroo.mods.wurm.server.ModConfig.disableDiscordReliance;

public class OnPlayerLogin {

    private static void onLogin(Player player) {
        byte k = MiscHooks.creatureKingdom(player, player.getKingdomId());
        if (k != player.getKingdomId()) MiscHooks.updateKingdom(player, k);
    }

    public static void onPlayerLogin(Player player) {
        try {
            if (!ModConfig.disableDatabaseChanges) {
                DbChanges.onPlayerLogin(player);
            }
            if (Misc.isRequiemPatreon(player) && DbHelper.canReceiveMonthlyGift(player, "PATREON")) {
                try {
                    if (Misc.isRequiemPatreon(player)) {
                        for (int i = 0; i < 5; i++) {
                            player.getInventory().insertItem(ItemFactory.createItem(ItemList.sleepPowder, 99f, Materials.MATERIAL_WHEAT, MaterialUtilities.COMMON, null), true);
                        }
                        player.getCommunicator().sendServerMessage("A shadowy figure appears behind you and your pocket gets heavier. You quickly turn around to see what is going on only to find that nobody is there. Your monthly sleep powder had been deposited into your inventory!", 255, 192, 203);
                    }
                } catch (FailedException | NoSuchTemplateException e) {
                    RequiemLogging.logException("Error in PatreonSleepPowderGift.giveItems()", e);
                    throw new RuntimeException(e);
                }
            }
            Holidays.holidayLogin(player);
            OnPlayerLogin.loginAnnouncement(player);
            OnPlayerLogin.onLogin(player);
            OnPlayerLogin.addBuffsToPlayersOnLogin(player);
            OnPlayerLogin.addMonthlyBuffsToPlayers(player);
            CustomTitles.playerLoginTitles(player);
            Misc.noobTips();
            if (player.getPlayingTime() <= TimeConstants.DAY) {
                player.getCommunicator().sendAlertServerMessage(Misc.noobMessage, MessageConstants.displayOnScreen);
            }
        } catch (IllegalArgumentException |
                ClassCastException e) {
            RequiemLogging.logException("Error in onPlayerLogin()", e);
        }
    }

    public static String dayOneBuffMessage = "You regenerate health faster on the first day of each month.";
    public static String daySevenBuffMessage = "Your defensive combat rating is increased on the seventh day of each month.";
    public static String dayFifteenBuffMessage = "You require food less on the fifteenth day of each month.";
    public static String dayTwentyThreeBuffMessage = "Your offensive combat rating is increased on the twenty third day of each month.";

    public static void addMonthlyBuffsToPlayers(Player player) {
        if (Misc.isDayOfMonth(1)) {
            CreatureTools.applyBuff(player, Enchants.CRET_CURIOSITY_HEALTH, 100f, (int) TimeConstants.DAY, false);
            player.getCommunicator().sendServerMessage(dayOneBuffMessage, 0, 255, 0);
        }
        if (Misc.isDayOfMonth(7)) {
            CreatureTools.applyBuff(player, Enchants.CRET_EXCEL, 100f, (int) TimeConstants.DAY, false);
            player.getCommunicator().sendServerMessage(daySevenBuffMessage, 0, 255, 0);
        }
        if (Misc.isDayOfMonth(15)) {
            CreatureTools.applyBuff(player, Enchants.CRET_CURIOSITY_FOOD, 100f, (int) TimeConstants.DAY, false);
            player.getCommunicator().sendServerMessage(dayFifteenBuffMessage, 0, 255, 0);
        }
        if (Misc.isDayOfMonth(23)) {
            CreatureTools.applyBuff(player, Enchants.CRET_TRUEHIT, 100f, (int) TimeConstants.DAY, false);
            player.getCommunicator().sendServerMessage(dayTwentyThreeBuffMessage, 0, 255, 0);
        }
    }

    private static void addBuffsToPlayersOnLogin(Player player) {
        if (player.getPlayingTime() < TimeConstants.DAY_MILLIS) {
            CreatureTools.applyBuff(player, Enchants.CRET_CURIOSITY_HEALTH, 500.0f, (int) TimeConstants.HOURS18_MILLIS / 2, false);
            player.getCommunicator().sendSafeServerMessage("New players heal much faster!");
        }
    }

    private static void loginAnnouncement(Player player) {
        if (player.getPower() < MiscConstants.POWER_HIGH_GOD)
            if (!disableDiscordReliance) {
                DiscordHandler.sendToDiscord(CustomChannel.LOGINS, String.format("[%s] Player %s has logged in.", Servers.getLocalServerName(), player.getName()));
                RequiemLogging.logInfo(String.format("[%s] Player %s has logged in.", Servers.getLocalServerName(), player.getName()));
            }
    }

}
