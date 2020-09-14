/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.sandbox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Vector;

public class BshReader {

    private static final String PALETTE_FILE_PATH = "/bsh/STADTFLD.COL";
    private static final String OUT_PATH = "E:/Bsh/out";

    private static final int HEADER_LENGTH = 20;

    private final File bshFile;
    private final File palFile;

    private final int[] palette = new int[256];
    private final List<Integer> offsets = new Vector<>();
    private ByteBuffer imagesBytes;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public BshReader(String bshUrl) throws FileNotFoundException {
        bshFile = new File(loadResource(bshUrl));
        palFile = new File(loadResource(PALETTE_FILE_PATH));
    }

    //-------------------------------------------------
    // Read bytes
    //-------------------------------------------------

    private ByteBuffer readBytes(File file) throws IOException {
        var in = new RandomAccessFile(file, "r");
        var filesize = in.length();
        byte[] bytes = new byte[(int)filesize];
        in.readFully(bytes);

        return ByteBuffer.wrap(bytes);
    }

    //-------------------------------------------------
    // Palette
    //-------------------------------------------------

    private void readPalette() throws IOException {
        System.out.println("Reading palette file.");

        ByteBuffer byteBuffer = readBytes(palFile);
        byteBuffer.order(ByteOrder.nativeOrder());

        // skip 20 byte header
        for (int i = 0; i < HEADER_LENGTH; i++) {
            byteBuffer.get();
        }

        for (int i = 0; i < palette.length; i++) {
            int red = unsignedByte(byteBuffer.get());
            int green = unsignedByte(byteBuffer.get());
            int blue = unsignedByte(byteBuffer.get());

            byteBuffer.get(); // skip 1 byte

            palette[i] = rgbToInt(red, green, blue);
        }
    }

    //-------------------------------------------------
    // Read images
    //-------------------------------------------------

    private void readImages() throws IOException {
        System.out.println("Reading images.");

        imagesBytes = readBytes(bshFile);
        imagesBytes.order(ByteOrder.nativeOrder());

        // skip 20 byte header
        imagesBytes.position(imagesBytes.position() + HEADER_LENGTH);

        // get offset of the first image
        var firstOffset = imagesBytes.getInt() + HEADER_LENGTH;

        // add offsets
        offsets.add(firstOffset);
        for (var offset = imagesBytes.getInt(); imagesBytes.position() <= firstOffset; offset = imagesBytes.getInt()) {
            offsets.add(offset + HEADER_LENGTH);
        }
    }

    //-------------------------------------------------
    // Store images
    //-------------------------------------------------

    private static void storeImage(BufferedImage img, String filename) throws IOException {
        File file = new File(filename);
        var result = file.createNewFile();
        if (!result) {
            throw new RuntimeException("Cannot create file " + filename + ".");
        }

        ImageIO.write(img, "PNG", file);
    }

    private void storeImages() throws IOException {
        System.out.println("Store images.");

        int i = 0;
        for (var offset : offsets) {
            imagesBytes.position(offset);

            var image = readImage(imagesBytes);

            if (image == null) {
                System.out.println("Invalid image found at offset: " + offset + ".");
                continue;
            }

            String filename = OUT_PATH + "/" +
                    bshFile.getName() + "_" +
                    i + ".png";

            storeImage(image, filename);

            i++;

            System.out.println("Saved image: " + filename);
            System.out.println(imagesBytes.remaining() + " bytes remaining");
        }
    }

    //-------------------------------------------------
    // Load
    //-------------------------------------------------

    public void load() throws IOException {
        readPalette();
        readImages();
        storeImages();
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private static int rgbToInt(int red, int green, int blue) {
        var alpha = 255;

        return (alpha << 24) |
                (red << 16) |
                (green << 8) |
                (blue);
    }

    private BufferedImage readImage(ByteBuffer buffer) {
        var width = buffer.getInt();
        var height = buffer.getInt();

        if (width <= 0 || height <= 0) {
            return null;
        }

        var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // skip 8 unknown bytes
        buffer.position(buffer.position() + 8);

        int x = 0;
        int y = 0;

        // Read until we reach the end marker
        while (true){
            // 0 ... 253
            int numAlpha = unsignedByte(buffer.get());

            // End marker - end of picture
            if (numAlpha == 255){
                break;
            }

            // End of row
            if (numAlpha == 254){
                x = 0;
                y++;
                continue;
            }

            // Pixel data
            for (int i = 0; i < numAlpha; i++) {
                image.setRGB(x, y, 0);
                x++;
            }

            int numPixels = unsignedByte(buffer.get());
            for (int i = 0; i < numPixels; i++) {
                int colorIndex = unsignedByte(buffer.get());
                int color = palette[colorIndex];
                image.setRGB(x, y, color);
                x++;
            }
        }

        return image;
    }

    private static int unsignedByte(byte b) {
        return b & 0xff;
    }

    //-------------------------------------------------
    // Get resource url
    //-------------------------------------------------

    private String loadResource(String resource) throws FileNotFoundException {
        return loadResourceByUrl(getClass().getResource(resource), resource);
    }

    private String loadResourceByUrl(URL url, String resource) throws FileNotFoundException {
        if (url != null) {
            return url.getPath().replaceFirst("^/(.:/)", "$1");
        } else {
            throw new FileNotFoundException("Resource " + resource + " not found.");
        }
    }
}
