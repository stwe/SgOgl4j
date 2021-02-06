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

import java.util.ArrayList;

public class GuiPanel extends GuiObject {

    private final ArrayList<GuiObject> guiObjects = new ArrayList<>();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    GuiPanel(GuiQuad parentQuad, Anchor anchor, Vector2f offset, float width, float height, Color color, Texture texture) {
        super(parentQuad, anchor, offset, width, height, color, texture);
    }

    GuiPanel(GuiQuad parentQuad, Anchor anchor, Vector2f offset, float width, float height, Color color) {
        super(parentQuad, anchor, offset, width, height, color);
    }

    GuiPanel(GuiQuad parentQuad, Anchor anchor, Vector2f offset, float width, float height, Texture texture) {
        super(parentQuad, anchor, offset, width, height, texture);
    }

    GuiPanel(GuiQuad parentQuad, Anchor anchor, Vector2f offset, float width, float height) {
        super(parentQuad, anchor, offset, width, height);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public ArrayList<GuiObject> getGuiObjects() {
        return guiObjects;
    }

    //-------------------------------------------------
    // Button
    //-------------------------------------------------

    public GuiButton addButton(Anchor anchor, Vector2f offset, float width, float height, Color color, Texture texture) {
        var button = new GuiButton(getGuiObjectQuad(), anchor, offset, width, height, color, texture);
        guiObjects.add(button);

        return button;
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
