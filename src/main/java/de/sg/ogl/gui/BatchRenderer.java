/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

public class BatchRenderer {
    public int offset;
    public int nrOfVertices;
    public int textureId;

    public BatchRenderer() {}

    public BatchRenderer(int offset, int nrOfVertices, int textureId) {
        this.offset = offset;
        this.nrOfVertices = nrOfVertices;
        this.textureId = textureId;
    }
}
