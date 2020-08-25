package de.sg.sandbox;

import de.sg.ogl.resource.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Ball extends GameObject {

    private float radius;
    private boolean stuck;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Ball(Vector2f position, float radius, Vector2f velocity, Texture texture) {
        super(position, new Vector2f(radius * 2.0f, radius * 2.0f), texture, new Vector3f(1.0f), velocity);

        this.radius = radius;
        this.stuck = true;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public float getRadius() {
        return radius;
    }

    public boolean isStuck() {
        return stuck;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setStuck(boolean stuck) {
        this.stuck = stuck;
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public Vector2f update(float dt, int width) {
        if (!stuck) {
            position.add(new Vector2f(velocity).mul(dt));

            if (position.x <= 0.0f) {
                velocity.x = -velocity.x;
                position.x = 0.0f;
            } else if (position.x + size.x >= width) {
                velocity.x = -velocity.x;
                position.x = width - size.x;
            }

            if (position.y <= 0.0f) {
                velocity.y = -velocity.y;
                position.y = 0.0f;
            }
        }

        return position;
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    public void reset(Vector2f position, Vector2f velocity) {
        this.position = position;
        this.velocity = velocity;
        this.stuck = true;
    }
}
