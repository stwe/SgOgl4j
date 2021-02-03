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
import de.sg.ogl.gui.Gui;
import de.sg.ogl.gui.GuiObject;
import de.sg.ogl.input.KeyInput;
import de.sg.ogl.resource.Texture;
import org.joml.Vector2f;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class TestApp extends SgOglApplication {

    private Gui gui;

    public TestApp() throws IOException, IllegalAccessException {
    }

    @Override
    public void init() throws Exception {
        var panelTexture = getEngine().getResourceManager().loadResource(Texture.class, "/texture/benno/panel.png");
        var buttonSingleTexture = getEngine().getResourceManager().loadResource(Texture.class, "/texture/benno/single.png");
        var buttonMultiTexture = getEngine().getResourceManager().loadResource(Texture.class, "/texture/benno/multi.png");

        // gui
        gui = new Gui(getEngine());

        // panels
        var panel0 = gui.addPanel(
                GuiObject.Anchor.TOP_LEFT,
                new Vector2f(0.0f, 0.0f),
                getEngine().getWindow().getWidth(), getEngine().getWindow().getHeight(),
                "panel0",
                panelTexture.getId()
        );

        // buttons
        var button0 = panel0.addButton(
                GuiObject.Anchor.TOP_LEFT,
                new Vector2f(113.0f, 362.0f),
                332.0f, 53.0f,
                "Singleplayer",
                buttonSingleTexture.getId()
        );

        var button1 = panel0.addButton(
                GuiObject.Anchor.TOP_LEFT,
                new Vector2f(113.0f, 415.0f),
                332.0f, 54.0f,
                "Multiplayer",
                buttonMultiTexture.getId()
        );

        // init gui renderer
        gui.initRender();
    }

    @Override
    public void input() {
        if (KeyInput.isKeyPressed(GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(getEngine().getWindow().getWindowHandle(), true);
        }

        gui.input();
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        OpenGL.setClearColor(Color.CORNFLOWER_BLUE);
        OpenGL.clear();

        gui.render(false);
    }

    @Override
    public void renderImGui() {

    }

    @Override
    public void cleanUp() {
        gui.cleanUp();
    }
}
