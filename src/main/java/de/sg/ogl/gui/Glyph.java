/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.buffer.Vertex2D;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Glyph {

    enum GlyphSortType {
        NONE,
        FRONT_TO_BACK,
        BACK_TO_FRONT,
        TEXTURE
    }

    // 42 floats (2 x 3 Vertex2D)
    public static final int NR_OF_VERTICES = 6;
    public static final int NR_OF_FLOATS = NR_OF_VERTICES * Vertex2D.BUFFER_LAYOUT_2D.getComponentCount();

    public Vertex2D topLeft = new Vertex2D();
    public Vertex2D bottomLeft = new Vertex2D();
    public Vertex2D topRight = new Vertex2D();
    public Vertex2D bottomRight = new Vertex2D();

    public int textureId;

    public Glyph() {}

    public Glyph(Vector4f destRect, int textureId, Vector3f color) {
        this.textureId = textureId;

        // 0, 0
        topLeft.position = new Vector2f(destRect.x, destRect.y);
        topLeft.uv = new Vector2f(0.0f, 1.0f);
        topLeft.color = new Vector3f(color);

        // 0, 1
        bottomLeft.position = new Vector2f(destRect.x, destRect.y + destRect.w);
        bottomLeft.uv = new Vector2f(0.0f, 0.0f);
        bottomLeft.color = new Vector3f(color);

        // 1, 1
        bottomRight.position = new Vector2f(destRect.x + destRect.z, destRect.y + destRect.w);
        bottomRight.uv = new Vector2f(1.0f, 0.0f);
        bottomRight.color = new Vector3f(color);

        // 1, 0
        topRight.position = new Vector2f(destRect.x + destRect.z, destRect.y);
        topRight.uv = new Vector2f(1.0f, 1.0f);
        topRight.color = new Vector3f(color);
    }
}
