/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class GuiButton extends GuiObject {

    String label;
    int textureId;
    GuiPanel guiPanel;

    public GuiButton(Vector4f bounds, Vector2f position, String label, int textureId, GuiPanel guiPanel) {

        this.bounds = bounds;
        this.position = new Vector2f(guiPanel.getPosition()).add(position);

        if (guiPanel.getGuiObjects().isEmpty()) {
            this.position.y += guiPanel.getBounds().w - this.bounds.w;
        } else {
            var last = guiPanel.getGuiObjects().get(guiPanel.getGuiObjects().size() - 1);
            this.position.y = last.position.y + last.bounds.w + 10.0f;
        }

        this.label = label;
        this.textureId = textureId;
        this.guiPanel = guiPanel;
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {
        /*
        glDepthMask(GL_FALSE);
        const glm::vec4 uvRect(0.0f, 0.0f, 1.0f, 1.0f);

        spriteBatch.Draw(glm::vec4(m_position.x, m_position.y, m_bounds.z, m_bounds.w), uvRect, m_texture.id, 0.0f, ColorRGBA8(255, 255, 255, 255));
         */
    }

    @Override
    public void onNotify(GuiObject guiObject, GuiEvent guiEvent) {

    }

    void onClick() {}
    void onRelease() {}
}
