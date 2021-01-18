package org.jubaroo.mods.wurm.server.utils;

import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.items.Item;
import com.wurmonline.shared.constants.EffectConstants;
import net.bdew.wurm.server.threedee.api.IDisplayHook;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.tools.ItemTools;

import java.lang.reflect.InvocationTargetException;

public class Compat3D {
    public static void installDisplayHook() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        try {
            Class.forName("net.bdew.wurm.server.threedee.api.DisplayHookRegistry")
                    .getMethod("add", int.class, IDisplayHook.class)
                    .invoke(null, CustomItems.fireCrystal.getTemplateId(), new IDisplayHook() {
                                @Override
                                public boolean addItem(Communicator comm, Item item, float x, float y, float z, float rot) {
                                    comm.sendRemoveEffect(item.getWurmId());
                                    ItemTools.sendItem(comm.getPlayer(), item, x, y, z, rot);
                                    comm.sendRepaint(item.getWurmId(), (byte) 255, (byte) 1, (byte) 1, (byte) 255, (byte) 0);
                                    comm.sendAttachEffect(item.getWurmId(), (byte) 0, (byte) 255, (byte) 1, (byte) 1, (byte) 255);
                                    comm.sendAddEffect(item.getWurmId(), item.getWurmId(), EffectConstants.EFFECT_GENERIC, x, y, z, (byte) 0, "copperBrazierFire", Float.MAX_VALUE, 0f);
                                    return true;
                                }

                                @Override
                                public boolean removeItem(Communicator comm, Item item) {
                                    comm.sendRemoveEffect(item.getWurmId());
                                    return false;
                                }
                            }
                    );

            Class.forName("net.bdew.wurm.server.threedee.api.DisplayHookRegistry")
                    .getMethod("add", int.class, IDisplayHook.class)
                    .invoke(null, CustomItems.frostCrystal.getTemplateId(), new IDisplayHook() {
                                @Override
                                public boolean addItem(Communicator comm, Item item, float x, float y, float z, float rot) {
                                    comm.sendRemoveEffect(item.getWurmId());
                                    ItemTools.sendItem(comm.getPlayer(), item, x, y, z, rot);
                                    comm.sendRepaint(item.getWurmId(), (byte) 131, (byte) 205, (byte) 232, (byte) 255, (byte) 0);
                                    comm.sendAttachEffect(item.getWurmId(), (byte) 0, (byte) 131, (byte) 205, (byte) 232, (byte) 255);
                                    comm.sendAddEffect(item.getWurmId(), item.getWurmId(), EffectConstants.EFFECT_GENERIC, x, y, z, (byte) 0, "iceBall_1_1", Float.MAX_VALUE, 0f);
                                    return true;
                                }

                                @Override
                                public boolean removeItem(Communicator comm, Item item) {
                                    comm.sendRemoveEffect(item.getWurmId());
                                    return false;
                                }
                            }
                    );

            Class.forName("net.bdew.wurm.server.threedee.api.DisplayHookRegistry")
                    .getMethod("add", int.class, IDisplayHook.class)
                    .invoke(null, CustomItems.deathCrystal.getTemplateId(), new IDisplayHook() {
                                @Override
                                public boolean addItem(Communicator comm, Item item, float x, float y, float z, float rot) {
                                    comm.sendRemoveEffect(item.getWurmId());
                                    ItemTools.sendItem(comm.getPlayer(), item, x, y, z, rot);
                                    comm.sendRepaint(item.getWurmId(), (byte) 59, (byte) 2, (byte) 144, (byte) 255, (byte) 0);
                                    comm.sendAttachEffect(item.getWurmId(), (byte) 0, (byte) 59, (byte) 2, (byte) 144, (byte) 255);
                                    comm.sendAddEffect(item.getWurmId(), item.getWurmId(), EffectConstants.EFFECT_GENERIC, x, y, z, (byte) 0, "fogSpider", Float.MAX_VALUE, 0f);
                                    return true;
                                }

                                @Override
                                public boolean removeItem(Communicator comm, Item item) {
                                    comm.sendRemoveEffect(item.getWurmId());
                                    return false;
                                }
                            }
                    );

            Class.forName("net.bdew.wurm.server.threedee.api.DisplayHookRegistry")
                    .getMethod("add", int.class, IDisplayHook.class)
                    .invoke(null, CustomItems.lifeCrystal.getTemplateId(), new IDisplayHook() {
                                @Override
                                public boolean addItem(Communicator comm, Item item, float x, float y, float z, float rot) {
                                    comm.sendRemoveEffect(item.getWurmId());
                                    ItemTools.sendItem(comm.getPlayer(), item, x, y, z, rot);
                                    comm.sendRepaint(item.getWurmId(), (byte) 34, (byte) 155, (byte) 4, (byte) 255, (byte) 0);
                                    comm.sendAttachEffect(item.getWurmId(), (byte) 0, (byte) 34, (byte) 155, (byte) 4, (byte) 255);
                                    comm.sendAddEffect(item.getWurmId(), item.getWurmId(), EffectConstants.EFFECT_GENERIC, x, y, z, (byte) 0, "acidBall_1_1", Float.MAX_VALUE, 0f);
                                    return true;
                                }

                                @Override
                                public boolean removeItem(Communicator comm, Item item) {
                                    comm.sendRemoveEffect(item.getWurmId());
                                    return false;
                                }
                            }
                    );

            Class.forName("net.bdew.wurm.server.threedee.api.DisplayHookRegistry")
                    .getMethod("add", int.class, IDisplayHook.class)
                    .invoke(null, CustomItems.essenceOfWoodId, new IDisplayHook() {
                                @Override
                                public boolean addItem(Communicator comm, Item item, float x, float y, float z, float rot) {
                                    comm.sendRemoveEffect(item.getWurmId());
                                    ItemTools.sendItem(comm.getPlayer(), item, x, y, z, rot);
                                    comm.sendAddEffect(item.getWurmId(), item.getWurmId(), EffectConstants.EFFECT_GENERIC, x, y, z, (byte) 0, "traitor", Float.MAX_VALUE, 0f);
                                    return true;
                                }

                                @Override
                                public boolean removeItem(Communicator comm, Item item) {
                                    comm.sendRemoveEffect(item.getWurmId());
                                    return false;
                                }
                            }
                    );

            RequiemLogging.logInfo("3D Stuff mod loaded - added compatibility hook");
        } catch (ClassNotFoundException e) {
            RequiemLogging.logWarning("3D Stuff mod doesn't seem to be loaded");
        }
    }
}
