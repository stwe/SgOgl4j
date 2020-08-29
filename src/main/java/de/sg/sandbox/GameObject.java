package de.sg.sandbox;

import de.sg.ogl.resource.Texture;
import de.sg.sandbox.physics.Aabb;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GameObject implements Entity {

    protected Vector2f position;
    protected Vector2f size;
    protected Texture texture;
    protected Vector3f color;
    protected Vector2f velocity;

    protected boolean isSolid = false;
    protected boolean destroyed = false;
    protected float rotation = 0.0f;

    protected final Aabb aabb;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public GameObject(Vector2f position, Vector2f size, Texture texture, Vector3f color, Vector2f velocity) {
        this.position = position;
        this.size = size;
        this.texture = texture;
        this.color = color;
        this.velocity = velocity;

        this.aabb = new Aabb(position, new Vector2f(position).add(size));
    }

    public GameObject(Vector2f position, Vector2f size, Texture texture, Vector3f color) {
        this(position, size, texture, color, new Vector2f(0.0f));
    }

    public GameObject(Vector2f position, Vector2f size, Texture texture) {
        this(position, size, texture, new Vector3f(1.0f), new Vector2f(0.0f));
    }

    //-------------------------------------------------
    // Implement Entity
    //-------------------------------------------------

    @Override
    public Vector2f update(float dt, int width) {
        this.aabb.min = new Vector2f(position);
        this.aabb.max = new Vector2f(position).add(size);

        return null;
    }

    @Override
    public void render(Renderer renderer) {
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

    public Aabb getAabb() {
        return aabb;
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
