/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.state;

public interface State {
    void init() throws Exception;
    void input();
    void update(float dt);
    void render();
    void cleanUp();
}
