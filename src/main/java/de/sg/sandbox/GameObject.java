package de.sg.sandbox;

import de.sg.ogl.resource.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GameObject {

    private final Vector2f position;
    private final Vector2f size;
    private final Texture texture;
    private final Vector3f color;
    private boolean isSolid;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public GameObject(Vector2f position, Vector2f size, Texture texture, Vector3f color) {
        this.position = position;
        this.size = size;
        this.texture = texture;
        this.color = color;
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void render(SpriteRenderer renderer) {
        renderer.render(texture, position, size, 0.0f, color);
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

    public boolean isSolid() {
        return isSolid;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setSolid(boolean solid) {
        isSolid = solid;
    }
}
