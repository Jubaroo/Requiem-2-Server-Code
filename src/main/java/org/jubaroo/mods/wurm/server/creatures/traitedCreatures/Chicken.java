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

public class Chicken implements ModCreature {
    public static int templateId;
    private static final int AUSTRALORP = 26;
    private static final int RHODE_ISLAND_RED = 30;
    private static final int SILVER_CAMPINE = 31;

    public CreatureTemplateBuilder createCreateTemplateBuilder() {
        templateId = CreatureTemplateIds.CHICKEN_CID;
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

    public boolean hasTraits() {
        return true;
    }

    public String getTraitName(final int trait) {
        switch (trait) {
            case AUSTRALORP:
                return "australorp";
            case RHODE_ISLAND_RED:
                return "rhodeislandred";
            case SILVER_CAMPINE:
                return "silvercampine";
            default:
                return null;
        }
    }

    public void assignTraits(final TraitsSetter traitsSetter) {
        if (Server.rand.nextInt(5) == 0) {
            traitsSetter.setTraitBit(AUSTRALORP, true);
        } else if (Server.rand.nextInt(5) == 0) {
            traitsSetter.setTraitBit(RHODE_ISLAND_RED, true);
        } else if (Server.rand.nextInt(5) == 0) {
            traitsSetter.setTraitBit(SILVER_CAMPINE, true);
        }
    }

    @Override
    public long calcNewTraits(double breederSkill, boolean inbred, long mothertraits, long fathertraits) {
        return ModTraits.calcNewTraits(breederSkill, inbred, mothertraits, fathertraits, ModTraits.REGULAR_TRAITS, 1 << AUSTRALORP | 1 << RHODE_ISLAND_RED | 1 << SILVER_CAMPINE);
    }

}
