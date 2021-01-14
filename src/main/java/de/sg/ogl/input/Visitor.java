/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.input;

public interface Visitor {
    void visitPositionCategory(PositionCategory positionCategory);
    void visitSizeCategory(SizeCategory sizeCategory);
    void visitSwitchCategory(SwitchCategory switchCategory);
    void visitUseDeviceCategory(UseDeviceCategory useDeviceCategory);
    void visitMouseCategory(MouseCategory mouseCategory);
    void visitKeyboardCategory(KeyboardCategory keyboardCategory);
}
