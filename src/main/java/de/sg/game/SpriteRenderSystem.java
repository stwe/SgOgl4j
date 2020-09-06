/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.game;

import de.sg.ogl.OpenGL;
import de.sg.ogl.SgOglEngine;
import de.sg.ogl.ecs.Manager;
import de.sg.ogl.ecs.System;
import de.sg.ogl.resource.Mesh;
import de.sg.ogl.resource.Shader;
import de.sg.ogl.resource.Texture;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class SpriteRenderSystem implements System {

    private final SgOglEngine engine;
    private final Manager manager;
    private final Mesh mesh;

    private Shader shader;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public SpriteRenderSystem(SgOglEngine engine, Manager manager, Mesh mesh) {
        this.engine = engine;
        this.manager = manager;
        this.mesh = mesh;
    }

    //-------------------------------------------------
    // Implement System
    //-------------------------------------------------

    @Override
    public void init() throws Exception {
        shader = engine.getResourceManager().loadShaderResource("sprite");
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render() {
        shader.bind();
        mesh.initDraw();

        var entities = manager.getEntities(MeshComponent.class);

        for (var e : entities) {
            var colorTextureComp = manager.getComponent(e.id, ColorTextureComponent.class).orElseThrow();
            var transformComp = manager.getComponent(e.id, TransformComponent.class).orElseThrow();

            renderMesh(
                    colorTextureComp.getTexture().getId(),
                    transformComp.getPosition(),
                    transformComp.getRotation(),
                    transformComp.getSize(),
                    colorTextureComp.getColor()
            );
        }

        mesh.endDraw();
        Shader.unbind();
    }

    @Override
    public void cleanUp() {
    }

    //-------------------------------------------------
    // Render helper
    //-------------------------------------------------

    public void prepareRendering() {
        OpenGL.enableAlphaBlending();
    }

    public void finishRendering() {
        OpenGL.disableBlending();
    }

    private void renderMesh(int textureId, Vector2f position, float rotation, Vector2f size, Vector3f color) {
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
        shader.setUniform("color", color);

        mesh.drawPrimitives();
    }
}
