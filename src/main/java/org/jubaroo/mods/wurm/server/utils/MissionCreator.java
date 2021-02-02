package org.jubaroo.mods.wurm.server.utils;

import com.wurmonline.server.Server;
import com.wurmonline.server.creatures.CreatureTemplate;
import com.wurmonline.server.deities.Deities;
import com.wurmonline.server.epic.EpicMission;
import com.wurmonline.server.epic.EpicServerStatus;
import com.wurmonline.server.players.PlayerInfo;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.creatures.CreatureTweaks;
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
                        RequiemLogging.logInfo(String.format("Detected an existing current mission for %s that was completed or expired. Removing now.", Deities.getDeityName(entityId)));
                        ReflectionUtil.callPrivateMethod(EpicServerStatus.class, ReflectionUtil.getMethod(EpicServerStatus.class, "destroyLastMissionForEntity"), entityId);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        RequiemLogging.logException("[ERROR] in pollMissions in MissionCreator", e);
                    }
                }
            }
            i++;
        }
        if (EpicServerStatus.getCurrentEpicMissions().length >= deityNums.length) {
            RequiemLogging.logInfo("All entities already have a mission. Aborting.");
            return;
        }
        i = 10;
        int number = 1;
        while (i > 0) {
            number = deityNums[Server.rand.nextInt(deityNums.length)];
            RequiemLogging.logInfo(String.format("Testing number %d", number));
            if (EpicServerStatus.getEpicMissionForEntity(number) == null) {
                RequiemLogging.logInfo("Has no mission, breaking loop.");
                break;
            } else {
                RequiemLogging.logInfo("Has mission, finding new number.");
            }
            i++;
            if (i == 0) {
                RequiemLogging.logInfo("Ran through 10 possible entities and could not find empty mission. Cancelling.");
                return;
            }
        }
        RequiemLogging.logInfo(String.format("Entity number = %d", number));
        String entityName = Deities.getDeityName(number);
        RequiemLogging.logInfo(String.format("Entity name = %s", entityName));
        int time = 604800;
        RequiemLogging.logInfo(String.format("Current epic missions: %d", EpicServerStatus.getCurrentEpicMissions().length));
        if (EpicServerStatus.getCurrentScenario() != null) {
            es.generateNewMissionForEpicEntity(number, entityName, -1, time, EpicServerStatus.getCurrentScenario().getScenarioName(), EpicServerStatus.getCurrentScenario().getScenarioNumber(), EpicServerStatus.getCurrentScenario().getScenarioQuest(), true);
        }
    }

    public static void awardMissionBonus(PlayerInfo info) {
        try {
            info.setMoney(info.money + 2000 + Server.rand.nextInt(2000));
        } catch (IOException e) {
            RequiemLogging.logException("[ERROR] in awardMissionBonus in MissionCreator", e);
        }
    }

    public static boolean isMissionOkaySlayable(CreatureTemplate template) {
        if (template.isSubmerged()) {
            return false;
        }
        if (template.isUnique()) {
            return false;
        }
        if (CreatureTweaks.isRareCreature(template.getTemplateId())) {
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
        if (CreatureTweaks.isRareCreature(template.getTemplateId())) {
            return false;
        }
        if (Titans.isTitan(template.getTemplateId()) || Titans.isTitanMinion(template.getTemplateId())) {
            return false;
        }
        return template.isHerbivore();
    }

}
