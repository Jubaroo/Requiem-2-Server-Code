package com.wurmonline.server.questions;

import com.wurmonline.server.Items;
import com.wurmonline.server.NoSuchItemException;
import com.wurmonline.server.Players;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.NoSuchActionException;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.actions.TentSleepAction;

import java.util.Properties;

public class TentSleepQuestion extends Question {
    private boolean properlySent;
    private Item item;

    TentSleepQuestion(final Creature aResponder, final String aTitle, final String aQuestion, final int aType, final long aTarget) {
        super(aResponder, aTitle, aQuestion, aType, aTarget);
        this.properlySent = false;
    }

    public TentSleepQuestion(final Creature aResponder, final String aTitle, final String aQuestion, final long aTarget) {
        super(aResponder, aTitle, aQuestion, LOCATEQUESTION, aTarget);
        this.properlySent = false;
    }

    public void answer(final Properties answers) {
        if (!this.properlySent) {
            return;
        }
        try {
            this.item = Items.getItem(this.target);
        } catch (NoSuchItemException e) {
            RequiemLogging.logException("[ERROR] in TentSleepQuestion answer", e);
        }
        final String val = answers.getProperty("sleep");
        if (val != null && val.equals("true")) {
            try {
                this.getResponder().getCurrentAction();
                this.getResponder().getCommunicator().sendNormalServerMessage("You are too busy to sleep right now.");
            } catch (NoSuchActionException nsa) {
                this.getResponder().getCommunicator().sendShutDown("You went to sleep. Sweet dreams.", true);
                ((Player) this.getResponder()).getSaveFile().setBed(this.item.getWurmId());
                Server.getInstance().broadCastAction(String.format("%s goes to sleep in %s.", this.getResponder().getName(), this.item.getNameWithGenus()), this.getResponder(), 5);
                if (this.getResponder().hasLink()) {
                    this.getResponder().setSecondsToLogout(2);
                } else {
                    Players.getInstance().logoutPlayer((Player) this.getResponder());
                }
            }
        } else {
            this.getResponder().getCommunicator().sendNormalServerMessage("You decide not to go to sleep right now.");
        }
    }

    public void sendQuestion() {
        boolean ok = true;
        try {
            ok = false;
            final Action act = this.getResponder().getCurrentAction();
            if (act.getNumber() == TentSleepAction.actionId) {
                ok = true;
            }
        } catch (NoSuchActionException act2) {
            throw new RuntimeException("No such action", act2);
        }
        if (ok) {
            this.properlySent = true;
            String buf = String.format("%stext{text='Do you want to go to sleep? You will log off Wurm.'}text{text=''}radio{ group='sleep'; id='true';text='Yes';selected='true'}radio{ group='sleep'; id='false';text='No'}%s", this.getBmlHeader(), this.createAnswerButton2());
            this.getResponder().getCommunicator().sendBml(300, 300, true, true, buf, 200, 200, 200, this.title);
        }
    }
}
