package de.sg.ogl.resource;

import de.sg.ogl.SgOglException;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class Texture implements Resource {

    private final String url;
    private final boolean loadVerticalFlipped;

    private int id = 0;
    private int width;
    private int height;
    private int nrChannels;
    private int format = 0;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Texture(String url, boolean loadVerticalFlipped) {
        LOGGER.debug("Creates Texture object.");

        this.url = url;
        this.loadVerticalFlipped = loadVerticalFlipped;
    }

    public Texture(String path) {
        this(path, false);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public String getUrl() {
        return url;
    }

    public boolean isLoadVerticalFlipped() {
        return loadVerticalFlipped;
    }

    public int getId() {
        assert id > 0 : "The \"load\" function must be called.";
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getNrChannels() {
        assert id > 0 : "The \"load\" function must be called.";
        return nrChannels;
    }

    public int getFormat() {
        assert id > 0 : "The \"load\" function must be called.";
        return format;
    }

    //-------------------------------------------------
    // Implement Resource
    //-------------------------------------------------

    @Override
    public void load() throws Exception {
        ByteBuffer buf;

        if (loadVerticalFlipped) {
            stbi_set_flip_vertically_on_load(true);
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer x = stack.mallocInt(1);
            IntBuffer y = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            buf = stbi_load(url, x, y, channels, 0);
            if (buf == null) {
                throw new SgOglException("Image file [" + url  + "] not loaded: " + stbi_failure_reason());
            }

            width = x.get();
            height = y.get();
            nrChannels = channels.get();

            assert width > 0;
            assert height > 0;
            assert nrChannels > 0;
        }

        if (nrChannels == STBI_grey)
            format = GL_RED;
        else if (nrChannels == STBI_rgb)
            format = GL_RGB;
        else if (nrChannels == STBI_rgb_alpha)
            format = GL_RGBA;

        assert format > 0;

        id = generateNewTextureHandle();

        assert id > 0;

        bind(id);

        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, buf);
        glGenerateMipmap(GL_TEXTURE_2D);

        stbi_image_free(buf);

        LOGGER.debug("Texture file {} was successfully loaded. The Id is {}.", url, id);
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

    static public void bind(int id, int target) {
        assert id > 0;
        glBindTexture(target, id);
    }

    static public void bind(int id) {
        bind(id, GL_TEXTURE_2D);
    }

    static public void unbind(int target) {
        glBindTexture(target, 0);
    }

    static public void unbind() {
        unbind(GL_TEXTURE_2D);
    }

    static public void bindForReading(int id, int textureUnit, int target) {
        glActiveTexture(id);
        bind(id, target);
    }

    static public void bindForReading(int id, int textureUnit) {
        bindForReading(id, textureUnit, GL_TEXTURE_2D);
    }

    //-------------------------------------------------
    // Filter
    //-------------------------------------------------

    static public void useNoFilter(int target) {
        glTexParameteri(target, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    }

    static public void useNoFilter() {
        useNoFilter(GL_TEXTURE_2D);
    }

    static public void useBilinearFilter(int target) {
        glTexParameteri(target, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    }

    static public void useBilinearFilter() {
        useBilinearFilter(GL_TEXTURE_2D);
    }

    static public void useBilinearMipmapFilter(int target) {
        glTexParameteri(target, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(target, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
    }

    static public void useBilinearMipmapFilter() {
        useBilinearMipmapFilter(GL_TEXTURE_2D);
    }

    //-------------------------------------------------
    // Wrapping
    //-------------------------------------------------

    static public void useRepeatWrapping(int target) {
        glTexParameteri(target, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(target, GL_TEXTURE_WRAP_T, GL_REPEAT);
    }

    static public void useRepeatWrapping() {
        useRepeatWrapping(GL_TEXTURE_2D);
    }

    static public void useClampToEdgeWrapping(int target) {
        glTexParameteri(target, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(target, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(target, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
    }

    static public void useClampToEdgeWrapping() {
        useClampToEdgeWrapping(GL_TEXTURE_2D);
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    static private int generateNewTextureHandle() {
        var textureId = glGenTextures();
        if (textureId == 0) {
            throw new SgOglException("Texture name creation has failed.");
        }

        return textureId;
    }
}
