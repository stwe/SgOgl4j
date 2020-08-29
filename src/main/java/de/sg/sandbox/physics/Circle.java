package de.sg.sandbox.physics;

import org.joml.Vector2f;

public class Circle {

    public Vector2f center;
    public float radius;

    public Circle() {
    }

    public Circle(Vector2f center, float radius) {
        this.center = center;
        this.radius = radius;
    }
}
