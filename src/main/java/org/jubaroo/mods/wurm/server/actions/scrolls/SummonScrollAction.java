package org.jubaroo.mods.wurm.server.actions.scrolls;

import com.wurmonline.server.Items;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemTypes;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;
import org.gotti.wurmunlimited.modsupport.actions.*;
import org.jubaroo.mods.wurm.server.Requiem;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.creatures.MethodsBestiary;
import org.jubaroo.mods.wurm.server.tools.ItemTools;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class SummonScrollAction implements WurmServerMod, ItemTypes, MiscConstants, ModAction, BehaviourProvider, ActionPerformer {
    public static short actionId;
    static Logger logger;
    static ActionEntry actionEntry;

    static {
        SummonScrollAction.logger = Logger.getLogger(Requiem.class.getName());
    }

    public SummonScrollAction() {
        SummonScrollAction.actionId = (short) ModActions.getNextActionId();
        ModActions.registerAction(SummonScrollAction.actionEntry = ActionEntry.createEntry(SummonScrollAction.actionId, "Summon Animal", "Summoning Animal", new int[]{}));
    }

    public BehaviourProvider getBehaviourProvider() {
        return this;
    }

    public ActionPerformer getActionPerformer() {
        return this;
    }

    public short getActionId() {
        return SummonScrollAction.actionId;
    }

    public List<ActionEntry> getBehavioursFor(final Creature performer, final Item source, final Item target) {
        if (!(performer instanceof Player)) {
            return null;
        }
        if (ItemTools.isScroll(target)) {
            return Collections.singletonList(SummonScrollAction.actionEntry);
        }
        return null;
    }

    public boolean action(final Action act, final Creature performer, final Item source, final Item target, final short action, final float counter) {
        if (performer instanceof Player && ItemTools.isScroll(target)) {
            if (performer.getFollowers().length > 0) {
                performer.getCommunicator().sendNormalServerMessage("You are too busy leading other creatures and can not focus on summoning.");
                return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            try {
                if (MethodsBestiary.spawnCreature(target.getTemplateId(), performer, true)) {
                    Items.destroyItem(target.getWurmId());
                } else {
                    performer.getCommunicator().sendNormalServerMessage("Something is not working, please tell the staff by opening a support ticket.");
                }
                return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            } catch (Exception ex) {
                RequiemLogging.logWarning(ex.getMessage() + ex);
                return propagate(act, ActionPropagation.FINISH_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
        }
        return propagate(act, ActionPropagation.CONTINUE_ACTION, ActionPropagation.NO_SERVER_PROPAGATION, ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
    }
}
