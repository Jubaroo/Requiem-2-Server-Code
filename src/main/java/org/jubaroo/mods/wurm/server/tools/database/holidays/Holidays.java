package org.jubaroo.mods.wurm.server.tools.database.holidays;

import com.wurmonline.server.EasterCalculator;
import com.wurmonline.server.WurmCalendar;

import java.time.Year;
import java.util.Calendar;

public class Holidays {

    // ============>>>>>> Months start at 0 <<<<<<============

    public static boolean isRequiemTreasureHunt() { // Oct 31 - Nov 4 8am-8am
        return WurmCalendar.nowIsBetween(8, 0, 31, 9, Year.now().getValue(), 8, 0, 4, 10, Year.now().getValue());
    }

    public static boolean isRequiemNewYear() { // Jan 1 - Jan 14 @Midnight
        return WurmCalendar.nowIsBetween(0, 1, 1, 0, Year.now().getValue(), 23, 59, 14, 0, Year.now().getValue());
    }

    public static boolean isRequiemMardiGras() { // Ash Wednesday is always 46 days before Easter, and Fat Tuesday is always the day before Ash Wednesday.
        Calendar c = EasterCalculator.findHolyDay(Calendar.getInstance().get(Calendar.YEAR));
        return WurmCalendar.nowIsAfter(10, 0, c.get(5) - 47, c.get(2), c.get(1));
    }

    public static boolean isRequiemValentinesDay() { // Feb 14
        return WurmCalendar.nowIsBetween(0, 1, 14, 1, Year.now().getValue(), 23, 59, 14, 1, Year.now().getValue());
    }

    // Store bededag, translated literally as Great Prayer Day or more loosely as General Prayer Day, "All Prayers" Day, Great Day of Prayers or Common Prayer Day, is a Danish holiday.
    public static boolean isRequiemStoreBededag() { //  Celebrated on the 4th Friday after Easter.
        return WurmCalendar.nowIsBetween(0, 1, 14, 1, Year.now().getValue(), 23, 59, 14, 1, Year.now().getValue());
    }

    public static boolean isRequiemStPatricksDay() { // March 17
        return WurmCalendar.nowIsBetween(0, 1, 17, 2, Year.now().getValue(), 23, 59, 17, 2, Year.now().getValue());
    }

    // 2019 date - Thursday, 21 March
    // 2020 date - Monday, 9 March
    // https://en.wikipedia.org/wiki/Holi
    public static boolean isRequiemHoliday() {
        return WurmCalendar.nowIsBetween(0, 1, 14, 1, Year.now().getValue(), 23, 59, 14, 1, Year.now().getValue());
    }

    //Victoria Day is a federal Canadian public holiday celebrated on the last Monday preceding May 25, in honour of Queen Victoria's birthday. As such, it is the Monday between the 18th to the 24th inclusive, and thus is always the penultimate Monday of May.
    public static boolean isRequiemVictoriaDay() { // may 24
        return WurmCalendar.nowIsBetween(0, 1, 24, 4, Year.now().getValue(), 23, 59, 20, 4, Year.now().getValue());
    }

    public static boolean isRequiemMidsummer() { // June 22
        return WurmCalendar.nowIsBetween(0, 1, 4, 6, Year.now().getValue(), 23, 59, 4, 6, Year.now().getValue());
    }

    // Canada Day is the national day of Canada. A federal statutory holiday, it celebrates the anniversary of July 1, 1867, the effective date of the Constitution Act, 1867 (then called the British North America Act, 1867), which united the three separate colonies of the Province of Canada, Nova Scotia, and New Brunswick into a single Dominion within the British Empire called Canada. Originally called Dominion Day (French: Le Jour de la Confédération), the holiday was renamed in 1982, the year the Canada Act was passed. Canada Day celebrations take place throughout the country, as well as in various locations around the world, attended by Canadians living abroad.
    public static boolean isRequiemCanadaDay() { // July 1
        return WurmCalendar.nowIsBetween(0, 1, 1, 6, Year.now().getValue(), 23, 59, 1, 6, Year.now().getValue());
    }

    public static boolean isRequiemIndependenceDay() { // July 4
        return WurmCalendar.nowIsBetween(0, 1, 4, 6, Year.now().getValue(), 23, 59, 4, 6, Year.now().getValue());
    }

    public static boolean isRequiemOktoberfest() { // Sept. 22 - Oct 7 @Midnight
        return WurmCalendar.nowIsBetween(0, 1, 4, 6, Year.now().getValue(), 23, 59, 4, 6, Year.now().getValue());
    }

    public static boolean isRequiemHalloween() { // Oct 23 - Nov 8 @Midnight
        return WurmCalendar.nowIsBetween(0, 1, 23, 9, Year.now().getValue(), 23, 59, 8, 10, Year.now().getValue());
    }

    public static boolean isRequiemThanksgiving() { // Nov 20 - Nov 30 @Midnight
        return WurmCalendar.nowIsBetween(0, 1, 20, 10, Year.now().getValue(), 23, 59, 30, 10, Year.now().getValue());
    }

    public static boolean isRequiemAnniversary() { // Dec 1 - Dec 13 @Midnight
        return WurmCalendar.nowIsBetween(0, 1, 1, 11, Year.now().getValue(), 23, 59, 13, 11, Year.now().getValue());
    }

    public static boolean isRequiemChristmas() { // Dec 20 - Dec 31 @Midnight
        return WurmCalendar.nowIsBetween(0, 1, 20, 11, Year.now().getValue(), 23, 59, 31, 11, Year.now().getValue());
    }

}
