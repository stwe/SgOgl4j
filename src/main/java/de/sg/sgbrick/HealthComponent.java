package de.sg.sgbrick;

public class HealthComponent {

    private boolean destroyed = false;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public HealthComponent() {
    }

    public HealthComponent(boolean destroyed) {
        this.destroyed = destroyed;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public boolean isDestroyed() {
        return destroyed;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
