/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

import imgui.*;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiConfigFlags;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.MemoryStack;

import java.util.Objects;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class Window {

    private static final int MIN_WIDTH = 640;
    private static final int MIN_HEIGHT = 480;

    private final String title;
    private int width;
    private int height;
    private final boolean vSync;

    private final Matrix4f projectionMatrix;
    private final Matrix4f orthographicProjectionMatrix;

    private long windowHandle;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Window() {
        LOGGER.debug("Creates Window object.");

        this.title = Objects.requireNonNull(Config.TITLE, "title must not be null");
        this.width = Config.WIDTH;
        this.height = Config.HEIGHT;
        this.vSync = Config.V_SYNC;

        if (width < MIN_WIDTH || height < MIN_HEIGHT) {
            this.width = MIN_WIDTH;
            this.height = MIN_HEIGHT;
            LOGGER.warn("Invalid width or height value given. The default values are now used.");
        }

        projectionMatrix = new Matrix4f();
        orthographicProjectionMatrix = new Matrix4f();
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f getOrthographicProjectionMatrix() {
        return orthographicProjectionMatrix;
    }

    public long getWindowHandle() {
        return windowHandle;
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
        initGlfw();
        initImGui();
        initProjectionMatrix();
    }

    private void initGlfw() {
        LOGGER.debug("Initializing Window.");

        // Setup an error callback.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW.
        LOGGER.debug("Configuring GLFW.");
        if (!glfwInit()) {
            throw new SgOglRuntimeException("Unable to initialize GLFW.");
        }

        // Configure GLFW.
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        // At the moment, the size of the window can no be changed during runtime.
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 4);

        // Create the window.
        LOGGER.debug("Initializing a {}x{} window.", width, height);
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) {
            throw new SgOglRuntimeException("Failed to create the GLFW window.");
        }

        // Setup resize callback.
        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            this.width = width;
            this.height = height;
        });

        // Get the thread stack and push a new frame.
        try (MemoryStack stack = stackPush()) {
            var pWidth = stack.mallocInt(1);
            var pHeight = stack.mallocInt(1);

            // Get the window size passed to glfwCreateWindow.
            glfwGetWindowSize(windowHandle, pWidth, pHeight);

            // Get the resolution of the primary monitor.
            var vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if (vidmode == null) {
                throw new SgOglRuntimeException("Failed to get the current video mode.");
            }

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
            glfwSwapInterval(GLFW_TRUE);
            LOGGER.info("VSync is enabled.");
        }

        // Make the window visible.
        glfwShowWindow(windowHandle);

        // Makes the OpenGL bindings available for use.
        OpenGL.init();
    }

    private void initImGui() {
        LOGGER.debug("Initializing ImGui.");

        ImGui.createContext();

        final var io = ImGui.getIO();

        io.setIniFilename(null); // We don't want to save .ini file
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);  // Enable Keyboard Controls
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);      // Enable Docking
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);    // Enable Multi-Viewport / Platform Windows
        io.setConfigViewportsNoTaskBarIcon(true);

        if (io.hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final var style = ImGui.getStyle();
            style.setWindowRounding(0.0f);
            style.setColor(ImGuiCol.WindowBg, ImGui.getColorU32(ImGuiCol.WindowBg, 1));
        }
    }

    private void initProjectionMatrix() {
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
        projectionMatrix.setPerspective(Config.FOV, (float) width / height, Config.NEAR, Config.FAR);
    }

    public void updateOrthographicProjectionMatrix() {
        /*
        ---------------
        | 0, 0        |
        |             |
        |             |
        |        w, h |
        ---------------
        */

        orthographicProjectionMatrix.setOrtho(0.0f, width, height, 0.0f, 1.0f, -1.0f);
    }

    //-------------------------------------------------
    // Clean up
    //-------------------------------------------------

    public void cleanUp() {
        LOGGER.debug("Clean up Window.");

        // Clean up ImGui.
        ImGui.destroyContext();

        // Frees callbacks associated with the window.
        glfwFreeCallbacks(windowHandle);

        // Destroy the window.
        glfwDestroyWindow(windowHandle);

        // Terminate GLFW.
        glfwTerminate();

        // Non-window callbacks must be reset and freed separately.
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }
}
