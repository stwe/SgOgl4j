/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.text;

import de.sg.ogl.Color;
import de.sg.ogl.OpenGL;
import de.sg.ogl.SgOglEngine;
import de.sg.ogl.SgOglRuntimeException;
import de.sg.ogl.buffer.Vao;
import de.sg.ogl.buffer.Vbo;
import de.sg.ogl.resource.Shader;
import de.sg.ogl.resource.Texture;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.Objects;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.opengl.GL15.*;

public class TextRenderer {

    private static final String SHADER_NAME = "text";
    private static final int NR_OF_FLOATS = 4096;

    private final SgOglEngine engine;
    private final Shader shader;
    private final Font font;

    private final FloatBuffer vertices;
    private int numVertices;
    private boolean drawing;

    private Vao vao;
    private int vboId;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public TextRenderer(SgOglEngine engine, java.awt.Font awtFont) throws Exception {
        LOGGER.debug("Creates TextRenderer object.");

        this.engine = Objects.requireNonNull(engine, "engine must not be null");
        this.shader = this.engine.getResourceManager().loadResource(Shader.class, SHADER_NAME);
        this.font = new Font(Objects.requireNonNull(awtFont, "awtFont must not be null"), true);

        vertices = MemoryUtil.memAllocFloat(NR_OF_FLOATS);
        numVertices = 0;
        drawing = false;

        initVao();
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    public int getTextWidth(CharSequence text) {
        Objects.requireNonNull(text, "text must not be null");

        return font.getTextWidth(text);
    }

    public int getTextHeight(CharSequence text) {
        Objects.requireNonNull(text, "text must not be null");

        return font.getTextHeight(text);
    }

    //-------------------------------------------------
    // Render
    //-------------------------------------------------

    public void render(CharSequence text, float x, float y, Color color) {
        Objects.requireNonNull(text, "text must not be null");
        Objects.requireNonNull(color, "color must not be null");

        font.render(this, text, x, y, color);
    }

    void begin() {
        if (drawing) {
            throw new SgOglRuntimeException("TextRenderer is already drawing.");
        }

        drawing = true;
        numVertices = 0;
    }

    void end() {
        if (!drawing) {
            throw new SgOglRuntimeException("TextRenderer isn't drawing.");
        }

        drawing = false;
        flush();
    }

    void drawTextureRegion(Texture texture, float x, float y, float regX, float regY, float regWidth, float regHeight, Color color) {
        Objects.requireNonNull(texture, "texture must not be null");
        Objects.requireNonNull(color, "color must not be null");

        // Vertex positions
        float x1 = x;
        float y1 = y;
        float x2 = x + regWidth;
        float y2 = y + regHeight;

        // Texture coordinates
        float s1 = regX / texture.getWidth();
        float t1 = regY / texture.getHeight();
        float s2 = (regX + regWidth) / texture.getWidth();
        float t2 = (regY + regHeight) / texture.getHeight();

        if (vertices.remaining() < 7 * 6) {
            // We need more space in the buffer, so flush it
            flush();
        }

        float r = color.getRed();
        float g = color.getGreen();
        float b = color.getBlue();
        float a = color.getAlpha();

        vertices.put(x1).put(y1).put(r).put(g).put(b).put(a).put(s1).put(t1);
        vertices.put(x1).put(y2).put(r).put(g).put(b).put(a).put(s1).put(t2);
        vertices.put(x2).put(y2).put(r).put(g).put(b).put(a).put(s2).put(t2);

        vertices.put(x1).put(y1).put(r).put(g).put(b).put(a).put(s1).put(t1);
        vertices.put(x2).put(y2).put(r).put(g).put(b).put(a).put(s2).put(t2);
        vertices.put(x2).put(y1).put(r).put(g).put(b).put(a).put(s2).put(t1);

        numVertices += 6;
    }

    private void flush() {
        if (numVertices > 0) {
            vertices.flip();

            vao.bind();
            shader.bind();

            updateUniforms();

            // todo

            // upload the new vertex data
            //Vbo.bindVbo(vboId);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);


            OpenGL.enableAlphaBlending();
            glDrawArrays(GL_TRIANGLES, 0, numVertices);
            OpenGL.disableBlending();


            // clear vertex data for next batch
            vertices.clear();
            numVertices = 0;

            Shader.unbind();
            vao.unbind();
        }
    }

    private void updateUniforms() {
        shader.setUniform("projection", engine.getWindow().getOrthographicProjectionMatrix());
        shader.setUniform("diffuseMap", 0);
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    private void initVao() {
        LOGGER.debug("Initializing TextRenderer Vao.");

        vao = new Vao();
        vao.bind();

        /*
        vboId = Vbo.createVbo();
        Vbo.initEmpty(vboId, NR_OF_FLOATS, Float.BYTES, GL_DYNAMIC_DRAW);

        var nrOfAllFloats = 8;

        // position: 2 floats
        Vbo.addFloatAttribute(vboId, 0, 2, nrOfAllFloats, 0);

        // color: 4 floats
        Vbo.addFloatAttribute(vboId, 1, 4, nrOfAllFloats, 2);

        // uv: 2 floats
        Vbo.addFloatAttribute(vboId, 2, 2, nrOfAllFloats, 6);

        Vao.unbind();
         */
    }
}
