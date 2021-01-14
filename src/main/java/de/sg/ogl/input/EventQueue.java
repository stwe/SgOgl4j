/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.input;

import org.lwjgl.glfw.*;

import java.util.LinkedList;
import java.util.Optional;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.glfw.GLFW.*;

public class EventQueue {

    private final LinkedList<VisitableEvent> events = new LinkedList<>();
    private final CoreVisitor coreVisitor = new CoreVisitor();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public EventQueue(long windowHandle) {
        LOGGER.debug("Creates EventQueue object.");

        initCallbacks(windowHandle);
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void update() {
        var event = retrieveOldestEvent();
        event.ifPresent(this::visitAndRemoveOldestEvent);
    }

    //-------------------------------------------------
    // Add
    //-------------------------------------------------

    private void addEvent(VisitableEvent event) {
        events.add(event);
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    private void initCallbacks(long windowHandle) {
        LOGGER.debug("Setting the Glfw callback functions.");

        var keyboard = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                var event = new KeyboardEvent();

                if (action == GLFW_PRESS)
                {
                    event.eventType = EventType.KEY_PRESSED;
                }
                else if (action == GLFW_RELEASE)
                {
                    event.eventType = EventType.KEY_RELEASED;
                }
                else if (action == GLFW_REPEAT)
                {
                    event.eventType = EventType.KEY_REPEATED;
                }

                event.windowHandle = window;
                event.key = key;
                event.scancode = scancode;
                event.action = action;
                event.mods = mods;

                addEvent(event);
            }
        };

        var mouseMove = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                var event = new UseDeviceEvent();

                event.eventType = EventType.CURSOR_MOVED;
                event.windowHandle = window;
                event.xPos = xpos;
                event.yPos = ypos;

                addEvent(event);
            }
        };

        var mouseButtons = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                var event = new MouseEvent();

                if (action == GLFW_PRESS) {
                    event.eventType = EventType.BUTTON_PRESSED;
                } else {
                    event.eventType = EventType.BUTTON_RELEASED;
                }

                event.windowHandle = window;
                event.button = button;
                event.action = action;
                event.mods = mods;

                addEvent(event);
            }
        };

        var mouseScroll = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double offsetx, double offsety) {
                var event = new UseDeviceEvent();

                event.eventType = EventType.SCROLLED;
                event.windowHandle = window;
                event.xPos = offsetx;
                event.yPos = offsety;

                addEvent(event);
            }
        };

        var cursorEnter = new GLFWCursorEnterCallback() {
            @Override
            public void invoke(long window, boolean entered) {
                var event = new SwitchEvent();

                event.eventType = entered ? EventType.CURSOR_ENTERED : EventType.CURSOR_LEFT;
                event.windowHandle = window;
                event.value = entered;

                addEvent(event);
            }
        };

        GLFW.glfwSetKeyCallback(windowHandle, keyboard);
        GLFW.glfwSetCursorPosCallback(windowHandle, mouseMove);
        GLFW.glfwSetMouseButtonCallback(windowHandle, mouseButtons);
        GLFW.glfwSetScrollCallback(windowHandle, mouseScroll);
        GLFW.glfwSetCursorEnterCallback(windowHandle, cursorEnter);
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private Optional<VisitableEvent> retrieveOldestEvent() {
        return Optional.ofNullable(events.peek());
    }

    private void visitAndRemoveOldestEvent(VisitableEvent event) {
        event.accept(coreVisitor);
        events.remove();
    }
}
