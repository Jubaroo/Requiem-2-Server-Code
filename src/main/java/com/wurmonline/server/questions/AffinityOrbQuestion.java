package com.wurmonline.server.questions;

import com.wurmonline.server.Items;
import com.wurmonline.server.Server;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.skills.Affinities;
import com.wurmonline.server.skills.Affinity;
import com.wurmonline.server.skills.SkillSystem;
import net.coldie.tools.BmlForm;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.util.HashMap;
import java.util.Properties;

public class AffinityOrbQuestion extends Question {
    public static HashMap<Integer, Integer> affinityMap = new HashMap<>();
    protected Item affinityOrb;

    public AffinityOrbQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget, Item orb) {
        super(aResponder, aTitle, aQuestion, LOCATEQUESTION, aTarget);
        this.affinityOrb = orb;
    }

    @Override
    public void answer(Properties answer) {
        boolean accepted = answer.containsKey("accept") && answer.get("accept") == "true";
        if (accepted) {
            RequiemLogging.logInfo("Accepted AffinityOrb");
            int entry = Integer.parseInt(answer.getProperty("affinity"));
            int skillNum = affinityMap.get(entry);
            if (affinityOrb == null || affinityOrb.getOwnerId() != this.getResponder().getWurmId()) {
                this.getResponder().getCommunicator().sendNormalServerMessage("You must own an affinity orb to obtain an affinity.");
            } else {
                if (this.getResponder() instanceof Player) {
                    Player player = (Player) this.getResponder();
                    String skillName = SkillSystem.getNameFor(skillNum);
                    RequiemLogging.logInfo(String.format("Adding affinity for skill %s to %s", skillName, player.getName()));
                    Items.destroyItem(affinityOrb.getWurmId());

                    Affinity[] affs = Affinities.getAffinities(player.getWurmId());
                    boolean found = false;
                    for (Affinity affinity : affs) {
                        if (affinity.getSkillNumber() != skillNum) continue;
                        if (affinity.getNumber() >= 5) {
                            player.getCommunicator().sendSafeServerMessage(String.format("You already have the maximum amount of affinities for %s", skillName));
                            return;
                        }
                        Affinities.setAffinity(player.getWurmId(), skillNum, affinity.getNumber() + 1, false);
                        found = true;
                        Items.destroyItem(affinityOrb.getWurmId());
                        player.getCommunicator().sendSafeServerMessage(String.format("Vynora infuses you with an affinity for %s!", skillName));
                        break;
                    }
                    if (!found) {
                        Affinities.setAffinity(player.getWurmId(), skillNum, 1, false);
                        Items.destroyItem(affinityOrb.getWurmId());
                        player.getCommunicator().sendSafeServerMessage(String.format("Vynora infuses you with an affinity for %s!", skillName));
                    }
                } else {
                    RequiemLogging.logWarning(String.format("Non-player used a %s?", affinityOrb.getName()));
                }
            }
        }
    }

    private String getAffinities() {
        StringBuilder builder = new StringBuilder();
        if (affinityOrb.getAuxData() == 0) {
            RequiemLogging.logInfo("Orb has no affinity set, creating random seed now.");
            affinityOrb.setAuxData((byte) ((1 + Server.rand.nextInt(120)) * (Server.rand.nextBoolean() ? 1 : -1)));
        }
        Server.rand.setSeed(affinityOrb.getAuxData());
        RequiemLogging.logInfo(String.format("Seed set to %s", affinityOrb.getAuxData()));
        affinityMap.clear();
        int i = 0;
        while (i < 10) {
            int num = Server.rand.nextInt(SkillSystem.getNumberOfSkillTemplates());
            if (!affinityMap.containsValue(num)) {
                builder.append(SkillSystem.getSkillTemplateByIndex(num).getName());
                affinityMap.put(i, SkillSystem.getSkillTemplateByIndex(num).getNumber());
                i++;
                if (i < 10) {
                    builder.append(",");
                }
            }
        }
        return builder.toString();
    }

    @Override
    public void sendQuestion() {
        if (affinityOrb == null || affinityOrb.getOwnerId() != this.getResponder().getWurmId()) {
            this.getResponder().getCommunicator().sendNormalServerMessage("You must own an affinity orb before being infused with its power.");
            return;
        }
        BmlForm f = new BmlForm("");
        f.addHidden("id", String.valueOf(this.id));
        f.addBoldText("Select the affinity you would like to obtain\n");
        f.addRaw("harray{label{text='Select Affinity:'}dropdown{id='affinity';options='");
        f.addRaw(getAffinities());
        f.addRaw("'}}");
        f.addText("\n\n");
        f.beginHorizontalFlow();
        f.addButton("Accept", "accept");
        f.endHorizontalFlow();
        this.getResponder().getCommunicator().sendBml(400, 300, true, true, f.toString(), 255, 255, 255, this.title);
    }
}
