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
import de.sg.ogl.event.GuiButtonAdapter;
import de.sg.ogl.event.GuiButtonEvent;
import de.sg.ogl.gui.Anchor;
import de.sg.ogl.gui.Gui;
import de.sg.ogl.gui.GuiButton;
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
        var buttonSingleSelTexture = getEngine().getResourceManager().loadResource(Texture.class, "/texture/benno/single_s.png");
        var buttonMultiSelTexture = getEngine().getResourceManager().loadResource(Texture.class, "/texture/benno/multi_s.png");

        // gui
        gui = new Gui(getEngine());

        // panel
        var panel0 = gui.addPanel(
                Anchor.TOP_LEFT,
                new Vector2f(0.0f, 0.0f),
                getEngine().getWindow().getWidth(), getEngine().getWindow().getHeight(),
                Color.BLUE,
                panelTexture
        );

        // singleplayer button
        var spButton = panel0.addButton(
                Anchor.TOP_LEFT,
                new Vector2f(113.0f, 362.0f),
                buttonSingleTexture.getWidth(), buttonSingleTexture.getHeight(),
                Color.WHITE,
                buttonSingleTexture
        );

        // singleplayer button listener
        spButton.addListener(new GuiButtonAdapter() {
            @Override
            public void onClick(GuiButtonEvent event) {
                Log.LOGGER.debug("On Click SinglePlayerButton");
            }

            @Override
            public void onHover(GuiButtonEvent event) {
                var source = (GuiButton)event.getSource();
                source.setTexture(buttonSingleSelTexture);
                Log.LOGGER.debug("Hover SinglePlayerButton");
            }

            @Override
            public void onRelease(GuiButtonEvent event) {
                var source = (GuiButton)event.getSource();
                source.setTexture(buttonSingleTexture);
                Log.LOGGER.debug("Release SinglePlayerButton");
            }
        });

        // multiplayer button
        var mpButton = panel0.addButton(
                Anchor.TOP_LEFT,
                new Vector2f(113.0f, 415.0f),
                buttonMultiTexture.getWidth(), buttonMultiTexture.getHeight(),
                Color.WHITE,
                buttonMultiTexture
        );

        // multiplayer button listener
        mpButton.addListener(new GuiButtonAdapter() {
            @Override
            public void onClick(GuiButtonEvent event) {
                Log.LOGGER.debug("On Click MultiPlayerButton");
            }

            @Override
            public void onHover(GuiButtonEvent event) {
                var source = (GuiButton)event.getSource();
                source.setTexture(buttonMultiSelTexture);
                Log.LOGGER.debug("Hover MultiPlayerButton");
            }

            @Override
            public void onRelease(GuiButtonEvent event) {
                var source = (GuiButton)event.getSource();
                source.setTexture(buttonMultiTexture);
                Log.LOGGER.debug("Release MultiPlayerButton");
            }
        });
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
        gui.update();
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
