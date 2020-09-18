package de.sg.sandbox;

import java.io.IOException;

public class ColFile extends Chunk  {

    private final int[] palette = new int[256];

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public ColFile(String filePath) throws IOException {
        super(filePath, Type.COL);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public int[] getPalette() {
        return palette;
    }

    //-------------------------------------------------
    // Override
    //-------------------------------------------------

    @Override
    public void loadData() {
        for (int i = 0; i < palette.length; i++) {
            int red = unsignedByte(byteBuffer.get());
            int green = unsignedByte(byteBuffer.get());
            int blue = unsignedByte(byteBuffer.get());

            byteBuffer.get(); // skip 1 byte

            palette[i] = rgbToInt(red, green, blue);
        }
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

    private static int unsignedByte(byte b) {
        return b & 0xff;
    }
}
