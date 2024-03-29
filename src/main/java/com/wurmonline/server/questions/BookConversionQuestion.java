package com.wurmonline.server.questions;

import com.wurmonline.server.Items;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.deities.Deities;
import com.wurmonline.server.deities.Deity;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;
import net.coldie.tools.BmlForm;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class BookConversionQuestion extends Question {
    public static HashMap<Integer, Integer> deityMap = new HashMap<>();
    protected Item convertBook;

    public BookConversionQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget, Item book) {
        super(aResponder, aTitle, aQuestion, LOCATEQUESTION, aTarget);
        this.convertBook = book;
    }

    public static String getDeityNames() {
        String builder = "";
        int i = 0;
        Deity[] deities = Deities.getDeities();
        while (i < deities.length) {
            builder = builder + deities[i].getName();
            deityMap.put(i, deities[i].getNumber());
            i++;
            if (i < deities.length) {
                builder = builder + ",";
            }
        }
        return builder;
    }

    @Override
    public void answer(Properties answer) {
        boolean accepted = answer.containsKey("accept") && answer.get("accept") == "true";
        if (accepted) {
            RequiemLogging.logInfo("Accepted BookOfConversion");
            int entry = Integer.parseInt(answer.getProperty("deity"));
            int deity = deityMap.get(entry);
            if (convertBook == null || convertBook.getOwnerId() != this.getResponder().getWurmId()) {
                this.getResponder().getCommunicator().sendNormalServerMessage("You must own a book of conversion to begin changing faith.");
            } else {
                if (this.getResponder() instanceof Player) {
                    try {
                        Player p = (Player) this.getResponder();
                        RequiemLogging.logInfo(String.format("Converting %s to %s", p.getName(), Deities.getDeityName(deity)));
                        Items.destroyItem(convertBook.getWurmId());
                        Deity d = Deities.getDeity(deity);
                        p.setDeity(d);
                        p.setPriest(true);
                        if (d.isHateGod()) {
                            p.setAlignment(-Math.abs(p.getAlignment()));
                        } else {
                            p.setAlignment(Math.abs(p.getAlignment()));
                        }
                        p.setFaith(p.getFaith() * 0.9f);
                        p.getCommunicator().sendAlertServerMessage(String.format("%s accepts your conversion.", Deities.getDeityName(deity)));
                    } catch (IOException e) {
                        RequiemLogging.logException("[Error] in answer in BookConversionQuestion", e);
                    }
                } else {
                    RequiemLogging.logWarning(String.format("Non-player used a %s?", convertBook.getName()));
                }
            }
        }
    }

    @Override
    public void sendQuestion() {
        if (convertBook == null || convertBook.getOwnerId() != this.getResponder().getWurmId()) {
            this.getResponder().getCommunicator().sendNormalServerMessage("You must own a book of conversion to begin changing faith.");
            return;
        }
        BmlForm f = new BmlForm("");
        f.addHidden("id", String.valueOf(this.id));
        f.addBoldText("Select the deity you would like to convert to\nAccepting this will reduce your faith by 10 percent\n", new String[0]);
        f.addRaw("harray{label{text='Select New Deity:'}dropdown{id='deity';options='");
        f.addRaw(getDeityNames());
        f.addRaw("'}}");
        f.addText("\n\n", new String[0]);
        f.beginHorizontalFlow();
        f.addButton("Accept", "accept");
        f.endHorizontalFlow();
        this.getResponder().getCommunicator().sendBml(400, 300, true, true, f.toString(), 255, 255, 255, this.title);
    }
}
