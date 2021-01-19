/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.renderer;

import de.sg.ogl.SgOglEngine;
import de.sg.ogl.SgOglRuntimeException;
import de.sg.ogl.buffer.Vao;
import de.sg.ogl.buffer.Vbo;
import de.sg.ogl.buffer.Vertex2D;
import de.sg.ogl.resource.Geometry;
import de.sg.ogl.resource.Shader;
import org.joml.Vector4f;

import java.util.Objects;

import static de.sg.ogl.Log.LOGGER;
import static de.sg.ogl.buffer.Vbo.*;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;

public class BatchRenderer {

    private final SgOglEngine engine;
    private final int maxQuads;

    private final Geometry quadGeometry;

    private final int verticesPerQuad;
    private final int floatsPerVertex;
    private final int floatsPerQuad;
    private final int maxFloats;

    private final Shader shader;

    private int currentNrOfQuads;
    private boolean hasRoom;

    private final float[] floats;
    private final Sprite[] quads;

    private final Vao vao;
    private int vboId;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public BatchRenderer(SgOglEngine engine, int maxQuads) throws Exception {
        Objects.requireNonNull(engine, "engine must not be null");

        LOGGER.debug("Creates BatchRenderer object.");

        this.engine = engine;
        this.maxQuads = maxQuads;

        this.quadGeometry = engine.getResourceManager().loadGeometry(Geometry.GeometryId.QUAD_2D);

        this.verticesPerQuad = quadGeometry.vertices.length;
        this.floatsPerVertex = quadGeometry.defaultBufferLayout.getComponentCount();
        this.floatsPerQuad = verticesPerQuad * floatsPerVertex;
        this.maxFloats = floatsPerQuad * maxQuads;

        this.shader = engine.getResourceManager().loadResource(Shader.class, "batch");

        this.currentNrOfQuads = 0;
        this.hasRoom = true;

        this.floats = new float[maxFloats];
        this.quads = new Sprite[maxQuads];

        this.vao = new Vao();

        initVao();
    }

    //-------------------------------------------------
    // Add
    //-------------------------------------------------

    public void addQuad(Sprite sprite) {
        if (!hasRoom) {
            throw new SgOglRuntimeException("Invalid number of sprites.");
        }

        var index = currentNrOfQuads;
        quads[index] = Objects.requireNonNull(sprite, "sprite must not be null");
        currentNrOfQuads++;

        loadVertexProperties(index);

        if (currentNrOfQuads >= maxQuads) {
            hasRoom = false;
        }

        storeData();
    }

    private void storeData() {
        Vbo.storeData(vboId, floats);
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void render() {
        if (currentNrOfQuads == 0) {
            return;
        }

        vao.bind();
        shader.bind();

        shader.setUniform("projection", engine.getWindow().getOrthographicProjectionMatrix());
        vao.drawPrimitives(GL_TRIANGLES);

        Shader.unbind();
        Vao.unbind();
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    private void initVao() {
        // bind the Vao
        vao.bind();

        // create a new Vbo
        vboId = createVbo();

        // store Vbo Id
        vao.getVbos().add(vboId);

        // set Vertex2D buffer layout
        Vbo.setBufferLayout(vboId, Vertex2D.BUFFER_LAYOUT_2D);

        // allocate space for all floats
        Vbo.initEmpty(vboId, maxFloats, Float.BYTES, GL_DYNAMIC_DRAW);

        // unbind Vao
        Vao.unbind();
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private void loadVertexProperties(int index) {
        var sprite = quads[index];
        int offset = index * floatsPerQuad;

        for (int i = 0; i < verticesPerQuad; i++) {
            // to world space
            var worldPosition = sprite.getTransformationMatrix().transform(new Vector4f(quadGeometry.vertices[i].position, 0.0f, 1.0f));

            // position (2 floats)
            floats[offset] = worldPosition.x;
            floats[offset + 1] = worldPosition.y;

            // color (3 floats)
            var color = sprite.color.toVector3f();
            floats[offset + 2] = color.x;
            floats[offset + 3] = color.y;
            floats[offset + 4] = color.z;

            // uv (2 floats)
            floats[offset + 5] = quadGeometry.vertices[i].uv.x;
            floats[offset + 6] = quadGeometry.vertices[i].uv.y;

            // next vertex
            offset += floatsPerVertex;
        }

        vao.setDrawCount((index + 1) * verticesPerQuad);
    }

    //-------------------------------------------------
    // Clean up
    //-------------------------------------------------

    public void cleanUp() {
        LOGGER.debug("Clean up BatchRenderer.");

        vao.cleanUp();
    }
}
