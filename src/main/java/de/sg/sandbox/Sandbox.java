package de.sg.sandbox;

import de.sg.ogl.BaseApplication;
import de.sg.ogl.Input;
import de.sg.ogl.buffer.BufferLayout;
import de.sg.ogl.buffer.Vao;
import de.sg.ogl.buffer.VertexAttribute;
import de.sg.ogl.resource.Shader;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.ArrayList;

import static de.sg.ogl.buffer.VertexAttribute.VertexAttributeType.COLOR;
import static de.sg.ogl.buffer.VertexAttribute.VertexAttributeType.POSITION;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.opengl.GL15.*;

public class Sandbox extends BaseApplication {

    private Vao vao;
    private Shader shader;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Sandbox() throws IOException, IllegalAccessException {
    }

    //-------------------------------------------------
    // Override
    //-------------------------------------------------

    @Override
    public void init() throws Exception {
        shader = getEngine().getResourceManager().loadShaderResource("simple");

        float[] vertices = new float[] {
                -0.5f,  0.5f, 0.0f, 0.5f, 0.0f, 0.0f,
                -0.5f, -0.5f, 0.0f, 0.0f, 0.5f, 0.0f,
                0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.5f,
                0.5f,  0.5f, 0.0f, 0.0f, 0.5f, 0.5f,
        };

        int[] indices = new int[] {
                0, 1, 3, 3, 1, 2,
        };

        BufferLayout bufferLayout = new BufferLayout(
                new ArrayList<>(){{
                    add(new VertexAttribute(POSITION, "aPosition"));
                    add(new VertexAttribute(COLOR, "aColor"));
                }}
        );

        vao = new Vao();
        vao.addVerticesVbo(vertices, 3, bufferLayout);
        vao.addIndicesEbo(indices);
    }

    @Override
    public void input() {
        // close app
        if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(getEngine().getWindow().getWindowHandle(), true);
        }

        // test keyboard
        if (Input.isKeyDown(GLFW.GLFW_KEY_D)) {
            System.out.println("isKeyDown Taste D");
        }

        if (Input.isKeyPressed(GLFW.GLFW_KEY_P)) {
            System.out.println("isKeyPressed Taste P");
        }

        if (Input.isKeyReleased(GLFW.GLFW_KEY_R)) {
            System.out.println("isKeyReleased Taste R");
        }

        // test mouse
        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            System.out.println("isButtonDown linke Maustaste");
        }

        if (Input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_MIDDLE)) {
            System.out.println("isMouseButtonPressed mittlere Maustaste");
        }

        if (Input.isMouseButtonReleased(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            System.out.println("isMouseButtonReleased rechte Maustaste");
        }
    }

    @Override
    public void update() {
        getEngine().getInput().update();
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
