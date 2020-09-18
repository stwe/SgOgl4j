package de.sg.sandbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class Chunk {

    enum Type {
        BSH("BSH"),
        COL("COL");

        private final String type;

        Type(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    /**
     * The expected file type identifier.
     */
    private final Type type;

    /**
     * The file to be read.
     */
    private final File file;

    /**
     * The ByteBuffer with the chunk data.
     */
    ByteBuffer byteBuffer;

    /**
     * The file type identifier.
     */
    private final byte[] id = new byte[16];

    /**
     * The size of the chunk, excluding the header.
     */
    private final byte[] dataLength = new byte[4];

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Chunk(String filePath, Type type) throws IOException {
        this.type = type;
        this.file = new File(loadResource(filePath));

        readChunkHeader();
    }

    //-------------------------------------------------
    // Read header
    //-------------------------------------------------

    private void readChunkHeader() throws IOException {
        var in = new FileInputStream(file);

        // read file type id
        var result = in.read(id, 0, id.length);
        if (result != id.length) {
            throw new RuntimeException("Unexpected error.");
        }

        // read size of chunk
        result = in.read(dataLength, 0, dataLength.length);
        if (result != dataLength.length) {
            throw new RuntimeException("Unexpected error.");
        }

        // if the type is right ...
        if (idToString().equals(this.type.getType())) {
            var length = bytesToInt(dataLength, 0);
            byte[] data = new byte[length];

            // read chunk data
            result = in.read(data, 0, length);
            if (result != length) {
                throw new RuntimeException("Unexpected error.");
            }

            // wraps into a byte buffer
            byteBuffer = ByteBuffer.wrap(data);
            byteBuffer.order(ByteOrder.nativeOrder());
        } else {
            throw new RuntimeException("Invalid file type identifier.");
        }

        in.close();
    }

    //-------------------------------------------------
    // Load data
    //-------------------------------------------------

    public abstract void loadData();

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private static int bytesToInt(byte[] bytes, int index)
    {
        return  (bytes[index + 3] & 0xff) << 24 |
                (bytes[index + 2] & 0xff) << 16 |
                (bytes[index + 1] & 0xff) << 8 |
                bytes[index] & 0xff;
    }

    private String idToString() {
        char[] chars = new char[] { (char)id[0], (char)id[1], (char)id[2] };
        return String.valueOf(chars);
    }

    //-------------------------------------------------
    // Create resource url
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
