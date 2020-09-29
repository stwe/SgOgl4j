/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.islands;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import java.io.File;

public class CodFile {

    private final String filePath;
    private byte[] bytes;
    private byte[] decrypted;

    public CodFile(String filePath) throws IOException {
        this.filePath = Objects.requireNonNull(filePath, "filePath must not be null");

        readFileData();
    }

    private void readFileData() throws IOException {
        var file = new java.io.File(Util.loadResource(filePath));
        var fileInputStream = new FileInputStream(file);

        bytes = fileInputStream.readAllBytes();
        decrypted = new byte[bytes.length];

        var i = 0;
        for (var b : bytes) {
            var r = (byte)Math.abs(b);
            decrypted[i] = r;
            i++;
        }

        File f = new File("haeuser.out");
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(decrypted);
    }
}
