package org.jubaroo.mods.wurm.server.items;

import com.wurmonline.server.items.*;
import com.wurmonline.server.skills.SkillList;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.pottals.PortalMod;
import org.jubaroo.mods.wurm.server.server.Constants;

import static com.wurmonline.server.items.ItemList.*;
import static org.jubaroo.mods.wurm.server.items.CustomItems.*;

public class CustomItemCreationEntries {

    //TODO flags have no creation entries!!!

    private static void canopyBedCreationEntry(int id) {
        final AdvancedCreationEntry entry = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY_FINE, ItemList.bedHeadboard, ItemList.bedFrame, id, false, false, 0f, true, true, 0, 65.0, CreationCategories.FURNITURE);
        entry.addRequirement(new CreationRequirement(1, ItemList.bedFootboard, 1, true));
        entry.addRequirement(new CreationRequirement(2, ItemList.plank, 10, true));
        entry.addRequirement(new CreationRequirement(3, ItemList.sheet, 8, true));
        entry.addRequirement(new CreationRequirement(4, ItemList.fur, 8, true));
        entry.addRequirement(new CreationRequirement(5, ItemList.nailsIronLarge, 2, true));
        entry.addRequirement(new CreationRequirement(6, ItemList.nailsIronSmall, 2, true));
        entry.addRequirement(new CreationRequirement(7, ItemList.log, 4, true));
    }

    private static void fullCanopyBedCreationEntry(int id) {
        final AdvancedCreationEntry entry = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY_FINE, ItemList.bedHeadboard, ItemList.bedFrame, id, false, false, 0f, true, true, 0, 65.0, CreationCategories.FURNITURE);
        entry.addRequirement(new CreationRequirement(1, ItemList.bedFootboard, 1, true));
        entry.addRequirement(new CreationRequirement(2, ItemList.plank, 10, true));
        entry.addRequirement(new CreationRequirement(3, ItemList.sheet, 12, true));
        entry.addRequirement(new CreationRequirement(4, ItemList.fur, 8, true));
        entry.addRequirement(new CreationRequirement(5, ItemList.nailsIronLarge, 2, true));
        entry.addRequirement(new CreationRequirement(6, ItemList.nailsIronSmall, 2, true));
        entry.addRequirement(new CreationRequirement(7, ItemList.log, 4, true));
    }

    private static void largeBedCreationEntry(int id) {
        final AdvancedCreationEntry entry = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY_FINE, ItemList.bedHeadboard, ItemList.bedFrame, id, false, false, 0f, true, true, 0, 65.0, CreationCategories.FURNITURE);
        entry.addRequirement(new CreationRequirement(1, ItemList.bedFootboard, 1, true));
        entry.addRequirement(new CreationRequirement(2, ItemList.plank, 10, true));
        entry.addRequirement(new CreationRequirement(3, ItemList.sheet, 6, true));
        entry.addRequirement(new CreationRequirement(4, ItemList.fur, 8, true));
        entry.addRequirement(new CreationRequirement(5, ItemList.nailsIronLarge, 2, true));
        entry.addRequirement(new CreationRequirement(6, ItemList.nailsIronSmall, 2, true));
    }

    private static void meditationRugCreationEntry(int id) {
        CreationEntryCreator.createSimpleEntry(SkillList.CLOTHTAILORING, ItemList.loom, ItemList.clothString, id, false, true, 0f, false, false, CreationCategories.RUGS);
    }

    private static void carpetCreationEntry(int id) {
        CreationEntryCreator.createSimpleEntry(SkillList.CLOTHTAILORING, ItemList.loom, ItemList.clothString, id, false, true, 0f, false, false, 0, 20.0, CreationCategories.RUGS).setDepleteFromTarget(3000);
    }

    private static void curtainCreationEntry(int id) {
        CreationEntryCreator.createSimpleEntry(SkillList.CLOTHTAILORING, ItemList.loom, ItemList.clothString, id, false, true, 0f, false, false, 0, 20.0, CreationCategories.DECORATION).setDepleteFromTarget(3000);
    }

    private static void tentCreationEntry(ItemTemplate template) {
        final AdvancedCreationEntry entry = CreationEntryCreator.createAdvancedEntry(SkillList.CLOTHTAILORING, ItemList.shaft, ItemList.clothYard, template.getTemplateId(), false, false, 0f, true, true, CreationCategories.TENTS);
        entry.addRequirement(new CreationRequirement(1, ItemList.shaft, 10, true));
        entry.addRequirement(new CreationRequirement(2, ItemList.clothYard, 12, true));
        entry.addRequirement(new CreationRequirement(3, ItemList.ropeThin, 10, true));
        entry.addRequirement(new CreationRequirement(4, ItemList.pegWood, 10, true));
    }

    private static void pavilionCreationEntry(ItemTemplate template) {
        final AdvancedCreationEntry entry = CreationEntryCreator.createAdvancedEntry(SkillList.CLOTHTAILORING, ItemList.shaft, ItemList.clothYard, template.getTemplateId(), false, false, 0f, true, true, CreationCategories.TENTS);
        entry.addRequirement(new CreationRequirement(1, ItemList.shaft, 10, true));
        entry.addRequirement(new CreationRequirement(2, ItemList.clothYard, 6, true));
        entry.addRequirement(new CreationRequirement(3, ItemList.ropeThin, 10, true));
        entry.addRequirement(new CreationRequirement(4, ItemList.pegWood, 10, true));
    }

    private static void overheadSignCreationEntry(int id) {
        final AdvancedCreationEntry entry = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY_FINE, ItemList.nailsIronSmall, ItemList.plank, id, false, false, 0f, true, false, CreationCategories.SIGNS);
        entry.addRequirement(new CreationRequirement(1, ItemList.shaft, 4, true));
        entry.addRequirement(new CreationRequirement(2, ItemList.plank, 8, true));
        entry.addRequirement(new CreationRequirement(3, ItemList.nailsIronSmall, 4, true));
    }

    private static void tapestryCreationEntry(int id) {
        final AdvancedCreationEntry entry = CreationEntryCreator.createAdvancedEntry(SkillList.CLOTHTAILORING, ItemList.clothYardWool, ItemList.tapestryStand, id, false, false, 0f, true, true, CreationCategories.DECORATION);
        entry.addRequirement(new CreationRequirement(1, ItemList.clothYardWool, 7, true));
        entry.addRequirement(new CreationRequirement(2, ItemList.woolYarn, 2, true));
    }

    private static void royalChaiseCreationEntry(int id) {
        final AdvancedCreationEntry blackRoyalLoungeChaise = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY_FINE, ItemList.plank, ItemList.shaft, id, false, false, 0f, true, true, 0, 70.0, CreationCategories.FURNITURE);
        blackRoyalLoungeChaise.addRequirement(new CreationRequirement(1, ItemList.plank, 4, true));
        blackRoyalLoungeChaise.addRequirement(new CreationRequirement(2, ItemList.shaft, 1, true));
        blackRoyalLoungeChaise.addRequirement(new CreationRequirement(3, ItemList.nailsIronSmall, 6, true));
        blackRoyalLoungeChaise.addRequirement(new CreationRequirement(4, ItemList.clothYardWool, 10, true));
        blackRoyalLoungeChaise.addRequirement(new CreationRequirement(5, ItemList.woolYarn, 4, true));
    }

    private static void highChairCreationEntry(int id) {
        final AdvancedCreationEntry blackFineHighChair = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY_FINE, ItemList.nailsIronSmall, ItemList.plank, id, false, false, 0f, true, true, 0, 60.0, CreationCategories.FURNITURE);
        blackFineHighChair.addRequirement(new CreationRequirement(1, ItemList.plank, 2, true));
        blackFineHighChair.addRequirement(new CreationRequirement(2, ItemList.clothYard, 2, true));
        blackFineHighChair.addRequirement(new CreationRequirement(3, ItemList.nailsIronSmall, 1, true));
    }

    private static void pictureCreationEntry(int id) {
        final AdvancedCreationEntry pictureNature = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY_FINE, ItemList.nailsIronSmall, ItemList.plank, id, false, false, 0f, true, false, 0, 35.0, CreationCategories.DECORATION);
        pictureNature.addRequirement(new CreationRequirement(1, ItemList.plank, 7, true));
        pictureNature.addRequirement(new CreationRequirement(2, ItemList.nailsIronSmall, 3, true));
        pictureNature.addRequirement(new CreationRequirement(3, ItemList.reedFibre, 5, true));
    }

    private static void tannedTaxCreationEntry(int headId, int tannedHeadId) {
        final AdvancedCreationEntry tannedBlackBearHead = CreationEntryCreator.createAdvancedEntry(Constants.skillTanning, ItemList.lye, headId, tannedHeadId, true, false, 0f, true, false, CreationCategories.DECORATION);
        tannedBlackBearHead.addRequirement(new CreationRequirement(1, ItemList.lye, 10, true));
    }

    private static void stuffedTaxCreationEntry(int groomedHeadId, int stuffedHeadId) {
        final AdvancedCreationEntry stuffedBlackBearHead = CreationEntryCreator.createAdvancedEntry(Constants.skillStuffing, ItemList.cotton, groomedHeadId, stuffedHeadId, true, true, 0f, true, false, CreationCategories.DECORATION);
        stuffedBlackBearHead.addRequirement(new CreationRequirement(1, ItemList.cotton, 30, true));
        stuffedBlackBearHead.addRequirement(new CreationRequirement(2, ItemList.wool, 20, true));
        stuffedBlackBearHead.addRequirement(new CreationRequirement(3, ItemList.eye, 2, true));
        stuffedBlackBearHead.addRequirement(new CreationRequirement(4, ItemList.clothString, 10, true));
        stuffedBlackBearHead.addRequirement(new CreationRequirement(5, ItemList.tooth, 10, true));
    }

    private static void groomedCreationEntry(int tannedHeadId, int groomedHeadId) {
        CreationEntryCreator.createSimpleEntry(Constants.skillGrooming, ItemList.groomingBrush, tannedHeadId, groomedHeadId, false, true, 0f, false, false, CreationCategories.DECORATION);
    }

    private static void trophyCreationCreationEntry(int stuffedHeadId, int trophyId) {
        final AdvancedCreationEntry blackBearHeadTrophy = CreationEntryCreator.createAdvancedEntry(Constants.skillMounting, taxidermyPlaqueId, stuffedHeadId, trophyId, true, true, 0f, true, false, CreationCategories.DECORATION);
        blackBearHeadTrophy.addRequirement(new CreationRequirement(1, ItemList.nailsIronSmall, 8, true));
        blackBearHeadTrophy.addRequirement(new CreationRequirement(2, ItemList.plank, 5, true));
    }

    public static void registerCustomItemCreationEntries() {
        long start = System.nanoTime();
        //==========================================================================================================================================
        //==========================================================================================================================================
        //==========================================================================================================================================
        //========================================================== SIMPLE CREATION ENTRIES =======================================================
        //==========================================================================================================================================
        //==========================================================================================================================================
        //==========================================================================================================================================
        //==========================================================================================================================================
        //================================================================= PAULDRONS ==============================================================
        //==========================================================================================================================================
        CreationEntryCreator.createSimpleEntry(SkillList.CLOTHTAILORING, ItemList.needleIron, ItemList.clothYard, ItemList.shoulderPads1, false, true, 0f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.CLOTHTAILORING, ItemList.needleIron, ItemList.clothYard, ItemList.shoulderPads2, false, true, 0f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.CLOTHTAILORING, ItemList.needleIron, ItemList.clothYard, ItemList.shoulderPads3, false, true, 0f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.LEATHERWORKING, ItemList.needleIron, ItemList.leather, ItemList.shoulderPads4, false, true, 10f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.LEATHERWORKING, ItemList.needleIron, ItemList.leather, ItemList.shoulderPads5, false, true, 10f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.LEATHERWORKING, ItemList.needleIron, ItemList.leather, ItemList.shoulderPads18, false, true, 10f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.LEATHERWORKING, ItemList.needleIron, ItemList.leather, ItemList.shoulderPads6, false, true, 10f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.GROUP_SMITHING, ItemList.anvilLarge, ItemList.ironBar, ItemList.shoulderPads7, false, true, 10f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.GROUP_SMITHING, ItemList.anvilLarge, ItemList.ironBar, ItemList.shoulderPads19, false, true, 10f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.GROUP_SMITHING, ItemList.anvilLarge, ItemList.ironBar, ItemList.shoulderPads8, false, true, 10f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.GROUP_SMITHING, ItemList.anvilLarge, ItemList.ironBar, ItemList.shoulderPads20, false, true, 10f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.GROUP_SMITHING, ItemList.anvilLarge, ItemList.goldBar, ItemList.shoulderPads9, false, true, 10f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.GROUP_SMITHING, ItemList.anvilLarge, ItemList.goldBar, ItemList.shoulderPads21, false, true, 10f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.GROUP_SMITHING, ItemList.anvilLarge, ItemList.ironBar, ItemList.shoulderPads10, false, true, 10f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.GROUP_SMITHING, ItemList.anvilLarge, ItemList.ironBar, ItemList.shoulderPads22, false, true, 10f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_ARMOUR_CHAIN, ItemList.anvilLarge, ItemList.armourChains, ItemList.shoulderPads11, false, true, 10f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.LEATHERWORKING, ItemList.needleIron, ItemList.fur, ItemList.shoulderPads12, false, true, 10f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.LEATHERWORKING, ItemList.needleIron, ItemList.fur, ItemList.shoulderPads13, false, true, 10f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.LEATHERWORKING, ItemList.nailsIronSmall, ItemList.plank, ItemList.shoulderPads14, false, true, 10f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.LEATHERWORKING, ItemList.needleIron, ItemList.fur, ItemList.shoulderPads15, false, true, 10f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.LEATHERWORKING, ItemList.needleIron, ItemList.fur, ItemList.shoulderPads16, false, true, 10f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.GROUP_SMITHING, ItemList.anvilLarge, ItemList.steelBar, ItemList.shoulderPads17, false, true, 10f, false, false, CreationCategories.ARMOUR);
        //==========================================================================================================================================
        //=============================================================== MEDITATION RUGS ==========================================================
        //==========================================================================================================================================
        meditationRugCreationEntry(waterfallExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(orientalExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(darkSquareExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(darkTriangleExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(hornetExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(cthulhuExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(floralExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(roseExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(chineseDragonExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(blueFloralExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(navyGoldExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(creamRoseOvalExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(pinkFloralExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(redWhiteExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(lightBlueWhiteExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(blueWhiteFloralExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(redWhiteTwoFloralExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(redGoldOneExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(redGoldTwoExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(redGoldThreeExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(redGoldFourExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(redGoldFiveExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(navyCreamOneExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(navyCreamTwoExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(navyRedExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(greenGoldOneExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(greenGoldTwoExquisiteMeditationRug.getTemplateId());
        meditationRugCreationEntry(creamFloralExquisiteMeditationRug.getTemplateId());
        //==========================================================================================================================================
        //================================================================== MISC STUFF ============================================================
        //==========================================================================================================================================
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY_FINE, ItemList.nailsIronSmall, ItemList.plank, taxidermyPlaqueId, true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_BLACKSMITHING, ItemList.anvilLarge, ItemList.ironBar, metalShaftId, false, true, 10f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_BLACKSMITHING, ItemList.anvilLarge, ItemList.copperBar, metalShaftId, false, true, 10f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_BLACKSMITHING, ItemList.anvilLarge, ItemList.brassBar, metalShaftId, false, true, 10f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_BLACKSMITHING, ItemList.anvilLarge, ItemList.steelBar, metalShaftId, false, true, 10f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_BLACKSMITHING, ItemList.anvilLarge, ItemList.bronzeBar, metalShaftId, false, true, 10f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.water, ItemList.flour, glueId, true, true, 0f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        //==========================================================================================================================================
        //==================================================================== CARPETS =============================================================
        //==========================================================================================================================================
        carpetCreationEntry(smallCarpet01.getTemplateId());
        carpetCreationEntry(smallCarpet02.getTemplateId());
        carpetCreationEntry(smallCarpet03.getTemplateId());
        carpetCreationEntry(smallCarpet04.getTemplateId());
        carpetCreationEntry(smallCarpet05.getTemplateId());
        carpetCreationEntry(smallCarpet06.getTemplateId());
        carpetCreationEntry(smallCarpet07.getTemplateId());
        carpetCreationEntry(smallCarpet08.getTemplateId());
        carpetCreationEntry(smallCarpet09.getTemplateId());
        carpetCreationEntry(smallCarpet10.getTemplateId());
        carpetCreationEntry(smallOvalCarpet01.getTemplateId());
        carpetCreationEntry(smallOvalCarpet02.getTemplateId());
        carpetCreationEntry(smallCarpetRunner01.getTemplateId());
        carpetCreationEntry(smallCarpetRunner02.getTemplateId());
        carpetCreationEntry(smallCarpetRunner03.getTemplateId());
        carpetCreationEntry(smallCarpetRunner04.getTemplateId());
        carpetCreationEntry(smallCarpetRunner05.getTemplateId());
        carpetCreationEntry(woodlandCamouflageSmallCarpet.getTemplateId());
        //==========================================================================================================================================
        //==================================================================== CURTAINS ============================================================
        //==========================================================================================================================================
        curtainCreationEntry(basicCurtain.getTemplateId());
        curtainCreationEntry(flowersRedCurtain.getTemplateId());
        curtainCreationEntry(kingCurtain.getTemplateId());
        curtainCreationEntry(melodyCurtain.getTemplateId());
        curtainCreationEntry(nightDreamsCurtain.getTemplateId());
        curtainCreationEntry(passionCurtain.getTemplateId());
        curtainCreationEntry(petalsCurtain.getTemplateId());
        curtainCreationEntry(zebraCurtain.getTemplateId());
        curtainCreationEntry(championCurtain.getTemplateId());
        curtainCreationEntry(velvetGreenCurtain.getTemplateId());
        curtainCreationEntry(fancyRedCurtain.getTemplateId());
        curtainCreationEntry(creamCurtain.getTemplateId());
        curtainCreationEntry(purpleNatureCurtain.getTemplateId());
        curtainCreationEntry(redCurtain.getTemplateId());
        curtainCreationEntry(greenCurtain.getTemplateId());
        curtainCreationEntry(tasseledBlueCurtain.getTemplateId());
        curtainCreationEntry(tasseledRedCurtain.getTemplateId());
        curtainCreationEntry(fancyGreenCurtain.getTemplateId());
        curtainCreationEntry(blackPatternCurtain.getTemplateId());
        curtainCreationEntry(camoCurtain.getTemplateId());
        curtainCreationEntry(woodlandCamoCurtain.getTemplateId());
        //==========================================================================================================================================
        //============================================================ TAXIDERMY - GROOMING ========================================================
        //==========================================================================================================================================
        groomedCreationEntry(blackBearTaxidermyTannedHead.getTemplateId(), blackBearTaxidermyGroomedHead.getTemplateId());
        groomedCreationEntry(brownBearTaxidermyTannedHead.getTemplateId(), brownBearTaxidermyGroomedHead.getTemplateId());
        groomedCreationEntry(bisonTaxidermyTannedHead.getTemplateId(), bisonTaxidermyGroomedHead.getTemplateId());
        groomedCreationEntry(stagTaxidermyTannedHead.getTemplateId(), stagTaxidermyGroomedHead.getTemplateId());
        groomedCreationEntry(wildcatTaxidermyTannedHead.getTemplateId(), wildcatTaxidermyGroomedHead.getTemplateId());
        groomedCreationEntry(hellHoundTaxidermyTannedHead.getTemplateId(), hellHoundTaxidermyGroomedHead.getTemplateId());
        groomedCreationEntry(lionTaxidermyTannedHead.getTemplateId(), lionTaxidermyGroomedHead.getTemplateId());
        groomedCreationEntry(hyenaTaxidermyTannedHead.getTemplateId(), hyenaTaxidermyGroomedHead.getTemplateId());
        groomedCreationEntry(boarTaxidermyTannedHead.getTemplateId(), boarTaxidermyGroomedHead.getTemplateId());
        groomedCreationEntry(wolfTaxidermyTannedHead.getTemplateId(), wolfTaxidermyGroomedHead.getTemplateId());
        groomedCreationEntry(worgTaxidermyTannedHead.getTemplateId(), worgTaxidermyGroomedHead.getTemplateId());
        groomedCreationEntry(blackBearTaxidermyTannedBody.getTemplateId(), blackBearTaxidermyGroomedBody.getTemplateId());
        //==========================================================================================================================================
        //================================================================ BOWL ALTARS =============================================================
        //==========================================================================================================================================
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_GOLDSMITHING, ItemList.anvilLarge, ItemList.goldBar, bowlAltarId, false, true, 10f, false, false, CreationCategories.ALTAR);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_GOLDSMITHING, ItemList.anvilLarge, ItemList.silverBar, bowlAltarId, false, true, 10f, false, false, CreationCategories.ALTAR);
        //==========================================================================================================================================
        //==========================================================================================================================================
        //==========================================================================================================================================
        //========================================================== ADVANCED CREATION ENTRIES =====================================================
        //==========================================================================================================================================
        //==========================================================================================================================================
        //==========================================================================================================================================
        //==========================================================================================================================================
        //================================================================ ALTARS ===================================================================
        //==========================================================================================================================================
        final AdvancedCreationEntry libilasAltar = CreationEntryCreator.createAdvancedEntry(SkillList.LEATHERWORKING, ItemList.metalWires, ItemList.metalWires, libilaAltarId, false, false, 0f, true, true, CreationCategories.ALTAR);
        libilasAltar.addRequirement(new CreationRequirement(1, ItemList.meat, 449, true));
        libilasAltar.addRequirement(new CreationRequirement(2, ItemList.heart, 10, true));
        libilasAltar.addRequirement(new CreationRequirement(3, ItemList.metalWires, 20, true));
        libilasAltar.addRequirement(new CreationRequirement(4, ItemList.scythe, 1, true));
        libilasAltar.addRequirement(new CreationRequirement(5, ItemList.corpse, 10, true));
        libilasAltar.addRequirement(new CreationRequirement(6, ItemList.glimmerSteelBar, 15, true));
        libilasAltar.addRequirement(new CreationRequirement(7, ItemList.spearSteel, 4, true));
        libilasAltar.addRequirement(new CreationRequirement(8, ItemList.opalBlack, 1, true));
        libilasAltar.setIsEpicBuildMissionTarget(false);
        final AdvancedCreationEntry altarOfThreeJubaroo = CreationEntryCreator.createAdvancedEntry(SkillList.MASONRY, ItemList.marbleBrick, ItemList.mortar, blessedAltarId, false, false, 0f, true, true, CreationCategories.ALTAR);
        altarOfThreeJubaroo.addRequirement(new CreationRequirement(1, ItemList.mortar, 449, true));
        altarOfThreeJubaroo.addRequirement(new CreationRequirement(2, ItemList.marbleBrick, 449, true));
        altarOfThreeJubaroo.addRequirement(new CreationRequirement(3, ItemList.marbleSlab, 50, true));
        altarOfThreeJubaroo.addRequirement(new CreationRequirement(4, ItemList.sourceCrystal, 10, true));
        altarOfThreeJubaroo.addRequirement(new CreationRequirement(5, ItemList.diamondStar, 1, true));
        altarOfThreeJubaroo.addRequirement(new CreationRequirement(6, ItemList.silverBar, 5, true));
        altarOfThreeJubaroo.addRequirement(new CreationRequirement(7, ItemList.goldBar, 5, true));
        altarOfThreeJubaroo.setIsEpicBuildMissionTarget(false);
        final AdvancedCreationEntry sourceAltar = CreationEntryCreator.createAdvancedEntry(SkillList.CHANNELING, ItemList.sourceCrystal, ItemList.dirtPile, sourceAltarId, false, false, 0f, true, true, CreationCategories.ALTAR);
        sourceAltar.addRequirement(new CreationRequirement(1, ItemList.sourceCrystal, 29, true));
        sourceAltar.addRequirement(new CreationRequirement(2, ItemList.dirtPile, 24, true));
        sourceAltar.addRequirement(new CreationRequirement(3, ItemList.seryllBar, 20, true));
        sourceAltar.addRequirement(new CreationRequirement(4, ItemList.blood, 1, true));
        sourceAltar.addRequirement(new CreationRequirement(5, ItemList.concrete, 80, true));
        sourceAltar.addRequirement(new CreationRequirement(6, ItemList.heart, 10, true));
        sourceAltar.setIsEpicBuildMissionTarget(false);
        final CreationEntry copperBowlAltar = CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_GOLDSMITHING, ItemList.copperBar, ItemList.anvilLarge, copperBowlAltarId, false, true, 100f, false, false, CreationCategories.ALTAR);
        copperBowlAltar.setDepleteFromTarget(3000);
        //==========================================================================================================================================
        //================================================================ TENTS ===================================================================
        //==========================================================================================================================================
        tentCreationEntry(empireMolRehanMilitaryTent);
        tentCreationEntry(crusadersMilitaryTent);
        tentCreationEntry(pandemoniumMilitaryTent);
        tentCreationEntry(dreadnoughtMilitaryTent);
        pavilionCreationEntry(hotsPavilion);
        pavilionCreationEntry(jennKellonPavilion);
        pavilionCreationEntry(molRehanPavilion);
        //==========================================================================================================================================
        //================================================================ MASKS ===================================================================
        //==========================================================================================================================================
        final AdvancedCreationEntry skullMask = CreationEntryCreator.createAdvancedEntry(SkillList.LEATHERWORKING, ItemList.beltLeather, ItemList.skull, skullMaskId, false, false, 0f, true, true, CreationCategories.CLOTHES);
        skullMask.addRequirement(new CreationRequirement(1, ItemList.leather, 2, true));
        skullMask.addRequirement(new CreationRequirement(2, ItemList.metalRivet, 1, true));
        //==========================================================================================================================================
        //================================================================ SIGNS ===================================================================
        //==========================================================================================================================================
        //overheadSignCreation(RequiemItemIds.REQUIEM_SIGN);
        overheadSignCreationEntry(enterOverheadSign.getTemplateId());
        overheadSignCreationEntry(exitOverheadSign.getTemplateId());
        overheadSignCreationEntry(dangerOverheadSign.getTemplateId());
        overheadSignCreationEntry(startOverheadSign.getTemplateId());
        overheadSignCreationEntry(finishOverheadSign.getTemplateId());
        overheadSignCreationEntry(mineEntranceOverheadSign.getTemplateId());
        //==========================================================================================================================================
        //============================================================ TAPESTRIES ==================================================================
        //==========================================================================================================================================
        tapestryCreationEntry(blueNecromancerTapestry.getTemplateId());
        tapestryCreationEntry(blueWizardTapestry.getTemplateId());
        tapestryCreationEntry(cryptWizardTapestry.getTemplateId());
        tapestryCreationEntry(darkWizardTapestry.getTemplateId());
        tapestryCreationEntry(dragonsTreeTapestry.getTemplateId());
        tapestryCreationEntry(fireDemonTapestry.getTemplateId());
        tapestryCreationEntry(landscapeTownTapestry.getTemplateId());
        tapestryCreationEntry(landscapeWaterfallTapestry.getTemplateId());
        tapestryCreationEntry(lichTapestry.getTemplateId());
        tapestryCreationEntry(nordicWarriorTapestry.getTemplateId());
        tapestryCreationEntry(phoenixTapestry.getTemplateId());
        tapestryCreationEntry(seaSerpentTapestry.getTemplateId());
        tapestryCreationEntry(sealTapestry.getTemplateId());
        tapestryCreationEntry(trollKingTapestry.getTemplateId());
        tapestryCreationEntry(wizardVDragonTapestry.getTemplateId());
        tapestryCreationEntry(fourGodsTapestry.getTemplateId());
        tapestryCreationEntry(foTapestry.getTemplateId());
        tapestryCreationEntry(magranonTapestry.getTemplateId());
        tapestryCreationEntry(libilaTapestry.getTemplateId());
        tapestryCreationEntry(vynoraTapestry.getTemplateId());
        tapestryCreationEntry(psychedelicTapestry.getTemplateId());
        tapestryCreationEntry(mushroomHouseTapestry.getTemplateId());
        tapestryCreationEntry(battleOneTapestry.getTemplateId());
        tapestryCreationEntry(threeCrownsBattleTapestry.getTemplateId());
        tapestryCreationEntry(wurmOneTapestry.getTemplateId());
        tapestryCreationEntry(wurmTwoTapestry.getTemplateId());
        tapestryCreationEntry(wurmThreeTapestry.getTemplateId());
        tapestryCreationEntry(battleHastingsTapestry.getTemplateId());
        tapestryCreationEntry(bayeuxTapestry.getTemplateId());
        tapestryCreationEntry(bloodWitchTapestry.getTemplateId());
        tapestryCreationEntry(calmForestTapestry.getTemplateId());
        tapestryCreationEntry(crowWitchTapestry.getTemplateId());
        tapestryCreationEntry(deathAngelTapestry.getTemplateId());
        tapestryCreationEntry(dragonElfTapestry.getTemplateId());
        tapestryCreationEntry(dragonFightTapestry.getTemplateId());
        tapestryCreationEntry(joyLifeTapestry.getTemplateId());
        tapestryCreationEntry(lifesRichesTapestry.getTemplateId());
        tapestryCreationEntry(nightSkyTapestry.getTemplateId());
        tapestryCreationEntry(paladinTapestry.getTemplateId());
        tapestryCreationEntry(reaperTapestry.getTemplateId());
        tapestryCreationEntry(sweetDreamsTapestry.getTemplateId());
        tapestryCreationEntry(graveWitchTapestry.getTemplateId());
        tapestryCreationEntry(ladyUnicornTapestry.getTemplateId());
        tapestryCreationEntry(knightChampionTapestry.getTemplateId());
        tapestryCreationEntry(battleTwoTapestry.getTemplateId());
        tapestryCreationEntry(whiteKnightTapestry.getTemplateId());
        tapestryCreationEntry(requiemMapTapestry.getTemplateId());
        //==========================================================================================================================================
        //=============================================================== CHAIRS ===================================================================
        //==========================================================================================================================================
        royalChaiseCreationEntry(blackRoyalLoungeChaise.getTemplateId());
        royalChaiseCreationEntry(blueRoyalLoungeChaise.getTemplateId());
        royalChaiseCreationEntry(greenRoyalLoungeChaise.getTemplateId());
        royalChaiseCreationEntry(greyRoyalLoungeChaise.getTemplateId());
        royalChaiseCreationEntry(purpleRoyalLoungeChaise.getTemplateId());
        royalChaiseCreationEntry(redRoyalLoungeChaise.getTemplateId());
        royalChaiseCreationEntry(yellowRoyalLoungeChaise.getTemplateId());
        highChairCreationEntry(blackFineHighChair.getTemplateId());
        highChairCreationEntry(blueFineHighChair.getTemplateId());
        highChairCreationEntry(greenFineHighChair.getTemplateId());
        highChairCreationEntry(greyFineHighChair.getTemplateId());
        highChairCreationEntry(yellowFineHighChair.getTemplateId());
        //==========================================================================================================================================
        //============================================================= STRUCTURES =================================================================
        //==========================================================================================================================================
        final AdvancedCreationEntry darkPylon = CreationEntryCreator.createAdvancedEntry(SkillList.MASONRY, ItemList.stoneSlab, ItemList.clay, darkPylonId, false, false, 0f, true, true, CreationCategories.DECORATION);
        darkPylon.addRequirement(new CreationRequirement(1, ItemList.slateShard, 9, true));
        darkPylon.addRequirement(new CreationRequirement(2, ItemList.clay, 499, true));
        darkPylon.addRequirement(new CreationRequirement(3, ItemList.slateBrick, 500, true));
        darkPylon.addRequirement(new CreationRequirement(4, ItemList.goldBar, 50, true));
        //==========================================================================================================================================
        //================================================================= BEDS ===================================================================
        //==========================================================================================================================================
        final AdvancedCreationEntry coffinBed = CreationEntryCreator.createAdvancedEntry(SkillList.MASONRY, ItemList.fur, ItemList.stoneCoffin, coffinBedId, false, false, 0f, true, true, CreationCategories.FURNITURE);
        coffinBed.addRequirement(new CreationRequirement(1, ItemList.sheet, 3, true));
        coffinBed.addRequirement(new CreationRequirement(2, ItemList.fur, 2, true));
        coffinBed.addRequirement(new CreationRequirement(3, glueId, 4, true));
        final AdvancedCreationEntry strawBed = CreationEntryCreator.createAdvancedEntry(SkillList.THATCHING, ItemList.clothString, ItemList.thatch, thatchBedId, false, false, 0f, true, true, CreationCategories.FURNITURE);
        strawBed.addRequirement(new CreationRequirement(1, ItemList.thatch, 19, true));
        strawBed.addRequirement(new CreationRequirement(2, ItemList.clothString, 7, true));
        canopyBedCreationEntry(flowersRedCanopyBed.getTemplateId());
        canopyBedCreationEntry(kingCanopyBed.getTemplateId());
        canopyBedCreationEntry(melodyCanopyBed.getTemplateId());
        canopyBedCreationEntry(nightDreamsCanopyBed.getTemplateId());
        canopyBedCreationEntry(passionCanopyBed.getTemplateId());
        canopyBedCreationEntry(petalsCanopyBed.getTemplateId());
        canopyBedCreationEntry(zebraCanopyBed.getTemplateId());
        fullCanopyBedCreationEntry(basicFullCanopyBed.getTemplateId());
        fullCanopyBedCreationEntry(flowersRedFullCanopyBed.getTemplateId());
        fullCanopyBedCreationEntry(kingFullCanopyBed.getTemplateId());
        fullCanopyBedCreationEntry(melodyFullCanopyBed.getTemplateId());
        fullCanopyBedCreationEntry(nightDreamsFullCanopyBed.getTemplateId());
        fullCanopyBedCreationEntry(passionFullCanopyBed.getTemplateId());
        fullCanopyBedCreationEntry(petalsFullCanopyBed.getTemplateId());
        fullCanopyBedCreationEntry(zebraFullCanopyBed.getTemplateId());
        largeBedCreationEntry(basicLargeBed.getTemplateId());
        largeBedCreationEntry(flowersRedLargeBed.getTemplateId());
        largeBedCreationEntry(kingLargeBed.getTemplateId());
        largeBedCreationEntry(melodyLargeBed.getTemplateId());
        largeBedCreationEntry(nightDreamsLargeBed.getTemplateId());
        largeBedCreationEntry(passionLargeBed.getTemplateId());
        largeBedCreationEntry(petalsLargeBed.getTemplateId());
        largeBedCreationEntry(zebraLargeBed.getTemplateId());
        //==========================================================================================================================================
        //=============================================================== TABLES ===================================================================
        //==========================================================================================================================================
        final AdvancedCreationEntry dinnerTableFine = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY_FINE, ItemList.shaft, ItemList.plank, fineDiningTable.getTemplateId(), false, false, 0f, true, true, CreationCategories.FURNITURE);
        dinnerTableFine.addRequirement(new CreationRequirement(1, ItemList.plank, 10, true));
        dinnerTableFine.addRequirement(new CreationRequirement(2, ItemList.shaft, 3, true));
        dinnerTableFine.addRequirement(new CreationRequirement(3, ItemList.nailsIronSmall, 8, true));
        final AdvancedCreationEntry recMarbleTableLong = CreationEntryCreator.createAdvancedEntry(SkillList.STONECUTTING, ItemList.marbleSlab, ItemList.clay, longRectangularMarbleTable.getTemplateId(), false, false, 0f, true, true, 0, 50.0, CreationCategories.FURNITURE);
        recMarbleTableLong.addRequirement(new CreationRequirement(1, ItemList.marbleSlab, 2, true));
        recMarbleTableLong.addRequirement(new CreationRequirement(2, ItemList.clay, 5, true));
        //==========================================================================================================================================
        //============================================================== PICTURES ==================================================================
        //==========================================================================================================================================
        pictureCreationEntry(naturePicture.getTemplateId());
        pictureCreationEntry(claudePicture.getTemplateId());
        pictureCreationEntry(deerPicture.getTemplateId());
        pictureCreationEntry(peasantsPicture.getTemplateId());
        pictureCreationEntry(shepardPicture.getTemplateId());
        pictureCreationEntry(snowdriftPicture.getTemplateId());
        pictureCreationEntry(streamPicture.getTemplateId());
        pictureCreationEntry(timePicture.getTemplateId());
        pictureCreationEntry(valleyPicture.getTemplateId());
        //==========================================================================================================================================
        //============================================================== FURNITURE =================================================================
        //==========================================================================================================================================
        final AdvancedCreationEntry dresser = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY_FINE, ItemList.nailsIronLarge, ItemList.plank, dresserId, false, false, 0f, true, true, 0, 55.0, CreationCategories.FURNITURE);
        dresser.addRequirement(new CreationRequirement(1, ItemList.plank, 11, true));
        dresser.addRequirement(new CreationRequirement(2, ItemList.shaft, 4, true));
        dresser.addRequirement(new CreationRequirement(3, ItemList.nailsIronLarge, 3, true));
        dresser.addRequirement(new CreationRequirement(4, ItemList.nailsIronSmall, 2, true));
        final AdvancedCreationEntry lowerCabinet = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY_FINE, ItemList.nailsIronLarge, ItemList.plank, lowerCabinetId, false, false, 0f, true, true, 0, 55.0, CreationCategories.FURNITURE);
        lowerCabinet.addRequirement(new CreationRequirement(1, ItemList.plank, 12, true));
        lowerCabinet.addRequirement(new CreationRequirement(2, ItemList.shaft, 4, true));
        lowerCabinet.addRequirement(new CreationRequirement(3, ItemList.nailsIronSmall, 6, true));
        final AdvancedCreationEntry upperCabinet = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY_FINE, ItemList.nailsIronLarge, ItemList.plank, upperCabinetId, false, false, 0f, true, true, 0, 55.0, CreationCategories.FURNITURE);
        upperCabinet.addRequirement(new CreationRequirement(1, ItemList.plank, 6, true));
        upperCabinet.addRequirement(new CreationRequirement(2, ItemList.shaft, 2, true));
        upperCabinet.addRequirement(new CreationRequirement(3, ItemList.nailsIronSmall, 3, true));
        //==========================================================================================================================================
        //============================================================= TAXIDERMY ==================================================================
        //==========================================================================================================================================
        //============================================================= TANNING ====================================================================
        tannedTaxCreationEntry(blackBearTaxidermyHead.getTemplateId(), blackBearTaxidermyTannedHead.getTemplateId());
        tannedTaxCreationEntry(brownBearTaxidermyHead.getTemplateId(), brownBearTaxidermyTannedHead.getTemplateId());
        tannedTaxCreationEntry(bisonTaxidermyHead.getTemplateId(), bisonTaxidermyTannedHead.getTemplateId());
        tannedTaxCreationEntry(stagTaxidermyHead.getTemplateId(), stagTaxidermyTannedHead.getTemplateId());
        tannedTaxCreationEntry(wildcatTaxidermyHead.getTemplateId(), wildcatTaxidermyTannedHead.getTemplateId());
        tannedTaxCreationEntry(lionTaxidermyHead.getTemplateId(), lionTaxidermyTannedHead.getTemplateId());
        tannedTaxCreationEntry(hyenaTaxidermyHead.getTemplateId(), hyenaTaxidermyTannedHead.getTemplateId());
        tannedTaxCreationEntry(boarTaxidermyHead.getTemplateId(), boarTaxidermyTannedHead.getTemplateId());
        tannedTaxCreationEntry(wolfTaxidermyHead.getTemplateId(), wolfTaxidermyTannedHead.getTemplateId());
        tannedTaxCreationEntry(worgTaxidermyHead.getTemplateId(), worgTaxidermyTannedHead.getTemplateId());
        final AdvancedCreationEntry tannedBlackBearBody = CreationEntryCreator.createAdvancedEntry(Constants.skillTanning, ItemList.lye, blackBearTaxidermyBody.getTemplateId(), blackBearTaxidermyTannedBody.getTemplateId(), true, false, 0f, true, false, CreationCategories.DECORATION);
        tannedBlackBearBody.addRequirement(new CreationRequirement(1, ItemList.lye, 30, true));
        //============================================================= STUFFING ==================================================================
        stuffedTaxCreationEntry(blackBearTaxidermyGroomedHead.getTemplateId(), blackBearTaxidermyStuffedHead.getTemplateId());
        stuffedTaxCreationEntry(brownBearTaxidermyGroomedHead.getTemplateId(), brownBearTaxidermyStuffedHead.getTemplateId());
        stuffedTaxCreationEntry(bisonTaxidermyGroomedHead.getTemplateId(), bisonTaxidermyStuffedHead.getTemplateId());
        stuffedTaxCreationEntry(stagTaxidermyGroomedHead.getTemplateId(), stagTaxidermyStuffedHead.getTemplateId());
        stuffedTaxCreationEntry(wildcatTaxidermyGroomedHead.getTemplateId(), wildcatTaxidermyStuffedHead.getTemplateId());
        stuffedTaxCreationEntry(hellHoundTaxidermyGroomedHead.getTemplateId(), hellHoundTaxidermyStuffedHead.getTemplateId());
        stuffedTaxCreationEntry(lionTaxidermyGroomedHead.getTemplateId(), lionTaxidermyStuffedHead.getTemplateId());
        stuffedTaxCreationEntry(hyenaTaxidermyGroomedHead.getTemplateId(), hyenaTaxidermyStuffedHead.getTemplateId());
        stuffedTaxCreationEntry(boarTaxidermyGroomedHead.getTemplateId(), boarTaxidermyStuffedHead.getTemplateId());
        stuffedTaxCreationEntry(wolfTaxidermyGroomedHead.getTemplateId(), wolfTaxidermyStuffedHead.getTemplateId());
        stuffedTaxCreationEntry(worgTaxidermyGroomedHead.getTemplateId(), worgTaxidermyStuffedHead.getTemplateId());
        final AdvancedCreationEntry stuffedBlackBear = CreationEntryCreator.createAdvancedEntry(Constants.skillStuffing, ItemList.cotton, blackBearTaxidermyGroomedBody.getTemplateId(), blackBearTaxidermyStuffedBody.getTemplateId(), true, true, 0f, true, false, CreationCategories.DECORATION);
        stuffedBlackBear.addRequirement(new CreationRequirement(1, ItemList.cotton, 80, true));
        stuffedBlackBear.addRequirement(new CreationRequirement(2, ItemList.wool, 40, true));
        stuffedBlackBear.addRequirement(new CreationRequirement(3, ItemList.eye, 2, true));
        stuffedBlackBear.addRequirement(new CreationRequirement(4, ItemList.clothString, 10, true));
        stuffedBlackBear.addRequirement(new CreationRequirement(5, ItemList.tooth, 15, true));
        stuffedBlackBear.addRequirement(new CreationRequirement(6, glueId, 10, true));
        //============================================================= MOUNTING ==================================================================
        trophyCreationCreationEntry(blackBearTaxidermyStuffedHead.getTemplateId(), blackBearTaxidermyHeadTrophy.getTemplateId());
        trophyCreationCreationEntry(brownBearTaxidermyStuffedHead.getTemplateId(), brownBearTaxidermyHeadTrophy.getTemplateId());
        trophyCreationCreationEntry(bisonTaxidermyStuffedHead.getTemplateId(), bisonTaxidermyHeadTrophy.getTemplateId());
        trophyCreationCreationEntry(stagTaxidermyStuffedHead.getTemplateId(), stagTaxidermyHeadTrophy.getTemplateId());
        trophyCreationCreationEntry(wildcatTaxidermyStuffedHead.getTemplateId(), wildcatTaxidermyHeadTrophy.getTemplateId());
        trophyCreationCreationEntry(hellHoundTaxidermyStuffedHead.getTemplateId(), hellHoundTaxidermyHeadTrophy.getTemplateId());
        trophyCreationCreationEntry(lionTaxidermyStuffedHead.getTemplateId(), lionTaxidermyHeadTrophy.getTemplateId());
        trophyCreationCreationEntry(hyenaTaxidermyGroomedHead.getTemplateId(), hyenaTaxidermyHeadTrophy.getTemplateId());
        trophyCreationCreationEntry(boarTaxidermyStuffedHead.getTemplateId(), boarTaxidermyHeadTrophy.getTemplateId());
        trophyCreationCreationEntry(wolfTaxidermyStuffedHead.getTemplateId(), wolfTaxidermyHeadTrophy.getTemplateId());
        trophyCreationCreationEntry(worgTaxidermyGroomedHead.getTemplateId(), worgTaxidermyHeadTrophy.getTemplateId());
        final AdvancedCreationEntry blackBearTrophy = CreationEntryCreator.createAdvancedEntry(Constants.skillMounting, metalShaftId, blackBearTaxidermyStuffedBody.getTemplateId(), blackBearTaxidermyTrophyBody.getTemplateId(), true, true, 0f, true, false, CreationCategories.DECORATION);
        blackBearTrophy.addRequirement(new CreationRequirement(1, metalShaftId, 2, true));
        blackBearTrophy.addRequirement(new CreationRequirement(2, ItemList.nailsIronLarge, 6, true));
        blackBearTrophy.addRequirement(new CreationRequirement(3, glueId, 12, true));
        //==========================================================================================================================================
        //============================================================= Decorations ================================================================
        //==========================================================================================================================================
        CreationEntryCreator.createSimpleEntry(SkillList.THATCHING, ItemList.thatch, ItemList.thatch, hayPile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.rock, ItemList.sand, mortarPile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.DIGGING, ItemList.dirtPile, ItemList.dirtPile, CustomItems.dirtPile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.sand, ItemList.sand, sandPile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.tar, ItemList.tar, tarPile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.peat, ItemList.peat, peatPile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.log, ItemList.log, logPile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.rock, ItemList.rock, rockPile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.bodyHand, ItemList.rock, rockShard.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.bodyHand, ItemList.rock, CustomItems.slateShard.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.rock, ItemList.rock, slatePile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.bodyHand, ItemList.rock, CustomItems.marbleShard.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.rock, ItemList.rock, marblePile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.bodyHand, ItemList.rock, sandstoneShard.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.rock, ItemList.rock, sandstonePile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.bodyHand, ItemList.rock, CustomItems.ironOre.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.rock, ItemList.rock, ironOrePile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.bodyHand, ItemList.rock, CustomItems.copperOre.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.rock, ItemList.rock, copperOrePile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.bodyHand, ItemList.rock, CustomItems.goldOre.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.rock, ItemList.rock, goldOrePile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.bodyHand, ItemList.rock, CustomItems.silverOre.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.rock, ItemList.rock, silverOrePile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.bodyHand, ItemList.rock, CustomItems.zincOre.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.rock, ItemList.rock, zincOrePile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.bodyHand, ItemList.rock, CustomItems.tinOre.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.rock, ItemList.rock, tinOrePile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.bodyHand, ItemList.rock, CustomItems.leadOre.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.rock, ItemList.rock, leadOrePile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.bodyHand, ItemList.rock, CustomItems.adamantineOre.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.rock, ItemList.rock, adamantineOrePile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.rock, ItemList.rock, CustomItems.adamantineBoulder.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.bodyHand, ItemList.rock, glimmersteelOre.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.rock, ItemList.rock, glimmersteelOrePile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.rock, ItemList.rock, glimmersteelBoulder.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.stoneChisel, ItemList.rock, CustomItems.riftStone1.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.stoneChisel, ItemList.rock, CustomItems.riftStone2.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.stoneChisel, ItemList.rock, CustomItems.riftStone3.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.stoneChisel, ItemList.rock, CustomItems.riftStone4.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(Constants.skillGemCrafting, ItemList.stoneChisel, ItemList.rock, CustomItems.riftCrystal1.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(Constants.skillGemCrafting, ItemList.stoneChisel, ItemList.rock, CustomItems.riftCrystal2.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(Constants.skillGemCrafting, ItemList.stoneChisel, ItemList.rock, CustomItems.riftCrystal3.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(Constants.skillGemCrafting, ItemList.stoneChisel, ItemList.rock, CustomItems.riftCrystal4.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.scrapwood, ItemList.mixedGrass, plant1.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.scrapwood, ItemList.mixedGrass, plant2.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.scrapwood, ItemList.mixedGrass, plant3.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.scrapwood, ItemList.mixedGrass, plant4.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.stoneChisel, ItemList.rock, rock1.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.stoneChisel, ItemList.rock, rock2.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.stoneChisel, ItemList.rock, rock3.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.scrapwood, ItemList.mixedGrass, fern1.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.scrapwood, ItemList.mixedGrass, fern2.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.scrapwood, ItemList.mixedGrass, fern3.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.scrapwood, ItemList.mixedGrass, fernFall1.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.scrapwood, ItemList.mixedGrass, fernFall2.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.scrapwood, ItemList.mixedGrass, fernFall3.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.scrapwood, ItemList.mushroomRed, redMushroomPile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.scrapwood, ItemList.mushroomYellow, yellowMushroomPile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.scrapwood, ItemList.mushroomBrown, brownMushroomPile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.scrapwood, ItemList.mushroomBlue, blueMushroomPile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.scrapwood, ItemList.mushroomGreen, greenMushroomPile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.scrapwood, ItemList.mushroomBlack, blackMushroomPile.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.CLOTHTAILORING, ItemList.cotton, ItemList.cotton, questionMarkBag.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.stoneChisel, ItemList.stoneSlab, stoneOfSoulfall.getTemplateId(), false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.corpse, ItemList.clothYard, bodyBag.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.rock, ItemList.swordLong, swordStone.getTemplateId(), true, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.FIREMAKING, ItemList.log, ItemList.log, CustomItems.campfire.getTemplateId(), true, true, 0f, false, true, CreationCategories.DECORATION);
        final AdvancedCreationEntry coinPile = CreationEntryCreator.createAdvancedEntry(SkillList.MISCELLANEOUS, ItemList.coinCopper, ItemList.coinCopper, CustomItems.coinPile.getTemplateId(), false, false, 0f, true, false, CreationCategories.DECORATION);
        coinPile.addRequirement(new CreationRequirement(1, ItemList.coinCopper, 3, true));
        final AdvancedCreationEntry dyeKit = CreationEntryCreator.createAdvancedEntry(SkillList.LEATHERWORKING, ItemList.needleIron, ItemList.leather, dyeKitId, true, false, 0f, false, false, 0, 65.0, CreationCategories.TOOLS);
        dyeKit.addRequirement(new CreationRequirement(1, ItemList.leather, 1, true));
        dyeKit.addRequirement(new CreationRequirement(2, ItemList.leatherStrip, 3, true));
        if (joustingLanceId > 0 && Constants.AllowCraftingLance) {
            final AdvancedCreationEntry creationEntry = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY, ItemList.knifeCarving, ItemList.log, joustingLanceId, false, true, 50.0f, false, false, CreationCategories.WEAPONS);
            creationEntry.addRequirement(new CreationRequirement(1, ItemList.sheetIron, 1, true));
        }
        final AdvancedCreationEntry shrineOfRush = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY_FINE, ItemList.clay, ItemList.stoneSlab, ItemList.speedShrineHota, false, false, 0f, true, true, 0, 65.0, CreationCategories.MAGIC);
        shrineOfRush.addRequirement(new CreationRequirement(1, ItemList.clay, 1, true));
        shrineOfRush.addRequirement(new CreationRequirement(2, ItemList.brassBar, 100, true));
        shrineOfRush.addRequirement(new CreationRequirement(3, ItemList.plank, 100, true));
        shrineOfRush.addRequirement(new CreationRequirement(4, ItemList.nailsIronSmall, 10, true));
        shrineOfRush.addRequirement(new CreationRequirement(5, ItemList.stoneSlab, 11, true));
        shrineOfRush.addRequirement(new CreationRequirement(6, ItemList.flower5, 10, true));
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.papyrusPress, ItemList.scrapwood, ItemList.plank, false, true, 0f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        // Gems
        final AdvancedCreationEntry starSapphire = CreationEntryCreator.createAdvancedEntry(11005, ItemList.sapphire, ItemList.sapphire, ItemList.sapphireStar, false, false, 0f, true, false, 0, 70.0, CreationCategories.JEWELRY);
        starSapphire.addRequirement(new CreationRequirement(1, ItemList.sapphire, 18, true));
        final AdvancedCreationEntry starRuby = CreationEntryCreator.createAdvancedEntry(11005, ItemList.ruby, ItemList.ruby, ItemList.rubyStar, false, false, 0f, true, false, 0, 70.0, CreationCategories.JEWELRY);
        starRuby.addRequirement(new CreationRequirement(1, ItemList.ruby, 18, true));
        final AdvancedCreationEntry starEmerald = CreationEntryCreator.createAdvancedEntry(11005, ItemList.emerald, ItemList.emerald, ItemList.emeraldStar, false, false, 0f, true, false, 0, 70.0, CreationCategories.JEWELRY);
        starEmerald.addRequirement(new CreationRequirement(1, ItemList.emerald, 18, true));
        final AdvancedCreationEntry starDiamond = CreationEntryCreator.createAdvancedEntry(11005, ItemList.diamond, ItemList.diamond, ItemList.diamondStar, false, false, 0f, true, false, 0, 70.0, CreationCategories.JEWELRY);
        starDiamond.addRequirement(new CreationRequirement(1, ItemList.diamond, 18, true));
        final AdvancedCreationEntry blackOpal = CreationEntryCreator.createAdvancedEntry(11005, ItemList.opal, ItemList.opal, ItemList.opalBlack, false, false, 0f, true, false, 0, 70.0, CreationCategories.JEWELRY);
        blackOpal.addRequirement(new CreationRequirement(1, ItemList.opal, 18, true));
        // Gem Fragments
        final AdvancedCreationEntry sapphireFragment = CreationEntryCreator.createAdvancedEntry(11005, CustomItems.sapphireFragment.getTemplateId(), CustomItems.sapphireFragment.getTemplateId(), ItemList.sapphire, false, false, 0f, true, false, 0, 0.0, CreationCategories.JEWELRY);
        sapphireFragment.addRequirement(new CreationRequirement(1, CustomItems.sapphireFragment.getTemplateId(), 8, true));
        final AdvancedCreationEntry diamondFragment = CreationEntryCreator.createAdvancedEntry(11005, CustomItems.diamondFragment.getTemplateId(), CustomItems.diamondFragment.getTemplateId(), ItemList.diamond, false, false, 0f, true, false, 0, 0.0, CreationCategories.JEWELRY);
        diamondFragment.addRequirement(new CreationRequirement(1, CustomItems.diamondFragment.getTemplateId(), 8, true));
        final AdvancedCreationEntry emeraldFragment = CreationEntryCreator.createAdvancedEntry(11005, CustomItems.emeraldFragment.getTemplateId(), CustomItems.emeraldFragment.getTemplateId(), ItemList.emerald, false, false, 0f, true, false, 0, 0.0, CreationCategories.JEWELRY);
        emeraldFragment.addRequirement(new CreationRequirement(1, CustomItems.emeraldFragment.getTemplateId(), 8, true));
        final AdvancedCreationEntry opalFragment = CreationEntryCreator.createAdvancedEntry(11005, CustomItems.opalFragment.getTemplateId(), CustomItems.opalFragment.getTemplateId(), ItemList.opal, false, false, 0f, true, false, 0, 0.0, CreationCategories.JEWELRY);
        opalFragment.addRequirement(new CreationRequirement(1, CustomItems.opalFragment.getTemplateId(), 8, true));
        final AdvancedCreationEntry rubyFragment = CreationEntryCreator.createAdvancedEntry(11005, CustomItems.rubyFragment.getTemplateId(), CustomItems.rubyFragment.getTemplateId(), ItemList.ruby, false, false, 0f, true, false, 0, 0.0, CreationCategories.JEWELRY);
        rubyFragment.addRequirement(new CreationRequirement(1, CustomItems.rubyFragment.getTemplateId(), 8, true));
        // Lathe Items
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.log, latheId, ItemList.shaft, true, false, 0f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.log, latheId, ItemList.spindle, true, false, 0f, false, false, CreationCategories.TOOLS);
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.log, latheId, ItemList.tenon, true, false, 0f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.log, latheId, ItemList.clubHuge, true, false, 0f, false, false, CreationCategories.WEAPONS);
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.log, latheId, ItemList.tenon, true, false, 0f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.log, latheId, ItemList.bowLong, true, false, 0f, false, false, CreationCategories.WEAPONS);
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.log, latheId, ItemList.bowMedium, true, false, 0f, false, false, CreationCategories.WEAPONS);
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.log, latheId, ItemList.bowShort, true, false, 0f, false, false, CreationCategories.WEAPONS);
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.log, latheId, ItemList.staffWood, true, false, 0f, false, false, CreationCategories.WEAPONS);
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.log, latheId, ItemList.tackleLarge, true, false, 0f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.log, latheId, ItemList.tackleSmall, true, false, 0f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.shaft, latheId, ItemList.hammerHeadWood, true, false, 0f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.shaft, latheId, ItemList.pegWood, true, false, 0f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.shaft, latheId, ItemList.clayShaper, true, false, 0f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.shaft, latheId, ItemList.deedStake, true, false, 0f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.shaft, latheId, ItemList.spatula, true, false, 0f, false, false, CreationCategories.TOOLS);
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.shaft, latheId, ItemList.arrowShaft, true, false, 0f, false, false, CreationCategories.WEAPONS);
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.shaft, latheId, ItemList.belayingPin, true, false, 0f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.shaft, latheId, ItemList.woodenHandleSword, true, false, 0f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        // Rock Crusher
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.rock, rockCrusherId, rockDustId, false, true, 0f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        CreationEntryCreator.createSimpleEntry(SkillList.MASONRY, ItemList.sandstone, rockCrusherId, ItemList.sand, false, true, 0f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        // Dust to Dirt
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.peat, rockDustId, ItemList.dirtPile, true, false, 0f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        //Armor
        // Glimmerscale
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_ARMOUR_PLATE, ItemList.anvilLarge, glimmerscaleId, glimmerscaleBoot.getTemplateId(), false, true, 0f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_ARMOUR_PLATE, ItemList.anvilLarge, glimmerscaleId, glimmerscaleGlove.getTemplateId(), false, true, 0f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_ARMOUR_PLATE, ItemList.anvilLarge, glimmerscaleId, glimmerscaleHelmet.getTemplateId(), false, true, 0f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_ARMOUR_PLATE, ItemList.anvilLarge, glimmerscaleId, glimmerscaleLeggings.getTemplateId(), false, true, 0f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_ARMOUR_PLATE, ItemList.anvilLarge, glimmerscaleId, glimmerscaleSleeve.getTemplateId(), false, true, 0f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_ARMOUR_PLATE, ItemList.anvilLarge, glimmerscaleId, glimmerscaleVest.getTemplateId(), false, true, 0f, false, false, CreationCategories.ARMOUR);
        // Spectral
        CreationEntryCreator.createSimpleEntry(SkillList.LEATHERWORKING, ItemList.needleIron, spectralHideId, spectralBoot.getTemplateId(), false, true, 0f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.LEATHERWORKING, ItemList.needleCopper, spectralHideId, spectralBoot.getTemplateId(), false, true, 0f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.LEATHERWORKING, ItemList.needleIron, spectralHideId, spectralGlove.getTemplateId(), false, true, 0f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.LEATHERWORKING, ItemList.needleCopper, spectralHideId, spectralGlove.getTemplateId(), false, true, 0f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.LEATHERWORKING, ItemList.needleIron, spectralHideId, spectralCap.getTemplateId(), false, true, 0f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.LEATHERWORKING, ItemList.needleCopper, spectralHideId, spectralCap.getTemplateId(), false, true, 0f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.LEATHERWORKING, ItemList.needleIron, spectralHideId, spectralLeggings.getTemplateId(), false, true, 0f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.LEATHERWORKING, ItemList.needleCopper, spectralHideId, spectralLeggings.getTemplateId(), false, true, 0f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.LEATHERWORKING, ItemList.needleIron, spectralHideId, spectralSleeve.getTemplateId(), false, true, 0f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.LEATHERWORKING, ItemList.needleCopper, spectralHideId, spectralSleeve.getTemplateId(), false, true, 0f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.LEATHERWORKING, ItemList.needleIron, spectralHideId, spectralVest.getTemplateId(), false, true, 0f, false, false, CreationCategories.ARMOUR);
        CreationEntryCreator.createSimpleEntry(SkillList.LEATHERWORKING, ItemList.needleCopper, spectralHideId, spectralVest.getTemplateId(), false, true, 0f, false, false, CreationCategories.ARMOUR);
        // Other Armor
        //CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.skull, ItemList.leatherStrip, dragonSkullHelmet.getTemplateId(), true, true, 0f, false, false, CreationCategories.ARMOUR);
        // Weapons
        CreationEntryCreator.createSimpleEntry(SkillList.TOYMAKING, ItemList.clothString, ItemList.shaft, battleYoyoId, false, true, 0f, false, false, CreationCategories.WEAPONS);
        CreationEntryCreator.createSimpleEntry(SkillList.CARPENTRY, ItemList.knifeCarving, ItemList.log, clubId, false, true, 0f, false, false, CreationCategories.WEAPONS);
        final AdvancedCreationEntry combatScythe = CreationEntryCreator.createAdvancedEntry(SkillList.GROUP_SMITHING_WEAPONSMITHING, ItemList.shaft, ItemList.scytheBlade, combatScytheId, false, false, 0f, true, false, CreationCategories.WEAPONS);
        combatScythe.addRequirement(new CreationRequirement(1, ItemList.leatherStrip, 3, true));
        combatScythe.addRequirement(new CreationRequirement(2, ItemList.metalRivet, 1, true));
        combatScythe.addRequirement(new CreationRequirement(3, CustomItems.glueId, 1, true));
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilSmall, ItemList.brassBar, knucklesId, false, true, 0f, false, false, CreationCategories.WEAPONS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilSmall, ItemList.ironBar, knucklesId, false, true, 0f, false, false, CreationCategories.WEAPONS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilSmall, ItemList.steelBar, knucklesId, false, true, 0f, false, false, CreationCategories.WEAPONS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilSmall, ItemList.goldBar, knucklesId, false, true, 0f, false, false, CreationCategories.WEAPONS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilSmall, ItemList.silverBar, knucklesId, false, true, 0f, false, false, CreationCategories.WEAPONS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilSmall, ItemList.zincBar, knucklesId, false, true, 0f, false, false, CreationCategories.WEAPONS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilSmall, ItemList.tinBar, knucklesId, false, true, 0f, false, false, CreationCategories.WEAPONS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilSmall, ItemList.bronzeBar, knucklesId, false, true, 0f, false, false, CreationCategories.WEAPONS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilSmall, ItemList.leadBar, knucklesId, false, true, 0f, false, false, CreationCategories.WEAPONS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilSmall, ItemList.copperBar, knucklesId, false, true, 0f, false, false, CreationCategories.WEAPONS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilSmall, ItemList.adamantineBar, knucklesId, false, true, 0f, false, false, CreationCategories.WEAPONS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilSmall, ItemList.glimmerSteelBar, knucklesId, false, true, 0f, false, false, CreationCategories.WEAPONS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilSmall, ItemList.seryllBar, knucklesId, false, true, 0f, false, false, CreationCategories.WEAPONS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.shaft, warhammerHeadId, warhammerId, true, true, 0f, false, false, CreationCategories.WEAPONS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilLarge, ItemList.ironBar, warhammerHeadId, false, true, 0f, false, false, CreationCategories.WEAPON_HEADS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilLarge, ItemList.steelBar, warhammerHeadId, false, true, 0f, false, false, CreationCategories.WEAPON_HEADS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilLarge, ItemList.silverBar, warhammerHeadId, false, true, 0f, false, false, CreationCategories.WEAPON_HEADS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilLarge, ItemList.copperBar, warhammerHeadId, false, true, 0f, false, false, CreationCategories.WEAPON_HEADS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilLarge, ItemList.tinBar, warhammerHeadId, false, true, 0f, false, false, CreationCategories.WEAPON_HEADS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilLarge, ItemList.goldBar, warhammerHeadId, false, true, 0f, false, false, CreationCategories.WEAPON_HEADS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilLarge, ItemList.zincBar, warhammerHeadId, false, true, 0f, false, false, CreationCategories.WEAPON_HEADS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilLarge, ItemList.bronzeBar, warhammerHeadId, false, true, 0f, false, false, CreationCategories.WEAPON_HEADS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilLarge, ItemList.brassBar, warhammerHeadId, false, true, 0f, false, false, CreationCategories.WEAPON_HEADS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilLarge, ItemList.adamantineBar, warhammerHeadId, false, true, 0f, false, false, CreationCategories.WEAPON_HEADS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilLarge, ItemList.glimmerSteelBar, warhammerHeadId, false, true, 0f, false, false, CreationCategories.WEAPON_HEADS);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_WEAPON_HEADS, ItemList.anvilLarge, ItemList.seryllBar, warhammerHeadId, false, true, 0f, false, false, CreationCategories.WEAPON_HEADS);
        //
        AdvancedCreationEntry huntingArrowPack = CreationEntryCreator.createAdvancedEntry(SkillList.GROUP_FLETCHING, ItemList.quiver, ItemList.arrowHunting, arrowPackHuntingId, false, false, 0f, true, false, 0, 50.0D, CreationCategories.FLETCHING);
        huntingArrowPack.addRequirement(new CreationRequirement(1, ItemList.arrowHunting, 39, true));
        AdvancedCreationEntry warArrowPack = CreationEntryCreator.createAdvancedEntry(SkillList.GROUP_FLETCHING, ItemList.quiver, ItemList.arrowWar, arrowPackWarId, false, false, 0f, true, false, 0, 50.0D, CreationCategories.FLETCHING);
        warArrowPack.addRequirement(new CreationRequirement(1, ItemList.arrowWar, 39, true));
        final AdvancedCreationEntry depthDrill = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY_FINE, ItemList.ironBand, ItemList.shaft, depthDrillId, false, false, 0f, true, false, CreationCategories.TOOLS);
        depthDrill.addRequirement(new CreationRequirement(1, ItemList.woodenHandleSword, 2, true));
        depthDrill.addRequirement(new CreationRequirement(2, ItemList.nailsIronSmall, 1, true));
        CreationEntryCreator.createSimpleEntry(SkillList.BUTCHERING, ItemList.knifeButchering, ItemList.corpse, skeletonDecorationId, false, true, 0f, false, false, CreationCategories.DECORATION);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_METALLURGY, ItemList.adamantineBar, ItemList.glimmerSteelBar, titaniumLumpId, true, true, 0f, true, false, CreationCategories.CONSTRUCTION_MATERIAL);
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_GOLDSMITHING, ItemList.anvilSmall, titaniumLumpId, titaniumSocketId, false, true, 0f, true, false, CreationCategories.JEWELRY);
        final AdvancedCreationEntry largeWheel = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY_FINE, plank, shaft, largeWheelId, false, false, 0f, true, true, CreationCategories.CONSTRUCTION_MATERIAL);
        largeWheel.addRequirement(new CreationRequirement(1, plank, 5, true));
        largeWheel.addRequirement(new CreationRequirement(2, shaft, 4, true));
        largeWheel.addRequirement(new CreationRequirement(3, ironBand, 2, true));
        largeWheel.addRequirement(new CreationRequirement(4, nailsIronSmall, 2, true));
        CreationEntryCreator.createSimpleEntry(SkillList.MILLING, rockCrusherId, ItemList.rock, rockDustId, false, true, 0f, false, false, CreationCategories.CONSTRUCTION_MATERIAL);
        CreationEntryCreator.createSimpleEntry(SkillList.ALCHEMY_NATURAL, ItemList.sourceCrystal, ItemList.log, essenceOfWoodId, true, true, 0f, false, false, CreationCategories.MAGIC);
        AdvancedCreationEntry creatureRenameCertificate = CreationEntryCreator.createAdvancedEntry(SkillList.PAPYRUSMAKING, ItemList.paperSheet, ItemList.clothString, creatureRenameCertificateId, false, false, 0.0f, false, false, CreationCategories.WRITING);
        creatureRenameCertificate.addRequirement(new CreationRequirement(1, ItemList.paperSheet, 1, true));
        creatureRenameCertificate.addRequirement(new CreationRequirement(2, CustomItems.glueId, 1, true));
        //CreationEntryCreator.createSimpleEntry(SkillList.PAPYRUSMAKING, ItemList.scissors, ItemList.paperSheet, labelId, false, false, 25, false, false, CreationCategories.WRITING);
        if (!Constants.disableScrollGearBinding) {
            final AdvancedCreationEntry scrollOfGearBinding = CreationEntryCreator.createAdvancedEntry(SkillList.PAPYRUSMAKING, ItemList.paperSheet, ItemList.leatherStrip, CustomItems.scrollOfGearBinding.getTemplateId(), true, false, 0f, false, false, CreationCategories.WRITING);
            scrollOfGearBinding.addRequirement(new CreationRequirement(1, ItemList.paperSheet, 2, true));
            scrollOfGearBinding.addRequirement(new CreationRequirement(2, ItemList.sourceSalt, 1, true));
        }
        CreationEntryCreator.createSimpleEntry(SkillList.SMITHING_BLACKSMITHING, ItemList.hammerMetal, ItemList.ironBar, guardTokenId, false, true, 0f, false, false, CreationCategories.TOOLS);
        // Potions
        final AdvancedCreationEntry diseasePotion = CreationEntryCreator.createAdvancedEntry(SkillList.ALCHEMY_NATURAL, ItemList.sassafras, ItemList.tallow, diseasePotionId, true, true, 0f, true, false, CreationCategories.ALCHEMY);
        diseasePotion.addRequirement(new CreationRequirement(1, ItemList.sassafras, 5, true));
        diseasePotion.addRequirement(new CreationRequirement(2, ItemList.belladonna, 4, true));
        diseasePotion.addRequirement(new CreationRequirement(3, ItemList.water, 1, true));
        diseasePotion.addRequirement(new CreationRequirement(4, ItemList.flaskPottery, 1, true));
        // Special Items
        final AdvancedCreationEntry machinaFortune = CreationEntryCreator.createAdvancedEntry(SkillList.SMITHING_BLACKSMITHING, ItemList.metalRivet, ItemList.sheetCopper, machinaOfFortuneId, false, false, 0f, true, true, CreationCategories.MAGIC);
        machinaFortune.addRequirement(new CreationRequirement(1, ItemList.sheetCopper, 4, true));
        machinaFortune.addRequirement(new CreationRequirement(2, ItemList.metalRivet, 9, true));
        machinaFortune.addRequirement(new CreationRequirement(3, ItemList.bronzeBar, 5, true));
        machinaFortune.addRequirement(new CreationRequirement(4, CustomItems.metalShaftId, 5, true));
        final AdvancedCreationEntry lathe = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY_FINE, ItemList.brassBand, shaft, latheId, false, false, 0f, true, true, CreationCategories.TOOLS);
        lathe.addRequirement(new CreationRequirement(1, woodenHandleSword, 4, true));
        lathe.addRequirement(new CreationRequirement(2, nailsIronSmall, 5, true));
        lathe.addRequirement(new CreationRequirement(3, plank, 16, true));
        lathe.addRequirement(new CreationRequirement(4, shaft, 9, true));
        lathe.addRequirement(new CreationRequirement(5, nailsIronLarge, 3, true));
        lathe.addRequirement(new CreationRequirement(6, sheetIron, 1, true));
        lathe.addRequirement(new CreationRequirement(7, brassBand, 2, true));
        final AdvancedCreationEntry rockCrusher = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY_FINE, ItemList.brassBand, ItemList.shaft, rockCrusherId, false, false, 0f, true, true, CreationCategories.TOOLS);
        rockCrusher.addRequirement(new CreationRequirement(1, ItemList.shaft, 4, true));
        rockCrusher.addRequirement(new CreationRequirement(2, ItemList.nailsIronSmall, 5, true));
        rockCrusher.addRequirement(new CreationRequirement(3, CustomItems.metalShaftId, 16, true));
        rockCrusher.addRequirement(new CreationRequirement(4, ItemList.plank, 9, true));
        rockCrusher.addRequirement(new CreationRequirement(5, ItemList.nailsIronLarge, 3, true));
        rockCrusher.addRequirement(new CreationRequirement(6, ItemList.sheetIron, 3, true));
        rockCrusher.addRequirement(new CreationRequirement(7, ItemList.brassBand, 2, true));
        // Portals
        if (PortalMod.craftPortals) {
            AdvancedCreationEntry serverPortal = CreationEntryCreator.createAdvancedEntry(SkillList.MASONRY, stoneBrick, ItemList.concrete, CustomItems.serverPortal.getTemplateId(), true, true, 0.0f, false, true, CreationCategories.EPIC);
            serverPortal.addRequirement(new CreationRequirement(1, ItemList.stoneBrick, 1000, true));
            serverPortal.addRequirement(new CreationRequirement(2, ItemList.concrete, 1000, true));

            AdvancedCreationEntry hugeServerPortal = CreationEntryCreator.createAdvancedEntry(SkillList.MASONRY, ItemList.marbleBrick, ItemList.concrete, CustomItems.hugeServerPortal.getTemplateId(), true, true, 0.0f, false, true, CreationCategories.EPIC);
            hugeServerPortal.addRequirement(new CreationRequirement(1, ItemList.marbleBrick, 1500, true));
            hugeServerPortal.addRequirement(new CreationRequirement(2, ItemList.concrete, 1500, true));

            AdvancedCreationEntry steelServerPortal = CreationEntryCreator.createAdvancedEntry(SkillList.SMITHING_BLACKSMITHING, ItemList.sheetSteel, ItemList.concrete, CustomItems.steelServerPortal.getTemplateId(), true, true, 0.0f, false, true, CreationCategories.EPIC);
            steelServerPortal.addRequirement(new CreationRequirement(1, ItemList.sheetSteel, 100, true));
            steelServerPortal.addRequirement(new CreationRequirement(2, ItemList.marbleBrick, 1000, true));
            steelServerPortal.addRequirement(new CreationRequirement(3, ItemList.concrete, 1000, true));

            AdvancedCreationEntry darkCrystalServerPortal = CreationEntryCreator.createAdvancedEntry(SkillList.SMITHING_GOLDSMITHING, ItemList.riftCrystal, ItemList.concrete, CustomItems.darkCrystalServerPortal.getTemplateId(), true, true, 0.0f, false, true, CreationCategories.EPIC);
            darkCrystalServerPortal.addRequirement(new CreationRequirement(1, ItemList.riftCrystal, 100, true));
            darkCrystalServerPortal.addRequirement(new CreationRequirement(2, ItemList.concrete, 1000, true));

            AdvancedCreationEntry crystalServerPortal = CreationEntryCreator.createAdvancedEntry(SkillList.SMITHING_GOLDSMITHING, ItemList.riftCrystal, ItemList.concrete, CustomItems.crystalServerPortal.getTemplateId(), true, true, 0.0f, false, true, CreationCategories.EPIC);
            crystalServerPortal.addRequirement(new CreationRequirement(1, ItemList.riftCrystal, 100, true));
            crystalServerPortal.addRequirement(new CreationRequirement(2, ItemList.concrete, 1000, true));
        }

        RequiemLogging.debug(String.format("Initialising custom item creation entries took %s millis.", (float) (System.nanoTime() - start) / 1000000f));
    }

}