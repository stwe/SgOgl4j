package de.sg.ogl.resource;

import de.sg.ogl.SgOglException;

import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

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
    // Texture resources
    //-------------------------------------------------

    public Texture loadTextureResource(String path, boolean loadVerticalFlipped) {
        return getResourceByPath(Objects.requireNonNull(path, "Path cannot be null."), Texture.class)
                .orElseGet(() -> addTextureResource(path, loadVerticalFlipped));
    }

    public Texture loadTextureResource(String path) {
        return loadTextureResource(path, false);
    }

    private Texture addTextureResource(String path, boolean loadVerticalFlipped) {
        LOGGER.debug("The texture {} must be loaded.", path);

        var url = loadResource(path);
        var texture = new Texture(url, loadVerticalFlipped);
        try {
            texture.load();
            resources.put(path, texture);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return texture;
    }

    //-------------------------------------------------
    // Shader resources
    //-------------------------------------------------

    public Shader loadShaderResource(String path) {
        return getResourceByPath(Objects.requireNonNull(path, "Path cannot be null."), Shader.class)
                .orElseGet(() -> addShaderResource(path));
    }

    private Shader addShaderResource(String path) {
        LOGGER.debug("The shader {} must be loaded.", path);

        var shader = new Shader(path);
        try {
            shader.load();
            resources.put(path, shader);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shader;
    }

    //-------------------------------------------------
    // Util
    //-------------------------------------------------

    private <T> Optional<T> getResourceByPath(String path, Class<T> type) {
        var result = Optional.ofNullable(type.cast(resources.get(path)));

        if (result.isPresent()) {
            LOGGER.debug("The resource {} already exists.", path);
        }

        return result;
    }

    static public String readFileIntoString(String path) throws Exception {
        String result;

        try (var in = ResourceManager.class.getResourceAsStream(path);
             Scanner scanner = new Scanner(in, java.nio.charset.StandardCharsets.UTF_8.name())) {
            result = scanner.useDelimiter("\\A").next();
        }

        return result;
    }

    private String loadResource(String resource) {
        return loadResourceByUrl(getClass().getResource(resource), resource);
    }

    private String loadResourceByUrl(URL url, String resource) {
        if (url != null) {
            return url.getPath().replaceFirst("^/(.:/)", "$1");
        } else {
            throw new SgOglException("Resource " + resource + " not found.");
        }
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
