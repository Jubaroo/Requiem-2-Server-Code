package org.jubaroo.mods.wurm.server.actions.items;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.banks.Bank;
import com.wurmonline.server.banks.Banks;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemTypes;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.questions.BankManagementQuestion;
import com.wurmonline.server.villages.NoSuchVillageException;
import com.wurmonline.server.villages.Village;
import com.wurmonline.server.villages.Villages;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.util.Collections;
import java.util.List;

public class PortableBankAction implements WurmServerMod, ItemTypes, MiscConstants, ModAction, BehaviourProvider, ActionPerformer {
    public static short actionId;
    static ActionEntry actionEntry;

    public PortableBankAction() {
        PortableBankAction.actionId = (short) ModActions.getNextActionId();
        ModActions.registerAction(PortableBankAction.actionEntry = ActionEntry.createEntry(PortableBankAction.actionId, "Bank", "Banking", new int[0]));
    }

    public BehaviourProvider getBehaviourProvider() {
        return this;
    }

    public ActionPerformer getActionPerformer() {
        return this;
    }

    public short getActionId() {
        return PortableBankAction.actionId;
    }

    public List<ActionEntry> getBehavioursFor(final Creature performer, final Item source, final Item target) {
        if (!(performer instanceof Player)) {
            return null;
        }
        if (target.isOwner(performer) && (target.getTemplateId() == CustomItems.portableBankId)) {
            return Collections.singletonList(PortableBankAction.actionEntry);
        }
        return null;
    }

    public boolean action(final Action act, final Creature performer, final Item source, final Item target, final short action, final float counter) {
        if (performer instanceof Player) {
            if (target.isOwner(performer)) {
                if (target.getTemplateId() != CustomItems.portableBankId) {
                    return true;
                }
                try {
                    final int villageId = target.getData2();
                    final Bank bank = Banks.getBank(performer.getWurmId());
                    if (bank != null) {
                        if (!performer.isGuest()) {
                            if (villageId == bank.currentVillage) {
                                if (!bank.open) {
                                    ((Player) performer).openBank();
                                } else {
                                    performer.getCommunicator().sendNormalServerMessage("Your bank account is already open.");
                                }
                            } else {
                                final BankManagementQuestion bm = new BankManagementQuestion(performer, "Bank management", "Move bank", target.getWurmId(), bank);
                                bm.sendQuestion();
                            }
                        } else {
                            performer.getCommunicator().sendNormalServerMessage("This feature is only available to paying players.");
                        }
                    } else if (!performer.isGuest()) {
                        try {
                            final Village v = Villages.getVillage(villageId);
                            if (v.kingdom == performer.getKingdomId()) {
                                final BankManagementQuestion bm2 = new BankManagementQuestion(performer, "Bank management", "Opening bank account", target.getWurmId(), null);
                                bm2.sendQuestion();
                            } else {
                                performer.getCommunicator().sendNormalServerMessage("You can't open a bank here.");
                            }
                        } catch (NoSuchVillageException nsv) {
                            RequiemLogging.logException(performer.getName() + ":" + nsv.getMessage(), nsv);
                        }
                    } else {
                        performer.getCommunicator().sendNormalServerMessage("This feature is only available to paying players.");
                    }
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
