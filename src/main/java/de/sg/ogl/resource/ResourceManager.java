/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.resource;

import de.sg.ogl.SgOglRuntimeException;
import de.sg.ogl.buffer.Vertex2D;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Optional;

import static de.sg.ogl.Log.LOGGER;

public class ResourceManager {

    private final HashMap<String, Resource> resources = new HashMap<>();
    private final HashMap<Geometry.GeometryId, Geometry> geometry = new HashMap<>();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public ResourceManager() {
        LOGGER.debug("Creates ResourceManager object.");
        initGeometry();
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

    public Geometry loadGeometry(Geometry.GeometryId id) {
        if (geometry.containsKey(id)) {
            return geometry.get(id);
        }

        throw new SgOglRuntimeException("Unexpected error. Geometry doesn't exist.");
    }

    private void initGeometry() {
        LOGGER.debug("Add 2D Quad vertices.");
        geometry.put(
                Geometry.GeometryId.QUAD_2D,
                new Geometry(
                        Geometry.GeometryId.QUAD_2D,
                        Vertex2D.toFloatArray(Geometry.getQuad2DVertices()),
                        Geometry.getQuad2DVertices().length,
                        Vertex2D.BUFFER_LAYOUT_2D
                )
        );
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
