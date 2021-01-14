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

import static org.lwjgl.glfw.GLFW.*;

public class EventQueue {

    private final LinkedList<EventCategory> events = new LinkedList<>();
    private final CoreVisitor coreVisitor = new CoreVisitor();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public EventQueue(long windowHandle) {
        initCallbacks(windowHandle);
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void update() {
        // peek at the element at the head of a Queue
        var oldest = events.peek(); // empty -> return null

        // removes the element at the head of the Queue
        if (oldest != null) {
            oldest.accept(coreVisitor);
            events.remove();
            //Log.LOGGER.debug("events: {}", events.size());
        }
    }

    //-------------------------------------------------
    // Add
    //-------------------------------------------------

    private void addEvent(EventCategory eventCategory) {
        events.add(eventCategory);
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    private void initCallbacks(long windowHandle) {
        var keyboard = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                var event = new KeyboardCategory();

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
                var event = new UseDeviceCategory();
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
                var event = new MouseCategory();

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
                var event = new UseDeviceCategory();
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
                var event = new SwitchCategory();
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
}
