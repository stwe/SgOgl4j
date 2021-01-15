/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.test;

import de.sg.ogl.SgOglApplication;
import de.sg.ogl.Color;
import de.sg.ogl.Log;
import de.sg.ogl.event.GuiButtonAdapter;
import de.sg.ogl.event.GuiButtonEvent;
import de.sg.ogl.gui.Gui;
import de.sg.ogl.gui.GuiButton;
import de.sg.ogl.gui.GuiObject;
import de.sg.ogl.input.KeyInput;
import de.sg.ogl.input.MouseInput;
import de.sg.ogl.text.TextRenderer;
import org.joml.Vector2f;

import java.io.IOException;

import static java.awt.Font.MONOSPACED;
import static java.awt.Font.PLAIN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class TestApp extends SgOglApplication {

    private Gui gui;
    private TextRenderer textRenderer;

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
                "button0",
                buttonTexture.getId()
        );

        button0.addListener(new GuiButtonAdapter() {
            @Override
            public void onClick(GuiButtonEvent event) {
                var source = (GuiButton)event.getSource();
                Log.LOGGER.debug("On Click: " + source.getLabel());
            }

            @Override
            public void onHover(GuiButtonEvent event) {
            }
        });

        var button1 = panel0.addButton(
                GuiObject.Anchor.BOTTOM_LEFT,
                new Vector2f(0.0f, 0.0f),
                64.0f, 20.0f,
                "button1",
                buttonTexture.getId()
        );

        button1.addListener(new GuiButtonAdapter() {
            @Override
            public void onClick(GuiButtonEvent event) {
            }

            @Override
            public void onHover(GuiButtonEvent event) {
                var source = (GuiButton)event.getSource();
                Log.LOGGER.debug("On Hover: " + source.getLabel());
            }
        });

        // init gui renderer
        gui.initRender();

        // create && init TextRenderer
        textRenderer = new TextRenderer(getEngine(), new java.awt.Font(MONOSPACED, PLAIN, 10));
    }

    @Override
    public void input() {
        gui.input();
    }

    @Override
    public void update(float dt) {
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
    public void render() {
        gui.render();

        var lorem = "Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua.\n" +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquid ex ea commodi consequat.\n" +
                "Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.\n" +
                "Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

        textRenderer.render(
                lorem,
                400.0f - textRenderer.getTextWidth(lorem) / 2.0f, 300.0f - textRenderer.getTextHeight(lorem) / 2.0f,
                Color.GREEN
        );
    }

    @Override
    public void renderImGui() {

    }

    @Override
    public void cleanUp() {

    }
}
