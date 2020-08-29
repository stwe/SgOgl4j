package de.sg.sandbox;

import de.sg.ogl.resource.Texture;
import de.sg.sandbox.physics.Circle;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Ball extends GameObject {

    private float radius;
    private boolean stuck;

    private final Circle circle;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Ball(Vector2f position, float radius, Vector2f velocity, Texture texture) {
        super(position, new Vector2f(radius * 2.0f, radius * 2.0f), texture, new Vector3f(1.0f), velocity);

        this.radius = radius;
        this.stuck = true;

        this.circle = new Circle(position, radius);
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

    public Circle getCircle() {
        return circle;
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
    // Override
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

        this.aabb.min = new Vector2f(position);
        this.aabb.max = new Vector2f(position).add(size);

        this.circle.center = new Vector2f(position).add(this.circle.radius, this.circle.radius);

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
