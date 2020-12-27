/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class GuiButton extends GuiObject {

    private static final Vector3f DEFAULT_COLOR = new Vector3f(0.0f, 0.0f, 1.0f);

    private final String label;
    private final GuiPanel guiPanel;

    private int textureId;
    private Vector3f color = DEFAULT_COLOR;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public GuiButton(Vector4f bounds, Vector2f position, String label, GuiPanel guiPanel) {
        this.bounds = bounds;
        this.position = new Vector2f(guiPanel.getPosition()).add(position);

        if (guiPanel.getGuiObjects().isEmpty()) {
            //this.position.y += guiPanel.getBounds().w - this.bounds.w;
        } else {
            var last = guiPanel.getGuiObjects().get(guiPanel.getGuiObjects().size() - 1);
            this.position.y = last.position.y + last.bounds.w + 10.0f;
        }

        this.label = label;
        this.guiPanel = guiPanel;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public int getTextureId() {
        return textureId;
    }

    public Vector3f getColor() {
        return color;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    //-------------------------------------------------
    // Implement GuiObject
    //-------------------------------------------------

    @Override
    public void update() {

    }

    @Override
    public void addToRenderer(SpriteBatch spriteBatch) {
        spriteBatch.addToRenderer(
                new Vector4f(position.x, position.y, bounds.z, bounds.w),
                textureId,
                color
        );
    }

    @Override
    public void onNotify(GuiObject guiObject, GuiEvent guiEvent) {

    }
}
