/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.Color;
import de.sg.ogl.input.MouseInput;
import de.sg.ogl.physics.Aabb;
import de.sg.ogl.renderer.TileRenderer;
import de.sg.ogl.resource.Texture;
import org.joml.Vector2f;

public abstract class GuiObject {

    private GuiQuad guiObjectQuad;

    private final Anchor anchor;
    private final Vector2f renderOrigin;

    private Color color;
    private Texture texture;

    private final Aabb aabb;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    GuiObject(GuiQuad parentQuad, Anchor anchor, Vector2f offset, float width, float height, Color color, Texture texture) {
        this.guiObjectQuad = new GuiQuad(
                new Vector2f(parentQuad.getOrigin()).add(offset),
                width, height
        );

        this.anchor = anchor;
        this.renderOrigin = this.guiObjectQuad.getRenderPosition(anchor);

        this.color = color;
        this.texture = texture;

        this.aabb = new Aabb(getRenderOrigin(), getSize());
    }

    GuiObject(GuiQuad parentQuad, Anchor anchor, Vector2f offset, float width, float height, Color color) {
        this(parentQuad, anchor, offset, width, height, color, null);
    }

    GuiObject(GuiQuad parentQuad, Anchor anchor, Vector2f offset, float width, float height, Texture texture) {
        this(parentQuad, anchor, offset, width, height, Color.WHITE, texture);
    }

    GuiObject(GuiQuad parentQuad, Anchor anchor, Vector2f offset, float width, float height) {
        this(parentQuad, anchor, offset, width, height, Color.WHITE, null);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public GuiQuad getGuiObjectQuad() {
        return guiObjectQuad;
    }

    public float getWidth() {
        return guiObjectQuad.getWidth();
    }

    public float getHeight() {
        return guiObjectQuad.getHeight();
    }

    public Vector2f getSize() {
        return new Vector2f(getWidth(), getHeight());
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public Vector2f getRenderOrigin() {
        return renderOrigin;
    }

    public Color getColor() {
        return color;
    }

    public Texture getTexture() {
        return texture;
    }

    public Aabb getAabb() {
        return aabb;
    }

    public boolean isMouseOver() {
        return Aabb.pointVsAabb(MouseInput.getXY(), aabb);
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setColor(Color color) {
        this.color = color;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public abstract void input();
    public abstract void update();
    public abstract void render(TileRenderer tileRenderer);
}
