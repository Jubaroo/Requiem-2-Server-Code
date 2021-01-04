package org.jubaroo.mods.wurm.server.communication.commands;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Players;
import com.wurmonline.server.behaviours.Methods;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.players.Player;
import com.wurmonline.shared.constants.SoundNames;
import org.jubaroo.mods.wurm.server.RequiemLogging;

public class CmdMassSleepBonus extends WurmCmd {

    public CmdMassSleepBonus() {
        super("#masssleep", MiscConstants.POWER_IMPLEMENTOR);
    }

    @Override
    public boolean runWurmCmd(Creature actor, String[] argv) {
        try {
            int nums = 0;
            Player[] allPlayers = Players.getInstance().getPlayers();
            for (Player player : allPlayers) {
                ++nums;
                Methods.sendSound(player, SoundNames.HUMM_SND);
                player.getSaveFile().addToSleep(18000);
            }
            if (nums > 0) {
                String message = "All players sleep bonus filled.";
                actor.getCommunicator().sendNormalServerMessage(message);
                RequiemLogging.logInfo(String.format("Player %s used %s", actor.getName(), CmdMassSleepBonus.class.getName()));
            }
        } catch (Throwable e) {
            actor.getCommunicator().sendNormalServerMessage(String.format("error: %s", e.toString()));
            return true;
        }
        return true;
    }
}