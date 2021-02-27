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
import de.sg.ogl.gui.event.GuiEvent;
import de.sg.ogl.gui.event.GuiListener;
import de.sg.ogl.input.MouseInput;
import de.sg.ogl.renderer.TileRenderer;
import de.sg.ogl.resource.Texture;
import de.sg.ogl.text.TextRenderer;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;

public class GuiLabel extends GuiQuad {

    private final TextRenderer textRenderer;
    private String label;
    private Color textColor;

    private final ArrayList<GuiListener<GuiLabel>> listeners = new ArrayList<>();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public GuiLabel(
            Vector2f origin,
            float width, float height,
            Color color, Texture texture,
            TextRenderer textRenderer,
            String label,
            Color textColor
    ) {
        super(origin, width, height, color, texture);

        this.textRenderer = textRenderer;
        this.label = label;
        this.textColor = textColor;
    }

    public GuiLabel(
            Vector2f origin,
            float width, float height,
            Color color,
            TextRenderer textRenderer,
            String label,
            Color textColor
    ) {
        super(origin, width, height, color);

        this.textRenderer = textRenderer;
        this.label = label;
        this.textColor = textColor;
    }

    public GuiLabel(
            Vector2f origin,
            float width, float height,
            Texture texture,
            TextRenderer textRenderer,
            String label,
            Color textColor
    ) {
        super(origin, width, height, texture);

        this.textRenderer = textRenderer;
        this.label = label;
        this.textColor = textColor;
    }

    public GuiLabel(
            Vector2f origin,
            float width, float height,
            TextRenderer textRenderer,
            String label,
            Color textColor
    ) {
        super(origin, width, height);

        this.textRenderer = textRenderer;
        this.label = label;
        this.textColor = textColor;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public String getLabel() {
        return label;
    }

    public Color getTextColor() {
        return textColor;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setLabel(String label) {
        this.label = label;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    //-------------------------------------------------
    // Implement GuiObject
    //-------------------------------------------------

    @Override
    public void init() {}

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

    @Override
    public void renderGuiObject(TileRenderer tileRenderer) {
        // renders the label only
        if (isRenderMe() && label != null) {
            textRenderer.render(
                    label,
                    getOrigin().x, getOrigin().y,
                    textColor
            );
        }
    }

    //-------------------------------------------------
    // Listener
    //-------------------------------------------------

    public void addListener(GuiListener<GuiLabel> listener) {
        Objects.requireNonNull(listener, "listener must not be null");

        if (listeners.contains(listener)) {
            return;
        }

        listeners.add(listener);
    }

    public void removeListener(GuiListener<GuiLabel> listener) {
        Objects.requireNonNull(listener, "listener must not be null");
        listeners.remove(listener);
    }

    private void runOnClickListeners() {
        if (listeners.isEmpty()) {
            return;
        }

        setMouseOverFlag(true);
        var event = new GuiEvent<>(this);
        for (var listener : listeners) {
            listener.onClick(event);
        }
    }

    private void runOnHoverListeners() {
        if (listeners.isEmpty()) {
            return;
        }

        setMouseOverFlag(true);
        var event = new GuiEvent<>(this);
        for (var listener : listeners) {
            listener.onHover(event);
        }
    }

    private void runOnReleaseListeners() {
        if (listeners.isEmpty()) {
            return;
        }

        setMouseOverFlag(false);
        var event = new GuiEvent<>(this);
        for (var listener : listeners) {
            listener.onRelease(event);
        }
    }
}
