package de.sg.ogl.resource;

import java.util.HashMap;

public class ResourceManager {
    private HashMap<String, Object> resources;

    public ResourceManager() {
        resources = new HashMap<String, Object>();
    }

    public int LoadTexture(String path) {
        Texture texture = (Texture) resources.get(path);

        if (texture != null) {
            return texture.getResourceId();
        }

        // todo load process

        return 0;
    }

    public HashMap<String, Object> getResources() {
        return resources;
    }
}
