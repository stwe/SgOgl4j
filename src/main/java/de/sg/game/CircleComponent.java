/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.game;

import org.joml.Vector2f;

public class CircleComponent {

    private Vector2f center;
    private float radius;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public CircleComponent() {
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public Vector2f getCenter() {
        return center;
    }

    public float getRadius() {
        return radius;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setCenter(Vector2f center) {
        this.center = center;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
