/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.buffer;

import de.sg.ogl.SgOglEngine;
import de.sg.ogl.SgOglRuntimeException;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.opengl.GL30.*;

public class Fbo implements Buffer {

    private int id;

    private final SgOglEngine engine;
    private final int width;
    private final int height;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Fbo(SgOglEngine engine, int width, int height) {
        LOGGER.debug("Creates Fbo object.");

        this.engine = engine;
        this.width = width;
        this.height = height;

        create();
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    //-------------------------------------------------
    // Implement Buffer
    //-------------------------------------------------

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void create() {
        id = glGenFramebuffers();
        if (id == 0) {
            throw new SgOglRuntimeException("Fbo creation has failed.");
        }

        LOGGER.debug("A new Fbo was created. The Id is {}.", id);
    }

    @Override
    public void delete() {
        if (id > 0) {
            glDeleteFramebuffers(id);
            LOGGER.debug("Fbo {} was deleted.", id);
        }
    }

    @Override
    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, id);
    }

    @Override
    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    @Override
    public void cleanUp() {
        LOGGER.debug("Clean up Fbo.");

        unbind();
        delete();
    }

    //-------------------------------------------------
    // Render target
    //-------------------------------------------------

    public void bindAsRenderTarget() {
        glViewport(0, 0, width, height);
        glBindTexture(GL_TEXTURE_2D, 0);
        bind();
    }

    public void unbindRenderTarget() {
        unbind();
        glViewport(0, 0, engine.getWindow().getWidth(), engine.getWindow().getHeight());
    }
}
