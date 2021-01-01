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
import org.joml.Vector4f;

public class GuiButton extends GuiObject {

    /**
     * The default color of each Button (green).
     */
    private static final Vector3f DEFAULT_COLOR = new Vector3f(0.0f, 1.0f, 0.0f);

    /**
     * The label of the Button.
     */
    private final String label;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public GuiButton(Vector2f anchor, Vector2f offset, float width, float height, String label) {
        this.position = new Vector2f(anchor).add(offset);

        this.width = width;
        this.height = height;

        this.color = DEFAULT_COLOR;

        this.aabb = new Aabb(this.position, new Vector2f(this.width, this.height));

        this.label = label;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public String getLabel() {
        return label;
    }

    //-------------------------------------------------
    // Implement GuiObject
    //-------------------------------------------------

    @Override
    public void input() {}

    @Override
    public void update() {}

    @Override
    public void addToRenderer(SpriteBatch spriteBatch) {
        spriteBatch.addToRenderer(
                new Vector4f(position.x, position.y, width, height),
                textureId,
                color
        );
    }
}
