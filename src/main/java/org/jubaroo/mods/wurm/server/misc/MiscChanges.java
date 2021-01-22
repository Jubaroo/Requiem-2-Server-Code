package org.jubaroo.mods.wurm.server.misc;

import com.wurmonline.server.*;
import com.wurmonline.server.behaviours.Actions;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.SpellEffectsEnum;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.ItemTemplate;
import com.wurmonline.server.items.SimpleCreationEntry;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.players.PlayerInfo;
import com.wurmonline.server.players.PlayerInfoFactory;
import com.wurmonline.server.skills.Skill;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.server.villages.Village;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zones;
import com.wurmonline.shared.constants.Enchants;
import javassist.*;
import javassist.bytecode.Descriptor;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import mod.sin.lib.Util;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.creatures.MethodsBestiary;
import org.jubaroo.mods.wurm.server.creatures.Titans;
import org.jubaroo.mods.wurm.server.items.ItemMod;

import javax.annotation.Nullable;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

import static org.jubaroo.mods.wurm.server.ModConfig.*;
import static org.jubaroo.mods.wurm.server.server.constants.ItemConstants.createItemDescs;

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
                RequiemLogging.debug("Null source or target.");
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


    public static void SpawnTowerGuards() {
        RequiemLogging.debug("Trying to make pollGuards() methods public.");
        try {
            CtMethod method = HookManager.getInstance().getClassPool().get("com.wurmonline.server.kingdom.GuardTower")
                    .getMethod("pollGuards", "()V");
            method.setModifiers((method.getModifiers() & ~java.lang.reflect.Modifier.PRIVATE) | java.lang.reflect.Modifier.PUBLIC);

            method = HookManager.getInstance().getClassPool().get("com.wurmonline.server.villages.GuardPlan")
                    .getMethod("pollGuards", "()V");
            method.setModifiers((method.getModifiers() & ~java.lang.reflect.Modifier.PRIVATE) | Modifier.PUBLIC);

            RequiemLogging.debug("pollGuards() methods are now public.");
        } catch (Exception e) {
            RequiemLogging.logException("Couldn't make pollGuards() methods public.", e);
        }
    }

    public static void preInit() {
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();
            final Class<MiscChanges> thisClass = MiscChanges.class;
            String replace;

            // - Enable bridges to be built inside/over/through houses - //
            //CtClass ctPlanBridgeChecks = classPool.get("com.wurmonline.server.structures.PlanBridgeChecks");
            //replace = "{ return new com.wurmonline.server.structures.PlanBridgeCheckResult(false); }";
            //Util.setBodyDeclared(thisClass, ctPlanBridgeChecks, "checkForBuildings", replace);

            // - Disable mailboxes from being used while loaded - //
            CtClass ctItem = classPool.get("com.wurmonline.server.items.Item");
            replace = "$_ = $proceed($$);"
                    + "com.wurmonline.server.items.Item theTarget = com.wurmonline.server.Items.getItem(targetId);"
                    + "if(theTarget != null && theTarget.getTemplateId() >= 510 && theTarget.getTemplateId() <= 513){"
                    + "  if(theTarget.getTopParent() != theTarget.getWurmId()){"
                    + "    mover.getCommunicator().sendNormalServerMessage(\"Mailboxes cannot be used while loaded.\");"
                    + "    return false;"
                    + "  }"
                    + "}";
            Util.instrumentDeclared(thisClass, ctItem, "moveToItem", "getOwnerId", replace);

            CtClass ctCreature = classPool.get("com.wurmonline.server.creatures.Creature");

            CtClass ctPlayer = classPool.get("com.wurmonline.server.players.Player");
            // - Make leather not suck even after it's able to be combined. - //
            CtClass ctMethodsItems = classPool.get("com.wurmonline.server.behaviours.MethodsItems");
            replace = String.format("if(com.wurmonline.server.behaviours.MethodsItems.getImproveTemplateId(target) != %d){  $_ = $proceed($$);}else{  $_ = false;}", ItemList.leather);
            Util.instrumentDeclared(thisClass, ctMethodsItems, "improveItem", "isCombine", replace);

            // - Check new improve materials - //
            replace = String.format("int temp = %s.getModdedImproveTemplateId($1);if(temp != -10){  return temp;}", ItemMod.class.getName());
            Util.insertBeforeDeclared(thisClass, ctMethodsItems, "getImproveTemplateId", replace);

            // - Remove fatiguing actions requiring you to be on the ground - //
            CtClass ctAction = classPool.get("com.wurmonline.server.behaviours.Action");
            CtConstructor[] ctActionConstructors = ctAction.getConstructors();
            for (CtConstructor constructor : ctActionConstructors) {
                constructor.instrument(new ExprEditor() {
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getMethodName().equals("isFatigue")) {
                            m.replace("if(com.wurmonline.server.Servers.localServer.PVPSERVER){  if(!com.wurmonline.server.behaviours.Actions.isActionDestroy(this.getNumber())){    $_ = false;  }else{    $_ = $proceed($$);  }}else{  $_ = false;}");
                            //RequiemLogging.debug("Set isFatigue to false in action constructor.");
                        }
                    }
                });
            }

            Util.setReason("Fix Portal Issues.");
            CtClass ctPortal = classPool.get("com.wurmonline.server.questions.PortalQuestion");
            Util.instrumentDeclared(thisClass, ctPortal, "sendQuestion", "willLeaveServer", "$_ = true;");

            Util.setReason("Fix Portal Issues.");
            Util.instrumentDeclared(thisClass, ctPortal, "sendQuestion", "getKnowledge", "$_ = true;");

            Util.setReason("Disable the minimum 0.01 damage on shield damage, allowing damage modifiers to rule.");
            CtClass ctCombatHandler = classPool.get("com.wurmonline.server.creatures.CombatHandler");
            replace = "if($1 < 0.5f){  $_ = $proceed((float) 0, (float) $2);}else{  $_ = $proceed($$);}";
            Util.instrumentDeclared(thisClass, ctCombatHandler, "checkShield", "max", replace);

            // - Allow GM's to bypass the 5 second emote sound limit. - //
            replace = "if(this.getPower() > 0){  return true;}";
            Util.insertBeforeDeclared(thisClass, ctPlayer, "mayEmote", replace);

            // - Make creatures wander slightly if they are shot from afar by an arrow - //
            CtClass ctArrows = classPool.get("com.wurmonline.server.combat.Arrows");
            replace = "if(!defender.isPathing()){  defender.startPathing(com.wurmonline.server.Server.rand.nextInt(100));}$_ = $proceed($$);";
            Util.instrumentDeclared(thisClass, ctArrows, "addToHitCreature", "addAttacker", replace);

            Util.setReason("Attempt to prevent libila from losing faith when crossing servers.");
            CtClass ctIntraServerConnection = classPool.get("com.wurmonline.server.intra.IntraServerConnection");
            ctIntraServerConnection.getDeclaredMethod("savePlayerToDisk").instrument(new ExprEditor() {
                @Override
                public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                    if (Objects.equals("PVPSERVER", fieldAccess.getFieldName())) {
                        fieldAccess.replace("$_ = false;");
                        RequiemLogging.debug("Instrumented PVPSERVER = false for Libila faith transfers.");
                    }
                }
            });
            ctIntraServerConnection.getDeclaredMethod("savePlayerToDisk").instrument(new ExprEditor() {
                @Override
                public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                    if (Objects.equals("HOMESERVER", fieldAccess.getFieldName())) {
                        fieldAccess.replace("$_ = false;");
                        RequiemLogging.debug("Instrumented HOMESERVER = false for Libila faith transfers.");
                    }
                }
            });
            Util.setReason("Increase food affinity to give 30% increased skillgain instead of 10%.");
            CtClass ctSkill = classPool.get("com.wurmonline.server.skills.Skill");
            CtClass[] params4 = {
                    CtClass.doubleType,
                    CtClass.booleanType,
                    CtClass.floatType,
                    CtClass.booleanType,
                    CtClass.doubleType
            };
            String desc4 = Descriptor.ofMethod(CtClass.voidType, params4);
            replace = "int timedAffinity = (com.wurmonline.server.skills.AffinitiesTimed.isTimedAffinity(pid, this.getNumber()) ? 2 : 0);advanceMultiplicator *= (double)(1f + (float)timedAffinity * 0.1f);$_ = $proceed($$);";
            Util.instrumentDescribed(thisClass, ctSkill, "alterSkill", desc4, "hasSleepBonus", replace);

            Util.setReason("Double the rate at which charcoal piles produce items.");
            CtClass[] params5 = {
                    CtClass.booleanType,
                    CtClass.booleanType,
                    CtClass.longType
            };
            String desc5 = Descriptor.ofMethod(CtClass.booleanType, params5);
            replace = "this.createDaleItems();decayed = this.setDamage(this.damage + 1f * this.getDamageModifier());$_ = $proceed($$);";
            Util.instrumentDescribed(thisClass, ctItem, "poll", desc5, "createDaleItems", replace);

            Util.setReason("Allow traders to display more than 9 items of a single type.");
            CtClass ctTradeHandler = classPool.get("com.wurmonline.server.creatures.TradeHandler");
            ctTradeHandler.getDeclaredMethod("addItemsToTrade").instrument(new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("size") && m.getLineNumber() > 200) { // I don't think the line number check matters, but I'm leaving it here anyway.
                        m.replace("$_ = 1;");
                        RequiemLogging.logInfo("Instrumented size for trades to allow traders to show more than 9 items at a time.");
                    }
                }
            });

            // -- Identify players making over 10 commands per second and causing the server log message -- //
            CtClass ctCommunicator = classPool.get("com.wurmonline.server.creatures.Communicator");
            Util.setReason("Log excessive actions per second.");
            replace = "$_ = $proceed($$);if(this.player != null){  logInfo(\"Potential player macro: \"+this.player.getName()+\" [\"+this.commandsThisSecond+\" commands]\");}";
            Util.instrumentDeclared(thisClass, ctCommunicator, "reallyHandle_CMD_ITEM_CREATION_LIST", "log", replace);

            //CtClass[] params = {
            //        CtClass.doubleType,
            //        CtClass.booleanType,
            //        CtClass.floatType,
            //        CtClass.booleanType,
            //        CtClass.doubleType
            //};
            //String desc = Descriptor.ofMethod(CtClass.voidType, params);
            //double minRate = 1.0D;
            //double maxRate = 8.0D;
            //double newPower = 2.5;

            //Util.setReason("Adjust skill rate to a new, dynamic rate system.");
            //replace = "double minRate = " + minRate + ";" +
            //        "double maxRate = " + maxRate + ";" +
            //        "double newPower = " + newPower + ";" +
            //        "$1 = $1*(minRate+(maxRate-minRate)*Math.pow((100-this.knowledge)*0.01, newPower));";
            //Util.insertBeforeDescribed(thisClass, ctSkill, "alterSkill", desc, replace);

            Util.setReason("Reduce chance of lockpicks breaking.");
            replace = "$_ = 40f + $proceed($$);";
            Util.instrumentDeclared(thisClass, ctMethodsItems, "checkLockpickBreakage", "getCurrentQualityLevel", replace);

            // Allow Freedom players to absorb mycelium
            CtClass ctTileBehaviour = classPool.get("com.wurmonline.server.behaviours.TileBehaviour");
            CtMethod[] ctGetBehavioursFors = ctTileBehaviour.getDeclaredMethods("getBehavioursFor");
            for (CtMethod method : ctGetBehavioursFors) {
                method.instrument(new ExprEditor() {
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getMethodName().equals("getKingdomTemplateId")) {
                            m.replace("$_ = 3;");
                        }
                    }
                });
            }

            CtClass ctMethodsStructure = classPool.get("com.wurmonline.server.behaviours.MethodsStructure");
            Util.setReason("Allow players to construct larger houses.");
            float carpentryMultiplier = 2f;
            replace = String.format("if(!com.wurmonline.server.Servers.localServer.PVPSERVER){  $_ = $proceed($$)*%s;}else{  $_ = $proceed($$);}", carpentryMultiplier);
            Util.instrumentDeclared(thisClass, ctMethodsStructure, "hasEnoughSkillToExpandStructure", "getKnowledge", replace);
            Util.setReason("Allow players to construct larger houses.");
            Util.instrumentDeclared(thisClass, ctMethodsStructure, "hasEnoughSkillToContractStructure", "getKnowledge", replace);

            Util.setReason("Reduce power of imbues.");
            replace = "$_ = Math.max(-80d, -80d+$2);";
            Util.instrumentDeclared(thisClass, ctMethodsItems, "smear", "max", replace);

            Util.setReason("Update vehicle speeds reliably.");
            replace = "if($1 == 8){  $_ = 0;}else{  $_ = $proceed($$);}";
            Util.instrumentDeclared(thisClass, ctPlayer, "checkVehicleSpeeds", "nextInt", replace);
            /*
            Util.setReason("Reduce mailing costs by 90%.");
            CtClass ctMailSendConfirmQuestion = classPool.get("com.wurmonline.server.questions.MailSendConfirmQuestion");
            replace = "$_ = $_ / 10;";
            Util.insertAfterDeclared(thisClass, ctMailSendConfirmQuestion, "getCostForItem", replace);
            */
            Util.setReason("Remove guard tower guards helping against certain types of enemies.");
            CtClass ctGuardTower = classPool.get("com.wurmonline.server.kingdom.GuardTower");
            replace = String.format("if($0.isUnique() || %s.isTitan($0) || %s.isRareCreature($0)){  $_ = false;}else{  $_ = $proceed($$);}", Titans.class.getName(), MethodsBestiary.class.getName());
            Util.instrumentDeclared(thisClass, ctGuardTower, "alertGuards", "isWithinTileDistanceTo", replace);

            // Enable Strongwall for Libila and other spells on PvE
            //CtClass ctSpellGenerator = classPool.get("com.wurmonline.server.spells.SpellGenerator");
            //ctSpellGenerator.getDeclaredMethod("createSpells").instrument(new ExprEditor() {
            //    @Override
            //    public void edit(FieldAccess fieldAccess) throws CannotCompileException {
            //        if (Objects.equals("PVPSERVER", fieldAccess.getFieldName()))
            //            fieldAccess.replace("$_ = true;");
            //        //Requiem.debug("Instrumented SpellGenerator PVPSERVER field to enable all spells.");
            //    }
            //});

            //Util.setReason("Make heated food never decay if cooked by a royal cook.");
            //CtClass ctTempStates = classPool.get("com.wurmonline.server.items.TempStates");
            //replace = "$_ = $proceed($$);" +
            //        "if(chefMade){" +
            //        "  $0.setName(\"royal \"+$0.getName());" +
            //        "  $0.setHasNoDecay(true);" +
            //        "}";
            //Util.instrumentDeclared(thisClass, ctTempStates, "checkForChange", "setName", replace);

            //Util.setReason("Stop royal food decay.");
            //// Item parent, int parentTemp, boolean insideStructure, boolean deeded, boolean saveLastMaintained, boolean inMagicContainer, boolean inTrashbin
            //CtClass[] params11 = {
            //        ctItem,
            //        CtClass.intType,
            //        CtClass.booleanType,
            //        CtClass.booleanType,
            //        CtClass.booleanType,
            //        CtClass.booleanType,
            //        CtClass.booleanType
            //};
            //String desc11 = Descriptor.ofMethod(CtClass.booleanType, params11);
            //replace = "if($0.isFood() && $0.hasNoDecay()){" +
            //        "  $_ = false;" +
            //        "}else{" +
            //        "  $_ = $proceed($$);" +
            //        "}";
            //Util.instrumentDescribed(thisClass, ctItem, "poll", desc11, "setDamage", replace);

            Util.setReason("Allow mayors to command abandoned vehicles off their deed.");
            replace = String.format("if(%s.checkMayorCommand($0, $1)){  return true;}", MiscChanges.class.getName());
            Util.insertBeforeDeclared(thisClass, ctItem, "mayCommand", replace);

            Util.setReason("Modify timed affinity timer.");
            CtClass ctAffinitiesTimed = classPool.get("com.wurmonline.server.skills.AffinitiesTimed");
            replace = String.format("$_ = %s.getFoodOpulenceBonus($0);", MiscChanges.class.getName());
            Util.instrumentDeclared(thisClass, ctAffinitiesTimed, "addTimedAffinityFromBonus", "getFoodComplexity", replace);

            // TODO needs moved from preInit
            //Util.setReason("Food affinity timer normalization.");
            //replace = String.format("long time = %s.getCurrentTime();if($0.getExpires($1) == null){  $_ = Long.valueOf(time);}else{  $_ = $proceed($$);}", WurmCalendar.class.getName());
            //Util.instrumentDeclared(thisClass, ctAffinitiesTimed, "add", "getExpires", replace);

            Util.setReason("Make bed QL affect sleep bonus timer.");
            CtClass ctPlayerInfo = classPool.get("com.wurmonline.server.players.PlayerInfo");
            replace = String.format("secs = %s.getBedBonus(secs, this.bed);$_ = $proceed($$);", MiscChanges.class.getName());
            Util.instrumentDeclared(thisClass, ctPlayerInfo, "calculateSleep", "setSleep", replace);

            // Fix for body strength not working properly when mounted. (Bdew)
            ctCreature.getMethod("getTraitMovePercent", "(Z)F").instrument(new ExprEditor() {
                private boolean first = true;

                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("getStrengthSkill")) {
                        if (first)
                            m.replace("wmod = wmod * 3D; $_ = $proceed() * (this.isUnicorn()?3D:2D);");
                        else
                            m.replace("$_ = $proceed() * (this.isUnicorn()?3D:2D);");
                        first = false;
                    }
                }
            });


            Util.setReason("Hook for rare material usage in improvement.");
            replace = String.format("if(%s.rollRarityImprove($0, usedWeight)){  rarity = source.getRarity();}$_ = $proceed($$);", MiscChanges.class.getName());
            Util.instrumentDeclared(thisClass, ctMethodsItems, "improveItem", "setWeight", replace);

            Util.setReason("Bad luck protection on rarity windows.");
            replace = String.format("if($1 == 3600){  $_ = %s.getRarityWindowChance(this.getWurmId());}else{  $_ = $proceed($$);}", MiscChanges.class.getName());
            Util.instrumentDeclared(thisClass, ctPlayer, "poll", "nextInt", replace);

            CtClass ctSimpleCreationEntry = classPool.get("com.wurmonline.server.items.SimpleCreationEntry");
            ctSimpleCreationEntry.getDeclaredMethod("run").instrument(new ExprEditor() {
                private boolean first = true;

                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("getRarity")) {
                        if (first) {
                            m.replace(String.format("byte newRarity = %s.getNewCreationRarity(this, source, target, template);if(newRarity > 0){  act.setRarity(newRarity);}$_ = $proceed($$);", MiscChanges.class.getName()));
                            RequiemLogging.debug("Replaced getRarity in SimpleCreationEntry to allow functional rare creations.");
                            first = false;
                        }
                    }
                }
            });

            Util.setReason("Make armour title benefits always occur.");
            replace = "$_ = improve.getNumber();";
            Util.instrumentDeclared(thisClass, ctMethodsItems, "improveItem", "getSkillId", replace);
            Util.instrumentDeclared(thisClass, ctMethodsItems, "polishItem", "getSkillId", replace);

            CtClass ctAbilities = classPool.get("com.wurmonline.server.players.Abilities");

            Util.setReason("Make it so sorcery's can be used anywhere with a flat 3x3 altar.");
            replace = "$_ = 1;";
            Util.instrumentDeclared(thisClass, ctAbilities, "isInProperLocation", "getTemplateId", replace);

/*
            Util.setReason("Make the key of the heavens only usable on PvE");
            replace = "if($1.getTemplateId() == 794 && com.wurmonline.server.Servers.localServer.PVPSERVER){" +
                    "  $2.getCommunicator().sendNormalServerMessage(\"The \"+$1.getName()+\" may not be used on PvP.\");" +
                    "  return false;" +
                    "}";
            Util.insertBeforeDeclared(thisClass, ctAbilities, "isInProperLocation", replace);
*/

            Util.setReason("Disable GM commands from displaying in /help unless the player is a GM.");
            CtClass ctServerTweaksHandler = classPool.get("com.wurmonline.server.ServerTweaksHandler");
            replace = "if($1.getPower() < 1){" +
                    "  return;" +
                    "}";
            Util.insertBeforeDeclared(thisClass, ctServerTweaksHandler, "sendHelp", replace);

            Util.setReason("Make damage less likely to interrupt actions during combat.");
            replace = "$1 = $1/2;";
            Util.insertBeforeDeclared(thisClass, ctCreature, "maybeInterruptAction", replace);

            Util.setReason("Fix mission null pointer exception.");
            CtClass ctEpicServerStatus = classPool.get("com.wurmonline.server.epic.EpicServerStatus");
            replace = "if(itemplates.size() < 1){" +
                    "  com.wurmonline.server.epic.EpicServerStatus.setupMissionItemTemplates();" +
                    "}";
            Util.insertBeforeDeclared(thisClass, ctEpicServerStatus, "getRandomItemTemplateUsed", replace);

            // Show power on vesseled gems
            classPool.getCtClass("com.wurmonline.server.items.DbItem")
                    .getMethod("setData1", "(I)V")
                    .insertBefore("if (this.isGem()) this.setName(this.getTemplate().getName()+\" [\"+$1+\"]\");");

            Util.setReason("Hide buff bar icons for sorceries.");
            CtClass ctSpellEffectsEnum = classPool.get("com.wurmonline.server.creatures.SpellEffectsEnum");
            CtClass ctString = classPool.get("java.lang.String");
            CtClass[] params15 = {
                    ctSpellEffectsEnum,
                    CtClass.intType,
                    ctString
            };
            String desc15 = Descriptor.ofMethod(CtClass.voidType, params15);
            CtClass[] params16 = {
                    ctSpellEffectsEnum,
                    CtClass.intType
            };
            String desc16 = Descriptor.ofMethod(CtClass.voidType, params16);
            replace = String.format("$_ = %s.shouldSendBuff($0);", MiscChanges.class.getName());
            Util.instrumentDescribed(thisClass, ctCommunicator, "sendAddStatusEffect", desc15, "isSendToBuffBar", replace);
            Util.setReason("Hide buff bar icons for sorceries.");
            Util.instrumentDescribed(thisClass, ctCommunicator, "sendAddStatusEffect", desc16, "isSendToBuffBar", replace);

            // 1.9 Achievement fix [Bdew]
            classPool.getCtClass("com.wurmonline.server.players.Achievements").getMethod("loadAllAchievements", "()V")
                    .instrument(new ExprEditor() {
                        @Override
                        public void edit(MethodCall m) throws CannotCompileException {
                            if (m.getMethodName().equals("getTimestamp"))
                                m.replace("$_=com.wurmonline.server.utils.DbUtilities.getTimestampOrNull(rs.getString($1)); " +
                                        "if ($_==null) $_=new java.sql.Timestamp(java.lang.System.currentTimeMillis());");
                        }
                    });

            // Enable Mycelium to spread.
            if (enableMyceliumSpread) {
                CtClass ctTilePoller = classPool.get("com.wurmonline.server.zones.TilePoller");
                replace = "$_ = true;";
                Util.setReason("Fix mycelium spread on Freedom servers.");
                Util.instrumentDeclared(thisClass, ctTilePoller, "checkEffects", "isThisAPvpServer", replace);
                ctTilePoller.getDeclaredMethod("checkForGrassSpread").instrument(new ExprEditor() {
                    @Override
                    public void edit(FieldAccess f) throws CannotCompileException {
                        if ("checkMycel".equals(f.getFieldName())) {
                            f.replace("$_ = true;");
                        }
                    }
                });
                ctTilePoller.getDeclaredMethod("checkForMycelGrowth").instrument(new ExprEditor() {
                    @Override
                    public void edit(FieldAccess f) throws CannotCompileException {
                        if ("checkMycel".equals(f.getFieldName())) {
                            f.replace("$_ = true;");
                        }
                    }
                });
            }
        } catch (CannotCompileException | NotFoundException | IllegalArgumentException | ClassCastException e) {
            throw new HookException(e);
        }
    }

    public static void init() {
        RequiemLogging.logInfo("========= Initializing MiscChanges.init =========");
        try {
            //setUpNpcMovement();

            HookManager hooks = HookManager.getInstance();
            ClassPool pool = hooks.getClassPool();
            if (stfuNpcs) {
                hooks.registerHook("com.wurmonline.server.creatures.ai.ChatManager",
                        "answerLocalChat",
                        "(Lcom/wurmonline/server/Message;Ljava/lang/String;)V",
                        () -> (proxy, method, args) -> {
                            return null;
                        });
                hooks.registerHook("com.wurmonline.server.creatures.ai.ChatManager",
                        "getSayToCreature",
                        "(Lcom/wurmonline/server/creatures/Creature;)Ljava/lang/String;",
                        () -> (proxy, method, args) -> {
                            return null;
                        });
            }

            if (hidePlayerGodInscriptions) {
                hooks.registerHook("com.wurmonline.server.deities.Deities",
                        "getRandomNonHateDeity",
                        "()Lcom/wurmonline/server/deities/Deity;",
                        () -> (proxy, method, args) -> {
                            return null;
                        });
            }

            if (gmFullFavor) {
                hooks.registerHook("com.wurmonline.server.players.Player",
                        "depleteFavor",
                        "(FZ)V",
                        () -> (proxy, method, args) -> {
                            if (proxy instanceof Player) {
                                Player player = (Player) proxy;
                                if (player.getPower() >= MiscConstants.POWER_IMPLEMENTOR) {
                                    return null;
                                }
                            }
                            return method.invoke(proxy, args);
                        });
            }
            hooks.registerHook("com.wurmonline.server.players.Player",
                    "increaseAffinity",
                    "(II)V",
                    () -> (proxy, method, args) -> {
                        RequiemLogging.logInfo(String.format("incAff: %d %d", args[0], args[1]));
                        return method.invoke(proxy, args);
                    });

            if (loadFullContainers) {
                hooks.registerHook("com.wurmonline.server.behaviours.CargoTransportationMethods",
                        "targetIsNotEmptyContainerCheck",
                        "(Lcom/wurmonline/server/items/Item;Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;Z)Z",
                        () -> (proxy, method, args) -> {
                            return false;
                        });
            }

            if (noMineDrift) {
                hooks.registerHook("com.wurmonline.server.behaviours.TileRockBehaviour",
                        "getFloorAndCeiling",
                        "(IIIIZZLcom/wurmonline/server/creatures/Creature;)[I",
                        () -> (proxy, method, args) -> {
                            int baseFloor = (int) args[2] + (int) args[3];
                            int[] ret = (int[]) method.invoke(proxy, args);
                            RequiemLogging.logInfo(String.format("getFloorAndCeiling: %d %d", baseFloor, ret[0]));
                            if (ret[0] > baseFloor && ret[0] - baseFloor <= 3) {
                                ret[0] = baseFloor;
                            }
                            if (ret[0] < baseFloor && baseFloor - ret[0] <= 3) {
                                ret[0] = baseFloor;
                            }
                            return ret;
                        });
            }

            if (allowTentsOnDeed) {
                hooks.registerHook("com.wurmonline.server.behaviours.MethodsItems",
                        "mayDropTentOnTile",
                        "(Lcom/wurmonline/server/creatures/Creature;)Z",
                        () -> (proxy, method, args) -> {
                            return true;
                        });
            }

            if (allSurfaceMine) {
                MiscHooks.hookSurfaceMine(pool);
            }

            /* Hook Item Creation */
            for (String desc : createItemDescs) {
                RequiemLogging.logInfo("createItem: " + desc);
                hooks.registerHook("com.wurmonline.server.items.ItemFactory", "createItem", desc,
                        () -> (proxy, method, args) -> {
                            Object retn = method.invoke(proxy, args);
                            if (retn != null) {
                                Item item = (Item) retn;
                                ItemMod.modifyOnCreate(item);
                            }
                            return retn;
                        });
            }

            if (lampsAutoLight) {
                hooks.registerHook("com.wurmonline.server.items.Item", "refuelLampFromClosestVillage", "()V",
                        () -> (proxy, method, args) -> {
                            Item lamp = (Item) proxy;
                            lamp.setAuxData((byte) 127);
                            return null;
                        });
            }

            hooks.registerHook("com.wurmonline.server.structures.Structure",
                    "isEnemyAllowed",
                    "(Lcom/wurmonline/server/creatures/Creature;S)Z",
                    () -> (proxy, method, args) -> {
                        short act = (short) args[1];
                        if (act >= Actions.actionEntrys.length) {
                            return false;
                        }
                        return method.invoke(proxy, args);
                    });
/*
        if ( gmFullStamina ) {
                CtClass ex = HookManager.getInstance().getClassPool().get("com.wurmonline.server.creatures.CreatureStatus");
                CtClass[] parameters = new CtClass[]{CtPrimitiveType.floatType};
                CtMethod method = ex.getMethod("modifyStamina", Descriptor.ofMethod(CtPrimitiveType.voidType, parameters));
                method.insertBefore("if ($1 < 0f) {\n" +
                        "            $1 = $1 * -1f;\n" +
                        "        }");
                method.insertBefore("{ if (this.getPower() >= MiscConstants.POWER_IMPLEMENTOR) return true; }");
        }

        if ( gmFullStamina ) {
            hooks.registerHook("com.wurmonline.server.creatures.CreatureStatus",
                               "modifyStamina2",
                               "(F)V",
                               () -> (proxy, method, args) -> {

                CreatureStatus status = (CreatureStatus) proxy;

                if ( status.statusHolder.getPower() >= MiscConstants.POWER_IMPLEMENTOR ) {
                    args[0] = 100f;
                }

                return method.invoke(proxy,args);

            });
        }
*/
        } catch (IllegalArgumentException | ClassCastException e) {
            throw new HookException(e);
        }
    }

    /*
    public static boolean startNpcMoveHook(final Creature npc) {
        return npc.getName().equals("Traveling Salesman");
    }

    private static void setUpNpcMovement() {
        final String descriptor = Descriptor.ofMethod(CtPrimitiveType.voidType, new CtClass[] { CtPrimitiveType.intType });
        HookManager.getInstance().registerHook("com.wurmonline.server.creatures.Creature", "startPathing", descriptor, new InvocationHandlerFactory() {
            public InvocationHandler createInvocationHandler() {
                return new InvocationHandler() {
                    @Override
                    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
                        if (logExecutionCost) {
                            tmpExecutionStartTime = System.nanoTime();
                        }
                        if (((Creature)proxy).isNpc() && startNpcMoveHook((Creature)proxy)) {
                            return true;
                        }
                        if (logExecutionCost) {
                            RequiemLogging.logInfo("setUpNpcMovementPrevention[hook] done, spent " + executionLogDf.format((System.nanoTime() - tmpExecutionStartTime) / 1.0E9) + "s");
                        }
                        final Object result = method.invoke(proxy, args);
                        RequiemLogging.logInfo("setUpNpcMovement return = " + result);
                        return result;
                    }
                };
            }
        });
    }

     */

    /*
    // Use this to spawn creatures in specific zones
    private static void spawnHeadVampire() {
        try {
            final CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(Creatures.vampireId);
            final int xPos = VampZones.getCovenCentre().getX() + 1;
            final int yPos = VampZones.getCovenCentre().getY() + 1;
            if (!Zones.isGoodTileForSpawn(xPos, yPos, VampZones.getCovenLayer() != -1)) {
                Creatures.logger.log(Level.SEVERE, "Could not spawn Orlok, designated tile is not suitable for spawning: " + xPos + ", " + yPos);
                return;
            }
            final Creature newCreature = Creature.doNew(template.getTemplateId(), true, xPos * 4.0f, yPos * 4.0f, 0.0f, VampZones.getCovenLayer(), "Orlok", (byte)0, (byte)4, (byte)0, false, (byte)3);
            newCreature.shouldStandStill = true;
        }
        catch (Exception e) {
            Creatures.logger.log(Level.SEVERE, "Could not spawn Orlok", e);
        }
    }
     */

}
