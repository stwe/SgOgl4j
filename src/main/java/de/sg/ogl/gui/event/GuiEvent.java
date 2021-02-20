/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui.event;

import de.sg.ogl.gui.GuiQuad;

import java.util.EventObject;

public class GuiEvent extends EventObject {

    public GuiEvent(GuiQuad source) {
        super(source);
    }
}
