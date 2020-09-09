/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.buffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;

public final class VertexAttribute {

    //-------------------------------------------------
    // Public member
    //-------------------------------------------------

    public boolean normalize = false;
    public int size = 0;
    public long offset = 0;

    //-------------------------------------------------
    // Private member
    //-------------------------------------------------

    private final VertexAttributeType vertexAttributeType;
    private final String name;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public VertexAttribute(VertexAttributeType vertexAttributeType, String name) {
        this.vertexAttributeType = vertexAttributeType;
        this.name = name;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public VertexAttributeType getVertexAttributeType() {
        return vertexAttributeType;
    }

    public String getName() {
        return name;
    }

    //-------------------------------------------------
    // Meta data
    //-------------------------------------------------

    public enum VertexAttributeType {
        POSITION, POSITION_2D, COLOR, UV, NORMAL, TANGENT, BITANGENT, BONE_IDS, WEIGHTS;

        public int getComponentCount() {
            switch (this) {
                case POSITION_2D:
                case UV: return 2;
                case POSITION:
                case COLOR:
                case NORMAL:
                case TANGENT:
                case BITANGENT: return 3;
                case BONE_IDS:
                case WEIGHTS: return 4;
                default: return 0;
            }
        }

        public int getAttributeSizeInBytes() {
            switch (this) {
                case POSITION_2D:
                case UV: return 2 * 4;
                case POSITION:
                case COLOR:
                case NORMAL:
                case TANGENT:
                case BITANGENT: return 3 * 4;
                case BONE_IDS:
                case WEIGHTS: return 4 * 4;
                default: return 0;
            }
        }

        public int getGlType() {
            switch (this) {
                case POSITION:
                case POSITION_2D:
                case COLOR:
                case UV:
                case NORMAL:
                case TANGENT:
                case BITANGENT:
                case WEIGHTS: return GL_FLOAT;
                case BONE_IDS: return GL_INT;
                default: return 0;
            }
        }
    }
}
