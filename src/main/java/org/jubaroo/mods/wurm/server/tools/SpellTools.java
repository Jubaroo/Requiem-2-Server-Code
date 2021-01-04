package org.jubaroo.mods.wurm.server.tools;

import com.wurmonline.server.WurmId;
import com.wurmonline.server.bodys.DbWound;
import com.wurmonline.server.bodys.TempWound;
import com.wurmonline.server.bodys.Wound;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.spells.Spell;
import com.wurmonline.server.spells.Spells;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.utils.TweakApiPerms;

public class SpellTools {

    static public boolean noSpellCooldown(String name) {
        Spell spell = getSpellByName(name);
        if (spell == null) {
            return false;
        }

        RequiemLogging.logInfo("noSpellCooldown: " + spell.name);
        return TweakApiPerms.setClassField("com.wurmonline.server.spells.Spell", "cooldown", spell, 0);
    }

    public static Spell getSpellByName(String name) {
        for (Spell spell : Spells.getAllSpells()) {
            RequiemLogging.logInfo(String.format("spell ->%s<-", spell.name));
            if (spell.name.equals(name)) {
                return spell;
            }
        }
        RequiemLogging.logInfo("getSpellByName failed: " + name);
        return null;
    }

    public static float getSpellCastPower(Item item, byte spellId) {
        return item.getBonusForSpellEffect(spellId);
    }

    public static void addMagicDamage(final Creature performer, final Creature defender, final int pos, double damage, byte type, boolean spell) {
        if (performer != null && performer.getTemplate().getCreatureAI() != null)
            damage = performer.getTemplate().getCreatureAI().causedWound(performer, defender, type, pos, 1, damage);
        if (defender != null && defender.getTemplate().getCreatureAI() != null)
            damage = defender.getTemplate().getCreatureAI().receivedWound(defender, performer, type, pos, 1, damage);
        if (damage > 500.0) {
            if (defender.getBody().getWounds() != null) {
                Wound wound = defender.getBody().getWounds().getWoundTypeAtLocation((byte) pos, type);
                if (wound != null && wound.getType() == type) {
                    defender.setWounded();
                    wound.setBandaged(false);
                    wound.modifySeverity((int) (damage), performer != null && performer.isPlayer(), spell);
                }
            }
            if (WurmId.getType(defender.getWurmId()) == 1) {
                defender.getBody().addWound(new TempWound(type, (byte) pos, (float) damage, defender.getWurmId(), 0.0f, 0.0f, spell));
            } else {
                defender.getBody().addWound(new DbWound(type, (byte) pos, (float) damage, defender.getWurmId(), 0.0f, 0.0f, performer != null && performer.isPlayer(), spell));
            }
        }
    }

}
