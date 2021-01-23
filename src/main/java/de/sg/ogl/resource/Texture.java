/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.resource;

import de.sg.ogl.SgOglRuntimeException;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.BufferUtils.createByteBuffer;
import static org.lwjgl.opengl.ARBBindlessTexture.glGetTextureHandleARB;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture implements Resource {

    private String path;
    private boolean loadVerticalFlipped;

    private int id;
    private int width;
    private int height;
    private int nrChannels;
    private int format;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    Texture(String path, boolean loadVerticalFlipped) {
        LOGGER.debug("Creates Texture object.");

        this.path = path;
        this.loadVerticalFlipped = loadVerticalFlipped;
    }

    Texture(String path) {
        this(path, false);
    }

    public Texture() {
        id = generateNewTextureId();
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
    // Setter
    //-------------------------------------------------

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setNrChannels(int nrChannels) {
        this.nrChannels = nrChannels;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    //-------------------------------------------------
    // Create
    //-------------------------------------------------

    public static Texture createTexture(int width, int height, ByteBuffer data) {
        var texture = new Texture();

        texture.setWidth(width);
        texture.setHeight(height);
        texture.setNrChannels(4);
        texture.setFormat(GL_RGBA);

        bind(texture.getId());

        useNoFilter();
        useClampToBorderWrapping();

        // todo

        //texture.uploadData(GL_RGBA8, width, height, GL_RGBA, data);
        /*
        public void uploadData(int internalFormat, int width, int height, int format, ByteBuffer data) {
            glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, data);
        }
        */

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, texture.getFormat(), GL_UNSIGNED_BYTE, data);

        unbind();

        return texture;
    }

    //-------------------------------------------------
    // Implement Resource
    //-------------------------------------------------

    @Override
    public void load() throws IOException {
        stbi_set_flip_vertically_on_load(loadVerticalFlipped);

        ByteBuffer buffer;
        var source = Texture.class.getResourceAsStream(path);
        var rbc = Channels.newChannel(source);
        buffer = createByteBuffer(8 * 1024);

        while (true) {
            var bytes = rbc.read(buffer);

            if (bytes == -1) {
                break;
            }

            if (buffer.remaining() == 0) {
                buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2);
            }
        }

        buffer.flip();

        ByteBuffer imageBuffer;
        try (var stack = MemoryStack.stackPush()) {
            var x = stack.mallocInt(1);
            var y = stack.mallocInt(1);
            var channels = stack.mallocInt(1);

            imageBuffer = stbi_load_from_memory(buffer, x, y, channels, 0);
            if (imageBuffer == null) {
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

        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, imageBuffer);
        glGenerateMipmap(GL_TEXTURE_2D);

        stbi_image_free(imageBuffer);

        LOGGER.debug("Texture file {} was successfully loaded. The Id is {}.", path, id);
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        var newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);

        return newBuffer;
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
        // make sure that the OpenGL constants are used here
        if (textureUnit < GL_TEXTURE0) {
            throw new SgOglRuntimeException("Invalid texture unit value.");
        }

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

    public static void useClampToBorderWrapping(int target) {
        glTexParameteri(target, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
        glTexParameteri(target, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
    }

    public static void useClampToBorderWrapping() {
        useClampToBorderWrapping(GL_TEXTURE_2D);
    }

    public static void setBorderColor(float r, float g, float b) {
        float[] color = { r, g, b, 1.0f };
        glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, color);
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
}
