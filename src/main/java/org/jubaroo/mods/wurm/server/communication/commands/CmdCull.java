package org.jubaroo.mods.wurm.server.communication.commands;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import org.jubaroo.mods.wurm.server.tools.CreatureTools;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

public class CmdCull extends WurmCmd {

    public CmdCull() {
        super("#cull", MiscConstants.POWER_IMPLEMENTOR);
    }

    @Override
    public boolean runWurmCmd(Creature actor, String[] argv) {
        Communicator comm = actor.getCommunicator();
        HashMap<String, Integer> histo = CreatureTools.getCreatureHisto(true);
        SortedSet<String> names = new TreeSet<>(histo.keySet());

        if (argv.length == 3) {
            Integer count = Integer.valueOf(argv[1]);
            String name = argv[2];
            comm.sendNormalServerMessage(String.format("Culling %d %s's from the total population", count, name));
            CreatureTools.cullByName(count, name);
        }

        for (String name : names) {
            String line = String.format("%s: %d", name, histo.get(name));
            comm.sendNormalServerMessage(line);
        }

        comm.sendNormalServerMessage("usage: cull [<count> <name>]");

        return true;
    }

}
