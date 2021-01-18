package org.jubaroo.mods.wurm.server.tools;

import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.shared.constants.EffectConstants;
import com.wurmonline.shared.constants.SoundNames;

public class EffectsTools {
    // =====================================================================
    // ============================== SOUNDS ===============================
    // =====================================================================

    public static String randomRepairSoundWood(int sound) {
        String snd = SoundNames.HAMMERONWOOD1_SND;
        if (sound == 0) {
            snd = SoundNames.HAMMERONWOOD2_SND;
        }
        return snd;
    }

    // =========================================================================
    // ============================== CREATURE EFFECTS =========================
    // =========================================================================

    static void sendParticleEffect(Communicator comm, long creatureId, Creature creature, String particle, float duration) {
        comm.sendAddEffect(creatureId, EffectConstants.EFFECT_GENERIC, creature.getPosX(), creature.getPosY(), creature.getPositionZ(), (byte) creature.getLayer(), particle, duration, 0);
    }

    static void sendAddEffect(Communicator comm, long creatureId, byte effectNum) {
        comm.sendAttachEffect(creatureId, effectNum, (byte) 1, (byte) -1, (byte) -1, (byte) 1);
    }

}
