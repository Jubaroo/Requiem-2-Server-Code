package org.jubaroo.mods.wurm.server.items;

import com.wurmonline.server.items.ItemTemplate;
import com.wurmonline.server.items.ItemTemplateFactory;
import com.wurmonline.server.items.NoSuchTemplateException;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.utils.TweakApiPerms;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ItemHelper {

    public static Method _getItemTemplate = TweakApiPerms.getClassMeth(
            "com.wurmonline.server.items.CreationWindowMethods",
            "getItemTemplate",
            "int");

    public static ItemTemplate getItemTemplate(int id) {
        if (_getItemTemplate == null) {
            return null;
        }
        try {
            return (ItemTemplate) _getItemTemplate.invoke(null, id);
        } catch (InvocationTargetException | IllegalAccessException e) {
            RequiemLogging.logException(String.format("[ERROR] in getItemTemplate: %s", e.toString()), e);
            return null;
        }
    }

    // ======================= Set Item Types, Name, Etc. =======================
    public static void setFragments(int templateId, int fragmentCount) {
        try {
            final ItemTemplate item = ItemTemplateFactory.getInstance().getTemplate(templateId);
            ReflectionUtil.setPrivateField(item, ReflectionUtil.getField(item.getClass(), "fragmentAmount"), fragmentCount);
        } catch (IllegalAccessException | NoSuchFieldException | NoSuchTemplateException e) {
            RequiemLogging.logException("[ERROR] in setFragments in ItemHelper", e);
        }
    }

    public static boolean missionsItem(int tempid) {
        if (_getItemTemplate == null) {
            return false;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return false;
        }
        return TweakApiPerms.setItemField(tplat, "missions", true);
    }

    public static boolean notMissionsItem(int tempid) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "notMissions", false);
    }

    public static boolean itemName(int tempid, String name) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "name", name);
    }

    public static boolean modelNameItem(int tempid, String modelName) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "modelName", modelName);
    }

    public static boolean itemDescriptionLongItem(int tempid, String description) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "itemDescriptionLong", description);
    }

    public static boolean noDropItem(int tempid, boolean b) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "nodrop", b);
    }

    public static boolean containerCentimetersXItem(int tempid, int containerCentimetersX) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "containerCentimetersX", containerCentimetersX);
    }

    public static boolean containerCentimetersYItem(int tempid, int containerCentimetersY) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "containerCentimetersY", containerCentimetersY);
    }

    public static boolean containerCentimetersZItem(int tempid, int containerCentimetersZ) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "containerCentimetersZ", containerCentimetersZ);
    }

    public static boolean containerVolumeItem(int tempid) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        int newVolume = tplat.getSizeX() * tplat.getSizeY() * tplat.getSizeZ();
        return TweakApiPerms.setItemField(tplat, "containerVolume", newVolume);
    }

    public static boolean weightItem(int tempid, int weight) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "weight", weight);
    }

    public static boolean valueItem(int tempid, int value) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "value", value);
    }

    public static boolean fullPriceItem(int tempid, boolean b) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "fullprice", b);
    }

    public static boolean isTransportableItem(int tempid, boolean b) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "isTransportable", b);
    }

    public static boolean combineItem(int tempid, boolean b) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "combine", b);
    }

    public static boolean combineColdItem(int tempid, boolean b) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "combineCold", b);
    }

    public static boolean centimetersXItem(int tempid, int centimetersX) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "centimetersX", centimetersX);
    }

    public static boolean centimetersYItem(int tempid, int centimetersY) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "centimetersY", centimetersY);
    }

    public static boolean centimetersZItem(int tempid, int centimetersZ) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "centimetersZ", centimetersZ);
    }

    public static boolean volumeItem(int tempid) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        int newVolume = tplat.getSizeX() * tplat.getSizeY() * tplat.getSizeZ();
        return TweakApiPerms.setItemField(tplat, "volume", newVolume);
    }

    public static boolean isTwohandedItem(int tempid, boolean b) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "isTwohanded", b);
    }

    public static boolean difficultyItem(int tempid, int difficulty) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "difficulty", (float) difficulty);
    }

    public static boolean decorationItem(int tempid, boolean b) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "decoration", b);
    }

    public static boolean foodItem(int tempid, boolean b) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "food", b);
    }

    public static boolean inFoodGroupItem(int tempid, int foodGroup) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "inFoodGroup", foodGroup);
    }

    public static boolean isRecipeItemItem(int tempid, boolean b) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "isRecipeItem", b);
    }

    public static boolean isImproveItem(int tempid, boolean b) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "isImproveItem", b);
    }

    public static boolean repairableItem(int tempid, boolean b) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "repairable", b);
    }

    public static boolean materialItem(int tempid, int material) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "material", (byte) material);
    }

    public static boolean decayOnDeedItem(int tempid, boolean b) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "decayOnDeed", b);
    }

    public static boolean isPlantOneAWeekItem(int tempid, boolean b) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "isPlantOneAWeek", b);
    }

    public static boolean hideAddToCreationWindowItem(int tempid, boolean b) {
        if (_getItemTemplate == null) {
            return true;
        }
        ItemTemplate tplat = getItemTemplate(tempid);
        if (tplat == null) {
            return true;
        }
        return TweakApiPerms.setItemField(tplat, "hideAddToCreationWindow", b);
    }
}
