/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.resource;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

import static de.sg.ogl.Log.LOGGER;

public class ResourceManager {

    private final HashMap<String, Resource> resources;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public ResourceManager() {
        LOGGER.debug("Creates ResourceManager object.");

        resources = new HashMap<>();
    }

    //-------------------------------------------------
    // Texture resources
    //-------------------------------------------------

    public Texture loadTextureResource(String path, boolean loadVerticalFlipped) throws URISyntaxException {
        var result = getResourceByPath(Objects.requireNonNull(path, "path must not be null"), Texture.class);

        if (result.isPresent()) {
            LOGGER.debug("The texture {} was already loaded.", path);
            return result.get();
        }

        LOGGER.debug("The texture {} must be loaded.", path);

        return addTextureResource(path, loadVerticalFlipped);
    }

    public Texture loadTextureResource(String path) throws URISyntaxException {
        return loadTextureResource(path, false);
    }

    private Texture addTextureResource(String path, boolean loadVerticalFlipped) throws URISyntaxException {
        var texture = new Texture(path, loadVerticalFlipped);
        texture.load();

        resources.put(path, texture);

        return texture;
    }

    //-------------------------------------------------
    // Shader resources
    //-------------------------------------------------

    public Shader loadShaderResource(String path) throws Exception {
        var result = getResourceByPath(Objects.requireNonNull(path, "path must not be null"), Shader.class);

        if (result.isPresent()) {
            LOGGER.debug("The shader {} was already loaded.", path);
            return result.get();
        }

        LOGGER.debug("The shader {} must be loaded.", path);

        return addShaderResource(path);
    }

    private Shader addShaderResource(String path) throws Exception {
        var shader = new Shader(path);
        shader.load();

        resources.put(path, shader);

        return shader;
    }

    //-------------------------------------------------
    // Util
    //-------------------------------------------------

    private <T> Optional<T> getResourceByPath(String path, Class<T> type) {
        return Optional.ofNullable(type.cast(resources.get(path)));
    }

    static public String readFileIntoString(String path) throws FileNotFoundException {
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
