/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.renderer;

import de.sg.ogl.Color;
import de.sg.ogl.Log;
import de.sg.ogl.OpenGL;
import de.sg.ogl.SgOglEngine;
import de.sg.ogl.buffer.Vao;
import de.sg.ogl.resource.Geometry;
import de.sg.ogl.resource.Shader;
import de.sg.ogl.resource.Texture;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Objects;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;

public class TileRenderer {

    private final SgOglEngine engine;

    private final Geometry quadGeometry;
    private final Shader shader;
    private final Vao vao;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public TileRenderer(SgOglEngine engine) throws Exception {
        LOGGER.debug("Creates TileRenderer object.");

        this.engine = Objects.requireNonNull(engine, "engine must not be null");

        this.quadGeometry = engine.getResourceManager().loadGeometry(Geometry.GeometryId.QUAD_2D);
        this.shader = engine.getResourceManager().loadResource(Shader.class, "tile");
        this.vao = new Vao();

        this.initVao();
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void render(int textureId, Vector2f position, Vector2f size) {
        OpenGL.enableAlphaBlending();
        shader.bind();
        Texture.bindForReading(textureId, GL_TEXTURE0);

        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix
                .identity()
                .translate(new Vector3f(position, 0.0f))
                .scale(new Vector3f(size, 1.0f));

        shader.setUniform("model", modelMatrix);
        shader.setUniform("projection", new Matrix4f(engine.getWindow().getOrthographicProjectionMatrix()));
        shader.setUniform("hasTexture", true);
        shader.setUniform("color", Color.WHITE.toVector4f()); // dummy color
        shader.setUniform("diffuseMap", 0);

        vao.bind();
        vao.drawPrimitives(GL_TRIANGLES);
        vao.unbind();

        OpenGL.disableBlending();
        Shader.unbind();
        Texture.unbind();
    }

    public void render(Texture texture, Vector2f position, Vector2f size) {
        render(texture.getId(), position, size);
    }

    public void render(Color color, Vector2f position, Vector2f size) {
        OpenGL.enableAlphaBlending();
        shader.bind();

        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix
                .identity()
                .translate(new Vector3f(position, 0.0f))
                .scale(new Vector3f(size, 1.0f));

        shader.setUniform("model", modelMatrix);
        shader.setUniform("projection", new Matrix4f(engine.getWindow().getOrthographicProjectionMatrix()));
        shader.setUniform("hasTexture", false);
        shader.setUniform("color", color.toVector4f());
        shader.setUniform("diffuseMap", 0); // dummy value

        vao.bind();
        vao.drawPrimitives(GL_TRIANGLES);
        vao.unbind();

        OpenGL.disableBlending();
        Shader.unbind();
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    private void initVao() {
        vao.addVbo(quadGeometry.vertices, quadGeometry.drawCount, quadGeometry.defaultBufferLayout);
    }

    //-------------------------------------------------
    // Clean up
    //-------------------------------------------------

    public void cleanUp() {
        Log.LOGGER.debug("Clean up TileRenderer.");

        this.vao.cleanUp();
    }
}
