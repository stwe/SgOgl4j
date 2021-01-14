/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.input;

public interface Visitor {
    void visitPositionEvent(PositionEvent positionEvent);
    void visitSizeEvent(SizeEvent sizeEvent);
    void visitSwitchEvent(SwitchEvent switchEvent);
    void visitUseDeviceEvent(UseDeviceEvent useDeviceEvent);
    void visitMouseEvent(MouseEvent mouseEvent);
    void visitKeyboardEvent(KeyboardEvent keyboardEvent);
}
