package org.jubaroo.mods.wurm.server.creatures;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.Server;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.Creatures;
import com.wurmonline.server.villages.Village;
import com.wurmonline.server.villages.Villages;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import mod.sin.lib.Util;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.tools.RandomUtils;

import java.util.ArrayList;

public class RareSpawns {
    public static ArrayList<Creature> rares = new ArrayList<>();

    public static void spawnRandomLocationCreature(int templateId) {
        boolean found = false;
        int spawnX = 2048;
        int spawnY = 2048;
        while (!found) {
            int x = Server.rand.nextInt(Server.surfaceMesh.getSize());
            int y = Server.rand.nextInt(Server.surfaceMesh.getSize());
            short height = Tiles.decodeHeight(Server.surfaceMesh.getTile(x, y));
            if (height > 0 && height < 1000 && Creature.getTileSteepness(x, y, true)[1] < 30) {
                Village v = Villages.getVillage(x, y, true);
                if (v == null) {
                    for (int vx = -50; vx < 50; vx += 5) {
                        for (int vy = -50; vy < 50 && (v = Villages.getVillage(x + vx, y + vy, true)) == null; vy += 5) {
                        }
                        if (v != null) {
                            break;
                        }
                    }
                }
                if (v != null) {
                    continue;
                }
                spawnX = x * 4;
                spawnY = y * 4;
                found = true;
            }
        }
        try {
            RequiemLogging.logInfo(String.format("Spawning new rare creature(%d) at %s, %s", templateId, spawnX * 0.25f, spawnY * 0.25f));
            Creature.doNew(templateId, spawnX, spawnY, RandomUtils.getRandomRotation(), 0, "", Server.rand.nextBoolean() ? (byte) 0 : (byte) 1);
        } catch (Exception e) {
            RequiemLogging.logException("Failed to create Rare Spawn.", e);
            e.printStackTrace();
        }
    }

    public static void pollRareSpawns() {
        Creature[] creatures = Creatures.getInstance().getCreatures();
        for (Creature c : creatures) {
            if (MethodsBestiary.isRareCreature(c) && !rares.contains(c)) {
                rares.add(c);
                RequiemLogging.logInfo(String.format("Existing rare spawn identified (%s). Adding to rares list.", c.getName()));
            }
        }
        int i = 0;
        while (i < rares.size()) {
            if (rares.get(i).isDead()) {
                rares.remove(rares.get(i));
                RequiemLogging.logInfo(String.format("Rare spawn was found dead (%s). Removing from rares list.", rares.get(i).getName()));
            } else {
                i++;
            }
        }
        if (rares.isEmpty()) {
            RequiemLogging.logInfo("No rare spawn was found. Spawning a new one.");
            int[] rareTemplates = {CustomCreatures.reaperId, CustomCreatures.spectralDragonHatchlingId};
            int rareTemplateId = rareTemplates[Server.rand.nextInt(rareTemplates.length)];
            if (rareTemplateId > 0) {
                spawnRandomLocationCreature(rareTemplateId);
            }
        }
    }

    public static void preInit() {
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();
            Class<RareSpawns> thisClass = RareSpawns.class;
            String replace;

            /*Util.setReason("Disable casting Smite on cluckster.");
            CtClass ctSmite = classPool.get("com.wurmonline.server.spells.Smite");
            replace = "if("+RareSpawns.class.getName()+".isRareCreature($3)){"
                    + "  $2.getCommunicator().sendNormalServerMessage(\"This creature is immune to Smite.\");"
                    + "  return false;"
                    + "}";
            Util.insertBeforeDeclared(thisClass, ctSmite, "precondition", replace);*/

            Util.setReason("Disable casting Worm Brains on rares.");
            CtClass ctWormBrains = classPool.get("com.wurmonline.server.spells.WormBrains");
            replace = String.format("if(%s.isRareCreature($3)){  $2.getCommunicator().sendNormalServerMessage(\"This creature is immune to Worm Brains.\");  return false;}", MethodsBestiary.class.getName());
            Util.insertBeforeDeclared(thisClass, ctWormBrains, "precondition", replace);

            //removed for update 1.9.0.0
            //Util.setReason("Increase titan extra damage to pets.");
            //CtClass ctCreature = classPool.get("com.wurmonline.server.creatures.Creature");
            //CtClass ctString = classPool.get("java.lang.String");
            //CtClass ctBattle = classPool.get("com.wurmonline.server.combat.Battle");
            //CtClass ctCombatEngine = classPool.get("com.wurmonline.server.combat.CombatEngine");
            //// @Nullable Creature performer, Creature defender, byte type, int pos, double damage, float armourMod,
            //// String attString, @Nullable Battle battle, float infection, float poison, boolean archery, boolean alreadyCalculatedResist
            //CtClass[] params1 = {
            //        ctCreature,
            //        ctCreature,
            //        CtClass.byteType,
            //        CtClass.intType,
            //        CtClass.doubleType,
            //        CtClass.floatType,
            //        ctString,
            //        ctBattle,
            //        CtClass.floatType,
            //        CtClass.floatType,
            //        CtClass.booleanType,
            //        CtClass.booleanType
            //};
            //String desc1 = Descriptor.ofMethod(CtClass.booleanType, params1);
            //replace = "if($2.isDominated() && $1 != null && " + MethodsBestiary.class.getName() + ".isRareCreature($1)){" +
            //        //"  RequiemLogging.logInfo(\"Detected rare spawn hit on a pet. Adding damage.\");" +
            //        "  $5 = $5 * 2d;" +
            //        "}";
            //Util.insertBeforeDescribed(thisClass, ctCombatEngine, "addWound", desc1, replace);
        } catch (NotFoundException e) {
            throw new HookException(e);
        }
    }
}
