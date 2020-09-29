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

    //-------------------------------------------------
    // FileInterface
    //-------------------------------------------------

    @Override
    public void readDataFromChunks() {
        for (var chunk : getChunks()) {
            if (chunk.getId().equals("INSEL5")) {
                island5 = new Island5(chunk);
                System.out.println("Insel5 found.");
            }
            if (chunk.getId().equals("INSELHAUS")) {
                islandHouse = new IslandHouse(chunk);
                System.out.println("Island House entries: " + islandHouse.getCount());
                System.out.println("Inselhaus found.");
            }
            if (chunk.getId().equals("HIRSCH2")) {
                System.out.println("Hirsch2 found.");
            }
        }
    }

    //-------------------------------------------------
    // Layer
    //-------------------------------------------------

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

            System.out.println("Tile dev name: " + developmentFile.getMeta(tile.developmentId).name);
        }
    }
}
