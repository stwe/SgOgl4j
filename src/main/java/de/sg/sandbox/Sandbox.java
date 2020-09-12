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
import de.sg.ogl.camera.FirstPersonCamera;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Sandbox extends BaseApplication {

    LineRenderer renderer;
    FirstPersonCamera camera;

    public Sandbox() throws IOException, IllegalAccessException {
    }

    @Override
    public void init() throws Exception {
        camera = new FirstPersonCamera();
        camera.setPosition(new Vector3f(0.0f, 0.0f, 1.0f));
        camera.setYaw(-90.0f);

        renderer = new LineRenderer(getEngine(), camera);
    }

    @Override
    public void input() {
        if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(getEngine().getWindow().getWindowHandle(), true);
        }

        camera.input();
    }

    @Override
    public void update(float dt) {
        camera.update(dt);
    }

    @Override
    public void render() {
        renderer.render();
    }

    @Override
    public void cleanUp() {

    }
}
