/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.input;

import org.lwjgl.glfw.*;

import java.util.ArrayDeque;
import java.util.Queue;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.glfw.GLFW.*;

public class EventQueue {

    private final Queue<VisitableEvent> mouseMotionEvents = new ArrayDeque<>();
    private final Queue<VisitableEvent> mouseButtonEvents = new ArrayDeque<>();
    private final Queue<VisitableEvent> keyInputEvents = new ArrayDeque<>();

    private final CoreVisitor coreVisitor = new CoreVisitor();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public EventQueue() {
        LOGGER.debug("Creates EventQueue object.");
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void input() {
        while (!keyInputEvents.isEmpty()) {
            keyInputEvents.poll().accept(coreVisitor);
        }

        while (!mouseMotionEvents.isEmpty()) {
            mouseMotionEvents.poll().accept(coreVisitor);
        }

        while (!mouseButtonEvents.isEmpty()) {
            mouseButtonEvents.poll().accept(coreVisitor);
        }
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    public void init(long windowHandle) {
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

                keyInputEvents.add(event);
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

                mouseMotionEvents.add(event);
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

                mouseButtonEvents.add(event);
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

                mouseMotionEvents.add(event);
            }
        };

        var cursorEnter = new GLFWCursorEnterCallback() {
            @Override
            public void invoke(long window, boolean entered) {
                var event = new SwitchEvent();

                event.eventType = entered ? EventType.CURSOR_ENTERED : EventType.CURSOR_LEFT;
                event.windowHandle = window;
                event.value = entered;

                mouseMotionEvents.add(event);
            }
        };

        GLFW.glfwSetKeyCallback(windowHandle, keyboard);
        GLFW.glfwSetCursorPosCallback(windowHandle, mouseMove);
        GLFW.glfwSetMouseButtonCallback(windowHandle, mouseButtons);
        GLFW.glfwSetScrollCallback(windowHandle, mouseScroll);
        GLFW.glfwSetCursorEnterCallback(windowHandle, cursorEnter);
    }
}
