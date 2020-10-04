/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.islands;

import de.sg.ogl.BaseApplication;
import de.sg.ogl.Input;
import de.sg.ogl.buffer.BufferLayout;
import de.sg.ogl.buffer.VertexAttribute;
import de.sg.ogl.camera.OrthographicCamera;
import de.sg.ogl.resource.Mesh;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.ArrayList;

import static de.sg.ogl.buffer.VertexAttribute.VertexAttributeType.POSITION_2D;
import static de.sg.ogl.buffer.VertexAttribute.VertexAttributeType.UV;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Islands extends BaseApplication {

    private static final int XRASTER = 32;
    private static final int YRASTER = 16;
    private static final int ELEVATION = 20;

    private DevelopmentFile devFile;
    private GraphicFile graphicFile;
    private PaletteFile paletteFile;
    private BshFile bshFile;
    private ScpFile scpFile;
    //private CodFile codFile;

    private OrthographicCamera camera;
    private Mesh mesh;
    private Renderer renderer;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Islands() throws IOException, IllegalAccessException {
    }

    //-------------------------------------------------
    // Override
    //-------------------------------------------------

    @Override
    public void init() throws Exception {
        camera = new OrthographicCamera(new Vector2f(580.0f, 220.0f));
        camera.setCameraVelocity(300.0f);

        createMesh();
        renderer = new Renderer(getEngine(), mesh);
        renderer.init();

        devFile = new DevelopmentFile("/island/bebauung.txt");
        graphicFile = new GraphicFile("/island/grafiken.txt");
        paletteFile = new PaletteFile("/island/STADTFLD.COL");
        bshFile = new BshFile("/island/STADTFLD.BSH", paletteFile.getPalette());
        scpFile = new ScpFile("/island/lit.scp", devFile);

        //codFile = new CodFile("/bsh/haeuser.cod");
    }

    @Override
    public void input() {
        if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(getEngine().getWindow().getWindowHandle(), true);
        }

        // todo dt -> input
        camera.update(0.016f);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        // Galgen: devId 841 -> graphicIdx 5372
        //var info = devFile.getMeta(841);
        //var galgenGraphicIdx = graphicFile.getGraphicIndexByDevId(841);

        var island5 = scpFile.getIsland5();

        for (int y = 0; y < island5.height; y++) {
            for (int x = 0; x < island5.width; x++) {

                // tile kann null sein
                var tile = scpFile.getTileFromLayer(x, y);
                var field = scpFile.getGraphicForTile(x, y, tile, devFile, graphicFile);

                if (field.index != -1) {
                    var bshTexture = bshFile.getBshTextures().get(field.index);
                    renderer.render(
                            bshTexture,
                            (x - y + island5.height) * XRASTER,
                            (x + y) * YRASTER + 2 * YRASTER - field.baseHeight * ELEVATION,
                            camera
                    );
                }
            }
        }
    }

    @Override
    public void cleanUp() {

    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

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
