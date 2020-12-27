/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

public abstract class GuiObject {

    enum GuiEvent {
        HOVER,
        CLICKED,
        RELEASED
    }

    public abstract void input();
    public abstract void update();
    public abstract void addToRenderer(SpriteBatch spriteBatch);
    public abstract void onNotify(GuiObject guiObject, GuiEvent guiEvent);
}
