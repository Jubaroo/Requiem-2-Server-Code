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

public class Cow implements ModCreature {
    public static int templateId;
    private static final int BLACK_ANGUS = 24;
    private static final int BELTED_GALLOWAY = 25;
    private static final int SPOTTED_BLACK = 26;
    private static final int BROWN = 27;

    public CreatureTemplateBuilder createCreateTemplateBuilder() {
        templateId = CreatureTemplateIds.COW_BROWN_CID;
        return new CreatureTemplateBuilder(templateId) {
            public CreatureTemplate build() {
                try {
                    return CreatureTemplateFactory.getInstance().getTemplate(templateId);
                } catch (NoSuchCreatureTemplateException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    public boolean hasTraits() {
        return true;
    }

    public String getTraitName(final int trait) {
        switch (trait) {
            case BLACK_ANGUS: {
                return "blackangus";
            }
            case BELTED_GALLOWAY: {
                return "beltedgalloway";
            }
            case SPOTTED_BLACK: {
                return "spottedblack";
            }
            case BROWN: {
                return "brown";
            }
            default: {
                return null;
            }
        }
    }

    public void assignTraits(final TraitsSetter traitsSetter) {
        if (Server.rand.nextInt(6) == 0) {
            traitsSetter.setTraitBit(BLACK_ANGUS, true);
        } else if (Server.rand.nextInt(6) == 0) {
            traitsSetter.setTraitBit(BELTED_GALLOWAY, true);
        } else if (Server.rand.nextInt(6) == 0) {
            traitsSetter.setTraitBit(SPOTTED_BLACK, true);
        } else if (Server.rand.nextInt(6) == 0) {
            traitsSetter.setTraitBit(BROWN, true);
        }
    }

    @Override
    public long calcNewTraits(double breederSkill, boolean inbred, long mothertraits, long fathertraits) {
        return ModTraits.calcNewTraits(breederSkill, inbred, mothertraits, fathertraits, ModTraits.REGULAR_TRAITS, 1 << BLACK_ANGUS | 1 << BELTED_GALLOWAY | 1 << SPOTTED_BLACK | 1 << BROWN);
    }

}
