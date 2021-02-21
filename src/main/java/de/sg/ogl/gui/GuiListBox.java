/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.Color;
import de.sg.ogl.Log;
import de.sg.ogl.gui.event.GuiAdapter;
import de.sg.ogl.gui.event.GuiEvent;
import de.sg.ogl.renderer.TileRenderer;
import de.sg.ogl.resource.Texture;
import de.sg.ogl.text.TextRenderer;
import org.joml.Vector2f;

import java.util.ArrayList;

public class GuiListBox extends GuiQuad {

    private final TextRenderer textRenderer;
    private final ArrayList<String> values;

    private final GuiQuad buttonUp;
    private final GuiQuad buttonDown;

    private final int visibleEntries = 14;
    private int start = 0;

    private final ArrayList<GuiQuad> guiQuads = new ArrayList<>();

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

        buttonUp = new GuiQuad(new Vector2f(-60.0f, 60.0f), up.getWidth(), up.getHeight(), up);
        buttonDown = new GuiQuad(new Vector2f(-60.0f, 10.0f), down.getWidth(), down.getHeight(), down);

        this.values = values;
        this.textRenderer = textRenderer;
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    public void init() {
        add(buttonUp, Anchor.BOTTOM_RIGHT);
        add(buttonDown, Anchor.BOTTOM_RIGHT);

        buttonUp.addListener(new GuiAdapter() {
            @Override
            public void onClick(GuiEvent event) {
                Log.LOGGER.debug("On Click Button Up");
                if (start > 0) {
                    start--;
                }
            }

            @Override
            public void onHover(GuiEvent event) {}

            @Override
            public void onRelease(GuiEvent event) {}
        });

        buttonDown.addListener(new GuiAdapter() {
            @Override
            public void onClick(GuiEvent event) {
                Log.LOGGER.debug("On Click Button Down");
                if (start < values.size() - visibleEntries) {
                    start++;
                }
            }

            @Override
            public void onHover(GuiEvent event) {}

            @Override
            public void onRelease(GuiEvent event) {}
        });

        var lineHeight = (int) (getHeight() / visibleEntries);
        // todo textrenderer: get FontHeight

        var t = 0.0f;
        for (int i = 0; i < visibleEntries; i++) {
            var line = new GuiQuad(new Vector2f(0.0f, 0.0f + t), getWidth(), lineHeight);
            line.setRenderMe(false);
            line.setName(Integer.toString(i));

            guiQuads.add(line);
            add(line, Anchor.TOP_LEFT);

            line.addListener(new GuiAdapter() {
                @Override
                public void onClick(GuiEvent event) {
                    var source = event.getSource(); // todo
                    Log.LOGGER.debug("On Click Line {}", line.getName());
                }

                @Override
                public void onHover(GuiEvent event) {
                }

                @Override
                public void onRelease(GuiEvent event) {
                }
            });

            t += lineHeight;
        }
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    @Override
    public void renderGuiQuad(TileRenderer tileRenderer) {
        super.renderGuiQuad(tileRenderer);

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
                    guiQuads.get(line).getOrigin().x + 8, guiQuads.get(line).getOrigin().y + 4,
                    Color.YELLOW
            );
            line++;
        }
    }
}
