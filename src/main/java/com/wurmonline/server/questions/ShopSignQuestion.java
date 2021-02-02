package com.wurmonline.server.questions;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zones;
import org.gotti.wurmunlimited.modsupport.questions.ModQuestion;
import org.gotti.wurmunlimited.modsupport.questions.ModQuestions;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.misc.ShopSigns;

import java.util.Properties;

public class ShopSignQuestion implements ModQuestion {
    private final Creature responder;
    private final Item target;

    private ShopSignQuestion(Creature responder, Item target) {
        this.responder = responder;
        this.target = target;
    }

    public static void send(Creature player, Item target) {
        ModQuestions.createQuestion(player, "Shop Sign designs", "Shop Sign designs", MiscConstants.NOID, new ShopSignQuestion(player, target)).sendQuestion();
    }

    @Override
    public void sendQuestion(Question question) {
        final StringBuilder buf = new StringBuilder(ModQuestions.getBmlHeader(question));
        buf.append("text{text='Change the design to whichever one you choose.'}text{text=''}");
        buf.append("text{text='Choose your design:'}text{text=''}");

        for (ShopSigns k : ShopSigns.values()) {
            if (k.canSetDesign(target)) {
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

        if (target.deleted || target.isTraded()) {
            comm.sendAlertServerMessage("Something went wrong or the item is being traded. Try again or notify staff using /support.");
            return;
        }

        if (((target.canHavePermissions() && target.mayManage(responder)) || target.lastOwner == responder.getWurmId())) {
            int aux = -1;
            try {
                aux = Integer.parseInt(answers.getProperty("aux"));
            } catch (Exception ignored) {
            }

            if (ShopSigns.canSetDesign(target, (byte) aux)) {
                try {
                    ShopSigns.setDesign(target, (byte) aux);
                    comm.sendNormalServerMessage(String.format("%s will now display the design: %s.", target.getName(), ShopSigns.getName((byte) aux)));
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
                    RequiemLogging.logException("[ERROR] in shop sign question", e);
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
