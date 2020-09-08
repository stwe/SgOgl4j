/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.sgbrick.component;

import org.joml.Vector2f;

public class AabbComponent {

    private Vector2f min;
    private Vector2f max;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public AabbComponent() {
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public Vector2f getMin() {
        return min;
    }

    public Vector2f getMax() {
        return max;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setMin(Vector2f min) {
        this.min = min;
    }

    public void setMax(Vector2f max) {
        this.max = max;
    }
}
