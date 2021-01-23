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
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

public class Vbo implements Buffer {

    private int id;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Vbo() {
        LOGGER.debug("Creates Vbo object.");

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
            throw new SgOglRuntimeException("Vbo creation has failed.");
        }

        LOGGER.debug("A new Vbo was created. The Id is {}.", id);
    }

    @Override
    public void delete() {
        if (id > 0) {
            glDeleteBuffers(id);
            LOGGER.debug("Vbo {} was deleted.", id);
        }
    }

    @Override
    public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, id);
    }

    @Override
    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void cleanUp() {
        LOGGER.debug("Clean up Vbo.");

        unbind();
        delete();
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    public void initEmpty(int elementCount, int elementSizeInBytes, int usage) {
        bind();
        glBufferData(GL_ARRAY_BUFFER, (long)elementCount * elementSizeInBytes, usage);
        unbind();
    }

    //-------------------------------------------------
    // Buffer layout
    //-------------------------------------------------

    public void setBufferLayout(BufferLayout bufferLayout) {
        bind();
        bufferLayout.createBufferLayout();
        unbind();
    }

    //-------------------------------------------------
    // Store data
    //-------------------------------------------------

    public void storeData(int offset, float[] data) {
        bind();
        glBufferSubData(GL_ARRAY_BUFFER, offset, data);
        unbind();
    }

    public void storeData(float[] data) {
        storeData(0, data);
    }

    //-------------------------------------------------
    // Attributes
    //-------------------------------------------------

    public void addFloatAttribute(
            int index,
            int nrOfFloatComponents,
            int nrOfAllFloats,
            int startPoint,
            boolean instancedRendering
    ) {
        bind();

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

        unbind();
    }

    public void addFloatAttribute(
            int index,
            int nrOfFloatComponents,
            int nrOfAllFloats,
            int startPoint
    ) {
        addFloatAttribute(index, nrOfFloatComponents, nrOfAllFloats, startPoint, false);
    }
}
