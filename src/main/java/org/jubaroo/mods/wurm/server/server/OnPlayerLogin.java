package org.jubaroo.mods.wurm.server.server;

import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.WurmCalendar;
import com.wurmonline.server.players.Player;
import com.wurmonline.shared.constants.Enchants;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.communication.discord.DiscordHandler;
import org.jubaroo.mods.wurm.server.creatures.CreatureTools;
import org.jubaroo.mods.wurm.server.misc.CustomTitles;
import org.jubaroo.mods.wurm.server.misc.Misc;
import org.jubaroo.mods.wurm.server.misc.MiscHooks;
import org.jubaroo.mods.wurm.server.misc.database.DatabaseHelper;
import org.jubaroo.mods.wurm.server.misc.database.holidays.*;
import org.jubaroo.mods.wurm.server.tools.RequiemTools;

public class OnPlayerLogin {

    private static void onLogin(Player player) {
        byte k = MiscHooks.creatureKingdom(player, player.getKingdomId());
        if (k != player.getKingdomId()) MiscHooks.updateKingdom(player, k);
    }

    public static void onPlayerLogin(Player player) {
        try {
            OnPlayerLogin.loginAnnouncement(player);
            DatabaseHelper.onPlayerLogin(player);
            DatabaseHelper.steamIdDatabase(player);
            OnPlayerLogin.onLogin(player);
            OnPlayerLogin.holidayLogin(player);
            OnPlayerLogin.addBuffsToPlayersOnLogin(player);
            OnPlayerLogin.addMonthlyBuffsToPlayers(player);
            CustomTitles.playerLoginTitles(player);
            Misc.noobTips();
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
            player.getCommunicator().sendServerMessage(Constants.dayOneBuffMessage, 0, 255, 0);
        }
        if (Misc.isDayOfMonth(7)) {
            CreatureTools.applyBuff(player, Enchants.CRET_EXCEL, 100.0f, (int) TimeConstants.HOURS18_MILLIS / 2, false);
            player.getCommunicator().sendServerMessage(Constants.daySevenBuffMessage, 0, 255, 0);
        }
        if (Misc.isDayOfMonth(15)) {
            CreatureTools.applyBuff(player, Enchants.CRET_CURIOSITY_FOOD, 100.0f, (int) TimeConstants.HOURS18_MILLIS / 2, false);
            player.getCommunicator().sendServerMessage(Constants.dayFifteenBuffMessage, 0, 255, 0);
        }
        if (Misc.isDayOfMonth(23)) {
            CreatureTools.applyBuff(player, Enchants.CRET_TRUEHIT, 100.0f, (int) TimeConstants.HOURS18_MILLIS / 2, false);
            player.getCommunicator().sendServerMessage(Constants.dayTwentyThreeBuffMessage, 0, 255, 0);
        }
    }

    private static void holidayLogin(Player player) {
        if (WurmCalendar.isEaster() | RequiemTools.isPrivateTestServer()) {
            String message = "Happy Easter! Be on the lookout for the Easter Bunny and its eggs!";
            player.getCommunicator().sendServerMessage(message, 0, 255, 0);
        }
        if (Holidays.isRequiemValentinesDay() | RequiemTools.isPrivateTestServer()) {
            String message = "Happy Valentines Day!";
            RequiemValentinesGift.onPlayerLogin(player);
            player.getCommunicator().sendServerMessage(message, 0, 255, 0);
        }
        if (Holidays.isRequiemIndependenceDay() | RequiemTools.isPrivateTestServer()) {
            RequiemIndependenceDayGift.onPlayerLogin(player);
            String message = "Happy Independence Day!";
            player.getCommunicator().sendServerMessage(message, 0, 255, 0);
        }
        if (Holidays.isRequiemCanadaDay() | RequiemTools.isPrivateTestServer()) {
            RequiemCanadaDayGift.onPlayerLogin(player);
            String message = "Happy Canada Day!";
            player.getCommunicator().sendServerMessage(message, 0, 255, 0);
        }
        if (Holidays.isRequiemVictoriaDay() | RequiemTools.isPrivateTestServer()) {
            RequiemVictoriaDayGift.onPlayerLogin(player);
            String message = "Happy Victoria Day!";
            player.getCommunicator().sendServerMessage(message, 0, 255, 0);
        }
        if (Holidays.isRequiemThanksgiving() | RequiemTools.isPrivateTestServer()) {
            RequiemThanksgivingGift.onPlayerLogin(player);
            String message = "Happy Thanksgiving Day!";
            player.getCommunicator().sendServerMessage(message, 0, 255, 0);
        }
        if (Holidays.isRequiemHalloween() | RequiemTools.isPrivateTestServer()) {
            String message = "Happy Halloween! The creepy crawly creatures are out and about. Go out and find some Halloween fun, but don't eat too many sweets!";
            player.getCommunicator().sendServerMessage(message, 0, 255, 0);
        }
        if (Holidays.isRequiemAnniversary() | RequiemTools.isPrivateTestServer()) {
            RequiemAnniversaryGift.onPlayerLogin(player);
            String message = String.format("It is now Requiem of Wurm's %s anniversary! December 1st through December 14th, we are celebrating our %s year here at Requiem of Wurm. Thank you for your support and for choosing this server. See you next year!", RequiemAnniversaryGift.formattedYear, RequiemAnniversaryGift.formattedYear);
            player.getCommunicator().sendServerMessage(message, 0, 255, 0);
        }
        if (Holidays.isRequiemChristmas() | RequiemTools.isPrivateTestServer()) {
            RequiemChristmasGift.onPlayerLogin(player);
            String message = "Merry Christmas to you! Enjoy your presents from Santa and be careful that the grinch does not pay you a visit.";
            player.getCommunicator().sendServerMessage(message, 0, 255, 0);
        }
        if (Holidays.isRequiemNewYear() | RequiemTools.isPrivateTestServer()) {
            RequiemNewYearGift.onPlayerLogin(player);
            String message = "Happy New Year!";
            player.getCommunicator().sendServerMessage(message, 0, 255, 0);
        }
        if (Holidays.isRequiemStPatricksDay() | RequiemTools.isPrivateTestServer()) {
            RequiemStPatrickDayGift.onPlayerLogin(player);
            String message = "Happy St. Patrick's Day!";
            player.getCommunicator().sendServerMessage(message, 0, 255, 0);
        }
        //if (Misc.isRequiemPatreon(player)) {
        //PatreonSleepPowderGift.onPlayerLogin(player);
        //}
    }

    private static void addBuffsToPlayersOnLogin(Player player) {
        if (player.getPlayingTime() < TimeConstants.DAY_MILLIS) {
            CreatureTools.applyBuff(player, Enchants.CRET_CURIOSITY_HEALTH, 500.0f, (int) TimeConstants.HOURS18_MILLIS / 2, false);
            player.getCommunicator().sendSafeServerMessage("New players heal extremely fast!");
        }
    }

    private static void loginAnnouncement(Player player) {
        if (player.getPower() == 0)
            if (!Constants.disableDiscordReliance)
                DiscordHandler.sendToDiscord(CustomChannel.EVENTS, String.format("%s has logged in.", player.getNameWithoutPrefixes()));
    }

}
