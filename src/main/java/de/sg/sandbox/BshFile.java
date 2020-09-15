package de.sg.sandbox;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class BshFile extends Chunk {

    private final List<Integer> offsets = new Vector<>();

    public BshFile(String filePath) throws IOException {
        super(filePath, Chunk.Type.BSH);
    }

    public void decodeImages() {
        for (var offset : offsets) {
            byteBuffer.position(offset);

            var width = byteBuffer.getInt();
            var height = byteBuffer.getInt();

            System.out.println("width: " + width + " height: " + height);
        }
    }

    @Override
    public void loadData() {
        // get offset of the first image
        var firstOffset = byteBuffer.getInt();

        // add offsets
        offsets.add(firstOffset);
        for (var offset = byteBuffer.getInt(); byteBuffer.position() <= firstOffset; offset = byteBuffer.getInt()) {
            offsets.add(offset);
        }
    }
}
