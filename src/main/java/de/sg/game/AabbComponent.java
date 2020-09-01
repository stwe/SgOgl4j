package de.sg.game;

import org.joml.Vector2f;

public class AabbComponent {

    private Vector2f min;
    private Vector2f max;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public AabbComponent() {
    }

    public AabbComponent(Vector2f min, Vector2f max) {
        this.min = min;
        this.max = max;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public Vector2f getMin() {
        return min;
    }

    public Vector2f getMax() {
        return max;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setMin(Vector2f min) {
        this.min = min;
    }

    public void setMax(Vector2f max) {
        this.max = max;
    }
}
