/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.Color;
import de.sg.ogl.gui.event.GuiButtonEvent;
import de.sg.ogl.gui.event.GuiButtonListener;
import de.sg.ogl.renderer.TileRenderer;
import de.sg.ogl.resource.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;

public class GuiButton extends GuiObject {

    private final ArrayList<GuiButtonListener> listeners = new ArrayList<>();
    public boolean mouseOver = false;

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
    // Listener
    //-------------------------------------------------

    public void addListener(GuiButtonListener listener) {
        if (listeners.contains(listener)) {
            return;
        }

        listeners.add(listener);
    }

    public void removeListener(GuiButtonListener listener) {
        listeners.remove(listener);
    }

    public void onClick() {
        if (listeners.isEmpty()) {
            return;
        }

        mouseOver = true;
        var event = new GuiButtonEvent(this);
        for (var listener : listeners) {
            listener.onClick(event);
        }
    }

    public void onHover() {
        if (listeners.isEmpty()) {
            return;
        }

        mouseOver = true;
        var event = new GuiButtonEvent(this);
        for (var listener : listeners) {
            listener.onHover(event);
        }
    }

    public void onRelease() {
        if (listeners.isEmpty()) {
            return;
        }

        mouseOver = false;
        var event = new GuiButtonEvent(this);
        for (var listener : listeners) {
            listener.onRelease(event);
        }
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
