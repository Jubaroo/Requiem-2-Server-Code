package org.jubaroo.mods.wurm.server.communication.commands;

import com.wurmonline.server.creatures.Creature;

public class WurmCmd {

    public int minPower;
    public String cmdName;

    public WurmCmd(String cmdName, int minPower) {
        this.cmdName = cmdName;
        this.minPower = minPower;
    }

    public boolean runWurmCmd(Creature player, String[] argv) {
        return true;
    }
}
