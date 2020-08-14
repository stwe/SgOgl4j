package de.sg.ogl;

import org.lwjgl.glfw.*;

public class Input {

    private static final boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];

    public void init(long windowHandle) {
        initCallbacks(windowHandle);
    }

    private void initCallbacks(long windowHandle) {
        var keyboard = new GLFWKeyCallback() {
            public void invoke(long window, int key, int scancode, int action, int mods) {
                keys[key] = (action != GLFW.GLFW_RELEASE);
            }
        };

        GLFW.glfwSetKeyCallback(windowHandle, keyboard);
    }

    public static boolean isKeyDown(int key) {
        return keys[key];
    }

    public void cleanUp() {
        // is done by the Window class
        //keyboard.free();
    }
}
