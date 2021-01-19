package org.jubaroo.mods.wurm.server.server;

import com.wurmonline.server.Servers;
import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.WurmCalendar;
import com.wurmonline.server.players.Player;
import com.wurmonline.shared.constants.EffectConstants;
import com.wurmonline.shared.constants.Enchants;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.communication.discord.DiscordHandler;
import org.jubaroo.mods.wurm.server.misc.CustomTitles;
import org.jubaroo.mods.wurm.server.misc.Misc;
import org.jubaroo.mods.wurm.server.misc.MiscHooks;
import org.jubaroo.mods.wurm.server.misc.database.DatabaseHelper;
import org.jubaroo.mods.wurm.server.misc.database.holidays.*;
import org.jubaroo.mods.wurm.server.server.constants.EffectsConstants;
import org.jubaroo.mods.wurm.server.server.constants.MessageConstants;
import org.jubaroo.mods.wurm.server.server.constants.ToggleConstants;
import org.jubaroo.mods.wurm.server.tools.CreatureTools;
import org.jubaroo.mods.wurm.server.tools.EffectsTools;

public class OnPlayerLogin {

    private static void onLogin(Player player) {
        byte k = MiscHooks.creatureKingdom(player, player.getKingdomId());
        if (k != player.getKingdomId()) MiscHooks.updateKingdom(player, k);
    }

    public static void onPlayerLogin(Player player) {
        try {
            DatabaseHelper.onPlayerLogin(player);
            EffectsTools.sendBeam(player, EffectConstants.RIFT_SPAWN, EffectsConstants.tileX, EffectsConstants.tileY, EffectsConstants.tileZ);
            OnPlayerLogin.loginAnnouncement(player);
            //DatabaseHelper.steamIdDatabase(player);
            OnPlayerLogin.onLogin(player);
            //OnPlayerLogin.holidayLogin(player);
            if (!Servers.isThisLoginServer()) {
                OnPlayerLogin.addBuffsToPlayersOnLogin(player);
                OnPlayerLogin.addMonthlyBuffsToPlayers(player);
                CustomTitles.playerLoginTitles(player);
                Misc.noobTips();
            }
            //LoginServerEffect.playerLogin(player);
            if (player.getPlayingTime() <= TimeConstants.DAY) {
                player.getCommunicator().sendAlertServerMessage(Misc.noobMessage, (byte) 1);
            }
        } catch (IllegalArgumentException |
                ClassCastException e) {
            RequiemLogging.logException("Error in onPlayerLogin()", e);
        }
    }

    public static void addMonthlyBuffsToPlayers(Player player) {
        if (Misc.isDayOfMonth(1)) {
            CreatureTools.applyBuff(player, Enchants.CRET_CURIOSITY_HEALTH, 100.0f, (int) TimeConstants.HOURS18_MILLIS / 2, false);
            player.getCommunicator().sendServerMessage(MessageConstants.dayOneBuffMessage, 0, 255, 0);
        }
        if (Misc.isDayOfMonth(7)) {
            CreatureTools.applyBuff(player, Enchants.CRET_EXCEL, 100.0f, (int) TimeConstants.HOURS18_MILLIS / 2, false);
            player.getCommunicator().sendServerMessage(MessageConstants.daySevenBuffMessage, 0, 255, 0);
        }
        if (Misc.isDayOfMonth(15)) {
            CreatureTools.applyBuff(player, Enchants.CRET_CURIOSITY_FOOD, 100.0f, (int) TimeConstants.HOURS18_MILLIS / 2, false);
            player.getCommunicator().sendServerMessage(MessageConstants.dayFifteenBuffMessage, 0, 255, 0);
        }
        if (Misc.isDayOfMonth(23)) {
            CreatureTools.applyBuff(player, Enchants.CRET_TRUEHIT, 100.0f, (int) TimeConstants.HOURS18_MILLIS / 2, false);
            player.getCommunicator().sendServerMessage(MessageConstants.dayTwentyThreeBuffMessage, 0, 255, 0);
        }
    }

    private static void holidayLogin(Player player) {
        if (!Servers.isThisLoginServer()) {
            if (WurmCalendar.isEaster() | Servers.isThisATestServer()) {
                String message = "Happy Easter! Be on the lookout for the Easter Bunny and its eggs!";
                player.getCommunicator().sendServerMessage(message, 0, 255, 0);
            }
            if (Holidays.isRequiemValentinesDay() | Servers.isThisATestServer()) {
                String message = "Happy Valentines Day!";
                RequiemValentinesGift.onPlayerLogin(player);
                player.getCommunicator().sendServerMessage(message, 0, 255, 0);
            }
            if (Holidays.isRequiemIndependenceDay() | Servers.isThisATestServer()) {
                RequiemIndependenceDayGift.onPlayerLogin(player);
                String message = "Happy Independence Day!";
                player.getCommunicator().sendServerMessage(message, 0, 255, 0);
            }
            if (Holidays.isRequiemCanadaDay() | Servers.isThisATestServer()) {
                RequiemCanadaDayGift.onPlayerLogin(player);
                String message = "Happy Canada Day!";
                player.getCommunicator().sendServerMessage(message, 0, 255, 0);
            }
            if (Holidays.isRequiemVictoriaDay() | Servers.isThisATestServer()) {
                RequiemVictoriaDayGift.onPlayerLogin(player);
                String message = "Happy Victoria Day!";
                player.getCommunicator().sendServerMessage(message, 0, 255, 0);
            }
            if (Holidays.isRequiemThanksgiving() | Servers.isThisATestServer()) {
                RequiemThanksgivingGift.onPlayerLogin(player);
                String message = "Happy Thanksgiving Day!";
                player.getCommunicator().sendServerMessage(message, 0, 255, 0);
            }
            if (Holidays.isRequiemHalloween() | Servers.isThisATestServer()) {
                String message = "Happy Halloween! The creepy crawly creatures are out and about. Go out and find some Halloween fun, but don't eat too many sweets!";
                player.getCommunicator().sendServerMessage(message, 0, 255, 0);
            }
            if (Holidays.isRequiemAnniversary() | Servers.isThisATestServer()) {
                RequiemAnniversaryGift.onPlayerLogin(player);
                String message = String.format("It is now Requiem of Wurm's %s anniversary! December 1st through December 14th, we are celebrating our %s year here at Requiem of Wurm. Thank you for your support and for choosing this server. See you next year!", RequiemAnniversaryGift.formattedYear, RequiemAnniversaryGift.formattedYear);
                player.getCommunicator().sendServerMessage(message, 0, 255, 0);
            }
            if (Holidays.isRequiemChristmas() | Servers.isThisATestServer()) {
                RequiemChristmasGift.onPlayerLogin(player);
                String message = "Merry Christmas to you! Enjoy your presents from Santa and be careful that the grinch does not pay you a visit.";
                player.getCommunicator().sendServerMessage(message, 0, 255, 0);
            }
            if (Holidays.isRequiemNewYear() | Servers.isThisATestServer()) {
                RequiemNewYearGift.onPlayerLogin(player);
                String message = "Happy New Year!";
                player.getCommunicator().sendServerMessage(message, 0, 255, 0);
            }
            if (Holidays.isRequiemStPatricksDay() | Servers.isThisATestServer()) {
                RequiemStPatrickDayGift.onPlayerLogin(player);
                String message = "Happy St. Patrick's Day!";
                player.getCommunicator().sendServerMessage(message, 0, 255, 0);
            }
            //if (Misc.isRequiemPatreon(player)) {
            //PatreonSleepPowderGift.onPlayerLogin(player);
            //}
        }
    }

    private static void addBuffsToPlayersOnLogin(Player player) {
        if (player.getPlayingTime() < TimeConstants.DAY_MILLIS) {
            CreatureTools.applyBuff(player, Enchants.CRET_CURIOSITY_HEALTH, 500.0f, (int) TimeConstants.HOURS18_MILLIS / 2, false);
            player.getCommunicator().sendSafeServerMessage("New players heal extremely fast!");
        }
    }

    private static void loginAnnouncement(Player player) {
        if (player.getPower() == 0)
            if (!ToggleConstants.disableDiscordReliance)
                DiscordHandler.sendToDiscord(CustomChannel.LOGINS, String.format("%s has logged in.", player.getNameWithoutPrefixes()));
    }

}
