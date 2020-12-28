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

    public enum AnchorPosition {
        TOP_LEFT,     // (0.0f, 0.0f)
        BOTTOM_LEFT,  // (0.0f, windowHeight - height)
        BOTTOM_RIGHT, // (windowWidth - width, windowHeight - height)
        TOP_RIGHT,    // (windowWidth - width, 0.0f)
        CENTER        // ((windowWidth * 0.5f) - (width * 0.5f), (windowHeight * 0.5f) - (height * 0.5f))
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
    // Add gui objects
    //-------------------------------------------------

    public void addGuiObject(GuiObject guiObject) {
        guiObjects.add(guiObject);
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    public GuiObject getLastGuiObject() {
        return guiObjects.get(guiObjects.size() - 1);
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
    public void onNotify(GuiEvent guiEvent) {
        switch (guiEvent) {
            case HOVER: onHover(); break;
            case CLICKED: onClick(); break;
            case RELEASED: onRelease(); break;
        }
    }
}
