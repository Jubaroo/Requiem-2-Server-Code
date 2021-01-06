package org.jubaroo.mods.wurm.server.items;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.Items;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.combat.ArmourTemplate;
import com.wurmonline.server.combat.Weapon;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.economy.Change;
import com.wurmonline.server.economy.Economy;
import com.wurmonline.server.economy.MonetaryConstants;
import com.wurmonline.server.items.*;
import com.wurmonline.server.zones.FocusZone;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.classhooks.InvocationHandlerFactory;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.actions.magicItems.PortalTeleportAction;
import org.jubaroo.mods.wurm.server.misc.templates.StructureTemplate;
import org.jubaroo.mods.wurm.server.server.Constants;
import org.jubaroo.mods.wurm.server.server.OnItemTemplateCreated;
import org.jubaroo.mods.wurm.server.tools.EffectsTools;
import org.jubaroo.mods.wurm.server.vehicles.CustomVehicles;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static org.jubaroo.mods.wurm.server.items.CustomItems.*;

public class ItemMod {

    public static void modifyOnCreate(Item item) {
        int templateId = item.getTemplate().getTemplateId();

        if (Constants.itemCreateLogging) {
            RequiemLogging.ItemCreationLogging(item);
        }
        if (item.getTemplateId() == ItemList.papyrusSheet && item.getName().contains("pirate") || item.getName().contains("Pirate")) {
            final String str = "\";maxlines=\"0\"}text{text=\"As I write this note we are being attacked by rival pirates, and it looks like we’re heavily outnumbered. I’m going to try to stand my ground as long as I can, but that may prove futile. If you find this note and I am deceased, please travel to the town of Tartarus to seek the help of our Goddess, Libila. Go where all souls are laid to rest, that’s where she will be able to get in contact with me so I can tell you more! I have to stop writing now, they’re knocking down the door!\n\n \n";
            item.setInscription(str, "Pirate Scout");
        }
        if (Constants.lampsAutoLight) {
            if (item.isStreetLamp() || templateId == ItemList.candelabra) {
                item.setAuxData((byte) 127);
                item.setIsAutoLit(true);
                item.savePermissions();
            }
        }
        if (item.getName().contains("Totem")) {
            item.setHasNoDecay(true);
            item.savePermissions();
        }
        if (item.getTemplateId() == treasureHuntChestId) {
            item.setRarity(MiscConstants.FANTASTIC);
        }
        if (item.getTemplateId() == eventGravestoneId) {
            item.setColor(WurmColor.createColor(1, 1, 1));
        }
        // GM only spawn for online purchases
        //if (templateId == steelToolsBackpack.getTemplateId()) {
        //    backpackOfToolsInsert(item, "Steel Tool Package", ItemMaterials.MATERIAL_STEEL);
        //}
        //if (templateId == addyToolsBackpack.getTemplateId()) {
        //    backpackOfToolsInsert(item, "Adamantine Tool Package", ItemMaterials.MATERIAL_ADAMANTINE);
        //}

    }

    static void createCustomArmours(){
        try {
            RequiemLogging.logInfo("Beginning custom armour creation.");
            // Spectral
            new ArmourTemplate(spectralBoot.getTemplateId(), ArmourTemplate.ARMOUR_TYPE_LEATHER_DRAGON, 0.002f);
            new ArmourTemplate(spectralCap.getTemplateId(), ArmourTemplate.ARMOUR_TYPE_LEATHER_DRAGON, 0.003f);
            new ArmourTemplate(spectralGlove.getTemplateId(), ArmourTemplate.ARMOUR_TYPE_LEATHER_DRAGON, 0.002f);
            new ArmourTemplate(spectralLeggings.getTemplateId(), ArmourTemplate.ARMOUR_TYPE_LEATHER_DRAGON, 0.0075f);
            new ArmourTemplate(spectralVest.getTemplateId(), ArmourTemplate.ARMOUR_TYPE_LEATHER_DRAGON, 0.01f);
            new ArmourTemplate(spectralSleeve.getTemplateId(), ArmourTemplate.ARMOUR_TYPE_LEATHER_DRAGON, 0.004f);
            // Glimmerscale
            new ArmourTemplate(glimmerscaleBoot.getTemplateId(), ArmourTemplate.ARMOUR_TYPE_SCALE_DRAGON, 0.002f);
            new ArmourTemplate(glimmerscaleGlove.getTemplateId(), ArmourTemplate.ARMOUR_TYPE_SCALE_DRAGON, 0.001f);
            new ArmourTemplate(glimmerscaleHelmet.getTemplateId(), ArmourTemplate.ARMOUR_TYPE_SCALE_DRAGON, 0.008f);
            new ArmourTemplate(glimmerscaleLeggings.getTemplateId(), ArmourTemplate.ARMOUR_TYPE_SCALE_DRAGON, 0.05f);
            new ArmourTemplate(glimmerscaleSleeve.getTemplateId(), ArmourTemplate.ARMOUR_TYPE_SCALE_DRAGON, 0.008f);
            new ArmourTemplate(glimmerscaleVest.getTemplateId(), ArmourTemplate.ARMOUR_TYPE_SCALE_DRAGON, 0.05f);
            // Special
            new ArmourTemplate(dragonSkullHelmet.getTemplateId(), ArmourTemplate.ARMOUR_TYPE_NONE, 0.003f);
            new ArmourTemplate(horsemanHelm.getTemplateId(), ArmourTemplate.ARMOUR_TYPE_PLATE, 0.001f);
            new ArmourTemplate(hatId, ArmourTemplate.ARMOUR_TYPE_PLATE, 0.005f);
        } catch (IllegalArgumentException | ClassCastException e) {
            e.printStackTrace();
        }
    }

    public static int getModdedImproveTemplateId(Item item) {
        try {
            int tid = item.getTemplateId();
        if (tid == spectralBoot.getTemplateId()) {
            return spectralHideId;
        } else if (tid == spectralCap.getTemplateId()) {
            return spectralHideId;
        } else if (tid == spectralGlove.getTemplateId()) {
            return spectralHideId;
        } else if (tid == spectralLeggings.getTemplateId()) {
            return spectralHideId;
        } else if (tid == spectralVest.getTemplateId()) {
            return spectralHideId;
        } else if (tid == spectralSleeve.getTemplateId()) {
            return spectralHideId;
        } else if (tid == glimmerscaleBoot.getTemplateId()) {
            return glimmerscaleId;
        } else if (tid == glimmerscaleGlove.getTemplateId()) {
            return glimmerscaleId;
        } else if (tid == glimmerscaleHelmet.getTemplateId()) {
            return glimmerscaleId;
        } else if (tid == glimmerscaleLeggings.getTemplateId()) {
            return glimmerscaleId;
        } else if (tid == glimmerscaleSleeve.getTemplateId()) {
            return glimmerscaleId;
        } else if (tid == glimmerscaleVest.getTemplateId()) {
            return glimmerscaleId;
        }
        } catch (IllegalArgumentException | ClassCastException e) {
            e.printStackTrace();
        }
        return (int) MiscConstants.NOID;
    }

    static void createCustomWeapons() {
        try {
            RequiemLogging.debug("Beginning custom weapon creation.");
            new Weapon(battleYoyoId, 6.85f, 3.85f, 0.008f, 2, 2, 0f, 0d);
            new Weapon(clubId, 8.1f, 4.5f, 0.002f, 3, 3, 0.4f, 0.5d);
            new Weapon(knucklesId, 3.6f, 2.2f, 0.002f, 1, 1, 0.2f, 0.5d);
            new Weapon(warhammerId, 9.40f, 5.6f, 0.008f, 4, 3, 1f, 0d);
            new Weapon(combatScytheId, 6.75f, 2.5f, 0.009f, 3, 2, 0.6f, 0d);
            // Titan Weaponry
            //new Weapon(MaartensMight.getId(), 11f, 5f, 0.02f, 4, 4, 1f, 0d);
            //new Weapon(RaffehsRage.getId(), 9.5f, 4.25f, 0.02f, 3, 3, 1f, 0d);
            //new Weapon(VindictivesVengeance.getId(), 9f, 4f, 0.02f, 3, 3, 0.5f, 0d);
            //new Weapon(WilhelmsWrath.getId(), 6f, 4.5f, 0.02f, 6, 6, 0.5f, 0d);
            // GM Weapon
            new Weapon(godSlayerId, 999f, 1f, 100f, 6, 6, 1f, 0d);
        } catch (IllegalArgumentException | ClassCastException e) {
            e.printStackTrace();
        }
    }

    /*
    public static Item createNewTitanWeapon() {
        try {
            int[] weapons = {MaartensMight.getId(),RaffehsRage.getId(), VindictivesVengeance.getId(), WilhelmsWrath.getId()};
            int templateId = weapons[Server.rand.nextInt(weapons.length)];
            Item titanWeapon = ItemFactory.createItem(templateId, 90f + (Server.rand.nextFloat() * 5f), Materials.MATERIAL_ADAMANTINE, Server.rand.nextBoolean() ? (byte) 2 : (byte) 3, "");
            if (templateId == MaartensMight.getId()) {
                ItemTools.applyEnchant(titanWeapon, Enchants.BUFF_NIMBLENESS, 250);
            } else if (templateId == RaffehsRage.getId()) {
                ItemTools.applyEnchant(titanWeapon, Enchants.BUFF_FLAMING_AURA, 150);
                ItemTools.applyEnchant(titanWeapon, Enchants.BUFF_FROSTBRAND, 150);
            } else if (templateId == VindictivesVengeance.getId()) {
                ItemTools.applyEnchant(titanWeapon, Enchants.BUFF_BLESSINGDARK, 300);
                ItemTools.applyEnchant(titanWeapon, Enchants.BUFF_NIMBLENESS, 200);
            } else if (templateId == WilhelmsWrath.getId()) {
                ItemTools.applyEnchant(titanWeapon, Enchants.BUFF_ROTTING_TOUCH, 300);
                ItemTools.applyEnchant(titanWeapon, Enchants.BUFF_BLOODTHIRST, 100);
            }
            return titanWeapon;
        } catch (FailedException | NoSuchTemplateException e) {
            e.printStackTrace();
        }
        return null;
    }
     */

    private static void backpackOfToolsInsert(Item item, String name, byte itemMaterial) {
        try {
            item.setQualityLevel((float) 70.0);
            item.setName(name);
            item.insertItem(ItemFactory.createItem(ItemList.hatchet, (float) 85.0, itemMaterial, MiscConstants.COMMON, null), true);
            item.insertItem(ItemFactory.createItem(ItemList.knifeCarving, (float) 85.0, itemMaterial, MiscConstants.COMMON, null), true);
            item.insertItem(ItemFactory.createItem(ItemList.flintSteel, (float) 85.0, itemMaterial, MiscConstants.COMMON, null), true);
            item.insertItem(ItemFactory.createItem(ItemList.knifeButchering, (float) 85.0, itemMaterial, MiscConstants.COMMON, null), true);
            item.insertItem(ItemFactory.createItem(ItemList.awl, (float) 85.0, itemMaterial, MiscConstants.COMMON, null), true);
            item.insertItem(ItemFactory.createItem(ItemList.leatherKnife, (float) 85.0, itemMaterial, MiscConstants.COMMON, null), true);
            item.insertItem(ItemFactory.createItem(ItemList.hammerMetal, (float) 85.0, itemMaterial, MiscConstants.COMMON, null), true);
            item.insertItem(ItemFactory.createItem(ItemList.crowbar, (float) 85.0, itemMaterial, MiscConstants.COMMON, null), true);
            item.insertItem(ItemFactory.createItem(ItemList.stoneChisel, (float) 85.0, itemMaterial, MiscConstants.COMMON, null), true);
            item.insertItem(ItemFactory.createItem(ItemList.anvilSmall, (float) 85.0, itemMaterial, MiscConstants.COMMON, null), true);
        } catch (FailedException | NoSuchTemplateException e) {
            e.printStackTrace();
        }
    }

    public static void modifyItemsOnCreated() {
        createCustomWeapons();
        createCustomArmours();
        // Items
        // Modify fragment counts
        ItemHelper.setFragments(armourCache.getTemplateId(), 18);
        ItemHelper.setFragments(artifactCache.getTemplateId(), 33);
        ItemHelper.setFragments(crystalCache.getTemplateId(), 11);
        ItemHelper.setFragments(dragonCache.getTemplateId(), 19);
        ItemHelper.setFragments(gemCache.getTemplateId(), 7);
        ItemHelper.setFragments(moonCache.getTemplateId(), 14);
        ItemHelper.setFragments(potionCache.getTemplateId(), 18);
        ItemHelper.setFragments(riftCache.getTemplateId(), 24);
        ItemHelper.setFragments(titanCache.getTemplateId(), 100);
        ItemHelper.setFragments(toolCache.getTemplateId(), 27);
        ItemHelper.setFragments(treasureMapCache.getTemplateId(), 38);
        ItemHelper.setFragments(affinityOrbId, 25);
        ItemHelper.setFragments(arcaneShardId, 100);
        ItemHelper.setFragments(lesserFireCrystal.getTemplateId(), 4);
        // Tier 4
        ItemHelper.setFragments(ItemList.statueWorg, 40);
        ItemHelper.setFragments(ItemList.statueEagle, 40);
        // Tier 5
        ItemHelper.setFragments(ItemList.statueHellHorse, 45);
        ItemHelper.setFragments(ItemList.statueDrake, 45);
        // Tier 6
        ItemHelper.setFragments(ItemList.statueFo, 50);
        ItemHelper.setFragments(ItemList.statueMagranon, 50);
        ItemHelper.setFragments(ItemList.statueLibila, 50);
        ItemHelper.setFragments(ItemList.statueVynora, 50);
        // =======================================  Make items able to be combined =====================================
        ItemHelper.combineItem(ItemList.leather, true);
        ItemHelper.combineItem(ItemList.log, true);
        ItemHelper.centimetersZItem(ItemList.log, 50);
        ItemHelper.volumeItem(ItemList.log);
        ItemHelper.combineColdItem(ItemList.cochineal, true);
        ItemHelper.combineItem(ItemList.cochineal, true);
        ItemHelper.combineColdItem(ItemList.woad, true);
        ItemHelper.combineItem(ItemList.woad, true);
        ItemHelper.combineColdItem(ItemList.acorn, true);
        ItemHelper.combineItem(ItemList.acorn, true);
        // =============================================== Set items value =============================================
        ItemHelper.valueItem(ItemList.handMirror, MonetaryConstants.COIN_SILVER * 2);
        ItemHelper.valueItem(ItemList.goldenMirror, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.valueItem(ItemList.potionAffinity, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.valueItem(ItemList.potionFrostDamage, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.valueItem(ItemList.potionFireDamage, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.valueItem(ItemList.potionAcidDamage, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.valueItem(ItemList.potionCarpentry, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.valueItem(ItemList.potionWoodcutting, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.valueItem(ItemList.potionMasonry, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.valueItem(ItemList.potionStonecutting, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.valueItem(ItemList.potionShipbuilding, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.valueItem(ItemList.potionLeatherworking, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.valueItem(ItemList.potionBlacksmithing, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.valueItem(ItemList.potionFletching, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.valueItem(ItemList.potionArmourSmithing, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.valueItem(ItemList.potionTailoring, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.valueItem(ItemList.potionMining, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.valueItem(ItemList.potionWaterwalking, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.valueItem(ItemList.potionRopemaking, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.valueItem(ItemList.potionWeaponSmithing, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.valueItem(ItemList.potionImbueShatterprot, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.valueItem(ItemList.potionTransmutation, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.valueItem(ItemList.creatureCrate, MonetaryConstants.COIN_SILVER * 5);
        ItemHelper.fullPriceItem(ItemList.handMirror, true);
        ItemHelper.fullPriceItem(ItemList.goldenMirror, true);
        ItemHelper.fullPriceItem(ItemList.potionAffinity, true);
        ItemHelper.fullPriceItem(ItemList.potionFrostDamage, true);
        ItemHelper.fullPriceItem(ItemList.potionFireDamage, true);
        ItemHelper.fullPriceItem(ItemList.potionAcidDamage, true);
        ItemHelper.fullPriceItem(ItemList.potionCarpentry, true);
        ItemHelper.fullPriceItem(ItemList.potionWoodcutting, true);
        ItemHelper.fullPriceItem(ItemList.potionMasonry, true);
        ItemHelper.fullPriceItem(ItemList.potionStonecutting, true);
        ItemHelper.fullPriceItem(ItemList.potionShipbuilding, true);
        ItemHelper.fullPriceItem(ItemList.potionLeatherworking, true);
        ItemHelper.fullPriceItem(ItemList.potionBlacksmithing, true);
        ItemHelper.fullPriceItem(ItemList.potionFletching, true);
        ItemHelper.fullPriceItem(ItemList.potionArmourSmithing, true);
        ItemHelper.fullPriceItem(ItemList.potionTailoring, true);
        ItemHelper.fullPriceItem(ItemList.potionMining, true);
        ItemHelper.fullPriceItem(ItemList.potionWaterwalking, true);
        ItemHelper.fullPriceItem(ItemList.potionRopemaking, true);
        ItemHelper.fullPriceItem(ItemList.potionWeaponSmithing, true);
        ItemHelper.fullPriceItem(ItemList.potionImbueShatterprot, true);
        ItemHelper.fullPriceItem(ItemList.potionTransmutation, true);
        ItemHelper.fullPriceItem(ItemList.creatureCrate, true);
        // ============================================= Make items loadable ===========================================
        ItemHelper.isTransportableItem(ItemList.mailboxWood, true);
        ItemHelper.isTransportableItem(ItemList.mailboxStone, true);
        ItemHelper.isTransportableItem(ItemList.mailboxWoodTwo, true);
        ItemHelper.isTransportableItem(ItemList.mailboxStoneTwo, true);
        ItemHelper.isTransportableItem(ItemList.bellTower, true);
        ItemHelper.isTransportableItem(ItemList.trashBin, true);
        ItemHelper.isTransportableItem(ItemList.altarStone, true);
        ItemHelper.isTransportableItem(ItemList.altarWood, true);
        ItemHelper.isTransportableItem(ItemList.altarSilver, true);
        ItemHelper.isTransportableItem(ItemList.altarGold, true);
        // ========================================= Make weapons one-handed ===========================================
        ItemHelper.isTwohandedItem(ItemList.spearLong, false);
        ItemHelper.isTwohandedItem(ItemList.spearSteel, false);
        // ============================================= Set item difficulty ===========================================
        ItemHelper.difficultyItem(ItemList.dirtPile, 50);
        ItemHelper.difficultyItem(ItemList.sand, 50);
        ItemHelper.difficultyItem(ItemList.sandstone, 50);
        ItemHelper.difficultyItem(ItemList.emeraldStar, 66);
        ItemHelper.difficultyItem(ItemList.rubyStar, 66);
        ItemHelper.difficultyItem(ItemList.sapphireStar, 66);
        ItemHelper.difficultyItem(ItemList.diamondStar, 66);
        ItemHelper.difficultyItem(ItemList.opalBlack, 66);
        ItemHelper.difficultyItem(ItemList.emerald, 30);
        ItemHelper.difficultyItem(ItemList.ruby, 30);
        ItemHelper.difficultyItem(ItemList.sapphire, 30);
        ItemHelper.difficultyItem(ItemList.diamond, 30);
        ItemHelper.difficultyItem(ItemList.opal, 30);
        // ========================================== Set items as decoration ==========================================
        ItemHelper.decorationItem(ItemList.stoneKeystone, true);
        ItemHelper.decorationItem(ItemList.marbleKeystone, true);
        ItemHelper.decorationItem(ItemList.skull, true);
        // ================================================ Make items food ============================================
        ItemHelper.foodItem(ItemList.wemp, true);
        // ============================================ Make items inFoodGroup =========================================
        ItemHelper.inFoodGroupItem(ItemList.wemp, 1158);
        // ============================================ Make items isRecipeItem ========================================
        ItemHelper.isRecipeItemItem(ItemList.shaft, true);
        // ========================================== Make items isImproveItem =========================================
        ItemHelper.isImproveItem(ItemList.ropeAnchor, true);
        ItemHelper.isImproveItem(ItemList.compass, true);
        // =============================================== Make items repairable =======================================
        ItemHelper.repairableItem(ItemList.ropeAnchor, true);
        ItemHelper.repairableItem(ItemList.compass, true);
        // ============================================ Set items material =============================================
        ItemHelper.materialItem(ItemList.dragonScaleJacket, Materials.MATERIAL_LEATHER);
        // ====================================== Set items CentimetersXYZ =============================================
        ItemHelper.centimetersYItem(ItemList.kindling, 10);
        ItemHelper.centimetersZItem(ItemList.kindling, 10);
        ItemHelper.volumeItem(ItemList.kindling);
        ItemHelper.containerCentimetersXItem(ItemList.fruitpress, 30);
        ItemHelper.containerCentimetersYItem(ItemList.fruitpress, 30);
        ItemHelper.containerCentimetersZItem(ItemList.fruitpress, 50);
        ItemHelper.containerVolumeItem(ItemList.fruitpress);
        // ============================================ Set items decayTime ============================================
        ItemHelper.decayOnDeedItem(ItemList.compass, false);
        ItemHelper.decayOnDeedItem(ItemList.compass, false);
        // ========================================== Set items isPlantOneAWeek ========================================
        ItemHelper.isPlantOneAWeekItem(ItemList.signLarge, false);
        ItemHelper.isPlantOneAWeekItem(ItemList.signSmall, false);
        ItemHelper.isPlantOneAWeekItem(ItemList.signPointing, false);
        ItemHelper.isPlantOneAWeekItem(ItemList.signShop, false);
        // ======================================= Set items hideAddToCreationWindow ===================================
        ItemHelper.hideAddToCreationWindowItem(ItemList.speedShrineHota, false);
        // ======================================= Set items noDrop ===================================
        ItemHelper.noDropItem(ItemList.affinityToken, false);
    }

    public static void modifyItemsOnServerStarted() {
        // ============================================== Set items name ===============================================
        ItemHelper.itemName(ItemList.guardTower, "changeable guard tower");
        ItemHelper.itemName(ItemList.guardTowerMol, "Horde of the Summoned guard tower");
        ItemHelper.itemName(ItemList.guardTowerHots, "Mol-Rehan guard tower");
        ItemHelper.itemName(ItemList.guardTowerFreedom, "Freedom guard tower");
        ItemHelper.itemName(ItemList.wemp, "hemp plants");
        ItemHelper.itemName(ItemList.wempSeed, "hemp seed");
        ItemHelper.itemName(ItemList.wempFibre, "hemp fibre");
        ItemHelper.itemName(ItemList.potionAffinity, "affinity potion");
        ItemHelper.itemName(ItemList.wagon, "changeable wagon");
        ItemHelper.itemName(ItemList.bannerKingdom, "changeable banner");
        ItemHelper.itemName(ItemList.tallKingdomBanner, "changeable tall banner");
        ItemHelper.itemName(ItemList.pavilion, "changeable pavilion");
        ItemHelper.itemName(ItemList.tentMilitary, "changeable military tent");
        ItemHelper.itemName(ItemList.flagKingdom, "changeable flag");
        ItemHelper.itemName(ItemList.tabard, "changeable tabard");
        // ============================================== Set itemDescriptionLong ======================================
        ItemHelper.itemDescriptionLongItem(ItemList.wemp, "Some hemp plants. It has very strong fibres.");
        ItemHelper.itemDescriptionLongItem(ItemList.wempSeed, "A few handfuls of hemp seed. It can be used to sow with, but hemp can be quite hard to grow to acceptable quality.");
        ItemHelper.itemDescriptionLongItem(ItemList.wempFibre, "Some strong hemp fibres.");
        ItemHelper.itemDescriptionLongItem(ItemList.rope, "A coarse rope made from hemp fibres.");
        ItemHelper.itemDescriptionLongItem(ItemList.bowString, "A strong string of entwined hemp fibres.");
        ItemHelper.itemDescriptionLongItem(ItemList.groomingBrush, "This wooden brush is used to groom animals. Its wooden handle is lined with lots of thin, strong hemp straws.");
        ItemHelper.itemDescriptionLongItem(CustomVehicles.vysePatreonWagon.getTemplateId(), "A wagon that has been designed and built specifically for the amazing Vyse.");
        ItemHelper.itemDescriptionLongItem(CustomVehicles.callanishPatreonWagon.getTemplateId(), "A wagon that has been designed and built specifically for the amazing Callanish.");
        // ============================================== Set isNoDrop =================================================
        ItemHelper.noDropItem(ItemList.sleepPowder, false);
        ItemHelper.noDropItem(ItemList.compass, false);
        ItemHelper.noDropItem(champagneId, false);
        ItemHelper.noDropItem(ItemList.rodTransmutation, false);
        // ============================================== Set modelName ===============================================
        ItemHelper.modelNameItem(ItemList.sleepPowder, "model.resource.flour.");
        // ========================================== Make items isImproveItem =========================================
        ItemHelper.isImproveItem(ItemList.ropeAnchor, true);
        ItemHelper.isImproveItem(ItemList.compass, true);
        // =============================================== Make items repairable =======================================
        ItemHelper.repairableItem(ItemList.ropeAnchor, true);
        ItemHelper.repairableItem(ItemList.compass, true);
        // =============================================== Change item container size ==================================
        ItemHelper.containerCentimetersXItem(ItemList.fruitpress, 30);
        ItemHelper.containerCentimetersYItem(ItemList.fruitpress, 30);
        ItemHelper.containerCentimetersZItem(ItemList.fruitpress, 50);
        ItemHelper.containerVolumeItem(ItemList.fruitpress);
        // =============================================== Change item weight ==========================================
        //ItemHelper.weightItem(RequiemItemIds.WAGON_ALTAR_ID, 300000);
        // ======================================= Set items noDrop ===================================
        ItemHelper.noDropItem(ItemList.affinityToken, false);
        if (Constants.addMissionItems) {
            // ============== Coin ================
            if (Constants.coins) {
                try {
                    ItemHelper.missionsItem(ItemList.coinIron);
                    ItemHelper.missionsItem(ItemList.coinIronFive);
                    ItemHelper.missionsItem(ItemList.coinIronTwenty);
                    ItemHelper.missionsItem(ItemList.coinCopper);
                    ItemHelper.missionsItem(ItemList.coinCopperFive);
                    ItemHelper.missionsItem(ItemList.coinCopperTwenty);
                    ItemHelper.missionsItem(ItemList.coinSilver);
                    ItemHelper.missionsItem(ItemList.coinSilverFive);
                    ItemHelper.missionsItem(ItemList.coinSilverTwenty);
                    ItemHelper.missionsItem(ItemList.coinGold);
                    ItemHelper.missionsItem(ItemList.coinGoldFive);
                    ItemHelper.missionsItem(ItemList.coinGoldTwenty);
                    RequiemLogging.debug("Coins added");
                } catch (Throwable e) {
                    RequiemLogging.logException("Error in adding Coins", e);
                }
            }
            // ============= Rift Items ==============
            if (Constants.riftItems) {
                try {
                    ItemHelper.missionsItem(ItemList.riftCrystal);
                    ItemHelper.missionsItem(ItemList.riftStone);
                    ItemHelper.missionsItem(ItemList.riftWood);
                    ItemHelper.notMissionsItem(ItemList.riftCrystal);
                    ItemHelper.notMissionsItem(ItemList.riftStone);
                    ItemHelper.notMissionsItem(ItemList.riftWood);
                    RequiemLogging.debug("Rift Items added");
                } catch (Throwable e) {
                    RequiemLogging.logException("Error in adding Rift Items", e);
                }
            }
            // ============= Magic Items ==============
            if (Constants.magicItems) {
                try {
                    ItemHelper.missionsItem(ItemList.shakerOrb);
                    ItemHelper.missionsItem(ItemList.rodBeguiling);
                    ItemHelper.missionsItem(ItemList.rodEruption);
                    ItemHelper.missionsItem(ItemList.sleepPowder);
                    ItemHelper.missionsItem(ItemList.chestNoDecayLarge);
                    ItemHelper.missionsItem(ItemList.chestNoDecaySmall);
                    ItemHelper.missionsItem(ItemList.tuningFork);
                    ItemHelper.missionsItem(ItemList.resurrectionStone);
                    ItemHelper.missionsItem(ItemList.teleportationStone);
                    ItemHelper.missionsItem(ItemList.teleportationTwig);
                    ItemHelper.missionsItem(ItemList.farwalkerAmulet);
                    ItemHelper.missionsItem(ItemList.rodTransmutation);
                    RequiemLogging.debug("Magic Items added");
                } catch (Throwable e) {
                    RequiemLogging.logException("Error in adding Magic Items", e);
                }
            }
            // ============= Wands ==============
            if (Constants.wands) {
                try {
                    ItemHelper.missionsItem(ItemList.wandOfTheSeas);
                    ItemHelper.missionsItem(ItemList.wandNature);
                    ItemHelper.missionsItem(ItemList.wandSculpting);
                    ItemHelper.missionsItem(ItemList.wandTeleport);
                    ItemHelper.missionsItem(ItemList.wandTile);
                    ItemHelper.missionsItem(ItemList.lcmWand);
                    RequiemLogging.debug("Wands added");
                } catch (Throwable e) {
                    RequiemLogging.logException("Error in adding Wands", e);
                }
            }
            // ============= Misc Items ==============
            if (Constants.miscItems) {
                try {
                    ItemHelper.missionsItem(ItemList.merchantContract);
                    ItemHelper.missionsItem(ItemList.traderContract);
                    ItemHelper.missionsItem(ItemList.merchantContract);
                    ItemHelper.missionsItem(ItemList.pillarHota);
                    ItemHelper.missionsItem(ItemList.julbord);
                    ItemHelper.missionsItem(ItemList.eggSmall);
                    ItemHelper.missionsItem(ItemList.pelt);
                    RequiemLogging.debug("Miscellaneous Items added");
                } catch (Throwable e) {
                    RequiemLogging.logException("Error in adding Miscellaneous Items", e);
                }
            }
            // ============ Metal Lumps =============
            if (Constants.metalLumps) {
                try {
                    ItemHelper.missionsItem(ItemList.goldBar);
                    ItemHelper.missionsItem(ItemList.silverBar);
                    ItemHelper.missionsItem(ItemList.ironBar);
                    ItemHelper.missionsItem(ItemList.copperBar);
                    ItemHelper.missionsItem(ItemList.zincBar);
                    ItemHelper.missionsItem(ItemList.leadBar);
                    ItemHelper.missionsItem(ItemList.steelBar);
                    ItemHelper.missionsItem(ItemList.adamantineBar);
                    ItemHelper.missionsItem(ItemList.glimmerSteelBar);
                    ItemHelper.missionsItem(ItemList.seryllBar);
                    ItemHelper.missionsItem(ItemList.tinBar);
                    ItemHelper.missionsItem(ItemList.brassBar);
                    ItemHelper.missionsItem(ItemList.bronzeBar);
                    ItemHelper.notMissionsItem(ItemList.goldBar);
                    ItemHelper.notMissionsItem(ItemList.silverBar);
                    ItemHelper.notMissionsItem(ItemList.ironBar);
                    ItemHelper.notMissionsItem(ItemList.copperBar);
                    ItemHelper.notMissionsItem(ItemList.zincBar);
                    ItemHelper.notMissionsItem(ItemList.leadBar);
                    ItemHelper.notMissionsItem(ItemList.steelBar);
                    ItemHelper.notMissionsItem(ItemList.adamantineBar);
                    ItemHelper.notMissionsItem(ItemList.glimmerSteelBar);
                    ItemHelper.notMissionsItem(ItemList.seryllBar);
                    ItemHelper.notMissionsItem(ItemList.tinBar);
                    ItemHelper.notMissionsItem(ItemList.brassBar);
                    ItemHelper.notMissionsItem(ItemList.bronzeBar);
                    RequiemLogging.debug("Metal Lumps added");
                } catch (Throwable e) {
                    RequiemLogging.logException("Error in adding Metal Lumps", e);
                }
            }
            // ============= Metal Ores ==============
            if (Constants.metalOres) {
                try {
                    ItemHelper.missionsItem(ItemList.ironOre);
                    ItemHelper.missionsItem(ItemList.copperOre);
                    ItemHelper.missionsItem(ItemList.goldOre);
                    ItemHelper.missionsItem(ItemList.silverOre);
                    ItemHelper.missionsItem(ItemList.adamantineOre);
                    ItemHelper.missionsItem(ItemList.glimmerSteelOre);
                    ItemHelper.missionsItem(ItemList.leadOre);
                    ItemHelper.missionsItem(ItemList.tinOre);
                    ItemHelper.missionsItem(ItemList.zincOre);
                    ItemHelper.notMissionsItem(ItemList.ironOre);
                    ItemHelper.notMissionsItem(ItemList.copperOre);
                    ItemHelper.notMissionsItem(ItemList.goldOre);
                    ItemHelper.notMissionsItem(ItemList.silverOre);
                    ItemHelper.notMissionsItem(ItemList.adamantineOre);
                    ItemHelper.notMissionsItem(ItemList.glimmerSteelOre);
                    ItemHelper.notMissionsItem(ItemList.leadOre);
                    ItemHelper.notMissionsItem(ItemList.tinOre);
                    ItemHelper.notMissionsItem(ItemList.zincOre);
                    RequiemLogging.debug("Metal Ores added");
                } catch (Throwable e) {
                    RequiemLogging.logException("Error in adding Metal Ores", e);
                }
            }
            // ============== Gems ================
            if (Constants.gems) {
                try {
                    ItemHelper.missionsItem(ItemList.emerald);
                    ItemHelper.missionsItem(ItemList.ruby);
                    ItemHelper.missionsItem(ItemList.diamond);
                    ItemHelper.missionsItem(ItemList.sapphire);
                    ItemHelper.missionsItem(ItemList.opal);
                    ItemHelper.missionsItem(ItemList.sapphireStar);
                    ItemHelper.missionsItem(ItemList.diamondStar);
                    ItemHelper.missionsItem(ItemList.emeraldStar);
                    ItemHelper.missionsItem(ItemList.rubyStar);
                    ItemHelper.missionsItem(ItemList.opalBlack);
                    RequiemLogging.debug("Gems added");
                } catch (Throwable e) {
                    RequiemLogging.logException("Error in adding Gems", e);
                }
            }
            // ======== Potions, Salves, Oils =========
            if (Constants.potionsSalvesOils) {
                try {
                    ItemHelper.missionsItem(ItemList.potionAcidDamage);
                    ItemHelper.missionsItem(ItemList.potionAffinity);
                    ItemHelper.missionsItem(ItemList.potionArmourSmithing);
                    ItemHelper.missionsItem(ItemList.potionBlacksmithing);
                    ItemHelper.missionsItem(ItemList.potionCarpentry);
                    ItemHelper.missionsItem(ItemList.potionFireDamage);
                    ItemHelper.missionsItem(ItemList.potionFletching);
                    ItemHelper.missionsItem(ItemList.potionFrostDamage);
                    ItemHelper.missionsItem(ItemList.potionIllusion);
                    ItemHelper.missionsItem(ItemList.potionImbueShatterprot);
                    ItemHelper.missionsItem(ItemList.potionLeatherworking);
                    ItemHelper.missionsItem(ItemList.potionMasonry);
                    ItemHelper.missionsItem(ItemList.potionMining);
                    ItemHelper.missionsItem(ItemList.potionRopemaking);
                    ItemHelper.missionsItem(ItemList.potionShipbuilding);
                    ItemHelper.missionsItem(ItemList.potionStonecutting);
                    ItemHelper.missionsItem(ItemList.potionTailoring);
                    ItemHelper.missionsItem(ItemList.potionTransmutation);
                    ItemHelper.missionsItem(ItemList.potionWaterwalking);
                    ItemHelper.missionsItem(ItemList.potionWeaponSmithing);
                    ItemHelper.missionsItem(ItemList.potionWoodcutting);
                    RequiemLogging.debug("Potions, Salves, and Oils added");
                } catch (Throwable e) {
                    RequiemLogging.logException("Error in adding Potions, Salves, and Oils", e);
                }
            }
            // ============== Mine Doors =============
            if (Constants.mineDoors) {
                try {
                    ItemHelper.missionsItem(ItemList.mineDoorGold);
                    ItemHelper.missionsItem(ItemList.mineDoorPlanks);
                    ItemHelper.missionsItem(ItemList.mineDoorSilver);
                    ItemHelper.missionsItem(ItemList.mineDoorSteel);
                    ItemHelper.missionsItem(ItemList.mineDoorStone);
                    ItemHelper.notMissionsItem(ItemList.mineDoorGold);
                    ItemHelper.notMissionsItem(ItemList.mineDoorPlanks);
                    ItemHelper.notMissionsItem(ItemList.mineDoorSilver);
                    ItemHelper.notMissionsItem(ItemList.mineDoorSteel);
                    ItemHelper.notMissionsItem(ItemList.mineDoorStone);
                    RequiemLogging.debug("Mine Doors added");
                } catch (Throwable e) {
                    RequiemLogging.logException("Error in adding Mine Doors", e);
                }
            }
            // ============= Hand Mirrors ==============
            if (Constants.mirrors) {
                try {
                    ItemHelper.missionsItem(ItemList.goldenMirror);
                    ItemHelper.missionsItem(ItemList.handMirror);
                    RequiemLogging.debug("Mirrors added");
                } catch (Throwable e) {
                    RequiemLogging.logException("Error in adding Mirrors", e);
                }
            }
            // ================ Masks =================
            if (Constants.masks) {
                try {
                    ItemHelper.missionsItem(ItemList.maskChallenge);
                    ItemHelper.missionsItem(ItemList.maskEnlightended);
                    ItemHelper.missionsItem(ItemList.maskIsles);
                    ItemHelper.missionsItem(ItemList.maskOfTheReturner);
                    ItemHelper.missionsItem(ItemList.maskPale);
                    ItemHelper.missionsItem(ItemList.maskRavager);
                    ItemHelper.missionsItem(ItemList.maskShadow);
                    ItemHelper.missionsItem(ItemList.maskTrollHalloween);
                    ItemHelper.missionsItem(ItemList.midsummerMask);
                    RequiemLogging.debug("Masks added");
                } catch (Throwable e) {
                    RequiemLogging.logException("Error in adding Masks", e);
                }
            }
        }

    }

    public static void addActPortal() {
        if (!Constants.actPortalDone) {
            //logger.log(Level.INFO,"Adding PortalTeleport Action");
            ModActions.registerAction(new PortalTeleportAction());
            Constants.actPortalDone = true;
        }
    }

    public static boolean isNotPortalItem(Item item) {
        int id = item.getTemplateId();
        for (int pid : OnItemTemplateCreated.portalItems) {
            if (id == pid) {
                return false;
            }
        }
        return true;
    }

    public static boolean isHomePortalItem(Item item) {
        int id = item.getTemplateId();
        for (int pid : OnItemTemplateCreated.homePortalItems) {
            if (id == pid) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSettlementToken(Item item) {
        return item.getTemplateId() == ItemList.villageToken;
    }

    // Make searched dens tick down and be able to be searched again
    public static void itemTick(Item item) {
        if (item.getTemplateId() == ItemList.creatureSpawn) {
            if (item.getData2() > 0) {
                item.setData2(item.getData2() - 1);
                if (item.getData2() == 0)
                    RequiemLogging.debug(String.format("A %s (creature den) at location X:%s Y:%s is now able to be searched again.", item.getName(), item.getPosX() / 4, item.getPosY() / 4));
            }
        }
    }

    /**
     * @param source item to be checked to move
     * @param target what item the source cannot be put into
     * @param performer the player
     * @return whether or not that an item will be blocked from moving into the target
     */
    // Block items from being moved to certain items
    public static boolean blockMove(Item source, Item target, Creature performer) {
        if (target.getTemplateId() == CustomVehicles.loggingWagon.getTemplateId()) {
            if (source.getTemplateId() != ItemList.logHuge) {
                performer.getCommunicator().sendNormalServerMessage("Only felled trees can be put into a logging wagon.");
                return true;
            }
        }
        return false;
    }

    // change item size
    public static int resizeItemId;
    public static float resizeItemSize;

    public float getSizeMod(Item item) {
        //resizeItemId = 0;
        //resizeItemSize = 1f;
        int id = item.getTemplate().getTemplateId();
        //RequiemLogging.debug("ItemMod.itemSizeMod called");
        if (id == CustomItems.machinaOfFortuneId) {
            return -2.5f;
        } else if (id == ItemList.sleepPowder) {
            return 3f;
        } else if (id == largeWheelId) {
            return -2.0f;
        } else if (id == resizeItemId) {
            return resizeItemSize;
        }
        return 1f;
    }

    public void preInit() {
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();
            CtClass ctItem = classPool.getCtClass("com.wurmonline.server.items.Item");
            // Change the size of items
            ctItem.getMethod("getSizeMod", "()F")
                    .insertAfter(String.format("$_ = $_ * %s.getSizeMod(this);", ItemMod.class.getName()));
        } catch (CannotCompileException | NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void init() {
        try {
            RequiemLogging.logInfo("========= Initializing ItemMod.init =========");
            final ClassPool classPool = HookManager.getInstance().getClassPool();
            final CtClass ctCommunicator = classPool.get("com.wurmonline.server.creatures.Communicator");


            // Sends effects to items
            ctCommunicator.getMethod("sendItem", "(Lcom/wurmonline/server/items/Item;JZ)V")
                    .insertAfter(String.format("%s.sendItemHook(this, $1);", EffectsTools.class.getName()));

            // Removes effects from items
            ctCommunicator.getMethod("sendRemoveItem", "(Lcom/wurmonline/server/items/Item;)V")
                    .insertAfter(String.format("%s.removeItemHook(this, $1);", ItemRemoval.class.getName()));

            // Make searched dens tick down and be able to be searched again
            CtClass ctItem = classPool.get("com.wurmonline.server.items.Item");
            ctItem.getMethod("poll", "(ZZJ)Z").insertAfter(String.format("%s.itemTick(this);", ItemMod.class.getName()));

            // block certain items from moving to another
            classPool.getCtClass("com.wurmonline.server.items.Item")
                    .getMethod("moveToItem", "(Lcom/wurmonline/server/creatures/Creature;JZ)Z")
                    .instrument(new ExprEditor() {
                        boolean patched = false;

                        @Override
                        public void edit(MethodCall m) throws CannotCompileException {
                            if (!patched && m.getMethodName().equals("getItem")) {
                                m.replace(String.format("$_=$proceed($$); if (%s.blockMove(this, $_, mover)) return false;", ItemMod.class.getName()));
                                RequiemLogging.logInfo(String.format("Hooking Item.moveToItem at %d", m.getLineNumber()));
                                patched = true;
                            }
                        }
                    });

            // control the max number of items that can be put into a container
            HookManager.getInstance().registerHook("com.wurmonline.server.items.Item", "mayCreatureInsertItem", "()Z", new InvocationHandlerFactory() {
                @Override
                public InvocationHandler createInvocationHandler() {
                    return new InvocationHandler() {
                        @Override
                        public Object invoke(Object object, Method method, Object[] args) throws Throwable {
                            Item item = (Item) object;
                            for ( StructureTemplate template : Constants.structureTemplates) {
                                if (item.getTemplateId() == template.templateID) {
                                    return item.getItemCount() < 1000;
                                }
                            }
                            if (item.getTemplateId() == CustomVehicles.loggingWagon.getTemplateId()) {
                                return item.getItemCount() < 200;
                            }
                            return method.invoke(object, args);
                        }
                    };
                }
            });

            // control what happens when cargo is unloaded
            HookManager.getInstance().registerHook("com.wurmonline.server.behaviours.CargoTransportationMethods", "unloadCargo", "(Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;F)Z", new InvocationHandlerFactory() {
                @Override
                public InvocationHandler createInvocationHandler() {
                    return new InvocationHandler() {
                        @Override
                        public Object invoke(Object object, Method method, Object[] args) throws Throwable {
                            Creature performer = (Creature) args[0];
                            Item item = (Item) args[1];
                            long coin = Constants.tradeTentCoinReward;
                            if (performer.getCurrentAction().currentSecond() == 3) {
                                for (FocusZone fz : FocusZone.getZonesAt(performer.currentTile.tilex, performer.currentTile.tiley)) {
                                    if (fz.getName().equals(Constants.tradeTentsNorthZoneName) && item.getData() == 801L) {
                                        Items.destroyItem(item.getWurmId());
                                        performer.addMoney(coin);
                                        final Change newch = Economy.getEconomy().getChangeFor(coin);
                                        performer.getCommunicator().sendNormalServerMessage(String.format("You receive a payment of %s for delivering the trade goods.", newch));
                                    } else if (fz.getName().equals(Constants.tradeTentsSouthZoneName) && item.getData() == 802L) {
                                        Items.destroyItem(item.getWurmId());
                                        performer.addMoney(coin);
                                        final Change newch = Economy.getEconomy().getChangeFor(coin);
                                        performer.getCommunicator().sendNormalServerMessage(String.format("You receive a payment of %s for delivering the trade goods.", newch));
                                    }
                                }
                            }
                            return method.invoke(object, args);
                        }
                    };
                }
            });
        } catch (CannotCompileException | NotFoundException e) {
            e.printStackTrace();
        }
    }
}

