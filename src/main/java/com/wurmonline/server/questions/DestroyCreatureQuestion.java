package com.wurmonline.server.questions;

import com.wurmonline.server.NoSuchPlayerException;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.MethodsCreatures;
import com.wurmonline.server.behaviours.NoSuchActionException;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.NoSuchCreatureException;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.actions.KillCreatureAction;

import java.util.Properties;

public class DestroyCreatureQuestion extends Question {
    private boolean properlySent;

    DestroyCreatureQuestion(final Creature aResponder, final String aTitle, final String aQuestion, final int aType, final long aTarget) {
        super(aResponder, aTitle, aQuestion, aType, aTarget);
        this.properlySent = false;
    }

    public DestroyCreatureQuestion(final Creature aResponder, final String aTitle, final String aQuestion, final long aTarget) {
        super(aResponder, aTitle, aQuestion, LOCATEQUESTION, aTarget);
        this.properlySent = false;
    }

    public void answer(final Properties answers) {
        if (!this.properlySent) {
            return;
        }
        final String val = answers.getProperty("creatureid");
        try {
            final long creatureid = Long.parseLong(val);
            final Creature c = Server.getInstance().getCreature(creatureid);
            if (c != null) {
                this.getResponder().getCommunicator().sendNormalServerMessage(String.format("You destroyed Creature: %s", c.getName()));
                MethodsCreatures.destroyCreature(c);
            }
        } catch (NoSuchPlayerException | NoSuchCreatureException | NumberFormatException ex2) {
            this.getResponder().getCommunicator().sendNormalServerMessage("That Id doesn't exist or is just messed up, get it right!");
            RequiemLogging.logException("[ERROR] in answer in DestroyCreatureQuestion", ex2);
        }
    }

    public void sendQuestion() {
        boolean ok = true;
        try {
            ok = false;
            final Action act = this.getResponder().getCurrentAction();
            if (act.getNumber() == KillCreatureAction.actionId) {
                ok = true;
            }
        } catch (NoSuchActionException act2) {
            throw new RuntimeException("No such action", act2);
        }
        if (ok) {
            this.properlySent = true;
            String buf = this.getBmlHeader() +
                    "text{text='Make sure you get the correct creature ID.'}text{text=''}" +
                    "label{text='creature id'};input{id='creatureid';maxchars=\"32\";text=\"\"};" +
                    this.createAnswerButton2();
            this.getResponder().getCommunicator().sendBml(300, 300, true, true, buf, 200, 200, 200, this.title);
        }
    }
}
