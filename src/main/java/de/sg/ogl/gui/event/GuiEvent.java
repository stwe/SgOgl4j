/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui.event;

import java.util.EventObject;

public class GuiEvent<T> extends EventObject {

    public GuiEvent(T source) {
        super(source);
    }
}
