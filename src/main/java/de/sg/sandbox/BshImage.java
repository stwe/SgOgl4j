/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.sandbox;

import java.awt.image.BufferedImage;

public class BshImage {

    private BufferedImage image;
    private int textureId;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public BshImage(BufferedImage image) {
        this.image = image;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public BufferedImage getImage() {
        return image;
    }

    public int getTextureId() {
        return textureId;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }
}
