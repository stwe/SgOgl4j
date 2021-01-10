/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl;

import java.io.IOException;
import java.util.Objects;

import static de.sg.ogl.Log.LOGGER;

public abstract class SgOglApplication implements Application {

    private SgOglEngine engine;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public SgOglApplication() throws IOException, IllegalAccessException {
        LOGGER.debug("Creates SgOglApplication object and load config.");

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
        this.engine = this.engine == null ? Objects.requireNonNull(engine, "engine must not be null") : throw_();
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private SgOglEngine throw_() {
        throw new SgOglRuntimeException("Engine is already set.");
    }
}
