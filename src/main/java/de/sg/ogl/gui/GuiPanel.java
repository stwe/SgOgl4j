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

enum GuiAnchorPos {
    TOP_RIGHT, TOP_LEFT, BOTTOM_RIGHT, BOTTOM_LEFT, CENTER
}

public class GuiPanel {

    private static final float DEFAULT_BOUNDS_X = 0.0f;
    private static final float DEFAULT_BOUNDS_Y = 0.0f;
    private static final float DEFAULT_WIDTH = 128.0f;
    private static final float DEFAULT_HEIGHT = 256.0f;

    private static final Vector3f DEFAULT_COLOR = new Vector3f(1.0f, 1.0f, 1.0f);

    private final Vector4f bounds;

    private final Vector2f position;
    private final float width;
    private final float height;

    private int textureId;
    private Vector3f color = DEFAULT_COLOR;

    private final ArrayList<GuiObject> guiObjects = new ArrayList<>();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public GuiPanel() {
        this(
            new Vector4f(DEFAULT_BOUNDS_X, DEFAULT_BOUNDS_Y, DEFAULT_WIDTH, DEFAULT_HEIGHT),
            new Vector2f(0.0f),
            new Vector2f(0.0f)
        );
    }

    // IGUIPanel(glm::vec4(0, 0, 150, 500), *GetAnchorPos(GUIAnchorPos::BottomRight, glm::vec4(0, 0, 150, 500)), glm::vec2(0,0));
    public GuiPanel(Vector4f bounds, Vector2f anchor, Vector2f offset) {
        this.bounds = bounds;

        this.position = new Vector2f(anchor).add(offset);
        this.width = this.bounds.z;
        this.height = this.bounds.w;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public Vector4f getBounds() {
        return bounds;
    }

    public Vector2f getPosition() {
        return position;
    }

    public ArrayList<GuiObject> getGuiObjects() {
        return guiObjects;
    }

    public int getTextureId() {
        return textureId;
    }

    public Vector3f getColor() {
        return color;
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
