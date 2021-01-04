package org.jubaroo.mods.wurm.server.actions.special.machinaOfFortune;

import com.wurmonline.server.Server;
import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.behaviours.Methods;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.players.Player;
import com.wurmonline.shared.constants.SoundNames;
import com.wurmonline.shared.util.MaterialUtilities;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.utils.Cooldowns;

import java.util.Collections;
import java.util.List;

import static org.gotti.wurmunlimited.modsupport.actions.ActionPropagation.*;

public class MachinaOfFortuneInstructionsAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final ActionEntry actionEntry;

    public MachinaOfFortuneInstructionsAction() {
        actionEntry = ActionEntry.createEntry((short) ModActions.getNextActionId(), "Inspect the device", "inspecting", new int[]{
                ActionTypesProxy.ACTION_TYPE_NOMOVE
        });
        ModActions.registerAction(actionEntry);
    }

    @Override
    public short getActionId() {
        return actionEntry.getNumber();
    }

    @Override
    public BehaviourProvider getBehaviourProvider() {
        return this;
    }

    @Override
    public ActionPerformer getActionPerformer() {
        return this;
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
        if (performer instanceof Player && target != null && (target.getTemplateId() == CustomItems.machinaOfFortuneId))
            return Collections.singletonList(actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {
        int timeLeft = 20;
        final String playerEffect = String.format("%s%s", performer.getName(), MachinaOfFortuneInstructionsAction.class.getName());
        long cooldown = TimeConstants.DAY_MILLIS;
        try {
            if (!performer.isWithinDistanceTo(target, 8f)) {
                performer.getCommunicator().sendNormalServerMessage("You must be closer to use that.");
                return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (target.getTemplate().getTemplateId() != CustomItems.machinaOfFortuneId) {
                performer.getCommunicator().sendNormalServerMessage("That cannot be done!");
                return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (!target.isPlanted()) {
                performer.getCommunicator().sendNormalServerMessage(String.format("The %s is not firmly secured to the ground and so it cannot be safely used.", target.getName()));
                return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (Cooldowns.isOnCooldown(playerEffect, cooldown) && performer.getPower() == 0) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You will need to wait %s before you can do that again.", Cooldowns.timeRemaining(playerEffect, cooldown)));
                return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (counter == 1f) {
                performer.getCommunicator().sendNormalServerMessage("You notice a small button hidden in the patterns of the device. You push it and the machine starts to faintly hum with magic.");
                Server.getInstance().broadCastAction(String.format("%s pushes a hidden button on the %s.", performer.getName(), target.getName()), performer, 5);
                action.setTimeLeft(timeLeft);
                performer.sendActionControl("inspecting", true, timeLeft);
            } else if (counter * 10f > action.getTimeLeft()) {
                // Note
                final Item papyrus = ItemFactory.createItem(ItemList.papyrusSheet, 10f, MaterialUtilities.COMMON, "");
                final String str = "\";maxlines=\"0\"}text{text=\"Activate any denomination of coin from your inventory.\n\nRight-click the Machina of Fortunes and choose 'Insert coin'.\n\nSit back and wait for your fortune, or misfortune.\n\nThe more valuable the coin that is inserted into the machine, the greater the chance to get a better reward. \n\nYou must enchant the machine with the spell Labouring Spirit in order for it to work. \n\nThe enchantment power must be 20 or greater. \n\nThe machine must be firmly planted to the ground. \n\nYou can burn this note after reading to easily dispose of it. \n\n \n";
                papyrus.setInscription(str, "Requiem Staff");
                papyrus.setName("Machina of Fortunes Instructions");
                performer.getInventory().insertItem(papyrus, true);
                Methods.sendSound(performer, SoundNames.HUMM_SND);
                Cooldowns.setUsed(playerEffect);
                performer.getCommunicator().sendNormalServerMessage(String.format("A %s magically appears in your inventory with instructions on how to use the machine.", papyrus.getName()));
                return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
            }
            return propagate(action, CONTINUE_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
        } catch (Exception e) {
            e.printStackTrace();
            performer.getCommunicator().sendNormalServerMessage(String.format("Machina Of Fortune action error: %s", e.toString()));
            performer.getCommunicator().sendNormalServerMessage("Please inform a GM by opening a ticket (/support) and provide the message above. Thank you.");
            return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
        }
    }
}