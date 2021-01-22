package org.jubaroo.mods.wurm.server.creatures;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.Players;
import com.wurmonline.server.Server;
import com.wurmonline.server.Servers;
import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.bodys.Wound;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.Creatures;
import com.wurmonline.server.creatures.MineDoorPermission;
import com.wurmonline.server.creatures.SpellEffects;
import com.wurmonline.server.spells.SpellEffect;
import com.wurmonline.server.zones.AreaSpellEffect;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zones;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import mod.sin.lib.Util;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modsupport.ModSupportDb;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.communication.discord.DiscordHandler;
import org.jubaroo.mods.wurm.server.server.constants.PollingConstants;
import org.jubaroo.mods.wurm.server.tools.RandomUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Titans {
    public static final long titanRespawnTime = TimeConstants.HOUR_MILLIS * 80L;
    public static HashMap<Creature, Integer> titanDamage = new HashMap<>();
    public static ArrayList<Creature> titans = new ArrayList<>();
    public static long lastPolledTitanSpawn = 0;
    public static long lastSpawnedTitan = 0;
    public static boolean initializedTitans = false;
    protected static HashMap<Long, Integer> titanAdvancedTimed = new HashMap<>();
    private static boolean detected = false;
    private static boolean located = false;

    public static void updateLastSpawnedTitan() {
        Connection dbcon;
        PreparedStatement ps;
        try {
            dbcon = ModSupportDb.getModSupportDb();
            ps = dbcon.prepareStatement(String.format("UPDATE ObjectiveTimers SET TIMER = %d WHERE ID = \"TITAN\"", System.currentTimeMillis()));
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initializeTitanTimer() {
        Connection dbcon;
        PreparedStatement ps;
        boolean foundLeaderboardOpt = false;
        try {
            dbcon = ModSupportDb.getModSupportDb();
            ps = dbcon.prepareStatement("SELECT * FROM ObjectiveTimers WHERE ID = \"TITAN\"");
            ResultSet rs = ps.executeQuery();
            lastSpawnedTitan = rs.getLong("TIMER");
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        RequiemLogging.logInfo("Initialized Titan timer: " + lastSpawnedTitan);
        initializedTitans = true;
    }

    public static void checkDestroyMineDoor(Creature titan, int x, int y) {
        int tile = Server.surfaceMesh.getTile(x, y);
        if (Tiles.isMineDoor(Tiles.decodeType(tile))) {
            if (Tiles.decodeType(Server.caveMesh.getTile(x, y)) == Tiles.Tile.TILE_CAVE_EXIT.id) {
                Server.setSurfaceTile(x, y, Tiles.decodeHeight(Server.surfaceMesh.getTile(x, y)), Tiles.Tile.TILE_HOLE.id, (byte) 0);
            } else {
                Server.setSurfaceTile(x, y, Tiles.decodeHeight(Server.surfaceMesh.getTile(x, y)), Tiles.Tile.TILE_ROCK.id, (byte) 0);
            }
            Players.getInstance().sendChangedTile(x, y, true, true);
            MineDoorPermission.deleteMineDoor(x, y);
            Server.getInstance().broadCastAction(String.format("%s's ability destroys a mine door!", titan.getName()), titan, 50);
        }
    }

    public static Creature[] getUndergroundCreatures(int x, int y) {
        VolaTile tCave = Zones.getOrCreateTile(x, y, false);
        if (tCave == null) {
            return null;
        }
        int tileCave = Server.caveMesh.getTile(x, y);
        byte typeCave = Tiles.decodeType(tileCave);
        if (typeCave != Tiles.Tile.TILE_CAVE.id && typeCave != Tiles.Tile.TILE_CAVE_EXIT.id && typeCave != Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id && typeCave != Tiles.Tile.TILE_CAVE_PREPATED_FLOOR_REINFORCED.id) {
            return null;
        }
        return tCave.getCreatures();
    }

    public static boolean isTitan(int templateId) {
        if (templateId == CustomCreatures.lilithId) {
            return true;
        } else return templateId == CustomCreatures.ifritId;
    }

    public static boolean isTitan(Creature creature) {
        return isTitan(creature.getTemplate().getTemplateId());
    }

    public static boolean isTitanMinion(int templateId) {
        if (templateId == CustomCreatures.lilithWraithId) {
            return true;
        } else if (templateId == CustomCreatures.lilithZombieId) {
            return true;
        } else if (templateId == CustomCreatures.ifritFiendId) {
            return true;
        } else return templateId == CustomCreatures.ifritSpiderId;
    }

    public static boolean isTitanMinion(Creature creature) {
        return isTitanMinion(creature.getTemplate().getTemplateId());
    }

    // --- Advanced Abilities --- //
    public static void lilithMyceliumVoidAttack(Creature titan, Creature lCret, int tilex, int tiley) {
        if (lCret.isUnique() || lCret.isInvulnerable() || lCret == titan || isTitanMinion(lCret)) {
            return;
        }
        lCret.addWoundOfType(lCret, Wound.TYPE_INFECTION, 1, true, 1f, true, 50000f, 0f, 0f, true, false);
        /*if (!lCret.addWoundOfType(lCret, Wound.TYPE_INFECTION, 1, true, 1f, true, 50000f)) {
            Creatures.getInstance().setCreatureDead(lCret);
            Players.getInstance().setCreatureDead(lCret);
            lCret.setTeleportPoints((short)tilex, (short)tiley, titan.getLayer(), 0);
            lCret.startTeleporting();
            lCret.getCommunicator().sendAlertServerMessage("You are absorbed by the Mycelium and brought to Lilith!");
            lCret.getCommunicator().sendTeleport(false);
            if (!lCret.isPlayer()) {
                lCret.getMovementScheme().resumeSpeedModifier();
            }
        }*/
    }

    public static void ifritMassIncinerateAttack(Creature titan, Creature lCret) {
        if (lCret.isUnique() || lCret.isInvulnerable() || lCret == titan || isTitanMinion(lCret)) {
            return;
        }
        SpellEffect eff;
        SpellEffects effs = lCret.getSpellEffects();
        if (effs == null) {
            effs = lCret.createSpellEffects();
        }
        eff = effs.getSpellEffect((byte) 94);
        if (eff == null) {
            lCret.getCommunicator().sendAlertServerMessage("You are engulfed by the flames of Ifrit!", (byte) 4);
            eff = new SpellEffect(lCret.getWurmId(), (byte) 94, 80f, 180, (byte) 9, (byte) 1, true);
            effs.addSpellEffect(eff);
            Server.getInstance().broadCastAction(String.format("%s has engulfed %s in flames!", titan.getName(), lCret.getNameWithGenus()), titan, 50);
        } else {
            lCret.getCommunicator().sendAlertServerMessage("The heat around you increases. The pain is excruciating!", (byte) 4);
            eff.setPower(eff.getPower() + 200f);
            eff.setTimeleft(180);
            lCret.sendUpdateSpellEffect(eff);
            Server.getInstance().broadCastAction(String.format("%s has engulfed %s in flames again, increasing the intensity!", titan.getName(), lCret.getNameWithGenus()), titan, 50);
        }
    }

    public static void performAdvancedAbility(Creature titan) {
        int tilex = titan.getTileX();
        int tiley = titan.getTileY();
        if (titan.getTemplate().getTemplateId() == CustomCreatures.lilithId) { // Lilith Ability
            int tarx = (tilex - 3) + (Server.rand.nextInt(7));
            int tary = (tiley - 3) + (Server.rand.nextInt(7));
            int sx = Zones.safeTileX(tarx - 1);
            int ex = Zones.safeTileX(tarx + 1);
            int sy = Zones.safeTileY(tary - 1);
            int ey = Zones.safeTileY(tary + 1);
            Zones.flash(tarx, tary, false);
            Server.getInstance().broadCastAction(String.format("%s casts Mycelium Void, turning the earth to fungus and pulling enemies to %s!", titan.getName(), titan.getHimHerItString()), titan, 50);
            for (int x = sx; x <= ex; ++x) {
                for (int y = sy; y <= ey; ++y) {
                    VolaTile t = Zones.getOrCreateTile(x, y, true);
                    if (t == null) {
                        continue;
                    }
                    checkDestroyMineDoor(titan, x, y);
                    int tile = Server.surfaceMesh.getTile(x, y);
                    byte type = Tiles.decodeType((int) tile);
                    Tiles.Tile theTile = Tiles.getTile((byte) type);
                    byte data = Tiles.decodeData((int) tile);
                    // Copied from Fungus to prevent wacko behaviours like deleting minedoors and glitching tunnels:
                    if (type != Tiles.Tile.TILE_FIELD.id && type != Tiles.Tile.TILE_FIELD2.id && type != Tiles.Tile.TILE_GRASS.id && type != Tiles.Tile.TILE_REED.id && type != Tiles.Tile.TILE_DIRT.id && type != Tiles.Tile.TILE_LAWN.id && type != Tiles.Tile.TILE_STEPPE.id && !theTile.isNormalTree() && !theTile.isEnchanted() && !theTile.isNormalBush()) {
                        //
                    } else {
                        if (theTile.isNormalTree()) {
                            Server.setSurfaceTile(x, y, Tiles.decodeHeight((int) tile), theTile.getTreeType(data).asMyceliumTree(), data);
                        } else if (theTile.isEnchantedTree()) {
                            Server.setSurfaceTile(x, y, Tiles.decodeHeight((int) tile), theTile.getTreeType(data).asNormalTree(), data);
                        } else if (theTile.isNormalBush()) {
                            Server.setSurfaceTile(x, y, Tiles.decodeHeight((int) tile), theTile.getBushType(data).asMyceliumBush(), data);
                        } else if (theTile.isEnchantedBush()) {
                            Server.setSurfaceTile(x, y, Tiles.decodeHeight((int) tile), theTile.getBushType(data).asNormalBush(), data);
                        } else if (type == Tiles.Tile.TILE_LAWN.id) {
                            Server.setSurfaceTile(x, y, Tiles.decodeHeight((int) tile), Tiles.Tile.TILE_MYCELIUM_LAWN.id, (byte) 0);
                        } else {
                            Server.setSurfaceTile(x, y, Tiles.decodeHeight((int) tile), Tiles.Tile.TILE_MYCELIUM.id, (byte) 0);
                        }
                        Players.getInstance().sendChangedTile(x, y, true, false);
                    }
                    Creature[] crets2 = t.getCreatures();
                    for (Creature lCret : crets2) {
                        lilithMyceliumVoidAttack(titan, lCret, tilex, tiley);
                    }
                    VolaTile tCave = Zones.getOrCreateTile(x, y, false);
                    if (tCave == null) {
                        continue;
                    }
                    int tileCave = Server.caveMesh.getTile(x, y);
                    byte typeCave = Tiles.decodeType(tileCave);
                    if (typeCave != Tiles.Tile.TILE_CAVE.id && typeCave != Tiles.Tile.TILE_CAVE_EXIT.id && typeCave != Tiles.Tile.TILE_CAVE_FLOOR_REINFORCED.id && typeCave != Tiles.Tile.TILE_CAVE_PREPATED_FLOOR_REINFORCED.id) {
                        continue;
                    }
                    Creature[] crets3 = tCave.getCreatures();
                    for (Creature lCret : crets3) {
                        lilithMyceliumVoidAttack(titan, lCret, tilex, tiley);
                    }
                }
            }
        } else if (titan.getTemplate().getTemplateId() == CustomCreatures.ifritId) { // Ifrit Ability
            int tarx = (tilex - 4) + (Server.rand.nextInt(9));
            int tary = (tiley - 4) + (Server.rand.nextInt(9));
            int sx = Zones.safeTileX(tarx - 1);
            int ex = Zones.safeTileX(tarx + 1);
            int sy = Zones.safeTileY(tary - 1);
            int ey = Zones.safeTileY(tary + 1);
            Zones.flash(tarx, tary, false);
            Server.getInstance().broadCastAction(String.format("%s casts Mass Incinerate, burning enemies near %s!", titan.getName(), titan.getHimHerItString()), titan, 50);
            for (int x = sx; x <= ex; ++x) {
                for (int y = sy; y <= ey; ++y) {
                    VolaTile t = Zones.getOrCreateTile(x, y, true);
                    if (t == null) {
                        continue;
                    }
                    checkDestroyMineDoor(titan, x, y);
                    new AreaSpellEffect(titan.getWurmId(), x, y, titan.getLayer(), (byte) 35, System.currentTimeMillis() + 5000, 200.0f, titan.getLayer(), 0, true);
                    Creature[] crets2 = t.getCreatures();
                    for (Creature lCret : crets2) {
                        ifritMassIncinerateAttack(titan, lCret);
                    }
                    Creature[] undergroundCreatures = getUndergroundCreatures(x, y);
                    if (undergroundCreatures != null) {
                        for (Creature lCret : undergroundCreatures) {
                            ifritMassIncinerateAttack(titan, lCret);
                        }
                    }
                }
            }
        }
    }

    // --- Basic Abilities --- //
    public static void lilithPainRainAttack(Creature titan, Creature lCret, VolaTile t) {
        if (lCret.isUnique() || lCret.isInvulnerable() || lCret == titan || isTitanMinion(lCret)) {
            return;
        }
        t.sendAttachCreatureEffect(lCret, (byte) 8, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
        try {
            if (lCret.addWoundOfType(titan, Wound.TYPE_INFECTION, lCret.getBody().getRandomWoundPos(), false, 1f, false, 25000.0 * (double) lCret.addSpellResistance((short) 448), 0f, 0f, true, false)) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        lCret.setTarget(titan.getWurmId(), false);
    }

    public static void performBasicAbility(Creature titan) {
        int tilex = titan.getTileX();
        int tiley = titan.getTileY();
        if (titan.getTemplate().getTemplateId() == CustomCreatures.lilithId) { // Lilith Ability
            int sx = Zones.safeTileX(tilex - 10);
            int sy = Zones.safeTileY(tiley - 10);
            int ex = Zones.safeTileX(tilex + 10);
            int ey = Zones.safeTileY(tiley + 10);
            //this.calculateArea(sx, sy, ex, ey, tilex, tiley, layer, currstr);
            int x, y;
            Server.getInstance().broadCastAction(String.format("%s casts Pain Rain, harming all around %s!", titan.getName(), titan.getHimHerItString()), titan, 50);
            for (x = sx; x <= ex; ++x) {
                for (y = sy; y <= ey; ++y) {
                    VolaTile t = Zones.getTileOrNull(x, y, titan.isOnSurface());
                    if (t == null) {
                        continue;
                    }
                    Creature[] crets2 = t.getCreatures();
                    for (Creature lCret : crets2) {
                        lilithPainRainAttack(titan, lCret, t);
                    }
                    Creature[] undergroundCreatures = getUndergroundCreatures(x, y);
                    if (undergroundCreatures != null) {
                        for (Creature lCret : undergroundCreatures) {
                            lilithPainRainAttack(titan, lCret, t);
                        }
                    }
                }
            }
        } else if (titan.getTemplate().getTemplateId() == CustomCreatures.ifritId) { // Ifrit Ability
            int sx = Zones.safeTileX(tilex - 10);
            int sy = Zones.safeTileY(tiley - 10);
            int ex = Zones.safeTileX(tilex + 10);
            int ey = Zones.safeTileY(tiley + 10);
            int x, y;
            ArrayList<Creature> targets = new ArrayList<>();
            for (x = sx; x <= ex; ++x) {
                for (y = sy; y <= ey; ++y) {
                    VolaTile t = Zones.getTileOrNull(x, y, titan.isOnSurface());
                    if (t == null) {
                        continue;
                    }
                    Creature[] crets2 = t.getCreatures();
                    for (Creature lCret : crets2) {
                        if (lCret.isUnique() || lCret.isInvulnerable() || lCret == titan || isTitanMinion(lCret)) {
                            continue;
                        }
                        targets.add(lCret);
                    }
                    Creature[] undergroundCreatures = getUndergroundCreatures(x, y);
                    if (undergroundCreatures != null) {
                        for (Creature lCret : undergroundCreatures) {
                            if (lCret.isUnique() || lCret.isInvulnerable() || lCret == titan || isTitanMinion(lCret)) {
                                continue;
                            }
                            targets.add(lCret);
                        }
                    }
                }
            }
            if (!targets.isEmpty()) {
                Creature target = null;
                for (Creature cret : targets) {
                    if (cret.isHitched() || cret.isRidden()) {
                        target = cret;
                        break;
                    }
                }
                if (target == null) {
                    for (Creature cret : targets) {
                        if (cret.isPlayer()) {
                            target = cret;
                            break;
                        }
                    }
                }
                if (target == null) {
                    target = targets.get(Server.rand.nextInt(targets.size()));
                }
                if (target == null) {
                    RequiemLogging.logWarning("Something went absolutely horribly wrong and there is no target for the Titan.");
                }
                int damage = target.getStatus().damage;
                int minhealth = 65435;
                float maxdam = (float) Math.max(0, minhealth - damage);
                if (maxdam > 500f) {
                    Server.getInstance().broadCastAction(String.format("%s picks a target at random and Smites %s!", titan.getName(), target.getName()), titan, 50);
                    target.getCommunicator().sendAlertServerMessage(String.format("%s smites you.", titan.getName()), (byte) 4);
                    try {
                        target.addWoundOfType(titan, Wound.TYPE_BURN, target.getBody().getRandomWoundPos(), false, 1f, false, maxdam, 0f, 0f, true, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void summonChampions(Creature titan, int nums) {
        int templateType = -10;
        String spellName = "";
        if (titan.getTemplate().getTemplateId() == CustomCreatures.lilithId) {
            templateType = CustomCreatures.lilithWraithId;
            spellName = "Raise Wraith";
        } else if (titan.getTemplate().getTemplateId() == CustomCreatures.ifritId) {
            templateType = CustomCreatures.ifritFiendId;
            spellName = "Summon Fiend";
        }
        if (templateType == -10) {
            RequiemLogging.logWarning("[ERROR]: Template type not set in summonChampions()");
            return;
        }
        try {
            Server.getInstance().broadCastAction(String.format("%s casts %s, calling champions to %s aid!", titan.getName(), spellName, titan.getHimHerItString()), titan, 50);
            for (int i = 0; i < nums; i++) {
                int tilex = ((titan.getTileX() * 4) + 3) - Server.rand.nextInt(7);
                int tiley = ((titan.getTileY() * 4) + 3) - Server.rand.nextInt(7);
                int sx = Zones.safeTileX(tilex - 2);
                int sy = Zones.safeTileY(tiley - 2);
                int ex = Zones.safeTileX(tilex + 2);
                int ey = Zones.safeTileY(tiley + 2);
                Creature target = null;
                for (int x = sx; x <= ex; ++x) {
                    for (int y = sy; y <= ey; ++y) {
                        VolaTile t = Zones.getTileOrNull(x, y, titan.isOnSurface());
                        if (t == null) {
                            continue;
                        }
                        Creature[] crets2 = t.getCreatures();
                        for (Creature lCret : crets2) {
                            if (lCret.isUnique() || lCret.isInvulnerable() || lCret == titan || isTitanMinion(lCret))
                                continue;
                            if (Server.rand.nextInt(3) == 0) {
                                target = lCret;
                                break;
                            }
                        }
                        Creature[] undergroundCreatures = getUndergroundCreatures(x, y);
                        if (undergroundCreatures != null) {
                            for (Creature lCret : undergroundCreatures) {
                                if (lCret.isUnique() || lCret.isInvulnerable() || lCret == titan || isTitanMinion(lCret))
                                    continue;
                                if (Server.rand.nextInt(3) == 0) {
                                    target = lCret;
                                    break;
                                }
                            }
                        }
                        if (target != null) {
                            break;
                        }
                    }
                    if (target != null) {
                        break;
                    }
                }
                // public static Creature doNew(int templateid, float aPosX, float aPosY, float aRot, int layer, String name, byte gender) throws Exception {
                Creature champion = Creature.doNew(templateType, tilex, tiley, RandomUtils.getRandomRotation(), titan.getLayer(), "", (byte) 0);
                if (target != null) {
                    champion.setOpponent(target);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void summonMinions(Creature titan, int nums) {
        int templateType = -10;
        String spellName = "";
        if (titan.getTemplate().getTemplateId() == CustomCreatures.lilithId) {
            templateType = CustomCreatures.lilithZombieId;
            spellName = "Raise Zombie";
        } else if (titan.getTemplate().getTemplateId() == CustomCreatures.ifritId) {
            templateType = CustomCreatures.ifritSpiderId;
            spellName = "Summon Spider";
        }
        if (templateType == -10) {
            RequiemLogging.logWarning("[ERROR]: Template type not set in summonMinions()");
            return;
        }
        try {
            Server.getInstance().broadCastAction(String.format("%s casts %s, calling minions to %s aid!", titan.getName(), spellName, titan.getHimHerItString()), titan, 50);
            for (int i = 0; i < nums; i++) {
                int tilex = ((titan.getTileX() * 4) + 3) - Server.rand.nextInt(7);
                int tiley = ((titan.getTileY() * 4) + 3) - Server.rand.nextInt(7);
                int sx = Zones.safeTileX(tilex - 10);
                int sy = Zones.safeTileY(tiley - 10);
                int ex = Zones.safeTileX(tilex + 10);
                int ey = Zones.safeTileY(tiley + 10);
                Creature target = null;
                for (int x = sx; x <= ex; ++x) {
                    for (int y = sy; y <= ey; ++y) {
                        VolaTile t = Zones.getTileOrNull(x, y, titan.isOnSurface());
                        if (t == null) {
                            continue;
                        }
                        Creature[] crets2 = t.getCreatures();
                        for (Creature lCret : crets2) {
                            if (lCret.isUnique() || lCret.isInvulnerable() || lCret == titan || isTitanMinion(lCret))
                                continue;
                            if (Server.rand.nextInt(3) == 0) {
                                target = lCret;
                                break;
                            }
                        }
                        Creature[] undergroundCreatures = getUndergroundCreatures(x, y);
                        if (undergroundCreatures != null) {
                            for (Creature lCret : undergroundCreatures) {
                                if (lCret.isUnique() || lCret.isInvulnerable() || lCret == titan || isTitanMinion(lCret))
                                    continue;
                                if (Server.rand.nextInt(3) == 0) {
                                    target = lCret;
                                    break;
                                }
                            }
                        }
                        if (target != null) {
                            break;
                        }
                    }
                    if (target != null) {
                        break;
                    }
                }
                Creature minion = Creature.doNew(templateType, tilex, tiley, RandomUtils.getRandomRotation(), titan.getLayer(), "", (byte) 0);
                if (target != null) {
                    minion.setOpponent(target);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pollTitanSpawn() {
        Creature[] crets = Creatures.getInstance().getCreatures();
        for (Creature c : crets) {
            if (isTitan(c) && !titans.contains(c)) {
                titans.add(c);
            }
        }

        titans.removeIf(Creature::isDead);

        if (titans.isEmpty() && !Servers.isThisLoginServer()) {
            if (PollingConstants.lastPolledTitans + titanRespawnTime < System.currentTimeMillis()) {
                RequiemLogging.logInfo("No Titan was found, and the timer has expired. Spawning a new one.");
                float worldSizeX = Zones.worldTileSizeX;
                float worldSizeY = Zones.worldTileSizeY;
                float minX = worldSizeX * 0.25f;
                float minY = worldSizeY * 0.25f;
                int tilex = (int) (minX + (minX * 2 * Server.rand.nextFloat())) * 4;
                int tiley = (int) (minY + (minY * 2 * Server.rand.nextFloat())) * 4;
                int[] titanTemplates = {CustomCreatures.lilithId, CustomCreatures.ifritId}; // Lilith disabled for memory leak
                try {
                    Creature titan = Creature.doNew(titanTemplates[Server.rand.nextInt(titanTemplates.length)], tilex, tiley, RandomUtils.getRandomRotation(), 1, "", (byte) 0);
                    RequiemLogging.logInfo(String.format("New Titan %s spawned at %d, %d", titan.getName(), tilex, tiley));
                } catch (Exception e) {
                    RequiemLogging.logException("Failed to create Titan.", e.getCause());
                    e.printStackTrace();
                }
            }
        } else {
            PollingConstants.lastPolledTitans = System.currentTimeMillis();
        }
    }

    public static void pollTitan(Creature titan) {
        if (titanDamage.containsKey(titan)) {
            int prevDamage = titanDamage.get(titan);
            int currentDamage = titan.getStatus().damage;
            if (titan.isOnSurface() && Server.rand.nextInt(50) == 0) {
                performAdvancedAbility(titan);
            } else if (Server.rand.nextInt(20) == 0) {
                performAdvancedAbility(titan);
            }
            if (currentDamage > prevDamage) {
                // Health threshold actions
                if (currentDamage > 0 && prevDamage == 0) { // First attack
                    String msg = "<" + titan.getName() + " [100%]> Mere mortals dare to face me?";
                    DiscordHandler.sendToDiscord(CustomChannel.TITAN, msg);
                    Zones.flash(titan.getTileX(), titan.getTileY(), false);
                }
                if (currentDamage > 8191 && prevDamage < 8191) { // 87.5%
                    String msg = "<" + titan.getName() + " [88%]> You actually think you can defeat me?";
                    DiscordHandler.sendToDiscord(CustomChannel.TITAN, msg);
                    Zones.flash(titan.getTileX(), titan.getTileY(), false);
                }
                if (currentDamage > 16383 && prevDamage < 16383) { // 75%
                    String msg = "<" + titan.getName() + " [75%]> I am not alone.";
                    DiscordHandler.sendToDiscord(CustomChannel.TITAN, msg);
                    Zones.flash(titan.getTileX(), titan.getTileY(), false);
                    summonMinions(titan, Server.rand.nextInt(2) + 2);
                }
                if (currentDamage > 26214 && prevDamage < 26214) { // 60%
                    String msg = "<" + titan.getName() + " [60%]> You will feel my wrath!";
                    DiscordHandler.sendToDiscord(CustomChannel.TITAN, msg);
                    Zones.flash(titan.getTileX(), titan.getTileY(), false);
                    performBasicAbility(titan);
                }
                if (currentDamage > 32767 && prevDamage < 32767) { // 50%
                    String msg = "<" + titan.getName() + " [50%]> I've had enough of you. Minions, assemble!";
                    DiscordHandler.sendToDiscord(CustomChannel.TITAN, msg);
                    Zones.flash(titan.getTileX(), titan.getTileY(), false);
                    summonMinions(titan, Server.rand.nextInt(4) + 4);
                    performBasicAbility(titan);
                }
                if (currentDamage > 39321 && prevDamage < 39321) { // 40%
                    String msg = "<" + titan.getName() + " [40%]> Let's try something new, shall we?";
                    DiscordHandler.sendToDiscord(CustomChannel.TITAN, msg);
                    Zones.flash(titan.getTileX(), titan.getTileY(), false);
                    performAdvancedAbility(titan);
                }
                if (currentDamage > 45874 && prevDamage < 45874) { // 30%
                    String msg = "<" + titan.getName() + " [30%]> Perhaps minions aren't enough. Now, try my champions!";
                    DiscordHandler.sendToDiscord(CustomChannel.TITAN, msg);
                    Zones.flash(titan.getTileX(), titan.getTileY(), false);
                    summonChampions(titan, Server.rand.nextInt(2) + 2);
                    performBasicAbility(titan);
                }
                if (currentDamage > 52428 && prevDamage < 52428) { // 20%
                    String msg = "<" + titan.getName() + " [20%]> Enough! I will end you!";
                    DiscordHandler.sendToDiscord(CustomChannel.TITAN, msg);
                    Zones.flash(titan.getTileX(), titan.getTileY(), false);
                    performBasicAbility(titan);
                    performAdvancedAbility(titan);
                }
                if (currentDamage > 58981 && prevDamage < 58981) { // 10%
                    String msg = "<" + titan.getName() + " [10%]> Minions... Champions... Only one way to win a battle: An army!";
                    DiscordHandler.sendToDiscord(CustomChannel.TITAN, msg);
                    Zones.flash(titan.getTileX(), titan.getTileY(), false);
                    summonMinions(titan, Server.rand.nextInt(5) + 7);
                    summonChampions(titan, Server.rand.nextInt(3) + 3);
                    performBasicAbility(titan);
                    performAdvancedAbility(titan);
                }
                // Extra abilities
                if (currentDamage > 16383 && Server.rand.nextInt(10) == 0) {
                    if (currentDamage > 45874) {
                        summonMinions(titan, Server.rand.nextInt(2) + 2);
                    } else if (currentDamage > 32767) {
                        summonMinions(titan, Server.rand.nextInt(3) + 1);
                    } else {
                        summonMinions(titan, Server.rand.nextInt(2) + 1);
                    }
                }
                if (currentDamage > 16383 && Server.rand.nextInt(15) == 0) {
                    if (currentDamage > 45874) { // 30%
                        if (Server.rand.nextInt(10) == 0) {
                            performBasicAbility(titan);
                        }
                    } else if (currentDamage > 32767) { // 50%
                        if (Server.rand.nextInt(12) == 0) {
                            performBasicAbility(titan);
                        }
                    } else { // 75%
                        if (Server.rand.nextInt(10) == 0) {
                            performBasicAbility(titan);
                        }
                    }
                }
                if (currentDamage > 32767 && Server.rand.nextInt(20) == 0) {
                    if (currentDamage > 45874) {
                        if (Server.rand.nextInt(15) == 0) {
                            performAdvancedAbility(titan);
                        }
                    } else {
                        if (Server.rand.nextInt(20) == 0) {
                            performAdvancedAbility(titan);
                        }
                    }
                }
                if (currentDamage > 45874 && Server.rand.nextInt(15) == 0) {
                    summonChampions(titan, 1);
                }
                titanDamage.put(titan, currentDamage);
            }
        } else {
            titanDamage.put(titan, titan.getStatus().damage);
        }
    }

    public static void pollTitans() {
        for (Creature c : titans) {
            if (isTitan(c)) {
                pollTitan(c);
            }
        }
    }

    public static void addTitan(Creature mob) {
        if (isTitan(mob) && !titans.contains(mob)) {
            titans.add(mob);
        }
    }

    public static void removeTitan(Creature mob) {
        if (isTitan(mob) && titans.contains(mob)) {
            titans.remove(mob);
        }
    }

    public static void locateTitans() {
        if (!detected) {
            if (Server.getSecondsUptime() >= TimeConstants.MINUTE * 4 && Server.getSecondsUptime() <= TimeConstants.MINUTE * 5) {
                for (Creature creature : Creatures.getInstance().getCreatures()) {
                    if (Titans.isTitan(creature)) {
                        DiscordHandler.sendToDiscord(CustomChannel.TITAN, String.format("A Titan has been detected by the mystical Wizards of Altura. The Titan has been identified as %s. The wizards are focusing their powers on it and pinpointing its location......", creature.getName()));
                        detected = true;
                    }
                }
            }
        }
        if (!located) {
            if (Server.getSecondsUptime() >= TimeConstants.MINUTE * 9 && Server.getSecondsUptime() <= TimeConstants.MINUTE * 10) {
                for (Creature creature : Creatures.getInstance().getCreatures()) {
                    if (Titans.isTitan(creature)) {
                        DiscordHandler.sendToDiscord(CustomChannel.TITAN, String.format("%s has been located! Its position in the world is %s, %s", creature.getName(), (int) creature.getPosX() / 4, (int) creature.getPosY() / 4));
                        located = true;
                    }
                }
            }
        }
    }

    public static void preInit() {
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();
            Class<Titans> thisClass = Titans.class;
            String replace;

            // - Add poll for Titans - //
            CtClass ctServer = classPool.get("com.wurmonline.server.Server");
            ctServer.getDeclaredMethod("run").instrument(new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("pruneTransfers")) {
                        m.replace(String.format("%s.pollTitanSpawn();$_ = $proceed($$);", Titans.class.getName()));
                        return;
                    }
                }
            });
            ctServer.getDeclaredMethod("run").instrument(new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("pollValreiData")) {
                        m.replace(String.format("%s.pollTitans();$_ = $proceed($$);", Titans.class.getName()));
                        return;
                    }
                }
            });

            // - Remove regeneration from titans - //
            CtClass ctWound = classPool.get("com.wurmonline.server.bodys.Wound");

            ctWound.getDeclaredMethod("poll").instrument(new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("modifySeverity")) {
                        m.replace(String.format("if(!%s.isTitan(this.creature)){  $_ = $proceed($$);}", Titans.class.getName()));
                        return;
                    }
                }
            });
            ctWound.getDeclaredMethod("poll").instrument(new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("checkInfection")) {
                        m.replace(String.format("if(!%s.isTitan(this.creature)){  $_ = $proceed($$);}", Titans.class.getName()));
                        return;
                    }
                }
            });
            ctWound.getDeclaredMethod("poll").instrument(new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("checkPoison")) {
                        m.replace(String.format("if(!%s.isTitan(this.creature)){  $_ = $proceed($$);}", Titans.class.getName()));
                        return;
                    }
                }
            });

            // - Disable Smiting Titans - //
            CtClass ctSmite = classPool.get("com.wurmonline.server.spells.Smite");
            ctSmite.getDeclaredMethod("precondition").insertBefore(String.format("if(%s.isTitan($3)){  $2.getCommunicator().sendNormalServerMessage(\"You cannot smite a Titan!\");  return false;}", Titans.class.getName()));

            Util.setReason("Disable casting Worm Brains on titan.");
            CtClass ctWormBrains = classPool.get("com.wurmonline.server.spells.WormBrains");
            replace = String.format("if(%s.isTitan($3)){  $2.getCommunicator().sendNormalServerMessage(\"Titans are immune to that spell.\");  return false;}", Titans.class.getName());
            Util.insertBeforeDeclared(thisClass, ctWormBrains, "precondition", replace);

        } catch (NotFoundException | CannotCompileException e) {
            throw new HookException(e);
        }
    }
}