package de.sg.ogl.resource;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.opengl.GL20.*;

import java.io.InputStream;
import java.util.EnumSet;
import java.util.Scanner;

public class Shader implements Resource {

    public enum Options
    {
        VERTEX_SHADER,
        TESSELLATION_CONTROL_SHADER,
        TESSELLATION_EVALUATION_SHADER,
        GEOMETRY_SHADER,
        FRAGMENT_SHADER;
    }

    private String path;
    private EnumSet<Options> options;

    private int id;

    private int vertexShaderId;
    private int tessellationControlShaderId;
    private int tessellationEvaluationShaderId;
    private int geometryShaderId;
    private int fragmentShaderId;

    //-------------------------------------------------
    // Ctors. / Dtor.
    //-------------------------------------------------

    public Shader(String path, EnumSet<Options> options) {
        LOGGER.debug("Creates Shader object.");

        this.path = path;
        this.options = options;

        this.id = 0;

        this.vertexShaderId = 0;
        this.tessellationControlShaderId = 0;
        this.tessellationEvaluationShaderId = 0;
        this.geometryShaderId = 0;
        this.fragmentShaderId = 0;
    }

    public Shader(String path) {
        this(path, EnumSet.of(Options.VERTEX_SHADER, Options.FRAGMENT_SHADER));
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public int getId() {
        return id;
    }

    //-------------------------------------------------
    // Implement Resource
    //-------------------------------------------------

    @Override
    public void load() throws Exception {
        id = createProgram();

        if (options.contains(Options.VERTEX_SHADER)) {
            addVertexShader(loadResource(path + "/shader/vertex.vs"));
        }

        if (options.contains(Options.FRAGMENT_SHADER)) {
            addFragmentShader(loadResource(path + "/shader/fragment.fs"));
        }

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

    static private int createProgram() {
        var programId = glCreateProgram();
        assert programId > 0;
        LOGGER.debug("A new Shader program was created. The Id is {}.", programId);

        return programId;
    }

    private void addVertexShader(String shaderCode) {
        vertexShaderId = addShader(shaderCode, GL_VERTEX_SHADER);
        LOGGER.debug("A new Vertex Shader was added. The Id is {}.", vertexShaderId);
    }

    private void addFragmentShader(String shaderCode) {
        fragmentShaderId = addShader(shaderCode, GL_FRAGMENT_SHADER);
        LOGGER.debug("A new Fragment Shader was added. The Id is {}.", fragmentShaderId);
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
