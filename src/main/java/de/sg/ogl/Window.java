package de.sg.ogl;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.MemoryStack;

import java.util.Objects;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class Window {

    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 5000.f;

    private final String title;
    private int width;
    private int height;
    private long windowHandle;
    private final boolean vSync;

    private Matrix4f projectionMatrix;
    private Matrix4f orthographicProjectionMatrix;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Window(String title, int width, int height, boolean vSync) {
        LOGGER.debug("Creates Window object.");

        this.title = Objects.requireNonNull(title, "Title cannot be null.");

        this.width = width;
        this.height = height;

        if (width <= 0 || height <= 0) {
            throw new SgOglException("Invalid width or height of the window.");
        }

        this.vSync = vSync;

        projectionMatrix = new Matrix4f();
        orthographicProjectionMatrix = new Matrix4f();
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public String getTitle() {
        return title;
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f getOrthographicProjectionMatrix() {
        return orthographicProjectionMatrix;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setTitle(String title) {
        glfwSetWindowTitle(windowHandle, title);
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    public void init() {
        LOGGER.debug("Initializing window.");

        // Setup an error callback.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW.
        LOGGER.debug("Configuring GLFW.");
        if (!glfwInit()) {
            throw new SgOglException("Unable to initialize GLFW.");
        }

        // Configure GLFW.
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        // Create the window.
        LOGGER.debug("Initializing a {}x{} window.", width, height);
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) {
            throw new SgOglException("Failed to create the GLFW window.");
        }

        // Setup resize callback.
        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            this.width = width;
            this.height = height;
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

        // Set/Update the projection matrix.
        updateProjectionMatrix();
        updateOrthographicProjectionMatrix();
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

    public void updateProjectionMatrix() {
        projectionMatrix.setPerspective(FOV, (float) width / height, Z_NEAR, Z_FAR);
    }

    public void updateOrthographicProjectionMatrix() {
        orthographicProjectionMatrix.setOrtho(0.0f, width, 0.0f, height, Z_NEAR, Z_FAR);
    }

    //-------------------------------------------------
    // Clean up
    //-------------------------------------------------

    public void cleanUp() {
        LOGGER.debug("Clean up Window.");

        // Free the window callbacks and destroy the window.
        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);

        // Terminate GLFW and free the error callback.
        glfwTerminate();

        // Non-window callbacks must be reset and freed separately.
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }
}
