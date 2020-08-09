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
                0.0f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f
        };

        BufferLayout bufferLayout = new BufferLayout(
                new ArrayList<>(){{
                    add(new VertexAttribute(POSITION, "aPosition"));
                }}
        );

        vao = new Vao();
        vao.addVertexDataVbo(vertices, 3, bufferLayout);
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
