package de.sg.sandbox;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import static org.lwjgl.opengl.GL11.*;

public class BshFile extends Chunk {

    private final int[] palette;
    private final List<Integer> offsets = new Vector<>();
    private final List<BshImage> images = new Vector<>();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public BshFile(String filePath, int[] palette) throws IOException {
        super(filePath, Chunk.Type.BSH);
        this.palette = palette;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public List<BshImage> getImages() {
        return images;
    }

    //-------------------------------------------------
    // Override
    //-------------------------------------------------

    @Override
    public void loadData() {
        // get offset of the first image
        var firstOffset = byteBuffer.getInt();

        // add offsets
        offsets.add(firstOffset);
        for (var offset = byteBuffer.getInt(); byteBuffer.position() <= firstOffset; offset = byteBuffer.getInt()) {
            offsets.add(offset);
        }

        // decode images
        decodeImages();

        // create textures
        createTextures();
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private static int unsignedByte(byte b) {
        return b & 0xff;
    }

    private void decodeImages() {
        for (var offset : offsets) {
            byteBuffer.position(offset);
            decodeImage();
        }
    }

    private void decodeImage() {
        var width = byteBuffer.getInt();
        var height = byteBuffer.getInt();
        var num = byteBuffer.getInt();
        var len = byteBuffer.getInt();

        if (width <= 0 || height <= 0) {
            throw new RuntimeException("Invalid width or height.");
        }

        var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // int 4 bytes with:
        // alpha = bits 24 - 31
        // red =        16 - 23
        // green         8 - 15
        // blue          0 -  7

        int x = 0;
        int y = 0;

        while (true) {
            int numAlpha = unsignedByte(byteBuffer.get());

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
                image.setRGB(x, y, 0);
                x++;
            }

            int numPixels = unsignedByte(byteBuffer.get());

            for (int i = 0; i < numPixels; i++) {
                int colorIndex = unsignedByte(byteBuffer.get());
                int color = palette[colorIndex];
                image.setRGB(x, y, color);
                x++;
            }
        }

        var bshImage = new BshImage(image);
        images.add(bshImage);
    }

    private void createTextures() {
        for (var image : images) {
            DataBufferInt dbb = (DataBufferInt) image.getImage().getRaster().getDataBuffer();
            var id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);
            glTexImage2D(
                    GL_TEXTURE_2D,
                    0,
                    GL_RGBA,
                    image.getImage().getWidth(), image.getImage().getHeight(),
                    0,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    dbb.getData()
            );

            image.setTextureId(id);
        }
    }
}
