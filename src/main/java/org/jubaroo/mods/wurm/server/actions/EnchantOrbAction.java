package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.Items;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemSpellEffects;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.skills.Skill;
import com.wurmonline.server.spells.ItemEnchantment;
import com.wurmonline.server.spells.Spell;
import com.wurmonline.server.spells.SpellEffect;
import com.wurmonline.server.spells.Spells;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class EnchantOrbAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public EnchantOrbAction() {
        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Transfer enchant",
                "transferring",
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
                if (performer instanceof Player && source != null && object != null && source.getTemplateId() == CustomItems.enchantOrbId && source != object) {
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
                    if (source.getTemplate().getTemplateId() != CustomItems.enchantOrbId) {
                        player.getCommunicator().sendNormalServerMessage("You must use an Enchant Orb to transfer enchants.");
                        return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                    }
                    if (target.getTemplate().getTemplateId() == CustomItems.enchantOrbId) {
                        player.getCommunicator().sendNormalServerMessage("You cannot enchant an Enchant Orb with another.");
                        return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                    }
                    ItemSpellEffects effs = source.getSpellEffects();
                    if (effs == null || effs.getEffects().length == 0) {
                        player.getCommunicator().sendNormalServerMessage(String.format("The %s has no enchants.", source.getTemplate().getName()));
                        return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                    }
					/*if(!Spell.mayBeEnchanted(target)){
						player.getCommunicator().sendNormalServerMessage("The "+target.getTemplate().getName()+" may not be enchanted.");
					}*/
                    ItemSpellEffects teffs = target.getSpellEffects();
                    if (teffs == null) {
                        teffs = new ItemSpellEffects(target.getWurmId());
                    }
                    for (SpellEffect eff : effs.getEffects()) {
                        Spell spell = Spells.getEnchantment(eff.type);
                        boolean canEnchant = false;// = Spell.mayBeEnchanted(target);
                        byte type = eff.type;
                        if (spell == null) {
                            if (eff.type < -60) { // It's a rune
                                if (teffs.getNumberOfRuneEffects() > 0) {
                                    teffs.getRandomRuneEffect();
                                    player.getCommunicator().sendAlertServerMessage(String.format("The %s already has a rune attached and resists the application of the %s.", target.getTemplate().getName(), eff.getName()));
                                    continue;
                                } else {
                                    canEnchant = true;
                                }
                            } else {
                                if (teffs.getSpellEffect(type) != null) {
                                    float power = teffs.getSpellEffect(type).getPower();
                                    if (power >= 100f) {
                                        player.getCommunicator().sendAlertServerMessage(String.format("The %s already has the maximum power for %s, and refuses to accept more.", target.getTemplate().getName(), eff.getName()));
                                        continue;
                                    } else if (power + eff.getPower() > 100) {
                                        float difference = 100 - power;
                                        eff.setPower(eff.getPower() - difference);
                                        teffs.getSpellEffect(type).setPower(100);
                                        player.getCommunicator().sendSafeServerMessage(String.format("The %s transfers some of its power to the %s.", eff.getName(), target.getTemplate().getName()));
                                        continue;
                                    } else {
                                        teffs.getSpellEffect(type).setPower(effs.getSpellEffect(type).getPower() + power);
                                        effs.removeSpellEffect(type);
                                        player.getCommunicator().sendSafeServerMessage(String.format("The %s fully transfers to the %s.", eff.getName(), target.getTemplate().getName()));
                                        continue;
                                    }
                                } else {
                                    canEnchant = true;
                                }
                            }
                        } else {
                            try {
                                Method m;
                                if (spell instanceof ItemEnchantment) {
                                    m = ItemEnchantment.class.getDeclaredMethod("precondition", Skill.class, Creature.class, Item.class);
                                } else {
                                    m = spell.getClass().getDeclaredMethod("precondition", Skill.class, Creature.class, Item.class);
                                }
                                canEnchant = ReflectionUtil.callPrivateMethod(spell, m, player.getChannelingSkill(), performer, target);
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
                                RequiemLogging.logException("[ERROR] in action in EnchantOrbAction", e);
                            }
                        }
                        if (canEnchant) {
                            if (teffs.getSpellEffect(type) != null) {
                                if (teffs.getSpellEffect(type).getPower() >= eff.getPower()) {
                                    player.getCommunicator().sendAlertServerMessage(String.format("The %s already has a more powerful %s and resists the transfer.", target.getTemplate().getName(), eff.getName()));
                                    continue;
                                } else {
                                    teffs.getSpellEffect(type).setPower(eff.getPower());
                                    effs.removeSpellEffect(type);
                                    player.getCommunicator().sendSafeServerMessage(String.format("The %s replaces the existing enchant.", eff.getName()));
                                    continue;
                                }
                            }
                            SpellEffect newEff = new SpellEffect(target.getWurmId(), type, eff.getPower(), 20000000);
                            teffs.addSpellEffect(newEff);
                            effs.removeSpellEffect(type);
                            player.getCommunicator().sendSafeServerMessage(String.format("The %s transfers to the %s.", eff.getName(), target.getTemplate().getName()));
                        }
                    }
                    if (effs.getEffects().length == 0) {
                        player.getCommunicator().sendSafeServerMessage(String.format("The %s exhausts the last of its magic and vanishes.", source.getTemplate().getName()));
                        Items.destroyItem(source.getWurmId());
                    }
                } else {
                    RequiemLogging.logInfo("Somehow a non-player activated an Enchant Orb...");
                }
                return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
        };
    }
}