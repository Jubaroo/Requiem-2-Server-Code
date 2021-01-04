package org.jubaroo.mods.wurm.server.creatures;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Server;
import com.wurmonline.server.combat.Archery;
import com.wurmonline.server.combat.Weapon;
import com.wurmonline.server.creatures.*;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.NoSpaceException;
import com.wurmonline.server.skills.NoSuchSkillException;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.shared.constants.BodyPartConstants;
import com.wurmonline.shared.constants.Enchants;
import javassist.*;
import javassist.bytecode.Descriptor;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import mod.sin.lib.Util;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.classhooks.InvocationHandlerFactory;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.creatures.traitedCreatures.Zebra;
import org.jubaroo.mods.wurm.server.server.Constants;
import org.jubaroo.mods.wurm.server.tools.EffectsTools;

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
            replace = String.format("if(%s.isSacrificeImmune($2)){  performer.getCommunicator().sendNormalServerMessage(\"This creature cannot be sacrificed.\");  return true;}", MethodsBestiary.class.getName());
            Util.insertBeforeDescribed(thisClass, ctMethodsReligion, "sacrifice", desc1, replace);

            Util.setReason("Disable afk training.");
            CtClass ctCombatHandler = classPool.get("com.wurmonline.server.creatures.CombatHandler");
            //"if($1.isPlayer() && $1.getTarget() != $0){" +
            //"  RequiemLogging.logInfo(\"Non-targeted mob detected - \" + $1.getName());" +
            replace = String.format("if(%s.blockSkillFrom($1, $0)){  $_ = true;}else{  $_ = $proceed($$);}", MethodsBestiary.class.getName());
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
                            m.replace(String.format("if(%s.blockSkillFrom($1, $0)){  $_ = true;}else{  $_ = $proceed($$);}", MethodsBestiary.class.getName()));
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
            replace = String.format("if(%s.denyPathingOverride($0)){  $_ = false;}else{  $_ = $proceed($$);}", MethodsBestiary.class.getName());
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
            replace = String.format("$10 = %s.newCreatureType($1, $10);", MethodsBestiary.class.getName());
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
            replace = String.format("if(%s.isArcheryImmune($1, $2)){  return true;}", MethodsBestiary.class.getName());
            Util.insertBeforeDescribed(thisClass, ctArchery, "attack", desc3, replace);

            Util.setReason("Auto-Genesis a creature born on enchanted grass");
            replace = String.format("%s.checkEnchantedBreed(newCreature);$_ = $proceed($$);", MethodsBestiary.class.getName());
            Util.instrumentDeclared(thisClass, ctCreature, "checkPregnancy", "saveCreatureName", replace);

            Util.setReason("Set custom corpse sizes.");
            replace = String.format("$_ = $proceed($$);if(%s.hasCustomCorpseSize(this)){  %s.setCorpseSizes(this, corpse);}", CreatureSize.class.getName(), CreatureSize.class.getName());
            Util.instrumentDescribed(thisClass, ctCreature, "die", desc5, "addItem", replace);

            Util.setReason("Add spell resistance to custom creatures.");
            replace = String.format("float cResist = %s.getCustomSpellResistance(this);if(cResist >= 0f){  return cResist;}", MethodsBestiary.class.getName());
            Util.insertBeforeDeclared(thisClass, ctCreature, "addSpellResistance", replace);

            Util.setReason("Allow custom creatures to have breeding names.");
            replace = String.format("$_ =%s.shouldBreedName(this);", MethodsBestiary.class.getName());
            Util.instrumentDeclared(thisClass, ctCreature, "checkPregnancy", "isHorse", replace);

            Util.setReason("Allow ghost creatures to breed (Chargers).");
            CtClass ctMethodsCreatures = classPool.get("com.wurmonline.server.behaviours.MethodsCreatures");
            replace = "$_ = false;";
            Util.instrumentDeclared(thisClass, ctMethodsCreatures, "breed", "isGhost", replace);

            Util.setReason("Allow ghost creatures to drop corpses.");
            replace = String.format("if(%s.isGhostCorpse(this)){  $_ = false;}else{  $_ = $proceed($$);}", MethodsBestiary.class.getName());
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
            replace = "$_ = $proceed($$);" +
                    EffectsTools.class.getName() + ".addCreatureSpecialEffect(copyId != -10 ? copyId : creatureId, $0, creature);";
            Util.instrumentDescribed(thisClass, ctVirtualZone, "addCreature", desc4, "sendNewCreature", replace);

            Util.setReason("Ensure unique creatures cannot be hitched to vehicles.");
            CtClass ctVehicle = classPool.get("com.wurmonline.server.behaviours.Vehicle");
            replace = String.format("if(%s.isNotHitchable($1)){  return false;}", MethodsBestiary.class.getName());
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

    private static boolean isConditionExempt(int templateId) {
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

    static boolean isUsuallyHitched(int templateId) {
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
    // Allow ghost creatures to drop corpses

    public static boolean isGhostCorpse(Creature creature) {
        int templateId = creature.getTemplate().getTemplateId();
        if (templateId == CustomCreatures.avengerId) {
            return true;
        } else if (templateId == CustomCreatures.spiritTrollId) {
            return true;
        } else return templateId == CustomCreatures.chargerId;
    }
    // Ensure unique creatures cannot be hitched to vehicles

    public static boolean isNotHitchable(Creature creature) {
        if (creature.isUnique()) {
            return true;
        }
        int cid = creature.getTemplate().getTemplateId();
        if (cid == CustomCreatures.avengerId) {
            return true;
        } else if (cid == CustomCreatures.giantId) {
            return true;
        } else if (cid == CustomCreatures.spiritTrollId) {
            return true;
        } else if (cid == CreatureTemplateIds.TROLL_CID) {
            return true;
        } else return cid == CreatureTemplateIds.GOBLIN_CID;
    }
    // Disable sacrificing strong creatures

    public static boolean isSacrificeImmune(Creature creature) {
        if (Titans.isTitan(creature) || Titans.isTitanMinion(creature)) {
            return true;
        } else if (isRareCreature(creature)) {
            return true;
        } else return creature.isUnique();
    }
    // Disable archery altogether against certain creatures

    public static boolean isArcheryImmune(Creature performer, Creature defender) {
        if (Titans.isTitan(defender) || Titans.isTitanMinion(defender)) {
            performer.getCommunicator().sendCombatNormalMessage(String.format("You cannot archer %s, as it is protected by a Titan.", defender.getName()));
            return true;
        }
        String message = String.format("The %s would be unaffected by your arrows.", defender.getName());
        boolean immune = false;
        Item arrow = Archery.getArrow(performer);
        if (arrow == null) { // Copied directly from the attack() method in Archery.
            performer.getCommunicator().sendCombatNormalMessage("You have no arrows left to shoot!");
            return true;
        }

        //int defenderTemplateId = defender.getTemplate().getTemplateId();
        if (defender.isRegenerating() && arrow.getTemplateId() == ItemList.arrowShaft) {
            message = String.format("The %s would be unaffected by the %s.", defender.getName(), arrow.getName());
            immune = true;
        }/*else if(defender.getTemplate().isNotRebirthable()){
			immune = true;
		}*/ else if (defender.isUnique()) {
            immune = true;
        }
        if (immune) {
            performer.getCommunicator().sendCombatNormalMessage(message);
        }
        return immune;
    }
    // Auto-Genesis a creature born on enchanted grass

    public static void checkEnchantedBreed(Creature creature) {
        int tile = Server.surfaceMesh.getTile(creature.getTileX(), creature.getTileY());
        byte type = Tiles.decodeType(tile);
        if (type == Tiles.Tile.TILE_ENCHANTED_GRASS.id) {
            RequiemLogging.logInfo(String.format("Creature %s was born on enchanted grass, and has a negative trait removed!", creature.getName()));
            Server.getInstance().broadCastAction(String.format("%s was born on enchanted grass, and feels more healthy!", creature.getName()), creature, 10);
            creature.removeRandomNegativeTrait();
        }
    }
    // Used to give random names to newborn, bred creatures

    public static boolean shouldBreedName(Creature creature) {
        int cid = creature.getTemplate().getTemplateId();
        if (cid == CustomCreatures.chargerId) {
            return true;
        } else if (cid == CustomCreatures.greenWyvernId) {
            return true;
        } else if (cid == CustomCreatures.redWyvernId) {
            return true;
        } else if (cid == CustomCreatures.whiteWyvernId) {
            return true;
        } else if (cid == CustomCreatures.blackWyvernId) {
            return true;
        } else if (cid == CustomCreatures.blueWyvernId) {
            return true;
        }
        return creature.isHorse();
    }
    // Add spell resistance to custom creatures

    public static float getCustomSpellResistance(Creature creature) {
        int templateId = creature.getTemplate().getTemplateId();
        if (templateId == CustomCreatures.avengerId) {
            return 0.5f;
        } else if (templateId == CustomCreatures.chargerId) {
            return 1.4f;
        } else if (templateId == CustomCreatures.giantId) {
            return 0.3f;
        } else if (templateId == CustomCreatures.largeBoarId) {
            return 0.8f;
        } else if (templateId == CustomCreatures.reaperId) {
            return 0.1f;
        } else if (templateId == CustomCreatures.spectralDragonHatchlingId) {
            return 0.1f;
        } else if (templateId == CustomCreatures.spiritTrollId) {
            return 0.2f;
        } else if (templateId == CustomCreatures.blackWyvernId) {
            return 0.4f;
        } else if (templateId == CustomCreatures.greenWyvernId) {
            return 0.6f;
        } else if (templateId == CustomCreatures.whiteWyvernId) {
            return 0.5f;
        } else if (templateId == CustomCreatures.redWyvernId) {
            return 0.25f;
        } else if (templateId == CustomCreatures.blueWyvernId) {
            return 0.15f;
        }
        return -1f;
    }
    // Disable afk training

    public static boolean blockSkillFrom(Creature defender, Creature attacker) {
        if (defender == null || attacker == null) {
            return false;
        }
        if (defender.isPlayer() && defender.getTarget() != attacker) {
            return true;
        }
        if (defender.isPlayer()) {
            Item weap = defender.getPrimWeapon();
            if (weap != null && weap.isWeapon()) {
                try {
                    double dam = Weapon.getModifiedDamageForWeapon(weap, defender.getSkills().getSkill(SkillList.BODY_STRENGTH), true) * 1000.0;
                    dam += Server.getBuffedQualityEffect(weap.getCurrentQualityLevel() / 100f) * (double) Weapon.getBaseDamageForWeapon(weap) * 2400.0;
                    if (attacker.getArmourMod() < 0.1f) {
                        return false;
                    }
                    if (dam * attacker.getArmourMod() < 3000) {
                        return true;
                    }
                } catch (NoSuchSkillException e) {
                    e.printStackTrace();
                }
            } else {
                if (defender.getBonusForSpellEffect(Enchants.CRET_BEARPAW) < 50f) {
                    return true;
                }
            }
        }
        try {
            if (defender.isPlayer() && attacker.getArmour(BodyPartConstants.TORSO) != null) {
                return true;
            }
        } catch (NoArmourException | NoSpaceException ignored) {
        }
        return false;
    }
    // Deny ghost creatures walking through walls

    public static boolean denyPathingOverride(Creature creature) {
        if (creature.getTemplate().getTemplateId() == CustomCreatures.chargerId) {
            return true;
        } else if (creature.getTemplate().getTemplateId() == CustomCreatures.ghostHellHorseId) {
            return true;
        } else if (creature.getTemplate().getTemplateId() == CustomCreatures.ghostHorseId) {
            return true;
        } else if (creature.getTemplate().getTemplateId() == CreatureTemplateIds.DRAKESPIRIT_CID) {
            return true;
        } else if (creature.getTemplate().getTemplateId() == CustomCreatures.spiritTrollId) {
            return true;
        } else if (creature.getTemplate().getTemplateId() == CreatureTemplateIds.EAGLESPIRIT_CID) {
            return true;
        } else if (creature.getTemplate().getTemplateId() == CustomCreatures.chargerId) {
            return true;
        } else return creature.getTemplate().getTemplateId() == CustomCreatures.avengerId;
    }
    // Apply random types to creatures in the wilderness

    public static byte newCreatureType(int templateid, byte ctype) throws Exception {
        CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateid);
        if (ctype == 0 && (template.isAggHuman() || template.getBaseCombatRating() > 10) && !template.isUnique() && !Titans.isTitan(templateid) && !isConditionExempt(templateid)) {
            if (Server.rand.nextInt(5) == 0) {
                ctype = (byte) (Server.rand.nextInt(11) + 1);
                if (Server.rand.nextInt(50) == 0) {
                    ctype = 99;
                }
            }
        }
        return ctype;
    }

    public static void setTemplateVariables() throws NoSuchCreatureTemplateException, IllegalAccessException, NoSuchFieldException {
        // Set fog spider corpse models
        String fog_spider = "fogspider.";
        String zombie = "zombie.butchered.free.";
        TemplateModifier.setCorpseModel(CustomCreatures.avengerId, fog_spider);
        TemplateModifier.setCorpseModel(CustomCreatures.spiritTrollId, fog_spider);
        TemplateModifier.setCorpseModel(CustomCreatures.spectralDragonHatchlingId, fog_spider);
        TemplateModifier.setCorpseModel(CustomCreatures.chargerId, fog_spider);
        TemplateModifier.setCorpseModel(CustomCreatures.spiritStagId, fog_spider);
        TemplateModifier.setCorpseModel(CustomCreatures.whiteBuffaloSpiritId, fog_spider);
        TemplateModifier.setCorpseModel(CustomCreatures.frostyId, fog_spider);
        TemplateModifier.setCorpseModel(CustomCreatures.snowmanId, fog_spider);
        TemplateModifier.setCorpseModel(CustomCreatures.blackWidowId, fog_spider);
        // Set custom creature corpse models
        TemplateModifier.setCorpseModel(CustomCreatures.injuredPirateId, "islestowerguard.free.");
        TemplateModifier.setCorpseModel(CustomCreatures.cobraId, "anaconda.");
        TemplateModifier.setCorpseModel(CustomCreatures.fireCrabId, "crab.");
        TemplateModifier.setCorpseModel(CustomCreatures.gorillaId, "mountaingorilla.");
        TemplateModifier.setCorpseModel(CustomCreatures.hyenaId, "rabidhyena.");
        TemplateModifier.setCorpseModel(CustomCreatures.largeBoarId, "wildboar.");
        TemplateModifier.setCorpseModel(CustomCreatures.viperId, "kingcobra.");
        TemplateModifier.setCorpseModel(CustomCreatures.warHoundId, "riftbeast.");
        TemplateModifier.setCorpseModel(CustomCreatures.whiteBuffaloId, "greatwhitebuffalo.");
        TemplateModifier.setCorpseModel(CustomCreatures.wolfPackmasterId, "blackwolf.");
        TemplateModifier.setCorpseModel(CustomCreatures.blackWyvernId, "blackdragonhatchling.");
        TemplateModifier.setCorpseModel(CustomCreatures.greenWyvernId, "greendragonhatchling.");
        TemplateModifier.setCorpseModel(CustomCreatures.redWyvernId, "reddragonhatchling.");
        TemplateModifier.setCorpseModel(CustomCreatures.whiteWyvernId, "whitedragonhatchling.");
        TemplateModifier.setCorpseModel(CustomCreatures.blueWyvernId, "bluedragonhatchling.");
        TemplateModifier.setCorpseModel(CustomCreatures.facebreykerId, "riftogre.");
        TemplateModifier.setCorpseModel(CustomCreatures.kongId, "mountaingorilla.");
        TemplateModifier.setCorpseModel(CustomCreatures.reanimatedSkeletonId, "skeleton.");
        TemplateModifier.setCorpseModel(CustomCreatures.zombieHulkId, zombie);
        TemplateModifier.setCorpseModel(CustomCreatures.zombieLeaderId, zombie);
        TemplateModifier.setCorpseModel(CustomCreatures.zombieWalkerId, zombie);
        TemplateModifier.setCorpseModel(CustomCreatures.forestSpiderId, "hugespider.");
        TemplateModifier.setCorpseModel(CustomCreatures.giantId, "forestgiant.");
        TemplateModifier.setCorpseModel(CustomCreatures.hornedPonyId, "unicorn.");
        TemplateModifier.setCorpseModel(CustomCreatures.cyclopsId, "kyklops.");
        TemplateModifier.setCorpseModel(CustomCreatures.clucksterId, "rooster.");
        TemplateModifier.setCorpseModel(CustomCreatures.ridingRoosterId, "rooster.");
        TemplateModifier.setCorpseModel(CustomCreatures.treasureGoblinMenageristGoblinId, "treasuregoblin.");
        TemplateModifier.setCorpseModel(CustomCreatures.golemId, "lavafiend.");
        TemplateModifier.setCorpseModel(CustomCreatures.golemlingId, "lavafiend.");
        TemplateModifier.setCorpseModel(CustomCreatures.bloblingId, "blob.");
        TemplateModifier.setCorpseModel(CustomCreatures.mimicId, "blob.");
        // Set titan corpse models
        TemplateModifier.setCorpseModel(CustomCreatures.lilithId, fog_spider);
        TemplateModifier.setCorpseModel(CustomCreatures.ifritId, fog_spider);
        // Set No Corpse
        TemplateModifier.setNoCorpse(CustomCreatures.iceCatId);
        TemplateModifier.setNoCorpse(CustomCreatures.ifritFiendId);
        TemplateModifier.setNoCorpse(CustomCreatures.ifritSpiderId);
        TemplateModifier.setNoCorpse(CustomCreatures.lilithWraithId);
        TemplateModifier.setNoCorpse(CustomCreatures.lilithZombieId);
        TemplateModifier.setNoCorpse(CustomCreatures.fireGiantId);
        TemplateModifier.setNoCorpse(CustomCreatures.terrorId);
        TemplateModifier.setNoCorpse(CustomCreatures.golemId);
        TemplateModifier.setNoCorpse(CustomCreatures.blobId);
        TemplateModifier.setNoCorpse(CustomCreatures.prismaticBlobId);
        TemplateModifier.setNoCorpse(CustomCreatures.spiritStagId);
        TemplateModifier.setNoCorpse(CustomCreatures.reaperId);
        TemplateModifier.setNoCorpse(CustomCreatures.spectralDragonHatchlingId);
        TemplateModifier.setNoCorpse(CustomCreatures.mimicChestId);
        // Set creatures base combat rating
        //setCombatRating(CreatureTemplateIds.GUARD_SPIRIT_GOOD_DANGEROUS, 30f);
        // Also apply the ghost modifier
        TemplateModifier.setGhost(CustomCreatures.spiritTrollId);
        TemplateModifier.setGhost(CustomCreatures.avengerId);
        TemplateModifier.setGhost(CustomCreatures.lilithWraithId);
        TemplateModifier.setGhost(CustomCreatures.chargerId);
        // Make uniques no rebirth and non-regenerative.
        TemplateModifier.setUniqueTypes(CreatureTemplateIds.DRAGON_BLACK_CID);
        TemplateModifier.setUniqueTypes(CreatureTemplateIds.DRAGON_BLUE_CID);
        TemplateModifier.setUniqueTypes(CreatureTemplateIds.DRAGON_GREEN_CID);
        TemplateModifier.setUniqueTypes(CreatureTemplateIds.DRAGON_RED_CID);
        TemplateModifier.setUniqueTypes(CreatureTemplateIds.DRAGON_WHITE_CID);
        TemplateModifier.setUniqueTypes(CreatureTemplateIds.DRAKE_BLACK_CID);
        TemplateModifier.setUniqueTypes(CreatureTemplateIds.DRAKE_BLUE_CID);
        TemplateModifier.setUniqueTypes(CreatureTemplateIds.DRAKE_GREEN_CID);
        TemplateModifier.setUniqueTypes(CreatureTemplateIds.DRAKE_RED_CID);
        TemplateModifier.setUniqueTypes(CreatureTemplateIds.DRAKE_WHITE_CID);
        TemplateModifier.setUniqueTypes(CreatureTemplateIds.GOBLIN_LEADER_CID);
        TemplateModifier.setUniqueTypes(CreatureTemplateIds.FOREST_GIANT_CID);
        TemplateModifier.setUniqueTypes(CreatureTemplateIds.TROLL_KING_CID);
        TemplateModifier.setUniqueTypes(CreatureTemplateIds.CYCLOPS_CID);
        // Natural Armour Increases:
        TemplateModifier.setNaturalArmour(CreatureTemplateIds.DRAGON_BLUE_CID, 0.035f);
        TemplateModifier.setNaturalArmour(CreatureTemplateIds.DRAGON_WHITE_CID, 0.035f);
        TemplateModifier.setNaturalArmour(CreatureTemplateIds.DRAGON_BLACK_CID, 0.045f);
        TemplateModifier.setNaturalArmour(CreatureTemplateIds.DRAGON_WHITE_CID, 0.035f);
        TemplateModifier.setNaturalArmour(CreatureTemplateIds.DRAKE_RED_CID, 0.065f);
        TemplateModifier.setNaturalArmour(CreatureTemplateIds.DRAKE_BLUE_CID, 0.065f);
        TemplateModifier.setNaturalArmour(CreatureTemplateIds.DRAKE_WHITE_CID, 0.075f);
        TemplateModifier.setNaturalArmour(CreatureTemplateIds.DRAKE_GREEN_CID, 0.065f);
        TemplateModifier.setNaturalArmour(CreatureTemplateIds.DRAKE_BLACK_CID, 0.055f);
        TemplateModifier.setNaturalArmour(CreatureTemplateIds.GOBLIN_LEADER_CID, 0.055f);
        TemplateModifier.setNaturalArmour(CreatureTemplateIds.GUARD_KINGDOM_TOWER_FREEDOM, 0.045f);
        TemplateModifier.setNaturalArmour(CreatureTemplateIds.GUARD_KINGDOM_TOWER_JENN, 0.045f);
        TemplateModifier.setNaturalArmour(CreatureTemplateIds.GUARD_KINGDOM_TOWER_MOLREHAN, 0.045f);
        TemplateModifier.setNaturalArmour(CreatureTemplateIds.GUARD_KINGDOM_TOWER_LIBILA, 0.045f);
        // Set Grazers
        //TemplateModifier.setGrazer(CreatureTemplateIds.HEN_CID);
        //TemplateModifier.setGrazer(CreatureTemplateIds.CHICKEN_CID);
        //TemplateModifier.setGrazer(CreatureTemplateIds.ROOSTER_CID);
        //TemplateModifier.setGrazer(CreatureTemplateIds.PIG_CID);
        // Domesticate whales and enable milking
        if (Constants.milkWhales) {
            TemplateModifier.setMilkable(CreatureTemplateIds.BLUE_WHALE_CID);// Set Milkable
            TemplateModifier.setDomestic(CreatureTemplateIds.BLUE_WHALE_CID);// Set Domestic
        }
        // Set worg fields
        TemplateModifier.setWorgFields();
        // Set leaderTemplateId
        TemplateModifier.setLeaderTemplateId(CreatureTemplateIds.WOLF_BLACK_CID, CustomCreatures.wolfPackmasterId);
        // Set age
        TemplateModifier.setMaxAge(CreatureTemplateIds.SKELETON_CID, 100);
        TemplateModifier.setMaxAge(CreatureTemplateIds.WORG_CID, 100);
        // Set skills for certain creatures
        //TemplateModifier.setSkill(CreatureTemplateIds.CYCLOPS_CID, SkillList.GROUP_FIGHTING, 50.0f);
        // Set combat rating for creatures to improve their bounty
        TemplateModifier.setBaseCombatRating(CreatureTemplateIds.SON_OF_NOGUMP_CID, 30.0f);
        TemplateModifier.setBaseCombatRating(CreatureTemplateIds.GUARD_KINGDOM_TOWER_FREEDOM, 80.0f);
        // Set grazer
        TemplateModifier.setGrazer(CreatureTemplateIds.HEN_CID);
        // Set Description
        //TemplateModifier.setLongDesc(CreatureTemplateIds.HUMAN_CID, "A fearsome warrior that never seems to stay dead for very long.");
        // Set Speed
        TemplateModifier.setSpeed(CreatureTemplateIds.DRAKESPIRIT_CID, 0.5f);
        TemplateModifier.setSpeed(CreatureTemplateIds.EAGLESPIRIT_CID, 0.5f);
        // Set non-Sentinel
        TemplateModifier.setNonSentinel(CustomCreatures.npcTravelingTraderId);
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

            CtClass ctCreature = classPool.get("com.wurmonline.server.creatures.Creature");

            ctCreature.getMethod("setDeathEffects", "(ZII)V")
                    .insertBefore(String.format("%s.creatureDied(this, this.attackers);", CreatureLootOnDeath.class.getName()));

            classPool.getCtClass("com.wurmonline.server.zones.VirtualZone").getMethod("addCreature", "(JZJFFF)Z")
                    .insertAfter(String.format("%s.addCreatureHook(this, $1);", EffectsTools.class.getName()));

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
