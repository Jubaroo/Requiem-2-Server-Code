package org.jubaroo.mods.wurm.server.vehicles;

import com.wurmonline.server.Features;
import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.behaviours.*;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemSizes;
import com.wurmonline.server.items.ItemTemplate;
import com.wurmonline.server.items.ItemTypes;
import com.wurmonline.shared.constants.IconConstants;
import com.wurmonline.shared.constants.ItemMaterials;
import com.wurmonline.shared.constants.ProtoConstants;
import javassist.CtClass;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import javassist.bytecode.Descriptor;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.classhooks.InvocationHandlerFactory;
import org.gotti.wurmunlimited.modsupport.ItemTemplateBuilder;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.vehicles.special.MagicCarpet;
import org.jubaroo.mods.wurm.server.vehicles.util.SeatsFacadeImpl;
import org.jubaroo.mods.wurm.server.vehicles.util.VehicleFacadeImpl;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.jubaroo.mods.wurm.server.vehicles.CustomVehicleCreationEntries.*;

public class CustomVehicles extends CustomItems {

    public static ItemTemplate jennKellonWagon, molRehanWagon, hotsWagon, dragonKingdomWagon, empireMolRehanWagon,
            blackLegionWagon, ebonauraWagon, kingdomSolWagon, romanRepublicWagon, valhallaWagon, valhallaDescendantsWagon,
            macedoniaWagon, dreadnoughtWagon, crusadersWagon, pandemoniumWagon, legionAnubisWagon, wurmUniversityWagon,
            skyBearWagon, dragonElfWagon, reaperWagon, blackWitchWagon, paladinWagon, whiteKnightWagon, elfNatureWagon,
            witchesWagon, deathAngelWagon, iceDragonWagon, bisonWagon, purpleDragonWagon, horsemanWarWagon, unicornWagon,
            theKnightWagon, dracoWagon, fairyGirlWagon, flowerWagon, spaceWagon, fairyDragonWagon, blackPatternWagon,
            purplePatternWagon, redPatternWagon, bluePatternWagon, yellowPlainWagon, limePlainWagon, greenPlainWagon,
            forestGreenPlainWagon, cyanPlainWagon, bluePlainWagon, navyBluePlainWagon, indigoPlainWagon, violetPlainWagon,
            magentaPlainWagon, hotPinkPlainWagon, redPlainWagon, orangePlainWagon, brownPlainWagon, greyPlainWagon,
            blackPlainWagon, whitePlainWagon, woodlandCamoWagon, blueCamoWagon, redCamoWagon, purpleCamoWagon, blackWhiteCamoWagon,
            desertCamoWagon, pinkCamoWagon, yellowCamoWagon, orangeCamoWagon, usaFlagWagon, canadaFlagWagon, germanyFlagWagon,
            ukFlagWagon, netherlandsFlagWagon, polandFlagWagon, russiaFlagWagon, ussrFlagWagon, brazilFlagWagon, southAfricaFlagWagon,
            greeceFlagWagon, denmarkFlagWagon, australiaFlagWagon, estoniaFlagWagon, yellowPlaidWagon, orangePlaidWagon,
            blueGreenPlaidWagon, greenPlaidWagon, blackWhitePlaidWagon, callanishPatreonWagon, vysePatreonWagon, toplessWagon,
            screenWagon, loggingWagon, altarWagon;

    public static ItemTemplate blackLegionCaravel, crusadersCaravel, ebonauraCaravel, empireMolRehanCaravel, eagleCaravel,
            freedomCaravel, hotSCaravel, jennKellonCaravel, kingdomSolCaravel, macedoniaCaravel, molRehanCaravel,
            pandemoniumCaravel, pirateCaravel, requiemCaravel, romanEmpireCaravel, vyseCaravel, callanishCaravel;

    public static ItemTemplate blackLegionCog, crusadersCog, ebonauraCog, empireMolRehanCog, eagleCog,
            freedomCog, hotSCog, jennKellonCog, kingdomSolCog, macedoniaCog, molRehanCog,
            pandemoniumCog, pirateCog, requiemCog, romanEmpireCog, vyseCog, callanishCog;

    public static ItemTemplate blackLegionCorbita, crusadersCorbita, ebonauraCorbita, empireMolRehanCorbita, eagleCorbita,
            freedomCorbita, hotSCorbita, jennKellonCorbita, kingdomSolCorbita, macedoniaCorbita, molRehanCorbita,
            pandemoniumCorbita, pirateCorbita, requiemCorbita, romanEmpireCorbita, vyseCorbita, callanishCorbita;

    public static ItemTemplate blackLegionKnarr, crusadersKnarr, ebonauraKnarr, empireMolRehanKnarr, eagleKnarr,
            freedomKnarr, hotSKnarr, jennKellonKnarr, kingdomSolKnarr, macedoniaKnarr, molRehanKnarr,
            pandemoniumKnarr, pirateKnarr, requiemKnarr, romanEmpireKnarr, vyseKnarr, callanishKnarr;

    public static ItemTemplate blackLegionSailing, crusadersSailing, dreadnoughtSailing, ebonauraSailing, empireMolRehanSailing,
            eagleSailing, freedomSailing, hotSSailing, kingdomSolSailing, macedoniaSailing, molRehanSailing, pandemoniumSailing,
            pirateSailing, requiemSailing, romanEmpireSailing, valhallaSailing, jennKellonTwoSailing, vyseSailing, callanishSailing;

    public static void registerCustomVehicles() throws IOException {
        long start = System.nanoTime();
        RequiemLogging.logInfo("=================  Registering Custom Requiem Vehicle Templates =================");
        registerWagons();
        registerCustomWagons();
        registerCustomWagonManageHooks();
        registerCaravels();
        registerCogs();
        registerCorbitas();
        registerKnarrs();
        registerSailboats();
        registerCustomBoatManageHooks();
        registerWagonVehicleSettings();
        registerSailingBoatVehicleSettings();
        registerCorbitaVehicleSettings();
        registerKnarrVehicleSettings();
        registerCogVehicleSettings();
        registerCaravelVehicleSettings();
        MagicCarpet.registerVehicle();
        RequiemLogging.logInfo(String.format("Registering all of Requiem's custom vehicle templates took %d milliseconds", (System.nanoTime() - start) / 1000000L));
    }

    private static ItemTemplate registerWagon(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.wagon.%s", id))
                .name(String.format("%s wagon", name), String.format("%s wagons", name), "A fairly large wagon designed to be dragged by four animals.")
                .modelName(String.format("model.transports.medium.wagon.%s.", modelSuffix))
                .descriptions("almost full", "somewhat occupied", "half-full", "emptyish")
                .imageNumber((short) IconConstants.ICON_NONE)
                .size(ItemSizes.ITEM_SIZE_MEDIUM)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_HOLLOW,
                        ItemTypes.ITEM_TYPE_NOTAKE,
                        ItemTypes.ITEM_TYPE_WOOD,
                        ItemTypes.ITEM_TYPE_TURNABLE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_VEHICLE,
                        ItemTypes.ITEM_TYPE_CART,
                        ItemTypes.ITEM_TYPE_VEHICLE_DRAGGED,
                        ItemTypes.ITEM_TYPE_LOCKABLE,
                        ItemTypes.ITEM_TYPE_HASDATA,
                        ItemTypes.ITEM_TYPE_TRANSPORTABLE,
                        ItemTypes.ITEM_TYPE_USES_SPECIFIED_CONTAINER_VOLUME,
                        ItemTypes.ITEM_TYPE_NOWORKPARENT,
                        ItemTypes.ITEM_TYPE_NORENAME,
                        ItemTypes.ITEM_TYPE_COLORABLE
                })
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .dimensions(550, 300, 410)
                .containerSize(200, 260, 400)
                .weightGrams(240000)
                .dyeAmountOverrideGrams((short) 0)
                .value(5000)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.vehicleBehaviour)
                .difficulty(70f)
                .isTraded(true)
                .build();
    }

    private static void registerWagons() throws IOException {
        jennKellonWagon = registerWagon("jennKellon", "jenn", "Jenn-Kellon");
        molRehanWagon = registerWagon("molRehan", "molr", "Mol-Rehan");
        hotsWagon = registerWagon("hots", "hots", "Horde of the Summoned");
        dragonKingdomWagon = registerWagon("dragonKingdom", "zjen", "Dragon Kingdom");
        empireMolRehanWagon = registerWagon("empireMolRehan", "empi", "Empire of Mol-Rehan");
        blackLegionWagon = registerWagon("blackLegion", "blac", "Black Legion");
        ebonauraWagon = registerWagon("ebonaura", "ebon", "Ebonaura");
        kingdomSolWagon = registerWagon("kingdomSol", "king", "Kingdom of Sol");
        romanRepublicWagon = registerWagon("romanRepublic", "ther", "The Roman Republic");
        valhallaWagon = registerWagon("valhalla", "valh", "Valhalla");
        valhallaDescendantsWagon = registerWagon("valhallaDescendants", "yval", "Descendants of Valhalla");
        macedoniaWagon = registerWagon("macedonia", "mace", "Macedonia");
        dreadnoughtWagon = registerWagon("dreadnought", "drea", "Dreadnought");
        crusadersWagon = registerWagon("crusaders", "thec", "The Crusaders");
        pandemoniumWagon = registerWagon("pandemonium", "pand", "Pandemonium");
        legionAnubisWagon = registerWagon("legionAnubis", "legi", "Legion of Anubis");
        wurmUniversityWagon = registerWagon("wurmUniversity", "wurm", "Wurm University");
        skyBearWagon = registerWagon("skyBear", "skybear", "Skybear");
        dragonElfWagon = registerWagon("dragonElf", "dragonelf", "Dragonelf");
        reaperWagon = registerWagon("reaper", "reaper", "Reaper");
        blackWitchWagon = registerWagon("blackWitch", "blackwitch", "black witch");
        paladinWagon = registerWagon("paladin", "paladin", "paladin");
        whiteKnightWagon = registerWagon("whiteKnight", "whiteknight", "white knight");
        elfNatureWagon = registerWagon("elfNature", "elfnature", "star elf");
        witchesWagon = registerWagon("witches", "witches", "witches");
        deathAngelWagon = registerWagon("deathAngel", "deathangel", "death angel");
        iceDragonWagon = registerWagon("iceDragon", "icedragon", "ice dragon");
        bisonWagon = registerWagon("bison", "bison", "bison");
        purpleDragonWagon = registerWagon("purpleDragon", "purpledragon", "purple dragon");
        horsemanWarWagon = registerWagon("horsemanWar", "horsemanwar", "horseman War");
        unicornWagon = registerWagon("unicorn", "unicorn", "unicorn");
        theKnightWagon = registerWagon("theKnight", "theknight", "the knight");
        dracoWagon = registerWagon("draco", "draco", "Drako");
        fairyGirlWagon = registerWagon("fairyGirl", "fairygirl", "fairy girl");
        flowerWagon = registerWagon("flower", "flower", "flower");
        spaceWagon = registerWagon("space", "space", "space");
        fairyDragonWagon = registerWagon("fairyDragon", "fairydragon", "fairy dragon");
        blackPatternWagon = registerWagon("blackPattern", "blackpattern", "pattern black");
        purplePatternWagon = registerWagon("purplePattern", "purplepattern", "pattern purple");
        redPatternWagon = registerWagon("redPattern", "redpattern", "pattern red");
        bluePatternWagon = registerWagon("bluePattern", "bluepattern", "pattern blue");
        yellowPlainWagon = registerWagon("yellowPlain", "yellowplain", "plain yellow");
        limePlainWagon = registerWagon("limePlain", "limeplain", "plain lime green");
        greenPlainWagon = registerWagon("greenPlain", "greenplain", "plain green");
        forestGreenPlainWagon = registerWagon("forestGreenPlain", "forestgreenplain", "plain forest green");
        cyanPlainWagon = registerWagon("cyanPlain", "cyanplain", "plain cyan");
        bluePlainWagon = registerWagon("bluePlain", "blueplain", "plain blue");
        navyBluePlainWagon = registerWagon("navyBluePlain", "navyblueplain", "plain navy blue");
        indigoPlainWagon = registerWagon("indigoPlain", "indigoplain", "plain indigo");
        violetPlainWagon = registerWagon("violetPlain", "violetplain", "plain violet");
        magentaPlainWagon = registerWagon("magentaPlain", "magentaplain", "plain magenta");
        hotPinkPlainWagon = registerWagon("hotPinkPlain", "hotpinkplain", "plain hot pink");
        redPlainWagon = registerWagon("redPlain", "redplain", "plain red");
        orangePlainWagon = registerWagon("orangePlain", "orangeplain", "plain orange");
        brownPlainWagon = registerWagon("brownPlain", "brownplain", "plain brown");
        greyPlainWagon = registerWagon("greyPlain", "greyplain", "plain grey");
        blackPlainWagon = registerWagon("blackPlain", "blackplain", "plain black");
        whitePlainWagon = registerWagon("whitePlain", "whiteplain", "plain white");
        woodlandCamoWagon = registerWagon("woodlandCamo", "woodlandcamo", "camouflage woodland");
        blueCamoWagon = registerWagon("blueCamo", "bluecamo", "camouflage blue and black");
        redCamoWagon = registerWagon("redCamo", "redcamo", "camouflage red and black");
        purpleCamoWagon = registerWagon("purpleCamo", "purplecamo", "camouflage purple and black");
        blackWhiteCamoWagon = registerWagon("blackWhiteCamo", "blackwhitecamo", "camouflage white and black");
        desertCamoWagon = registerWagon("desertCamo", "desertcamo", "camouflage desert");
        pinkCamoWagon = registerWagon("pinkCamo", "pinkcamo", "camouflage pink and black");
        yellowCamoWagon = registerWagon("yellowCamo", "yellowcamo", "camouflage yellow and black");
        orangeCamoWagon = registerWagon("orangeCamo", "orangecamo", "camouflage autumn");
        usaFlagWagon = registerWagon("usaFlag", "usaflag", "USA");
        canadaFlagWagon = registerWagon("canadaFlag", "canadaflag", "Canada");
        germanyFlagWagon = registerWagon("germanyFlag", "germany", "Germany");
        ukFlagWagon = registerWagon("ukFlag", "uk", "United Kingdom");
        netherlandsFlagWagon = registerWagon("netherlandsFlag", "netherlands", "Netherlands");
        polandFlagWagon = registerWagon("polandFlag", "poland", "Poland");
        russiaFlagWagon = registerWagon("russiaFlag", "russia", "Russia");
        ussrFlagWagon = registerWagon("ussrFlag", "ussr", "Soviet Union");
        brazilFlagWagon = registerWagon("brazilFlag", "brazil", "Brazil");
        southAfricaFlagWagon = registerWagon("southAfricaFlag", "southafrica", "South Africa");
        greeceFlagWagon = registerWagon("greeceFlag", "greece", "Greece");
        denmarkFlagWagon = registerWagon("denmarkFlag", "denmark", "Denmark");
        australiaFlagWagon = registerWagon("australiaFlag", "australia", "Australia");
        estoniaFlagWagon = registerWagon("estoniaFlag", "estonia", "Estonia");
        yellowPlaidWagon = registerWagon("yellowPlaid", "yellowplaid", "plaid yellow and black");
        orangePlaidWagon = registerWagon("orangePlaid", "orangeplaid", "plaid orange");
        blueGreenPlaidWagon = registerWagon("blueGreenPlaid", "bluegreenplaid", "plaid blue and green");
        greenPlaidWagon = registerWagon("greenPlaid", "greenplaid", "plaid green");
        blackWhitePlaidWagon = registerWagon("blackWhitePlaid", "blackwhiteplaid", "plaid black and white");
        callanishPatreonWagon = registerWagon("callanishPatreon", "callanish", "Callanish");
        vysePatreonWagon = registerWagon("vysePatreon", "vyse", "Vyse");
        toplessWagon = registerWagon("topless", "topless", "topless");
        screenWagon = registerWagon("screen", "screen", "netted");
    }

    private static ItemTemplate registerCustomWagon(String id, String modelSuffix, String name, String description, short[] itemTypes, int containerSizeX, int containerSizeY, int containerSizeZ, int weight, float difficulty) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.wagon.%s", id))
                .name(String.format("%s wagon", name), String.format("%s wagons", name), description)
                .modelName(String.format("model.transports.medium.wagon.%s.", modelSuffix))
                .descriptions("almost full", "somewhat occupied", "half-full", "emptyish")
                .imageNumber((short) IconConstants.ICON_NONE)
                .size(ItemSizes.ITEM_SIZE_MEDIUM)
                .itemTypes(itemTypes)
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .dimensions(550, 300, 410)
                .containerSize(containerSizeX, containerSizeY, containerSizeZ)
                .weightGrams(weight)
                .dyeAmountOverrideGrams((short) 0)
                .value(5000)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.vehicleBehaviour)
                .difficulty(difficulty)
                .isTraded(true)
                .build();
    }

    private static void registerCustomWagons() throws IOException {
        short[] normalWagonTypes = {
                ItemTypes.ITEM_TYPE_NAMED,
                ItemTypes.ITEM_TYPE_HOLLOW,
                ItemTypes.ITEM_TYPE_NOTAKE,
                ItemTypes.ITEM_TYPE_WOOD,
                ItemTypes.ITEM_TYPE_TURNABLE,
                ItemTypes.ITEM_TYPE_DECORATION,
                ItemTypes.ITEM_TYPE_REPAIRABLE,
                ItemTypes.ITEM_TYPE_VEHICLE,
                ItemTypes.ITEM_TYPE_CART,
                ItemTypes.ITEM_TYPE_VEHICLE_DRAGGED,
                ItemTypes.ITEM_TYPE_LOCKABLE,
                ItemTypes.ITEM_TYPE_HASDATA,
                ItemTypes.ITEM_TYPE_TRANSPORTABLE,
                ItemTypes.ITEM_TYPE_USES_SPECIFIED_CONTAINER_VOLUME,
                ItemTypes.ITEM_TYPE_NOWORKPARENT,
                ItemTypes.ITEM_TYPE_NORENAME,
                ItemTypes.ITEM_TYPE_COLORABLE
        };
        loggingWagon = registerCustomWagon("logging", "logging", "logging", "A wagon designed specifically for hauling felled trees and can not hold anything else.",
                normalWagonTypes, 750, 750, 750, 240000, 70f);
        altarWagon = registerCustomWagon("altar", "altar", "altar", "A wagon that has an altar built into it so you can pray on the go. It has a lantern fixed to it for better light when traveling at night.",
                normalWagonTypes, 200, 260, 400, 350000, 85f);
    }

    private static ItemTemplate registerCaravel(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.boat.caravel.%s", id))
                .name(String.format("%s caravel", name), String.format("%s caravels", name), "An impressive merchant ship.")
                .modelName(String.format("model.%s.", modelSuffix))
                .descriptions("almost full", "somewhat occupied", "half-full", "emptyish")
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_FLOATING,
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_HOLLOW,
                        ItemTypes.ITEM_TYPE_NOTAKE,
                        ItemTypes.ITEM_TYPE_WOOD,
                        ItemTypes.ITEM_TYPE_TURNABLE,
                        ItemTypes.ITEM_TYPE_DRAGGABLE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_COLORABLE,
                        ItemTypes.ITEM_TYPE_VEHICLE,
                        ItemTypes.ITEM_TYPE_LOCKABLE,
                        ItemTypes.ITEM_TYPE_HASDATA,
                        ItemTypes.ITEM_TYPE_NORENAME,
                        ItemTypes.ITEM_TYPE_NOT_MISSION,
                        ItemTypes.ITEM_TYPE_USES_SPECIFIED_CONTAINER_VOLUME,
                        ItemTypes.ITEM_TYPE_NOWORKPARENT,
                        ItemTypes.ITEM_TYPE_SUPPORTS_SECONDARY_COLOR
                })
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .dimensions(600, 1200, 2100)
                .containerSize(300, 600, 600)
                .weightGrams(2400000)
                .dyeAmountOverrideGrams((short) 220000)
                .secondaryItemName("sail")
                .value(10000)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.vehicleBehaviour)
                .difficulty(70f)
                .isTraded(true)
                .build();
    }

    private static void registerCaravels() throws IOException {
        blackLegionCaravel = registerCaravel("blackLegion", "structure.boat.caravel.blac", "Black Legion");
        crusadersCaravel = registerCaravel("crusaders", "structure.boat.caravel.crus", "Crusaders");
        ebonauraCaravel = registerCaravel("ebonaura", "structure.boat.caravel.ebon", "Ebonaura");
        empireMolRehanCaravel = registerCaravel("empireMolRehan", "structure.boat.caravel.empi", "Empire of Mol-Rehan");
        eagleCaravel = registerCaravel("eagle", "structure.boat.caravel.fluf", "eagle");
        freedomCaravel = registerCaravel("freedom", "structure.boat.caravel.free", "Freedom");
        hotSCaravel = registerCaravel("hotS", "structure.boat.caravel.hots", "HotS");
        jennKellonCaravel = registerCaravel("jennKellon", "structure.boat.caravel.zjen", "Dragon Kingdom");
        kingdomSolCaravel = registerCaravel("kingdomSol", "structure.boat.caravel.kos", "Kingdom of Sol");
        macedoniaCaravel = registerCaravel("macedonia", "structure.boat.caravel.mace", "Macedon");
        molRehanCaravel = registerCaravel("molRehan", "structure.boat.caravel.molr", "Mol-Rehan");
        pandemoniumCaravel = registerCaravel("pandemonium", "structure.boat.caravel.pand", "Pandemonium");
        pirateCaravel = registerCaravel("pirate", "structure.boat.caravel.pira", "pirate");
        requiemCaravel = registerCaravel("requiem", "structure.boat.caravel.row", "Requiem");
        romanEmpireCaravel = registerCaravel("romanEmpire", "structure.boat.caravel.ther", "Roman Empire");
        vyseCaravel = registerCaravel("patreon.vyse", "boat.caravel.patreon.vyse", "Vyse's");
        callanishCaravel = registerCaravel("patreon.callanish", "boat.caravel.patreon.callanish", "Callanish's");
    }

    private static ItemTemplate registerCog(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.boat.cog.%s", id))
                .name(String.format("%s cog", name), String.format("%s cogs", name), "A sturdy, one-masted merchant ship with a flat bottom.")
                .modelName(String.format("model.%s.", modelSuffix))
                .descriptions("almost full", "somewhat occupied", "half-full", "emptyish")
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_FLOATING,
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_HOLLOW,
                        ItemTypes.ITEM_TYPE_NOTAKE,
                        ItemTypes.ITEM_TYPE_WOOD,
                        ItemTypes.ITEM_TYPE_TURNABLE,
                        ItemTypes.ITEM_TYPE_DRAGGABLE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_COLORABLE,
                        ItemTypes.ITEM_TYPE_VEHICLE,
                        ItemTypes.ITEM_TYPE_LOCKABLE,
                        ItemTypes.ITEM_TYPE_HASDATA,
                        ItemTypes.ITEM_TYPE_NORENAME,
                        ItemTypes.ITEM_TYPE_NOT_MISSION,
                        ItemTypes.ITEM_TYPE_USES_SPECIFIED_CONTAINER_VOLUME,
                        ItemTypes.ITEM_TYPE_NOWORKPARENT,
                        ItemTypes.ITEM_TYPE_SUPPORTS_SECONDARY_COLOR
                })
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .dimensions(210, 400, 610)
                .weightGrams(800000)
                .secondaryItemName("sail")
                .value(10000)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.vehicleBehaviour)
                .difficulty(50.0f)
                .isTraded(true)
                .build();
    }

    private static void registerCogs() throws IOException {
        blackLegionCog = registerCog("blackLegion", "structure.boat.cog.blac", "Black Legion");
        crusadersCog = registerCog("crusaders", "structure.boat.cog.crus", "Crusaders");
        ebonauraCog = registerCog("ebonaura", "structure.boat.cog.ebon", "Ebonaura");
        empireMolRehanCog = registerCog("empireMolRehan", "structure.boat.cog.empi", "Empire of Mol-Rehan");
        eagleCog = registerCog("eagle", "structure.boat.cog.fluf", "eagle");
        freedomCog = registerCog("freedom", "structure.boat.cog.free", "Freedom");
        hotSCog = registerCog("hotS", "structure.boat.cog.hots", "HotS");
        jennKellonCog = registerCog("jennKellon", "structure.boat.cog.zjen", "Dragon Kingdom");
        kingdomSolCog = registerCog("kingdomSol", "structure.boat.cog.kos", "Kingdom of Sol");
        macedoniaCog = registerCog("macedonia", "structure.boat.cog.mace", "Macedon");
        molRehanCog = registerCog("molRehan", "structure.boat.cog.molr", "Mol-Rehan");
        pandemoniumCog = registerCog("pandemonium", "structure.boat.cog.pand", "Pandemonium");
        pirateCog = registerCog("pirate", "structure.boat.cog.pira", "pirate");
        requiemCog = registerCog("requiem", "structure.boat.cog.row", "Requiem");
        romanEmpireCog = registerCog("romanEmpire", "structure.boat.cog.ther", "Roman Empire");
        vyseCog = registerCog("patreon.vyse", "boat.cog.patreon.vyse", "Vyse's");
        callanishCog = registerCog("patreon.callanish", "boat.cog.patreon.callanish", "Callanish's");
    }

    private static ItemTemplate registerCorbita(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.boat.corbita.%s", id))
                .name(String.format("%s corbita", name), String.format("%s corbitas", name), "A ship with square sails, steered using two side rudders connected to each other.")
                .modelName(String.format("model.%s.", modelSuffix))
                .descriptions("almost full", "somewhat occupied", "half-full", "emptyish")
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_FLOATING,
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_HOLLOW,
                        ItemTypes.ITEM_TYPE_NOTAKE,
                        ItemTypes.ITEM_TYPE_WOOD,
                        ItemTypes.ITEM_TYPE_TURNABLE,
                        ItemTypes.ITEM_TYPE_DRAGGABLE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_COLORABLE,
                        ItemTypes.ITEM_TYPE_VEHICLE,
                        ItemTypes.ITEM_TYPE_LOCKABLE,
                        ItemTypes.ITEM_TYPE_HASDATA,
                        ItemTypes.ITEM_TYPE_NORENAME,
                        ItemTypes.ITEM_TYPE_NOT_MISSION,
                        ItemTypes.ITEM_TYPE_USES_SPECIFIED_CONTAINER_VOLUME,
                        ItemTypes.ITEM_TYPE_NOWORKPARENT,
                        ItemTypes.ITEM_TYPE_SUPPORTS_SECONDARY_COLOR
                })
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .dimensions(210, 600, 810)
                .weightGrams(1400000)
                .secondaryItemName("sail")
                .value(10000)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.vehicleBehaviour)
                .difficulty(40f)
                .isTraded(true)
                .build();
    }

    private static void registerCorbitas() throws IOException {
        blackLegionCorbita = registerCorbita("blackLegion", "structure.boat.corbita.blac", "Black Legion");
        crusadersCorbita = registerCorbita("crusaders", "structure.boat.corbita.crus", "Crusaders");
        ebonauraCorbita = registerCorbita("ebonaura", "structure.boat.corbita.ebon", "Ebonaura");
        empireMolRehanCorbita = registerCorbita("empireMolRehan", "structure.boat.corbita.empi", "Empire of Mol-Rehan");
        eagleCorbita = registerCorbita("eagle", "structure.boat.corbita.fluf", "eagle");
        freedomCorbita = registerCorbita("freedom", "structure.boat.corbita.free", "Freedom");
        hotSCorbita = registerCorbita("hotS", "structure.boat.corbita.hots", "HotS");
        jennKellonCorbita = registerCorbita("jennKellon", "structure.boat.corbita.zjen", "Dragon Kingdom");
        kingdomSolCorbita = registerCorbita("kingdomSol", "structure.boat.corbita.kos", "Kingdom of Sol");
        macedoniaCorbita = registerCorbita("macedonia", "structure.boat.corbita.mace", "Macedon");
        molRehanCorbita = registerCorbita("molRehan", "structure.boat.corbita.molr", "Mol-Rehan");
        pandemoniumCorbita = registerCorbita("pandemonium", "structure.boat.corbita.pand", "Pandemonium");
        pirateCorbita = registerCorbita("pirate", "structure.boat.corbita.pira", "pirate");
        requiemCorbita = registerCorbita("requiem", "structure.boat.corbita.row", "Requiem");
        romanEmpireCorbita = registerCorbita("romanEmpire", "structure.boat.corbita.ther", "Roman Empire");
        vyseCorbita = registerCorbita("patreon.vyse", "boat.corbita.patreon.vyse", "Vyse's");
        callanishCorbita = registerCorbita("patreon.callanish", "boat.corbita.patreon.callanish", "Callanish's");
    }

    private static ItemTemplate registerKnarr(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.boat.knarr.%s", id))
                .name(String.format("%s knarr", name), String.format("%s knarr", name), "A ship with a clinker-built hull assembled with iron rivets, and one mast with a square yard sail. In insufficient wind it is rowed with oars. The side rudder is on the starboard side.")
                .modelName(String.format("model.%s.", modelSuffix))
                .descriptions("almost full", "somewhat occupied", "half-full", "emptyish")
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_FLOATING,
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_HOLLOW,
                        ItemTypes.ITEM_TYPE_NOTAKE,
                        ItemTypes.ITEM_TYPE_WOOD,
                        ItemTypes.ITEM_TYPE_TURNABLE,
                        ItemTypes.ITEM_TYPE_DRAGGABLE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_COLORABLE,
                        ItemTypes.ITEM_TYPE_VEHICLE,
                        ItemTypes.ITEM_TYPE_LOCKABLE,
                        ItemTypes.ITEM_TYPE_HASDATA,
                        ItemTypes.ITEM_TYPE_NORENAME,
                        ItemTypes.ITEM_TYPE_NOT_MISSION,
                        ItemTypes.ITEM_TYPE_USES_SPECIFIED_CONTAINER_VOLUME,
                        ItemTypes.ITEM_TYPE_NOWORKPARENT,
                        ItemTypes.ITEM_TYPE_SUPPORTS_SECONDARY_COLOR
                })
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .dimensions(400, 800, 1500)
                .weightGrams(2000000)
                .secondaryItemName("sail")
                .value(10000)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.vehicleBehaviour)
                .difficulty(60f)
                .isTraded(true)
                .build();
    }

    private static void registerKnarrs() throws IOException {
        blackLegionKnarr = registerKnarr("blackLegion", "structure.boat.knarr.blac", "Black Legion");
        crusadersKnarr = registerKnarr("crusaders", "structure.boat.knarr.crus", "Crusaders");
        ebonauraKnarr = registerKnarr("ebonaura", "structure.boat.knarr.ebon", "Ebonaura");
        empireMolRehanKnarr = registerKnarr("empireMolRehan", "structure.boat.knarr.empi", "Empire of Mol-Rehan");
        eagleKnarr = registerKnarr("eagle", "structure.boat.knarr.fluf", "eagle");
        freedomKnarr = registerKnarr("freedom", "structure.boat.knarr.free", "Freedom");
        hotSKnarr = registerKnarr("hotS", "structure.boat.knarr.hots", "HotS");
        jennKellonKnarr = registerKnarr("jennKellon2", "structure.boat.knarr.zjen", "Dragon Kingdom");
        kingdomSolKnarr = registerKnarr("kingdomSol", "structure.boat.knarr.kos", "Kingdom of Sol");
        macedoniaKnarr = registerKnarr("macedonia", "structure.boat.knarr.mace", "Macedon");
        molRehanKnarr = registerKnarr("molRehan", "structure.boat.knarr.molr", "Mol-Rehan");
        pandemoniumKnarr = registerKnarr("pandemonium", "structure.boat.knarr.pand", "Pandemonium");
        pirateKnarr = registerKnarr("pirate", "structure.boat.knarr.pira", "pirate");
        requiemKnarr = registerKnarr("requiem", "structure.boat.knarr.row", "Requiem");
        romanEmpireKnarr = registerKnarr("romanEmpire", "structure.boat.knarr.ther", "Roman Empire");
        vyseKnarr = registerKnarr("patreon.vyse", "boat.knarr.patreon.vyse", "Vyse's");
        callanishKnarr = registerKnarr("patreon.callanish", "boat.knarr.patreon.callanish", "Callanish's");
    }

    private static ItemTemplate registerSailboat(String id, String modelSuffix, String name) throws IOException {
        return new ItemTemplateBuilder(String.format("jubaroo.boat.sailing.%s", id))
                .name(String.format("%s sailing boat", name), String.format("%s sailing boats", name), "A small sailing boat that will accommodate five people.")
                .modelName(String.format("model.%s.", modelSuffix))
                .descriptions("almost full", "somewhat occupied", "half-full", "emptyish")
                .imageNumber((short) IconConstants.ICON_NONE)
                .itemTypes(new short[]{
                        ItemTypes.ITEM_TYPE_FLOATING,
                        ItemTypes.ITEM_TYPE_NAMED,
                        ItemTypes.ITEM_TYPE_HOLLOW,
                        ItemTypes.ITEM_TYPE_NOTAKE,
                        ItemTypes.ITEM_TYPE_WOOD,
                        ItemTypes.ITEM_TYPE_TURNABLE,
                        ItemTypes.ITEM_TYPE_DRAGGABLE,
                        ItemTypes.ITEM_TYPE_DECORATION,
                        ItemTypes.ITEM_TYPE_REPAIRABLE,
                        ItemTypes.ITEM_TYPE_COLORABLE,
                        ItemTypes.ITEM_TYPE_VEHICLE,
                        ItemTypes.ITEM_TYPE_LOCKABLE,
                        ItemTypes.ITEM_TYPE_HASDATA,
                        ItemTypes.ITEM_TYPE_NORENAME,
                        ItemTypes.ITEM_TYPE_NOT_MISSION,
                        ItemTypes.ITEM_TYPE_USES_SPECIFIED_CONTAINER_VOLUME,
                        ItemTypes.ITEM_TYPE_NOWORKPARENT,
                        ItemTypes.ITEM_TYPE_SUPPORTS_SECONDARY_COLOR
                })
                .decayTime(TimeConstants.DECAYTIME_WOOD)
                .dimensions(60, 60, 210)
                .weightGrams(140000)
                .secondaryItemName("sail")
                .value(10000)
                .material(ItemMaterials.MATERIAL_WOOD_BIRCH)
                .behaviourType(BehaviourList.vehicleBehaviour)
                .difficulty(30.0f)
                .dyeAmountOverrideGrams((short) -1)
                .dyePrimaryAmountGrams(5000)
                .isTraded(true)
                .build();
    }

    private static void registerSailboats() throws IOException {
        blackLegionSailing = registerSailboat("blackLegion", "structure.small.boat.sailing.blac", "Black Legion");
        crusadersSailing = registerSailboat("crusaders", "structure.small.boat.sailing.crus", "Crusaders");
        dreadnoughtSailing = registerSailboat("dreadnought", "structure.small.boat.sailing.drea", "Dreadnought");
        ebonauraSailing = registerSailboat("ebonaura", "structure.small.boat.sailing.ebon", "Ebonaura");
        empireMolRehanSailing = registerSailboat("empireMolRehan", "structure.small.boat.sailing.empi", "Empire of Mol-Rehan");
        eagleSailing = registerSailboat("eagle", "structure.small.boat.sailing.fluf", "eagle");
        freedomSailing = registerSailboat("freedom", "structure.small.boat.sailing.free", "Freedom");
        hotSSailing = registerSailboat("hotS", "structure.small.boat.sailing.hots", "HotS");
        kingdomSolSailing = registerSailboat("kingdomSol", "structure.small.boat.sailing.kos", "Kingdom of Sol");
        macedoniaSailing = registerSailboat("macedonia", "structure.small.boat.sailing.mace", "Macedon");
        molRehanSailing = registerSailboat("molRehan", "structure.small.boat.sailing.molr", "Mol-Rehan");
        pandemoniumSailing = registerSailboat("pandemonium", "structure.small.boat.sailing.pand", "Pandemonium");
        pirateSailing = registerSailboat("pirate", "structure.small.boat.sailing.pira", "pirate");
        requiemSailing = registerSailboat("requiem", "structure.small.boat.sailing.row", "Requiem");
        romanEmpireSailing = registerSailboat("romanEmpire", "structure.small.boat.sailing.ther", "Roman Empire");
        valhallaSailing = registerSailboat("valhalla", "structure.small.boat.sailing.valh", "Valhalla");
        jennKellonTwoSailing = registerSailboat("jennKellon2", "structure.small.boat.sailing.zjen", "Dragon Empire");
        vyseSailing = registerSailboat("patreon.vyse", "boat.sailing.patreon.vyse", "Vyse's");
        callanishSailing = registerSailboat("patreon.callanish", "boat.sailing.patreon.callanish", "Callanish's");
    }

    public static void registerCustomWagonManageHooks() {
        try {
            CtClass[] input = {
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.creatures.Creature"),
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.Item")
            };
            CtClass output = HookManager.getInstance().getClassPool().get("java.util.List");

            HookManager.getInstance().registerHook("com.wurmonline.server.behaviours.VehicleBehaviour", "getVehicleBehaviours",
                    Descriptor.ofMethod(output, input), new InvocationHandlerFactory() {
                        @Override
                        public InvocationHandler createInvocationHandler() {
                            return new InvocationHandler() {
                                @Override
                                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                    List<ActionEntry> original = (List<ActionEntry>) method.invoke(proxy, args);
                                    Item item = (Item) args[1];
                                    Creature performer = (Creature) args[0];
                                    LinkedList<ActionEntry> permissions = new LinkedList<ActionEntry>();

                                    if (item.mayManage(performer)) {
                                        int itemId = item.getTemplateId();
                                        for (int id : customWagonList) {
                                            if (id == itemId) {
                                                permissions.add(Actions.actionEntrys[Actions.MANAGE_WAGON]);
                                            }
                                        }
                                    }
                                    if (item.maySeeHistory(performer)) {
                                        int itemId = item.getTemplateId();
                                        for (int id : customWagonList) {
                                            if (id == itemId) {
                                                permissions.add(new ActionEntry(Actions.SHOW_HISTORY_FOR_OBJECT, "History of Wagon", "viewing"));
                                            }
                                        }
                                    }
                                    if (!permissions.isEmpty()) {
                                        if (permissions.size() > 1) {
                                            Collections.sort(permissions);
                                            original.add(new ActionEntry((short) (-permissions.size()), "Permissions", "viewing"));
                                        }
                                        original.addAll(permissions);
                                    }
                                    return original;
                                }

                            };
                        }
                    });
        } catch (Exception e) {
            RequiemLogging.logWarning("Custom Wagon Permission Hooks Exception!: " + e.toString());
        }
    }

    public static void registerCustomBoatManageHooks() {
        try {
            CtClass[] input = {
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.creatures.Creature"),
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.Item")
            };

            CtClass output = HookManager.getInstance().getClassPool().get("java.util.List");

            HookManager.getInstance().registerHook("com.wurmonline.server.behaviours.VehicleBehaviour", "getVehicleBehaviours",
                    Descriptor.ofMethod(output, input), () -> (proxy, method, args) -> {
                        List<ActionEntry> original = (List<ActionEntry>) method.invoke(proxy, args);
                        Item item = (Item) args[1];
                        Creature performer = (Creature) args[0];
                        if (item.mayManage(performer)) {
                            int itemId = item.getTemplateId();
                            for (int id : customCaravelList) {
                                if (id == itemId) {
                                    original.add(Actions.actionEntrys[Actions.MANAGE_SHIP]);
                                }
                            }
                            for (int id : customCogList) {
                                if (id == itemId) {
                                    original.add(Actions.actionEntrys[Actions.MANAGE_SHIP]);
                                }
                            }
                            for (int id : customCorbitaList) {
                                if (id == itemId) {
                                    original.add(Actions.actionEntrys[Actions.MANAGE_SHIP]);
                                }
                            }
                            for (int id : customKnarrList) {
                                if (id == itemId) {
                                    original.add(Actions.actionEntrys[Actions.MANAGE_SHIP]);
                                }
                            }
                            for (int id : customSailingBoatList) {
                                if (id == itemId) {
                                    original.add(Actions.actionEntrys[Actions.MANAGE_SHIP]);
                                }
                            }
                        }
                        if (item.maySeeHistory(performer)) {
                            int itemId = item.getTemplateId();
                            for (int id : customCaravelList) {
                                if (id == itemId) {
                                    original.add(new ActionEntry(Actions.SHOW_HISTORY_FOR_OBJECT, "History of Ship", "viewing"));
                                }
                            }
                            for (int id : customCogList) {
                                if (id == itemId) {
                                    original.add(new ActionEntry(Actions.SHOW_HISTORY_FOR_OBJECT, "History of Ship", "viewing"));
                                }
                            }
                            for (int id : customCorbitaList) {
                                if (id == itemId) {
                                    original.add(new ActionEntry(Actions.SHOW_HISTORY_FOR_OBJECT, "History of Ship", "viewing"));
                                }
                            }
                            for (int id : customKnarrList) {
                                if (id == itemId) {
                                    original.add(new ActionEntry(Actions.SHOW_HISTORY_FOR_OBJECT, "History of Ship", "viewing"));
                                }
                            }
                            for (int id : customSailingBoatList) {
                                if (id == itemId) {
                                    original.add(new ActionEntry(Actions.SHOW_HISTORY_FOR_OBJECT, "History of Ship", "viewing"));
                                }
                            }
                        }
                        return original;
                    });
        } catch (Exception e) {
            RequiemLogging.logWarning("Custom Boat Permission Hooks Exception!: " + e.toString());
        }
    }

    public static void registerCaravelVehicleSettings() {
        try {
            CtClass[] input = {
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.Item"),
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.behaviours.Vehicle")
            };
            CtClass output = CtPrimitiveType.voidType;
            HookManager.getInstance().registerHook("com.wurmonline.server.behaviours.Vehicles", "setSettingsForVehicle",
                    Descriptor.ofMethod(output, input), () -> (proxy, method, args) -> {
                        Item item = (Item) args[0];
                        int templateId = item.getTemplateId();
                        for (int i : customCaravelList) {
                            if (i == templateId) {
                                Vehicle vehicle = (Vehicle) args[1];
                                VehicleFacadeImpl vehfacade = new VehicleFacadeImpl(vehicle);
                                vehfacade.createPassengerSeats(13);
                                vehfacade.setPilotName("Captain");
                                vehfacade.setCreature(false);
                                vehfacade.setEmbarkString("board");
                                vehfacade.setEmbarksString("boards");
                                vehicle.name = item.getName();
                                vehicle.setSeatFightMod(0, 0.9f, 0.9f);
                                vehicle.setSeatOffset(0, 0f, 0f, 0f, 3.866f);
                                vehicle.setSeatOffset(1, -6.98f, 0f, 12.189f);
                                vehicle.setSeatOffset(2, -14.716f, -0.202f, 3.402f);
                                vehicle.setSeatOffset(3, -4.417f, 1.024f, 2.013f);
                                vehicle.setSeatOffset(4, 1.206f, -0.657f, 4.099f);
                                vehicle.setSeatOffset(5, -7.953f, 0.028f, 0.731f);
                                vehicle.setSeatOffset(6, -5.317f, -1.134f, 1.941f);
                                vehicle.setSeatOffset(7, -7.518f, 1.455f, 0.766f);
                                vehicle.setSeatOffset(8, -2.598f, -0.104f, 2.22f);
                                vehicle.setSeatOffset(9, -12.46f, 0.796f, 2.861f);
                                vehicle.setSeatOffset(10, -12.417f, -0.82f, 2.852f);
                                vehicle.setSeatOffset(11, -4.046f, -0.536f, 2.056f);
                                vehicle.setSeatOffset(12, -1.089f, 1.004f, 3.65f);
                                vehicle.setSeatOffset(13, -0.942f, -0.845f, 3.678f);
                                vehfacade.setWindImpact((byte) 70);
                                vehicle.maxHeight = -2f;
                                vehicle.skillNeeded = 24f;
                                vehfacade.setMaxSpeed(4f);
                                vehicle.commandType = ProtoConstants.TELE_START_COMMAND_BOAT;
                                vehicle.setMaxAllowedLoadDistance(12);
                                return null;
                            }
                        }
                        return method.invoke(proxy, args);
                    });
        } catch (NotFoundException e) {
            RequiemLogging.logInfo(String.format("Caravel hook: %s", e.toString()));
        }
    }

    public static void registerCogVehicleSettings() {
        try {
            CtClass[] input = {
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.Item"),
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.behaviours.Vehicle")
            };
            CtClass output = CtPrimitiveType.voidType;
            HookManager.getInstance().registerHook("com.wurmonline.server.behaviours.Vehicles", "setSettingsForVehicle",
                    Descriptor.ofMethod(output, input), () -> (proxy, method, args) -> {
                        Item item = (Item) args[0];
                        int templateId = item.getTemplateId();
                        for (int i : customCogList) {
                            if (i == templateId) {
                                Vehicle vehicle = (Vehicle) args[1];
                                VehicleFacadeImpl vehfacade = new VehicleFacadeImpl(vehicle);
                                vehfacade.createPassengerSeats(8);
                                vehfacade.setPilotName("captain");
                                vehfacade.setCreature(false);
                                vehfacade.setEmbarkString("board");
                                vehfacade.setEmbarksString("boards");
                                vehicle.name = item.getName();
                                vehicle.setSeatFightMod(0, 0.9f, 0.9f);
                                vehicle.setSeatOffset(0, 0f, 0f, 0f, 4.011f);
                                vehicle.setSeatOffset(1, -16.042f, -0.901f, 3.96f);
                                vehicle.setSeatOffset(2, -7.629f, 0f, 14.591f);
                                vehicle.setSeatOffset(3, -4.411f, -2.097f, 3.51f);
                                vehicle.setSeatOffset(4, -16.01f, 0.838f, 3.96f);
                                vehicle.setSeatOffset(5, -9.588f, -1.855f, 1.802f);
                                vehicle.setSeatOffset(6, -11.08f, 2.451f, 1.805f);
                                vehicle.setSeatOffset(7, -4.411f, 1.774f, 3.52f);
                                vehicle.setSeatOffset(8, -1.813f, -1.872f, 3.789f);
                                vehfacade.setWindImpact((byte) 80);
                                vehicle.maxHeight = -2f;
                                vehicle.skillNeeded = 22f;
                                vehfacade.setMaxSpeed(3.5f);
                                vehicle.commandType = ProtoConstants.TELE_START_COMMAND_BOAT;
                                vehicle.setMaxAllowedLoadDistance(12);
                                return null;
                            }
                        }
                        return method.invoke(proxy, args);
                    });
        } catch (NotFoundException e) {
            RequiemLogging.logInfo(String.format("Cog hook: %s", e.toString()));
        }
    }

    public static void registerCorbitaVehicleSettings() {
        try {
            CtClass[] input = {
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.Item"),
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.behaviours.Vehicle")
            };
            CtClass output = CtPrimitiveType.voidType;
            HookManager.getInstance().registerHook("com.wurmonline.server.behaviours.Vehicles", "setSettingsForVehicle",
                    Descriptor.ofMethod(output, input), () -> (proxy, method, args) -> {
                        Item item = (Item) args[0];
                        int templateId = item.getTemplateId();
                        for (int i : customCorbitaList) {
                            if (i == templateId) {
                                Vehicle vehicle = (Vehicle) args[1];
                                VehicleFacadeImpl vehfacade = new VehicleFacadeImpl(vehicle);
                                vehfacade.createPassengerSeats(6);
                                vehfacade.setPilotName("captain");
                                vehfacade.setCreature(false);
                                vehfacade.setEmbarkString("board");
                                vehfacade.setEmbarksString("boards");
                                vehicle.name = item.getName();
                                vehicle.setSeatFightMod(0, 0.9f, 0.9f);
                                vehicle.setSeatOffset(0, 0f, 0f, 0f, 3.02f);
                                vehicle.setSeatOffset(1, -7.192f, -1.036f, 2.16f);
                                vehicle.setSeatOffset(2, 3f, 1.287f, 2.47f);
                                vehicle.setSeatOffset(3, -3.657f, 1.397f, 1.93f);
                                vehicle.setSeatOffset(4, 2.858f, -1.076f, 2.473f);
                                vehicle.setSeatOffset(5, -5.625f, 0.679f, 1.926f);
                                vehicle.setSeatOffset(6, -2.3f, -1.838f, 1.93f);
                                vehfacade.setWindImpact((byte) 60);
                                vehicle.maxHeight = -2f;
                                vehicle.skillNeeded = 21f;
                                vehfacade.setMaxSpeed(3.8f);
                                vehicle.commandType = ProtoConstants.TELE_START_COMMAND_BOAT;
                                vehicle.setMaxAllowedLoadDistance(12);
                                return null;
                            }
                        }
                        return method.invoke(proxy, args);
                    });
        } catch (NotFoundException e) {
            RequiemLogging.logInfo(String.format("Corbita hook: %s", e.toString()));
        }
    }

    public static void registerKnarrVehicleSettings() {
        try {
            CtClass[] input = {
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.Item"),
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.behaviours.Vehicle")
            };
            CtClass output = CtPrimitiveType.voidType;
            HookManager.getInstance().registerHook("com.wurmonline.server.behaviours.Vehicles", "setSettingsForVehicle",
                    Descriptor.ofMethod(output, input), () -> (proxy, method, args) -> {
                        Item item = (Item) args[0];
                        int templateId = item.getTemplateId();
                        for (int i : customKnarrList) {
                            if (i == templateId) {
                                Vehicle vehicle = (Vehicle) args[1];
                                VehicleFacadeImpl vehfacade = new VehicleFacadeImpl(vehicle);
                                vehfacade.createPassengerSeats(12);
                                vehfacade.setPilotName("captain");
                                vehfacade.setCreature(false);
                                vehfacade.setEmbarkString("board");
                                vehfacade.setEmbarksString("boards");
                                vehicle.name = item.getName();
                                vehicle.setSeatFightMod(0, 0.9f, 0.9f);
                                vehicle.setSeatOffset(0, 0f, 0f, 0f, 0.787f);
                                vehicle.setSeatOffset(1, -7.713f, -0.41f, 0.485f);
                                vehicle.setSeatOffset(2, -9.722f, 0.455f, 0.417f);
                                vehicle.setSeatOffset(3, -3.85f, -0.412f, 0.598f);
                                vehicle.setSeatOffset(4, -11.647f, 0f, 0.351f);
                                vehicle.setSeatOffset(5, -1.916f, -0.211f, 0.651f);
                                vehicle.setSeatOffset(6, -12.627f, 0.018f, 0.469f);
                                vehicle.setSeatOffset(7, -5.773f, 0.429f, 0.547f);
                                vehicle.setSeatOffset(8, -2.882f, 0.388f, 0.626f);
                                vehicle.setSeatOffset(9, -8.726f, 0.013f, 0.445f);
                                vehicle.setSeatOffset(10, -10.66f, -0.162f, 0.387f);
                                vehicle.setSeatOffset(11, -7.708f, 0.454f, 0.479f);
                                vehicle.setSeatOffset(12, -5.773f, -0.429f, 0.547f);
                                vehfacade.setWindImpact((byte) 50);
                                vehicle.maxHeight = -0.5f;
                                vehicle.skillNeeded = 23f;
                                vehfacade.setMaxSpeed(4.1f);
                                vehicle.commandType = ProtoConstants.TELE_START_COMMAND_BOAT;
                                vehicle.setMaxAllowedLoadDistance(8);
                                return null;
                            }
                        }
                        return method.invoke(proxy, args);
                    });
        } catch (NotFoundException e) {
            RequiemLogging.logInfo(String.format("Knarr hook: %s", e.toString()));
        }
    }

    public static void registerSailingBoatVehicleSettings() {
        try {
            CtClass[] input = {
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.Item"),
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.behaviours.Vehicle")
            };
            CtClass output = CtPrimitiveType.voidType;
            HookManager.getInstance().registerHook("com.wurmonline.server.behaviours.Vehicles", "setSettingsForVehicle",
                    Descriptor.ofMethod(output, input), () -> (proxy, method, args) -> {
                        Item item = (Item) args[0];
                        int templateId = item.getTemplateId();
                        for (int i : customSailingBoatList) {
                            if (i == templateId) {
                                Vehicle vehicle = (Vehicle) args[1];
                                VehicleFacadeImpl vehfacade = new VehicleFacadeImpl(vehicle);
                                vehfacade.createPassengerSeats(4);
                                vehfacade.setPilotName("captain");
                                vehfacade.setCreature(false);
                                vehfacade.setEmbarkString("board");
                                vehfacade.setEmbarksString("boards");
                                vehicle.name = item.getName();
                                vehicle.setSeatFightMod(0, 0.9f, 0.4f);
                                vehicle.setSeatOffset(0, 0f, 0f, 0f, 0.351f);
                                vehicle.setSeatOffset(1, -1.392f, 0.378f, 0.351f);
                                vehicle.setSeatOffset(2, -2.15f, -0.349f, 0.341f);
                                vehicle.setSeatOffset(3, -3.7f, -0.281f, 0.34f);
                                vehicle.setSeatOffset(4, -4.39f, 0.14f, 0.352f);
                                vehfacade.setWindImpact((byte) 30);
                                vehicle.maxHeight = -0.5f;
                                vehicle.skillNeeded = 20.1f;
                                vehfacade.setMaxSpeed(5f);
                                vehicle.commandType = ProtoConstants.TELE_START_COMMAND_BOAT;
                                vehicle.setMaxAllowedLoadDistance(6);
                                return null;
                            }
                        }
                        return method.invoke(proxy, args);
                    });
        } catch (NotFoundException e) {
            RequiemLogging.logInfo(String.format("Sailing boat hook: %s", e.toString()));
        }
    }

    public static void registerWagonVehicleSettings() {
        try {
            CtClass[] input = {
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.Item"),
                    HookManager.getInstance().getClassPool().get("com.wurmonline.server.behaviours.Vehicle")
            };
            CtClass output = CtPrimitiveType.voidType;
            HookManager.getInstance().registerHook("com.wurmonline.server.behaviours.Vehicles", "setSettingsForVehicle",
                    Descriptor.ofMethod(output, input), () -> (proxy, method, args) -> {
                        Item item = (Item) args[0];
                        int templateId = item.getTemplateId();
                        for (int i : customWagonList) {
                            if (i == templateId) {
                                Vehicle vehicle = (Vehicle) args[1];
                                VehicleFacadeImpl vehfacade = new VehicleFacadeImpl(vehicle);
                                if (Features.Feature.WAGON_PASSENGER.isEnabled()) {
                                    vehfacade.createPassengerSeats(1);
                                } else {
                                    vehfacade.createPassengerSeats(0);
                                }
                                vehfacade.setPilotName("driver");
                                vehfacade.setCreature(false);
                                vehfacade.setEmbarkString("ride");
                                vehfacade.setEmbarksString("rides");
                                vehicle.name = item.getName();
                                vehicle.setSeatFightMod(0, 0.9f, 0.3f);
                                vehicle.setSeatOffset(0, 0f, 0f, 0f, 1.453f);
                                if (Features.Feature.WAGON_PASSENGER.isEnabled()) {
                                    vehicle.setSeatFightMod(1, 1f, 0.4f);
                                    vehicle.setSeatOffset(1, 4.05f, 0f, 0.84f);
                                }
                                vehicle.maxHeightDiff = 0.07f;
                                vehicle.maxDepth = -0.7f;
                                vehicle.skillNeeded = 21f;
                                vehfacade.setMaxSpeed(1f);
                                vehicle.commandType = ProtoConstants.TELE_START_COMMAND_CART;
                                SeatsFacadeImpl seatfacad = new SeatsFacadeImpl();

                                final Seat[] hitches = {
                                        seatfacad.CreateSeat((byte) 2),
                                        seatfacad.CreateSeat((byte) 2),
                                        seatfacad.CreateSeat((byte) 2),
                                        seatfacad.CreateSeat((byte) 2)
                                };
                                hitches[0].offx = -2f;
                                hitches[0].offy = -1f;
                                hitches[1].offx = -2f;
                                hitches[1].offy = 1f;
                                hitches[2].offx = -5f;
                                hitches[2].offy = -1f;
                                hitches[3].offx = -5f;
                                hitches[3].offy = 1f;
                                vehicle.addHitchSeats(hitches);
                                vehicle.setMaxAllowedLoadDistance(4);
                                return null;
                            }
                        }
                        return method.invoke(proxy, args);
                    });
        } catch (NotFoundException e) {
            RequiemLogging.logInfo(String.format("Wagon hook: %s", e.toString()));
        }
    }

}