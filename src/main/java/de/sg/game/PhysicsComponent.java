package de.sg.game;

public class PhysicsComponent {

    private float velocity = 0.0f;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public PhysicsComponent() {
    }

    public PhysicsComponent(float velocity) {
        this.velocity = velocity;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public float getVelocity() {
        return velocity;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }
}
