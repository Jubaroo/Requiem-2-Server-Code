package org.jubaroo.mods.wurm.server.creatures;

import com.wurmonline.server.Server;
import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.creatures.Creature;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.Descriptor;
import mod.sin.lib.Util;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.util.HashMap;
import java.util.Map;

public class Bounty {
    // Map all damage dealt instances in a map. This is extremely useful in some cases.
    public static HashMap<Long, Map<Long, Double>> dealtDamage = new HashMap<>();
    //protected static WyvernMods mod;
    public static HashMap<String, Integer> reward = new HashMap<>();

    public static void addDealtDamage(long defender, long attacker, double damage) {
        if (dealtDamage.containsKey(defender)) {
            Map<Long, Double> dealers = dealtDamage.get(defender);
            if (!dealers.containsKey(attacker)) {
                dealers.put(attacker, damage);
            } else {
                double newDam = dealers.get(attacker);
                newDam += damage;
                dealers.put(attacker, newDam);
            }
        } else {
            Map<Long, Double> dealers = new HashMap<>();
            dealers.put(attacker, damage);
            dealtDamage.put(defender, dealers);
        }
    }

    public static long lastAttacked(Map<Long, Long> attackers, long playerId) {
        return System.currentTimeMillis() - attackers.get(playerId);
    }

    public static boolean isCombatant(Map<Long, Long> attackers, long playerId) {
        long now = System.currentTimeMillis();
        long delta = now - attackers.get(playerId);
        return delta < TimeConstants.MINUTE_MILLIS * 2;
    	/*if(delta > 120000){
    		return false;
    	}
    	return true;*/
    }

    public static Map<Long, Long> getAttackers(Creature mob) {
        try {
            return ReflectionUtil.getPrivateField(mob, ReflectionUtil.getField(mob.getClass(), "attackers"));
        } catch (IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double getCreatureStrength(Creature mob) {
        float combatRating = mob.getBaseCombatRating() + mob.getBonusCombatRating();
        float maxDmg = Math.max(mob.getTemplate().getBreathDamage(), mob.getHandDamage());
        maxDmg = Math.max(maxDmg, mob.getBiteDamage());
        maxDmg = Math.max(maxDmg, mob.getKickDamage());
        maxDmg = Math.max(maxDmg, mob.getHeadButtDamage());
        double fighting = mob.getFightingSkill().getKnowledge();
        double weaponlessFighting = mob.getWeaponLessFightingSkill().getKnowledge();
        double fs = Math.max(fighting, weaponlessFighting);
        double bodyStr = mob.getBodyStrength().getKnowledge();
        fs /= mob.getArmourMod();
        double cretStr = 100D + (combatRating * Math.cbrt(maxDmg) * Math.cbrt(fs) * Math.cbrt(bodyStr));
        cretStr *= 0.8d;
        double k = 100000d;
        cretStr = (cretStr * Math.pow(2, (-(cretStr / k))) + k * (1 - Math.pow(2, -cretStr / k))) / (1 + Math.pow(2, -cretStr / k));
        if (mob.isAggHuman() && cretStr < 100D) {
            cretStr *= 1 + (Server.rand.nextFloat() * 0.2f);
            cretStr = Math.max(cretStr, 100D);
        } else if (!mob.isAggHuman() && cretStr < 300D) {
            cretStr *= 0.4f;
            cretStr *= 1 + (Server.rand.nextFloat() * 0.2f);
            cretStr = Math.max(cretStr, 10D);
        }
        //Requiem.logInfo("capped: "+cretStr);
        return cretStr;
    }

    public static void init() throws CannotCompileException {
        RequiemLogging.logInfo("========= Initializing Bounty.init =========");
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();
            final Class<Bounty> thisClass = Bounty.class;

            CtClass ctCreature = classPool.get("com.wurmonline.server.creatures.Creature");
            String replace;

            /*
            // Give coin bounty based on creature strength
            replace = String.format("%s.checkPlayerBounty(player, this);$_ = $proceed($$);", PlayerBounty.class.getName());
            Util.instrumentDeclared(thisClass, ctCreature, "modifyFightSkill", "checkCoinAward", replace);

            // Die method description
            CtClass ctString = classPool.get("java.lang.String");
            CtClass[] params1 = new CtClass[]{
                    CtClass.booleanType,
                    ctString,
                    CtClass.booleanType
            };
            String desc1 = Descriptor.ofMethod(CtClass.voidType, params1);

            replace = String.format("$_ = $proceed($$);%s.checkLootTable(this, corpse);", CreatureLootOnDeath.class.getName());
            Util.instrumentDescribed(thisClass, ctCreature, "die", desc1, "setRotation", replace);
            */
            CtClass[] params = {
                    CtClass.intType,
                    CtClass.booleanType,
                    CtClass.floatType,
                    CtClass.floatType,
                    CtClass.floatType,
                    CtClass.intType,
                    classPool.get("java.lang.String"),
                    CtClass.byteType,
                    CtClass.byteType,
                    CtClass.byteType,
                    CtClass.booleanType,
                    CtClass.byteType,
                    CtClass.intType
            };

            String desc = Descriptor.ofMethod(ctCreature, params);

            // Debugging to show all new creatures created.
            //if (Constants.creatureCreateLogging) {
            //    CtMethod ctDoNew = ctCreature.getMethod("doNew", "(IZFFFILjava/lang/String;BBBZB)Lcom/wurmonline/server/creatures/Creature;");
            //    ctDoNew.insertBefore(String.format("%s.logInfo(\"Creating new creature: \"+templateid+\" - \"+(aPosX/4)+\", \"+(aPosY/4)+\" [\"+com.wurmonline.server.creatures.CreatureTemplateFactory.getInstance().getTemplate(templateid).getName()+\"]\");", RequiemLogging.class.getName()));
            //}
            // Modify new creatures
            replace = String.format("$_ = $proceed($$);%s.modifyNewCreature($1);", CreatureSpawns.class.getName());
            Util.instrumentDescribed(thisClass, ctCreature, "doNew", desc, "sendToWorld", replace);

            // -- Enable adjusting size for creatures -- //
            CtClass ctCreatureStatus = classPool.get("com.wurmonline.server.creatures.CreatureStatus");
            Util.setBodyDeclared(thisClass, ctCreatureStatus, "getSizeMod", String.format("{return %s.getAdjustedSizeMod(this);}", CreatureSize.class.getName()));

        } catch (NotFoundException e) {
            throw new HookException(e);
        }
    }
}
