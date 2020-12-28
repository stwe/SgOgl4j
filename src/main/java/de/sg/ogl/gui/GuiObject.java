/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.physics.Aabb;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class GuiObject {

    public enum Anchor {
        TOP_LEFT,     // (0.0f, 0.0f)
        BOTTOM_LEFT,  // (0.0f, windowHeight - height)
        BOTTOM_RIGHT, // (windowWidth - width, windowHeight - height)
        TOP_RIGHT,    // (windowWidth - width, 0.0f)
        CENTER        // ((windowWidth * 0.5f) - (width * 0.5f), (windowHeight * 0.5f) - (height * 0.5f))
    }

    public enum Event {
        HOVER,
        CLICKED,
        RELEASED
    }

    protected Vector2f position;
    protected float width;
    protected float height;

    protected int textureId;
    protected Vector3f color;

    protected Aabb aabb;

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public abstract void input();
    public abstract void update();
    public abstract void addToRenderer(SpriteBatch spriteBatch);
    public abstract void onNotify(Event event);
}
