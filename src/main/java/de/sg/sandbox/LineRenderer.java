/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.sandbox;

import de.sg.ogl.OpenGL;
import de.sg.ogl.SgOglEngine;
import de.sg.ogl.buffer.BufferLayout;
import de.sg.ogl.buffer.VertexAttribute;
import de.sg.ogl.camera.FirstPersonCamera;
import de.sg.ogl.math.Transform;
import de.sg.ogl.resource.Mesh;
import de.sg.ogl.resource.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static de.sg.ogl.buffer.VertexAttribute.VertexAttributeType.*;
import static org.lwjgl.opengl.GL11.GL_LINES;

public class LineRenderer {

    private final SgOglEngine engine;
    private final FirstPersonCamera camera;

    private Mesh mesh;
    private Shader shader;

    private float[] vertices;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public LineRenderer(SgOglEngine engine, FirstPersonCamera camera) throws Exception {
        this.engine = engine;
        this.camera = camera;

        createLines();
        createMesh();
        loadShader();
    }

    //-------------------------------------------------
    // Render
    //-------------------------------------------------

    public void render() {
        OpenGL.enableAlphaBlending();
        shader.bind();
        mesh.initDraw();

        var mvp = new Matrix4f(engine.getWindow().getProjectionMatrix());

        Transform t = new Transform();
        t.setScale(new Vector3f(1.0f));
        t.setPosition(new Vector3f(0.0f, 0.0f, 0.0f));
        t.setRotation(new Vector3f(0.0f));

        shader.setUniform("mvpMatrix",
                mvp.mul(camera.getViewMatrix()).mul(t.getModelMatrix())
        );

        mesh.drawPrimitives(GL_LINES);

        mesh.endDraw();
        Shader.unbind();
        OpenGL.disableBlending();
    }

    //-------------------------------------------------
    // Clean up
    //-------------------------------------------------

    public void cleanUp() {
        mesh.cleanUp();
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private void loadShader() throws Exception {
        shader = engine.getResourceManager().loadShaderResource("line");
    }

    private void createLines() {
        vertices = new float[] {
                // pos             // color
                0.0f, 0.0f, 0.0f,  1.0f, 0.0f, 0.0f, // start
                1.0f, 0.0f, 0.0f,  1.0f, 0.0f, 0.0f, // end

                0.0f, 0.0f, 0.0f,  0.0f, 1.0f, 0.0f, // start
                0.0f, 1.0f, 0.0f,  0.0f, 1.0f, 0.0f, // end

                0.0f, 0.0f, 0.0f,  0.0f, 0.0f, 1.0f, // start
                0.0f, 0.0f, 1.0f,  0.0f, 0.0f, 1.0f, // end
        };
    }

    private void createMesh() {
        BufferLayout bufferLayout = new BufferLayout(
                new ArrayList<>(){{
                    add(new VertexAttribute(POSITION, "aPosition"));
                    add(new VertexAttribute(COLOR, "aColor"));
                }}
        );

        mesh = new Mesh();
        mesh.getVao().addVerticesVbo(vertices, 4, bufferLayout);
    }
}
