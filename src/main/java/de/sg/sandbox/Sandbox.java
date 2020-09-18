/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.sandbox;

import de.sg.ogl.BaseApplication;
import de.sg.ogl.Input;
import de.sg.ogl.buffer.BufferLayout;
import de.sg.ogl.buffer.VertexAttribute;
import de.sg.ogl.resource.Mesh;
import org.lwjgl.glfw.GLFW;
import java.io.IOException;
import java.util.ArrayList;

import static de.sg.ogl.buffer.VertexAttribute.VertexAttributeType.POSITION_2D;
import static de.sg.ogl.buffer.VertexAttribute.VertexAttributeType.UV;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Sandbox extends BaseApplication {

    ColFile colFile;
    BshFile bshFile;

    private Mesh mesh;
    private Renderer renderer;

    public Sandbox() throws IOException, IllegalAccessException {
    }

    @Override
    public void init() throws Exception {
        createMesh();
        renderer = new Renderer(getEngine(), mesh);
        renderer.init();

        colFile = new ColFile("/bsh/STADTFLD.COL");
        colFile.loadData();

        bshFile = new BshFile("/bsh/STADTFLD.BSH", colFile.getPalette());
        bshFile.loadData();
    }

    @Override
    public void input() {
        if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(getEngine().getWindow().getWindowHandle(), true);
        }
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render() {
        renderer.render(bshFile.getImages().get(200));
    }

    @Override
    public void cleanUp() {

    }

    private void createMesh() {
        float[] vertices = new float[] {
                // pos      // tex
                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f,

                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 0.0f
        };

        BufferLayout bufferLayout = new BufferLayout(
                new ArrayList<>(){{
                    add(new VertexAttribute(POSITION_2D, "aPosition"));
                    add(new VertexAttribute(UV, "aUv"));
                }}
        );

        mesh = new Mesh();
        mesh.getVao().addVerticesVbo(vertices, 6, bufferLayout);
    }
}
