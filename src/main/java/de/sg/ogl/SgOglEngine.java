/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl;

import de.sg.ogl.resource.ResourceManager;

import java.io.File;
import java.util.Objects;

import static de.sg.ogl.Log.LOGGER;

public class SgOglEngine implements Runnable {

    public static final boolean RUNNING_FROM_JAR = isRunningFromJar();

    private final Window window;
    private final BaseApplication application;
    private final ResourceManager resourceManager;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public SgOglEngine(BaseApplication application) {
        LOGGER.debug("Creates SgOglEngine object.");

        LOGGER.info("Running from Jar: {}", RUNNING_FROM_JAR ? "yes" : "no");

        this.window = new Window();
        this.application = Objects.requireNonNull(application, "application must not be null");
        this.resourceManager = new ResourceManager();

        this.application.setEngineUnlessAlreadySet(this);
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
            this.loop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanUp();
        }

        LOGGER.debug("Goodbye ...");
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    private void init() throws Exception {
        LOGGER.debug("Initializing SgOglEngine.");

        window.init();
        Input.init(window.getWindowHandle());
        application.init();
    }

    private static String getJarName() {
        return new File(SgOglEngine.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath())
                .getName();
    }

    private static boolean isRunningFromJar()
    {
        return getJarName().contains(".jar");
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    private void input() {
        application.input();
    }

    private void update(float dt) {
        Input.update(dt);
        application.update(dt);
    }

    private void render() {
        OpenGL.clear();
        application.render();
        window.update();
    }

    //-------------------------------------------------
    // Loop
    //-------------------------------------------------

    private void loop() {
        LOGGER.debug("Starting the main loop.");

        OpenGL.setClearColor(0.3f, 0.4f, 0.5f, 1.0f);
        renderingLoop();
    }

    private void renderingLoop() {
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
        Input.cleanUp();
        window.cleanUp();
        application.cleanUp();
    }
}
