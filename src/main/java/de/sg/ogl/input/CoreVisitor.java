/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.input;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class CoreVisitor implements Visitor {

    @Override
    public void visitPositionCategory(PositionCategory positionCategory) {
        //Log.LOGGER.debug("PositionCategory");
    }

    @Override
    public void visitSizeCategory(SizeCategory sizeCategory) {
        //Log.LOGGER.debug("SizeCategory");
    }

    @Override
    public void visitSwitchCategory(SwitchCategory switchCategory) {
        MouseInput.setMouseInWindow(switchCategory.value);
    }

    @Override
    public void visitUseDeviceCategory(UseDeviceCategory useDeviceCategory) {
        if (useDeviceCategory.eventType == EventType.SCROLLED) {
            MouseInput.setScrollX(useDeviceCategory.xPos);
            MouseInput.setScrollY(useDeviceCategory.yPos);
        }

        if (useDeviceCategory.eventType == EventType.CURSOR_MOVED) {
            MouseInput.setX(useDeviceCategory.xPos);
            MouseInput.setY(useDeviceCategory.yPos);
        }
    }

    @Override
    public void visitMouseCategory(MouseCategory mouseCategory) {
        MouseInput.setMouseButton(mouseCategory.button, mouseCategory.action != GLFW_RELEASE);
        MouseInput.setMouseButtonState(mouseCategory.button, mouseCategory.action);
    }

    @Override
    public void visitKeyboardCategory(KeyboardCategory keyboardCategory) {
        KeyInput.setKey(keyboardCategory.key, keyboardCategory.action != GLFW_RELEASE);
        KeyInput.setKeyState(keyboardCategory.key, keyboardCategory.action);
    }
}
