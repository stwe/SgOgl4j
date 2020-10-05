/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.islands;

import de.sg.ogl.OpenGL;
import de.sg.ogl.SgOglEngine;
import de.sg.ogl.camera.OrthographicCamera;
import de.sg.ogl.resource.Mesh;
import de.sg.ogl.resource.Shader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Renderer {

    private final SgOglEngine engine;
    private final Mesh mesh;

    private Shader shader;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Renderer(SgOglEngine engine, Mesh mesh) {
        this.engine = engine;
        this.mesh = mesh;
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void init() throws Exception {
        shader = engine.getResourceManager().loadShaderResource("sprite2");
    }

    public void render(BshTexture texture, int posX, int posY, OrthographicCamera camera) {
        OpenGL.enableAlphaBlending();
        shader.bind();
        mesh.initDraw();

        renderMesh(texture, new Vector2f(posX, posY), 0.0f, camera);

        mesh.endDraw();
        Shader.unbind();
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private void renderMesh(BshTexture texture, Vector2f position, float rotation, OrthographicCamera camera) {
        glActiveTexture(texture.getTextureId());
        glBindTexture(GL_TEXTURE_2D, texture.getTextureId());

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        var size = new Vector2f(texture.getBufferedImage().getWidth(), texture.getBufferedImage().getHeight());

        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix
                .identity()
                .translate(new Vector3f(position, 0.0f))
                //.translate(new Vector3f(size.x * 0.5f, size.y * 0.5f, 0.0f))
                //.rotateZ((float) Math.toRadians(rotation))
                //.translate(new Vector3f(size.x * -0.5f, size.y * -0.5f, 0.0f))
                .scale(new Vector3f(size, 1.0f));

        shader.setUniform("model", modelMatrix);
        shader.setUniform("viewProjection", new Matrix4f(engine.getWindow().getOrthographicProjectionMatrix()).mul(camera.getViewMatrix()));
        shader.setUniform("diffuseMap", 0);

        mesh.drawPrimitives();
    }
}
