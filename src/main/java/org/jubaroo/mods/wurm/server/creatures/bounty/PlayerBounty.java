package org.jubaroo.mods.wurm.server.creatures.bounty;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemTemplate;
import com.wurmonline.server.items.NoSuchTemplateException;
import com.wurmonline.server.players.Player;
import org.jubaroo.mods.wurm.server.creatures.Titans;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.tools.ItemTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class PlayerBounty {
    protected static final Random random = new Random();
    public static HashMap<String, Long> steamIdMap = new HashMap<>();
    public static HashMap<Long, ArrayList<Long>> playersRewarded = new HashMap<>();

    public static double getTypeBountyMod(Creature mob, String mobType) {
        if (!mob.isUnique()) {
            if (mobType.endsWith("fierce ")) {
                return 1.5;
            } else if (mobType.endsWith("angry ")) {
                return 1.4;
            } else if (mobType.endsWith("raging ")) {
                return 1.6;
            } else if (mobType.endsWith("slow ")) {
                return 0.95;
            } else if (mobType.endsWith("alert ")) {
                return 1.2;
            } else if (mobType.endsWith("greenish ")) {
                return 1.7;
            } else if (mobType.endsWith("lurking ")) {
                return 1.1;
            } else if (mobType.endsWith("sly ")) {
                return 0.8;
            } else if (mobType.endsWith("hardened ")) {
                return 1.3;
            } else if (mobType.endsWith("scared ")) {
                return 0.85;
            } else if (mobType.endsWith("diseased ")) {
                return 0.9;
            } else if (mobType.endsWith("champion ")) {
                return 2.0;
            }
        }
        return 1.0;
    }

    public static void rewardPowerfulLoot(Player player, Creature mob) {
        try {
            // Affinity Orb:
            player.getInventory().insertItem(ItemFactory.createItem(CustomItems.affinityOrbId, 99f + (random.nextFloat()), ""));
            // Enchant Orb:
            float power;
            if (mob.getStatus().isChampion() || Titans.isTitan(mob)) {
                power = 100f + (random.nextFloat() * 20f);
            } else {
                power = 90f + (random.nextFloat() * 30f);
            }
            Item enchantOrb = ItemTools.createEnchantOrb(power);
            assert enchantOrb != null;
            player.getInventory().insertItem(enchantOrb);
            player.getCommunicator().sendSafeServerMessage(String.format("Libila takes the %s's soul,  but leaves something else behind...", mob.getNameWithoutPrefixes()));
        } catch (NoSuchTemplateException | FailedException e) {
            e.printStackTrace();
        }
    }

    public static void rewardSpectralLoot(Player player) {
        try {
            final double fightSkill = player.getFightingSkill().getKnowledge();
            final Item spectralHide = ItemFactory.createItem(CustomItems.spectralHideId, 70 + (30 * random.nextFloat()), "");
            final ItemTemplate hideTemplate = spectralHide.getTemplate();
            final int weightGrams = hideTemplate.getWeightGrams();
            spectralHide.setWeight((int) ((weightGrams * 0.25f) + (weightGrams * 0.25f * fightSkill / 100f * random.nextFloat())), true);
            player.getInventory().insertItem(spectralHide);
            String fightStrength = "strong";
            if (fightSkill >= 60) {
                fightStrength = "great";
            }
            if (fightSkill >= 70) {
                fightStrength = "powerful";
            }
            if (fightSkill >= 80) {
                fightStrength = "master";
            }
            if (fightSkill >= 90) {
                fightStrength = "legendary";
            }
            player.getCommunicator().sendSafeServerMessage(String.format("The spirit recognizes you as a %s warrior, and rewards you accordingly.", fightStrength));
        } catch (NoSuchTemplateException | FailedException e) {
            e.printStackTrace();
        }
    }

}
