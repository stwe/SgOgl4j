/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.input;

import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseInput {

    private static final long MOUSE_DOUBLE_CLICK_PERIOD_NS = 1000000000 / 5;

    private static MouseInput instance;

    private double currentMouseX = 0.0;
    private double currentMouseY = 0.0;
    private double previousMouseX = -1.0;
    private double previousMouseY = -1.0;
    private double scrollX = 0.0;
    private double scrollY = 0.0;
    private boolean mouseInWindow = false;
    private final boolean[] BUTTONS = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private final int[] BUTTON_STATES = new int[GLFW.GLFW_MOUSE_BUTTON_LAST];

    private long lastButtonReleaseNs = 0;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    private MouseInput() {}

    //-------------------------------------------------
    // Singleton
    //-------------------------------------------------

    public static MouseInput get() {
        if (instance == null) {
            instance = new MouseInput();
        }

        return instance;
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public static void input() {
        update();
    }

    public static void update() {
        get().previousMouseX = get().currentMouseX;
        get().previousMouseY = get().currentMouseY;

        get().scrollX = 0.0;
        get().scrollY = 0.0;

        Arrays.fill(get().BUTTONS, false);
        Arrays.fill(get().BUTTON_STATES, -1);

        var now = System.nanoTime();
        if (now - get().lastButtonReleaseNs > MOUSE_DOUBLE_CLICK_PERIOD_NS) {
            get().lastButtonReleaseNs = 0;
        }
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

    public static boolean isMouseButtonDown(int button) {
        if (isValidButton(button)) {
            return get().BUTTONS[button];
        }

        return false;
    }

    public static boolean isMouseButtonPressed(int button) {
        if (isValidButton(button)) {
            return get().BUTTON_STATES[button] == GLFW_PRESS;
        }

        return false;
    }

    public static boolean isMouseButtonReleased(int button) {
        if (isValidButton(button)) {
            var result = get().BUTTON_STATES[button] == GLFW_RELEASE;
            if (result) {
                get().lastButtonReleaseNs = System.nanoTime();
            }

            return result;
        }

        return false;
    }

    public static boolean isMouseButtonDoubleClicked(int button) {
        var last = get().lastButtonReleaseNs;
        var released = isMouseButtonReleased(button);

        var now = System.nanoTime();

        if (released && now - last < MOUSE_DOUBLE_CLICK_PERIOD_NS) {
            get().lastButtonReleaseNs = 0;

            return true;
        }

        return false;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public static void setX(double newMouseX) {
        get().previousMouseX = get().currentMouseX;
        get().currentMouseX = newMouseX;
    }

    public static void setY(double newMouseY) {
        get().previousMouseY = get().currentMouseY;
        get().currentMouseY = newMouseY;
    }

    public static void setScrollX(double scrollX) {
        get().scrollX = scrollX;
    }

    public static void setScrollY(double scrollY) {
        get().scrollY = scrollY;
    }

    public static void setMouseInWindow(boolean mouseInWindow) {
        get().mouseInWindow = mouseInWindow;
    }

    public static void setMouseButton(int button, boolean value) {
        if (isValidButton(button)) {
            get().BUTTONS[button] = value;
        }
    }

    public static void setMouseButtonState(int button, int action) {
        if (isValidButton(button)) {
            get().BUTTON_STATES[button] = action;
        }
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private static boolean isValidButton(int button) {
        return button >= 0 && button < get().BUTTONS.length;
    }
}
