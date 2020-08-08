package de.sg.ogl.buffer;

import de.sg.ogl.SgOglException;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static de.sg.ogl.Log.LOGGER;
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

    static public void unbind() {
        glBindVertexArray(0);
    }

    //-------------------------------------------------
    // Add data
    //-------------------------------------------------

    public void addVertexDataVbo(float[] vertices, int drawCount) {
        bind();

        var vboId = Vbo.createVbo();
        vbos.add(vboId);

        Vbo.bindVbo(vboId);

        FloatBuffer verticesBuffer = null;
        try {
            verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
            verticesBuffer.put(vertices).flip();

            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        } finally {
            if (verticesBuffer != null) {
                MemoryUtil.memFree(verticesBuffer);
            }
        }

        Vbo.unbindVbo();
        unbind();

        setDrawCount(drawCount);
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
    // Helper
    //-------------------------------------------------

    static private int createVao() {
        var id = glGenVertexArrays();
        if (id == 0) {
            throw new SgOglException("Vao creation has failed.");
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
        Vbo.unbindVbo();
        for (var vboId : vbos) {
            Vbo.deleteVbo(vboId);
        }

        // delete Vbo / IndexBuffer
        if (hasIndexBuffer()) {
            Vbo.deleteEbo(eboId);
        }

        // delete Vao
        unbind();
        deleteVao();
    }
}
