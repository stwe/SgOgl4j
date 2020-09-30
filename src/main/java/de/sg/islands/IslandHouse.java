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

import static de.sg.ogl.Log.LOGGER;

public class IslandHouse {

    private static final int BYTES_PER_TILE = 8;

    private final List<IslandTile> tiles = new Vector<>();
    private final int count;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public IslandHouse(Chunk chunk) {
        LOGGER.debug("Creates IslandHouse object.");

        count = calcNrOfTiles(chunk);
        readTileData(chunk);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public List<IslandTile> getTiles() {
        return tiles;
    }

    public int getCount() {
        return count;
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private int calcNrOfTiles(Chunk chunk) {
        var res = chunk.getDataLength() / BYTES_PER_TILE;
        LOGGER.debug("Detected {} tiles.", count);

        return res;
    }

    private void readTileData(Chunk chunk) {
        LOGGER.debug("Start reading the tile data...");

        for (var i = 0; i < count; i++) {
            var developmentId = shortToInt(chunk.getData().getShort()); // 2
            var xPosOnIsland = byteToInt(chunk.getData().get());        // 1
            var yPosOnIsland = byteToInt(chunk.getData().get());        // 1
            var packedInt = chunk.getData().getInt();                   // 4
                                                                            // = 8 Bytes

            var tile = new IslandTile();
            tile.developmentId = developmentId;
            tile.xPosOnIsland = xPosOnIsland;
            tile.yPosOnIsland = yPosOnIsland;

            tile.rotation = Util.bitExtracted(packedInt, 2, 1);
            tile.animationCount = Util.bitExtracted(packedInt, 4, 3);
            tile.unknow = Util.bitExtracted(packedInt, 8, 7);
            tile.state = Util.bitExtracted(packedInt, 3, 15);
            tile.random = Util.bitExtracted(packedInt, 5, 18);
            tile.player = Util.bitExtracted(packedInt, 3, 23);
            tile.empty = Util.bitExtracted(packedInt,7, 26);

            if (developmentId < 101 || developmentId > 2822) {
                throw new RuntimeException("Invalid Id");
            }

            tiles.add(tile);
        }

        LOGGER.debug("Tile data read successfully.");
    }
}
