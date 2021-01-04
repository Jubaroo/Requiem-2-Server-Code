package org.jubaroo.mods.wurm.server.server;

import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.Creatures;
import com.wurmonline.server.creatures.NoSuchCreatureTemplateException;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.actions.AddActions;
import org.jubaroo.mods.wurm.server.communication.KeyEvent;
import org.jubaroo.mods.wurm.server.communication.commands.*;
import org.jubaroo.mods.wurm.server.creatures.CreatureHelper;
import org.jubaroo.mods.wurm.server.creatures.CustomCreatures;
import org.jubaroo.mods.wurm.server.creatures.MethodsBestiary;
import org.jubaroo.mods.wurm.server.items.ItemMod;
import org.jubaroo.mods.wurm.server.misc.AchievementChanges;
import org.jubaroo.mods.wurm.server.misc.CustomTitles;
import org.jubaroo.mods.wurm.server.misc.SkillChanges;
import org.jubaroo.mods.wurm.server.misc.database.DatabaseHelper;
import org.jubaroo.mods.wurm.server.misc.database.holidays.*;
import org.jubaroo.mods.wurm.server.tools.CmdTools;
import org.jubaroo.mods.wurm.server.tools.SpellTools;
import org.jubaroo.mods.wurm.server.utils.Compat3D;

import java.lang.reflect.InvocationTargetException;

public class OnServerStarted {
    public static CmdTools cmdtool = null;

    public static void onServerStarted() {
        try {
            RequiemLogging.debug("Attempting to load 3D Stuff mod...");
            Compat3D.installDisplayHook();
            addCommands();
            RequiemLogging.debug("Registering Holiday Gifts...");
            RequiemChristmasGift.onServerStarted();
            RequiemAnniversaryGift.onServerStarted();
            RequiemStPatrickDayGift.onServerStarted();
            RequiemNewYearGift.onServerStarted();
            RequiemCanadaDayGift.onServerStarted();
            RequiemValentinesGift.onServerStarted();
            RequiemThanksgivingGift.onServerStarted();
            RequiemIndependenceDayGift.onServerStarted();
            RequiemVictoriaDayGift.onServerStarted();
            RequiemLogging.debug("Registering Patreon Gifts...");
            //PatreonSleepPowderGift.onServerStarted();
            RequiemLogging.debug("Registering Database helper...");
            DatabaseHelper.onServerStarted();
            RequiemLogging.debug("Registering Item Mod creation entries...");
            //Requiem.debug("Registering Deity changes...");
            //DeityChanges.onServerStarted();
            RequiemLogging.debug("Registering Skill changes...");
            SkillChanges.onServerStarted();
            RequiemLogging.debug("Registering actions...");
            AddActions.registerActions();
            RequiemLogging.debug("Setting custom creature corpse models...");
            MethodsBestiary.setTemplateVariables();
            RequiemLogging.debug("Setting up Achievement templates...");
            AchievementChanges.onServerStarted();
            RequiemLogging.debug("Editing existing item templates...");
            ItemMod.modifyItemsOnServerStarted();
            RequiemLogging.debug("Setting creatures to have custom names when bred...");
            CreatureHelper.makeLikeHorse("Unicorn");
            RequiemLogging.debug("Setting creatures to have no aggro...");
            for (String name : Constants.makeNonAggro.split(",")) {
                CreatureHelper.makeAlignZero(name);
                CreatureHelper.makeNoAggHuman(name);
            }
            RequiemLogging.debug("Setting spells to have no cooldown...");
            for (String name : Constants.noCooldownSpells.split(",")) {
                SpellTools.noSpellCooldown(name);
            }
            DatabaseHelper.setUniques();
            if (!Constants.initialGoblinCensus) {
                for (Creature c : Creatures.getInstance().getCreatures()) {
                    if (c.getTemplate().getTemplateId() == CustomCreatures.fogGoblinId && !Constants.fogGoblins.contains(c)) {
                        Constants.fogGoblins.add(c);
                    }
                }
                RequiemLogging.debug(String.format("Performed census of fog goblins, currently there are: %d fog goblins", Constants.fogGoblins.size()));
                Constants.initialGoblinCensus = true;
            }
            KeyEvent.preInit();
            CustomTitles.register();
            RequiemLogging.RequiemLoggingMessages();
        } catch (IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchMethodException | InvocationTargetException e) {
            RequiemLogging.logException("Error in modifyItemsOnServerStarted()", e);
        } catch (NoSuchCreatureTemplateException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void addCommands() {
        RequiemLogging.debug("Registering Server commands...");
        if (Constants.addCommands) {
            cmdtool = new CmdTools();
            cmdtool.addWurmCmd(new CmdGoTo());
            cmdtool.addWurmCmd(new CmdGmCommands());
            cmdtool.addWurmCmd(new CmdAddAff());
            cmdtool.addWurmCmd(new CmdSendToHome());
            cmdtool.addWurmCmd(new CmdFillUp());
            cmdtool.addWurmCmd(new CmdMovePlayer());
            cmdtool.addWurmCmd(new CmdTraderReset());
            cmdtool.addWurmCmd(new CmdCoffers());
            cmdtool.addWurmCmd(new CmdCull());
            cmdtool.addWurmCmd(new CmdAddSleepBonus());
            cmdtool.addWurmCmd(new CmdMassRefresh());
            cmdtool.addWurmCmd(new CmdMassSleepBonus());
            cmdtool.addWurmCmd(new CmdAdd5BankSlots());
        }
    }

}