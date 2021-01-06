package org.jubaroo.mods.wurm.server.items;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.behaviours.BehaviourList;
import com.wurmonline.server.bodys.BodyTemplate;
import com.wurmonline.server.items.ItemSizes;
import com.wurmonline.server.items.ItemTemplate;
import com.wurmonline.server.items.ItemTypes;
import com.wurmonline.server.items.Materials;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.shared.constants.BodyPartConstants;
import com.wurmonline.shared.constants.IconConstants;
import com.wurmonline.shared.constants.ItemMaterials;
import com.wurmonline.shared.constants.ModelConstants;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.misc.Caches;
import org.jubaroo.mods.wurm.server.misc.templates.EnchantScrollTemplate;
import org.jubaroo.mods.wurm.server.misc.templates.ScrollTemplate;
import org.jubaroo.mods.wurm.server.misc.templates.SpawnerTemplate;
import org.jubaroo.mods.wurm.server.misc.templates.StructureTemplate;
import org.jubaroo.mods.wurm.server.server.Constants;

import java.io.IOException;

import static com.wurmonline.server.items.ItemTypes.*;
import static org.jubaroo.mods.wurm.server.server.Constants.*;

public class CustomItems {

    public static int libilaAltarId, blessedAltarId, sourceAltarId, bowlAltarId, copperBowlAltarId, skullMaskId, coffinBedId,
            thatchBedId, taxidermyPlaqueId, darkPylonId, metalShaftId, glueId, champagneId, healingFountainId,
            eventGravestoneId, dresserId, lowerCabinetId, upperCabinetId, dyeKitId, wagonKitId, pirateNoteId, pirateBossKeyId,
            summoningStoneId, joustingLanceId, skullCoinId, smallLootCarpetId, lootCarpetId, tradeTentId, tradeGoodsId,
            smallLootBoxId, lootBoxId, athanorMechanismId, spectralHideId, glimmerscaleId, gravestoneId, hatId, maskId,
            amuletId, largeWheelId, rockDustId, portableBankId, treasureHuntChestId, arcaneShardId, championOrbId,
            essenceOfWoodId, tamingStickId, creatureRenameCertificateId, heraldicCertificateId, foodCertificateId,
            labelId, newsletterId, prayerBookId, guardTokenId, diseasePotionId, machinaOfFortuneId, latheId, rockCrusherId,
            battleYoyoId, clubId, combatScytheId, godSlayerId, knucklesId, warhammerId, warhammerHeadId, affinityCatcherId,
            affinityOrbId, arrowPackHuntingId, arrowPackWarId, bookOfConversionId, chaosCrystalId, depthDrillId,
            disintegrationRodId, enchantersCrystalId, enchantOrbId, eternalOrbId, friyanTabletId, keyFragmentId, requiemDepotCacheId,
            requiemDepotId, sealedMapId, skeletonDecorationId, sorceryFragmentId, titaniumLumpId, titaniumSocketId,
            treasureBoxId, zombiePracticeDollId;

    public static ItemTemplate empireMolRehanMilitaryTent, crusadersMilitaryTent, pandemoniumMilitaryTent,
            dreadnoughtMilitaryTent, hotsMilitaryTent, jennKellonMilitaryTent, molRehanMilitaryTent, woodlandCamoMilitaryTent;

    public static ItemTemplate hotsPavilion, jennKellonPavilion, molRehanPavilion;

    public static ItemTemplate blackLegionFlag, crusadersFlag, ebonauraFlag, empireMolRehanFlag,
            jennKellonTwoFlag, kosFlag, macedoniaFlag, pandemoniumFlag, valhallaFlag, molRehanFlag, hotsFlag,
            jennKellonFlag, eagleFlag, frostHavenFlag, woodlandCamouflageFlag;

    public static ItemTemplate molRehanTallBanner, hotsTallBanner,
            jennKellonTallBanner, jennKellonTwoTallBanner, blackLegionTallBanner, crusadersTallBanner, pandemoniumTallBanner,
            dreadnoughtTallBanner, ebonauraTallBanner, romanEmpireTallBanner, valhallaTallBanner, macedoniaTallBanner,
            crossbowTallBanner, crownTallBanner, deerTallBanner, fireBloodTallBanner, blackFreedomTallBanner,
            horseSwordTallBanner, horseTallBanner, redHotsTallBanner, blackJennKellonTallBanner, lionTallBanner,
            blackMolRehanTallBanner, scorpionTallBanner, skullTallBanner, throwingStarTallBanner, winterComingTallBanner,
            woodlandCamouflageTallBanner, universityWurmTallBanner;

    public static ItemTemplate molRehanBanner, hotsBanner, jennKellonBanner,
            empireMolRehanBanner, jennKellonTwoBanner, blackLegionBanner, crusadersBanner, pandemoniumBanner, dreadnoughtBanner,
            ebonauraBanner, romanEmpireBanner, valhallaBanner, macedoniaBanner, eagleBanner, frostHavenBanner, bloodyAxeBanner,
            burningTreeBanner, chainBanner, boneBanner, spikeBanner, murlocBanner, hornsBanner, gateBanner, bloodyMacheteBanner,
            bloodHandBanner, blackRedTornBanner, woodlandCamouflageBanner;

    public static ItemTemplate rightClickTutorialSign, alertBarTutorialSign,
            wurmpediaTutorialSign, activateTutorialSign, askTutorialSign, openTutorialSign, healthBarTutorialSign,
            foodTutorialSign, fireTutorialSign, compassTutorialSign, quickbarTutorialSign, mapTutorialSign, equipTutorialSign,
            creaturesTutorialSign, tentsTutorialSign, fightingTutorialSign, healingTutorialSign, helpTutorialSign,
            deathTutorialSign, climbTutorialSign, swimTutorialSign, skillsTutorialSign, toolsTutorialSign, craftingTutorialSign,
            decayTutorialSign, terraformTutorialSign, theftTutorialSign, bartenderTutorialSign;

    public static ItemTemplate deathIslandOverheadSign, enterOverheadSign, exitOverheadSign, dangerOverheadSign,
            arenaOverheadSign, startOverheadSign, finishOverheadSign, mineEntranceOverheadSign, constructionOverheadSign,
            eventCenterOverheadSign, joustingOverheadSign, portalAreaOverheadSign, callanishPatreonOverheadSign,
            cattibriePatreonOverheadSign, vysePatreonOverheadSign;

    public static ItemTemplate callanishPatreonBlueprint, cattibriePatreonBlueprint, vysePatreonBlueprint;

    public static ItemTemplate blueNecromancerTapestry, blueWizardTapestry, cryptWizardTapestry, darkWizardTapestry, dragonsTreeTapestry,
            fireDemonTapestry, landscapeTownTapestry, landscapeWaterfallTapestry, lichTapestry, nordicWarriorTapestry, phoenixTapestry,
            seaSerpentTapestry, sealTapestry, trollKingTapestry, wizardVDragonTapestry, fourGodsTapestry, foTapestry, magranonTapestry,
            libilaTapestry, vynoraTapestry, battleOneTapestry, psychedelicTapestry, mushroomHouseTapestry, threeCrownsBattleTapestry,
            wurmOneTapestry, wurmTwoTapestry, wurmThreeTapestry, battleHastingsTapestry, bayeuxTapestry, bloodWitchTapestry,
            calmForestTapestry, crowWitchTapestry, deathAngelTapestry, dragonElfTapestry, dragonFightTapestry, joyLifeTapestry,
            lifesRichesTapestry, nightSkyTapestry, paladinTapestry, reaperTapestry, sweetDreamsTapestry, graveWitchTapestry,
            ladyUnicornTapestry, knightChampionTapestry, battleTwoTapestry, whiteKnightTapestry, requiemMapTapestry;

    public static ItemTemplate waterfallExquisiteMeditationRug, orientalExquisiteMeditationRug, darkSquareExquisiteMeditationRug,
            darkTriangleExquisiteMeditationRug, hornetExquisiteMeditationRug, cthulhuExquisiteMeditationRug,
            floralExquisiteMeditationRug, roseExquisiteMeditationRug, chineseDragonExquisiteMeditationRug,
            blueFloralExquisiteMeditationRug, navyGoldExquisiteMeditationRug, creamRoseOvalExquisiteMeditationRug,
            pinkFloralExquisiteMeditationRug, greenGoldOneExquisiteMeditationRug, redWhiteExquisiteMeditationRug,
            redGoldOneExquisiteMeditationRug, navyCreamOneExquisiteMeditationRug, lightBlueWhiteExquisiteMeditationRug,
            redGoldTwoExquisiteMeditationRug, blueWhiteFloralExquisiteMeditationRug, redWhiteTwoFloralExquisiteMeditationRug, redGoldThreeExquisiteMeditationRug,
            redGoldFourExquisiteMeditationRug, redGoldFiveExquisiteMeditationRug, navyCreamTwoExquisiteMeditationRug, navyRedExquisiteMeditationRug,
            greenGoldTwoExquisiteMeditationRug, creamFloralExquisiteMeditationRug;

    public static ItemTemplate blackRoyalLoungeChaise, blueRoyalLoungeChaise, greenRoyalLoungeChaise,
            greyRoyalLoungeChaise, purpleRoyalLoungeChaise, redRoyalLoungeChaise, yellowRoyalLoungeChaise;

    public static ItemTemplate blackFineHighChair, blueFineHighChair, greenFineHighChair, greyFineHighChair, yellowFineHighChair;

    public static ItemTemplate addyToolsBackpack, steelToolsBackpack;

    public static ItemTemplate diamondFragment, emeraldFragment, opalFragment, rubyFragment, sapphireFragment;

    public static ItemTemplate jennKellonTabard, jennKellonTwoTabard, dreadnoughtTabard, pandemoniumTabard, crusadersTabard,
            empireMolRehanTabard, hotsTabard, descendantsValhallaTabard, blackLegionTabard, abralonTabard, molRehanTabard,
            romanRepublicTabard, kosTabard, valhallaTabard, macedonTabard, universityWurmTabard, requiemTabard,
            phoenixTabard, foreverlandsTabard, woodlandCamouflageTabard;

    public static ItemTemplate hayPile, mortarPile, dirtPile, sandPile, tarPile, peatPile, logPile, rockPile, rockShard, slateShard, slatePile,
            marbleShard, marblePile, sandstoneShard, sandstonePile, ironOre, ironOrePile, copperOre, copperOrePile, goldOre, goldOrePile,
            silverOre, silverOrePile, zincOre, zincOrePile, tinOre, tinOrePile, leadOre, leadOrePile, adamantineOre, adamantineOrePile,
            adamantineBoulder, glimmersteelOre, glimmersteelOrePile, glimmersteelBoulder, riftStone1, riftStone2, riftStone3, riftStone4,
            riftCrystal1, riftCrystal2, riftCrystal3, riftCrystal4, plant1, plant2, plant3, plant4, rock1, rock2, rock3, fern1, fern2,
            fern3, fernFall1, fernFall2, fernFall3, redMushroomPile, yellowMushroomPile, brownMushroomPile, blueMushroomPile, greenMushroomPile,
            blackMushroomPile, questionMarkBag, stoneOfSoulfall, bodyBag, swordStone, campfire, coinPile;

    public static ItemTemplate flowersRedCanopyBed, kingCanopyBed, melodyCanopyBed, nightDreamsCanopyBed, passionCanopyBed, petalsCanopyBed,
            zebraCanopyBed;

    public static ItemTemplate basicFullCanopyBed, flowersRedFullCanopyBed, kingFullCanopyBed, melodyFullCanopyBed, nightDreamsFullCanopyBed,
            passionFullCanopyBed, petalsFullCanopyBed, zebraFullCanopyBed;

    public static ItemTemplate basicLargeBed, flowersRedLargeBed, kingLargeBed, melodyLargeBed, nightDreamsLargeBed, passionLargeBed,
            petalsLargeBed, zebraLargeBed;

    public static ItemTemplate fineDiningTable, longRectangularMarbleTable;

    public static ItemTemplate smallCarpet01, smallCarpet02, smallCarpet03, smallCarpet04, smallCarpet05, smallCarpet06, smallCarpet07,
            smallCarpet08, smallCarpet09, smallCarpet10, woodlandCamouflageSmallCarpet;

    public static ItemTemplate smallOvalCarpet01, smallOvalCarpet02;

    public static ItemTemplate smallCarpetRunner01, smallCarpetRunner02, smallCarpetRunner03, smallCarpetRunner04, smallCarpetRunner05;

    public static ItemTemplate naturePicture, claudePicture, deerPicture, peasantsPicture, shepardPicture, snowdriftPicture,
            streamPicture, timePicture, valleyPicture;

    public static ItemTemplate basicCurtain, flowersRedCurtain, kingCurtain, melodyCurtain, nightDreamsCurtain, passionCurtain,
            petalsCurtain, zebraCurtain, championCurtain, velvetGreenCurtain, creamCurtain, purpleNatureCurtain, redCurtain,
            greenCurtain, tasseledBlueCurtain, tasseledRedCurtain, fancyRedCurtain, fancyGreenCurtain, blackPatternCurtain,
            camoCurtain, woodlandCamoCurtain;

    public static ItemTemplate blackBearTaxidermyHead, brownBearTaxidermyHead, bisonTaxidermyHead, stagTaxidermyHead,
            wildcatTaxidermyHead, hellHoundTaxidermyHead, lionTaxidermyHead, hyenaTaxidermyHead, boarTaxidermyHead,
            wolfTaxidermyHead, worgTaxidermyHead;

    public static ItemTemplate blackBearTaxidermyHeadTrophy, brownBearTaxidermyHeadTrophy, bisonTaxidermyHeadTrophy,
            stagTaxidermyHeadTrophy, wildcatTaxidermyHeadTrophy, hellHoundTaxidermyHeadTrophy, lionTaxidermyHeadTrophy, hyenaTaxidermyHeadTrophy,
            boarTaxidermyHeadTrophy, wolfTaxidermyHeadTrophy, worgTaxidermyHeadTrophy;

    public static ItemTemplate blackBearTaxidermyStuffedHead, brownBearTaxidermyStuffedHead, bisonTaxidermyStuffedHead,
            stagTaxidermyStuffedHead, wildcatTaxidermyStuffedHead, hellHoundTaxidermyStuffedHead, lionTaxidermyStuffedHead,
            hyenaTaxidermyStuffedHead, boarTaxidermyStuffedHead, wolfTaxidermyStuffedHead, worgTaxidermyStuffedHead;

    public static ItemTemplate blackBearTaxidermyTannedHead, brownBearTaxidermyTannedHead, bisonTaxidermyTannedHead,
            stagTaxidermyTannedHead, wildcatTaxidermyTannedHead, hellHoundTaxidermyTannedHead, lionTaxidermyTannedHead,
            hyenaTaxidermyTannedHead, boarTaxidermyTannedHead, wolfTaxidermyTannedHead, worgTaxidermyTannedHead;

    public static ItemTemplate blackBearTaxidermyGroomedHead, brownBearTaxidermyGroomedHead, bisonTaxidermyGroomedHead,
            stagTaxidermyGroomedHead, wildcatTaxidermyGroomedHead, hellHoundTaxidermyGroomedHead, lionTaxidermyGroomedHead,
            hyenaTaxidermyGroomedHead, boarTaxidermyGroomedHead, wolfTaxidermyGroomedHead, worgTaxidermyGroomedHead;

    public static ItemTemplate blackBearTaxidermyBody;

    public static ItemTemplate blackBearTaxidermyTrophyBody;

    public static ItemTemplate blackBearTaxidermyStuffedBody;

    public static ItemTemplate blackBearTaxidermyTannedBody;

    public static ItemTemplate blackBearTaxidermyGroomedBody;

    public static ItemTemplate glimmerscaleBoot, glimmerscaleGlove, glimmerscaleHelmet, glimmerscaleLeggings, glimmerscaleSleeve,
            glimmerscaleVest, spectralBoot, spectralGlove, spectralCap, spectralLeggings, spectralSleeve, spectralVest,
            dragonSkullHelmet, horsemanHelm;

    public static ItemTemplate animalCache, armourCache, artifactCache, crystalCache, dragonCache, gemCache,
            moonCache, potionCache, riftCache, titanCache, toolCache, treasureMapCache, weaponCache;

    public static ItemTemplate deathCrystal, fireCrystal, frostCrystal, lifeCrystal, lesserFireCrystal;

    public static ItemTemplate scrollOfBankSlots, scrollOfGearBinding, scrollOfSummoningHorse, scrollOfTitles,
            scrollOfTownPortal, scrollOfVillageCreation, scrollOfVillageHeal, scrollOfVillageWar;

    public static ItemTemplate demonHomePortal, demonPortal, nymphHomePortal, nymphPortal;

    public static void registerCustomItems() throws IOException {
        long start = System.nanoTime();
        RequiemLogging.debug("=================  Registering Custom Server Mod Item Templates =================");
        registerToolBackpacks();
        registerGemFragments();
        registerElementalCrystals();
        registerScrolls();
        registerTabards();
        registerMilitaryTents();
        registerPavilions();
        registerFlags();
        registerTallBanners();
        registerBanners();
        registerTutorialSigns();
        registerOverheadSigns();
        registerBlueprints();
        registerTapestries();
        registerExquisiteMeditationRugs();
        registerRoyalLoungeChaises();
        registerFineHighChairs();
        registerDecorativeItems();
        registerCanopyBeds();
        registerFullCanopyBeds();
        registerLargeBeds();
        registerTables();
        registerSmallCarpets();
        registerSmallOvalCarpets();
        registerCarpetRunners();
        registerPictures();
        registerCurtains();
        registerTaxidermyHeads();
        registerTaxidermyHeadTrophies();
        registerTaxidermyStuffedHeads();
        registerTaxidermyTannedHeads();
        registerTaxidermyGroomedHeads();
        registerTaxidermyBodies();
        registerTaxidermyBodyTrophies();
        registerTaxidermyStuffedBodies();
        registerTaxidermyTannedBodies();
        registerTaxidermyGroomedBodies();
        registerCustomArmors();
        registerCaches();
        registerLibilasAltar();
        registerBlessedAltar();
        registerSourceAltar();
        registerBowlAltar();
        registerCopperBowlAltar();
        registerSkullMask();
        registerCoffinBed();
        registerThatchBed();
        registerTaxidermyPlaque();
        registerDarkPylon();
        registerMetalShaft();
        registerGlue();
        registerChampagne();
        registerHealingFountain();
        registerEventGravestone();
        registerDresser();
        registerLowerCabinet();
        registerUpperCabinet();
        registerDyeKit();
        registerWagonKit();
        registerPirateScoutNote();
        registerPirateBossKey();
        registerSummoningStone();
        registerJoustingLance();
        registerSkullCoin();
        registerSmallLootCarpet();
        registerLootCarpet();
        registerSmallLootBox();
        registerLootBox();
        registerTradeGoodTent();
        registerTradeGood();
        registerZombieTrainingDoll();
        registerStructureTemplates();
        registerSpawnerTemplates();
        registerScrollTemplates();
        registerEnchantScrollTemplates();
        registerAthanorMechanism();
        registerSpectralHide();
        registerGlimmerscale();
        registerGravestone();
        registerHat();
        registerMask();
        registerAmulet();
        registerLargeWheel();
        registerRockDust();
        registerPortableBank();
        registerPouch();
        registerTreasureHuntChest();
        registerArcaneShard();
        registerChampionOrb();
        registerEssenceOfWood();
        registerTamingStick();
        registerCreatureRenameCertificate();
        registerHeraldicCertificate();
        registerFoodCertificate();
        registerLabel();
        registerNewsletter();
        registerGuardToken();
        registerPortals();
        registerDiseasePotion();
        registerMachinaOfFortune();
        registerLathe();
        registerRockCrusher();
        registerBattleYoyo();
        registerClub();
        registerCombatScythe();
        registerGodSlayer();
        registerKnuckles();
        registerWarhammer();
        registerWarhammerHead();
        registerAffinityCatcher();
        registerAffinityOrb();
        registerArrowPackHunting();
        registerArrowPackWar();
        registerBookOfConversion();
        registerChaosCrystal();
        registerDepthDrill();
        registerDisintegrationRod();
        registerEnchantersCrystal();
        registerEnchantOrb();
        registerEternalOrb();
        registerFriyanTablet();
        registerKeyFragment();
        registerRequiemDepotCache();
        registerSupplyDepot();
        registerSealedMap();
        registerSkeletonDecoration();
        registerSorceryFragment();
        registerTitaniumLump();
        registerTitaniumSocket();
        registerTreasureBox();
        if (Constants.itemHolyBook) {
            registerPrayerBook();
        }
        RequiemLogging.debug(String.format("========== Registering all of Requiem's custom item templates took %d milliseconds ==========", (System.nanoTime() - start) / 1000000L));
    }

    private static void registerLibilasAltar() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.altar.libila")
                .name("altar of Libila", "altar of Libila", "A huge dark altar built from charred bones and skulls held together by bronze wires. The altar is covered in rotting meat, but you manage to notice some weird inscriptions on a stone slab tainted by dried blood.")
                .modelName("model.decoration.altar.unholy.")
                .imageNumber((short) IconConstants.ICON_NONE)
                .weightGrams(350000)
                .value(100)
                .dimensions(500, 500, 1000)
                .decayTime(TimeConstants.DECAYTIME_STONE)
                .material(ItemMaterials.MATERIAL_BONE)
                .behaviourType(BehaviourList.domainItemBehaviour)
                .combatDamage(0)
                .difficulty(75f)
                .containerSize(150, 150, 150)
                .isTraded(false)
                .itemTypes(new short[]{
                        ITEM_TYPE_HOLLOW,
                        ITEM_TYPE_METAL,
                        ITEM_TYPE_NOTAKE,
                        ITEM_TYPE_LIGHT,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_DOMAIN,
                        ITEM_TYPE_USE_GROUND_ONLY,
                        ITEM_TYPE_COLORABLE,
                        ITEM_TYPE_NAMED,
                        ITEM_TYPE_ONE_PER_TILE,
                        ITEM_TYPE_LIGHT_BRIGHT,
                        ITEM_TYPE_OWNER_DESTROYABLE,
                        ITEM_TYPE_USES_SPECIFIED_CONTAINER_VOLUME,
                        ITEM_TYPE_TRANSPORTABLE,
                        ITEM_TYPE_OWNER_TURNABLE,
                        ITEM_TYPE_OWNER_MOVEABLE,
                        ITEM_TYPE_IMPROVEITEM
                })
                .build();

        libilaAltarId = temp.getTemplateId();
    }

    private static void registerBlessedAltar() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.altar.blessed")
                .name("blessed Altar of Three", "blessed Altar of Three", "A huge shiny altar, built from white marble and polished granite laden with gold and silver. It has three figures around it.")
                .modelName("model.decoration.altar.holy.")
                .imageNumber((short) IconConstants.ICON_NONE)
                .weightGrams(350000)
                .value(100)
                .dimensions(500, 500, 1000)
                .decayTime(TimeConstants.DECAYTIME_STONE)
                .material(ItemMaterials.MATERIAL_BONE)
                .behaviourType(BehaviourList.domainItemBehaviour)
                .combatDamage(0)
                .difficulty(75f)
                .containerSize(150, 150, 150)
                .isTraded(false)
                .itemTypes(new short[]{
                        ITEM_TYPE_HOLLOW,
                        ITEM_TYPE_METAL,
                        ITEM_TYPE_NOTAKE,
                        ITEM_TYPE_LIGHT,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_DOMAIN,
                        ITEM_TYPE_USE_GROUND_ONLY,
                        ITEM_TYPE_COLORABLE,
                        ITEM_TYPE_NAMED,
                        ITEM_TYPE_ONE_PER_TILE,
                        ITEM_TYPE_LIGHT_BRIGHT,
                        ITEM_TYPE_OWNER_DESTROYABLE,
                        ITEM_TYPE_USES_SPECIFIED_CONTAINER_VOLUME,
                        ITEM_TYPE_TRANSPORTABLE,
                        ITEM_TYPE_OWNER_TURNABLE,
                        ITEM_TYPE_OWNER_MOVEABLE,
                        ITEM_TYPE_IMPROVEITEM
                })
                .build();

        blessedAltarId = temp.getTemplateId();
    }

    private static void registerSourceAltar() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.altar.source")
                .name("source altar", "source altar", "A huge altar that seems to have appeared from the void! Huge crystals surround an earthen basin that emanates a powerful aura. Many have guessed at its origin, but no true answer has emerged.")
                .modelName("model.structure.rift.altar.1.")
                .imageNumber((short) IconConstants.ICON_NONE)
                .weightGrams(350000)
                .value(100)
                .dimensions(500, 500, 1000)
                .decayTime(TimeConstants.DECAYTIME_STONE)
                .material(ItemMaterials.MATERIAL_BONE)
                .behaviourType(BehaviourList.domainItemBehaviour)
                .combatDamage(0)
                .difficulty(99f)
                .containerSize(250, 250, 250)
                .isTraded(false)
                .itemTypes(new short[]{
                        ITEM_TYPE_HOLLOW,
                        ITEM_TYPE_METAL,
                        ITEM_TYPE_NOTAKE,
                        ITEM_TYPE_LIGHT,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_DOMAIN,
                        ITEM_TYPE_USES_SPECIFIED_CONTAINER_VOLUME,
                        ITEM_TYPE_USE_GROUND_ONLY,
                        ITEM_TYPE_COLORABLE,
                        ITEM_TYPE_NAMED,
                        ITEM_TYPE_ONE_PER_TILE,
                        ITEM_TYPE_LIGHT_BRIGHT,
                        ITEM_TYPE_OWNER_DESTROYABLE,
                        ITEM_TYPE_TRANSPORTABLE,
                        ITEM_TYPE_OWNER_TURNABLE,
                        ITEM_TYPE_OWNER_MOVEABLE,
                        ITEM_TYPE_IMPROVEITEM
                })
                .build();

        sourceAltarId = temp.getTemplateId();
    }

    private static void registerBowlAltar() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.altar.bowl")
                .name("bowl altar", "bowl altar", "A one-foot wide shiny metal bowl, hammered thin. It can be blessed by a priest to pray on the go.")
                .modelName("model.decoration.bowl.")
                .imageNumber((short) IconConstants.ICON_DECO_BOWL)
                .weightGrams(350)
                .value(6500)
                .dimensions(5, 5, 5)
                .decayTime(TimeConstants.DECAYTIME_STONE)
                .material(ItemMaterials.MATERIAL_SILVER)
                .behaviourType(BehaviourList.domainItemBehaviour)
                .combatDamage(0)
                .difficulty(60f)
                .isTraded(true)
                .itemTypes(new short[]{
                        ITEM_TYPE_HOLLOW,
                        ITEM_TYPE_METAL,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_DOMAIN,
                        ITEM_TYPE_USE_GROUND_ONLY,
                        ITEM_TYPE_COLORABLE,
                        ITEM_TYPE_NAMED,
                        ITEM_TYPE_ONE_PER_TILE,
                        ITEM_TYPE_OWNER_DESTROYABLE,
                        ITEM_TYPE_TRANSPORTABLE,
                        ITEM_TYPE_OWNER_TURNABLE,
                        ITEM_TYPE_OWNER_MOVEABLE,
                        ITEM_TYPE_IMPROVEITEM
                })
                .build();

        bowlAltarId = temp.getTemplateId();
    }

    private static void registerCopperBowlAltar() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.altar.bowl.copper")
                .name("bowl altar", "bowl altar", "A one-foot wide shiny copper bowl, hammered thin. It can be blessed by a priest to pray on the go.")
                .modelName("model.decoration.brazier.bowl.")
                .size(ItemSizes.ITEM_SIZE_MEDIUM)
                .imageNumber((short) IconConstants.ICON_METAL_COPPER_BRAZIER_BOWL)
                .weightGrams(350)
                .value(6500)
                .dimensions(5, 5, 5)
                .decayTime(TimeConstants.DECAYTIME_STONE)
                .material(ItemMaterials.MATERIAL_SILVER)
                .behaviourType(BehaviourList.domainItemBehaviour)
                .combatDamage(0)
                .difficulty(60f)
                .isTraded(true)
                .itemTypes(new short[]{
                        ITEM_TYPE_HOLLOW,
                        ITEM_TYPE_METAL,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_DOMAIN,
                        ITEM_TYPE_USE_GROUND_ONLY,
                        ITEM_TYPE_COLORABLE,
                        ITEM_TYPE_NAMED,
                        ITEM_TYPE_ONE_PER_TILE,
                        ITEM_TYPE_OWNER_DESTROYABLE,
                        ITEM_TYPE_TRANSPORTABLE,
                        ITEM_TYPE_OWNER_TURNABLE,
                        ITEM_TYPE_OWNER_MOVEABLE,
                        ITEM_TYPE_IMPROVEITEM
                })
                .build();

        copperBowlAltarId = temp.getTemplateId();
    }

    private static void registerSkullMask() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.mask.skull")
                .name("skull mask", "skull masks", "At first glance the mask looks brittle, it's however surprisingly sturdy and the materials used are of the highest quality.")
                .modelName("model.armour.head.mask.skull.bone.")
                .imageNumber((short) IconConstants.ICON_SMALL_SKULL)
                .weightGrams(200)
                .value(100)
                .dimensions(1, 10, 20)
                .decayTime(TimeConstants.DECAYTIME_STONE)
                .material(ItemMaterials.MATERIAL_BONE)
                .behaviourType(BehaviourList.itemBehaviour)
                .bodySpaces(new byte[]{BodyTemplate.face})
                .combatDamage(0)
                .difficulty(25f)
                .isTraded(false)
                .itemTypes(new short[]{
                        ITEM_TYPE_NAMED,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_LEATHER,
                        ITEM_TYPE_ARMOUR,
                        ITEM_TYPE_COLORABLE,
                        ITEM_TYPE_NO_IMPROVE
                })
                .build();

        skullMaskId = temp.getTemplateId();
    }

    private static void registerCoffinBed() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.bed.coffin")
                .name("coffin bed", "coffin beds", "An average sized coffin made from stone with a heavy lid. This coffin has been fitted with cloth and a pillow to be slept in.")
                .modelName("model.furniture.coffin.")
                .imageNumber((short) IconConstants.ICON_STONE_COFFIN)
                .weightGrams(360000)
                .value(500)
                .dimensions(50, 50, 200)
                .decayTime(TimeConstants.DECAYTIME_STONE)
                .material(ItemMaterials.MATERIAL_STONE)
                .behaviourType(BehaviourList.itemBehaviour)
                .combatDamage(0)
                .difficulty(20f)
                .isTraded(false)
                .itemTypes(new short[]{
                        ITEM_TYPE_ONE_PER_TILE,
                        ITEM_TYPE_NAMED,
                        ITEM_TYPE_STONE,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_COLORABLE,
                        ITEM_TYPE_NOTAKE,
                        ITEM_TYPE_USE_GROUND_ONLY,
                        ITEM_TYPE_OWNER_DESTROYABLE,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_BED,
                        ITEM_TYPE_INSIDE_ONLY,
                        ITEM_TYPE_TRANSPORTABLE,
                        ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION,
                        ITEM_TYPE_NOT_MISSION,
                        ITEM_TYPE_IMPROVEITEM
                })
                .build();

        coffinBedId = temp.getTemplateId();
    }

    private static void registerThatchBed() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.bed.thatch")
                .name("thatch bed", "thatch beds", "A crude bed made from thatch wrapped with cotton string. The bed is not the most comfortable, but will work in a pinch.")
                .modelName("model.furniture.bed.straw.")
                .imageNumber((short) IconConstants.ICON_STONE_COFFIN)
                .weightGrams(8400)
                .value(500)
                .dimensions(50, 50, 200)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_STRAW)
                .behaviourType(BehaviourList.itemBehaviour)
                .combatDamage(0)
                .difficulty(10f)
                .isTraded(false)
                .itemTypes(new short[]{
                        ITEM_TYPE_ONE_PER_TILE,
                        ITEM_TYPE_NAMED,
                        ITEM_TYPE_STONE,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_COLORABLE,
                        ITEM_TYPE_NOTAKE,
                        ITEM_TYPE_USE_GROUND_ONLY,
                        ITEM_TYPE_OWNER_DESTROYABLE,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_BED,
                        ITEM_TYPE_INSIDE_ONLY,
                        ITEM_TYPE_TRANSPORTABLE,
                        ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION,
                        ITEM_TYPE_NOT_MISSION,
                        ITEM_TYPE_IMPROVEITEM
                })
                .build();

        thatchBedId = temp.getTemplateId();
    }

    private static void registerTaxidermyPlaque() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.taxidermy.plaque")
                .name("plaque", "plaques", "A plaque for mounting trophies.")
                .modelName("model.furniture.plaque.")
                .imageNumber((short) IconConstants.ICON_NONE)
                .weightGrams(2200)
                .value(200)
                .dimensions(10, 60, 250)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.itemBehaviour)
                .combatDamage(0)
                .difficulty(30f)
                .isTraded(false)
                .itemTypes(new short[]{
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_COLORABLE,
                        ITEM_TYPE_NAMED,
                        ITEM_TYPE_IMPROVEITEM
                })
                .build();

        taxidermyPlaqueId = temp.getTemplateId();
    }

    private static void registerDarkPylon() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.pylon.dark")
                .name("dark pylon", "dark pylons", "This dark version of a monumental structure is said to represent two hills between which the moon rises and sets. It is commonly associated with death and decay.")
                .modelName("model.structure.pylon.dark.")
                .imageNumber((short) IconConstants.ICON_NONE)
                .weightGrams(2000000)
                .value(200)
                .dimensions(300, 500, 1500)
                .decayTime(TimeConstants.DECAYTIME_STONE)
                .material(ItemMaterials.MATERIAL_STONE)
                .behaviourType(BehaviourList.itemBehaviour)
                .combatDamage(0)
                .difficulty(70f)
                .isTraded(false)
                .itemTypes(new short[]{
                        ITEM_TYPE_STONE,
                        ITEM_TYPE_OUTSIDE_ONLY,
                        ITEM_TYPE_NOTAKE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_OWNER_TURNABLE,
                        ITEM_TYPE_OWNER_MOVEABLE,
                        ITEM_TYPE_USE_GROUND_ONLY,
                        ITEM_TYPE_ONE_PER_TILE,
                        ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION,
                        ITEM_TYPE_IMPROVEITEM
                })
                .build();

        darkPylonId = temp.getTemplateId();
    }

    private static void registerMetalShaft() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.shaft.metal")
                .name("metal shaft", "metal shafts", "A one step long metal shaft that could be used to create things.")
                .modelName("model.weapon.polearm.staff.")
                .imageNumber((short) IconConstants.ICON_WOOD_SHAFT)
                .weightGrams(2500)
                .value(120)
                .dimensions(3, 7, 45)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_IRON)
                .behaviourType(BehaviourList.itemBehaviour)
                .combatDamage(4)
                .difficulty(13f)
                .primarySkill(SkillList.SMITHING_BLACKSMITHING)
                .isTraded(false)
                .itemTypes(new short[]{
                        ITEM_TYPE_DISARM_TRAP,
                        ITEM_TYPE_BULK,
                        ITEM_TYPE_METAL,
                        ITEM_TYPE_WEAPON_CRUSH,
                        ITEM_TYPE_WEAPON,
                        ITEM_TYPE_TWOHANDED,
                        ITEM_TYPE_DECAYDESTROYS,
                        ITEM_TYPE_MASSPRODUCTION
                })
                .build();

        metalShaftId = temp.getTemplateId();
    }

    private static void registerGlue() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.glue")
                .name("glue", "glue", "An adhesive substance used for sticking objects or materials together.")
                .modelName("model.liquid.lye.")
                .imageNumber((short) IconConstants.ICON_LIQUID_LYE)
                .weightGrams(150)
                .value(120)
                .dimensions(3, 3, 3)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_WHEAT)
                .behaviourType(BehaviourList.itemBehaviour)
                .combatDamage(0)
                .difficulty(5f)
                .isTraded(false)
                .itemTypes(new short[]{
                        ITEM_TYPE_LIQUID,
                        ITEM_TYPE_RECIPE_ITEM
                })
                .build();

        glueId = temp.getTemplateId();
    }

    private static void registerChampagne() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.champagne")
                .name("Requiem champagne", "Requiem champagne", "A bottle of Requiem's finest champagne to celebrate another year.")
                .modelName("model.container.champagne.")
                .imageNumber((short) IconConstants.ICON_MISC_FLASK_GLASS)
                .weightGrams(150)
                .value(120)
                .dimensions(3, 3, 6)
                .decayTime(TimeConstants.DECAYTIME_MAGIC)
                .material(ItemMaterials.MATERIAL_GLASS)
                .behaviourType(BehaviourList.itemBehaviour)
                .combatDamage(0)
                .difficulty(5f)
                .isTraded(false)
                .itemTypes(new short[]{
                        ITEM_TYPE_HASDATA
                })
                .build();

        champagneId = temp.getTemplateId();
    }

    private static void registerHealingFountain() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.fountain.healing")
                .name("healing fountain", "healing fountain", "A fountain that was bless by the gods and can heal any who drink from it.")
                .modelName("model.decoration.fountain.2.")
                .imageNumber((short) IconConstants.ICON_NONE)
                .weightGrams(420000)
                .value(100)
                .dimensions(1000, 1000, 1000)
                .decayTime(TimeConstants.DECAYTIME_NEVER)
                .material(ItemMaterials.MATERIAL_STONE)
                .behaviourType(BehaviourList.itemBehaviour)
                .combatDamage(0)
                .difficulty(5f)
                .isTraded(false)
                .itemTypes(new short[]{
                        ITEM_TYPE_ONE_PER_TILE,
                        ITEM_TYPE_PLANTABLE,
                        ITEM_TYPE_STONE,
                        ITEM_TYPE_POSITIVE_DECAY,
                        ITEM_TYPE_NOTAKE,
                        ITEM_TYPE_NOMOVE
                })
                .build();

        healingFountainId = temp.getTemplateId();
    }

    private static void registerEventGravestone() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.event.gravestone")
                .name("pirate gravestone", "pirate gravestones", "A gravestone for a traitorous pirate that wanted to take the treasure for himself. The crew killed him and buried him where the treasure used to rest.")
                .modelName("model.decoration.gravestone.buried.")
                .imageNumber((short) IconConstants.ICON_DECO_GRAVESTONE)
                .weightGrams(200000)
                .value(100)
                .dimensions(300, 300, 300)
                .decayTime(TimeConstants.DECAYTIME_STONE)
                .material(ItemMaterials.MATERIAL_STONE)
                .behaviourType(BehaviourList.itemBehaviour)
                .isTraded(false)
                .itemTypes(new short[]{
                        ITEM_TYPE_OWNER_MOVEABLE,
                        ITEM_TYPE_OWNER_TURNABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_NOTAKE
                })
                .build();

        eventGravestoneId = temp.getTemplateId();
    }

    private static void registerDresser() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.furniture.dresser")
                .name("dresser", "dressers", "A dresser with wooden inlays.")
                .modelName("model.furniture.dresser.")
                .imageNumber((short) IconConstants.ICON_NONE)
                .weightGrams(90000)
                .value(10000)
                .difficulty(60f)
                .dimensions(120, 80, 210)
                .containerSize(90, 80, 200)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.itemBehaviour)
                .isTraded(false)
                .itemTypes(new short[]{
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_HOLLOW,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_USE_GROUND_ONLY,
                        ITEM_TYPE_NOTAKE,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_PLANTABLE,
                        ITEM_TYPE_TRANSPORTABLE,
                        ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION,
                        ITEM_TYPE_USES_SPECIFIED_CONTAINER_VOLUME,
                        ITEM_TYPE_LOCKABLE
                })
                .build();

        dresserId = temp.getTemplateId();
    }

    private static void registerLowerCabinet() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.furniture.cabinet.lower")
                .name("lower cabinet", "lower cabinets", "A lower cabinet for storing things in.")
                .modelName("model.furniture.cabinet.low.")
                .imageNumber((short) IconConstants.ICON_NONE)
                .weightGrams(100000)
                .value(10000)
                .difficulty(60f)
                .dimensions(120, 80, 210)
                .containerSize(90, 80, 200)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.itemBehaviour)
                .isTraded(false)
                .itemTypes(new short[]{
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_HOLLOW,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_USE_GROUND_ONLY,
                        ITEM_TYPE_NOTAKE,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_PLANTABLE,
                        ITEM_TYPE_TRANSPORTABLE,
                        ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION,
                        ITEM_TYPE_USES_SPECIFIED_CONTAINER_VOLUME,
                        ITEM_TYPE_LOCKABLE
                })
                .build();

        lowerCabinetId = temp.getTemplateId();
    }

    private static void registerUpperCabinet() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.furniture.cabinet.upper")
                .name("upper cabinet", "upper cabinets", "A upper cabinet for storing things in.")
                .modelName("model.furniture.cabinet.high.")
                .imageNumber((short) IconConstants.ICON_NONE)
                .weightGrams(100000)
                .value(10000)
                .difficulty(60f)
                .dimensions(120, 80, 210)
                .containerSize(90, 80, 200)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.itemBehaviour)
                .isTraded(false)
                .itemTypes(new short[]{
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_HOLLOW,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_USE_GROUND_ONLY,
                        ITEM_TYPE_NOTAKE,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_PLANTABLE,
                        ITEM_TYPE_TRANSPORTABLE,
                        ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION,
                        ITEM_TYPE_USES_SPECIFIED_CONTAINER_VOLUME,
                        ITEM_TYPE_LOCKABLE
                })
                .build();

        upperCabinetId = temp.getTemplateId();
    }

    private static void registerDyeKit() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.tool.dyekit")
                .name("dye kit", "dye kits", "A kit containing multiple dyes to change the color of dragon armor.")
                .modelName("model.container.backpack.")
                .imageNumber((short) IconConstants.ICON_CONTAINER_BACKPACK)
                .weightGrams(6500)
                .value(1)
                .dimensions(30, 50, 50)
                .decayTime(TimeConstants.BASEDECAY)
                .material(ItemMaterials.MATERIAL_LEATHER)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ITEM_TYPE_NOT_MISSION
                })
                .build();

        dyeKitId = temp.getTemplateId();
    }

    private static void registerWagonKit() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.wagonKit")
                .name("dye kit", "dye kits", "A kit containing instructions and all the materials to build a wagon.")
                .modelName("model.container.crate.large.")
                .imageNumber((short) IconConstants.ICON_LARGE_CRATE)
                .weightGrams(38500)
                .value(100)
                .dimensions(150, 150, 150)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ITEM_TYPE_NOT_MISSION
                })
                .build();

        wagonKitId = temp.getTemplateId();
    }

    private static void registerPirateScoutNote() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.event.note.pirate")
                .name("Pirate Scout Note", "notes", "A hastily written note from a pirate facing certain death.")
                .modelName("model.resource.sheet.paper.")
                .imageNumber((short) IconConstants.ICON_NONE)
                .weightGrams(100)
                .value(100)
                .dimensions(2, 2, 5)
                .decayTime(TimeConstants.DECAYTIME_VALUABLE)
                .material(ItemMaterials.MATERIAL_PAPER)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ITEM_TYPE_NOT_MISSION
                })
                .build();

        pirateNoteId = temp.getTemplateId();
    }

    private static void registerPirateBossKey() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.event.key.pirate")
                .name("pirate key", "pirate keys", "A key to a treasure chest.")
                .modelName("model.key.")
                .imageNumber((short) IconConstants.ICON_TOOL_KEY)
                .weightGrams(10)
                .value(100)
                .dimensions(2, 2, 5)
                .decayTime(TimeConstants.DECAYTIME_STEEL)
                .material(ItemMaterials.MATERIAL_IRON)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ITEM_TYPE_NOT_MISSION
                })
                .build();

        pirateBossKeyId = temp.getTemplateId();
    }

    private static void registerSummoningStone() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.event.summoning.stone")
                .name("summoning stone", "summoning stones", "A large stone with strange runic writing on it. If the words are read in the correctly, it could summon a powerful creature from beyond the void! A section of writing seems to pop out at you, it reads \"\"\"Place your hand upon the stone to learn the creatures desires.")
                .modelName("model.structure.portal.10.")
                .imageNumber((short) IconConstants.ICON_FRAGMENT_STONE)
                .weightGrams(8000000)
                .value(1)
                .dimensions(200, 200, 200)
                .decayTime(TimeConstants.DECAYTIME_STONE)
                .material(ItemMaterials.MATERIAL_STONE)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ITEM_TYPE_NOT_MISSION,
                        ITEM_TYPE_DECORATION
                })
                .build();

        summoningStoneId = temp.getTemplateId();
    }

    private static void registerJoustingLance() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.event.joust.lance")
                .name("jousting lance", "jousting lances", "A long spear used in the sport of jousting.")
                .modelName("model.weapon.polearm.spear.long.")
                .imageNumber((short) IconConstants.ICON_WEAPON_SPEAR_LONG)
                .weightGrams(2700)
                .value(1)
                .primarySkill(SkillList.SPEAR_LONG)
                .combatDamage(30)
                .difficulty(20.0f)
                .dimensions(3, 5, 205)
                .decayTime(TimeConstants.DECAYTIME_CORPSE)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ITEM_TYPE_WEAPON,
                        ITEM_TYPE_WEAPON_PIERCE,
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_EQUIPMENTSLOT,
                        ITEM_TYPE_DECAYDESTROYS
                })
                .build();

        joustingLanceId = temp.getTemplateId();
    }

    private static void registerSkullCoin() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.currency.coin.skull")
                .name("skull coin", "skull coins", "A silver coin with the imprint of a skull on it. Maybe someone would be interested in it?")
                .modelName("model.coin.skull.")
                .imageNumber((short) IconConstants.ICON_SMALL_SKULL)
                .weightGrams(10)
                .value(1)
                .difficulty(200.0f)
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_NEVER)
                .material(ItemMaterials.MATERIAL_SILVER)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ITEM_TYPE_METAL,
                        ITEM_TYPE_INDESTRUCTIBLE,
                        ITEM_TYPE_NOT_MISSION
                })
                .build();

        skullCoinId = temp.getTemplateId();
    }

    private static void registerSmallLootCarpet() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.loot.carpet.small")
                .name("loot carpet", "loot carpets", "A place where loot is stored.")
                .modelName("model.decoration.carpet.medi.three.")
                .imageNumber((short) IconConstants.ICON_LARGE_CRATE)
                .size(ItemSizes.ITEM_SIZE_SMALL)
                .weightGrams(100000)
                .value(1)
                .difficulty(5f)
                .dimensions(120, 120, 120)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_NOTAKE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_USE_GROUND_ONLY,
                        ITEM_TYPE_ONE_PER_TILE,
                        ITEM_TYPE_INDESTRUCTIBLE,
                        ITEM_TYPE_OUTSIDE_ONLY,
                        ITEM_TYPE_OWNER_MOVEABLE,
                        ITEM_TYPE_OWNER_TURNABLE,
                        ITEM_TYPE_NOMOVE,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION
                })
                .build();

        smallLootCarpetId = temp.getTemplateId();
    }

    private static void registerLootCarpet() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.loot.carpet")
                .name("loot carpet", "loot carpets", "A place where loot is stored.")
                .modelName("model.decoration.colorful.carpet.large.")
                .imageNumber((short) IconConstants.ICON_LARGE_CRATE)
                .weightGrams(100000)
                .value(1)
                .difficulty(5f)
                .dimensions(120, 120, 120)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_NOTAKE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_USE_GROUND_ONLY,
                        ITEM_TYPE_ONE_PER_TILE,
                        ITEM_TYPE_INDESTRUCTIBLE,
                        ITEM_TYPE_OUTSIDE_ONLY,
                        ITEM_TYPE_OWNER_MOVEABLE,
                        ITEM_TYPE_OWNER_TURNABLE,
                        ITEM_TYPE_NOMOVE,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION
                })
                .build();

        lootCarpetId = temp.getTemplateId();
    }

    private static void registerSmallLootBox() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.loot.box.small")
                .name("loot box", "trade goods", "Some loot stashed by the denizens of the dungeon.")
                .modelName("model.container.chest.small.magical.")
                .size(ItemSizes.ITEM_SIZE_SMALL)
                .imageNumber((short) IconConstants.ICON_TACKLEBOX_COMPARTMENT)
                .weightGrams(100000)
                .value(1)
                .difficulty(5f)
                .dimensions(120, 120, 120)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_GOLD)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ITEM_TYPE_NOTAKE,
                        ITEM_TYPE_NOPUT,
                        ITEM_TYPE_NAMED,
                        ITEM_TYPE_OWNER_DESTROYABLE,
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_TRANSPORTABLE,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION
                })
                .build();

        smallLootBoxId = temp.getTemplateId();
    }

    private static void registerLootBox() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.loot.box")
                .name("loot box", "trade goods", "Some loot stashed by the denizens of the dungeon.")
                .modelName("model.container.chest.large.magical.")
                .imageNumber((short) IconConstants.ICON_TACKLEBOX_COMPARTMENT)
                .weightGrams(100000)
                .value(1)
                .difficulty(5f)
                .dimensions(120, 120, 120)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_GOLD)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ITEM_TYPE_NOTAKE,
                        ITEM_TYPE_NOPUT,
                        ITEM_TYPE_NAMED,
                        ITEM_TYPE_OWNER_DESTROYABLE,
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_TRANSPORTABLE,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION
                })
                .build();

        lootBoxId = temp.getTemplateId();
    }

    private static void registerTradeGoodTent() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.trade.tent")
                .name("trade goods tent", "trade goods tents", "A place where trade goods are delivered.")
                .modelName("model.structure.tent.pavilion.")
                .imageNumber((short) IconConstants.ICON_LARGE_CRATE)
                .weightGrams(100000)
                .value(1)
                .difficulty(5f)
                .dimensions(120, 120, 120)
                .decayTime(TimeConstants.DECAYTIME_NEVER)
                .material(ItemMaterials.MATERIAL_COTTON)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ITEM_TYPE_NAMED,
                        ITEM_TYPE_OWNER_DESTROYABLE,
                        ITEM_TYPE_CLOTH,
                        ITEM_TYPE_NOTAKE,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_TRANSPORTABLE,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION
                })
                .build();

        tradeTentId = temp.getTemplateId();
    }

    private static void registerTradeGood() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.trade.goods")
                .name("trade goods", "trade goods", "A crate of goods for trade.")
                .modelName("model.container.crate.large.")
                .imageNumber((short) IconConstants.ICON_LARGE_CRATE)
                .weightGrams(100000)
                .value(1)
                .difficulty(5f)
                .dimensions(120, 120, 120)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ITEM_TYPE_NAMED,
                        ITEM_TYPE_OWNER_DESTROYABLE,
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_NOTAKE,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_TRANSPORTABLE,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION
                })
                .build();

        tradeGoodsId = temp.getTemplateId();
    }

    private static void registerZombieTrainingDoll() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.training.zombie")
                .name("practice doll (zombie)", "practice doll (zombie)", "An exquisite mansized practice doll made from shafts, a pair of planks and a pumpkin. It looks like a zombie!")
                .modelName("model.decoration.practicedoll.zombie")
                .imageNumber((short) IconConstants.ICON_DECO_PRACTICEDOLL)
                .weightGrams(7000)
                .value(10000)
                .difficulty(5f)
                .dimensions(10, 30, 180)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.practiceDollBehaviour)
                .itemTypes(new short[]{
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_REPAIRABLE
                })
                .isTraded(true)
                .build();

        zombiePracticeDollId = temp.getTemplateId();
    }

    private static void registerStructureTemplates() throws IOException {
        for (StructureTemplate template : structureTemplates) {
            ItemTemplateBuilder itemTemplateBuilder = new ItemTemplateBuilder("jubaroo.item.template.structure." + template.name);
            itemTemplateBuilder.name(template.name, String.format("%ss", template.name), String.format("A %s.", template.name));
            itemTemplateBuilder.descriptions("excellent", "good", "ok", "poor");
            itemTemplateBuilder.itemTypes(new short[]{
                    ITEM_TYPE_WOOD,
                    ITEM_TYPE_NOTAKE,
                    ITEM_TYPE_DECORATION,
                    ITEM_TYPE_USE_GROUND_ONLY,
                    ITEM_TYPE_ONE_PER_TILE,
                    ITEM_TYPE_INDESTRUCTIBLE,
                    ITEM_TYPE_OUTSIDE_ONLY,
                    ITEM_TYPE_OWNER_MOVEABLE,
                    ITEM_TYPE_OWNER_TURNABLE,
                    ITEM_TYPE_NOMOVE,
                    ITEM_TYPE_HASDATA,
                    ITEM_TYPE_HOLLOW,
                    ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION
            });
            itemTemplateBuilder.imageNumber((short) IconConstants.ICON_NONE);
            itemTemplateBuilder.behaviourType(BehaviourList.vehicleBehaviour);
            itemTemplateBuilder.combatDamage(0);
            itemTemplateBuilder.decayTime(TimeConstants.DECAYTIME_WOOD);
            itemTemplateBuilder.dimensions(400, 400, 400);
            itemTemplateBuilder.primarySkill((int) MiscConstants.NOID);
            itemTemplateBuilder.bodySpaces(MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY);
            itemTemplateBuilder.modelName(template.model);
            itemTemplateBuilder.difficulty(5.0f);
            itemTemplateBuilder.weightGrams(70000);
            itemTemplateBuilder.material(Materials.MATERIAL_WOOD_BIRCH);
            ItemTemplate thisTemplate = itemTemplateBuilder.build();

            template.templateID = thisTemplate.getTemplateId();
        }
    }

    private static void registerSpawnerTemplates() throws IOException {
        for (SpawnerTemplate template : spawnerTemplates) {
            ItemTemplateBuilder itemTemplateBuilder = new ItemTemplateBuilder("jubaroo.item.template.spawner." + template.name);
            itemTemplateBuilder.name(template.name, String.format("%ss", template.name), String.format("A %s.", template.name));
            itemTemplateBuilder.descriptions("excellent", "good", "ok", "poor");
            itemTemplateBuilder.itemTypes(new short[]{
                    ITEM_TYPE_WOOD,
                    ITEM_TYPE_NOTAKE,
                    ITEM_TYPE_DECORATION,
                    ITEM_TYPE_USE_GROUND_ONLY,
                    ITEM_TYPE_ONE_PER_TILE,
                    ITEM_TYPE_INDESTRUCTIBLE,
                    ITEM_TYPE_OUTSIDE_ONLY,
                    ITEM_TYPE_OWNER_MOVEABLE,
                    ITEM_TYPE_OWNER_TURNABLE,
                    ITEM_TYPE_NOMOVE,
                    ITEM_TYPE_HASDATA,
                    ITEM_TYPE_HOLLOW,
                    ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION
            });
            itemTemplateBuilder.imageNumber((short) IconConstants.ICON_NONE);
            itemTemplateBuilder.behaviourType(BehaviourList.vehicleBehaviour);
            itemTemplateBuilder.combatDamage(0);
            itemTemplateBuilder.decayTime(TimeConstants.DECAYTIME_WOOD);
            itemTemplateBuilder.dimensions(400, 400, 400);
            itemTemplateBuilder.primarySkill((int) MiscConstants.NOID);
            itemTemplateBuilder.bodySpaces(MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY);
            itemTemplateBuilder.modelName(template.model);
            itemTemplateBuilder.difficulty(5.0f);
            itemTemplateBuilder.weightGrams(70000);
            itemTemplateBuilder.material(Materials.MATERIAL_WOOD_BIRCH);
            ItemTemplate thisTemplate = itemTemplateBuilder.build();

            template.templateID = thisTemplate.getTemplateId();
        }
    }

    private static void registerScrollTemplates() throws IOException {
        for (ScrollTemplate template : scrollTemplates) {
            ItemTemplateBuilder itemTemplateBuilder = new ItemTemplateBuilder("jubaroo.item.template.scroll." + template.name);
            itemTemplateBuilder.name(template.name, String.format("%ss", template.name), template.description);
            itemTemplateBuilder.descriptions("excellent", "good", "ok", "poor");
            itemTemplateBuilder.itemTypes(new short[]{
                    ITEM_TYPE_FULLPRICE,
                    ITEM_TYPE_HASDATA,
                    ITEM_TYPE_NAMED,
                    ITEM_TYPE_DECORATION,
                    ITEM_TYPE_WOOD,
                    ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION

            });
            itemTemplateBuilder.imageNumber((short) IconConstants.ICON_NONE);
            itemTemplateBuilder.behaviourType(BehaviourList.vehicleBehaviour);
            itemTemplateBuilder.combatDamage(0);
            itemTemplateBuilder.decayTime(TimeConstants.DECAYTIME_WOOD);
            itemTemplateBuilder.dimensions(15, 15, 15);
            itemTemplateBuilder.primarySkill((int) MiscConstants.NOID);
            itemTemplateBuilder.bodySpaces(MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY);
            itemTemplateBuilder.modelName(template.model);
            itemTemplateBuilder.difficulty(5.0f);
            itemTemplateBuilder.weightGrams(100);
            itemTemplateBuilder.material(Materials.MATERIAL_WOOD_BIRCH);
            ItemTemplate thisTemplate = itemTemplateBuilder.build();

            template.templateID = thisTemplate.getTemplateId();
        }
    }

    private static void registerEnchantScrollTemplates() throws IOException {
        for (EnchantScrollTemplate template : enchantScrollTemplates) {
            ItemTemplateBuilder itemTemplateBuilder = new ItemTemplateBuilder("jubaroo.item.template.scroll.enchant." + template.name);
            itemTemplateBuilder.name(template.name, String.format("%ss", template.name), template.description);
            itemTemplateBuilder.descriptions("excellent", "good", "ok", "poor");
            itemTemplateBuilder.itemTypes(new short[]{
                    ITEM_TYPE_FULLPRICE,
                    ITEM_TYPE_HASDATA,
                    ITEM_TYPE_NAMED,
                    ITEM_TYPE_DECORATION,
                    ITEM_TYPE_WOOD,
                    ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION
            });
            itemTemplateBuilder.imageNumber((short) IconConstants.ICON_NONE);
            itemTemplateBuilder.behaviourType(BehaviourList.vehicleBehaviour);
            itemTemplateBuilder.combatDamage(0);
            itemTemplateBuilder.decayTime(TimeConstants.DECAYTIME_WOOD);
            itemTemplateBuilder.dimensions(15, 15, 15);
            itemTemplateBuilder.primarySkill((int) MiscConstants.NOID);
            itemTemplateBuilder.bodySpaces(MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY);
            itemTemplateBuilder.modelName(template.model);
            itemTemplateBuilder.difficulty(5.0f);
            itemTemplateBuilder.weightGrams(100);
            itemTemplateBuilder.material(Materials.MATERIAL_WOOD_BIRCH);
            ItemTemplate thisTemplate = itemTemplateBuilder.build();

            template.templateID = thisTemplate.getTemplateId();
        }
    }

    private static void registerAthanorMechanism() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.athanorMechanism")
                .name("Athanor Mechanism", "Athanor Mechanism", "A strange mechanism that seems out of place here. It has a hole in it the shape of a star gem. It seems to give off a very faint humming sound.")
                .modelName(ModelConstants.MODEL_STRUCTURE_OBELISQUE)
                .imageNumber((short) IconConstants.ICON_OBELISQUE)
                .weightGrams(1000000)
                .value(1)
                .difficulty(200f)
                .dimensions(1200, 1200, 1200)
                .decayTime(TimeConstants.DECAYTIME_NEVER)
                .material(ItemMaterials.MATERIAL_MARBLE)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_NOTAKE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_USE_GROUND_ONLY,
                        ITEM_TYPE_ONE_PER_TILE,
                        ITEM_TYPE_INDESTRUCTIBLE,
                        ITEM_TYPE_OUTSIDE_ONLY,
                        ITEM_TYPE_OWNER_MOVEABLE,
                        ITEM_TYPE_OWNER_TURNABLE,
                        ITEM_TYPE_NOMOVE,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION
                })
                .build();

        athanorMechanismId = temp.getTemplateId();
    }

    private static void registerSpectralHide() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.spectral.hide")
                .name("spectral hide", "spectral hide", "Lightweight and transparent, this ethereal leather comes from another plane of existence. It is stronger than natural drake hide.")
                .modelName("model.resource.leather.dragon.")
                .imageNumber((short) IconConstants.ICON_LEATHER_SKIN)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_HASDATA,
                        ItemTypes.ITEM_TYPE_METAL,
                        ItemTypes.ITEM_TYPE_BULK,
                        ItemTypes.ITEM_TYPE_COMBINE,
                        ItemTypes.ITEM_TYPE_NOT_MISSION
                })
                .dimensions(10, 30, 30)
                .decayTime(TimeConstants.DECAYTIME_VALUABLE)
                .material(ItemMaterials.MATERIAL_LEATHER)
                .behaviourType(BehaviourList.itemBehaviour)
                .weightGrams(200)
                .value(200000)
                .difficulty(20f)
                .build();

        spectralHideId = temp.getTemplateId();
    }

    private static void registerGlimmerscale() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.glimmerscale.scale")
                .name("glimmerscale", "glimmerscales", "Legendary scales, made from the careful combination of dragon scales and glimmersteel.")
                .modelName("model.resource.scales.dragon.")
                .imageNumber((short) IconConstants.ICON_BODY_DRAGON_SCAILS)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_HASDATA,
                        ItemTypes.ITEM_TYPE_METAL,
                        ItemTypes.ITEM_TYPE_BULK,
                        ItemTypes.ITEM_TYPE_COMBINE,
                        ItemTypes.ITEM_TYPE_NOT_MISSION
                })
                .dimensions(10, 30, 30)
                .decayTime(TimeConstants.DECAYTIME_VALUABLE)
                .material(ItemMaterials.MATERIAL_UNDEFINED)
                .behaviourType(BehaviourList.itemBehaviour)
                .weightGrams(400)
                .value(200000)
                .difficulty(60f)
                .build();

        glimmerscaleId = temp.getTemplateId();
    }

    private static void registerGravestone() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.halloween.gravestone")
                .name("flaming gravestone", "flaming gravestone", "An flaming gravestone. The souls of the dead forever haunt this grave.")
                .modelName("model.decoration.gravestone.buried.")
                .imageNumber((short) IconConstants.ICON_DECO_GRAVESTONE)
                .weightGrams(100000)
                .dimensions(20, 20, 400)
                .decayTime(TimeConstants.DECAYTIME_MAGIC)
                .material(ItemMaterials.MATERIAL_STONE)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NOMOVE,
                        ItemTypes.ITEM_TYPE_NOTAKE,
                        ItemTypes.ITEM_TYPE_NOT_MISSION,
                        ItemTypes.ITEM_TYPE_INDESTRUCTIBLE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                })
                .isTraded(true)
                .build();

        gravestoneId = temp.getTemplateId();
    }

    private static void registerHat() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.halloween.hat")
                .name("wicked witch hat", "wicked witch hats", "A hat obtained from a powerful witch of the West.")
                .imageNumber((short) IconConstants.ICON_ARMOR_SUMMERHAT)
                .modelName("model.armour.head.hat.witch.")
                .weightGrams(500)
                .dimensions(15, 20, 20)
                .decayTime(TimeConstants.DECAYTIME_CLOTHING)
                .value(10000)
                .material(ItemMaterials.MATERIAL_COTTON)
                .behaviourType(BehaviourList.itemBehaviour)
                .bodySpaces(new byte[]{BodyTemplate.head, BodyTemplate.secondHead})
                .primarySkill(SkillList.CLOTHTAILORING)
                .difficulty(60)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_ARMOUR,
                        ItemTypes.ITEM_TYPE_CLOTH,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_IMPROVEITEM,
                        ItemTypes.ITEM_TYPE_REPAIRABLE
                })
                .isTraded(true)
                .build();

        hatId = temp.getTemplateId();
    }

    private static void registerMask() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.halloween.mask")
                .name("skull mask", "skull masks", "A spooky mask made from a skull.")
                .imageNumber((short) IconConstants.ICON_MASKS)
                .modelName("model.armour.head.mask.skull.")
                .weightGrams(500)
                .dimensions(1, 10, 20)
                .decayTime(TimeConstants.DECAYTIME_CLOTHING)
                .value(10000)
                .material(ItemMaterials.MATERIAL_BONE)
                .behaviourType(BehaviourList.itemBehaviour)
                .bodySpaces(new byte[]{BodyTemplate.face})
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_ARMOUR,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_NO_IMPROVE
                })
                .isTraded(true)
                .build();

        maskId = temp.getTemplateId();
    }

    private static void registerAmulet() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.amulet.ancient")
                .name("ancient amulet", "ancient amulets", "The amulet is of archaic design beaten into a thick coin of hammered bronze and hanging from a chain. Its time worn surface still shows a myriad of magical protection runes.")
                .imageNumber((short) IconConstants.ICON_AMULET_FARWALKER)
                .modelName("model.magic.amulet.farwalker.")
                .weightGrams(100)
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_CLOTHING)
                .value(10000)
                .behaviourType(BehaviourList.itemBehaviour)
                .bodySpaces(new byte[]{BodyPartConstants.NECK})
                .itemTypes(new short[]{
                        ITEM_TYPE_METAL,
                        ITEM_TYPE_NO_IMPROVE
                })
                .difficulty(5f)
                .material(ItemMaterials.MATERIAL_MAGIC)
                .isTraded(false)
                .build();

        amuletId = temp.getTemplateId();
    }

    private static void registerLargeWheel() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.wheel.large")
                .name("wheel", "wheels", "A larger version of the small wheel. It has many uses besides a vehicle.")
                .imageNumber((short) IconConstants.ICON_TOOL_WHEEL)
                .modelName("model.part.small.wheel.")
                .size(ItemSizes.ITEM_SIZE_LARGE)
                .weightGrams(13000)
                .dimensions(5, 80, 80)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .value(5000)
                .behaviourType(BehaviourList.itemBehaviour)
                .bodySpaces(new byte[]{BodyPartConstants.NECK})
                .itemTypes(new short[]{
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_IMPROVEITEM
                })
                .difficulty(50f)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .primarySkill(SkillList.CARPENTRY_FINE)
                .isTraded(true)
                .build();

        largeWheelId = temp.getTemplateId();
    }

    private static void registerRockDust() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.rock.dust")
                .name("rock dust", "rock dust", "Rock dust is created by crushing rock shards. Rock dust is used to create a type of fertilizer to break down the rock and form dirt.")
                .imageNumber((short) IconConstants.ICON_MORTAR_PILE)
                .modelName("model.pile.sand.")
                .weightGrams(20000)
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_STONE)
                .value(2500)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_IMPROVEITEM
                })
                .difficulty(50f)
                .material(ItemMaterials.MATERIAL_STONE)
                .primarySkill(SkillList.MASONRY)
                .build();

        rockDustId = temp.getTemplateId();
    }

    private static void registerPortableBank() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.bank.portable")
                .name("portable bank", "portable banks", "A portable bank that works through some form of arcane magic.")
                .descriptions("new", "good", "ok", "old")
                .imageNumber((short) IconConstants.ICON_CONTAINER_CHEST)
                .modelName("model.container.chest.small.magical.")
                .weightGrams(20000)
                .dimensions(25,25,25)
                .decayTime(TimeConstants.DECAYTIME_MAGIC)
                .value(1000)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_METAL,
                        ItemTypes.ITEM_TYPE_SERVERBOUND,
                        ItemTypes.ITEM_TYPE_FLOATING,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_HASDATA,
                        ItemTypes.ITEM_TYPE_FULLPRICE
                })
                .difficulty(50f)
                .material(ItemMaterials.MATERIAL_GOLD)
                .isTraded(false)
                .build();

        portableBankId = temp.getTemplateId();
    }

    private static void registerPouch() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.pouch.velvet")
                .name("red velvet pouch", "red velvet pouches", "A red velvet pouch.")
                .descriptions("new", "good", "ok", "old")
                .imageNumber((short) IconConstants.ICON_CONTAINER_CHEST)
                .modelName("model.container.satchel.")
                .weightGrams(500)
                .dimensions(25,25,25)
                .decayTime(TimeConstants.DECAYTIME_CLOTHING)
                .value(1000)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_CLOTH,
                        ItemTypes.ITEM_TYPE_HOLLOW
                })
                .difficulty(50f)
                .material(ItemMaterials.MATERIAL_COTTON)
                .build();

        portableBankId = temp.getTemplateId();
    }

    private static void registerTreasureHuntChest() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.event.pirate.chest")
                .name("Pirate Treasure Chest", "Pirate Treasure Chest", "A shiny treasure chest that is filled to the brim with goodies. If only you could get inside.")
                .descriptions("new", "good", "ok", "old")
                .imageNumber((short) IconConstants.ICON_CONTAINER_CHEST_LARGE)
                .modelName("model.container.chest.large.treasure.")
                .size(ItemSizes.ITEM_SIZE_LARGE)
                .weightGrams(500000)
                .dimensions(100, 100, 100)
                .decayTime(TimeConstants.DECAYTIME_NEVER)
                .value(10000)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_WOOD,
                        ItemTypes.ITEM_TYPE_NOTAKE,
                        ItemTypes.ITEM_TYPE_NOMOVE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_NOT_MISSION
                })
                .difficulty(200f)
                .material(ItemMaterials.MATERIAL_COTTON)
                .build();

        treasureHuntChestId = temp.getTemplateId();
    }

    private static void registerArcaneShard() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.crystal.shard.arcane")
                .name("arcane shard", "arcane shards", "A dull arcane crystal that seems to emit a small amount of energy. It can be transformed into other crystal by communing with the gods.")
                .imageNumber((short) IconConstants.ICON_DECO_GEM_DIAMOND)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_INDESTRUCTIBLE,
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_NOSELLBACK,
                        ItemTypes.ITEM_TYPE_DECORATION
                })
                .modelName("model.resource.crystal.")
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_NEVER)
                .material(ItemMaterials.MATERIAL_CRYSTAL)
                .value(100000)
                .weightGrams(200)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(200f)
                .isTraded(false)
                .build();

        arcaneShardId = temp.getTemplateId();
    }

    private static void registerChampionOrb() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.orb.champion")
                .name("champion orb", "champion orbs", "An ancient orb that contains the essence of champions long past. You can absorb the power within to gain increased energy as a champion(adds champion points).")
                .imageNumber((short) IconConstants.ICON_ARTIFACT_ORB_DOOM)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_INDESTRUCTIBLE,
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_NOSELLBACK
                })
                .modelName("model.artifact.orbdoom.")
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_MAGIC)
                .material(ItemMaterials.MATERIAL_CRYSTAL)
                .value(5000)
                .weightGrams(500)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(200f)
                .isTraded(false)
                .build();

        championOrbId = temp.getTemplateId();
    }

    private static void registerEssenceOfWood() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.essence.wood")
                .name("essence of wood", "essence's of wood", "An ancient orb that contains the essence of champions long past. You can absorb the power within to gain increased energy as a champion(adds champion points).")
                .imageNumber((short) IconConstants.ICON_TWIG_FARWALKER)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_INDESTRUCTIBLE,
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_NOSELLBACK
                })
                .modelName("model.resource.shaft.crude.")
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .value(1000)
                .weightGrams(250)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(20f)
                .isTraded(false)
                .build();

        essenceOfWoodId = temp.getTemplateId();
    }

    private static void registerTamingStick() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.taming.stick")
                .name("taming stick", "taming sticks", "This stick has been enchanted by nymphs to make a creature follow the wielder.")
                .imageNumber((short) IconConstants.ICON_TWIG_FARWALKER)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_INDESTRUCTIBLE,
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_NOSELLBACK
                })
                .modelName("model.resource.shaft.crude.")
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .value(10000)
                .weightGrams(250)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(40f)
                .isTraded(false)
                .build();

        tamingStickId = temp.getTemplateId();
    }

    private static void registerCreatureRenameCertificate() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.certificate.rename")
                .name("creature renaming certificate", "creature renaming certificates", "A certificate entitling the owner to rename a creature.")
                .imageNumber((short) IconConstants.ICON_SCROLL_VILLAGE)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_INDESTRUCTIBLE,
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_NOSELLBACK
                })
                .modelName("model.writ.deed.")
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_CLOTHING)
                .material(ItemMaterials.MATERIAL_PAPER)
                .value(10000)
                .weightGrams(50)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(20f)
                .isTraded(false)
                .build();

        creatureRenameCertificateId = temp.getTemplateId();
    }

    private static void registerHeraldicCertificate() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.certificate.heraldic")
                .name("heraldic certificate", "heraldic certificates", "A certificate entitling the owner to apply the colors of any kingdom to select items.")
                .imageNumber((short) IconConstants.ICON_SCROLL_VILLAGE)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_INDESTRUCTIBLE,
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_NOSELLBACK
                })
                .modelName("model.writ.deed.")
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_CLOTHING)
                .material(ItemMaterials.MATERIAL_PAPER)
                .value(10000)
                .weightGrams(50)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(80f)
                .isTraded(false)
                .build();

        heraldicCertificateId = temp.getTemplateId();
    }

    private static void registerFoodCertificate() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.certificate.food")
                .name("heraldic certificate", "heraldic certificates", "A certificate entitling the owner to receive food from a bartender forever.")
                .imageNumber((short) IconConstants.ICON_SCROLL_VILLAGE)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_INDESTRUCTIBLE,
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_NOSELLBACK
                })
                .modelName("model.writ.deed.")
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_CLOTHING)
                .material(ItemMaterials.MATERIAL_PAPER)
                .value(10000)
                .weightGrams(50)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(80f)
                .isTraded(false)
                .build();

        foodCertificateId = temp.getTemplateId();
    }

    private static void registerLabel() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.label")
                .name("label", "label", "A small piece of paper used to label items for better organization.")
                .imageNumber((short) IconConstants.ICON_SCROLL_BLANK)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_INDESTRUCTIBLE,
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_NOSELLBACK
                })
                .modelName("model.resource.sheet.")
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_CLOTHING)
                .material(ItemMaterials.MATERIAL_PAPER)
                .value(10000)
                .weightGrams(50)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(15f)
                .isTraded(false)
                .build();

        labelId = temp.getTemplateId();
    }

    private static void registerNewsletter() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.newsletter")
                .name("newsletter", "newsletters", "This is a copy of the Requiem Herald. A newspaper for the people, by the... developer.")
                .imageNumber((short) IconConstants.ICON_SCROLL_VILLAGE)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_INDESTRUCTIBLE,
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_NOSELLBACK
                })
                .modelName("model.resource.sheet.")
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_FOOD)
                .material(ItemMaterials.MATERIAL_PAPER)
                .value(100)
                .weightGrams(50)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(1f)
                .isTraded(false)
                .build();

        newsletterId = temp.getTemplateId();
    }

    private static void registerPrayerBook() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.book.prayer")
                .name("prayer book", "prayer book", "A leather bound holy book of prayers.")
                .imageNumber((short) IconConstants.ICON_TOME_MAGIC_RED)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_LEATHER,
                        ItemTypes.ITEM_TYPE_MISSION,
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_IMPROVEITEM
                })
                .modelName("model.artifact.tomemagic.red.paper.")
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_LEATHER)
                .material(ItemMaterials.MATERIAL_LEATHER)
                .value(5000)
                .primarySkill(SkillList.LEATHERWORKING)
                .weightGrams(800)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(60f)
                .isTraded(false)
                .build();

        prayerBookId = temp.getTemplateId();
    }

    private static void registerGuardToken() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.token.guard")
                .name("guard token", "guard tokens", "A token to be used on your settlement token. It will add more upkeep into your guards if it is bugged somehow so that you can hire more.")
                .imageNumber((short) IconConstants.ICON_COIN_IRON)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_NOSELLBACK
                })
                .modelName("model.coin.")
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.BASEDECAY)
                .material(ItemMaterials.MATERIAL_IRON)
                .value(10)
                .weightGrams(10)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(1f)
                .isTraded(false)
                .build();

        guardTokenId = temp.getTemplateId();
    }

    private static void registerDiseasePotion() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.potion.disease")
                .name("potion of cure disease", "potions of cure disease", "A dark brown liquid with a consistency slightly thicker than water. It will cure any disease on any creature whether it is a trait or not.")
                .imageNumber((short) IconConstants.ICON_MISC_POTION)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_MAGIC,
                        ItemTypes.ITEM_TYPE_NOSELLBACK
                })
                .modelName("model.magic.potion.")
                .dimensions(2, 2, 2)
                .decayTime(TimeConstants.BASEDECAY)
                .material(ItemMaterials.MATERIAL_GLASS)
                .value(6000)
                .weightGrams(600)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(60f)
                .isTraded(true)
                .build();

        diseasePotionId = temp.getTemplateId();
    }

    private static void registerMachinaOfFortune() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.machina.fortune")
                .name("Machina of Fortunes", "Machina of Fortunes", "A very strange machine that is of unknown origin. It gives fortune, but also misfortune.")
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_TURNABLE,
                        ItemTypes.ITEM_TYPE_USE_GROUND_ONLY,
                        ItemTypes.ITEM_TYPE_PLANTABLE,
                        ItemTypes.ITEM_TYPE_COLORABLE,
                        ItemTypes.ITEM_TYPE_OWNER_DESTROYABLE,
                        ItemTypes.ITEM_TYPE_TRANSPORTABLE,
                        ItemTypes.ITEM_TYPE_FLOATING,
                        ItemTypes.ITEM_TYPE_DECORATION
                })
                .modelName("model.ausimus.GamblingMachine.")
                .dimensions(150, 150, 300)
                .decayTime(TimeConstants.DECAYTIME_STEEL)
                .material(ItemMaterials.MATERIAL_COPPER)
                .value(5000)
                .primarySkill(SkillList.SMITHING_BLACKSMITHING)
                .weightGrams(110000)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(68f)
                .isTraded(false)
                .build();

        machinaOfFortuneId = temp.getTemplateId();
    }

    private static void registerLathe() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.lathe")
                .name("lathe", "lathes", "A tool for creating rounded wood objects from logs and shafts. Items created with this can be made faster than normal.")
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_TOOL,
                        ItemTypes.ITEM_TYPE_IMPROVEITEM,
                        ItemTypes.ITEM_TYPE_TURNABLE,
                        ItemTypes.ITEM_TYPE_TRANSPORTABLE,
                        ItemTypes.ITEM_TYPE_USE_GROUND_ONLY,
                        ItemTypes.ITEM_TYPE_PLANTABLE,
                        ItemTypes.ITEM_TYPE_ONE_PER_TILE,
                        ItemTypes.ITEM_TYPE_DECORATION
                })
                .modelName(ModelConstants.MODEL_TOOL_WOOD_LOOM)
                .dimensions(50, 50, 200)
                .decayTime(TimeConstants.BASEDECAY)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .value(5000)
                .primarySkill(SkillList.CARPENTRY_FINE)
                .weightGrams(100000)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(40f)
                .isTraded(false)
                .build();

        latheId = temp.getTemplateId();
    }

    private static void registerRockCrusher() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.crusher.rock")
                .name("rock crusher", "rock crusher", "A tool for crushing rocks and sandstone into fine dust.")
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_TOOL,
                        ItemTypes.ITEM_TYPE_IMPROVEITEM,
                        ItemTypes.ITEM_TYPE_TURNABLE,
                        ItemTypes.ITEM_TYPE_TRANSPORTABLE,
                        ItemTypes.ITEM_TYPE_USE_GROUND_ONLY,
                        ItemTypes.ITEM_TYPE_PLANTABLE,
                        ItemTypes.ITEM_TYPE_ONE_PER_TILE,
                        ItemTypes.ITEM_TYPE_DECORATION
                })
                .modelName(ModelConstants.MODEL_TOOL_WOOD_LOOM)
                .dimensions(50, 50, 200)
                .decayTime(TimeConstants.BASEDECAY)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .value(5000)
                .primarySkill(SkillList.CARPENTRY_FINE)
                .weightGrams(100000)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(40f)
                .isTraded(false)
                .build();

        rockCrusherId = temp.getTemplateId();
    }

    private static void registerBattleYoyo() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.weapon.battle.yoyo")
                .name("battle yoyo", "battle yoyo's", "A reinforced yoyo meant for combat. Designed to see whether walking the dog is an effective murder technique.")
                .imageNumber((short) IconConstants.ICON_TOY_YOYO)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_WOOD,
                        ItemTypes.ITEM_TYPE_WEAPON,
                        ItemTypes.ITEM_TYPE_WEAPON_CRUSH
                })
                .modelName("model.toy.yoyo.")
                .dimensions(15, 25, 25)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .value(1000)
                .primarySkill(SkillList.YOYO)
                .weightGrams(1000)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(40f)
                .combatDamage(25)
                .isTraded(false)
                .build();

        battleYoyoId = temp.getTemplateId();
    }

    private static void registerClub() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.weapon.club")
                .name("club", "club", "A huge wooden club. It would be great for bashing.")
                .imageNumber((short) IconConstants.ICON_WEAPON_CLUB_HUGE)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_WOOD,
                        ItemTypes.ITEM_TYPE_WEAPON,
                        ItemTypes.ITEM_TYPE_WEAPON_CRUSH
                })
                .modelName("model.weapon.club.huge.")
                .dimensions(5, 10, 80)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .value(1000)
                .primarySkill(SkillList.CLUB_HUGE)
                .weightGrams(8000)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(40f)
                .combatDamage(35)
                .isTraded(false)
                .build();

        clubId = temp.getTemplateId();
    }

    private static void registerCombatScythe() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.weapon.combat.scythe")
                .name("combat scythe", "combat scythe", "A one-handed scythe designed for combat.")
                .imageNumber((short) IconConstants.ICON_WEAPON_SCYTHE)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_METAL,
                        ItemTypes.ITEM_TYPE_WEAPON,
                        ItemTypes.ITEM_TYPE_WEAPON_SLASH
                })
                .modelName(ModelConstants.MODEL_WEAPON_SCYTHE)
                .dimensions(5, 10, 80)
                .decayTime(TimeConstants.DECAYTIME_STEEL)
                .material(ItemMaterials.MATERIAL_IRON)
                .value(1000)
                .primarySkill(SkillList.SCYTHE)
                .weightGrams(1500)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(45f)
                .combatDamage(33)
                .isTraded(false)
                .build();

        combatScytheId = temp.getTemplateId();
    }

    private static void registerGodSlayer() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.weapon.gm.godSlayer")
                .name("God Slayer", "God Slayer", "A massive battleaxe imbued with powerful magics. This weapon has the capacity to strike down any foe mortal or not.")
                .imageNumber((short) IconConstants.ICON_WEAPON_AXE_DUAL)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_METAL,
                        ItemTypes.ITEM_TYPE_WEAPON,
                        ItemTypes.ITEM_TYPE_WEAPON_SLASH
                })
                .modelName(ModelConstants.MODEL_WEAPON_AXE_LARGE)
                .dimensions(6, 15, 90)
                .decayTime(TimeConstants.DECAYTIME_NEVER)
                .material(ItemMaterials.MATERIAL_SERYLL)
                .value(1000000)
                .primarySkill(SkillList.AXE_HUGE)
                .weightGrams(4500)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(200f)
                .combatDamage(999)
                .isTraded(false)
                .build();

        godSlayerId = temp.getTemplateId();
    }

    private static void registerKnuckles() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.weapon.knuckles")
                .name("knuckles", "knuckles", "A classic weapon used in hand-to-hand combat.")
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_METAL,
                        ItemTypes.ITEM_TYPE_WEAPON,
                        ItemTypes.ITEM_TYPE_WEAPON_CRUSH
                })
                .modelName("model.decoration.ring.rift.2.")
                .dimensions(1, 5, 5)
                .decayTime(TimeConstants.DECAYTIME_STEEL)
                .material(ItemMaterials.MATERIAL_BRASS)
                .value(1000)
                .primarySkill(SkillList.WEAPONLESS_FIGHTING)
                .weightGrams(800)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(35f)
                .combatDamage(42)
                .isTraded(false)
                .build();

        knucklesId = temp.getTemplateId();
    }

    private static void registerWarhammer() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.weapon.warhammer")
                .name("warhammer", "warhammer", "A classic weapon used in hand-to-hand combat.")
                .imageNumber((short) IconConstants.ICON_ARTIFACT_HAMMER_MAGRANON)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_METAL,
                        ItemTypes.ITEM_TYPE_WEAPON,
                        ItemTypes.ITEM_TYPE_WEAPON_CRUSH
                })
                .size(ItemSizes.ITEM_SIZE_LARGE)
                .modelName("model.artifact.hammerhuge.")
                .dimensions(5, 10, 80)
                .decayTime(TimeConstants.DECAYTIME_STEEL)
                .material(ItemMaterials.MATERIAL_IRON)
                .value(1000)
                .primarySkill(SkillList.WARHAMMER)
                .weightGrams(7000)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(35f)
                .combatDamage(50)
                .isTraded(false)
                .build();

        warhammerId = temp.getTemplateId();
    }

    private static void registerWarhammerHead() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.weapon.head.warhammer")
                .name("warhammer head", "warhammer head", "A warhammer head.")
                .imageNumber((short) IconConstants.ICON_WEAPON_HAMMER_LARGE_HEAD)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_METAL
                })
                .modelName("model.weapon.head.large.maul.")
                .dimensions(5, 10, 10)
                .decayTime(TimeConstants.DECAYTIME_STEEL)
                .material(ItemMaterials.MATERIAL_IRON)
                .value(1000)
                .primarySkill(SkillList.WARHAMMER)
                .weightGrams(5500)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(40f)
                .isTraded(false)
                .build();

        warhammerHeadId = temp.getTemplateId();
    }

    private static void registerAffinityCatcher() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.affinity.catcher")
                .name("affinity catcher", "affinity catcher", "A valuable orb that can transfer knowledge from one person to another.")
                .imageNumber((short) IconConstants.ICON_ARTIFACT_EYE_VYNORA)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_MAGIC,
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_HASDATA,
                        ItemTypes.ITEM_TYPE_NOSELLBACK,
                        ItemTypes.ITEM_TYPE_ALWAYS_BANKABLE
                })
                .modelName("model.artifact.orbdoom.")
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_MAGIC)
                .material(ItemMaterials.MATERIAL_CRYSTAL)
                .value(100000)
                .weightGrams(500)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(200f)
                .isTraded(true)
                .build();

        affinityCatcherId = temp.getTemplateId();
    }

    private static void registerAffinityOrb() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.affinity.orb")
                .name("affinity orb", "affinity orb", "A valuable orb that infuses the user with hidden knowledge.")
                .imageNumber((short) IconConstants.ICON_ARTIFACT_EYE_VYNORA)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_MAGIC,
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_HASDATA,
                        ItemTypes.ITEM_TYPE_NOSELLBACK,
                        ItemTypes.ITEM_TYPE_ALWAYS_BANKABLE
                })
                .modelName("model.artifact.orbdoom.")
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_MAGIC)
                .material(ItemMaterials.MATERIAL_CRYSTAL)
                .value(500000)
                .weightGrams(500)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(200f)
                .isTraded(true)
                .build();

        affinityOrbId = temp.getTemplateId();
    }

    private static void registerArrowPackHunting() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.pack.arrow.hunting")
                .name("hunting arrow pack", "hunting arrow packs", "A pack of hunting arrows, able to be unpacked into a full quiver.")
                .imageNumber((short) IconConstants.ICON_CONTAINER_QUIVER)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_WOOD,
                        ItemTypes.ITEM_TYPE_WEAPON
                })
                .modelName("model.container.quiver.")
                .dimensions(15, 15, 50)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_LEATHER)
                .value(3500)
                .weightGrams(4500)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(55f)
                .isTraded(true)
                .build();

        arrowPackHuntingId = temp.getTemplateId();
    }

    private static void registerArrowPackWar() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.pack.arrow.war")
                .name("war arrow pack", "war arrow packs", "A pack of war arrows, able to be unpacked into a full quiver.")
                .imageNumber((short) IconConstants.ICON_CONTAINER_QUIVER)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_WOOD,
                        ItemTypes.ITEM_TYPE_WEAPON
                })
                .modelName("model.container.quiver.")
                .dimensions(15, 15, 50)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_LEATHER)
                .value(3500)
                .weightGrams(4500)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(55f)
                .isTraded(true)
                .build();

        arrowPackWarId = temp.getTemplateId();
    }

    private static void registerBookOfConversion() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.book.conversion")
                .name("book of conversion", "books of conversion", "A book used to convert religion. This comes at a slight faith loss.")
                .imageNumber((short) IconConstants.ICON_TOME_MAGIC_BLACK)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_MAGIC,
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_NOSELLBACK,
                        ItemTypes.ITEM_TYPE_ALWAYS_BANKABLE
                })
                .modelName("model.artifact.tomemagic.black.paper.")
                .dimensions(1,1,1)
                .decayTime(TimeConstants.DECAYTIME_VALUABLE)
                .material(ItemMaterials.MATERIAL_PAPER)
                .value(50000)
                .weightGrams(500)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(200f)
                .isTraded(true)
                .build();

        bookOfConversionId = temp.getTemplateId();
    }

    private static void registerChaosCrystal() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.crystal.chaos")
                .name("chaos crystal", "chaos crystals", "This volatile crystal will either enhance an item, or destroy it outright.")
                .imageNumber((short) IconConstants.ICON_FRAGMENT_STONE)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_MAGIC,
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_NOSELLBACK,
                        ItemTypes.ITEM_TYPE_ALWAYS_BANKABLE
                })
                .modelName("model.valrei.magic.")
                .dimensions(1,1,1)
                .decayTime(TimeConstants.DECAYTIME_MAGIC)
                .material(ItemMaterials.MATERIAL_CRYSTAL)
                .value(5000)
                .weightGrams(250)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(200f)
                .isTraded(true)
                .build();

        chaosCrystalId = temp.getTemplateId();
    }

    private static void registerDepthDrill() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.depth.drill")
                .name("depth drill", "depth drills", "A tool for determining dirt depth.")
                .imageNumber((short) IconConstants.ICON_TOOL_DIOPTRA)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_TOOL,
                        ItemTypes.ITEM_TYPE_WEAPON_PIERCE
                })
                .modelName("model.valrei.magic.")
                .dimensions(6, 6, 96)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .primarySkill(SkillList.CARPENTRY_FINE)
                .material(ItemMaterials.MATERIAL_IRON)
                .value(2400)
                .weightGrams(1400)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(30f)
                .isTraded(true)
                .build();

        depthDrillId = temp.getTemplateId();
    }

    private static void registerDisintegrationRod() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.rod.disintegration")
                .name("Rod of Disintegration", "Rods of Disintegration", "A steel rod that can magically mine ore veins and cave walls. It will remove xx worth of mining actions.")
                .imageNumber((short) IconConstants.ICON_ARTIFACT_RODBEGUILING)
                .itemTypes(new short[]{
                        ITEM_TYPE_FULLPRICE,
                        ITEM_TYPE_NOSELLBACK,
                        ITEM_TYPE_ALWAYS_BANKABLE
                })
                .modelName("model.tool.rodtrans.")
                .dimensions(1,1,1)
                .decayTime(TimeConstants.DECAYTIME_NEVER)
                .material(ItemMaterials.MATERIAL_STEEL)
                .value(20000)
                .weightGrams(1000)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(200f)
                .isTraded(true)
                .build();

        disintegrationRodId = temp.getTemplateId();
    }

    private static void registerEnchantersCrystal() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.crystal.enchanters")
                .name("enchanters crystal", "enchanters crystal", "This crystal can manipulate the magical properties of an item.")
                .imageNumber((short) IconConstants.ICON_ARTIFACT_RODBEGUILING)
                .itemTypes(new short[]{
                        ITEM_TYPE_FULLPRICE,
                        ITEM_TYPE_NOSELLBACK,
                        ITEM_TYPE_ALWAYS_BANKABLE
                })
                .modelName("model.tool.rodtrans.")
                .dimensions(1,1,1)
                .decayTime(TimeConstants.DECAYTIME_NEVER)
                .material(ItemMaterials.MATERIAL_STEEL)
                .value(20000)
                .weightGrams(1000)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(200f)
                .isTraded(true)
                .build();

        enchantersCrystalId = temp.getTemplateId();
    }

    private static void registerEnchantOrb() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.rod.orb.enchant")
                .name("enchant orb", "enchant orbs", "It shimmers lightly, the magic inside waiting for a proper vessel.")
                .imageNumber((short) IconConstants.ICON_ARTIFACT_ORB_DOOM)
                .itemTypes(new short[]{
                        ITEM_TYPE_MAGIC,
                        ITEM_TYPE_INDESTRUCTIBLE
                })
                .modelName("model.artifact.orbdoom.")
                .dimensions(1,1,1)
                .decayTime(TimeConstants.DECAYTIME_MAGIC)
                .material(ItemMaterials.MATERIAL_CRYSTAL)
                .value(50000)
                .weightGrams(500)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(200f)
                .isTraded(true)
                .build();

        enchantOrbId = temp.getTemplateId();
    }

    private static void registerEternalOrb() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.rod.orb.eternal")
                .name("eternal orb", "eternal orbs", "Legends say it consumes magic from an object, and moves it to another.")
                .imageNumber((short) IconConstants.ICON_ARTIFACT_ORB_DOOM)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_NOSELLBACK,
                        ItemTypes.ITEM_TYPE_ALWAYS_BANKABLE
                })
                .modelName("model.artifact.orbdoom.")
                .dimensions(1,1,1)
                .decayTime(TimeConstants.DECAYTIME_MAGIC)
                .material(ItemMaterials.MATERIAL_CRYSTAL)
                .value(200000)
                .weightGrams(500)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(200f)
                .isTraded(true)
                .build();

        eternalOrbId = temp.getTemplateId();
    }

    private static void registerFriyanTablet() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.tablet.friyan")
                .name("Tablet of Friyan", "Tablets of Friyan", "Once a great scholar and sorceress, Friyan's faith had reached the zenith. While in this world, she wrote her knowledge in tablets like these. Perhaps you may learn more of the gods from it...")
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_STONE,
                        ItemTypes.ITEM_TYPE_OUTSIDE_ONLY,
                        ItemTypes.ITEM_TYPE_NOTAKE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_INDESTRUCTIBLE,
                        ItemTypes.ITEM_TYPE_USE_GROUND_ONLY,
                        ItemTypes.ITEM_TYPE_NOMOVE,
                        ItemTypes.ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION,
                        ItemTypes.ITEM_TYPE_NOT_MISSION
                })
                .modelName("model.structure.portal.10.")
                .dimensions(500, 500, 1000)
                .decayTime(TimeConstants.DECAYTIME_MAGIC)
                .material(ItemMaterials.MATERIAL_STONE)
                .value(10000)
                .weightGrams(2000000)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(200f)
                .isTraded(false)
                .build();

        friyanTabletId = temp.getTemplateId();
    }

    private static void registerKeyFragment() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.fragment.key")
                .name("key fragment [1/50]", "key fragments", "A fragment of the fabled Key of the Heavens.")
                .imageNumber((short) IconConstants.ICON_ARTIFACT_VALREI)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_MAGIC,
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_NOSELLBACK,
                        ItemTypes.ITEM_TYPE_SERVERBOUND,
                        ItemTypes.ITEM_TYPE_ARTIFACT
                })
                .modelName("model.decoration.gem.resurrectionstone.")
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_NEVER)
                .material(ItemMaterials.MATERIAL_CRYSTAL)
                .value(5000)
                .weightGrams(250)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(200f)
                .isTraded(false)
                .build();

        keyFragmentId = temp.getTemplateId();
    }

    private static void registerRequiemDepotCache() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.cache.depot")
                .name("Requiem cache", "Requiem caches", "A cache of goods from a supply depot, waiting to be opened. What could be inside?")
                .imageNumber((short) IconConstants.ICON_CONTAINER_GIFT)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_MAGIC,
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_NOSELLBACK,
                        ItemTypes.ITEM_TYPE_ALWAYS_BANKABLE
                })
                .modelName("model.container.giftbox.")
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .material(ItemMaterials.MATERIAL_GOLD)
                .value(10000)
                .weightGrams(500)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(200f)
                .isTraded(false)
                .build();

        requiemDepotCacheId = temp.getTemplateId();
    }

    private static void registerSupplyDepot() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.depot")
                .name("Requiem depot", "Requiem depots", "Contains a valuable cache of treasures.")
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_WOOD,
                        ItemTypes.ITEM_TYPE_NOTAKE,
                        ItemTypes.ITEM_TYPE_LOCKABLE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_ONE_PER_TILE,
                        ItemTypes.ITEM_TYPE_OWNER_TURNABLE,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_MISSION,
                        ItemTypes.ITEM_TYPE_PLANTABLE
                })
                .modelName(ModelConstants.MODEL_SUPPLYDEPOT2)
                .dimensions(1000, 1000, 1000)
                .decayTime(TimeConstants.DECAYTIME_NEVER)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .value(5000)
                .weightGrams(2000000)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(200f)
                .isTraded(false)
                .build();

        requiemDepotId = temp.getTemplateId();
    }

    private static void registerSealedMap() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.map.sealed")
                .name("sealed map", "sealed maps", "A sealed treasure map, waiting to be opened.")
                .imageNumber((short) IconConstants.ICON_CLOTH_BOLT)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_MAGIC,
                        ItemTypes.ITEM_TYPE_NOSELLBACK,
                        ItemTypes.ITEM_TYPE_FULLPRICE, // Not actually full price, the hook for value works before this is applied. This ensures coins are used.
                        ItemTypes.ITEM_TYPE_ALWAYS_BANKABLE
                })
                .modelName("model.container.giftbox.")
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_NEVER)
                .material(ItemMaterials.MATERIAL_MAGIC)
                .value(2000000)
                .weightGrams(500)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(200f)
                .isTraded(false)
                .build();

        sealedMapId = temp.getTemplateId();
    }

    private static void registerSkeletonDecoration() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.skeleton")
                .name("skeleton", "skeleton", "A skeleton.")
                .imageNumber((short) IconConstants.ICON_CORPSE_HUMAN)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_OWNER_DESTROYABLE,
                        ItemTypes.ITEM_TYPE_DESTROYABLE,
                        ItemTypes.ITEM_TYPE_TURNABLE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_NOT_MISSION,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_COLORABLE
                })
                .modelName("model.corpse.human.butchered.")
                .dimensions(20, 50, 200)
                .decayTime(TimeConstants.DECAYTIME_STONE)
                .material(ItemMaterials.MATERIAL_FLESH)
                .value(1000)
                .weightGrams(30000)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(200f)
                .isTraded(false)
                .build();

        skeletonDecorationId = temp.getTemplateId();
    }

    private static void registerSorceryFragment() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.fragment.sorcery")
                .name("sorcery fragment [1/10]", "sorcery fragments", "A scrap of a tome.")
                .imageNumber((short) IconConstants.ICON_SCROLL_BINDING)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_MAGIC,
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_NOSELLBACK,
                        ItemTypes.ITEM_TYPE_SERVERBOUND,
                        ItemTypes.ITEM_TYPE_ARTIFACT
                })
                .modelName("model.artifact.scrollbind.paper.")
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_NEVER)
                .material(ItemMaterials.MATERIAL_PAPER)
                .value(5000)
                .weightGrams(150)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(200f)
                .isTraded(false)
                .build();

        sorceryFragmentId = temp.getTemplateId();
    }

    private static void registerTitaniumLump() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.lump.titanium")
                .name("lump, titanium", "sorcery fragments", "A lightweight lump of glistening titanium.")
                .imageNumber((short) IconConstants.ICON_METAL_GLIMMERSTEEL_LUMP)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_METAL,
                        ItemTypes.ITEM_TYPE_BULK,
                        ItemTypes.ITEM_TYPE_COMBINE
                })
                .modelName("model.resource.lump.")
                .dimensions(3, 3, 3)
                .decayTime(TimeConstants.DECAYTIME_NEVER)
                .material(ItemMaterials.MATERIAL_UNDEFINED)
                .value(200)
                .weightGrams(400)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(40f)
                .isTraded(false)
                .build();

        titaniumLumpId = temp.getTemplateId();
    }

    private static void registerTitaniumSocket() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.socket.titanium")
                .name("titanium socket", "titanium sockets", "A socket for a gem, designed for insertion into an item.")
                .imageNumber((short) IconConstants.ICON_DECO_ARMRING_RIFT1)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_METAL
                })
                .modelName("model.resource.lump.")
                .dimensions(3, 3, 3)
                .decayTime(TimeConstants.DECAYTIME_VALUABLE)
                .material(ItemMaterials.MATERIAL_UNDEFINED)
                .value(200)
                .weightGrams(400)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(40f)
                .isTraded(false)
                .build();

        titaniumSocketId = temp.getTemplateId();
    }

    private static void registerTreasureBox() throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder("jubaroo.item.treasure.box")
                .name("treasure box", "treasure box", "A box of treasure, waiting to be opened. What could be inside?")
                .imageNumber((short) IconConstants.ICON_CONTAINER_GIFT)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_MAGIC,
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_NOSELLBACK,
                        ItemTypes.ITEM_TYPE_ALWAYS_BANKABLE
                })
                .modelName("model.container.giftbox.")
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_VALUABLE)
                .material(ItemMaterials.MATERIAL_GOLD)
                .value(10000)
                .weightGrams(500)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(200f)
                .isTraded(false)
                .build();

        treasureBoxId = temp.getTemplateId();
    }

    private static ItemTemplate registerScroll(String id, String name, String description, float difficulty) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.scroll.%s.", id))
                .name(String.format("scroll of %s", name), String.format("scrolls of %s", name), description)
                .imageNumber((short) IconConstants.ICON_SCROLL_VILLAGE)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_INDESTRUCTIBLE,
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_NOSELLBACK
                })
                .modelName("model.resource.sheet.")
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_CLOTHING)
                .material(ItemMaterials.MATERIAL_PAPER)
                .value(2500)
                .weightGrams(50)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(difficulty)
                .isTraded(false)
                .build();
    }

    private static void registerScrolls() throws IOException {
        scrollOfBankSlots = registerScroll("bank.slots", "bank slots", "A scroll with glyphs that appear and disappear randomly on the papyrus. This scroll will give you more room to store things in your personal bank.", 200f);
        scrollOfGearBinding = registerScroll("binding.gear", "gear binding", "A scroll with glyphs that appear and disappear randomly on the papyrus. This scroll will bind the equipment to you until it is destroyed.", 60f);
        scrollOfSummoningHorse = registerScroll("horse", "summoning (horse)", "A scroll with glyphs that appear and disappear randomly on the papyrus. This scroll will summon a random colored horse with no traits or name.", 200f);
        scrollOfTitles = registerScroll("titles", "titles", "A scroll with glyphs that appear and disappear randomly on the papyrus. This scroll will give you a specific title when read.", 200f);
        scrollOfTownPortal = registerScroll("portal.town", "town portal", "A scroll with glyphs that appear and disappear randomly on the papyrus. This scroll will take you home when used. It has infinite uses.", 200f);
        scrollOfVillageCreation = registerScroll("village.create", "village creation knowledge", "A scroll with glyphs that appear and disappear randomly on the papyrus. This scroll will grant a village creation bonus for a limited time.", 200f);
        scrollOfVillageHeal = registerScroll("village.heal", "village healing knowledge", "A scroll with glyphs that appear and disappear randomly on the papyrus. This scroll will grant a village healing bonus for a limited time.", 200f);
        scrollOfVillageWar = registerScroll("village.war", "village war knowledge", "A scroll with glyphs that appear and disappear randomly on the papyrus. This scroll will grant a village war bonus for a limited time.", 200f);
    }

    private static ItemTemplate registerPortal(String id, String name, String description, String modelSuffix) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.portal.%s.", id))
                .name(name, name, description)
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_NOTAKE,
                        ItemTypes.ITEM_TYPE_OWNER_DESTROYABLE,
                        ItemTypes.ITEM_TYPE_STONE,
                        ItemTypes.ITEM_TYPE_TURNABLE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_DESTROYABLE,
                        ItemTypes.ITEM_TYPE_COLORABLE,
                        ItemTypes.ITEM_TYPE_TRANSPORTABLE,
                        ItemTypes.ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION,
                        ItemTypes.ITEM_TYPE_HASDATA,
                })
                .modelName(String.format("model.decoration.statue.%s.", modelSuffix))
                .dimensions(20, 30, 160)
                .decayTime(TimeConstants.DECAYTIME_STONE)
                .material(ItemMaterials.MATERIAL_STONE)
                .value(2500)
                .weightGrams(200000)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(200f)
                .isTraded(false)
                .build();
    }

    private static void registerPortals() throws IOException {
        demonHomePortal = registerPortal("home.demon", "demon home portal", "A portal statue that takes you home. It is in the shape of a demon.", "demon");
        demonPortal = registerPortal("demon", "demon portal", "A portal statue. It is in the shape of a demon.", "demon");
        nymphHomePortal = registerPortal("home.nymph", "nymph home portal", "A portal statue that takes you home. It is in the shape of a nymph", "nymph");
        nymphPortal = registerPortal("nymph", "nymph portal", "A portal statue. It is in the shape of a nymph.", "nymph");
    }

    private static ItemTemplate registerElementalCrystal(String id, String name, String description, short icon, boolean fragments) throws IOException {
        ItemTemplate temp = new ItemTemplateBuilder(String.format("jubaroo.item.crystal.%s.", id))
                .name(name, name, description)
                .imageNumber(icon)
                .modelName("model.resource.crystal.")
                .weightGrams(200)
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_NEVER)
                .value(100000)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_INDESTRUCTIBLE,
                        ItemTypes.ITEM_TYPE_LIGHT,
                        ItemTypes.ITEM_TYPE_ALWAYS_LIT,
                        ItemTypes.ITEM_TYPE_INSULATED,
                        ItemTypes.ITEM_TYPE_DECORATION
                })
                .difficulty(200f)
                .material(ItemMaterials.MATERIAL_CRYSTAL)
                .build();

        if (fragments) {
            if (id.equals("lesser.fire")) {
                temp.setFragmentAmount(5);
            }
        }
        return temp;
    }

    private static void registerElementalCrystals() throws IOException {
        deathCrystal = registerElementalCrystal("death", "crystal of death and decay", "A dark and sinister crystal. It emits an aura of death and decay.", (short) IconConstants.ICON_ARTIFACT_SCAIL_LIBILA, false);
        fireCrystal = registerElementalCrystal("fire", "crystal of eternal flame", "A glowing red crystal. It is on fire, yet you are able to hold it in your hand. It emits an aura of burning flames.", (short) IconConstants.ICON_ARTIFACT_ORB_DOOM, false);
        frostCrystal = registerElementalCrystal("frost", "crystal of permafrost", "A shiny blue crystal. It is extremely cold in your hand. It emits an aura of wintry chill.", (short) IconConstants.ICON_ARTIFACT_EAR_VYNORA, false);
        lifeCrystal = registerElementalCrystal("life", "crystal of life", "A glimmering green crystal. It emits a warm and calming aura.", (short) IconConstants.ICON_ARTIFACT_CHARM_FO, false);
        lesserFireCrystal = registerElementalCrystal("lesser.fire", "lesser crystal of eternal flame", "A glowing red crystal. It is on fire, yet you are able to hold it in your hand. It emits an aura of burning flames. It will be destroyed when used.", (short) IconConstants.ICON_ARTIFACT_ORB_DOOM, true);
    }

    private static ItemTemplate registerToolBackpack(String id, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.%s.", id))
                .name(String.format("backpack of %s", name), String.format("backpacks of %s", name), "A backpack intended for a purchase of special tools.")
                .descriptions("strong", "well-made", "ok", "fragile")
                .imageNumber((short) IconConstants.ICON_CONTAINER_BACKPACK)
                .modelName("model.container.backpack.")
                .weightGrams(2000)
                .dimensions(30, 30, 50)
                .decayTime(TimeConstants.DECAYTIME_CLOTHING)
                .value(2500)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_LEATHER,
                        ItemTypes.ITEM_TYPE_HOLLOW
                })
                .difficulty(200f)
                .material(ItemMaterials.MATERIAL_LEATHER)
                .build();
    }

    private static void registerToolBackpacks() throws IOException {
        addyToolsBackpack = registerToolBackpack("addy.tools", "addy tools");
        steelToolsBackpack = registerToolBackpack("sale.steel.tools", "steel tools");
    }

    private static ItemTemplate registerGemFragment(String name, short icon) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.gem.fragment.%s.", name))
                .name(String.format("%s fragment ", name), String.format("%s fragments ", name), "A fragment of a gem that can be used to create a complete gem.")
                .imageNumber(icon)
                .modelName("model.resource.rock.")
                .weightGrams(200)
                .dimensions(1, 1, 1)
                .decayTime(TimeConstants.DECAYTIME_VALUABLE)
                .value(2500)
                .primarySkill(11005)
                .behaviourType(BehaviourList.itemBehaviour)
                .itemTypes(new short[0])
                .difficulty(70f)
                .material(ItemMaterials.MATERIAL_CRYSTAL)
                .build();
    }

    private static void registerGemFragments() throws IOException {
        diamondFragment = registerGemFragment("diamond", (short) IconConstants.ICON_DECO_GEM_DIAMOND);
        emeraldFragment = registerGemFragment("emerald",(short) IconConstants.ICON_DECO_GEM_EMERALD);
        opalFragment = registerGemFragment("opal",(short) IconConstants.ICON_DECO_GEM_OPAL);
        rubyFragment = registerGemFragment("ruby",(short) IconConstants.ICON_DECO_GEM_RUBY);
        sapphireFragment = registerGemFragment("sapphire",(short) IconConstants.ICON_DECO_GEM_SAPPHIRE);
    }

    private static ItemTemplate registerTabard(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.tabard.%s.", id))
                .name(String.format("%s tabard", name), String.format("%ss", name), "A tabard that is worn to show off which kingdom you belong to.")
                .modelName(String.format("model.clothing.tabard.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_ARMOR_TABARD_CLOTH)
                .itemTypes(new short[]{
                        ITEM_TYPE_CLOTH,
                        ITEM_TYPE_REPAIRABLE
                })
                .decayTime(TimeConstants.DECAYTIME_CLOTHING)
                .dimensions(30, 30, 5)
                .weightGrams(300)
                .value(100)
                .material(ItemMaterials.MATERIAL_COTTON)
                .behaviourType(BehaviourList.itemBehaviour)
                .primarySkill(SkillList.CLOTHTAILORING)
                .bodySpaces(new byte[]{BodyTemplate.tabardSlot})
                .difficulty(25f)
                .combatDamage(0)
                .isTraded(true)
                .build();
    }

    private static void registerTabards() throws IOException {
        jennKellonTabard = registerTabard("jennKellon", "jenn", "Jenn-Kellon");
        jennKellonTwoTabard = registerTabard("jennKellonTwo", "zjen", "Dragon Empire");
        dreadnoughtTabard = registerTabard("dreadnought", "drea", "Dreadnought");
        pandemoniumTabard = registerTabard("pandemonium", "pand", "Pandemonium");
        crusadersTabard = registerTabard("crusaders", "thec", "Crusaders");
        empireMolRehanTabard = registerTabard("empireMolRehan", "empi", "Empire of Mol-Rehan");
        hotsTabard = registerTabard("hots", "hots", "Horde of the Summoned");
        descendantsValhallaTabard = registerTabard("descendantsValhalla", "yval", "Descendants of Valhalla");
        blackLegionTabard = registerTabard("blackLegion", "blac", "Black Legion");
        abralonTabard = registerTabard("abralon", "abra", "Abralon");
        molRehanTabard = registerTabard("molRehan", "molr", "Mol-Rehan");
        romanRepublicTabard = registerTabard("romanRepublic", "ther", "The Roman Republic");
        kosTabard = registerTabard("kos", "kos", "Kingdom of Sol");
        valhallaTabard = registerTabard("valhalla", "valh", "Valhalla");
        macedonTabard = registerTabard("macedonia", "mace", "Macedonia");
        universityWurmTabard = registerTabard("universityWurm", "wurm", "University of Wurm");
        requiemTabard = registerTabard("requiem", "requ", "Requiem of Wurm");
        phoenixTabard = registerTabard("phoenix", "phoe", "Phoenix");
        foreverlandsTabard = registerTabard("foreverlands", "fore", "Foreverlands");
        woodlandCamouflageTabard = registerTabard("woodlandCamouflage", "woodlandcamo", "woodland camouflage");
    }

    private static ItemTemplate registerMilitaryTent(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.militaryTent.%s.", id))
                .name(String.format("%s military tent", name), String.format("%ss", name), "This is the standard tent for military actions.")
                .modelName(String.format("model.structure.tent.military.cotton.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_CLOTH_BOLT)
                .itemTypes(new short[]{
                        ITEM_TYPE_CLOTH,
                        ITEM_TYPE_HOLLOW,
                        ITEM_TYPE_LOCKABLE,
                        ITEM_TYPE_TENT,
                        ITEM_TYPE_ONE_PER_TILE,
                        ITEM_TYPE_VEHICLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_TILE_ALIGNED,
                        ITEM_TYPE_USES_SPECIFIED_CONTAINER_VOLUME
                })
                .decayTime(TimeConstants.DECAYTIME_CLOTHING)
                .dimensions(5, 5, 50)
                .weightGrams(3500)
                .value(100)
                .material(ItemMaterials.MATERIAL_COTTON)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(10f)
                .combatDamage(0)
                .isTraded(true)
                .build();
    }

    private static void registerMilitaryTents() throws IOException {
        empireMolRehanMilitaryTent = registerMilitaryTent("molRehan.empire", "empi", "Empire of Mol-Rehan");
        crusadersMilitaryTent = registerMilitaryTent("crusaders", "thec", "Crusaders");
        pandemoniumMilitaryTent = registerMilitaryTent("pandemonium", "pand", "Pandemonium");
        dreadnoughtMilitaryTent = registerMilitaryTent("dreadnought", "drea", "Dreadnought");
        hotsMilitaryTent = registerMilitaryTent("hots", "hots", "Horde of the Summoned");
        jennKellonMilitaryTent = registerMilitaryTent("jennKellon", "jenn", "Jenn-Kellon");
        molRehanMilitaryTent = registerMilitaryTent("molRehan", "molr", "Mol-Rehan");
        woodlandCamoMilitaryTent = registerMilitaryTent("camouflage.woodland", "woodlandcamo", "woodland camouflage");
    }

    private static ItemTemplate registerPavilion(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.pavilion.%s.", id))
                .name(String.format("%s pavilion", name), String.format("%ss", name), "An open air tent designed for various kinds of gatherings.")
                .modelName(String.format("model.structure.tent.pavilion.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_CLOTH_BOLT)
                .itemTypes(new short[]{
                        ITEM_TYPE_CLOTH,
                        ITEM_TYPE_HOLLOW,
                        ITEM_TYPE_LOCKABLE,
                        ITEM_TYPE_ONE_PER_TILE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_TILE_ALIGNED,
                        ITEM_TYPE_USES_SPECIFIED_CONTAINER_VOLUME,
                        ITEM_TYPE_USEMATERIAL_AND_KINGDOM
                })
                .decayTime(TimeConstants.DECAYTIME_CLOTHING)
                .dimensions(5, 5, 50)
                .weightGrams(2500)
                .value(100)
                .material(ItemMaterials.MATERIAL_COTTON)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(10f)
                .combatDamage(0)
                .isTraded(true)
                .build();
    }

    private static void registerPavilions() throws IOException {
        hotsPavilion = registerPavilion("hots", "hots", "Horde of the Summoned");
        jennKellonPavilion = registerPavilion("jennKellon", "jenn", "Jenn-Kellon");
        molRehanPavilion = registerPavilion("molRehan", "molr", "Mol-Rehan");
    }

    private static ItemTemplate registerFlag(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.flag.%s.", id))
                .name(String.format("%s flag", name), String.format("%ss", name), "Apart from being a symbol of your allegiance and territorial demands, this is also a good indication of where the wind is blowing.")
                .modelName(String.format("model.decoration.flag.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_CLOTH_BOLT)
                .itemTypes(new short[]{
                        ITEM_TYPE_CLOTH,
                        ITEM_TYPE_COLORABLE,
                        ITEM_TYPE_WIND,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_ONE_PER_TILE,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_IMPROVEITEM,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_IMPROVE_USES_TYPE_AS_MATERIAL,
                        ITEM_TYPE_PLANTABLE
                })
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .dimensions(5, 5, 205)
                .weightGrams(2500)
                .value(300)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(40f)
                .combatDamage(0)
                .isTraded(false)
                .build();
    }

    private static void registerFlags() throws IOException {
        blackLegionFlag = registerFlag("blackLegion", "blac", "Black Legion");
        crusadersFlag = registerFlag("crusaders", "thec", "Crusaders");
        ebonauraFlag = registerFlag("ebonaura", "ebon", "Ebonaura");
        empireMolRehanFlag = registerFlag("empireMolRehan", "empi", "Empire of Mol-Rehan");
        jennKellonTwoFlag = registerFlag("jennKellonTwo", "zjen", "Dragon Empire");
        kosFlag = registerFlag("kos", "kos", "Kingdom of Sol");
        macedoniaFlag = registerFlag("macedonia", "mace", "Macedonia");
        pandemoniumFlag = registerFlag("pandemonium", "pand", "Pandemonium");
        valhallaFlag = registerFlag("valhalla", "valh", "Valhalla");
        molRehanFlag = registerFlag("molRehan", "molr", "Mol-Rehan");
        hotsFlag = registerFlag("hots", "hots", "Horde of the Summoned");
        jennKellonFlag = registerFlag("jennKellon", "jenn", "Jenn-Kellon");
        eagleFlag = registerFlag("eagle", "eagle", "Eagle");
        frostHavenFlag = registerFlag("frostHaven", "frosthaven", "Frost Haven");
        woodlandCamouflageFlag = registerFlag("woodlandCamouflage", "woodlandcamo", "woodland camouflage");
    }

    private static ItemTemplate registerTallBanner(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.banner.tall.%s.", id))
                .name(String.format("tall %s banner", name), String.format("%ss", name), "An elegant symbol of allegiance and faith.")
                .modelName(String.format("model.decoration.tallbanner.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_CLOTH_BOLT)
                .itemTypes(new short[]{
                        ITEM_TYPE_CLOTH,
                        ITEM_TYPE_COLORABLE,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_FOUR_PER_TILE,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_IMPROVEITEM,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_IMPROVE_USES_TYPE_AS_MATERIAL,
                        ITEM_TYPE_PLANTABLE
                })
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .dimensions(5, 5, 600)
                .weightGrams(5500)
                .value(10000)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(50f)
                .combatDamage(0)
                .isTraded(true)
                .build();
    }

    private static void registerTallBanners() throws IOException {
        molRehanTallBanner = registerTallBanner("molRehan", "molr", "Mol-Rehan");
        hotsTallBanner = registerTallBanner("hots", "hots", "Horde of the Summoned");
        jennKellonTallBanner = registerTallBanner("jennKellon", "jenn", "Jenn-Kellon");
        jennKellonTwoTallBanner = registerTallBanner("jennKellonTwo", "zjen", "Dragon Empire");
        blackLegionTallBanner = registerTallBanner("blackLegion", "blac", "Crusaders");
        crusadersTallBanner = registerTallBanner("crusaders", "thec", "Mol-Rehan");
        dreadnoughtTallBanner = registerTallBanner("dreadnought", "drea", "Dreadnought");
        ebonauraTallBanner = registerTallBanner("ebonaura", "ebon", "Ebonaura");
        romanEmpireTallBanner = registerTallBanner("romanEmpire", "ther", "Roman Empire");
        valhallaTallBanner = registerTallBanner("valhalla", "valh", "Valhalla");
        macedoniaTallBanner = registerTallBanner("macedonia", "mace", "Macedonia");
        crossbowTallBanner = registerTallBanner("crossbow", "crossbow", "crossbow");
        crownTallBanner = registerTallBanner("crown", "crown", "crown");
        deerTallBanner = registerTallBanner("deer", "deer", "deer");
        fireBloodTallBanner = registerTallBanner("fireBlood", "fireblood", "Fire Blood");
        blackFreedomTallBanner = registerTallBanner("blackFreedom", "blackfreedom", "Freedom");
        horseSwordTallBanner = registerTallBanner("horseSword", "horsesword", "horse and sword");
        horseTallBanner = registerTallBanner("horse", "horse", "horse");
        pandemoniumTallBanner = registerTallBanner("pandemonium", "pand", "Pandemonium");
        redHotsTallBanner = registerTallBanner("redHots", "redHots", "red Horde of the Summoned");
        blackJennKellonTallBanner = registerTallBanner("blackJennKellon", "blackjk", "black Jenn-Kellon");
        lionTallBanner = registerTallBanner("lion", "lion", "lion");
        blackMolRehanTallBanner = registerTallBanner("blackMolRehan", "blackmr", "black Mol-Rehan");
        scorpionTallBanner = registerTallBanner("scorpion", "scorpion", "scorpion");
        skullTallBanner = registerTallBanner("skull", "skull", "skull");
        throwingStarTallBanner = registerTallBanner("throwingStar", "throwingstar", "throwing star");
        winterComingTallBanner = registerTallBanner("winterComing", "wintercoming", "winter is coming");
        woodlandCamouflageTallBanner = registerTallBanner("woodlandCamouflage", "woodlandcamo", "woodland camouflage");
        universityWurmTallBanner = registerTallBanner("universityWurm", "wurm", "University of Wurm");
    }

    private static ItemTemplate registerBanner(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.banner.%s.", id))
                .name(String.format("%s banner", name), String.format("%ss", name), "An elegant symbol of allegiance and faith.")
                .modelName(String.format("model.decoration.tallbanner.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_CLOTH_BOLT)
                .itemTypes(new short[]{
                        ITEM_TYPE_CLOTH,
                        ITEM_TYPE_COLORABLE,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_FOUR_PER_TILE,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_IMPROVEITEM,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_IMPROVE_USES_TYPE_AS_MATERIAL,
                        ITEM_TYPE_PLANTABLE
                })
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .dimensions(5, 5, 205)
                .weightGrams(2500)
                .value(10000)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(50f)
                .combatDamage(0)
                .isTraded(true)
                .build();
    }

    private static void registerBanners() throws IOException {
        molRehanBanner = registerBanner("molRehan", "molr", "Mol-Rehan");
        hotsBanner = registerBanner("hots", "hots", "Horde of the Summoned");
        jennKellonBanner = registerBanner("jennKellon", "jenn", "Jenn-Kellon");
        empireMolRehanBanner = registerBanner("empireMolRehan", "empi", "Empire of Mol-Rehan");
        jennKellonTwoBanner = registerBanner("jennKellonTwo", "zjen", "Dragon Empire");
        blackLegionBanner = registerBanner("blackLegion", "blac", "Crusaders");
        crusadersBanner = registerBanner("crusaders", "thec", "Mol-Rehan");
        pandemoniumBanner = registerBanner("pandemonium", "pand", "Pandemonium");
        dreadnoughtBanner = registerBanner("dreadnought", "drea", "Dreadnought");
        ebonauraBanner = registerBanner("ebonaura", "ebon", "Ebonaura");
        romanEmpireBanner = registerBanner("romanEmpire", "ther", "Roman Empire");
        valhallaBanner = registerBanner("valhalla", "valh", "Valhalla");
        macedoniaBanner = registerBanner("macedonia", "mace", "Macedonia");
        eagleBanner = registerBanner("eagle", "eagle", "eagle");
        frostHavenBanner = registerBanner("frostHaven", "frosthaven", "Frost Haven");
        bloodyAxeBanner = registerBanner("bloodyAxe", "bloodyaxe", "bloody axe");
        burningTreeBanner = registerBanner("burningTree", "burningtree", "burning tree");
        chainBanner = registerBanner("chain", "chain", "chain");
        boneBanner = registerBanner("bone", "bone", "bone");
        spikeBanner = registerBanner("spike", "spike", "spike");
        murlocBanner = registerBanner("murloc", "murloc", "murloc");
        hornsBanner = registerBanner("horns", "horns", "horns");
        gateBanner = registerBanner("gate", "gate", "gate");
        bloodyMacheteBanner = registerBanner("bloodyMachete", "bloodymachete", "bloody machete");
        bloodHandBanner = registerBanner("bloodHand", "bloodhand", "blood hand");
        blackRedTornBanner = registerBanner("blackRedTorn", "blackredtorn", "torn black & red");
        woodlandCamouflageBanner = registerBanner("woodlandCamouflage", "woodlandcamo", "woodland camouflage");
    }

    private static ItemTemplate registerTutorialSign(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.sign.tutorial.%s.", id))
                .name(String.format("tutorial %s sign", name), String.format("%s", name), "A large wooden sign meant to help guide new players.")
                .modelName(String.format("model.decoration.sign.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_WOOD_SIGN_LARGE)
                .itemTypes(new short[]{
                        ITEM_TYPE_NAMED,
                        ITEM_TYPE_MISSION,
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_SIGN,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_WEAPON_MISC,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_PLANT_ONE_A_WEEK
                })
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .size(ItemSizes.ITEM_SIZE_LARGE)
                .dimensions(2, 5, 100)
                .weightGrams(5000)
                .value(500)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(15f)
                .combatDamage(0)
                .isTraded(false)
                .build();
    }

    private static void registerTutorialSigns() throws IOException {
        rightClickTutorialSign = registerTutorialSign("rightClick", "rightclick", "right click");
        alertBarTutorialSign = registerTutorialSign("alertBar", "alert", "bar sign");
        wurmpediaTutorialSign = registerTutorialSign("wurmpedia", "wurmpedia", "Wurmpedia");
        activateTutorialSign = registerTutorialSign("activate", "activate", "activate item");
        askTutorialSign = registerTutorialSign("ask", "ask", "ask for help");
        openTutorialSign = registerTutorialSign("open", "open", "open");
        healthBarTutorialSign = registerTutorialSign("healthBar", "healthbar", "health bar");
        foodTutorialSign = registerTutorialSign("food", "food", "food and water");
        fireTutorialSign = registerTutorialSign("fire", "fire", "fire");
        compassTutorialSign = registerTutorialSign("compass", "compass", "compass");
        quickbarTutorialSign = registerTutorialSign("quickbar", "quickbar", "quickbar");
        mapTutorialSign = registerTutorialSign("map", "map", "map");
        equipTutorialSign = registerTutorialSign("equip", "equip", "equip");
        creaturesTutorialSign = registerTutorialSign("creatures", "creatures", "creatures");
        tentsTutorialSign = registerTutorialSign("tents", "tents", "tents");
        fightingTutorialSign = registerTutorialSign("fighting", "fighting", "fighting");
        healingTutorialSign = registerTutorialSign("healing", "healing", "healing");
        helpTutorialSign = registerTutorialSign("help", "help", "help");
        deathTutorialSign = registerTutorialSign("death", "death", "death");
        climbTutorialSign = registerTutorialSign("climb", "climb", "climb");
        swimTutorialSign = registerTutorialSign("swim", "swim", "swim");
        skillsTutorialSign = registerTutorialSign("skills", "skills", "skills");
        toolsTutorialSign = registerTutorialSign("tools", "tools", "skills and tools");
        craftingTutorialSign = registerTutorialSign("crafting", "crafting", "crafting");
        decayTutorialSign = registerTutorialSign("decay", "decay", "decay, damage and repairing");
        terraformTutorialSign = registerTutorialSign("terraform", "terraform", "terraforming");
        theftTutorialSign = registerTutorialSign("theft", "theft", "theft");
        bartenderTutorialSign = registerTutorialSign("bartender", "bartender", "bartender");
    }

    private static ItemTemplate registerOverheadSign(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.sign.overhead.%s.", id))
                .name(String.format("%s overhead sign", name), String.format("%s", name), "A wide and large wooden sign. These kind are meant to be displayed along a path near a gate or entry.")
                .modelName(String.format("model.decoration.sign.overhead.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_WOOD_SIGN_LARGE)
                .itemTypes(new short[]{
                        ITEM_TYPE_COLORABLE,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_ONE_PER_TILE,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_IMPROVEITEM,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_DESC_IS_NAME,
                        ITEM_TYPE_PLANTABLE,
                        ITEM_TYPE_IMPROVE_USES_TYPE_AS_MATERIAL,
                        ITEM_TYPE_DESC_IS_NAME,
                        ITEM_TYPE_WOOD
                })
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .size(ItemSizes.ITEM_SIZE_LARGE)
                .dimensions(15, 20, 300)
                .weightGrams(12000)
                .value(1000)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(40f)
                .combatDamage(0)
                .isTraded(false)
                .build();
    }

    private static void registerOverheadSigns() throws IOException {
        deathIslandOverheadSign = registerOverheadSign("deathIsland", "deathisland", "Death Island");
        enterOverheadSign = registerOverheadSign("enter", "enter", "enter");
        exitOverheadSign = registerOverheadSign("exit", "exit", "exit");
        dangerOverheadSign = registerOverheadSign("danger", "danger", "danger");
        arenaOverheadSign = registerOverheadSign("arena", "arena", "arena");
        startOverheadSign = registerOverheadSign("start", "start", "start");
        finishOverheadSign = registerOverheadSign("finish", "finish", "finish");
        mineEntranceOverheadSign = registerOverheadSign("mineEntrance", "mineentrance", "mine entrance");
        constructionOverheadSign = registerOverheadSign("construction", "construction", "construction");
        eventCenterOverheadSign = registerOverheadSign("eventCenter", "eventCenter", "event center");
        joustingOverheadSign = registerOverheadSign("jousting", "jousting", "jousting");
        portalAreaOverheadSign = registerOverheadSign("portalArea", "PortalArea", "portal area");
        // Patreons
        callanishPatreonOverheadSign = registerOverheadSign("patreon.callanish", "callanish", "Meow");
        cattibriePatreonOverheadSign = registerOverheadSign("patreon.cattibrie", "cattibrie", "Dame de Dragons");
        vysePatreonOverheadSign = registerOverheadSign("patreon.vyse", "vyse", "The Serpent's Rest");
    }

    private static ItemTemplate registerBlueprint(String id, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.blueprint.%s.", id))
                .name(String.format("%s's blueprint", name), String.format("%s", name), "A schematic to build something.")
                .modelName(ModelConstants.MODEL_WRIT)
                .imageNumber((short) IconConstants.ICON_SCROLL_HOUSE)
                .itemTypes(new short[]{
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_DESTROYABLE
                })
                .decayTime(TimeConstants.DECAYTIME_MAGIC)
                .dimensions(1, 1, 1)
                .weightGrams(100)
                .value(1000)
                .material(ItemMaterials.MATERIAL_PAPER)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(40f)
                .combatDamage(0)
                .isTraded(false)
                .build();
    }

    private static void registerBlueprints() throws IOException {
        callanishPatreonBlueprint = registerBlueprint("patreon.callanish", "Callanish");
        cattibriePatreonBlueprint = registerBlueprint("patreon.cattibrie", "Cattibrie");
        vysePatreonBlueprint = registerBlueprint("patreon.vyse", "Vyse");
    }

    private static ItemTemplate registerTapestry(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.tapestry.%s.", id))
                .name(String.format("%s tapestry", name), String.format("%s tapestries", name), "A nice woven tapestry.")
                .modelName(String.format("model.furniture.tapestry.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_CLOTH_BOLT)
                .itemTypes(new short[]{
                        ITEM_TYPE_CLOTH,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_ONE_PER_TILE,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_IMPROVEITEM,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_TAPESTRY,
                        ITEM_TYPE_NOT_MISSION,
                        ITEM_TYPE_IMPROVE_USES_TYPE_AS_MATERIAL,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_PLANTABLE
                })
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .dimensions(5, 5, 205)
                .weightGrams(12000)
                .value(1000)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(30f)
                .combatDamage(0)
                .isTraded(true)
                .build();
    }

    private static void registerTapestries() throws IOException {
        blueNecromancerTapestry = registerTapestry("necromancer.blue", "bluenecro", "blue necromancer");
        blueWizardTapestry = registerTapestry("wizard.blue", "bluewizard", "blue wizard");
        cryptWizardTapestry = registerTapestry("wizard.crypt", "cryptwizard", "crypt wizard");
        darkWizardTapestry = registerTapestry("wizard.dark", "darkwizard", "dark wizard");
        wizardVDragonTapestry = registerTapestry("wizard.dragon", "wizardvdragon", "wizard versus dragon");
        dragonsTreeTapestry = registerTapestry("dragon.tree", "dragonstree", "dragons in a tree");
        fireDemonTapestry = registerTapestry("demon.fire", "firedemon", "fire demon");
        landscapeTownTapestry = registerTapestry("landscape.town", "landscapetown", "landscape town");
        landscapeWaterfallTapestry = registerTapestry("landscape.waterfall", "landscapewaterfall", "landscape waterfall");
        lichTapestry = registerTapestry("lich", "lich", "lich");
        nordicWarriorTapestry = registerTapestry("warrior.nordic", "nordicwarrior", "nordic warrior");
        phoenixTapestry = registerTapestry("phoenix", "phoenix", "phoenix");
        seaSerpentTapestry = registerTapestry("serpent.sea", "seaserpent", "sea serpent");
        sealTapestry = registerTapestry("seal", "seal", "seal");
        trollKingTapestry = registerTapestry("troll.king", "trollking", "troll king");
        fourGodsTapestry = registerTapestry("gods.four", "fourgods", "four gods");
        foTapestry = registerTapestry("gods.fo", "fo", "Fo");
        magranonTapestry = registerTapestry("magranon", "magranon", "Magranon");
        libilaTapestry = registerTapestry("libila", "libila", "Libila");
        vynoraTapestry = registerTapestry("vynora", "vynora", "Vynora");
        psychedelicTapestry = registerTapestry("psychedelic", "psychedelic", "psychedelic");
        mushroomHouseTapestry = registerTapestry("house.mushroom", "mushroomhouse", "mushroom house");
        wurmOneTapestry = registerTapestry("wurm.1", "wurm1", "Wurm I");
        wurmTwoTapestry = registerTapestry("wurm.2", "wurm2", "Wurm II");
        wurmThreeTapestry = registerTapestry("wurm.3", "wurm3", "Wurm III");
        battleOneTapestry = registerTapestry("battle.1", "battle1", "battle I");
        battleTwoTapestry = registerTapestry("battle.2", "battle2", "battle II");
        threeCrownsBattleTapestry = registerTapestry("battle.threeCrowns", "threecrownsbattle", "Three Crowns battle");
        battleHastingsTapestry = registerTapestry("battle.hastings", "battlehastings", "Battle of Hastings");
        bayeuxTapestry = registerTapestry("bayeux", "bayeux", "Bayeux");
        calmForestTapestry = registerTapestry("forest.calm", "calmforest", "calm forest");
        deathAngelTapestry = registerTapestry("angel.death", "deathangel", "death angel");
        dragonElfTapestry = registerTapestry("dragon.elf", "dragonelf", "dragon and elf");
        dragonFightTapestry = registerTapestry("dragon.fight", "dragonfight", "dragon fight");
        joyLifeTapestry = registerTapestry("joyLife", "joylife", "Joy of Life");
        lifesRichesTapestry = registerTapestry("lifesRiches", "lifesriches", "Life's Riches");
        nightSkyTapestry = registerTapestry("sky.night", "nightsky", "night sky");
        paladinTapestry = registerTapestry("paladin", "paladin", "paladin");
        reaperTapestry = registerTapestry("reaper", "reaper", "reaper");
        sweetDreamsTapestry = registerTapestry("sweetDreams", "sweetdreams", "sweet dreams");
        crowWitchTapestry = registerTapestry("witch.crow", "crowwitch", "crow witch");
        bloodWitchTapestry = registerTapestry("witch.blood", "bloodwitch", "blood witch");
        graveWitchTapestry = registerTapestry("witch.grave", "gravewitch", "grave witch");
        ladyUnicornTapestry = registerTapestry("unicorn.lady", "ladyunicorn", "The Lady and the Unicorn");
        knightChampionTapestry = registerTapestry("knight.champion", "knightchampion", "knight champion");
        whiteKnightTapestry = registerTapestry("knight.white", "whiteknight", "white knight");
        requiemMapTapestry = registerTapestry("map.requiem", "map", "Requiem map");
    }

    private static ItemTemplate registerExquisiteMeditationRug(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.rug.meditation.exquisite.%s.", id))
                .name(String.format("exquisite %s meditation rug", name), String.format("exquisite %s meditation rugs", name), "This thin, colorful rug provides some comfort as you sit on it but its main purpose is to create a meditation zone where your mind can relax.")
                .modelName(String.format("model.decoration.carpet.medi.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_CARPET_MEDITATION)
                .itemTypes(new short[]{
                        ITEM_TYPE_CLOTH,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_MEDITATION,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_CARPET
                })
                .decayTime(TimeConstants.DECAYTIME_CLOTHING)
                .dimensions(5, 5, 5)
                .weightGrams(1500)
                .value(30000)
                .material(ItemMaterials.MATERIAL_COTTON)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(95f)
                .isTraded(true)
                .build();
    }

    private static void registerExquisiteMeditationRugs() throws IOException {
        waterfallExquisiteMeditationRug = registerExquisiteMeditationRug("waterfall", "waterfall", "waterfall");
        orientalExquisiteMeditationRug = registerExquisiteMeditationRug("oriental", "oriental", "oriental");
        darkSquareExquisiteMeditationRug = registerExquisiteMeditationRug("darkSquare", "darksquare", "dark square");
        darkTriangleExquisiteMeditationRug = registerExquisiteMeditationRug("darkTriangle", "darktriangle", "dark triangle");
        hornetExquisiteMeditationRug = registerExquisiteMeditationRug("hornet", "hornet", "hornet");
        chineseDragonExquisiteMeditationRug = registerExquisiteMeditationRug("dragon.chinese", "chinesedragon", "Chinese dragon");
        cthulhuExquisiteMeditationRug = registerExquisiteMeditationRug("cthulhu", "cthulhu", "Cthulhu");
        roseExquisiteMeditationRug = registerExquisiteMeditationRug("rose", "rose", "rose");
        floralExquisiteMeditationRug = registerExquisiteMeditationRug("floral", "floral", "floral");
        pinkFloralExquisiteMeditationRug = registerExquisiteMeditationRug("floral.pink", "pinkfloral", "pink floral");
        blueFloralExquisiteMeditationRug = registerExquisiteMeditationRug("floral.blue", "bluefloral", "blue floral");
        blueWhiteFloralExquisiteMeditationRug = registerExquisiteMeditationRug("floral.blueWhite", "blueandwhitefloral", "blue and white floral");
        creamFloralExquisiteMeditationRug = registerExquisiteMeditationRug("floral.cream", "creamfloral", "cream floral");
        navyRedExquisiteMeditationRug = registerExquisiteMeditationRug("navyRed", "navyandred", "navy and red");
        navyGoldExquisiteMeditationRug = registerExquisiteMeditationRug("navyGold", "navyandgold", "navy and gold");
        creamRoseOvalExquisiteMeditationRug = registerExquisiteMeditationRug("oval.creamRose", "creamandroseoval", "cream and rose oval");
        lightBlueWhiteExquisiteMeditationRug = registerExquisiteMeditationRug("lightBlueWhite", "lightblueandwhite", "light blue and white");
        redWhiteExquisiteMeditationRug = registerExquisiteMeditationRug("redWhite", "redandwhite", "red and white");
        redWhiteTwoFloralExquisiteMeditationRug = registerExquisiteMeditationRug("redWhite2", "redandwhite2", "red and white II");
        redGoldOneExquisiteMeditationRug = registerExquisiteMeditationRug("redGold1", "redandgold", "red and gold I");
        redGoldTwoExquisiteMeditationRug = registerExquisiteMeditationRug("redGold2", "redandgold1", "red and gold II");
        redGoldThreeExquisiteMeditationRug = registerExquisiteMeditationRug("redGold3", "redandgold2", "red and gold III");
        redGoldFourExquisiteMeditationRug = registerExquisiteMeditationRug("redGold4", "redandgold3", "red and gold IIII");
        redGoldFiveExquisiteMeditationRug = registerExquisiteMeditationRug("redGold5", "redandgold4", "red and gold V");
        navyCreamOneExquisiteMeditationRug = registerExquisiteMeditationRug("navyCream1", "navyandcream", "navy and cream I");
        navyCreamTwoExquisiteMeditationRug = registerExquisiteMeditationRug("navyCream2", "navyandcream1", "navy and cream II");
        greenGoldOneExquisiteMeditationRug = registerExquisiteMeditationRug("greenGold1", "greenandgold", "green and gold I");
        greenGoldTwoExquisiteMeditationRug = registerExquisiteMeditationRug("greenGold2", "greenandgold2", "green and gold II");
    }

    private static ItemTemplate registerRoyalLoungeChaise(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.seat.LoungeChaise.royal.%s.", id))
                .name(String.format("%s royal lounge chaise", name), String.format("%s royal lounge chaises", name), "Feel like the captain of a dragon ship while leaning back comfortably.")
                .modelName(String.format("model.furniture.chair.lounge.royal.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_FURN_ROYAL_OUNGE_CHAISE)
                .itemTypes(new short[]{
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_USE_GROUND_ONLY,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_TRANSPORTABLE,
                        ITEM_TYPE_VEHICLE,
                        ITEM_TYPE_CHAIR,
                        ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION,
                        ITEM_TYPE_PLANTABLE
                })
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .dimensions(294, 110, 150)
                .weightGrams(10000)
                .value(20000)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.vehicleBehaviour)
                .difficulty(50f)
                .isTraded(true)
                .dyeAmountOverrideGrams((short) 0)
                .secondaryItemName("Seat")
                .build();
    }

    private static void registerRoyalLoungeChaises() throws IOException {
        blackRoyalLoungeChaise = registerRoyalLoungeChaise("black", "black", "black");
        blueRoyalLoungeChaise = registerRoyalLoungeChaise("blue", "blue", "blue");
        greenRoyalLoungeChaise = registerRoyalLoungeChaise("green", "green", "green");
        greyRoyalLoungeChaise = registerRoyalLoungeChaise("grey", "grey", "grey");
        purpleRoyalLoungeChaise = registerRoyalLoungeChaise("purple", "purple", "purple");
        redRoyalLoungeChaise = registerRoyalLoungeChaise("red", "red", "red");
        yellowRoyalLoungeChaise = registerRoyalLoungeChaise("yellow", "yellow", "yellow");
    }

    private static ItemTemplate registerFineHighChair(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.seat.highChair.fine.%s.", id))
                .name(String.format("%s royal lounge chaise", name), String.format("%s royal lounge chaises", name), "A comfy looking chair which invites you to rest.")
                .modelName(String.format("model.decoration.fine.high.chair.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_FURN_LOUNGE_CHAIR)
                .itemTypes(new short[]{
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_USE_GROUND_ONLY,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_VEHICLE,
                        ITEM_TYPE_CHAIR,
                        ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION,
                        ITEM_TYPE_PLANTABLE
                })
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .dimensions(60, 65, 175)
                .weightGrams(7500)
                .value(20000)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.vehicleBehaviour)
                .difficulty(60f)
                .isTraded(true)
                .dyeAmountOverrideGrams((short) 0)
                .secondaryItemName("Seat")
                .build();
    }

    private static void registerFineHighChairs() throws IOException {
        blackFineHighChair = registerFineHighChair("black", "black", "black");
        blueFineHighChair = registerFineHighChair("blue", "blue", "blue");
        greenFineHighChair = registerFineHighChair("green", "green", "green");
        greyFineHighChair = registerFineHighChair("grey", "grey", "grey");
        yellowFineHighChair = registerFineHighChair("yellow", "yellow", "yellow");
    }

    private static ItemTemplate registerDecorativeItem(String id, String name, String plural, String description, String modelName, short icon, int weight, byte material) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.decoration.%s.", id))
                .name(name, plural, description)
                .modelName(modelName)
                .imageNumber(icon)
                .itemTypes(new short[]{
                        ITEM_TYPE_COLORABLE,
                        ITEM_TYPE_NOT_MISSION,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_REPAIRABLE
                })
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .dimensions(20, 20, 20)
                .weightGrams(weight)
                .value(1000)
                .material(material)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(20f)
                .isTraded(false)
                .build();
    }

    private static void registerDecorativeItems() throws IOException {
        hayPile = registerDecorativeItem("pile.hay", "hay pile", "hay piles", "A pile of hay.", "model.pile.mortar.", (short) IconConstants.ICON_THATCH, 6500, ItemMaterials.MATERIAL_STRAW);
        mortarPile = registerDecorativeItem("pile.mortar", "mortar pile", "mortar piles", "A pile of mortar.", "model.pile.mortar.", (short) IconConstants.ICON_MORTAR_PILE, 20000, ItemMaterials.MATERIAL_CLAY);
        dirtPile = registerDecorativeItem("pile.dirt", "dirt pile", "dirt piles", "A pile of dirt.", "model.pile.dirt.", (short) IconConstants.ICON_DIRT_PILE, 20000, ItemMaterials.MATERIAL_CLAY);
        sandPile = registerDecorativeItem("pile.sand", "sand pile", "sand piles", "A pile of sand.", "model.pile.heapofsand.", (short) IconConstants.ICON_SAND_PILE, 20000, ItemMaterials.MATERIAL_STONE);
        tarPile = registerDecorativeItem("pile.tar", "tar pile", "tar pile", "A pile of tar.", "model.pile.tar.", (short) IconConstants.ICON_LIQUID_TAR, 60000, ItemMaterials.MATERIAL_TAR);
        peatPile = registerDecorativeItem("pile.peat", "peat pile", "peat piles", "A pile of peat.", "model.pile.peat.", (short) IconConstants.ICON_PEAT, 30000, ItemMaterials.MATERIAL_PEAT);
        logPile = registerDecorativeItem("pile.log", "log pile", "log pile", "A pile of logs.", "model.pile.log.birchwood.", (short) IconConstants.ICON_WOOD_LOG, 72000, ItemMaterials.MATERIAL_WOOD_BIRCH);
        rockPile = registerDecorativeItem("pile.rock", "rock pile", "rock pile", "A pile of rocks.", "model.pile.rockshards.stone.", (short) IconConstants.ICON_ROCK_PILE, 60000, ItemMaterials.MATERIAL_STONE);
        rockShard = registerDecorativeItem("shard.rock", "rock shard", "rock shards", "A rock shard.", "model.resource.stone.", (short) IconConstants.ICON_FRAGMENT_STONE, 20000, ItemMaterials.MATERIAL_STONE);
        slateShard = registerDecorativeItem("shard.slate", "slate shard", "slate shards", "A slate shard.", "model.resource.", (short) IconConstants.ICON_FRAGMENT_SLATE, 20000, ItemMaterials.MATERIAL_SLATE);
        slatePile = registerDecorativeItem("pile.slate", "slate pile", "slate pile", "A pile of slate.", "model.pile.shards.slate.", (short) IconConstants.ICON_FRAGMENT_SLATE, 60000, ItemMaterials.MATERIAL_SLATE);
        marbleShard = registerDecorativeItem("shard.marble", "marble shard", "marble shards", "A marble shard.", "model.resource.", (short) IconConstants.ICON_FRAGMENT_MARBLE, 20000, ItemMaterials.MATERIAL_MARBLE);
        marblePile = registerDecorativeItem("pile.marble", "marble pile", "marble pile", "A pile of marble.", "model.pile.shards.marble.", (short) IconConstants.ICON_FRAGMENT_MARBLE, 60000, ItemMaterials.MATERIAL_MARBLE);
        sandstoneShard = registerDecorativeItem("shard.sandstone", "sandstone shard", "sandstone shards", "A sandstone shard.", "model.resource.", (short) IconConstants.ICON_FRAGMENT_SANDSTONE, 20000, ItemMaterials.MATERIAL_SANDSTONE);
        sandstonePile = registerDecorativeItem("pile.sandstone", "sandstone pile", "sandstone pile", "A pile of sandstone.", "model.pile.shards.sandstone.", (short) IconConstants.ICON_SANDSTONE_PILE, 60000, ItemMaterials.MATERIAL_SANDSTONE);
        ironOre = registerDecorativeItem("ore.iron", "iron ore", "iron ore", "An iron ore.", "model.resource.ore.iron.", (short) IconConstants.ICON_METAL_IRON_ORE, 20000, ItemMaterials.MATERIAL_IRON);
        ironOrePile = registerDecorativeItem("pile.ore.iron", "iron ore pile", "iron ore piles", "A pile of iron ore.", "model.pile.ore.iron.", (short) IconConstants.ICON_METAL_IRON_ORE, 60000, ItemMaterials.MATERIAL_IRON);
        copperOre = registerDecorativeItem("ore.copper", "copper ore", "copper ores", "A shard of copper ore.", "model.resource.ore.copper.", (short) IconConstants.ICON_FRAGMENT_COPPER, 20000, ItemMaterials.MATERIAL_COPPER);
        copperOrePile = registerDecorativeItem("pile.ore.copper", "copper ore pile", "copper ore piles", "A pile of copper ore.", "model.pile.ore.copper.", (short) IconConstants.ICON_FRAGMENT_COPPER, 60000, ItemMaterials.MATERIAL_COPPER);
        goldOre = registerDecorativeItem("ore.gold", "gold ore", "gold ores", "A shard of gold ore.", "model.resource.ore.gold.", (short) IconConstants.ICON_FRAGMENT_GOLD, 20000, ItemMaterials.MATERIAL_GOLD);
        goldOrePile = registerDecorativeItem("pile.ore.gold", "gold ore pile", "gold ore piles", "A pile of gold ore.", "model.pile.ore.gold.", (short) IconConstants.ICON_FRAGMENT_GOLD, 60000, ItemMaterials.MATERIAL_GOLD);
        silverOre = registerDecorativeItem("ore.silver", "silver ore", "silver ore", "A shard of silver ore.", "model.resource.ore.silver.", (short) IconConstants.ICON_FRAGMENT_SILVER, 20000, ItemMaterials.MATERIAL_SILVER);
        silverOrePile = registerDecorativeItem("pile.ore.silver", "silver ore pile", "silver ore piles", "A pile of silver ore.", "model.pile.ore.silver.", (short) IconConstants.ICON_FRAGMENT_SILVER, 60000, ItemMaterials.MATERIAL_SILVER);
        zincOre = registerDecorativeItem("ore.zinc", "zinc ore", "zinc ores", "A shard of zinc ore.", "model.resource.ore.zinc.", (short) IconConstants.ICON_FRAGMENT_ZINC, 20000, ItemMaterials.MATERIAL_ZINC);
        zincOrePile = registerDecorativeItem("pile.ore.zinc", "zinc ore pile", "zinc ore piles", "A pile of zinc ore.", "model.pile.ore.zinc.", (short) IconConstants.ICON_FRAGMENT_ZINC, 60000, ItemMaterials.MATERIAL_ZINC);
        tinOre = registerDecorativeItem("ore.tin", "tin ore", "tin ores", "A shard of tin ore.", "model.resource.ore.tin.", (short) IconConstants.ICON_FRAGMENT_TIN, 20000, ItemMaterials.MATERIAL_TIN);
        tinOrePile = registerDecorativeItem("pile.ore.tin", "tin ore pile", "tin ore piles", "A pile of tin ore.", "model.pile.ore.tin.", (short) IconConstants.ICON_FRAGMENT_TIN, 60000, ItemMaterials.MATERIAL_TIN);
        leadOre = registerDecorativeItem("ore.lead", "lead ore", "lead ores", "A shard of lead ore.", "model.resource.ore.lead.", (short) IconConstants.ICON_FRAGMENT_LEAD, 20000, ItemMaterials.MATERIAL_LEAD);
        leadOrePile = registerDecorativeItem("pile.ore.lead", "lead ore piles", "lead ore piles", "A pile of lead ore.", "model.pile.ore.lead.", (short) IconConstants.ICON_FRAGMENT_LEAD, 60000, ItemMaterials.MATERIAL_LEAD);
        adamantineOre = registerDecorativeItem("ore.adamantine", "adamantine ore", "adamantine ores", "A shard of adamantine ore.", "model.resource.ore.adamantine.", (short) IconConstants.ICON_METAL_ADAMANTINE_ORE, 20000, ItemMaterials.MATERIAL_ADAMANTINE);
        adamantineOrePile = registerDecorativeItem("pile.ore.adamantine", "adamantine ore pile", "adamantine ore piles", "A pile of adamantine ore.", "model.pile.ore.adamantine.", (short) IconConstants.ICON_METAL_ADAMANTINE_ORE, 60000, ItemMaterials.MATERIAL_ADAMANTINE);
        adamantineBoulder = registerDecorativeItem("ore.adamantine.boulder", "adamantine boulder", "adamantine boulders", "An adamantine boulder.", "model.resource.boulder.adamantine.", (short) IconConstants.ICON_METAL_ADAMANTINE_BOULDER, 60000, ItemMaterials.MATERIAL_ADAMANTINE);
        glimmersteelOre = registerDecorativeItem("ore.glimmersteel", "glimmersteel ore", "glimmersteel ores", "A shard of glimmersteel ore.", "model.resource.ore.glimmersteel.", (short) IconConstants.ICON_METAL_GLIMMERSTEEL_ORE, 20000, ItemMaterials.MATERIAL_GLIMMERSTEEL);
        glimmersteelOrePile = registerDecorativeItem("pile.ore.glimmersteel", "glimmersteel ore pile", "glimmersteel ore piles", "A pile of glimmersteel ore.", "model.pile.ore.glimmersteel.", (short) IconConstants.ICON_METAL_GLIMMERSTEEL_ORE, 60000, ItemMaterials.MATERIAL_GLIMMERSTEEL);
        glimmersteelBoulder = registerDecorativeItem("ore.glimmersteel.boulder", "glimmersteel boulder", "glimmersteel boulders", "A glimmersteel boulder.", "model.resource.boulder.glimmersteel.", (short) IconConstants.ICON_METAL_GLIMMERSTEEL_BOULDER, 60000, ItemMaterials.MATERIAL_GLIMMERSTEEL);
        riftStone1 = registerDecorativeItem("stone.tall.1", "tall stone 1", "tall stone 1", "A tall oddly shaped stone.", ModelConstants.MODEL_STRUCTURE_RIFTSTONE + "1.", (short) IconConstants.ICON_FRAGMENT_STONE, 50000, ItemMaterials.MATERIAL_STONE);
        riftStone2 = registerDecorativeItem("stone.tall.2", "tall stone 2", "tall stone 2", "A tall oddly shaped stone.", ModelConstants.MODEL_STRUCTURE_RIFTSTONE + "2.", (short) IconConstants.ICON_FRAGMENT_STONE, 50000, ItemMaterials.MATERIAL_STONE);
        riftStone3 = registerDecorativeItem("stone.tall.3", "tall stone 3", "tall stone 3", "A tall oddly shaped stone.", ModelConstants.MODEL_STRUCTURE_RIFTSTONE + "3.", (short) IconConstants.ICON_FRAGMENT_STONE, 50000, ItemMaterials.MATERIAL_STONE);
        riftStone4 = registerDecorativeItem("stone.tall.4", "tall stone 4", "tall stone 4", "A tall oddly shaped stone.", ModelConstants.MODEL_STRUCTURE_RIFTSTONE + "4.", (short) IconConstants.ICON_FRAGMENT_STONE, 50000, ItemMaterials.MATERIAL_STONE);
        riftCrystal1 = registerDecorativeItem("crystal.rift.1", "strange crystal 1", "rift crystal 1", "An odd looking crystal formation.", ModelConstants.MODEL_STRUCTURE_RIFTCRYSTAL + "1.", (short) IconConstants.ICON_NONE, 50000, ItemMaterials.MATERIAL_CRYSTAL);
        riftCrystal2 = registerDecorativeItem("crystal.rift.2", "strange crystal 2", "rift crystal 2", "An odd looking crystal formation.", ModelConstants.MODEL_STRUCTURE_RIFTCRYSTAL + "2.", (short) IconConstants.ICON_NONE, 50000, ItemMaterials.MATERIAL_CRYSTAL);
        riftCrystal3 = registerDecorativeItem("crystal.rift.3", "strange crystal 3", "rift crystal 3", "An odd looking crystal formation.", ModelConstants.MODEL_STRUCTURE_RIFTCRYSTAL + "3.", (short) IconConstants.ICON_NONE, 50000, ItemMaterials.MATERIAL_CRYSTAL);
        riftCrystal4 = registerDecorativeItem("crystal.rift.4", "strange crystal 4", "rift crystal 4", "An odd looking crystal formation.", ModelConstants.MODEL_STRUCTURE_RIFTCRYSTAL + "4.", (short) IconConstants.ICON_NONE, 50000, ItemMaterials.MATERIAL_CRYSTAL);
        plant1 = registerDecorativeItem("plant.rift.1", "plant 1", "plant 1", "A small plant.", ModelConstants.MODEL_STRUCTURE_RIFTPLANT + "1.", (short) IconConstants.ICON_FENNEL_PLANT, 500, ItemMaterials.MATERIAL_VEGETARIAN);
        plant2 = registerDecorativeItem("plant.rift.2", "plant 2", "plant 2", "A small plant.", ModelConstants.MODEL_STRUCTURE_RIFTPLANT + "2.", (short) IconConstants.ICON_FENNEL_PLANT, 500, ItemMaterials.MATERIAL_VEGETARIAN);
        plant3 = registerDecorativeItem("plant.rift.3", "plant 3", "plant 3", "A small plant.", ModelConstants.MODEL_STRUCTURE_RIFTPLANT + "3.", (short) IconConstants.ICON_FENNEL_PLANT, 500, ItemMaterials.MATERIAL_VEGETARIAN);
        plant4 = registerDecorativeItem("plant.rift.4", "plant 4", "plant 4", "A small plant.", ModelConstants.MODEL_STRUCTURE_RIFTPLANT + "4.", (short) IconConstants.ICON_FENNEL_PLANT, 500, ItemMaterials.MATERIAL_VEGETARIAN);
        rock1 = registerDecorativeItem("rock.1", "rock 1", "rock 1", "A rock.", "model.terrain.decoration.rock1.", (short) IconConstants.ICON_ROCK_PILE, 20000, ItemMaterials.MATERIAL_STONE);
        rock2 = registerDecorativeItem("rock.2", "rock 2", "rock 2", "A rock.", "model.terrain.decoration.rock2.", (short) IconConstants.ICON_ROCK_PILE, 20000, ItemMaterials.MATERIAL_STONE);
        rock3 = registerDecorativeItem("rock.3", "rock 3", "rock 3", "A rock.", "model.terrain.decoration.rock3.", (short) IconConstants.ICON_ROCK_PILE, 20000, ItemMaterials.MATERIAL_STONE);
        fern1 = registerDecorativeItem("fern.1", "fern 1", "fern 1", "A fern.", "model.terrain.decoration.fern1.", (short) IconConstants.ICON_FENNEL_PLANT, 500, ItemMaterials.MATERIAL_VEGETARIAN);
        fern2 = registerDecorativeItem("fern.2", "fern 2", "fern 2", "A fern.", "model.terrain.decoration.fern2.", (short) IconConstants.ICON_FENNEL_PLANT, 500, ItemMaterials.MATERIAL_VEGETARIAN);
        fern3 = registerDecorativeItem("fern.3", "fern 3", "fern 3", "A fern.", "model.terrain.decoration.fern3.", (short) IconConstants.ICON_FENNEL_PLANT, 500, ItemMaterials.MATERIAL_VEGETARIAN);
        fernFall1 = registerDecorativeItem("fern.fall.1", "fall fern 1", "fall fern 1", "A fern that is starting to wilt away.", "model.terrain.decoration.fern1.fall.", (short) IconConstants.ICON_FENNEL_PLANT, 500, ItemMaterials.MATERIAL_VEGETARIAN);
        fernFall2 = registerDecorativeItem("fern.fall.2", "fall fern 2", "fall fern 2", "A fern that is starting to wilt away.", "model.terrain.decoration.fern2.fall.", (short) IconConstants.ICON_FENNEL_PLANT, 500, ItemMaterials.MATERIAL_VEGETARIAN);
        fernFall3 = registerDecorativeItem("fern.fall.3", "fall fern 3", "fall fern 3", "A fern that is starting to wilt away.", "model.terrain.decoration.fern3.fall.", (short) IconConstants.ICON_FENNEL_PLANT, 500, ItemMaterials.MATERIAL_VEGETARIAN);
        redMushroomPile = registerDecorativeItem("pile.mushroom.red", "red mushroom pile", "red mushroom piles", "A pile of red mushrooms.", "model.pile.redmushroom.", (short) IconConstants.ICON_FOOD_MUSHROOM_RED, 500, ItemMaterials.MATERIAL_VEGETARIAN);
        yellowMushroomPile = registerDecorativeItem("pile.mushroom.yellow", "yellow mushroom pile", "yellow mushroom piles", "A pile of yellow mushrooms.", "model.pile.yellowmushroom.", (short) IconConstants.ICON_FOOD_MUSHROOM_YELLOW, 500, ItemMaterials.MATERIAL_VEGETARIAN);
        brownMushroomPile = registerDecorativeItem("pile.mushroom.brown", "brown mushroom pile", "brown mushroom piles", "A pile of brown mushrooms.", "model.pile.brownmushroom.", (short) IconConstants.ICON_FOOD_MUSHROOM_BROWN, 500, ItemMaterials.MATERIAL_VEGETARIAN);
        blueMushroomPile = registerDecorativeItem("pile.mushroom.blue", "blue mushroom pile", "blue mushroom piles", "A pile of blue mushrooms.", "model.pile.bluemushroom.", (short) IconConstants.ICON_FOOD_MUSHROOM_BLUE, 500, ItemMaterials.MATERIAL_VEGETARIAN);
        greenMushroomPile = registerDecorativeItem("pile.mushroom.green", "green mushroom pile", "green mushroom piles", "A pile of green mushrooms.", "model.pile.greenmushroom.", (short) IconConstants.ICON_FOOD_MUSHROOM_GREEN, 500, ItemMaterials.MATERIAL_VEGETARIAN);
        blackMushroomPile = registerDecorativeItem("pile.mushroom.black", "black mushroom pile", "black mushroom piles", "A pile of black mushrooms.", "model.pile.blackmushroom.", (short) IconConstants.ICON_FOOD_MUSHROOM_BLACK, 500, ItemMaterials.MATERIAL_VEGETARIAN);
        questionMarkBag = registerDecorativeItem("bag.questionMark", "bag", "bags", "A mystery bag.", "model.jubaroo.blahblah.", (short) IconConstants.ICON_CONTAINER_BAG, 1000, ItemMaterials.MATERIAL_COTTON);
        stoneOfSoulfall = registerDecorativeItem("stone.soulfall", "runic stone", "runic stones", "A large stone with strange runic writing on it.", "model.structure.portal.10.", (short) IconConstants.ICON_FRAGMENT_STONE, 80000, ItemMaterials.MATERIAL_STONE);
        bodyBag = registerDecorativeItem("bodybag", "body bag", "body bags", "A dead body stuffed inside of a burlap sack. You can see stains on it from the blood still leaking from the corpse.", "model.corpse.", (short) IconConstants.ICON_CORPSE_HUMAN, 50000, ItemMaterials.MATERIAL_MEAT_HUMAN);
        swordStone = registerDecorativeItem("stone.sword", "sword in the stone", "sword in the stone", "A sword has been embedded into a rock. No matter how hard you pull, you cannot pry it from the stones eternal grip.", "model.resource.stone.swordstone.occupied.", (short) IconConstants.ICON_WEAPON_SWORD_LONG, 150000, ItemMaterials.MATERIAL_STONE);
        campfire = registerDecorativeItem("campfire.burning", "burning campfire", "burning campfires", "A campfire that never seems to run out of fuel. Strangely you cannot cook on it.", ModelConstants.MODEL_FIREPLACE_CAMPFIRE, (short) IconConstants.ICON_COOKER_CAMPFIRE, 1500, ItemMaterials.MATERIAL_WOOD_BIRCH);
        coinPile = registerDecorativeItem("pile.coin", "coin pile", "coin piles", "A pile of coin.", "model.pile.coin.", (short) IconConstants.ICON_COIN_COPPER, 30000, ItemMaterials.MATERIAL_COPPER);
    }

    private static ItemTemplate registerCanopyBed(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.bed.canopy.%s", id))
                .name(String.format("canopy bed %s pattern", name), String.format("canopy bed %s patterns", name), "An inviting canopy bed fit for royalty.")
                .modelName(String.format("model.furniture.bed.canopy.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_DECO_BED_STANDARD)
                .itemTypes(new short[]{
                        ITEM_TYPE_ONE_PER_TILE,
                        ITEM_TYPE_NAMED,
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_NOTAKE,
                        ITEM_TYPE_USE_GROUND_ONLY,
                        ITEM_TYPE_OWNER_DESTROYABLE,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_BED,
                        ITEM_TYPE_INSIDE_ONLY,
                        ITEM_TYPE_TRANSPORTABLE,
                        ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION
                })
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .dimensions(87, 50, 81)
                .weightGrams(50000)
                .value(20000)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(65f)
                .isTraded(false)
                .build();
    }

    private static void registerCanopyBeds() throws IOException {
        flowersRedCanopyBed = registerCanopyBed("flowers.red", "flowersred", "red flowers");
        kingCanopyBed = registerCanopyBed("king", "king", "king");
        melodyCanopyBed = registerCanopyBed("melody", "melody", "melody");
        nightDreamsCanopyBed = registerCanopyBed("nightDreams", "nightdreams", "night dreams");
        passionCanopyBed = registerCanopyBed("passion", "passion", "passion");
        petalsCanopyBed = registerCanopyBed("petals", "petals", "petals");
        zebraCanopyBed = registerCanopyBed("zebra", "zebra", "zebra");
    }

    private static ItemTemplate registerFullCanopyBed(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.bed.canopy.full.%s", id))
                .name(String.format("full canopy bed %s pattern", name), String.format("full canopy beds %s pattern", name), "An inviting canopy bed fit for royalty.")
                .modelName(String.format("model.furniture.bed.canopy2.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_DECO_BED_STANDARD)
                .itemTypes(new short[]{
                        ITEM_TYPE_ONE_PER_TILE,
                        ITEM_TYPE_NAMED,
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_NOTAKE,
                        ITEM_TYPE_USE_GROUND_ONLY,
                        ITEM_TYPE_OWNER_DESTROYABLE,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_BED,
                        ITEM_TYPE_INSIDE_ONLY,
                        ITEM_TYPE_TRANSPORTABLE,
                        ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION
                })
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .dimensions(87, 50, 81)
                .weightGrams(60000)
                .value(25000)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(70f)
                .isTraded(false)
                .build();
    }

    private static void registerFullCanopyBeds() throws IOException {
        basicFullCanopyBed = registerFullCanopyBed("basic", null, "basic");
        flowersRedFullCanopyBed = registerFullCanopyBed("flowers.red", "flowersred", "red flowers");
        kingFullCanopyBed = registerFullCanopyBed("king", "king", "king");
        melodyFullCanopyBed = registerFullCanopyBed("melody", "melody", "melody");
        nightDreamsFullCanopyBed = registerFullCanopyBed("nightDreams", "nightdreams", "night dreams");
        passionFullCanopyBed = registerFullCanopyBed("passion", "passion", "passion");
        petalsFullCanopyBed = registerFullCanopyBed("petals", "petals", "petals");
        zebraFullCanopyBed = registerFullCanopyBed("zebra", "zebra", "zebra");
    }

    private static ItemTemplate registerLargeBed(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.bed.large.%s", id))
                .name(String.format("large bed %s pattern", name), String.format("large beds %s pattern", name), "A larger than average bed that looks very comfortable.")
                .modelName(String.format("model.furniture.bed.large.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_DECO_BED_STANDARD)
                .itemTypes(new short[]{
                        ITEM_TYPE_ONE_PER_TILE,
                        ITEM_TYPE_NAMED,
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_NOTAKE,
                        ITEM_TYPE_USE_GROUND_ONLY,
                        ITEM_TYPE_OWNER_DESTROYABLE,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_BED,
                        ITEM_TYPE_INSIDE_ONLY,
                        ITEM_TYPE_TRANSPORTABLE,
                        ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION
                })
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .dimensions(87, 50, 81)
                .weightGrams(40000)
                .value(15000)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(60f)
                .isTraded(false)
                .build();
    }

    private static void registerLargeBeds() throws IOException {
        basicLargeBed = registerLargeBed("basic", null, "basic");
        flowersRedLargeBed = registerLargeBed("flowers.red", "flowersred", "red flowers");
        kingLargeBed = registerLargeBed("king", "king", "king");
        melodyLargeBed = registerLargeBed("melody", "melody", "melody");
        nightDreamsLargeBed = registerLargeBed("nightDreams", "nightdreams", "night dreams");
        passionLargeBed = registerLargeBed("passion", "passion", "passion");
        petalsLargeBed = registerLargeBed("petals", "petals", "petals");
        zebraLargeBed = registerLargeBed("zebra", "zebra", "zebra");
    }

    private static ItemTemplate registerTable(String id, String modelSuffix, String name, String desc, int itemSize, int wieght, float difficulty, byte material) throws IOException {
        long decayTime = TimeConstants.BASEDECAY;
        if (material == ItemMaterials.MATERIAL_WOOD_BIRCH) {
            decayTime = TimeConstants.DECAYTIME_WOOD;
        } else if (material == ItemMaterials.MATERIAL_STONE | material == ItemMaterials.MATERIAL_MARBLE |
                material == ItemMaterials.MATERIAL_SLATE | material == ItemMaterials.MATERIAL_SANDSTONE) {
            decayTime = TimeConstants.DECAYTIME_STONE;
        }
        return new ItemTemplateBuilder(String.format("jubaroo.item.table.%s", id))
                .name(String.format("%s table", name), String.format("%s tables", name), desc)
                .modelName(String.format("model.furniture.table.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_DECO_BED_STANDARD)
                .itemTypes(new short[]{
                        ITEM_TYPE_ONE_PER_TILE,
                        ITEM_TYPE_NAMED,
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_NOTAKE,
                        ITEM_TYPE_USE_GROUND_ONLY,
                        ITEM_TYPE_OWNER_DESTROYABLE,
                        ITEM_TYPE_HASDATA,
                        ITEM_TYPE_BED,
                        ITEM_TYPE_INSIDE_ONLY,
                        ITEM_TYPE_TRANSPORTABLE,
                        ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION
                })
                .decayTime(decayTime)
                .dimensions(10, 60, 250)
                .weightGrams(wieght)
                .size(itemSize)
                .value(10000)
                .material(material)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(difficulty)
                .isTraded(false)
                .build();
    }

    private static void registerTables() throws IOException {
        fineDiningTable = registerTable("fine.dining", "large.fine", "fine dining", "A large fancy square dinner table.", ItemSizes.ITEM_SIZE_LARGE, 18000, 30f, ItemMaterials.MATERIAL_WOOD_BIRCH);
        longRectangularMarbleTable = registerTable("square.marble.long", "square.marble.long", "long rectangular marble", "A sturdy long rectangular table made from marble.", ItemSizes.ITEM_SIZE_MEDIUM, 100000, 45f, ItemMaterials.MATERIAL_MARBLE);
    }

    private static ItemTemplate registerSmallCarpet(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.carpet.small.%s", id))
                .name(String.format("carpet %s", name), String.format("carpet %ss", name), "This carpet adds a nice patch of colour to any house or castle.")
                .modelName(String.format("model.decoration.colorful.carpet.small.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_CARPET_MEDITATION)
                .itemTypes(new short[]{
                        ITEM_TYPE_CLOTH,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_CARPET,
                        ITEM_TYPE_PLANTABLE
                })
                .decayTime(TimeConstants.DECAYTIME_CLOTHING)
                .dimensions(200, 120, 5)
                .weightGrams(3000)
                .size(ItemSizes.ITEM_SIZE_SMALL)
                .value(3000)
                .material(ItemMaterials.MATERIAL_COTTON)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(20f)
                .isTraded(true)
                .build();
    }

    private static void registerSmallCarpets() throws IOException {
        smallCarpet01 = registerSmallCarpet("01", "01", "01");
        smallCarpet02 = registerSmallCarpet("02", "02", "02");
        smallCarpet03 = registerSmallCarpet("03", "03", "03");
        smallCarpet04 = registerSmallCarpet("04", "04", "04");
        smallCarpet05 = registerSmallCarpet("05", "05", "05");
        smallCarpet06 = registerSmallCarpet("06", "06", "06");
        smallCarpet07 = registerSmallCarpet("07", "07", "07");
        smallCarpet08 = registerSmallCarpet("08", "08", "08");
        smallCarpet09 = registerSmallCarpet("09", "09", "09");
        smallCarpet10 = registerSmallCarpet("10", "10", "10");
        woodlandCamouflageSmallCarpet = registerSmallCarpet("camo.woodland", "woodlandcamo", "woodland camouflage");
    }

    private static ItemTemplate registerSmallOvalCarpet(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.carpet.oval.%s", id))
                .name(String.format("oval carpet %s", name), String.format("oval carpet %ss", name), "This oval carpet adds a nice patch of colour to any house or castle.")
                .modelName(String.format("model.decoration.colorful.carpet.oval.small.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_CARPET_MEDITATION)
                .itemTypes(new short[]{
                        ITEM_TYPE_CLOTH,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_CARPET,
                        ITEM_TYPE_PLANTABLE
                })
                .decayTime(TimeConstants.DECAYTIME_CLOTHING)
                .dimensions(200, 120, 5)
                .weightGrams(3000)
                .value(3000)
                .material(ItemMaterials.MATERIAL_COTTON)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(20f)
                .isTraded(true)
                .build();
    }

    private static void registerSmallOvalCarpets() throws IOException {
        smallOvalCarpet01 = registerSmallOvalCarpet("01", "01", "01");
        smallOvalCarpet02 = registerSmallOvalCarpet("02", "02", "02");
    }

    private static ItemTemplate registerCarpetRunner(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.carpet.runner.%s", id))
                .name(String.format("carpet runner %s", name), String.format("carpet runner %ss", name), "This carpet runner adds a nice patch of colour to any house or castle.")
                .modelName(String.format("model.decoration.colorful.carpet.runner.small.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_CARPET_MEDITATION)
                .itemTypes(new short[]{
                        ITEM_TYPE_CLOTH,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_CARPET,
                        ITEM_TYPE_PLANTABLE
                })
                .decayTime(TimeConstants.DECAYTIME_CLOTHING)
                .dimensions(200, 120, 5)
                .weightGrams(3000)
                .value(3000)
                .material(ItemMaterials.MATERIAL_COTTON)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(20f)
                .isTraded(true)
                .build();
    }

    private static void registerCarpetRunners() throws IOException {
        smallCarpetRunner01 = registerCarpetRunner("01", "01", "01");
        smallCarpetRunner02 = registerCarpetRunner("02", "02", "02");
        smallCarpetRunner03 = registerCarpetRunner("03", "03", "03");
        smallCarpetRunner04 = registerCarpetRunner("04", "04", "04");
        smallCarpetRunner05 = registerCarpetRunner("05", "05", "05");
    }

    private static ItemTemplate registerPicture(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.picture.%s", id))
                .name(String.format("%s picture", name), String.format("%s pictures", name), "A small picture.")
                .modelName(String.format("model.decoration.picture.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[]{
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_USE_GROUND_ONLY,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_NOT_MISSION,
                        ITEM_TYPE_PLANTABLE,
                        ITEM_TYPE_NEVER_SHOW_CREATION_WINDOW_OPTION,
                        ITEM_TYPE_USES_SPECIFIED_CONTAINER_VOLUME,
                        ITEM_TYPE_TURNABLE
                })
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .dimensions(70, 70, 84)
                .weightGrams(12000)
                .size(ItemSizes.ITEM_SIZE_SMALL)
                .value(6000)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(35f)
                .isTraded(true)
                .build();
    }

    private static void registerPictures() throws IOException {
        naturePicture = registerPicture("nature", "nature", "nature");
        claudePicture = registerPicture("claude", "claude", "Claude");
        deerPicture = registerPicture("deer", "deer", "deer");
        peasantsPicture = registerPicture("peasants", "peasants", "peasants");
        shepardPicture = registerPicture("shepherd", "shepherd", "shepherd");
        snowdriftPicture = registerPicture("snowdrift", "snowdrift", "snowdrift");
        streamPicture = registerPicture("stream", "stream", "stream");
        timePicture = registerPicture("time", "time", "time");
        valleyPicture = registerPicture("valley", "valley", "valley");
    }

    private static ItemTemplate registerCurtain(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.curtain.%s", id))
                .name(String.format("%s curtain", name), String.format("%s curtains", name), "This curtain adds a nice patch of colour to any house or castle.")
                .modelName(String.format("model.decoration.colorful.curtain.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_CLOTH_BOLT)
                .itemTypes(new short[]{
                        ITEM_TYPE_CLOTH,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_TURNABLE,
                        ITEM_TYPE_IMPROVEITEM,
                        ITEM_TYPE_PLANTABLE
                })
                .decayTime(TimeConstants.DECAYTIME_CLOTHING)
                .dimensions(200, 120, 5)
                .weightGrams(3000)
                .size(ItemSizes.ITEM_SIZE_SMALL)
                .value(3000)
                .material(ItemMaterials.MATERIAL_COTTON)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(20f)
                .isTraded(true)
                .build();
    }

    private static void registerCurtains() throws IOException {
        basicCurtain = registerCurtain("basic", null, "basic");
        flowersRedCurtain = registerCurtain("flowersred", "flowersred", "red flowers");
        kingCurtain = registerCurtain("king", "king", "king");
        melodyCurtain = registerCurtain("melody", "melody", "melody");
        nightDreamsCurtain = registerCurtain("nightDreams", "nightdreams", "night dreams");
        passionCurtain = registerCurtain("passion", "passion", "passion");
        petalsCurtain = registerCurtain("petals", "petals", "petals");
        zebraCurtain = registerCurtain("zebra", "zebra", "zebra");
        championCurtain = registerCurtain("champion", "champion", "champion");
        velvetGreenCurtain = registerCurtain("velvetGreen", "velvetgreen", "velvet green");
        creamCurtain = registerCurtain("cream", "cream", "cream");
        purpleNatureCurtain = registerCurtain("purpleNature", "purplenature", "purpl enature");
        redCurtain = registerCurtain("red", "red", "red");
        greenCurtain = registerCurtain("green", "green", "green");
        tasseledBlueCurtain = registerCurtain("tasseled.blue", "tasseledblue", "blue tasseled");
        tasseledRedCurtain = registerCurtain("tasseled.red", "tasseledred", "red tasseled");
        fancyRedCurtain = registerCurtain("fancy.red", "fancyred", "fancy red");
        fancyGreenCurtain = registerCurtain("fancy.green", "fancygreen", "fancy green");
        blackPatternCurtain = registerCurtain("pattern.black", "blackpattern", "black patterned");
        camoCurtain = registerCurtain("camo", "camo", "camo");
        woodlandCamoCurtain = registerCurtain("camo.woodland", "woodlandcamo", "woodland camouflage");
    }

    private static ItemTemplate registerTaxidermyHead(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.taxidermy.head.%s", id))
                .name(String.format("%s head", name), String.format("%s heads", name), "The severed head of an animal.")
                .modelName(String.format("model.animal.head.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[]{
                        ITEM_TYPE_BUTCHERED,
                        ITEM_TYPE_FOOD
                })
                .decayTime(TimeConstants.DECAYTIME_CORPSE)
                .dimensions(5, 5, 5)
                .weightGrams(25000)
                .value(4000)
                .material(ItemMaterials.MATERIAL_ANIMAL)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(85f)
                .isTraded(false)
                .nutritionValues(500, 200, 370, 300)
                .build();
    }

    private static void registerTaxidermyHeads() throws IOException {
        blackBearTaxidermyHead = registerTaxidermyHead("bear.black", "blackbear", "black bear");
        brownBearTaxidermyHead = registerTaxidermyHead("bear.brown", "brownbear", "brown bear");
        bisonTaxidermyHead = registerTaxidermyHead("bison", "bison", "bison");
        stagTaxidermyHead = registerTaxidermyHead("stag", "stag", "stag");
        wildcatTaxidermyHead = registerTaxidermyHead("wildcat", "wildcat", "wildcat");
        hellHoundTaxidermyHead = registerTaxidermyHead("hell.hound", "hellhound", "hell hound");
        lionTaxidermyHead = registerTaxidermyHead("lion", "lion", "lion");
        hyenaTaxidermyHead = registerTaxidermyHead("hyena", "hyena", "hyena");
        boarTaxidermyHead = registerTaxidermyHead("boar", "boar", "boar");
        wolfTaxidermyHead = registerTaxidermyHead("wolf", "wolf", "wolf");
        worgTaxidermyHead = registerTaxidermyHead("worg", "worg", "worg");
    }

    private static ItemTemplate registerTaxidermyHeadTrophy(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.taxidermy.trophy.head.%s", id))
                .name(String.format("%s trophy", name), String.format("%s trophies", name), "A beautiful trophy of a particularly fine specimen of this animals species.")
                .modelName(String.format("model.furniture.trophy.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[]{
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_PLANTABLE
                })
                .decayTime(TimeConstants.DECAYTIME_CORPSE)
                .dimensions(25, 25, 25)
                .weightGrams(20000)
                .value(12000)
                .material(ItemMaterials.MATERIAL_LEATHER)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(40f)
                .isTraded(true)
                .build();
    }

    private static void registerTaxidermyHeadTrophies() throws IOException {
        blackBearTaxidermyHeadTrophy = registerTaxidermyHeadTrophy("bear.black", "blackbear", "black bear");
        brownBearTaxidermyHeadTrophy = registerTaxidermyHeadTrophy("bear.brown", "brownbear", "brown bear");
        bisonTaxidermyHeadTrophy = registerTaxidermyHeadTrophy("bison", "bison", "bison");
        stagTaxidermyHeadTrophy = registerTaxidermyHeadTrophy("stag", "stag", "stag");
        wildcatTaxidermyHeadTrophy = registerTaxidermyHeadTrophy("wildcat", "wildcat", "wildcat");
        hellHoundTaxidermyHeadTrophy = registerTaxidermyHeadTrophy("hell.hound", "hellhound", "hell hound");
        lionTaxidermyHeadTrophy = registerTaxidermyHeadTrophy("lion", "lion", "lion");
        hyenaTaxidermyHeadTrophy = registerTaxidermyHeadTrophy("hyena", "hyena", "hyena");
        boarTaxidermyHeadTrophy = registerTaxidermyHeadTrophy("boar", "boar", "boar");
        wolfTaxidermyHeadTrophy = registerTaxidermyHeadTrophy("wolf", "wolf", "wolf");
        worgTaxidermyHeadTrophy = registerTaxidermyHeadTrophy("worg", "worg", "worg");
    }

    private static ItemTemplate registerTaxidermyStuffedHead(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.taxidermy.stuffed.head.%s", id))
                .name(String.format("stuffed %s head", name), String.format("stuffed %s heads", name), String.format("A stuffed %s head.", name))
                .modelName(String.format("model.animal.stuffed.head.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[0])
                .decayTime(TimeConstants.DECAYTIME_CORPSE)
                .dimensions(10, 10, 10)
                .weightGrams(7500)
                .value(12000)
                .material(ItemMaterials.MATERIAL_LEATHER)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(40f)
                .isTraded(false)
                .build();
    }

    private static void registerTaxidermyStuffedHeads() throws IOException {
        blackBearTaxidermyStuffedHead = registerTaxidermyStuffedHead("bear.black", "blackbear", "black bear");
        brownBearTaxidermyStuffedHead = registerTaxidermyStuffedHead("bear.brown", "brownbear", "brown bear");
        bisonTaxidermyStuffedHead = registerTaxidermyStuffedHead("bison", "bison", "bison");
        stagTaxidermyStuffedHead = registerTaxidermyStuffedHead("stag", "stag", "stag");
        wildcatTaxidermyStuffedHead = registerTaxidermyStuffedHead("wildcat", "wildcat", "wildcat");
        hellHoundTaxidermyStuffedHead = registerTaxidermyStuffedHead("hell.hound", "hellhound", "hell hound");
        lionTaxidermyStuffedHead = registerTaxidermyStuffedHead("lion", "lion", "lion");
        hyenaTaxidermyStuffedHead = registerTaxidermyStuffedHead("hyena", "hyena", "hyena");
        boarTaxidermyStuffedHead = registerTaxidermyStuffedHead("boar", "boar", "boar");
        wolfTaxidermyStuffedHead = registerTaxidermyStuffedHead("wolf", "wolf", "wolf");
        worgTaxidermyStuffedHead = registerTaxidermyStuffedHead("worg", "worg", "worg");
    }

    private static ItemTemplate registerTaxidermyTannedHead(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.taxidermy.tanned.head.%s", id))
                .name(String.format("tanned %s head", name), String.format("tanned %s heads", name), String.format("A %s head that has been tanned.", name))
                .modelName(String.format("model.animal.tanned.head.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[0])
                .decayTime(TimeConstants.DECAYTIME_CORPSE)
                .dimensions(10, 10, 10)
                .weightGrams(7500)
                .value(12000)
                .material(ItemMaterials.MATERIAL_LEATHER)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(40f)
                .isTraded(false)
                .build();
    }

    private static void registerTaxidermyTannedHeads() throws IOException {
        blackBearTaxidermyTannedHead = registerTaxidermyTannedHead("bear.black", "blackbear", "black bear");
        brownBearTaxidermyTannedHead = registerTaxidermyTannedHead("bear.brown", "brownbear", "brown bear");
        bisonTaxidermyTannedHead = registerTaxidermyTannedHead("bison", "bison", "bison");
        stagTaxidermyTannedHead = registerTaxidermyTannedHead("stag", "stag", "stag");
        wildcatTaxidermyTannedHead = registerTaxidermyTannedHead("wildcat", "wildcat", "wildcat");
        hellHoundTaxidermyTannedHead = registerTaxidermyTannedHead("hell.hound", "hellhound", "hell hound");
        lionTaxidermyTannedHead = registerTaxidermyTannedHead("lion", "lion", "lion");
        hyenaTaxidermyTannedHead = registerTaxidermyTannedHead("hyena", "hyena", "hyena");
        boarTaxidermyTannedHead = registerTaxidermyTannedHead("boar", "boar", "boar");
        wolfTaxidermyTannedHead = registerTaxidermyTannedHead("wolf", "wolf", "wolf");
        worgTaxidermyTannedHead = registerTaxidermyTannedHead("worg", "worg", "worg");
    }

    private static ItemTemplate registerTaxidermyGroomedHead(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.taxidermy.groomed.head.%s", id))
                .name(String.format("groomed %s head", name), String.format("groomed %s heads", name), String.format("A %s head that has been groomed.", name))
                .modelName(String.format("model.animal.groomed.head.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[0])
                .decayTime(TimeConstants.DECAYTIME_CORPSE)
                .dimensions(10, 10, 10)
                .weightGrams(6000)
                .value(12000)
                .material(ItemMaterials.MATERIAL_LEATHER)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(40f)
                .isTraded(false)
                .build();
    }

    private static void registerTaxidermyGroomedHeads() throws IOException {
        blackBearTaxidermyGroomedHead = registerTaxidermyGroomedHead("bear.black", "blackbear", "black bear");
        brownBearTaxidermyGroomedHead = registerTaxidermyGroomedHead("bear.brown", "brownbear", "brown bear");
        bisonTaxidermyGroomedHead = registerTaxidermyGroomedHead("bison", "bison", "bison");
        stagTaxidermyGroomedHead = registerTaxidermyGroomedHead("stag", "stag", "stag");
        wildcatTaxidermyGroomedHead = registerTaxidermyGroomedHead("wildcat", "wildcat", "wildcat");
        hellHoundTaxidermyGroomedHead = registerTaxidermyGroomedHead("hell.hound", "hellhound", "hell hound");
        lionTaxidermyGroomedHead = registerTaxidermyGroomedHead("lion", "lion", "lion");
        hyenaTaxidermyGroomedHead = registerTaxidermyGroomedHead("hyena", "hyena", "hyena");
        boarTaxidermyGroomedHead = registerTaxidermyGroomedHead("boar", "boar", "boar");
        wolfTaxidermyGroomedHead = registerTaxidermyGroomedHead("wolf", "wolf", "wolf");
        worgTaxidermyGroomedHead = registerTaxidermyGroomedHead("worg", "worg", "worg");
    }

    private static ItemTemplate registerTaxidermyBody(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.taxidermy.body.%s", id))
                .name(String.format("%s body", name), String.format("%s bodies", name), "The intact body of an animal.")
                .modelName(String.format("model.corpse.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[]{
                        ITEM_TYPE_BUTCHERED
                })
                .decayTime(TimeConstants.DECAYTIME_CORPSE)
                .dimensions(160, 50, 50)
                .weightGrams(75000)
                .value(4000)
                .material(ItemMaterials.MATERIAL_ANIMAL)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(95f)
                .isTraded(false)
                .nutritionValues(500, 200, 370, 300)
                .build();
    }

    private static void registerTaxidermyBodies() throws IOException {
        blackBearTaxidermyBody = registerTaxidermyBody("bear.black", "blackbear", "black bear");
    }

    private static ItemTemplate registerTaxidermyBodyTrophy(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.taxidermy.body.trophy.%s", id))
                .name(String.format("%s body", name), String.format("%s bodies", name), "A full sized, stuffed animal trophy.")
                .modelName(String.format("model.furniture.trophy.%s.full.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[]{
                        ITEM_TYPE_DECORATION,
                        ITEM_TYPE_WOOD,
                        ITEM_TYPE_DESTROYABLE,
                        ITEM_TYPE_IMPROVEITEM,
                        ITEM_TYPE_REPAIRABLE,
                        ITEM_TYPE_PLANTABLE
                })
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .dimensions(160, 100, 100)
                .weightGrams(76000)
                .value(30000)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(50f)
                .isTraded(true)
                .build();
    }

    private static void registerTaxidermyBodyTrophies() throws IOException {
        blackBearTaxidermyTrophyBody = registerTaxidermyBodyTrophy("bear.black", "blackbear", "black bear");
    }

    private static ItemTemplate registerTaxidermyStuffedBody(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.taxidermy.body.stuffed.%s", id))
                .name(String.format("stuffed %s body", name), String.format("%s bodies", name), "The intact body of an animal. It has been filled with stuffing for taxidermy purposes.")
                .modelName(String.format("model.corpse.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[0])
                .decayTime(TimeConstants.DECAYTIME_CORPSE)
                .dimensions(160, 50, 50)
                .weightGrams(7500)
                .value(10000)
                .material(ItemMaterials.MATERIAL_ANIMAL)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(50f)
                .isTraded(false)
                .build();
    }

    private static void registerTaxidermyStuffedBodies() throws IOException {
        blackBearTaxidermyStuffedBody = registerTaxidermyStuffedBody("bear.black", "blackbear", "black bear");
    }

    private static ItemTemplate registerTaxidermyTannedBody(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.taxidermy.body.tanned.%s", id))
                .name(String.format("tanned %s body", name), String.format("%s bodies", name), "The intact body of an animal. It has been filled with stuffing for taxidermy purposes.")
                .modelName(String.format("model.corpse.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[0])
                .decayTime(TimeConstants.DECAYTIME_CORPSE)
                .dimensions(160, 50, 50)
                .weightGrams(7500)
                .value(10000)
                .material(ItemMaterials.MATERIAL_ANIMAL)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(50f)
                .isTraded(false)
                .build();
    }

    private static void registerTaxidermyTannedBodies() throws IOException {
        blackBearTaxidermyTannedBody = registerTaxidermyTannedBody("bear.black", "blackbear", "black bear");
    }

    private static ItemTemplate registerTaxidermyGroomedBody(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.taxidermy.body.groomed.%s", id))
                .name(String.format("groomed %s body", name), String.format("%s bodies", name), "The intact body of an animal. It has been filled with stuffing for taxidermy purposes.")
                .modelName(String.format("model.corpse.%s.", modelSuffix))
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[0])
                .decayTime(TimeConstants.DECAYTIME_CORPSE)
                .dimensions(160, 50, 50)
                .weightGrams(7500)
                .value(10000)
                .material(ItemMaterials.MATERIAL_ANIMAL)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(50f)
                .isTraded(false)
                .build();
    }

    private static void registerTaxidermyGroomedBodies() throws IOException {
        blackBearTaxidermyGroomedBody = registerTaxidermyGroomedBody("bear.black", "blackbear", "black bear");
    }

    private static ItemTemplate registerCustomArmor(String id, String modelSuffix, String name, String description, short icon, short[] itemTypes, long decayTime, byte[] bodySpaces, int weight, int value, byte material, float difficulty) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.armor.%s", id))
                .name(name, String.format("%ss", name), description)
                .modelName(String.format("model.%s.", modelSuffix))
                .imageNumber(icon)
                .itemTypes(itemTypes)
                .decayTime(decayTime)
                .bodySpaces(bodySpaces)
                .dimensions(2, 40, 40)
                .weightGrams(weight)
                .value(value)
                .material(material)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(difficulty)
                .isTraded(false)
                .build();
    }

    private static void registerCustomArmors() throws IOException {
        short[] glimmerscaleTypes = new short[]{
                ItemTypes.ITEM_TYPE_NAMED,
                ItemTypes.ITEM_TYPE_REPAIRABLE,
                ItemTypes.ITEM_TYPE_METAL,
                ItemTypes.ITEM_TYPE_COLORABLE,
                ItemTypes.ITEM_TYPE_ARMOUR,
                ItemTypes.ITEM_TYPE_DRAGONARMOUR
        };
        short[] spectralTypes = new short[]{
                ItemTypes.ITEM_TYPE_NAMED,
                ItemTypes.ITEM_TYPE_REPAIRABLE,
                ItemTypes.ITEM_TYPE_LEATHER,
                ItemTypes.ITEM_TYPE_COLORABLE,
                ItemTypes.ITEM_TYPE_ARMOUR,
                ItemTypes.ITEM_TYPE_DRAGONARMOUR
        };
        glimmerscaleBoot = registerCustomArmor("glimmerscale.boot", "armour.foot.dragon.scale.leather", "glimmerscale boot", "A glimmerscale boot.", (short) IconConstants.ICON_ARMOR_FOOT_SCALE_DRAGON, glimmerscaleTypes, TimeConstants.DECAYTIME_NEVER, new byte[]{BodyPartConstants.LEFT_FOOT, BodyPartConstants.RIGHT_FOOT}, 2000, 1000000, Materials.MATERIAL_GLIMMERSTEEL, 72f);
        glimmerscaleGlove = registerCustomArmor("glimmerscale.glove", "armour.hand.dragon.scale.leather", "glimmerscale glove", "A glimmerscale glove.", (short) IconConstants.ICON_ARMOR_HAND_SCALE_DRAGON, glimmerscaleTypes, TimeConstants.DECAYTIME_NEVER, new byte[]{BodyPartConstants.LEFT_HAND, BodyPartConstants.RIGHT_HAND}, 1200, 1000000, Materials.MATERIAL_GLIMMERSTEEL, 70f);
        glimmerscaleHelmet = registerCustomArmor("glimmerscale.helmet", "armour.head.greathelmHornedOfGold", "glimmerscale helmet", "A horned helmet made of glimmerscale.", (short) IconConstants.ICON_ARMOR_HEAD_METAL_GREAT, glimmerscaleTypes, TimeConstants.DECAYTIME_NEVER, new byte[]{BodyPartConstants.HEAD, BodyPartConstants.SECOND_HEAD}, 2500, 1000000, Materials.MATERIAL_GLIMMERSTEEL, 76f);
        glimmerscaleLeggings = registerCustomArmor("glimmerscale.leggings", "armour.leg.dragon.scale.leather", "glimmerscale leggings", "A set of glimmerscale leggings.", (short) IconConstants.ICON_ARMOR_LEG_SCALE_DRAGON, glimmerscaleTypes, TimeConstants.DECAYTIME_NEVER, new byte[]{BodyPartConstants.LEGS}, 3200, 1000000, Materials.MATERIAL_GLIMMERSTEEL, 78f);
        glimmerscaleSleeve = registerCustomArmor("glimmerscale.sleeve", "armour.sleeve.dragon.scale.leather", "glimmerscale sleeve", "A glimmerscale sleeve.", (short) IconConstants.ICON_ARMOR_ARM_SCALE_DRAGON, glimmerscaleTypes, TimeConstants.DECAYTIME_NEVER, new byte[]{BodyPartConstants.LEFT_ARM, BodyPartConstants.RIGHT_ARM}, 1800, 1000000, Materials.MATERIAL_GLIMMERSTEEL, 74f);
        glimmerscaleVest = registerCustomArmor("glimmerscale.vest", "armour.torso.dragon.scale.leather", "glimmerscale vest", "A glimmerscale vest.", (short) IconConstants.ICON_ARMOR_TORSO_SCALE_DRAGON, glimmerscaleTypes, TimeConstants.DECAYTIME_NEVER, new byte[]{BodyPartConstants.TORSO}, 4500, 1000000, Materials.MATERIAL_GLIMMERSTEEL, 80f);
        spectralBoot = registerCustomArmor("spectral.boot", "armour.foot.dragon", "spectral boot", "A spectral boot.", (short) IconConstants.ICON_ARMOR_FOOT_LEATHER_DRAKE, spectralTypes, TimeConstants.DECAYTIME_NEVER, new byte[]{BodyPartConstants.LEFT_FOOT, BodyPartConstants.RIGHT_FOOT}, 300, 1000000, Materials.MATERIAL_LEATHER, 70f);
        spectralGlove = registerCustomArmor("spectral.glove", "armour.hand.dragon", "spectral glove", "A spectral glove.", (short) IconConstants.ICON_ARMOR_HAND_LEATHER_DRAKE, spectralTypes, TimeConstants.DECAYTIME_NEVER, new byte[]{BodyPartConstants.LEFT_HAND, BodyPartConstants.RIGHT_HAND}, 250, 1000000, Materials.MATERIAL_LEATHER, 68f);
        spectralCap = registerCustomArmor("spectral.cap", "armour.head.dragon", "spectral cap", "A spectral cap.", (short) IconConstants.ICON_ARMOR_HEAD_LEATHER_DRAKE, spectralTypes, TimeConstants.DECAYTIME_NEVER, new byte[]{BodyPartConstants.HEAD, BodyPartConstants.SECOND_HEAD}, 500, 1000000, Materials.MATERIAL_LEATHER, 74f);
        spectralLeggings = registerCustomArmor("spectral.leggings", "armour.leg.dragon", "spectral leggings", "A set of spectral leggings.", (short) IconConstants.ICON_ARMOR_LEG_LEATHER_DRAKE, spectralTypes, TimeConstants.DECAYTIME_NEVER, new byte[]{BodyPartConstants.LEGS}, 700, 1000000, Materials.MATERIAL_LEATHER, 77f);
        spectralSleeve = registerCustomArmor("spectral.sleeve", "armour.sleeve.dragon", "spectral sleeve", "A spectral sleeve.", (short) IconConstants.ICON_ARMOR_ARM_LEATHER_DRAKE, spectralTypes, TimeConstants.DECAYTIME_NEVER, new byte[]{BodyPartConstants.LEFT_ARM, BodyPartConstants.RIGHT_ARM}, 400, 1000000, Materials.MATERIAL_LEATHER, 72f);
        spectralVest = registerCustomArmor("spectral.vest", "armour.torso.dragon", "spectral vest", "A spectral vest.", (short) IconConstants.ICON_ARMOR_TORSO_LEATHER_DRAKE, spectralTypes, TimeConstants.DECAYTIME_NEVER, new byte[]{BodyPartConstants.TORSO}, 800, 1000000, Materials.MATERIAL_LEATHER, 80f);
        dragonSkullHelmet = registerCustomArmor("helm.skull.dragon", "resource.skull", "dragon skull helm", "A dragon skull helmet.", (short) IconConstants.ICON_BODY_SKULL, new short[]{ItemTypes.ITEM_TYPE_NAMED, ItemTypes.ITEM_TYPE_REPAIRABLE, ItemTypes.ITEM_TYPE_ARMOUR, ItemTypes.ITEM_TYPE_COLORABLE}, TimeConstants.DECAYTIME_STEEL, new byte[]{BodyPartConstants.HEAD, BodyPartConstants.SECOND_HEAD}, 800, 10000, Materials.MATERIAL_BONE, 30f);
        horsemanHelm = registerCustomArmor("helm.horseman", "armour.head.horsmanHelm", "horseman helm", "A helmet from the headless horseman. It seems to make your head disappear when worn.", (short) IconConstants.ICON_ARMOR_HEAD_METAL_BASINET, new short[]{ItemTypes.ITEM_TYPE_NAMED, ItemTypes.ITEM_TYPE_METAL, ItemTypes.ITEM_TYPE_ARMOUR}, TimeConstants.DECAYTIME_STEEL, new byte[]{BodyPartConstants.HEAD, BodyPartConstants.SECOND_HEAD}, 3000, 10000, Materials.MATERIAL_STEEL, 99f);
    }

    private static ItemTemplate registerCache(String id, String name, String description) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.item.cache.%s", id))
                .name(name, String.format("%ss", name), description)
                .modelName("model.container.giftbox.")
                .imageNumber((short) IconConstants.ICON_CONTAINER_GIFT)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_MAGIC,
                        ItemTypes.ITEM_TYPE_FULLPRICE,
                        ItemTypes.ITEM_TYPE_NOSELLBACK
                })
                .decayTime(TimeConstants.DECAYTIME_VALUABLE)
                .dimensions(5, 5, 5)
                .weightGrams(3500)
                .value(1000)
                .material(Materials.MATERIAL_GOLD)
                .behaviourType(BehaviourList.itemBehaviour)
                .difficulty(200f)
                .isTraded(false)
                .build();
    }

    private static void registerCaches() throws IOException {
        animalCache = registerCache("animal", "animal cache", "A cache containing a random animal. It might even be a unique creature.");
        armourCache = registerCache("armour", "armour cache", "A cache of armour. This armour may contain special properties.");
        artifactCache = registerCache("artifact", "artifact cache", "A cache containing a special artifact.");
        crystalCache = registerCache("crystal", "crystal cache", "A cache of magical crystals.");
        dragonCache = registerCache("dragon", "dragon cache", "A cache of dragon material. May also contain drake hide or dragonscale armour.");
        gemCache = registerCache("gem", "gem cache", "A cache of gems. May also contain star gems.");
        moonCache = registerCache("moon", "moon cache", "A cache of moon metals. May also contain items created from moon metals.");
        potionCache = registerCache("potion", "potion cache", "A cache of potions.");
        riftCache = registerCache("rift", "rift cache", "A cache of rift material.");
        titanCache = registerCache("titan", "titan cache", "A cache of treasures from a titan.");
        toolCache = registerCache("tool", "tool cache", "A cache containing a few tools. These tools could have special properties.");
        treasureMapCache = registerCache("map.treasure", "treasure map cache", "A cache containing a new treasure map.");
        weaponCache = registerCache("weapon", "weapon cache", "A cache containing a few weapons. These weapons could have special properties.");
        Caches.CACHE_IDS.add(CustomItems.animalCache.getTemplateId());
        Caches.CACHE_IDS.add(CustomItems.armourCache.getTemplateId());
        Caches.CACHE_IDS.add(CustomItems.artifactCache.getTemplateId());
        Caches.CACHE_IDS.add(CustomItems.crystalCache.getTemplateId());
        Caches.CACHE_IDS.add(CustomItems.dragonCache.getTemplateId());
        Caches.CACHE_IDS.add(CustomItems.gemCache.getTemplateId());
        Caches.CACHE_IDS.add(CustomItems.moonCache.getTemplateId());
        Caches.CACHE_IDS.add(CustomItems.potionCache.getTemplateId());
        Caches.CACHE_IDS.add(CustomItems.riftCache.getTemplateId());
        Caches.CACHE_IDS.add(CustomItems.titanCache.getTemplateId());
        Caches.CACHE_IDS.add(CustomItems.toolCache.getTemplateId());
        Caches.CACHE_IDS.add(CustomItems.treasureMapCache.getTemplateId());
        Caches.CACHE_IDS.add(CustomItems.weaponCache.getTemplateId());
    }

}