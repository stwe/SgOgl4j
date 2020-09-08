package de.sg.sgbrick;

import org.joml.Vector2f;

public class PhysicsComponent {

    private Vector2f velocity = new Vector2f(0.0f);

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public PhysicsComponent() {
    }

    public PhysicsComponent(Vector2f velocity) {
        this.velocity = velocity;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public Vector2f getVelocity() {
        return velocity;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }
}
