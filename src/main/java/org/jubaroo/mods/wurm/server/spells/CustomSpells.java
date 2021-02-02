package org.jubaroo.mods.wurm.server.spells;

import com.wurmonline.server.deities.Deities;
import com.wurmonline.server.spells.Spell;
import com.wurmonline.server.spells.Spells;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CustomSpells {

    public static void registerCustomSpells() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method mAddSpell = Spells.class.getDeclaredMethod("addSpell", Spell.class);
        mAddSpell.setAccessible(true);

        // Feeding Hand
        Spell feedingHand = new FeedingHand();
        Arrays.stream(Deities.getDeities()).forEach(d -> d.addSpell(feedingHand));
        mAddSpell.invoke(null, feedingHand);

        // Greater Dirt
        Spell greaterDirt = new GreaterDirt();
        Arrays.stream(Deities.getDeities()).forEach(d -> d.addSpell(greaterDirt));
        mAddSpell.invoke(null, greaterDirt);
    }

}
