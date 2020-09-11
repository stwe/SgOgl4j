/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl;

import org.joml.Vector2d;
import org.lwjgl.glfw.*;

import java.util.Arrays;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class Input {

    private static final boolean[] KEYS = new boolean[GLFW.GLFW_KEY_LAST];
    private static final int[] KEY_STATES = new int[GLFW.GLFW_KEY_LAST];

    private static final boolean[] BUTTONS = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private static final int[] MOUSE_BUTTON_STATES = new int[GLFW.GLFW_MOUSE_BUTTON_LAST];

    private static double currentMouseX = 0.0;
    private static double currentMouseY = 0.0;
    private static double previousMouseX = -1.0;
    private static double previousMouseY = -1.0;
    private static double scrollX, scrollY;

    private static final Vector2d displVec = new Vector2d(0.0, 0.0);
    private static boolean mouseInWindow = false;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Input() {
        LOGGER.debug("Creates Input object.");

        resetKeyboard();
        resetMouse();
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public static boolean isKeyDown(int key) {
        return KEYS[key];
    }

    public static boolean isKeyPressed(int key)
    {
        return KEY_STATES[key] == GLFW_PRESS;
    }

    public static boolean isKeyReleased(int key)
    {
        return KEY_STATES[key] == GLFW_RELEASE;
    }

    public static boolean isButtonDown(int button) {
        return BUTTONS[button];
    }

    public static boolean isMouseButtonPressed(int button) {
        return MOUSE_BUTTON_STATES[button] == GLFW_RELEASE;
    }

    public static boolean isMouseButtonReleased(int button) {
        return MOUSE_BUTTON_STATES[button] == GLFW_RELEASE;
    }

    public static double getCurrentMouseX() {
        return currentMouseX;
    }

    public static double getCurrentMouseY() {
        return currentMouseY;
    }

    public static double getScrollX() {
        return scrollX;
    }

    public static double getScrollY() {
        return scrollY;
    }

    public static Vector2d getDisplVec() {
        return displVec;
    }

    public static boolean isMouseInWindow() {
        return mouseInWindow;
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    public static void init(long windowHandle) {
        initCallbacks(windowHandle);
    }

    private static void initCallbacks(long windowHandle) {
        var keyboard = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                KEYS[key] = (action != GLFW_RELEASE);
                KEY_STATES[key] = action;
            }
        };

        var mouseMove = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                currentMouseX = xpos;
                currentMouseY = ypos;
            }
        };

        var mouseButtons = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                BUTTONS[button] = (action != GLFW_RELEASE);
                MOUSE_BUTTON_STATES[button] = action;
            }
        };

        var mouseScroll = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double offsetx, double offsety) {
                scrollX += offsetx;
                scrollY += offsety;
            }
        };

        var cursorEnter = new GLFWCursorEnterCallback() {
            @Override
            public void invoke(long window, boolean entered) {
                mouseInWindow = entered;
            }
        };

        GLFW.glfwSetKeyCallback(windowHandle, keyboard);
        GLFW.glfwSetCursorPosCallback(windowHandle, mouseMove);
        GLFW.glfwSetMouseButtonCallback(windowHandle, mouseButtons);
        GLFW.glfwSetScrollCallback(windowHandle, mouseScroll);
        GLFW.glfwSetCursorEnterCallback(windowHandle, cursorEnter);
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public static void update(float dt) {
        resetKeyboard();
        resetMouse();
        updateMouse();
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private static void resetKeyboard() {
        Arrays.fill(KEY_STATES, -1);
    }

    private static void resetMouse() {
        Arrays.fill(MOUSE_BUTTON_STATES, -1);
    }

    private static void updateMouse() {
        displVec.x = 0.0;
        displVec.y = 0.0;

        if (previousMouseX > 0.0 && previousMouseY > 0.0 && mouseInWindow) {
            displVec.x = currentMouseX - previousMouseX;
            displVec.y = currentMouseY - previousMouseY;
        }

        previousMouseX = currentMouseX;
        previousMouseY = currentMouseY;
    }

    //-------------------------------------------------
    // Clean up
    //-------------------------------------------------

    public static void cleanUp() {
        LOGGER.debug("Clean up Input.");

        LOGGER.debug("There is nothing to clean up.");

        // is done by the Window class
        // e.g. keyboard.free();
    }
}
