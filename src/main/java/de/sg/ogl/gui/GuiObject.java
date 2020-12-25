/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import org.joml.Vector2f;
import org.joml.Vector4f;

enum GuiEvent {
    HOVER_OVER,
    RELEASED,
    CLICKED
}

public abstract class GuiObject {

    protected Vector4f bounds;
    protected Vector2f position;

    public abstract void update();
    public abstract void render();
    public abstract void onNotify(GuiObject guiObject, GuiEvent guiEvent);
}
