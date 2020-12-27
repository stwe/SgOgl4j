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
     * The position of the Panel.
     */
    private final Vector2f position;

    /**
     * The width of the Panel.
     */
    private final float width;

    /**
     * The height of the Panel.
     */
    private final float height;

    /**
     * The texture for the Panel.
     */
    private int textureId;

    /**
     * The color for the Panel.
     */
    private Vector3f color = DEFAULT_COLOR;

    /**
     * For the collision check.
     */
    private final Aabb aabb;

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

        aabb = new Aabb(position, new Vector2f(width, height));
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public Vector2f getPosition() {
        return position;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public int getTextureId() {
        return textureId;
    }

    public Vector3f getColor() {
        return color;
    }

    public Aabb getAabb() {
        return aabb;
    }

    public Status getStatus() {
        return status;
    }

    public ArrayList<GuiObject> getGuiObjects() {
        return guiObjects;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    //-------------------------------------------------
    // Add gui objects
    //-------------------------------------------------

    public void addGuiObject(GuiObject guiObject) {
        guiObjects.add(guiObject);
    }

    //-------------------------------------------------
    // Events
    //-------------------------------------------------

    private void onHover() {
        if (status != Status.HOVER) {
            Log.LOGGER.debug("set hover status");
            status = Status.HOVER;
        }
    }

    private void onClick() {
        if (status != Status.CLICKED) {
            Log.LOGGER.debug("set clicked status");
            status = Status.CLICKED;
        }
    }

    private void onRelease() {
        Log.LOGGER.debug("set released status");
        status = Status.RELEASED;
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
    public void onNotify(GuiObject guiObject, GuiEvent guiEvent) {
        switch (guiEvent) {
            case HOVER: onHover(); break;
            case CLICKED: onClick(); break;
            case RELEASED: onRelease(); break;
        }
    }
}
