/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.test;

import de.sg.ogl.BaseApplication;
import de.sg.ogl.Input;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class TestApp extends BaseApplication {

    private SpriteRenderer renderer;

    public TestApp() throws IOException, IllegalAccessException {
    }

    @Override
    public void init() throws Exception {
        renderer = new SpriteRenderer(getEngine());
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
        renderer.prepareRendering();
        renderer.render(new Vector2f(100.0f, 100.0f), new Vector2f(64.0f, 64.0f), 1);
        renderer.render(new Vector2f(200.0f, 200.0f), new Vector2f(64.0f, 64.0f), 0);
        renderer.finishRendering();
    }

    @Override
    public void cleanUp() {

    }
}
