/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {

    private static KeyListener instance;

    private final boolean[] KEYS = new boolean[GLFW.GLFW_KEY_LAST];

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    private KeyListener() {}

    //-------------------------------------------------
    // Singleton
    //-------------------------------------------------

    public static KeyListener get() {
        if (instance == null) {
            instance = new KeyListener();
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
        var keyboard = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key >= 0 && key < KeyListener.get().KEYS.length) {
                    switch (action) {
                        case GLFW_RELEASE:
                            KeyListener.get().KEYS[key] = false;
                            break;
                        case GLFW_PRESS:
                            KeyListener.get().KEYS[key] = true;
                            break;
                        default:
                            break;
                    }
                }
            }
        };

        GLFW.glfwSetKeyCallback(windowHandle, keyboard);
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public static void update() {
        Arrays.fill(get().KEYS, false);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public static boolean isKeyPressed(int key) {
        if (key >= 0 && key < get().KEYS.length) {
            return get().KEYS[key];
        }

        return false;
    }
}
