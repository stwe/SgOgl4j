/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.islands;

import java.io.IOException;
import java.util.Objects;

import static de.sg.ogl.Log.LOGGER;

public class PaletteFile extends File {

    private static final int NUMBER_OF_COLORS = 256;
    private static final int NUMBER_OF_CHUNKS = 1;
    private static final String CHUNK_ID = "COL";

    private final int[] palette = new int[NUMBER_OF_COLORS];

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public PaletteFile(String filePath) throws IOException {
        super(Objects.requireNonNull(filePath, "filePath must not be null"));

        LOGGER.debug("Creates Palette object from file {}.", filePath);

        if (getNumberOfChunks() != NUMBER_OF_CHUNKS) {
            throw new RuntimeException("Invalid number of Chunks.");
        }

        if (!chunkIndexHasId(0, CHUNK_ID)) {
            throw new RuntimeException("Invalid Chunk Id.");
        }

        readDataFromChunks();
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public int[] getPalette() {
        return palette;
    }

    //-------------------------------------------------
    // FileInterface
    //-------------------------------------------------

    @Override
    public void readDataFromChunks() {
        LOGGER.debug("Start reading data from Chunks...");

        var chunk0 = getChunk(0);

        for (int i = 0; i < palette.length; i++) {
            int red = Util.byteToInt(chunk0.getData().get());
            int green = Util.byteToInt(chunk0.getData().get());
            int blue = Util.byteToInt(chunk0.getData().get());

            chunk0.getData().get(); // skip next byte

            palette[i] = rgbToInt(red, green, blue);
        }

        LOGGER.debug("Chunks data read successfully.");
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private static int rgbToInt(int red, int green, int blue) {
        var alpha = 255;

        return (alpha << 24) |
                (red << 16) |
                (green << 8) |
                (blue);
    }
}
