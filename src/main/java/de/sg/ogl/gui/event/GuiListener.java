/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui.event;

import java.util.EventListener;

public interface GuiListener extends EventListener {

    void onClick(GuiEvent event);
    void onHover(GuiEvent event);
    void onRelease(GuiEvent event);
}
