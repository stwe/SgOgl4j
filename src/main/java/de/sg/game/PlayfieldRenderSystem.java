package de.sg.game;

import de.sg.ogl.OpenGL;
import de.sg.ogl.SgOglEngine;
import de.sg.ogl.buffer.BufferLayout;
import de.sg.ogl.buffer.VertexAttribute;
import de.sg.ogl.ecs.Manager;
import de.sg.ogl.resource.Mesh;
import de.sg.ogl.resource.Shader;
import de.sg.ogl.resource.Texture;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static de.sg.ogl.buffer.VertexAttribute.VertexAttributeType.POSITION_2D;
import static de.sg.ogl.buffer.VertexAttribute.VertexAttributeType.UV;

public class PlayfieldRenderSystem {

    private final SgOglEngine engine;
    private final Manager manager;

    private Mesh mesh;
    private Shader shader;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public PlayfieldRenderSystem(SgOglEngine engine, Manager manager) {
        this.engine = engine;
        this.manager = manager;
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

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

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void render() {
        shader.bind();
        mesh.initDraw();

        // get entities by signature
        var brickEntities = manager.getEntities("brickSig");
        var paddleEntities = manager.getEntities("paddleSig");
        var ballEntities = manager.getEntities("ballSig");

        // render bricks
        for (var e : brickEntities) {
            var colorTextureComp = manager.getComponent(e.id, ColorTextureComponent.class).orElseThrow();
            var healthComp = manager.getComponent(e.id, HealthComponent.class).orElseThrow();
            var transformComp = manager.getComponent(e.id, TransformComponent.class).orElseThrow();

            if (!healthComp.isDestroyed()) {
                renderMesh(
                        colorTextureComp.getTexture().getId(),
                        transformComp.getPosition(),
                        transformComp.getRotation(),
                        transformComp.getSize(),
                        colorTextureComp.getColor()
                );
            }
        }

        // render player / paddle
        for (var e : paddleEntities) {
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

        // render ball
        for (var e : ballEntities) {
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

    //-------------------------------------------------
    // Clean up
    //-------------------------------------------------

    public void cleanUp() {
        mesh.cleanUp();
    }
}
