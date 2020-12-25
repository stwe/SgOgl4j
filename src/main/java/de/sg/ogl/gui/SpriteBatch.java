/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.resource.Mesh;

import static de.sg.ogl.Log.LOGGER;

public class SpriteBatch {

    private final Mesh mesh;

    public SpriteBatch() {
        LOGGER.debug("Creates SpriteBatch object.");

        mesh = new Mesh();
    }
}
