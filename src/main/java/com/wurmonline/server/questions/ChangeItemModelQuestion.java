package com.wurmonline.server.questions;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemTemplate;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modsupport.questions.ModQuestion;
import org.gotti.wurmunlimited.modsupport.questions.ModQuestions;

import java.util.Properties;

//TODO insert your own code at the start of Item.getModelName, check for your specific item and return the exact mapping you want without adding material or anything else to it

public class ChangeItemModelQuestion implements ModQuestion {
    private final Creature performer;
    private final Item renameTarget;

    private ChangeItemModelQuestion(Creature performer, Item renameTarget) {
        this.performer = performer;
        this.renameTarget = renameTarget;
    }

    @Override
    public void sendQuestion(Question question) {
        String buf = ModQuestions.getBmlHeader(question) + "harray{input{text='" + renameTarget.getModelName() + "'; id='newModel'; text=''}}" +
                "text{text=''}" +
                "text{text=''}" +
                ModQuestions.createAnswerButton2(question, "Confirm");
        question.getResponder().getCommunicator().sendBml(300, 200, false, true, buf, 200, 200, 200, question.getTitle());
    }

    @Override
    public void answer(Question question, Properties answer) {
        String renameText = answer.getProperty("newModel");

        if (renameText.isEmpty()) {
            performer.getCommunicator().sendNormalServerMessage("The model name field is blank! Nothing changed.");
            return;
        }

        if (!containsIllegalCharacters(renameText, performer)) {
            try {
                ReflectionUtil.setPrivateField(renameTarget.getTemplate(), ReflectionUtil.getField(ItemTemplate.class, "modelName"), renameText);
                renameTarget.updateModelNameOnGroundItem();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void send(Creature performer, Item renameTarget) {
        ModQuestions.createQuestion(performer, "Change Model", "What model are we going to change to?", MiscConstants.NOID, new ChangeItemModelQuestion(performer, renameTarget)).sendQuestion();
    }

    private boolean containsIllegalCharacters(String text, Creature performer) {
        if (Character.isWhitespace(text.charAt(0))) {
            performer.getCommunicator().sendNormalServerMessage("The model name must not start with a space.");
            return true;
        }

        return false;
    }
}