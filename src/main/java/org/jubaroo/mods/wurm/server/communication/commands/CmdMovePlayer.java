package org.jubaroo.mods.wurm.server.communication.commands;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.behaviours.Methods;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.shared.constants.SoundNames;
import org.jubaroo.mods.wurm.server.utils.GoTo;

public class CmdMovePlayer extends WurmCmd {

    public CmdMovePlayer() {
        super("#moveplayer", MiscConstants.POWER_HIGH_GOD);
    }

    @Override
    public boolean runWurmCmd(Creature actor, String[] argv) {
        Communicator comm = actor.getCommunicator();
        if (argv.length != 2) {
            comm.sendNormalServerMessage("usage: #moveplayer <player>");
            return true;
        }
        if (GoTo.sendPlayerHere(actor, argv[1])) {
            Methods.sendSound(actor, SoundNames.HUMM_SND);
            return true;
        }
        return true;
    }

}
