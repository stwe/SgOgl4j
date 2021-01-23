/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.buffer;

public interface Buffer {

    int getId();

    void create();
    void delete();

    void bind();
    void unbind();

    void cleanUp();
}
