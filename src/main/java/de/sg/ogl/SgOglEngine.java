package de.sg.ogl;

import de.sg.ogl.resource.ResourceManager;

import static de.sg.ogl.Log.LOGGER;

public class SgOglEngine implements Runnable {

    private final Window window;
    private final BaseApplication application;
    private final ResourceManager resourceManager;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public SgOglEngine(String title, int width, int height, boolean vSync, BaseApplication application) {
        LOGGER.debug("Creates SgOglEngine object.");

        this.window = new Window(title, width, height, vSync);
        this.application = application;
        this.resourceManager = new ResourceManager();

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
        OpenGL.init();
        application.init();
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    private void input() {
        application.input();
    }

    private void update() {
        application.update();
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
        final var frameTime = 1000000000.0 / 60.0;
        var dt = 0.0;
        var fps = 0;
        var updates = 0;

        while(!window.windowShouldClose()) {
            var now = System.nanoTime();
            dt += (now - lastTime) / frameTime;
            lastTime = now;

            input();

            while (dt >= 1.0) {
                update();
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
        window.cleanUp();
        application.cleanUp();
    }
}
