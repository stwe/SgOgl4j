package de.sg.ogl.resource;

import java.util.HashMap;

import static de.sg.ogl.Log.LOGGER;

public class ResourceManager {

    private HashMap<String, Resource> resources;

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

    public Resource LoadTextureResource(String path, boolean loadVerticalFlipped) {
        // try to get an existing texture
        Texture existingTexture = (Texture) resources.get(path);

        if (existingTexture != null) {
            return existingTexture;
        }

        // load texture
        var texture = new Texture(path, loadVerticalFlipped);

        try {
            texture.loadTexture();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // store texture as resource
        resources.put(path, texture);

        return texture;
    }

    public Resource LoadTextureResource(String path) {
        return LoadTextureResource(path, false);
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
