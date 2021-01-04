package org.jubaroo.mods.wurm.server.communication.commands;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.economy.Economy;
import com.wurmonline.server.economy.Shop;

public class CmdCoffers extends WurmCmd {

    public CmdCoffers() {
        super("#coffers", MiscConstants.POWER_HERO);
    }

    @Override
    public boolean runWurmCmd(Creature actor, String[] command) {
        Communicator comm = actor.getCommunicator();
        Shop kingsShop = Economy.getEconomy().getKingsShop();
        if (command.length == 2) {
            long cash = Long.parseLong(command[1]);
            kingsShop.setMoney(cash);
        }
        Long coff = kingsShop.getMoney();
        comm.sendNormalServerMessage(String.format("kings coffers: %d", coff));
        return true;
    }

}
