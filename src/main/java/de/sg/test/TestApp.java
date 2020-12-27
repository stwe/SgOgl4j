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
import de.sg.ogl.gui.Gui;
import de.sg.ogl.gui.GuiPanel;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class TestApp extends BaseApplication {

    private Gui gui;

    public TestApp() throws IOException, IllegalAccessException {
    }

    @Override
    public void init() throws Exception {

        // panel texture
        var panelTexture = getEngine().getResourceManager().loadTextureResource("/texture/gui.png");

        // gui
        gui = new Gui(getEngine());

        // panel
        gui.addPanel(GuiPanel.AnchorPosition.BOTTOM_LEFT, new Vector2f(10.0f, -10.0f), 100.0f, 200.0f, panelTexture.getId());

        // init gui renderer
        gui.initRender();
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
        gui.render();
    }

    @Override
    public void renderImGui() {

    }

    @Override
    public void cleanUp() {

    }
}
