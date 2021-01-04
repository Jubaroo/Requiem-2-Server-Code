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

public class Cat implements ModCreature {
    public static int templateId;
    private static final int MAINE_COON = 26;

    public CreatureTemplateBuilder createCreateTemplateBuilder() {
        templateId = CreatureTemplateIds.CAT_WILD_CID;
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
        if (trait == MAINE_COON) {
            return "mainecoon";
        }
        return null;
    }

    public void assignTraits(final TraitsSetter traitsSetter) {
        if (Server.rand.nextInt(3) == 0) {
            traitsSetter.setTraitBit(MAINE_COON, true);
        }
    }

    @Override
    public long calcNewTraits(double breederSkill, boolean inbred, long mothertraits, long fathertraits) {
        return ModTraits.calcNewTraits(breederSkill, inbred, mothertraits, fathertraits, ModTraits.REGULAR_TRAITS, 1 << MAINE_COON);
    }
}
