package org.jubaroo.mods.wurm.server.actions.machinaOfFortune;

import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.tools.RequiemTools;
import org.jubaroo.mods.wurm.server.tools.SpellTools;

import java.util.Collections;
import java.util.List;

import static org.gotti.wurmunlimited.modsupport.actions.ActionPropagation.*;

public class MachinaOfFortuneAction implements ModAction, ActionPerformer, BehaviourProvider {
    private final short actionId;
    private final ActionEntry actionEntry;

    public MachinaOfFortuneAction() {
        this.actionId = (short) ModActions.getNextActionId();
        // Create the action entry
        this.actionEntry = new ActionEntryBuilder(this.actionId, "Insert coin", "inserting", new int[]{ActionTypesProxy.ACTION_TYPE_NOMOVE}).range(4).build();
        // Register the action entry
        ModActions.registerAction(this.actionEntry);
    }

    @Override
    public short getActionId() {
        return this.actionId;
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
        if (performer instanceof Player && target != null && (target.getTemplateId() == CustomItems.machinaOfFortuneId && source.isCoin()))
            return Collections.singletonList(this.actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {
        float rollMultiplier;
        final byte spellId = 121;
        final int timeLeft = (int) (50 - ((SpellTools.getSpellCastPower(target, spellId) / 4)));

        try {
            if (SpellTools.getSpellCastPower(target, spellId) <= 0) {
                performer.getCommunicator().sendNormalServerMessage("The device must be enchanted with Labouring Spirits before it can begin working.");
                return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (SpellTools.getSpellCastPower(target, spellId) > 0 && SpellTools.getSpellCastPower(target, spellId) < 20) {
                performer.getCommunicator().sendNormalServerMessage("The device needs a stronger enchantment before it can begin working.");
                return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (target.getTemplate().getTemplateId() != CustomItems.machinaOfFortuneId || !source.isCoin()) {
                performer.getCommunicator().sendNormalServerMessage("That cannot be done!");
                return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (!target.isPlanted()) {
                performer.getCommunicator().sendNormalServerMessage(String.format("The %s is not firmly secured to the ground and so it cannot be safely used.", target.getName()));
                return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (counter == 1f) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You pull a coin from your pocket and insert it into a small slot on the %s. The machine starts to hum with magic...", target.getName()));
                Server.getInstance().broadCastAction(String.format("%s pulls %s %s from %s pocket and inserts it into the %s.", performer.getName(), RequiemTools.a_an(source.getName()), source.getName(), performer.getHisHerItsString(), target.getName()), performer, 5);
                action.setTimeLeft(Math.max(timeLeft, 0));
                performer.sendActionControl("waiting for fortune", true, action.getTimeLeft());
            } else if (counter * 10f > action.getTimeLeft()) {
                if (source.getTemplate().getTemplateId() == ItemList.coinIron) {
                    rollMultiplier = 2f;
                    CoinInsertion.coinInsert(performer, performer.getCommunicator(), source, rollMultiplier);
                }
                if (source.getTemplate().getTemplateId() == ItemList.coinIronFive) {
                    rollMultiplier = 1.8f;
                    CoinInsertion.coinInsert(performer, performer.getCommunicator(), source, rollMultiplier);
                }
                if (source.getTemplate().getTemplateId() == ItemList.coinIronTwenty) {
                    rollMultiplier = 1.6f;
                    CoinInsertion.coinInsert(performer, performer.getCommunicator(), source, rollMultiplier);
                }
                if (source.getTemplate().getTemplateId() == ItemList.coinCopper) {
                    rollMultiplier = 1.4f;
                    CoinInsertion.coinInsert(performer, performer.getCommunicator(), source, rollMultiplier);
                }
                if (source.getTemplate().getTemplateId() == ItemList.coinCopperFive) {
                    rollMultiplier = 1.2f;
                    CoinInsertion.coinInsert(performer, performer.getCommunicator(), source, rollMultiplier);
                }
                if (source.getTemplate().getTemplateId() == ItemList.coinCopperTwenty) {
                    rollMultiplier = 1f;
                    CoinInsertion.coinInsert(performer, performer.getCommunicator(), source, rollMultiplier);
                }
                if (source.getTemplate().getTemplateId() == ItemList.coinSilver) {
                    rollMultiplier = 0f;
                    CoinInsertion.coinInsert(performer, performer.getCommunicator(), source, rollMultiplier);
                }
                if (source.getTemplate().getTemplateId() == ItemList.coinSilverFive) {
                    rollMultiplier = -0.5f;
                    CoinInsertion.coinInsert(performer, performer.getCommunicator(), source, rollMultiplier);
                }
                if (source.getTemplate().getTemplateId() == ItemList.coinSilverTwenty) {
                    rollMultiplier = -1f;
                    CoinInsertion.coinInsert(performer, performer.getCommunicator(), source, rollMultiplier);
                }
                if (source.getTemplate().getTemplateId() == ItemList.coinGold) {
                    rollMultiplier = -1.5f;
                    for (int i = 0; i < 2; i++) {
                        CoinInsertion.coinInsert(performer, performer.getCommunicator(), source, rollMultiplier);
                    }
                }
                if (source.getTemplate().getTemplateId() == ItemList.coinGoldFive) {
                    rollMultiplier = -2f;
                    for (int i = 0; i < 3; i++) {
                        CoinInsertion.coinInsert(performer, performer.getCommunicator(), source, rollMultiplier);
                    }
                }
                if (source.getTemplate().getTemplateId() == ItemList.coinGoldTwenty) {
                    rollMultiplier = -3f;
                    for (int i = 0; i < 4; i++) {
                        CoinInsertion.coinInsert(performer, performer.getCommunicator(), source, rollMultiplier);
                    }
                }
                RequiemLogging.logInfo(String.format("%s has used %s %s %s on a %s", RequiemTools.a_an_FromTemplateId(source.getTemplateId()), performer.getNameWithoutPrefixes(), source.getMaterial(), source.getName(), target.getName()));
                return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
            }
            return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
        } catch (Exception e) {
            e.printStackTrace();
            performer.getCommunicator().sendNormalServerMessage(String.format("Machina Of Fortune action error: %s", e.toString()));
            performer.getCommunicator().sendNormalServerMessage("Please inform a GM by opening a ticket (by typing /support) and provide the message above. Thank you.");
            return propagate(action, FINISH_ACTION, NO_SERVER_PROPAGATION, NO_ACTION_PERFORMER_PROPAGATION);
        }
    }

}




