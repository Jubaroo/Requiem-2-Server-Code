package org.jubaroo.mods.wurm.server.communication.commands;

import com.wurmonline.server.Items;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.Creatures;
import com.wurmonline.server.economy.Economy;
import com.wurmonline.server.economy.Shop;
import com.wurmonline.server.items.Item;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.util.LinkedList;

public class CmdTraderReset extends WurmCmd {

    public CmdTraderReset() {
        super("#resettraders", MiscConstants.POWER_IMPLEMENTOR);
    }

    private static int refreshTraders() {
        int affected = 0;
        for (Shop shop : Economy.getTraders()) {
            try {
                if (shop.isPersonal()) continue;
                Creature creature = Creatures.getInstance().getCreatureOrNull(shop.getWurmId());
                if (creature == null) continue;
                new LinkedList<>(creature.getInventory().getItems()).stream().map(Item::getWurmId).forEach(Items::destroyItem);
                ReflectionUtil.callPrivateMethod(null, ReflectionUtil.getMethod(Shop.class, "createShop", new Class[]{Creature.class}), creature);
                affected++;
            } catch (Exception e) {
                RequiemLogging.logException("Error in shop refresh", e);
            }
        }
        return affected;
    }

    @Override
    public boolean runWurmCmd(Creature actor, String[] argv) {
        refreshTraders();
        return true;
    }
}
