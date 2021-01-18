package org.jubaroo.mods.wurm.server.creatures;

import com.wurmonline.server.Server;
import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.Creatures;
import javassist.ClassPool;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.util.HashMap;

public class Bloodlust {
    protected static HashMap<Long, Float> lusts = new HashMap<>();
    protected static HashMap<Long, Long> lastLusted = new HashMap<>();

    public static float lustUnique(Creature creature) {
        long wurmid = creature.getWurmId();
        if (lusts.containsKey(wurmid)) {
            float currentLust = lusts.get(wurmid);
            if (currentLust >= 1f) { // When dealing more than 100% extra damage
                Server.getInstance().broadCastAction(String.format("%s becomes enraged!", creature.getName()), creature, 50);
            } else if (currentLust >= 0.49f) { // When dealing between 50% and 100% extra damage.
                Server.getInstance().broadCastAction(String.format("%s is becoming enraged!", creature.getName()), creature, 50);
            } else {
                Server.getInstance().broadCastAction(String.format("%s is beginning to see red!", creature.getName()), creature, 50);
            }
            lusts.put(wurmid, currentLust + 0.01f);
        } else {
            lusts.put(wurmid, 0.01f);
        }
        lastLusted.put(wurmid, System.currentTimeMillis());
        return 1f + lusts.get(wurmid);
    }


    public static float getLustMult(Creature creature) {
        long wurmid = creature.getWurmId();
        if (lusts.containsKey(wurmid)) {
            return 1f + lusts.get(wurmid);
        }
        return 1f;
    }

    public static void pollLusts() {
        for (Long wurmid : lastLusted.keySet()) {
            if (System.currentTimeMillis() >= lastLusted.get(wurmid) + TimeConstants.MINUTE_MILLIS * 10) {
                RequiemLogging.logInfo(String.format("Bloodlust for %d expired. Removing from lists.", wurmid));
                Creature creature = Creatures.getInstance().getCreatureOrNull(wurmid);
                if (creature != null && !creature.isDead()) {
                    Server.getInstance().broadCastAction(String.format("%s calms down and is no longer enraged.", creature.getName()), creature, 50);
                }
                lastLusted.remove(wurmid);
                lusts.remove(wurmid);
            }
        }
    }

    public static void preInit() {
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();
            final Class<Bloodlust> thisClass = Bloodlust.class;
            String replace;

            /* Disabled in Wurm Unlimited 1.9 - May need to be revisited in the future.

            Util.setReason("Hook for bloodlust system.");
            CtClass ctCreature = classPool.get("com.wurmonline.server.creatures.Creature");
            CtClass ctString = classPool.get("java.lang.String");
            CtClass ctBattle = classPool.get("com.wurmonline.server.combat.Battle");
            CtClass ctCombatEngine = classPool.get("com.wurmonline.server.combat.CombatEngine");
            // @Nullable Creature performer, Creature defender, byte type, int pos, double damage, float armourMod,
            // String attString, @Nullable Battle battle, float infection, float poison, boolean archery, boolean alreadyCalculatedResist
            CtClass[] params1 = {
                    ctCreature,
                    ctCreature,
                    CtClass.byteType,
                    CtClass.intType,
                    CtClass.doubleType,
                    CtClass.floatType,
                    ctString,
                    ctBattle,
                    CtClass.floatType,
                    CtClass.floatType,
                    CtClass.booleanType,
                    CtClass.booleanType
            };
            String desc1 = Descriptor.ofMethod(CtClass.booleanType, params1);
            replace = "if($2.isDominated() && $1 != null && ($1.isUnique() || "+RareSpawns.class.getName()+".isRareCreature($1))){" +
                    //"  RequiemLogging.logInfo(\"Detected unique hit on a pet. Adding damage.\");" +
                    "  "+Bloodlust.class.getName()+".lustUnique($1);" +
                    "}" +
                    "if($1 != null && ($1.isUnique() || "+RareSpawns.class.getName()+".isRareCreature($1))){" +
                    "  float lustMult = "+Bloodlust.class.getName()+".getLustMult($1);" +
                    "  $5 = $5 * lustMult;" +
                    "}";
            Util.insertBeforeDescribed(thisClass, ctCombatEngine, "addWound", desc1, replace);*/
        } catch (IllegalArgumentException | ClassCastException e) {
            throw new HookException(e);
        }
    }
}
