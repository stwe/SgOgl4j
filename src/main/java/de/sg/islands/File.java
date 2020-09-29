/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.islands;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

public abstract class File implements FileInterface {

    private final String filePath;
    private final List<Chunk> chunks = new Vector<>();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public File(String filePath) throws IOException {
        this.filePath = Objects.requireNonNull(filePath, "filePath must not be null");
        readChunksFromFile();
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public List<Chunk> getChunks() {
        return chunks;
    }

    public int getNumberOfChunks() {
        return chunks.size();
    }

    public Chunk getChunk(int chunkIndex) {
        return chunks.get(chunkIndex);
    }

    //-------------------------------------------------
    // FileInterface
    //-------------------------------------------------

    public void readChunksFromFile() throws IOException {
        var file = new java.io.File(Util.loadResource(filePath));
        var fileInputStream = new FileInputStream(file);

        while (fileInputStream.available() > 0) {
            chunks.add(new Chunk(fileInputStream));
        }
    }

    public abstract void readDataFromChunks();

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    boolean chunkIndexHasId(int chunkIndex, String chunkId) {
        return getChunk(chunkIndex).getId().equals(chunkId);
    }
}
