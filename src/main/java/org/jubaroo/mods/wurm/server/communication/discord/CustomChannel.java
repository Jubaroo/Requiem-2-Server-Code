package org.jubaroo.mods.wurm.server.communication.discord;

public enum CustomChannel {
    GLOBAL("Global", (byte) -1, true, false),
    HELP("Help", (byte) -2, true, false),
    INFO("Info", (byte) -3, false, false),
    TITLES("Titles", (byte) -4, false, false),
    EVENTS("Events", (byte) -5, false, false),
    TITAN("Titan", (byte) -6, false, false),
    UNIQUES("Uniques", (byte) -6, true, false),
    TRADE("Trade", (byte) -7, true, false),
    SERVER_STATUS("*", (byte) -8, false, true),
    TICKETS("*", (byte) -9, false, true),
    LOGINS("*", (byte) -10, false, true);

    public final String ingameName;
    public String discordName;
    public final byte kingdom;
    public final boolean canPlayersSend, discordOnly;

    CustomChannel(String ingameName, byte kingdom, boolean canPlayersSend, boolean discordOnly) {
        this.ingameName = ingameName;
        this.kingdom = kingdom;
        this.canPlayersSend = canPlayersSend;
        this.discordOnly = discordOnly;
    }

    public static CustomChannel findByIngameName(String name) {
        for (CustomChannel ch : values())
            if (name.equals(ch.ingameName))
                return ch;
        return null;
    }

    public static CustomChannel findByDiscordName(String name) {
        for (CustomChannel ch : values())
            if (ch.discordName != null && name.toLowerCase().equals(ch.discordName.toLowerCase()))
                return ch;
        return null;
    }

    public static CustomChannel findByKingdom(byte kingdom) {
        for (CustomChannel ch : values())
            if (kingdom == ch.kingdom)
                return ch;
        return null;
    }
}
