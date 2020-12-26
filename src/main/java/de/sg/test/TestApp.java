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
import de.sg.ogl.gui.GuiButton;
import de.sg.ogl.gui.GuiPanel;
import de.sg.ogl.gui.SpriteBatch;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class TestApp extends BaseApplication {

    private SpriteBatch spriteBatch;

    public TestApp() throws IOException, IllegalAccessException {
    }

    @Override
    public void init() throws Exception {
        spriteBatch = new SpriteBatch(getEngine());

        var alm = getEngine().getResourceManager().loadTextureResource("/texture/sgbrick/alm.jpg");
        var water = getEngine().getResourceManager().loadTextureResource("/texture/tiles/water.png");

        // panel
        var panel = new GuiPanel();
        panel.setTextureId(alm.getId());

        // button0
        GuiButton button0 = new GuiButton(
                new Vector4f(10.0f, 10.0f, 40.0f, 20.0f),
                new Vector2f(32.0f, -50.0f),
                "New Label",
                panel
        );
        button0.setColor(new Vector3f(0.0f, 1.0f, 1.0f));
        button0.setTextureId(water.getId());

        // button1
        GuiButton button1 = new GuiButton(
                new Vector4f(10.0f, 10.0f, 40.0f, 20.0f),
                new Vector2f(32.0f, -20.0f),
                "New Label",
                panel
        );
        button1.setColor(new Vector3f(1.0f, 1.0f, 0.0f));
        button1.setTextureId(water.getId());

        panel.addGuiObject(button0);
        panel.addGuiObject(button1);

        panel.addToRenderer(spriteBatch);

        spriteBatch.end();
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
        spriteBatch.render();
    }

    @Override
    public void renderImGui() {

    }

    @Override
    public void cleanUp() {

    }
}
