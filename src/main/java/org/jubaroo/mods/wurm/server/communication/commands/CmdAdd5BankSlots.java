package org.jubaroo.mods.wurm.server.communication.commands;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.NoSuchPlayerException;
import com.wurmonline.server.Players;
import com.wurmonline.server.behaviours.Methods;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.players.Player;
import com.wurmonline.shared.constants.SoundNames;
import org.jubaroo.mods.wurm.server.misc.database.DatabaseHelper;

public class CmdAdd5BankSlots extends WurmCmd {

    public CmdAdd5BankSlots() {
        super("#bankslots", MiscConstants.POWER_IMPLEMENTOR);
    }

    @Override
    public boolean runWurmCmd(Creature actor, String[] argv) {
        Communicator comm = actor.getCommunicator();
        if (argv.length != 2) {
            comm.sendNormalServerMessage("usage: #bankslots <player name> ");
            return true;
        }
        try {
            Player player = Players.getInstance().getPlayer(argv[1]);
            Methods.sendSound(player, SoundNames.HUMM_SND);
            DatabaseHelper.addFiveBankSlots(player.getWurmId());
            player.getCommunicator().sendSafeServerMessage("You now have access to 5 additional bank slots!", (byte) 2);
            String mesg = String.format("Player %s has had 5 bank slots added.", argv[1]);
            comm.sendNormalServerMessage(mesg);
        } catch (NoSuchPlayerException e) {
            comm.sendNormalServerMessage(String.format("Player %s not found", argv[1]));
            return false;
        } catch (Throwable e) {
            comm.sendNormalServerMessage(String.format("error: %s", e.toString()));
            return true;
        }
        return true;
    }

}
