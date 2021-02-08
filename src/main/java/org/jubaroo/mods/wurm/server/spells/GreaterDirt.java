package org.jubaroo.mods.wurm.server.spells;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.FailedException;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Players;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.behaviours.MethodsItems;
import com.wurmonline.server.behaviours.Terraforming;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.*;
import com.wurmonline.server.skills.Skill;
import com.wurmonline.server.spells.ModReligiousSpell;
import com.wurmonline.server.spells.Spell;
import com.wurmonline.server.structures.Fence;
import com.wurmonline.server.zones.NoSuchZoneException;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zone;
import com.wurmonline.server.zones.Zones;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modsupport.actions.ActionEntryBuilder;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;

public class GreaterDirt extends ModReligiousSpell {

    public GreaterDirt() {
        super("Greater Dirt", ModActions.getNextActionId(), 15, 40, 50, 40, 1000L);
        this.targetTile = true;
        this.targetItem = true;

        try {
            ReflectionUtil.setPrivateField(this, ReflectionUtil.getField(Spell.class, "description"), "Creates and destroys dirt");
        } catch (Exception e) {
            RequiemLogging.logException( null, e);
        }

        ActionEntry actionEntry = new ActionEntryBuilder((short) number, name, "casting",
                new int[]{
                        ActionTypesProxy.ACTION_TYPE_SPELL,
                        ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM,
                        ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS
                }).build();

        ModActions.registerAction(actionEntry);

    }

    @Override
    public boolean precondition(final Skill castSkill, final Creature performer, final int tilex, final int tiley, final int layer) {
        if (layer >= 0) {
            for (int x = -2; x <= 1; ++x) {
                for (int y = -2; y <= 1; ++y) {
                    if (isBlocked(tilex + x, tiley + y, performer)) {
                        return false;
                    }
                }
            }
            return true;
        }
        performer.getCommunicator().sendNormalServerMessage("This spell does not work on rock.", (byte) 3);
        return false;
    }

    @Override
    public boolean precondition(final Skill castSkill, final Creature performer, final Item target) {
        if (!target.isHollow() || (target.getTemplate().hasViewableSubItems() && !target.getTemplate().isContainerWithSubItems())) {
            performer.getCommunicator().sendNormalServerMessage(String.format("The %s is not a container.", target.getName()), (byte) 3);
            return false;
        }
        if (target.isBulkContainer()) {
            if (target.getTemplateId() == ItemList.hopper) {
                performer.getCommunicator().sendNormalServerMessage(String.format("The spell wont work on the %s.", target.getName()), (byte) 3);
                return false;
            }
            final long topParent = target.getTopParent();
            if (topParent != MiscConstants.NOID && topParent == performer.getInventory().getWurmId()) {
                performer.getCommunicator().sendNormalServerMessage(String.format("The %s must not be in your inventory.", target.getName()), (byte) 3);
                return false;
            }
        }
        if (target.isLockable() && target.getLockId() != MiscConstants.NOID && !MethodsItems.mayUseInventoryOfVehicle(performer, target) && !MethodsItems.hasKeyForContainer(performer, target)) {
            performer.getCommunicator().sendNormalServerMessage(String.format("You must be able to open the %s to create dirt inside of it.", target.getName()), (byte) 3);
            return false;
        }
        if (target.getTemplateId() == ItemList.smelter) {
            performer.getCommunicator().sendNormalServerMessage(String.format("The %s can only hold ore.", target.getName()), (byte) 3);
            return false;
        }
        if (target.isTent() && target.getParentOrNull() != null) {
            performer.getCommunicator().sendNormalServerMessage("You cannot create dirt inside of that.", (byte) 3);
            return false;
        }
        if (target.isCrate()) {
            final int nums = target.getRemainingCrateSpace();
            if (nums <= 0) {
                performer.getCommunicator().sendNormalServerMessage(String.format("The %s will not be able to contain all that dirt.", target.getName()), (byte) 3);
                return false;
            }
        } else {
            final int sizeLeft = target.getFreeVolume();
            try {
                final ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(ItemList.dirtPile);
                final int nums2 = sizeLeft / template.getVolume();
                if (nums2 <= 0) {
                    performer.getCommunicator().sendNormalServerMessage(String.format("The %s will not be able to contain all that dirt.", target.getName()), (byte) 3);
                    return false;
                }
                if (target.getOwnerId() == performer.getWurmId() && !performer.canCarry(template.getWeightGrams())) {
                    performer.getCommunicator().sendNormalServerMessage("You would not be able to carry all that dirt.", (byte) 3);
                    return false;
                }
                if (target.isContainerLiquid()) {
                    for (final Item i : target.getAllItems(false)) {
                        if (i.isLiquid()) {
                            performer.getCommunicator().sendNormalServerMessage("That would destroy the liquid.", (byte) 3);
                            return false;
                        }
                    }
                }
            } catch (NoSuchTemplateException nst) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void doEffect(final Skill castSkill, final double power, final Creature performer, final Item target) {
        final int sizeLeft = target.getFreeVolume();
        final int dirtAmount = 100;
        try {
            final ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(ItemList.dirtPile);
            boolean created = false;
            int nums = Math.min(dirtAmount, sizeLeft / template.getVolume());
            if (target.isCrate()) {
                nums = Math.min(dirtAmount, target.getRemainingCrateSpace());
            }
            if (nums > 0) {
                if (target.isBulkContainer()) {
                    final Item dirt = ItemFactory.createItem(ItemList.dirtPile, (float) power, performer.getName());
                    dirt.setWeight(template.getWeightGrams() * nums, true);
                    dirt.setMaterial(template.getMaterial());
                    if (target.isCrate()) {
                        dirt.AddBulkItemToCrate(performer, target);
                    } else {
                        dirt.AddBulkItem(performer, target);
                    }
                    created = true;
                } else {
                    for (int x = 0; x < nums; ++x) {
                        if (target.getOwnerId() == performer.getWurmId()) {
                            if (!performer.canCarry(template.getWeightGrams())) {
                                if (created) {
                                    performer.getCommunicator().sendNormalServerMessage("You create a lot of  dirt.", (byte) 2);
                                }
                                return;
                            }
                        } else if (!target.mayCreatureInsertItem()) {
                            if (created) {
                                performer.getCommunicator().sendNormalServerMessage("You create a lot of dirt.", (byte) 2);
                            }
                            return;
                        }
                        final Item dirt2 = ItemFactory.createItem(ItemList.dirtPile, (float) power, performer.getName());
                        target.insertItem(dirt2);
                        created = true;
                    }
                }
            }
            if (created) {
                performer.getCommunicator().sendNormalServerMessage("You create a lot of dirt.", (byte) 2);
            }
        } catch (NoSuchTemplateException | FailedException e) {
            RequiemLogging.logException("[Error] in doEffect in GreaterDirt", e);
        }
    }

    private static boolean isBlocked(final int tx, final int ty, final Creature performer) {
        final int otile = Server.surfaceMesh.getTile(tx, ty);
        final int diff = Math.abs(Terraforming.getMaxSurfaceDifference(otile, tx, ty));
        if (diff > 270) {
            performer.getCommunicator().sendNormalServerMessage("The slope would crumble.", (byte) 3);
            return true;
        }
        for (int x = 0; x >= -1; --x) {
            for (int y = 0; y >= -1; --y) {
                try {
                    final int tile = Server.surfaceMesh.getTile(tx + x, ty + y);
                    final byte type = Tiles.decodeType(tile);
                    if (Terraforming.isNonDiggableTile(type)) {
                        performer.getCommunicator().sendNormalServerMessage("You need to clear the area first.", (byte) 3);
                        return true;
                    }
                    if (Terraforming.isRoad(type)) {
                        performer.getCommunicator().sendNormalServerMessage("The road is too hard.", (byte) 3);
                        return true;
                    }
                    if (type == Tiles.Tile.TILE_CLAY.id || type == Tiles.Tile.TILE_TAR.id || type == Tiles.Tile.TILE_PEAT.id) {
                        return true;
                    }
                    if (Tiles.decodeHeight(tile) < -3000) {
                        performer.getCommunicator().sendNormalServerMessage("Nothing happens at this depth.", (byte) 3);
                        return true;
                    }
                    final Zone zone = Zones.getZone(tx + x, ty + y, performer.isOnSurface());
                    final VolaTile vtile = zone.getTileOrNull(tx + x, ty + y);
                    if (vtile != null) {
                        if (vtile.getStructure() != null) {
                            performer.getCommunicator().sendNormalServerMessage("The structure is in the way.", (byte) 3);
                            return true;
                        }
                        final Fence[] fences = vtile.getFencesForLevel(0);
                        if (fences.length > 0) {
                            if (x == 0 && y == 0) {
                                performer.getCommunicator().sendNormalServerMessage(String.format("The %s is in the way.", fences[0].getName()), (byte) 3);
                                return true;
                            }
                            if (x == -1 && y == 0) {
                                for (final Fence f : fences) {
                                    if (f.isHorizontal()) {
                                        final String wname = f.getName();
                                        performer.getCommunicator().sendNormalServerMessage(String.format("The %s is in the way.", wname), (byte) 3);
                                        return true;
                                    }
                                }
                            } else if (y == -1 && x == 0) {
                                for (final Fence f : fences) {
                                    if (!f.isHorizontal()) {
                                        final String wname = f.getName();
                                        performer.getCommunicator().sendNormalServerMessage(String.format("The %s is in the way.", wname), (byte) 3);
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                } catch (NoSuchZoneException nsz) {
                    performer.getCommunicator().sendNormalServerMessage("The water is too deep to dig in.", (byte) 3);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void doEffect(final Skill castSkill, final double power, final Creature performer, final int tilex, final int tiley, final int layer, final int heightOffset) {
        for (int x = -2; x <= 1; ++x) {
            for (int y = -2; y <= 1; ++y) {
                if (!isBlocked(tilex + x, tiley + y, performer)) {
                    final int tile = Server.surfaceMesh.getTile(tilex + x, tiley + y);
                    final byte oldType = Tiles.decodeType(tile);
                    final int rocktile = Server.rockMesh.getTile(tilex + x, tiley + y);
                    final short rockheight = Tiles.decodeHeight(rocktile);
                    short mod = 0;
                    if (x > -2 && y > -2) {
                        mod = 3;
                    }
                    if (x == 0 && y == 0) {
                        mod = 5;
                    }
                    final short newHeight = (short) Math.max(rockheight, Tiles.decodeHeight(tile) - mod);
                    byte type = Tiles.Tile.TILE_DIRT.id;
                    if (oldType == Tiles.Tile.TILE_SAND.id) {
                        type = oldType;
                    } else if (oldType == Tiles.Tile.TILE_CLAY.id || oldType == Tiles.Tile.TILE_TAR.id || oldType == Tiles.Tile.TILE_PEAT.id) {
                        type = oldType;
                    } else if (oldType == Tiles.Tile.TILE_MOSS.id) {
                        type = oldType;
                    } else if (oldType == Tiles.Tile.TILE_MARSH.id) {
                        type = oldType;
                    }
                    if (Terraforming.allCornersAtRockLevel(tilex + x, tiley + y, Server.surfaceMesh)) {
                        type = Tiles.Tile.TILE_ROCK.id;
                    }
                    Server.setSurfaceTile(tilex + x, tiley + y, newHeight, type, (byte) 0);
                    Players.getInstance().sendChangedTile(tilex + x, tiley + y, true, true);
                }
            }
        }
    }

}