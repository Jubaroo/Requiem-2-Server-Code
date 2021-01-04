package org.jubaroo.mods.wurm.server.actions.items;

import com.wurmonline.server.Items;
import com.wurmonline.server.NoSuchItemException;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.WurmColor;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.ActionPropagation;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.util.ArrayList;
import java.util.List;

public class DyeDragonArmorAction implements BehaviourProvider {
    private final List<ActionEntry> menu;

    public DyeDragonArmorAction() {
        menu = new ArrayList<>();
        menu.add(new ActionEntry((short) -5, "Dye", ""));
        menu.add(new DyeAction("Black", WurmColor.createColor(10, 10, 10)).actionEntry);
        menu.add(new DyeAction("Blue", WurmColor.createColor(40, 40, 215)).actionEntry);
        menu.add(new DyeAction("Green", WurmColor.createColor(10, 210, 10)).actionEntry);
        menu.add(new DyeAction("Red", WurmColor.createColor(215, 40, 40)).actionEntry);
        menu.add(new DyeAction("White", WurmColor.createColor(255, 255, 255)).actionEntry);
    }

    public boolean canUse(Creature performer, Item source, Item target) {
        return performer.isPlayer() && source != null && target != null &&
                source.getTemplateId() == CustomItems.dyeKitId &&
                source.getTopParent() == performer.getInventory().getWurmId() &&
                target.getTopParent() == performer.getInventory().getWurmId() &&
                target.isDragonArmour() && target.isArmour();
    }

    @Override
    public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {
        if (canUse(performer, source, target))
            return menu;
        else
            return null;
    }

    public class DyeAction implements ActionPerformer {
        private final ActionEntry actionEntry;

        private final String name;
        private final int color;

        public DyeAction(String name, int color) {
            this.name = name;
            this.color = color;

            actionEntry = ActionEntry.createEntry((short) ModActions.getNextActionId(), name, "dyeing", new int[]{
                    48 /* ACTION_TYPE_ENEMY_ALWAYS */,
                    36 /* ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM */
            });
            ModActions.registerAction(actionEntry);
            ModActions.registerActionPerformer(this);
        }

        @Override
        public short getActionId() {
            return actionEntry.getNumber();
        }

        @Override
        public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {
            if (!canUse(performer, source, target)) {
                performer.getCommunicator().sendAlertServerMessage("Do you have the correct item activated and/or targeting the correct item/creature? Please try again or use /support to open a ticket with as much info as possible, and the staff will get to it as soon as possible.");
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }

            if (target.getColor() == color) {
                performer.getCommunicator().sendAlertServerMessage(String.format("The %s is already %s.", target.getName(), name.toLowerCase()));
                return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }

            String origName = target.getName();

            target.setColor(color);
            target.setName(name.toLowerCase() + " " + target.getTemplate().getName());
            performer.getCommunicator().sendUpdateInventoryItem(target);

            byte aux = source.getAuxData();
            String remaining;

            if (aux >= 7) {
                try {
                    source.getParent().dropItem(source.getWurmId(), false);
                } catch (NoSuchItemException ignored) {
                }
                Items.destroyItem(source.getWurmId());
                remaining = String.format("The %s crumbles to dust as you use it's last charge.", source.getName());
            } else {
                source.setAuxData((byte) (aux + 1));
                remaining = String.format("The %s now has %d charges remaining.", source.getName(), 7 - aux);
            }

            performer.getCommunicator().sendNormalServerMessage(String.format("You dye the %s in %s. %s", origName, name.toLowerCase(), remaining));

            return propagate(action, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        }
    }
}