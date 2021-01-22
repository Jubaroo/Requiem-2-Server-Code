package org.jubaroo.mods.wurm.server.creatures.traitedCreatures;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Vehicle;
import com.wurmonline.server.bodys.BodyTemplate;
import com.wurmonline.server.bodys.Wound;
import com.wurmonline.server.combat.ArmourTemplate;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.shared.constants.CreatureTypes;
import com.wurmonline.shared.constants.ItemMaterials;
import com.wurmonline.shared.constants.ProtoConstants;
import org.gotti.wurmunlimited.modsupport.CreatureTemplateBuilder;
import org.gotti.wurmunlimited.modsupport.creatures.EncounterBuilder;
import org.gotti.wurmunlimited.modsupport.creatures.ModCreature;
import org.gotti.wurmunlimited.modsupport.creatures.ModTraits;
import org.gotti.wurmunlimited.modsupport.creatures.TraitsSetter;
import org.gotti.wurmunlimited.modsupport.vehicles.ModVehicleBehaviour;
import org.gotti.wurmunlimited.modsupport.vehicles.VehicleFacade;

public class Zebra implements ModCreature, CreatureTypes, ItemMaterials, MiscConstants {
    private static final int VARIANT_1 = 24;
    public static int templateId;

    public CreatureTemplateBuilder createCreateTemplateBuilder() {

        final int[] types = {C_TYPE_MOVE_LOCAL, C_TYPE_SWIMMING, C_TYPE_VEHICLE, C_TYPE_DOMESTIC, C_TYPE_ANIMAL, C_TYPE_LEADABLE, C_TYPE_GRAZER, C_TYPE_HERBIVORE, C_TYPE_DOMINATABLE};
        final int[] itemsButchered = new int[]{ItemList.tail, ItemList.hoof, ItemList.tallow, ItemList.animalHide, ItemList.bladder, ItemList.eye};
        final CreatureTemplateBuilder builder = new CreatureTemplateBuilder("mod.creature.zebra", "Zebra", "Zebras like this one have many stripes.", "model.creature.quadraped.horse.zebra", types, BodyTemplate.TYPE_HORSE, (short) 3, SEX_MALE, (short) 180, (short) 50, (short) 250, "sound.death.horse",
                "sound.death.horse", "sound.combat.hit.horse", "sound.combat.hit.horse", 1f, 1f, 2.5f, 1.5f, 2f, 0f, 1.5f, 100, itemsButchered, 5, 0, MATERIAL_MEAT_HORSE);

        builder.skill(SkillList.BODY_STRENGTH, 25f);
        builder.skill(SkillList.BODY_CONTROL, 20f);
        builder.skill(SkillList.BODY_STAMINA, 40f);
        builder.skill(SkillList.MIND_LOGICAL, 7f);
        builder.skill(SkillList.MIND_SPEED, 7f);
        builder.skill(SkillList.SOUL_STRENGTH, 22f);
        builder.skill(SkillList.SOUL_DEPTH, 5f);
        builder.skill(SkillList.WEAPONLESS_FIGHTING, 28f);

        builder.maxAge(200);
        builder.baseCombatRating(6f);
        builder.combatDamageType(Wound.TYPE_CRUSH);
        builder.alignment(100f);
        builder.handDamString("kick");
        builder.armourType(ArmourTemplate.ARMOUR_TYPE_CLOTH);
        builder.isHorse(true);
        builder.maxPercentOfCreatures(0.02f);
        builder.maxGroupAttackSize(10);

        templateId = builder.getTemplateId();

        return builder;
    }

    @Override
    public ModVehicleBehaviour getVehicleBehaviour() {
        return new ModVehicleBehaviour() {
            @Override
            public void setSettingsForVehicle(Item item, Vehicle vehicle) {
            }

            @Override
            public void setSettingsForVehicle(Creature creature, Vehicle v) {
                VehicleFacade vehicle = wrap(v);

                vehicle.createPassengerSeats(0);
                vehicle.setSeatFightMod(0, 0.7f, 0.9f);
                vehicle.setSeatOffset(0, 0f, 0f, 0f);
                vehicle.setCreature(true);
                vehicle.setSkillNeeded(21f);
                vehicle.setName(creature.getName());
                vehicle.setMaxDepth(-0.7f);
                vehicle.setMaxHeightDiff(0.04f);
                vehicle.setMaxSpeed(26f);
                vehicle.setCommandType(ProtoConstants.TELE_START_COMMAND_CREATURE);
                vehicle.setCanHaveEquipment(true);
            }
        };
    }

    public boolean hasTraits() {
        return true;
    }

    public String getTraitName(int trait) {
        if (trait == VARIANT_1) {
            return "variant1";
        }
        return null;
    }

    public void assignTraits(TraitsSetter traitsSetter) {
        if (Server.rand.nextInt(3) == 0) {
            traitsSetter.setTraitBit(VARIANT_1, true);
        }

    }

    public long calcNewTraits(double breederSkill, boolean inbred, long mothertraits, long fathertraits) {
        return ModTraits.calcNewTraits(breederSkill, inbred, mothertraits, fathertraits, ModTraits.REGULAR_TRAITS, 1 << VARIANT_1);
    }

    @Override
    public void addEncounters() {
        if (templateId == 0)
            return;

        new EncounterBuilder(Tiles.Tile.TILE_STEPPE.id).addCreatures(templateId, 2).build(2);
    }

}
