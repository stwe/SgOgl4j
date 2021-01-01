/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.event.GuiPanelEvent;
import de.sg.ogl.event.GuiPanelListener;
import de.sg.ogl.physics.Aabb;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Vector;

public class GuiPanel extends GuiObject {

    /**
     * The default color of each Panel (white).
     */
    private static final Vector3f DEFAULT_COLOR = new Vector3f(1.0f, 1.0f, 1.0f);

    /**
     * The Panel is a container for other gui elements (Button, Slider).
     */
    private final ArrayList<GuiObject> guiObjects = new ArrayList<>();

    /**
     * Callback functions.
     */
    private final Vector<GuiPanelListener> guiPanelListeners = new Vector<>();

    /**
     * The title of the Panel.
     */
    private String title;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public GuiPanel(Vector2f anchor, Vector2f offset, float width, float height) {
        this.position = new Vector2f(anchor).add(offset);

        this.width = width;
        this.height = height;

        this.color = DEFAULT_COLOR;

        this.aabb = new Aabb(this.position, new Vector2f(this.width, this.height));

        updateCornerPoints();
    }

    public GuiPanel(Vector2f anchor, Vector2f offset, float width, float height, String title) {
        this(anchor, offset, width, height);
        setTitle(title);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public ArrayList<GuiObject> getGuiObjects() {
        return guiObjects;
    }

    public String getTitle() {
        return title;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setTitle(String title) {
        this.title = title;
    }

    //-------------------------------------------------
    // Listener
    //-------------------------------------------------

    public void addListener(GuiPanelListener listener) {
        if (guiPanelListeners.contains(listener)) {
            return;
        }

        guiPanelListeners.add(listener);
    }

    public void removeListener(GuiPanelListener listener) {
        guiPanelListeners.remove(listener);
    }

    public void onClick() {
        fireOnClick();
    }

    public void onHover() {
        fireOnHover();
    }

    public void onRelease() {
        fireOnRelease();
    }

    private void fireOnClick() {
        var event = new GuiPanelEvent(this);
        for (var listener : guiPanelListeners) {
            listener.onClick(event);
        }
    }

    private void fireOnHover() {
        var event = new GuiPanelEvent(this);
        for (var listener : guiPanelListeners) {
            listener.onHover(event);
        }
    }

    private void fireOnRelease() {
        var event = new GuiPanelEvent(this);
        for (var listener : guiPanelListeners) {
            listener.onRelease(event);
        }
    }

    //-------------------------------------------------
    // Add Button
    //-------------------------------------------------

    public GuiButton addButton(Anchor anchor, Vector2f offset, float width, float height, String label) {
        var button = new GuiButton(
                getGuiObjectScreenPosition(anchor, width, height),
                offset,
                width, height,
                label
        );

        guiObjects.add(button);

        return button;
    }

    public GuiButton addButton(Anchor anchor, Vector2f offset, float width, float height, String label, int textureId) {
        var button = addButton(anchor, offset, width, height, label);
        button.setTextureId(textureId);

        return button;
    }

    public GuiButton addButton(Anchor anchor, Vector2f offset, float width, float height, String label, Vector3f color) {
        var button = addButton(anchor, offset, width, height, label);
        button.setColor(color);

        return button;
    }

    //-------------------------------------------------
    // Implement GuiObject
    //-------------------------------------------------

    @Override
    public void input() {}

    @Override
    public void update() {
        for (var guiObject : guiObjects) {
            guiObject.update();
        }
    }

    @Override
    public void addToRenderer(SpriteBatch spriteBatch) {
        spriteBatch.addToRenderer(
            new Vector4f(position.x, position.y, width, height),
            textureId,
            color
        );

        for (var guiObject : guiObjects) {
            guiObject.addToRenderer(spriteBatch);
        }
    }
}
