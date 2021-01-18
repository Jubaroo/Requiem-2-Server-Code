package org.jubaroo.mods.wurm.server.actions.magicItems;

import com.wurmonline.server.Items;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemSpellEffects;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.server.spells.SpellEffect;
import com.wurmonline.shared.constants.Enchants;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.misc.Crystals;

import java.util.Collections;
import java.util.List;

public class EnchantersCrystalInfuseAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public EnchantersCrystalInfuseAction() {
        RequiemLogging.logWarning("EnchantersCrystalInfuseAction()");

        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Infuse",
                "infusing",
                new int[]{ActionTypesProxy.ACTION_TYPE_NOMOVE}
        );
        ModActions.registerAction(actionEntry);
    }


    @Override
    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            // Menu with activated object
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item object) {
                if (performer instanceof Player && source != null && object != null && source.getTemplateId() == CustomItems.enchantersCrystalId && object.getTemplateId() != CustomItems.enchantersCrystalId) {
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
                try {
                    if (performer instanceof Player) {
                        if (source.getTemplate().getTemplateId() != CustomItems.enchantersCrystalId) {
                            performer.getCommunicator().sendNormalServerMessage("You must use an enchanters crystal to infuse an item.");
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        if (Crystals.shouldCancelEnchantersInfusion(performer, target)) {
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        if (counter == 1f) {
                            performer.getCommunicator().sendNormalServerMessage(String.format("You begin to infuse the %s with the %s.", target.getName(), source.getName()));
                            Server.getInstance().broadCastAction(String.format("%s begins infusing with a %s.", performer.getName(), source.getName()), performer, 5);
                            act.setTimeLeft(300);
                            performer.sendActionControl("Infusing", true, act.getTimeLeft());
                        } else if (counter * 10f > performer.getCurrentAction().getTimeLeft()) {
                            double diff = Crystals.getEnchantersInfusionDifficulty(performer, source, target);
                            double power = -100;
                            int i = source.getRarity();
                            while (i >= 0) {
                                power = Math.max(power, performer.getSkills().getSkill(SkillList.SOUL).skillCheck(diff, source, 0d, false, 1));
                                i--;
                            }
                            ItemSpellEffects effs = target.getSpellEffects();
                            if (power > 90) {
                                byte ench = Crystals.getNewRandomEnchant(target);
                                if (ench != -10) {
                                    performer.getCommunicator().sendNormalServerMessage(String.format("You handle the crystals expertly and infuse the %s, adding a new enchant!", target.getName()));
                                    SpellEffect eff = new SpellEffect(target.getWurmId(), ench, (float) power * Server.rand.nextFloat(), 20000000);
                                    effs.addSpellEffect(eff);
                                    Items.destroyItem(source.getWurmId());
                                } else {
                                    RequiemLogging.debug(String.format("Failed to find a valid enchant to add to item %s for player %s!", target.getName(), performer.getName()));
                                    performer.getCommunicator().sendNormalServerMessage("Nothing happens!");
                                }
                            } else if (power > 75) {
                                performer.getCommunicator().sendNormalServerMessage(String.format("You very carefully infuse the metal %s, increasing its magical properties!", target.getName()));
                                for (SpellEffect eff : effs.getEffects()) {
                                    eff.setPower(eff.getPower() + (eff.getPower() * Server.rand.nextFloat() * 0.2f));
                                }
                                Items.destroyItem(source.getWurmId());
                            } else if (power > 60) {
                                performer.getCommunicator().sendNormalServerMessage(String.format("You carefully infuse the %s, changing one of its magical properties!", target.getName()));
                                SpellEffect oldEff = effs.getEffects()[Server.rand.nextInt(effs.getEffects().length)];
                                float oldPower = oldEff.getPower();
                                if (oldEff.type == Enchants.BUFF_BLOODTHIRST) {
                                    oldPower *= 0.01f;
                                }
                                effs.removeSpellEffect(oldEff.type);
                                byte ench = Crystals.getNewRandomEnchant(target);
                                if (ench != -10) {
                                    SpellEffect eff = new SpellEffect(target.getWurmId(), ench, oldPower, 20000000);
                                    effs.addSpellEffect(eff);
                                } else {
                                    performer.getCommunicator().sendNormalServerMessage(String.format("However, something goes wrong and the %s instead loses the property!", target.getName()));
                                }
                                Items.destroyItem(source.getWurmId());
                            } else if (power > 35) {
                                performer.getCommunicator().sendNormalServerMessage(String.format("You manage to infuse the %s, shifting its magical properties.", target.getName()));
                                for (SpellEffect eff : effs.getEffects()) {
                                    eff.setPower(eff.getPower() + ((eff.getPower() * Server.rand.nextFloat() * 0.3f) * (Server.rand.nextBoolean() ? 1 : -1)));
                                }
                                Items.destroyItem(source.getWurmId());
                            } else if (power > 0) {
                                performer.getCommunicator().sendNormalServerMessage(String.format("You barely manage to infuse the %s, destroying a magical property but increasing the rest.", target.getName()));
                                SpellEffect oldEff = effs.getEffects()[Server.rand.nextInt(effs.getEffects().length)];
                                effs.removeSpellEffect(oldEff.type);
                                if (effs.getEffects().length >= 1) {
                                    for (SpellEffect eff : effs.getEffects()) {
                                        eff.setPower(eff.getPower() + (eff.getPower() * Server.rand.nextFloat() * 0.2f));
                                    }
                                } else {
                                    performer.getCommunicator().sendNormalServerMessage(String.format("However, the %s does not have any other properties, and the effect is wasted!", target.getName()));
                                }
                                Items.destroyItem(source.getWurmId());
                            } else if (power > -30) {
                                performer.getCommunicator().sendNormalServerMessage(String.format("You barely fail to infuse the %s, reducing the power of its magical properties.", target.getName()));
                                for (SpellEffect eff : effs.getEffects()) {
                                    eff.setPower(eff.getPower() - (eff.getPower() * Server.rand.nextFloat() * 0.2f));
                                }
                                Items.destroyItem(source.getWurmId());
                            } else if (power > -60) {
                                performer.getCommunicator().sendNormalServerMessage(String.format("You horribly fail to infuse the %s, removing one of its magical properties.", target.getName()));
                                SpellEffect oldEff = effs.getEffects()[Server.rand.nextInt(effs.getEffects().length)];
                                effs.removeSpellEffect(oldEff.type);
                                Items.destroyItem(source.getWurmId());
                            } else {
                                performer.getCommunicator().sendNormalServerMessage("The infusion fails catastrophically, destroying all the magic on the " + target.getName() + "!");
                                for (SpellEffect eff : effs.getEffects()) {
                                    effs.removeSpellEffect(eff.type);
                                }
                                Items.destroyItem(source.getWurmId());
                            }
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                    } else {
                        RequiemLogging.debug(String.format("Somehow a non-player activated a custom item (%d)...", source.getTemplateId()));
                    }
                    return propagate(act, ActionPropagation.CONTINUE_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                } catch (Exception e) {
                    e.printStackTrace();
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
            }


        }; // ActionPerformer
    }
}