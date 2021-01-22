package org.jubaroo.mods.wurm.server.actions;

import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.actions.manageNpc.ManageBehaviourProvider;
import org.jubaroo.mods.wurm.server.actions.crystals.ArcaneShardAction;
import org.jubaroo.mods.wurm.server.actions.crystals.death.CrystalAbortAction;
import org.jubaroo.mods.wurm.server.actions.crystals.death.DeathAction;
import org.jubaroo.mods.wurm.server.actions.crystals.death.RemovePlantsAction;
import org.jubaroo.mods.wurm.server.actions.crystals.fire.BurnCorpseAction;
import org.jubaroo.mods.wurm.server.actions.crystals.fire.HeatUpAction;
import org.jubaroo.mods.wurm.server.actions.crystals.fire.LightFireAction;
import org.jubaroo.mods.wurm.server.actions.crystals.frost.CoolAction;
import org.jubaroo.mods.wurm.server.actions.crystals.frost.FreezeLavaAction;
import org.jubaroo.mods.wurm.server.actions.crystals.frost.PreserveFoodAction;
import org.jubaroo.mods.wurm.server.actions.crystals.life.EnchantGroundAction;
import org.jubaroo.mods.wurm.server.actions.crystals.life.GrowGrassAction;
import org.jubaroo.mods.wurm.server.actions.crystals.life.GrowTreesAction;
import org.jubaroo.mods.wurm.server.actions.crystals.life.RebirthAction;
import org.jubaroo.mods.wurm.server.actions.labyrinth.LabyrinthAction;
import org.jubaroo.mods.wurm.server.actions.labyrinth.LabyrinthRemoveAction;
import org.jubaroo.mods.wurm.server.actions.portals.*;
import org.jubaroo.mods.wurm.server.actions.machinaOfFortune.MachinaOfFortuneAction;
import org.jubaroo.mods.wurm.server.actions.machinaOfFortune.MachinaOfFortuneInstructionsAction;
import org.jubaroo.mods.wurm.server.items.CustomItems;
import org.jubaroo.mods.wurm.server.items.ItemMod;

import static org.jubaroo.mods.wurm.server.ModConfig.*;
import static org.jubaroo.mods.wurm.server.server.constants.ItemConstants.*;

public class AddActions {

    public static void registerActions() {
        RequiemLogging.debug("Registering Item actions...");
        ModActions.registerAction(new JoustAction(LanceDamage, BaseHitChance, SpearSkillRange, BonusLanceDamage, LoseHelmetChance, PerKMDamageBoost, AllowSkillGain, LanceRange));
        ItemMod.addActPortal();
        ModActions.registerAction(new PortalAction());
        ModActions.registerAction(new PortalAddLocationAction());
        ModActions.registerAction(new PortalEditAction());
        ModActions.registerAction(new AddUpkeepAction());
        ModActions.registerAction(new ActivatePortalAction());
        ModActions.registerAction(new EmptyTrashHeapAction());
        ModActions.registerAction(new LightningStormAction());
        ModActions.registerAction(new TreasureHuntChestAction());
        ModActions.registerAction(new SummoningStoneAction());
        ModActions.registerAction(new ShopSignAction());
        //ModActions.registerAction(new ItemRenameAction());
        ModActions.registerBehaviourProvider(new DyeDragonArmorAction());
        ModActions.registerAction(new WagonKitPackAction());
        ModActions.registerAction(new GuardTokenAction());
        ModActions.registerAction(new DiseasePotionAction());
        ModActions.registerAction(new MachinaOfFortuneAction());
        ModActions.registerAction(new ScrollOfGearBindingAction());
        ModActions.registerAction(new ScrollOfVillageCreateAction());
        ModActions.registerAction(new ScrollOfVillageHealAction());
        ModActions.registerAction(new ScrollOfVillageWarAction());
        ModActions.registerAction(new MachinaOfFortuneInstructionsAction());
        ModActions.registerAction(new AffinityCatcherCaptureAction());
        ModActions.registerAction(new AffinityCatcherConsumeAction());
        ModActions.registerAction(new AffinityOrbAction());
        ModActions.registerAction(new RequiemCacheOpenAction());
        ModActions.registerAction(new ArrowPackUnpackAction());
        ModActions.registerAction(new BookConversionAction());
        ModActions.registerAction(new CrystalCombineAction());
        ModActions.registerAction(new TamingStickAction());
        ModActions.registerAction(new ChaosCrystalInfuseAction());
        ModActions.registerAction(new DepthDrillAction());
        ModActions.registerAction(new DisintegrationRodAction());
        ModActions.registerAction(new EnchantersCrystalInfuseAction());
        ModActions.registerAction(new EnchantOrbAction());
        ModActions.registerAction(new EternalOrbAction());
        ModActions.registerAction(new FriyanTabletAction());
        ModActions.registerAction(new KeyCombinationAction());
        ModActions.registerAction(new SealedMapAction());
        ModActions.registerAction(new SupplyDepotAction());
        ModActions.registerAction(new TreasureBoxAction());
        ModActions.registerAction(new SorceryCombineAction());
        ModActions.registerAction(new SorcerySplitAction());
        ModActions.registerAction(new NewsletterAction());
        ModActions.registerAction(new PortableBankAction());
        ModActions.registerAction(new TownScrollAction());
        ModActions.registerAction(new BuyStarterKitAction());
        ModActions.registerAction(new HorseScrollAction());
        ModActions.registerAction(new GemSmashAction());
        ModActions.registerAction(new BankSlotsAction());
        //ModActions.registerAction(new SleepPowderVendorAction());
        ModActions.registerAction(new BartenderFoodAction());
        ModActions.registerAction(new InnkeeperFoodAction());
        ModActions.registerAction(new SmashAction());
        ModActions.registerAction(new StealthAction());
        ModActions.registerAction(new SkullAction());
        ModActions.registerAction(new TentSleepAction());
        //ModActions.registerAction(new SummonScrollAction());
        ModActions.registerAction(new CreateAshAction());
        ModActions.registerAction(new ChampionOrbAction());
        ModActions.registerAction(new HolyBookPrayAction());
        ModActions.registerAction(new AltarWagonPrayAction());
        ModActions.registerAction(new CreateSnowballAction());
        ModActions.registerAction(new ConvertWoodTypeAction());
        ModActions.registerAction(new HeraldicCertificateAction());
        ModActions.registerAction(new RenameAction());
        ModActions.registerAction(new ScrollOfTitlesAction());
        RequiemLogging.debug("Registering Cache actions...");
        RequiemLogging.debug("Registering Server actions...");
        ModActions.registerAction(new MissionAddAction());
        ModActions.registerAction(new MissionRemoveAction());
        ModActions.registerAction(new CreatureReportAction());
        //ModActions.registerAction(new SmoothTerrainAction());
        ModActions.registerAction(new UnequipAllAction());
        ModActions.registerAction(new ReceiveMailAction());
        ModActions.registerAction(new LeaderboardAction());
        ModActions.registerAction(new LeaderboardSkillAction());
        ModActions.registerAction(new AddSubGroupAction());
        ModActions.registerAction(new SprintAction());
        //ModActions.registerAction(new ReplacementChristmasGiftAction());
        RequiemLogging.debug("Registering Crystal actions...");
        ModActions.registerAction(new ArcaneShardAction());
        // Fire Crystal
        ModActions.registerAction(new BurnCorpseAction());
        ModActions.registerAction(new HeatUpAction());
        ModActions.registerAction(new LightFireAction());
        // Frost Crystal
        ModActions.registerAction(new CoolAction());
        ModActions.registerAction(new PreserveFoodAction());
        ModActions.registerAction(new FreezeLavaAction());
        // Death Crystal
        ModActions.registerAction(new CrystalAbortAction());
        ModActions.registerAction(new DeathAction());
        ModActions.registerAction(new RemovePlantsAction());
        // Life Crystal
        ModActions.registerAction(new RebirthAction());
        ModActions.registerAction(new GrowGrassAction());
        ModActions.registerAction(new GrowTreesAction());
        ModActions.registerAction(new EnchantGroundAction());
        if (cashPerCorpse > 0) ModActions.registerAction(new CorpseBountyAction());
        if (searchDens) {
            ModActions.registerAction(new DisturbDenAction());
        }
        RequiemLogging.debug("Registering GM actions...");
        if (addGmProtect) {
            ModActions.registerAction(new GmProtectAction());
        }
        if (addGmUnprotect) {
            ModActions.registerAction(new GMUnprotectAction());
        }
        if (addSpawnGuard) {
            ModActions.registerAction(new SpawnTowerGuardAction());
        }
        //ModActions.registerAction(new RequiemReloadAction());
        ModActions.registerAction(new KillCreatureAction());
        if (addLabyrinth) {
            //ModActions.registerBehaviourProvider(new LabyrinthMenuProvider());
            ModActions.registerAction(new LabyrinthAction());
            ModActions.registerAction(new LabyrinthRemoveAction());
        }
        // Conquest
        ModActions.registerAction(new LearnScrollAction(scrollTemplates));
        ModActions.registerAction(new UseEnchantScrollAction(enchantScrollTemplates));
        ModActions.registerAction(new OpenCrateAction(lootBoxID, smallLootBoxID, scrollTemplates));
        ModActions.registerAction(new PlaceGemAction(CustomItems.athanorMechanismId, enchantScrollTemplates));
        ModActions.registerBehaviourProvider(new ManageBehaviourProvider());

        ModActions.registerAction(new TreasureCacheOpenAction());
    }
}
