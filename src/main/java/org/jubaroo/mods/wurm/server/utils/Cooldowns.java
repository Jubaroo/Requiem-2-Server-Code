package org.jubaroo.mods.wurm.server.utils;

import com.wurmonline.server.TimeConstants;

import java.util.HashMap;

public class Cooldowns {
    private static HashMap<String, Long> lastUses;

    static {
        Cooldowns.lastUses = new HashMap<>();
    }

    public static boolean isOnCooldown(final String playerEffect, final long cooldown) {
        return Cooldowns.lastUses.containsKey(playerEffect) && System.currentTimeMillis() < Cooldowns.lastUses.get(playerEffect) + cooldown;
    }

    public static long getPreviousUse(final String playerEffect) {
        if (!Cooldowns.lastUses.containsKey(playerEffect)) {
            return 0L;
        }
        return Cooldowns.lastUses.get(playerEffect);
    }

    public static void setUsed(final String playerEffect) {
        Cooldowns.lastUses.put(playerEffect, System.currentTimeMillis());
    }

    public static String timeRemaining(final String playerEffect, long cooldown) {
        final long expireTime = Cooldowns.lastUses.get(playerEffect) + cooldown;
        final long timeRemaining = expireTime - System.currentTimeMillis();
        if (timeRemaining > TimeConstants.MINUTE_MILLIS) {
            return String.format("%d minute(s)", timeRemaining / TimeConstants.MINUTE_MILLIS);
        } else {
            return String.format("%d seconds", timeRemaining / TimeConstants.SECOND_MILLIS);
        }
    }

}