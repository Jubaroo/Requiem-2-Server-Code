package org.jubaroo.mods.wurm.server.communication.commands;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Players;
import com.wurmonline.server.behaviours.Methods;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.skills.Skill;
import com.wurmonline.shared.constants.SoundNames;

public class CmdAddAff extends WurmCmd {

    public CmdAddAff() {
        super("#addaff", MiscConstants.POWER_IMPLEMENTOR);
    }

    @Override
    public boolean runWurmCmd(Creature actor, String[] argv) {
        Communicator comm = actor.getCommunicator();
        if (argv.length != 3) {
            comm.sendNormalServerMessage("usage: #addaff <player> <skill>");
            return true;
        }
        try {
            Methods.sendSound(actor, SoundNames.HUMM_SND);
            Player player = Players.getInstance().getPlayer(argv[1]);
            Skill skill = player.getSkills().getSkill(argv[2]);
            player.increaseAffinity(skill.getNumber(), 1);
            String mesg = String.format("affinity set on %s %s: %d", argv[1], argv[2], skill.affinity);
            comm.sendNormalServerMessage(mesg);
        } catch (Throwable e) {
            comm.sendNormalServerMessage("error: " + e.toString());
            return true;
        }
        return true;
    }

}
