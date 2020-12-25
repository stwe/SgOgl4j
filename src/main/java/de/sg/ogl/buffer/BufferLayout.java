/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.buffer;

import java.util.ArrayList;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public final class BufferLayout {

    private final ArrayList<VertexAttribute> vertexAttributes;

    private int stride;
    private int componentCount;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public BufferLayout(ArrayList<VertexAttribute> vertexAttributes) {
        LOGGER.debug("Creates BufferLayout object.");

        this.vertexAttributes = vertexAttributes;
        init();
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public ArrayList<VertexAttribute> getVertexAttributes() {
        return vertexAttributes;
    }

    public int getStride() {
        return stride;
    }

    public int getComponentCount() {
        return componentCount;
    }


    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    /**
     * Specify how OpenGL should interpret the vertex data.
     * A Vbo must be bound before!
     */
    public void createBufferLayout() {
        var index = 0;
        for (var attribute : vertexAttributes) {
            glEnableVertexAttribArray(index);
            glVertexAttribPointer(
                    index,
                    attribute.getVertexAttributeType().getComponentCount(),
                    attribute.getVertexAttributeType().getGlType(),
                    attribute.normalize,
                    stride,
                    attribute.offset
            );

            index++;
        }
    }

    private void init() {
        LOGGER.debug("Initialize Buffer layout.");

        long offset = 0;

        for (var attribute : vertexAttributes) {
            attribute.size = attribute.getVertexAttributeType().getAttributeSizeInBytes();
            attribute.offset = offset;

            offset += attribute.size;

            this.stride += attribute.size;
            this.componentCount += attribute.getVertexAttributeType().getComponentCount();
        }
    }
}
