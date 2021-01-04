package org.jubaroo.mods.wurm.server.misc.maze;

import com.wurmonline.shared.constants.StructureConstantsEnum;

public class HedgeMaze extends TileBorderMaze {
    public HedgeMaze(final int startX, final int startY, final int size, final StructureConstantsEnum fenceType) {
        super(startX, startY, size, fenceType);
    }
}
