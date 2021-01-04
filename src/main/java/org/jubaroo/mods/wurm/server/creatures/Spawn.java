package org.jubaroo.mods.wurm.server.creatures;

import com.wurmonline.server.players.Player;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import mod.sin.lib.Util;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.jubaroo.mods.wurm.server.server.OnPlayerLogin;

public class Spawn {

    public static void spawnQuestion(Player player) {
        try {
            OnPlayerLogin.addMonthlyBuffsToPlayers(player);
        } catch (IllegalArgumentException | ClassCastException e) {
            throw new HookException(e);
        }
    }

    public static void preInit() {
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();
            final Class<Spawn> thisClass = Spawn.class;
            String replace;

            Util.setReason("Add buffs to players after death on monthly buff days.");
            CtClass ctBuffs = classPool.get("com.wurmonline.server.players.Player");
            replace = String.format("%s.spawnQuestion($0);", Spawn.class.getName());
            Util.insertBeforeDeclared(thisClass, ctBuffs, "spawn", replace);

        } catch (NotFoundException | IllegalArgumentException | ClassCastException e) {
            throw new HookException(e);
        }
    }
}
