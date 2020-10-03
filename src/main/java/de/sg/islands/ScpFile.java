/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.islands;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import static de.sg.ogl.Log.LOGGER;

public class ScpFile extends de.sg.islands.File {

    private final DevelopmentFile developmentFile;

    private Island5 island5;
    private IslandHouse islandHouse;
    private List<IslandTile> layer;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public ScpFile(String filePath, DevelopmentFile developmentFile) throws IOException {
        super(Objects.requireNonNull(filePath, "filePath must not be null"));

        LOGGER.debug("Creates ScpFile object from file {}.", filePath);

        this.developmentFile = developmentFile;

        readDataFromChunks();
        raster();
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public Island5 getIsland5() {
        return island5;
    }

    public IslandHouse getIslandHouse() {
        return islandHouse;
    }

    public List<IslandTile> getLayer() {
        return layer;
    }

    //-------------------------------------------------
    // FileInterface
    //-------------------------------------------------

    @Override
    public void readDataFromChunks() {
        LOGGER.debug("Start reading data from Chunks...");

        for (var chunk : getChunks()) {
            if (chunk.getId().equals("INSEL5")) {
                island5 = new Island5(chunk);
                LOGGER.debug("INSEL5 found");
            }
            if (chunk.getId().equals("INSELHAUS")) {
                islandHouse = new IslandHouse(chunk);
                LOGGER.debug("INSELHAUS found");
            }
            if (chunk.getId().equals("HIRSCH2")) {
                LOGGER.debug("HIRSCH2 found");
            }
        }

        LOGGER.debug("Chunks data read successfully.");
    }

    //-------------------------------------------------
    // Layer
    //-------------------------------------------------

    private void raster() {
        if (island5 == null) {
            LOGGER.warn("ISLAND5 missing.");

            return;
        }

        layer = new Vector<>(island5.width * island5.height);

        LOGGER.debug("Create a Layer with {}x{} Tiles", island5.width, island5.height);

        clearTiles();
        initTiles();
    }

    private void clearTiles() {
        LOGGER.debug("Set FF as default value for the devId for each of the {} Tiles", island5.width * island5.height);

        for (int y = 0; y < island5.height; y++) {
            for (int x = 0; x < island5.width; x++) {
                layer.add(new IslandTile());
                layer.get(y * island5.width + x).developmentId = 0xFF;
            }
        }
    }

    /*
        Im INSELHAUS chunk ist an einer Stelle an der ein Haus steht nur an einer Stelle
        (naemlich am Feld des unteren linken Ecks des Gebaeudes) die Gebaeude-ID gespeichert.
                                                                                     |
                                                                                     |
                                                                                    / \
                                                                              -----    ------
y                                                                             |             |
                                                                              |             |
          269    270    271    272    273    274    275    276    277         |             | 278
        |------|------|------|------|------|------|------|------|------|------|------|------|------|
9       | 1201 | 1201 | 1204 | 1203 | 1214 | 1011 | 2722 | 2722 | 1104 |      | berg5| berg5| 1104 |
        |------|------|------|------|------|------|------|------|------|------|------|------|------|
                                                                              |             |
                                                                              |             |
          240    241    242    243    244    245    246    247    248    249  | 250         | 251
        |------|------|------|------|------|------|------|------|------|------|------|------|------|
8       | 1201 | 1201 | 1204 | 1203 | 1214 | 1011 | 2722 | 2722 | 2722 | 2722 | 1104 | berg5| 2722 |
        |------|------|------|------|------|------|------|------|------|------|------|------|------|
    x      0       1      2     3      4      5      6      7      8      9   |   10     11 |   12
     */

    private void initTiles() {
        LOGGER.debug("Initialize the Layer Tiles with the IslandHouse data.");

        for (int i = 0; i < islandHouse.getCount(); i++) {
            var tile = islandHouse.getTiles().get(i);

            if (isValidTilePosition(tile.xPosOnIsland, tile.yPosOnIsland)) {
                var tileDevInfo = developmentFile.getMeta(tile.developmentId);

                for (int y = 0; y < tileDevInfo.height && isValidTilePosY(tile.yPosOnIsland + y); y++) {
                    for (int x = 0; x < tileDevInfo.width && isValidTilePosX(tile.xPosOnIsland + x); x++) {
                        var targetIndex = (tile.yPosOnIsland + y) * island5.width + tile.xPosOnIsland + x;

                        layer.get(targetIndex).developmentId = tile.developmentId;
                        layer.get(targetIndex).xPosOnIsland = x;
                        layer.get(targetIndex).yPosOnIsland = y;
                    }
                }
            } else {
                throw new RuntimeException("Invalid Tile position.");
            }
        }
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private boolean isValidTilePosition(int xPosOnIsland, int yPosOnIsland) {
        return isValidTilePosX(xPosOnIsland) && isValidTilePosY(yPosOnIsland);
    }

    private boolean isValidTilePosX(int xPosOnIsland) {
        return xPosOnIsland < island5.width;
    }

    private boolean isValidTilePosY(int yPosOnIsland) {
        return yPosOnIsland < island5.height;
    }
}
