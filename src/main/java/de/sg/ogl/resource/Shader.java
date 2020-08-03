package de.sg.ogl.resource;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.opengl.GL20.*;

import java.io.InputStream;
import java.util.Scanner;

public class Shader implements Resource {

    private final String path;

    private int id;
    private int vertexShaderId;
    private int fragmentShaderId;

    //-------------------------------------------------
    // Ctors. / Dtor.
    //-------------------------------------------------

    public Shader(String path) {
        LOGGER.debug("Creates Shader object.");

        this.path = path;
        this.id = 0;
        this.vertexShaderId = 0;
        this.fragmentShaderId = 0;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public String getPath() {
        return path;
    }

    public int getId() {
        return id;
    }

    //-------------------------------------------------
    // Implement Resource
    //-------------------------------------------------

    @Override
    public void load() throws Exception {
        id = glCreateProgram();
        assert id > 0;

        addVertexShader(loadResource("/shader/vertex.vs"));
        addFragmentShader(loadResource("/shader/fragment.fs"));
        linkAndValidateProgram();
    }

    @Override
    public void cleanUp() {

    }

    //-------------------------------------------------
    // Bind / Unbind
    //-------------------------------------------------

    public void bind() {
        glUseProgram(id);
    }

    static public void unbind() {
        glUseProgram(0);
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private void addVertexShader(String shaderCode) {
        vertexShaderId = addShader(shaderCode, GL_VERTEX_SHADER);
    }

    private void addFragmentShader(String shaderCode) {
        fragmentShaderId = addShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    static private int generateShader(int shaderType) {
        return glCreateShader(shaderType);
    }

    static private void compileShader(int shaderId, String shaderCode) {
        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);
    }

    static private void checkCompileStatus(int shaderId) {
        // todo
    }

    private int addShader(String shaderCode, int shaderType) {
        var shaderId = generateShader(shaderType);
        assert shaderId > 0;

        compileShader(shaderId, shaderCode);
        checkCompileStatus(shaderId);

        glAttachShader(id, shaderId);

        return shaderId;
    }

    private void linkAndValidateProgram() {
        glLinkProgram(id);
        glValidateProgram(id);
    }

    static public String loadResource(String path) throws Exception {
        String result;

        try (InputStream in = Texture.class.getResourceAsStream(path);
             Scanner scanner = new Scanner(in, java.nio.charset.StandardCharsets.UTF_8.name())) {
             result = scanner.useDelimiter("\\A").next();
        }

        return result;
    }
}
