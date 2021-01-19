/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.renderer;

import de.sg.ogl.Color;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Sprite {
    public Vector2f position = new Vector2f(0.0f);
    public Vector2f scale = new Vector2f(1.0f);
    public Color color = Color.WHITE;

    public Matrix4f getTransformationMatrix() {
        return new Matrix4f()
                .identity()
                .translate(position.x, position.y, 0.0f)
                .scale(scale.x, scale.y, 1.0f);
    }
}
