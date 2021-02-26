/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl;

import de.sg.ogl.input.EventQueue;
import de.sg.ogl.input.KeyInput;
import de.sg.ogl.input.MouseInput;
import de.sg.ogl.resource.ResourceManager;
import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import java.io.File;
import java.util.Objects;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

public class SgOglEngine implements Runnable {

    public static final boolean RUNNING_FROM_JAR = isRunningFromJar();

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private final Window window;
    private final EventQueue eventQueue;
    private final ResourceManager resourceManager;

    private final SgOglApplication application;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public SgOglEngine(SgOglApplication application) {
        LOGGER.debug("Creates SgOglEngine object.");

        LOGGER.info("The SgOgl4j lib runs from a Jar file: {}", RUNNING_FROM_JAR ? "yes" : "no");

        this.window = new Window();
        this.eventQueue = new EventQueue();
        this.resourceManager = new ResourceManager();

        this.application = Objects.requireNonNull(application, "application must not be null");
        this.application.setEngine(this);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public Window getWindow() {
        return window;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    //-------------------------------------------------
    // Implement Runnable
    //-------------------------------------------------

    @Override
    public void run() {
        LOGGER.debug("Running SgOglEngine.");

        try {
            this.init();
            this.renderingLoop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.cleanUp();
        }

        LOGGER.debug("Goodbye ...");
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    private void init() throws Exception {
        LOGGER.debug("Initializing SgOglEngine.");

        window.init();
        eventQueue.init(window.getWindowHandle());

        imGuiGlfw.init(window.getWindowHandle(), true);
        imGuiGl3.init("#version 130");

        application.init();
    }

    private static String getJarName() {
        return new File(SgOglEngine.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath())
                .getName();
    }

    private static boolean isRunningFromJar() {
        return getJarName().contains(".jar");
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    private void input() {
        eventQueue.input();

        application.input();

        MouseInput.input();
        KeyInput.input();
    }

    private void update(float dt) {
        application.update(dt);
    }

    private void render() {
        startFrame();
        frame();
        endFrame();
    }

    private void startFrame() {
        OpenGL.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        OpenGL.clear();
    }

    private void frame() {
        application.render();

        imGuiGlfw.newFrame();
        ImGui.newFrame();

        application.renderImGui();
        ImGui.render();
    }

    private void endFrame() {
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            glfwMakeContextCurrent(backupWindowPtr);
        }

        window.update();
    }

    //-------------------------------------------------
    // Loop
    //-------------------------------------------------

    private void renderingLoop() {
        LOGGER.debug("Starting the main loop.");

        var lastTime = System.nanoTime();
        var timer = System.currentTimeMillis();
        final var frameTime = 1000000000.0 / Config.FPS;
        final var frameTimeS = 1.0f / (float)Config.FPS;
        var dt = 0.0;
        var fps = 0;
        var updates = 0;

        while(!window.windowShouldClose()) {
            var now = System.nanoTime();
            dt += (now - lastTime) / frameTime;
            lastTime = now;

            input();

            while (dt >= 1.0) {
                update(frameTimeS);
                updates++;
                dt--;
            }

            render();
            fps++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                window.setTitle(window.getTitle() + "  |  " + fps + " frames  |  " + updates + " updates");
                updates = 0;
                fps = 0;
            }

            // todo vsync
        }
    }

    //-------------------------------------------------
    // Clean up
    //-------------------------------------------------

    private void cleanUp() {
        LOGGER.debug("Clean up SgOglEngine.");

        resourceManager.cleanUp();

        imGuiGl3.dispose();
        imGuiGlfw.dispose();

        window.cleanUp();
        application.cleanUp();
    }
}
