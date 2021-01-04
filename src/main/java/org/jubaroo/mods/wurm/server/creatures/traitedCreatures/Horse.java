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

public class Horse implements ModCreature {
    public static int templateId;
    private static final int COLOR_EBONY_BLACK = ModTraits.COLOR_EBONY_BLACK;
    private static final int COLOR_BLOOD_BAY = ModTraits.COLOR_BLOOD_BAY;
    private static final int COLOR_PIEBALD_PINTO = ModTraits.COLOR_PIEBALD_PINTO;
    private static final int COLOR_WHITE = ModTraits.COLOR_WHITE;
    private static final int COLOR_BLACK = ModTraits.COLOR_BLACK;
    private static final int COLOR_GOLD = ModTraits.COLOR_GOLD;
    private static final int COLOR_BROWN = ModTraits.COLOR_BROWN;
    private static final int COLOR_SKEWBALD = 26;
    private static final int COLOR_ROCKY_MOUNTAIN = 30;
    private static final int COLOR_REGULAR_BAY = 31;
    private static final int COLOR_MANGA_LARGA_MARCHADOR = 32;
    private static final int COLOR_PIEBALD = 33;
    private static final int COLOR_XAPPALOOSA = 34;
    private static final int COLOR_KNABSTRUPPER = 35;
    private static final int COLOR_XAPPALOOSA2 = 36;
    private static final int COLOR_CREAMY_PALAMINO = 37;
    private static final int COLOR_KNABSTRUPPER2 = 38;
    private static final int COLOR_DUN = 39;
    private static final int COLOR_DAPPLED_GREY = 40;
    private static final int COLOR_BROWN2 = 41;
    private static final int COLOR_CREAMY_PALAMINO_MIX = 42;
    private static final int COLOR_PAINTED = 43;
    private static final int COLOR_KNABSTRUPPER3 = 44;
    private static final int COLOR_YELLOW2 = 45;
    private static final int COLOR_TRAITS =
            1 << COLOR_EBONY_BLACK |
            1 << COLOR_BLOOD_BAY |
            1 << COLOR_PIEBALD_PINTO |
            1 << COLOR_WHITE |
            1 << COLOR_BLACK |
            1 << COLOR_GOLD |
            1 << COLOR_BROWN |
            1 << COLOR_SKEWBALD |
            1 << COLOR_ROCKY_MOUNTAIN |
            1 << COLOR_REGULAR_BAY |
            1 << COLOR_MANGA_LARGA_MARCHADOR |
            1 << COLOR_PIEBALD |
            1 << COLOR_XAPPALOOSA |
            1 << COLOR_KNABSTRUPPER |
            1 << COLOR_XAPPALOOSA2 |
            1 << COLOR_CREAMY_PALAMINO |
            1 << COLOR_KNABSTRUPPER2 |
            1 << COLOR_DUN |
            1 << COLOR_DAPPLED_GREY |
            1 << COLOR_BROWN2 |
            1 << COLOR_CREAMY_PALAMINO_MIX |
            1 << COLOR_PAINTED |
            1 << COLOR_KNABSTRUPPER3 |
            1 << COLOR_YELLOW2;

    public CreatureTemplateBuilder createCreateTemplateBuilder() {
        templateId = CreatureTemplateIds.HORSE_CID;
        return new CreatureTemplateBuilder(templateId) {
            public CreatureTemplate build() {
                try {
                    return CreatureTemplateFactory.getInstance().getTemplate(templateId);
                } catch (NoSuchCreatureTemplateException var2) {
                    throw new RuntimeException(var2);
                }
            }
        };
    }

    public boolean hasTraits() {
        return true;
    }

    public String getTraitName(int trait) {
        switch (trait) {
            case COLOR_EBONY_BLACK:
                return "ebonyblack";
            case COLOR_PIEBALD_PINTO:
                return "piebaldpinto";
            case COLOR_BLOOD_BAY:
                return "bloodbay";
            case COLOR_SKEWBALD:
                return "skewbald";
            case COLOR_KNABSTRUPPER:
                return "knabstrupper";
            case COLOR_XAPPALOOSA:
                return "knabstrupperxappaloosa";
            case COLOR_ROCKY_MOUNTAIN:
                return "rockymountain";
            case COLOR_REGULAR_BAY:
                return "regularbay";
            case COLOR_MANGA_LARGA_MARCHADOR:
                return "mangalargamarchador";
            case COLOR_PIEBALD:
                return "piebald";
            case COLOR_XAPPALOOSA2:
                return "knabstrupperxappaloosa2";
            case COLOR_CREAMY_PALAMINO:
                return "creamypalamino";
            case COLOR_KNABSTRUPPER2:
                return "knabstrupper2";
            case COLOR_DUN:
                return "dun";
            case COLOR_DAPPLED_GREY:
                return "dappledgrey";
            case COLOR_BROWN2:
                return "brownvariant2";
            case COLOR_CREAMY_PALAMINO_MIX:
                return "creamypalaminomix";
            case COLOR_PAINTED:
                return "painted";
            case COLOR_KNABSTRUPPER3:
                return "knabstrupper3";
            case COLOR_YELLOW2:
                return "yellow2";
            default:
                return null;
        }
    }

    public String getColourName(int trait) {
        switch (trait) {
            case COLOR_EBONY_BLACK:
                return "ebony black";
            case COLOR_PIEBALD_PINTO:
                return "piebald pinto";
            case COLOR_BLOOD_BAY:
                return "blood bay";
            case COLOR_SKEWBALD:
                return "skewbald";
            case COLOR_KNABSTRUPPER:
                return "knabstrupper";
            case COLOR_XAPPALOOSA:
                return "knabstrupper x appaloosa";
            case COLOR_ROCKY_MOUNTAIN:
                return "rocky mountain";
            case COLOR_REGULAR_BAY:
                return "regular bay";
            case COLOR_MANGA_LARGA_MARCHADOR:
                return "manga larga marchador";
            case COLOR_PIEBALD:
                return "piebald";
            case COLOR_XAPPALOOSA2:
                return "knabstrupper appaloosa variant 1";
            case COLOR_CREAMY_PALAMINO:
                return "creamy palamino variant 1";
            case COLOR_KNABSTRUPPER2:
                return "knabstrupper variant 1";
            case COLOR_DUN:
                return "dun";
            case COLOR_DAPPLED_GREY:
                return "dappled grey";
            case COLOR_BROWN2:
                return "brown variant 2";
            case COLOR_CREAMY_PALAMINO_MIX:
                return "creamy palamino variant 2";
            case COLOR_PAINTED:
                return "painted";
            case COLOR_KNABSTRUPPER3:
                return "knabstrupper variant 2";
            case COLOR_YELLOW2:
                return "gold variant 2";
            default:
                return null;
        }
    }

    public void assignTraits(TraitsSetter traitsSetter) {
        int rand = Server.rand.nextInt(175);
        if (rand < 30) {
            traitsSetter.setTraitBit(COLOR_BROWN, true);
        } else if (rand < 40) {
            traitsSetter.setTraitBit(COLOR_GOLD, true);
        } else if (rand < 50) {
            traitsSetter.setTraitBit(COLOR_BLACK, true);
        } else if (rand < 60) {
            traitsSetter.setTraitBit(COLOR_WHITE, true);
        } else if (rand < 65) {
            traitsSetter.setTraitBit(COLOR_PIEBALD_PINTO, true);
        } else if (rand < 70) {
            traitsSetter.setTraitBit(COLOR_BLOOD_BAY, true);
        } else if (rand < 75) {
            traitsSetter.setTraitBit(COLOR_EBONY_BLACK, true);
        } else if (rand < 80) {
            traitsSetter.setTraitBit(COLOR_REGULAR_BAY, true);
        } else if (rand < 85) {
            traitsSetter.setTraitBit(COLOR_MANGA_LARGA_MARCHADOR, true);
        } else if (rand < 90) {
            traitsSetter.setTraitBit(COLOR_SKEWBALD, true);
        } else if (rand < 95) {
            traitsSetter.setTraitBit(COLOR_PIEBALD, true);
        } else if (rand < 100) {
            traitsSetter.setTraitBit(COLOR_KNABSTRUPPER, true);
        } else if (rand < 105) {
            traitsSetter.setTraitBit(COLOR_XAPPALOOSA, true);
        } else if (rand < 110) {
            traitsSetter.setTraitBit(COLOR_ROCKY_MOUNTAIN, true);
        } else if (rand < 112) {
            traitsSetter.setTraitBit(COLOR_XAPPALOOSA2, true);
        } else if (rand < 117) {
            traitsSetter.setTraitBit(COLOR_CREAMY_PALAMINO, true);
        } else if (rand < 120) {
            traitsSetter.setTraitBit(COLOR_KNABSTRUPPER2, true);
        } else if (rand < 125) {
            traitsSetter.setTraitBit(COLOR_DUN, true);
        } else if (rand < 130) {
            traitsSetter.setTraitBit(COLOR_DAPPLED_GREY, true);
        } else if (rand < 140) {
            traitsSetter.setTraitBit(COLOR_BROWN2, true);
        } else if (rand < 150) {
            traitsSetter.setTraitBit(COLOR_CREAMY_PALAMINO_MIX, true);
        } else if (rand < 155) {
            traitsSetter.setTraitBit(COLOR_PAINTED, true);
        } else if (rand < 157) {
            traitsSetter.setTraitBit(COLOR_KNABSTRUPPER3, true);
        } else if (rand < 175) {
            traitsSetter.setTraitBit(COLOR_YELLOW2, true);
        }
    }

    public long calcNewTraits(double breederSkill, boolean inbred, long mothertraits, long fathertraits) {
        return ModTraits.calcNewTraits(breederSkill, inbred, mothertraits, fathertraits, ModTraits.REGULAR_TRAITS, Horse.COLOR_TRAITS);
    }

}
