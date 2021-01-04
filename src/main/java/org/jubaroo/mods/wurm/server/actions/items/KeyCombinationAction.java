package org.jubaroo.mods.wurm.server.actions.items;

import com.wurmonline.server.Items;
import com.wurmonline.server.Server;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.ActionTypesProxy;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.deities.Deities;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemFactory;
import com.wurmonline.server.items.ItemList;
import com.wurmonline.server.players.Player;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModAction;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.communication.KeyEvent;
import org.jubaroo.mods.wurm.server.communication.discord.ChatHandler;
import org.jubaroo.mods.wurm.server.communication.discord.CustomChannel;
import org.jubaroo.mods.wurm.server.communication.discord.DiscordHandler;
import org.jubaroo.mods.wurm.server.items.CustomItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeyCombinationAction implements ModAction {
    private final short actionId;
    private final ActionEntry actionEntry;

    public KeyCombinationAction() {
        RequiemLogging.logWarning( "KeyCombinationAction()");

        actionId = (short) ModActions.getNextActionId();
        actionEntry = ActionEntry.createEntry(
                actionId,
                "Call upon the heavens",
                "intervening",
                new int[]{ActionTypesProxy.ACTION_TYPE_NOMOVE}    // 6 /* ACTION_TYPE_NOMOVE */, 48 /* ACTION_TYPE_ENEMY_ALWAYS */, 36 /* ACTION_TYPE_ALWAYS_USE_ACTIVE_ITEM */
        );
        ModActions.registerAction(actionEntry);
    }


    @Override
    public BehaviourProvider getBehaviourProvider() {
        return new BehaviourProvider() {
            // Menu with activated object
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item object) {
                return this.getBehavioursFor(performer, object);
            }

            // Menu without activated object
            @Override
            public List<ActionEntry> getBehavioursFor(Creature performer, Item object) {
                if (performer instanceof Player && object != null && object.getTemplateId() == CustomItems.keyFragmentId) {
                    return Collections.singletonList(actionEntry);
                }

                return null;
            }
        };
    }

    @Override
    public ActionPerformer getActionPerformer() {
        return new ActionPerformer() {

            @Override
            public short getActionId() {
                return actionId;
            }

            protected boolean hasEnoughFragments(Creature performer) {
                int count = 0;
                for (Item item : performer.getInventory().getItems()) {
                    if (item.getTemplateId() == CustomItems.keyFragmentId) {
                        count++;
                    }
                }
                return count >= 50;
            }

            protected void removeFragments(Creature performer) {
                int count = 0;
                ArrayList<Long> fragments = new ArrayList<>();
                for (Item item : performer.getInventory().getItems()) {
                    if (item.getTemplateId() == CustomItems.keyFragmentId && count < 50) {
                        fragments.add(item.getWurmId());
                        count++;
                    }
                }
                for (long wid : fragments) {
                    Items.destroyItem(wid);
                }
            }

            protected void broadcast(Creature performer, int speaker, String message) {
                String name;
                int r;
                int g;
                int b;
                if (speaker == 0) {
                    name = "Unknown Entity";
                    r = 0;
                    g = 255;
                    b = 255;
                } else if (speaker == Deities.DEITY_FO) {
                    name = "Fo";
                    r = 128;
                    g = 255;
                    b = 128;
                } else if (speaker == Deities.DEITY_MAGRANON) {
                    name = "Magranon";
                    r = 255;
                    g = 128;
                    b = 128;
                } else if (speaker == Deities.DEITY_VYNORA) {
                    name = "Vynora";
                    r = 192;
                    g = 192;
                    b = 255;
                } else if (speaker == Deities.DEITY_LIBILA) {
                    name = "Libila";
                    r = 192;
                    g = 192;
                    b = 192;
                } else {
                    name = Deities.getEntityName(speaker);
                    r = 192;
                    g = 192;
                    b = 192;
                }
                ChatHandler.sendToPlayersAndServers(CustomChannel.GLOBAL, name, message, performer.getWurmId(), r, g, b);
                DiscordHandler.sendToDiscord(CustomChannel.GLOBAL, String.format("<%s> %s", name, message));
            }

            // Without activated object
            @Override
            public boolean action(Action act, Creature performer, Item target, short action, float counter) {
                try {
                    if (performer instanceof Player) {
                        if (target.getTemplateId() != CustomItems.keyFragmentId) {
                            performer.getCommunicator().sendNormalServerMessage("That is not a key fragment.");
                            return true;
                        }
                        if (target.getLastOwnerId() != performer.getWurmId() && target.getOwnerId() != performer.getWurmId()) {
                            performer.getCommunicator().sendNormalServerMessage(String.format("You must own the %s to begin.", target.getName()));
                            return true;
                        }
                        Communicator comm = performer.getCommunicator();
                        if (counter == 1f) {
                            if (!hasEnoughFragments(performer)) {
                                performer.getCommunicator().sendSafeServerMessage("You must obtain enough fragments to form a full key before you begin.");
                                return true;
                            }
                            performer.getCommunicator().sendNormalServerMessage("You begin to combine the the key fragments.");
                            Server.getInstance().broadCastAction(String.format("%s begins unseal %s %s.", performer.getName(), performer.getHisHerItsString(), target.getName()), performer, 5);
                            act.setTimeLeft(3800);
                            performer.sendActionControl("Heaven intervention", true, act.getTimeLeft());
                            KeyEvent.setActive(System.currentTimeMillis(), performer);
                        } else if (act.currentSecond() == 5) {
                            comm.sendAlertServerMessage("Stand still and do not move until the process is complete.", (byte) 2);
                        } else if (act.currentSecond() == 9) {
                            comm.sendAlertServerMessage("Pay attention to GL-Freedom chat, and respond to divine inquiries there.", (byte) 2);
                        } else if (act.currentSecond() == 13) {
                            comm.sendAlertServerMessage("Answer the inquiries directly in GL-Freedom.", (byte) 2);
                        } else if (act.currentSecond() == 17) {
                            comm.sendAlertServerMessage("You have 20 to 30 seconds to answer each question. Let's begin.", (byte) 2);
                        } else if (act.currentSecond() == 20) {
                            broadcast(performer, 0, "What is this I feel? Something is happening.");
                        } else if (act.currentSecond() == 25) {
                            broadcast(performer, 0, String.format("Who is this creature? This... %s...", performer.getName()));
                        } else if (act.currentSecond() == 29) {
                            broadcast(performer, 0, "Ah, there you are. Like a speck of dust. I see you.");
                        } else if (act.currentSecond() == 33) {
                            broadcast(performer, 0, "You possess a large quantity of divine power.");
                        } else if (act.currentSecond() == 37) {
                            broadcast(performer, 0, "I'll take it. In exchange, I will grant you one wish.");
                        } else if (act.currentSecond() == 40) {
                            broadcast(performer, 0, String.format("Let's hear it. %s: What do you desire most? Power? Wealth? Recognition?", performer.getName()));
                        } else if (act.currentSecond() == 50) { // Reminder
                            if (KeyEvent.getResponse(0).equals("")) {
                                broadcast(performer, 0, "Nothing to say? I'll give you a few more seconds...");
                            }
                        } else if (act.currentSecond() == 60) {
                            String response = KeyEvent.getResponse(0);
                            if (response.equals("")) {
                                broadcast(performer, 0, "No answer. Just as I expected.");
                            } else {
                                broadcast(performer, 0, String.format("Hah! You want %s? Such a lack of imagination.", KeyEvent.getResponse(0)));
                            }
                        } else if (act.currentSecond() == 65) {
                            if (performer.getDeity() != null) {
                                broadcast(performer, performer.getDeity().getNumber(), "Master, I recognize this one.");
                            } else {
                                broadcast(performer, 1 + Server.rand.nextInt(4), "This one seems important."); // Random entity.
                            }
                        } else if (act.currentSecond() == 70) {
                            if (performer.getDeity() != null) {
                                broadcast(performer, performer.getDeity().getNumber(), String.format("%s is my champion, and possesses the fragments of the hunt.", performer.getName()));
                            } else {
                                broadcast(performer, 1 + Server.rand.nextInt(4), "They possess the fragments of the hunt.");
                            }
                        } else if (act.currentSecond() == 75) {
                            if (performer.getDeity() != null) {
                                broadcast(performer, 0, String.format("Silence, %s! I was just offering a trade, that's all.", performer.getDeity().getName()));
                            } else {
                                broadcast(performer, 0, "Silence! I was just offering a trade, that's all.");
                            }
                        } else if (act.currentSecond() == 80) {
                            if (performer.getDeity() != null) {
                                broadcast(performer, performer.getDeity().getNumber(), "Master, you know the rules. Ascension is required.");
                            } else {
                                broadcast(performer, 1 + Server.rand.nextInt(4), "Master, you know the rules. Ascension is required.");
                            }
                        } else if (act.currentSecond() == 84) {
                            broadcast(performer, 0, "Yes, yes. Rules and tradition and all that wonderful stuff. Alright. Let's begin.");
                        } else if (act.currentSecond() == 88) {
                            broadcast(performer, 0, String.format("%s! Your efforts have not gone unnoticed.", performer.getName()));
                        } else if (act.currentSecond() == 92) {
                            broadcast(performer, 0, "For your valor, I shall lift you into the heavens and bestow upon you ascension to demigod.");
                        } else if (act.currentSecond() == 95) {
                            broadcast(performer, 0, String.format("A shame, really. Now you wont get %s. Oh well.", KeyEvent.getResponse(0)));
                        } else if (act.currentSecond() == 99) {
                            broadcast(performer, 1 + Server.rand.nextInt(4), "Master...");
                        } else if (act.currentSecond() == 103) {
                            broadcast(performer, 0, "I digress. We, as the council of the heavens, shall grant you one power each.");
                        } else if (act.currentSecond() == 107) {
                            broadcast(performer, 0, "Fo. We'll start with you.");
                        } else if (act.currentSecond() == 110) {
                            broadcast(performer, Deities.DEITY_FO, String.format("Greetings, %s. I am Fo, the Silence and the Trees.", performer.getName()));
                        } else if (act.currentSecond() == 114) {
                            broadcast(performer, Deities.DEITY_FO, "For you, I grant a decision between four of my powers.");
                        } else if (act.currentSecond() == 117) {
                            broadcast(performer, Deities.DEITY_FO, KeyEvent.getFoPowers());
                        } else if (act.currentSecond() == 120) {
                            broadcast(performer, Deities.DEITY_FO, String.format("Choose, %s: Which power do you select?", performer.getName()));
                        } else if (act.currentSecond() == 140) {
                            if (KeyEvent.getResponse(1).equals("")) {
                                broadcast(performer, Deities.DEITY_FO, String.format("Are you still there? %s, you must choose.", performer.getName()));
                            }
                        } else if (act.currentSecond() == 150) {
                            if (!KeyEvent.isValidFo()) {
                                broadcast(performer, Deities.DEITY_FO, "If you refuse to choose, I will choose for you. I grant you Life Transfer.");
                                KeyEvent.foPower = "Life Transfer";
                                KeyEvent.hasWeaponEnchant = true;
                            } else {
                                broadcast(performer, Deities.DEITY_FO, String.format("I hear you, %s, and grant you the power of %s.", performer.getName(), KeyEvent.foPower));
                            }
                        } else if (act.currentSecond() == 155) {
                            broadcast(performer, 0, String.format("Your first power is %s from Fo. Next up, Magranon.", KeyEvent.foPower));
                        } else if (act.currentSecond() == 159) {
                            broadcast(performer, Deities.DEITY_MAGRANON, String.format("Greetings, %s. I am Magranon, the Fire and the Mountain.", performer.getName()));
                        } else if (act.currentSecond() == 163) {
                            broadcast(performer, Deities.DEITY_MAGRANON, String.format("I was going to offer you six powers, but you already chose %s, which is incompatible with one of mine.", KeyEvent.foPower));
                        } else if (act.currentSecond() == 167) {
                            broadcast(performer, Deities.DEITY_MAGRANON, KeyEvent.getMagranonPowers());
                        } else if (act.currentSecond() == 170) {
                            broadcast(performer, Deities.DEITY_MAGRANON, String.format("Choose, %s: Which power do you wish to wield?", performer.getName()));
                        } else if (act.currentSecond() == 190) {
                            if (KeyEvent.getResponse(2).equals("")) {
                                broadcast(performer, Deities.DEITY_MAGRANON, String.format("Are you still there? %s, you must choose.", performer.getName()));
                            }
                        } else if (act.currentSecond() == 200) {
                            if (!KeyEvent.isValidMagranon()) {
                                KeyEvent.setRandomMagranonPower();
                                broadcast(performer, Deities.DEITY_MAGRANON, String.format("If you refuse to choose, I will choose for you. I grant you %s.", KeyEvent.magranonPower));
                            } else {
                                broadcast(performer, Deities.DEITY_MAGRANON, String.format("I hear you, %s, and grant you the power of %s.", performer.getName(), KeyEvent.magranonPower));
                            }
                        } else if (act.currentSecond() == 205) {
                            broadcast(performer, 0, String.format("Your second power is %s from Magranon. Your next power shall come from Vynora.", KeyEvent.magranonPower));
                        } else if (act.currentSecond() == 209) {
                            broadcast(performer, Deities.DEITY_VYNORA, String.format("Greetings, %s. I am Vynora, the Water and the Wind.", performer.getName()));
                        } else if (act.currentSecond() == 213) {
                            broadcast(performer, Deities.DEITY_VYNORA, "I have much to offer you. Unlike my peers, I can only offer you enchant powers to choose from.");
                        } else if (act.currentSecond() == 217) {
                            broadcast(performer, Deities.DEITY_VYNORA, KeyEvent.getVynoraPowers());
                        } else if (act.currentSecond() == 220) {
                            broadcast(performer, Deities.DEITY_VYNORA, String.format("Choose, %s: Which enchant power suits your needs?", performer.getName()));
                        } else if (act.currentSecond() == 240) {
                            if (KeyEvent.getResponse(3).equals("")) {
                                broadcast(performer, Deities.DEITY_VYNORA, String.format("Are you still there? %s, you must choose.", performer.getName()));
                            }
                        } else if (act.currentSecond() == 250) {
                            if (!KeyEvent.isValidVynora()) {
                                KeyEvent.setRandomVynoraPower();
                                broadcast(performer, Deities.DEITY_VYNORA, String.format("If you refuse to choose, I will choose for you. I grant you %s.", KeyEvent.vynoraPower));
                            } else {
                                broadcast(performer, Deities.DEITY_VYNORA, String.format("I hear you, %s, and grant you the power of %s.", performer.getName(), KeyEvent.vynoraPower));
                            }
                        } else if (act.currentSecond() == 255) {
                            broadcast(performer, 0, String.format("Your third power is %s from Vynora. You have one more to choose. Libila!", KeyEvent.vynoraPower));
                        } else if (act.currentSecond() == 259) {
                            broadcast(performer, Deities.DEITY_LIBILA, String.format("Greetings, %s. I am Libila, the Hate and the Deceit.", performer.getName()));
                        } else if (act.currentSecond() == 263) {
                            broadcast(performer, Deities.DEITY_LIBILA, "My offerings are the most powerful of all, so choose wisely.");
                        } else if (act.currentSecond() == 267) {
                            broadcast(performer, Deities.DEITY_LIBILA, KeyEvent.getLibilaPowers());
                        } else if (act.currentSecond() == 270) {
                            broadcast(performer, Deities.DEITY_LIBILA, String.format("Choose, %s: What shall be your strongest power?", performer.getName()));
                        } else if (act.currentSecond() == 290) {
                            if (KeyEvent.getResponse(4).equals("")) {
                                broadcast(performer, Deities.DEITY_LIBILA, String.format("Are you still there? %s, you must choose.", performer.getName()));
                            }
                        } else if (act.currentSecond() == 300) {
                            if (!KeyEvent.isValidLibila()) {
                                KeyEvent.setRandomLibilaPower();
                                broadcast(performer, Deities.DEITY_LIBILA, "If you refuse to choose, I will choose for you. I grant you " + KeyEvent.libilaPower + ".");
                            } else {
                                broadcast(performer, Deities.DEITY_LIBILA, String.format("I hear you, %s, and grant you the power of %s.", performer.getName(), KeyEvent.libilaPower));
                            }
                        } else if (act.currentSecond() == 305) {
                            broadcast(performer, 0, "So it appears you have completed your choices.");
                        } else if (act.currentSecond() == 310) {
                            broadcast(performer, 0, String.format("Upon ascension, you will receive %s, %s, %s, and %s", KeyEvent.foPower, KeyEvent.magranonPower, KeyEvent.vynoraPower, KeyEvent.libilaPower));
                        } else if (act.currentSecond() == 314) {
                            broadcast(performer, 0, "There is one last question to answer. Which of the council shall you base your ascension on?");
                        } else if (act.currentSecond() == 318) {
                            broadcast(performer, 0, "Each comes with their own benefit. They will explain them to you.");
                        } else if (act.currentSecond() == 322) {
                            broadcast(performer, Deities.DEITY_FO, "I can grant you immunity to thorns and the wildlife shall no longer be aggressive.");
                        } else if (act.currentSecond() == 326) {
                            broadcast(performer, Deities.DEITY_MAGRANON, "I can grant you immunity to lava and increased learning from combat.");
                        } else if (act.currentSecond() == 330) {
                            broadcast(performer, Deities.DEITY_VYNORA, "I can grant you industrial aptitude.");
                        } else if (act.currentSecond() == 334) {
                            broadcast(performer, Deities.DEITY_LIBILA, "I can grant you healing and replenishment from mycelium.");
                        } else if (act.currentSecond() == 340) {
                            broadcast(performer, 0, String.format("The choice is yours, %s: Which deity shall you base your ascension on? Fo, Magranon, Vynora, or Libila?", performer.getName()));
                        } else if (act.currentSecond() == 360) {
                            if (KeyEvent.getResponse(5).equals("")) {
                                broadcast(performer, 0, String.format("Are you still there? %s, you must choose.", performer.getName()));
                            }
                        } else if (act.currentSecond() == 370) {
                            if (!KeyEvent.isValidAscendTemplate()) {
                                KeyEvent.setRandomAscendTemplate();
                                broadcast(performer, 0, "If you refuse to choose, I will choose for you. Your ascension shall be based on " + KeyEvent.ascendTemplate + ".");
                            } else {
                                broadcast(performer, 0, String.format("Your ascension shall be based on %s.", KeyEvent.ascendTemplate));
                            }
                        } else if (act.currentSecond() == 375) {
                            broadcast(performer, 0, String.format("With this council concluded, I now award you, %s, with the Key of the Heavens.", performer.getName()));
                        } else if (act.currentSecond() == 378) {
                            broadcast(performer, 0, "Use it, and claim your place among the gods.");
                        } else if (counter * 10f > performer.getCurrentAction().getTimeLeft()) {
                            if (!hasEnoughFragments(performer)) {
                                performer.getCommunicator().sendSafeServerMessage("You have lost the fragments required.");
                                return true;
                            }
                            removeFragments(performer);
                            Item key = ItemFactory.createItem(ItemList.keyHeavens, 99f, "Entity");
                            performer.getInventory().insertItem(key, true);
                            performer.getCommunicator().sendSafeServerMessage(String.format("You obtain a %s!", key.getTemplate().getName()));
                            RequiemLogging.debug(String.format("Player %s obtained %s - %s, %s, %s, %s, %s", performer.getName(), key.getName(), KeyEvent.foPower, KeyEvent.magranonPower, KeyEvent.vynoraPower, KeyEvent.libilaPower, KeyEvent.ascendTemplate));
                            return true;
                        }
                    } else {
                        RequiemLogging.debug("Somehow a non-player activated a Treasure Box...");
                    }
                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                    return true;
                }
            }

            @Override
            public boolean action(Action act, Creature performer, Item source, Item target, short action, float counter) {
                return this.action(act, performer, target, action, counter);
            }


        }; // ActionPerformer
    }
}