/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui.widget;

import de.sg.ogl.Color;
import de.sg.ogl.gui.GuiQuad;
import de.sg.ogl.gui.event.GuiButtonEvent;
import de.sg.ogl.gui.event.GuiButtonListener;
import de.sg.ogl.input.MouseInput;
import de.sg.ogl.resource.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;

public class GuiButton extends GuiQuad {

    private final ArrayList<GuiButtonListener> listeners = new ArrayList<>();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public GuiButton(Vector2f origin, float width, float height, Color color, Texture texture) {
        super(origin, width, height, color, texture);
    }

    public GuiButton(Vector2f origin, float width, float height, Color color) {
        super(origin, width, height, color);
    }

    public GuiButton(Vector2f origin, float width, float height, Texture texture) {
        super(origin, width, height, texture);
    }

    public GuiButton(Vector2f origin, float width, float height) {
        super(origin, width, height);
    }

    //-------------------------------------------------
    // Implement GuiObject
    //-------------------------------------------------

    @Override
    public void inputGuiObject() {
        if (listeners.isEmpty()) {
            return;
        }

        if (isMouseOver()) {
            if (MouseInput.isMouseButtonPressed(GLFW_MOUSE_BUTTON_LEFT) ||
                    MouseInput.isMouseButtonPressed(GLFW_MOUSE_BUTTON_RIGHT) ||
                    MouseInput.isMouseButtonPressed(GLFW_MOUSE_BUTTON_MIDDLE)
            ) {
                runOnClickListeners();
            }

            runOnHoverListeners();
        } else if (isMouseOverFlag()) {
            runOnReleaseListeners();
        }
    }

    //-------------------------------------------------
    // Listener
    //-------------------------------------------------

    public void addListener(GuiButtonListener listener) {
        Objects.requireNonNull(listener, "listener must not be null");

        if (listeners.contains(listener)) {
            return;
        }

        listeners.add(listener);
    }

    public void removeListener(GuiButtonListener listener) {
        Objects.requireNonNull(listener, "listener must not be null");
        listeners.remove(listener);
    }

    private void runOnClickListeners() {
        if (listeners.isEmpty()) {
            return;
        }

        setMouseOverFlag(true);
        var event = new GuiButtonEvent(this);
        for (var listener : listeners) {
            listener.onClick(event);
        }
    }

    private void runOnHoverListeners() {
        if (listeners.isEmpty()) {
            return;
        }

        setMouseOverFlag(true);
        var event = new GuiButtonEvent(this);
        for (var listener : listeners) {
            listener.onHover(event);
        }
    }

    private void runOnReleaseListeners() {
        if (listeners.isEmpty()) {
            return;
        }

        setMouseOverFlag(false);
        var event = new GuiButtonEvent(this);
        for (var listener : listeners) {
            listener.onRelease(event);
        }
    }
}
