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

        var entities = manager.getEntities();

        for (int i = 0; i < manager.getEntityCount(); i++) {
            var e = entities.get(i);

            if (manager.matchesSignature(e.id, "brickSig")) {
                var colorTextureComp = manager.getComponent(e.id, ColorTextureComponent.class).get();
                var healthComp = manager.getComponent(e.id, HealthComponent.class).get();
                var transformComp = manager.getComponent(e.id, TransformComponent.class).get();

                if (!healthComp.isDestroyed()) {
                    Texture.bindForReading(colorTextureComp.getTexture().getId(), 0);

                    Matrix4f modelMatrix = new Matrix4f();
                    modelMatrix
                            .identity()
                            .translate(new Vector3f(transformComp.getPosition(), 0.0f))
                            .translate(new Vector3f(transformComp.getSize().x * 0.5f, transformComp.getSize().y * 0.5f, 0.0f))
                            .rotateZ((float) Math.toRadians(transformComp.getRotation()))
                            .translate(new Vector3f(transformComp.getSize().x * -0.5f, transformComp.getSize().y * -0.5f, 0.0f))
                            .scale(new Vector3f(transformComp.getSize(), 1.0f));

                    shader.setUniform("model", modelMatrix);
                    shader.setUniform("projection", engine.getWindow().getOrthographicProjectionMatrix());
                    shader.setUniform("diffuseMap", 0);
                    shader.setUniform("color", colorTextureComp.getColor());

                    mesh.initDraw();
                    mesh.drawPrimitives();
                    mesh.endDraw();
                }
            } else if (manager.matchesSignature(e.id, "paddleSig")) {
                var colorTextureComp = manager.getComponent(e.id, ColorTextureComponent.class).get();
                var transformComp = manager.getComponent(e.id, TransformComponent.class).get();

                Texture.bindForReading(colorTextureComp.getTexture().getId(), 0);

                Matrix4f modelMatrix = new Matrix4f();
                modelMatrix
                        .identity()
                        .translate(new Vector3f(transformComp.getPosition(), 0.0f))
                        .translate(new Vector3f(transformComp.getSize().x * 0.5f, transformComp.getSize().y * 0.5f, 0.0f))
                        .rotateZ((float) Math.toRadians(transformComp.getRotation()))
                        .translate(new Vector3f(transformComp.getSize().x * -0.5f, transformComp.getSize().y * -0.5f, 0.0f))
                        .scale(new Vector3f(transformComp.getSize(), 1.0f));

                shader.setUniform("model", modelMatrix);
                shader.setUniform("projection", engine.getWindow().getOrthographicProjectionMatrix());
                shader.setUniform("diffuseMap", 0);
                shader.setUniform("color", colorTextureComp.getColor());

                mesh.initDraw();
                mesh.drawPrimitives();
                mesh.endDraw();
            } else if (manager.matchesSignature(e.id, "ballSig")) {
                var colorTextureComp = manager.getComponent(e.id, ColorTextureComponent.class).get();
                var transformComp = manager.getComponent(e.id, TransformComponent.class).get();

                Texture.bindForReading(colorTextureComp.getTexture().getId(), 0);

                Matrix4f modelMatrix = new Matrix4f();
                modelMatrix
                        .identity()
                        .translate(new Vector3f(transformComp.getPosition(), 0.0f))
                        .translate(new Vector3f(transformComp.getSize().x * 0.5f, transformComp.getSize().y * 0.5f, 0.0f))
                        .rotateZ((float) Math.toRadians(transformComp.getRotation()))
                        .translate(new Vector3f(transformComp.getSize().x * -0.5f, transformComp.getSize().y * -0.5f, 0.0f))
                        .scale(new Vector3f(transformComp.getSize(), 1.0f));

                shader.setUniform("model", modelMatrix);
                shader.setUniform("projection", engine.getWindow().getOrthographicProjectionMatrix());
                shader.setUniform("diffuseMap", 0);
                shader.setUniform("color", colorTextureComp.getColor());

                mesh.initDraw();
                mesh.drawPrimitives();
                mesh.endDraw();
            }
        }

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

    //-------------------------------------------------
    // Clean up
    //-------------------------------------------------

    public void cleanUp() {
        mesh.cleanUp();
    }
}
