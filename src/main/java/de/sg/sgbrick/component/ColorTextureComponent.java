/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.sgbrick.component;

import de.sg.ogl.resource.Texture;
import org.joml.Vector3f;

public class ColorTextureComponent {

    private Vector3f color = new Vector3f(1.0f);
    private Texture texture;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public ColorTextureComponent() {
    }

    public ColorTextureComponent(Vector3f color, Texture texture) {
        this.color = color;
        this.texture = texture;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public Vector3f getColor() {
        return color;
    }

    public Texture getTexture() {
        return texture;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
