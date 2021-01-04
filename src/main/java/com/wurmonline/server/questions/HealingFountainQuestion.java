package com.wurmonline.server.questions;

import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.NoSuchActionException;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import org.jubaroo.mods.wurm.server.actions.items.HealingFountainAction;

import java.util.Properties;

public class HealingFountainQuestion extends Question {
    private boolean properlySent;
    private Item item;

    HealingFountainQuestion(final Creature aResponder, final String aTitle, final String aQuestion, final int aType, final long aTarget) {
        super(aResponder, aTitle, aQuestion, aType, aTarget);
        this.properlySent = false;
    }

    public HealingFountainQuestion(final Creature aResponder, final String aTitle, final String aQuestion, final long aTarget) {
        super(aResponder, aTitle, aQuestion, 79, aTarget);
        this.properlySent = false;
    }

    public void answer(final Properties answers) {
        if (!this.properlySent) {
            return;
        }

    }

    public void sendQuestion() {
        boolean ok = true;
        try {
            ok = false;
            final Action act = this.getResponder().getCurrentAction();
            if (act.getNumber() == HealingFountainAction.getActId()) {
                ok = true;
            }
        } catch (NoSuchActionException act2) {
            throw new RuntimeException("No such action", act2);
        }
        if (ok) {
            this.properlySent = true;
            String buf = this.getBmlHeader() +
                    "text{text='Do you want to go to sleep? You will log off Wurm.'}text{text=''}" +
                    "radio{ group='sleep'; id='true';text='Yes';selected='true'}" +
                    "radio{ group='sleep'; id='false';text='No'}" +
                    this.createAnswerButton2();
            this.getResponder().getCommunicator().sendBml(300, 300, true, true, buf, 200, 200, 200, this.title);
        }
    }
}
