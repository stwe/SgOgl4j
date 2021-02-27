/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui.widget;

import de.sg.ogl.Color;
import de.sg.ogl.gui.Anchor;
import de.sg.ogl.gui.GuiQuad;
import de.sg.ogl.gui.event.*;
import de.sg.ogl.resource.Texture;
import de.sg.ogl.text.TextRenderer;
import org.joml.Vector2f;

import java.nio.file.Path;
import java.util.ArrayList;

public class GuiListBox<T> extends GuiQuad {

    private static final int MAX_VISIBLE_ENTRIES = 14;

    private final TextRenderer textRenderer;
    private final ArrayList<String> labels;
    private final ArrayList<Path> values;

    private final GuiButton buttonUp;
    private final GuiButton buttonDown;

    private int start = 0;

    private final ArrayList<GuiLabel<T>> guiLabels = new ArrayList<>();
    private GuiLabel<T> activeLabel;

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
            ArrayList<String> labels,
            ArrayList<Path> values,
            TextRenderer textRenderer
    ) {
        super(origin, width, height, texture);

        buttonUp = new GuiButton(new Vector2f(-60.0f, 60.0f), up.getWidth(), up.getHeight(), up);
        buttonDown = new GuiButton(new Vector2f(-60.0f, 10.0f), down.getWidth(), down.getHeight(), down);

        this.labels = labels;
        this.values = values;

        this.textRenderer = textRenderer;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public ArrayList<GuiLabel<T>> getGuiLabels() {
        return guiLabels;
    }

    public GuiLabel<T> getActiveLabel() {
        return activeLabel;
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
                //Log.LOGGER.debug("On Click Button Up");
                if (labels.size() > MAX_VISIBLE_ENTRIES) {
                    if (start > 0) {
                        start--;
                    }
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
                //Log.LOGGER.debug("On Click Button Down");
                if (labels.size() > MAX_VISIBLE_ENTRIES) {
                    if (start < labels.size() - MAX_VISIBLE_ENTRIES) {
                        start++;
                    }
                }
            }

            @Override
            public void onHover(GuiEvent<GuiButton>event) {}

            @Override
            public void onRelease(GuiEvent<GuiButton> event) {}
        });

        var lineHeight = (int) (getHeight() / MAX_VISIBLE_ENTRIES);
        var fontHeight = textRenderer.getAwtFont().getSize();

        // todo
        var xOffset = fontHeight / 2;
        var yOffset = (lineHeight - fontHeight) / 2;

        var t = 0.0f;
        var entries = labels.size();
        if (labels.size() > MAX_VISIBLE_ENTRIES) {
            entries = MAX_VISIBLE_ENTRIES;
        }

        for (int i = 0; i < entries; i++) {
            // for each visible line create a new label
            var label = new GuiLabel<T>(
                    new Vector2f(0.0f + xOffset, 0.0f + t + yOffset),
                    getWidth(), lineHeight,
                    textRenderer,
                    null,
                    null,
                    Color.YELLOW
            );

            // set a debug name
            label.setName(Integer.toString(i));

            // cache the label
            guiLabels.add(label);

            // add label as child
            add(label, Anchor.TOP_LEFT);

            // to set the active Label
            label.addListener(new GuiListener<>() {
                @Override
                public void onClick(GuiEvent<GuiLabel<T>> event) {
                    activeLabel = (GuiLabel<T>)event.getSource();
                }

                @Override
                public void onHover(GuiEvent<GuiLabel<T>> event) {}

                @Override
                public void onRelease(GuiEvent<GuiLabel<T>> event) {}
            });

            t += lineHeight;
        }
    }

    @Override
    public void inputGuiObject() {
        var line = 0;

        var entries = labels.size();
        if (labels.size() > MAX_VISIBLE_ENTRIES) {
            entries = MAX_VISIBLE_ENTRIES;
        }

        for (int i = start; i < entries + start; i++) {
            guiLabels.get(line).setLabel(labels.get(i));
            guiLabels.get(line).setValue((T)values.get(i));
            line++;
        }
    }
}
