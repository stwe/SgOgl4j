/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.islands;

import java.awt.image.BufferedImage;

public class BshTexture {

    private final BufferedImage bufferedImage;
    private int textureId;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public BshTexture(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public BufferedImage getBufferedImage() {
        return bufferedImage;
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
