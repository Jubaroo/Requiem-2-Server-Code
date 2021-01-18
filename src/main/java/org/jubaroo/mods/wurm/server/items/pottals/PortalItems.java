package org.jubaroo.mods.wurm.server.items.pottals;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.TimeConstants;
import com.wurmonline.server.items.ItemTypes;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;

import java.io.IOException;

public class PortalItems implements WurmServerMod, ItemTypes, MiscConstants {

    public PortalItems() {
        try {
            com.wurmonline.server.items.ItemTemplateCreator.createItemTemplate(4002, "Server Portal", "portals", "almost full", "somewhat occupied", "half-full", "emptyish",
                    "This rudimentary structure is rumoured to lead to far away lands.",
                    new short[]{108, 25, 49, 31, 52, 44, 48, 67, 51, 86, 178, 92}, (short) 60, (short) 1, 0, TimeConstants.DECAYTIME_STONE, 100, 100, 200, -10,
                    MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.portal.8.", 90.0F, 1400, (byte) 15, 100, false);

            com.wurmonline.server.items.ItemTemplateCreator.createItemTemplate(4003, "Huge Server Portal", "portals", "almost full", "somewhat occupied", "half-full",
                    "emptyish",
                    "This impressive structure leads to far away lands.",
                    new short[]{108, 25, 49, 31, 52, 44, 48, 67, 51, 178, 92}, (short) 60, (short) 1, 0, TimeConstants.DECAYTIME_STONE, 100, 100,
                    200, -10, MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.portal.7.", 90.0F, 1400, (byte) 15,
                    100, false);

            com.wurmonline.server.items.ItemTemplateCreator.createItemTemplate(4004, 3, "Steel Server Portal", "portals", "superb", "good", "ok", "poor", "",
                    new short[]{108, 25, 49, 31, 52, 44, 48, 67, 51, 178, 92}, (short) 60, (short) 1, 0, 9223372036854775807L, 100, 100, 200, -10,
                    MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.portal.9.", 90.0F, 1400, (byte) 57, 10000, false,
                    -1);

            com.wurmonline.server.items.ItemTemplateCreator.createItemTemplate(4010, 3, "Dark Crystal Server Portal", "portals", "superb", "good", "ok", "poor", "",
                    new short[]{108, 25, 49, 31, 52, 44, 48, 67, 51, 178, 92}, (short) 60, (short) 1, 0, 9223372036854775807L, 100, 100, 200, -10,
                    MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.portal.3.", 90.0F, 1400, (byte) 57, 10000, false,
                    -1);

            com.wurmonline.server.items.ItemTemplateCreator.createItemTemplate(4011, 3, "Crystal Server Portal", "portals", "superb", "good", "ok", "poor", "",
                    new short[]{108, 25, 49, 31, 52, 44, 48, 67, 51, 178, 92}, (short) 60, (short) 1, 0, 9223372036854775807L, 100, 100, 200, -10,
                    MiscConstants.EMPTY_BYTE_PRIMITIVE_ARRAY, "model.structure.portal.4.", 90.0F, 1400, (byte) 57, 10000, false,
                    -1);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}