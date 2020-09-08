/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.sgbrick.component;

import org.joml.Vector2f;

public class PhysicsComponent {

    private Vector2f velocity = new Vector2f(0.0f);

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public PhysicsComponent() {
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public Vector2f getVelocity() {
        return velocity;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }
}
