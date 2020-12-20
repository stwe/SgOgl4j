/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
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
import static de.sg.ogl.buffer.Vbo.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

public final class Vao {

    private final int vaoId;
    private final ArrayList<Integer> vbos;
    private int eboId;
    private int drawCount;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Vao() {
        LOGGER.debug("Creates Vao object.");

        vaoId = createVao();
        vbos = new ArrayList<>();
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public int getVaoId() {
        return vaoId;
    }

    public ArrayList<Integer> getVbos() {
        return vbos;
    }

    public int getEboId() {
        return eboId;
    }

    public int getDrawCount() {
        return drawCount;
    }

    public boolean hasIndexBuffer() {
        return eboId != 0;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setDrawCount(int drawCount) {
        this.drawCount = drawCount;
    }

    //-------------------------------------------------
    // Bind / Unbind
    //-------------------------------------------------

    public void bind() {
        glBindVertexArray(vaoId);
    }

    public static void unbind() {
        glBindVertexArray(0);
    }

    //-------------------------------------------------
    // Add data
    //-------------------------------------------------

    public void addVbo(float[] vertices, int drawCount, BufferLayout bufferLayout) {
        // bind this Vao
        bind();

        // create a new Vbo
        var vboId = createVbo();

        // store Vbo Id
        vbos.add(vboId);

        // bind the new Vbo
        bindVbo(vboId);

        // store vertices
        FloatBuffer verticesBuffer = null;
        try {
            verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
            verticesBuffer.put(vertices).flip();

            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

            // specify how OpenGL should interpret the vertex data
            var index = 0;
            for (var attribute : bufferLayout.getVertexAttributes()) {
                glEnableVertexAttribArray(index);
                glVertexAttribPointer(
                        index,
                        attribute.getVertexAttributeType().getComponentCount(),
                        attribute.getVertexAttributeType().getGlType(),
                        attribute.normalize,
                        bufferLayout.getStride(),
                        attribute.offset
                );

                index++;
            }
        } finally {
            if (verticesBuffer != null) {
                MemoryUtil.memFree(verticesBuffer);
            }
        }

        // unbind buffers
        unbindVbo();
        unbind();

        // set draw count - where 0 is a valid value
        if (drawCount >= 0) {
            setDrawCount(drawCount);
        }
    }

    public void addVbo(float[] vertices, BufferLayout bufferLayout) {
        addVbo(vertices, -1, bufferLayout);
    }

    public void addIndexBuffer(int[] indices) {
        // bind this Vao
        bind();

        // create a new Ebo
        eboId = createEbo();

        // bind the new Ebo
        bindEbo(eboId);

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

        // unbind Vao
        unbind();

        // override draw count
        setDrawCount(indices.length);
    }

    //-------------------------------------------------
    // Draw
    //-------------------------------------------------

    public void drawPrimitives(int drawMode) {
        if (hasIndexBuffer()) {
            glDrawElements(drawMode, drawCount, GL_UNSIGNED_INT, 0);
        } else {
            glDrawArrays(drawMode, 0, drawCount);
        }
    }

    //-------------------------------------------------
    // Live and let die
    //-------------------------------------------------

    private static int createVao() {
        var id = glGenVertexArrays();
        if (id == 0) {
            throw new SgOglRuntimeException("Vao creation has failed.");
        }

        LOGGER.debug("A new Vao was created. The Id is {}.", id);

        return id;
    }

    private void deleteVao() {
        if (vaoId > 0) {
            glDeleteVertexArrays(vaoId);
            LOGGER.debug("Vao {} was deleted.", vaoId);
        }
    }

    //-------------------------------------------------
    // Clean up
    //-------------------------------------------------

    public void cleanUp() {
        LOGGER.debug("Clean up Vao.");

        glDisableVertexAttribArray(0);

        // delete Vbos
        unbindVbo();
        for (var vboId : vbos) {
            deleteVbo(vboId);
        }

        // delete Vbo / IndexBuffer
        if (hasIndexBuffer()) {
            deleteEbo(eboId);
        }

        // delete Vao
        unbind();
        deleteVao();
    }
}
