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
    public void visitPositionEvent(PositionEvent positionEvent) {
        //Log.LOGGER.debug("PositionEvent");
    }

    @Override
    public void visitSizeEvent(SizeEvent sizeEvent) {
        //Log.LOGGER.debug("SizeEvent");
    }

    @Override
    public void visitSwitchEvent(SwitchEvent switchEvent) {
        MouseInput.setMouseInWindow(switchEvent.value);
    }

    @Override
    public void visitUseDeviceEvent(UseDeviceEvent useDeviceEvent) {
        if (useDeviceEvent.eventType == EventType.SCROLLED) {
            MouseInput.setScrollX(useDeviceEvent.xPos);
            MouseInput.setScrollY(useDeviceEvent.yPos);
        }

        if (useDeviceEvent.eventType == EventType.CURSOR_MOVED) {
            MouseInput.setX(useDeviceEvent.xPos);
            MouseInput.setY(useDeviceEvent.yPos);
        }
    }

    @Override
    public void visitMouseEvent(MouseEvent mouseEvent) {
        MouseInput.setMouseButton(mouseEvent.button, mouseEvent.action != GLFW_RELEASE);
        MouseInput.setMouseButtonState(mouseEvent.button, mouseEvent.action);
    }

    @Override
    public void visitKeyboardEvent(KeyboardEvent keyboardEvent) {
        KeyInput.setKey(keyboardEvent.key, keyboardEvent.action != GLFW_RELEASE);
        KeyInput.setKeyState(keyboardEvent.key, keyboardEvent.action);
    }
}
