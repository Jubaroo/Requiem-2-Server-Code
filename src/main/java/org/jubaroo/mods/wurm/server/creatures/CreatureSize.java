package org.jubaroo.mods.wurm.server.creatures;

import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.CreatureStatus;
import com.wurmonline.server.creatures.CreatureTemplateIds;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.zones.NoSuchZoneException;
import com.wurmonline.server.zones.Zone;
import com.wurmonline.server.zones.Zones;
import com.wurmonline.shared.constants.CreatureTypes;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;

public class CreatureSize {

    //TODO
    //move to creature tools

    // Enable adjusting size for creatures
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
                floatToRet *= 5f;
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
