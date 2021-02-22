/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.renderer.TileRenderer;

public interface GuiObject {

    void input();
    void update(float dt);
    void render(TileRenderer tileRenderer);

    void inputGuiObject();
    void updateGuiObject(float dt);
    void renderGuiObject(TileRenderer tileRenderer);
}
