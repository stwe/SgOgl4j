/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.SgOglEngine;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class Gui {

    private final SgOglEngine engine;

    private final Vector2f topLeft;
    private final Vector2f bottomLeft;
    private final Vector2f topRight;
    private final Vector2f bottomRight;
    private final Vector2f center;

    private final ArrayList<GuiPanel> guiPanels;

    public Gui(SgOglEngine engine) {
        this.engine = engine;

        var windowWidth = (float)this.engine.getWindow().getWidth();
        var windowHeight = (float)this.engine.getWindow().getHeight();

        topLeft = new Vector2f(0.0f, 0.0f);
        bottomLeft = new Vector2f(0.0f, windowHeight);
        topRight = new Vector2f(windowWidth, 0.0f);
        bottomRight = new Vector2f(windowWidth, windowHeight);
        center = new Vector2f(windowWidth * 0.5f, windowHeight * 0.5f);

        guiPanels = new ArrayList<>();
    }

    public void addGuiPanel(GuiPanel guiPanel) {
        guiPanels.add(guiPanel);
    }

    public Vector2f getAnchorPosition(GuiAnchorPos anchorPos, Vector4f bounds) {
        switch(anchorPos) {
            case TOP_LEFT:
                topLeft.y += bounds.w;
                return topLeft;
            case BOTTOM_LEFT:
                return bottomLeft;
            case TOP_RIGHT:
                return topRight;
            case BOTTOM_RIGHT:
                return bottomRight;
            case CENTER:
                return center;
        }

        return null;
    }
}
