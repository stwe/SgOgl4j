package de.sg.sgbrick;

public class SolidComponent {

    private boolean solid = false;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public SolidComponent() {
    }

    public SolidComponent(boolean solid) {
        this.solid = solid;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public boolean isSolid() {
        return solid;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setSolid(boolean solid) {
        this.solid = solid;
    }
}
