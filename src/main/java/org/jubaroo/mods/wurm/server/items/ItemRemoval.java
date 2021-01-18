package org.jubaroo.mods.wurm.server.items;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.jubaroo.mods.wurm.server.tools.Hooks;

public class ItemRemoval {

    public static void preInit() {
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();
            CtClass ctItem = classPool.getCtClass("com.wurmonline.server.items.Item");
            ctItem.getMethod("removeItem", "(JZZZ)Lcom/wurmonline/server/items/Item;")
                    .insertAfter(String.format("%s.removeFromItemHook(this, $_);", Hooks.class.getName()));
        } catch (CannotCompileException | NotFoundException e) {
            throw new HookException(e);
        }
    }

}
