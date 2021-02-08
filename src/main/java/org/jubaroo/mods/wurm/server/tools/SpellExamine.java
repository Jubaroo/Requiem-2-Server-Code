package org.jubaroo.mods.wurm.server.tools;

import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.spells.SpellEffect;
import com.wurmonline.shared.constants.Enchants;

public class SpellExamine {

    public static String getName(SpellEffect enchant, Item item) {
        if (enchant.type == Enchants.BUFF_COURIER && !item.isMailBox())
            return "Bag of Holding";
        else if (enchant.type == Enchants.BUFF_DARKMESSENGER && item.getTemplateId() == ItemList.hopper)
            return "Feeding Hand";
        return enchant.getName();
    }

    public static String getLongDesc(SpellEffect enchant, Item item) {
        if (enchant.type == Enchants.BUFF_COURIER && !item.isMailBox())
            return "is bigger on the inside.";
        else if (enchant.type == Enchants.BUFF_DARKMESSENGER && item.getTemplateId() == ItemList.hopper)
            return "will periodically feed animals around it.";
        return enchant.getLongDesc();
    }

}