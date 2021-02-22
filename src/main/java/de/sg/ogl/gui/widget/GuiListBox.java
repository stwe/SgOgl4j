/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui.widget;

import de.sg.ogl.Color;
import de.sg.ogl.Log;
import de.sg.ogl.gui.Anchor;
import de.sg.ogl.gui.GuiQuad;
import de.sg.ogl.gui.event.GuiButtonEvent;
import de.sg.ogl.gui.event.GuiButtonListener;
import de.sg.ogl.gui.event.GuiPanelEvent;
import de.sg.ogl.gui.event.GuiPanelListener;
import de.sg.ogl.renderer.TileRenderer;
import de.sg.ogl.resource.Texture;
import de.sg.ogl.text.TextRenderer;
import org.joml.Vector2f;

import java.util.ArrayList;

public class GuiListBox extends GuiQuad {

    private final TextRenderer textRenderer;
    private final ArrayList<String> values;

    private final GuiButton buttonUp;
    private final GuiButton buttonDown;

    private final int visibleEntries = 14;
    private int start = 0;

    private final ArrayList<GuiPanel> guiPanels = new ArrayList<>();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public GuiListBox(
            Vector2f origin,
            float width,
            float height,
            Texture texture,
            Texture up,
            Texture down,
            ArrayList<String> values,
            TextRenderer textRenderer
    ) {
        super(origin, width, height, texture);

        buttonUp = new GuiButton(new Vector2f(-60.0f, 60.0f), up.getWidth(), up.getHeight(), up);
        buttonDown = new GuiButton(new Vector2f(-60.0f, 10.0f), down.getWidth(), down.getHeight(), down);

        this.values = values;
        this.textRenderer = textRenderer;
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    public void init() {
        add(buttonUp, Anchor.BOTTOM_RIGHT);
        add(buttonDown, Anchor.BOTTOM_RIGHT);

        buttonUp.addListener(new GuiButtonListener() {
            @Override
            public void onClick(GuiButtonEvent event) {
                Log.LOGGER.debug("On Click Button Up");
                if (start > 0) {
                    start--;
                }
            }

            @Override
            public void onHover(GuiButtonEvent event) {}

            @Override
            public void onRelease(GuiButtonEvent event) {}
        });

        buttonDown.addListener(new GuiButtonListener() {
            @Override
            public void onClick(GuiButtonEvent event) {
                Log.LOGGER.debug("On Click Button Down");
                if (start < values.size() - visibleEntries) {
                    start++;
                }
            }

            @Override
            public void onHover(GuiButtonEvent event) {}

            @Override
            public void onRelease(GuiButtonEvent event) {}
        });

        var lineHeight = (int) (getHeight() / visibleEntries);
        // todo textrenderer: get FontHeight

        var t = 0.0f;
        for (int i = 0; i < visibleEntries; i++) {
            var line = new GuiPanel(new Vector2f(0.0f, 0.0f + t), getWidth(), lineHeight);
            line.setRenderMe(false);
            line.setName(Integer.toString(i));

            guiPanels.add(line);
            add(line, Anchor.TOP_LEFT);

            line.addListener(new GuiPanelListener() {
                @Override
                public void onClick(GuiPanelEvent event) {
                    var source = event.getSource(); // todo
                    Log.LOGGER.debug("On Click Line {}", line.getName());
                }

                @Override
                public void onHover(GuiPanelEvent event) {
                }

                @Override
                public void onRelease(GuiPanelEvent event) {
                }
            });

            t += lineHeight;
        }
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    @Override
    public void inputGuiObject() {}

    @Override
    public void renderGuiObject(TileRenderer tileRenderer) {
        super.renderGuiObject(tileRenderer);

        renderTable(start);
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private void renderTable(int start) {
        var line = 0;
        for (int i = start; i < visibleEntries + start; i++) {
            textRenderer.render(
                    values.get(i),
                    guiPanels.get(line).getOrigin().x + 8, guiPanels.get(line).getOrigin().y + 4,
                    Color.YELLOW
            );
            line++;
        }
    }
}
