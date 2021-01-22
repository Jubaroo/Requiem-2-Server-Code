package com.wurmonline.server.questions;

import com.wurmonline.server.Items;
import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zones;
import org.gotti.wurmunlimited.modsupport.questions.ModQuestion;
import org.gotti.wurmunlimited.modsupport.questions.ModQuestions;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.misc.DecorativeKingdoms;

import java.util.Properties;

public class HeraldicQuestion implements ModQuestion {
    private final Creature responder;
    private final Item certificate, target;

    private HeraldicQuestion(Creature responder, Item certificate, Item target) {
        this.responder = responder;
        this.certificate = certificate;
        this.target = target;
    }

    public static void send(Creature player, Item certificate, Item target) {
        ModQuestions.createQuestion(player, "Heraldic Certificate", "Heraldic Certificate", MiscConstants.NOID, new HeraldicQuestion(player, certificate, target)).sendQuestion();
    }

    @Override
    public void sendQuestion(Question question) {
        final StringBuilder buf = new StringBuilder(ModQuestions.getBmlHeader(question));
        buf.append("text{text='This certificate Allows you to change the kingdom or color to whichever one you choose.'}text{text=''}");
        buf.append("text{text='Choose the kingdom or color:'}text{text=''}");

        for (DecorativeKingdoms k : DecorativeKingdoms.values()) {
            if (k.canSetKingdom(target)) {
                buf.append(String.format("radio{ group='aux'; id='%d';text='%s'%s}", k.ordinal(), k.name, k.ordinal() > 1 ? "" : ";selected='true'"));
            }
        }

        buf.append("text{text=''}");

        buf.append(ModQuestions.createAnswerButton2(question, "Confirm"));
        question.getResponder().getCommunicator().sendBml(300, 500, true, true, buf.toString(), 200, 200, 200, question.getTitle());
    }

    @Override
    public void answer(Question question, Properties answers) {
        Communicator comm = responder.getCommunicator();

        if (certificate.deleted || certificate.isTraded() || certificate.getTopParentOrNull() != responder.getInventory()) {
            comm.sendAlertServerMessage("The certificate must be in your inventory to use.");
            return;
        }

        if (((target.canHavePermissions() && target.mayManage(responder)) || target.lastOwner == responder.getWurmId() || target.getTopParentOrNull() == responder.getInventory())) {
            int aux = -1;
            try {
                aux = Integer.parseInt(answers.getProperty("aux"));
            } catch (Exception ignored) {
            }

            if (DecorativeKingdoms.canSetKingdom(target, (byte) aux)) {
                try {
                    DecorativeKingdoms.setKingdom(target, (byte) aux);
                    Items.destroyItem(certificate.getWurmId());
                    comm.sendNormalServerMessage(String.format("%s will now proudly carry the colors of %s.", target.getName(), DecorativeKingdoms.getName((byte) aux)));
                    if (target.getParentId() == MiscConstants.NOID) {
                        VolaTile tile = Zones.getTileOrNull(target.getTilePos(), target.isOnSurface());
                        if (tile != null) {
                            tile.makeInvisible(target);
                            tile.makeVisible(target);
                        }
                    } else {
                        comm.sendUpdateInventoryItem(target);
                    }
                } catch (Exception e) {
                    RequiemLogging.logException("Heraldic certificate error", e);
                    comm.sendAlertServerMessage("Something went wrong. Try again or notify staff using /support.");
                }
            } else {
                comm.sendAlertServerMessage("Invalid selection.");
            }
        } else {
            comm.sendAlertServerMessage(String.format("You are not allowed to change the %s.", target.getName()));
        }
    }
}
