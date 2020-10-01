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

    // todo
    private void raster() {
        layer = new Vector<>(island5.width * island5.height);

        // default dev Id for each Tile (4200)
        for (int y = 0; y < island5.height; y++) {
            for (int x = 0; x < island5.width; x++) {
                layer.add(new IslandTile());
                layer.get(y * island5.width + x).developmentId = 0xFF;
            }
        }

        // only 4068
        for (int i = 0; i < islandHouse.getCount(); i++) {
            var tile = islandHouse.getTiles().get(i);

            if (tile.xPosOnIsland >= island5.width || tile.yPosOnIsland >= island5.height) {
                throw new RuntimeException("Invalid tile position.");
            }

            //System.out.println("Tile dev name: " + developmentFile.getMeta(tile.developmentId).name);

            var info = developmentFile.getMeta(tile.developmentId);
            int buildingWidth = info.width;
            int buildingHeight = info.height;
            for (int y = 0; y < buildingHeight && tile.yPosOnIsland + y < island5.height; y++) {
                for (int x = 0; x < buildingWidth && tile.xPosOnIsland + x < island5.width; x++) {
                    layer.get((tile.yPosOnIsland + y) * island5.width + tile.xPosOnIsland + x).developmentId = tile.developmentId;
                    layer.get((tile.yPosOnIsland + y) * island5.width + tile.xPosOnIsland + x).xPosOnIsland = x;
                    layer.get((tile.yPosOnIsland + y) * island5.width + tile.xPosOnIsland + x).yPosOnIsland = y;
                }
            }
        }
    }
}
