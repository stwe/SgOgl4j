/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;

enum GuiAnchorPos {
    TOP_RIGHT, TOP_LEFT, BOTTOM_RIGHT, BOTTOM_LEFT, CENTER
}

public class GuiPanel {

    private Vector4f bounds;
    private Vector2f offset;

    private Vector2f position;
    private float width;
    private float height;

    private ArrayList<GuiObject> guiObjects = new ArrayList<>();

    GuiPanel() {
        this(
                new Vector4f(0.0f, 0.0f, 200.0f, 480.0f),
                new Vector2f(0.0f),
                new Vector2f(0.0f)
        );
    }

    GuiPanel(Vector4f bounds, Vector2f offset, Vector2f anchor) {
        this.bounds = bounds;
        this.offset = offset;

        this.position = new Vector2f(anchor).add(offset);
        this.width = this.bounds.z;
        this.height = this.bounds.w;
    }

    void addGuiObject(GuiObject guiObject) {
        guiObjects.add(guiObject);
    }

    void input() {}

    void update() {
        for (var guiObject : guiObjects) {
            guiObject.update();
        }
    }

    void render() {
        // render this

        for (var guiObject : guiObjects) {
            guiObject.render();
        }
    }

    public Vector4f getBounds() {
        return bounds;
    }

    public Vector2f getPosition() {
        return position;
    }

    public ArrayList<GuiObject> getGuiObjects() {
        return guiObjects;
    }
}
