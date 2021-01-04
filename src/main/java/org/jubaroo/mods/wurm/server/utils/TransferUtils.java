package org.jubaroo.mods.wurm.server.utils;

import com.wurmonline.math.TilePos;
import com.wurmonline.server.Server;
import com.wurmonline.server.ServerEntry;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.players.Player;

public class TransferUtils {
    public static void sendPlayerToServer(Player player, ServerEntry server, TilePos tile, byte kingdom) {
        if (kingdom < 0) kingdom = server.KINGDOM;

        if (tile == null) {
            tile = new TilePos();
            if (kingdom == 2) {
                tile.x = server.SPAWNPOINTMOLX;
                tile.y = server.SPAWNPOINTMOLY;
            } else if (kingdom == 3) {
                tile.x = server.SPAWNPOINTLIBX;
                tile.y = server.SPAWNPOINTLIBY;
            } else {
                tile.x = server.SPAWNPOINTJENNX;
                tile.y = server.SPAWNPOINTJENNY;
            }
        }

        for (Item inventoryItem : player.getInventory().getAllItems(true)) {
            if (!inventoryItem.willLeaveServer(true, false, player.getPower() > 0)) {
                player.getCommunicator().sendNormalServerMessage("The " + inventoryItem.getName() + " stays behind.");
            }
        }

        for (Item bodyItem : player.getBody().getAllItems()) {
            if (!bodyItem.willLeaveServer(true, false, player.getPower() > 0)) {
                player.getCommunicator().sendNormalServerMessage("The " + bodyItem.getName() + " stays behind.");
            }
        }

        player.sendTransfer(
                Server.getInstance(),
                server.INTRASERVERADDRESS,
                Integer.parseInt(server.INTRASERVERPORT),
                server.INTRASERVERPASSWORD,
                server.id,
                tile.x,
                tile.y,
                true,
                false,
                server.getKingdom()
        );
    }
}
