package com.wurmonline.server.questions;

import com.wurmonline.server.Servers;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.NoSuchActionException;
import com.wurmonline.server.creatures.Creature;
import net.coldie.tools.BmlForm;
import org.jubaroo.mods.wurm.server.actions.items.NewsletterAction;

import java.util.Properties;

import static org.jubaroo.mods.wurm.server.server.constants.ItemConstants.newsletterImage;

public class NewsletterQuestion extends Question {
    private boolean properlySent;

    NewsletterQuestion(final Creature aResponder, final String aTitle, final String aQuestion, final int aType, final long aTarget) {
        super(aResponder, aTitle, aQuestion, aType, aTarget);
        this.properlySent = false;
    }

    public NewsletterQuestion(final Creature aResponder, final String aTitle, final String aQuestion, final long aTarget) {
        super(aResponder, aTitle, aQuestion, 79, aTarget);
        this.properlySent = false;
    }

    public void answer(final Properties answer) {
        if (!this.properlySent) {
            return;
        }
    }

    public void sendQuestion() {
        boolean ok = true;
        if (this.getResponder().getPower() <= 0) {
            try {
                ok = false;
                final Action act = this.getResponder().getCurrentAction();
                if (act.getNumber() == NewsletterAction.actionId) {
                    ok = true;
                }
            } catch (NoSuchActionException act2) {
                throw new RuntimeException("No such action", act2);
            }
        }
        if (ok) {
            this.properlySent = true;
            final BmlForm f = new BmlForm("");
            f.addHidden("id", String.valueOf(this.id));
            f.addImage(String.format("%s?s=%s&i=%s", newsletterImage, Servers.localServer.getName(), Servers.localServer.EXTERNALIP), 345, 420);
            f.beginHorizontalFlow();
            f.addButton("Okay", "accept");
            f.endHorizontalFlow();
            f.addText(" \n");
            f.addText(" \n");
            this.getResponder().getCommunicator().sendBml(370, 520, true, true, f.toString(), 200, 150, 150, this.title);
        }
    }
}
