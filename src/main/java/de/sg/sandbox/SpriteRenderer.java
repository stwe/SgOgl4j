package de.sg.sandbox;

import de.sg.ogl.OpenGL;
import de.sg.ogl.SgOglEngine;
import de.sg.ogl.buffer.BufferLayout;
import de.sg.ogl.buffer.VertexAttribute;
import de.sg.ogl.resource.Mesh;
import de.sg.ogl.resource.Shader;
import de.sg.ogl.resource.Texture;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static de.sg.ogl.buffer.VertexAttribute.VertexAttributeType.*;

public class SpriteRenderer implements Renderer {

    private final SgOglEngine engine;

    private Mesh mesh;
    private Shader shader;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public SpriteRenderer(SgOglEngine engine) {
        this.engine = engine;
    }

    //-------------------------------------------------
    // Implement Renderer
    //-------------------------------------------------

    @Override
    public void init() throws Exception {
        float[] vertices = new float[] {
                // pos      // tex
                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f,

                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 0.0f
        };

        BufferLayout bufferLayout = new BufferLayout(
                new ArrayList<>(){{
                    add(new VertexAttribute(POSITION_2D, "aPosition"));
                    add(new VertexAttribute(UV, "aUv"));
                }}
        );

        mesh = new Mesh();
        mesh.getVao().addVerticesVbo(vertices, 6, bufferLayout);

        shader = engine.getResourceManager().loadShaderResource("sprite");
    }

    @Override
    public void input() {}

    @Override
    public void update(float dt) {}

    @Override
    public void render(Texture texture, Vector2f position, Vector2f size, float rotate, Vector3f color) {
        shader.bind();

        Texture.bindForReading(texture.getId(), 0);

        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix
                .identity()
                .translate(new Vector3f(position, 0.0f))
                .translate(new Vector3f(size.x * 0.5f, size.y * 0.5f, 0.0f))
                .rotateZ((float)Math.toRadians(rotate))
                .translate(new Vector3f(size.x * -0.5f, size.y * -0.5f, 0.0f))
                .scale(new Vector3f(size, 1.0f));

        shader.setUniform("model", modelMatrix);
        shader.setUniform("projection", engine.getWindow().getOrthographicProjectionMatrix());
        shader.setUniform("diffuseMap", 0);
        shader.setUniform("color", color);

        mesh.initDraw();
        mesh.drawPrimitives();
        mesh.endDraw();

        Shader.unbind();
    }

    @Override
    public void prepareRendering() {
        OpenGL.enableAlphaBlending();
    }

    @Override
    public void finishRendering() {
        OpenGL.disableBlending();
    }

    @Override
    public void cleanUp() {
        mesh.cleanUp();
    }
}
