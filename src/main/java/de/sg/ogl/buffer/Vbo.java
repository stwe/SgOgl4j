/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.buffer;

import de.sg.ogl.SgOglRuntimeException;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.opengl.GL15.*;

public final class Vbo {

    //-------------------------------------------------
    // Live and let die
    //-------------------------------------------------

    static public int createVbo() {
        var id = glGenBuffers();
        if (id == 0) {
            throw new SgOglRuntimeException("Vbo creation has failed.");
        }

        LOGGER.debug("A new Vbo was created. The Id is {}.", id);

        return id;
    }

    static public int createEbo() {
        return createVbo();
    }

    static public void deleteVbo(int vboId) {
        if (vboId > 0) {
            glDeleteBuffers(vboId);
            LOGGER.debug("Vbo {} was deleted.", vboId);
        }
    }

    static public void deleteEbo(int eboId) {
        deleteVbo(eboId);
    }

    //-------------------------------------------------
    // Bind / Unbind
    //-------------------------------------------------

    static public void bindVbo(int vboId, int target) {
        glBindBuffer(target, vboId);
    }

    static public void bindVbo(int vboId) {
        bindVbo(vboId, GL_ARRAY_BUFFER);
    }

    static public void bindEbo(int eboId) {
        bindVbo(eboId, GL_ELEMENT_ARRAY_BUFFER);
    }

    static public void unbindVbo() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    private Vbo() {}
}
