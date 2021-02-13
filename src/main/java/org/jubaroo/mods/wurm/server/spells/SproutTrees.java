package org.jubaroo.mods.wurm.server.spells;

import com.wurmonline.mesh.GrassData;
import com.wurmonline.mesh.Tiles;
import com.wurmonline.mesh.TreeData;
import com.wurmonline.server.Players;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.skills.Skill;
import com.wurmonline.server.spells.ModReligiousSpell;
import com.wurmonline.server.spells.Spell;
import com.wurmonline.server.zones.NoSuchZoneException;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zone;
import com.wurmonline.server.zones.Zones;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modsupport.actions.ActionEntryBuilder;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.util.ArrayList;
import java.util.List;

class SproutTrees extends ModReligiousSpell {

    SproutTrees() {
        super("Sprout trees", ModActions.getNextActionId(), 30, 50, 60, 50, 300000L);
        this.offensive = false;
        this.targetTile = true;

        try {
            ReflectionUtil.setPrivateField(this, ReflectionUtil.getField(Spell.class, "description"), "Sprouts trees on grass or dirt tiles in the area");
        } catch (Exception e) {
            RequiemLogging.logException(null, e);
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
        if (layer < 0) {
            performer.getCommunicator().sendNormalServerMessage("You need to be on the surface to cast this spell");
            return false;
        }
        try {
            final Zone zone = Zones.getZone(tilex, tiley, true);
            final VolaTile tile = zone.getOrCreateTile(tilex, tiley);
            if (tile.getVillage() != null) {
                if (performer.getCitizenVillage() == null) {
                    performer.getCommunicator().sendNormalServerMessage("You may not cast that spell on someone elses deed.");
                    return false;
                }
                if (performer.getCitizenVillage().getId() != tile.getVillage().getId()) {
                    performer.getCommunicator().sendNormalServerMessage("You may not cast that spell on someone elses deed.");
                    return false;
                }
            }
        } catch (NoSuchZoneException nsz) {
            performer.getCommunicator().sendNormalServerMessage("You fail to focus the spell on that area.");
            return false;
        }
        return true;
    }

    @Override
    public void doEffect(final Skill castSkill, final double power, final Creature performer, final int tilex, final int tiley, final int layer, final int heightOffset) {
        List<VolaTile> tiles = null;
        try {
            final Zone zone = Zones.getZone(tilex, tiley, true);
            tiles = new ArrayList<>();
            for (int x = tilex - 2; x < tilex + 2; ++x) {
                for (int y = tiley - 2; y < tiley + 2; ++y) {
                    final VolaTile tile = zone.getOrCreateTile(x, y);
                    if (tile.getVillage() != null) {
                        if (performer.getCitizenVillage() == null) {
                            continue;
                        }
                        if (performer.getCitizenVillage().getId() != tile.getVillage().getId()) {
                            continue;
                        }
                    }
                    tiles.add(tile);
                }
            }
        } catch (NoSuchZoneException nz) {
            RequiemLogging.logException("Unable to find zone for sprout trees.", nz);
        }
        if (tiles == null || tiles.size() == 0) {
            performer.getCommunicator().sendNormalServerMessage("You fail to focus the spell on that area.");
            return;
        }
        final int treeType = Math.min(13, (int) (power / 7.5));
        for (final VolaTile currTile : tiles) {
            if (!this.needsToCheckSurrounding(treeType) || !this.isTreeNearby(currTile.tilex, currTile.tiley)) {
                final int tileId = Server.surfaceMesh.getTile(currTile.tilex, currTile.tiley);
                final byte type = Tiles.decodeType(tileId);
                if (type == Tiles.TILE_TYPE_DIRT || type == Tiles.TILE_TYPE_GRASS || type == Tiles.TILE_TYPE_MYCELIUM) {
                    final byte age = (byte) (7 + Server.rand.nextInt(8));
                    byte ttype;
                    ttype = TreeData.TreeType.fromInt(type).asMyceliumTree();
                    final byte newData = Tiles.encodeTreeData(age, false, false, GrassData.GrowthTreeStage.SHORT);
                    Server.surfaceMesh.setTile(currTile.tilex, currTile.tiley, Tiles.encode(Tiles.decodeHeight(tileId), ttype, newData));
                    Players.getInstance().sendChangedTile(currTile.tilex, currTile.tiley, true, false);
                }
            }
        }
    }

    private boolean needsToCheckSurrounding(final int type) {
        return type == TreeData.TreeType.OAK.getTypeId() || type == TreeData.TreeType.WILLOW.getTypeId();
    }

    private boolean isTreeNearby(final int tx, final int ty) {
        for (int x = tx - 1; x < tx + 1; ++x) {
            for (int y = ty - 1; y < ty + 1; ++y) {
                if (x != tx || y != ty) {
                    try {
                        final int tileId = Server.surfaceMesh.getTile(x, y);
                        final byte type = Tiles.decodeType(tileId);
                        final Tiles.Tile tile = Tiles.getTile(type);
                        if (tile.isTree()) {
                            return true;
                        }
                    } catch (Exception ex) {
                        RequiemLogging.logWarning(ex.getMessage());
                    }
                }
            }
        }
        return false;
    }

}
