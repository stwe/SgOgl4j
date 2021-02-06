/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.Color;
import de.sg.ogl.event.GuiPanelListener;
import de.sg.ogl.input.MouseInput;
import de.sg.ogl.renderer.TileRenderer;
import de.sg.ogl.resource.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class GuiPanel extends GuiObject {

    private final ArrayList<GuiObject> guiObjects = new ArrayList<>();
    private final ArrayList<GuiPanelListener> listeners = new ArrayList<>();

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
    // Listener
    //-------------------------------------------------

    public void addListener(GuiPanelListener listener) {
        if (listeners.contains(listener)) {
            return;
        }

        listeners.add(listener);
    }

    public void removeListener(GuiPanelListener listener) {
        listeners.remove(listener);
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
        for (var guiObject : guiObjects) {
            if (guiObject.isMouseOver()) {
                if (guiObject instanceof GuiButton) {
                    if (MouseInput.isMouseButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
                        ((GuiButton) guiObject).onClick();
                    } else {
                        ((GuiButton) guiObject).onHover();
                    }
                }
            }
        }
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

        for (var guiObject : guiObjects) {
            guiObject.render(tileRenderer);
        }
    }
}
