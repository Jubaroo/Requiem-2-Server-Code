package com.wurmonline.server.questions;

import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.ItemTemplateFactory;
import com.wurmonline.server.items.NoSuchTemplateException;
import com.wurmonline.server.players.Abilities;
import org.gotti.wurmunlimited.modsupport.questions.ModQuestion;
import org.gotti.wurmunlimited.modsupport.questions.ModQuestions;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import static com.wurmonline.server.MiscConstants.*;

public class SorceryTitleQuestion implements ModQuestion {
    private Creature responder;

    public SorceryTitleQuestion(Creature responder) {
        this.responder = responder;
    }

    private static boolean isBlack(int ability) {
        switch (ability) {
            case ABILITY_WITCH:
            case ABILITY_HAG:
            case ABILITY_CRONE:
            case ABILITY_INQUISITOR:
            case ABILITY_NECROMANCER:
            case ABILITY_OCCULTIST: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    private static boolean isRed(int ability) {
        switch (ability) {
            case ABILITY_NORN:
            case ABILITY_FORTUNETELLER:
            case ABILITY_MESMERIZER:
            case ABILITY_EVOCATOR:
            case ABILITY_CONJURER:
            case ABILITY_SORCEROR:
            case ABILITY_SORCERESS:
            case ABILITY_INCINERATOR: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    private static boolean isOther(int ability) {
        switch (ability) {
            case ABILITY_SOOTHSAYER:
            case ABILITY_MEDIUM:
            case ABILITY_SIREN:
            case ABILITY_DIVINER:
            case ABILITY_WITCHHUNTER:
            case ABILITY_SUMMONER:
            case ABILITY_SPELLBINDER:
            case ABILITY_ILLUSIONIST:
            case ABILITY_ENCHANTER:
            case ABILITY_DRUID:
            case ABILITY_WORGMASTER:
            case ABILITY_VALKYRIE:
            case ABILITY_BERSERKER: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    private static boolean isMayorBlack(int black, int red, int rest) {
        return black > red && black > rest;
    }

    private static boolean isMayorRed(int black, int red, int rest) {
        return red > black && red > rest;
    }

    private static boolean isMayorRest(int black, int red, int rest) {
        return rest > black && rest > red;
    }


    public static int getComboTitle(Creature performer) {
        int black = 0;
        int red = 0;
        int rest = 0;
        int nums = 0;
        int lastAbility = 0;
        for (int x = 0; x <= ABILITY_CURRENT_MAX; ++x) {
            if (performer.hasAbility(x)) {
                ++nums;
                if (isBlack(x)) {
                    ++black;
                } else if (isRed(x)) {
                    ++red;
                } else if (isOther(x)) {
                    ++rest;
                }
                lastAbility = x;
            }
        }
        if (nums <= 1) {
            return lastAbility;
        }
        boolean isBlack = false;
        boolean isRed = false;
        boolean isOther = false;
        if (isMayorBlack(black, red, rest)) {
            isBlack = true;
        } else if (isMayorRed(black, red, rest)) {
            isRed = true;
        } else if (isMayorRest(black, red, rest)) {
            isOther = true;
        }
        if (nums <= 5 && performer.hasAbility(39)) {
            return 39;
        }
        if (nums > 9 && performer.hasAbility(39)) {
            return 40;
        }
        if (nums >= 9) {
            return 26;
        }
        if (nums >= 6) {
            if (isBlack) {
                return 38;
            }
            return 23;
        } else if (isBlack) {
            if (performer.getSex() == 1) {
                if (black == 2) {
                    return 4;
                }
                if (black == 3) {
                    return 36;
                }
            } else {
                if (black == 2) {
                    return 14;
                }
                if (black == 3) {
                    return 17;
                }
            }
            if (nums == 4) {
                return 18;
            }
            return 38;
        } else if (isRed) {
            if (red == 2) {
                return 21;
            }
            if (red == 3) {
                return 22;
            }
            return 25;
        } else if (isOther) {
            if (rest == 2) {
                if (performer.getSex() == 1) {
                    return 5;
                }
                return 28;
            } else {
                if (rest == 3) {
                    return 37;
                }
                return 37;
            }
        } else {
            if (nums > 3) {
                return 19;
            }
            return performer.getAbilityTitleVal();
        }
    }

    @Override
    public void sendQuestion(Question question) {
        StringBuilder buf = new StringBuilder(ModQuestions.getBmlHeader(question));

        buf.append("text{text=''}");

        boolean hasTitles = false;

        for (int i = 0; i < 64; i++) {
            String ab = Abilities.getAbilityString(i);
            if (ab != null && ab.length() > 0 && responder.hasAbility(i)) {
                hasTitles = true;
                buf.append(String.format("radio{ group='title'; id='%d';text='%s'}", i, ab));
            }
        }

        if (!hasTitles) {
            buf.append("text{text='You have no sorcery titles available.'}");
        }

        int nab = getComboTitle(responder);
        if (nab > 0)
            buf.append(String.format("radio{ group='title'; id='%d';text='%s'}", nab, Abilities.getAbilityString(nab)));

        List<String> missing = new LinkedList<>();

        for (int i = 795; i <= 810; i++) {
            int a = Abilities.getAbilityForItem(i, responder);
            if (a > 0 && !responder.hasAbility(a)) {
                try {
                    missing.add(ItemTemplateFactory.getInstance().getTemplate(i).getName());
                } catch (NoSuchTemplateException e) {
                    RequiemLogging.logException("No template for sorcery item " + i, e);
                }
            }
        }

        buf.append("text{text=''}");

        if (missing.size() > 0) {
            buf.append(String.format("text{text='Missing tomes: %s'}", String.join(", ", missing)));
        } else {
            buf.append("text{text='You got all the tomes, good job!'}");
        }

        buf.append("text{text=''}");

        buf.append(ModQuestions.createAnswerButton2(question, "Confirm"));
        question.getResponder().getCommunicator().sendBml(400, 500, true, true, buf.toString(), 200, 200, 200, question.getTitle());
    }

    @Override
    public void answer(Question question, Properties answers) {
        Communicator comm = responder.getCommunicator();

        int newTitle = -1;

        try {
            newTitle = Integer.parseInt(answers.getProperty("title"));
        } catch (Exception ignored) {

        }

        int nab = getComboTitle(responder);

        if (newTitle >= 0 && (responder.hasAbility(newTitle) || newTitle == nab)) {
            responder.setAbilityTitle(newTitle);
        } else {
            comm.sendAlertServerMessage("Invalid selection.");
        }
    }

    public static void send(Creature player) {
        ModQuestions.createQuestion(player, "Title selection", "Select sorcery title:", -10, new SorceryTitleQuestion(player)).sendQuestion();
    }
}