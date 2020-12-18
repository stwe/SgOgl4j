/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.test;

import de.sg.ogl.OpenGL;
import de.sg.ogl.SgOglEngine;
import de.sg.ogl.buffer.BufferLayout;
import de.sg.ogl.buffer.Vbo;
import de.sg.ogl.buffer.VertexAttribute;
import de.sg.ogl.resource.Mesh;
import de.sg.ogl.resource.Shader;
import de.sg.ogl.resource.Texture;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.util.ArrayList;

import static de.sg.ogl.buffer.VertexAttribute.VertexAttributeType.POSITION_2D;
import static de.sg.ogl.buffer.VertexAttribute.VertexAttributeType.UV;
import static org.lwjgl.opengl.ARBBindlessTexture.glGetTextureHandleARB;
import static org.lwjgl.opengl.ARBBindlessTexture.glMakeTextureHandleResidentARB;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.glBindBufferRange;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;

public class SpriteRenderer {

    private final SgOglEngine engine;

    private Shader shader;
    private Mesh mesh;
    private Texture water;
    private Texture grass;

    private int matricesUniformBufferId;

    private int textureUniformBufferId;
    private long[] handles = new long[4];

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public SpriteRenderer(SgOglEngine engine) throws Exception {
        this.engine = engine;

        loadShader();
        createMesh();

        water = engine.getResourceManager().loadTextureResource("/texture/tiles/water.png");
        grass = engine.getResourceManager().loadTextureResource("/texture/tiles/grass.png");

        createMatricesUniformBuffer();

        createTextureHandles();
        createTexturesUniformBuffer();
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

    public void render(Vector2f position, Vector2f size, int index) {
        shader.bind();
        mesh.initDraw();

        renderMesh(position, size, index);

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

    private void renderMesh(Vector2f position, Vector2f size, int index) {
        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix
                .identity()
                .translate(new Vector3f(position, 0.0f))
                .scale(new Vector3f(size, 1.0f));

        shader.setUniform("index", index);
        updateMatricesUniformBuffer(modelMatrix);

        mesh.drawPrimitives();
    }

    private void loadShader() throws Exception {
        shader = engine.getResourceManager().loadShaderResource("test");
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
        mesh.getVao().addVerticesVbo(vertices, 6, bufferLayout);
    }

    private void createMatricesUniformBuffer() {
        shader.setUniformBlockBindingPoint("matrices", 0);

        matricesUniformBufferId = Vbo.createVbo();
        Vbo.bindVbo(matricesUniformBufferId, GL_UNIFORM_BUFFER);
        glBufferData(GL_UNIFORM_BUFFER, 2 * 64, GL_STATIC_DRAW);
        Vbo.unbindVbo(GL_UNIFORM_BUFFER);

        glBindBufferRange(GL_UNIFORM_BUFFER, 0, matricesUniformBufferId, 0, 2 * 64);
    }

    private void updateMatricesUniformBuffer(Matrix4f modelMatrix) {
        var fb = BufferUtils.createFloatBuffer(16);

        Vbo.bindVbo(matricesUniformBufferId, GL_UNIFORM_BUFFER);
        glBufferSubData(GL_UNIFORM_BUFFER, 0, engine.getWindow().getOrthographicProjectionMatrix().get(fb));
        glBufferSubData(GL_UNIFORM_BUFFER, 64, modelMatrix.get(fb));
        Vbo.unbindVbo(GL_UNIFORM_BUFFER);
    }

    private void createTextureHandles() {
        Texture.bind(grass.getId());
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        Texture.unbind();

        handles[0] = glGetTextureHandleARB(grass.getId());
        glMakeTextureHandleResidentARB(handles[0]);
        handles[1] = 0;


        Texture.bind(water.getId());
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        Texture.unbind();

        handles[2] = glGetTextureHandleARB(water.getId());
        glMakeTextureHandleResidentARB(handles[2]);
        handles[3] = 0;
    }

    private void createTexturesUniformBuffer() {
        shader.setUniformBlockBindingPoint("maps", 1);

        textureUniformBufferId = Vbo.createVbo();
        Vbo.bindVbo(textureUniformBufferId, GL_UNIFORM_BUFFER);
        glBufferData(GL_UNIFORM_BUFFER, 32, GL_STATIC_DRAW);
        Vbo.unbindVbo(GL_UNIFORM_BUFFER);

        glBindBufferRange(GL_UNIFORM_BUFFER, 1, textureUniformBufferId, 0, 32);

        Vbo.bindVbo(textureUniformBufferId, GL_UNIFORM_BUFFER);
        glBufferSubData(GL_UNIFORM_BUFFER, 0, handles);
        Vbo.unbindVbo(GL_UNIFORM_BUFFER);
    }
}
