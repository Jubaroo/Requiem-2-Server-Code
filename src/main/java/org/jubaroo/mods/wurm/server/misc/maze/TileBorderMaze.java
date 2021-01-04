package org.jubaroo.mods.wurm.server.misc.maze;

import com.wurmonline.shared.constants.StructureConstantsEnum;

public class TileBorderMaze extends Maze {
    public TileBorderMaze(final int startX, final int startY, final int size, final StructureConstantsEnum fenceType) {
        super(startX, startY, size, fenceType);
    }
}
