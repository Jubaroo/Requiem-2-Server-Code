package org.jubaroo.mods.wurm.server.actions.vehicles;

import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.deities.Deity;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.skills.NoSuchSkillException;
import com.wurmonline.server.skills.Skill;
import com.wurmonline.server.skills.SkillList;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.utils.TweakApiPerms;
import org.jubaroo.mods.wurm.server.vehicles.CustomVehicles;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class AltarWagonPrayAction implements ModAction, BehaviourProvider, ActionPerformer {
    public final short actionId;
    public final Method prayResult;
    public final ActionEntry actionEntry;

    public AltarWagonPrayAction() {
        actionId = (short) ModActions.getNextActionId();
        prayResult = TweakApiPerms.getClassMeth(
                "com.wurmonline.server.behaviours.MethodsReligion",
                "prayResult",
                "com.wurmonline.server.creatures.Creature",
                "float",
                "com.wurmonline.server.deities.Deity",
                "int");
        int[] types = {
                ActionTypesProxy.ACTION_TYPE_FATIGUE,
                ActionTypesProxy.ACTION_TYPE_MISSION,
                ActionTypesProxy.ACTION_TYPE_SHOW_ON_SELECT_BAR,
        };
        actionEntry = ActionEntry.createEntry(actionId, "Pray", "Praying", types);
        try {
            ReflectionUtil.setPrivateField(actionEntry, ReflectionUtil.getField(ActionEntry.class, "maxRange"), 5);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        ModActions.registerAction(actionEntry);
    }

    @Override
    public short getActionId() {
        return actionId;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
        if (target == null) {
            return null;
        }
        if (target.getTemplateId() != CustomVehicles.altarWagon.getTemplateId()) {
            return null;
        }
        if (performer.getDeity() == null) {
            return null;
        }
        return Collections.singletonList(actionEntry);
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
        return getBehavioursFor(performer, null, target);
    }

    @Override
    public boolean action(Action action, Creature performer, Item target, short num, float counter) {
        return action(action, performer, null, target, num, counter);
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {
        Deity deity;
        Skill prayer;
        int time = action.getTimeLeft();
        float faith = performer.getFaith();
        Communicator comm = performer.getCommunicator();

        deity = performer.getDeity();
        if (deity == null) {
            comm.sendNormalServerMessage("You cannot pray without being a priest");
            return true;
        }
        try {
            prayer = performer.getSkills().getSkill(SkillList.PRAYER);
        } catch (NoSuchSkillException nss) {
            return true;
        }
        if (counter == 1f) {
            int remain = 300 - (int) prayer.getKnowledge() - ((int) target.getQualityLevel() / 2);
            //int remain =  (int) (300 - prayer.getKnowledge() - (target.getQualityLevel() / 2));
            performer.sendActionControl("praying", true, remain);
            action.setTimeLeft(remain);
            comm.sendNormalServerMessage(String.format("You begin to pray to %s", deity.name));
            return false;
        }

        if (counter * 10f >= (float) time) {
            float res = (float) prayer.skillCheck(prayer.getKnowledge(0.0) - (double) (30f + Server.rand.nextFloat() * 60f), faith, false, counter / 3f);
            int rare = action.getRarity();
            if (res > 0.0) {
                try {
                    prayResult.invoke(null, performer, res, deity, rare);
                } catch (IllegalAccessException e) {
                    RequiemLogging.logException("IllegalAccess: prayResult", e);
                } catch (InvocationTargetException e) {
                    RequiemLogging.logException("InvokeExc: prayResult", e);
                } catch (Throwable e) {
                    RequiemLogging.logException("InvokeExc: prayResult ", e);
                }
            }
            deity.increaseFavor();
            performer.checkPrayerFaith();
            comm.sendNormalServerMessage(String.format("You finish your prayer to %s", deity.name));
            return true;
        }
        return false;
    }

}
