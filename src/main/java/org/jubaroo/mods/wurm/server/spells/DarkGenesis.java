package org.jubaroo.mods.wurm.server.spells;

import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.skills.Skill;
import com.wurmonline.server.spells.ModReligiousSpell;
import com.wurmonline.server.spells.Spell;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modsupport.actions.ActionEntryBuilder;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;

class DarkGenesis extends ModReligiousSpell {

    DarkGenesis() {
        super("DarkGenesis", ModActions.getNextActionId(), 10, 30, 40, 70, 30000L);
        this.targetCreature = true;
        this.type = SPELL_TYPE_RELIGIOUS;

        try {
            ReflectionUtil.setPrivateField(this, ReflectionUtil.getField(Spell.class, "description"), "Remove a single negative trait from a creature");
        } catch (Exception e) {
            RequiemLogging.logException(null, e);
        }

        ActionEntry actionEntry = new ActionEntryBuilder((short) number, name, "casting",
                new int[]{
                        ActionTypesProxy.ACTION_TYPE_SPELL,
                        ActionTypesProxy.ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM,
                        ActionTypesProxy.ACTION_TYPE_ENEMY_ALWAYS
                }).build();

        ModActions.registerAction(actionEntry);

    }

    @Override
    public boolean precondition(final Skill castSkill, final Creature performer, final Creature target) {
        return target != performer && performer.getDeity() != null;
    }

    @Override
    public void doEffect(final Skill castSkill, final double power, final Creature performer, final Creature target) {
        if (target.removeRandomNegativeTrait()) {
            performer.getCommunicator().sendNormalServerMessage(String.format("You rid the %s of genetic weaknesses. It will now produce healthier offspring.", target.getName()));
        } else {
            performer.getCommunicator().sendNormalServerMessage(String.format("The %s did not possess any genetic mutations.", target.getName()), (byte) 3);
        }
    }

}
