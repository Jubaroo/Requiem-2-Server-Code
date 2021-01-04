package org.jubaroo.mods.wurm.server.actions.crystals.life;

import com.wurmonline.server.LoginHandler;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Players;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.*;
import com.wurmonline.server.creatures.*;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.skills.Skill;
import com.wurmonline.server.skills.Skills;
import com.wurmonline.server.skills.SkillsFactory;
import com.wurmonline.server.zones.Zones;
import com.wurmonline.shared.constants.EffectConstants;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.utils.Cooldowns;

import java.util.Collections;
import java.util.List;

public class RebirthAction implements ModAction, ActionPerformer, BehaviourProvider {
    private ActionEntry actionEntry;

    public RebirthAction() {
        actionEntry = ActionEntry.createEntry((short) ModActions.getNextActionId(), "Raise the dead", "raising", new int[]{
                ActionTypesProxy.ACTION_TYPE_NOMOVE,
                ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS,
                ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM
        });
        ModActions.registerAction(actionEntry);
    }

    private static void raise(final Creature performer, final Item target) {
        //if (target.getTemplateId() == ItemList.corpse) {
        //if (target.getDamage() < 10f) {
        try {
            final CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(target.getData1());
            Creature cret = null;
            if (template.isHuman()) {
                if (MethodsItems.isLootableBy(performer, target)) {
                    final String name = target.getName().substring(10);
                    try {
                        final long wid = Players.getInstance().getWurmIdFor(name);
                        byte sex = 0;
                        if (target.female) {
                            sex = 1;
                        }
                        if (wid == performer.getWurmId()) {
                            performer.getCommunicator().sendNormalServerMessage(performer.getDeity().getName() + " does not allow you to raise your own corpse.", (byte) 3);
                            return;
                        }
                        cret = Creature.doNew(template.getTemplateId(), false, target.getPosX(), target.getPosY(), target.getRotation(), target.isOnSurface() ? 0 : -1, LoginHandler.raiseFirstLetter(name + " , Risen"), sex, performer.getKingdomId(), (byte) 0, true);
                        cret.getStatus().setTraitBit(63, true);
                        final Skills skills = SkillsFactory.createSkills(wid);
                        try {
                            skills.load();
                            cret.getSkills().delete();
                            cret.getSkills().clone(skills.getSkills());
                            final Skill[] skills3;
                            skills3 = cret.getSkills().getSkills();
                            for (final Skill cSkill : skills3) {
                                if (cSkill.getNumber() == 10052) {
                                    cSkill.setKnowledge(Math.min(70.0, cSkill.getKnowledge() * 0.699999988079071), false);
                                } else {
                                    cSkill.setKnowledge(Math.min(40.0, cSkill.getKnowledge() * 0.699999988079071), false);
                                }
                            }
                            cret.getSkills().save();
                        } catch (Exception e) {
                            RequiemLogging.logWarning( e.getMessage() + e);
                            performer.getCommunicator().sendNormalServerMessage("You struggle to bring the corpse back to life, but you sense problems.", (byte) 3);
                        }
                    } catch (Exception ex) {
                        performer.getCommunicator().sendNormalServerMessage("There is no soul attached to this corpse any longer.", (byte) 3);
                    }
                } else {
                    performer.getCommunicator().sendNormalServerMessage("You may not touch this body right now. It is protected by the gods, for now.", (byte) 3);
                }
            } else {
                final byte ctype = CreatureStatus.getModtypeForString(target.getName());
                if (template.isUnique()) {
                    performer.getCommunicator().sendNormalServerMessage("The soul of this creature is strong, and it manages to resist the crystals power.", (byte) 3);
                    return;
                }
                if (ctype == 99) {
                    performer.getCommunicator().sendNormalServerMessage("The soul of this creature is strong, and it manages to resist the crystals power.", (byte) 3);
                    return;
                }
                byte sex2 = 0;
                if (target.female) {
                    sex2 = 1;
                }
                try {
                    cret = Creature.doNew(template.getTemplateId(), false, target.getPosX(), target.getPosY(), target.getRotation(), target.isOnSurface() ? 0 : -1, template.getName() + ", Risen", sex2, performer.getKingdomId(), ctype, true);
                    cret.getStatus().setTraitBit(63, true);
                    if (template.isUnique()) {
                        try {
                            final Skill[] skills4;
                            skills4 = cret.getSkills().getSkills();
                            for (final Skill lSkill : skills4) {
                                if (lSkill.getNumber() == 10052) {
                                    lSkill.setKnowledge(lSkill.getKnowledge() * 0.4000000059604645, false);
                                } else {
                                    lSkill.setKnowledge(lSkill.getKnowledge() * 0.20000000298023224, false);
                                }
                            }
                        } catch (Exception e2) {
                            RequiemLogging.logWarning( e2.getMessage() + e2);
                            performer.getCommunicator().sendNormalServerMessage("You struggle to bring the corpse back to life, but you sense problems.", (byte) 3);
                        }
                    }
                } catch (Exception e2) {
                    performer.getCommunicator().sendNormalServerMessage("You struggle to bring the corpse back to life.", (byte) 3);
                }
            }
            if (cret != null) {
                if (performer.getPet() != null) {
                    performer.getCommunicator().sendNormalServerMessage(performer.getPet().getName() + " stops obeying you.", (byte) 2);
                    if (performer.getPet().getLeader() == performer) {
                        performer.getPet().setLeader(null);
                    }
                    if (performer.getPet().isReborn()) {
                        performer.getPet().die(false, "Rebirth");
                    } else {
                        performer.getPet().setDominator(MiscConstants.NOID);
                    }
                }
                performer.setPet(cret.getWurmId());
                cret.setDominator(performer.getWurmId());
                cret.setLoyalty(Math.max(10f, (float) Server.rand.nextInt(10)));
                cret.getStatus().setLastPolledLoyalty();
                cret.setTarget(MiscConstants.NOID, true);
                if (performer.getTarget() == cret) {
                    performer.setTarget(MiscConstants.NOID, true);
                }
                if (cret.opponent != null) {
                    cret.setOpponent(null);
                }
                if (performer.opponent == cret) {
                    performer.setOpponent(null);
                }
                performer.getCommunicator().sendNormalServerMessage(cret.getName() + " now obeys you.", (byte) 2);
                //final VolaTile targetVolaTile = Zones.getTileOrNull(cret.getTileX(), cret.getTileY(), cret.isOnSurface());
                //if (targetVolaTile != null) {
                //    targetVolaTile.sendAttachCreatureEffect(cret, (byte) 8, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
                //}
                performer.getCommunicator().sendAddEffect(target.getWurmId(), target.getWurmId(), EffectConstants.EFFECT_GENERIC, target.getPosX(), target.getPosY(), target.getPosZ(), (byte) 0, "uttachaExplode", 1, 0f);

            }
            target.setDamage(Math.max(target.getDamage(), 10f));
        } catch (NoSuchCreatureTemplateException nst) {
            performer.getCommunicator().sendNormalServerMessage("There is no soul attached to this corpse any longer.", (byte) 3);
        }
        //} else {
        //    performer.getCommunicator().sendNormalServerMessage("The corpse is too damaged.", (byte) 3);
        //}
        //} else {
        //    performer.getCommunicator().sendNormalServerMessage("This ability will only work on corpses.", (byte) 3);
        //}
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

    private boolean canUse(Creature performer, Item source, Item target) {
        return performer.isPlayer() && source != null && target != null &&
                source.getTemplateId() == CustomItems.lifeCrystal.getTemplateId() &&
                source.getTopParent() == performer.getInventory().getWurmId();
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
        if (canUse(performer, source, target))
            return Collections.singletonList(actionEntry);
        else
            return null;
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {
        try {
            final int tileX = target.getTileX();
            final int tileY = target.getTileY();
            final String playerEffect = performer.getName() + "crystalRebirthAction";
            int cooldown = 360; // minutes

            if (Cooldowns.isOnCooldown(playerEffect, cooldown)) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You will need to wait %s before you can do that again.", Cooldowns.timeRemaining(playerEffect, cooldown)));
                return true;
            }
            if (!mayRaise(performer, target) && !canUse(performer, source, target)) {
                performer.getCommunicator().sendAlertServerMessage("Do you have the correct item activated and/or targeting the correct item/creature? Please try again or use /support to open a ticket with as much info as possible, and the staff will get to it as soon as possible.");
                return true;
            }
            if (mayRaise(performer, target) && canUse(performer, source, target)) {
                if (counter == 1f) {
                    performer.getCommunicator().sendNormalServerMessage(String.format("You touch the crystal to the %s in an attempt to bring its soul back from beyond. A lightning bolt strikes the corpse and to begins to move a little!", target.getName()));
                    performer.getCurrentAction().setTimeLeft(100);
                    performer.sendActionControl("Resurrecting a " + target.getName(), true, 100);
                    performer.getCommunicator().sendAddEffect(target.getWurmId(), target.getWurmId(), EffectConstants.EFFECT_GENERIC, target.getPosX(), target.getPosY(), target.getPosZ(), (byte) 0, "lightningBall01", 10, 0f);
                    Server.getWeather().setRainAdd(40f);
                    Server.getWeather().setCloudTarget(40f);
                    Zones.flashSpell(tileX, tileY, 0, performer);
                    //SoundPlayer.playSound("sound.weather.thunderclap.01", performer, 0f);
                    //Methods.sendSound(performer, "sound.weather.thunderclap.01");
                    return false;
                } else {
                    if (counter * 10f > action.getTimeLeft()) {
                        Server.getWeather().setRainAdd(40f);
                        Server.getWeather().setCloudTarget(40f);
                        Zones.flashSpell(tileX, tileY, 0, performer);
                        //SoundPlayer.playSound("sound.weather.thunderclap.intense", performer, 0f);
                        //Methods.sendSound(performer, "sound.weather.thunderclap.intense");
                        performer.getCommunicator().sendRemoveEffect(target.getWurmId());
                        raise(performer, target);
                        performer.getCommunicator().sendNormalServerMessage(String.format("%s calls to the soul of %s and hits the corpse with one more bolt of lightning. The %s springs back to life!", performer.getDeity().name, target.getName(), target.getName()));
                        Cooldowns.setUsed(playerEffect);
                        return true;
                    }
                }
            }
            return false;
        } catch (NoSuchActionException ex3) {
            RequiemLogging.logWarning( "Error on death action: " + ex3.getMessage());
        }
        return true;
    }

    private boolean mayRaise(Creature performer, Item target) {
        try {
            byte ctype = CreatureStatus.getModtypeForString(target.getName());
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(target.getData1());
            if (target.getTemplateId() == ItemList.corpse) {
                return true;
            }
            if (!performer.isWithinDistanceTo(target, 8f)) {
                performer.getCommunicator().sendNormalServerMessage(String.format("You must be closer to the %s.", target.getName()));
                return true;
            }
            if (target.getDamage() < 10f) {
                performer.getCommunicator().sendNormalServerMessage("This corpse is too far gone for that to work.", (byte) 3);
                return true;
            }
            if (!target.isButchered()) {
                performer.getCommunicator().sendNormalServerMessage("The corpse is butchered and may not be raised.", (byte) 3);
                return true;
            }
            if (target.isUnique() | ctype == 99) {
                performer.getCommunicator().sendNormalServerMessage("The soul of this creature is too strong for the crystals power.", (byte) 3);
                return true;
            }
            if (template.isRiftCreature()) {
                performer.getCommunicator().sendNormalServerMessage("This corpse is too far gone for that to work.", (byte) 3);
                return true;
            }
            if (template.isNotRebirthable()) {
                performer.getCommunicator().sendNormalServerMessage("The soul refuses to return to this corpse.", (byte) 3);
                return true;
            }
            if (template.isTowerBasher()) {
                performer.getCommunicator().sendNormalServerMessage("There is no soul attached to this corpse any longer.", (byte) 3);
                return true;
            }
            if (!template.isHuman()) {
                return false;
            }
            if (MethodsItems.isLootableBy(performer, target)) {
                String name = target.getName().substring(10);
                try {
                    long wid = Players.getInstance().getWurmIdFor(name);
                    if (wid == performer.getWurmId()) {
                        performer.getCommunicator().sendNormalServerMessage(performer.getDeity().getName() + " does not allow you to raise your own corpse.", (byte) 3);
                        return true;
                    }
                    return false;
                } catch (Exception var7) {
                    performer.getCommunicator().sendNormalServerMessage("There is no soul attached to this corpse any longer.", (byte) 3);
                }
            }
        } catch (NoSuchCreatureTemplateException var8) {
            performer.getCommunicator().sendNormalServerMessage("There is no soul attached to this corpse any longer.", (byte) 3);
            return true;
        }
        return true;
    }
}


