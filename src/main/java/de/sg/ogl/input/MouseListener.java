/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.input;

import org.lwjgl.glfw.*;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {

    private static MouseListener instance;

    private double currentMouseX = 0.0;
    private double currentMouseY = 0.0;
    private double previousMouseX = -1.0;
    private double previousMouseY = -1.0;
    private double scrollX = 0.0;
    private double scrollY = 0.0;
    private boolean mouseInWindow = false;
    private final boolean[] BUTTONS = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    private MouseListener() {}

    //-------------------------------------------------
    // Singleton
    //-------------------------------------------------

    public static MouseListener get() {
        if (instance == null) {
            instance = new MouseListener();
        }

        return instance;
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    public static void init(long windowHandle) {
        initCallbacks(windowHandle);
    }

    private static void initCallbacks(long windowHandle) {
        var mouseMove = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                MouseListener.get().previousMouseX = MouseListener.get().currentMouseX;
                MouseListener.get().previousMouseY = MouseListener.get().currentMouseY;
                MouseListener.get().currentMouseX = xpos;
                MouseListener.get().currentMouseY = ypos;
            }
        };

        var mouseButtons = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (button >= 0 && button < MouseListener.get().BUTTONS.length) {
                    switch (action) {
                        case GLFW_RELEASE:
                            MouseListener.get().BUTTONS[button] = false;
                            break;
                        case GLFW_PRESS:
                            MouseListener.get().BUTTONS[button] = true;
                            break;
                        default:
                            break;
                    }
                }
            }
        };

        var mouseScroll = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double offsetx, double offsety) {
                MouseListener.get().scrollX = offsetx;
                MouseListener.get().scrollY = offsety;
            }
        };

        var cursorEnter = new GLFWCursorEnterCallback() {
            @Override
            public void invoke(long window, boolean entered) {
                MouseListener.get().mouseInWindow = entered;
            }
        };

        GLFW.glfwSetCursorPosCallback(windowHandle, mouseMove);
        GLFW.glfwSetMouseButtonCallback(windowHandle, mouseButtons);
        GLFW.glfwSetScrollCallback(windowHandle, mouseScroll);
        GLFW.glfwSetCursorEnterCallback(windowHandle, cursorEnter);
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public static void update() {
        get().previousMouseX = get().currentMouseX;
        get().previousMouseY = get().currentMouseY;
        get().scrollX = 0.0;
        get().scrollY = 0.0;
        get().mouseInWindow = false;
        Arrays.fill(get().BUTTONS, false);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public static float getX() {
        return (float)get().currentMouseX;
    }

    public static float getY() {
        return (float)get().currentMouseY;
    }

    public static float getDx() {
        return (float)(get().previousMouseX - get().currentMouseX);
    }

    public static float getDy() {
        return (float)(get().previousMouseY - get().currentMouseY);
    }

    public static float getScrollX() {
        return (float)get().scrollX;
    }

    public static float getScrollY() {
        return (float)get().scrollY;
    }

    public static boolean isMouseInWindow() {
        return get().mouseInWindow;
    }

    public static boolean isMouseButtonPressed(int button) {
        if (button >= 0 && button < get().BUTTONS.length) {
            return get().BUTTONS[button];
        }

        return false;
    }
}
