package org.jubaroo.mods.wurm.server.tools;

import com.wurmonline.server.Server;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zones;
import com.wurmonline.shared.constants.EffectConstants;
import com.wurmonline.shared.constants.SoundNames;

public class EffectsTools {
    // =====================================================================
    // ============================== SOUNDS ===============================
    // =====================================================================

    public static String randomRepairSoundWood() {
        int n = Server.rand.nextInt(2);
        String snd = null;
        if (n == 0)
            snd = SoundNames.HAMMERONWOOD1_SND;
        if (n == 1)
            snd = SoundNames.HAMMERONWOOD2_SND;
        return snd;
    }

    // =========================================================================
    // ============================== CREATURE EFFECTS =========================
    // =========================================================================

    static void sendParticleEffect(Communicator comm, long creatureId, Creature creature, String particle, float duration) {
        comm.sendAddEffect(creatureId, EffectConstants.EFFECT_GENERIC, creature.getPosX(), creature.getPosY(), creature.getPositionZ(), (byte) creature.getLayer(), particle, duration, 0);
    }

    static void sendAddCreatureEffect(Communicator comm, long creatureId, byte effectNum) {
        comm.sendAttachEffect(creatureId, effectNum, (byte) 1, (byte) -1, (byte) -1, (byte) 1);
    }

    // =========================================================================
    // =============================== GENERAL EFFECTS =========================
    // =========================================================================

    public static void tickRandomLightning(int tileX, int tileY) {
        //VolaTile vt = getSpawnTile();
        final VolaTile vt = Zones.getTileOrNull(tileX, tileY, true);
        Zones.flash(vt.getTileX(), vt.getTileY(), false);
    }

    public static void sendBeam(Player player, short effectId, int tileX, int tileY, int tileZ) {
        player.getCommunicator().sendAddEffect(Long.MAX_VALUE - Server.rand.nextInt(100000), effectId, tileX * 4 + 2, tileY * 4 + 2, tileZ, (byte) 0);
    }

}
