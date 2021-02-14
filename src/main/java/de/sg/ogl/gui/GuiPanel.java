/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.Color;
import de.sg.ogl.gui.event.GuiPanelListener;
import de.sg.ogl.input.MouseInput;
import de.sg.ogl.renderer.TileRenderer;
import de.sg.ogl.resource.Texture;
import de.sg.ogl.text.TextRenderer;
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
    // ListBox
    //-------------------------------------------------

    public GuiListBox addListBox(
            Anchor anchor,
            Vector2f offset,
            float width, float height,
            Texture texture,
            Texture up,
            Texture down,
            TextRenderer textRenderer,
            ArrayList<String> lines
    ) {
        var listBox = new GuiListBox(getGuiObjectQuad(), anchor, offset, width, height, texture, up, down, textRenderer, lines);
        guiObjects.add(listBox);

        return listBox;
    }

    //-------------------------------------------------
    // Implement GuiObject
    //-------------------------------------------------

    @Override
    public void input() {
        for (var guiObject : guiObjects) {

            // panel events ...
            // todo

            // listBox events
            if (guiObject instanceof GuiListBox) {
                var listBox = (GuiListBox)guiObject;
                var upBtn = listBox.getButtonUp();
                var downBtn = listBox.getButtonDown();
                if (upBtn.isMouseOver()) {
                    if (MouseInput.isMouseButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
                        upBtn.onClick();
                    }
                    upBtn.onHover();
                } else if(upBtn.mouseOver) {
                    upBtn.onRelease();
                }
                if (downBtn.isMouseOver()) {
                    if (MouseInput.isMouseButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
                        downBtn.onClick();
                    }
                    downBtn.onHover();
                } else if(downBtn.mouseOver) {
                    downBtn.onRelease();
                }
            }

            // button events
            if (guiObject instanceof GuiButton) {
                var btn = (GuiButton)guiObject;
                if (btn.isMouseOver()) {
                    if (MouseInput.isMouseButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
                        btn.onClick();
                    }
                    btn.onHover();
                } else if (btn.mouseOver) {
                    btn.onRelease();
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
