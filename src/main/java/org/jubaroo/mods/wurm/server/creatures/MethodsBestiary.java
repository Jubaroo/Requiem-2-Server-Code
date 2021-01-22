package org.jubaroo.mods.wurm.server.creatures;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Server;
import com.wurmonline.server.creatures.*;
import javassist.*;
import javassist.bytecode.Descriptor;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import mod.sin.lib.Util;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.classhooks.InvocationHandlerFactory;
import org.jubaroo.mods.wurm.server.ModConfig;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.creatures.traitedCreatures.Zebra;
import org.jubaroo.mods.wurm.server.tools.CreatureTools;
import org.jubaroo.mods.wurm.server.tools.Hooks;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MethodsBestiary {

    public static void preInit() {
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();
            final Class<MethodsBestiary> thisClass = MethodsBestiary.class;
            String replace;

            Util.setReason("Disable sacrificing strong creatures.");
            CtClass ctCreature = classPool.get("com.wurmonline.server.creatures.Creature");
            CtClass ctItem = classPool.get("com.wurmonline.server.items.Item");
            CtClass ctAction = classPool.get("com.wurmonline.server.behaviours.Action");
            CtClass ctMethodsReligion = classPool.get("com.wurmonline.server.behaviours.MethodsReligion");
            CtClass[] params1 = {
                    ctCreature,
                    ctCreature,
                    ctItem,
                    ctAction,
                    CtClass.floatType
            };
            String desc1 = Descriptor.ofMethod(CtClass.booleanType, params1);
            replace = String.format("if(%s.isSacrificeImmune($2)){  performer.getCommunicator().sendNormalServerMessage(\"This creature cannot be sacrificed.\");  return true;}", Hooks.class.getName());
            Util.insertBeforeDescribed(thisClass, ctMethodsReligion, "sacrifice", desc1, replace);

            Util.setReason("Disable afk training.");
            CtClass ctCombatHandler = classPool.get("com.wurmonline.server.creatures.CombatHandler");
            //"if($1.isPlayer() && $1.getTarget() != $0){" +
            //"  RequiemLogging.logInfo(\"Non-targeted mob detected - \" + $1.getName());" +
            replace = String.format("if(%s.blockSkillFrom($1, $0)){  $_ = true;}else{  $_ = $proceed($$);}", Hooks.class.getName());
            Util.instrumentDeclared(thisClass, ctCombatHandler, "setDamage", "isNoSkillFor", replace);
            Util.instrumentDeclared(thisClass, ctCombatHandler, "checkDefenderParry", "isNoSkillFor", replace);
            Util.instrumentDeclared(thisClass, ctCombatHandler, "checkShield", "isNoSkillFor", replace);
            Util.instrumentDeclared(thisClass, ctCombatHandler, "setBonuses", "isNoSkillFor", replace);
            CtMethod[] ctGetDamages = ctCombatHandler.getDeclaredMethods("getDamage");
            for (CtMethod method : ctGetDamages) {
                method.instrument(new ExprEditor() {
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getMethodName().equals("isNoSkillFor")) {
                            //"if($1.isPlayer() && $1.getTarget() != $0){" +
                            //"  RequiemLogging.logInfo(\"Non-targeted mob detected - \" + $1.getName());" +
                            m.replace(String.format("if(%s.blockSkillFrom($1, $0)){  $_ = true;}else{  $_ = $proceed($$);}", Hooks.class.getName()));
                        }
                    }
                });
            }

            // Die method description
            CtClass ctString = classPool.get("java.lang.String");
            CtClass[] params5 = new CtClass[]{
                    CtClass.booleanType,
                    ctString,
                    CtClass.booleanType
            };
            String desc5 = Descriptor.ofMethod(CtClass.voidType, params5);

            Util.setReason("Deny chargers walking through walls.");
            CtClass ctPathFinder = classPool.get("com.wurmonline.server.creatures.ai.PathFinder");
            replace = String.format("if(%s.denyPathingOverride($0)){  $_ = false;}else{  $_ = $proceed($$);}", Hooks.class.getName());
            Util.instrumentDeclared(thisClass, ctPathFinder, "canPass", "isGhost", replace);
            Util.instrumentDeclared(thisClass, ctCreature, "setPathing", "isGhost", replace);
            Util.instrumentDeclared(thisClass, ctCreature, "startPathingToTile", "isGhost", replace);
            Util.instrumentDeclared(thisClass, ctCreature, "moveAlongPath", "isGhost", replace);
            Util.instrumentDeclared(thisClass, ctCreature, "takeSimpleStep", "isGhost", replace);
            Util.instrumentDescribed(thisClass, ctCreature, "die", desc5, "isGhost", replace);

            Util.setReason("Apply random types to creatures in the wilderness.");
            CtClass[] params2 = {
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
            String desc2 = Descriptor.ofMethod(ctCreature, params2);
            replace = String.format("$10 = %s.newCreatureType($1, $10);", Hooks.class.getName());
            Util.insertBeforeDescribed(thisClass, ctCreature, "doNew", desc2, replace);

            Util.setReason("Enable archery against ghost targets.");
            CtClass ctArchery = classPool.get("com.wurmonline.server.combat.Archery");
            CtMethod[] archeryAttacks = ctArchery.getDeclaredMethods("attack");
            for (CtMethod method : archeryAttacks) {
                method.instrument(new ExprEditor() {
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getMethodName().equals("isGhost")) {
                            m.replace("$_ = false;");
                            RequiemLogging.logInfo("Enabled archery against ghost targets in archery attack method.");
                        }
                    }
                });
            }

            Util.setReason("Disable archery altogether against certain creatures.");
            CtClass[] params3 = {
                    ctCreature,
                    ctCreature,
                    ctItem,
                    CtClass.floatType,
                    ctAction
            };
            String desc3 = Descriptor.ofMethod(CtClass.booleanType, params3);
            replace = String.format("if(%s.isArcheryImmune($1, $2)){  return true;}", Hooks.class.getName());
            Util.insertBeforeDescribed(thisClass, ctArchery, "attack", desc3, replace);

            Util.setReason("Auto-Genesis a creature born on enchanted grass");
            replace = String.format("%s.checkEnchantedBreed(newCreature);$_ = $proceed($$);", Hooks.class.getName());
            Util.instrumentDeclared(thisClass, ctCreature, "checkPregnancy", "saveCreatureName", replace);

            Util.setReason("Set custom corpse sizes.");
            replace = String.format("$_ = $proceed($$);if(%s.hasCustomCorpseSize(this)){  %s.setCorpseSizes(this, corpse);}", Hooks.class.getName(), CreatureTools.class.getName());
            Util.instrumentDescribed(thisClass, ctCreature, "die", desc5, "addItem", replace);

            Util.setReason("Add spell resistance to custom creatures.");
            replace = String.format("float cResist = %s.getCustomSpellResistance(this);if(cResist >= 0f){  return cResist;}", Hooks.class.getName());
            Util.insertBeforeDeclared(thisClass, ctCreature, "addSpellResistance", replace);

            Util.setReason("Allow custom creatures to have breeding names.");
            replace = String.format("$_ =%s.shouldBreedName(this);", Hooks.class.getName());
            Util.instrumentDeclared(thisClass, ctCreature, "checkPregnancy", "isHorse", replace);

            Util.setReason("Allow ghost creatures to breed (Chargers).");
            CtClass ctMethodsCreatures = classPool.get("com.wurmonline.server.behaviours.MethodsCreatures");
            replace = "$_ = false;";
            Util.instrumentDeclared(thisClass, ctMethodsCreatures, "breed", "isGhost", replace);

            Util.setReason("Allow ghost creatures to drop corpses.");
            replace = String.format("if(%s.isGhostCorpse(this)){  $_ = false;}else{  $_ = $proceed($$);}", Hooks.class.getName());
            Util.instrumentDescribed(thisClass, ctCreature, "die", desc5, "isGhost", replace);

            Util.setReason("Attach special effects to creatures.");
            CtClass ctVirtualZone = classPool.get("com.wurmonline.server.zones.VirtualZone");
            CtClass[] params4 = {
                    CtClass.longType,
                    CtClass.booleanType,
                    CtClass.longType,
                    CtClass.floatType,
                    CtClass.floatType,
                    CtClass.floatType
            };
            String desc4 = Descriptor.ofMethod(CtClass.booleanType, params4);
            replace = String.format("$_ = $proceed($$);%s.addCreatureSpecialEffect(copyId != -10 ? copyId : creatureId, $0, creature);", Hooks.class.getName());
            Util.instrumentDescribed(thisClass, ctVirtualZone, "addCreature", desc4, "sendNewCreature", replace);

            Util.setReason("Ensure unique creatures cannot be hitched to vehicles.");
            CtClass ctVehicle = classPool.get("com.wurmonline.server.behaviours.Vehicle");
            replace = String.format("if(%s.isNotHitchable($1)){  return false;}", Hooks.class.getName());
            Util.insertBeforeDeclared(thisClass, ctVehicle, "addDragger", replace);

        } catch (CannotCompileException | NotFoundException | IllegalArgumentException | ClassCastException e) {
            throw new HookException(e);
        }
    }

    public static boolean isRequiemUnique(Creature creature) {
        return isRequiemUnique(creature.getTemplate().getTemplateId());
    }

    public static boolean isRequiemUnique(int templateId) {
        if (templateId == CustomCreatures.kongId) {
            return true;
        } else if (templateId == CustomCreatures.whiteBuffaloSpiritId) {
            return true;
        } else if (templateId == CustomCreatures.blackKnightId) {
            return true;
        } else if (templateId == CustomCreatures.clucksterId) {
            return true;
        } else if (templateId == CustomCreatures.facebreykerId) {
            return true;
        } else return templateId == CustomCreatures.spiritStagId;
    }

    public static boolean isConditionExempt(int templateId) {
        if (templateId == CustomCreatures.golemlingId) {
            return true;
        } else if (templateId == CustomCreatures.bloblingId) {
            return true;
        } else if (templateId == CustomCreatures.prismaticBloblingId) {
            return true;
        } else if (templateId == CustomCreatures.snowmanId || templateId == CustomCreatures.sinisterSantaId || templateId == CustomCreatures.frostyId || templateId == CreatureTemplateIds.SANTA_CLAUS || templateId == CreatureTemplateIds.EASTERBUNNY_CID) {
            return true;
        } else if (templateId == CreatureTemplateIds.FOAL_CID || templateId == CreatureTemplateIds.LAMB_CID || templateId == CreatureTemplateIds.CALF_CID || templateId == CreatureTemplateIds.CHICKEN_CID || templateId == CreatureTemplateIds.HEN_CID || templateId == CreatureTemplateIds.ROOSTER_CID || templateId == CreatureTemplateIds.HORSE_CID || templateId == CreatureTemplateIds.COW_BROWN_CID || templateId == CreatureTemplateIds.RAM_CID || templateId == CreatureTemplateIds.SHEEP_CID || templateId == CreatureTemplateIds.BULL_CID || templateId == CreatureTemplateIds.PIG_CID) {
            return true;
        } else if (templateId == CreatureTemplateIds.NPC_HUMAN_CID || templateId == CreatureTemplateIds.HUMAN_CID || templateId == CreatureTemplateIds.BARTENDER_CID || templateId == CreatureTemplateIds.SALESMAN_CID || templateId == CreatureTemplateIds.GUIDE_CID || templateId == CustomCreatures.injuredPirateId) {
            return true;
        } else return isRequiemNPC(templateId);
    }

    public static boolean isUsuallyHitched(int templateId) {
        if (templateId == CustomCreatures.chargerId) {
            return true;
        } else if (templateId == CreatureTemplateIds.HORSE_CID || templateId == CreatureTemplateIds.HELL_HORSE_CID) {
            return true;
        } else if (templateId == CustomCreatures.blackWyvernId) {
            return true;
        } else if (templateId == CustomCreatures.blueWyvernId) {
            return true;
        } else if (templateId == CustomCreatures.greenWyvernId) {
            return true;
        } else if (templateId == CustomCreatures.redWyvernId) {
            return true;
        } else if (templateId == Zebra.templateId) {
            return true;
        } else if (templateId == CustomCreatures.rainbowZebraId) {
            return true;
        } else return templateId == CustomCreatures.whiteWyvernId;
    }

    public static boolean isRequiemGhost(Creature creature) {
        return isRequiemGhost(creature.getTemplate().getTemplateId());
    }

    public static boolean isRequiemGhost(int templateId) {
        if (templateId == CustomCreatures.whiteBuffaloSpiritId) {
            return true;
        } else return templateId == CustomCreatures.spiritStagId;
    }

    public static boolean isTreasureGoblin(Creature creature) {
        return isTreasureGoblin(creature.getTemplate().getTemplateId());
    }

    public static boolean isTreasureGoblin(int templateId) {
        if (templateId == CustomCreatures.treasureGoblinMenageristGoblinId) {
            return true;
        } else if (templateId == CustomCreatures.treasureGoblinId) {
            return true;
        } else if (templateId == CustomCreatures.treasureGoblinGemHoarderId) {
            return true;
        } else if (templateId == CustomCreatures.treasureGoblinMalevolentTormentorId) {
            return true;
        } else if (templateId == CustomCreatures.treasureGoblinOdiousCollectorId) {
            return true;
        } else if (templateId == CustomCreatures.treasureGoblinRainbowGoblinId) {
            return true;
        } else return templateId == CustomCreatures.treasureGoblinBloodThiefId;
    }

    public static boolean isRequiemNPC(Creature creature) {
        return isRequiemNPC(creature.getTemplate().getTemplateId());
    }

    public static boolean isRequiemNPC(int templateId) {
        if (templateId == CustomCreatures.npcGoblinTraderId) {
            return true;
        } else if (templateId == CustomCreatures.npcStarId) {
            return true;
        } else if (templateId == CustomCreatures.npcCyclopsId) {
            return true;
        } else if (templateId == CustomCreatures.npcDarkInnkeeperId) {
            return true;
        } else if (templateId == CustomCreatures.npcInnkeeperId) {
            return true;
        } else if (templateId == CustomCreatures.npcTravelingTraderId) {
            return true;
        } else if (templateId == CustomCreatures.npcWagonerId) {
            return true;
        } else if (templateId == CustomCreatures.npcZombieTraderId) {
            return true;
        } else if (templateId == CustomCreatures.npcGoblinId) {
            return true;
        } else if (templateId == CustomCreatures.npcTrollId) {
            return true;
        } else return templateId == CustomCreatures.npcWraithId;
    }

    public static boolean isChristmasMob(int templateId) {
        if (templateId == CustomCreatures.sinisterSantaId) {
            return true;
        } else if (templateId == CustomCreatures.frostyId) {
            return true;
        } else if (templateId == CustomCreatures.grinchId) {
            return true;
        } else if (templateId == CustomCreatures.rudolphId) {
            return true;
        } else return templateId == CustomCreatures.snowmanId;
    }

    public static boolean isChristmasMob(Creature creature) {
        return isChristmasMob(creature.getTemplate().getTemplateId());
    }

    public static boolean isHalloweenMob(int templateId) {
        if (templateId == CustomCreatures.ominousTreeId) {
            return true;
        } else return templateId == CustomCreatures.scaryPumpkinId;
    }

    public static boolean isHalloweenMob(Creature creature) {
        return isHalloweenMob(creature.getTemplate().getTemplateId());
    }

    public static boolean isRareCreature(int templateId) {
        if (templateId == CustomCreatures.spectralDragonHatchlingId) {
            return true;
        } else return templateId == CustomCreatures.reaperId;
    }

    public static boolean isRareCreature(Creature creature) {
        return isRareCreature(creature.getTemplate().getTemplateId());
    }

    public static void setTemplateVariables() throws NoSuchCreatureTemplateException, IllegalAccessException, NoSuchFieldException {
        // Set fog spider corpse models
        String dustPile = "fogspider.";
        String zombie = "zombie.butchered.free.";
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.avengerId, dustPile);
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.spiritTrollId, dustPile);
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.spectralDragonHatchlingId, dustPile);
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.chargerId, dustPile);
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.spiritStagId, dustPile);
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.whiteBuffaloSpiritId, dustPile);
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.frostyId, dustPile);
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.snowmanId, dustPile);
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.blackWidowId, dustPile);
        // Set custom creature corpse models
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.injuredPirateId, "islestowerguard.free.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.cobraId, "anaconda.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.fireCrabId, "crab.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.gorillaId, "mountaingorilla.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.hyenaId, "rabidhyena.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.largeBoarId, "wildboar.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.viperId, "kingcobra.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.warHoundId, "riftbeast.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.whiteBuffaloId, "greatwhitebuffalo.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.wolfPackmasterId, "blackwolf.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.blackWyvernId, "blackdragonhatchling.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.greenWyvernId, "greendragonhatchling.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.redWyvernId, "reddragonhatchling.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.whiteWyvernId, "whitedragonhatchling.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.blueWyvernId, "bluedragonhatchling.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.facebreykerId, "riftogre.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.kongId, "mountaingorilla.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.reanimatedSkeletonId, "skeleton.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.zombieHulkId, zombie);
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.zombieLeaderId, zombie);
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.zombieWalkerId, zombie);
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.forestSpiderId, "hugespider.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.giantId, "forestgiant.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.hornedPonyId, "unicorn.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.cyclopsId, "kyklops.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.clucksterId, "rooster.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.ridingRoosterId, "rooster.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.treasureGoblinMenageristGoblinId, "treasuregoblin.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.golemId, "lavafiend.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.golemlingId, "lavafiend.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.bloblingId, "blob.");
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.mimicId, "blob.");
        // Set titan corpse models
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.lilithId, dustPile);
        CreatureTemplateModifier.setCorpseModel(CustomCreatures.ifritId, dustPile);
        // Set No Corpse
        CreatureTemplateModifier.setNoCorpse(CustomCreatures.iceCatId);
        CreatureTemplateModifier.setNoCorpse(CustomCreatures.ifritFiendId);
        CreatureTemplateModifier.setNoCorpse(CustomCreatures.ifritSpiderId);
        CreatureTemplateModifier.setNoCorpse(CustomCreatures.lilithWraithId);
        CreatureTemplateModifier.setNoCorpse(CustomCreatures.lilithZombieId);
        CreatureTemplateModifier.setNoCorpse(CustomCreatures.fireGiantId);
        CreatureTemplateModifier.setNoCorpse(CustomCreatures.terrorId);
        CreatureTemplateModifier.setNoCorpse(CustomCreatures.golemId);
        CreatureTemplateModifier.setNoCorpse(CustomCreatures.blobId);
        CreatureTemplateModifier.setNoCorpse(CustomCreatures.prismaticBlobId);
        CreatureTemplateModifier.setNoCorpse(CustomCreatures.spiritStagId);
        CreatureTemplateModifier.setNoCorpse(CustomCreatures.reaperId);
        CreatureTemplateModifier.setNoCorpse(CustomCreatures.spectralDragonHatchlingId);
        CreatureTemplateModifier.setNoCorpse(CustomCreatures.mimicChestId);
        // Set creatures base combat rating
        //setCombatRating(CreatureTemplateIds.GUARD_SPIRIT_GOOD_DANGEROUS, 30f);
        // Also apply the ghost modifier
        CreatureTemplateModifier.setGhost(CustomCreatures.spiritTrollId);
        CreatureTemplateModifier.setGhost(CustomCreatures.avengerId);
        CreatureTemplateModifier.setGhost(CustomCreatures.lilithWraithId);
        CreatureTemplateModifier.setGhost(CustomCreatures.chargerId);
        // Make uniques no rebirth and non-regenerative.
        CreatureTemplateModifier.setUniqueTypes(CreatureTemplateIds.DRAGON_BLACK_CID);
        CreatureTemplateModifier.setUniqueTypes(CreatureTemplateIds.DRAGON_BLUE_CID);
        CreatureTemplateModifier.setUniqueTypes(CreatureTemplateIds.DRAGON_GREEN_CID);
        CreatureTemplateModifier.setUniqueTypes(CreatureTemplateIds.DRAGON_RED_CID);
        CreatureTemplateModifier.setUniqueTypes(CreatureTemplateIds.DRAGON_WHITE_CID);
        CreatureTemplateModifier.setUniqueTypes(CreatureTemplateIds.DRAKE_BLACK_CID);
        CreatureTemplateModifier.setUniqueTypes(CreatureTemplateIds.DRAKE_BLUE_CID);
        CreatureTemplateModifier.setUniqueTypes(CreatureTemplateIds.DRAKE_GREEN_CID);
        CreatureTemplateModifier.setUniqueTypes(CreatureTemplateIds.DRAKE_RED_CID);
        CreatureTemplateModifier.setUniqueTypes(CreatureTemplateIds.DRAKE_WHITE_CID);
        CreatureTemplateModifier.setUniqueTypes(CreatureTemplateIds.GOBLIN_LEADER_CID);
        CreatureTemplateModifier.setUniqueTypes(CreatureTemplateIds.FOREST_GIANT_CID);
        CreatureTemplateModifier.setUniqueTypes(CreatureTemplateIds.TROLL_KING_CID);
        CreatureTemplateModifier.setUniqueTypes(CreatureTemplateIds.CYCLOPS_CID);
        // Natural Armour Increases:
        CreatureTemplateModifier.setNaturalArmour(CreatureTemplateIds.DRAGON_BLUE_CID, 0.035f);
        CreatureTemplateModifier.setNaturalArmour(CreatureTemplateIds.DRAGON_WHITE_CID, 0.035f);
        CreatureTemplateModifier.setNaturalArmour(CreatureTemplateIds.DRAGON_BLACK_CID, 0.045f);
        CreatureTemplateModifier.setNaturalArmour(CreatureTemplateIds.DRAGON_WHITE_CID, 0.035f);
        CreatureTemplateModifier.setNaturalArmour(CreatureTemplateIds.DRAKE_RED_CID, 0.065f);
        CreatureTemplateModifier.setNaturalArmour(CreatureTemplateIds.DRAKE_BLUE_CID, 0.065f);
        CreatureTemplateModifier.setNaturalArmour(CreatureTemplateIds.DRAKE_WHITE_CID, 0.075f);
        CreatureTemplateModifier.setNaturalArmour(CreatureTemplateIds.DRAKE_GREEN_CID, 0.065f);
        CreatureTemplateModifier.setNaturalArmour(CreatureTemplateIds.DRAKE_BLACK_CID, 0.055f);
        CreatureTemplateModifier.setNaturalArmour(CreatureTemplateIds.GOBLIN_LEADER_CID, 0.055f);
        CreatureTemplateModifier.setNaturalArmour(CreatureTemplateIds.GUARD_KINGDOM_TOWER_FREEDOM, 0.045f);
        CreatureTemplateModifier.setNaturalArmour(CreatureTemplateIds.GUARD_KINGDOM_TOWER_JENN, 0.045f);
        CreatureTemplateModifier.setNaturalArmour(CreatureTemplateIds.GUARD_KINGDOM_TOWER_MOLREHAN, 0.045f);
        CreatureTemplateModifier.setNaturalArmour(CreatureTemplateIds.GUARD_KINGDOM_TOWER_LIBILA, 0.045f);
        // Set Grazers
        //CreatureTemplateModifier.setGrazer(CreatureTemplateIds.HEN_CID);
        //CreatureTemplateModifier.setGrazer(CreatureTemplateIds.CHICKEN_CID);
        //CreatureTemplateModifier.setGrazer(CreatureTemplateIds.ROOSTER_CID);
        //CreatureTemplateModifier.setGrazer(CreatureTemplateIds.PIG_CID);
        // Domesticate whales and enable milking
        if (ModConfig.milkWhales) {
            CreatureTemplateModifier.setMilkable(CreatureTemplateIds.BLUE_WHALE_CID);// Set Milkable
            CreatureTemplateModifier.setDomestic(CreatureTemplateIds.BLUE_WHALE_CID);// Set Domestic
        }
        // Set worg fields
        CreatureTemplateModifier.setWorgFields();
        // Set leaderTemplateId
        CreatureTemplateModifier.setLeaderTemplateId(CreatureTemplateIds.WOLF_BLACK_CID, CustomCreatures.wolfPackmasterId);
        // Set age
        CreatureTemplateModifier.setMaxAge(CreatureTemplateIds.SKELETON_CID, 100);
        CreatureTemplateModifier.setMaxAge(CreatureTemplateIds.WORG_CID, 100);
        // Set skills for certain creatures
        //CreatureTemplateModifier.setSkill(CreatureTemplateIds.CYCLOPS_CID, SkillList.GROUP_FIGHTING, 50.0f);
        // Set combat rating for creatures to improve their bounty
        CreatureTemplateModifier.setBaseCombatRating(CreatureTemplateIds.SON_OF_NOGUMP_CID, 30.0f);
        CreatureTemplateModifier.setBaseCombatRating(CreatureTemplateIds.GUARD_KINGDOM_TOWER_FREEDOM, 80.0f);
        // Set grazer
        CreatureTemplateModifier.setGrazer(CreatureTemplateIds.HEN_CID);
        // Set Description
        //CreatureTemplateModifier.setLongDesc(CreatureTemplateIds.HUMAN_CID, "A fearsome warrior that never seems to stay dead for very long.");
        // Set Speed
        CreatureTemplateModifier.setSpeed(CreatureTemplateIds.DRAKESPIRIT_CID, 0.5f);
        CreatureTemplateModifier.setSpeed(CreatureTemplateIds.EAGLESPIRIT_CID, 0.5f);
        // Set non-Sentinel
        CreatureTemplateModifier.setNonSentinel(CustomCreatures.npcTravelingTraderId);
    }

    public static boolean spawnCreature(final int templateId, final Creature performer, final boolean scroll) {
        try {
            final CreatureTemplate ct = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            byte sex = 0;
            boolean reborn = true;
            if (Server.rand.nextInt(2) == 0) {
                sex = 1;
            }
            if (scroll) {
                sex = 0;
            }
            byte ctype = (byte) Math.max(0, Server.rand.nextInt(22) - 10);
            if (Server.rand.nextInt(20) == 0) {
                ctype = 99;
            }
            if (scroll) {
                ctype = 0;
            }
            if (scroll) {
                reborn = false;
            }
            final Creature creature = Creature.doNew(templateId, true, performer.getPosX(), performer.getPosY(), Server.rand.nextFloat() * 360f, performer.getLayer(), ct.getName(), sex, performer.getKingdomId(), ctype, reborn);
            if (performer.getPet() != null && !performer.getPet().equals(creature)) {
                performer.getCommunicator().sendNormalServerMessage(String.format("%s stops following you.", performer.getPet().getNameWithGenus()));
                if (performer.getPet().getLeader() == performer) {
                    performer.getPet().setLeader(null);
                }
                performer.getPet().setDominator(MiscConstants.NOID);
                performer.setPet(MiscConstants.NOID);
            }
            if (!scroll) {
                creature.setDominator(performer.getWurmId());
                creature.setLoyalty((float) Math.max(10.0, 50.0));
                performer.setPet(creature.getWurmId());
            }
            creature.setLeader(performer);
            creature.getCommunicator().sendNormalServerMessage(String.format("%s charms you!", performer.getNameWithGenus()));
            performer.getCommunicator().sendNormalServerMessage(String.format("You Summon %s with great force and power.", creature.getNameWithGenus()));
        } catch (Exception nst) {
            RequiemLogging.logWarning(nst.getMessage());
            return false;
        }
        return true;
    }

    public static void init() throws CannotCompileException {
        RequiemLogging.logInfo("========= Initializing MethodsBestiary.init =========");
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();

            classPool.getCtClass("com.wurmonline.server.zones.VirtualZone").getMethod("addCreature", "(JZJFFF)Z")
                    .insertAfter(String.format("%s.addCreatureHook(this, $1);", Hooks.class.getName()));

            HookManager.getInstance().registerHook("com.wurmonline.server.creatures.Creature", "doNew", "(IZFFFILjava/lang/String;BBBZB)Lcom/wurmonline/server/creatures/Creature;", new InvocationHandlerFactory() {
                @Override
                public InvocationHandler createInvocationHandler() {
                    return new InvocationHandler() {
                        @Override
                        public Object invoke(Object object, Method method, Object[] args) throws Throwable {
                            int id = (int) args[0];
                            // Spawn Fog Goblins instead of Fog Spiders
                            if (id == CreatureTemplateIds.SPIDER_FOG_CID) {
                                args[0] = CustomCreatures.fogGoblinId;
                                id = CustomCreatures.fogGoblinId;
                            }
                            if (id == CustomCreatures.fogGoblinId) {
                                args[1] = true;
                            }
                            return method.invoke(object, args);
                        }
                    };
                }
            });
        } catch (NotFoundException e) {
            throw new HookException(e);
        }
    }
}
