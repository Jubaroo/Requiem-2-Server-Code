package com.wurmonline.server.questions;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Servers;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.players.AchievementTemplate;
import com.wurmonline.server.skills.SkillSystem;
import com.wurmonline.server.skills.SkillTemplate;
import net.coldie.tools.BmlForm;
import org.gotti.wurmunlimited.modsupport.ModSupportDb;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.misc.AchievementChanges;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class LeaderboardQuestion extends Question {

    protected HashMap<Integer, String> skillMap = new HashMap<>();
    protected HashMap<Integer, Integer> skillIdMap = new HashMap<>();
    protected HashMap<Integer, String> customMap = new HashMap<>();
    protected HashMap<Integer, String> achievementMap = new HashMap<>();
    protected HashMap<Integer, Integer> achievementIdMap = new HashMap<>();

    public LeaderboardQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
        super(aResponder, aTitle, aQuestion, 79, aTarget);
    }

    protected void setPlayerOptStatus(String name, int opt) {
        Connection dbcon;
        PreparedStatement ps;
        try {
            dbcon = ModSupportDb.getModSupportDb();
            ps = dbcon.prepareStatement("UPDATE LeaderboardOpt SET OPTIN = " + opt + " WHERE name = \"" + name + "\"");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException((Throwable) e);
        }
    }

    @Override
    public void answer(Properties answer) {
        boolean skill = answer.containsKey("accept") && answer.get("accept") == "true";
        boolean achievements = answer.containsKey("achievements") && answer.get("achievements") == "true";
        boolean custom = answer.containsKey("custom") && answer.get("custom") == "true";
        if (skill) {
            int entry = Integer.parseInt(answer.getProperty("leaderboard"));
            String val = skillMap.get(entry);
            int skillNum = skillIdMap.get(entry);
            LeaderboardSkillQuestion lbsq = new LeaderboardSkillQuestion(this.getResponder(), "Leaderboard", val, this.getResponder().getWurmId(), skillNum);
            lbsq.sendQuestion();
        } else if (achievements) {
            int entry = Integer.parseInt(answer.getProperty("achievementboard"));
            String val = achievementMap.get(entry);
            int achievementNum = achievementIdMap.get(entry);
            LeaderboardAchievementQuestion lbaq = new LeaderboardAchievementQuestion(this.getResponder(), "Leaderboard", val, this.getResponder().getWurmId(), achievementNum);
            lbaq.sendQuestion();
        } else if (custom) {
            int entry = Integer.parseInt(answer.getProperty("customboard"));
            String val = customMap.get(entry);
            LeaderboardCustomQuestion lbcq = new LeaderboardCustomQuestion(this.getResponder(), "Leaderboard", val, this.getResponder().getWurmId(), entry);
            lbcq.sendQuestion();
        } else {
            String name = this.getResponder().getName();
            if (answer.containsKey("optin") && answer.get("optin") == "true") {
                RequiemLogging.logInfo(String.format("Player %s has opted into Leaderboard system.", name));
                setPlayerOptStatus(name, 1);
                this.getResponder().getCommunicator().sendNormalServerMessage("You have opted into the Leaderboard system!");
            } else if (answer.containsKey("optout") && answer.get("optout") == "true") {
                RequiemLogging.logInfo(String.format("Player %s has opted out of the Leaderboard system.", name));
                setPlayerOptStatus(name, 0);
                this.getResponder().getCommunicator().sendNormalServerMessage("You have opted out of the Leaderboard system.");
            }
        }
    }

    public String getSkillOptions() {
        String builder = "";
        SkillTemplate[] skillTemplates = SkillSystem.getAllSkillTemplates();
        Arrays.sort(skillTemplates, new Comparator<SkillTemplate>() {
            public int compare(SkillTemplate o1, SkillTemplate o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        int i = 0;
        int index = 0;
        skillMap.clear();
        while (i < skillTemplates.length) {
            builder = builder + skillTemplates[i].getName();
            skillMap.put(index, skillTemplates[i].getName());
            skillIdMap.put(index, skillTemplates[i].getNumber());
            i++;
            index++;
            if (i < skillTemplates.length) {
                builder = builder + ",";
            }
        }
        return builder;
    }

    public String getAchievementOptions() {
        String builder = "";
        Collection<AchievementTemplate> achievements = AchievementChanges.goodAchievements.values();
        List<AchievementTemplate> sortedAchievements = new ArrayList<>(achievements);
        sortedAchievements.sort(new Comparator<AchievementTemplate>() {
            public int compare(AchievementTemplate o1, AchievementTemplate o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        int i = 0;
        int index = 0;
        achievementMap.clear();
        while (i < sortedAchievements.size()) {
            builder = builder + sortedAchievements.get(i).getName();
            achievementMap.put(i, sortedAchievements.get(i).getName());
            achievementIdMap.put(i, sortedAchievements.get(i).getNumber());
            i++;
            if (i < sortedAchievements.size()) {
                builder = builder + ",";
            }
        }
        builder = builder.replaceAll("'", "");
        return builder;
    }

    public String getCustomOptions() {
        String builder = "Total Skill";
        customMap.put(0, "Total Skill");

        builder = builder + ",High Skills";
        customMap.put(1, "High Skills");

        builder = builder + ",Most Titles";
        customMap.put(2, "Most Titles");

        builder = builder + ",Uniques Slain";
        customMap.put(3, "Uniques Slain");
        // added treasure goblins
        builder = builder + ",Treasure Goblins";
        customMap.put(4, "Treasure Goblins Slain");

        builder = builder + ",Titans Slain";
        customMap.put(5, "Titans Slain");

        builder = builder + ",Most Affinities";
        customMap.put(6, "Most Affinities");

        builder = builder + ",Most Unique Achievements";
        customMap.put(7, "Most Unique Achievements");

        builder = builder + ",Largest Structures";
        customMap.put(8, "Largest Structures");

        builder = builder + ",Most Populated Villages";
        customMap.put(9, "Most Populated Villages");

        builder = builder + ",Depots Captured";
        customMap.put(10, "Depots Captured");

        if (Servers.localServer.PVPSERVER || this.getResponder().getPower() >= MiscConstants.POWER_IMPLEMENTOR) {
            builder = builder + ",PvP Kills";
            customMap.put(11, "PvP Kills");

            builder = builder + ",PvP Deaths";
            customMap.put(12, "PvP Deaths");
        }
        return builder;
    }

    @Override
    public void sendQuestion() {
        BmlForm f = new BmlForm("");
        f.addHidden("id", String.valueOf(this.id));
        int opted;
        Connection dbcon;
        PreparedStatement ps;
        ResultSet rs;
        try {
            dbcon = ModSupportDb.getModSupportDb();
            ps = dbcon.prepareStatement(String.format("SELECT * FROM LeaderboardOpt WHERE name = \"%s\"", this.getResponder().getName()));
            rs = ps.executeQuery();
            opted = rs.getInt("OPTIN");
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException((Throwable) e);
        }
        f.addBoldText(String.format("You are currently %sopted into the leaderboard system.\n\n", opted == 0 ? "not " : ""));
        f.addBoldText("Skill Leaderboards");
        f.addRaw("harray{label{text='View leaderboard:'}dropdown{id='leaderboard';options='");
        f.addRaw(getSkillOptions());
        f.addRaw("'}}");
        f.beginHorizontalFlow();
        f.addButton("Accept", "accept");
        f.endHorizontalFlow();
        f.addText(" \n\n");
        f.addBoldText("Achievement Leaderboards");
        f.addRaw("harray{label{text='View leaderboard:'}dropdown{id='achievementboard';options='");
        f.addRaw(getAchievementOptions());
        f.addRaw("'}}");
        f.beginHorizontalFlow();
        f.addButton("Accept", "achievements");
        f.endHorizontalFlow();
        f.addText(" \n\n");
        f.addBoldText("Special Leaderboards");
        f.addRaw("harray{label{text='View leaderboard:'}dropdown{id='customboard';options='");
        f.addRaw(getCustomOptions());
        f.addRaw("'}}");
        f.beginHorizontalFlow();
        f.addButton("Accept", "custom");
        f.endHorizontalFlow();
        f.addText(" \n\n");
        f.addBoldText("Opt into or out of the Leaderboard system.");
        f.beginHorizontalFlow();
        f.addButton("Opt In", "optin");
        f.addButton("Opt Out", "optout");
        f.endHorizontalFlow();
        this.getResponder().getCommunicator().sendBml(400, 500, true, true, f.toString(), 150, 150, 200, this.title);
    }
}
