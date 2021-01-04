package org.jubaroo.mods.wurm.server.misc;

import com.wurmonline.server.Server;
import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.items.CreationEntry;
import com.wurmonline.server.items.CreationMatrix;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.skills.*;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import mod.sin.lib.Util;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.jubaroo.mods.wurm.server.RequiemLogging;

public class SkillChanges {

    public static double newDoSkillGainNew(Skill skill, double check, double power, double learnMod, float times, double skillDivider) {
        double bonus = 1.0;
        double diff = Math.abs(check - skill.getKnowledge());
        short sType = SkillSystem.getTypeFor(skill.getNumber());
        boolean awardBonus = true;
        if (sType == 1 || sType == 0) {
            awardBonus = false;
        }
        if (diff <= 15.0 && awardBonus) {
            bonus = 1.0 + (0.1 * (diff / 15.0));
        }
        /*if (power < 0.0) {
            if (this.knowledge < 20.0) {
                this.alterSkill((100.0 - this.knowledge) / (this.getDifficulty(this.parent.priest) * this.knowledge * this.knowledge) * learnMod * bonus, false, times, true, skillDivider);
            }
        } else {
            this.alterSkill((100.0 - this.knowledge) / (this.getDifficulty(this.parent.priest) * this.knowledge * this.knowledge) * learnMod * bonus, false, times, true, skillDivider);
        }*/
        try {
            Skills parent = ReflectionUtil.getPrivateField(skill, ReflectionUtil.getField(skill.getClass(), "parent"));
            double advanceMultiplicator = (100.0 - skill.getKnowledge()) / (skill.getDifficulty(parent.priest) * skill.getKnowledge() * skill.getKnowledge()) * learnMod * bonus;
            double p = 5;
            double q = 3;
            //advanceMultiplicator *= Math.pow(2, ((2-Math.pow((100/(100+power)), p))*(100-power)/100));
            double mult = Math.pow(2, (2 - Math.pow(100 / (100 + power), p)) * Math.pow((100 - power) * 0.01, q));
            if (mult < 0.5 && skill.getKnowledge() < 20) {
                advanceMultiplicator *= 0.5 + (Server.rand.nextDouble() * 0.5);
            } else if (skill.getNumber() == SkillList.MEDITATING || skill.getNumber() == SkillList.LOCKPICKING) {
                advanceMultiplicator *= Math.max(mult, 0.8d);
            } else if (mult > 0.0001) {
                advanceMultiplicator *= mult * 0.95d;
            } else {
                advanceMultiplicator = 0;
            }
            /*if(skill.getType() > 3) {
                Requiem.debug(String.format("Power of %.2f on skill %s gives multiplier of %.2f.", power, skill.getName(), mult));
            }*/
            return advanceMultiplicator;
            //ReflectionUtil.callPrivateMethod(skill, ReflectionUtil.getMethod(skill.getClass(), "alterSkill"), advanceMultiplicator, false, times, true, skillDivider);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            RequiemLogging.debug(e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public static void onServerStarted() {
        /*
        SkillTemplate exorcism = SkillSystem.templates.get(SkillList.EXORCISM);
        int[] deps = {SkillList.GROUP_ALCHEMY};
        try {
            String newName = "Crystal handling";
            ReflectionUtil.setPrivateField(exorcism, ReflectionUtil.getField(exorcism.getClass(), "name"), newName);
            SkillSystem.skillNames.put(exorcism.getNumber(), newName);
            SkillSystem.namesToSkill.put(newName, exorcism.getNumber());
            ReflectionUtil.setPrivateField(exorcism, ReflectionUtil.getField(exorcism.getClass(), "dependencies"), deps);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Requiem.debug("Failed to rename exorcism!");
            e.printStackTrace();
        }
        SkillTemplate ballistae = SkillSystem.templates.get(SkillList.BALLISTA);
        int[] deps2 = {SkillList.GROUP_ALCHEMY};
        try {
            String newName = "Mystic components";
            ReflectionUtil.setPrivateField(ballistae, ReflectionUtil.getField(ballistae.getClass(), "name"), newName);
            SkillSystem.skillNames.put(ballistae.getNumber(), newName);
            SkillSystem.namesToSkill.put(newName, ballistae.getNumber());
            ReflectionUtil.setPrivateField(ballistae, ReflectionUtil.getField(ballistae.getClass(), "dependencies"), deps2);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Requiem.debug("Failed to rename ballistae!");
            e.printStackTrace();
        }
        SkillTemplate preaching = SkillSystem.templates.get(SkillList.PREACHING);
        int[] deps3 = {SkillList.MASONRY};
        try {
            String newName = "Gem augmentation";
            ReflectionUtil.setPrivateField(preaching, ReflectionUtil.getField(preaching.getClass(), "name"), newName);
            SkillSystem.skillNames.put(preaching.getNumber(), newName);
            SkillSystem.namesToSkill.put(newName, preaching.getNumber());
            ReflectionUtil.setPrivateField(preaching, ReflectionUtil.getField(preaching.getClass(), "dependencies"), deps3);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Requiem.debug("Failed to rename preaching!");
            e.printStackTrace();
        }
        SkillTemplate stealing = SkillSystem.templates.get(SkillList.STEALING);
        try {
            ReflectionUtil.setPrivateField(stealing, ReflectionUtil.getField(stealing.getClass(), "tickTime"), 0);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Requiem.debug("Failed to set tickTime for stealing!");
            e.printStackTrace();
        }
        */
        CreationEntry lockpicks = CreationMatrix.getInstance().getCreationEntry(ItemList.lockpick);
        try {
            ReflectionUtil.setPrivateField(lockpicks, ReflectionUtil.getField(lockpicks.getClass(), "hasMinimumSkillRequirement"), false);
            ReflectionUtil.setPrivateField(lockpicks, ReflectionUtil.getField(lockpicks.getClass(), "minimumSkill"), 0.0);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            RequiemLogging.debug("Failed to set lockpick creation entry changes!");
            e.printStackTrace();
        }

        SkillTemplate meditating = SkillSystem.templates.get(SkillList.MEDITATING);
        try {
            ReflectionUtil.setPrivateField(meditating, ReflectionUtil.getField(meditating.getClass(), "tickTime"), TimeConstants.HOUR_MILLIS);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            RequiemLogging.debug("Failed to set tickTime for meditating!");
            e.printStackTrace();
        }
        meditating.setDifficulty(300f);

        // Set mining difficulty down to be equivalent to digging.
        SkillTemplate mining = SkillSystem.templates.get(SkillList.MINING);
        mining.setDifficulty(3000f);
        // Triple lockpicking skill
        SkillTemplate lockpicking = SkillSystem.templates.get(SkillList.LOCKPICKING);
        lockpicking.setDifficulty(700f);
    }

    public static void preInit() {
        try {
            final ClassPool classPool = HookManager.getInstance().getClassPool();
            final Class<SkillChanges> thisClass = SkillChanges.class;
            final String replace;
            Util.setReason("Allow skill check failures to add skill gain.");
            CtClass ctSkill = classPool.get("com.wurmonline.server.skills.Skill");
            replace = String.format("{  double advanceMultiplicator = %s.newDoSkillGainNew($0, $1, $2, $3, $4, $5);  $0.alterSkill(advanceMultiplicator, false, $4, true, $5);}", SkillChanges.class.getName());
            Util.setBodyDeclared(thisClass, ctSkill, "doSkillGainNew", replace);
        } catch (NotFoundException | IllegalArgumentException | ClassCastException e) {
            throw new HookException(e);
        }
    }

}
