package de.sg.ogl.resource;

import de.sg.ogl.SgOglException;

import static de.sg.ogl.Log.LOGGER;
import static de.sg.ogl.resource.ResourceManager.readFileIntoString;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_CONTROL_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_EVALUATION_SHADER;

import java.util.EnumSet;

public class Shader implements Resource {

    public enum Options
    {
        VERTEX_SHADER,
        TESSELLATION_CONTROL_SHADER,
        TESSELLATION_EVALUATION_SHADER,
        GEOMETRY_SHADER,
        FRAGMENT_SHADER
    }

    private final String path;
    private final EnumSet<Options> options;

    private int id = 0;

    private int vertexShaderId = 0;
    private int tessellationControlShaderId = 0;
    private int tessellationEvaluationShaderId = 0;
    private int geometryShaderId = 0;
    private int fragmentShaderId = 0;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Shader(String path, EnumSet<Options> options) {
        LOGGER.debug("Creates Shader object.");

        this.path = path;
        this.options = options;
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
        // creates an empty program
        id = createProgram();

        var shaderPath = "/shader/" + path;

        // add VERTEX_SHADER
        if (options.contains(Options.VERTEX_SHADER)) {
            addVertexShader(readFileIntoString(shaderPath + "/Vertex.vert"));
        }

        // add TESSELLATION_CONTROL_SHADER
        if (options.contains(Options.TESSELLATION_CONTROL_SHADER)) {
            addTessellationControlShader(readFileIntoString(shaderPath + "/TessControl.tesc"));
        }

        // add TESSELLATION_EVALUATION_SHADER
        if (options.contains(Options.TESSELLATION_EVALUATION_SHADER)) {
            addTessellationEvaluationShader(readFileIntoString(shaderPath + "/TessEval.tese"));
        }

        // add GEOMETRY_SHADER
        if (options.contains(Options.GEOMETRY_SHADER)) {
            addGeometryShader(readFileIntoString(shaderPath + "/Geometry.geom"));
        }

        // add FRAGMENT_SHADER
        if (options.contains(Options.FRAGMENT_SHADER)) {
            addFragmentShader(readFileIntoString(shaderPath + "/Fragment.frag"));
        }

        linkAndValidateProgram();
    }

    @Override
    public void cleanUp() {
        LOGGER.debug("Start clean up for Shader program {}.", id);

        unbind();

        if (vertexShaderId > 0) {
            glDeleteShader(vertexShaderId);
            LOGGER.debug("Vertex shader {} was deleted.", vertexShaderId);
        }

        if (tessellationControlShaderId > 0) {
            glDeleteShader(tessellationControlShaderId);
            LOGGER.debug("Tessellation control shader {} was deleted.", tessellationControlShaderId);
        }

        if (tessellationEvaluationShaderId > 0) {
            glDeleteShader(tessellationEvaluationShaderId);
            LOGGER.debug("Tessellation evaluation shader {} was deleted.", tessellationEvaluationShaderId);
        }

        if (geometryShaderId > 0) {
            glDeleteShader(geometryShaderId);
            LOGGER.debug("Geometry shader {} was deleted.", geometryShaderId);
        }

        if (fragmentShaderId > 0) {
            glDeleteShader(fragmentShaderId);
            LOGGER.debug("Fragment shader {} was deleted.", fragmentShaderId);
        }

        if (id > 0) {
            glDeleteProgram(id);
            LOGGER.debug("Shader program {} was deleted.", id);
        }
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
        if (programId == 0) {
            throw new SgOglException("Shader program creation has failed.");
        }

        LOGGER.debug("A new Shader program was created. The Id is {}.", programId);

        return programId;
    }

    static private int createShaderObject(int shaderType) {
        var shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new SgOglException("Shader object creation has failed. The type is " + shaderType + ".");
        }

        return shaderId;
    }

    private void addVertexShader(String shaderCode) {
        vertexShaderId = addShader(shaderCode, GL_VERTEX_SHADER);
        LOGGER.debug("A new Vertex Shader was added to program {}. The Id is {}.", id, vertexShaderId);
    }

    private void addTessellationControlShader(String shaderCode) {
        tessellationControlShaderId = addShader(shaderCode, GL_TESS_CONTROL_SHADER);
        LOGGER.debug("A new Tessellation Control Shader was added to program {}. The Id is {}.", id, tessellationControlShaderId);
    }

    private void addTessellationEvaluationShader(String shaderCode) {
        tessellationEvaluationShaderId = addShader(shaderCode, GL_TESS_EVALUATION_SHADER);
        LOGGER.debug("A new Tessellation Evaluation Shader was added to program {}. The Id is {}.", id, tessellationEvaluationShaderId);
    }

    private void addGeometryShader(String shaderCode) {
        geometryShaderId = addShader(shaderCode, GL_GEOMETRY_SHADER);
        LOGGER.debug("A new Geometry Shader was added to program {}. The Id is {}.", id, geometryShaderId);
    }

    private void addFragmentShader(String shaderCode) {
        fragmentShaderId = addShader(shaderCode, GL_FRAGMENT_SHADER);
        LOGGER.debug("A new Fragment Shader was added to program {}. The Id is {}.", id, fragmentShaderId);
    }

    private int addShader(String shaderCode, int shaderType) {
        var shaderId = createShaderObject(shaderType);

        compileShader(shaderId, shaderCode);
        checkCompileStatus(shaderId);
        glAttachShader(id, shaderId);

        // todo find structs && uniforms

        return shaderId;
    }

    static private void compileShader(int shaderId, String shaderCode) {
        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);
    }

    static private void checkCompileStatus(int shaderId) {
        var status = glGetShaderi(shaderId, GL_COMPILE_STATUS);
        if (status == GL_FALSE) {
            throw new SgOglException("Error while compiling Shader code. Log: " + glGetShaderInfoLog(shaderId, 1024));
        }
    }

    private void linkAndValidateProgram() {
        // link
        glLinkProgram(id);
        var status = glGetProgrami(id, GL_LINK_STATUS);
        if (status == GL_FALSE) {
            throw new SgOglException("Error while linking Shader program. Log: " + glGetProgramInfoLog(id, 1024));
        }

        // always detach shaders after a successful link
        if (vertexShaderId != 0)
        {
            glDetachShader(id, vertexShaderId);
        }

        if (tessellationControlShaderId != 0)
        {
            glDetachShader(id, tessellationControlShaderId);
        }

        if (tessellationEvaluationShaderId != 0)
        {
            glDetachShader(id, tessellationEvaluationShaderId);
        }

        if (geometryShaderId != 0)
        {
            glDetachShader(id, geometryShaderId);
        }

        if (fragmentShaderId != 0)
        {
            glDetachShader(id, fragmentShaderId);
        }

        // validate
        glValidateProgram(id);
        status = glGetProgrami(id, GL_VALIDATE_STATUS);
        if (status == GL_FALSE ) {
            LOGGER.warn("Warning validating Shader code. Log: " + glGetProgramInfoLog(id, 1024));
        }
    }
}
