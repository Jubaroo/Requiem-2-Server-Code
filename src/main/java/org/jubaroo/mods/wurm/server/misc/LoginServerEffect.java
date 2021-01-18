package org.jubaroo.mods.wurm.server.misc;

import com.wurmonline.server.MiscConstants;
import com.wurmonline.server.Server;
import com.wurmonline.server.players.Player;
import com.wurmonline.shared.constants.EffectConstants;

public class LoginServerEffect {
    public static int tileX;
    public static int tileY;
    public static int tileZ;

    //public static void onServerStarted() {
    //    try {
    //        //Players.getInstance().sendGlobalNonPersistantComplexEffect(MiscConstants.NOID, EffectConstants.EFFECT_ERUPTION, ((LoginServerEffect.tileX << 2) + 2), ((LoginServerEffect.tileY << 2) + 2), Tiles.decodeHeightAsFloat(Server.surfaceMesh.getTile(LoginServerEffect.tileX, LoginServerEffect.tileY)), 20, 100, 2, (byte) 4, (byte) 0);
    //    } catch (Exception ex2) {
    //        RequiemLogging.logException("Error adding login server effect marker", ex2.getCause());
    //        ex2.printStackTrace();
    //    }
    //}

    public static void playerLogin(Player player) {
        //Players.getInstance().sendGlobalNonPersistantComplexEffect(MiscConstants.NOID, EffectConstants.EFFECT_ERUPTION, ((LoginServerEffect.tileX << 2) + 2), ((LoginServerEffect.tileY << 2) + 2), Tiles.decodeHeightAsFloat(Server.surfaceMesh.getTile(LoginServerEffect.tileX, LoginServerEffect.tileY)), 20, 100, 2, (byte) 4, (byte) 0);

        player.getCommunicator().sendAddComplexEffect(Long.MAX_VALUE - Server.rand.nextInt(1000), MiscConstants.NOID, EffectConstants.EFFECT_ERUPTION, (float) tileX, (float) tileY, (float) tileZ, (byte) 0, (float) 20, (float) 20, 2, (byte) 4, (byte) 0);

        //final Player[] players = Players.getInstance().getPlayers();
        //if (players.length != 0) {
        //    for (Player player : players) {
        //        if (player != null) {
        //            final long effectId = Long.MAX_VALUE - Server.rand.nextInt(1000);
        //            //player.getCommunicator().sendAddEffect(markArr.getWurmId(), EffectConstants.EFFECT_ERUPTION, (float) ((tileX << 2) + 2), (float) ((tileY << 2) + 2), 0.0f, (byte) 0);
        //            //player.getCommunicator().sendAddComplexEffect(effectId, MiscConstants.NOID, EffectConstants.EFFECT_ERUPTION, (float) tileX, (float) tileY, (float) tileZ, (byte) 0, (float) 20, (float) 20, 2, (byte) 4, (byte) 0);
        //            //Players.getInstance().sendGlobalNonPersistantComplexEffect(MiscConstants.NOID, EffectConstants.EFFECT_ERUPTION, ((tileX << 2) + 2), ((tileY << 2) + 2), Tiles.decodeHeightAsFloat(Server.surfaceMesh.getTile(tileX, tileY)), 20, 10, 10, (byte) 4, (byte) 0);
        //        }
        //    }
        //}
    }

    static {
        tileX = 0;
        tileY = 0;
        tileZ = 3777;
    }

}
