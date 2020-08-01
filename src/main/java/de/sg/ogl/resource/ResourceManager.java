package de.sg.ogl.resource;

import java.util.HashMap;

public class ResourceManager {
    HashMap<String, Integer> textures;
    HashMap<String, Integer> shaders;
    HashMap<String, Model> models;

    public ResourceManager() {
        textures = new HashMap<String, Integer>();
        shaders = new HashMap<String, Integer>();
        models = new HashMap<String, Model>();
    }

    public void testMaps() {
        textures.put("TestTexture", 1);
        shaders.put("TestShader", 2);
        models.put("TestModel", new Model());
    }
}
