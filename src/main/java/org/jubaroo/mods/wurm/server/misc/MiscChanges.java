package org.jubaroo.mods.wurm.server.misc;

import com.wurmonline.server.Items;
import com.wurmonline.server.Server;
import com.wurmonline.server.Servers;
import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.SpellEffectsEnum;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemTemplate;
import com.wurmonline.server.items.SimpleCreationEntry;
import com.wurmonline.server.players.PlayerInfo;
import com.wurmonline.server.players.PlayerInfoFactory;
import com.wurmonline.server.skills.Skill;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.server.villages.Village;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zones;
import com.wurmonline.shared.constants.Enchants;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Optional;

public class MiscChanges {
    private static final int rarityChance = 3600;
    private static final HashMap<Long, Integer> pseudoMap = new HashMap<>();

    private static String fixActionString(final Creature c, String s) {
        s = s.replace("%HIS", c.isNotFemale() ? "his" : "her");
        s = s.replace("%NAME", c.getName());
        s = s.replace("%NAME'S", c.getName() + "'s");
        s = s.replace("%HIMSELF", c.isNotFemale() ? "himself" : "herself");
        s = s.replace("%HIM", c.isNotFemale() ? "him" : "her");
        return s;
    }

    private static void actionNotify(final Creature c, @Nullable String myMsg, @Nullable String othersMsg, @Nullable String stealthOthersMsg, @Nullable final Creature[] excludeFromBroadCast) {
        if (excludeFromBroadCast != null) {
            final int length = excludeFromBroadCast.length;
        }
        if (myMsg != null) {
            myMsg = fixActionString(c, myMsg);
            c.getCommunicator().sendNormalServerMessage(myMsg);
        }
        if (stealthOthersMsg != null && c.isStealth()) {
            stealthOthersMsg = fixActionString(c, stealthOthersMsg);
            Server.getInstance().broadCastAction(stealthOthersMsg, c, 8);
        } else if (othersMsg != null) {
            othersMsg = fixActionString(c, othersMsg);
            Server.getInstance().broadCastAction(othersMsg, c, 8);
        }
    }

    public static void actionNotify(final Creature c, @Nullable final String myMsg, @Nullable final String othersMsg, @Nullable final String stealthOthersMsg) {
        actionNotify(c, myMsg, othersMsg, stealthOthersMsg, null);
    }

    public static boolean checkMayorCommand(Item item, Creature creature) {
        if (Servers.localServer.PVPSERVER) {
            return false;
        }
        PlayerInfo pinf = PlayerInfoFactory.getPlayerInfoWithWurmId(item.getLastOwnerId());
        if (pinf != null) {
            if (pinf.getLastLogout() < System.currentTimeMillis() - TimeConstants.MONTH_MILLIS) {
                if (creature.getCitizenVillage() != null) {
                    Village v = creature.getCitizenVillage();
                    if (v.getMayor().getId() == creature.getWurmId()) {
                        VolaTile vt = Zones.getTileOrNull(item.getTilePos(), item.isOnSurface());
                        return vt != null && vt.getVillage() != null && vt.getVillage() == v;
                    }
                }
            }
        }
        return false;
    }

    public static float getFoodOpulenceBonus(Item food) {
        float mult = 1f;
        if (food.getSpellEffectPower(Enchants.BUFF_OPULENCE) > 0f) {
            mult *= 1f + (food.getSpellEffectPower(Enchants.BUFF_OPULENCE) * 0.0025f);
        }
        return food.getFoodComplexity() * mult;
    }

    public static long getBedBonus(long secs, long bed) {
        // TODO make different beds give different bonuses
        Optional<Item> beds = Items.getItemOptional(bed);
        if (beds.isPresent()) {
            Item bedItem = beds.get();
            if (bedItem.isBed()) {
                secs *= 1 + (bedItem.getCurrentQualityLevel() * 0.005f);
            }
        }
        secs *= 2;
        return secs;
    }

    public static boolean royalSmithImprove(Creature performer, Skill improve) {
        if (performer.isRoyalSmith()) {
            return improve.getNumber() == SkillList.SMITHING_ARMOUR_CHAIN
                    || improve.getNumber() == SkillList.SMITHING_ARMOUR_PLATE
                    || improve.getNumber() == SkillList.SMITHING_BLACKSMITHING
                    || improve.getNumber() == SkillList.SMITHING_GOLDSMITHING
                    || improve.getNumber() == SkillList.SMITHING_LOCKSMITHING
                    || improve.getNumber() == SkillList.SMITHING_METALLURGY
                    || improve.getNumber() == SkillList.SMITHING_SHIELDS
                    || improve.getNumber() == SkillList.SMITHING_WEAPON_BLADES
                    || improve.getNumber() == SkillList.SMITHING_WEAPON_HEADS;
        }
        return false;
    }

    public static int getNewFoodFill(float qlevel) {
        float startPercent = 0.004f;
        float endPercent = 0.015f;
        return (int) ((startPercent * (1f - qlevel / 100f) + endPercent * (qlevel / 100f)) * 65535);
    }

    public static boolean rollRarityImprove(Item source, int usedWeight) {
        int templateWeight = source.getTemplate().getWeightGrams();
        float percentUsage = (float) usedWeight / (float) templateWeight;
        float chance = percentUsage * 0.05f;
        return Server.rand.nextFloat() < chance;
    }

    public static boolean getRarityWindowChance(long wurmid) { //nextInt checks against 0. False is true, true is false.
        if (pseudoMap.containsKey(wurmid)) {
            int currentChance = pseudoMap.get(wurmid);
            boolean success = Server.rand.nextInt(currentChance) == 0;
            if (success) {
                pseudoMap.put(wurmid, currentChance + rarityChance - 1);
            } else {
                pseudoMap.put(wurmid, currentChance - 1);
            }
            return !success;
        } else {
            pseudoMap.put(wurmid, rarityChance - 1);
            return !(Server.rand.nextInt(rarityChance) == 0);
        }
    }

    public static byte getNewCreationRarity(SimpleCreationEntry entry, Item source, Item target, ItemTemplate template) {
        if (source.getRarity() > 0 || target.getRarity() > 0) {
            byte sRarity = source.getRarity();
            byte tRarity = target.getRarity();
            int sourceid = entry.getObjectSource();
            int targetid = entry.getObjectTarget();
            Item realSource = null;
            if (source.getTemplateId() == sourceid) {
                realSource = source;
            } else if (target.getTemplateId() == sourceid) {
                realSource = target;
            }
            Item realTarget = null;
            if (source.getTemplateId() == targetid) {
                realTarget = source;
            } else if (target.getTemplateId() == targetid) {
                realTarget = target;
            }
            if (entry.depleteSource && entry.depleteTarget) {
                int min = Math.min(sRarity, tRarity);
                int max = Math.max(sRarity, tRarity);
                return (byte) (min + Server.rand.nextInt(1 + (max - min)));
            }
            if (realSource == null || realTarget == null) {
                RequiemLogging.logInfo("Null source or target.");
                return 0;
            }
            if (entry.depleteSource && realSource.getRarity() > 0) {
                int templateWeight = realSource.getTemplate().getWeightGrams();
                int usedWeight = entry.getSourceWeightToRemove(realSource, realTarget, template, false);
                float percentUsage = (float) usedWeight / (float) templateWeight;
                float chance = percentUsage * 0.05f;
                if (Server.rand.nextFloat() < chance) {
                    return realSource.getRarity();
                }
            } else if (entry.depleteTarget && realTarget.getRarity() > 0) {
                int templateWeight = realTarget.getTemplate().getWeightGrams();
                int usedWeight = entry.getTargetWeightToRemove(realSource, realTarget, template, false);
                float percentUsage = (float) usedWeight / (float) templateWeight;
                float chance = percentUsage * 0.05f;
                if (Server.rand.nextFloat() < chance) {
                    return target.getRarity();
                }
            }
        }
        return 0;
    }

    public static boolean shouldSendBuff(SpellEffectsEnum effect) {
        // Continue not showing any that don't have a buff in the first place
        if (!effect.isSendToBuffBar()) {
            return false;
        }
        // Resistances and vulnerabilities are 20 - 43
        return effect.getTypeId() > 43 || effect.getTypeId() < 20;

        // Is send to buff bar and not something we're stopping, so allow it.
    }
}
