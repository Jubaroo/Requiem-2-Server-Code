package org.jubaroo.mods.wurm.server.utils;

import com.wurmonline.server.Server;
import com.wurmonline.server.creatures.CreatureTemplate;
import com.wurmonline.server.deities.Deities;
import com.wurmonline.server.epic.EpicMission;
import com.wurmonline.server.epic.EpicServerStatus;
import com.wurmonline.server.players.PlayerInfo;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import mod.sin.lib.Util;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.creatures.MethodsBestiary;
import org.jubaroo.mods.wurm.server.creatures.Titans;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class MissionCreator {

    public static void pollMissions() {
        int[] deityNums = {
                1, 2, 3, 4, // Original Gods
                6, 7, 8, 9, 10, 11, 12 // Valrei Entities
        };
        EpicServerStatus es = new EpicServerStatus();
        EpicMission[] missions = EpicServerStatus.getCurrentEpicMissions();
        int i = 0;
        while (i < missions.length) {
            if (missions[i].isCurrent()) {
                if (missions[i].isCompleted() || missions[i].getEndTime() >= System.currentTimeMillis()) {
                    try {
                        int entityId = missions[i].getEpicEntityId();
                        RequiemLogging.debug(String.format("Detected an existing current mission for %s that was completed or expired. Removing now.", Deities.getDeityName(entityId)));
                        ReflectionUtil.callPrivateMethod(EpicServerStatus.class, ReflectionUtil.getMethod(EpicServerStatus.class, "destroyLastMissionForEntity"), entityId);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
            i++;
        }
        if (EpicServerStatus.getCurrentEpicMissions().length >= deityNums.length) {
            RequiemLogging.debug("All entities already have a mission. Aborting.");
            return;
        }
        i = 10;
        int number = 1;
        while (i > 0) {
            number = deityNums[Server.rand.nextInt(deityNums.length)];
            RequiemLogging.debug(String.format("Testing number %d", number));
            if (EpicServerStatus.getEpicMissionForEntity(number) == null) {
                RequiemLogging.debug("Has no mission, breaking loop.");
                break;
            } else {
                RequiemLogging.debug("Has mission, finding new number.");
            }
            i++;
            if (i == 0) {
                RequiemLogging.debug("Ran through 10 possible entities and could not find empty mission. Cancelling.");
                return;
            }
        }
        RequiemLogging.debug(String.format("Entity number = %d", number));
        String entityName = Deities.getDeityName(number);
        RequiemLogging.debug(String.format("Entity name = %s", entityName));
        int time = 604800;
        RequiemLogging.debug(String.format("Current epic missions: %d", EpicServerStatus.getCurrentEpicMissions().length));
        if (EpicServerStatus.getCurrentScenario() != null) {
            es.generateNewMissionForEpicEntity(number, entityName, -1, time, EpicServerStatus.getCurrentScenario().getScenarioName(), EpicServerStatus.getCurrentScenario().getScenarioNumber(), EpicServerStatus.getCurrentScenario().getScenarioQuest(), true);
        }
    }

    public static void awardMissionBonus(PlayerInfo info) {
        try {
            info.setMoney(info.money + 2000 + Server.rand.nextInt(2000));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isMissionOkaySlayable(CreatureTemplate template) {
        if (template.isSubmerged()) {
            return false;
        }
        if (template.isUnique()) {
            return false;
        }
        if (MethodsBestiary.isRareCreature(template.getTemplateId())) {
            return false;
        }
        if (Titans.isTitan(template.getTemplateId()) || Titans.isTitanMinion(template.getTemplateId())) {
            return false;
        }
        return template.isEpicMissionSlayable();
    }

    public static boolean isMissionOkayHerbivore(CreatureTemplate template) {
        if (template.isSubmerged()) {
            return false;
        }
        if (template.isUnique()) {
            return false;
        }
        if (MethodsBestiary.isRareCreature(template.getTemplateId())) {
            return false;
        }
        if (Titans.isTitan(template.getTemplateId()) || Titans.isTitanMinion(template.getTemplateId())) {
            return false;
        }
        return template.isHerbivore();
    }

    public static void preInit() {
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();
            final Class<MissionCreator> thisClass = MissionCreator.class;
            String replace;

            Util.setReason("Give players currency for completing a mission.");
            CtClass ctTriggerEffect = classPool.get("com.wurmonline.server.tutorial.TriggerEffect");
            replace = String.format("$_ = $proceed($$);%s.awardMissionBonus($0);", MissionCreator.class.getName());
            Util.instrumentDeclared(thisClass, ctTriggerEffect, "effect", "addToSleep", replace);

            Util.setReason("Prevent mission creatures from spawning in water.");
            CtClass ctEpicServerStatus = classPool.get("com.wurmonline.server.epic.EpicServerStatus");
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
            CtClass ctEpicMissionEnum = classPool.get("com.wurmonline.server.epic.EpicMissionEnum");
            replace = "{ if($0.getMissionType() == 108 || $0.getMissionType() == 120 || $0.getMissionType() == 124){  return 0;}return $0.missionChance; }";
            Util.setBodyDeclared(thisClass, ctEpicMissionEnum, "getMissionChance", replace);

        } catch (NotFoundException | IllegalArgumentException | ClassCastException e) {
            throw new HookException(e);
        }
    }

}
