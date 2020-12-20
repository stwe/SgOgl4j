/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.isomap;

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

public class SpriteRenderer {

    private final SgOglEngine engine;

    private Mesh mesh;
    private Shader shader;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public SpriteRenderer(SgOglEngine engine) throws Exception {
        this.engine = engine;
        createMesh();
        loadShader();
    }

    //-------------------------------------------------
    // Render
    //-------------------------------------------------

    public void prepareRendering() {
        OpenGL.enableAlphaBlending();
    }

    public void finishRendering() {
        OpenGL.disableBlending();
    }

    public void render(int textureId, Vector2f position, float rotation, Vector2f size) {
        shader.bind();
        mesh.initDraw();

        renderMesh(textureId, position, rotation, size);

        mesh.endDraw();
        Shader.unbind();
    }

    //-------------------------------------------------
    // Clean up
    //-------------------------------------------------

    public void cleanUp() {
        mesh.cleanUp();
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private void renderMesh(int textureId, Vector2f position, float rotation, Vector2f size) {
        Texture.bindForReading(textureId, 0);

        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix
                .identity()
                .translate(new Vector3f(position, 0.0f))
                .translate(new Vector3f(size.x * 0.5f, size.y * 0.5f, 0.0f))
                .rotateZ((float) Math.toRadians(rotation))
                .translate(new Vector3f(size.x * -0.5f, size.y * -0.5f, 0.0f))
                .scale(new Vector3f(size, 1.0f));

        shader.setUniform("model", modelMatrix);
        shader.setUniform("projection", engine.getWindow().getOrthographicProjectionMatrix());
        shader.setUniform("diffuseMap", 0);

        mesh.drawPrimitives();
    }

    private void loadShader() throws Exception {
        shader = engine.getResourceManager().loadShaderResource("iso");
    }

    private void createMesh() {
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
        mesh.getVao().addVbo(vertices, 6, bufferLayout);
    }
}
