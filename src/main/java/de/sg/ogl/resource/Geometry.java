/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.resource;

import de.sg.ogl.buffer.BufferLayout;

public class Geometry {

    public enum GeometryId {
        QUAD_2D
    }

    public GeometryId id;
    public float[] vertices;
    public int drawCount;
    public BufferLayout defaultBufferLayout;

    public Geometry(GeometryId id, float[] vertices, int drawCount, BufferLayout bufferLayout) {
        this.id = id;
        this.vertices = vertices;
        this.drawCount = drawCount;
        this.defaultBufferLayout = bufferLayout;
    }

    /*
    public VerticesResource loadVerticesResource(VerticesResourceId verticesResourceId) {
        return verticesResources.get(verticesResourceId);
    }

    private void addQuadVertices2DResource() {
        LOGGER.debug("Add vertices for a 2D quad.");

        var vertices = new Vertex2D[] {
                new Vertex2D(new Vector2f(0.0f, 1.0f), new Vector2f(0.0f, 1.0f)),
                new Vertex2D(new Vector2f(1.0f, 0.0f), new Vector2f(1.0f, 0.0f)),
                new Vertex2D(new Vector2f(0.0f, 0.0f), new Vector2f(0.0f, 0.0f)),

                new Vertex2D(new Vector2f(0.0f, 1.0f), new Vector2f(0.0f, 1.0f)),
                new Vertex2D(new Vector2f(1.0f, 1.0f), new Vector2f(1.0f, 1.0f)),
                new Vertex2D(new Vector2f(1.0f, 0.0f), new Vector2f(1.0f, 0.0f))
        };

        verticesResources.put(
                VerticesResourceId.QUAD_2D,
                new VerticesResource(
                        VerticesResourceId.QUAD_2D,
                        Vertex2D.toFloatArray(vertices),
                        vertices.length,
                        Vertex2D.BUFFER_LAYOUT_2D
                )
        );
    }
    */
}
