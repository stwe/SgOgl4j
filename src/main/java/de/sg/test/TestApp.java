/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.test;

import de.sg.ogl.Color;
import de.sg.ogl.Log;
import de.sg.ogl.OpenGL;
import de.sg.ogl.SgOglApplication;
import de.sg.ogl.gui.Anchor;
import de.sg.ogl.gui.Gui;
import de.sg.ogl.gui.event.GuiEvent;
import de.sg.ogl.gui.event.GuiListener;
import de.sg.ogl.gui.widget.GuiLabel;
import de.sg.ogl.gui.widget.GuiListBox;
import de.sg.ogl.input.KeyInput;
import de.sg.ogl.resource.Texture;
import de.sg.ogl.text.TextRenderer;
import org.joml.Vector2f;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static java.awt.Font.MONOSPACED;
import static java.awt.Font.PLAIN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class TestApp extends SgOglApplication {

    private Gui gui;
    private final ArrayList<String> filesListLables = new ArrayList<>();
    private final ArrayList<Path> filesListValues = new ArrayList<>();
    private GuiListBox<Path> listBox;

    public TestApp() throws IOException, IllegalAccessException {
    }

    @Override
    public void init() throws Exception {
        var panelTexture = getEngine().getResourceManager().loadResource(Texture.class, "/texture/benno/panel.png");
        var listBoxTexture = getEngine().getResourceManager().loadResource(Texture.class, "/texture/benno/list.png");
        var upTexture = getEngine().getResourceManager().loadResource(Texture.class, "/texture/benno/up.png");
        var downTexture = getEngine().getResourceManager().loadResource(Texture.class, "/texture/benno/down.png");

        gui = new Gui(getEngine());

        for (int i = 0; i < 20; i++) {
            filesListLables.add("Test Label " + i);
            filesListValues.add(null);
        }

        listBox = new GuiListBox<>(
                new Vector2f(0.0f, 0.0f),
                300.0f, 300.0f,
                listBoxTexture,
                upTexture,
                downTexture,
                filesListLables,
                filesListValues,
                new TextRenderer(getEngine(), new java.awt.Font(MONOSPACED, PLAIN, 14))
        );

        gui.getMainPanel().add(listBox, Anchor.CENTER);

        for (var label : listBox.getGuiLabels()) {
            label.addListener(new GuiListener<>() {
                @Override
                public void onClick(GuiEvent<GuiLabel<Path>> event) {
                    var guiLabel = (GuiLabel<Path>) event.getSource();
                    loadFile((Path)guiLabel.getValue());
                }

                @Override
                public void onHover(GuiEvent<GuiLabel<Path>> event) {
                }

                @Override
                public void onRelease(GuiEvent<GuiLabel<Path>> event) {
                }
            });
        }
    }

    private void loadFile(Path path) {
        Log.LOGGER.debug("Load file {}", path);
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
