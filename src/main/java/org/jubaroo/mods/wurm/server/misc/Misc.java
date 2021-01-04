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
import com.wurmonline.server.questions.VillageFoundationQuestion;
import com.wurmonline.server.skills.Affinity;
import com.wurmonline.server.sounds.SoundPlayer;
import com.wurmonline.server.villages.Citizen;
import com.wurmonline.server.villages.Village;
import com.wurmonline.server.zones.FocusZone;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zone;
import com.wurmonline.server.zones.Zones;
import javassist.*;
import javassist.bytecode.Descriptor;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;
import mod.sin.lib.Util;
import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.classhooks.InvocationHandlerFactory;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.actions.special.decorative.DecorativeKingdoms;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.communication.discord.DiscordHandler;
import org.jubaroo.mods.wurm.server.creatures.CustomCreatures;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.items.behaviours.SupplyDepotBehaviour;
import org.jubaroo.mods.wurm.server.misc.templates.SpawnerTemplate;
import org.jubaroo.mods.wurm.server.misc.templates.StructureTemplate;
import org.jubaroo.mods.wurm.server.server.Constants;
import org.jubaroo.mods.wurm.server.tools.EffectsTools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Calendar;

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
        switch (player.getName()) {
            case "Vyse":
                return true;
            case "Oluf":
                return true;
            case "Akuzic":
                return true;
            default:
                return player.getName().equals("Callanish");
        }
    }

    public static boolean isWeaponOrArmor(Item target) {
        return target.isWeapon() || target.isArmour() || target.isShield() || target.isWeaponBow() || target.isBowUnstringed();
    }

    public static boolean isBoss(final Creature creature) {
        String[] bossids;
        for (int length = (bossids = Constants.bossids).length, i = 0; i < length; ++i) {
            final String boss = bossids[i];
            if (creature.getTemplate().getTemplateId() == Integer.parseInt(boss)) {
                return true;
            }
        }
        return false;
    }

    public static void sendHotaMessage(String message) {
        if (!Constants.disableDiscordReliance)
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
            if (Constants.fogGoblins.size() < Constants.maxFogGoblins) {
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
                        Constants.fogGoblins.add(fg);
                    }
                }
                RequiemLogging.debug(String.format("Added some fog goblins, there are now: %d", Constants.fogGoblins.size()));
            }
        } else {
            if (Constants.fogGoblins.size() > 0) {
                Creature fg = Constants.fogGoblins.iterator().next();
                Constants.fogGoblins.remove(fg);
                fg.destroy();
                RequiemLogging.debug(String.format("Removed a fog goblin from the world, there are now: %d", Constants.fogGoblins.size()));
            }
        }
    }

    public static void pollRepairingNPCs() {
        // If no repairing NPC's found, search for them
        if (Constants.soundEmissionNpcs.size() == 0) {
            for (Creature creature : Creatures.getInstance().getCreatures()) {
                if (creature.getName().equals("Dock Worker") || creature.getName().equals("Dock worker")) {
                    Constants.soundEmissionNpcs.add(creature);
                    RequiemLogging.debug(String.format("Repairing NPC located and remembered, with wurmid: %d", creature.getWurmId()));
                }
            }
        }
        // Loop through known repairing NPC's and play sound & animation
        for (Creature creature : Constants.soundEmissionNpcs) {
            if (creature.getName().equals("Dock Worker") || creature.getName().equals("Dock worker")) {
                creature.playAnimation("buildwoodenwall", false);
                SoundPlayer.playSound(EffectsTools.randomRepairSoundWood(Server.rand.nextInt(1)), creature, 0f);
            }
        }

    }

    public static void pollMobSpawners() {
        // If no mob spawners found, search for them
        if (Constants.mobSpawners.size() == 0) {
            for (Item item : Items.getAllItems()) {
                for (SpawnerTemplate template : Constants.spawnerTemplates) {
                    if (item.getTemplateId() == template.templateID) {
                        Constants.mobSpawners.add(item);
                        RequiemLogging.debug(String.format("Mob Spawner located and remembered, with name: %s, and wurmid: %d", item.getName(), item.getWurmId()));
                    }
                }
            }
        }

        // Loop through known Mob Spawners and spawn dungeon mobs
        for (Item mobSpawner : Constants.mobSpawners) {
            for (SpawnerTemplate template : Constants.spawnerTemplates) {
                if (mobSpawner.getTemplateId() == template.templateID) {

                    int tileX = mobSpawner.getTileX();
                    int tileY = mobSpawner.getTileY();
                    int mobCount = 0;

                    for (int i = tileX - 3; i < tileX + 3; i++) {
                        for (int j = tileY - 3; j < tileY + 3; j++) {
                            try {
                                mobCount = mobCount + Zones.getOrCreateTile(i, j, mobSpawner.isOnSurface()).getCreatures().length;
                            } catch (Exception e) {
                                e.printStackTrace();
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
        if (Constants.lootCarpets.size() == 0) {
            for (Item item : Items.getAllItems()) {
                if (item.getTemplateId() == Constants.lootFlagID || item.getTemplateId() == Constants.smallLootFlagID) {
                    if (item.getTemplateId() != 0 && !item.getName().equals("inventory")) {
                        Constants.lootCarpets.add(item);
                        RequiemLogging.debug(String.format("Loot Carpet located and remembered, with name: %s, and wurmid: %d", item.getName(), item.getWurmId()));
                    }
                }
            }
        }

        // Loop through known loot carpets and spawn loot boxes
        for (Item lootCarpet : Constants.lootCarpets) {

            int tileX = lootCarpet.getTileX();
            int tileY = lootCarpet.getTileY();
            int boxID = (lootCarpet.getTemplateId() == Constants.smallLootFlagID) ? Constants.smallLootBoxID : Constants.lootBoxID;

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
        if (Constants.resourcePoints.size() == 0) {
            for (Item item : Items.getAllItems()) {
                for (StructureTemplate template : Constants.structureTemplates) {
                    if (item.getTemplateId() == template.templateID) {
                        if (item.getTemplateId() != 0 && !item.getName().equals("inventory")) {
                            Constants.resourcePoints.add(item);
                            RequiemLogging.debug(String.format("Resource Point located and remembered, with name: %s, and wurmid: %d", item.getName(), item.getWurmId()));
                        }
                    }
                }
            }
        }
        // Loop through known resource points and spawn items
        for (Item resourcePoint : Constants.resourcePoints) {
            for (StructureTemplate template : Constants.structureTemplates) {
                if (resourcePoint.getTemplateId() == template.templateID) {
                    spawnItemSpawn(resourcePoint, template.templateProduce, template.templateConsume, template.templateSecondaryConsume, template.maxNum, template.maxitems);
                    SoundPlayer.playSound(template.sound, resourcePoint, 0);
                }
            }
        }

    }

    public static void pollTradeTents() {

        // If no Trade Tents found, search for them
        if (Constants.tradeTents.size() == 0) {
            for (Item item : Items.getAllItems()) {
                if (item.getTemplateId() == Constants.tradeTentID) {
                    if (item.getTemplateId() != 0) {
                        Constants.tradeTents.add(item);
                        RequiemLogging.debug(String.format("Trade Tents located and remembered, with name: %s, and wurmid: %d", item.getName(), item.getWurmId()));
                    }
                }
            }
        }

        // Loop through known Trade Tents and spawn crates

        for (Item tradeTent : Constants.tradeTents) {

            int tileX = tradeTent.getTileX();
            int tileY = tradeTent.getTileY();
            int crateCount = 0;

            for (int i = tileX - 5; i < tileX + 5; i++) {
                for (int j = tileY - 5; j < tileY + 5; j++) {
                    VolaTile tile = Zones.getTileOrNull(i, j, tradeTent.isOnSurface());

                    if ((tile != null)) {
                        for (Item possibleCrate : tile.getItems()) {
                            if (possibleCrate.getTemplateId() == Constants.tradeGoodsID) {
                                crateCount++;
                            }
                        }
                    }
                }
            }

            if (crateCount < 20) {
                for (FocusZone fz : FocusZone.getZonesAt(tileX, tileY)) {
                    if (fz.getName().equals(Constants.tradeTentsSouthZoneName)) {
                        spawnTradeCrate(tradeTent, 801L);
                    } else if (fz.getName().equals(Constants.tradeTentsNorthZoneName)) {
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
                e.printStackTrace();
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
            Item crate = ItemFactory.createItem(Constants.tradeGoodsID, 50.0F + Server.rand.nextFloat() * 50.0F,
                    item.getPosX() - 5.0F + (Server.rand.nextFloat() * 10.0F), item.getPosY() - 5.0F + (Server.rand.nextFloat() * 10.0F), 65.0F,
                    item.isOnSurface(), (byte) 0, MiscConstants.NOID, "");
            crate.setData(id);
        } catch (Exception ex) {
            ex.printStackTrace();
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
        } catch (Exception ex) {
            ex.printStackTrace();
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

    public static void init() {
        RequiemLogging.logInfo("========= Initializing Misc.init =========");
        try {
            final ClassPool classPool = HookManager.getInstance().getClassPool();
            CtClass ctItem = classPool.get("com.wurmonline.server.items.Item");
            ctItem.getMethod("getName", "(Z)Ljava/lang/String;").instrument(new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("getNameFor"))
                        m.replace("$_ = " + DecorativeKingdoms.class.getName() + ".getName($1);");
                }
            });

            ctItem.getMethod("getModelName", "()Ljava/lang/String;").instrument(new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("getSuffixFor"))
                        m.replace("$_ = " + DecorativeKingdoms.class.getName() + ".getModelSuffix($1);");
                }
            });

            final CtClass ctCommunicator = classPool.get("com.wurmonline.server.creatures.Communicator");
            ctCommunicator.getMethod("sendAllKingdoms", "()V").setBody(MiscHooks.class.getName() + ".sendKingdoms(this);");

            ctItem.getMethod("sendWear", "(Lcom/wurmonline/server/items/Item;B)V")
                    .insertAfter("if (!item.isBodyPartAttached()) " + MiscHooks.class.getName() + ".sendWearHook(item, this.getOwnerOrNull());");

            ctItem.getMethod("removeItem", "(JZZZ)Lcom/wurmonline/server/items/Item;")
                    .instrument(new ExprEditor() {
                        @Override
                        public void edit(MethodCall m) throws CannotCompileException {
                            if (m.getMethodName().equals("hasItemBonus"))
                                m.replace(MiscHooks.class.getName() + ".sendWearHook(item, owner); $_ = $proceed($$);");
                        }
                    });

            CtMethod mAddCreature = classPool.getCtClass("com.wurmonline.server.zones.VirtualZone").getMethod("addCreature", "(JZJFFF)Z");
            mAddCreature.instrument(new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("sendNewCreature"))
                        m.replace("$proceed($1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11,$12,$13," + MiscHooks.class.getName() + ".creatureKingdom(creature,$14),$15,$16,$17,$18,$19);");
                }
            });

            final CtClass ctServer = classPool.get("com.wurmonline.server.Server");
            ctServer.getMethod("run", "()V").insertAfter(MiscHooks.class.getName() + ".serverTick(this);");

            classPool.getCtClass("com.wurmonline.server.items.ChickenCoops")
                    .getMethod("eggPoller", "(Lcom/wurmonline/server/items/Item;)V")
                    .instrument(new ExprEditor() {
                        @Override
                        public void edit(MethodCall m) throws CannotCompileException {
                            if (m.getMethodName().equals("getQualityLevel"))
                                m.replace("$_=99f;");
                        }
                    });
        } catch (NotFoundException | CannotCompileException e) {
            RequiemLogging.logWarning(e.getMessage());
            throw new HookException(e);
        }

        HookManager.getInstance().registerHook("com.wurmonline.server.creatures.Creature", "getMountSpeedPercent", "(Z)F", new InvocationHandlerFactory() {
            @Override
            public InvocationHandler createInvocationHandler() {
                return new InvocationHandler() {
                    @Override
                    public Object invoke(Object object, Method method, Object[] args) throws Throwable {
                        Creature creature = (Creature) object;
                        float speed = (float) method.invoke(object, args);
                        //Requiem.debug("old speed: " + speed);
                        if (!creature.isRidden()) {
                            try {
                                if (creature.getBonusForSpellEffect((byte) 22) > 0.0F) {
                                    speed -= 0.2F * (creature.getBonusForSpellEffect((byte) 22) / 100.0F);
                                    //Requiem.debug("decrease because oakshell: " + (0.2F * (creature.getBonusForSpellEffect((byte) 22) / 100.0F)));
                                }
                                Item barding = creature.getArmour((byte) 2);
                                if (barding != null) {
                                    switch (barding.getMaterial()) {
                                        case Materials.MATERIAL_LEATHER:
                                            speed -= 0.1F;
                                            //Requiem.debug("decrease because leather: 0.1");
                                            break;
                                        default:
                                            speed -= 0.2F;
                                            //Requiem.debug("decrease because barding: 0.2");
                                    }
                                }
                            } catch (Exception ex) {
                            }
                        }
                        //Requiem.debug("new speed: " + speed);
                        return speed;
                    }
                };
            }
        });

        HookManager.getInstance().registerHook("com.wurmonline.server.spells.Dominate", "mayDominate", "(Lcom/wurmonline/server/creatures/Creature;Lcom/wurmonline/server/creatures/Creature;)Z", new InvocationHandlerFactory() {

            @Override
            public InvocationHandler createInvocationHandler() {
                return new InvocationHandler() {

                    @Override
                    public Object invoke(Object object, Method method, Object[] args) throws Throwable {

                        Creature performer = (Creature) args[0];
                        Creature target = (Creature) args[1];

                        if (target != null && target.isUnique()) {

                            performer.getCommunicator().sendNormalServerMessage("You cannot dominate " + target
                                    .getName() + ", because of its immense power.", (byte) 3);
                            return false;

                        }

                        return method.invoke(object, args);

                    }
                };
            }
        });

        HookManager.getInstance().registerHook("com.wurmonline.server.weather.Weather", "getFog", "()F", new InvocationHandlerFactory() {

            @Override
            public InvocationHandler createInvocationHandler() {
                return new InvocationHandler() {

                    @Override
                    public Object invoke(Object object, Method method, Object[] args) throws Throwable, Exception {

                        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                        if (stackTrace[4].getClassName().equals("com.wurmonline.server.zones.Zone") && stackTrace[4].getMethodName().equals("poll")) {
                            // If zone poller asks about fog, pretend there is none so fog spiders do not spawn
                            return 0.0F;
                        }

                        return method.invoke(object, args);

                    }
                };
            }
        });

        HookManager.getInstance().registerHook("com.wurmonline.server.questions.VillageFoundationQuestion", "checkTile", "()Z", new InvocationHandlerFactory() {

            @Override
            public InvocationHandler createInvocationHandler() {
                return new InvocationHandler() {

                    @Override
                    public Object invoke(Object object, Method method, Object[] args) throws Throwable {

                        VillageFoundationQuestion vfq = (VillageFoundationQuestion) object;

                        VolaTile tile = Zones.getOrCreateTile(vfq.tokenx, vfq.tokeny, vfq.surfaced);
                        if (tile != null) {
                            int tx = tile.tilex;
                            int ty = tile.tiley;
                            int tt = Server.surfaceMesh.getTile(tx, ty);
                            if ((Tiles.decodeType(tt) == Tiles.Tile.TILE_LAVA.id) || (Tiles.isMineDoor(Tiles.decodeType(tt)))) {
                                vfq.getResponder().getCommunicator().sendSafeServerMessage("You cannot found a settlement here.");
                                return false;
                            }

                            for (int i = (tx - vfq.selectedWest); i <= (tx + vfq.selectedEast); i++) {
                                for (int j = (ty - vfq.selectedNorth); j <= (ty + vfq.selectedSouth); j++) {
                                    //RequiemLogging.debug("checking tile: " + i + ", " + j);
                                    if (FocusZone.isNoBuildZoneAt(i, j)) {
                                        FocusZone fz = FocusZone.getZonesAt(i, j).iterator().next();
                                        vfq.getResponder().getCommunicator().sendSafeServerMessage("You cannot found a settlement here. It is too close to the " + fz.getName());
                                        return false;
                                    }

                                }
                            }

                            for (int x = -1; x <= 1; x++) {
                                for (int y = -1; y <= 1; y++) {
                                    int t = Server.surfaceMesh.getTile(tx + x, ty + y);
                                    if (Tiles.decodeHeight(t) < 0) {
                                        vfq.getResponder().getCommunicator().sendSafeServerMessage("You cannot found a settlement here. Too close to water.");
                                        return false;
                                    }
                                }
                            }
                        }
                        return true;
                    }
                };
            }
        });
    }

    /**
     * Enables Courier enchants of newly created mailboxes.
     */
    public static void EnableEnchant() {
        try {
            // Places a 30 power courier enchantment on newly created mailboxes.
            CtClass ctClass = HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.ItemFactory");
            CtClass[] parameters = new CtClass[]{
                    CtPrimitiveType.intType,
                    CtPrimitiveType.floatType,
                    CtPrimitiveType.byteType,
                    CtPrimitiveType.byteType,
                    CtPrimitiveType.longType,
                    HookManager.getInstance().getClassPool().get("java.lang.String")
            };
            CtMethod ctMethod = ctClass.getMethod("createItem", Descriptor.ofMethod(HookManager.getInstance().getClassPool().get("com.wurmonline.server.items.Item"), parameters));
            ctMethod.instrument(new ExprEditor() {
                @Override
                public void edit(NewExpr newExpr) throws CannotCompileException {
                    if (newExpr.getClassName().equals("com.wurmonline.server.items.DbItem")) {
                        ctMethod.insertAt(newExpr.getLineNumber() + 1,
                                "{ if (templateId >= 510 && templateId <= 513) {" +
                                        "com.wurmonline.server.items.ItemSpellEffects effs;" +
                                        "if ((effs = toReturn.getSpellEffects()) == null) effs = new com.wurmonline.server.items.ItemSpellEffects(toReturn.getWurmId());" +
                                        "toReturn.getSpellEffects().addSpellEffect(new com.wurmonline.server.spells.SpellEffect(toReturn.getWurmId(), (byte)20, " + Constants.mailboxEnchantPower + "f, 20000000));" +
                                        "toReturn.permissions.setPermissionBit(com.wurmonline.server.players.Permissions.Allow.HAS_COURIER.getBit(), true);" +
                                        "} }");
                    }
                }
            });
        } catch (NotFoundException | CannotCompileException ex) {
            RequiemLogging.logException("Error in EnableEnchant - ", ex);
        }
    }

    public static void preInit() {
        try {
            ClassPool classPool = HookManager.getInstance().getClassPool();
            Class<Misc> thisClass = Misc.class;
            String replace;

            CtClass ctCreature = classPool.get("com.wurmonline.server.creatures.Creature");
            CtClass ctItem = classPool.get("com.wurmonline.server.items.Item");
            CtClass ctTerraforming = classPool.get("com.wurmonline.server.behaviours.Terraforming");
            CtClass ctVillage = classPool.get("com.wurmonline.server.villages.Village");

            Util.setReason("Change HotA reward");
            replace = String.format("%s.createNewHotaPrize(this, $1);", Misc.class.getName());
            Util.setBodyDeclared(thisClass, ctVillage, "createHotaPrize", replace);

            CtClass ctHota = classPool.get("com.wurmonline.server.epic.Hota");

            Util.setReason("Display discord message for HotA announcements.");
            replace = String.format("%s.sendHotaMessage($1);$_ = $proceed($$);", Misc.class.getName());
            Util.instrumentDeclared(thisClass, ctHota, "poll", "broadCastSafe", replace);

            Util.setReason("Display discord message for HotA wins.");
            Util.instrumentDeclared(thisClass, ctHota, "win", "broadCastSafe", replace);

            Util.setReason("Display discord message for HotA conquers & neutralizes.");
            replace = String.format("if($2.getData1() == 0){  %s.sendHotaMessage($1.getName() + \" neutralizes the \" + $2.getName() + \".\");}else{  %s.sendHotaMessage($1.getName() + \" conquers the \" + $2.getName() + \".\");}", Misc.class.getName(), Misc.class.getName());
            Util.insertBeforeDeclared(thisClass, ctHota, "addPillarConquered", replace);

            CtClass ctMeshIO = classPool.get("com.wurmonline.mesh.MeshIO");
            CtClass[] params7 = new CtClass[]{
                    ctCreature,
                    ctItem,
                    CtClass.intType,
                    CtClass.intType,
                    CtClass.intType,
                    CtClass.floatType,
                    CtClass.booleanType,
                    ctMeshIO,
                    CtClass.booleanType
            };
            String desc7 = Descriptor.ofMethod(CtClass.booleanType, params7);
            Util.setReason("Announce digging up artifacts on Discord.");
            replace = String.format("%s.sendHotaMessage($1+\" \"+$2);$_ = $proceed($$);", Misc.class.getName());
            Util.instrumentDescribed(thisClass, ctTerraforming, "dig", desc7, "addHistory", replace);

            // Add light effects for the supply depots, since they are unique
            CtClass ctPlayers = classPool.get("com.wurmonline.server.Players");
            ctPlayers.getDeclaredMethod("sendAltarsToPlayer").insertBefore(String.format("%s.sendDepotEffectsToPlayer($1);", SupplyDepotBehaviour.class.getName()));

            /*
            if (Holidays.isRequiemChristmas()) {
                // - Add creatures to the list of spawnable uniques - //
                CtClass ctDens = classPool.get("com.wurmonline.server.zones.Dens");
                ctDens.getDeclaredMethod("checkDens").insertAt(0, "com.wurmonline.server.zones.Dens.checkTemplate(" + CustomCreatures.frostyId + ", whileRunning)" +
                        " && com.wurmonline.server.zones.Dens.checkTemplate(" + CustomCreatures.grinchId + ", whileRunning)" +
                        " && com.wurmonline.server.zones.Dens.checkTemplate(" + CustomCreatures.rudolphId + ", whileRunning);");
            }
             */
        } catch (NotFoundException | CannotCompileException e) {
            throw new HookException(e);
        }
    }
}
