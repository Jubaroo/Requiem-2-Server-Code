package com.wurmonline.server.questions;

import com.wurmonline.server.Items;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.NoSuchPlayerException;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.NoSuchCreatureException;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zones;
import org.gotti.wurmunlimited.modsupport.questions.ModQuestion;
import org.gotti.wurmunlimited.modsupport.questions.ModQuestions;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

public class RenameQuestion implements ModQuestion {
    private final Creature performer;
    private final Creature renameTarget;
    private final Item subject;
    private static Logger logger;


    private RenameQuestion(Creature performer, Creature renameTarget, Item subject) {
        this.performer = performer;
        this.renameTarget = renameTarget;
        this.subject = subject;
        logger = Logger.getLogger(this.getClass().getName());
    }

    @Override
    public void sendQuestion(Question question) {
        final StringBuilder buf = new StringBuilder(ModQuestions.getBmlHeader(question));
        buf.append("harray{input{text='").append(renameTarget.getName()).append("'; id='data1'; maxchars='40'}}");
        buf.append("text{text=''}");
        if (renameTarget.getSex() == 0) {
            buf.append("radio{ group='sex'; id='sexmale';text='Male';selected='true'}");
            buf.append("radio{ group='sex'; id='sexfemale';text='Female'}");
        } else {
            buf.append("radio{ group='sex'; id='sexmale';text='Male'}");
            buf.append("radio{ group='sex'; id='sexfemale';text='Female';selected='true'}");
        }
        buf.append("text{text=''}");
        buf.append(ModQuestions.createAnswerButton2(question, "Confirm"));

        question.getResponder().getCommunicator().sendBml(300, 200, false, true, buf.toString(), 200, 200, 200, question.getTitle());
    }

    @Override
    public void answer(Question question, Properties answer) {
        String renameText;
        String renameText2;
        String guardGender;
        renameText = answer.getProperty("data1");
        renameText2 = answer.getProperty("data1");
        guardGender = answer.getProperty("sex");
        int tXPos;
        int tYPos;
        boolean toDestroy = true;
        VolaTile tile = Zones.getTileOrNull(renameTarget.getTilePos(), renameTarget.isOnSurface());

        renameText = renameText + "-x"; // Modloader doesn't like null variables being passed around. This is to ensure renameText is never null... I hope
        tXPos = (int) renameTarget.getPosX() / 4;
        tYPos = (int) renameTarget.getPosY() / 4;

        if (subject.getTemplateId() == ItemList.wandDeity || subject.getTemplateId() == ItemList.wandGM) {
            toDestroy = false;
        } // Don't want to destroy GM wands!

        if (guardGender.equals("sexmale") && renameTarget.getSex() == MiscConstants.SEX_FEMALE) {
            renameTarget.setSex(MiscConstants.SEX_MALE, false);

        } else if (guardGender.equals("sexfemale") && renameTarget.getSex() == MiscConstants.SEX_MALE) {
            renameTarget.setSex(MiscConstants.SEX_MALE, false);
        }

        if (Objects.equals(renameText, "-x")) {
            renameText = renameTarget.getTemplate().getName();
            if (renameTarget.isAnimal() || renameTarget.isMonster() || !renameTarget.isUnique() || !renameTarget.isNpc()) {
                renameText = "Spot";
            }
            performer.getCommunicator().sendNormalServerMessage("The nickname field is blank. Setting name to " + renameText);
            renameTarget.setName(renameText);
            renameTarget.blinkTo(tXPos, tYPos, renameTarget.getLayer(), renameTarget.getFloorLevel());
            if (toDestroy) {
                Items.destroyItem(subject.getWurmId());
            }
            return;
        }

        if (!containsIllegalCharacters(renameText2, performer)) {
            renameTarget.setName(renameText2);
            // There's probably a better method to force the client to update the NPC's name. But I can't find it.
            //renameTarget.blinkTo(tXPos,tYPos,renameTarget.getLayer(),renameTarget.getFloorLevel());
            try {
                if (tile != null) {
                    tile.makeInvisible(renameTarget);
                    tile.makeVisible(renameTarget);
                }
            } catch (NoSuchCreatureException | NoSuchPlayerException e) {
                RequiemLogging.logException("[Error] in answer in RenameQuestion", e);
            }
            if (toDestroy) {
                Items.destroyItem(subject.getWurmId());
            }
        }
    }

    public static void send(Creature performer, Creature renameTarget, Item subject) {
        ModQuestions.createQuestion(performer, "Rename Creature", "What name shall you give this creature?", MiscConstants.NOID, new RenameQuestion(performer, renameTarget, subject)).sendQuestion();
    }

    private boolean containsIllegalCharacters(String text, Creature performer) {
        char[] chars = text.toCharArray();
        boolean toReturn = false;

        if (text.length() > 40) {
            performer.getCommunicator().sendNormalServerMessage("The nickname must be less than 40 characters.");
            return true;
        }

        if (Character.isWhitespace(text.charAt(0))) {
            performer.getCommunicator().sendNormalServerMessage("The nickname must not start with a space.");
            return true;
        }

        for (char aChar : chars) {
            if ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890 ".indexOf(aChar) < 0)
                if (!toReturn) {
                    performer.getCommunicator().sendNormalServerMessage("The nickname may only contain uppercase letters, lowercase letters, numbers and spaces.");
                    toReturn = true;
                }
        }

        return toReturn;
    }
}