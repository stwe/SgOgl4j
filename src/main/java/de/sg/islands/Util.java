/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.islands;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

public class Util {

    //-------------------------------------------------
    // Generate resource url
    //-------------------------------------------------

    public static String loadResource(String resource) throws FileNotFoundException {
        return loadResourceByUrl(Util.class.getResource(Objects.requireNonNull(resource)), resource);
    }

    private static String loadResourceByUrl(URL url, String resource) throws FileNotFoundException {
        if (url != null) {
            return url.getPath().replaceFirst("^/(.:/)", "$1");
        } else {
            throw new FileNotFoundException("Resource " + resource + " not found.");
        }
    }

    //-------------------------------------------------
    // File helper
    //-------------------------------------------------

    public static Optional<String> getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    //-------------------------------------------------
    // Emulating unsigned arithmetic
    //-------------------------------------------------

    /*
    There are no primitive unsigned bytes in Java.
    The usual thing is to cast it to bigger type:
    */

    public static int byteToInt(byte value) {
        return (int) value & 0xFF;
    }

    public static int shortToInt(short value) {
        return (int) value & 0xFFFF;
    }
}
