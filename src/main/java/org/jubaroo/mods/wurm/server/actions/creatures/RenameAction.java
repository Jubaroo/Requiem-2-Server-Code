package org.jubaroo.mods.wurm.server.actions.creatures;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.questions.RenameQuestion;
import com.wurmonline.server.villages.Village;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RenameAction implements ModAction, BehaviourProvider, ActionPerformer {

    private final short actionId;
    private final ActionEntry actionEntry;
    Village vCheck;

    public RenameAction() {
        actionId = (short) ModActions.getNextActionId();
        actionEntry = new ActionEntryBuilder(actionId, "Rename Creature", "renaming", new int[] {
                ActionTypesProxy.ACTION_TYPE_ENEMY_NEVER,
                ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM
        }).build();
        ModActions.registerAction(actionEntry);
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item subject, Creature target) {
        if (subject != null && subject.getTemplateId() == ItemList.wandDeity || Objects.requireNonNull(subject).getTemplateId() == ItemList.wandGM || subject.getTemplateId() == CustomItems.creatureRenameCertificateId && target.getTemplate().getTemplateId() == 32 || target.getTemplate().getTemplateId() == 33) {
            return Collections.singletonList(actionEntry);
        } else {
            return null;
        }
    }

    @Override
    public short getActionId() {
        return actionId;
    }

    public boolean action(Action action, Creature performer, Item subject, Creature target, short num, float counter) {
        if (target.isAnimal() || target.isMonster() || !target.isUnique() || !target.isNpc()) { // Sanity check, and a fallback if someone uses actions mod.
            vCheck = performer.getCitizenVillage();
            if (performer.getPower() >= MiscConstants.POWER_DEMIGOD ) {
                RenameQuestion.send(performer, target, subject);
            } else {

                // Hopefully a workaround for the server modloader's non-handling of null variables.
                if (performer.getVillageId() == 0) {
                    performer.getCommunicator().sendNormalServerMessage("You may only rename creatures that belong to your own deed!");
                    return true;
                }

                if (target.getVillageId() == performer.getVillageId()) {
                    if (vCheck.getMayor().getId() == performer.getWurmId()) {
                        RenameQuestion.send(performer, target, subject);
                    } else {
                        performer.getCommunicator().sendNormalServerMessage("You must be mayor to do this!");
                    }
                } else {
                    performer.getCommunicator().sendNormalServerMessage("You may only rename spirit templars or spirit shadows that belong to your own deed!");
                }
            }
        } else {
            performer.getCommunicator().sendNormalServerMessage("Nicknames may only be given to creatures!");
        }

        return true;
    }

}