package com.wurmonline.server.questions;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.ServerEntry;
import com.wurmonline.server.Servers;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.kingdom.Kingdoms;
import com.wurmonline.server.players.Player;
import com.wurmonline.server.players.Titles;
import com.wurmonline.server.utils.BMLBuilder;
import org.gotti.wurmunlimited.modsupport.questions.ModQuestion;
import org.gotti.wurmunlimited.modsupport.questions.ModQuestions;
import org.jubaroo.mods.wurm.server.RequiemLogging;
import org.jubaroo.mods.wurm.server.utils.TransferUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class NewPlayerQuestion implements ModQuestion {
    private Player responder;

    public NewPlayerQuestion(Player responder) {
        this.responder = responder;
    }

    @Override
    public void answer(Question question, Properties answers) {
        boolean ok = false;
        try {
            boolean male = Boolean.parseBoolean(answers.getProperty("male"));
            boolean skip = Boolean.parseBoolean(answers.getProperty("skip"));
            int serverId = Integer.parseInt(answers.getProperty("server"));
            Optional<ServerEntry> server = Arrays.stream(Servers.getAllServers()).filter(e -> e.id == serverId).findFirst();
            if (server.isPresent()) {
                responder.loadSkills();
                responder.getBody().createBodyParts();
                responder.createPossessions();
                responder.getStatus().setStatusExists(true);

                if (!male) responder.setSex((byte) 1, false);

                responder.createSomeItems(1.0f, false);

                responder.setFullyLoaded();
                responder.getSaveFile().setLogin();

                responder.save();

                responder.getCommunicator().setReady(true);

                if (skip) {
                    responder.getCommunicator().sendOpenWindow((short) 9, false);
                    responder.getCommunicator().sendOpenWindow((short) 5, false);
                    responder.getCommunicator().sendOpenWindow((short) 1, false);
                    responder.getCommunicator().sendOpenWindow((short) 3, false);
                    responder.getCommunicator().sendOpenWindow((short) 11, false);
                    responder.getCommunicator().sendOpenWindow((short) 4, false);
                    responder.getCommunicator().sendOpenWindow((short) 6, false);
                    responder.getCommunicator().sendOpenWindow((short) 7, false);
                    responder.getCommunicator().sendOpenWindow((short) 2, false);
                    responder.getCommunicator().sendOpenWindow((short) 12, false);
                    responder.getCommunicator().sendOpenWindow((short) 13, false);
                    responder.getCommunicator().sendOpenWindow((short) 41, false);
                    responder.getCommunicator().sendToggleAllQuickbarBtns(true);
                    responder.addTitle(Titles.Title.Educated);
                }

                TransferUtils.sendPlayerToServer(responder, server.get(), null, (byte) -1);

                RequiemLogging.logInfo(String.format("Created player %s (%d) as %s and sent to %s skip=%s", responder.getName(), responder.getWurmId(), male ? "male" : "female", server.get().name, skip));
Servers.getServerWithId(1);
                ok = true;
            }
        } catch (Exception e) {
            RequiemLogging.logException(String.format("Error in character creation for %s", responder.toString()), e);
        }

        if (!ok) NewPlayerQuestion.send(responder);
    }

    private BMLBuilder createServerSelection() {
        BMLBuilder bml = BMLBuilder.createGenericBuilder();
        AtomicInteger num = new AtomicInteger();
        getServers(responder)
                .sorted(Comparator.<ServerEntry, Integer>comparing(i -> -i.currentPlayers).thenComparing(i -> -i.KINGDOM))
                .forEach(e -> bml.addRadioButton(
                        Integer.toString(e.id), "server",
                        String.format("%s - %s %s (%d player%s online)", e.getName(), Kingdoms.getKingdom(e.KINGDOM).getChatName(), e.PVPSERVER ? "PVP" : "PVE", e.currentPlayers, e.currentPlayers == 1 ? "" : "s"),
                        null, num.incrementAndGet() == 1, true, false));
        return bml;
    }

    @Override
    public void sendQuestion(Question question) {
        responder.getCommunicator().sendCloseWindow((short) 9);
        responder.getCommunicator().sendCloseWindow((short) 5);
        responder.getCommunicator().sendCloseWindow((short) 1);
        responder.getCommunicator().sendCloseWindow((short) 3);
        responder.getCommunicator().sendCloseWindow((short) 16);
        responder.getCommunicator().sendCloseWindow((short) 20);
        responder.getCommunicator().sendCloseWindow((short) 26);
        responder.getCommunicator().sendCloseWindow((short) 11);
        responder.getCommunicator().sendCloseWindow((short) 4);
        responder.getCommunicator().sendCloseWindow((short) 41);
        responder.getCommunicator().sendCloseWindow((short) 6);
        responder.getCommunicator().sendCloseWindow((short) 7);
        responder.getCommunicator().sendCloseWindow((short) 2);
        responder.getCommunicator().sendCloseWindow((short) 12);
        responder.getCommunicator().sendCloseWindow((short) 13);
        responder.getCommunicator().sendToggleAllQuickbarBtns(false);

        BMLBuilder bml = BMLBuilder.createNormalWindow(String.valueOf(question.getId()), question.getQuestion(),
                BMLBuilder.createGenericBuilder()
                        .addText("")
                        .addText("Select your character's gender:", null, BMLBuilder.TextType.BOLD, null)
                        .addRadioButton("true", "male", "Male", null, true, true, false)
                        .addRadioButton("false", "male", "Female", null, false, true, false)
                        .addText("")
                        .addText("Select your starting server:", null, BMLBuilder.TextType.BOLD, null)
                        .addString(createServerSelection().toString())
                        .addText("")
                        .addText("Tranquil Garden is recommended for new players wanting a peaceful time.")
                        .addText("Cragmoor Isles is recommended for experienced players.")
                        .addText("The Wilds is recommended for players wanting a fighting challenge.")
                        .addText("")
                        .addText("The selection is not permanent, you will be able to freely move between servers using portals. So don't worry about choosing wrong!")
                        .addText("")
                        .addCheckbox("skip", "Skip the tutorial", "Are you sure?", "If you're new to Wurm it's highly recommended to NOT skip the tutorial!", null, null, null, false, true, null)
                        .addText("")
                        .addString(BMLBuilder.createCenteredNode(BMLBuilder.createHorizArrayNode(false)
                                .addButton("send", "Create", 100, 20, true)
                        ).toString())
        );
        responder.getCommunicator().sendBml(300, 370, true, false, bml.toString(), 200, 200, 200, question.getTitle());
    }

    private static Stream<ServerEntry> getServers(Player player) {
        return Arrays.stream(Servers.getAllServers())
                .filter(server -> !server.entryServer && server != Servers.localServer && server.isAvailable(player.getPower(), true));
    }

    public static boolean send(Creature creature) {
        if (!creature.isPlayer()) return false;

        if (getServers((Player) creature).count() > 0) {
            ModQuestions.createQuestion(creature, "Welcome", "Welcome to the Requiem of Wurm PVE Cluster!", MiscConstants.NOID, new NewPlayerQuestion((Player) creature)).sendQuestion();
            return true;
        }

        return false;
    }
}
