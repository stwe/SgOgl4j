/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.resource;

import de.sg.ogl.SgOglRuntimeException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;

import static de.sg.ogl.Log.LOGGER;

public class ResourceManager {

    private final HashMap<String, Resource> resources = new HashMap<>();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public ResourceManager() {
        LOGGER.debug("Creates ResourceManager object.");
    }

    //-------------------------------------------------
    // Load
    //-------------------------------------------------

    public <T extends Resource> T loadResource(Class<T> resourceType, String path, Object... args) throws Exception {
        var resource = Optional.ofNullable(resourceType.cast(resources.get(path)));
        if (resource.isPresent()) {
            LOGGER.debug("The resource {} was already loaded.", path);
            return resource.get();
        }

        LOGGER.debug("The resource {} must be loaded.", path);

        switch (resourceType.getSimpleName()) {
            case "Texture" :
                addTexture(path, args);
                break;
            case "Shader" :
                addShader(path, args);
                break;
            default:
                throw new SgOglRuntimeException("Invalid resource type: " + resourceType.getSimpleName());
        }

        return resourceType.cast(resources.get(path));
    }

    //-------------------------------------------------
    // Add
    //-------------------------------------------------

    private void addTexture(String path, Object... args) throws IOException {
        Texture texture;

        if (args.length > 0) {
            if (!(args[0] instanceof Boolean)) {
                throw new SgOglRuntimeException("Invalid argument given.");
            }
            texture = new Texture(path, (boolean)args[0]);
        } else {
            texture = new Texture(path);
        }

        texture.load();

        resources.put(path, texture);
    }

    @SuppressWarnings("unchecked")
    private void addShader(String path, Object... args) throws IOException {
        Shader shader;

        if (args.length > 0) {
            if (!(args[0] instanceof EnumSet)) {
                throw new SgOglRuntimeException("Invalid argument given.");
            }
            shader = new Shader(path, (EnumSet<Shader.Options>)args[0]);
        } else {
            shader = new Shader(path);
        }

        shader.load();

        resources.put(path, shader);
    }

    //-------------------------------------------------
    // Util
    //-------------------------------------------------

    static String readFileIntoString(String path) throws FileNotFoundException {
        String result;

        var in = ResourceManager.class.getResourceAsStream(path);
        if (in == null) {
            throw new FileNotFoundException("Resource " + path + " not found.");
        }

        try (Scanner scanner = new Scanner(in, java.nio.charset.StandardCharsets.UTF_8.name())) {
            result = scanner.useDelimiter("\\A").next();
        }

        return result;
    }

    //-------------------------------------------------
    // Clean up
    //-------------------------------------------------

    public void cleanUp() {
        LOGGER.debug("Clean up Resources.");

        if (!resources.isEmpty()) {
            LOGGER.debug("Clean up {} resources.", resources.size());

            for (var resource : resources.entrySet()) {
                resource.getValue().cleanUp();
            }
        } else {
            LOGGER.debug("There is nothing to clean up.");
        }
    }
}
