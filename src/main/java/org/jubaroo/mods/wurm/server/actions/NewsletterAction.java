package org.jubaroo.mods.wurm.server.actions;

import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.questions.NewsletterQuestion;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewsletterAction implements ModAction {
    public static short actionId;
    private static Logger logger;

    static {
        NewsletterAction.logger = Logger.getLogger(NewsletterAction.class.getName());
    }

    private final ActionEntry actionEntry;

    public NewsletterAction() {
        NewsletterAction.logger.log(Level.INFO, "NewsletterAction()");
        NewsletterAction.actionId = (short) ModActions.getNextActionId();
        ModActions.registerAction(this.actionEntry = ActionEntry.createEntry(NewsletterAction.actionId, "Read newsletter", "reading", new int[]{
                ActionTypesProxy.ACTION_TYPE_NOMOVE
        }));
    }

    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            public List<ActionEntry> getBehavioursFor(final Creature performer, final Item source, final Item object) {
                return this.getBehavioursFor(performer, object);
            }

            public List<ActionEntry> getBehavioursFor(final Creature performer, final Item object) {
                if (performer instanceof Player && object != null && object.getTemplateId() == CustomItems.newsletterId) {
                    return Collections.singletonList(NewsletterAction.this.actionEntry);
                }
                return null;
            }
        };
    }

    public ActionPerformer getActionPerformer() {
        return new ActionPerformer() {
            public short getActionId() {
                return NewsletterAction.actionId;
            }

            public boolean action(final Action act, final Creature performer, final Item target, final short action, final float counter) {
                return NewsletterAction.this.read(performer, target);
            }

            public boolean action(final Action act, final Creature performer, final Item source, final Item target, final short action, final float counter) {
                return this.action(act, performer, target, action, counter);
            }
        };
    }

    private boolean read(final Creature performer, final Item target) {
        if (!performer.isPlayer() || target == null || target.getTemplateId() != CustomItems.newsletterId) {
            return true;
        }
        final NewsletterQuestion aq = new NewsletterQuestion(performer, String.format("Here is the current news for, Requiem of Wurm, %s...", performer.getName()), "", performer.getWurmId());
        aq.sendQuestion();
        return true;
    }
}
