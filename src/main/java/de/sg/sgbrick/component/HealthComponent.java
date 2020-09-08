/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.sgbrick.component;

public class HealthComponent {

    private boolean destroyed = false;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public HealthComponent() {
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
