/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.islands;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Chunk {

    private static final int ID_BYTES = 16;
    private static final int SIZE_BYTES = 4;

    /**
     * The chunk type identifier.
     */
    private final byte[] id = new byte[ID_BYTES];

    /**
     * The chunk type identifier as String.
     */
    private String idStr;

    /**
     * The size of the chunk, excluding the header.
     */
    private final byte[] dataLength = new byte[SIZE_BYTES];

    /**
     * The size of the chunk, excluding the header as Int.
     */
    private int dataLengthInt = 0;

    /**
     * The ByteBuffer with the chunk data.
     */
    private ByteBuffer byteBuffer;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Chunk(FileInputStream fileInputStream) throws IOException {
        readId(fileInputStream);
        readDataLength(fileInputStream);
        readData(fileInputStream);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public String getId() {
        return idStr;
    }

    public int getDataLength() {
        return dataLengthInt;
    }

    public ByteBuffer getData() {
        return byteBuffer;
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private void readId(FileInputStream fileInputStream) throws IOException {
        var result = fileInputStream.read(id, 0, id.length);
        checkResult(result, id.length);

        idStr = new String(id).split("\0")[0];
    }

    private void readDataLength(FileInputStream fileInputStream) throws IOException {
        var result = fileInputStream.read(dataLength, 0, dataLength.length);
        checkResult(result, dataLength.length);

        dataLengthInt = ByteBuffer.wrap(dataLength).order(ByteOrder.nativeOrder()).getInt();
    }

    private void readData(FileInputStream fileInputStream) throws IOException {
        byte[] data = new byte[dataLengthInt];
        var result = fileInputStream.read(data, 0, dataLengthInt);
        checkResult(result, dataLengthInt);

        byteBuffer = ByteBuffer.wrap(data).order(ByteOrder.nativeOrder());
    }

    private void checkResult(int result, int length) {
        if (result != length) {
            throw new RuntimeException("Wrong total number of bytes read.");
        }
    }
}
