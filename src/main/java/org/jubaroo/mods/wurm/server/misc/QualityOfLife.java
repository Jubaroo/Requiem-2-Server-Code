package org.jubaroo.mods.wurm.server.misc;

import com.wurmonline.server.Items;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.NoSuchItemException;
import com.wurmonline.server.NoSuchPlayerException;
import com.wurmonline.server.behaviours.Actions;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.NoSuchCreatureException;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.zones.NoSuchZoneException;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.Descriptor;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import mod.sin.lib.Util;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.ItemMod;

import static org.jubaroo.mods.wurm.server.server.constants.ItemConstants.createItemDescs;
import static org.jubaroo.mods.wurm.server.server.constants.ItemConstants.hidePlayerGodInscriptions;
import static org.jubaroo.mods.wurm.server.server.constants.OtherConstants.*;

public class QualityOfLife {

    private static boolean insertItemIntoVehicle(Item item, Item vehicle, Creature performer) {
        // If can put into crates, try that
        if (item.getTemplate().isBulk() && item.getRarity() == 0) {
            for (Item container : vehicle.getAllItems(true)) {
                if (container.getTemplateId() == ItemList.bulkContainer) {
                    if (container.getFreeVolume() >= item.getVolume()) {
                        if (item.AddBulkItem(performer, container)) {
                            performer.getCommunicator().sendNormalServerMessage(String.format("You put the %s in the %s in your %s.", item.getName(), container.getName(), vehicle.getName()));
                            return true;
                        }
                    }
                }
            }
            for (Item container : vehicle.getAllItems(true)) {
                if (container.isCrate() && container.canAddToCrate(item)) {
                    if (item.AddBulkItemToCrate(performer, container)) {
                        performer.getCommunicator().sendNormalServerMessage(String.format("You put the %s in the %s in your %s.", item.getName(), container.getName(), vehicle.getName()));
                        return true;
                    }
                }
            }
        }
        // No empty crates or disabled, try the vehicle itself
        if (vehicle.getNumItemsNotCoins() < 100 && vehicle.getFreeVolume() >= item.getVolume() && vehicle.insertItem(item)) {
            performer.getCommunicator().sendNormalServerMessage(String.format("You put the %s in the %s.", item.getName(), vehicle.getName()));
            return true;
        } else {
            // Send message if the vehicle is too full
            performer.getCommunicator().sendNormalServerMessage(String.format("The %s is too full to hold the %s.", vehicle.getName(), item.getName()));
            return false;
        }
    }

    public static Item getVehicleSafe(Creature pilot) {
        try {
            if (pilot.getVehicle() != -10)
                return Items.getItem(pilot.getVehicle());
        } catch (NoSuchItemException ignored) {
        }
        return null;
    }

    public static void vehicleHook(Creature performer, Item item) {
        Item vehicleItem = getVehicleSafe(performer);
        if (vehicleItem != null && vehicleItem.isHollow()) {
            if (insertItemIntoVehicle(item, vehicleItem, performer)) {
                return;
            }
        }
        // Last resort, if no suitable vehicle is found.
        try {
            item.putItemInfrontof(performer);
        } catch (NoSuchCreatureException | NoSuchItemException | NoSuchPlayerException | NoSuchZoneException e) {
            e.printStackTrace();
        }
    }

    public static void preInit() {
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();
            final Class<QualityOfLife> thisClass = QualityOfLife.class;
            String replace;

            Util.setReason("Allow players to mine directly into vehicles.");
            CtClass ctAction = classPool.get("com.wurmonline.server.behaviours.Action");
            CtClass ctCreature = classPool.get("com.wurmonline.server.creatures.Creature");
            CtClass ctItem = classPool.get("com.wurmonline.server.items.Item");
            CtClass ctCaveWallBehaviour = classPool.get("com.wurmonline.server.behaviours.CaveWallBehaviour");
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
            replace = "$_ = null;"
                    + QualityOfLife.class.getName() + ".vehicleHook(performer, $0);";
            Util.instrumentDescribed(thisClass, ctCaveWallBehaviour, "action", desc1, "putItemInfrontof", replace);

            CtClass ctTileRockBehaviour = classPool.get("com.wurmonline.server.behaviours.TileRockBehaviour");
            Util.setReason("Allow players to surface mine directly into vehicles.");
            replace = "$_ = $proceed($$);" +
                    QualityOfLife.class.getName() + ".vehicleHook(performer, $0);";
            Util.instrumentDeclared(thisClass, ctTileRockBehaviour, "mine", "setDataXY", replace);

            Util.setReason("Allow players to chop logs directly into vehicles.");
            CtClass ctMethodsItems = classPool.get("com.wurmonline.server.behaviours.MethodsItems");
            replace = "$_ = null;" +
                    QualityOfLife.class.getName() + ".vehicleHook(performer, $0);";
            Util.instrumentDeclared(thisClass, ctMethodsItems, "chop", "putItemInfrontof", replace);

            //Util.setReason("Allow statuettes to be used when not gold/silver.");
            //String desc100 = Descriptor.ofMethod(CtClass.booleanType, new CtClass[]{});
            //replace = "{ return this.template.holyItem; }";
            //Util.setBodyDescribed(thisClass, ctItem, "isHolyItem", desc100, replace);

            /* Disabled in Wurm Unlimited 1.9 - Priest Rework changes removed this restriction.

            Util.setReason("Remove requirement for Libila priests to bless creatures before taming.");
            CtClass ctMethodsCreatures = classPool.get("com.wurmonline.server.behaviours.MethodsCreatures");
            replace = "$_ = false;";
            Util.instrumentDeclared(thisClass, ctMethodsCreatures, "tame", "isPriest", replace);*/

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
            replace = "$_ = null;" +
                    QualityOfLife.class.getName() + ".vehicleHook(performer, $0);";
            Util.instrumentDescribed(thisClass, ctTileRockBehaviour, "createGem", desc2, "putItemInfrontof", replace);

            CtClass ctPlayer = classPool.get("com.wurmonline.server.players.Player");
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
                        RequiemLogging.logWarning(String.format("incAff: %d %d", args[0], args[1]));
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

}
