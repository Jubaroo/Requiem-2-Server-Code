package org.jubaroo.mods.wurm.server.tools;

import com.wurmonline.server.creatures.*;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.spells.SpellEffect;
import com.wurmonline.server.villages.Villages;
import com.wurmonline.server.zones.NoSuchZoneException;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zone;
import com.wurmonline.server.zones.Zones;
import com.wurmonline.shared.constants.CreatureTypes;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.creatures.CustomCreatures;
import org.jubaroo.mods.wurm.server.creatures.MethodsBestiary;
import org.jubaroo.mods.wurm.server.creatures.Titans;
import org.jubaroo.mods.wurm.server.utils.TweakApiPerms;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class CreatureTools {

    //TODO redo these like other int arrays
    public static int[] randomDepotCreature = {
            // Requiem
            CustomCreatures.largeBoarId,
            CustomCreatures.gorillaId,
            CustomCreatures.hyenaId,
            CustomCreatures.wolfPackmasterId,
            CustomCreatures.iceCatId,
            CustomCreatures.fireCrabId,
            CustomCreatures.fireGiantId,
            CustomCreatures.blackWidowId,
            CustomCreatures.ocelotId,
            CustomCreatures.pandaId,
            CustomCreatures.pantherId,
            CustomCreatures.polarBearId,
            CustomCreatures.tombRaiderId,
            CustomCreatures.avengerId,
            CustomCreatures.blobId,
            CustomCreatures.prismaticBlobId,
            CustomCreatures.cyclopsId,
            CustomCreatures.forestSpiderId,
            CustomCreatures.golemId,
            CustomCreatures.spiritTrollId,
            CustomCreatures.reanimatedSkeletonId,
            CustomCreatures.zombieLeaderId,
            CustomCreatures.zombieHulkId,
            CustomCreatures.zombieWalkerId,
            CustomCreatures.depotTrollId,
            CustomCreatures.depotTrollId, CustomCreatures.depotTrollId, CustomCreatures.depotTrollId, // 3x the chance to spawn one
            // Vanilla
            CreatureTemplateIds.WOLF_BLACK_CID,
            CreatureTemplateIds.TROLL_CID,
            CreatureTemplateIds.BEAR_BROWN_CID,
            CreatureTemplateIds.RAT_LARGE_CID,
            CreatureTemplateIds.LION_MOUNTAIN_CID,
            CreatureTemplateIds.CAT_WILD_CID,
            CreatureTemplateIds.GOBLIN_CID,
            CreatureTemplateIds.SPIDER_CID,
            CreatureTemplateIds.ANACONDA_CID,
            CreatureTemplateIds.BEAR_BLACK_CID,
            CreatureTemplateIds.CAVE_BUG_CID,
            CreatureTemplateIds.LAVA_SPIDER_CID,
            CreatureTemplateIds.LAVA_CREATURE_CID,
            CreatureTemplateIds.CROCODILE_CID,
            CreatureTemplateIds.SCORPION_CID,
            CreatureTemplateIds.DEMON_SOL_CID,
            CreatureTemplateIds.DEATHCRAWLER_MINION_CID,
            CreatureTemplateIds.SPAWN_UTTACHA_CID,
            CreatureTemplateIds.DRAKESPIRIT_CID,
            CreatureTemplateIds.EAGLESPIRIT_CID,
            CreatureTemplateIds.HELL_HOUND_CID,
            CreatureTemplateIds.HELL_SCORPION_CID,
            CreatureTemplateIds.WORG_CID,
            CreatureTemplateIds.DEMON_SOL_CID,
            CreatureTemplateIds.WRAITH_CID,
            CreatureTemplateIds.SPIDER_FOG_CID
    };

    public static int[] randomUndead = {
            CustomCreatures.zombieWalkerId,
            CustomCreatures.reanimatedSkeletonId,
            CustomCreatures.zombieHulkId,
            CustomCreatures.wraithId
    };

    public static int[] randomDrakeling = {
            CreatureTemplateIds.DRAKE_BLACK_CID,
            CreatureTemplateIds.DRAKE_BLUE_CID,
            CreatureTemplateIds.DRAKE_GREEN_CID,
            CreatureTemplateIds.DRAKE_RED_CID,
            CreatureTemplateIds.DRAKE_WHITE_CID
    };

    public static int[] randomDragon = {
            CreatureTemplateIds.DRAGON_BLACK_CID,
            CreatureTemplateIds.DRAGON_BLUE_CID,
            CreatureTemplateIds.DRAGON_GREEN_CID,
            CreatureTemplateIds.DRAGON_RED_CID,
            CreatureTemplateIds.DRAGON_WHITE_CID
    };

    public static int[] randomRiftCreature = {
            CreatureTemplateIds.RIFT_JACKAL_ONE_CID,
            CreatureTemplateIds.RIFT_JACKAL_TWO_CID,
            CreatureTemplateIds.RIFT_JACKAL_THREE_CID,
            CreatureTemplateIds.RIFT_JACKAL_FOUR_CID,
            CreatureTemplateIds.RIFT_JACKAL_CASTER_CID,
            CreatureTemplateIds.RIFT_JACKAL_SUMMONER_CID,
            CreatureTemplateIds.RIFT_OGRE_MAGE_CID
    };

    public static int[] randomUniqueCreature = {
            CreatureTemplateIds.FOREST_GIANT_CID,
            CreatureTemplateIds.TROLL_KING_CID,
            CreatureTemplateIds.GOBLIN_LEADER_CID,
            CreatureTemplateIds.CYCLOPS_CID
    };

    public static int[] randomCreatureType = {
            CreatureTypes.C_MOD_NONE,
            CreatureTypes.C_MOD_FIERCE,
            CreatureTypes.C_MOD_ANGRY,
            CreatureTypes.C_MOD_RAGING,
            CreatureTypes.C_MOD_SLOW,
            CreatureTypes.C_MOD_ALERT,
            CreatureTypes.C_MOD_GREENISH,
            CreatureTypes.C_MOD_LURKING,
            CreatureTypes.C_MOD_SLY,
            CreatureTypes.C_MOD_HARDENED,
            CreatureTypes.C_MOD_SCARED,
            CreatureTypes.C_MOD_DISEASED,
            CreatureTypes.C_MOD_CHAMPION
    };

    public static String getTypeString(Creature creature) {
        if (CreatureStatus.getModtypeForString(creature.getName()) > 0) {
            switch (CreatureStatus.getModtypeForString(creature.getName())) {
                case 1:
                    return "fierce ";
                case 2:
                    return "angry ";
                case 3:
                    return "raging ";
                case 4:
                    return "slow ";
                case 5:
                    return "alert ";
                case 6:
                    return "greenish ";
                case 7:
                    return "lurking ";
                case 8:
                    return "sly ";
                case 9:
                    return "hardened ";
                case 10:
                    return "scared ";
                case 11:
                    return "diseased ";
                case 99:
                    return "champion ";
                default:
                    return "";
            }
        } else {
            return "normal";
        }
    }

    public static String getSexString(byte sex) {
        switch (sex) {
            case 0:
                return "male";
            case 1:
                return "female";
            default:
                return "";
        }
    }

    /**
     * @param performer creature to check around
     * @param tileDist  distance from performer to check around for players
     * @return list of creatures near the player in a distance set by tileDist
     */
    public static Set<Creature> getCreaturesAround(final Creature performer, final int tileDist) {
        Set<Creature> result = new HashSet<>();
        final int lTileDist = Math.abs(tileDist);
        final int tilex = performer.getTileX();
        final int tiley = performer.getTileY();
        for (int x = tilex - lTileDist; x <= tilex + lTileDist; ++x) {
            for (int y = tiley - lTileDist; y <= tiley + lTileDist; ++y) {
                VolaTile tile = Zones.getTileOrNull(x, y, performer.isOnSurface());
                if (tile != null)
                    result.addAll(Arrays.asList(tile.getCreatures()));
            }
        }
        return result;
    }

    public static void changeCreatureModType(Creature creature, byte newType) throws NoSuchFieldException, IllegalAccessException {
        ReflectionUtil.setPrivateField(creature.getStatus(), ReflectionUtil.getField(CreatureStatus.class, "modtype"), newType);
    }

    public static CreatureTemplate getTemplate(String name) {
        CreatureTemplateFactory fact = CreatureTemplateFactory.getInstance();
        HashMap<String, CreatureTemplate> tmap = (HashMap<String, CreatureTemplate>) TweakApiPerms.getItemField(fact, "templatesByName");
        if (tmap == null) {
            RequiemLogging.logInfo(String.format("CreatureTool.getTemplate(%s) -> null", name));
            return null;
        }
        return tmap.get(name);
    }

    public static HashMap<String, Integer> getCreatureHisto(boolean killable) {
        HashMap<String, Integer> histo = new HashMap<>();
        Integer v;
        for (Creature targ : Creatures.getInstance().getCreatures()) {

            if (killable && !isOkToDestroy(targ)) {
                continue;
            }

            CreatureTemplate tplat = targ.getTemplate();
            String name = tplat.getName();

            v = histo.get(name);
            if (v == null) {
                histo.put(name, 1);
            } else {
                histo.put(name, v + 1);
            }
        }
        return histo;
    }

    public static boolean isOnDeed(Creature target) {
        return Villages.getVillage(target.getTileX(), target.getTileY(), true) != null;
    }

    public static boolean isOkToDestroy(Creature target) {
        if (isOnDeed(target)) {
            RequiemLogging.logInfo("onDeed:" + target.getName());
            return false;
        }
        if (target.isBranded()) {
            return false;
        }
        if (target.isDominated()) {
            return false;
        }
        if (target.isUndead()) {
            return false;
        }
        if (target.isReborn()) {
            return false;
        }
        if (target.getHitched() != null) {
            return false;
        }
        return !target.isRidden();
    }

    public static void cullByName(Integer count, String name) throws ArrayIndexOutOfBoundsException {
        LinkedList<Creature> list = new LinkedList<>();

        for (Creature targ : getCreaturesByName(name)) {
            RequiemLogging.logInfo("by name: " + targ.getName());
            if (!isOkToDestroy(targ)) {
                continue;
            }
            RequiemLogging.logInfo("to kill!");
            list.add(targ);
        }

        Collections.shuffle(list);

        //Set<Creature> randSet = new HashSet<Creature>(list.subList(0, count));
        count = Math.min(count, list.size());
        for (Creature targ : list.subList(0, count)) {
            targ.destroy();
        }

    }

    public static Creature[] getCreaturesByName(String name) {
        HashSet<Creature> ret = new HashSet<>();
        for (Creature targ : Creatures.getInstance().getCreatures()) {
            if (targ.getTemplate().getName().equals(name)) {
                ret.add(targ);
            }
        }
        return ret.toArray(new Creature[ret.size()]);
    }

    static boolean addTemplateTypes(String creatureName, int... types) {
        Method meth;

        CreatureTemplate tmpl = getTemplate(creatureName);
        if (tmpl == null) {
            return false;
        }

        for (Method derp : tmpl.getClass().getDeclaredMethods()) {
            RequiemLogging.logWarning("templ Meth: " + derp.getName());
        }

        meth = TweakApiPerms.getClassMeth("com.wurmonline.server.creatures.CreatureTemplate", "assignTypes", "int[]");
        if (meth == null) {
            return false;
        }

        try {
            meth.invoke(tmpl, types);
            return true;
        } catch (Throwable e) {
            RequiemLogging.logException("addTemplateTypes: " + e.toString(), e);
        }
        return false;
    }

    public static void applyBuff(Player player, byte enchant, float power, int time, boolean persist) {
        final SpellEffects effs = player.createSpellEffects();
        SpellEffect spellEffect = effs.getSpellEffect(enchant);
        if (spellEffect == null) {
            spellEffect = new SpellEffect(player.getWurmId(), enchant, power, time, (byte) 1, (byte) 0, persist);
            effs.addSpellEffect(spellEffect);
        }
    }

    public static float getAdjustedSizeMod(CreatureStatus status) {
        try {
            Creature statusHolder = ReflectionUtil.getPrivateField(status, ReflectionUtil.getField(status.getClass(), "statusHolder"));
            float aiDataModifier = 1f;
            if (statusHolder.getCreatureAIData() != null) {
                aiDataModifier = statusHolder.getCreatureAIData().getSizeModifier();
            }
            byte modtype = ReflectionUtil.getPrivateField(status, ReflectionUtil.getField(status.getClass(), "modtype"));
            float ageSizeModifier = ReflectionUtil.callPrivateMethod(status, ReflectionUtil.getMethod(status.getClass(), "getAgeSizeModifier"));
            float floatToRet = 1f;
            if (/*(!statusHolder.isVehicle() || statusHolder.isDragon()) &&*/ modtype != 0) {
                float change = 0f;
                switch (modtype) {
                    case CreatureTypes.C_MOD_RAGING: {
                        change = 0.4f;
                        break;
                    }
                    case CreatureTypes.C_MOD_SLOW: {
                        change = 0.7f;
                        break;
                    }
                    case CreatureTypes.C_MOD_GREENISH: {
                        change = 1f;
                        break;
                    }
                    case CreatureTypes.C_MOD_LURKING: {
                        change = -0.2f;
                        break;
                    }
                    case CreatureTypes.C_MOD_SLY: {
                        change = -0.1f;
                        break;
                    }
                    case CreatureTypes.C_MOD_HARDENED: {
                        change = 0.5f;
                        break;
                    }
                    case CreatureTypes.C_MOD_SCARED: {
                        change = 0.3f;
                        break;
                    }
                    case CreatureTypes.C_MOD_CHAMPION: {
                        change = 2f;
                        break;
                    }
                    case CreatureTypes.C_MOD_SIZESMALL: {
                        change = -0.5f;
                        break;
                    }
                    case CreatureTypes.C_MOD_SIZEMINI: {
                        change = -0.75f;
                        break;
                    }
                    case CreatureTypes.C_MOD_SIZETINY: {
                        change = -0.875f;
                        break;
                    }
                    default: {
                    }
                }
                if (MethodsBestiary.isUsuallyHitched(statusHolder.getTemplate().getTemplateId())) {
                    change *= 0.2f;
                }
                floatToRet += change;
            }
            int templateId = statusHolder.getTemplate().getTemplateId();
            if (templateId == CustomCreatures.lilithId) {
                floatToRet *= 0.5f;
            } else if (templateId == CustomCreatures.ifritId) {
                floatToRet *= 2.5f;
            } else if (templateId == CustomCreatures.blackWyvernId) {
                floatToRet *= 0.6f;
            } else if (templateId == CustomCreatures.greenWyvernId) {
                floatToRet *= 0.6f;
            } else if (templateId == CustomCreatures.redWyvernId) {
                floatToRet *= 0.6f;
            } else if (templateId == CustomCreatures.whiteWyvernId) {
                floatToRet *= 0.6f;
            } else if (templateId == CustomCreatures.blueWyvernId) {
                floatToRet *= 0.6f;
            } else if (templateId == CustomCreatures.forestSpiderId) {
                floatToRet *= 0.7f;
            } else if (templateId == CustomCreatures.avengerId) {
                floatToRet *= 0.35f;
            } else if (templateId == CustomCreatures.largeBoarId) {
                floatToRet *= 1.8f;
            } else if (templateId == CustomCreatures.spiritTrollId) {
                floatToRet *= 1.2f;
            } else if (templateId == CustomCreatures.giantId) {
                floatToRet *= 0.75f;
            } else if (templateId == CustomCreatures.lilithZombieId) {
                floatToRet *= 0.75f;
            } else if (templateId == CustomCreatures.chargerId) {
                floatToRet *= 1.15f;
            } else if (templateId == CustomCreatures.terrorId) {
                floatToRet *= 3f;
            } else if (templateId == CustomCreatures.iceCatId) {
                floatToRet *= 1.7f;
            } else if (templateId == CustomCreatures.kongId) {
                floatToRet *= 10f;
            } else if (templateId == CustomCreatures.bloblingId) {
                floatToRet *= 0.35f;
            } else if (templateId == CustomCreatures.prismaticBloblingId) {
                floatToRet *= 0.35f;
            }
            if (statusHolder.getHitched() == null && statusHolder.getTemplate().getTemplateId() == CreatureTemplateIds.BISON_CID && !statusHolder.getNameWithoutPrefixes().equalsIgnoreCase(statusHolder.getTemplate().getName())) {
                floatToRet *= 2f;
            }
            if (!statusHolder.isVehicle() && statusHolder.hasTrait(28)) {
                floatToRet *= 1.5f;
            }

            return floatToRet * ageSizeModifier * aiDataModifier;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return 1f;
    }

    // Set what creatures have custom corpse sizes
    public static boolean hasCustomCorpseSize(Creature creature) {
        int templateId = creature.getTemplate().getTemplateId();
        if (templateId == CustomCreatures.avengerId) {
            return true;
        } else if (templateId == CustomCreatures.cyclopsId) {
            return true;
        } else if (templateId == CustomCreatures.warHoundId) {
            return true;
        } else if (templateId == CustomCreatures.polarBearId) {
            return true;
        } else if (templateId == CustomCreatures.ominousTreeId) {
            return true;
        } else if (templateId == CustomCreatures.kongId) {
            return true;
        } else {
            return Titans.isTitan(creature);
        }
    }

    // Set custom corpse sizes
    public static void setCorpseSizes(Creature creature, Item corpse) {
        if (corpse.getTemplateId() != ItemList.corpse) {
            return;
        }
        int cid = creature.getTemplate().getTemplateId();
        boolean sendStatus = false;
        int size = 50000;
        if (cid == CustomCreatures.avengerId) {
            size *= 1.2;
            corpse.setSizes(size);
            sendStatus = true;
        } else if (Titans.isTitan(creature)) {
            size *= 1.5;
            corpse.setSizes(size);
            sendStatus = true;
        } else if (cid == CustomCreatures.cyclopsId) {
            size *= 0.05;
            corpse.setSizes(size);
            sendStatus = true;
        } else if (cid == CustomCreatures.warHoundId) {
            size *= 0.3;
            corpse.setSizes(size);
            sendStatus = true;
        } else if (cid == CustomCreatures.polarBearId) {
            size *= 1.3;
            corpse.setSizes(size);
            sendStatus = true;
        } else if (cid == CustomCreatures.ominousTreeId) {
            size *= 100.0;
            corpse.setSizes(size);
            sendStatus = true;
        } else if (cid == CustomCreatures.kongId) {
            size *= 100.0;
            corpse.setSizes(size);
            sendStatus = true;
        } else {
            corpse.setSizes((int) ((float) (corpse.getSizeX() * (creature.getSizeModX() & 255)) / 64f), (int) ((float) (corpse.getSizeY() * (creature.getSizeModY() & 255)) / 64f), (int) ((float) (corpse.getSizeZ() * (creature.getSizeModZ() & 255)) / 64f));
        }
        if (sendStatus) {
            try {
                Zone zone = Zones.getZone((int) corpse.getPosX() >> 2, (int) corpse.getPosY() >> 2, corpse.isOnSurface());
                zone.removeItem(corpse, true, true);
                zone.addItem(corpse, true, false, false);
            } catch (NoSuchZoneException e) {
                e.printStackTrace();
            }
        }
    }

}
