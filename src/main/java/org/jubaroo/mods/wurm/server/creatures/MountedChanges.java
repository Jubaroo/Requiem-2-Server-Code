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

public class MountedChanges {

    public static void registerCustomCreaturesVehicleSettings() {
        // TODO double check there are no duplicate entries!!!

        ModVehicleBehaviours.addCreatureVehicle(CustomCreatures.pandaId, new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
                VehicleFacade facade = wrap(vehicle);
                vehicle.creature = true;
                facade.setMaxSpeed(20f);
                vehicle.setMaxDepth(-0.7f);
                //vehicle.setMaxHeight(3000f);
                facade.setCommandType(ProtoConstants.TELE_START_COMMAND_CREATURE);
                vehicle.setMaxHeightDiff(0.04f);
                vehicle.setSkillNeeded(25f);
                facade.createPassengerSeats(0);
                vehicle.setSeatFightMod(0, 0.8f, 1.1f);
                vehicle.setSeatOffset(0, 0f, 0f, 0f);
                facade.setCanHaveEquipment(false);
            }

            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {

            }
        });

        ModVehicleBehaviours.addCreatureVehicle(CustomCreatures.polarBearId, new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
                VehicleFacade facade = wrap(vehicle);
                vehicle.creature = true;
                facade.setMaxSpeed(21f);
                vehicle.setMaxDepth(-1.1f);
                //vehicle.setMaxHeight(3000f);
                vehicle.setMaxHeightDiff(0.04f);
                facade.setCommandType(ProtoConstants.TELE_START_COMMAND_CREATURE);
                vehicle.setSkillNeeded(27f);
                facade.createPassengerSeats(0);
                vehicle.setSeatFightMod(0, 0.8f, 1.1f);
                vehicle.setSeatOffset(0, 0f, 0f, 0f);
                facade.setCanHaveEquipment(false);
            }

            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {

            }
        });

        ModVehicleBehaviours.addCreatureVehicle(CustomCreatures.reindeerId, new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
                VehicleFacade facade = wrap(vehicle);
                vehicle.creature = true;
                facade.setMaxSpeed(21f);
                vehicle.setMaxDepth(-1.1f);
                //vehicle.setMaxHeight(3000f);
                vehicle.setMaxHeightDiff(0.04f);
                vehicle.setSkillNeeded(27f);
                facade.setCommandType(ProtoConstants.TELE_START_COMMAND_CREATURE);
                facade.createPassengerSeats(0);
                vehicle.setSeatFightMod(0, 0.8f, 1.1f);
                vehicle.setSeatOffset(0, 0f, 0f, 0f);
                facade.setCanHaveEquipment(false);
            }

            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {

            }
        });

        ModVehicleBehaviours.addCreatureVehicle(Zebra.templateId, new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
                VehicleFacade facade = wrap(vehicle);
                vehicle.creature = true;
                facade.setMaxSpeed(26f);
                vehicle.setMaxDepth(-0.7f);
                //vehicle.setMaxHeight(3000f);
                vehicle.setMaxHeightDiff(0.04f);
                vehicle.setSkillNeeded(27f);
                facade.setCommandType(ProtoConstants.TELE_START_COMMAND_CREATURE);
                facade.createPassengerSeats(0);
                vehicle.setSeatFightMod(0, 0.7f, 0.9f);
                vehicle.setSeatOffset(0, 0f, 0f, 0f);
                facade.setCanHaveEquipment(true);
            }

            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {

            }
        });

        ModVehicleBehaviours.addCreatureVehicle(CustomCreatures.chargerId, new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
                VehicleFacade facade = wrap(vehicle);
                vehicle.creature = true;
                facade.createPassengerSeats(0);
                vehicle.setSkillNeeded(37f);
                facade.setName(creature.getName());
                vehicle.setMaxHeightDiff(0.09f);
                vehicle.setMaxDepth(-1.7f);
                facade.setMaxSpeed(30f);
                facade.setCommandType(ProtoConstants.TELE_START_COMMAND_CREATURE);
                vehicle.setSeatFightMod(0, 0.8f, 1.1f);
                vehicle.setSeatOffset(0, 0f, 0f, 0.3f);
                facade.setCanHaveEquipment(true);
            }

            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {

            }
        });

        ModVehicleBehaviours.addCreatureVehicle(CustomCreatures.hornedPonyId, new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
                VehicleFacade facade = wrap(vehicle);
                vehicle.creature = true;
                facade.createPassengerSeats(0);
                vehicle.setSkillNeeded(35f);
                facade.setName(creature.getName());
                vehicle.setMaxHeightDiff(0.08f);
                vehicle.setMaxDepth(-1.7f);
                facade.setMaxSpeed(25f);
                facade.setCommandType(ProtoConstants.TELE_START_COMMAND_CREATURE);
                vehicle.setSeatFightMod(0, 0.8f, 1.1f);
                vehicle.setSeatOffset(0, 0f, 0f, 0f);
                facade.setCanHaveEquipment(true);
            }

            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {

            }
        });

        ModVehicleBehaviours.addCreatureVehicle(CustomCreatures.purpleRainbowUnicornId, new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
                VehicleFacade facade = wrap(vehicle);
                vehicle.creature = true;
                facade.createPassengerSeats(0);
                vehicle.setSkillNeeded(21f);
                facade.setName(creature.getName());
                vehicle.setMaxDepth(-0.7f);
                vehicle.setMaxHeightDiff(0.04f);
                facade.setMaxSpeed(50f);
                facade.setCommandType(ProtoConstants.TELE_START_COMMAND_CREATURE);
                vehicle.setSeatFightMod(0, 0.7f, 0.9f);
                vehicle.setSeatOffset(0, 0f, 0f, 0f);
                facade.setCanHaveEquipment(true);
            }

            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {

            }
        });

        ModVehicleBehaviours.addCreatureVehicle(CustomCreatures.whiteRainbowUnicornId, new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
                VehicleFacade facade = wrap(vehicle);
                facade.createPassengerSeats(0);
                vehicle.setSeatFightMod(0, 0.7f, 0.9f);
                vehicle.setSeatOffset(0, 0f, 0f, 0f);
                facade.setCreature(true);
                vehicle.setSkillNeeded(27f);
                facade.setName(creature.getName());
                vehicle.setMaxDepth(-0.7f);
                vehicle.setMaxHeightDiff(0.04f);
                facade.setMaxSpeed(50f);
                facade.setCommandType(ProtoConstants.TELE_START_COMMAND_CREATURE);
                facade.setCanHaveEquipment(true);
            }

            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {

            }
        });

        ModVehicleBehaviours.addCreatureVehicle(CustomCreatures.rainbowZebraId, new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
                VehicleFacade facade = wrap(vehicle);
                vehicle.creature = true;
                facade.createPassengerSeats(0);
                vehicle.setSkillNeeded(23f);
                facade.setName(creature.getName());
                vehicle.setMaxDepth(-0.7f);
                vehicle.setMaxHeightDiff(0.04f);
                facade.setMaxSpeed(35f);
                facade.setCommandType(ProtoConstants.TELE_START_COMMAND_CREATURE);
                vehicle.setSeatFightMod(0, 0.7f, 0.9f);
                vehicle.setSeatOffset(0, 0f, 0f, 0f);
                facade.setCanHaveEquipment(true);
            }

            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {

            }
        });

        ModVehicleBehaviours.addCreatureVehicle(CreatureTemplateFactory.WORG_CID, new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
                VehicleFacade facade = wrap(vehicle);
                facade.createPassengerSeats(0);
                vehicle.setSeatFightMod(0, 0.8f, 1.1f);
                vehicle.setSeatOffset(0, -0.2f, 0f, -0.1f);
                facade.setCreature(true);
                vehicle.setSkillNeeded(30f);
                facade.setName(creature.getName());
                vehicle.setMaxHeightDiff(0.07f);
                vehicle.setMaxDepth(-1.7f);
                facade.setMaxSpeed(23f);
                facade.setCommandType(ProtoConstants.TELE_START_COMMAND_CREATURE);
                facade.setCanHaveEquipment(false);
            }

            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {

            }
        });

        ModVehicleBehaviours.addCreatureVehicle(CustomCreatures.ghostHellHorseId, new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
                VehicleFacade facade = wrap(vehicle);
                facade.createPassengerSeats(0);
                vehicle.setSeatFightMod(0, 0.7f, 0.9f);
                vehicle.setSeatOffset(0, 0f, 0f, 0f);
                facade.setCreature(true);
                vehicle.setSkillNeeded(21f);
                facade.setName(creature.getName());
                vehicle.setMaxDepth(-0.7f);
                vehicle.setMaxHeightDiff(0.04f);
                facade.setMaxSpeed(40f);
                facade.setCommandType(ProtoConstants.TELE_START_COMMAND_CREATURE);
                facade.setCanHaveEquipment(true);
            }

            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {

            }
        });

        ModVehicleBehaviours.addCreatureVehicle(CustomCreatures.ghostHorseId, new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
                VehicleFacade facade = wrap(vehicle);
                facade.createPassengerSeats(0);
                vehicle.setSeatFightMod(0, 0.7f, 0.9f);
                vehicle.setSeatOffset(0, 0f, 0f, 0f);
                facade.setCreature(true);
                vehicle.setSkillNeeded(21f);
                facade.setName(creature.getName());
                vehicle.setMaxDepth(-0.7f);
                vehicle.setMaxHeightDiff(0.04f);
                facade.setMaxSpeed(28f);
                facade.setCommandType(ProtoConstants.TELE_START_COMMAND_CREATURE);
                facade.setCanHaveEquipment(true);
            }

            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {

            }
        });

        ModVehicleBehaviours.addCreatureVehicle(CreatureTemplateIds.BISON_CID, new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
                VehicleFacade facade = wrap(vehicle);
                facade.createPassengerSeats(0);
                vehicle.setSeatFightMod(0, 0.7f, 0.9f);
                vehicle.setSeatOffset(0, 0f, 0f, 0f);
                facade.setCreature(true);
                vehicle.setSkillNeeded(27f);
                facade.setName(creature.getName());
                vehicle.setMaxDepth(-0.7f);
                vehicle.setMaxHeightDiff(0.04f);
                facade.setMaxSpeed(25f);
                facade.setCommandType(ProtoConstants.TELE_START_COMMAND_CREATURE);
                facade.setCanHaveEquipment(true);
            }

            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {
            }
        });

        ModVehicleBehaviours.addCreatureVehicle(CustomCreatures.chargerId, new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
                VehicleFacade facade = wrap(vehicle);
                facade.createPassengerSeats(0);
                vehicle.setSeatFightMod(0, 0.8f, 1.1f);
                vehicle.setSeatOffset(0, 0f, 0f, 0f);
                facade.setCreature(true);
                vehicle.setSkillNeeded(37f);
                facade.setName(creature.getName());
                vehicle.setMaxDepth(-0.7f);
                vehicle.setMaxHeightDiff(0.09f);
                facade.setMaxSpeed(30f);
                facade.setCommandType(ProtoConstants.TELE_START_COMMAND_CREATURE);
                facade.setCanHaveEquipment(true);
            }

            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {
            }
        });

        ModVehicleBehaviours.addCreatureVehicle(CustomCreatures.hornedPonyId, new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
                VehicleFacade facade = wrap(vehicle);
                facade.createPassengerSeats(0);
                vehicle.setSeatFightMod(0, 0.8f, 1.1f);
                vehicle.setSeatOffset(0, 0f, 0f, 0f);
                facade.setCreature(true);
                vehicle.setSkillNeeded(25f);
                facade.setName(creature.getName());
                vehicle.setMaxDepth(-1.7f);
                vehicle.setMaxHeightDiff(0.08f);
                facade.setMaxSpeed(25f);
                facade.setCommandType(ProtoConstants.TELE_START_COMMAND_CREATURE);
                facade.setCanHaveEquipment(true);
            }

            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {
            }
        });

        ModVehicleBehaviours.addCreatureVehicle(CustomCreatures.ridingRoosterId, new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle vehicle) {
                VehicleFacade facade = wrap(vehicle);
                facade.createPassengerSeats(0);
                vehicle.setSeatFightMod(0, 0.7f, 0.9f);
                vehicle.setSeatOffset(0, 0f, 0f, 0f);
                facade.setCreature(true);
                vehicle.setSkillNeeded(23f);
                facade.setName(creature.getName());
                vehicle.setMaxDepth(-0.7f);
                vehicle.setMaxHeightDiff(0.04f);
                facade.setMaxSpeed(32f);
                facade.setCommandType(ProtoConstants.TELE_START_COMMAND_CREATURE);
                facade.setCanHaveEquipment(false);
            }

            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {
            }
        });

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
            final Class<MountedChanges> thisClass = MountedChanges.class;
            String replace;

            Util.setReason("Scaling horse speed.");
            CtClass ctCreature = classPool.get("com.wurmonline.server.creatures.Creature");
            replace = String.format("{ return %s.newMountSpeedMultiplier(this, $1); }", MountedChanges.class.getName());
            Util.setBodyDeclared(thisClass, ctCreature, "getMountSpeedPercent", replace);

            Util.setReason("Force mount speed change check on damage.");
            replace = "forceMountSpeedChange();";
            Util.insertBeforeDeclared(thisClass, ctCreature, "setWounded", replace);

        } catch (NotFoundException | IllegalArgumentException | ClassCastException e) {
            throw new HookException(e);
        }

    }

}
