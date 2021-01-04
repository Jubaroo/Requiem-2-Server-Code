package org.jubaroo.mods.wurm.server.misc;

import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.economy.Economy;
import com.wurmonline.server.items.Item;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.jubaroo.mods.wurm.server.RequiemLogging;

public class Fishing {

    public static boolean catchFishHook(Creature performer, Item target, Item fish) {
        double fishBounty = fish.getFullWeight() * 0.005;
        if (fishBounty >= 1) {
            performer.getCommunicator().sendNormalServerMessage(String.format("You find %s iron coins inside the %s. You put that into your pocket.", (int) fishBounty, fish.getName()));
            Item[] coins = Economy.getEconomy().getCoinsFor((long) fishBounty);
            for (Item coin : coins) {
                performer.getInventory().insertItem(coin, true);
            }
        }
        return (performer.getInventory().insertItem(fish) || target.insertItem(fish))/* && performer.addMoney((int) fishBounty)*/;
    }

    public static void preInit() throws NotFoundException, CannotCompileException {
        ClassPool classPool = HookManager.getInstance().getClassPool();

        // added to make caught fish also give coin
        CtClass ctFishing = classPool.get("com.wurmonline.server.behaviours.MethodsFishing");

        ctFishing.getMethod("makeDeadFish", "(Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/behaviours/Action;Lcom/wurmonline/server/skills/Skill;BLcom/wurmonline/server/items/Item;Lcom/wurmonline/server/items/Item;)I")
                .instrument(new ExprEditor() {
                    @Override
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getMethodName().equals("insertItem")) {
                            m.replace(String.format("$_=%s.catchFishHook(performer, $0, $1);", Fishing.class.getName()));
                            RequiemLogging.logInfo(String.format("Injected fish bounty in makeDeadFish() at %d", m.getLineNumber()));
                        }
                    }
                });
    }

}
