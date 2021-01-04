package org.jubaroo.mods.wurm.server.vehicles;

import com.wurmonline.server.items.*;
import com.wurmonline.server.skills.SkillList;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.server.Constants;

import java.util.Arrays;
import java.util.List;

import static org.jubaroo.mods.wurm.server.vehicles.CustomVehicles.*;

public class CustomVehicleCreationEntries {

    public static boolean isPatreonVyseBoat(int templateId) {
        if (templateId == CustomVehicles.vyseCaravel.getTemplateId()) {
            return true;
        } else if (templateId == CustomVehicles.vyseCog.getTemplateId()) {
            return true;
        } else if (templateId == CustomVehicles.vyseCorbita.getTemplateId()) {
            return true;
        } else if (templateId == CustomVehicles.vyseKnarr.getTemplateId()) {
            return true;
        } else return templateId == CustomVehicles.vyseSailing.getTemplateId();
    }

    public static boolean isPatreonCallanishBoat(int templateId) {
        if (templateId == CustomVehicles.callanishCaravel.getTemplateId()) {
            return true;
        } else if (templateId == CustomVehicles.callanishCog.getTemplateId()) {
            return true;
        } else if (templateId == CustomVehicles.callanishCorbita.getTemplateId()) {
            return true;
        } else if (templateId == CustomVehicles.callanishKnarr.getTemplateId()) {
            return true;
        } else return templateId == CustomVehicles.callanishSailing.getTemplateId();
    }

    public static void createWagonCreationEntry(int templateId) {
        AdvancedCreationEntry entry = CreationEntryCreator.createAdvancedEntry(SkillList.CARPENTRY_FINE, ItemList.plank, ItemList.wheelAxleSmall, templateId, false, false, 0f, true, true, 0, 40.0D, CreationCategories.CARTS);
        entry.addRequirement(new CreationRequirement(1, ItemList.wheelAxleSmall, 1, true));
        entry.addRequirement(new CreationRequirement(2, ItemList.plank, 20, true));
        entry.addRequirement(new CreationRequirement(3, ItemList.shaft, 4, true));
        entry.addRequirement(new CreationRequirement(4, ItemList.nailsIronSmall, 10, true));
        entry.addRequirement(new CreationRequirement(5, ItemList.yoke, 2, true));
        if (templateId != CustomVehicles.loggingWagon.getTemplateId()) {
            entry.addRequirement(new CreationRequirement(6, ItemList.sheet, 2, true));
        }
        if (templateId == CustomVehicles.altarWagon.getTemplateId()) {
            entry.addRequirement(new CreationRequirement(6, ItemList.altarWood, 1, true));
        }
        if (templateId == CustomVehicles.vysePatreonWagon.getTemplateId()) {
            entry.addRequirement(new CreationRequirement(6, CustomItems.vysePatreonBlueprint.getTemplateId(), 1, false));
        }
        if (templateId == CustomVehicles.callanishPatreonWagon.getTemplateId()) {
            entry.addRequirement(new CreationRequirement(6, CustomItems.callanishPatreonBlueprint.getTemplateId(), 1, false));
        }
    }

    public static void createCaravelCreationEntry(int templateId) {
        final AdvancedCreationEntry entry = CreationEntryCreator.createAdvancedEntry(SkillList.SHIPBUILDING, ItemList.keelPart, ItemList.keelPart, templateId, false, false, 0f, true, true, CreationCategories.SHIPS);
        entry.addRequirement(new CreationRequirement(1, ItemList.keelPart, 3, true));
        entry.addRequirement(new CreationRequirement(2, ItemList.stern, 1, true));
        entry.addRequirement(new CreationRequirement(3, ItemList.hullPlank, 400, true));
        entry.addRequirement(new CreationRequirement(4, ItemList.tenon, 300, true));
        entry.addRequirement(new CreationRequirement(5, ItemList.pegWood, 600, true));
        entry.addRequirement(new CreationRequirement(6, ItemList.rigTriangular, 1, true));
        entry.addRequirement(new CreationRequirement(7, ItemList.ropeThick, 12, true));
        entry.addRequirement(new CreationRequirement(8, ItemList.ropeMooring, 8, true));
        entry.addRequirement(new CreationRequirement(9, ItemList.rudder, 1, true));
        entry.addRequirement(new CreationRequirement(10, ItemList.deckBoard, 80, true));
        entry.addRequirement(new CreationRequirement(11, ItemList.steeringWheel, 1, true));
        entry.addRequirement(new CreationRequirement(12, ItemList.tar, 150, true));
        entry.addRequirement(new CreationRequirement(13, ItemList.oar, 10, true));
        entry.addRequirement(new CreationRequirement(14, ItemList.belayingPin, 10, true));
        entry.addRequirement(new CreationRequirement(15, ItemList.rigSquareTall, 1, true));
        entry.addRequirement(new CreationRequirement(16, ItemList.rigSquare, 1, true));
        entry.addRequirement(new CreationRequirement(17, ItemList.rigSpinnaker, 1, true));
        if (isPatreonVyseBoat(templateId)) {
            entry.addRequirement(new CreationRequirement(18, CustomItems.vysePatreonBlueprint.getTemplateId(), 1, false));
        }
        if (isPatreonCallanishBoat(templateId)) {
            entry.addRequirement(new CreationRequirement(18, CustomItems.callanishPatreonBlueprint.getTemplateId(), 1, false));
        }

        entry.setIsEpicBuildMissionTarget(false);
    }

    private static void createCogCreationEntry(int templateId) {
        final AdvancedCreationEntry entry = CreationEntryCreator.createAdvancedEntry(SkillList.SHIPBUILDING, ItemList.keelPart, ItemList.keelPart, templateId, false, false, 0f, true, true, CreationCategories.SHIPS);
        entry.addRequirement(new CreationRequirement(1, ItemList.keelPart, 2, true));
        entry.addRequirement(new CreationRequirement(2, ItemList.stern, 1, true));
        entry.addRequirement(new CreationRequirement(3, ItemList.hullPlank, 300, true));
        entry.addRequirement(new CreationRequirement(4, ItemList.tenon, 200, true));
        entry.addRequirement(new CreationRequirement(5, ItemList.pegWood, 400, true));
        entry.addRequirement(new CreationRequirement(6, ItemList.rigSquareLarge, 1, true));
        entry.addRequirement(new CreationRequirement(7, ItemList.ropeThick, 8, true));
        entry.addRequirement(new CreationRequirement(8, ItemList.ropeMooring, 4, true));
        entry.addRequirement(new CreationRequirement(9, ItemList.rudder, 1, true));
        entry.addRequirement(new CreationRequirement(10, ItemList.deckBoard, 60, true));
        entry.addRequirement(new CreationRequirement(11, ItemList.tar, 50, true));
        entry.addRequirement(new CreationRequirement(12, ItemList.oar, 2, true));
        entry.addRequirement(new CreationRequirement(13, ItemList.belayingPin, 10, true));
        if (isPatreonVyseBoat(templateId)) {
            entry.addRequirement(new CreationRequirement(14, CustomItems.vysePatreonBlueprint.getTemplateId(), 1, false));
        }
        if (isPatreonCallanishBoat(templateId)) {
            entry.addRequirement(new CreationRequirement(14, CustomItems.callanishPatreonBlueprint.getTemplateId(), 1, false));
        }
        entry.setIsEpicBuildMissionTarget(false);
    }

    private static void createCorbitaCreationEntry(int templateId) {
        final AdvancedCreationEntry entry = CreationEntryCreator.createAdvancedEntry(SkillList.SHIPBUILDING, ItemList.keelPart, ItemList.keelPart, templateId, false, false, 0f, true, true, CreationCategories.SHIPS);
        entry.addRequirement(new CreationRequirement(1, ItemList.keelPart, 2, true));
        entry.addRequirement(new CreationRequirement(2, ItemList.stern, 1, true));
        entry.addRequirement(new CreationRequirement(3, ItemList.hullPlank, 200, true));
        entry.addRequirement(new CreationRequirement(4, ItemList.tenon, 200, true));
        entry.addRequirement(new CreationRequirement(5, ItemList.pegWood, 400, true));
        entry.addRequirement(new CreationRequirement(6, ItemList.rigSquare, 1, true));
        entry.addRequirement(new CreationRequirement(7, ItemList.ropeThick, 8, true));
        entry.addRequirement(new CreationRequirement(8, ItemList.ropeMooring, 4, true));
        entry.addRequirement(new CreationRequirement(9, ItemList.rudder, 2, true));
        entry.addRequirement(new CreationRequirement(10, ItemList.deckBoard, 40, true));
        entry.addRequirement(new CreationRequirement(11, ItemList.tar, 50, true));
        entry.addRequirement(new CreationRequirement(12, ItemList.oar, 2, true));
        entry.addRequirement(new CreationRequirement(13, ItemList.belayingPin, 10, true));
        entry.addRequirement(new CreationRequirement(14, ItemList.rigSpinnaker, 1, true));
        if (isPatreonVyseBoat(templateId)) {
            entry.addRequirement(new CreationRequirement(15, CustomItems.vysePatreonBlueprint.getTemplateId(), 1, false));
        }
        if (isPatreonCallanishBoat(templateId)) {
            entry.addRequirement(new CreationRequirement(15, CustomItems.callanishPatreonBlueprint.getTemplateId(), 1, false));
        }
        entry.setIsEpicBuildMissionTarget(false);
    }

    private static void createKnarrCreationEntry(int templateId) {
        final AdvancedCreationEntry entry = CreationEntryCreator.createAdvancedEntry(SkillList.SHIPBUILDING, ItemList.keelPart, ItemList.keelPart, templateId, false, false, 0f, true, true, CreationCategories.SHIPS);
        entry.addRequirement(new CreationRequirement(1, ItemList.keelPart, 3, true));
        entry.addRequirement(new CreationRequirement(2, ItemList.stern, 1, true));
        entry.addRequirement(new CreationRequirement(3, ItemList.hullPlank, 400, true));
        entry.addRequirement(new CreationRequirement(4, ItemList.tenon, 200, true));
        entry.addRequirement(new CreationRequirement(5, ItemList.pegWood, 400, true));
        entry.addRequirement(new CreationRequirement(6, ItemList.metalRivet, 200, true));
        entry.addRequirement(new CreationRequirement(7, ItemList.rigSquareYard, 1, true));
        entry.addRequirement(new CreationRequirement(8, ItemList.ropeThick, 8, true));
        entry.addRequirement(new CreationRequirement(9, ItemList.ropeMooring, 4, true));
        entry.addRequirement(new CreationRequirement(10, ItemList.rudder, 1, true));
        entry.addRequirement(new CreationRequirement(11, ItemList.deckBoard, 80, true));
        entry.addRequirement(new CreationRequirement(12, ItemList.tar, 100, true));
        entry.addRequirement(new CreationRequirement(13, ItemList.oar, 10, true));
        entry.addRequirement(new CreationRequirement(14, ItemList.belayingPin, 10, true));
        if (isPatreonVyseBoat(templateId)) {
            entry.addRequirement(new CreationRequirement(15, CustomItems.vysePatreonBlueprint.getTemplateId(), 1, false));
        }
        if (isPatreonCallanishBoat(templateId)) {
            entry.addRequirement(new CreationRequirement(15, CustomItems.callanishPatreonBlueprint.getTemplateId(), 1, false));
        }
        entry.setIsEpicBuildMissionTarget(false);
    }

    private static void createSailingCreationEntry(int templateId) {
        final AdvancedCreationEntry entry = CreationEntryCreator.createAdvancedEntry(SkillList.SHIPBUILDING, ItemList.keelPart, ItemList.keelPart, templateId, false, false, 0f, true, true, CreationCategories.SHIPS);
        entry.addRequirement(new CreationRequirement(1, ItemList.keelPart, 1, true));
        entry.addRequirement(new CreationRequirement(2, ItemList.stern, 1, true));
        entry.addRequirement(new CreationRequirement(3, ItemList.hullPlank, 50, true));
        entry.addRequirement(new CreationRequirement(4, ItemList.tenon, 50, true));
        entry.addRequirement(new CreationRequirement(5, ItemList.pegWood, 50, true));
        entry.addRequirement(new CreationRequirement(6, ItemList.ropeThick, 2, true));
        entry.addRequirement(new CreationRequirement(7, ItemList.ropeMooring, 1, true));
        entry.addRequirement(new CreationRequirement(8, ItemList.tar, 10, true));
        entry.addRequirement(new CreationRequirement(9, ItemList.oar, 2, true));
        entry.addRequirement(new CreationRequirement(10, ItemList.seat, 4, true));
        entry.addRequirement(new CreationRequirement(11, ItemList.rigTriangular, 1, true));
        entry.addRequirement(new CreationRequirement(12, ItemList.belayingPin, 4, true));
        if (isPatreonVyseBoat(templateId)) {
            entry.addRequirement(new CreationRequirement(13, CustomItems.vysePatreonBlueprint.getTemplateId(), 1, false));
        }
        if (isPatreonCallanishBoat(templateId)) {
            entry.addRequirement(new CreationRequirement(13, CustomItems.callanishPatreonBlueprint.getTemplateId(), 1, false));
        }
        entry.setIsEpicBuildMissionTarget(false);
    }

    public static final List<Integer> customCaravelList = Arrays.asList(
            blackLegionCaravel.getTemplateId(), crusadersCaravel.getTemplateId(), ebonauraCaravel.getTemplateId(), empireMolRehanCaravel.getTemplateId(), eagleCaravel.getTemplateId(),
            freedomCaravel.getTemplateId(), hotSCaravel.getTemplateId(), jennKellonCaravel.getTemplateId(), kingdomSolCaravel.getTemplateId(), macedoniaCaravel.getTemplateId(), molRehanCaravel.getTemplateId(),
            pandemoniumCaravel.getTemplateId(), pirateCaravel.getTemplateId(), requiemCaravel.getTemplateId(), romanEmpireCaravel.getTemplateId(), vyseCaravel.getTemplateId(), callanishCaravel.getTemplateId());

    public static final List<Integer> customCogList = Arrays.asList(
            blackLegionCog.getTemplateId(), crusadersCog.getTemplateId(), ebonauraCog.getTemplateId(), empireMolRehanCog.getTemplateId(), eagleCog.getTemplateId(),
            freedomCog.getTemplateId(), hotSCog.getTemplateId(), jennKellonCog.getTemplateId(), kingdomSolCog.getTemplateId(), macedoniaCog.getTemplateId(), molRehanCog.getTemplateId(),
            pandemoniumCog.getTemplateId(), pirateCog.getTemplateId(), requiemCog.getTemplateId(), romanEmpireCog.getTemplateId(), vyseCog.getTemplateId(), callanishCog.getTemplateId());

    public static final List<Integer> customCorbitaList = Arrays.asList(
            blackLegionCorbita.getTemplateId(), crusadersCorbita.getTemplateId(), ebonauraCorbita.getTemplateId(), empireMolRehanCorbita.getTemplateId(), eagleCorbita.getTemplateId(),
            freedomCorbita.getTemplateId(), hotSCorbita.getTemplateId(), jennKellonCorbita.getTemplateId(), kingdomSolCorbita.getTemplateId(), macedoniaCorbita.getTemplateId(), molRehanCorbita.getTemplateId(),
            pandemoniumCorbita.getTemplateId(), pirateCorbita.getTemplateId(), requiemCorbita.getTemplateId(), romanEmpireCorbita.getTemplateId(), vyseCorbita.getTemplateId(), callanishCorbita.getTemplateId());

    public static final List<Integer> customKnarrList = Arrays.asList(
            blackLegionKnarr.getTemplateId(), crusadersKnarr.getTemplateId(), ebonauraKnarr.getTemplateId(), empireMolRehanKnarr.getTemplateId(), eagleKnarr.getTemplateId(),
            freedomKnarr.getTemplateId(), hotSKnarr.getTemplateId(), jennKellonKnarr.getTemplateId(), kingdomSolKnarr.getTemplateId(), macedoniaKnarr.getTemplateId(), molRehanKnarr.getTemplateId(),
            pandemoniumKnarr.getTemplateId(), pirateKnarr.getTemplateId(), requiemKnarr.getTemplateId(), romanEmpireKnarr.getTemplateId(), vyseKnarr.getTemplateId(), callanishKnarr.getTemplateId());

    public static final List<Integer> customSailingBoatList = Arrays.asList(
            blackLegionCaravel.getTemplateId(), crusadersCaravel.getTemplateId(), ebonauraCaravel.getTemplateId(), empireMolRehanCaravel.getTemplateId(), eagleCaravel.getTemplateId(),
            freedomCaravel.getTemplateId(), hotSCaravel.getTemplateId(), jennKellonCaravel.getTemplateId(), kingdomSolCaravel.getTemplateId(), macedoniaCaravel.getTemplateId(), molRehanCaravel.getTemplateId(),
            pandemoniumCaravel.getTemplateId(), pirateCaravel.getTemplateId(), requiemCaravel.getTemplateId(), romanEmpireCaravel.getTemplateId(), vyseCaravel.getTemplateId(), callanishCaravel.getTemplateId());

    public static final List<Integer> customWagonList = Arrays.asList(
            jennKellonWagon.getTemplateId(), molRehanWagon.getTemplateId(), hotsWagon.getTemplateId(), dragonKingdomWagon.getTemplateId(), empireMolRehanWagon.getTemplateId(),
            blackLegionWagon.getTemplateId(), ebonauraWagon.getTemplateId(), kingdomSolWagon.getTemplateId(), romanRepublicWagon.getTemplateId(), valhallaWagon.getTemplateId(), valhallaDescendantsWagon.getTemplateId(),
            macedoniaWagon.getTemplateId(), dreadnoughtWagon.getTemplateId(), crusadersWagon.getTemplateId(), pandemoniumWagon.getTemplateId(), legionAnubisWagon.getTemplateId(), wurmUniversityWagon.getTemplateId(),
            skyBearWagon.getTemplateId(), dragonElfWagon.getTemplateId(), reaperWagon.getTemplateId(), blackWitchWagon.getTemplateId(), paladinWagon.getTemplateId(), whiteKnightWagon.getTemplateId(), elfNatureWagon.getTemplateId(),
            witchesWagon.getTemplateId(), deathAngelWagon.getTemplateId(), iceDragonWagon.getTemplateId(), bisonWagon.getTemplateId(), purpleDragonWagon.getTemplateId(), horsemanWarWagon.getTemplateId(), unicornWagon.getTemplateId(),
            theKnightWagon.getTemplateId(), dracoWagon.getTemplateId(), fairyGirlWagon.getTemplateId(), flowerWagon.getTemplateId(), spaceWagon.getTemplateId(), fairyDragonWagon.getTemplateId(), blackPatternWagon.getTemplateId(),
            purplePatternWagon.getTemplateId(), redPatternWagon.getTemplateId(), bluePatternWagon.getTemplateId(), yellowPlainWagon.getTemplateId(), limePlainWagon.getTemplateId(), greenPlainWagon.getTemplateId(),
            forestGreenPlainWagon.getTemplateId(), cyanPlainWagon.getTemplateId(), bluePlainWagon.getTemplateId(), navyBluePlainWagon.getTemplateId(), indigoPlainWagon.getTemplateId(), violetPlainWagon.getTemplateId(),
            magentaPlainWagon.getTemplateId(), hotPinkPlainWagon.getTemplateId(), redPlainWagon.getTemplateId(), orangePlainWagon.getTemplateId(), brownPlainWagon.getTemplateId(), greyPlainWagon.getTemplateId(),
            blackPlainWagon.getTemplateId(), whitePlainWagon.getTemplateId(), woodlandCamoWagon.getTemplateId(), blueCamoWagon.getTemplateId(), redCamoWagon.getTemplateId(), purpleCamoWagon.getTemplateId(), blackWhiteCamoWagon.getTemplateId(),
            desertCamoWagon.getTemplateId(), pinkCamoWagon.getTemplateId(), yellowCamoWagon.getTemplateId(), orangeCamoWagon.getTemplateId(), usaFlagWagon.getTemplateId(), canadaFlagWagon.getTemplateId(), germanyFlagWagon.getTemplateId(),
            ukFlagWagon.getTemplateId(), netherlandsFlagWagon.getTemplateId(), polandFlagWagon.getTemplateId(), russiaFlagWagon.getTemplateId(), ussrFlagWagon.getTemplateId(), brazilFlagWagon.getTemplateId(), southAfricaFlagWagon.getTemplateId(),
            greeceFlagWagon.getTemplateId(), denmarkFlagWagon.getTemplateId(), australiaFlagWagon.getTemplateId(), estoniaFlagWagon.getTemplateId(), yellowPlaidWagon.getTemplateId(), orangePlaidWagon.getTemplateId(),
            blueGreenPlaidWagon.getTemplateId(), greenPlaidWagon.getTemplateId(), blackWhitePlaidWagon.getTemplateId(), callanishPatreonWagon.getTemplateId(), vysePatreonWagon.getTemplateId(), toplessWagon.getTemplateId(),
            screenWagon.getTemplateId(),loggingWagon.getTemplateId(), altarWagon.getTemplateId());

    public static void registerCustomVehicleCreationEntries() {
        if (Constants.vehicleCreationEntries) {
            long start = System.nanoTime();
            if (Constants.wagons) {
                //wagons
                for (int i = 0; i < customWagonList.size(); i++) {
                    int id = customWagonList.get(i);
                    if (id != 0) {
                        createWagonCreationEntry(i);
                    } else {
                        RequiemLogging.logWarning(String.format("%d vehicle cant' be created, id is 0", customWagonList.get(i)));
                    }
                }
            }
            if (Constants.caravels) {
                // caravels
                for (int i = 0; i < customCaravelList.size(); i++) {
                    int id = customCaravelList.get(i);
                    if (id != 0) {
                        createCaravelCreationEntry(i);
                    } else {
                        RequiemLogging.logWarning(String.format("%d vehicle cant' be created, id is 0", customCaravelList.get(i)));
                    }
                }
            }
            if (Constants.cogs) {
                // cogs
                for (int i = 0; i < customCogList.size(); i++) {
                    int id = customCogList.get(i);
                    if (id != 0) {
                        createCogCreationEntry(i);
                    } else {
                        RequiemLogging.logWarning(String.format("%d vehicle cant' be created, id is 0", customCogList.get(i)));
                    }
                }
            }
            if (Constants.corbitas) {
                //corbitas
                for (int i = 0; i < customCorbitaList.size(); i++) {
                    int id = customCorbitaList.get(i);
                    if (id != 0) {
                        createCorbitaCreationEntry(i);
                    } else {
                        RequiemLogging.logWarning(String.format("%d vehicle cant' be created, id is 0", customCorbitaList.get(i)));
                    }
                }
            }
            if (Constants.knarrs) {
                //knarrs
                for (int i = 0; i < customKnarrList.size(); i++) {
                    int id = customKnarrList.get(i);
                    if (id != 0) {
                        createKnarrCreationEntry(i);
                    } else {
                        RequiemLogging.logWarning(String.format("%d vehicle cant' be created, id is 0", customKnarrList.get(i)));
                    }
                }
            }
            if (Constants.sailingBoats) {
                //sailingBoats
                for (int i = 0; i < customSailingBoatList.size(); i++) {
                    int id = customSailingBoatList.get(i);
                    if (id != 0) {
                        createSailingCreationEntry(i);
                    } else {
                        RequiemLogging.logWarning(String.format("%d vehicle cant' be created, id is 0", customSailingBoatList.get(i)));
                    }
                }
            }
            RequiemLogging.debug(String.format("Initialising custom vehicle creation entries took %s millis.", (float) (System.nanoTime() - start) / 1000000f));
        }
    }

}
