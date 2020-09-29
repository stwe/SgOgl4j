/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.islands;

import java.util.List;
import java.util.Vector;

import static de.sg.islands.Util.byteToInt;
import static de.sg.islands.Util.shortToInt;

public class IslandHouse {

    private static final int BYTES_PER_TILE = 8;

    private List<IslandTile> tiles = new Vector<>();
    private int count;

    public IslandHouse(Chunk chunk) {

        count = chunk.getDataLength() / BYTES_PER_TILE;

        for (var i = 0; i < count; i++) {
            var developmentId = shortToInt(chunk.getData().getShort()); // 2
            var xPosOnIsland = byteToInt(chunk.getData().get());        // 1
            var yPosOnIsland = byteToInt(chunk.getData().get());        // 1
            chunk.getData().getInt();                                        // 4
                                                                             // 8 Bytes

            var tile = new IslandTile();
            tile.developmentId = developmentId;
            tile.xPosOnIsland = xPosOnIsland;
            tile.yPosOnIsland = yPosOnIsland;

            if (developmentId < 101 || developmentId > 2822) {
                throw new RuntimeException("Invalid dev Id");
            }

            tiles.add(tile);
        }
    }

    public List<IslandTile> getTiles() {
        return tiles;
    }

    public int getCount() {
        return count;
    }
}
