package org.jubaroo.mods.wurm.server.creatures;

import com.wurmonline.server.behaviours.Vehicle;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.CreatureTemplateFactory;
import com.wurmonline.server.creatures.CreatureTemplateIds;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.NoSpaceException;
import com.wurmonline.shared.constants.BodyPartConstants;
import com.wurmonline.shared.constants.ProtoConstants;
import org.gotti.wurmunlimited.modsupport.vehicles.ModVehicleBehaviour;
import org.gotti.wurmunlimited.modsupport.vehicles.ModVehicleBehaviours;
import org.gotti.wurmunlimited.modsupport.vehicles.VehicleFacade;
import org.jubaroo.mods.wurm.server.creatures.traitedCreatures.Zebra;

import java.util.ArrayList;

public class CustomMountSettings {

    private static void registerCreatureMountSettings(int creatureId, float maxSpeed, float maxDepth, float maxHeightDiff, float skill) {
        ModVehicleBehaviours.addCreatureVehicle(creatureId, new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
                VehicleFacade facade = wrap(vehicle);
                vehicle.creature = true;
                facade.setMaxSpeed(maxSpeed);
                vehicle.setMaxDepth(-maxDepth);
                facade.setCommandType(ProtoConstants.TELE_START_COMMAND_CREATURE);
                vehicle.setMaxHeightDiff(maxHeightDiff);
                vehicle.setSkillNeeded(skill);
                facade.createPassengerSeats(0);
                vehicle.setSeatFightMod(0, 0.8f, 1.1f);
                vehicle.setSeatOffset(0, 0f, 0f, 0f);
                facade.setCanHaveEquipment(false);
            }

            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {

            }
        });
    }

    private static void registerCreatureMountSettings(int creatureId, float maxSpeed, float maxDepth, float maxHeightDiff, float skill, boolean canHaveEquipment) {
        ModVehicleBehaviours.addCreatureVehicle(creatureId, new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
                VehicleFacade facade = wrap(vehicle);
                vehicle.creature = true;
                facade.setMaxSpeed(maxSpeed);
                vehicle.setMaxDepth(-maxDepth);
                facade.setCommandType(ProtoConstants.TELE_START_COMMAND_CREATURE);
                vehicle.setMaxHeightDiff(maxHeightDiff);
                vehicle.setSkillNeeded(skill);
                facade.createPassengerSeats(0);
                vehicle.setSeatFightMod(0, 0.8f, 1.1f);
                vehicle.setSeatOffset(0, 0f, 0f, 0f);
                facade.setCanHaveEquipment(canHaveEquipment);
            }

            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {
            }
        });
    }

    private static void registerCreatureMountSettings(int creatureId, float maxSpeed, float skill) {
        ModVehicleBehaviours.addCreatureVehicle(creatureId, new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
                VehicleFacade facade = wrap(vehicle);
                vehicle.creature = true;
                facade.setMaxSpeed(maxSpeed);
                vehicle.setMaxDepth(-0.7f);
                //vehicle.setMaxHeight(3000f);
                facade.setCommandType(ProtoConstants.TELE_START_COMMAND_CREATURE);
                vehicle.setMaxHeightDiff(0.05f);
                vehicle.setSkillNeeded(skill);
                facade.createPassengerSeats(0);
                vehicle.setSeatFightMod(0, 0.8f, 1.1f);
                vehicle.setSeatOffset(0, 0f, 0f, 0f);
            }

            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {
            }
        });
    }

    public static void registerCustomMountSettings() {
        // maybe make some mounts not able to go not so high of a slope but much deeper in water and vise/versa
        registerCreatureMountSettings(CustomCreatures.pandaId, 20f, 25f);
        registerCreatureMountSettings(CustomCreatures.polarBearId, 21f, 27f);
        registerCreatureMountSettings(CustomCreatures.reindeerId, 21f, 27f);
        registerCreatureMountSettings(Zebra.templateId, 26f, 0.7f, 0.04f, 27f, true);
        registerCreatureMountSettings(CustomCreatures.chargerId, 30f, 0.5f, 0.09f, 37f, true);
        registerCreatureMountSettings(CustomCreatures.hornedPonyId, 25f, 1.7f, 0.04f, 35f, true);
        registerCreatureMountSettings(CustomCreatures.purpleRainbowUnicornId, 25f, 0.7f, 0.04f, 21f, true);
        registerCreatureMountSettings(CustomCreatures.whiteRainbowUnicornId, 50f, 0.7f, 0.04f, 27f, true);
        registerCreatureMountSettings(CustomCreatures.rainbowZebraId, 35f, 0.7f, 0.04f, 23f, true);
        registerCreatureMountSettings(CreatureTemplateFactory.WORG_CID, 23f, 0.7f, 0.04f, 30f);
        registerCreatureMountSettings(CustomCreatures.ghostHellHorseId, 40f, 0.7f, 0.04f, 21f);
        registerCreatureMountSettings(CustomCreatures.ghostHorseId, 28f, 0.7f, 0.04f, 21f, true);
        registerCreatureMountSettings(CreatureTemplateIds.BISON_CID, 25f, 0.7f, 0.04f, 21f);
        registerCreatureMountSettings(CustomCreatures.ridingRoosterId, 23f, 0.7f, 0.04f, 21f);
    }

    public static float newCalcHorseShoeBonus(Creature creature) {
        float factor = 1.0f;
        ArrayList<Item> gear = new ArrayList<>();
        try {
            Item leftFoot = creature.getEquippedItem(BodyPartConstants.LEFT_FOOT);
            if (leftFoot != null) {
                leftFoot.setDamage(leftFoot.getDamage() + (leftFoot.getDamageModifier() * 0.002f));
                gear.add(leftFoot);
            }
        } catch (NoSpaceException ignored) {
        }
        try {
            Item rightFoot = creature.getEquippedItem(BodyPartConstants.RIGHT_FOOT);
            if (rightFoot != null) {
                rightFoot.setDamage(rightFoot.getDamage() + (rightFoot.getDamageModifier() * 0.002f));
                gear.add(rightFoot);
            }
        } catch (NoSpaceException ignored) {
        }
        try {
            Item leftHand = creature.getEquippedItem(BodyPartConstants.LEFT_HAND);
            if (leftHand != null) {
                leftHand.setDamage(leftHand.getDamage() + (leftHand.getDamageModifier() * 0.002f));
                gear.add(leftHand);
            }
        } catch (NoSpaceException ignored) {
        }
        try {
            Item rightHand = creature.getEquippedItem(BodyPartConstants.RIGHT_HAND);
            if (rightHand != null) {
                rightHand.setDamage(rightHand.getDamage() + (rightHand.getDamageModifier() * 0.002f));
                gear.add(rightHand);
            }
        } catch (NoSpaceException ignored) {
        }
        for (Item shoe : gear) {
            factor += Math.max(10f, shoe.getCurrentQualityLevel()) / 2000f;
            factor += shoe.getSpellSpeedBonus() / 2000f;
            factor += shoe.getRarity() * 0.03f;
        }
        return factor;
    }

}
