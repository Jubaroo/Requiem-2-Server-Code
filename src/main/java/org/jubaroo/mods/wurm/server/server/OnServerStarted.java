package org.jubaroo.mods.wurm.server.server;

import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.Creatures;
import com.wurmonline.server.creatures.NoSuchCreatureTemplateException;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.actions.AddActions;
import org.jubaroo.mods.wurm.server.communication.KeyEvent;
import org.jubaroo.mods.wurm.server.communication.commands.*;
import org.jubaroo.mods.wurm.server.creatures.CreatureSpawns;
import org.jubaroo.mods.wurm.server.creatures.CreatureTweaks;
import org.jubaroo.mods.wurm.server.creatures.CustomCreatures;
import org.jubaroo.mods.wurm.server.creatures.Titans;
import org.jubaroo.mods.wurm.server.creatures.bounty.LootTable;
import org.jubaroo.mods.wurm.server.items.ItemMod;
import org.jubaroo.mods.wurm.server.items.pottals.PortalMod;
import org.jubaroo.mods.wurm.server.misc.AchievementChanges;
import org.jubaroo.mods.wurm.server.misc.CustomTitles;
import org.jubaroo.mods.wurm.server.tools.CmdTools;
import org.jubaroo.mods.wurm.server.tools.SpellTools;
import org.jubaroo.mods.wurm.server.tools.database.DatabaseHelper;
import org.jubaroo.mods.wurm.server.tools.database.holidays.*;
import org.jubaroo.mods.wurm.server.utils.Compat3D;

import java.lang.reflect.InvocationTargetException;

import static org.jubaroo.mods.wurm.server.ModConfig.*;
import static org.jubaroo.mods.wurm.server.server.constants.PollingConstants.fogGoblins;

public class OnServerStarted {
    public static CmdTools cmdtool = null;

    public static void onServerStarted() {
        try {
            RequiemLogging.logInfo("Attempting to load 3D Stuff mod...");
            Compat3D.installDisplayHook();
            addCommands();
            PortalMod.onServerStarted();
            RequiemLogging.logInfo("Registering Creature Loot Drops...");
            LootTable.creatureDied();
            RequiemLogging.logInfo("Registering Holiday Gifts...");
            RequiemChristmasGift.onServerStarted();
            RequiemAnniversaryGift.onServerStarted();
            RequiemStPatrickDayGift.onServerStarted();
            RequiemNewYearGift.onServerStarted();
            RequiemCanadaDayGift.onServerStarted();
            RequiemValentinesGift.onServerStarted();
            RequiemThanksgivingGift.onServerStarted();
            RequiemIndependenceDayGift.onServerStarted();
            RequiemVictoriaDayGift.onServerStarted();
            //RequiemLogging.logInfo("Registering Patreon Gifts...");
            //PatreonSleepPowderGift.onServerStarted();
            RequiemLogging.logInfo("Registering Database helper...");
            DatabaseHelper.onServerStarted();
            //RequiemLogging.logInfo("Registering Item Mod creation entries...");
            //Requiem.debug("Registering Deity changes...");
            //DeityChanges.onServerStarted();
            RequiemLogging.logInfo("Registering Skill changes...");
            RequiemLogging.logInfo("Registering actions...");
            AddActions.registerActions();
            RequiemLogging.logInfo("Setting custom creature corpse models...");
            CreatureTweaks.setTemplateVariables();
            RequiemLogging.logInfo("Setting up Achievement templates...");
            AchievementChanges.onServerStarted();
            RequiemLogging.logInfo("Editing existing item templates...");
            ItemMod.modifyItemsOnServerStarted();
            RequiemLogging.logInfo("Setting creatures to have custom names when bred...");
            RequiemLogging.logInfo("Setting spells to have no cooldown...");
            for (String name : noCooldownSpells.split(",")) {
                SpellTools.noSpellCooldown(name);
            }
            DatabaseHelper.setUniques();
            Titans.initializeTitanTimer();
            //SupplyDepotBehaviour.initializeDepotTimer();

            if (!initialGoblinCensus) {
                for (Creature c : Creatures.getInstance().getCreatures()) {
                    if (c.getTemplate().getTemplateId() == CustomCreatures.fogGoblinId && !fogGoblins.contains(c)) {
                        fogGoblins.add(c);
                    }
                }
                RequiemLogging.logInfo(String.format("Performed census of fog goblins, currently there are: %d fog goblins", fogGoblins.size()));
                initialGoblinCensus = true;
            }
            KeyEvent.preInit();
            CustomTitles.register();
            CreatureSpawns.spawnTable();

            RequiemLogging.RequiemLoggingMessages();
        } catch (IllegalArgumentException | IllegalAccessException | ClassCastException | NoSuchMethodException | InvocationTargetException e) {
            RequiemLogging.logException("Error in onServerStarted()", e);
        } catch (NoSuchCreatureTemplateException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void addCommands() {
        RequiemLogging.logInfo("Registering Server commands...");
        if (addCommands) {
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
