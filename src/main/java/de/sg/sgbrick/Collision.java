/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.sgbrick;

import org.joml.Vector2f;

public class Collision {

    public enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT;

        public static Direction fromInt(int value) {
            switch (value) {
                case 0: return UP;
                case 1: return RIGHT;
                case 2: return DOWN;
                case 3: return LEFT;
            }

            return null;
        }
    }

    private final boolean collision;
    private final Direction direction;
    private final Vector2f difference;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Collision(boolean collision, Direction direction, Vector2f difference) {
        this.collision = collision;
        this.direction = direction;
        this.difference = difference;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public boolean isCollision() {
        return collision;
    }

    public Direction getDirection() {
        return direction;
    }

    public Vector2f getDifference() {
        return difference;
    }
}
