package com.wurmonline.server.questions;

import com.wurmonline.server.DbConnector;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.players.AchievementTemplate;
import com.wurmonline.server.utils.DbUtilities;
import net.coldie.tools.BmlForm;
import org.gotti.wurmunlimited.modsupport.ModSupportDb;
import org.jubaroo.mods.wurm.server.misc.AchievementChanges;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class LeaderboardAchievementQuestion extends Question {
    protected int achievementNum;
    protected AchievementTemplate template;
    protected HashMap<String, Integer> optIn = new HashMap<>();

    public LeaderboardAchievementQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget, int achievementNum) {
        super(aResponder, aTitle, aQuestion, LOCATEQUESTION, aTarget);
        this.achievementNum = achievementNum;
        this.template = AchievementChanges.goodAchievements.get(achievementNum);
    }

    @Override
    public void answer(Properties answer) {
        boolean accepted = answer.containsKey("okay") && answer.get("okay") == "true";
        if (accepted) {
            LeaderboardQuestion lbq = new LeaderboardQuestion(this.getResponder(), "Leaderboard", "Which leaderboard would you like to view?", this.getResponder().getWurmId());
            lbq.sendQuestion();
        }
    }

    protected void identifyOptIn() {
        String name;
        int opted;
        Connection dbcon;
        PreparedStatement ps;
        ResultSet rs;
        try {
            dbcon = ModSupportDb.getModSupportDb();
            ps = dbcon.prepareStatement("SELECT * FROM LeaderboardOpt");
            rs = ps.executeQuery();
            while (rs.next()) {
                name = rs.getString("name");
                opted = rs.getInt("OPTIN");
                optIn.put(name, opted);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException((Throwable) e);
        }
    }

    @Override
    public void sendQuestion() {
        BmlForm f = new BmlForm("");
        f.addHidden("id", String.valueOf(this.id));
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> counts = new ArrayList<>();
        //ArrayList<Integer> deities = new ArrayList<>();
        String name;
        int counter;
        int deity;
        String extra = "";

        // Populates HashMap with latest opt-in data.
        identifyOptIn();

        Connection dbcon;
        PreparedStatement ps;
        ResultSet rs;
        try {
            dbcon = DbConnector.getPlayerDbCon();
            //ps = dbcon.prepareStatement("SELECT players.name, skills.value, players.deity FROM skills JOIN players ON skills.owner = players.wurmid WHERE skills.number = " + skillNum + " AND (players.power = 0) ORDER BY skills.value DESC LIMIT 20");
            ps = dbcon.prepareStatement(String.format("SELECT players.name, achievements.counter FROM achievements JOIN players ON achievements.player = players.wurmid WHERE achievements.achievement = %d AND achievements.counter > 0 AND players.power = 0 ORDER BY achievements.counter DESC LIMIT 20", achievementNum));
            rs = ps.executeQuery();
            while (rs.next()) {
                name = rs.getString(1);
                counter = rs.getInt(2);
                //deity = rs.getInt(3);
                names.add(name);
                counts.add(counter);
                //deities.add(deity);
            }
            DbUtilities.closeDatabaseObjects(ps, rs);
        } catch (SQLException e) {
            throw new RuntimeException((Throwable) e);
        }
        f.addBoldText(String.format("Top 20 players with achievement %s", this.getQuestion()));
        f.addBoldText(template.getRequirement());
        f.addText("\n\n");
        int i = 0;
        DecimalFormat df = new DecimalFormat(".000");
        while (i < names.size() && i < counts.size()) {
            name = names.get(i);
            if (!optIn.containsKey(name)) {
                name = "Unknown";
            } else if (optIn.get(name).equals(0)) {
                name = "Unknown";
            }
            if (names.get(i).equals(this.getResponder().getName())) {
                name = names.get(i);
                f.addBoldText(counts.get(i) + " - " + name + extra);
            } else {
                f.addText(counts.get(i) + " - " + name + extra);
            }
            i++;
        }
        f.addText(" \n");
        f.beginHorizontalFlow();
        f.addButton("Ok", "okay");
        f.endHorizontalFlow();
        f.addText(" \n");
        this.getResponder().getCommunicator().sendBml(400, 500, true, true, f.toString(), 150, 150, 200, this.title);
    }
}
