package org.jubaroo.mods.wurm.server.creatures;

import com.wurmonline.server.creatures.*;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.server.skills.Skills;
import com.wurmonline.server.skills.SkillsFactory;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.util.Arrays;

public class CreatureTemplateModifier {

    public static void setCorpseModel(int templateId, String corpseModel) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "corpsename"), corpseModel);
                RequiemLogging.logInfo(String.format("Creature %s had its corpse model changed to %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), corpseModel));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setUniqueTypes(int templateId) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "isNotRebirthable"), true);
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "regenerating"), false);
                RequiemLogging.logInfo(String.format("Creature %s is now a Unique creature.", templateId));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setGhost(int templateId) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "ghost"), true);
                RequiemLogging.logInfo(String.format("Creature %s is now a ghost.", templateId));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setNoCorpse(int templateId) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "noCorpse"), true);
                RequiemLogging.logInfo(String.format("Creature %s now has no corpse when it dies.", templateId));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setGrazer(int templateId) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "grazer"), true);
                RequiemLogging.logInfo(String.format("Creature %s will now graze on grass to feed itself.", templateId));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setMilkable(int templateId) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "milkable"), true);
                RequiemLogging.logInfo(String.format("Creature %s will now be able to be milked.", templateId));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setDomestic(int templateId) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "domestic"), true);
                RequiemLogging.logInfo(String.format("Creature %s will now be a domestic animal able to be lead without taming.", templateId));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setVision(int templateId, short vision) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "vision"), vision);
                RequiemLogging.logInfo(String.format("Creature %s had its vision changed. It is now %d.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), vision));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setLongDesc(int templateId, String description) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "longDesc"), description);
                RequiemLogging.logInfo(String.format("Creature %s had its description changed. It is now \"\"%s\"\"", templateId, description));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setDeathSoundMale(int templateId, String deathSoundMale) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "deathSoundMale"), deathSoundMale);
                RequiemLogging.logInfo(String.format("Creature %s had its male death sound changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), deathSoundMale));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setHitSoundMale(int templateId, String hitSoundMale) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "hitSoundMale"), hitSoundMale);
                RequiemLogging.logInfo(String.format("Creature %s had its male hit sound changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), hitSoundMale));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setDeathSoundFemale(int templateId, String deathSoundFemale) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "deathSoundFemale"), deathSoundFemale);
                RequiemLogging.logInfo(String.format("Creature %s had its female death sound changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), deathSoundFemale));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setHitSoundFemale(int templateId, String hitSoundFemale) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "hitSoundFemale"), hitSoundFemale);
                RequiemLogging.logInfo(String.format("Creature %s had its female hit sound changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), hitSoundFemale));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setMeatMaterial(int templateId, byte meatMaterial) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "meatMaterial"), meatMaterial);
                RequiemLogging.logInfo(String.format("Creature %s had its meat material changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), meatMaterial));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setDenName(int templateId, String denName) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "denName"), denName);
                RequiemLogging.logInfo(String.format("Creature %s had its den name changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), denName));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setDenMaterial(int templateId, byte denMaterial) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "denMaterial"), denMaterial);
                RequiemLogging.logInfo(String.format("Creature %s had its den material changed. It is now %d.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), denMaterial));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setHandDamString(int templateId, String handDamageString) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "handDamString"), handDamageString);
                RequiemLogging.logInfo(String.format("Creature %s had its hand damage string changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), handDamageString));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setBiteDamString(int templateId, String biteDamageString) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "biteDamString"), biteDamageString);
                RequiemLogging.logInfo(String.format("Creature %s had its bite damage string changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), biteDamageString));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setKickDamString(int templateId, String kickDamageString) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "kickDamString"), kickDamageString);
                RequiemLogging.logInfo(String.format("Creature %s had its kick damage string changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), kickDamageString));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setHeadbuttDamString(int templateId, String headbuttDamageString) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "headbuttDamString"), headbuttDamageString);
                RequiemLogging.logInfo(String.format("Creature %s had its headbutt damage string changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), headbuttDamageString));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setBreathDamString(int templateId, String breathDamageString) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "kickDamString"), breathDamageString);
                RequiemLogging.logInfo(String.format("Creature %s had its breath damage string changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), breathDamageString));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setAggression(int templateId, int aggressivity) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "aggressivity"), aggressivity);
                RequiemLogging.logInfo(String.format("Creature %s had its aggression changed. It is now %d.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), aggressivity));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setAlignment(int templateId, float alignment) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "alignment"), alignment);
                RequiemLogging.logInfo(String.format("Creature %s had its alignment changed. It is now %f.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), alignment));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setNaturalArmour(int templateId, float naturalArmour) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "naturalArmour"), naturalArmour);
                RequiemLogging.logInfo(String.format("Creature %s had its natural armor changed. It is now %f.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), naturalArmour));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setArmourType(int templateId, byte armourType) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "armourType"), armourType);
                RequiemLogging.logInfo(String.format("Creature %s had its armor type changed. It is now %d.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), armourType));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setEggLayer(int templateId, boolean eggLayer) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "eggLayer"), eggLayer);
                RequiemLogging.logInfo(String.format("Creature %s can lay eggs? %b.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), eggLayer));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setEggTemplateId(int templateId, int eggTemplateId) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "eggTemplateId"), eggTemplateId);
                RequiemLogging.logInfo(String.format("Creature %s had its egg template Id changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), CreatureTemplateFactory.getInstance().getTemplate(eggTemplateId).getName()));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setChildTemplateId(int templateId, int childTemplateId) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "childTemplateId"), childTemplateId);
                RequiemLogging.logInfo(String.format("Creature %s had its child template Id changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), CreatureTemplateFactory.getInstance().getTemplate(childTemplateId).getName()));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setCombatMoves(int templateId, int[] combatMoves) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "combatMoves"), combatMoves);
                RequiemLogging.logInfo(String.format("Creature %s had its combat moves changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), Arrays.toString(combatMoves)));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setSubterranean(int templateId, boolean subterranean) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "subterranean"), subterranean);
                RequiemLogging.logInfo(String.format("Creature %s is now considered a subterranean creature? %b.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), subterranean));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setIsNoSkillgain(int templateId, boolean isNoSkillgain) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "isNoSkillgain"), isNoSkillgain);
                RequiemLogging.logInfo(String.format("Creature %s will not gix skill when fought and killed? %b", templateId, isNoSkillgain));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setSkill(int templateId, int skillId, float value) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                Skills skills = ReflectionUtil.getPrivateField(template, ReflectionUtil.getField(template.getClass(), "skills"));
                skills.learnTemp(skillId, value);
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "skills"), skills);
                RequiemLogging.logInfo(String.format("Creature %s had its %d changed to %f", templateId, skillId, value));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setMateTemplateId(int templateId, int mateTemplateId) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "mateTemplateId"), mateTemplateId);
                RequiemLogging.logInfo(String.format("Creature %s had its mate template Id changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), CreatureTemplateFactory.getInstance().getTemplate(mateTemplateId).getName()));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setHandDamage(int templateId, float handDamage) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "handDamage"), handDamage);
                RequiemLogging.logInfo(String.format("Creature %s had its hand damage changed. It is now %f.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), handDamage));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setBiteDamage(int templateId, float biteDamage) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "biteDamage"), biteDamage);
                RequiemLogging.logInfo(String.format("Creature %s had its bite damage changed. It is now %f.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), biteDamage));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setKickDamage(int templateId, float kickDamage) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "kickDamage"), kickDamage);
                RequiemLogging.logInfo(String.format("Creature %s had its kick damage changed. It is now %f.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), kickDamage));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setHeadButtDamage(int templateId, float headButtDamage) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "headButtDamage"), headButtDamage);
                RequiemLogging.logInfo(String.format("Creature %s had its headbutt damage changed. It is now %f.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), headButtDamage));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setBreathDamage(int templateId, float breathDamage) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "breathDamString"), breathDamage);
                RequiemLogging.logInfo(String.format("Creature %s had its breath damage changed. It is now %f.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), breathDamage));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setSpeed(int templateId, float speed) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "speed"), speed);
                RequiemLogging.logInfo(String.format("Creature %s had its speed changed. It is now %f.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), speed));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setMoveRate(int templateId, int moveRate) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "moveRate"), moveRate);
                RequiemLogging.logInfo(String.format("Creature %s had its move rate changed. It is now %d.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), moveRate));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setButcheredItems(int templateId, int[] butcheredItems) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "butcheredItems"), butcheredItems);
                RequiemLogging.logInfo(String.format("Creature %s had its butchered items changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), Arrays.toString(butcheredItems)));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setMaxHuntDistance(int templateId, int maxHuntDistance) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "maxHuntDistance"), maxHuntDistance);
                RequiemLogging.logInfo(String.format("Creature %s had its max hunt distance changed. It is now %d.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), maxHuntDistance));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setLeaderTemplateId(int templateId, int leaderTemplateId) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "leaderTemplateId"), leaderTemplateId);
                RequiemLogging.logInfo(String.format("Creature %s had its leader template Id changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), CreatureTemplateFactory.getInstance().getTemplate(leaderTemplateId).getName()));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setAdultFemaleTemplateId(int templateId, int adultFemaleTemplateId) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "adultFemaleTemplateId"), adultFemaleTemplateId);
                RequiemLogging.logInfo(String.format("Creature %s had its adult female template Id changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), CreatureTemplateFactory.getInstance().getTemplate(adultFemaleTemplateId).getName()));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setAdultMaleTemplateId(int templateId, int adultMaleTemplateId) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "adultMaleTemplateId"), adultMaleTemplateId);
                RequiemLogging.logInfo(String.format("Creature %s had its adult male template Id changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), CreatureTemplateFactory.getInstance().getTemplate(adultMaleTemplateId).getName()));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setKeepSex(int templateId, boolean keepSex) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "keepSex"), keepSex);
                RequiemLogging.logInfo(String.format("Creature %s will keep its sex? %b.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), keepSex));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setMaxGroupAttackSize(int templateId, int maxGroupAttackSize) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "maxGroupAttackSize"), maxGroupAttackSize);
                RequiemLogging.logInfo(String.format("Creature %s had its max group attack size changed. It is now %d.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), maxGroupAttackSize));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setMaxAge(int templateId, int maxAge) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "maxAge"), maxAge);
                RequiemLogging.logInfo(String.format("Creature %s had its max age changed. It is now %d.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), maxAge));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setBaseCombatRating(int templateId, float baseCombatRating) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "baseCombatRating"), baseCombatRating);
                RequiemLogging.logInfo(String.format("Creature %s had its base combat rating changed. It is now %f.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), baseCombatRating));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setBonusCombatRating(int templateId, float bonusCombatRating) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "bonusCombatRating"), bonusCombatRating);
                RequiemLogging.logInfo(String.format("Creature %s had its bonus combat rating changed. It is now %f.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), bonusCombatRating));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setCombatDamageType(int templateId, byte combatDamageType) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "combatDamageType"), combatDamageType);
                RequiemLogging.logInfo(String.format("Creature %s had its bonus combat damage type changed. It is now %d.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), combatDamageType));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setColor(int templateId, Integer colorRed, Integer colorGreen, Integer colorBlue) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "colorRed"), colorRed);
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "colorGreen"), colorGreen);
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "colorBlue"), colorBlue);
                RequiemLogging.logInfo(String.format("Creature %s had its color changed. It is now %dR %dG %dB.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), colorRed, colorGreen, colorBlue));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setGlowing(int templateId, boolean glowing) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "glowing"), glowing);
                RequiemLogging.logInfo(String.format("Creature %s will glow? %b.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), glowing));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setSize(int templateId, Integer sizeModX, Integer sizeModY, Integer sizeModZ) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "sizeModX"), sizeModX);
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "sizeModY"), sizeModY);
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "sizeModZ"), sizeModZ);
                RequiemLogging.logInfo(String.format("Creature %s had its size changed. It's size is now X: %d Y: %d Z: %d.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), sizeModX, sizeModY, sizeModZ));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setIsOnFire(int templateId, boolean isOnFire) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "isOnFire"), isOnFire);
                RequiemLogging.logInfo(String.format("Creature %s will on fire? %b.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), isOnFire));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setFireRadius(int templateId, byte fireRadius) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "fireRadius"), fireRadius);
                RequiemLogging.logInfo(String.format("Creature %s had its fire radius changed. It is now %d.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), fireRadius));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setColdResistance(int templateId, float coldResistance) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "coldResistance"), coldResistance);
                RequiemLogging.logInfo(String.format("Creature %s had its cold resistance changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), coldResistance));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setDiseaseResistance(int templateId, float diseaseResistance) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "diseaseResistance"), diseaseResistance);
                RequiemLogging.logInfo(String.format("Creature %s had its disease resistance changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), diseaseResistance));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setPhysicalResistance(int templateId, float physicalResistance) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "physicalResistance"), physicalResistance);
                RequiemLogging.logInfo(String.format("Creature %s had its physical resistance changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), physicalResistance));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setPierceResistance(int templateId, float pierceResistance) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "pierceResistance"), pierceResistance);
                RequiemLogging.logInfo(String.format("Creature %s had its pierce resistance changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), pierceResistance));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setSlashResistance(int templateId, float slashResistance) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "slashResistance"), slashResistance);
                RequiemLogging.logInfo(String.format("Creature %s had its slash resistance changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), slashResistance));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setCrushResistance(int templateId, float crushResistance) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "crushResistance"), crushResistance);
                RequiemLogging.logInfo(String.format("Creature %s had its crush resistance changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), crushResistance));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setBiteResistance(int templateId, float biteResistance) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "biteResistance"), biteResistance);
                RequiemLogging.logInfo(String.format("Creature %s had its bite resistance changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), biteResistance));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setPoisonResistance(int templateId, float poisonResistance) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "poisonResistance"), poisonResistance);
                RequiemLogging.logInfo(String.format("Creature %s had its poison resistance changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), poisonResistance));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setWaterResistance(int templateId, float waterResistance) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "waterResistance"), waterResistance);
                RequiemLogging.logInfo(String.format("Creature %s had its water resistance changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), waterResistance));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setAcidResistance(int templateId, float acidResistance) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "acidResistance"), acidResistance);
                RequiemLogging.logInfo(String.format("Creature %s had its acid resistance changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), acidResistance));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setInternalResistance(int templateId, float internalResistance) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "internalResistance"), internalResistance);
                RequiemLogging.logInfo(String.format("Creature %s had its internal resistance changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), internalResistance));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setFireVulnerability(int templateId, float fireVulnerability) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "fireVulnerability"), fireVulnerability);
                RequiemLogging.logInfo(String.format("Creature %s had its fire vulnerability changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), fireVulnerability));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setColdVulnerability(int templateId, float coldVulnerability) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "coldVulnerability"), coldVulnerability);
                RequiemLogging.logInfo(String.format("Creature %s had its cold vulnerability changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), coldVulnerability));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setDiseaseVulnerability(int templateId, float diseaseVulnerability) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "diseaseVulnerability"), diseaseVulnerability);
                RequiemLogging.logInfo(String.format("Creature %s had its disease vulnerability changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), diseaseVulnerability));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setPhysicalVulnerability(int templateId, float physicalVulnerability) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "physicalVulnerability"), physicalVulnerability);
                RequiemLogging.logInfo(String.format("Creature %s had its physical vulnerability changed. It is now %s.", CreatureTemplateFactory.getInstance().getTemplate(templateId).getName(), physicalVulnerability));
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setPierceVulnerability(int templateId, float pierceVulnerability) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "pierceVulnerability"), pierceVulnerability);
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setSlashVulnerability(int templateId, float slashVulnerability) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "slashVulnerability"), slashVulnerability);
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setBiteVulnerability(int templateId, float biteVulnerability) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "biteVulnerability"), biteVulnerability);
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setPoisonVulnerability(int templateId, float poisonVulnerability) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "poisonVulnerability"), poisonVulnerability);
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setWaterVulnerability(int templateId, float waterVulnerability) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "waterVulnerability"), waterVulnerability);
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setAcidVulnerability(int templateId, float acidVulnerability) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "acidVulnerability"), acidVulnerability);
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setInternalVulnerability(int templateId, float internalVulnerability) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "internalVulnerability"), internalVulnerability);
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setMaxPercentOfCreatures(int templateId, float maxPercentOfCreatures) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "maxPercentOfCreatures"), maxPercentOfCreatures);
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void setMaxPopulationOfCreatures(int templateId, int maxPopulationOfCreatures) {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "maxPopulationOfCreatures"), maxPopulationOfCreatures);
            }
        } catch (NoSuchCreatureTemplateException | IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    static void setWorgFields() {
        try {
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(CreatureTemplateIds.WORG_CID);
            if (template != null) {
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "isVehicle"), true);
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "dominatable"), true);
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "isHorse"), true);
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "isDetectInvis"), false);
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "monster"), true);
                Skills skills = SkillsFactory.createSkills("Worg");
                skills.learnTemp(SkillList.BODY_STRENGTH, 40f);
                skills.learnTemp(SkillList.BODY_CONTROL, 25f);
                skills.learnTemp(SkillList.BODY_STAMINA, 35f);
                skills.learnTemp(SkillList.MIND_LOGICAL, 10f);
                skills.learnTemp(SkillList.MIND_SPEED, 15f);
                skills.learnTemp(SkillList.SOUL_STRENGTH, 20f);
                skills.learnTemp(SkillList.SOUL_DEPTH, 12f);
                skills.learnTemp(SkillList.WEAPONLESS_FIGHTING, 50f);
                ReflectionUtil.setPrivateField(template, ReflectionUtil.getField(template.getClass(), "skills"), skills);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setNonSentinel(int templateId) throws NoSuchFieldException, NoSuchCreatureTemplateException, IllegalAccessException {
        ReflectionUtil.setPrivateField(CreatureTemplateFactory.getInstance().getTemplate(templateId), ReflectionUtil.getField(CreatureTemplate.class, "sentinel"), false);
    }

    public static void setNonSentinel(Creature creature) throws Exception {
        ReflectionUtil.setPrivateField(CreatureTemplateFactory.getInstance().getTemplate(creature.getNameWithoutPrefixes()), ReflectionUtil.getField(CreatureTemplate.class, "sentinel"), false);
    }

}
