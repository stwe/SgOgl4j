/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.SgOglRuntimeException;
import de.sg.ogl.physics.Aabb;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class GuiObject {

    public enum Anchor {
        TOP_LEFT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        TOP_RIGHT,
        CENTER
    }

    protected Vector2f topLeft = new Vector2f();
    protected Vector2f bottomLeft = new Vector2f();
    protected Vector2f bottomRight = new Vector2f();
    protected Vector2f topRight = new Vector2f();
    protected Vector2f center = new Vector2f();

    protected Vector2f position;
    protected float width;
    protected float height;

    protected int textureId;
    protected Vector3f color;

    protected Aabb aabb;

    protected void updateCornerPoints() {
        topLeft = new Vector2f(this.position);
        bottomLeft = new Vector2f(this.position.x, this.position.y + height);
        topRight = new Vector2f(this.position.x + width, this.position.y);
        bottomRight = new Vector2f(this.position.x + width, this.position.y + height);
        center = new Vector2f((this.position.x + topRight.x) * 0.5f, (this.position.y + bottomLeft.y) * 0.5f);
    }

    protected Vector2f getGuiObjectScreenPosition(Anchor anchor, float width, float height) {
        updateCornerPoints();

        Vector2f position;
        switch(anchor) {
            case TOP_LEFT:
                position = new Vector2f(topLeft);
                break;
            case BOTTOM_LEFT:
                position = new Vector2f(bottomLeft.x, bottomLeft.y - height);
                break;
            case BOTTOM_RIGHT:
                position = new Vector2f(bottomRight.x - width, bottomRight.y - height);
                break;
            case TOP_RIGHT:
                position = new Vector2f(topRight.x - width, topRight.y);
                break;
            case CENTER:
                position = new Vector2f(center.x - width * 0.5f, center.y - height * 0.5f);
                break;
            default:
                throw new SgOglRuntimeException("Invalid anchor position type: " + anchor);
        }

        return position;
    }

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public abstract void input();
    public abstract void update();
    public abstract void addToRenderer(SpriteBatch spriteBatch);
}
