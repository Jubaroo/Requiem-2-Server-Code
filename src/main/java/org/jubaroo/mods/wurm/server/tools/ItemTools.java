package org.jubaroo.mods.wurm.server.tools;

import com.wurmonline.server.FailedException;
import com.wurmonline.server.Server;
import com.wurmonline.server.items.*;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.spells.SpellEffect;
import com.wurmonline.shared.constants.Enchants;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.server.constants.ItemConstants;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemTools {
    public static Item lumps;

    public static String getRarityString(byte rarity) {
        switch (rarity) {
            case 0:
                return "common";
            case 1:
                return "rare";
            case 2:
                return "supreme";
            case 3:
                return "fantastic";
            default:
                return "";
        }
    }

    public static byte randomRarity() {
        return (byte) Server.rand.nextInt(3);
    }

    public static byte makeRarity(int chance, boolean alwaysRare) {
        byte rarity = 0;
        if (alwaysRare || Server.rand.nextInt(chance) == 0) {
            rarity = 1;
            if (Server.rand.nextInt(chance) == 0) {
                rarity = 2;
                if (Server.rand.nextInt(chance) == 0) {
                    rarity = 3;
                }
            }
        }
        return rarity;
    }

    private static void removeRecipes(int result) throws NoSuchFieldException, IllegalAccessException {
        Map<Integer, List<CreationEntry>> matrix = ReflectionUtil.getPrivateField(null, ReflectionUtil.getField(CreationMatrix.class, "matrix"));
        Map<Integer, List<CreationEntry>> advancedEntries = ReflectionUtil.getPrivateField(null, ReflectionUtil.getField(CreationMatrix.class, "advancedEntries"));
        Map<Integer, List<CreationEntry>> simpleEntries = ReflectionUtil.getPrivateField(null, ReflectionUtil.getField(CreationMatrix.class, "simpleEntries"));

        Set<CreationEntry> toRemove = new HashSet<>();
        if (advancedEntries.containsKey(result)) {
            toRemove.addAll(advancedEntries.get(result));
            advancedEntries.remove(result);
        }

        if (simpleEntries.containsKey(result)) {
            toRemove.addAll(simpleEntries.get(result));
            simpleEntries.remove(result);
        }

        toRemove.forEach(entry -> {
            int target = entry.getObjectTarget();
            if (matrix.containsKey(target)) {
                List<CreationEntry> list = matrix.get(target);
                list.removeIf(e -> e.equals(entry));
                if (list.isEmpty())
                    matrix.remove(target);
                RequiemLogging.logInfo(String.format("Removed recipe from game for item: %s", result));
            }
        });
    }

    public static void applyEnchant(Item item, byte enchant, float power) {
        ItemSpellEffects effs = item.getSpellEffects();
        if (effs == null) {
            effs = new ItemSpellEffects(item.getWurmId());
        }
        SpellEffect eff = new SpellEffect(item.getWurmId(), enchant, power, 20000000);
        effs.addSpellEffect(eff);
        if (item.getDescription().length() > 0) {
            item.setDescription(String.format("%s ", item.getDescription()));
        }
        item.setDescription(String.format("%s%s%d", item.getDescription(), eff.getName().charAt(0), Math.round(power)));
    }

    public static Item createRandomSorcery(byte charges) {
        try {
            Item sorcery = ItemFactory.createItem(RandomUtils.randomSorceryItemTemplates(), 90 + (10 * Server.rand.nextFloat()), null);
            sorcery.setAuxData((byte) (3 - charges));
            return sorcery;
        } catch (FailedException | NoSuchTemplateException e) {
            RequiemLogging.logException("[ERROR] in createRandomSorcery in ItemTools", e);
        }
        return null;
    }

    public static Item createEnchantOrb(float power) {
        byte[] enchantOrbEnchants = {
                Enchants.BUFF_CIRCLE_CUNNING,
                Enchants.BUFF_FLAMING_AURA,
                Enchants.BUFF_SHARED_PAIN,
                Enchants.BUFF_ROTTING_TOUCH,
                Enchants.BUFF_LIFETRANSFER,
                Enchants.BUFF_NIMBLENESS,
                Enchants.BUFF_MINDSTEALER,
                Enchants.BUFF_FROSTBRAND,
                Enchants.BUFF_WEBARMOUR,
                Enchants.BUFF_BLESSINGDARK,
                Enchants.BUFF_VENOM,
                Enchants.BUFF_WIND_OF_AGES,
                110, 110, //Harden
                114, //Efficiency
                115, //Quarry
                116, //Prowess
                117, //Industry
                118, //Endurance
                119 //Acuity
        };
        try {
            Item enchantOrb = ItemFactory.createItem(CustomItems.enchantOrbId, 99 + (1 * Server.rand.nextFloat()), "");
            ItemSpellEffects effs = enchantOrb.getSpellEffects();
            if (effs == null) {
                effs = new ItemSpellEffects(enchantOrb.getWurmId());
            }
            byte enchant = (byte) RandomUtils.getRandArrayByte(enchantOrbEnchants); // changed
            SpellEffect eff = new SpellEffect(enchantOrb.getWurmId(), enchant, power, 20000000);
            effs.addSpellEffect(eff);
            enchantOrb.setDescription(String.format("%s %d", eff.getName(), Math.round(power)));
            return enchantOrb;
        } catch (FailedException | NoSuchTemplateException e) {
            RequiemLogging.logException("[ERROR] in createEnchantOrb in ItemTools", e);
        }
        return null;
    }

    public static Item createRandomPlateChain(float minQL, float maxQL, byte material, String creator) {
        try {
            Item armour = ItemFactory.createItem(RandomUtils.randomPlateChainTemplates(), minQL + ((maxQL - minQL) * Server.rand.nextFloat()), creator);
            armour.setMaterial(material);
            return armour;
        } catch (FailedException | NoSuchTemplateException e) {
            RequiemLogging.logException("[ERROR] in createRandomPlateChain in ItemTools", e);
        }
        return null;
    }

    public static Item createRandomToolWeapon(float minQL, float maxQL, String creator) {
        try {
            return ItemFactory.createItem(RandomUtils.randomToolWeaponTemplates(), minQL + ((maxQL - minQL) * Server.rand.nextFloat()), creator);
        } catch (FailedException | NoSuchTemplateException e) {
            RequiemLogging.logException("[ERROR] in createRandomToolWeapon in ItemTools", e);
        }
        return null;
    }

    public static Item createRandomMaterialsLumps(float minQL, float maxQL, String creator) {
        try {
            return lumps = ItemFactory.createItem(RandomUtils.randomLumpTemplates(), minQL + ((maxQL - minQL) * Server.rand.nextFloat()), creator);
        } catch (FailedException | NoSuchTemplateException e) {
            RequiemLogging.logException("[ERROR] in createRandomMaterialsLumps in ItemTools", e);
        }
        return null;
    }

    public static Item createRandomMaterialsConstruction(float minQL, float maxQL, String creator) {
        try {
            return ItemFactory.createItem(RandomUtils.randomMaterialConstructionTemplates(), minQL + ((maxQL - minQL) * Server.rand.nextFloat()), creator);
        } catch (FailedException | NoSuchTemplateException e) {
            RequiemLogging.logException("[ERROR] in createRandomMaterialsConstruction in ItemTools", e);
        }
        return null;
    }

    public static Item createRandomGem(float minQL, float maxQL, String creator) {
        try {
            return ItemFactory.createItem(RandomUtils.randomGem(false), minQL + ((maxQL - minQL) * Server.rand.nextFloat()), creator);
        } catch (FailedException | NoSuchTemplateException e) {
            RequiemLogging.logException("[ERROR] in createRandomGem in ItemTools", e);
        }
        return null;
    }

    public static Item createRandomStarGem(float minQL, float maxQL, String creator) {
        try {
            return ItemFactory.createItem(RandomUtils.randomGem(true), RandomUtils.getRandomQl(1f, 99f), creator);
        } catch (FailedException | NoSuchTemplateException e) {
            RequiemLogging.logException("[ERROR] in createRandomStarGem in ItemTools", e);
        }
        return null;
    }

    public static Item createRandomLootTool() {
        try {
            int[] templates = {
                    ItemList.hatchet,
                    ItemList.knifeCarving,
                    ItemList.pickAxe,
                    ItemList.saw,
                    ItemList.shovel,
                    ItemList.rake,
                    ItemList.hammerMetal,
                    ItemList.knifeButchering,
                    ItemList.stoneChisel,
                    ItemList.anvilSmall
            };
            int template = RandomUtils.getRandArrayInt(templates);
            float quality = 100;
            for (int i = 0; i < 2; i++) {
                quality = Math.min(quality, Math.max((float) 10, 70 * Server.rand.nextFloat()));
            }
            byte[] materials = {
                    Materials.MATERIAL_GOLD,
                    Materials.MATERIAL_SILVER,
                    Materials.MATERIAL_STEEL, Materials.MATERIAL_STEEL, Materials.MATERIAL_STEEL,
                    Materials.MATERIAL_COPPER, Materials.MATERIAL_COPPER,
                    Materials.MATERIAL_IRON, Materials.MATERIAL_IRON, Materials.MATERIAL_IRON, Materials.MATERIAL_IRON,
                    Materials.MATERIAL_LEAD, Materials.MATERIAL_LEAD,
                    Materials.MATERIAL_ZINC, Materials.MATERIAL_ZINC,
                    Materials.MATERIAL_BRASS, Materials.MATERIAL_BRASS,
                    Materials.MATERIAL_BRONZE, Materials.MATERIAL_BRONZE,
                    Materials.MATERIAL_TIN, Materials.MATERIAL_TIN,
                    Materials.MATERIAL_ADAMANTINE,
                    Materials.MATERIAL_GLIMMERSTEEL,
                    Materials.MATERIAL_SERYLL
            };
            byte material = (byte) RandomUtils.getRandArrayByte(materials); // changed
            byte rarity = 0;
            if (Server.rand.nextInt(80) <= 2) {
                rarity = 1;
            } else if (Server.rand.nextInt(250) <= 2) {
                rarity = 2;
            }
            byte[] enchants = {
                    Enchants.BUFF_WIND_OF_AGES, Enchants.BUFF_WIND_OF_AGES,
                    Enchants.BUFF_CIRCLE_CUNNING, Enchants.BUFF_CIRCLE_CUNNING,
                    Enchants.BUFF_BLESSINGDARK
            };
            byte enchant = enchants[Server.rand.nextInt(enchants.length)];
            float power = 100;
            for (int i = 0; i < 2; i++) {
                power = Math.min(power, 20 + (60 * Server.rand.nextFloat()));
            }
            Item tool = ItemFactory.createItem(template, quality, material, rarity, "");
            ItemSpellEffects effs = tool.getSpellEffects();
            if (effs == null) {
                effs = new ItemSpellEffects(tool.getWurmId());
            }
            SpellEffect eff = new SpellEffect(tool.getWurmId(), enchant, power, 20000000);
            effs.addSpellEffect(eff);
            tool.setDescription(String.format("%s %s", eff.getName(), String.valueOf((byte) power)));
            return tool;
        } catch (FailedException | NoSuchTemplateException e) {
            RequiemLogging.logException("[ERROR] in createRandomLootTool in ItemTools", e);
        }
        return null;
    }

    public static Item createTreasureBox() {
        try {
            Item treasureBox = ItemFactory.createItem(CustomItems.treasureBoxId, 10f + (90f * Server.rand.nextFloat()), "");
            if (Server.rand.nextInt(20) == 0) {
                treasureBox.setRarity((byte) 3);
            } else if (Server.rand.nextInt(5) == 0) {
                treasureBox.setRarity((byte) 2);
            } else if (Server.rand.nextBoolean()) {
                treasureBox.setRarity((byte) 1);
            }
            return treasureBox;
        } catch (FailedException | NoSuchTemplateException e) {
            RequiemLogging.logException("[ERROR] in createTreasureBox in ItemTools", e);
        }
        return null;
    }

    public static boolean isSingleUseRune(byte rune) {
        if (rune == -80) {
            return true;
        } else if (rune == -81) {
            return true;
        } else if (rune == -91) {
            return true;
        } else if (rune == -97) {
            return true;
        } else if (rune == -104) {
            return true;
        } else if (rune == -107) {
            return true;
        } else if (rune == -119) {
            return true;
        } else return rune == -126;
    }

    public static boolean isScroll(final Item item) {
        String[] scrollIds;
        try {
            for (int length = (scrollIds = ItemConstants.scrollids).length, i = 0; i < length; ++i) {
                final String boss = scrollIds[i];
                if (item.getTemplateId() == Integer.parseInt(boss)) {
                    return true;
                }
            }
        } catch (NumberFormatException e) {
            RequiemLogging.logException("[ERROR] in isScroll in ItemTools", e);
        }
        return false;
    }

    public static void sendItem(Player player, Item item, float x, float y, float z, float rot) {
        if (player.hasLink() && item.getTemplateId() != ItemList.firemarker) {
            try {
                final long id = item.getWurmId();
                final ByteBuffer bb = player.getCommunicator().getConnection().getBuffer();

                bb.put((byte) (-9));

                bb.putLong(id);
                bb.putFloat(x);
                bb.putFloat(y);
                bb.putFloat(rot);
                bb.putFloat(z);

                byte[] tempStringArr = item.getName().getBytes(StandardCharsets.UTF_8);
                bb.put((byte) tempStringArr.length);
                bb.put(tempStringArr);

                tempStringArr = item.getHoverText().getBytes(StandardCharsets.UTF_8);
                bb.put((byte) tempStringArr.length);
                bb.put(tempStringArr);

                tempStringArr = item.getModelName().getBytes(StandardCharsets.UTF_8);
                bb.put((byte) tempStringArr.length);
                bb.put(tempStringArr);

                bb.put((byte) (item.isOnSurface() ? 0 : -1));

                bb.put(item.getMaterial());

                tempStringArr = item.getDescription().getBytes(StandardCharsets.UTF_8);
                bb.put((byte) tempStringArr.length);
                bb.put(tempStringArr);

                bb.putShort(item.getImageNumber());

                if (item.getTemplateId() == ItemList.itemPile) {
                    bb.put((byte) 0);
                } else {
                    bb.put((byte) 1);
                    bb.putFloat(item.getQualityLevel());
                    bb.putFloat(item.getDamage());
                }

                bb.putFloat(item.getSizeMod());
                bb.putLong(item.onBridge());
                bb.put(item.getRarity());

                bb.put((byte) 0);
                bb.put((byte) 0);

                player.getCommunicator().getConnection().flush();

            } catch (Exception e) {
                RequiemLogging.logException(String.format("[ERROR] in sendItem in ItemTools. Failed to send item %s (%d) to player %s (%d)", player.getName(), player.getWurmId(), item.getName(), item.getWurmId()), e);
                player.setLink(false);
            }
        }
    }

}