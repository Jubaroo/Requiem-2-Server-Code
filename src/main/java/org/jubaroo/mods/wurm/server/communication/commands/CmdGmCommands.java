package org.jubaroo.mods.wurm.server.communication.commands;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;

public class CmdGmCommands extends WurmCmd {

    public CmdGmCommands() {
        super("#gmcommands", MiscConstants.POWER_HERO);
    }

    @Override
    public boolean runWurmCmd(Creature actor, String[] argv) {
        Communicator comm = actor.getCommunicator();
        comm.sendSystemMessage("=========================================================================================");
        comm.sendSystemMessage("Add an affinity to a player: GM Power 5");
        comm.sendSystemMessage("#addaff <player> <skill>");
        comm.sendSystemMessage("=========================================================================================");
        comm.sendSystemMessage("Fill sleep bonus of chosen player: GM Power 5");
        comm.sendSystemMessage("#addsleep <player>");
        comm.sendSystemMessage("=========================================================================================");
        comm.sendSystemMessage("Show current coffer value: GM Power 5");
        comm.sendSystemMessage("#coffers");
        comm.sendSystemMessage("=========================================================================================");
        comm.sendSystemMessage("Cull a certain amount of specific creature: GM Power 5");
        comm.sendSystemMessage("#cull <count> <name>");
        comm.sendSystemMessage("=========================================================================================");
        comm.sendSystemMessage("Fill a players food, water, health, CCFP, and nutrition bars: GM Power 5");
        comm.sendSystemMessage("#fillup <player>");
        comm.sendSystemMessage("=========================================================================================");
        comm.sendSystemMessage("Teleport yourself to any player or village (choose either a deed or a player): GM Power 5");
        comm.sendSystemMessage("#goto <deed|player>");
        comm.sendSystemMessage("=========================================================================================");
        comm.sendSystemMessage("Fill food, water, health, CCFP, and nutrition bars of all current players: GM Power 5");
        comm.sendSystemMessage("#massrefresh");
        comm.sendSystemMessage("=========================================================================================");
        comm.sendSystemMessage("Summon player to you: GM Power 3");
        comm.sendSystemMessage("#moveplayer <player>");
        comm.sendSystemMessage("=========================================================================================");
        comm.sendSystemMessage("Send a player to their home deed: GM Power 3");
        comm.sendSystemMessage("#sendhome <player>");
        comm.sendSystemMessage("=========================================================================================");
        comm.sendSystemMessage("Reset items on all traders: GM Power 5");
        comm.sendSystemMessage("#resettraders");
        comm.sendSystemMessage("=========================================================================================");
        return true;
    }

}
