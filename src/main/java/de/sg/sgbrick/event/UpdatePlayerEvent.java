/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.sgbrick.event;

import de.sg.ogl.ecs.Event;

public class UpdatePlayerEvent extends Event {

    public enum DIRECTION {
        LEFT, RIGHT, NONE
    }

    private DIRECTION direction = DIRECTION.NONE;
    private float velocity = 0.0f;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public UpdatePlayerEvent() {
    }

    public UpdatePlayerEvent(DIRECTION direction, float velocity) {
        this.direction = direction;
        this.velocity = velocity;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public DIRECTION getDirection() {
        return direction;
    }

    public float getVelocity() {
        return velocity;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setDirection(DIRECTION direction) {
        this.direction = direction;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }
}
