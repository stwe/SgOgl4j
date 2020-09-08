/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.sgbrick.component;

public class BallComponent {

    private float radius = 0.0f;
    private boolean stuck = true;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public BallComponent() {
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public float getRadius() {
        return radius;
    }

    public boolean isStuck() {
        return stuck;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setStuck(boolean stuck) {
        this.stuck = stuck;
    }
}
