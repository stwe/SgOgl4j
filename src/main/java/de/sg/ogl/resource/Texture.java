/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.resource;

import de.sg.ogl.SgOglRuntimeException;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static de.sg.ogl.Log.LOGGER;
import static de.sg.ogl.SgOglEngine.RUNNING_FROM_JAR;
import static org.lwjgl.opengl.ARBBindlessTexture.glGetTextureHandleARB;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture implements Resource {

    private String path;
    private final boolean loadVerticalFlipped;

    private int id = 0;
    private int width;
    private int height;
    private int nrChannels;
    private int format = 0;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Texture(String path, boolean loadVerticalFlipped) throws URISyntaxException {
        LOGGER.debug("Creates Texture object.");

        initPath(path);

        this.loadVerticalFlipped = loadVerticalFlipped;
    }

    public Texture(String path) throws URISyntaxException {
        this(path, false);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public String getPath() {
        return path;
    }

    public boolean isLoadVerticalFlipped() {
        return loadVerticalFlipped;
    }

    public int getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getNrChannels() {
        return nrChannels;
    }

    public int getFormat() {
        return format;
    }

    //-------------------------------------------------
    // Implement Resource
    //-------------------------------------------------

    @Override
    public void load() {
        ByteBuffer buf;

        if (loadVerticalFlipped) {
            stbi_set_flip_vertically_on_load(true);
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer x = stack.mallocInt(1);
            IntBuffer y = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            buf = stbi_load(path, x, y, channels, 0);
            if (buf == null) {
                throw new SgOglRuntimeException("Failed to load texture file " + path
                        + System.lineSeparator() + stbi_failure_reason());
            }

            width = x.get();
            height = y.get();
            nrChannels = channels.get();
        }

        if (nrChannels == STBI_grey)
            format = GL_RED;
        else if (nrChannels == STBI_rgb)
            format = GL_RGB;
        else if (nrChannels == STBI_rgb_alpha)
            format = GL_RGBA;

        id = generateNewTextureId();

        bind(id);

        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, buf);
        glGenerateMipmap(GL_TEXTURE_2D);

        stbi_image_free(buf);

        LOGGER.debug("Texture file {} was successfully loaded. The Id is {}.", path, id);
    }

    @Override
    public void cleanUp() {
        if (id > 0) {
            glDeleteTextures(id);

            LOGGER.debug("Texture Id {} was deleted.", id);
        }
    }

    //-------------------------------------------------
    // Bind / Unbind
    //-------------------------------------------------

    public static void bind(int id, int target) {
        glBindTexture(target, id);
    }

    public static void bind(int id) {
        bind(id, GL_TEXTURE_2D);
    }

    public static void unbind(int target) {
        glBindTexture(target, 0);
    }

    public static void unbind() {
        unbind(GL_TEXTURE_2D);
    }

    public static void bindForReading(int id, int textureUnit, int target) {
        glActiveTexture(textureUnit);
        bind(id, target);
    }

    public static void bindForReading(int id, int textureUnit) {
        bindForReading(id, textureUnit, GL_TEXTURE_2D);
    }

    //-------------------------------------------------
    // Filter
    //-------------------------------------------------

    public static void useNoFilter(int target) {
        glTexParameteri(target, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    }

    public static void useNoFilter() {
        useNoFilter(GL_TEXTURE_2D);
    }

    public static void useBilinearFilter(int target) {
        glTexParameteri(target, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    }

    public static void useBilinearFilter() {
        useBilinearFilter(GL_TEXTURE_2D);
    }

    public static void useBilinearMipmapFilter(int target) {
        glTexParameteri(target, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
    }

    public static void useBilinearMipmapFilter() {
        useBilinearMipmapFilter(GL_TEXTURE_2D);
    }

    //-------------------------------------------------
    // Wrapping
    //-------------------------------------------------

    public static void useRepeatWrapping(int target) {
        glTexParameteri(target, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(target, GL_TEXTURE_WRAP_T, GL_REPEAT);
    }

    public static void useRepeatWrapping() {
        useRepeatWrapping(GL_TEXTURE_2D);
    }

    public static void useClampToEdgeWrapping(int target) {
        glTexParameteri(target, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(target, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(target, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
    }

    public static void useClampToEdgeWrapping() {
        useClampToEdgeWrapping(GL_TEXTURE_2D);
    }

    //-------------------------------------------------
    // Texture Id/Handle
    //-------------------------------------------------

    public static int generateNewTextureId() {
        var textureId = glGenTextures();
        if (textureId == 0) {
            throw new SgOglRuntimeException("Texture name creation has failed.");
        }

        return textureId;
    }

    public static long generateNewTextureHandle(int textureId) {
        if (textureId <= 0) {
            throw new SgOglRuntimeException("Invalid texture Id " + textureId + ".");
        }

        var textureHandle = glGetTextureHandleARB(textureId);
        if (textureHandle == 0) {
            throw new SgOglRuntimeException("Texture handle creation has failed.");
        }

        return textureHandle;
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private void initPath(String path) throws URISyntaxException {
        var url = Texture.class.getProtectionDomain().getCodeSource().getLocation();
        var file = new File(url.toURI().getPath());
        this.path = file.getPath() + path;

        if (RUNNING_FROM_JAR) {
            this.path = file.getParentFile().getPath() + path;
        }
    }
}
