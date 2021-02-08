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
        Deities.getDeity(Deities.DEITY_LIBILA).addSpell(greaterDirt);
        mAddSpell.invoke(null, greaterDirt);

        // Sprout Trees
        Spell sproutTrees = new SproutTrees();
        Deities.getDeity(Deities.DEITY_LIBILA).addSpell(sproutTrees);
        mAddSpell.invoke(null, sproutTrees);

        // Dark Genesis
        Spell darkGenesis = new DarkGenesis();
        Deities.getDeity(Deities.DEITY_LIBILA).addSpell(darkGenesis);
        mAddSpell.invoke(null, darkGenesis);

    }

}
