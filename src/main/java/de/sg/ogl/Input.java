package de.sg.ogl;

import org.lwjgl.glfw.*;

import static de.sg.ogl.Log.LOGGER;

public class Input {

    private static final boolean[] KEYS = new boolean[GLFW.GLFW_KEY_LAST];
    private static boolean[] BUTTONS = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private static double mouseX, mouseY;
    private static double scrollX, scrollY;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Input() {
        LOGGER.debug("Creates Input object.");
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public static boolean isKeyDown(int key) {
        return KEYS[key];
    }

    public static boolean isButtonDown(int button) {
        return BUTTONS[button];
    }

    public static double getMouseX() {
        return mouseX;
    }

    public static double getMouseY() {
        return mouseY;
    }

    public static double getScrollX() {
        return scrollX;
    }

    public static double getScrollY() {
        return scrollY;
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    public static void init(long windowHandle) {
        initCallbacks(windowHandle);
    }

    private static void initCallbacks(long windowHandle) {
        var keyboard = new GLFWKeyCallback() {
            public void invoke(long window, int key, int scancode, int action, int mods) {
                KEYS[key] = (action != GLFW.GLFW_RELEASE);
            }
        };

        var mouseMove = new GLFWCursorPosCallback() {
            public void invoke(long window, double xpos, double ypos) {
                mouseX = xpos;
                mouseY = ypos;
            }
        };

        var mouseButtons = new GLFWMouseButtonCallback() {
            public void invoke(long window, int button, int action, int mods) {
                BUTTONS[button] = (action != GLFW.GLFW_RELEASE);
            }
        };

        var mouseScroll = new GLFWScrollCallback() {
            public void invoke(long window, double offsetx, double offsety) {
                scrollX += offsetx;
                scrollY += offsety;
            }
        };

        GLFW.glfwSetKeyCallback(windowHandle, keyboard);
        GLFW.glfwSetCursorPosCallback(windowHandle, mouseMove);
        GLFW.glfwSetMouseButtonCallback(windowHandle, mouseButtons);
        GLFW.glfwSetScrollCallback(windowHandle, mouseScroll);
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    public static void cleanUp() {
        LOGGER.debug("Clean up Input.");

        LOGGER.debug("There is nothing to clean up.");

        // is done by the Window class
        // e.g. keyboard.free();
    }
}
