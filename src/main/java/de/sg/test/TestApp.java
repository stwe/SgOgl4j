/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.test;

import de.sg.ogl.Color;
import de.sg.ogl.OpenGL;
import de.sg.ogl.SgOglApplication;
import de.sg.ogl.Log;
import de.sg.ogl.input.KeyInput;
import de.sg.ogl.input.MouseInput;
import de.sg.ogl.input.MousePickingTexture;
import de.sg.ogl.renderer.BatchRenderer;
import de.sg.ogl.renderer.Sprite;
import org.joml.Vector2f;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class TestApp extends SgOglApplication {

    BatchRenderer batchRenderer;
    MousePickingTexture mousePickingTexture;

    public TestApp() throws IOException, IllegalAccessException {
    }

    @Override
    public void init() throws Exception {
        batchRenderer = new BatchRenderer(getEngine(), 10);

        var sprite0 = new Sprite();
        sprite0.position = new Vector2f(100.0f, 100.0f);
        sprite0.color = Color.RED;

        var sprite1 = new Sprite();
        sprite1.position = new Vector2f(400.0f, 100.0f);
        sprite1.color = Color.YELLOW;

        batchRenderer.addQuad(sprite0);
        batchRenderer.addQuad(sprite1);

        mousePickingTexture = new MousePickingTexture(getEngine(), 800, 600);
    }

    @Override
    public void input() {
        /*
        if (MouseInput.isMouseInWindow()) {
            if (MouseInput.isMouseButtonDown(0)) {
                Log.LOGGER.debug("pixel: {}", mousePickingTexture.readPixel(
                        (int)MouseInput.getX(), (int)MouseInput.getY()
                ));
            }
        }
        */

        if (KeyInput.isKeyPressed(GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(getEngine().getWindow().getWindowHandle(), true);
        }
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        mousePickingTexture.startFrame();
        batchRenderer.render();
        var res = mousePickingTexture.readPixel(
                (int)MouseInput.getX(), 600 - (int)MouseInput.getY()
        );
        if (res >= 0)
            Log.LOGGER.debug("pixel: {}", res);

        mousePickingTexture.endFrame();

        OpenGL.setClearColor(Color.CORNFLOWER_BLUE);
        OpenGL.clear();
        batchRenderer.render();
    }

    @Override
    public void renderImGui() {

    }

    @Override
    public void cleanUp() {
        batchRenderer.cleanUp();
        mousePickingTexture.cleanUp();
    }
}
