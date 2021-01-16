/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.test;

import de.sg.ogl.SgOglApplication;
import de.sg.ogl.Log;
import de.sg.ogl.input.KeyInput;
import de.sg.ogl.input.MouseInput;
import de.sg.ogl.resource.Shader;
import de.sg.ogl.resource.Texture;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class TestApp extends SgOglApplication {

    public TestApp() throws IOException, IllegalAccessException {
    }

    @Override
    public void init() throws Exception {
        var texture = getEngine().getResourceManager().loadResource(Texture.class, "/texture/gui.png", true);
        var shader = getEngine().getResourceManager().loadResource(
                Shader.class,
                "sprite"
        );
        int i = 0;
    }

    @Override
    public void input() {
        if (MouseInput.isMouseInWindow()) {
            if (MouseInput.isMouseButtonDoubleClicked(0)) {
                Log.LOGGER.debug("double");
            }
        }

        if (KeyInput.isKeyPressed(GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(getEngine().getWindow().getWindowHandle(), true);
        }
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {

    }

    @Override
    public void renderImGui() {

    }

    @Override
    public void cleanUp() {

    }
}
