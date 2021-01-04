package org.jubaroo.mods.wurm.server.tools;

import com.wurmonline.server.creatures.CreatureTemplate;
import org.jubaroo.mods.wurm.server.RequiemLogging;

public class CreatureHelper {

    public static boolean makeAlignZero(String name) {
        RequiemLogging.logInfo("Making Align Zero: " + name);
        CreatureTemplate tmpl = CreatureTools.getTemplate(name);
        if (tmpl == null) {
            return false;
        }
        tmpl.setAlignment(0f);
        return true;
    }

    public static boolean makeNoAggHuman(String name) {
        RequiemLogging.logInfo("Making No Aggro: " + name);
        return CreatureTools.setTemplateField(name, "aggHuman", false);
    }

    public static boolean makeLikeHorse(String creatureName) {
        CreatureTemplate tmpl = CreatureTools.getTemplate(creatureName);
        if (tmpl == null) {
            return false;
        }
        CreatureTools.addTemplateTypes("Unicorn", 42, 14, 12, 43); // HORSE, LEADABLE, SWIM, DOMESTIC
        CreatureTools.setTemplateField("Unicorn", "isHorse", true);
        return true;
    }

}
