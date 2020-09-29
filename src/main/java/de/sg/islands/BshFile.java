/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.islands;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import static org.lwjgl.opengl.GL11.*;

public class BshFile extends File {

    private static final int NUMBER_OF_CHUNKS = 1;
    private static final String CHUNK_ID = "BSH";

    private final int[] palette;

    private final List<Integer> offsets = new Vector<>();
    private final List<BshTexture> bshTextures = new Vector<>();

    private final Chunk chunk0;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public BshFile(String filePath, int[] palette) throws IOException {
        super(Objects.requireNonNull(filePath, "filePath must not be null"));

        this.palette = Objects.requireNonNull(palette, "palette must not be null");

        if (getNumberOfChunks() != NUMBER_OF_CHUNKS) {
            throw new RuntimeException("Invalid number of Chunks.");
        }

        if (!chunkIndexHasId(0, CHUNK_ID)) {
            throw new RuntimeException("Invalid Chunk Id.");
        }

        chunk0 = getChunk(0);
        readDataFromChunks();
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public List<BshTexture> getBshTextures() {
        return bshTextures;
    }

    //-------------------------------------------------
    // FileInterface
    //-------------------------------------------------

    @Override
    public void readDataFromChunks() {
        // get offset of the first texture
        var texturesStartOffset = chunk0.getData().getInt();
        offsets.add(texturesStartOffset);

        // add remaining offsets
        for (var offset = chunk0.getData().getInt(); chunk0.getData().position() <= texturesStartOffset; offset = chunk0.getData().getInt()) {
            offsets.add(offset);
        }

        // decode
        decodeTextures();

        // create OpenGL textures
        createGlTextures();
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private void decodeTextures() {
        for (var offset : offsets) {
            chunk0.getData().position(offset);
            decodeTexture();
        }
    }

    private void decodeTexture() {
        var width = chunk0.getData().getInt();
        var height = chunk0.getData().getInt();
        var type = chunk0.getData().getInt();
        var len = chunk0.getData().getInt();

        if (width <= 0 || height <= 0) {
            throw new RuntimeException("Invalid width or height.");
        }

        var bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // int 4 bytes with:
        // alpha = bits 24 - 31
        // red =        16 - 23
        // green         8 - 15
        // blue          0 -  7

        int x = 0;
        int y = 0;

        while (true) {
            int numAlpha = Util.byteToInt(chunk0.getData().get());

            // end marker
            if (numAlpha == 255){
                break;
            }

            // end of row
            if (numAlpha == 254){
                x = 0;
                y++;
                continue;
            }

            for (int i = 0; i < numAlpha; i++) {
                bufferedImage.setRGB(x, y, 0);
                x++;
            }

            int numPixels = Util.byteToInt(chunk0.getData().get());

            for (int i = 0; i < numPixels; i++) {
                int colorIndex = Util.byteToInt(chunk0.getData().get());
                int color = palette[colorIndex];
                bufferedImage.setRGB(x, y, color);
                x++;
            }
        }

        var bshTexture = new BshTexture(bufferedImage);
        bshTextures.add(bshTexture);
    }

    private void createGlTextures() {
        for (var texture : bshTextures) {
            DataBufferInt dbb = (DataBufferInt) texture.getBufferedImage().getRaster().getDataBuffer();
            var id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);
            glTexImage2D(
                    GL_TEXTURE_2D,
                    0,
                    GL_RGBA,
                    texture.getBufferedImage().getWidth(),
                    texture.getBufferedImage().getHeight(),
                    0,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    dbb.getData()
            );

            texture.setTextureId(id);
        }
    }
}
