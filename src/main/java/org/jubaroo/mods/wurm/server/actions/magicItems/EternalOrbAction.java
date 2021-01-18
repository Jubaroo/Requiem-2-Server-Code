package org.jubaroo.mods.wurm.server.actions.magicItems;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.Items;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.*;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.spells.SpellEffect;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.util.Collections;
import java.util.List;

public class EternalOrbAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public EternalOrbAction() {
        RequiemLogging.logWarning("EternalOrbAction()");

        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Absorb enchants",
                "absorbing",
                new int[0]
        );
        ModActions.registerAction(actionEntry);
    }


    @Override
    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            // Menu with activated object
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item object) {
                if (performer instanceof Player && source != null && object != null && source.getTemplateId() == CustomItems.eternalOrbId && source != object) {
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

            // With activated object
            @Override
            public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
                if (performer instanceof Player) {
                    Player player = (Player) performer;
                    if (source.getTemplate().getTemplateId() != CustomItems.eternalOrbId) {
                        player.getCommunicator().sendNormalServerMessage("You must use an Eternal Orb to absorb enchants.");
                        return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                    }
                    if (source.getWurmId() == target.getWurmId()) {
                        player.getCommunicator().sendNormalServerMessage("You cannot absorb the orb with itself!");
                        return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                    }
                    if (target.getTemplateId() == ItemList.arrowHunting || target.getTemplateId() == ItemList.arrowWar) {
                        player.getCommunicator().sendNormalServerMessage("You cannot use Eternal Orbs on arrows.");
                        return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                    }
                    ItemSpellEffects teffs = target.getSpellEffects();
                    if (teffs == null || teffs.getEffects().length == 0) {
                        player.getCommunicator().sendNormalServerMessage("The " + target.getTemplate().getName() + " has no enchants.");
                        return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                    }
                    for (SpellEffect eff : teffs.getEffects()) {
                        if (eff.type == 120) {
                            player.getCommunicator().sendNormalServerMessage("The " + eff.getName() + " enchant makes this item immune to the effects of the " + source.getName() + ".");
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                    }
                    try {
                        Item enchantOrb = ItemFactory.createItem(CustomItems.eternalOrbId, source.getCurrentQualityLevel(), "");
                        ItemSpellEffects effs = enchantOrb.getSpellEffects();
                        if (effs == null) {
                            effs = new ItemSpellEffects(enchantOrb.getWurmId());
                        }
                        for (SpellEffect teff : teffs.getEffects()) {
                            byte type = teff.type;
                            SpellEffect newEff = new SpellEffect(enchantOrb.getWurmId(), type, teff.getPower(), 20000000);
                            effs.addSpellEffect(newEff);
                            teffs.removeSpellEffect(type);
                            player.getCommunicator().sendSafeServerMessage(String.format("The %s transfers to the %s.", teff.getName(), enchantOrb.getTemplate().getName()));
                            if (enchantOrb.getDescription().equals("")) {
                                enchantOrb.setDescription(newEff.getName().charAt(0) + String.format("%d", (int) newEff.getPower()));
                            } else {
                                enchantOrb.setDescription(enchantOrb.getDescription() + " " + newEff.getName().charAt(0) + String.format("%d", (int) newEff.getPower()));
                            }
                        }
                        performer.getInventory().insertItem(enchantOrb, true);
                        Items.destroyItem(source.getWurmId());
                    } catch (FailedException | NoSuchTemplateException e) {
                        e.printStackTrace();
                    }
                } else {
                    RequiemLogging.logInfo("Somehow a non-player activated an Enchant Orb...");
                }
                return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
        };
    }
}