/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.SgOglRuntimeException;
import org.joml.Vector2f;

public class GuiQuad {

    /**
     * The top left position of the quad.
     */
    private final Vector2f origin;

    /**
     * The width of the quad.
     */
    private final float width;

    /**
     * The height of the quad.
     */
    private final float height;

    private Vector2f bottomLeft = new Vector2f();
    private Vector2f bottomRight = new Vector2f();
    private Vector2f topRight = new Vector2f();
    private Vector2f center = new Vector2f();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public GuiQuad(Vector2f origin, float width, float height) {
        this.origin = origin;
        this.width = width;
        this.height = height;

        init();
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public Vector2f getOrigin() {
        return origin;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Vector2f getBottomLeft() {
        return bottomLeft;
    }

    public Vector2f getBottomRight() {
        return bottomRight;
    }

    public Vector2f getTopRight() {
        return topRight;
    }

    public Vector2f getCenter() {
        return center;
    }

    //-------------------------------------------------
    // Screen position
    //-------------------------------------------------

    public static Vector2f getRenderPosition(GuiQuad parentQuad, GuiQuad currentQuad, Anchor anchor) {
        Vector2f screenPosition;
        switch(anchor) {
            case TOP_LEFT:
                screenPosition = new Vector2f(parentQuad.origin).add(currentQuad.origin);
                break;
            case BOTTOM_LEFT:
                screenPosition = new Vector2f(parentQuad.bottomLeft.x, parentQuad.bottomLeft.y - currentQuad.height);
                screenPosition.x += currentQuad.origin.x;
                screenPosition.y -= currentQuad.origin.y;
                break;
            case BOTTOM_RIGHT:
                screenPosition = new Vector2f(parentQuad.bottomRight.x - currentQuad.width, parentQuad.bottomRight.y - currentQuad.height);
                screenPosition.x -= currentQuad.origin.x;
                screenPosition.y -= currentQuad.origin.y;
                break;
            case TOP_RIGHT:
                screenPosition = new Vector2f(parentQuad.topRight.x - currentQuad.width, parentQuad.topRight.y);
                screenPosition.x -= currentQuad.origin.x;
                screenPosition.y += currentQuad.origin.y;
                break;
            case CENTER:
                screenPosition = new Vector2f(parentQuad.center.x - currentQuad.width * 0.5f, parentQuad.center.y - currentQuad.height * 0.5f);
                screenPosition.x += currentQuad.origin.x;
                screenPosition.y += currentQuad.origin.y;
                break;
            default:
                throw new SgOglRuntimeException("Invalid anchor position type: " + anchor);
        }

        return screenPosition;
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    private void init() {
        bottomLeft = new Vector2f(origin.x, origin.y + height);
        topRight = new Vector2f(origin.x + width, origin.y);
        bottomRight = new Vector2f(origin.x + width, origin.y + height);
        center = new Vector2f((origin.x + topRight.x) * 0.5f, (origin.y + bottomLeft.y) * 0.5f);
    }
}
