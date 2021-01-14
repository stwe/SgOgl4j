/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.input;

class PositionCategory implements EventCategory {
    EventType eventType = EventType.NONE;
    long windowHandle;
    int xPos = 0;
    int yPos = 0;

    @Override
    public void accept(Visitor visitor) {
        visitor.visitPositionCategory(this);
    }
}

class SizeCategory implements EventCategory {
    EventType eventType = EventType.NONE;
    long windowHandle;
    int width = 0;
    int height = 0;

    @Override
    public void accept(Visitor visitor) {
        visitor.visitSizeCategory(this);
    }
}

class SwitchCategory implements EventCategory {
    EventType eventType = EventType.NONE;
    long windowHandle;
    boolean value = false;

    @Override
    public void accept(Visitor visitor) {
        visitor.visitSwitchCategory(this);
    }
}

class UseDeviceCategory implements EventCategory {
    EventType eventType = EventType.NONE;
    long windowHandle;
    double xPos = 0.0;
    double yPos = 0.0;

    @Override
    public void accept(Visitor visitor) {
        visitor.visitUseDeviceCategory(this);
    }
}

class MouseCategory implements EventCategory {
    EventType eventType = EventType.NONE;
    long windowHandle;
    int button = 0;
    int action = -1;
    int mods = 0;

    @Override
    public void accept(Visitor visitor) {
        visitor.visitMouseCategory(this);
    }
}

class KeyboardCategory implements EventCategory {
    EventType eventType = EventType.NONE;
    long windowHandle;
    int key = 0;
    int scancode = 0;
    int action = -1;
    int mods = 0;

    @Override
    public void accept(Visitor visitor) {
        visitor.visitKeyboardCategory(this);
    }
}
