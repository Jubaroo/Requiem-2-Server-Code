package org.jubaroo.mods.wurm.server.misc;

import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.players.Titles;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import mod.sin.lib.Util;
import net.bdew.wurm.tools.server.ModTitles;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.communication.CustomChat;
import org.jubaroo.mods.wurm.server.server.constants.SkillIds;


public class CustomTitles {
    public static int DONOR = 10000;
    public static int PATREON = 10001;
    public static int SERPENTS_SIN = 10002;
    public static int DAME_DE_DRAGONS = 10003;
    public static int TITAN_SLAYER = 10050;
    public static int SPECTRAL = 10051;
    public static int KING_KONG = 10052;
    public static int WHITE_BUFFALO = 10053;
    public static int ARENA_CHAMPION = 10054;
    public static int TREASURE_HUNT = 10055;
    public static int ARCHITECT = 10100;
    public static int FLOYDIAN = 10101;
    public static int APOTHECARY = 10102;
    public static int REQUIEM = 10103;
    public static int CRAZY_CAT = 10104;
    public static int ZEALOT_LIBILA = 10105;
    public static int ZEALOT_FO = 10106;
    public static int ZEALOT_MAG = 10107;
    public static int ZEALOT_VYNORA = 10108;
    public static int DREAMER = 10109;
    public static int FEARLESS = 10110;
    public static int TITANS = 10111;
    public static int HAND_OF_ZEUS = 10112;
    public static int WARLORD = 10113;
    public static int DEVOTED = 10114;
    public static int ELDER = 10115;
    public static int PATRIOT = 10116;
    public static int SALTY = 10117;
    public static int ANCIENT = 10118;
    public static int TREASURE_GOBLIN = 10119;
    public static int EXPERIENCED = 10120;
    public static int GAME_MASTER = 11000;
    public static int DEVELOPER = 11001;
    public static int MACRO_ROYALTY = 11100;
    public static int DRAMA_QUEEN = 11101;
    public static int ZERGLING = 11102;
    public static int SPECIAL_TITLE = 11103;
    public static int PROPHET_EAR = 11104;
    public static int KOZA = 11105;
    public static int HOME_DECORATOR = 11106;
    public static int TAXIDERMY_MINOR = 12000;
    public static int TAXIDERMY_NORMAL = 12001;
    public static int TAXIDERMY_MASTER = 12002;
    public static int TAXIDERMY_EPIC = 12003;
    public static int TAXIDERMY_LEGENDARY = 12004;
    public static int GEMCRAFTING_MINOR = 12010;
    public static int GEMCRAFTING_NORMAL = 12011;
    public static int GEMCRAFTING_MASTER = 12012;
    public static int GEMCRAFTING_EPIC = 12013;
    public static int GEMCRAFTING_LEGENDARY = 12014;
    public static int RIFT_WARRIOR = 12015;
    public static int RIFT_SLAYER = 12016;
    public static int FACEBREYKER = 12017;
    public static int WIKI_MASTER = 12018;

    public static void register() {
        RequiemLogging.logInfo("Registering Custom Titles");
        ModTitles.addTitle(DONOR, "Donor");
        ModTitles.addTitle(PATREON, "Patreon");
        ModTitles.addTitle(SERPENTS_SIN, "Serpent's Sin");
        ModTitles.addTitle(DAME_DE_DRAGONS, "Dame de Dragons");
        ModTitles.addTitle(TITAN_SLAYER, "Titanslayer");
        ModTitles.addTitle(SPECTRAL, "Spectral");
        ModTitles.addTitle(KING_KONG, "King of the Kong", "Queen of the Kong");
        ModTitles.addTitle(WHITE_BUFFALO, "Spirit Slayer");
        ModTitles.addTitle(ARENA_CHAMPION, "Champion of the Arena");
        ModTitles.addTitle(TREASURE_HUNT, "Booty Finder");
        ModTitles.addTitle(ARCHITECT, "Architect");
        ModTitles.addTitle(FLOYDIAN, "Floydian");
        ModTitles.addTitle(APOTHECARY, "Apothecary");
        ModTitles.addTitle(REQUIEM, "of Requiem");
        ModTitles.addTitle(CRAZY_CAT, "Crazy Cat Man", "Crazy Cat Lady");
        ModTitles.addTitle(ZEALOT_LIBILA, "Zealot of Libila");
        ModTitles.addTitle(ZEALOT_FO, "Zealot of Fo");
        ModTitles.addTitle(ZEALOT_MAG, "Zealot of Magranon");
        ModTitles.addTitle(ZEALOT_VYNORA, "Zealot of Vynora");
        ModTitles.addTitle(DREAMER, "Dreamer");
        ModTitles.addTitle(FEARLESS, "Fearless");
        ModTitles.addTitle(TITANS, "Herald of Titans");
        ModTitles.addTitle(HAND_OF_ZEUS, "Hand of Zeus");
        ModTitles.addTitle(WARLORD, "Warlord of Requiem");
        ModTitles.addTitle(DEVOTED, "Devoted");
        ModTitles.addTitle(ELDER, "Elder");
        ModTitles.addTitle(PATRIOT, "Patriot");
        ModTitles.addTitle(SALTY, "Salty");
        ModTitles.addTitle(ANCIENT, "Ancient");
        ModTitles.addTitle(TREASURE_GOBLIN, "Treasure Goblin Hunter", "Treasure Goblin Huntress");
        ModTitles.addTitle(EXPERIENCED, "Experienced");
        ModTitles.addTitle(GAME_MASTER, "Game Master");
        ModTitles.addTitle(DEVELOPER, "Developer");
        ModTitles.addTitle(MACRO_ROYALTY, "Macro King", "Macro Queen");
        ModTitles.addTitle(DRAMA_QUEEN, "Drama King", "Drama Queen");
        ModTitles.addTitle(ZERGLING, "Zergling");
        ModTitles.addTitle(SPECIAL_TITLE, "Special Guy", "Special Girl");
        ModTitles.addTitle(PROPHET_EAR, "Prophet Ear");
        ModTitles.addTitle(KOZA, "Koza");
        ModTitles.addTitle(HOME_DECORATOR, "Home Decorator");
        ModTitles.addTitle(TAXIDERMY_MINOR, "Junior Taxidermist", SkillIds.skillTaxidermy, "MINOR");
        ModTitles.addTitle(TAXIDERMY_NORMAL, "Anthropomorphic Taxidermist", SkillIds.skillTaxidermy, "NORMAL");
        ModTitles.addTitle(TAXIDERMY_MASTER, "Head Hunter", SkillIds.skillTaxidermy, "MASTER");
        ModTitles.addTitle(TAXIDERMY_EPIC, "Epic Taxidermist", SkillIds.skillTaxidermy, "EPIC");
        ModTitles.addTitle(TAXIDERMY_LEGENDARY, "Legendary Taxidermist", SkillIds.skillTaxidermy, "LEGENDARY");
        ModTitles.addTitle(GEMCRAFTING_MINOR, "Facetor", SkillIds.skillGemCrafting, "MINOR");
        ModTitles.addTitle(GEMCRAFTING_NORMAL, "Gem Stone Cutter", SkillIds.skillGemCrafting, "NORMAL");
        ModTitles.addTitle(GEMCRAFTING_MASTER, "Diamond Driller", SkillIds.skillGemCrafting, "MASTER");
        ModTitles.addTitle(GEMCRAFTING_EPIC, "Gem Expert", SkillIds.skillGemCrafting, "EPIC");
        ModTitles.addTitle(GEMCRAFTING_LEGENDARY, "Gemologist", SkillIds.skillGemCrafting, "LEGENDARY");
        ModTitles.addTitle(RIFT_WARRIOR, "Rift Warrior");
        ModTitles.addTitle(RIFT_SLAYER, "Rift Slayer");
        ModTitles.addTitle(FACEBREYKER, "Facebreykerer");
        ModTitles.addTitle(WIKI_MASTER, "Wiki Master");
    }

    public static void playerLoginTitles(Player player) {
        if (player.getPlayingTime() >= TimeConstants.YEAR_MILLIS / 2) {
            player.addTitle(Titles.Title.getTitle(ELDER));
        }
        if (player.getPlayingTime() >= TimeConstants.YEAR_MILLIS) {
            player.addTitle(Titles.Title.getTitle(ANCIENT));
        }
    }

    public static void announceTitles() {
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();
            final Class<MiscChanges> thisClass = MiscChanges.class;
            String replace;

            // - Announce player titles - //
            CtClass ctPlayer = classPool.get("com.wurmonline.server.players.Player");
            Util.setReason("Announce player titles in the titles tab.");
            replace = String.format("$_ = $proceed($$);if(this.getPower() < 1){  %s.sendTitleTabMessage(\"event\", this.getName()+\" just earned the title of \"+title.getName(this.isNotFemale())+\"!\", 200, 100, 0);}", CustomChat.class.getName());
            Util.instrumentDeclared(thisClass, ctPlayer, "addTitle", "sendNormalServerMessage", replace);

        } catch (NotFoundException | IllegalArgumentException | ClassCastException e) {
            throw new HookException(e);
        }
    }

}
