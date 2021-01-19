package org.jubaroo.mods.wurm.server.tools;

import com.wurmonline.communication.SocketConnection;
import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.Items;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Server;
import com.wurmonline.server.bodys.Body;
import com.wurmonline.server.bodys.BodyTemplate;
import com.wurmonline.server.combat.Archery;
import com.wurmonline.server.combat.Weapon;
import com.wurmonline.server.creatures.*;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.items.NoSpaceException;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.skills.NoSuchSkillException;
import com.wurmonline.server.skills.SkillList;
import com.wurmonline.server.sounds.SoundPlayer;
import com.wurmonline.server.villages.Village;
import com.wurmonline.server.zones.VirtualZone;
import com.wurmonline.shared.constants.*;
import net.bdew.wurm.server.threedee.Utils;
import net.bdew.wurm.server.threedee.api.DisplayHookRegistry;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.communication.discord.ChatHandler;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.communication.discord.DiscordHandler;
import org.jubaroo.mods.wurm.server.creatures.*;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.misc.Misc;
import org.jubaroo.mods.wurm.server.server.OnPlayerLogin;
import org.jubaroo.mods.wurm.server.server.constants.LoggingConstants;
import org.jubaroo.mods.wurm.server.vehicles.CustomVehicles;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.Set;

import static org.jubaroo.mods.wurm.server.items.CustomItems.largeWheelId;
import static org.jubaroo.mods.wurm.server.server.constants.CreatureConstants.soundEmissionNpcs;
import static org.jubaroo.mods.wurm.server.server.constants.LoggingConstants.itemRemoveLogging;
import static org.jubaroo.mods.wurm.server.server.constants.MessageConstants.displayOnScreen;

public class Hooks {
    public static int resizeItemId;
    public static float resizeItemSize;

    public static void spawnQuestion(Player player) {
        try {
            OnPlayerLogin.addMonthlyBuffsToPlayers(player);
        } catch (IllegalArgumentException | ClassCastException e) {
            throw new HookException(e);
        }
    }

    /**
     * @param creatureId the Id of the creature that you want to add effects to
     * @param comm       the communicator of the player that is looking at the creature
     * @param creature   creature template of the creature you want to add effects to
     */
    public static void addCreatureSpecialEffect(long creatureId, Communicator comm, Creature creature) {
        int templateId = creature.getTemplate().getTemplateId();
        if (templateId == CustomCreatures.iceCatId || templateId == CustomCreatures.npcStarId || templateId == CustomCreatures.willOWispId) {
            String particle1 = "iceBall_1_1";
            EffectsTools.sendParticleEffect(comm, creatureId, creature, particle1, Float.MAX_VALUE);
            String particle2 = "iceTail1";
            EffectsTools.sendParticleEffect(comm, creatureId, creature, particle2, Float.MAX_VALUE);
        } else if (MethodsBestiary.isTreasureGoblin(creature)) {
            String particle = "treasureP";
            EffectsTools.sendParticleEffect(comm, creatureId, creature, particle, Float.MAX_VALUE);
            String particle2 = "sparkleDense";
            EffectsTools.sendParticleEffect(comm, creatureId, creature, particle2, Float.MAX_VALUE);
        } else if (templateId == CustomCreatures.ifritId) {
            String particle = "rift01";
            EffectsTools.sendParticleEffect(comm, creatureId, creature, particle, Float.MAX_VALUE);
        } else if (templateId == CustomCreatures.lilithId) {
            String particle = "rift02";
            EffectsTools.sendParticleEffect(comm, creatureId, creature, particle, Float.MAX_VALUE);
        } else if (creature.getName().contains("High Wizard") || creature.getName().contains("Black Mage")) {
            String particle = "portal1";
            EffectsTools.sendParticleEffect(comm, creatureId, creature, particle, Float.MAX_VALUE);
        } else if (templateId == CustomCreatures.fireCrabId || templateId == CustomCreatures.fireGiantId) {
            EffectsTools.sendAddCreatureEffect(comm, creatureId, (byte) 1);
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
     * Here we attach special effects to items when they are created
     *
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

    // Make searched dens tick down and be able to be searched again
    public static void itemTick(Item item) {
        if (item.getTemplateId() == ItemList.creatureSpawn) {
            if (item.getData2() > 0) {
                item.setData2(item.getData2() - 1);
                if (item.getData2() == 0)
                    RequiemLogging.debug(String.format("A %s (creature den) at location X:%s Y:%s is now able to be searched again.", item.getName(), item.getPosX() / 4, item.getPosY() / 4));
            }
        }
    }

    // Allow ghost creatures to drop corpses
    public static boolean isGhostCorpse(Creature creature) {
        int templateId = creature.getTemplate().getTemplateId();
        if (templateId == CustomCreatures.avengerId) {
            return true;
        } else if (templateId == CustomCreatures.spiritTrollId) {
            return true;
        } else return templateId == CustomCreatures.chargerId;
    }

    // Ensure unique creatures cannot be hitched to vehicles
    public static boolean isNotHitchable(Creature creature) {
        if (creature.isUnique()) {
            return true;
        }
        int cid = creature.getTemplate().getTemplateId();
        if (cid == CustomCreatures.avengerId) {
            return true;
        } else if (cid == CustomCreatures.giantId) {
            return true;
        } else if (cid == CustomCreatures.fireGiantId) {
            return true;
        }else if (cid == CustomCreatures.spiritTrollId) {
            return true;
        } else if (cid == CreatureTemplateIds.TROLL_CID) {
            return true;
        } else return cid == CreatureTemplateIds.GOBLIN_CID;
    }

    // Disable sacrificing strong creatures
    public static boolean isSacrificeImmune(Creature creature) {
        if (Titans.isTitan(creature) || Titans.isTitanMinion(creature)) {
            return true;
        } else if (MethodsBestiary.isRareCreature(creature)) {
            return true;
        } else return creature.isUnique();
    }

    // Disable archery altogether against certain creatures
    public static boolean isArcheryImmune(Creature performer, Creature defender) {
        if (Titans.isTitan(defender) || Titans.isTitanMinion(defender)) {
            performer.getCommunicator().sendCombatNormalMessage(String.format("You cannot archer %s, as it is protected by a Titan.", defender.getName()));
            return true;
        }
        String message = String.format("The %s would be unaffected by your arrows.", defender.getName());
        boolean immune = false;
        Item arrow = Archery.getArrow(performer);
        if (arrow == null) { // Copied directly from the attack() method in Archery.
            performer.getCommunicator().sendCombatNormalMessage("You have no arrows left to shoot!");
            return true;
        }

        //int defenderTemplateId = defender.getTemplate().getTemplateId();
        if (defender.isRegenerating() && arrow.getTemplateId() == ItemList.arrowShaft) {
            message = String.format("The %s would be unaffected by the %s.", defender.getName(), arrow.getName());
            immune = true;
        }/*else if(defender.getTemplate().isNotRebirthable()){
			immune = true;
		}*/ else if (defender.isUnique()) {
            immune = true;
        }
        if (immune) {
            performer.getCommunicator().sendCombatNormalMessage(message);
        }
        return immune;
    }

    // Auto-Genesis a creature born on enchanted grass
    public static void checkEnchantedBreed(Creature creature) {
        int tile = Server.surfaceMesh.getTile(creature.getTileX(), creature.getTileY());
        byte type = Tiles.decodeType(tile);
        if (type == Tiles.Tile.TILE_ENCHANTED_GRASS.id) {
            RequiemLogging.logInfo(String.format("Creature %s was born on enchanted grass, and has a negative trait removed!", creature.getName()));
            Server.getInstance().broadCastAction(String.format("%s was born on enchanted grass, and feels more healthy!", creature.getName()), creature, 10);
            creature.removeRandomNegativeTrait();
        }
    }

    // Used to give random names to newborn, bred creatures
    public static boolean shouldBreedName(Creature creature) {
        int cid = creature.getTemplate().getTemplateId();
        if (cid == CustomCreatures.chargerId) {
            return true;
        } else if (cid == CustomCreatures.greenWyvernId) {
            return true;
        } else if (cid == CustomCreatures.redWyvernId) {
            return true;
        } else if (cid == CustomCreatures.whiteWyvernId) {
            return true;
        } else if (cid == CustomCreatures.blackWyvernId) {
            return true;
        } else if (cid == CustomCreatures.blueWyvernId) {
            return true;
        }
        return creature.isHorse();
    }

    // Add spell resistance to custom creatures
    public static float getCustomSpellResistance(Creature creature) {
        int templateId = creature.getTemplate().getTemplateId();
        if (templateId == CustomCreatures.avengerId) {
            return 0.5f;
        } else if (templateId == CustomCreatures.chargerId) {
            return 1.4f;
        } else if (templateId == CustomCreatures.giantId) {
            return 0.3f;
        } else if (templateId == CustomCreatures.largeBoarId) {
            return 0.8f;
        } else if (templateId == CustomCreatures.reaperId) {
            return 0.1f;
        } else if (templateId == CustomCreatures.spectralDragonHatchlingId) {
            return 0.1f;
        } else if (templateId == CustomCreatures.spiritTrollId) {
            return 0.2f;
        } else if (templateId == CustomCreatures.blackWyvernId) {
            return 0.4f;
        } else if (templateId == CustomCreatures.greenWyvernId) {
            return 0.6f;
        } else if (templateId == CustomCreatures.whiteWyvernId) {
            return 0.5f;
        } else if (templateId == CustomCreatures.redWyvernId) {
            return 0.25f;
        } else if (templateId == CustomCreatures.blueWyvernId) {
            return 0.15f;
        }
        return -1f;
    }

    // Disable afk training
    public static boolean blockSkillFrom(Creature defender, Creature attacker) {
        if (defender == null || attacker == null) {
            return false;
        }
        if (defender.isPlayer() && defender.getTarget() != attacker) {
            return true;
        }
        if (defender.isPlayer()) {
            Item weap = defender.getPrimWeapon();
            if (weap != null && weap.isWeapon()) {
                try {
                    double dam = Weapon.getModifiedDamageForWeapon(weap, defender.getSkills().getSkill(SkillList.BODY_STRENGTH), true) * 1000.0;
                    dam += Server.getBuffedQualityEffect(weap.getCurrentQualityLevel() / 100f) * (double) Weapon.getBaseDamageForWeapon(weap) * 2400.0;
                    if (attacker.getArmourMod() < 0.1f) {
                        return false;
                    }
                    if (dam * attacker.getArmourMod() < 3000) {
                        return true;
                    }
                } catch (NoSuchSkillException e) {
                    e.printStackTrace();
                }
            } else {
                if (defender.getBonusForSpellEffect(Enchants.CRET_BEARPAW) < 50f) {
                    return true;
                }
            }
        }
        try {
            if (defender.isPlayer() && attacker.getArmour(BodyPartConstants.TORSO) != null) {
                return true;
            }
        } catch (NoArmourException | NoSpaceException ignored) {
        }
        return false;
    }

    // Deny ghost creatures walking through walls
    public static boolean denyPathingOverride(Creature creature) {
        if (creature.getTemplate().getTemplateId() == CustomCreatures.chargerId) {
            return true;
        } else if (creature.getTemplate().getTemplateId() == CustomCreatures.ghostHellHorseId) {
            return true;
        } else if (creature.getTemplate().getTemplateId() == CustomCreatures.ghostHorseId) {
            return true;
        } else if (creature.getTemplate().getTemplateId() == CreatureTemplateIds.DRAKESPIRIT_CID) {
            return true;
        } else if (creature.getTemplate().getTemplateId() == CustomCreatures.spiritTrollId) {
            return true;
        } else if (creature.getTemplate().getTemplateId() == CreatureTemplateIds.EAGLESPIRIT_CID) {
            return true;
        } else if (creature.getTemplate().getTemplateId() == CustomCreatures.chargerId) {
            return true;
        } else return creature.getTemplate().getTemplateId() == CustomCreatures.avengerId;
    }

    // Apply random types to creatures in the wilderness
    public static byte newCreatureType(int templateid, byte ctype) throws Exception {
        CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(templateid);
        if (ctype == 0 && (template.isAggHuman() || template.getBaseCombatRating() > 10) && !template.isUnique() && !Titans.isTitan(templateid) && !MethodsBestiary.isConditionExempt(templateid)) {
            if (Server.rand.nextInt(5) == 0) {
                ctype = (byte) (Server.rand.nextInt(11) + 1);
                if (Server.rand.nextInt(50) == 0) {
                    ctype = 99;
                }
            }
        }
        return ctype;
    }

    public static void modifyNewCreature(Creature creature) {
        try {
            int id = creature.getTemplate().getTemplateId();
            Body bodyPart = creature.getBody();
            Village village = creature.getCurrentTile().getVillage();
            String jingleSound = "sound.emote.bucketthree.jump";

            if (LoggingConstants.creatureCreateLogging) {
                RequiemLogging.CreatureSpawnLogging(creature);
            }
            // Titans
            if (Titans.isTitan(creature)) {
                String message = String.format("The titan %s has stepped into the mortal realm. Challenge %s if you dare", creature.getNameWithoutPrefixes(), creature.getHisHerItsString());
                Titans.addTitan(creature);
                DiscordHandler.sendToDiscord(CustomChannel.TITAN, message);
                Server.getInstance().broadCastAlert(message, true, displayOnScreen);
            }
            // Rare Creatures
            else if (MethodsBestiary.isRareCreature(creature)) {
                String message = String.format("A rare %s has surfaced.", creature.getNameWithoutPrefixes());
                DiscordHandler.sendToDiscord(CustomChannel.EVENTS, message);
                Server.getInstance().broadCastAlert(message, true, displayOnScreen);
            }
            // Zombie Leader
            else if (id == CustomCreatures.zombieLeaderId) {
                CreatureSpawns.spawnZombieHorde(creature);
            }
            // Wolf Packmaster
            else if (id == CustomCreatures.wolfPackmasterId) {
                CreatureSpawns.spawnWolves(creature);
            }
            // Insert Clubs into Creatures
            else if (id == CustomCreatures.cyclopsId || id == CustomCreatures.fireGiantId || id == CustomCreatures.facebreykerId || id == CustomCreatures.giantId || id == CustomCreatures.depotTrollId) {
                Item shodClub = ItemFactory.createItem(ItemList.clubHuge, (float) RandomUtils.generateRandomDoubleInRange(50, 99), ItemMaterials.MATERIAL_WOOD_BIRCH, MiscConstants.COMMON, null);
                bodyPart.getBodyPart(BodyTemplate.rightHand).insertItem(shodClub, true);
            }
            // White Buffalo
            else if (id == CustomCreatures.whiteBuffaloId) {
                String message = String.format("The %s has appeared somewhere in the world. If killed, its spirit will seek vengeance upon those that destroyed its physical form.", creature.getNameWithoutPrefixes());
                DiscordHandler.sendToDiscord(CustomChannel.EVENTS, message);
                Server.getInstance().broadCastAlert(message, true, displayOnScreen);
            }
            // Frosty
            else if (id == CustomCreatures.frostyId) {
                String message = String.format("%s the snowman has appeared somewhere in the world. If you can find him and give him a blessed snowball, he will give you something in return!", creature.getNameWithoutPrefixes());
                SoundPlayer.playSound(jingleSound, creature, 0f);
                DiscordHandler.sendToDiscord(CustomChannel.EVENTS, message);
                Server.getInstance().broadCastAlert(message, true, displayOnScreen);
            }
            // Rudolph
            else if (id == CustomCreatures.rudolphId) {
                String message = String.format("%s has appeared somewhere in the world. Santa must not be far away!.", creature.getNameWithoutPrefixes());
                SoundPlayer.playSound(jingleSound, creature, 0f);
                DiscordHandler.sendToDiscord(CustomChannel.EVENTS, message);
                Server.getInstance().broadCastAlert(message, true, displayOnScreen);
            }
            // Snowman
            else if (id == CustomCreatures.snowmanId) {
                SoundPlayer.playSound(jingleSound, creature, 0f);
            }
            // Dock Worker
            else if (id == CustomCreatures.npcDockWorkerId) {
                Item mallet = ItemFactory.createItem(ItemList.hammerWood, (float) RandomUtils.generateRandomDoubleInRange(60, 90), ItemMaterials.MATERIAL_WOOD_BIRCH, MiscConstants.COMMON, null);
                bodyPart.getBodyPart(BodyTemplate.rightHand).insertItem(mallet, true);
                soundEmissionNpcs.add(creature);
            }
            // Santa Claus
            else if (id == CreatureTemplateIds.SANTA_CLAUS) {
                String message = String.format("HO HO HO, Merry Christmas everyone! %s has landed and is now taking up residence in %s, be sure to visit him on christmas or look under your own christmas tree to get your present.", creature.getName(), village.getName());
                SoundPlayer.playSound(jingleSound, creature, 0f);
                DiscordHandler.sendToDiscord(CustomChannel.EVENTS, message);
                Server.getInstance().broadCastAlert(message, true, displayOnScreen);
            }
            // Horseman Conquest
            else if (id == CustomCreatures.horsemanConquestId) {
                String message = "The 4 Horseman of the apocalypse have begun to enter our mortal realm to end the lives of all living things. Stop them before they complete their task.";
                Creature horse = Creature.doNew(CreatureTemplateIds.HORSE_CID, creature.getPosX(), creature.getPosY(), 360f * Server.rand.nextFloat(), creature.getLayer(), creature.getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE);
                SoundPlayer.playSound(SoundNames.HIT_HORSE_SND, creature, 0f);
                creature.setVehicle(horse.getWurmId(), true, (byte) 3);
                DiscordHandler.sendToDiscord(CustomChannel.EVENTS, message);
                Server.getInstance().broadCastAlert(message, true, displayOnScreen);
            }
            // Horseman War
            else if (id == CustomCreatures.horsemanWarId) {
                String message = "The 4 Horseman of the apocalypse have begun to enter our mortal realm to end the lives of all living things. Stop them before they complete their task.";
                Creature horse = Creature.doNew(CreatureTemplateIds.HORSE_CID, creature.getPosX(), creature.getPosY(), 360f * Server.rand.nextFloat(), creature.getLayer(), creature.getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE);
                horse.setModelName("model.creature.quadraped.horse.ebonyblack.");
                SoundPlayer.playSound(SoundNames.HIT_HORSE_SND, creature, 0f);
                creature.setVehicle(horse.getWurmId(), true, (byte) 3);
                DiscordHandler.sendToDiscord(CustomChannel.EVENTS, message);
                Server.getInstance().broadCastAlert(message, true, displayOnScreen);
            }
            // Horseman Famine
            else if (id == CustomCreatures.horsemanFamineId) {
                String message = "The 4 Horseman of the apocalypse have begun to enter our mortal realm to end the lives of all living things. Stop them before they complete their task.";
                Creature horse = Creature.doNew(CreatureTemplateIds.HORSE_CID, creature.getPosX(), creature.getPosY(), 360f * Server.rand.nextFloat(), creature.getLayer(), creature.getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE);
                horse.setModelName("model.creature.quadraped.horse.white.");
                SoundPlayer.playSound(SoundNames.HIT_HORSE_SND, creature, 0f);
                creature.setVehicle(horse.getWurmId(), true, (byte) 3);
                DiscordHandler.sendToDiscord(CustomChannel.EVENTS, message);
                Server.getInstance().broadCastAlert(message, true, displayOnScreen);
            }
            // Horseman Death
            else if (id == CustomCreatures.horsemanDeathId) {
                String message = "The 4 Horseman of the apocalypse have begun to enter our mortal realm to end the lives of all living things. Stop them before they complete their task.";
                Creature horse = Creature.doNew(CreatureTemplateIds.HORSE_CID, creature.getPosX(), creature.getPosY(), 360f * Server.rand.nextFloat(), creature.getLayer(), creature.getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE);
                horse.setModelName("model.creature.quadraped.horse.white.");
                SoundPlayer.playSound(SoundNames.HIT_HORSE_SND, creature, 0f);
                creature.setVehicle(horse.getWurmId(), true, (byte) 3);
                ChatHandler.systemMessage((Player) creature, CustomChannel.EVENTS, "The 4 Horseman of the apocalypse have begun to enter our mortal realm to end the lives of all living things. Stop them before they complete their task.", 255, 255, 255);
                DiscordHandler.sendToDiscord(CustomChannel.EVENTS, message);
                Server.getInstance().broadCastAlert(message, true, displayOnScreen);
            }
            // Treasure Goblin
            else if (id == CustomCreatures.treasureGoblinId) {
                String message = "The 4 Horseman of the apocalypse have begun to enter our mortal realm to end the lives of all living things. Stop them before they complete their task.";
                Item shortSword = ItemFactory.createItem(ItemList.swordShort, 50f + (Server.rand.nextFloat() * 30f), ItemMaterials.MATERIAL_STEEL, MiscConstants.COMMON, null);
                shortSword.setName("goblin shortsword");
                bodyPart.getBodyPart(BodyTemplate.rightHand).insertItem(shortSword, true);
                Server.getInstance().broadCastAction(String.format("%s finds a short sword and holds it in its hand.", creature.getName()), creature, 8);
                ChatHandler.systemMessage((Player) creature, CustomChannel.EVENTS, String.format("A %s has surfaced. Be quick to catch it before someone else does!", creature.getNameWithoutPrefixes()), 242, 242, 8);
                DiscordHandler.sendToDiscord(CustomChannel.EVENTS, message);
                Server.getInstance().broadCastAlert(message, true, displayOnScreen);
            }
            // Menagerist Goblin
            else if (id == CustomCreatures.treasureGoblinMenageristGoblinId) {
                String message = String.format("A %s has surfaced. Be quick to catch it before someone else does!", creature.getNameWithoutPrefixes());
                Item shortSword = ItemFactory.createItem(ItemList.swordShort, 50f + (Server.rand.nextFloat() * 30f), ItemMaterials.MATERIAL_STEEL, MiscConstants.COMMON, null);
                shortSword.setName("goblin shortsword");
                bodyPart.getBodyPart(BodyTemplate.rightHand).insertItem(shortSword, true);
                Server.getInstance().broadCastAction(String.format("%s finds a short sword and holds it in its hand.", creature.getName()), creature, 8);
                DiscordHandler.sendToDiscord(CustomChannel.EVENTS, message);
                Server.getInstance().broadCastAlert(message, true, displayOnScreen);
            }
            // Injured Pirate
            else if (creature.getNameWithoutPrefixes().equals("Injured Pirate")) {
                Item shortSword = ItemFactory.createItem(ItemList.swordShort, 50f + (Server.rand.nextFloat() * 30f), ItemMaterials.MATERIAL_STEEL, MiscConstants.COMMON, null);
                shortSword.setName("pirate shortsword");
                bodyPart.getBodyPart(BodyTemplate.rightHand).insertItem(shortSword, true);
                Server.getInstance().broadCastAction(String.format("The %s pulls a short sword from its sheath and holds it in %s hand.", creature.getName().toLowerCase(), creature.getHisHerItsString()), creature, 6);
                //ChatTabs.sendLocalChat(creature, "YARRRRR!!! Who goes there? How dare ye try to steal ma' booty you scallywagger", 1, 220, 1);
                //ChatHandler.systemMessage();
                //CreatureTools.getCreaturesAround(creature, 10);
                //for ()
            }
            // Fog Goblin
            else if (id == CustomCreatures.fogGoblinId) {
                Item potion = Misc.createItem(ItemList.potion, 100.0F);
                creature.getInventory().insertItem(potion);
                int reward = Server.rand.nextInt(100);
                if (reward >= 20) {
                    Item secondPotion = Misc.createItem(ItemList.potion, 100.0F);
                    creature.getInventory().insertItem(secondPotion);
                }
                if (reward >= 60) {
                    Item thirdPotion = Misc.createItem(ItemList.potion, 100.0F);
                    creature.getInventory().insertItem(thirdPotion);
                }
                if (reward >= 90) {
                    Item yellowPotion = Misc.createItem(ItemList.potionIllusion, 100.0F);
                    creature.getInventory().insertItem(yellowPotion);
                }
                if (reward >= 97) {
                    int potionId = RandomUtils.randomPotionTemplates();
                    Item specialPotion = Misc.createItem(potionId, 100.0F);
                    creature.getInventory().insertItem(specialPotion);
                }
            }
            // Black Knight
            else if (id == CustomCreatures.blackKnightId) {
                CreatureSpawns.blackKnightSpawn(creature);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Block items from being moved to certain items
    public static boolean blockMove(Item source, Item target, Creature performer) {
        if (target.getTemplateId() == CustomVehicles.loggingWagon.getTemplateId()) {
            if (source.getTemplateId() != ItemList.logHuge) {
                performer.getCommunicator().sendNormalServerMessage("Only felled trees can be put into a logging wagon.");
                return true;
            }
        }
        return false;
    }

    // change item size
    public float getSizeModifier(Item item) {
        int id = item.getTemplate().getTemplateId();
        //RequiemLogging.debug("ItemMod.itemSizeMod called");
        if (id == CustomItems.machinaOfFortuneId) {
            return -2.5f;
        } else if (id == ItemList.sleepPowder) {
            return 3f;
        } else if (id == largeWheelId) {
            return -2.0f;
        } else if (id == resizeItemId) {
            return resizeItemSize;
        }
        return 1f;
    }

    /**
     * Here we control what happens when items are destroyed
     *
     * @param comm player vision
     * @param item item being destroyed
     */
    public static void removeItemHook(Communicator comm, Item item) {
        int templateId;
        templateId = item.getTemplateId();
        if (itemRemoveLogging) {
            RequiemLogging.ItemRemovalLogging(item);
        }
        if (templateId == CustomItems.fireCrystal.getTemplateId()) {
            comm.sendRemoveEffect(item.getWurmId());
        } else if (templateId == CustomItems.frostCrystal.getTemplateId()) {
            comm.sendRemoveEffect(item.getWurmId());
        } else if (templateId == CustomItems.deathCrystal.getTemplateId()) {
            comm.sendRemoveEffect(item.getWurmId());
        } else if (templateId == CustomItems.lifeCrystal.getTemplateId()) {
            comm.sendRemoveEffect(item.getWurmId());
        } else if (templateId == CustomItems.essenceOfWoodId) {
            comm.sendRemoveEffect(item.getWurmId());
        } else if (templateId == CustomVehicles.altarWagon.getTemplateId()) {
            comm.sendRemoveEffect(item.getWurmId());
        } else if (templateId == CustomItems.campfire.getTemplateId()) {
            comm.sendRemoveEffect(item.getWurmId());
        } else if (templateId == CustomItems.eventGravestoneId) {
            comm.sendRemoveEffect(item.getWurmId());
        }
    }

    private static void doRemoveItem(Communicator comm, Item item) {
        if (!DisplayHookRegistry.doRemoveItem(comm, item))
            comm.sendRemoveItem(item);
    }

    /**
     * Here we control what happens when items are destroyed
     *
     * @param item former parent
     * @param ret  item that was removed
     */
    public static void removeFromItemHook(Item item, Item ret) {
        if (itemRemoveLogging) {
            RequiemLogging.ItemRemovalLogging(item);
        }
        if (item.getTemplateId() == CustomItems.steelToolsBackpack.getTemplateId() && item.isEmpty(false)) {
            Item parent = item.getParentOrNull();
            if (parent != null && parent.getParentId() == MiscConstants.NOID) {
                Utils.forAllWatchers(parent, (player) -> doRemoveItem(player.getCommunicator(), ret));
            }
            if (ret.getParentId() == item.getWurmId())
                ret.setParentId(item.getParentId(), item.isOnSurface());
            Set<Creature> watchers = ret.getWatcherSet();
            if (watchers != null) {
                for (Creature creature : watchers) {
                    if (creature.getCommunicator().sendCloseInventoryWindow(ret.getWurmId())) {
                        ret.removeWatcher(creature, true);
                    }
                }
            }
            if (item.getItemsAsArray().length == 0)
                Items.destroyItem(item.getWurmId());
        }
        if (item.getTemplateId() == CustomItems.addyToolsBackpack.getTemplateId() && item.isEmpty(false)) {
            Item parent = item.getParentOrNull();
            if (parent != null && parent.getParentId() == MiscConstants.NOID) {
                Utils.forAllWatchers(parent, (player) -> doRemoveItem(player.getCommunicator(), ret));
            }
            if (ret.getParentId() == item.getWurmId())
                ret.setParentId(item.getParentId(), item.isOnSurface());
            Set<Creature> watchers = ret.getWatcherSet();
            if (watchers != null) {
                for (Creature creature : watchers) {
                    if (creature.getCommunicator().sendCloseInventoryWindow(ret.getWurmId())) {
                        ret.removeWatcher(creature, true);
                    }
                }
            }
            if (item.getItemsAsArray().length == 0)
                Items.destroyItem(item.getWurmId());
        }
    }

    public static float newMountSpeedMultiplier(Creature creature, boolean mounting) {
        float hunger = creature.getStatus().getHunger() / 65535f;
        float damage = creature.getStatus().damage / 65535f;
        float factor = ((((1f - damage * damage) * (1f - damage) + (1f - 2f * damage) * damage) * (1f - damage) + (1f - damage) * damage) * (1f - 0.4f * hunger * hunger));
        try {
            float traitMove = ReflectionUtil.callPrivateMethod(creature, ReflectionUtil.getMethod(creature.getClass(), "getTraitMovePercent"), mounting);
            factor += traitMove;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (creature.isHorse() || creature.isUnicorn()) {
            factor *= CustomMountSettings.newCalcHorseShoeBonus(creature);
        }
        /*if(creature.isHorse()){
            try {
                Item barding = creature.getArmour(BodyPartConstants.TORSO);
                if(barding != null){
                    if(barding.getTemplateId() == ItemList.clothBarding){
                        factor *= 0.9f;
                    }else if(barding.getTemplateId() == ItemList.leatherBarding){
                        factor *= 0.82f;
                    }else if(barding.getTemplateId() == ItemList.chainBarding){
                        factor *= 0.75f;
                    }
                }
            } catch (NoArmourException | NoSpaceException ignored) {
            }
        }*/
        if (creature.getBonusForSpellEffect(Enchants.CRET_OAKSHELL) > 0.0f) {
            factor *= 1f - (0.3f * (creature.getBonusForSpellEffect(Enchants.CRET_OAKSHELL) / 100.0f));
        }
        if (creature.isRidden()) {
            try {
                float saddleFactor = 1.0f;
                Item saddle = creature.getEquippedItem(BodyPartConstants.TORSO);
                if (saddle != null) {
                    saddle.setDamage(saddle.getDamage() + (saddle.getDamageModifier() * 0.001f));
                    saddleFactor += Math.max(10f, saddle.getCurrentQualityLevel()) / 2000f;
                    saddleFactor += saddle.getSpellSpeedBonus() / 2000f;
                    saddleFactor += saddle.getRarity() * 0.03f;
                    factor *= saddleFactor;
                }
            } catch (NoSpaceException ignored) {
            }
            factor *= creature.getMovementScheme().getSpeedModifier();
        }
        return factor;
    }

}
