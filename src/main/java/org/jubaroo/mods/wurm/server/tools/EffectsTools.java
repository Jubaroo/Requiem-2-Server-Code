package org.jubaroo.mods.wurm.server.tools;

import com.wurmonline.communication.SocketConnection;
import com.wurmonline.server.Server;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.CreatureTemplateFactory;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.zones.VirtualZone;
import com.wurmonline.shared.constants.EffectConstants;
import com.wurmonline.shared.constants.SoundNames;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.creatures.CustomCreatures;
import org.jubaroo.mods.wurm.server.creatures.MethodsBestiary;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.vehicles.CustomVehicles;

import java.nio.ByteBuffer;

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

    private static void sendParticleEffect(Communicator comm, long creatureId, Creature creature, String particle, float duration) {
        comm.sendAddEffect(creatureId, EffectConstants.EFFECT_GENERIC, creature.getPosX(), creature.getPosY(), creature.getPositionZ(), (byte) creature.getLayer(), particle, duration, 0);
    }

    private static void sendAddEffect(Communicator comm, long creatureId, byte effectNum) {
        comm.sendAttachEffect(creatureId, effectNum, (byte) 1, (byte) -1, (byte) -1, (byte) 1);
    }

    // - Good Effects -
    // rift01 [large], rift02 [small]
    // treasureP [light bubbles]
    // spawneffect2 [sparkle fireworks]
    // reindeer [light sparkles]
    // iceBall_1_1 [clean ice effect]
    // acidBall_1_1 [clean green ball effect]
    // byte 1 [fire]
    // - Bad Effects -
    // spawneffect [sparkle eye cancer]
    // snow1emitter [disappears instantly}, snow2emitter [disappears instantly]
    // spawneffectshort [disappears instantly]
    // iceWispSpark [tiny and unnoticeable]
    // iceBolt1 [very flickery]
    // iceTail1 [small and points downwards]
    // acidWispSpark [basically invisible]
    // acidTail1 [inconsistent trail]
    // acidBolt1 [very flickery]
    // lightningTail1 [weird effect]


    public static void addCreatureSpecialEffect(long creatureId, Communicator comm, Creature creature) {
        int templateId = creature.getTemplate().getTemplateId();
        if (templateId == CustomCreatures.iceCatId || templateId == CustomCreatures.npcStarId || templateId == CustomCreatures.willOWispId) {
            String particle1 = "iceBall_1_1";
            sendParticleEffect(comm, creatureId, creature, particle1, Float.MAX_VALUE);
            String particle2 = "iceTail1";
            sendParticleEffect(comm, creatureId, creature, particle2, Float.MAX_VALUE);
        } else if (MethodsBestiary.isTreasureGoblin(creature)) {
            String particle = "treasureP";
            sendParticleEffect(comm, creatureId, creature, particle, Float.MAX_VALUE);
            String particle2 = "sparkleDense";
            sendParticleEffect(comm, creatureId, creature, particle2, Float.MAX_VALUE);
        } else if (templateId == CustomCreatures.ifritId) {
            String particle = "rift01";
            sendParticleEffect(comm, creatureId, creature, particle, Float.MAX_VALUE);
        } else if (templateId == CustomCreatures.lilithId) {
            String particle = "rift02";
            sendParticleEffect(comm, creatureId, creature, particle, Float.MAX_VALUE);
        } else if (creature.getName().contains("High Wizard") || creature.getName().contains("Black Mage")) {
            String particle = "portal1";
            sendParticleEffect(comm, creatureId, creature, particle, Float.MAX_VALUE);
        } else if (templateId == CustomCreatures.fireCrabId || templateId == CustomCreatures.fireGiantId) {
            sendAddEffect(comm, creatureId, (byte) 1);
        }
    }

    public static void addCreatureHook(VirtualZone vz, long creatureId) {
        Creature watcher = vz.getWatcher();
        if (watcher != null && watcher.isPlayer() && watcher.hasLink()) {
            try {
                Creature creature = Server.getInstance().getCreature(creatureId);
                if (creature.getTemplate().getTemplateId() == CreatureTemplateFactory.UNICORN_CID) {
                    SocketConnection conn = watcher.getCommunicator().getConnection();
                    ByteBuffer bb = conn.getBuffer();
                    Server.rand.setSeed(creatureId);
                    bb.put((byte) 92);
                    bb.putLong(creatureId);
                    bb.put((byte) Server.rand.nextInt(256));
                    bb.put((byte) Server.rand.nextInt(256));
                    bb.put((byte) Server.rand.nextInt(256));
                    bb.put((byte) 255);
                    bb.put((byte) 0);
                    conn.flush();
                }
            } catch (Exception e) {
                RequiemLogging.logWarning(String.format("Error in addCreatureHook: %s", e));
            }
        }
    }

    // =====================================================================
    // ============================== ITEM EFFECTS =========================
    // =====================================================================
        	/*
	comm.sendAttachEffect(item.getWurmId(), (byte) 1, (byte) 5, (byte) 0, (byte) 0, (byte) 0); - this adds a simpler fire effect
	comm.sendAttachEffect(item.getWurmId(), (byte) 0, (byte) 255, (byte) 1, (byte) 1, (byte) 255); - this is light emission, the values are RBGA
	comm.sendAddEffect(item.getWurmId(), item.getWurmId(), (short) 27, item.getPosX(), item.getPosY(), item.getPosZ(), (byte) 0, "copperBrazierFire", Float.MAX_VALUE, 0f); - this adds more complex particle effects as defined in particle.xml in the client

	 * sendRepaint
	 * @param wurmid
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 * @param paintType 0 for primary color, 1 for secondary. Like for items with wood/metal color etc

	 * sendAttachEffect
	 * @param targetId
	 * @param effectType
	 * @param data0
	 * @param data1
	 * @param data2
	 * @param dimension

	 * sendAddEffect
	 * @param id
	 * @param target
	 * @param type
	 * @param x
	 * @param y
	 * @param z
	 * @param layer
	 * @param effectString
	 * @param timeout
	 * @param rotationOffset
	 */

    /**
     * Here we attach special effect to items when they are created
     * @param comm the player
     * @param item the item being destroyed
     */
    public static void sendItemHook(Communicator comm, Item item) {
        int templateId;
        templateId = item.getTemplateId();
        if (templateId == CustomItems.fireCrystal.getTemplateId()) {
            comm.sendRemoveEffect(item.getWurmId());
            comm.sendAttachEffect(item.getWurmId(), (byte) 1, (byte) 5, (byte) 0, (byte) 0, (byte) 0);
            comm.sendRepaint(item.getWurmId(), (byte) 255, (byte) 1, (byte) 1, (byte) 255, (byte) 0);
            comm.sendAttachEffect(item.getWurmId(), (byte) 0, (byte) 255, (byte) 1, (byte) 1, (byte) 255);
            comm.sendAddEffect(item.getWurmId(), item.getWurmId(), EffectConstants.EFFECT_GENERIC, item.getPosX(), item.getPosY(), item.getPosZ(), (byte) 0, "copperBrazierFire", Float.MAX_VALUE, 0f);
        } else if (templateId == CustomItems.frostCrystal.getTemplateId()) {
            comm.sendRemoveEffect(item.getWurmId());
            comm.sendRepaint(item.getWurmId(), (byte) 131, (byte) 205, (byte) 232, (byte) 255, (byte) 0);
            comm.sendAttachEffect(item.getWurmId(), (byte) 0, (byte) 131, (byte) 205, (byte) 232, (byte) 255);
            comm.sendAddEffect(item.getWurmId(), item.getWurmId(), EffectConstants.EFFECT_GENERIC, item.getPosX(), item.getPosY(), item.getPosZ(), (byte) 0, "frostflake", Float.MAX_VALUE, 0f);
        } else if (templateId == CustomItems.lifeCrystal.getTemplateId()) {
            comm.sendRemoveEffect(item.getWurmId());
            comm.sendRepaint(item.getWurmId(), (byte) 34, (byte) 155, (byte) 4, (byte) 255, (byte) 0);
            comm.sendAttachEffect(item.getWurmId(), (byte) 0, (byte) 34, (byte) 155, (byte) 4, (byte) 255);
            comm.sendAddEffect(item.getWurmId(), item.getWurmId(), EffectConstants.EFFECT_GENERIC, item.getPosX(), item.getPosY(), item.getPosZ(), (byte) 0, "acidBall_1_1", Float.MAX_VALUE, 0f);
        } else if (templateId == CustomItems.deathCrystal.getTemplateId()) {
            comm.sendRemoveEffect(item.getWurmId());
            comm.sendRepaint(item.getWurmId(), (byte) 59, (byte) 2, (byte) 144, (byte) 255, (byte) 0);
            comm.sendAttachEffect(item.getWurmId(), (byte) 0, (byte) 59, (byte) 2, (byte) 144, (byte) 255);
            comm.sendAddEffect(item.getWurmId(), item.getWurmId(), EffectConstants.EFFECT_GENERIC, item.getPosX(), item.getPosY(), item.getPosZ(), (byte) 0, "fogSpider", Float.MAX_VALUE, 0f);
        } else if (templateId == CustomItems.essenceOfWoodId) {
            comm.sendRemoveEffect(item.getWurmId());
            comm.sendAddEffect(item.getWurmId(), item.getWurmId(), EffectConstants.EFFECT_GENERIC, item.getPosX(), item.getPosY(), item.getPosZ(), (byte) 0, "traitor", Float.MAX_VALUE, 0f);
        } else if (templateId == CustomVehicles.altarWagon.getTemplateId()) {
            comm.sendRemoveEffect(item.getWurmId());
            comm.sendAttachEffect(item.getWurmId(), (byte) 1, (byte) 5, (byte) 0, (byte) 0, (byte) 0);
            comm.sendAttachEffect(item.getWurmId(), (byte) 0, (byte) 226, (byte) 88, (byte) 34, (byte) 255);
        } else if (templateId == CustomItems.campfire.getTemplateId()) {
            comm.sendRemoveEffect(item.getWurmId());
            comm.sendAttachEffect(item.getWurmId(), (byte) 1, (byte) 5, (byte) 0, (byte) 0, (byte) 0);
            comm.sendAttachEffect(item.getWurmId(), (byte) 0, (byte) 226, (byte) 88, (byte) 34, (byte) 255);
            comm.sendAddEffect(item.getWurmId(), item.getWurmId(), EffectConstants.EFFECT_GENERIC, item.getPosX(), item.getPosY(), item.getPosZ(), (byte) 0, "copperBrazierFire", Float.MAX_VALUE, 0f);
        } else if (templateId == CustomItems.eventGravestoneId) {
            comm.sendRemoveEffect(item.getWurmId());
            comm.sendAddEffect(item.getWurmId(), item.getWurmId(), EffectConstants.EFFECT_GENERIC, item.getPosX(), item.getPosY(), item.getPosZ(), (byte) 0, "treasureP", Float.MAX_VALUE, 0f);
        }
    }

    /*
      Make sure we remove the item effect when the item is destroyed. We can add that in ItemRemoval.removeItemHook
     */

}
