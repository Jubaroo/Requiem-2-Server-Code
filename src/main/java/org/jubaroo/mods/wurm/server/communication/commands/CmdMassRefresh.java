package org.jubaroo.mods.wurm.server.communication.commands;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Players;
import com.wurmonline.server.behaviours.Methods;
import com.wurmonline.server.creatures.Communicator;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.creatures.CreatureStatus;
import com.wurmonline.server.deities.Deities;
import com.wurmonline.server.players.Player;
import com.wurmonline.shared.constants.SoundNames;
import org.jubaroo.mods.wurm.server.RequiemLogging;

public class CmdMassRefresh extends WurmCmd {

    public CmdMassRefresh() {
        super("#massrefresh", MiscConstants.POWER_IMPLEMENTOR);
    }

    @Override
    public boolean runWurmCmd(Creature actor, String[] argv) {
        massRefresh(actor);
        return true;
    }

    public static void massRefresh(Creature actor) {
        Communicator comm = actor.getCommunicator();
        try {
            int nums = 0;
            Player[] allPlayers = Players.getInstance().getPlayers();
            for (Player player : allPlayers) {
                CreatureStatus status = player.getStatus();
                if (player.hasLink()) {
                    ++nums;
                    Methods.sendSound(player, SoundNames.HUMM_SND);
                    status.refresh(0.99f, true);
                    status.removeWounds();
                    status.setMaxCCFP();
                    if (player.getDeity() != null) {
                        player.getCommunicator().sendSafeServerMessage(getDeityMessage(player), (byte) 2);
                    } else
                        player.getCommunicator().sendSafeServerMessage("A strange and calming energy flows through the lands and into its people as the Gods grant favor upon them. You suddenly feel refreshed, well fed, and fully healed!", (byte) 2);
                    String mesg = String.format("Player %s has been refreshed and healed.", player.getName());
                    comm.sendNormalServerMessage(mesg);
                }
            }
            if (nums > 0) {
                RequiemLogging.logInfo(String.format("Player %s used %s", actor.getName(), CmdMassRefresh.class.getName()));
                String message = "===== All players refreshed and healed =====";
                comm.sendNormalServerMessage(message);
            }
        } catch (Throwable e) {
            comm.sendNormalServerMessage(String.format("error: %s", e.toString()));
        }
    }

    private static String getDeityMessage(Player player) {
        switch (player.getDeity().number) {
            case Deities.DEITY_FO:
                return "A strange and calming energy flows through the lands and into its people as Fo grants favor upon them. You suddenly feel refreshed, well fed, and fully healed!";
            case Deities.DEITY_MAGRANON:
                return "A strange and calming energy flows through the lands and into its people as Magranon grants favor upon them. You suddenly feel refreshed, well fed, and fully healed!";
            case Deities.DEITY_VYNORA:
                return "A strange and calming energy flows through the lands and into its people as Vynora grants favor upon them. You suddenly feel refreshed, well fed, and fully healed!";
            case Deities.DEITY_LIBILA:
                return "A strange and calming energy flows through the lands and into its people as Libila grants favor upon them. You suddenly feel refreshed, well fed, and fully healed!";
            default:
                return "A strange and calming energy flows through the lands and into its people as the Gods grant favor upon them. You suddenly feel refreshed, well fed, and fully healed!";
        }
    }

}