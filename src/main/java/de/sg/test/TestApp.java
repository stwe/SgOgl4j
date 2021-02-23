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
import de.sg.ogl.gui.Anchor;
import de.sg.ogl.gui.Gui;
import de.sg.ogl.gui.widget.GuiListBox;
import de.sg.ogl.input.KeyInput;
import de.sg.ogl.resource.Texture;
import de.sg.ogl.text.TextRenderer;
import org.joml.Vector2f;

import java.io.IOException;
import java.util.ArrayList;

import static java.awt.Font.MONOSPACED;
import static java.awt.Font.PLAIN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class TestApp extends SgOglApplication {

    private Gui gui;
    private final ArrayList<String> filesList = new ArrayList<>();

    public TestApp() throws IOException, IllegalAccessException {
    }

    @Override
    public void init() throws Exception {
        var panelTexture = getEngine().getResourceManager().loadResource(Texture.class, "/texture/benno/panel.png");
        var listBoxTexture = getEngine().getResourceManager().loadResource(Texture.class, "/texture/benno/list.png");
        var upTexture = getEngine().getResourceManager().loadResource(Texture.class, "/texture/benno/up.png");
        var downTexture = getEngine().getResourceManager().loadResource(Texture.class, "/texture/benno/down.png");

        gui = new Gui(getEngine());

        for (int i = 0; i < 35; i++) {
            filesList.add("Test" + i);
        }

        var listbox = new GuiListBox(
                new Vector2f(0.0f, 0.0f),
                300.0f, 300.0f,
                listBoxTexture,
                upTexture,
                downTexture,
                filesList,
                new TextRenderer(getEngine(), new java.awt.Font(MONOSPACED, PLAIN, 14))
        );

        gui.getMainPanel().add(listbox, Anchor.CENTER);
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
        gui.update(dt);
    }

    @Override
    public void render() {
        OpenGL.setClearColor(Color.CORNFLOWER_BLUE);
        OpenGL.clear();

        gui.render();
    }

    @Override
    public void renderImGui() {

    }

    @Override
    public void cleanUp() {
        gui.cleanUp();
    }
}
