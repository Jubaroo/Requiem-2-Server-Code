package org.jubaroo.mods.wurm.server.server;

import com.wurmonline.server.Items;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.CreatureTemplateIds;
import com.wurmonline.server.economy.Change;
import com.wurmonline.server.economy.Economy;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.Materials;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.zones.FocusZone;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.classhooks.InvocationHandlerFactory;
import org.gotti.wurmunlimited.modsupport.creatures.ModCreatures;
import org.jubaroo.mods.wurm.server.ModConfig;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.creatures.CustomCreatures;
import org.jubaroo.mods.wurm.server.items.ItemMod;
import org.jubaroo.mods.wurm.server.items.behaviours.SupplyDepots;
import org.jubaroo.mods.wurm.server.misc.DecorativeKingdoms;
import org.jubaroo.mods.wurm.server.misc.MiscHooks;
import org.jubaroo.mods.wurm.server.misc.templates.StructureTemplate;
import org.jubaroo.mods.wurm.server.tools.Hooks;
import org.jubaroo.mods.wurm.server.spells.SpellExamine;
import org.jubaroo.mods.wurm.server.vehicles.CustomVehicles;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static org.jubaroo.mods.wurm.server.ModConfig.*;
import static org.jubaroo.mods.wurm.server.server.constants.ItemConstants.createItemDescs;
import static org.jubaroo.mods.wurm.server.server.constants.ItemConstants.structureTemplates;

public class Initialize {

    public static void init() throws NotFoundException, CannotCompileException {

        ModCreatures.init();

        ClassPool classPool = HookManager.getInstance().getClassPool();
        CtMethod mAddCreature = classPool.getCtClass("com.wurmonline.server.zones.VirtualZone").getMethod("addCreature", "(JZJFFF)Z");
        CtClass ctCreature = classPool.getCtClass("com.wurmonline.server.creatures.Creature");
        CtClass ctCreatureStatus = classPool.getCtClass("com.wurmonline.server.creatures.CreatureStatus");
        CtClass ctCommunicator = classPool.getCtClass("com.wurmonline.server.creatures.Communicator");
        CtClass ctItem = classPool.getCtClass("com.wurmonline.server.items.Item");
        CtClass ctServer = classPool.getCtClass("com.wurmonline.server.Server");
        CtClass ctMethodsItems = classPool.getCtClass("com.wurmonline.server.behaviours.MethodsItems");

        try {
            // Spell examine
            ctMethodsItems.getMethod("getEnhancementStrings", "(Lcom/wurmonline/server/items/Item;)Ljava/util/List;")
                    .instrument(new ExprEditor() {
                        @Override
                        public void edit(MethodCall m) throws CannotCompileException {
                            if (m.getMethodName().equals("getName")) {
                                m.replace(String.format("$_ = %s.getName($0, item);", SpellExamine.class.getName()));
                                RequiemLogging.logInfo(String.format("Applied getEnhancementStrings patch for getName at %s line %d", m.where().getLongName(), m.getLineNumber()));
                            } else if (m.getMethodName().equals("getLongDesc")) {
                                m.replace(String.format("$_ = %s.getLongDesc($0, item);", SpellExamine.class.getName()));
                                RequiemLogging.logInfo(String.format("Applied getEnhancementStrings patch for getLongDesc at %s line %d", m.where().getLongName(), m.getLineNumber()));
                            }
                        }
                    });

            ctItem.getMethod("sendColoredEnchant", "(Lcom/wurmonline/server/creatures/Communicator;Lcom/wurmonline/server/spells/SpellEffect;)V")
                    .instrument(new ExprEditor() {
                        @Override
                        public void edit(MethodCall m) throws CannotCompileException {
                            if (m.getMethodName().equals("getName")) {
                                m.replace(String.format("$_ = %s.getName($0, this);", SpellExamine.class.getName()));
                                RequiemLogging.logInfo(String.format("Applied sendColoredEnchant patch for getName at %s line %d", m.where().getLongName(), m.getLineNumber()));
                            } else if (m.getMethodName().equals("getLongDesc")) {
                                m.replace(String.format("$_ = %s.getLongDesc($0, this);", SpellExamine.class.getName()));
                                RequiemLogging.logInfo(String.format("Applied sendColoredEnchant patch for getLongDesc at %s line %d", m.where().getLongName(), m.getLineNumber()));
                            }
                        }
                    });
        } catch (CannotCompileException | NotFoundException e) {
            RequiemLogging.logException("[ERROR] in custom spell display.", e);
        }

        try {
            classPool.getCtClass("com.wurmonline.server.spells.Dirt")
                    .getMethod("doEffect", "(Lcom/wurmonline/server/skills/Skill;DLcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;)V")
                    .instrument(new ExprEditor() {
                        @Override
                        public void edit(MethodCall m) throws CannotCompileException {
                            if (m.getMethodName().equals("min"))
                                m.replace("$_=$proceed(20,$2);");
                        }
                    });
        } catch (CannotCompileException | NotFoundException e) {
            RequiemLogging.logException("[ERROR] in changing amount of dirt created in the Dirt spell.", e);
        }

        try {
            HookManager.getInstance().registerHook("com.wurmonline.server.Players", "sendAltarsToPlayer", "(Lcom/wurmonline/server/players/Player;)V", new InvocationHandlerFactory() {

                @Override
                public InvocationHandler createInvocationHandler() {
                    return new InvocationHandler() {

                        @Override
                        public Object invoke(Object object, Method method, Object[] args) throws Throwable {

                            Player player = (Player) args[0];

                            SupplyDepots.sendDepotEffectsToPlayer(player);

                            return method.invoke(object, args);
                        }

                    };
                }
            });
        } catch (Exception e) {
            RequiemLogging.logException("[ERROR] in sendAltarsToPlayer.", e);
        }

        try {
            CtMethod ctDoNew = ctCreature.getMethod("doNew", "(IZFFFILjava/lang/String;BBBZB)Lcom/wurmonline/server/creatures/Creature;");
            ctDoNew.instrument(new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("sendToWorld")) {
                        m.replace("$_ = $proceed($$);org.jubaroo.mods.wurm.server.tools.Hooks.modifyNewCreature($1);");
                        return;
                    }
                }
            });
        } catch (NotFoundException | CannotCompileException e) {
            RequiemLogging.logException("[ERROR] in doNew.", e);
        }

        try {
            // -- Enable adjusting size for creatures -- //
            ctCreatureStatus.getDeclaredMethod("getSizeMod").setBody("{return org.jubaroo.mods.wurm.server.tools.CreatureTools.getAdjustedSizeMod(this);}");
        } catch (CannotCompileException | NotFoundException e) {
            RequiemLogging.logException("[ERROR] in getSizeMod.", e);
        }

        //setUpNpcMovement();
        HookManager hooks = HookManager.getInstance();
        ClassPool pool = hooks.getClassPool();
        if (stfuNpcs) {
            try {
            hooks.registerHook("com.wurmonline.server.creatures.ai.ChatManager",
                    "answerLocalChat",
                    "(Lcom/wurmonline/server/Message;Ljava/lang/String;)V",
                    () -> (proxy, method, args) -> null);
            hooks.registerHook("com.wurmonline.server.creatures.ai.ChatManager",
                    "getSayToCreature",
                    "(Lcom/wurmonline/server/creatures/Creature;)Ljava/lang/String;",
                    () -> (proxy, method, args) -> null);
            } catch (Exception e) {
                RequiemLogging.logException("[ERROR] in getRandomNonHateDeity.", e);
            }
        }

        if (hidePlayerGodInscriptions) {
            try {
            hooks.registerHook("com.wurmonline.server.deities.Deities",
                    "getRandomNonHateDeity",
                    "()Lcom/wurmonline/server/deities/Deity;",
                    () -> (proxy, method, args) -> null);
            } catch (Exception e) {
                RequiemLogging.logException("[ERROR] in getRandomNonHateDeity.", e);
            }
        }

        if (gmFullFavor) {
            try {
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
            } catch (Exception e) {
                RequiemLogging.logException("[ERROR] in depleteFavor.", e);
            }
        }
        try {
            hooks.registerHook("com.wurmonline.server.players.Player",
                    "increaseAffinity",
                    "(II)V",
                    () -> (proxy, method, args) -> {
                        RequiemLogging.logWarning(String.format("incAff: %d %d", args[0], args[1]));
                        return method.invoke(proxy, args);
                    });
        } catch (Exception e) {
            RequiemLogging.logException("[ERROR] in increaseAffinity.", e);
        }

        if (loadFullContainers) {
            try {
                hooks.registerHook("com.wurmonline.server.behaviours.CargoTransportationMethods",
                        "targetIsNotEmptyContainerCheck",
                        "(Lcom/wurmonline/server/items/Item;Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;Z)Z",
                        () -> (proxy, method, args) -> false);
            } catch (Exception e) {
                RequiemLogging.logException("[ERROR] in targetIsNotEmptyContainerCheck.", e);
            }
        }

        if (noMineDrift) {
            try {
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
            } catch (Exception e) {
                RequiemLogging.logException("[ERROR] in getFloorAndCeiling.", e);
            }
        }

        if (allowTentsOnDeed) {
            try {
                hooks.registerHook("com.wurmonline.server.behaviours.MethodsItems",
                        "mayDropTentOnTile",
                        "(Lcom/wurmonline/server/creatures/Creature;)Z",
                        () -> (proxy, method, args) -> true);
            } catch (Exception e) {
                RequiemLogging.logException("[ERROR] in mayDropTentOnTile.", e);
            }
        }

        if (allSurfaceMine) {
            try {
                MiscHooks.hookSurfaceMine(pool);
            } catch (Exception e) {
                RequiemLogging.logException("[ERROR] in hookSurfaceMine.", e);
            }
        }

        try {
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
        } catch (Exception e) {
            RequiemLogging.logException("[ERROR] in createItem.", e);
        }

        if (lampsAutoLight) {
            try {
                hooks.registerHook("com.wurmonline.server.items.Item", "refuelLampFromClosestVillage", "()V",
                        () -> (proxy, method, args) -> {
                            Item lamp = (Item) proxy;
                            lamp.setAuxData((byte) 127);
                            return method.invoke(proxy, args);
                        });
            } catch (Exception e) {
                RequiemLogging.logException("[ERROR] in refuelLampFromClosestVillage.", e);
            }
        }

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

        try {
            RequiemLogging.logInfo("========= Initializing ItemMod.init =========");

            // Sends effects to items
            ctCommunicator.getMethod("sendItem", "(Lcom/wurmonline/server/items/Item;JZ)V")
                    .insertAfter(String.format("%s.sendItemHook(this, $1);", Hooks.class.getName()));

            // Removes effects from items
            ctCommunicator.getMethod("sendRemoveItem", "(Lcom/wurmonline/server/items/Item;)V")
                    .insertAfter(String.format("%s.removeItemHook(this, $1);", Hooks.class.getName()));

            // Make searched dens tick down and be able to be searched again
            ctItem.getMethod("poll", "(ZZJ)Z").insertAfter(String.format("%s.itemTick(this);", Hooks.class.getName()));

            // block certain items from moving to another
            classPool.getCtClass("com.wurmonline.server.items.Item")
                    .getMethod("moveToItem", "(Lcom/wurmonline/server/creatures/Creature;JZ)Z")
                    .instrument(new ExprEditor() {
                        boolean patched = false;

                        @Override
                        public void edit(MethodCall m) throws CannotCompileException {
                            if (!patched && m.getMethodName().equals("getItem")) {
                                m.replace(String.format("$_=$proceed($$); if (%s.blockMove(this, $_, mover)) return false;", Hooks.class.getName()));
                                RequiemLogging.logInfo(String.format("Hooking Item.moveToItem at %d", m.getLineNumber()));
                                patched = true;
                            }
                        }
                    });

            // control the max number of items that can be put into a container
            HookManager.getInstance().registerHook("com.wurmonline.server.items.Item", "mayCreatureInsertItem", "()Z", new InvocationHandlerFactory() {
                @Override
                public InvocationHandler createInvocationHandler() {
                    return new InvocationHandler() {
                        @Override
                        public Object invoke(Object object, Method method, Object[] args) throws Throwable {
                            Item item = (Item) object;
                            for (StructureTemplate template : structureTemplates) {
                                if (item.getTemplateId() == template.templateID) {
                                    return item.getItemCount() < 500;
                                }
                            }
                            if (item.getTemplateId() == CustomVehicles.loggingWagon.getTemplateId()) {
                                return item.getItemCount() < 200;
                            }
                            return method.invoke(object, args);
                        }
                    };
                }
            });

            // control what happens when cargo is unloaded
            HookManager.getInstance().registerHook("com.wurmonline.server.behaviours.CargoTransportationMethods", "unloadCargo", "(Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/items/Item;F)Z", new InvocationHandlerFactory() {
                @Override
                public InvocationHandler createInvocationHandler() {
                    return new InvocationHandler() {
                        @Override
                        public Object invoke(Object object, Method method, Object[] args) throws Throwable {
                            Creature performer = (Creature) args[0];
                            Item item = (Item) args[1];
                            long coin = tradeTentCoinReward;
                            if (performer.getCurrentAction().currentSecond() == 3) {
                                for (FocusZone fz : FocusZone.getZonesAt(performer.currentTile.tilex, performer.currentTile.tiley)) {
                                    if (fz.getName().equals(tradeTentsNorthZoneName) && item.getData() == 801L) {
                                        Items.destroyItem(item.getWurmId());
                                        performer.addMoney(coin);
                                        final Change newch = Economy.getEconomy().getChangeFor(coin);
                                        performer.getCommunicator().sendNormalServerMessage(String.format("You receive a payment of %s for delivering the trade goods.", newch));
                                    } else if (fz.getName().equals(tradeTentsSouthZoneName) && item.getData() == 802L) {
                                        Items.destroyItem(item.getWurmId());
                                        performer.addMoney(coin);
                                        final Change newch = Economy.getEconomy().getChangeFor(coin);
                                        performer.getCommunicator().sendNormalServerMessage(String.format("You receive a payment of %s for delivering the trade goods.", newch));
                                    }
                                }
                            }
                            return method.invoke(object, args);
                        }
                    };
                }
            });
        } catch (CannotCompileException | NotFoundException e) {
            RequiemLogging.logException("[Error] in createInvocationHandler for unloadCargo in Initialize", e);
        }

        try {
            if (!ModConfig.disableColoredUnicorns) {
                // Make unicorns have random colors
                classPool.getCtClass("com.wurmonline.server.zones.VirtualZone").getMethod("addCreature", "(JZJFFF)Z")
                        .insertAfter(String.format("%s.addCreatureHook(this, $1);", Hooks.class.getName()));
            }

            if (!ModConfig.disableFogGoblins) {
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
            }
        } catch (NotFoundException e) {
            throw new HookException(e);
        }

        try {
            ctItem.getMethod("getName", "(Z)Ljava/lang/String;").instrument(new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("getNameFor"))
                        m.replace(String.format("$_ = %s.getName($1);", DecorativeKingdoms.class.getName()));
                }
            });

            ctItem.getMethod("getModelName", "()Ljava/lang/String;").instrument(new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("getSuffixFor"))
                        m.replace(String.format("$_ = %s.getModelSuffix($1);", DecorativeKingdoms.class.getName()));
                }
            });

            ctCommunicator.getMethod("sendAllKingdoms", "()V").setBody(String.format("%s.sendKingdoms(this);", MiscHooks.class.getName()));

            ctItem.getMethod("sendWear", "(Lcom/wurmonline/server/items/Item;B)V")
                    .insertAfter(String.format("if (!item.isBodyPartAttached()) %s.sendWearHook(item, this.getOwnerOrNull());", MiscHooks.class.getName()));

            ctItem.getMethod("removeItem", "(JZZZ)Lcom/wurmonline/server/items/Item;")
                    .instrument(new ExprEditor() {
                        @Override
                        public void edit(MethodCall m) throws CannotCompileException {
                            if (m.getMethodName().equals("hasItemBonus"))
                                m.replace(String.format("%s.sendWearHook(item, owner); $_ = $proceed($$);", MiscHooks.class.getName()));
                        }
                    });

            mAddCreature.instrument(new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("sendNewCreature"))
                        m.replace(String.format("$proceed($1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11,$12,$13,%s.creatureKingdom(creature,$14),$15,$16,$17,$18,$19);", MiscHooks.class.getName()));
                }
            });

            ctServer.getMethod("run", "()V").insertAfter(String.format("%s.serverTick(this);", MiscHooks.class.getName()));

            classPool.getCtClass("com.wurmonline.server.items.ChickenCoops")
                    .getMethod("eggPoller", "(Lcom/wurmonline/server/items/Item;)V")
                    .instrument(new ExprEditor() {
                        @Override
                        public void edit(MethodCall m) throws CannotCompileException {
                            if (m.getMethodName().equals("getQualityLevel"))
                                m.replace("$_=99f;");
                        }
                    });
        } catch (NotFoundException | CannotCompileException e) {
            RequiemLogging.logWarning(e.getMessage());
            throw new HookException(e);
        }

        HookManager.getInstance().registerHook("com.wurmonline.server.creatures.Creature", "getMountSpeedPercent", "(Z)F", new InvocationHandlerFactory() {
            @Override
            public InvocationHandler createInvocationHandler() {
                return new InvocationHandler() {
                    @Override
                    public Object invoke(Object object, Method method, Object[] args) throws Throwable {
                        Creature creature = (Creature) object;
                        float speed = (float) method.invoke(object, args);
                        //Requiem.debug("old speed: " + speed);
                        if (!creature.isRidden()) {
                            try {
                                if (creature.getBonusForSpellEffect((byte) 22) > 0.0F) {
                                    speed -= 0.2F * (creature.getBonusForSpellEffect((byte) 22) / 100.0F);
                                    //Requiem.debug("decrease because oakshell: " + (0.2F * (creature.getBonusForSpellEffect((byte) 22) / 100.0F)));
                                }
                                Item barding = creature.getArmour((byte) 2);
                                if (barding != null) {
                                    if (barding.getMaterial() == Materials.MATERIAL_LEATHER) {
                                        speed -= 0.1F;
                                        //Requiem.debug("decrease because leather: 0.1");
                                    } else {
                                        speed -= 0.2F;
                                        //Requiem.debug("decrease because barding: 0.2");
                                    }
                                }
                            } catch (Exception e) {
                                RequiemLogging.logException("[Error] in createInvocationHandler for getMountSpeedPercent in Initialize", e);
                            }
                        }
                        //Requiem.debug("new speed: " + speed);
                        return speed;
                    }
                };
            }
        });

        HookManager.getInstance().registerHook("com.wurmonline.server.spells.Dominate", "mayDominate", "(Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/creatures/Creature;)Z", new InvocationHandlerFactory() {

            @Override
            public InvocationHandler createInvocationHandler() {
                return new InvocationHandler() {

                    @Override
                    public Object invoke(Object object, Method method, Object[] args) throws Throwable {
                        Creature performer = (Creature) args[0];
                        Creature target = (Creature) args[1];

                        if (target != null && target.isUnique()) {
                            performer.getCommunicator().sendNormalServerMessage(String.format("You cannot dominate %s, because of its immense power.", target.getName()), (byte) 3);
                            return false;
                        }
                        return method.invoke(object, args);
                    }
                };
            }
        });

    }

}
