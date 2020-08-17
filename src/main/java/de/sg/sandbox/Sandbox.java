package de.sg.sandbox;

import de.sg.ogl.BaseApplication;
import de.sg.ogl.Input;
import de.sg.ogl.buffer.BufferLayout;
import de.sg.ogl.buffer.VertexAttribute;
import de.sg.ogl.camera.FirstPersonCamera;
import de.sg.ogl.math.Transform;
import de.sg.ogl.resource.Mesh;
import de.sg.ogl.resource.Shader;
import de.sg.ogl.resource.Texture;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.ArrayList;

import static de.sg.ogl.buffer.VertexAttribute.VertexAttributeType.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Sandbox extends BaseApplication {

    private Mesh mesh;
    private Texture texture;
    private Shader shader;
    private FirstPersonCamera camera;
    private Transform transform;

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
                 // positions         // colors           // texture coords
                 0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f,   1.0f, 1.0f,   // top right
                 0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f,   1.0f, 0.0f,   // bottom right
                -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f,   // bottom left
                -0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 1.0f    // top left
        };

        int[] indices = new int[] {
                0, 1, 3, // first triangle
                1, 2, 3  // second triangle
        };

        BufferLayout bufferLayout = new BufferLayout(
                new ArrayList<>(){{
                    add(new VertexAttribute(POSITION, "aPosition"));
                    add(new VertexAttribute(COLOR, "aColor"));
                    add(new VertexAttribute(UV, "aUv"));
                }}
        );

        texture = getEngine().getResourceManager().loadTextureResource("/texture/Grass.jpg");

        mesh = new Mesh();
        mesh.getVao().addVerticesVbo(vertices, 6, bufferLayout);
        mesh.getVao().addIndicesEbo(indices);

        // todo mesh set material

        camera = new FirstPersonCamera();
        camera.setPosition(new Vector3f(0.0f, 0.0f, -2.0f));

        transform = new Transform();
        transform.setPosition(new Vector3f(0.0f, 0.0f, 0.0f));
        transform.setRotation(new Vector3f(0.0f, 0.0f, 0.0f));
        transform.setScale(new Vector3f(1.0f, 1.0f, 1.0f));
    }

    @Override
    public void input() {
        if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(getEngine().getWindow().getWindowHandle(), true);
        }

        camera.input();
    }

    @Override
    public void update(float dt) {
        getEngine().getInput().update(dt);
        camera.update(dt);
    }

    @Override
    public void render() {
        shader.bind();

        Texture.bindForReading(texture.getId(), 0);

        shader.setUniform("p", getEngine().getWindow().getProjectionMatrix());
        shader.setUniform("v", camera.getViewMatrix());
        shader.setUniform("m", transform.getModelMatrix());
        shader.setUniform("hasDiffuseMap", true);
        shader.setUniform("diffuseMap", 0);

        mesh.initDraw();
        mesh.drawPrimitives();
        mesh.endDraw();

        Shader.unbind();
    }

    @Override
    public void cleanUp() {
        mesh.cleanUp();
    }
}
