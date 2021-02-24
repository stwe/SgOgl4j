/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui.event;

import java.util.EventListener;

public interface GuiListener<T> extends EventListener {

    void onClick(GuiEvent<T> event);
    void onHover(GuiEvent<T> event);
    void onRelease(GuiEvent<T> event);
}
