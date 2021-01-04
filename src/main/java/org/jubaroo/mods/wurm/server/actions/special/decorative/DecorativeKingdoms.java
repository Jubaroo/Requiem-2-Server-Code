package org.jubaroo.mods.wurm.server.actions.special.decorative;

import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;

import java.util.Arrays;
import java.util.List;

public enum DecorativeKingdoms {
    Invalid("Invalid", ""),
    JennKellon("Jenn-Kellon", "jenn.", Applicables.Tabard, Applicables.Flag, Applicables.Banner, Applicables.TallBanner, Applicables.MillitaryTent, Applicables.Pavillion, Applicables.Wagon, Applicables.Tower),
    MolRehan("Mol Rehan", "molr.", Applicables.Tabard, Applicables.Flag, Applicables.Banner, Applicables.TallBanner, Applicables.MillitaryTent, Applicables.Pavillion, Applicables.Wagon, Applicables.Tower),
    HOTS("Horde of the Summoned", "hots.", Applicables.Tabard, Applicables.Flag, Applicables.Banner, Applicables.TallBanner, Applicables.MillitaryTent, Applicables.Pavillion, Applicables.Wagon, Applicables.Tower),
    Freedom("Freedom Islands", "free.", Applicables.Flag, Applicables.Banner, Applicables.TallBanner, Applicables.MillitaryTent, Applicables.Pavillion, Applicables.Wagon, Applicables.Tower),
    JKPMK("Kingdom of Jenn-Kellon", "zjen.", Applicables.Tabard, Applicables.Flag, Applicables.Banner, Applicables.TallBanner, Applicables.MillitaryTent, Applicables.Wagon),
    MRPMK("Empire of Mol Rehan", "empi.", Applicables.Tabard, Applicables.Flag, Applicables.Banner, Applicables.TallBanner, Applicables.MillitaryTent, Applicables.Wagon),
    KingdomOfSol("Kingdom of Sol", "king.", Applicables.Tabard, Applicables.Flag, Applicables.Banner, Applicables.Wagon),
    BlackLegion("Black Legion", "blac.", Applicables.Tabard, Applicables.Flag, Applicables.Banner, Applicables.TallBanner, Applicables.Wagon),
    //Crusaders("Crusaders", "thec.", Applicables.Tabard, Applicables.Flag, Applicables.Banner, Applicables.TallBanner, Applicables.MillitaryTent, Applicables.Wagon),
    Pandemonium("Pandemonium", "pand.", Applicables.Tabard, Applicables.Flag, Applicables.Banner, Applicables.TallBanner, Applicables.MillitaryTent, Applicables.Wagon),
    Dreadnought("Dreadnaught Dynasty", "drea.", Applicables.Tabard, Applicables.Flag, Applicables.Banner, Applicables.TallBanner, Applicables.MillitaryTent, Applicables.Wagon),
    Ebonaura("Ebonaura", "ebon.", Applicables.Tabard, Applicables.Flag, Applicables.Banner, Applicables.TallBanner, Applicables.Wagon),
    Rome("The Roman Republic", "ther.", Applicables.Tabard, Applicables.Flag, Applicables.Banner, Applicables.TallBanner, Applicables.Wagon),
    Valhalla("Valhalla Descendants", "valh.", Applicables.Tabard, Applicables.Flag, Applicables.Banner, Applicables.TallBanner, Applicables.Wagon),
    Macedon("Macedon", "mace.", Applicables.Tabard, Applicables.Flag, Applicables.Banner, Applicables.TallBanner, Applicables.Wagon),
    Abralon("Kingdom of Abralon", "thek.", Applicables.Tabard),
    ValhallaAlt("Valhalla Descendants (Alt)", "Valhalla Descendants", "yval.", Applicables.Tabard, Applicables.Wagon),
    WurmUni("Wurm University", "wurm.", Applicables.Tabard, Applicables.Wagon),
    WagonerEmpty("Wagoner (empty)", "Wagoner", "wagoner.unloaded.", Applicables.Wagon),
    WagonerLoaded("Wagoner (loaded)", "Wagoner", "wagoner.loaded.", Applicables.Wagon),
    Neutral("Neutral", "", Applicables.Tower),
    LegionofAnubis("Legion of Anubis", "legi.", Applicables.Wagon, Applicables.Flag, Applicables.Banner, Applicables.TallBanner, Applicables.MillitaryTent, Applicables.Tabard),
    ApocalypseOrder("Apocalypse Order", "apoc.", Applicables.Wagon, Applicables.Flag, Applicables.Banner, Applicables.TallBanner, Applicables.MillitaryTent, Applicables.Tabard),
    SKYBEAR("Skybear", "skybear.", Applicables.Wagon),
    DRAGONELF("Dragonelf", "dragonelf.", Applicables.Wagon),
    REAPER("Reaper", "reaper.", Applicables.Wagon),
    BLACK_WITCH("Black Witch", "blackwitch.", Applicables.Wagon),
    PALADIN("Paladin", "paladin.", Applicables.Wagon),
    WHITE_KNIGHT("White Knight", "whiteknight.", Applicables.Wagon),
    STAR_ELF("Star Elf", "elfnature.", Applicables.Wagon),
    WITCHES("Witches", "witches.", Applicables.Wagon),
    DEATH_ANGEL("Death Angel", "deathangel.", Applicables.Wagon),
    ICE_DRAGON("Ice Dragon", "icedragon.", Applicables.Wagon),
    BISON("Bison", "bison.", Applicables.Wagon),
    PURPLE_DRAGON("Purple Dragon", "purpledragon.", Applicables.Wagon),
    HORSEMAN_WAR("Horseman War", "horsemanwar.", Applicables.Wagon),
    UNICORN("Unicorn", "unicorn.", Applicables.Wagon),
    THE_KNIGHT("The Knight", "theknight.", Applicables.Wagon),
    DRAKO("Drako", "draco.", Applicables.Wagon),
    FAIRY_GIRL("Fairy Girl", "fairygirl.", Applicables.Wagon),
    FLOWERS("Flowers", "flower.", Applicables.Wagon),
    SPACE("Space", "space.", Applicables.Wagon),
    FAIRY_DRAGON("Fairy Dragon", "fairydragon.", Applicables.Wagon),
    BLACK_PATTERN("Black Patterned", "blackpattern.", Applicables.Wagon),
    PURPLE_PATTERN("Purple Patterned", "purplepattern.", Applicables.Wagon),
    RED_PATTERN("Red Patterned", "redpattern.", Applicables.Wagon),
    BLUE_PATTERN("Blue Patterned", "bluepattern.", Applicables.Wagon),
    YELLOW_PLAIN("Plain Yellow", "yellowplain.", Applicables.Wagon),
    LIME_PLAIN("Plain Lime", "limeplain.", Applicables.Wagon),
    GREEN_PLAIN("Plain Green", "greenplain.", Applicables.Wagon),
    FOREST_GREEN_PLAIN("Plain Forest Green", "forestgreenplain.", Applicables.Wagon),
    CYAN_PLAIN("Plain Cyan", "cyanplain.", Applicables.Wagon),
    BLUE_PLAIN("Plain Blue", "blueplain.", Applicables.Wagon),
    NAVY_BLUE_PLAIN("Plain Navy Blue", "navyblueplain.", Applicables.Wagon),
    INDIGO_PLAIN("Plain Indigo", "indigoplain.", Applicables.Wagon),
    VIOLET_PLAIN("Plain Violet", "violetplain.", Applicables.Wagon),
    MAGENTA_PLAIN("Plain Magenta", "magentaplain.", Applicables.Wagon),
    HOT_PINK_PLAIN("Plain Hot Pink", "hotpinkplain.", Applicables.Wagon),
    RED_PLAIN("Plain Red", "redplain.", Applicables.Wagon),
    ORANGE_PLAIN("Plain Orange", "orangeplain.", Applicables.Wagon),
    BROWN_PLAIN("Plain Brown", "brownplain.", Applicables.Wagon),
    GREY_PLAIN("Plain Grey", "greyplain.", Applicables.Wagon),
    BLACK_PLAIN("Plain Black", "blackplain.", Applicables.Wagon),
    WHITE_PLAIN("Plain White", "whiteplain.", Applicables.Wagon),
    WOODLAND_CAMO("Woodland Camo", "woodlandcamo.", Applicables.Wagon, Applicables.Tabard, Applicables.Banner, Applicables.Flag, Applicables.MillitaryTent, Applicables.TallBanner),
    BLUE_CAMO("Blue Camo", "bluecamo.", Applicables.Wagon),
    RED_CAMO("Red Camo", "redcamo.", Applicables.Wagon),
    PURPLE_CAMO("Purple Camo", "purplecamo.", Applicables.Wagon),
    BLACK_WHITE_CAMO("Black and White Camo", "blackwhitecamo.", Applicables.Wagon),
    DESERT_CAMO("Desert Camo", "desertcamo.", Applicables.Wagon),
    PINK_CAMO("Pink Camo", "pinkcamo.", Applicables.Wagon),
    YELLOW_CAMO("Yellow Camo", "yellowcamo.", Applicables.Wagon),
    ORANGE_CAMO("Orange Camo", "orangecamo.", Applicables.Wagon),
    USA("USA", "usaflag.", Applicables.Wagon),
    Canada("Canada", "canadaflag.", Applicables.Wagon),
    GERMANY("Germany", "germany.", Applicables.Wagon),
    UK("United Kingdom", "uk.", Applicables.Wagon),
    NETHERLANDS("Netherlands", "netherlands.", Applicables.Wagon),
    POLAND("Poland", "poland.", Applicables.Wagon),
    RUSSIA("Russia", "russia.", Applicables.Wagon),
    SOVIET_UNION("Soviet Union", "ussr.", Applicables.Wagon),
    BRAZIL("Brazil", "brazil.", Applicables.Wagon),
    SOUTH_AFRICA("South Africa", "southafrica.", Applicables.Wagon),
    GREECE("Greece", "greece.", Applicables.Wagon),
    DENMARK("Denmark", "denmark.", Applicables.Wagon),
    AUSTRALIA("Australia", "australia.", Applicables.Wagon),
    ESTONIA("Estonia", "estonia.", Applicables.Wagon),
    YELLOW_PLAID("Plaid Yellow", "yellowplaid.", Applicables.Wagon),
    ORANGE_PLAID("Plaid Orange", "orangeplaid.", Applicables.Wagon),
    BLUE_GREEN_PLAID("Plaid Blue and Green", "bluegreenplaid.", Applicables.Wagon),
    GREEN_PLAID("Plaid Green", "greenplaid.", Applicables.Wagon),
    BLACK_WHITE_PLAID("Plaid Black and White", "blackwhiteplaid.", Applicables.Wagon),
    TOPLESS("Topless", "topless.", Applicables.Wagon),
    SCREEN("Netted", "screen.", Applicables.Wagon),
    REQUIEM("Requiem", "requ.", Applicables.Tabard),
    FROST_HAVEN("Frost Haven", "frosthaven.", Applicables.Tabard, Applicables.Banner),
    CROSSBOW("Crossbow", "crossbow.", Applicables.TallBanner),
    CROWN("Crown", "crown.", Applicables.TallBanner),
    DEER("Deer", "deer.", Applicables.TallBanner),
    FIRE_BLOOD("Fire blood", "fireblood.", Applicables.TallBanner),
    B_FREE("Black Freedom", "blackfreedom.", Applicables.TallBanner),
    HORSE_SWORD("Horse and sword", "horsesword.", Applicables.TallBanner),
    HORSE("Horse", "horse.", Applicables.TallBanner),
    R_HOTS("Red Horde of the Summoned", "redhots.", Applicables.TallBanner),
    B_JK("Black Jenn-Kellon", "blackjk.", Applicables.TallBanner),
    LION("Lion", "lion.", Applicables.TallBanner),
    B_MR("Black Mol-Rehan", "blackmr.", Applicables.TallBanner),
    SCROPION("Scorpion", "scropion.", Applicables.TallBanner),
    SKULL("Skull", "skull.", Applicables.TallBanner),
    T_STAR("Throwing Star", "throwingstar.", Applicables.TallBanner),
    WINTER_COMING("Winter Coming", "wintercoming.", Applicables.TallBanner),
    BLOODY_AXE("Bloody Axe", "bloodyaxe.", Applicables.Banner),
    BURNING_TREE("Burning Tree", "burningtree.", Applicables.Banner),
    CHAIN("Chain", "chain.", Applicables.Banner),
    BONE("Bone", "bone.", Applicables.Banner),
    SPIKE("Spike", "spike.", Applicables.Banner),
    MURLOC("Murloc", "murloc.", Applicables.Banner),
    HORNS("Horns", "horns.", Applicables.Banner),
    GATE("Gate", "gate.", Applicables.Banner),
    BLOODY_MACHETE("Bloody Machete", "bloodymachete.", Applicables.Banner),
    BLOOD_HAND("Blood Hand", "bloodhand.", Applicables.Banner),
    BLACK_RED_TORN("Black and Red Torn", "blackredtorn.", Applicables.Banner),
    EAGLE("Eagle", "eagle.", Applicables.Flag, Applicables.Banner);

    public final String name, display, suffix;
    public final List<Applicables> applicables;

    DecorativeKingdoms(String name, String suffix, Applicables... applicables) {
        this(name, name, suffix, applicables);
    }

    DecorativeKingdoms(String name, String display, String suffix, Applicables... applicables) {
        this.name = name;
        this.suffix = suffix;
        this.display = display;
        this.applicables = Arrays.asList(applicables);
    }

    public static boolean isValidKingdom(byte id) {
        return id > 0 && id < values().length;
    }


    public static String getModelSuffix(byte id) {
        if (isValidKingdom(id))
            return values()[id].suffix;
        else
            return "";
    }

    public static String getName(byte id) {
        if (isValidKingdom(id))
            return values()[id].name;
        else
            return "unknown";
    }


    public static String getDisplay(byte id) {
        if (isValidKingdom(id))
            return values()[id].display;
        else
            return "unknown";
    }

    public static boolean canSetKingdom(Item item, byte kingdom) {
        if (!isValidKingdom(kingdom)) return false;
        DecorativeKingdoms rec = values()[kingdom];
        for (Applicables app : rec.applicables) {
            if (app.handler.canApply(item)) return true;
        }
        return false;
    }

    public static boolean canSetAnyKingdom(Item item) {
        for (Applicables app : Applicables.values()) {
            if (app.handler.canApply(item)) return true;
        }
        return false;
    }

    public static void setKingdom(Item item, byte kingdom) {
        if (!isValidKingdom(kingdom)) return;
        DecorativeKingdoms rec = values()[kingdom];
        for (Applicables app : rec.applicables) {
            if (app.handler.canApply(item)) {
                app.handler.doApply(item, kingdom);
                return;
            }
        }
    }

    public boolean canSetKingdom(Item item) {
        for (Applicables app : applicables) {
            if (app.handler.canApply(item)) return true;
        }
        return false;
    }

    // ===================

    public enum Applicables {
        Tabard(new AuxApplicable(ItemList.tabard)),
        Flag(new AuxApplicable(ItemList.flagKingdom)),
        Banner(new AuxApplicable(ItemList.bannerKingdom)),
        TallBanner(new AuxApplicable(ItemList.tallKingdomBanner)),
        ShopSign(new AuxApplicable(ItemList.signShop)),
        //Tapestry(new TapestryApplicable()),
        MillitaryTent(new AuxApplicable(ItemList.tentMilitary)),
        Pavillion(new AuxApplicable(ItemList.pavilion)),
        Wagon(new Data1Applicable(ItemList.wagon)),
        Tower(new TowerApplicable());
        //Ship(new ShipApplicable()),
        //SailingBoat(new Data1Applicable(ItemList.boatSailing));

        public final IApplicable handler;

        Applicables(IApplicable handler) {
            this.handler = handler;
        }
    }

    interface IApplicable {
        boolean canApply(Item item);

        void doApply(Item item, byte kingdom);
    }

    static class AuxApplicable implements IApplicable {
        private final int templateId;

        AuxApplicable(int templateId) {
            this.templateId = templateId;
        }

        @Override
        public boolean canApply(Item item) {
            return item.getTemplateId() == templateId;
        }

        @Override
        public void doApply(Item item, byte kingdom) {
            item.setAuxData(kingdom);
        }
    }

    static class Data1Applicable implements IApplicable {
        private final int templateId;

        Data1Applicable(int templateId) {
            this.templateId = templateId;
        }

        @Override
        public boolean canApply(Item item) {
            return item.getTemplateId() == templateId;
        }

        @Override
        public void doApply(Item item, byte kingdom) {
            item.setData1(kingdom);
        }
    }

    static class TowerApplicable implements IApplicable {
        @Override
        public boolean canApply(Item item) {
            int t = item.getTemplateId();
            return t == ItemList.guardTower || t == ItemList.guardTowerFreedom || t == ItemList.guardTowerHots || t == ItemList.guardTowerMol || t == ItemList.guardTowerNeutral;
        }

        @Override
        public void doApply(Item item, byte kingdom) {
            if (kingdom == JennKellon.ordinal())
                item.setTemplateId(ItemList.guardTower);
            else if (kingdom == MolRehan.ordinal())
                item.setTemplateId(ItemList.guardTowerMol);
            else if (kingdom == HOTS.ordinal())
                item.setTemplateId(ItemList.guardTowerHots);
            else if (kingdom == Freedom.ordinal())
                item.setTemplateId(ItemList.guardTowerFreedom);
            else if (kingdom == Neutral.ordinal())
                item.setTemplateId(ItemList.guardTowerNeutral);
        }
    }

}
