/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.resource;

import de.sg.ogl.buffer.BufferLayout;
import de.sg.ogl.buffer.Vertex2D;
import org.joml.Vector2f;

public class Geometry {

    public enum GeometryId {
        QUAD_2D
    }

    public GeometryId id;
    public Vertex2D[] vertices;
    public float[] floats;
    public int drawCount;
    public BufferLayout defaultBufferLayout;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Geometry(GeometryId id, Vertex2D[] vertices, int drawCount, BufferLayout bufferLayout) {
        this.id = id;
        this.vertices = vertices;
        this.floats = Vertex2D.toFloatArray(vertices);
        this.drawCount = drawCount;
        this.defaultBufferLayout = bufferLayout;
    }

    //-------------------------------------------------
    // Vertices
    //-------------------------------------------------

    static Vertex2D[] getQuad2DVertices() {
        return new Vertex2D[] {
                new Vertex2D(new Vector2f(0.0f, 1.0f), new Vector2f(0.0f, 1.0f)),
                new Vertex2D(new Vector2f(1.0f, 0.0f), new Vector2f(1.0f, 0.0f)),
                new Vertex2D(new Vector2f(0.0f, 0.0f), new Vector2f(0.0f, 0.0f)),

                new Vertex2D(new Vector2f(0.0f, 1.0f), new Vector2f(0.0f, 1.0f)),
                new Vertex2D(new Vector2f(1.0f, 1.0f), new Vector2f(1.0f, 1.0f)),
                new Vertex2D(new Vector2f(1.0f, 0.0f), new Vector2f(1.0f, 0.0f))
        };
    }
}
