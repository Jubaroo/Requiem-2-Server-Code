package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.deities.Deities;
import com.wurmonline.server.epic.EpicServerStatus;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.util.Collections;
import java.util.List;

public class MissionAddAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public MissionAddAction() {
        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Add Epic Mission",
                "generating",
                new int[]{0}
                //new int[] { 6 /* ACTION_TYPE_NOMOVE */ }	// 6 /* ACTION_TYPE_NOMOVE */, 48 /* ACTION_TYPE_ENEMY_ALWAYS */, 36 /* ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM */
        );
        ModActions.registerAction(actionEntry);
    }

    @Override
    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            // Menu with activated object
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item object) {
                return this.getBehavioursFor(performer, object);
            }

            // Menu without activated object
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item object) {
                if (performer instanceof Player && object != null && (object.getTemplateId() == ItemList.bodyBody || object.getTemplateId() == ItemList.bodyHand) && performer.getPower() >= MiscConstants.POWER_IMPLEMENTOR) {
                    return Collections.singletonList(actionEntry);
                }

                return null;
            }
        };
    }

    @Override
    public ActionPerformer getActionPerformer() {
        return new ActionPerformer() {

            @Override
            public short getActionId() {
                return actionId;
            }

            // Without activated object
            @Override
            public boolean action(Action act, Creature performer, Item target, short action, float counter) {
                if (performer instanceof Player) {
                    if (performer.getPower() < 5) {
                        performer.getCommunicator().sendNormalServerMessage("You do not have permission to do that.");
                        return true;
                    }
                    Player player = (Player) performer;
                    int[] deityNums = {
                            1, 2, 3, 4, // Original Gods
                            6, 7, 8, 9, 10, 11, 12 // Valrei Entities
                    };
                    if (EpicServerStatus.getCurrentEpicMissions().length >= deityNums.length) {
                        RequiemLogging.logInfo("All entities already have a mission. Aborting.");
                        return true;
                    }
                    int i = 10;
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
                            return true;
                        }
                    }
                    RequiemLogging.logInfo(String.format("Entity number = %d", number));
                    String entityName = Deities.getDeityName(number);
                    RequiemLogging.logInfo(String.format("Entity name = %s", entityName));
                    int time = 604800;
                    RequiemLogging.logInfo(String.format("Current epic missions: %d", EpicServerStatus.getCurrentEpicMissions().length));
                    EpicServerStatus es = new EpicServerStatus();
                    if (EpicServerStatus.getCurrentScenario() != null) {
                        es.generateNewMissionForEpicEntity(number, entityName, -1, time, EpicServerStatus.getCurrentScenario().getScenarioName(), EpicServerStatus.getCurrentScenario().getScenarioNumber(), EpicServerStatus.getCurrentScenario().getScenarioQuest(), true);
                    }
                } else {
                    RequiemLogging.logInfo(String.format("Somehow a non-player activated action ID %d...", actionEntry.getNumber()));
                }
                return true;
            }

            @Override
            public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
                return this.action(act, performer, target, action, counter);
            }


        }; // ActionPerformer
    }
}