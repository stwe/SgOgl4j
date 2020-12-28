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

    enum GuiEvent {
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
    public abstract void onNotify(GuiEvent guiEvent);
}
