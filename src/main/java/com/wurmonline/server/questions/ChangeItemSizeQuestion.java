package com.wurmonline.server.questions;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zones;
import org.gotti.wurmunlimited.modsupport.questions.ModQuestion;
import org.gotti.wurmunlimited.modsupport.questions.ModQuestions;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.tools.Hooks;

import java.util.Properties;

public class ChangeItemSizeQuestion implements ModQuestion {
    private final Creature performer;
    private final Item resizeTarget;

    private ChangeItemSizeQuestion(Creature performer, Item resizeTarget) {
        this.performer = performer;
        this.resizeTarget = resizeTarget;
    }

    @Override
    public void sendQuestion(Question question) {
        String buf = ModQuestions.getBmlHeader(question) + "harray{input{text='Multiplier to resize the item'; id='size'; maxchars='2'; text=''}}" +
                "text{text=''}" +
                "text{text=''}" +
                ModQuestions.createAnswerButton2(question, "Confirm");
        question.getResponder().getCommunicator().sendBml(300, 200, false, true, buf, 200, 200, 200, question.getTitle());
    }

    @Override
    public void answer(Question question, Properties answer) {
        String newSize = answer.getProperty("size");

        if (newSize.isEmpty()) {
            performer.getCommunicator().sendNormalServerMessage("The item size field is blank! Nothing changed.");
            return;
        }

        if (!containsIllegalCharacters(newSize, performer)) {
            try {
                Hooks.resizeItemId = resizeTarget.getTemplate().getTemplateId();
                Hooks.resizeItemSize = Float.parseFloat(answer.getProperty("size"));
                VolaTile tile = Zones.getTileOrNull(resizeTarget.getTilePos(), resizeTarget.isOnSurface());
                if (tile != null) {
                    tile.makeInvisible(resizeTarget);
                    tile.makeVisible(resizeTarget);
                }
            } catch (Throwable e) {
                RequiemLogging.logException("[ERROR] in answer in ChangeItemSizeQuestion", e);
            }
        }
    }

    public static void send(Creature performer, Item renameTarget) {
        ModQuestions.createQuestion(performer, "Change Item Size", "What size are we going to change to?", MiscConstants.NOID, new ChangeItemSizeQuestion(performer, renameTarget)).sendQuestion();
    }

    private boolean containsIllegalCharacters(String text, Creature performer) {
        if (Character.isWhitespace(text.charAt(0))) {
            performer.getCommunicator().sendNormalServerMessage("The new item size must not start with a space.");
            return true;
        }

        return false;
    }
}