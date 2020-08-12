package de.sg.ogl;

import static de.sg.ogl.Log.LOGGER;

public abstract class BaseApplication implements Application {

    private SgOglEngine engine;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public BaseApplication() {
        LOGGER.debug("Creates Application object and load config.");

        ConfigLoader.load(Config.class, "/config.properties");
        LOGGER.debug("Configuration loaded successfully.");
        LOGGER.debug("Title: {}", Config.TITLE);
        LOGGER.debug("Width: {}", Config.WIDTH);
        LOGGER.debug("Height: {}", Config.HEIGHT);
        LOGGER.debug("VSync: {}", Config.V_SYNC);
        LOGGER.debug("Fov: {}", Config.FOV);
        LOGGER.debug("Near: {}", Config.NEAR);
        LOGGER.debug("Far: {}", Config.FAR);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public SgOglEngine getEngine() {
        return engine;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setEngine(SgOglEngine engine) {
        this.engine = engine;
    }
}
