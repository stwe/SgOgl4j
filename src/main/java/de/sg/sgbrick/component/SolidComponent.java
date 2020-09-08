/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.sgbrick.component;

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
