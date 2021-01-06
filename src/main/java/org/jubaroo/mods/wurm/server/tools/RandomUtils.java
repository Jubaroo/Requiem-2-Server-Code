package org.jubaroo.mods.wurm.server.tools;

import com.wurmonline.server.Server;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.Materials;
import com.wurmonline.shared.constants.ItemMaterials;

import java.util.Arrays;
import java.util.List;

public class RandomUtils {

    public static final List<Integer> maskTemplates = Arrays.asList(
            ItemList.maskEnlightended,
            ItemList.maskRavager,
            ItemList.maskPale,
            ItemList.maskShadow,
            ItemList.maskChallenge,
            ItemList.maskIsles,
            ItemList.maskOfTheReturner
    );

    private static final List<Integer> mineDoorIds = Arrays.asList(
            ItemList.mineDoorPlanks,
            ItemList.mineDoorStone,
            ItemList.mineDoorGold,
            ItemList.mineDoorSilver,
            ItemList.mineDoorSteel
    );

    private static final List<Integer> flowerIds = Arrays.asList(
            ItemList.flower1,
            ItemList.flower1,
            ItemList.flower1,
            ItemList.flower1,
            ItemList.flower1,
            ItemList.flower1,
            ItemList.flower1
    );

    private static final List<Integer> fishIds = Arrays.asList(
            ItemList.deadPike,
            ItemList.deadBass,
            ItemList.deadHerring,
            ItemList.deadCatFish,
            ItemList.deadSnook,
            ItemList.deadRoach,
            ItemList.deadPerch,
            ItemList.deadCarp,
            ItemList.deadTrout
    );

    private static final List<Integer> runeTemplates = Arrays.asList(
            ItemList.runeMag,
            ItemList.runeFo,
            ItemList.runeVyn,
            ItemList.runeLib,
            ItemList.runeJackal
    );

    private static final List<Integer> horseEquipment = Arrays.asList(
            ItemList.saddle,
            ItemList.saddleBags,
            ItemList.horseShoe,
            ItemList.bridle,
            ItemList.leatherBarding,
            ItemList.chainBarding,
            ItemList.clothBarding
    );

    private static final List<Integer> potions = Arrays.asList(
            ItemList.potionWeaponSmithing,
            ItemList.potionRopemaking,
            ItemList.potionMining,
            ItemList.potionTailoring,
            ItemList.potionArmourSmithing,
            ItemList.potionFletching,
            ItemList.potionBlacksmithing,
            ItemList.potionLeatherworking,
            ItemList.potionShipbuilding,
            ItemList.potionStonecutting,
            ItemList.potionMasonry,
            ItemList.potionWoodcutting,
            ItemList.potionCarpentry
    );

    private static final List<Integer> riftItemIds = Arrays.asList(
            ItemList.riftStone,
            ItemList.riftCrystal,
            ItemList.riftWood
    );

    private static final List<Byte> runeMaterials = Arrays.asList(
            ItemMaterials.MATERIAL_GOLD,
            ItemMaterials.MATERIAL_SILVER,
            ItemMaterials.MATERIAL_STEEL,
            ItemMaterials.MATERIAL_COPPER,
            ItemMaterials.MATERIAL_IRON,
            ItemMaterials.MATERIAL_LEAD,
            ItemMaterials.MATERIAL_ZINC,
            ItemMaterials.MATERIAL_BRASS,
            ItemMaterials.MATERIAL_BRONZE,
            ItemMaterials.MATERIAL_TIN,
            ItemMaterials.MATERIAL_ADAMANTINE,
            ItemMaterials.MATERIAL_GLIMMERSTEEL,
            ItemMaterials.MATERIAL_SERYLL,
            ItemMaterials.MATERIAL_ELECTRUM
    );

    private static final List<Byte> metalMaterials = Arrays.asList(
            Materials.MATERIAL_GOLD,
            Materials.MATERIAL_SILVER,
            Materials.MATERIAL_STEEL,
            Materials.MATERIAL_COPPER,
            Materials.MATERIAL_IRON,
            Materials.MATERIAL_LEAD,
            Materials.MATERIAL_ZINC,
            Materials.MATERIAL_BRASS,
            Materials.MATERIAL_BRONZE,
            Materials.MATERIAL_TIN,
            Materials.MATERIAL_ADAMANTINE,
            Materials.MATERIAL_GLIMMERSTEEL,
            Materials.MATERIAL_SERYLL,
            Materials.MATERIAL_ELECTRUM
    );

    private static final List<Byte> woodMaterials = Arrays.asList(
            ItemMaterials.MATERIAL_WOOD_BIRCH,
            ItemMaterials.MATERIAL_WOOD_PINE,
            ItemMaterials.MATERIAL_WOOD_OAK,
            ItemMaterials.MATERIAL_WOOD_CEDAR,
            ItemMaterials.MATERIAL_WOOD_WILLOW,
            ItemMaterials.MATERIAL_WOOD_MAPLE,
            ItemMaterials.MATERIAL_WOOD_APPLE,
            ItemMaterials.MATERIAL_WOOD_LEMON,
            ItemMaterials.MATERIAL_WOOD_OLIVE,
            ItemMaterials.MATERIAL_WOOD_CHERRY,
            ItemMaterials.MATERIAL_WOOD_LAVENDER,
            ItemMaterials.MATERIAL_WOOD_ROSE,
            ItemMaterials.MATERIAL_WOOD_THORN,
            ItemMaterials.MATERIAL_WOOD_GRAPE,
            ItemMaterials.MATERIAL_WOOD_CAMELLIA,
            ItemMaterials.MATERIAL_WOOD_OLEANDER,
            ItemMaterials.MATERIAL_WOOD_CHESTNUT,
            ItemMaterials.MATERIAL_WOOD_WALNUT,
            ItemMaterials.MATERIAL_WOOD_FIR,
            ItemMaterials.MATERIAL_WOOD_LINDEN,
            ItemMaterials.MATERIAL_WOOD_IVY,
            ItemMaterials.MATERIAL_WOOD_HAZELNUT,
            ItemMaterials.MATERIAL_WOOD_ORANGE,
            ItemMaterials.MATERIAL_WOOD_RASPBERRY,
            ItemMaterials.MATERIAL_WOOD_BLUEBERRY,
            ItemMaterials.MATERIAL_WOOD_LINGONBERRY
    );

    private static final List<Integer> sorceryIds = Arrays.asList(
            ItemList.bloodAngels,
            ItemList.smokeSol,
            ItemList.slimeUttacha,
            ItemList.tomeMagicRed,
            ItemList.scrollBinding,
            ItemList.cherryWhite,
            ItemList.cherryRed,
            ItemList.cherryGreen,
            ItemList.giantWalnut,
            ItemList.libramNight,
            ItemList.tomeMagicGreen,
            ItemList.tomeMagicBlack,
            ItemList.tomeMagicBlue,
            ItemList.tomeMagicWhite
    );

    private static final List<Integer> plateChainTemplates = Arrays.asList(
            ItemList.plateBoot,
            ItemList.plateGauntlet,
            ItemList.plateHose,
            ItemList.plateJacket,
            ItemList.plateSleeve,
            ItemList.chainBoot,
            ItemList.chainCoif,
            ItemList.chainGlove,
            ItemList.chainHose,
            ItemList.chainJacket,
            ItemList.chainSleeve,
            ItemList.helmetBasinet,
            ItemList.helmetGreat,
            ItemList.helmetOpen
    );

    private static final List<Integer> toolWeaponTemplates = Arrays.asList(
            ItemList.axeSmall,
            ItemList.shieldMedium,
            ItemList.hatchet,
            ItemList.knifeCarving,
            ItemList.pickAxe,
            ItemList.swordLong,
            ItemList.saw,
            ItemList.shovel,
            ItemList.rake,
            ItemList.hammerMetal,
            ItemList.hammerWood,
            ItemList.anvilSmall,
            ItemList.cheeseDrill,
            ItemList.swordShort,
            ItemList.swordTwoHander,
            ItemList.shieldSmallWood,
            ItemList.shieldSmallMetal,
            ItemList.shieldMediumWood,
            ItemList.shieldLargeWood,
            ItemList.shieldLargeMetal,
            ItemList.axeHuge,
            ItemList.axeMedium,
            ItemList.knifeButchering,
            ItemList.fishingRodIronHook,
            ItemList.stoneChisel,
            ItemList.spindle,
            ItemList.anvilLarge,
            ItemList.grindstone,
            ItemList.needleIron,
            ItemList.knifeFood,
            ItemList.sickle,
            ItemList.scythe,
            ItemList.maulLarge,
            ItemList.maulSmall,
            ItemList.maulMedium,
            ItemList.file,
            ItemList.awl,
            ItemList.leatherKnife,
            ItemList.scissors,
            ItemList.clayShaper,
            ItemList.spatula,
            ItemList.fruitpress,
            ItemList.bowShortNoString,
            ItemList.bowMediumNoString,
            ItemList.bowLongNoString,
            ItemList.trowel,
            ItemList.groomingBrush,
            ItemList.spearLong,
            ItemList.halberd,
            ItemList.spearSteel,
            ItemList.staffSteel
    );

    private static final List<Integer> lumpTemplates = Arrays.asList(
            ItemList.goldBar,
            ItemList.silverBar,
            ItemList.ironBar,
            ItemList.copperBar,
            ItemList.zincBar,
            ItemList.leadBar,
            ItemList.brassBar,
            ItemList.bronzeBar,
            ItemList.adamantineBar,
            ItemList.electrumBar,
            ItemList.glimmerSteelBar,
            ItemList.seryllBar,
            ItemList.tinBar,
            ItemList.steelBar
    );

    private static final List<Integer> materialConstructionTemplates = Arrays.asList(
            ItemList.plank,
            ItemList.shaft,
            ItemList.log,
            ItemList.nailsIronLarge,
            ItemList.nailsIronSmall,
            ItemList.leather,
            ItemList.tar,
            ItemList.wool,
            ItemList.clay,
            ItemList.cotton,
            ItemList.metalRivet,
            ItemList.ironBand,
            ItemList.concrete,
            ItemList.flint,
            ItemList.peat,
            ItemList.mortar,
            ItemList.fenceBars,
            ItemList.ash,
            ItemList.rock,
            ItemList.thatch,
            ItemList.sheetIron
    );

    public static int randomMaskTemplates() {
        return maskTemplates.get(Server.rand.nextInt(maskTemplates.size()));
    }

    public static int randomMineDoorTemplates() {
        return mineDoorIds.get(Server.rand.nextInt(mineDoorIds.size()));
    }

    public static int randomFlowerTemplates() {
        return flowerIds.get(Server.rand.nextInt(flowerIds.size()));
    }

    public static int randomFishTemplates() {
        return fishIds.get(Server.rand.nextInt(fishIds.size()));
    }

    public static int randomHorseEquipmentTemplates() {
        return horseEquipment.get(Server.rand.nextInt(horseEquipment.size()));
    }

    public static int randomPotionTemplates() {
        return potions.get(Server.rand.nextInt(potions.size()));
    }

    public static int randomRiftItemTemplates() {
        return riftItemIds.get(Server.rand.nextInt(riftItemIds.size()));
    }

    public static int randomToolWeaponTemplates() {
        return toolWeaponTemplates.get(Server.rand.nextInt(toolWeaponTemplates.size()));
    }

    public static byte randomRuneMaterialIds() {
        return runeMaterials.get(Server.rand.nextInt(runeMaterials.size()));
    }

    public static byte randomMetalMaterialIds() {
        return metalMaterials.get(Server.rand.nextInt(metalMaterials.size()));
    }

    public static byte randomWoodMaterialIds() {
        return woodMaterials.get(Server.rand.nextInt(woodMaterials.size()));
    }

    public static int randomSorceryItemTemplates() {
        return sorceryIds.get(Server.rand.nextInt(sorceryIds.size()));
    }

    public static int randomPlateChainTemplates() {
        return plateChainTemplates.get(Server.rand.nextInt(plateChainTemplates.size()));
    }

    public static int randomLumpTemplates() {
        return lumpTemplates.get(Server.rand.nextInt(lumpTemplates.size()));
    }

    public static int randomMaterialConstructionTemplates() {
        return materialConstructionTemplates.get(Server.rand.nextInt(materialConstructionTemplates.size()));
    }

    public static int randomRuneTemplates() {
        return runeTemplates.get(Server.rand.nextInt(runeTemplates.size()));
    }

    public static byte randomRarity(int upgradeChance, boolean alwaysRare) {
        byte rarity = 0;
        if (alwaysRare || Server.rand.nextInt(upgradeChance) == 0) {
            rarity = 1;
            if (Server.rand.nextInt(upgradeChance) == 0) {
                rarity = 2;
                if (Server.rand.nextInt(upgradeChance) == 0) {
                    rarity = 3;
                }
            }
        }
        return rarity;
    }

    public static int randomGem(boolean star) {
        switch (Server.rand.nextInt(5)) {
            case 0:
                if (star) return ItemList.diamondStar;
                else return ItemList.diamond;
            case 1:
                if (star) return ItemList.emeraldStar;
                else return ItemList.emerald;
            case 2:
                if (star) return ItemList.rubyStar;
                else return ItemList.ruby;
            case 3:
                if (star) return ItemList.opalBlack;
                else return ItemList.opal;
            case 4:
            default:
                if (star) return ItemList.sapphireStar;
                else return ItemList.sapphire;
        }
    }

    public static float getRandomQl(float min, float max) {
        float diff = max - min;
        float rnd = Server.rand.nextFloat();
        return min + diff * rnd;
    }

    public static boolean isSorcery(Item item) {
        for (int id : RandomUtils.sorceryIds) {
            if (item.getTemplateId() == id) {
                return true;
            }
        }
        return false;
    }

}