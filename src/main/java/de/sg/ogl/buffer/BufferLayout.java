package de.sg.ogl.buffer;

import java.util.ArrayList;

import static de.sg.ogl.Log.LOGGER;

public final class BufferLayout {

    private final ArrayList<VertexAttribute> vertexAttributes;

    private int stride = 0;
    private int componentCount = 0;

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
