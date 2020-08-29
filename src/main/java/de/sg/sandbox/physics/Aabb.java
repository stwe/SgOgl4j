package de.sg.sandbox.physics;

import org.joml.Vector2f;

public class Aabb {

    public Vector2f min;
    public Vector2f max;

    public Aabb() {
    }

    public Aabb(Vector2f min, Vector2f max) {
        this.min = min;
        this.max = max;
    }
}
