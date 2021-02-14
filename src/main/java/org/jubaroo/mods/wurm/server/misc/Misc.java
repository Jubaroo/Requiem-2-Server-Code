package org.jubaroo.mods.wurm.server.misc;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.*;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.CreatureTemplate;
import com.wurmonline.server.creatures.CreatureTemplateFactory;
import com.wurmonline.server.creatures.Creatures;
import com.wurmonline.server.economy.Economy;
import com.wurmonline.server.epic.Hota;
import com.wurmonline.server.items.*;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.skills.Affinity;
import com.wurmonline.server.sounds.SoundPlayer;
import com.wurmonline.server.villages.Citizen;
import com.wurmonline.server.villages.Village;
import com.wurmonline.server.zones.FocusZone;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zone;
import com.wurmonline.server.zones.Zones;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.communication.discord.DiscordHandler;
import org.jubaroo.mods.wurm.server.creatures.CustomCreatures;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.misc.templates.SpawnerTemplate;
import org.jubaroo.mods.wurm.server.misc.templates.StructureTemplate;
import org.jubaroo.mods.wurm.server.tools.EffectsTools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;

import static org.jubaroo.mods.wurm.server.ModConfig.*;
import static org.jubaroo.mods.wurm.server.server.constants.CreatureConstants.soundEmissionNpcs;
import static org.jubaroo.mods.wurm.server.server.constants.ItemConstants.*;
import static org.jubaroo.mods.wurm.server.server.constants.PollingConstants.fogGoblins;

public class Misc {
    public final static String noobMessage = "Check out the Server Info tab to the left for all the information you could need about this custom server! To view the links, right click on them to open it in your default internet browser.";

    public static String formatYear(int num) {
        String[] suffix = {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        int m = num % 100;
        return num + suffix[(m > 3 && m < 21) ? 0 : (m % 10)];
    }

    public static boolean isDayOfMonth(int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH) == dayOfMonth;
    }

    public static boolean isRequiemPatreon(Player player) {
        if ("Taita".equals(player.getName())) {
            return true;
        } else return "Zeus".equals(player.getName());
    }

    public static boolean isWeaponOrArmor(Item target) {
        return target.isWeapon() || target.isArmour() || target.isShield() || target.isWeaponBow() || target.isBowUnstringed();
    }

    public static void sendHotaMessage(String message) {
        if (!disableDiscordReliance)
            DiscordHandler.sendToDiscord(CustomChannel.EVENTS, message);
    }

    public static void createNewHotaPrize(Village v, int winStreak) {
        try {
            Item lump;
            int x;
            Item statue = ItemFactory.createItem(ItemList.statueHota, 99f, null);
            byte material = Materials.MATERIAL_GOLD;
            if (winStreak > 30) {
                material = Materials.MATERIAL_ADAMANTINE;
            } else if (winStreak > 20) {
                material = Materials.MATERIAL_GLIMMERSTEEL;
            } else if (winStreak > 10) {
                material = Materials.MATERIAL_DIAMOND;
            } else if (winStreak > 5) {
                material = Materials.MATERIAL_CRYSTAL;
            }
            statue.setMaterial(material);
            float posX = v.getToken().getPosX() - 2f + Server.rand.nextFloat() * 4f;
            float posY = v.getToken().getPosY() - 2f + Server.rand.nextFloat() * 4f;
            statue.setPosXYZRotation(posX, posY, Zones.calculateHeight(posX, posY, true), Server.rand.nextInt(350));
            for (int i = 0; i < winStreak; ++i) {
                if (i / 11 == winStreak % 11) {
                    statue.setAuxData((byte) 0);
                    statue.setData1(1);
                    continue;
                }
                statue.setAuxData((byte) winStreak);
            }
            int r = winStreak * 50 & 255;
            int g = 0;
            int b = 0;
            if (winStreak > 5 && winStreak < 16) {
                r = 0;
            }
            if (winStreak > 5 && winStreak < 20) {
                g = winStreak * 50 & 255;
            }
            if (winStreak > 5 && winStreak < 30) {
                b = winStreak * 50 & 255;
            }
            statue.setColor(WurmColor.createColor(r, g, b));
            statue.getColor();
            Zone z = Zones.getZone(statue.getTileX(), statue.getTileY(), true);
            int numHelpers = 0;
            for (Citizen c : v.citizens.values()) {
                if (Hota.getHelpValue(c.getId()) <= 0) continue;
                ++numHelpers;
            }
            numHelpers = Math.min(5, numHelpers);
            for (x = 0; x < numHelpers; ++x) {
                Item sleepPowder = ItemFactory.createItem(ItemList.sleepPowder, 99f, null);
                statue.insertItem(sleepPowder, true);
            }
            for (x = 0; x < 5; ++x) {
                lump = ItemFactory.createItem(ItemList.adamantineBar, Math.min(99f, 60 + (winStreak * Server.rand.nextFloat() * 1.5f)), null);
                float baseWeight = lump.getWeightGrams();
                float multiplier = 1f;//+(winStreak*0.4f*Server.rand.nextFloat());
                lump.setWeight((int) (baseWeight * multiplier), true);
                statue.insertItem(lump, true);
            }
            for (x = 0; x < 2; ++x) {
                Item riftCrystal = ItemFactory.createItem(ItemList.riftCrystal, 50.0F + (Server.rand.nextFloat() * 40.0F), null);
                statue.insertItem(riftCrystal);
                Item riftStone = ItemFactory.createItem(ItemList.riftStone, 50.0F + (Server.rand.nextFloat() * 40.0F), null);
                statue.insertItem(riftStone);
                Item riftWood = ItemFactory.createItem(ItemList.riftWood, 50.0F + (Server.rand.nextFloat() * 40.0F), null);
                statue.insertItem(riftWood);
            }
            for (x = 0; x < 5; ++x) {
                lump = ItemFactory.createItem(ItemList.glimmerSteelBar, Math.min(99f, 60 + (winStreak * Server.rand.nextFloat() * 1.5f)), null);
                float baseWeight = lump.getWeightGrams();
                float multiplier = 1f;//+(winStreak*0.2f*Server.rand.nextFloat());
                lump.setWeight((int) (baseWeight * multiplier), true);
                statue.insertItem(lump, true);
            }
            // Key fragment
            Item keyFragment = ItemFactory.createItem(CustomItems.keyFragmentId, 99f, null);
            statue.insertItem(keyFragment, true);
            // Add affinity orb
            Item affinityOrb = ItemFactory.createItem(CustomItems.affinityOrbId, 99f, null);
            statue.insertItem(affinityOrb, true);
            // Add 5 medium-quality caches
            int[] cacheIds = {
                    CustomItems.artifactCache.getTemplateId(),
                    CustomItems.crystalCache.getTemplateId(), CustomItems.crystalCache.getTemplateId(),
                    CustomItems.riftCache.getTemplateId(), CustomItems.riftCache.getTemplateId(), CustomItems.riftCache.getTemplateId(),
                    CustomItems.toolCache.getTemplateId(),
                    CustomItems.treasureMapCache.getTemplateId()
            };
            // 5-8 caches
            int i = 5 + Server.rand.nextInt(4);
            while (i > 0) {
                Item cache = ItemFactory.createItem(cacheIds[Server.rand.nextInt(cacheIds.length)], 40f + (50f * Server.rand.nextFloat()), "");
                statue.insertItem(cache, true);
                i--;
            }
            // 10 - 20 kingdom tokens
            //i = 10+Server.rand.nextInt(11);
            //while(i > 0){
            //    Item token = ItemFactory.createItem(22765, 40f+(50f*Server.rand.nextFloat()), "");
            //    statue.insertItem(token, true);
            //    i--;
            //}
            // Add 3-5 seryll lumps of medium ql
            i = 3 + Server.rand.nextInt(3); // 3-5 lumps
            while (i > 0) {
                Item seryll = ItemFactory.createItem(ItemList.seryllBar, 40f + (60f * Server.rand.nextFloat()), null);
                statue.insertItem(seryll, true);
                i--;
            }
            // Add 2-4 silver
            long iron = 20000; // 2 silver
            iron += Server.rand.nextInt(20000); // add up to 2 more silver
            Item[] coins = Economy.getEconomy().getCoinsFor(iron);
            for (Item coin : coins) {
                statue.insertItem(coin, true);
            }
            z.addItem(statue);
        } catch (Exception ex) {
            RequiemLogging.logWarning(ex.getMessage());
        }
    }

    public static Affinity[] getNullAffinities() {
        return new Affinity[0];
    }

    public static void pollFogGoblins() {
        if (Server.getWeather().getFog() > 0.5F) {
            if (fogGoblins.size() < maxFogGoblins) {
                int spawnAttempts = 15;
                for (int i = 0; i < spawnAttempts; i++) {
                    float worldSizeX = Zones.worldTileSizeX;
                    float worldSizeY = Zones.worldTileSizeY;
                    float minX = worldSizeX * 0.25f;
                    float minY = worldSizeY * 0.25f;
                    int tilex = (int) (minX + (minX * 2 * Server.rand.nextFloat()));
                    int tiley = (int) (minY + (minY * 2 * Server.rand.nextFloat()));
                    int tile = Server.surfaceMesh.getTile(tilex, tiley);
                    if (Tiles.decodeHeight(tile) > 0) {
                        Creature fg = spawnCreature(CustomCreatures.fogGoblinId, tilex * 4, tiley * 4, true, (byte) 0);
                        fogGoblins.add(fg);
                    }
                }
                RequiemLogging.logInfo(String.format("Added some fog goblins, there are now: %d", fogGoblins.size()));
            }
        } else {
            if (fogGoblins.size() > 0) {
                Creature fg = fogGoblins.iterator().next();
                fogGoblins.remove(fg);
                fg.destroy();
                RequiemLogging.logInfo(String.format("Removed a fog goblin from the world, there are now: %d", fogGoblins.size()));
            }
        }

    }

    public static void pollRepairingNPCs() {
        // If no repairing NPC's found, search for them
        if (soundEmissionNpcs.size() == 0) {
            for (Creature creature : Creatures.getInstance().getCreatures()) {
                if (creature.getTemplate().getTemplateId() == CustomCreatures.npcDockWorkerId) {
                    soundEmissionNpcs.add(creature);
                    RequiemLogging.logInfo(String.format("Repairing NPC located and remembered, with wurmid: %d", creature.getWurmId()));
                }
            }
        }
        // Loop through known repairing NPC's and play sound & animation
        for (Creature creature : soundEmissionNpcs) {
            if (creature.getTemplate().getTemplateId() == CustomCreatures.npcDockWorkerId) {
                creature.playAnimation("buildwoodenwall", false);
                SoundPlayer.playSound(EffectsTools.randomRepairSoundWood(), creature, 0f);
            }
        }

    }

    public static void pollMobSpawners() {
        // If no mob spawners found, search for them
        if (mobSpawners.size() == 0) {
            for (Item item : Items.getAllItems()) {
                for (SpawnerTemplate template : spawnerTemplates) {
                    if (item.getTemplateId() == template.templateID) {
                        mobSpawners.add(item);
                        RequiemLogging.logInfo(String.format("Mob Spawner located and remembered, with name: %s, and wurmid: %d", item.getName(), item.getWurmId()));
                    }
                }
            }
        }

        // Loop through known Mob Spawners and spawn dungeon mobs
        for (Item mobSpawner : mobSpawners) {
            for (SpawnerTemplate template : spawnerTemplates) {
                if (mobSpawner.getTemplateId() == template.templateID) {

                    int tileX = mobSpawner.getTileX();
                    int tileY = mobSpawner.getTileY();
                    int mobCount = 0;

                    for (int i = tileX - 3; i < tileX + 3; i++) {
                        for (int j = tileY - 3; j < tileY + 3; j++) {
                            try {
                                mobCount = mobCount + Zones.getOrCreateTile(i, j, mobSpawner.isOnSurface()).getCreatures().length;
                            } catch (Exception e) {
                                RequiemLogging.logException("[Error] in pollMobSpawners in Misc", e);
                            }
                        }
                    }
                    if (mobCount == 0) {
                        spawnCreature(template.mobType, mobSpawner, (byte) 1);
                    } else if (mobCount < template.maxNum) {
                        spawnCreature(template.mobType, mobSpawner, (byte) 0);
                    }
                }
            }
        }
    }


    public static void pollLootCarpets() {
        // If no loot carpets found, search for them
        if (lootCarpets.size() == 0) {
            for (Item item : Items.getAllItems()) {
                if (item.getTemplateId() == lootFlagID || item.getTemplateId() == smallLootFlagID) {
                    if (item.getTemplateId() != 0 && !item.getName().equals("inventory")) {
                        lootCarpets.add(item);
                        RequiemLogging.logInfo(String.format("Loot Carpet located and remembered, with name: %s, and wurmid: %d", item.getName(), item.getWurmId()));
                    }
                }
            }
        }

        // Loop through known loot carpets and spawn loot boxes
        for (Item lootCarpet : lootCarpets) {

            int tileX = lootCarpet.getTileX();
            int tileY = lootCarpet.getTileY();
            int boxID = (lootCarpet.getTemplateId() == smallLootFlagID) ? smallLootBoxID : lootBoxID;

            int boxCount = 0;

            for (int i = tileX - 1; i < tileX + 1; i++) {
                for (int j = tileY - 1; j < tileY + 1; j++) {
                    VolaTile t = Zones.getTileOrNull(i, j, lootCarpet.isOnSurface());
                    if ((t != null)) {
                        for (Item possibleBox : t.getItems()) {
                            if (possibleBox.getTemplateId() == boxID) {
                                boxCount++;
                            }
                        }
                    }
                }
            }
            if (boxCount < 2) {
                spawnLootBox(lootCarpet, boxID);
            }
        }

    }

    public static void pollResourcePoints() {
        // If no resource points found, search for them
        if (resourcePoints.size() == 0) {
            for (Item item : Items.getAllItems()) {
                for (StructureTemplate template : structureTemplates) {
                    if (item.getTemplateId() == template.templateID) {
                        if (item.getTemplateId() != 0 && !item.getName().equals("inventory")) {
                            resourcePoints.add(item);
                            RequiemLogging.logInfo(String.format("Resource Point located and remembered, with name: %s, and wurmid: %d", item.getName(), item.getWurmId()));
                        }
                    }
                }
            }
        }
        // Loop through known resource points and spawn items
        for (Item resourcePoint : resourcePoints) {
            for (StructureTemplate template : structureTemplates) {
                if (resourcePoint.getTemplateId() == template.templateID) {
                    spawnItemSpawn(resourcePoint, template.templateProduce, template.templateConsume, template.templateSecondaryConsume, template.maxNum, template.maxitems);
                    SoundPlayer.playSound(template.sound, resourcePoint, 0);
                }
            }
        }

    }

    public static void pollTradeTents() {

        // If no Trade Tents found, search for them
        if (tradeTents.size() == 0) {
            for (Item item : Items.getAllItems()) {
                if (item.getTemplateId() == tradeTentID) {
                    if (item.getTemplateId() != 0) {
                        tradeTents.add(item);
                        RequiemLogging.logInfo(String.format("Trade Tents located and remembered, with name: %s, and wurmid: %d", item.getName(), item.getWurmId()));
                    }
                }
            }
        }

        // Loop through known Trade Tents and spawn crates

        for (Item tradeTent : tradeTents) {

            int tileX = tradeTent.getTileX();
            int tileY = tradeTent.getTileY();
            int crateCount = 0;

            for (int i = tileX - 5; i < tileX + 5; i++) {
                for (int j = tileY - 5; j < tileY + 5; j++) {
                    VolaTile tile = Zones.getTileOrNull(i, j, tradeTent.isOnSurface());

                    if ((tile != null)) {
                        for (Item possibleCrate : tile.getItems()) {
                            if (possibleCrate.getTemplateId() == tradeGoodsID) {
                                crateCount++;
                            }
                        }
                    }
                }
            }

            if (crateCount < 20) {
                for (FocusZone fz : FocusZone.getZonesAt(tileX, tileY)) {
                    if (fz.getName().equals(tradeTentsSouthZoneName)) {
                        spawnTradeCrate(tradeTent, 801L);
                    } else if (fz.getName().equals(tradeTentsNorthZoneName)) {
                        spawnTradeCrate(tradeTent, 802L);
                    }
                }
            }
        }
    }

    private static void spawnItemSpawn(Item item, int templateProduce, int templateConsume, int templateSecondaryConsume, int maxNums, int maxItems) {
        Byte material = null;
        if (templateProduce == ItemList.chainBoot) {
            templateProduce = ItemList.chainBoot + Server.rand.nextInt(6);
            material = Materials.MATERIAL_IRON;
        } else if (templateProduce == ItemList.plateBoot) {
            templateProduce = ItemList.plateBoot + Server.rand.nextInt(8);
            material = Materials.MATERIAL_STEEL;
        } else if (templateProduce == ItemList.steelBar) {
            int[] weaponIDs = {ItemList.shieldSmallMetal, ItemList.shieldMedium, ItemList.maulLarge, ItemList.maulSmall, ItemList.axeMedium, ItemList.axeSmall, ItemList.swordShort, ItemList.swordTwoHander, ItemList.swordLong, ItemList.halberd};
            templateProduce = weaponIDs[Server.rand.nextInt(weaponIDs.length)];
            material = Materials.MATERIAL_STEEL;
        } else if (templateProduce == ItemList.log) {
            material = Materials.MATERIAL_WOOD_BIRCH;
        } else if (templateProduce == ItemList.stoneBrick) {
            material = Materials.MATERIAL_STONE;
        } else if (templateProduce == ItemList.ball) {
            material = Materials.MATERIAL_IRON;
        }

        Item[] currentItems = item.getAllItems(true);
        int produceTally = 0;
        int consumeTally = 0;
        int secondaryConsumeTally = 0;

        float[] consumeQLs = new float[maxNums];
        float[] secondaryConsumeQLs = new float[maxNums];

        for (Item i : currentItems) {
            if (templateProduce == i.getTemplateId() || templateProduce == 50) {
                produceTally++;
            } else if (templateConsume == i.getTemplateId()) {
                if (consumeTally < consumeQLs.length) {
                    consumeQLs[consumeTally] = i.getQualityLevel();
                }
                consumeTally++;
            } else if (templateSecondaryConsume == i.getTemplateId()) {
                if (secondaryConsumeTally < secondaryConsumeQLs.length) {
                    secondaryConsumeQLs[secondaryConsumeTally] = i.getQualityLevel();
                }
                secondaryConsumeTally++;
            }
        }
        if (templateConsume != 0) {
            maxNums = Math.min(maxNums, consumeTally);
        }
        if (templateSecondaryConsume != 0) {
            maxNums = Math.min(maxNums, secondaryConsumeTally);
        }
        if (produceTally + maxNums > maxItems) {
            return;
        }
        if (templateConsume != 0) {
            consumeTally = Math.min(maxNums, consumeTally);
            if (templateSecondaryConsume != 0) {
                secondaryConsumeTally = Math.min(maxNums, secondaryConsumeTally);
            }
            for (Item i : currentItems) {
                if (consumeTally > 0 && i.getTemplateId() == templateConsume) {
                    Items.destroyItem(i.getWurmId());
                    consumeTally--;
                }
            }
            for (Item i : currentItems) {
                if (secondaryConsumeTally > 0 && i.getTemplateId() == templateSecondaryConsume) {
                    Items.destroyItem(i.getWurmId());
                    secondaryConsumeTally--;
                }
            }
        }

        for (int nums = 0; nums < maxNums; nums++) {
            try {
                byte rrarity = 0;
                float newql = (float) 50.0 + Server.rand.nextFloat() * (float) 30.0;
                if (templateConsume != 0) {
                    newql = Math.min(newql, consumeQLs[nums]);
                }

                if (templateSecondaryConsume != 0) {
                    newql = Math.min(newql, secondaryConsumeQLs[nums]);
                }

                Item toInsert;

                if (material == null) {
                    toInsert = ItemFactory.createItem(templateProduce, newql, rrarity, "");
                } else {
                    toInsert = ItemFactory.createItem(templateProduce, newql, material, rrarity, "");
                }

                item.insertItem(toInsert, true);

            } catch (FailedException | NoSuchTemplateException e) {
                RequiemLogging.logException("[Error] in spawnItemSpawn in Misc", e);
            }

        }
    }

    private static Creature spawnCreature(int templateId, Item item, byte ctype) {
        return spawnCreature(templateId, (int) item.getPosX(), (int) item.getPosY(), item.isOnSurface(), ctype);
    }

    private static Creature spawnCreature(int templateId, int x, int y, boolean isOnSurface, byte ctype) {
        try {
            float xMod = (3.0F * Server.rand.nextFloat()) - 1;
            float yMod = (3.0F * Server.rand.nextFloat()) - 1;

            CreatureTemplate ct = CreatureTemplateFactory.getInstance().getTemplate(templateId);
            byte sex = 0;
            if (Server.rand.nextInt(2) == 0) {
                sex = 1;
            }
            return Creature.doNew(templateId, true, x + xMod, y + yMod, Server.rand
                    .nextFloat() * 360.0F, isOnSurface ? 0 : -1, ct.getName(), sex, (byte) 0, ctype, false);
        } catch (Exception nst) {
            RequiemLogging.logWarning(nst.getMessage() + nst);
        }
        return null;
    }

    private static void spawnTradeCrate(Item item, long id) {
        try {
            Item crate = ItemFactory.createItem(tradeGoodsID, 50.0F + Server.rand.nextFloat() * 50.0F,
                    item.getPosX() - 5.0F + (Server.rand.nextFloat() * 10.0F), item.getPosY() - 5.0F + (Server.rand.nextFloat() * 10.0F), 65.0F,
                    item.isOnSurface(), (byte) 0, MiscConstants.NOID, "");
            crate.setData(id);
        } catch (Exception e) {
            RequiemLogging.logException("[Error] in spawnTradeCrate in Misc", e);
        }
    }

    private static void spawnLootBox(Item item, int id) {
        try {
            Item box = ItemFactory.createItem(id, 50.0F + Server.rand.nextFloat() * 50.0F,
                    item.getPosX() - 3.0F + (Server.rand.nextFloat() * 3.0F), item.getPosY() - 1.0F + Server.rand.nextFloat(), 65.0F,
                    item.isOnSurface(), (byte) 0, MiscConstants.NOID, "");
            if (box.isOnSurface()) {
                Items.destroyItem(box.getWurmId());
            }
        } catch (Exception e) {
            RequiemLogging.logException("[Error] in spawnLootBox in Misc", e);
        }
    }

    public static Item createItem(int templateId, float qualityLevel) throws Exception {
        return ItemFactory.createItem(templateId, qualityLevel, (byte) 0, MiscConstants.COMMON, null);
    }

    public static void appendToFile(final Exception e) {
        try {
            final FileWriter fstream = new FileWriter("RequiemException.txt", true);
            final BufferedWriter out = new BufferedWriter(fstream);
            final PrintWriter pWriter = new PrintWriter(out, true);
            e.printStackTrace(pWriter);
            pWriter.close();
        } catch (Exception ie) {
            throw new RuntimeException("Could not write Exception to file", ie);
        }
    }


    public static void noobTips() {
        Player[] allPlayers = Players.getInstance().getPlayers();
        for (Player player : allPlayers) {
            if (player.hasLink()) {
                boolean timeLimit = player.getPlayingTime() <= TimeConstants.DAY_MILLIS;
                if (timeLimit) {
                    if (player.getPlayingTime() == TimeConstants.FIFTEEN_MINUTES_MILLIS) {
                        player.getCommunicator().sendAlertServerMessage(noobMessage, (byte) 1);
                    } else if (player.getPlayingTime() == TimeConstants.HALF_HOUR_MILLIS) {
                        player.getCommunicator().sendAlertServerMessage(noobMessage, (byte) 1);
                    } else if (player.getPlayingTime() == TimeConstants.HALF_HOUR_MILLIS + TimeConstants.FIFTEEN_MINUTES_MILLIS) {
                        player.getCommunicator().sendAlertServerMessage(noobMessage, (byte) 1);
                    } else if (player.getPlayingTime() == TimeConstants.HOUR_MILLIS) {
                        player.getCommunicator().sendAlertServerMessage(noobMessage, (byte) 1);
                    } else if (player.getPlayingTime() == TimeConstants.HOUR_MILLIS * 2) {
                        player.getCommunicator().sendAlertServerMessage(noobMessage, (byte) 1);
                    } else if (player.getPlayingTime() == TimeConstants.HOUR_MILLIS * 3) {
                        player.getCommunicator().sendAlertServerMessage(noobMessage, (byte) 1);
                    } else if (player.getPlayingTime() == TimeConstants.HOUR_MILLIS * 6) {
                        player.getCommunicator().sendAlertServerMessage(noobMessage, (byte) 1);
                    } else if (player.getPlayingTime() == TimeConstants.HOUR_MILLIS * 12) {
                        player.getCommunicator().sendAlertServerMessage(noobMessage, (byte) 1);
                    } else if (player.getPlayingTime() == TimeConstants.HOURS18_MILLIS) {
                        player.getCommunicator().sendAlertServerMessage(noobMessage, (byte) 1);
                    }
                }
            }
        }
    }

}
