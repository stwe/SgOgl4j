/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.Color;
import de.sg.ogl.SgOglEngine;
import de.sg.ogl.renderer.TileRenderer;
import de.sg.ogl.resource.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;

public class Gui {

    private final GuiQuad guiQuad;
    private final TileRenderer tileRenderer;

    private final ArrayList<GuiPanel> guiPanels = new ArrayList<>();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Gui(SgOglEngine engine) throws Exception {
        this.guiQuad = new GuiQuad(
                new Vector2f(0.0f),
                engine.getWindow().getWidth(),
                engine.getWindow().getHeight()
        );

        this.tileRenderer = new TileRenderer(engine);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public ArrayList<GuiPanel> getGuiPanels() {
        return guiPanels;
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void input() {
        for (var guiPanel : guiPanels) {
            guiPanel.input();

            for (var guiButton : guiPanel.getGuiObjects()) {
                guiButton.input();
            }
        }
    }

    public void update() {
        for (var guiPanel : guiPanels) {
            guiPanel.update();

            for (var guiButton : guiPanel.getGuiObjects()) {
                guiButton.update();
            }
        }
    }

    public void render() {
        for (var guiPanel : guiPanels) {
            guiPanel.render(tileRenderer);

            for (var guiButton : guiPanel.getGuiObjects()) {
                guiButton.render(tileRenderer);
            }
        }
    }

    //-------------------------------------------------
    // Panel
    //-------------------------------------------------

    public GuiPanel addPanel(Anchor anchor, Vector2f offset, float width, float height, Color color, Texture texture) {
        var panel = new GuiPanel(guiQuad, anchor, offset, width, height, color, texture);
        guiPanels.add(panel);

        return panel;
    }

    //-------------------------------------------------
    // Clean up
    //-------------------------------------------------

    public void cleanUp() {
        tileRenderer.cleanUp();
    }
}
