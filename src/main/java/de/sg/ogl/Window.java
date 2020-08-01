package de.sg.ogl;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.MemoryStack;

import java.util.Objects;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private final String title;
    private int width;
    private int height;
    private long windowHandle;
    private boolean vSync;

    public Window(String title, int width, int height, boolean vSync) {
        LOGGER.debug("Creates Window object.");

        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
    }

    public void init() {
        LOGGER.debug("Initializing window.");

        // Setup an error callback.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW.
        LOGGER.debug("Configuring GLFW.");
        if (!glfwInit()) {
            throw new IllegalStateException("[...Window] Unable to initialize GLFW.");
        }

        // Configure GLFW.
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        assert !title.isEmpty();
        assert width > 0;
        assert height > 0;

        // Create the window.
        LOGGER.debug("Initializing a {}x{} window.", width, height);
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) {
            throw new RuntimeException("[...Window] Failed to create the GLFW window.");
        }

        // Setup resize callback.
        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            this.width = width;
            this.height = height;
        });

        // todo
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop.
            }
        });

        // Get the thread stack and push a new frame.
        try (MemoryStack stack = stackPush()) {
            var pWidth = stack.mallocInt(1); // int*
            var pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow.
            glfwGetWindowSize(windowHandle, pWidth, pHeight);

            // Get the resolution of the primary monitor.
            var vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window.
            glfwSetWindowPos(
                    windowHandle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // The stack frame is popped automatically.

        // Determines what window is the opengl context.
        glfwMakeContextCurrent(windowHandle);

        if (vSync) {
            // Enable v-sync.
            glfwSwapInterval(1);
            LOGGER.info("VSync is enabled.");
        }

        // Make the window visible.
        glfwShowWindow(windowHandle);
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        glfwSetWindowTitle(windowHandle, title);
    }

    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

    public void cleanUp() {
        LOGGER.debug("Clean up Window object.");

        // Free the window callbacks and destroy the window.
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);

        // Terminate GLFW and free the error callback.
        glfwTerminate();

        // Non-window callbacks must be reset and freed separately.
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }
}
