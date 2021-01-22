package org.jubaroo.mods.wurm.server.creatures.bounty;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.jubaroo.mods.wurm.server.RequiemLogging;

public class Bounty {

    public static void init() throws CannotCompileException {
        RequiemLogging.logInfo("========= Initializing Bounty.init =========");
        try {
            final Class<Bounty> thisClass = Bounty.class;
            ClassPool classPool = HookManager.getInstance().getClassPool();
            CtClass ctCreature = classPool.get("com.wurmonline.server.creatures.Creature");
            CtMethod ctDoNew = ctCreature.getMethod("doNew", "(IZFFFILjava/lang/String;BBBZB)Lcom/wurmonline/server/creatures/Creature;");

            ctDoNew.instrument(new ExprEditor(){
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("sendToWorld")) {
                        m.replace("$_ = $proceed($$);"
                                + "mod.sin.wyvernmods.bestiary.MethodsBestiary.modifyNewCreature($1);");
                        return;
                    }
                }
            });

            // -- Enable adjusting size for creatures -- //
            CtClass ctCreatureStatus = classPool.get("com.wurmonline.server.creatures.CreatureStatus");
            ctCreatureStatus.getDeclaredMethod("getSizeMod").setBody("{return mod.sin.wyvernmods.bestiary.MethodsBestiary.getAdjustedSizeMod(this);}");

        } catch (NotFoundException e) {
            throw new HookException(e);
        }
    }
}
