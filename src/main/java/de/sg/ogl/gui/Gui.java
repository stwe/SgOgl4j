/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.SgOglEngine;
import de.sg.ogl.renderer.TileRenderer;
import org.joml.Vector2f;

public class Gui {

    private final GuiQuad guiQuad;
    private final TileRenderer tileRenderer;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Gui(SgOglEngine engine) throws Exception {
        this.guiQuad = new GuiQuad(
                new Vector2f(0.0f),
                engine.getWindow().getWidth(),
                engine.getWindow().getHeight()
        );

        // don't render the root, just the children
        this.guiQuad.setRenderMe(false);

        this.tileRenderer = new TileRenderer(engine);
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void input() {
        guiQuad.input();
    }

    public void update(float dt) {}

    public void render() {
        guiQuad.render(tileRenderer);
    }

    //-------------------------------------------------
    // Add
    //-------------------------------------------------

    public void add(GuiQuad child, Anchor anchor) {
        guiQuad.add(child, anchor);
    }

    public void add(GuiQuad child) {
        guiQuad.add(child, Anchor.TOP_LEFT);
    }

    //-------------------------------------------------
    // Clean up
    //-------------------------------------------------

    public void cleanUp() {
        tileRenderer.cleanUp();
    }
}
