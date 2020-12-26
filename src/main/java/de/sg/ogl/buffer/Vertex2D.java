/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.buffer;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static de.sg.ogl.buffer.VertexAttribute.VertexAttributeType.*;

public class Vertex2D {

    public static final BufferLayout BUFFER_LAYOUT_2D = new BufferLayout(
            new ArrayList<>(){{
                add(new VertexAttribute(POSITION_2D, "aPosition"));
                add(new VertexAttribute(COLOR, "aColor"));
                add(new VertexAttribute(UV, "aUv"));
            }}
    );

    private static final Vector3f DEFAULT_COLOR = new Vector3f(1.0f);

    //-------------------------------------------------
    // Public member
    //-------------------------------------------------

    public Vector2f position;
    public Vector3f color;
    public Vector2f uv;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Vertex2D() {}

    public Vertex2D(Vector2f position, Vector3f color, Vector2f uv) {
        this.position = position;
        this.color = color;
        this.uv = uv;
    }

    public Vertex2D(Vector2f position, Vector2f uv) {
        this(position, DEFAULT_COLOR, uv);
    }

    //-------------------------------------------------
    // Cast
    //-------------------------------------------------

    public static float[] toFloatArray(Vertex2D[] vertices) {
        var floats = new float[vertices.length * BUFFER_LAYOUT_2D.getComponentCount()];
        var i = 0;
        for (var vertex : vertices) {
            floats[i] = vertex.position.x;
            floats[i + 1] = vertex.position.y;
            floats[i + 2] = vertex.color.x;
            floats[i + 3] = vertex.color.y;
            floats[i + 4] = vertex.color.z;
            floats[i + 5] = vertex.uv.x;
            floats[i + 6] = vertex.uv.y;
            i += BUFFER_LAYOUT_2D.getComponentCount();
        }

        return floats;
    }
}
