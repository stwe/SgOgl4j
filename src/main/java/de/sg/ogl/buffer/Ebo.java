/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.buffer;

import de.sg.ogl.SgOglRuntimeException;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.opengl.GL15.*;

public class Ebo implements Buffer {

    private int id;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Ebo() {
        LOGGER.debug("Creates Ebo object.");

        create();
    }

    //-------------------------------------------------
    // Implement Buffer
    //-------------------------------------------------

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void create() {
        id = glGenBuffers();
        if (id == 0) {
            throw new SgOglRuntimeException("Ebo creation has failed.");
        }

        LOGGER.debug("A new Ebo was created. The Id is {}.", id);
    }

    @Override
    public void delete() {
        if (id > 0) {
            glDeleteBuffers(id);
            LOGGER.debug("Ebo {} was deleted.", id);
        }
    }

    @Override
    public void bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
    }

    @Override
    public void unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    @Override
    public void cleanUp() {
        LOGGER.debug("Clean up Ebo.");

        unbind();
        delete();
    }
}
