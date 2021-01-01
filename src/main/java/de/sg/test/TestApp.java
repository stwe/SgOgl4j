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
import de.sg.ogl.gui.GuiObject;
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

        var panelTexture = getEngine().getResourceManager().loadTextureResource("/texture/gui.png");
        var buttonTexture = getEngine().getResourceManager().loadTextureResource("/texture/sgbrick/paddle.png");

        // gui
        gui = new Gui(getEngine());

        // panels
        var panel0 = gui.addPanel(GuiObject.Anchor.BOTTOM_LEFT, new Vector2f(10.0f, -10.0f), 100.0f, 200.0f, "Panel0", panelTexture.getId());
        var panel1 = gui.addPanel(GuiObject.Anchor.TOP_RIGHT, new Vector2f(-10.0f, 10.0f), 100.0f, 200.0f, "Panel1", panelTexture.getId());

        // buttons
        var button0 = panel0.addButton(
                GuiObject.Anchor.TOP_RIGHT,
                new Vector2f(0.0f, 0.0f),
                64.0f, 20.0f,
                "test0",
                buttonTexture.getId()
        );

        var button1 = panel0.addButton(
                GuiObject.Anchor.BOTTOM_LEFT,
                new Vector2f(0.0f, 0.0f),
                64.0f, 20.0f,
                "test1",
                buttonTexture.getId()
        );

        // init gui renderer
        gui.initRender();
    }

    @Override
    public void input() {
        if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(getEngine().getWindow().getWindowHandle(), true);
        }

        gui.input();
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
