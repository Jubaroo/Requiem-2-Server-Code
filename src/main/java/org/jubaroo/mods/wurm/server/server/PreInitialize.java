package org.jubaroo.mods.wurm.server.server;

import com.wurmonline.server.items.ItemList;
import javassist.*;
import javassist.bytecode.Descriptor;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;
import mod.sin.lib.Util;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.gotti.wurmunlimited.modsupport.vehicles.ModVehicleBehaviours;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.communication.discord.ChatHandler;
import org.jubaroo.mods.wurm.server.communication.discord.TicketHandler;
import org.jubaroo.mods.wurm.server.creatures.CreatureTweaks;
import org.jubaroo.mods.wurm.server.creatures.CustomMountSettings;
import org.jubaroo.mods.wurm.server.creatures.Titans;
import org.jubaroo.mods.wurm.server.items.ItemMod;
import org.jubaroo.mods.wurm.server.items.behaviours.AthanorMechanismBehaviour;
import org.jubaroo.mods.wurm.server.misc.Misc;
import org.jubaroo.mods.wurm.server.misc.MiscChanges;
import org.jubaroo.mods.wurm.server.misc.QualityOfLife;
import org.jubaroo.mods.wurm.server.tools.CreatureTools;
import org.jubaroo.mods.wurm.server.tools.Hooks;
import org.jubaroo.mods.wurm.server.tools.database.DatabaseHelper;
import org.jubaroo.mods.wurm.server.utils.MissionCreator;

import java.lang.reflect.Modifier;
import java.util.Objects;

import static org.jubaroo.mods.wurm.server.ModConfig.*;

public class PreInitialize {

    public static void preInit() {
        try {
            ModActions.init();
            ModVehicleBehaviours.init();
            ClassPool classPool = HookManager.getInstance().getClassPool();
            CtClass ctClass = classPool.getCtClass("com.wurmonline.server.items.ItemFactory");
            CtMethod ctGuardPlan = classPool.getCtClass("com.wurmonline.server.villages.GuardPlan").getMethod("pollGuards", "()V");
            CtClass ctGuardTower = classPool.getCtClass("com.wurmonline.server.kingdom.GuardTower");
            CtClass ctPlayers = classPool.getCtClass("com.wurmonline.server.Players");
            CtClass ctCreature = classPool.getCtClass("com.wurmonline.server.creatures.Creature");
            CtClass ctItem = classPool.getCtClass("com.wurmonline.server.items.Item");
            CtClass ctTerraforming = classPool.getCtClass("com.wurmonline.server.behaviours.Terraforming");
            CtClass ctVillage = classPool.getCtClass("com.wurmonline.server.villages.Village");
            CtClass ctHota = classPool.getCtClass("com.wurmonline.server.epic.Hota");
            CtClass ctMeshIO = classPool.getCtClass("com.wurmonline.mesh.MeshIO");
            CtClass ctCommunicator = classPool.getCtClass("com.wurmonline.server.creatures.Communicator");
            CtClass ctTriggerEffect = classPool.getCtClass("com.wurmonline.server.tutorial.TriggerEffect");
            CtClass ctEpicServerStatus = classPool.getCtClass("com.wurmonline.server.epic.EpicServerStatus");
            CtClass ctEpicMissionEnum = classPool.getCtClass("com.wurmonline.server.epic.EpicMissionEnum");
            CtClass ctMethodsItems = classPool.getCtClass("com.wurmonline.server.behaviours.MethodsItems");
            CtClass ctAction = classPool.getCtClass("com.wurmonline.server.behaviours.Action");
            CtClass ctCombatHandler = classPool.getCtClass("com.wurmonline.server.creatures.CombatHandler");
            CtClass ctPortal = classPool.getCtClass("com.wurmonline.server.questions.PortalQuestion");
            CtClass ctArchery = classPool.getCtClass("com.wurmonline.server.combat.Archery");
            CtClass ctArrows = classPool.getCtClass("com.wurmonline.server.combat.Arrows");
            CtClass ctIntraServerConnection = classPool.getCtClass("com.wurmonline.server.intra.IntraServerConnection");
            CtClass ctSkill = classPool.getCtClass("com.wurmonline.server.skills.Skill");
            CtClass ctTradeHandler = classPool.getCtClass("com.wurmonline.server.creatures.TradeHandler");
            CtClass ctTileBehaviour = classPool.getCtClass("com.wurmonline.server.behaviours.TileBehaviour");
            CtClass ctMethodsStructure = classPool.getCtClass("com.wurmonline.server.behaviours.MethodsStructure");
            CtClass ctAffinitiesTimed = classPool.getCtClass("com.wurmonline.server.skills.AffinitiesTimed");
            CtClass ctPlayerInfo = classPool.getCtClass("com.wurmonline.server.players.PlayerInfo");
            CtClass ctSimpleCreationEntry = classPool.getCtClass("com.wurmonline.server.items.SimpleCreationEntry");
            CtClass ctAbilities = classPool.getCtClass("com.wurmonline.server.players.Abilities");
            CtClass ctServerTweaksHandler = classPool.getCtClass("com.wurmonline.server.ServerTweaksHandler");
            CtClass ctSpellEffectsEnum = classPool.getCtClass("com.wurmonline.server.creatures.SpellEffectsEnum");
            CtClass ctString = classPool.getCtClass("java.lang.String");
            CtClass ctTilePoller = classPool.getCtClass("com.wurmonline.server.zones.TilePoller");
            CtClass ctWound = classPool.getCtClass("com.wurmonline.server.bodys.Wound");
            CtClass ctMethodsReligion = classPool.getCtClass("com.wurmonline.server.behaviours.MethodsReligion");
            CtClass ctPathFinder = classPool.getCtClass("com.wurmonline.server.creatures.ai.PathFinder");
            CtClass ctMethodsCreatures = classPool.getCtClass("com.wurmonline.server.behaviours.MethodsCreatures");
            CtClass ctVirtualZone = classPool.getCtClass("com.wurmonline.server.zones.VirtualZone");
            CtClass ctVehicle = classPool.getCtClass("com.wurmonline.server.behaviours.Vehicle");
            CtClass ctWormBrains = classPool.getCtClass("com.wurmonline.server.spells.WormBrains");
            CtClass ctCaveWallBehaviour = classPool.getCtClass("com.wurmonline.server.behaviours.CaveWallBehaviour");
            CtClass ctTileRockBehaviour = classPool.getCtClass("com.wurmonline.server.behaviours.TileRockBehaviour");
            CtClass ctFishing = classPool.getCtClass("com.wurmonline.server.behaviours.MethodsFishing");
            CtClass ctPlayer = classPool.getCtClass("com.wurmonline.server.players.Player");
            CtClass ctLoginHandler = classPool.getCtClass("com.wurmonline.server.LoginHandler");
            CtClass CtServer = classPool.getCtClass("com.wurmonline.server.Server");

            if (mailboxEnableEnchant) {
                try {
                    // Places a 30 power courier enchantment on newly created mailboxes.
                    CtClass[] parameters = new CtClass[]{
                            CtPrimitiveType.intType,
                            CtPrimitiveType.floatType,
                            CtPrimitiveType.byteType,
                            CtPrimitiveType.byteType,
                            CtPrimitiveType.longType,
                            HookManager.getInstance().getClassPool().get("java.lang.String")
                    };
                    CtMethod ctMethod = ctClass.getMethod("createItem", Descriptor.ofMethod(HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.Item"), parameters));
                    ctMethod.instrument(new ExprEditor() {
                        @Override
                        public void edit(NewExpr newExpr) throws CannotCompileException {
                            if (newExpr.getClassName().equals("com.wurmonline.server.items.DbItem")) {
                                ctMethod.insertAt(newExpr.getLineNumber() + 1,
                                        String.format("{ if (templateId >= %d && templateId <= %d) {com.wurmonline.server.items.ItemSpellEffects effs;if ((effs = toReturn.getSpellEffects()) == null) effs = new com.wurmonline.server.items.ItemSpellEffects(toReturn.getWurmId());toReturn.getSpellEffects().addSpellEffect(new com.wurmonline.server.spells.SpellEffect(toReturn.getWurmId(), (byte)20, %sf, 20000000));toReturn.permissions.setPermissionBit(com.wurmonline.server.players.Permissions.Allow.HAS_COURIER.getBit(), true);} }", ItemList.mailboxWood, ItemList.mailboxStoneTwo, mailboxEnchantPower));
                            }
                        }
                    });
                } catch (NotFoundException | CannotCompileException ex) {
                    RequiemLogging.logException("Error in EnableEnchant - ", ex);
                }
            }

            try {
                // Discord Stuff
                ctPlayers.getMethod("sendGlobalKingdomMessage", "(Lcom/wurmonline/server/creatures/Creature;JLjava/lang/String;Ljava/lang/String;ZBIII)V")
                        .insertBefore(String.format(" if (kingdom < 0) {%s.sendMessage($$); return;}", ChatHandler.class.getName()));

                ctPlayers.getMethod("sendStartKingdomChat", "(Lcom/wurmonline/server/players/Player;)V").setBody("return;");
                ctPlayers.getMethod("sendStartGlobalKingdomChat", "(Lcom/wurmonline/server/players/Player;)V").setBody("return;");

                ctLoginHandler.getMethod("sendLoggedInPeople", "(Lcom/wurmonline/server/players/Player;)V")
                        .insertBefore(String.format("%s.sendBanner(player);", ChatHandler.class.getName()));


                ctPlayer.getMethod("isKingdomChat", "()Z").setBody("return false;");
                ctPlayer.getMethod("isGlobalChat", "()Z").setBody("return false;");
                ctPlayer.getMethod("seesPlayerAssistantWindow", "()Z").setBody("return false;");

                CtServer.getMethod("shutDown", "()V").insertBefore(String.format("%s.serverStopped();", ChatHandler.class.getName()));
                CtServer.getMethod("broadCastNormal", "(Ljava/lang/String;Z)V").insertBefore(String.format("%s.handleBroadcast($1);", ChatHandler.class.getName()));
                CtServer.getMethod("broadCastSafe", "(Ljava/lang/String;ZB)V").insertBefore(String.format("%s.handleBroadcast($1);", ChatHandler.class.getName()));
                CtServer.getMethod("broadCastAlert", "(Ljava/lang/String;ZB)V").insertBefore(String.format("%s.handleBroadcast($1);", ChatHandler.class.getName()));

                classPool.getCtClass("com.wurmonline.server.ServerEntry").getMethod("setAvailable", "(ZZIIII)V").insertBefore(String.format("if (this.isAvailable != $1) %s.serverAvailable(this, $1);", ChatHandler.class.getName()));
                classPool.getCtClass("com.wurmonline.server.support.Tickets").getMethod("addTicket", "(Lcom/wurmonline/server/support/Ticket;Z)Lcom/wurmonline/server/support/Ticket;").insertBefore(String.format("%s.updateTicket($1);", TicketHandler.class.getName()));
                classPool.getCtClass("com.wurmonline.server.support.Ticket").getMethod("addTicketAction", "(Lcom/wurmonline/server/support/TicketAction;)V").insertBefore(String.format("%s.addTicketAction(this, $1);", TicketHandler.class.getName()));
            } catch (Exception e) {
                RequiemLogging.logException("error initializing Discord relay", e.getCause());
            }

            final Class<CustomMountSettings> thisClass = CustomMountSettings.class;
            String replace;

            Util.setReason("Scaling horse speed.");
            replace = String.format("{ return %s.newMountSpeedMultiplier(this, $1); }", CustomMountSettings.class.getName());
            Util.setBodyDeclared(thisClass, ctCreature, "getMountSpeedPercent", replace);

            Util.setReason("Force mount speed change check on damage.");
            replace = "forceMountSpeedChange();";
            Util.insertBeforeDeclared(thisClass, ctCreature, "setWounded", replace);

            Util.setReason("Change HotA reward");
            replace = String.format("%s.createNewHotaPrize(this, $1);", Misc.class.getName());
            Util.setBodyDeclared(thisClass, ctVillage, "createHotaPrize", replace);

            Util.setReason("Display discord message for HotA announcements.");
            replace = String.format("%s.sendHotaMessage($1);$_ = $proceed($$);", Misc.class.getName());
            Util.instrumentDeclared(thisClass, ctHota, "poll", "broadCastSafe", replace);

            Util.setReason("Display discord message for HotA wins.");
            Util.instrumentDeclared(thisClass, ctHota, "win", "broadCastSafe", replace);

            Util.setReason("Display discord message for HotA conquers & neutralizes.");
            replace = String.format("if($2.getData1() == 0){  %s.sendHotaMessage($1.getName() + \" neutralizes the \" + $2.getName() + \".\");}else{  %s.sendHotaMessage($1.getName() + \" conquers the \" + $2.getName() + \".\");}", Misc.class.getName(), Misc.class.getName());
            Util.insertBeforeDeclared(thisClass, ctHota, "addPillarConquered", replace);

            CtClass[] params7 = new CtClass[]{
                    ctCreature,
                    ctItem,
                    CtClass.intType,
                    CtClass.intType,
                    CtClass.intType,
                    CtClass.floatType,
                    CtClass.booleanType,
                    ctMeshIO,
                    CtClass.booleanType
            };
            String desc7 = Descriptor.ofMethod(CtClass.booleanType, params7);
            Util.setReason("Announce digging up artifacts on Discord.");
            replace = String.format("%s.sendHotaMessage($1+\" \"+$2);$_ = $proceed($$);", Misc.class.getName());
            Util.instrumentDescribed(thisClass, ctTerraforming, "dig", desc7, "addHistory", replace);

            // Show custom slash commands in /help readout
            Util.setReason("Show Custom Slash Commands In /help Readout");
            CtMethod ctSendHelp = ctCommunicator.getDeclaredMethod("sendHelp");
            ctSendHelp.insertAfter("this.sendHelpMessage(\"/roll - randomly rolls a pair of dice and displays it in the chat window.\");");
            ctSendHelp.insertAfter("this.sendHelpMessage(\"/gps or /location or /coordinates - tells you your location on the server.\");");
            ctSendHelp.insertAfter("this.sendHelpMessage(\"/stime or /servertime - tells you the current server time and date.\");");

            RequiemLogging.logInfo("Trying to make pollGuards() methods public.");
            try {
                ctGuardTower.setModifiers((ctGuardTower.getModifiers() & ~java.lang.reflect.Modifier.PRIVATE) | java.lang.reflect.Modifier.PUBLIC);
                ctGuardPlan.setModifiers((ctGuardTower.getModifiers() & ~java.lang.reflect.Modifier.PRIVATE) | Modifier.PUBLIC);
                RequiemLogging.logInfo("pollGuards() methods are now public.");
            } catch (Exception e) {
                RequiemLogging.logException("Couldn't make pollGuards() methods public.", e);
            }

            if (disableMissionChanges) {
                try {
                    Util.setReason("Give players currency for completing a mission.");
                    replace = String.format("$_ = $proceed($$);%s.awardMissionBonus($0);", MissionCreator.class.getName());
                    Util.instrumentDeclared(thisClass, ctTriggerEffect, "effect", "addToSleep", replace);

                    Util.setReason("Prevent mission creatures from spawning in water.");
                    replace = "$_ = false;";
                    Util.instrumentDeclared(thisClass, ctEpicServerStatus, "spawnSingleCreature", "isSwimming", replace);

                    Util.setReason("Modify which templates are allowed to spawn on herbivore-only epic missions.");
                    replace = String.format("$_ = %s.isMissionOkayHerbivore($0);", MissionCreator.class.getName());
                    Util.instrumentDeclared(thisClass, ctEpicServerStatus, "createSlayCreatureMission", "isHerbivore", replace);
                    Util.instrumentDeclared(thisClass, ctEpicServerStatus, "createSlayTraitorMission", "isHerbivore", replace);
                    Util.instrumentDeclared(thisClass, ctEpicServerStatus, "createSacrificeCreatureMission", "isHerbivore", replace);

                    Util.setReason("Modify which templates are allowed to spawn on slay missions.");
                    replace = String.format("$_ = %s.isMissionOkaySlayable($0);", MissionCreator.class.getName());
                    Util.instrumentDeclared(thisClass, ctEpicServerStatus, "createSlayCreatureMission", "isEpicMissionSlayable", replace);
                    Util.instrumentDeclared(thisClass, ctEpicServerStatus, "createSacrificeCreatureMission", "isEpicMissionSlayable", replace);

                    Util.setReason("Adjust which epic missions are available..");
                    replace = "{ if($0.getMissionType() == 108 || $0.getMissionType() == 120 || $0.getMissionType() == 124){  return 0;}return $0.missionChance; }";
                    Util.setBodyDeclared(thisClass, ctEpicMissionEnum, "getMissionChance", replace);
                } catch (IllegalArgumentException | ClassCastException e) {
                    throw new HookException(e);
                }
            }

            try {
                // - Disable mailboxes from being used while loaded - //
                replace = "$_ = $proceed($$);"
                        + "com.wurmonline.server.items.Item theTarget = com.wurmonline.server.Items.getItem(targetId);"
                        + "if(theTarget != null && theTarget.getTemplateId() >= 510 && theTarget.getTemplateId() <= 513){"
                        + "  if(theTarget.getTopParent() != theTarget.getWurmId()){"
                        + "    mover.getCommunicator().sendNormalServerMessage(\"Mailboxes cannot be used while loaded.\");"
                        + "    return false;"
                        + "  }"
                        + "}";
                Util.instrumentDeclared(thisClass, ctItem, "moveToItem", "getOwnerId", replace);

                // - Make leather not suck even after it's able to be combined. - //
                replace = String.format("if(com.wurmonline.server.behaviours.MethodsItems.getImproveTemplateId(target) != %d){  $_ = $proceed($$);}else{  $_ = false;}", ItemList.leather);
                Util.instrumentDeclared(thisClass, ctMethodsItems, "improveItem", "isCombine", replace);

                // - Check new improve materials - //
                replace = String.format("int temp = %s.getModdedImproveTemplateId($1);if(temp != -10){  return temp;}", ItemMod.class.getName());
                Util.insertBeforeDeclared(thisClass, ctMethodsItems, "getImproveTemplateId", replace);

                // - Remove fatiguing actions requiring you to be on the ground - //
                CtConstructor[] ctActionConstructors = ctAction.getConstructors();
                for (CtConstructor constructor : ctActionConstructors) {
                    constructor.instrument(new ExprEditor() {
                        public void edit(MethodCall m) throws CannotCompileException {
                            if (m.getMethodName().equals("isFatigue")) {
                                m.replace("if(com.wurmonline.server.Servers.localServer.PVPSERVER){  if(!com.wurmonline.server.behaviours.Actions.isActionDestroy(this.getNumber())){    $_ = false;  }else{    $_ = $proceed($$);  }}else{  $_ = false;}");
                                //RequiemLogging.logInfo("Set isFatigue to false in action constructor.");
                            }
                        }
                    });
                }

                Util.setReason("Fix Portal Issues.");
                Util.instrumentDeclared(thisClass, ctPortal, "sendQuestion", "willLeaveServer", "$_ = true;");

                Util.setReason("Fix Portal Issues.");
                Util.instrumentDeclared(thisClass, ctPortal, "sendQuestion", "getKnowledge", "$_ = true;");

                Util.setReason("Disable the minimum 0.01 damage on shield damage, allowing damage modifiers to rule.");
                replace = "if($1 < 0.5f){  $_ = $proceed((float) 0, (float) $2);}else{  $_ = $proceed($$);}";
                Util.instrumentDeclared(thisClass, ctCombatHandler, "checkShield", "max", replace);

                // - Allow GM's to bypass the 5 second emote sound limit. - //
                replace = "if(this.getPower() > 0){  return true;}";
                Util.insertBeforeDeclared(thisClass, ctPlayer, "mayEmote", replace);

                // - Make creatures wander slightly if they are shot from afar by an arrow - //
                replace = "if(!defender.isPathing()){  defender.startPathing(com.wurmonline.server.Server.rand.nextInt(100));}$_ = $proceed($$);";
                Util.instrumentDeclared(thisClass, ctArrows, "addToHitCreature", "addAttacker", replace);

                Util.setReason("Attempt to prevent libila from losing faith when crossing servers.");
                ctIntraServerConnection.getDeclaredMethod("savePlayerToDisk").instrument(new ExprEditor() {
                    @Override
                    public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                        if (Objects.equals("PVPSERVER", fieldAccess.getFieldName())) {
                            fieldAccess.replace("$_ = false;");
                            RequiemLogging.logInfo("Instrumented PVPSERVER = false for Libila faith transfers.");
                        }
                    }
                });
                ctIntraServerConnection.getDeclaredMethod("savePlayerToDisk").instrument(new ExprEditor() {
                    @Override
                    public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                        if (Objects.equals("HOMESERVER", fieldAccess.getFieldName())) {
                            fieldAccess.replace("$_ = false;");
                            RequiemLogging.logInfo("Instrumented HOMESERVER = false for Libila faith transfers.");
                        }
                    }
                });
                Util.setReason("Increase food affinity to give 30% increased skillgain instead of 10%.");
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
                ctTradeHandler.getDeclaredMethod("addItemsToTrade").instrument(new ExprEditor() {
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getMethodName().equals("size") && m.getLineNumber() > 200) { // I don't think the line number check matters, but I'm leaving it here anyway.
                            m.replace("$_ = 1;");
                            RequiemLogging.logInfo("Instrumented size for trades to allow traders to show more than 9 items at a time.");
                        }
                    }
                });

                // -- Identify players making over 10 commands per second and causing the server log message -- //
                Util.setReason("Log excessive actions per second.");
                replace = "$_ = $proceed($$);if(this.player != null){  logInfo(\"Potential player macro: \"+this.player.getName()+\" [\"+this.commandsThisSecond+\" commands]\");}";
                Util.instrumentDeclared(thisClass, ctCommunicator, "reallyHandle_CMD_ITEM_CREATION_LIST", "log", replace);

                Util.setReason("Reduce chance of lockpicks breaking.");
                replace = "$_ = 40f + $proceed($$);";
                Util.instrumentDeclared(thisClass, ctMethodsItems, "checkLockpickBreakage", "getCurrentQualityLevel", replace);

                // Allow Freedom players to absorb mycelium
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

                Util.setReason("Remove guard tower guards helping against certain types of enemies.");
                replace = String.format("if($0.isUnique() || %s.isTitan($0) || %s.isRareCreature($0)){  $_ = false;}else{  $_ = $proceed($$);}", Titans.class.getName(), CreatureTweaks.class.getName());
                Util.instrumentDeclared(thisClass, ctGuardTower, "alertGuards", "isWithinTileDistanceTo", replace);

                Util.setReason("Allow mayors to command abandoned vehicles off their deed.");
                replace = String.format("if(%s.checkMayorCommand($0, $1)){  return true;}", MiscChanges.class.getName());
                Util.insertBeforeDeclared(thisClass, ctItem, "mayCommand", replace);

                Util.setReason("Modify timed affinity timer.");
                replace = String.format("$_ = %s.getFoodOpulenceBonus($0);", MiscChanges.class.getName());
                Util.instrumentDeclared(thisClass, ctAffinitiesTimed, "addTimedAffinityFromBonus", "getFoodComplexity", replace);

                Util.setReason("Make bed QL affect sleep bonus timer.");
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

                ctSimpleCreationEntry.getDeclaredMethod("run").instrument(new ExprEditor() {
                    private boolean first = true;

                    @Override
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getMethodName().equals("getRarity")) {
                            if (first) {
                                m.replace(String.format("byte newRarity = %s.getNewCreationRarity(this, source, target, template);if(newRarity > 0){  act.setRarity(newRarity);}$_ = $proceed($$);", MiscChanges.class.getName()));
                                RequiemLogging.logInfo("Replaced getRarity in SimpleCreationEntry to allow functional rare creations.");
                                first = false;
                            }
                        }
                    }
                });

                Util.setReason("Make armour title benefits always occur.");
                replace = "$_ = improve.getNumber();";
                Util.instrumentDeclared(thisClass, ctMethodsItems, "improveItem", "getSkillId", replace);
                Util.instrumentDeclared(thisClass, ctMethodsItems, "polishItem", "getSkillId", replace);


                Util.setReason("Make it so sorcery's can be used anywhere with a flat 3x3 altar.");
                replace = "$_ = 1;";
                Util.instrumentDeclared(thisClass, ctAbilities, "isInProperLocation", "getTemplateId", replace);

                Util.setReason("Disable GM commands from displaying in /help unless the player is a GM.");
                replace = "if($1.getPower() < 1){" +
                        "  return;" +
                        "}";
                Util.insertBeforeDeclared(thisClass, ctServerTweaksHandler, "sendHelp", replace);

                Util.setReason("Make damage less likely to interrupt actions during combat.");
                replace = "$1 = $1/2;";
                Util.insertBeforeDeclared(thisClass, ctCreature, "maybeInterruptAction", replace);

                Util.setReason("Fix mission null pointer exception.");
                replace = "if(itemplates.size() < 1){" +
                        "  com.wurmonline.server.epic.EpicServerStatus.setupMissionItemTemplates();" +
                        "}";
                Util.insertBeforeDeclared(thisClass, ctEpicServerStatus, "getRandomItemTemplateUsed", replace);

                // Show power on vesseled gems
                classPool.getCtClass("com.wurmonline.server.items.DbItem")
                        .getMethod("setData1", "(I)V")
                        .insertBefore("if (this.isGem()) this.setName(this.getTemplate().getName()+\" [\"+$1+\"]\");");

                Util.setReason("Hide buff bar icons for sorceries.");
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

            try {
                Util.setReason("Disable natural regeneration on titans.");
                replace = String.format("if(!%s.isTitan(this.creature)){  $_ = $proceed($$);}", Titans.class.getName());
                Util.instrumentDeclared(thisClass, ctWound, "poll", "modifySeverity", replace);
                Util.setReason("Disable natural regeneration on titans.");
                Util.instrumentDeclared(thisClass, ctWound, "poll", "checkInfection", replace);
                Util.setReason("Disable natural regeneration on titans.");
                Util.instrumentDeclared(thisClass, ctWound, "poll", "checkPoison", replace);

                try {
                    Util.setReason("Disable sacrificing strong creatures.");
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
                } catch (IllegalArgumentException | ClassCastException e) {
                    throw new HookException(e);
                }

                Util.setReason("Disable afk training.");
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
                CtClass[] params5 = new CtClass[]{
                        CtClass.booleanType,
                        ctString,
                        CtClass.booleanType
                };
                String desc5 = Descriptor.ofMethod(CtClass.voidType, params5);

                Util.setReason("Deny chargers walking through walls.");
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
                replace = "$_ = false;";
                Util.instrumentDeclared(thisClass, ctMethodsCreatures, "breed", "isGhost", replace);

                Util.setReason("Allow ghost creatures to drop corpses.");
                replace = String.format("if(%s.isGhostCorpse(this)){  $_ = false;}else{  $_ = $proceed($$);}", Hooks.class.getName());
                Util.instrumentDescribed(thisClass, ctCreature, "die", desc5, "isGhost", replace);

                Util.setReason("Attach special effects to creatures.");
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
                replace = String.format("if(%s.isNotHitchable($1)){  return false;}", Hooks.class.getName());
                Util.insertBeforeDeclared(thisClass, ctVehicle, "addDragger", replace);
            } catch (CannotCompileException | NotFoundException | IllegalArgumentException | ClassCastException e) {
                throw new HookException(e);
            }

            Util.setReason("Disable casting Worm Brains on rares.");
            replace = String.format("if(%s.isRareCreature($3)){  $2.getCommunicator().sendNormalServerMessage(\"This creature is immune to Worm Brains.\");  return false;}", CreatureTweaks.class.getName());
            Util.insertBeforeDeclared(thisClass, ctWormBrains, "precondition", replace);

            try {
                Util.setReason("Allow players to mine directly into vehicles.");
                CtClass[] params1 = {
                        ctAction,
                        ctCreature,
                        ctItem,
                        CtClass.intType,
                        CtClass.intType,
                        CtClass.booleanType,
                        CtClass.intType,
                        CtClass.intType,
                        CtClass.intType,
                        CtClass.shortType,
                        CtClass.floatType
                };
                String desc1 = Descriptor.ofMethod(CtClass.booleanType, params1);
                replace = String.format("$_ = null;%s.vehicleHook(performer, $0);", QualityOfLife.class.getName());
                Util.instrumentDescribed(thisClass, ctCaveWallBehaviour, "action", desc1, "putItemInfrontof", replace);

                Util.setReason("Allow players to surface mine directly into vehicles.");
                replace = String.format("$_ = $proceed($$);%s.vehicleHook(performer, $0);", QualityOfLife.class.getName());
                Util.instrumentDeclared(thisClass, ctTileRockBehaviour, "mine", "setDataXY", replace);

                Util.setReason("Allow players to chop logs directly into vehicles.");
                replace = String.format("$_ = null;%s.vehicleHook(performer, $0);", QualityOfLife.class.getName());
                Util.instrumentDeclared(thisClass, ctMethodsItems, "chop", "putItemInfrontof", replace);

                Util.setReason("Send gems, source crystals, flint, etc. into vehicle.");
                CtClass[] params2 = {
                        CtClass.intType,
                        CtClass.intType,
                        CtClass.intType,
                        CtClass.intType,
                        ctCreature,
                        CtClass.doubleType,
                        CtClass.booleanType,
                        ctAction
                };
                String desc2 = Descriptor.ofMethod(ctItem, params2);
                replace = String.format("$_ = null;%s.vehicleHook(performer, $0);", QualityOfLife.class.getName());
                Util.instrumentDescribed(thisClass, ctTileRockBehaviour, "createGem", desc2, "putItemInfrontof", replace);

                // Regen stamina in vehicle on any slope
                ctPlayer.getMethod("poll", "()Z").instrument(new ExprEditor() {
                    @Override
                    public void edit(FieldAccess f) throws CannotCompileException {
                        if (f.getFieldName().equals("vehicle") && f.isReader())
                            f.replace("$_ = -10L;");
                    }
                });

            } catch (NotFoundException | IllegalArgumentException | ClassCastException e) {
                throw new HookException(e);
            } catch (CannotCompileException e) {
                e.printStackTrace();
            }

            if (!disableDatabaseChanges) {
                try {
                    DatabaseHelper.createRequiemDatabase("E:\\SteamLibrary\\steamapps\\common\\Wurm Unlimited Dedicated Server\\Cragmoor Isles", "Requiem.db");
                    DatabaseHelper.createRequiemDatabase("E:\\SteamLibrary\\steamapps\\common\\Wurm Unlimited Dedicated Server\\The Wilds", "Requiem.db");
                    DatabaseHelper.createRequiemDatabase("E:\\SteamLibrary\\steamapps\\common\\Wurm Unlimited Dedicated Server\\Login", "Requiem.db");
                    DatabaseHelper.createRequiemDatabase("E:\\SteamLibrary\\steamapps\\common\\Wurm Unlimited Dedicated Server\\Tranquil Garden", "Requiem.db");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                ctItem.getMethod("removeItem", "(JZZZ)Lcom/wurmonline/server/items/Item;")
                        .insertAfter(String.format("%s.removeFromItemHook(this, $_);", Hooks.class.getName()));
            } catch (CannotCompileException | NotFoundException e) {
                throw new HookException(e);
            }

            try {
                // added to make caught fish also give coin
                ctFishing.getMethod("makeDeadFish", "(Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/behaviours/Action;Lcom/wurmonline/server/skills/Skill;BLcom/wurmonline/server/items/Item;Lcom/wurmonline/server/items/Item;)I")
                        .instrument(new ExprEditor() {
                            @Override
                            public void edit(MethodCall m) throws CannotCompileException {
                                if (m.getMethodName().equals("insertItem")) {
                                    m.replace(String.format("$_=%s.catchFishHook(performer, $0, $1);", Hooks.class.getName()));
                                    RequiemLogging.logInfo(String.format("Injected fish bounty in makeDeadFish() at %d", m.getLineNumber()));
                                }
                            }
                        });
            } catch (NotFoundException | CannotCompileException e) {
                e.printStackTrace();
            }

            try {
                Util.setReason("Add buffs to players after death on monthly buff days.");
                replace = String.format("%s.spawnQuestion($0);", Hooks.class.getName());
                Util.insertBeforeDeclared(thisClass, ctPlayer, "spawn", replace);

            } catch (IllegalArgumentException | ClassCastException e) {
                throw new HookException(e);
            }

            if (enableNewPlayerQuestion) {
                // New player spawn question
                classPool.getCtClass("com.wurmonline.server.LoginHandler").getMethod("handleLogin", "(Ljava/lang/String;Ljava/lang/String;ZZZZLjava/lang/String;Ljava/lang/String;)V")
                        .instrument(new ExprEditor() {
                            @Override
                            public void edit(MethodCall m) throws CannotCompileException {
                                if (m.getMethodName().equals("sendQuestion")) {
                                    m.replace("if (!com.wurmonline.server.questions.NewPlayerQuestion.send($0.getResponder())) $proceed();");
                                }
                            }
                        });
            }

            if (enableAthanorMechanism) {
                // - Add xmas light effect for the athanor mechanism - //
                ctPlayers.getDeclaredMethod("sendAltarsToPlayer").insertBefore(String.format("%s.sendMechanismEffectsToPlayer($1);", AthanorMechanismBehaviour.class.getName()));
                RequiemLogging.logInfo("AthanorMechanism.preInit called");
            }
        } catch (IllegalArgumentException | ClassCastException | NotFoundException | CannotCompileException e) {
            throw new HookException(e);
        }
    }

}
