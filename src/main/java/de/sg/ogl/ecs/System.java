/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.ecs;

public interface System {

    void init() throws Exception;
    void update(float dt);
    void render();
    void cleanUp();
}
