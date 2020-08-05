package de.sg.ogl.resource;

import java.util.HashMap;

import static de.sg.ogl.Log.LOGGER;

public class ResourceManager {

    private final HashMap<String, Resource> resources;

    //-------------------------------------------------
    // Ctors. / Dtor.
    //-------------------------------------------------

    public ResourceManager() {
        LOGGER.debug("Creates ResourceManager object.");

        resources = new HashMap<String, Resource>();
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public HashMap<String, Resource> getResources() {
        return resources;
    }

    //-------------------------------------------------
    // Load texture resources
    //-------------------------------------------------

    public Texture loadTextureResource(String path, boolean loadVerticalFlipped) {
        // try to get an existing texture
        var existingTexture = (Texture) resources.get(path);
        if (existingTexture != null) {
            return existingTexture;
        }

        // load texture
        var texture = new Texture(path, loadVerticalFlipped);
        try {
            texture.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // store texture as resource
        resources.put(path, texture);

        return texture;
    }

    public Texture loadTextureResource(String path) {
        return loadTextureResource(path, false);
    }

    //-------------------------------------------------
    // Load shader resources
    //-------------------------------------------------

    public Shader loadShaderResource(String path) {
        // try to get an existing texture
        var existingShader = (Shader) resources.get(path);
        if (existingShader != null) {
            return existingShader;
        }

        // load shader
        var shader = new Shader(path);
        try {
            shader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // store shader as resource
        resources.put(path, shader);

        return shader;
    }

    //-------------------------------------------------
    // Clean up
    //-------------------------------------------------

    public void cleanUp() {
        if (!resources.isEmpty()) {
            LOGGER.debug("Clean up {} resources.", resources.size());

            for (var resource : resources.entrySet()) {
                resource.getValue().cleanUp();
            }
        }
    }
}
