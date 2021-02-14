package org.jubaroo.mods.wurm.server.server.constants;

import org.jubaroo.mods.wurm.server.misc.Misc;

import java.util.Calendar;

/**
 * Created by Jubaroo on 2/12/2021
 */

public class Constants {
        public static int serverBirthYear = 2021;
        public static int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        public static String formattedYear = Misc.formatYear(currentYear - serverBirthYear);
}
