/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.event;

import java.util.EventListener;

public interface GuiButtonListener extends EventListener {

    void onClick(GuiButtonEvent event);
    void onHover(GuiButtonEvent event);
    void onRelease(GuiButtonEvent event);
}
