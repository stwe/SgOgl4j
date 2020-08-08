package de.sg.sandbox;

import de.sg.ogl.BaseApplication;
import de.sg.ogl.resource.Shader;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Sandbox extends BaseApplication {

    private int vaoId;
    private Shader shader;

    @Override
    public void init() throws Exception {
        shader = getEngine().getResourceManager().loadShaderResource("simple");

        float[] vertices = new float[]{
                0.0f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f
        };

        FloatBuffer verticesBuffer = null;
        try {
            verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
            verticesBuffer.put(vertices).flip();

            // Create the VAO and bind to it
            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Create the VBO and bint to it
            int vboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            // Enable location 0
            glEnableVertexAttribArray(0);
            // Define structure of the data
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Unbind the VBO
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            // Unbind the VAO
            glBindVertexArray(0);
        } finally {
            if (verticesBuffer != null) {
                MemoryUtil.memFree(verticesBuffer);
            }
        }
    }

    @Override
    public void input() {

    }

    @Override
    public void update() {

    }

    @Override
    public void render() {
        shader.bind();

        glBindVertexArray(vaoId);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        glBindVertexArray(0);

        Shader.unbind();
    }

    @Override
    public void cleanUp() {

    }
}
