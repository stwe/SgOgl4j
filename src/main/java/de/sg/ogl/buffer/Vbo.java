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

    public static int createVbo() {
        var id = glGenBuffers();
        if (id == 0) {
            throw new SgOglRuntimeException("Vbo creation has failed.");
        }

        LOGGER.debug("A new Vbo was created. The Id is {}.", id);

        return id;
    }

    public static int createEbo() {
        return createVbo();
    }

    public static void deleteVbo(int vboId) {
        if (vboId > 0) {
            glDeleteBuffers(vboId);
            LOGGER.debug("Vbo {} was deleted.", vboId);
        }
    }

    public static void deleteEbo(int eboId) {
        deleteVbo(eboId);
    }

    //-------------------------------------------------
    // Bind / Unbind
    //-------------------------------------------------

    public static void bindVbo(int vboId, int target) {
        glBindBuffer(target, vboId);
    }

    public static void bindVbo(int vboId) {
        bindVbo(vboId, GL_ARRAY_BUFFER);
    }

    public static void bindEbo(int eboId) {
        bindVbo(eboId, GL_ELEMENT_ARRAY_BUFFER);
    }

    public static void unbindVbo(int target) {
        glBindBuffer(target, 0);
    }

    public static void unbindVbo() {
        unbindVbo(GL_ARRAY_BUFFER);
    }

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    private Vbo() {}
}
