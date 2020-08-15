package de.sg.ogl;

import org.lwjgl.glfw.*;

import static de.sg.ogl.Log.LOGGER;

public class Input {

    private static final boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];

    public Input() {
        LOGGER.debug("Creates Input object.");
    }

    public void init(long windowHandle) {
        initCallbacks(windowHandle);
    }

    private void initCallbacks(long windowHandle) {
        GLFWKeyCallback keyboard = new GLFWKeyCallback() {
            public void invoke(long window, int key, int scancode, int action, int mods) {
                keys[key] = (action != GLFW.GLFW_RELEASE);
            }
        };

        GLFW.glfwSetKeyCallback(windowHandle, keyboard);
    }

    public static boolean isKeyDown(int key) {
        return keys[key];
    }

    public static void cleanUp() {
        LOGGER.debug("Clean up Input.");

        LOGGER.debug("There is nothing to clean up.");

        // is done by the Window class
        //keyboard.free();
    }
}
