package org.jubaroo.mods.wurm.server.communication.commands;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.behaviours.Methods;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.shared.constants.SoundNames;
import org.jubaroo.mods.wurm.server.utils.GoTo;

public class CmdGoTo extends WurmCmd {

    public CmdGoTo() {
        super("#goto", MiscConstants.POWER_HIGH_GOD);
    }

    @Override
    public boolean runWurmCmd(Creature actor, String[] argv) {
        Communicator comm = actor.getCommunicator();
        if (argv.length != 2) {
            comm.sendNormalServerMessage("usage: #goto <deed|player>");
            return true;
        }
        if (argv[1].matches("^[0-9]+,[0-9]+$")) {
            String[] coords = argv[1].split(",");
            GoTo.sendToXy(actor, Integer.valueOf(coords[0]), Integer.valueOf(coords[1]), 0, 0);
            Methods.sendSound(actor, SoundNames.HUMM_SND);
            return true;
        }
        if (GoTo.sendToPlayer(actor, argv[1])) {
            Methods.sendSound(actor, SoundNames.HUMM_SND);
            return true;
        }
        if (GoTo.sendToVillage(actor, argv[1])) {
            Methods.sendSound(actor, SoundNames.HUMM_SND);
            return true;
        }
        return true;
    }

}
