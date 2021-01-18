package org.jubaroo.mods.wurm.server.creatures.traitedCreatures;

import com.wurmonline.server.Server;
import com.wurmonline.server.creatures.CreatureTemplate;
import com.wurmonline.server.creatures.CreatureTemplateFactory;
import com.wurmonline.server.creatures.CreatureTemplateIds;
import com.wurmonline.server.creatures.NoSuchCreatureTemplateException;
import org.gotti.wurmunlimited.modsupport.CreatureTemplateBuilder;
import org.gotti.wurmunlimited.modsupport.creatures.ModCreature;
import org.gotti.wurmunlimited.modsupport.creatures.ModTraits;
import org.gotti.wurmunlimited.modsupport.creatures.TraitsSetter;

public class Dog implements ModCreature {
    public static int templateId;
    private static final int DALMATIAN = 24;
    private static final int SIBERIAN_HUSKY = 25;
    private static final int COLOR_TRAITS =
            1 << DALMATIAN |
                    1 << SIBERIAN_HUSKY;

    public CreatureTemplateBuilder createCreateTemplateBuilder() {
        templateId = CreatureTemplateIds.DOG_CID;
        return new CreatureTemplateBuilder(templateId) {
            public CreatureTemplate build() {
                try {
                    return CreatureTemplateFactory.getInstance().getTemplate(templateId);
                } catch (NoSuchCreatureTemplateException e) {
                    throw new RuntimeException((Throwable) e);
                }
            }
        };
    }

    @Override
    public boolean hasTraits() {
        return true;
    }

    @Override
    public String getTraitName(final int trait) {
        switch (trait) {
            case DALMATIAN: {
                return "dalmatian";
            }
            case SIBERIAN_HUSKY: {
                return "siberianhusky";
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public String getColourName(final int trait) {
        switch (trait) {
            case DALMATIAN:
                return "dalmatian";
            case SIBERIAN_HUSKY:
                return "siberian husky";
            default:
                return null;
        }
    }

    @Override
    public void assignTraits(final TraitsSetter traitsSetter) {
        int rand = Server.rand.nextInt(90);
        if (rand < 30) {
            traitsSetter.setTraitBit(DALMATIAN, true);
        } else if (rand < 40) {
            traitsSetter.setTraitBit(SIBERIAN_HUSKY, true);
        }
    }

    @Override
    public long calcNewTraits(double breederSkill, boolean inbred, long mothertraits, long fathertraits) {
        return ModTraits.calcNewTraits(breederSkill, inbred, mothertraits, fathertraits, ModTraits.REGULAR_TRAITS, COLOR_TRAITS);
    }
}
