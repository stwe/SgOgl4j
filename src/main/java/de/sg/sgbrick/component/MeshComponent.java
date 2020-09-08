/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.sgbrick.component;

import de.sg.ogl.resource.Mesh;

public class MeshComponent {

    private Mesh mesh;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public MeshComponent() {
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public Mesh getMesh() {
        return mesh;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }
}
