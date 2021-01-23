/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.buffer;

import de.sg.ogl.SgOglRuntimeException;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

public class Vao implements Buffer {

    private int id;

    private final ArrayList<Vbo> vbos;
    private Ebo ebo;

    private int drawCount;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Vao() {
        LOGGER.debug("Creates Vao object.");

        create();

        vbos = new ArrayList<>();
        ebo = null;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public ArrayList<Vbo> getVbos() {
        return vbos;
    }

    public Ebo getEbo() {
        return ebo;
    }

    public int getDrawCount() {
        return drawCount;
    }

    public boolean hasIndexBuffer() {
        return ebo != null;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setDrawCount(int drawCount) {
        this.drawCount = drawCount;
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
        id = glGenVertexArrays();
        if (id == 0) {
            throw new SgOglRuntimeException("Vao creation has failed.");
        }

        LOGGER.debug("A new Vao was created. The Id is {}.", id);
    }

    @Override
    public void delete() {
        if (id > 0) {
            glDeleteVertexArrays(id);
            LOGGER.debug("Vao {} was deleted.", id);
        }
    }

    @Override
    public void bind() {
        glBindVertexArray(id);
    }

    @Override
    public void unbind() {
        glBindVertexArray(0);
    }

    @Override
    public void cleanUp() {
        LOGGER.debug("Clean up Vao.");

        glDisableVertexAttribArray(0);

        for (var vbo : vbos) {
            vbo.cleanUp();
        }

        if (hasIndexBuffer()) {
            ebo.cleanUp();
        }

        unbind();
        delete();
    }

    //-------------------------------------------------
    // Add Vbo
    //-------------------------------------------------

    public Vbo addVbo() {
        var vbo = new Vbo();
        vbos.add(vbo);

        return vbo;
    }

    public Vbo addVbo(float[] vertices, int drawCount, BufferLayout bufferLayout) {
        // bind this Vao
        bind();

        // create a new Vbo
        var vbo = addVbo();

        // bind the new Vbo
        vbo.bind();

        // store vertices
        FloatBuffer verticesBuffer = null;
        try {
            verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
            verticesBuffer.put(vertices).flip();

            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

            bufferLayout.createBufferLayout();

        } finally {
            if (verticesBuffer != null) {
                MemoryUtil.memFree(verticesBuffer);
            }
        }

        // unbind Vbo
        vbo.unbind();

        // unbind Vao
        unbind();

        // set draw count - where 0 is a valid value
        if (drawCount >= 0) {
            setDrawCount(drawCount);
        }

        return vbo;
    }

    public Vbo addVbo(float[] vertices, BufferLayout bufferLayout) {
        return addVbo(vertices, -1, bufferLayout);
    }

    public Vbo addVbo(Vertex2D[] vertices, int drawCount, BufferLayout bufferLayout) {
        return addVbo(Vertex2D.toFloatArray(vertices), drawCount, bufferLayout);
    }

    //-------------------------------------------------
    // Add Ebo
    //-------------------------------------------------

    public Ebo addIndexBuffer(int[] indices) {
        // bind this Vao
        bind();

        // create a new Ebo
        ebo = new Ebo();

        // bind the new Ebo
        ebo.bind();

        // store indices
        IntBuffer indicesBuffer = null;
        try {
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();

            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        } finally {
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }

        // unbind Ebo
        ebo.unbind();

        // unbind Vao
        unbind();

        // override draw count
        setDrawCount(indices.length);

        return ebo;
    }

    //-------------------------------------------------
    // Draw
    //-------------------------------------------------

    public void drawPrimitives(int drawMode, int first) {
        if (hasIndexBuffer()) {
            glDrawElements(drawMode, drawCount, GL_UNSIGNED_INT, 0);
        } else {
            glDrawArrays(drawMode, first, drawCount);
        }
    }

    public void drawPrimitives(int drawMode) {
        drawPrimitives(drawMode, 0);
    }
}
