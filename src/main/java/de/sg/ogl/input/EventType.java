/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.input;

enum EventType {
    // Default
    NONE,

    // Keyboard input
    KEY_PRESSED,
    KEY_REPEATED,
    KEY_RELEASED,

    // Mouse input
    BUTTON_PRESSED,
    BUTTON_RELEASED,
    CURSOR_MOVED,
    CURSOR_ENTERED,
    CURSOR_LEFT,
    SCROLLED,

    // Window events
    WINDOW_MOVED,
    WINDOW_RESIZED,
    WINDOW_CLOSED,
    WINDOW_REFRESH,
    WINDOW_FOCUSED,
    WINDOW_DEFOCUSED,
    WINDOW_ICONIFIED,
    WINDOW_UNICONIFIED,

    // Framebuffer size
    FRAMEBUFFER_RESIZED,
}
