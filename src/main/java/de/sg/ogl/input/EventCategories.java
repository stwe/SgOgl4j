/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.input;

class PositionEvent extends VisitableEvent {
    long windowHandle;
    int xPos;
    int yPos;

    @Override
    public void accept(Visitor visitor) {
        visitor.visitPositionEvent(this);
    }
}

class SizeEvent extends VisitableEvent {
    long windowHandle;
    int width;
    int height;

    @Override
    public void accept(Visitor visitor) {
        visitor.visitSizeEvent(this);
    }
}

class SwitchEvent extends VisitableEvent{
    long windowHandle;
    boolean value = false;

    @Override
    public void accept(Visitor visitor) {
        visitor.visitSwitchEvent(this);
    }
}

class UseDeviceEvent extends VisitableEvent {
    long windowHandle;
    double xPos;
    double yPos;

    @Override
    public void accept(Visitor visitor) {
        visitor.visitUseDeviceEvent(this);
    }
}

class MouseEvent extends VisitableEvent {
    long windowHandle;
    int button;
    int action = -1;
    int mods;

    @Override
    public void accept(Visitor visitor) {
        visitor.visitMouseEvent(this);
    }
}

class KeyboardEvent extends VisitableEvent {
    long windowHandle;
    int key;
    int scancode;
    int action = -1;
    int mods;

    @Override
    public void accept(Visitor visitor) {
        visitor.visitKeyboardEvent(this);
    }
}
