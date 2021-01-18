package org.jubaroo.mods.wurm.server.tools;

import com.wurmonline.mesh.Tiles;
import com.wurmonline.server.Servers;
import com.wurmonline.server.items.ItemTemplateFactory;
import com.wurmonline.server.items.NoSuchTemplateException;
import com.wurmonline.shared.util.StringUtilities;

public class RequiemTools {

    public static boolean isRequiemServer() {
        return Servers.localServer.getName().contains("Requiem of Wurm");
    }

    public static int[] getPropertyArray(String str) {
        String[] arr = str.split(",");
        int[] ret = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ret[i] = Integer.parseInt(arr[i].trim());
        }
        return ret;
    }

    public static String a_an(String string) {
        return (StringUtilities.isVowel(string.charAt(0))) ? "an" : "a";
    }

    public static String a_an_FromTemplateId(int itemId) throws NoSuchTemplateException {
        String name = ItemTemplateFactory.getInstance().getTemplate(itemId).getName();
        return (StringUtilities.isVowel(name.charAt(0))) ? "an" : "a";
    }

    public static boolean isTreeGrassBushTile(byte type) {
        return Tiles.isTree(type) || Tiles.isBush(type) || Tiles.isGrassType(type);
    }

    public static boolean isEnchantedTile(byte type) {
        return Tiles.isEnchanted(type) || Tiles.isEnchantedBush(type) || Tiles.isEnchantedTree(type);
    }

    public static boolean isMineDoor(Tiles.Tile theTile) {
        return theTile.getId() == Tiles.TILE_TYPE_MINE_DOOR_GOLD || theTile.getId() == Tiles.TILE_TYPE_MINE_DOOR_SILVER || theTile.getId() == Tiles.TILE_TYPE_MINE_DOOR_STEEL || theTile.getId() == Tiles.TILE_TYPE_MINE_DOOR_STONE || theTile.getId() == Tiles.TILE_TYPE_MINE_DOOR_WOOD;
    }

    //without decimal digits
    public static String toPercentage(float n) {
        return String.format("%.0f", n * 100) + "%";
    }

    //accept a param to determine the numbers of decimal digits
    public static String toPercentage(float n, int digits) {
        return String.format("%s%%", String.format(String.format("%%.%df", digits), n * 100));
    }

}
