package de.sg.sandbox;

import de.sg.ogl.BaseApplication;
import de.sg.ogl.buffer.Vao;
import de.sg.ogl.resource.Shader;

import static org.lwjgl.opengl.GL15.*;

public class Sandbox extends BaseApplication {

    private Vao vao;
    private Shader shader;

    @Override
    public void init() throws Exception {
        shader = getEngine().getResourceManager().loadShaderResource("simple");

        float[] vertices = new float[]{
                0.0f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f
        };

        vao = new Vao();
        vao.addVertexDataVbo(vertices, 3);
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

        vao.bind();
        vao.drawPrimitives(GL_TRIANGLES);
        Vao.unbind();

        Shader.unbind();
    }

    @Override
    public void cleanUp() {
        vao.cleanUp();
    }
}
