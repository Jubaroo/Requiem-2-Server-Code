package org.jubaroo.mods.wurm.server.spells;

import com.wurmonline.server.deities.Deities;
import com.wurmonline.server.deities.Deity;
import com.wurmonline.server.spells.Spell;
import com.wurmonline.server.spells.Spells;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CustomSpells {

    public static void registerCustomSpells() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Deity fo = Deities.getDeity(Deities.DEITY_FO);
        final Deity magranon = Deities.getDeity(Deities.DEITY_MAGRANON);
        final Deity libila = Deities.getDeity(Deities.DEITY_LIBILA);
        final Deity vynora = Deities.getDeity(Deities.DEITY_VYNORA);

        Method mAddSpell = Spells.class.getDeclaredMethod("addSpell", Spell.class);
        mAddSpell.setAccessible(true);

        // Feeding Hand
        Spell feedingHand = new FeedingHand();
        Arrays.stream(Deities.getDeities()).forEach(d -> d.addSpell(feedingHand));
        mAddSpell.invoke(null, feedingHand);

        // Greater Dirt
        Spell greaterDirt = new GreaterDirt();
        libila.addSpell(greaterDirt);
        mAddSpell.invoke(null, greaterDirt);

        // Sprout Trees
        Spell sproutTrees = new SproutTrees();
        libila.addSpell(greaterDirt);
        mAddSpell.invoke(null, sproutTrees);

        // Dark Genesis
        Spell darkGenesis = new DarkGenesis();
        libila.addSpell(greaterDirt);
        mAddSpell.invoke(null, darkGenesis);
    }

}
