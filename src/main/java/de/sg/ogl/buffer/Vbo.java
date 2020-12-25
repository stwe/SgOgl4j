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
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

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
    // Init
    //-------------------------------------------------

    public static void initEmpty(int vboId, int elementCount, int elementSizeInBytes, int usage) {
        bindVbo(vboId);

        glBufferData(GL_ARRAY_BUFFER, (long)elementCount * elementSizeInBytes, usage);

        unbindVbo();
    }

    //-------------------------------------------------
    // Attributes
    //-------------------------------------------------

    public static void addFloatAttribute(
            int vboId,
            int index,
            int nrOfFloatComponents,
            int nrOfAllFloats,
            int startPoint,
            boolean instancedRendering
    ) {
        bindVbo(vboId);

        glEnableVertexAttribArray(index);
        glVertexAttribPointer(
                index,
                nrOfFloatComponents,
                GL_FLOAT,
                false,
                nrOfAllFloats * Float.BYTES,
                (long) startPoint * Float.BYTES
        );

        if (instancedRendering) {
            glVertexAttribDivisor(index, 1);
        }

        unbindVbo();
    }

    public static void addFloatAttribute(
            int vboId,
            int index,
            int nrOfFloatComponents,
            int nrOfAllFloats,
            int startPoint
    ) {
        addFloatAttribute(vboId, index, nrOfFloatComponents, nrOfAllFloats, startPoint, false);
    }

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    private Vbo() {}
}
