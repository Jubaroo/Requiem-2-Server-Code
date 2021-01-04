package org.jubaroo.mods.wurm.server.items;

import com.wurmonline.server.Items;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import net.bdew.wurm.server.threedee.Utils;
import net.bdew.wurm.server.threedee.api.DisplayHookRegistry;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.server.Constants;
import org.jubaroo.mods.wurm.server.vehicles.CustomVehicles;

import java.util.Set;

public class ItemRemoval {
    /**
     * Here we control what happens when items are destroyed
     * @param comm player vision
     * @param item item being destroyed
     */
    public static void removeItemHook(Communicator comm, Item item) {
        int templateId;
        templateId = item.getTemplateId();
        if (Constants.itemRemoveLogging) {
            RequiemLogging.ItemRemovalLogging(item);
        }
        if (templateId == CustomItems.fireCrystal.getTemplateId()) {
            comm.sendRemoveEffect(item.getWurmId());
        } else if (templateId == CustomItems.frostCrystal.getTemplateId()) {
            comm.sendRemoveEffect(item.getWurmId());
        } else if (templateId == CustomItems.deathCrystal.getTemplateId()) {
            comm.sendRemoveEffect(item.getWurmId());
        } else if (templateId == CustomItems.lifeCrystal.getTemplateId()) {
            comm.sendRemoveEffect(item.getWurmId());
        } else if (templateId == CustomItems.essenceOfWoodId) {
            comm.sendRemoveEffect(item.getWurmId());
        } else if (templateId == CustomVehicles.altarWagon.getTemplateId()) {
            comm.sendRemoveEffect(item.getWurmId());
        } else if (templateId == CustomItems.campfire.getTemplateId()) {
            comm.sendRemoveEffect(item.getWurmId());
        } else if (templateId == CustomItems.eventGravestoneId) {
            comm.sendRemoveEffect(item.getWurmId());
        }
    }

    private static void doRemoveItem(Communicator comm, Item item) {
        if (!DisplayHookRegistry.doRemoveItem(comm, item))
            comm.sendRemoveItem(item);
    }

    /**
     * Here we control when items are destroyed
     * @param item former parent
     * @param ret  item that was removed
     */
    public static void removeFromItemHook(Item item, Item ret) {
        if (Constants.itemRemoveLogging) {
            RequiemLogging.ItemRemovalLogging(item);
        }
        if (item.getTemplateId() == CustomItems.steelToolsBackpack.getTemplateId() && item.isEmpty(false)) {
            Item parent = item.getParentOrNull();
            if (parent != null && parent.getParentId() == -10) {
                Utils.forAllWatchers(parent, (player) -> doRemoveItem(player.getCommunicator(), ret));
            }
            if (ret.getParentId() == item.getWurmId())
                ret.setParentId(item.getParentId(), item.isOnSurface());
            Set<Creature> watchers = ret.getWatcherSet();
            if (watchers != null) {
                for (Creature creature : watchers) {
                    if (creature.getCommunicator().sendCloseInventoryWindow(ret.getWurmId())) {
                        ret.removeWatcher(creature, true);
                    }
                }
            }
            if (item.getItemsAsArray().length == 0)
                Items.destroyItem(item.getWurmId());
        }
        if (item.getTemplateId() == CustomItems.addyToolsBackpack.getTemplateId() && item.isEmpty(false)) {
            Item parent = item.getParentOrNull();
            if (parent != null && parent.getParentId() == -10) {
                Utils.forAllWatchers(parent, (player) -> doRemoveItem(player.getCommunicator(), ret));
            }
            if (ret.getParentId() == item.getWurmId())
                ret.setParentId(item.getParentId(), item.isOnSurface());
            Set<Creature> watchers = ret.getWatcherSet();
            if (watchers != null) {
                for (Creature creature : watchers) {
                    if (creature.getCommunicator().sendCloseInventoryWindow(ret.getWurmId())) {
                        ret.removeWatcher(creature, true);
                    }
                }
            }
            if (item.getItemsAsArray().length == 0)
                Items.destroyItem(item.getWurmId());
        }

    }

    public static void preInit() {
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();
            CtClass ctItem = classPool.getCtClass("com.wurmonline.server.items.Item");
            ctItem.getMethod("removeItem", "(JZZZ)Lcom/wurmonline/server/items/Item;")
                    .insertAfter(String.format("%s.removeFromItemHook(this, $_);", ItemRemoval.class.getName()));
        } catch (CannotCompileException | NotFoundException e) {
            throw new HookException(e);
        }
    }

}
