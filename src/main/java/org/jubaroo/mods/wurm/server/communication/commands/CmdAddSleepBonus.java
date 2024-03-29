package org.jubaroo.mods.wurm.server.communication.commands;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.NoSuchPlayerException;
import com.wurmonline.server.Players;
import com.wurmonline.server.behaviours.Methods;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.players.Player;
import com.wurmonline.shared.constants.SoundNames;

public class CmdAddSleepBonus extends WurmCmd {

    public CmdAddSleepBonus() {
        super("#addsleep", MiscConstants.POWER_IMPLEMENTOR);
    }

    @Override
    public boolean runWurmCmd(Creature actor, String[] argv) {
        Communicator comm = actor.getCommunicator();
        if (argv.length != 2) {
            comm.sendNormalServerMessage("usage: #addsleep <player> ");
            return true;
        }
        try {
            Player player = Players.getInstance().getPlayer(argv[1]);
            Methods.sendSound(player, SoundNames.HUMM_SND);
            player.getSaveFile().addToSleep(18000);
            player.getCommunicator().sendSafeServerMessage("A sudden burst of energy flows through your body and you feel like you just slept a whole week! You feel full of sleep bonus now.", (byte) 2);
            String mesg = String.format("Player %s sleep bonus is filled", argv[1]);
            comm.sendNormalServerMessage(mesg);
        } catch (NoSuchPlayerException e) {
            comm.sendNormalServerMessage(String.format("Player %s not found", argv[1]));
            return false;
        } catch (Throwable e) {
            comm.sendNormalServerMessage(String.format("[ERROR]: %s", e.toString()));
            return true;
        }
        return true;
    }

}
