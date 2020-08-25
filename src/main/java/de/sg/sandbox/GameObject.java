package de.sg.sandbox;

import de.sg.ogl.resource.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GameObject {

    protected Vector2f position;
    protected Vector2f size;
    protected Texture texture;
    protected Vector3f color;
    protected Vector2f velocity;

    protected boolean isSolid = false;
    protected boolean destroyed = false;
    protected float rotation = 0.0f;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public GameObject(Vector2f position, Vector2f size, Texture texture, Vector3f color, Vector2f velocity) {
        this.position = position;
        this.size = size;
        this.texture = texture;
        this.color = color;
        this.velocity = velocity;
    }

    public GameObject(Vector2f position, Vector2f size, Texture texture, Vector3f color) {
        this(position, size, texture, color, new Vector2f(0.0f));
    }

    public GameObject(Vector2f position, Vector2f size, Texture texture) {
        this(position, size, texture, new Vector3f(1.0f), new Vector2f(0.0f));
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void render(SpriteRenderer renderer) {
        renderer.render(texture, position, size, rotation, color);
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

    public Texture getTexture() {
        return texture;
    }

    public Vector3f getColor() {
        return color;
    }

    public Vector2f getVelocity() {
        return velocity;
    }

    public boolean isSolid() {
        return isSolid;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public float getRotation() {
        return rotation;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setSolid(boolean solid) {
        isSolid = solid;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
