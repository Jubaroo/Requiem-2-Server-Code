package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.Items;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.behaviours.Actions;
import com.wurmonline.server.bodys.BodyTemplate;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.CreatureTemplate;
import com.wurmonline.server.creatures.CreatureTemplateFactory;
import com.wurmonline.server.creatures.CreatureTemplateIds;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.skills.SkillList;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.util.Collections;
import java.util.List;

public class HorseScrollAction implements ModAction {
    // TODO make it so you cannot summon the horse in a village you do not have ride permissions in
    private final short actionId;
    private final ActionEntry actionEntry;

    public HorseScrollAction() {
        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Summon horse",
                "summoning",
                new int[]{ActionTypesProxy.ACTION_TYPE_NOMOVE}
        );
        ModActions.registerAction(actionEntry);
    }

    @Override
    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            // Menu with activated object
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item object) {
                return this.getBehavioursFor(performer, object);
            }

            // Menu without activated object
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item object) {
                if (performer instanceof Player && object != null && object.getTemplateId() == CustomItems.scrollOfSummoningHorse.getTemplateId()) {
                    return Collections.singletonList(actionEntry);
                }
                return null;
            }
        };
    }

    @Override
    public ActionPerformer getActionPerformer() {
        return new ActionPerformer() {

            @Override
            public short getActionId() {
                return actionId;
            }

            // Without activated object
            @Override
            public boolean action(Action act, Creature performer, Item target, short action, float counter) {
                try {
                    String itemName = target.getName().toLowerCase();
                    float x = performer.getPosX();
                    float y = performer.getPosY();
                    if (performer instanceof Player) {
                        if (target.getTemplate().getTemplateId() != CustomItems.scrollOfSummoningHorse.getTemplateId()) {
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        if (performer.getCurrentVillage() != null && (!performer.getCurrentVillage().isActionAllowed(Actions.LEAD, performer))) {
                            performer.getCommunicator().sendSafeServerMessage("The village you are currently in does not allow you to lead the horse you would summon. Move away from the village and try again.");
                            return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                        }
                        CreatureTemplate template = CreatureTemplateFactory.getInstance().getTemplate(CreatureTemplateIds.HORSE_CID);
                        Creature.doNew(template.getTemplateId(), true, x, y + 1, Server.rand.nextFloat() * 360f, performer.getLayer(), template.getName(), Server.rand.nextBoolean() ? MiscConstants.SEX_MALE : MiscConstants.SEX_FEMALE, performer.getKingdomId(), BodyTemplate.TYPE_HORSE, false, /*age*/(byte) 20);
                        performer.getSkills().getSkill(SkillList.BODY_CONTROL).skillCheck(10d, 0d, false, 1);
                        performer.getInventory().insertItem(ItemFactory.createItem(ItemList.rope, 50f, MiscConstants.COMMON, performer.getNameWithoutPrefixes()), true);
                        Items.destroyItem(target.getWurmId());
                        performer.getCommunicator().sendSafeServerMessage(String.format("The %s exhausts the last of its energy bringing the horse from the Void and crumbles to dust in your hand.", itemName));
                        Server.getInstance().broadCastAction(String.format("%s summons a horse from thin air using some sort of scroll.", performer.getName()), performer, 5);
                    } else {
                        RequiemLogging.logWarning(String.format("Somehow a non-player activated a %s...", itemName));
                    }
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                } catch (Exception e) {
                    RequiemLogging.logException("[ERROR] in action in HorseScrollAction", e);
                    return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
                }
            }

            @Override
            public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
                return this.action(act, performer, target, action, counter);
            }
        };
    }

}