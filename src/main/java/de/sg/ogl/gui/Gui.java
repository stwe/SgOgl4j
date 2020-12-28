/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.Input;
import de.sg.ogl.Log;
import de.sg.ogl.SgOglEngine;
import de.sg.ogl.SgOglRuntimeException;
import de.sg.ogl.physics.Aabb;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Gui {

    private final SgOglEngine engine;

    private final SpriteBatch spriteBatch;
    private final ArrayList<GuiPanel> guiPanels = new ArrayList<>();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Gui(SgOglEngine engine) throws Exception {
        this.engine = engine;
        spriteBatch = new SpriteBatch(engine);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public ArrayList<GuiPanel> getGuiPanels() {
        return guiPanels;
    }

    //-------------------------------------------------
    // Add Panel
    //-------------------------------------------------

    public GuiPanel addPanel(GuiPanel.AnchorPosition anchorPosition, Vector2f offset, float width, float height) {
        var panel = new GuiPanel(
                getAnchorScreenPosition(anchorPosition, width, height),
                offset,
                width, height
        );

        guiPanels.add(panel);

        return panel;
    }

    public GuiPanel addPanel(GuiPanel.AnchorPosition anchorPosition, Vector2f offset, float width, float height, int textureId) {
        var panel = addPanel(anchorPosition, offset, width, height);
        panel.textureId = textureId;

        return panel;
    }

    public GuiPanel addPanel(GuiPanel.AnchorPosition anchorPosition, Vector2f offset, float width, float height, Vector3f color) {
        var panel = addPanel(anchorPosition, offset, width, height);
        panel.color = color;

        return panel;
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    public void initRender() {
        for (var panel : guiPanels) {
            panel.addToRenderer(spriteBatch);
        }

        spriteBatch.end();
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void input() {
        if (Input.isMouseInWindow()) {
            for (var panel : guiPanels) {
                if (isMouseIn(panel)) {
                    if (Input.isMouseButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
                        panel.onNotify(GuiObject.GuiEvent.CLICKED);
                    } else {
                        panel.onNotify(GuiObject.GuiEvent.HOVER);
                    }

                    for (var child : panel.getGuiObjects()) {
                        if (isMouseIn(child)) {
                            Log.LOGGER.debug("Button");
                        }
                    }

                } else {
                    panel.onNotify(GuiObject.GuiEvent.RELEASED);
                }

            }
        }
    }

    public void render() {
        spriteBatch.render();
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private boolean isMouseIn(GuiObject guiObject) {
        return Aabb.pointVsAabb(Input.getCurrentMouseXY(), guiObject.aabb);
    }

    private Vector2f getAnchorScreenPosition(GuiPanel.AnchorPosition anchorPosition, float width, float height) {
        Vector2f position;
        switch(anchorPosition) {
            case TOP_LEFT:
                position = new Vector2f(engine.getWindow().getTopLeft());
                break;
            case BOTTOM_LEFT:
                position = new Vector2f(engine.getWindow().getBottomLeft().x, engine.getWindow().getBottomLeft().y - height);
                break;
            case BOTTOM_RIGHT:
                position = new Vector2f(engine.getWindow().getBottomRight().x - width, engine.getWindow().getBottomRight().y - height);
                break;
            case TOP_RIGHT:
                position = new Vector2f(engine.getWindow().getTopRight().x - width, engine.getWindow().getTopRight().y);
                break;
            case CENTER:
                position = new Vector2f(engine.getWindow().getCenter().x - width * 0.5f, engine.getWindow().getCenter().y - height * 0.5f);
                break;
            default:
                throw new SgOglRuntimeException("Invalid anchor position type: " + anchorPosition);
        }

        return position;
    }
}
