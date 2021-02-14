/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.Color;
import de.sg.ogl.gui.event.GuiButtonAdapter;
import de.sg.ogl.gui.event.GuiButtonEvent;
import de.sg.ogl.renderer.TileRenderer;
import de.sg.ogl.resource.Texture;
import de.sg.ogl.text.TextRenderer;
import org.joml.Vector2f;

import java.util.ArrayList;

public class GuiListBox extends GuiObject {

    private final TextRenderer textRenderer;
    private final ArrayList<String> lines;
    private final GuiButton buttonUp;
    private final GuiButton buttonDown;

    private int entries = 14;
    private int start = 0;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    GuiListBox(
            GuiQuad parentQuad,
            Anchor anchor,
            Vector2f offset,
            float width, float height,
            Texture texture,
            Texture up,
            Texture down,
            TextRenderer textRenderer,
            ArrayList<String> lines
    )
    {
        super(parentQuad, anchor, offset, width, height, texture);

        this.textRenderer = textRenderer;
        this.lines = lines;

        this.buttonUp = new GuiButton(
                getGuiObjectQuad(),
                Anchor.BOTTOM_RIGHT,
                new Vector2f(-60.f, 60.0f),
                up.getWidth(), up.getHeight(),
                up
        );

        this.buttonUp.addListener(new GuiButtonAdapter() {
            @Override
            public void onClick(GuiButtonEvent event) {
                if (start > 0) {
                    start--;
                }
            }

            @Override
            public void onHover(GuiButtonEvent event) {}

            @Override
            public void onRelease(GuiButtonEvent event) {}
        });

        this.buttonDown = new GuiButton(
                getGuiObjectQuad(),
                Anchor.BOTTOM_RIGHT,
                new Vector2f(-60.f, 10.0f),
                down.getWidth(), down.getHeight(),
                down
        );

        this.buttonDown.addListener(new GuiButtonAdapter() {
            @Override
            public void onClick(GuiButtonEvent event) {
                if (start < lines.size() - entries) {
                    start++;
                }
            }

            @Override
            public void onHover(GuiButtonEvent event) {}

            @Override
            public void onRelease(GuiButtonEvent event) {}
        });

    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public GuiButton getButtonUp() {
        return buttonUp;
    }

    public GuiButton getButtonDown() {
        return buttonDown;
    }

    //-------------------------------------------------
    // Implement GuiObject
    //-------------------------------------------------

    @Override
    public void input() {

    }

    @Override
    public void update() {

    }

    private void renderTable(int start) {
        var c = 0;
        var x = getRenderOrigin().x + 24;
        var y = getRenderOrigin().y + 10;
        for (int i = start; i < entries + start; i++) {
            textRenderer.render(
                    lines.get(i),
                    x, y + (c * 26),
                    Color.YELLOW
            );
            c++;
        }
    }

    @Override
    public void render(TileRenderer tileRenderer) {
        tileRenderer.render(
                getTexture(),
                getRenderOrigin(),
                getSize()
        );

        buttonUp.render(tileRenderer);
        buttonDown.render(tileRenderer);

        renderTable(start);
    }
}
