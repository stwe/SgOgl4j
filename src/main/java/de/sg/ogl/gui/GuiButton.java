/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.Color;
import de.sg.ogl.renderer.TileRenderer;
import de.sg.ogl.resource.Texture;
import org.joml.Vector2f;

public class GuiButton extends GuiObject {

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    GuiButton(GuiQuad parentQuad, Anchor anchor, Vector2f offset, float width, float height, Color color, Texture texture) {
        super(parentQuad, anchor, offset, width, height, color, texture);
    }

    GuiButton(GuiQuad parentQuad, Anchor anchor, Vector2f offset, float width, float height, Color color) {
        super(parentQuad, anchor, offset, width, height, color);
    }

    GuiButton(GuiQuad parentQuad, Anchor anchor, Vector2f offset, float width, float height, Texture texture) {
        super(parentQuad, anchor, offset, width, height, texture);
    }

    GuiButton(GuiQuad parentQuad, Anchor anchor, Vector2f offset, float width, float height) {
        super(parentQuad, anchor, offset, width, height);
    }

    //-------------------------------------------------
    // Implement GuiObject
    //-------------------------------------------------

    @Override
    public void input() {

    }

    @Override
    public void update() {

    }

    @Override
    public void render(TileRenderer tileRenderer) {
        tileRenderer.render(
                getTexture(),
                getRenderOrigin(),
                getSize()
        );
    }
}
