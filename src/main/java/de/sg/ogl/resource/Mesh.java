/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.resource;

import de.sg.ogl.buffer.Vao;

import java.util.Objects;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class Mesh {

    private final Vao vao;
    private Material defaultMaterial;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Mesh() {
        LOGGER.debug("Creates Mesh object.");

        vao = new Vao();
    }

    public Mesh(Material defaultMaterial) {
        this();
        this.defaultMaterial = Objects.requireNonNull(defaultMaterial, "defaultMaterial must not be null");
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public Vao getVao() {
        return vao;
    }

    public Material getDefaultMaterial() {
        return defaultMaterial;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setDefaultMaterial(Material defaultMaterial) {
        this.defaultMaterial = Objects.requireNonNull(defaultMaterial, "defaultMaterial must not be null");
    }

    //-------------------------------------------------
    // Draw - methods created for convenience
    //-------------------------------------------------

    public void initDraw() {
        vao.bind();
    }

    public void drawPrimitives(int drawMode) {
        vao.drawPrimitives(drawMode);
    }

    public void drawPrimitives() {
        vao.drawPrimitives(GL_TRIANGLES);
    }

    public void drawInstanced() {
        // todo
    }

    public void endDraw() {
        Vao.unbind();
    }

    //-------------------------------------------------
    // Clean up
    //-------------------------------------------------

    public void cleanUp() {
        LOGGER.debug("Clean up Mesh.");

        vao.cleanUp();
    }
}
