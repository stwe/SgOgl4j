/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.Log;
import de.sg.ogl.physics.Aabb;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class GuiPanel extends GuiObject {

    public enum Status {
        DEFAULT,
        HOVER,
        CLICKED,
        RELEASED
    }

    /**
     * The default color of each Panel (white).
     */
    private static final Vector3f DEFAULT_COLOR = new Vector3f(1.0f, 1.0f, 1.0f);

    /**
     * The current status of the Panel.
     */
    private Status status = Status.DEFAULT;

    /**
     * The Panel is a container for other gui elements (Button, Slider).
     */
    private final ArrayList<GuiObject> guiObjects = new ArrayList<>();

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

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public Status getStatus() {
        return status;
    }

    public ArrayList<GuiObject> getGuiObjects() {
        return guiObjects;
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
    // Handle events
    //-------------------------------------------------

    private void onHover() {
        if (status != Status.HOVER) {
            Log.LOGGER.debug("set panel hover status");
            status = Status.HOVER;
        }
    }

    private void onClick() {
        if (status != Status.CLICKED) {
            Log.LOGGER.debug("set panel clicked status");
            status = Status.CLICKED;
        }
    }

    private void onRelease() {
        if (status == Status.HOVER) {
            Log.LOGGER.debug("set panel released status");
            status = Status.RELEASED;
        }
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

    @Override
    public void onNotify(Event event) {
        switch (event) {
            case HOVER: onHover(); break;
            case CLICKED: onClick(); break;
            case RELEASED: onRelease(); break;
        }
    }
}
