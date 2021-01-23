/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.OpenGL;
import de.sg.ogl.SgOglEngine;
import de.sg.ogl.buffer.Vao;
import de.sg.ogl.buffer.Vbo;
import de.sg.ogl.buffer.Vertex2D;
import de.sg.ogl.resource.Shader;
import de.sg.ogl.resource.Texture;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Comparator;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;

public class SpriteBatch {

    private final SgOglEngine engine;
    private final Shader shader;

    private final Vao vao = new Vao();
    private Vbo vbo;

    private Glyph.GlyphSortType glyphSortType = Glyph.GlyphSortType.TEXTURE;
    private final ArrayList<Glyph> glyphs = new ArrayList<>();

    private final ArrayList<BatchRenderer> renderBatches = new ArrayList<>();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public SpriteBatch(SgOglEngine engine) throws Exception {
        LOGGER.debug("Creates SpriteBatch object.");

        this.engine = engine;
        this.shader = engine.getResourceManager().loadResource(Shader.class, "batch");

        initVao();
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    public void begin(Glyph.GlyphSortType glyphSortType) {
        this.glyphSortType = glyphSortType;

        // todo: clear arrays
    }

    public void end() {
        sortGlyphsByType();
        createRenderBatches();
    }

    //-------------------------------------------------
    // Renderer
    //-------------------------------------------------

    public void addToRenderer(Vector4f destRect, int textureId, Vector3f color) {
        glyphs.add(new Glyph(destRect, textureId, color));
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void render() {
        vao.bind();
        shader.bind();

        shader.setUniform("projection", engine.getWindow().getOrthographicProjectionMatrix());
        shader.setUniform("diffuseMap", 0);

        OpenGL.enableAlphaBlending();

        for (var batch : renderBatches) {
            Texture.bindForReading(batch.textureId, GL_TEXTURE0);
            vao.drawPrimitives(GL_TRIANGLES, batch.offset);
        }

        OpenGL.disableBlending();

        Shader.unbind();
        vao.unbind();
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private void initVao() {
        // bind the Vao
        vao.bind();

        // create a new Vbo
        vbo = vao.addVbo();

        // set Vertex2D buffer layout
        vbo.setBufferLayout(Vertex2D.BUFFER_LAYOUT_2D);

        // unbind Vao
        vao.unbind();
    }

    private void sortGlyphsByType() {
        switch (glyphSortType) {
            case NONE:
            case FRONT_TO_BACK:
            case BACK_TO_FRONT: break;
            case TEXTURE:
                glyphs.sort(Comparator.comparingInt(a -> a.textureId));
                break;
        }
    }

    private void createRenderBatches() {
        if (glyphs.isEmpty()) {
            return;
        }

        float[] floats = new float[glyphs.size() * Glyph.NR_OF_FLOATS];

        var offset = 0;
        int currentVertexValue = 0;
        int[] currentVertex = { currentVertexValue };

        // add the first batch
        renderBatches.add(new BatchRenderer(offset, Glyph.NR_OF_VERTICES, glyphs.get(0).textureId));
        addVertex2D(floats, glyphs.get(0).topLeft, currentVertex);
        addVertex2D(floats, glyphs.get(0).bottomLeft, currentVertex);
        addVertex2D(floats, glyphs.get(0).bottomRight, currentVertex);
        addVertex2D(floats, glyphs.get(0).bottomRight, currentVertex);
        addVertex2D(floats, glyphs.get(0).topRight, currentVertex);
        addVertex2D(floats, glyphs.get(0).topLeft, currentVertex);

        offset += Glyph.NR_OF_VERTICES;

        // add the rest
        for (int i = 1; i < glyphs.size(); i++) {
            if (glyphs.get(i).textureId != glyphs.get(i - 1).textureId) {
                renderBatches.add(new BatchRenderer(offset, Glyph.NR_OF_VERTICES, glyphs.get(i).textureId));
            } else {
                renderBatches.get(renderBatches.size() - 1).nrOfVertices += Glyph.NR_OF_VERTICES;
            }

            addVertex2D(floats, glyphs.get(i).topLeft, currentVertex);
            addVertex2D(floats, glyphs.get(i).bottomLeft, currentVertex);
            addVertex2D(floats, glyphs.get(i).bottomRight, currentVertex);
            addVertex2D(floats, glyphs.get(i).bottomRight, currentVertex);
            addVertex2D(floats, glyphs.get(i).topRight, currentVertex);
            addVertex2D(floats, glyphs.get(i).topLeft, currentVertex);

            offset += Glyph.NR_OF_VERTICES;
        }

        storeData(floats);
        vao.setDrawCount(offset);
    }

    private void addVertex2D(float[] vertices, Vertex2D vertex2D, int[] index) {
        vertices[index[0]++] = vertex2D.position.x;
        vertices[index[0]++] = vertex2D.position.y;

        vertices[index[0]++] = vertex2D.color.x;
        vertices[index[0]++] = vertex2D.color.y;
        vertices[index[0]++] = vertex2D.color.z;

        vertices[index[0]++] = vertex2D.uv.x;
        vertices[index[0]++] = vertex2D.uv.y;
    }

    private void storeData(float[] floats) {
        vbo.initEmpty(floats.length, Float.BYTES, GL_DYNAMIC_DRAW);
        vbo.storeData(floats);
    }
}
