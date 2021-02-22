/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.SgOglEngine;
import de.sg.ogl.gui.widget.GuiPanel;
import de.sg.ogl.renderer.TileRenderer;
import org.joml.Vector2f;

import java.util.Objects;

public class Gui {

    private final GuiPanel mainPanel;
    private final TileRenderer tileRenderer;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Gui(SgOglEngine engine) throws Exception {
        Objects.requireNonNull(engine, "engine must not be null");

        this.mainPanel = new GuiPanel(
                new Vector2f(0.0f),
                engine.getWindow().getWidth(),
                engine.getWindow().getHeight()
        );

        // don't render the root, just the children
        this.mainPanel.setRenderMe(false);

        this.tileRenderer = new TileRenderer(engine);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public GuiPanel getMainPanel() {
        return mainPanel;
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void input() {
        mainPanel.input();
    }

    public void update(float dt) {
        mainPanel.update(dt);
    }

    public void render() {
        mainPanel.render(tileRenderer);
    }

    //-------------------------------------------------
    // Clean up
    //-------------------------------------------------

    public void cleanUp() {
        tileRenderer.cleanUp();
    }
}
