/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.Log;
import de.sg.ogl.SgOglEngine;
import de.sg.ogl.SgOglRuntimeException;
import de.sg.ogl.event.GuiPanelAdapter;
import de.sg.ogl.event.GuiPanelEvent;
import de.sg.ogl.input.MouseInput;
import de.sg.ogl.physics.Aabb;
import de.sg.ogl.renderer.TileRenderer;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Gui {

    private final SgOglEngine engine;

    private final SpriteBatch spriteBatch;
    private final ArrayList<GuiPanel> guiPanels = new ArrayList<>();

    private final TileRenderer tileRenderer; // todo cleanup

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Gui(SgOglEngine engine) throws Exception {
        this.engine = Objects.requireNonNull(engine, "engine must not be null");
        this.spriteBatch = new SpriteBatch(engine);
        this.tileRenderer = new TileRenderer(engine);
    }

    //-------------------------------------------------
    // Add Panel
    //-------------------------------------------------

    public GuiPanel addPanel(GuiObject.Anchor anchor, Vector2f offset, float width, float height, String title) {
        var panel = new GuiPanel(
                getPanelScreenPosition(anchor, width, height),
                offset,
                width, height,
                title
        );

        panel.addListener(new GuiPanelAdapter() {
            @Override
            public void onClick(GuiPanelEvent event) {
                var source = (GuiPanel)event.getSource();
                Log.LOGGER.debug("On Click: " + source.getTitle());
            }

            @Override
            public void onHover(GuiPanelEvent event) {
                var source = (GuiPanel)event.getSource();
                //Log.LOGGER.debug("On Hover: " + source.getTitle());
            }

            @Override
            public void onRelease(GuiPanelEvent event) {
                var source = (GuiPanel)event.getSource();
                //Log.LOGGER.debug("On Release: " + source.getTitle());
            }
        });

        guiPanels.add(panel);

        return panel;
    }

    public GuiPanel addPanel(GuiObject.Anchor anchor, Vector2f offset, float width, float height, String title, int textureId) {
        var panel = addPanel(anchor, offset, width, height, title);
        panel.textureId = textureId;

        return panel;
    }

    public GuiPanel addPanel(GuiObject.Anchor anchor, Vector2f offset, float width, float height, String title, Vector3f color) {
        var panel = addPanel(anchor, offset, width, height, title);
        panel.color = color;

        return panel;
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    public void initRender() {
        for (var panel : guiPanels) {
            panel.addToRenderer(spriteBatch);
        }

        spriteBatch.end();
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void input() {
        if (MouseInput.isMouseInWindow()) {
            for (var panel : guiPanels) {
                if (isMouseIn(panel)) {
                    if (MouseInput.isMouseButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
                        panel.onClick();
                    } else {
                        panel.onHover();
                    }

                    for (var child : panel.getGuiObjects()) {
                        if (isMouseIn(child)) {
                            if (child instanceof GuiButton) {
                                if (MouseInput.isMouseButtonPressed(GLFW_MOUSE_BUTTON_LEFT)) {
                                    ((GuiButton) child).onClick();
                                } else {
                                    ((GuiButton) child).onHover();
                                }
                            }
                        }
                    }
                } else {
                    panel.onRelease();
                }
            }
        }
    }

    public void render(boolean useBatch) {
        if (useBatch) {
            spriteBatch.render();
        } else {
            for (var guiPanel : guiPanels) {
                tileRenderer.render(guiPanel.textureId, guiPanel.position, new Vector2f(guiPanel.width, guiPanel.height));
                for (var guiObjects : guiPanel.getGuiObjects()) {
                    tileRenderer.render(guiObjects.textureId, guiObjects.position, new Vector2f(guiObjects.width, guiObjects.height));
                }
            }
        }
    }

    public void render() {
        render(true);
    }

    //-------------------------------------------------
    // Clean up
    //-------------------------------------------------

    public void cleanUp() {
        tileRenderer.cleanUp();
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private static boolean isMouseIn(GuiObject guiObject) {
        return Aabb.pointVsAabb(MouseInput.getXY(), guiObject.aabb);
    }

    private Vector2f getPanelScreenPosition(GuiObject.Anchor anchor, float width, float height) {
        Vector2f position;
        switch(anchor) {
            case TOP_LEFT:
                position = new Vector2f(engine.getWindow().getTopLeft());
                break;
            case BOTTOM_LEFT:
                position = new Vector2f(engine.getWindow().getBottomLeft().x, engine.getWindow().getBottomLeft().y - height);
                break;
            case BOTTOM_RIGHT:
                position = new Vector2f(engine.getWindow().getBottomRight().x - width, engine.getWindow().getBottomRight().y - height);
                break;
            case TOP_RIGHT:
                position = new Vector2f(engine.getWindow().getTopRight().x - width, engine.getWindow().getTopRight().y);
                break;
            case CENTER:
                position = new Vector2f(engine.getWindow().getCenter().x - width * 0.5f, engine.getWindow().getCenter().y - height * 0.5f);
                break;
            default:
                throw new SgOglRuntimeException("Invalid anchor position type: " + anchor);
        }

        return position;
    }
}
