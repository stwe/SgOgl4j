package de.sg.ogl;

import org.lwjgl.glfw.*;

import static de.sg.ogl.Log.LOGGER;

public class Input {

    private static final boolean[] KEYS = new boolean[GLFW.GLFW_KEY_LAST];

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Input() {
        LOGGER.debug("Creates Input object.");
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    public static boolean isKeyDown(int key) {
        return KEYS[key];
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    public static void init(long windowHandle) {
        initCallbacks(windowHandle);
    }

    private static void initCallbacks(long windowHandle) {
        GLFWKeyCallback keyboard = new GLFWKeyCallback() {
            public void invoke(long window, int key, int scancode, int action, int mods) {
                KEYS[key] = (action != GLFW.GLFW_RELEASE);
            }
        };

        GLFW.glfwSetKeyCallback(windowHandle, keyboard);
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    public static void cleanUp() {
        LOGGER.debug("Clean up Input.");

        LOGGER.debug("There is nothing to clean up.");

        // is done by the Window class
        //keyboard.free();
    }
}
