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
import de.sg.ogl.gui.event.*;
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

    private final ArrayList<GuiLabel> guiLabels = new ArrayList<>();

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
    // Implement GuiObject
    //-------------------------------------------------

    @Override
    public void init() {
        add(buttonUp, Anchor.BOTTOM_RIGHT);
        add(buttonDown, Anchor.BOTTOM_RIGHT);

        buttonUp.addListener(new GuiListener<>() {
            @Override
            public void onClick(GuiEvent<GuiButton> event) {
                Log.LOGGER.debug("On Click Button Up");
                if (start > 0) {
                    start--;
                }
            }

            @Override
            public void onHover(GuiEvent<GuiButton> event) {}

            @Override
            public void onRelease(GuiEvent<GuiButton> event) {}
        });

        buttonDown.addListener(new GuiListener<>() {
            @Override
            public void onClick(GuiEvent<GuiButton> event) {
                Log.LOGGER.debug("On Click Button Down");
                if (start < values.size() - visibleEntries) {
                    start++;
                }
            }

            @Override
            public void onHover(GuiEvent<GuiButton>event) {}

            @Override
            public void onRelease(GuiEvent<GuiButton> event) {}
        });

        var lineHeight = (int) (getHeight() / visibleEntries);
        // todo textrenderer: get FontHeight

        var t = 0.0f;
        for (int i = 0; i < visibleEntries; i++) {

            // for each visible line create a new label
            var label = new GuiLabel(
                    new Vector2f(0.0f, 0.0f + t),
                    getWidth(), lineHeight,
                    textRenderer,
                    null,
                    Color.YELLOW
            );

            // set a debug name
            label.setName(Integer.toString(i));

            // cache the label
            guiLabels.add(label);

            // add label as child
            add(label, Anchor.TOP_LEFT);

            label.addListener(new GuiListener<>() {
                @Override
                public void onClick(GuiEvent<GuiLabel> event) {
                    Log.LOGGER.debug("On Click Line {}", label.getLabel());
                }

                @Override
                public void onHover(GuiEvent<GuiLabel> event) {
                }

                @Override
                public void onRelease(GuiEvent<GuiLabel> event) {
                }
            });

            t += lineHeight;
        }
    }

    @Override
    public void inputGuiObject() {
        var line = 0;
        for (int i = start; i < visibleEntries + start; i++) {
            guiLabels.get(line).setLabel(values.get(i));
            line++;
        }
    }
}
