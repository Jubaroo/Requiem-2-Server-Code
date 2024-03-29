package org.jubaroo.mods.wurm.server.misc.labyrinth.events;

import com.wurmonline.server.structures.DbFence;
import com.wurmonline.server.zones.VolaTile;
import com.wurmonline.server.zones.Zones;
import com.wurmonline.shared.constants.StructureConstantsEnum;
import org.jubaroo.mods.wurm.server.RequiemLogging;

import java.io.IOException;

public class GrowMazeHedgeEvent extends EventOnce {
    private DbFence fence;
    private byte stage;

    public GrowMazeHedgeEvent(final int fromNow, final DbFence fence, final byte stage) {
        super(fromNow, Unit.MILLISECONDS);
        this.fence = null;
        this.stage = 0;
        this.fence = fence;
        this.stage = stage;
    }

    @Override
    public boolean invoke() {
        try {
            this.fence.setType(StructureConstantsEnum.getEnumByINDEX(this.stage));
            this.fence.save();
            final VolaTile tile = Zones.getTileOrNull(this.fence.getTileX(), this.fence.getTileY(), true);
            tile.updateFence(this.fence);
        } catch (IOException e) {
            RequiemLogging.logException("[ERROR] in invoke in GrowMazeHedgeEvent", e);
        }
        return true;
    }
}
