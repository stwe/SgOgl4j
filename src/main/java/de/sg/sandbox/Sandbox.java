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
import org.lwjgl.glfw.GLFW;
import java.io.IOException;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Sandbox extends BaseApplication {

    ColFile colFile;
    BshFile bshFile;

    public Sandbox() throws IOException, IllegalAccessException {
    }

    @Override
    public void init() throws Exception {
        colFile = new ColFile("/bsh/STADTFLD.COL");
        colFile.loadData();

        bshFile = new BshFile("/bsh/STADTFLD.BSH");
        bshFile.loadData();
        bshFile.decodeImages();
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
    }

    @Override
    public void cleanUp() {

    }
}
