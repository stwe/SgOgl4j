package de.sg.ogl;

import java.io.IOException;
import java.util.Objects;

import static de.sg.ogl.Log.LOGGER;

public abstract class BaseApplication implements Application {

    private SgOglEngine engine;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public BaseApplication() throws IOException, IllegalAccessException {
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
        LOGGER.debug("FPS: {}", Config.FPS);
        LOGGER.debug("Enable depth and stencil testing: {}", Config.ENABLE_DEPTH_AND_STENCIL_TESTING);
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

    public void setEngineUnlessAlreadySet(SgOglEngine engine) {
        this.engine = this.engine == null ? Objects.requireNonNull(engine, "engine must not be null") : throw_();
    }

    private SgOglEngine throw_() {
        throw new SgOglRuntimeException("Engine is already set.");
    }
}
