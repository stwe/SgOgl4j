/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class GuiPanel {

    /**
     * The anchor position of the Panel.
     */
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
    // Logic
    //-------------------------------------------------

    public void input() {}

    public void update() {
        for (var guiObject : guiObjects) {
            guiObject.update();
        }
    }

    //-------------------------------------------------
    // Renderer
    //-------------------------------------------------

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
