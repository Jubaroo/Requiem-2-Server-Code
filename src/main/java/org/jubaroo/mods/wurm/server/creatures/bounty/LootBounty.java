package org.jubaroo.mods.wurm.server.creatures.bounty;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.*;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.CreatureTemplateFactory;
import com.wurmonline.server.creatures.Creatures;
import com.wurmonline.server.items.*;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.villages.Village;
import com.wurmonline.server.villages.Villages;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.communication.discord.ChatHandler;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.communication.discord.DiscordHandler;
import org.jubaroo.mods.wurm.server.creatures.Bounty;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class LootBounty {

    public static void sendLootAssist(Creature c, Player p) {
        RequiemLogging.logInfo("Beginning loot assistance message generation...");
        LootBounty.displayLootAssistance(c, p);
    }

    public static void displayLootAssistance(Creature mob, Player player) {
        if (Bounty.dealtDamage.containsKey(mob.getWurmId())) {
            RequiemLogging.logInfo("Found the damageDealt entry, parsing...");
            ArrayList<String> names = new ArrayList<String>();
            ArrayList<Double> damages = new ArrayList<Double>();
            for (long creatureId : Bounty.dealtDamage.get(mob.getWurmId()).keySet()) {
                if (Players.getInstance().getPlayerOrNull(creatureId) != null) {
                    names.add(Players.getInstance().getPlayerOrNull(creatureId).getName());
                    damages.add(Bounty.dealtDamage.get(mob.getWurmId()).get(creatureId));
                } else {
                    if (Creatures.getInstance().getCreatureOrNull(creatureId) != null) {
                        RequiemLogging.logInfo(String.format("Skipping creature %s in loot assistance.", Creatures.getInstance().getCreatureOrNull(creatureId).getName()));
                    }
                }
            }
            RequiemLogging.logInfo("Names have been added: " + names);
            String strBuilder = String.format("Loot Assistance <Damagers> (%s): ", mob.getName());
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            while (names.size() > 0) {
                int index = Server.rand.nextInt(names.size());
                strBuilder += names.get(index);
                strBuilder += " [" + formatter.format(Math.round(damages.get(index))) + "]";
                names.remove(index);
                damages.remove(index);
                if (names.size() > 0) {
                    strBuilder += ", ";
                }
            }
            ChatHandler.systemMessage(player, CustomChannel.EVENTS, strBuilder);
            RequiemLogging.logInfo("Broadcast loot assistance message success [Damage].");
        } else {
            RequiemLogging.logWarning(String.format("Powerful creature %s died, but no players were credited to its death [Damage].", mob.getName()));
        }
    }

    public static int doRollingCrystalReward(Creature mob, Item corpse, double cretStr, int templateId, int chance, double reductionPerRoll) {
        try {
            double rollingCounter = cretStr;
            int addedCrystals = 0;
            if (mob.isUnique()) { // Uniques will drop 3x as many, and have special properties to enable dropping rare and possibly supreme versions as well.
                rollingCounter *= 3;
            } else if (Servers.localServer.PVPSERVER) { // Arena gives double the amount of crystals.
                rollingCounter *= 2;
            }
            while (rollingCounter > 0) {
                if (Server.rand.nextInt(chance + addedCrystals) == 0) { // Give a chance at a crystal, decreasing with the amount of crystals contained.
                    // The crystal quality is the cube root of the rolling counter, capped at 100 of course
                    Item chaosCrystal = ItemFactory.createItem(templateId, (float) (Server.rand.nextFloat() * Math.min(100, Math.cbrt(rollingCounter))), "");
                    if (Server.rand.nextInt(40) == 0) {
                        chaosCrystal.setRarity((byte) 1);
                    } else if (mob.isUnique() && Server.rand.nextInt(5) == 0) {
                        if (Server.rand.nextInt(5) == 0) {
                            chaosCrystal.setRarity((byte) 2);
                        } else {
                            chaosCrystal.setRarity((byte) 1);
                        }
                    }
                    corpse.insertItem(chaosCrystal);
                    addedCrystals++;
                }
                rollingCounter -= reductionPerRoll;
            }
            return addedCrystals;
        } catch (FailedException | NoSuchTemplateException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void insertUniqueLoot(Creature mob, Item corpse) {
        try {
            Item affinityOrb = ItemFactory.createItem(CustomItems.affinityOrbId, 90 + (10 * Server.rand.nextFloat()), "");
            corpse.insertItem(affinityOrb);
            int[] cacheIds = {
                    CustomItems.gemCache.getTemplateId(), CustomItems.gemCache.getTemplateId(),
                    CustomItems.riftCache.getTemplateId(),
                    CustomItems.treasureMapCache.getTemplateId()
            };
            int i = 1 + Server.rand.nextInt(3);
            while (i > 0) {
                Item cache = ItemFactory.createItem(cacheIds[Server.rand.nextInt(cacheIds.length)], 50 + (30 * Server.rand.nextFloat()), "");
                if (Server.rand.nextInt(5) == 0) {
                    cache.setRarity((byte) 1);
                }
                corpse.insertItem(cache);
                i--;
            }
            if (mob.isDragon()) {
                int mTemplate = mob.getTemplate().getTemplateId();
                int lootTemplate = ItemList.drakeHide;
                if (mTemplate == CreatureTemplateFactory.DRAGON_BLACK_CID || mTemplate == CreatureTemplateFactory.DRAGON_BLUE_CID || mTemplate == CreatureTemplateFactory.DRAGON_GREEN_CID
                        || mTemplate == CreatureTemplateFactory.DRAGON_RED_CID || mTemplate == CreatureTemplateFactory.DRAGON_WHITE_CID) {
                    lootTemplate = ItemList.dragonScale;
                }
                RequiemLogging.logInfo(String.format("Generating extra hide & scale to insert on the corpse of %s.", mob.getName()));
                ItemTemplate itemTemplate = ItemTemplateFactory.getInstance().getTemplate(lootTemplate);
                for (i = 0; i < 2; i++) {
                    Item loot = ItemFactory.createItem(lootTemplate, 80 + (15 * Server.rand.nextFloat()), "");
                    String creatureName = mob.getTemplate().getName().toLowerCase();
                    if (!loot.getName().contains(creatureName)) {
                        loot.setName(creatureName.toLowerCase() + " " + itemTemplate.getName());
                    }
                    loot.setData2(mTemplate);
                    int weightGrams = itemTemplate.getWeightGrams() * (lootTemplate == ItemList.drakeHide ? 3 : 1);
                    loot.setWeight((int) ((weightGrams * 0.02f) + (weightGrams * 0.02f * Server.rand.nextFloat())), true);
                    corpse.insertItem(loot);
                }
            }
        } catch (FailedException | NoSuchTemplateException e) {
            e.printStackTrace();
        }
    }

    public static void blessWorldWithMoonVeins(Creature mob) {
        int i = 8 + Server.rand.nextInt(5);
        while (i > 0) {
            int x = Server.rand.nextInt(Server.surfaceMesh.getSize());
            int y = Server.rand.nextInt(Server.surfaceMesh.getSize());
            short height = Tiles.decodeHeight(Server.surfaceMesh.getTile(x, y));
            int type = Tiles.decodeType(Server.caveMesh.getTile(x, y));

            if (height >= 100 && (type == Tiles.Tile.TILE_CAVE_WALL.id || type == Tiles.Tile.TILE_CAVE.id)) {
                Tiles.Tile tileType = Server.rand.nextBoolean() ? Tiles.Tile.TILE_CAVE_WALL_ORE_ADAMANTINE : Tiles.Tile.TILE_CAVE_WALL_ORE_GLIMMERSTEEL;
                Server.caveMesh.setTile(x, y, Tiles.encode(Tiles.decodeHeight(Server.caveMesh.getTile(x, y)), tileType.id, Tiles.decodeData(Server.caveMesh.getTile(x, y))));
                Players.getInstance().sendChangedTile(x, y, false, true);
                Server.setCaveResource(x, y, 400 + Server.rand.nextInt(600));
                Village v = Villages.getVillage(x, y, true);

                if (v == null) {
                    for (int vx = -20; vx < 20; vx += 5) {
                        for (int vy = -20; vy < 20 && (v = Villages.getVillage(x + vx, y + vy, true)) == null; vy += 5) {
                            if (v != null) {
                                break;
                            }
                        }
                    }
                }

                if (v != null) {
                    HistoryManager.addHistory(mob.getTemplate().getName(), String.format("blesses the world with a %s near %s!", tileType.getName(), v.getName()));
                    DiscordHandler.sendToDiscord(CustomChannel.EVENTS, String.format("%s blesses the world with a %s near %s!", mob.getTemplate().getName(), tileType.getName(), v.getName()));
                }
                RequiemLogging.logInfo(String.format("Placed a %s at %d, %d - %s height", tileType.getName(), x, y, height));
                i--;
            }
        }
        Server.getInstance().broadCastAlert(String.format("The death of the %s has blessed the world with valuable ores!", mob.getTemplate().getName()));
    }

    public static void spawnFriyanTablets(int min, int max) {
        int i = Server.rand.nextInt((max - min) + 1) + min;
        while (i > 0) {
            int x = Server.rand.nextInt(Server.surfaceMesh.getSize());
            int y = Server.rand.nextInt(Server.surfaceMesh.getSize());
            short height = Tiles.decodeHeight(Server.surfaceMesh.getTile(x, y));
            if (height > 0 && height < 1000 && Creature.getTileSteepness(x, y, true)[1] < 30) {
                try {
                    ItemFactory.createItem(CustomItems.friyanTabletId, 80f + Server.rand.nextInt(20), (float) x * 4, (float) y * 4, Server.rand.nextFloat() * 360f, true, (byte) 0, -10, "Friyanouce");
                    RequiemLogging.logInfo(String.format("Created a Tablet of Friyan at %d, %d.", x, y));
                } catch (NoSuchTemplateException | FailedException e) {
                    e.printStackTrace();
                }
                i--;
            }
        }
        String message = String.format("%d Tablets of Friyan were created somewhere in the world!", i);
        Server.getInstance().broadCastAlert(message);
        DiscordHandler.sendToDiscord(CustomChannel.EVENTS, message);
    }

    public static void handleDragonLoot(Creature mob) {
        try {
            int mTemplate = mob.getTemplate().getTemplateId();
            int lootTemplate = ItemList.drakeHide;
            byte ctype;
            if (mTemplate == CreatureTemplateFactory.DRAGON_BLACK_CID || mTemplate == CreatureTemplateFactory.DRAGON_BLUE_CID || mTemplate == CreatureTemplateFactory.DRAGON_GREEN_CID
                    || mTemplate == CreatureTemplateFactory.DRAGON_RED_CID || mTemplate == CreatureTemplateFactory.DRAGON_WHITE_CID) {
                ctype = 99; // Champion creature type
                lootTemplate = ItemList.dragonScale;
            } else {
                ctype = (byte) Math.max(0, Server.rand.nextInt(17) - 5);
            }

            float x = mob.getPosX();
            float y = mob.getPosY();

            /*
            // Spawn the spectral drake.
            RequiemLogging.logInfo("Spawning a spectral drake.");
            CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(CustomCreatures.spectralDragonHatchlingId);
            Creature spectralDrake = Creature.doNew(template.getTemplateId(), true, x, y, Server.rand.nextFloat() * 360f, mob.getLayer(), template.getName(), (byte) 0, mob.getKingdomId(), ctype, false, (byte) 150);
            Server.getInstance().broadCastAction("The spirit of the " + mob.getTemplate().getName() + " is released into the world!", mob, 20);
            Server.getInstance().broadCastAlert(spectralDrake.getName() + " is released from the soul of the " + mob.getTemplate().getName() + ", seeking vengeance for its physical form!");
            // Insert extra hide / scale

            RequiemLogging.logInfo(String.format("Generating extra hide & scale to insert on the corpse of %s.", mob.getName()));
            ItemTemplate itemTemplate = ItemTemplateFactory.getInstance().getTemplate(lootTemplate);
            for (int i = 0; i < 2; i++) {
                Item loot = ItemFactory.createItem(lootTemplate, 80 + (15 * Server.rand.nextFloat()), "");
                String creatureName = mob.getTemplate().getName().toLowerCase();
                if (!loot.getName().contains(creatureName)) {
                    loot.setName(creatureName.toLowerCase() + " " + itemTemplate.getName());
                }
                loot.setData2(mTemplate);
                int weightGrams = itemTemplate.getWeightGrams() * (lootTemplate == 371 ? 3 : 1);
                loot.setWeight((int) ((weightGrams * 0.1f) + (weightGrams * 0.1f * Server.rand.nextFloat())), true);
                corpse.insertItem(loot);
            }
            for (int i = 0; i < 4; i++) {
                Item loot = ItemFactory.createItem(lootTemplate, 80 + (15 * Server.rand.nextFloat()), "");
                String creatureName = mob.getTemplate().getName().toLowerCase();
                if (!loot.getName().contains(creatureName)) {
                    loot.setName(creatureName.toLowerCase() + " " + itemTemplate.getName());
                }
                loot.setData2(mTemplate);
                int weightGrams = itemTemplate.getWeightGrams() * (lootTemplate == 371 ? 3 : 1);
                loot.setWeight((int) ((weightGrams * 0.05f) + (weightGrams * 0.05f * Server.rand.nextFloat())), true);
                corpse.insertItem(loot);
            }
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
