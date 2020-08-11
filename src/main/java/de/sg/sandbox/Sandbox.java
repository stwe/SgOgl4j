package de.sg.sandbox;

import de.sg.ogl.BaseApplication;
import de.sg.ogl.buffer.BufferLayout;
import de.sg.ogl.buffer.Vao;
import de.sg.ogl.buffer.VertexAttribute;
import de.sg.ogl.resource.Shader;

import java.util.ArrayList;

import static de.sg.ogl.buffer.VertexAttribute.VertexAttributeType.POSITION;
import static org.lwjgl.opengl.GL15.*;

public class Sandbox extends BaseApplication {

    private Vao vao;
    private Shader shader;

    @Override
    public void init() throws Exception {
        shader = getEngine().getResourceManager().loadShaderResource("simple");

        float[] vertices = new float[]{
                -0.5f,  0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f,  0.5f, 0.0f,
        };

        int[] indices = new int[]{
                0, 1, 3, 3, 1, 2,
        };

        BufferLayout bufferLayout = new BufferLayout(
                new ArrayList<>(){{
                    add(new VertexAttribute(POSITION, "aPosition"));
                }}
        );

        vao = new Vao();
        vao.addVerticesVbo(vertices, 3, bufferLayout);
        vao.addIndicesEbo(indices);
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

        shader.setUniform("test", 1.0f);

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
