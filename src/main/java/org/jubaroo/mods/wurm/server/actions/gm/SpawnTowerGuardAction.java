package org.jubaroo.mods.wurm.server.actions.gm;

import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.kingdom.GuardTower;
import com.wurmonline.server.kingdom.Kingdoms;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.villages.GuardPlan;
import com.wurmonline.server.villages.Village;
import com.wurmonline.server.villages.Villages;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpawnTowerGuardAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final short actionID;
    private final ActionEntry actionEntry;

    public SpawnTowerGuardAction() {
        actionID = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(actionID, "GM Spawn Guard", "spawning guard", new int[
                ActionTypesProxy.ACTION_TYPE_IGNORERANGE
                ]);
        ModActions.registerAction(actionEntry);
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item target) {
        return getBehavioursFor(performer, null, target);
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item activated, Item target) {
        if (performer.getPower() > 0 && (IsGuardTower(target.getTemplateId()) || IsSettlementToken(target.getTemplateId())))
            return Collections.singletonList(actionEntry);

        return null;
    }

    @Override
    public short getActionId() {
        return actionID;
    }

    @Override
    public boolean action(Action action, Creature performer, Item activated, Item target, short num, float counter) {
        if (!(performer instanceof Player))
            return true;

        try {
            if (IsGuardTower(target.getTemplateId())) {
                GuardTower tower = Kingdoms.getTower(target);

                if (tower == null) {
                    performer.getCommunicator().sendNormalServerMessage("This is not a guard tower.");
                    return true;
                }

                tower.getClass().getDeclaredMethod("pollGuards").invoke(tower);
            } else if (IsSettlementToken(target.getTemplateId())) {
                Village village = Villages.getVillage(target.getTilePos(), target.isOnSurface());
                if (village == null) {
                    performer.getCommunicator().sendNormalServerMessage("Village for settlement token could not be found.");
                    return true;
                }

                GuardPlan.class.getDeclaredMethod("pollGuards").invoke(village.plan);
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            Logger.getLogger(SpawnTowerGuardAction.class.getName())
                    .log(Level.SEVERE, "Manual tower guard spawning failed.", e);
            performer.getCommunicator().sendNormalServerMessage("Manual tower guard spawning failed.");
        }

        return true;
    }

    @Override
    public boolean action(Action action, Creature performer, Item target, short num, float counter) {
        return action(action, performer, null, target, num, counter);
    }

    public boolean IsGuardTower(int templateID) {
        return templateID == ItemList.guardTower || templateID == ItemList.guardTowerHots || templateID == ItemList.guardTowerMol || templateID == ItemList.guardTowerFreedom;

    }

    public boolean IsSettlementToken(int templateID) {
        return (templateID == ItemList.villageToken);
    }
}
/*
[04:47:03 PM] SEVERE SpawnTowerGuardAction: Manual tower guard spawning failed.
java.lang.NoSuchMethodException: com.wurmonline.server.villages.GuardPlan.pollGuards()
	at java.lang.Class.getMethod(Class.java:1786)
	at SpawnTowerGuardAction.action(SpawnTowerGuardAction.java:65)
	at SpawnTowerGuardAction.action(SpawnTowerGuardAction.java:76)
	at org.gotti.wurmunlimited.modsupport.actions.ActionPerformerBehaviour.action(ActionPerformerBehaviour.java:82)
	at org.gotti.wurmunlimited.modsupport.actions.WrappedBehaviour.lambda$9(WrappedBehaviour.java:102)
	at org.gotti.wurmunlimited.modsupport.actions.WrappedBehaviour.action(WrappedBehaviour.java:211)
	at org.gotti.wurmunlimited.modsupport.actions.WrappedBehaviour.action(WrappedBehaviour.java:102)
	at org.gotti.wurmunlimited.modsupport.actions.ActionPerformerChain.action(ActionPerformerChain.java:77)
	at com.wurmonline.server.behaviours.Action.poll(Action.java:3587)
	at com.wurmonline.server.behaviours.ActionStack.poll(ActionStack.java:243)
	at com.wurmonline.server.players.Player.pollActions(Player.java:9525)
	at com.wurmonline.server.Players.pollPlayers$1(Players.java:4543)
	at sun.reflect.GeneratedMethodAccessor23.invoke(Unknown Source)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.nyxcode.wurm.timedpay.TimedPay.lambda$null$0(TimedPay.java:41)
	at org.gotti.wurmunlimited.modloader.classhooks.HookManager.invoke(HookManager.java:344)
	at com.wurmonline.server.Players.pollPlayers$2(Players.java)
	at sun.reflect.GeneratedMethodAccessor22.invoke(Unknown Source)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.gotti.wurmunlimited.modloader.server.ProxyServerHook$6$1.invoke(ProxyServerHook.java:181)
	at org.gotti.wurmunlimited.modloader.classhooks.HookManager.invoke(HookManager.java:344)
	at com.wurmonline.server.Players.pollPlayers(Players.java)
	at com.wurmonline.server.Server.run(Server.java:1905)
	at java.util.TimerThread.mainLoop(Stopwatch.java:555)
	at java.util.TimerThread.run(Stopwatch.java:505)

 */