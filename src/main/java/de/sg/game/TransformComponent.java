package de.sg.game;

import org.joml.Vector2f;

public class TransformComponent {

    private Vector2f position = new Vector2f(0.0f);
    private Vector2f size = new Vector2f(1.0f);
    private float rotation = 0.0f;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public TransformComponent() {
    }

    public TransformComponent(Vector2f position, Vector2f size, float rotation) {
        this.position = position;
        this.size = size;
        this.rotation = rotation;
    }

    public TransformComponent(Vector2f position, Vector2f size) {
        this(position, size, 0.0f);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getSize() {
        return size;
    }

    public float getRotation() {
        return rotation;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void setSize(Vector2f size) {
        this.size = size;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
