package de.sg.ogl;

import org.lwjgl.glfw.*;

import java.util.Arrays;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class Input {

    private static final boolean[] KEYS = new boolean[GLFW.GLFW_KEY_LAST];
    private static final int[] KEY_STATES = new int[GLFW.GLFW_KEY_LAST];

    private static boolean[] BUTTONS = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
    private static int[] MOUSE_BUTTON_STATES = new int[GLFW.GLFW_MOUSE_BUTTON_LAST];

    private static double mouseX, mouseY;
    private static double scrollX, scrollY;

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
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                KEYS[key] = (action != GLFW_RELEASE);
                KEY_STATES[key] = action;
            }
        };

        var mouseMove = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                mouseX = xpos;
                mouseY = ypos;
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

        GLFW.glfwSetKeyCallback(windowHandle, keyboard);
        GLFW.glfwSetCursorPosCallback(windowHandle, mouseMove);
        GLFW.glfwSetMouseButtonCallback(windowHandle, mouseButtons);
        GLFW.glfwSetScrollCallback(windowHandle, mouseScroll);
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void update() {
        resetKeyboard();
        resetMouse();
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
