/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.input;

import de.sg.ogl.SgOglEngine;
import de.sg.ogl.SgOglRuntimeException;
import de.sg.ogl.buffer.Fbo;
import de.sg.ogl.resource.Texture;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class MousePickingTexture {

    private Fbo fbo;
    private Texture colorTextureAttachment;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public MousePickingTexture(SgOglEngine engine, int width, int height) {
        LOGGER.debug("Creates MousePickingTexture object.");

        init(engine, width, height);
    }

    //-------------------------------------------------
    // Render
    //-------------------------------------------------

    public void startFrame() {
        fbo.bindAsRenderTarget();
    }

    public void endFrame() {
        fbo.unbindRenderTarget();
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    private void createColorTextureAttachment(int width, int height) {
        colorTextureAttachment = new Texture();
        Texture.bind(colorTextureAttachment.getId());

        Texture.useNoFilter();
        Texture.useRepeatWrapping();

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, width, height, 0, GL_RGB, GL_FLOAT, 0);
    }

    private void attachColorTextureToFbo() {
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, colorTextureAttachment.getId(), 0);
    }

    private void checkFbo() {
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw new SgOglRuntimeException("Error while creating Fbo attachments.");
        }
    }

    private void init(SgOglEngine engine, int width, int height) {
        fbo = new Fbo(engine, width, height);
        fbo.bind();

        glDrawBuffer(GL_COLOR_ATTACHMENT0);

        createColorTextureAttachment(width, height);
        attachColorTextureToFbo();
        checkFbo();

        Texture.unbind();
        fbo.unbind();
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    public int readPixel(int x, int y) {
        fbo.bind();
        glReadBuffer(GL_COLOR_ATTACHMENT0);

        float[] pixels = new float[3];
        glReadPixels(x, y, 1, 1, GL_RGB, GL_FLOAT, pixels);

        return (int)(pixels[0]) - 1;
    }

    //-------------------------------------------------
    // Clean up
    //-------------------------------------------------

    public void cleanUp() {
        LOGGER.debug("Clean up MousePickingTexture.");

        colorTextureAttachment.cleanUp();
    }
}
