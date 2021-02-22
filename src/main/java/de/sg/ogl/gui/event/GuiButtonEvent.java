/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui.event;

import de.sg.ogl.gui.widget.GuiButton;

import java.util.EventObject;

public class GuiButtonEvent extends EventObject {

    public GuiButtonEvent(GuiButton guiButton) {
        super(guiButton);
    }
}
