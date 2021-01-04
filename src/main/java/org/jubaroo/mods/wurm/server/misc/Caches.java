package org.jubaroo.mods.wurm.server.misc;

import com.pveplands.treasurehunting.Treasuremap;
import com.wurmonline.server.FailedException;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Server;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.*;
import com.wurmonline.shared.constants.Enchants;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.items.ItemTools;

import java.util.ArrayList;

public class Caches {
    public static ArrayList<Integer> CACHE_IDS = new ArrayList<>();
    public static float minimumQuality = 10f;

    public static boolean isTreasureCache(Item item) {
        int templateId = item.getTemplateId();
        return CACHE_IDS.contains(templateId);
    }

    public static float getBaseQuality(float quality) {
        return quality * 0.25f;
    }

    public static float getRandomQuality(float quality) {
        return quality * 0.6f;
    }

    public static float getWeightMultiplier(int templateId, float quality) {
        if (templateId == CustomItems.dragonCache.getTemplateId()) {
            return 0.005f + (quality * 0.0005f) + (quality * 0.0001f * Server.rand.nextFloat());
        } else if (templateId == CustomItems.moonCache.getTemplateId()) {
            return 1f + (quality * 0.005f) + (quality * 0.005f * Server.rand.nextFloat());
        }
        return 1f + (quality * 0.002f);
    }

    public static boolean adjustBasicWeight(int templateId) {
        if (templateId == CustomItems.dragonCache.getTemplateId()) {
            return true;
        } else return templateId == CustomItems.moonCache.getTemplateId();
    }

    public static boolean createsCustomBasic(int templateId) {
        if (templateId == CustomItems.titanCache.getTemplateId()) {
            return true;
        } else return templateId == CustomItems.treasureMapCache.getTemplateId();
    }

    public static void getCustomBasic(Creature performer, Item cache) {
        int templateId = cache.getTemplateId();
        if (templateId == CustomItems.titanCache.getTemplateId()) {
            Item efficiencyTool = ItemTools.createRandomToolWeapon(20f, 40f, cache.getCreatorName());
            if (efficiencyTool != null) {
                ItemTools.applyEnchant(efficiencyTool, (byte) 120, 40f + (20f * Server.rand.nextFloat())); // Titanforged enchant is 120
                if (efficiencyTool.isMetal()) {
                    efficiencyTool.setMaterial(Server.rand.nextBoolean() ? Materials.MATERIAL_ADAMANTINE : Materials.MATERIAL_GLIMMERSTEEL);
                } else if (efficiencyTool.isWood()) {
                    efficiencyTool.setMaterial(Materials.MATERIAL_WOOD_WILLOW);
                }
                performer.getInventory().insertItem(efficiencyTool, true);
            }
        } else if (templateId == CustomItems.treasureMapCache.getTemplateId()) {
            Item map = Treasuremap.CreateTreasuremap(performer, cache, null, null, true);
            map.setRarity(cache.getRarity());
            performer.getInventory().insertItem(map, true);
        }

    }

    public static int[] getBasicTemplates(int templateId) {
        if (templateId == CustomItems.armourCache.getTemplateId()) {
            return new int[]{
                    ItemList.clothGlove, ItemList.clothHood, ItemList.clothHose, ItemList.clothJacket, ItemList.clothJacket, ItemList.clothShirt, ItemList.clothShoes, ItemList.clothSleeve,
                    ItemList.leatherBoot, ItemList.leatherCap, ItemList.leatherGlove, ItemList.leatherHose, ItemList.leatherJacket, ItemList.leatherSleeve,
                    ItemList.studdedLeatherBoot, ItemList.studdedLeatherCap, ItemList.studdedLeatherGlove, ItemList.studdedLeatherHose, ItemList.studdedLeatherHose, ItemList.studdedLeatherJacket, ItemList.studdedLeatherSleeve,
                    ItemList.chainBoot, ItemList.chainCoif, ItemList.chainGlove, ItemList.chainHose, ItemList.chainJacket, ItemList.chainSleeve,
                    ItemList.plateBoot, ItemList.plateGauntlet, ItemList.plateHose, ItemList.plateJacket, ItemList.plateSleeve, ItemList.helmetGreat, ItemList.helmetBasinet, ItemList.helmetOpen
            };
        } else if (templateId == CustomItems.artifactCache.getTemplateId()) {
            return new int[]{
                    ItemList.swordShort, ItemList.swordLong, ItemList.swordTwoHander,
                    ItemList.axeSmall, ItemList.axeMedium, ItemList.axeHuge,
                    ItemList.maulSmall, ItemList.maulMedium, ItemList.maulLarge,
                    ItemList.spearLong, ItemList.staffSteel, ItemList.halberd,
                    CustomItems.clubId, CustomItems.battleYoyoId,
                    CustomItems.knucklesId, CustomItems.warhammerId
            };
        } else if (templateId == CustomItems.crystalCache.getTemplateId()) {
            return new int[]{
                    CustomItems.chaosCrystalId, CustomItems.chaosCrystalId,
                    CustomItems.enchantersCrystalId
            };
        } else if (templateId == CustomItems.dragonCache.getTemplateId()) {
            return new int[]{
                    ItemList.drakeHide,
                    ItemList.drakeHide,
                    ItemList.dragonScale,
                    ItemList.dragonScale
                    //SpectralHide.getId()
            };
        } else if (templateId == CustomItems.gemCache.getTemplateId()) {
            return new int[]{
                    ItemList.diamond,
                    ItemList.emerald,
                    ItemList.opal,
                    ItemList.ruby,
                    ItemList.sapphire
            };
        } else if (templateId == CustomItems.moonCache.getTemplateId()) {
            return new int[]{
                    ItemList.glimmerSteelBar, ItemList.glimmerSteelBar, ItemList.glimmerSteelBar, ItemList.glimmerSteelBar, ItemList.glimmerSteelBar,
                    ItemList.adamantineBar, ItemList.adamantineBar, ItemList.adamantineBar, ItemList.adamantineBar, ItemList.adamantineBar,
                    ItemList.seryllBar
            };
        } else if (templateId == CustomItems.potionCache.getTemplateId()) {
            return new int[]{
                    ItemList.potionAcidDamage,
                    ItemList.potionArmourSmithing,
                    ItemList.potionBlacksmithing,
                    ItemList.potionCarpentry,
                    ItemList.potionFireDamage,
                    ItemList.potionFletching,
                    ItemList.potionFrostDamage,
                    ItemList.potionLeatherworking,
                    ItemList.potionMasonry,
                    ItemList.potionMining,
                    ItemList.potionRopemaking,
                    ItemList.potionShipbuilding,
                    ItemList.potionStonecutting,
                    ItemList.potionTailoring,
                    ItemList.potionWeaponSmithing,
                    ItemList.potionWoodcutting
            };
        } else if (templateId == CustomItems.riftCache.getTemplateId()) {
            return new int[]{
                    ItemList.riftCrystal,
                    ItemList.riftWood,
                    ItemList.riftStone
            };
        } else if (templateId == CustomItems.toolCache.getTemplateId()) {
            return new int[]{
                    ItemList.hatchet,
                    ItemList.knifeCarving,
                    ItemList.pickAxe,
                    ItemList.saw,
                    ItemList.shovel,
                    ItemList.rake,
                    ItemList.hammerMetal,
                    ItemList.hammerWood,
                    ItemList.anvilSmall,
                    ItemList.cheeseDrill,
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
                    ItemList.file,
                    ItemList.awl,
                    ItemList.leatherKnife,
                    ItemList.scissors,
                    ItemList.clayShaper,
                    ItemList.spatula,
                    ItemList.fruitpress,
                    ItemList.trowel,
                    ItemList.groomingBrush
            };
        }
        return null;
    }

    public static void adjustBasicItem(int templateId, float quality, Item item) {
        if (templateId == CustomItems.armourCache.getTemplateId()) {
            if (Server.rand.nextInt(800) < quality) {
                if (item.getRarity() == 0) {
                    if (Server.rand.nextInt(1800) < quality) {
                        item.setRarity(MiscConstants.SUPREME);
                    } else {
                        item.setRarity(MiscConstants.RARE);
                    }
                }
            }
            if (quality > 50) {
                if (quality > 95 && Server.rand.nextBoolean()) {
                    ItemTools.applyEnchant(item, Enchants.BUFF_SHARED_PAIN, quality * Server.rand.nextFloat() * 0.7f);
                    ItemTools.applyEnchant(item, Enchants.BUFF_WEBARMOUR, quality * Server.rand.nextFloat() * 0.7f);
                } else if (Server.rand.nextBoolean()) {
                    byte[] armourEnchants = {
                            Enchants.BUFF_SHARED_PAIN,
                            Enchants.BUFF_WEBARMOUR
                    };
                    ItemTools.applyEnchant(item, armourEnchants[Server.rand.nextInt(armourEnchants.length)], quality * Server.rand.nextFloat() * 1.5f);
                }
            }
            if (quality > 80 && Server.rand.nextInt(4) == 0) {
                byte[] materials = {
                        Materials.MATERIAL_ADAMANTINE,
                        Materials.MATERIAL_COTTON,
                        Materials.MATERIAL_GLIMMERSTEEL,
                        Materials.MATERIAL_IRON,
                        Materials.MATERIAL_LEATHER,
                        Materials.MATERIAL_SERYLL,
                        Materials.MATERIAL_STEEL
                };
                item.setMaterial(materials[Server.rand.nextInt(materials.length)]);
            } else {
                if (item.isMetal()) {
                    item.setMaterial(Materials.MATERIAL_IRON);
                } else if (item.isLeather()) {
                    item.setMaterial(Materials.MATERIAL_LEATHER);
                }
            }
        } else if (templateId == CustomItems.artifactCache.getTemplateId()) {
            byte[] materials = {
                    Materials.MATERIAL_ADAMANTINE,
                    Materials.MATERIAL_GLIMMERSTEEL,
                    Materials.MATERIAL_SERYLL,
                    Materials.MATERIAL_STEEL,
                    Materials.MATERIAL_STEEL
            };
            item.setMaterial(materials[Server.rand.nextInt(materials.length)]);
            if (Server.rand.nextInt(400) < quality) {
                if (item.getRarity() == 0) {
                    if (Server.rand.nextInt(900) < quality) {
                        item.setRarity(MiscConstants.SUPREME);
                    } else {
                        item.setRarity(MiscConstants.RARE);
                    }
                }
            }
            if (quality > 50 && Server.rand.nextBoolean()) {
                byte[] enchants = {
                        Enchants.BUFF_WIND_OF_AGES,
                        Enchants.BUFF_BLESSINGDARK
                };
                ItemTools.applyEnchant(item, enchants[Server.rand.nextInt(enchants.length)], quality * 0.5f + (quality * 0.5f * Server.rand.nextFloat()));
                ItemTools.applyEnchant(item, Enchants.BUFF_NIMBLENESS, quality * 0.3f + (quality * 0.7f * Server.rand.nextFloat()));
            } else if (quality > 30) {
                ItemTools.applyEnchant(item, Enchants.BUFF_LIFETRANSFER, quality * 0.6f + (quality * 0.6f * Server.rand.nextFloat()));
            }
        } else if (templateId == CustomItems.crystalCache.getTemplateId()) {
            if (Server.rand.nextInt(500) < quality) {
                item.setRarity(MiscConstants.RARE);
            }
        } else if (templateId == CustomItems.toolCache.getTemplateId()) {
            byte[] materials = {
                    Materials.MATERIAL_SERYLL,
                    Materials.MATERIAL_GLIMMERSTEEL,
                    Materials.MATERIAL_ADAMANTINE,
                    Materials.MATERIAL_STEEL,
                    Materials.MATERIAL_TIN,
                    Materials.MATERIAL_BRONZE,
                    Materials.MATERIAL_BRASS,
                    Materials.MATERIAL_ZINC,
                    Materials.MATERIAL_IRON,
                    Materials.MATERIAL_COPPER,
                    Materials.MATERIAL_GOLD,
                    Materials.MATERIAL_LEAD,
                    Materials.MATERIAL_SILVER
            };
            item.setMaterial(materials[Server.rand.nextInt(materials.length)]);
            if (Server.rand.nextInt(1200) < quality) {
                if (item.getRarity() == 0) {
                    if (Server.rand.nextInt(2700) < quality) {
                        item.setRarity(MiscConstants.SUPREME);
                    } else {
                        item.setRarity(MiscConstants.RARE);
                    }
                }
            }
            if (Server.rand.nextInt(200) < quality) {
                byte rune = (byte) (Server.rand.nextInt(78) - 128);
                if (!ItemTools.isSingleUseRune(rune)) {
                    ItemTools.applyEnchant(item, rune, 50);
                }
            }
            if (quality > 30 && Server.rand.nextInt(250) < quality) {
                ItemTools.applyEnchant(item, Enchants.BUFF_WIND_OF_AGES, quality * 0.6f + (quality * 0.6f * Server.rand.nextFloat()));
            }
            if (quality > 30 && Server.rand.nextInt(250) < quality) {
                ItemTools.applyEnchant(item, Enchants.BUFF_CIRCLE_CUNNING, quality * 0.6f + (quality * 0.6f * Server.rand.nextFloat()));
            }
            if (quality > 50 && Server.rand.nextInt(250) < quality) { // Efficiency
                ItemTools.applyEnchant(item, (byte) 114, quality * 0.6f + (quality * 0.6f * Server.rand.nextFloat()));
            }
            if (quality > 70 && Server.rand.nextInt(350) < quality) {
                ItemTools.applyEnchant(item, Enchants.BUFF_BLESSINGDARK, quality * 0.6f + (quality * 0.6f * Server.rand.nextFloat()));
            }
            if (quality > 90 && Server.rand.nextInt(5000) < quality) { // Titanforged
                ItemTools.applyEnchant(item, (byte) 120, quality * 0.2f + (quality * 0.2f * Server.rand.nextFloat()));
            }
        }
    }

    public static int getBasicNums(int templateId) {
        if (templateId == CustomItems.crystalCache.getTemplateId()) {
            return Server.rand.nextInt(5) + 8;
        } else if (templateId == CustomItems.gemCache.getTemplateId()) {
            return 2;
        }
        return 1;
    }

    public static int getExtraBasicNums(int templateId, float quality) {
        if (templateId == CustomItems.armourCache.getTemplateId()) {
            return Server.rand.nextInt(2);
        } else if (templateId == CustomItems.crystalCache.getTemplateId()) {
            return Server.rand.nextInt(Math.max((int) (quality * 0.08f), 2));
        } else if (templateId == CustomItems.dragonCache.getTemplateId()) {
            if (Server.rand.nextInt(200) <= quality) {
                return 1;
            }
        } else if (templateId == CustomItems.gemCache.getTemplateId()) {
            return Server.rand.nextInt(Math.max((int) (quality * 0.03f), 2));
        } else if (templateId == CustomItems.potionCache.getTemplateId()) {
            if (Server.rand.nextInt(300) <= quality) {
                return 1;
            }
        } else if (templateId == CustomItems.riftCache.getTemplateId()) {
            if (Server.rand.nextInt(300) <= quality) {
                return 2;
            } else if (Server.rand.nextInt(100) <= quality) {
                return 1;
            }
        }
        return 0;
    }

    public static int getExtraItemChance(int templateId) {
		/*if(templateId == CustomItems.armourCache.getTemplateId()){
			return 1600;
		}else*/
        if (templateId == CustomItems.dragonCache.getTemplateId()) {
            return 500;
        } else if (templateId == CustomItems.gemCache.getTemplateId()) {
            return 150;
        } else if (templateId == CustomItems.moonCache.getTemplateId()) {
            return 500;
        }
        return -1;
    }

    public static int[] getExtraTemplates(int templateId) {
		/*if(templateId == CustomItems.armourCache.getTemplateId()){
			return new int[]{
					GlimmerscaleBoot.getId(),
					GlimmerscaleGlove.getId(),
					GlimmerscaleHelmet.getId(),
					GlimmerscaleHose.getId(),
					GlimmerscaleSleeve.getId(),
					GlimmerscaleVest.getId(),
					SpectralBoot.getId(),
					SpectralCap.getId(),
					SpectralGlove.getId(),
					SpectralHose.getId(),
					SpectralJacket.getId(),
					SpectralSleeve.getId()
			};
		}else*/
        if (templateId == CustomItems.dragonCache.getTemplateId()) {
            return new int[]{
                    ItemList.dragonLeatherBoot,
                    ItemList.dragonLeatherCap,
                    ItemList.dragonLeatherGlove,
                    ItemList.dragonLeatherHose,
                    ItemList.dragonLeatherJacket,
                    ItemList.dragonLeatherSleeve,
                    ItemList.dragonScaleBoot,
                    ItemList.dragonScaleGauntlet,
                    ItemList.dragonScaleHose,
                    ItemList.dragonScaleJacket,
                    ItemList.dragonScaleSleeve
            };
        } else if (templateId == CustomItems.gemCache.getTemplateId()) {
            return new int[]{
                    ItemList.opalBlack,
                    ItemList.diamondStar,
                    ItemList.emeraldStar,
                    ItemList.rubyStar,
                    ItemList.sapphireStar
            };
        } else if (templateId == CustomItems.moonCache.getTemplateId()) {
            return new int[]{
                    ItemList.chainSleeve,
                    ItemList.chainJacket,
                    ItemList.chainHose,
                    ItemList.chainGlove,
                    ItemList.chainCoif,
                    ItemList.chainBoot,
                    ItemList.plateSleeve,
                    ItemList.plateJacket,
                    ItemList.plateHose,
                    ItemList.plateBoot,
                    ItemList.plateGauntlet,
                    ItemList.helmetOpen,
                    ItemList.helmetGreat,
                    ItemList.helmetBasinet
            };
        }
        return null;
    }

    public static void adjustExtraItem(int templateId, Item item) {
        if (templateId == CustomItems.armourCache.getTemplateId()) {
            item.setColor(WurmColor.createColor(100, 100, 100));
        } else if (templateId == CustomItems.dragonCache.getTemplateId()) {
            item.setMaterial(Materials.MATERIAL_LEATHER);
        } else if (templateId == CustomItems.moonCache.getTemplateId()) {
            item.setMaterial(Server.rand.nextBoolean() ? Materials.MATERIAL_ADAMANTINE : Materials.MATERIAL_GLIMMERSTEEL);
        }
    }

    public static void openCache(Creature performer, Item cache) {
        int templateId = cache.getTemplateId();
        Item inv = performer.getInventory();
        float quality = cache.getCurrentQualityLevel();
        float baseQL = getBaseQuality(quality);
        float randQL = getRandomQuality(quality);
        if (createsCustomBasic(templateId)) {
            getCustomBasic(performer, cache);
        } else {
            int[] basicTemplates = getBasicTemplates(templateId);
            if (basicTemplates == null) {
                RequiemLogging.logWarning("Error: Basic Templates are null for cache with template id " + templateId);
                return;
            }
            int basicNums = getBasicNums(templateId);
            basicNums += getExtraBasicNums(templateId, quality);
            int i = 0;
            while (i < basicNums) {
                try {
                    float basicQuality = Math.max(baseQL + (randQL * Server.rand.nextFloat()), baseQL + (randQL * Server.rand.nextFloat()));
                    basicQuality = Math.min(minimumQuality + basicQuality, 100f);
                    Item basicItem = ItemFactory.createItem(basicTemplates[Server.rand.nextInt(basicTemplates.length)], basicQuality, "");
                    if (cache.getRarity() > basicItem.getRarity()) {
                        basicItem.setRarity(cache.getRarity());
                    }
                    adjustBasicItem(templateId, quality, basicItem);
                    if (adjustBasicWeight(templateId)) {
                        float weightMult = getWeightMultiplier(templateId, quality);
                        basicItem.setWeight((int) (basicItem.getWeightGrams() * weightMult), true);
                    }
                    inv.insertItem(basicItem, true);
                } catch (FailedException | NoSuchTemplateException e) {
                    RequiemLogging.logWarning("Error: Failed to create item for cache with template id " + templateId);
                    e.printStackTrace();
                }
                i++;
            }
        }
        int chance = getExtraItemChance(templateId);
        if (chance > 0 && Server.rand.nextInt(chance) <= quality) {
            try {
                int[] extraTemplates = getExtraTemplates(templateId);
                if (extraTemplates != null) {
                    float extraQuality = Math.max(baseQL + (randQL * Server.rand.nextFloat()), baseQL + (randQL * Server.rand.nextFloat()));
                    extraQuality = Math.min(minimumQuality + extraQuality, 100f);
                    Item extraItem = ItemFactory.createItem(extraTemplates[Server.rand.nextInt(extraTemplates.length)], extraQuality, "");
                    if (cache.getRarity() > extraItem.getRarity()) {
                        extraItem.setRarity(cache.getRarity());
                    }
                    adjustExtraItem(templateId, extraItem);
                    inv.insertItem(extraItem, true);
                }
            } catch (FailedException | NoSuchTemplateException e) {
                RequiemLogging.logWarning("Error: Failed to create item for cache with template id " + templateId);
                e.printStackTrace();
            }
        }
    }

}
