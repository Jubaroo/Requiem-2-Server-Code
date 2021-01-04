package org.jubaroo.mods.wurm.server.creatures;

import com.wurmonline.server.behaviours.Vehicle;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.CreatureTemplateFactory;
import com.wurmonline.server.creatures.CreatureTemplateIds;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.NoSpaceException;
import com.wurmonline.shared.constants.BodyPartConstants;
import com.wurmonline.shared.constants.Enchants;
import com.wurmonline.shared.constants.ProtoConstants;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import mod.sin.lib.Util;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modsupport.vehicles.ModVehicleBehaviour;
import org.gotti.wurmunlimited.modsupport.vehicles.ModVehicleBehaviours;
import org.gotti.wurmunlimited.modsupport.vehicles.VehicleFacade;
import org.jubaroo.mods.wurm.server.creatures.traitedCreatures.Zebra;

import java.lang.reflect.InvocationTargetException;
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

    public static float newMountSpeedMultiplier(Creature creature, boolean mounting) {
        float hunger = creature.getStatus().getHunger() / 65535f;
        float damage = creature.getStatus().damage / 65535f;
        float factor = ((((1f - damage * damage) * (1f - damage) + (1f - 2f * damage) * damage) * (1f - damage) + (1f - damage) * damage) * (1f - 0.4f * hunger * hunger));
        try {
            float traitMove = ReflectionUtil.callPrivateMethod(creature, ReflectionUtil.getMethod(creature.getClass(), "getTraitMovePercent"), mounting);
            factor += traitMove;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (creature.isHorse() || creature.isUnicorn()) {
            factor *= newCalcHorseShoeBonus(creature);
        }
        /*if(creature.isHorse()){
            try {
                Item barding = creature.getArmour(BodyPartConstants.TORSO);
                if(barding != null){
                    if(barding.getTemplateId() == ItemList.clothBarding){
                        factor *= 0.9f;
                    }else if(barding.getTemplateId() == ItemList.leatherBarding){
                        factor *= 0.82f;
                    }else if(barding.getTemplateId() == ItemList.chainBarding){
                        factor *= 0.75f;
                    }
                }
            } catch (NoArmourException | NoSpaceException ignored) {
            }
        }*/
        if (creature.getBonusForSpellEffect(Enchants.CRET_OAKSHELL) > 0.0f) {
            factor *= 1f - (0.3f * (creature.getBonusForSpellEffect(Enchants.CRET_OAKSHELL) / 100.0f));
        }
        if (creature.isRidden()) {
            try {
                float saddleFactor = 1.0f;
                Item saddle = creature.getEquippedItem(BodyPartConstants.TORSO);
                if (saddle != null) {
                    saddle.setDamage(saddle.getDamage() + (saddle.getDamageModifier() * 0.001f));
                    saddleFactor += Math.max(10f, saddle.getCurrentQualityLevel()) / 2000f;
                    saddleFactor += saddle.getSpellSpeedBonus() / 2000f;
                    saddleFactor += saddle.getRarity() * 0.03f;
                    factor *= saddleFactor;
                }
            } catch (NoSpaceException ignored) {
            }
            factor *= creature.getMovementScheme().getSpeedModifier();
        }
        return factor;
    }

    public static void preInit() {
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();
            final Class<CustomMountSettings> thisClass = CustomMountSettings.class;
            String replace;

            Util.setReason("Scaling horse speed.");
            CtClass ctCreature = classPool.get("com.wurmonline.server.creatures.Creature");
            replace = String.format("{ return %s.newMountSpeedMultiplier(this, $1); }", CustomMountSettings.class.getName());
            Util.setBodyDeclared(thisClass, ctCreature, "getMountSpeedPercent", replace);

            Util.setReason("Force mount speed change check on damage.");
            replace = "forceMountSpeedChange();";
            Util.insertBeforeDeclared(thisClass, ctCreature, "setWounded", replace);

        } catch (NotFoundException | IllegalArgumentException | ClassCastException e) {
            throw new HookException(e);
        }

    }

}
