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

import static org.lwjgl.glfw.GLFW.*;

public class KeyInput {

    private static KeyInput instance;

    private final boolean[] KEYS = new boolean[GLFW.GLFW_KEY_LAST];
    private final int[] KEY_STATES = new int[GLFW.GLFW_KEY_LAST];

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    private KeyInput() {}

    //-------------------------------------------------
    // Singleton
    //-------------------------------------------------

    public static KeyInput get() {
        if (instance == null) {
            instance = new KeyInput();
        }

        return instance;
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public static void input() {
        Arrays.fill(get().KEYS, false);
        Arrays.fill(get().KEY_STATES, -1);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public static boolean isKeyDown(int key) {
        if (isValidKey(key)) {
            return get().KEYS[key];
        }

        return false;
    }

    public static boolean isKeyPressed(int key) {
        if (isValidKey(key)) {
            return get().KEY_STATES[key] == GLFW_PRESS;
        }

        return false;
    }

    public static boolean isKeyReleased(int key) {
        if (isValidKey(key)) {
            return get().KEY_STATES[key] == GLFW_RELEASE;
        }

        return false;
    }

    public static boolean isKeyRepeated(int key) {
        if (isValidKey(key)) {
            return get().KEY_STATES[key] == GLFW_REPEAT;
        }

        return false;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public static void setKey(int key, boolean value) {
        if (isValidKey(key)) {
            get().KEYS[key] = value;
        }
    }

    public static void setKeyState(int key, int action) {
        if (isValidKey(key)) {
            get().KEY_STATES[key] = action;
        }
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private static boolean isValidKey(int key) {
        return key >= 0 && key < get().KEYS.length;
    }
}
