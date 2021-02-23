/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.gui;

import de.sg.ogl.Color;
import de.sg.ogl.SgOglRuntimeException;
import de.sg.ogl.input.MouseInput;
import de.sg.ogl.physics.Aabb;
import de.sg.ogl.renderer.TileRenderer;
import de.sg.ogl.resource.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Objects;

public abstract class GuiQuad implements GuiObject {

    /**
     * The top left position of the GuiQuad.
     */
    private Vector2f origin;

    /**
     * The width of the GuiQuad.
     */
    private final float width;

    /**
     * The height of the GuiQuad.
     */
    private final float height;

    /**
     * The bottom left position of the GuiQuad.
     */
    private Vector2f bottomLeft = new Vector2f();

    /**
     * The bottom right position of the GuiQuad.
     */
    private Vector2f bottomRight = new Vector2f();

    /**
     * The top right position of the GuiQuad.
     */
    private Vector2f topRight = new Vector2f();

    /**
     * The center position of the GuiQuad.
     */
    private Vector2f center = new Vector2f();

    /**
     * The parent GuiQuad.
     */
    private GuiQuad parent;

    /**
     * The children of the GuiQuad.
     */
    private final ArrayList<GuiQuad> children = new ArrayList<>();

    /**
     * The anchor of the GuiQuad.
     */
    private Anchor anchor = Anchor.TOP_LEFT;

    /**
     * The Aabb of the GuiQuad in world space.
     */
    private Aabb aabb;

    /**
     * The color of the GuiQuad.
     */
    private Color color;

    /**
     * The texture of the GuiQuad.
     */
    private Texture texture;

    /**
     * The render flag.
     */
    private boolean renderMe = true;

    /**
     * Is true if the mouse was last over the GuiQuad.
     */
    private boolean mouseOverFlag = false;

    /**
     * A name for this GuiQuad.
     */
    private String name;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    /**
     * Creates a new GuiQuad.
     *
     * @param origin The top left position.
     * @param width The width of the GuiQuad.
     * @param height The height of the GuiQuad.
     * @param color The color of the GuiQuad.
     * @param texture The texture of the GuiQuad.
     */
    public GuiQuad(Vector2f origin, float width, float height, Color color, Texture texture) {
        this.origin = Objects.requireNonNull(origin, "origin must not be null");
        this.width = width;
        this.height = height;

        this.origin = calcRenderPosition();
        initCorners(this.origin);

        this.aabb = new Aabb(this.origin, new Vector2f(this.width, this.height));

        this.color = color;
        this.texture = texture;
    }

    /**
     * Creates a new GuiQuad.
     *
     * @param origin The top left position.
     * @param width The width of the GuiQuad.
     * @param height The height of the GuiQuad.
     * @param color The color of the GuiQuad.
     */
    public GuiQuad(Vector2f origin, float width, float height, Color color) {
        this(origin, width, height, color, null);
    }

    /**
     * Creates a new GuiQuad.
     *
     * @param origin The top left position.
     * @param width The width of the GuiQuad.
     * @param height The height of the GuiQuad.
     * @param texture The texture of the GuiQuad.
     */
    public GuiQuad(Vector2f origin, float width, float height, Texture texture) {
        this(origin, width, height, Color.WHITE, texture);
    }

    /**
     * Creates a new GuiQuad.
     *
     * @param origin The top left position.
     * @param width The width of the GuiQuad.
     * @param height The height of the GuiQuad.
     */
    public GuiQuad(Vector2f origin, float width, float height) {
        this(origin, width, height, Color.WHITE, null);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public Vector2f getOrigin() {
        return origin;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Vector2f getSize() {
        return new Vector2f(getWidth(), getHeight());
    }

    public Vector2f getBottomLeft() {
        return bottomLeft;
    }

    public Vector2f getBottomRight() {
        return bottomRight;
    }

    public Vector2f getTopRight() {
        return topRight;
    }

    public Vector2f getCenter() {
        return center;
    }

    public GuiQuad getParent() {
        return parent;
    }

    public ArrayList<GuiQuad> getChildren() {
        return children;
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public Aabb getAabb() {
        return aabb;
    }

    public Color getColor() {
        return color;
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean isMouseOver() {
        return Aabb.pointVsAabb(MouseInput.getXY(), aabb);
    }

    public boolean isRenderMe() {
        return renderMe;
    }

    public boolean isMouseOverFlag() {
        return mouseOverFlag;
    }

    public String getName() {
        return name;
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

    public void setRenderMe(boolean renderMe) {
        this.renderMe = renderMe;
    }

    public void setMouseOverFlag(boolean mouseOverFlag) {
        this.mouseOverFlag = mouseOverFlag;
    }

    public void setName(String name) {
        this.name = name;
    }

    //-------------------------------------------------
    // Implement GuiObject
    //-------------------------------------------------

    @Override
    public abstract void init();

    @Override
    public void input() {
        inputTree(this);
    }

    @Override
    public void update(float dt) {}

    @Override
    public void render(TileRenderer tileRenderer) {
        renderTree(this, tileRenderer);
    }

    @Override
    public abstract void inputGuiObject();

    @Override
    public void updateGuiObject(float dt) {}

    @Override
    public void renderGuiObject(TileRenderer tileRenderer) {
        if (renderMe) {
            if (getTexture() != null) {
                tileRenderer.render(getTexture(), getOrigin(), getSize());
            } else {
                tileRenderer.render(getColor(), getOrigin(), getSize());
            }
        }
    }

    //-------------------------------------------------
    // Traverse tree
    //-------------------------------------------------

    private void inputTree(GuiQuad guiQuad) {
        guiQuad.inputGuiObject();

        for (var child : guiQuad.children) {
            inputTree(child);
        }
    }

    private void renderTree(GuiQuad guiQuad, TileRenderer tileRenderer) {
        guiQuad.renderGuiObject(tileRenderer);

        for (var child : guiQuad.children) {
            renderTree(child, tileRenderer);
        }
    }

    //-------------------------------------------------
    // Add children
    //-------------------------------------------------

    /**
     * Adds another GuiQuad at a specific anchor.
     *
     * @param child The object to be added.
     * @param anchor The anchor of the child.
     */
    public void add(GuiQuad child, Anchor anchor) {
        Objects.requireNonNull(child, "child must not be null");

        child.parent = this;

        child.anchor = anchor;
        child.origin = child.calcRenderPosition();
        child.initCorners(child.origin);

        child.aabb = new Aabb(child.origin, new Vector2f(child.width, child.height));

        children.add(child);

        child.init();
    }

    /**
     * Adds another GuiQuad at a specific anchor.
     *
     * @param child The object to be added.
     */
    public void add(GuiQuad child) {
        add(child, Anchor.TOP_LEFT);
    }

    //-------------------------------------------------
    // Screen position
    //-------------------------------------------------

    /**
     * Calculates the new position of the top left corner, depending on the anchor and the parent object.
     *
     * @return Vector2f The new top left position.
     */
    private Vector2f calcRenderPosition() {
        // The root quad returns the origin position.
        if (parent == null) {
            return origin;
        }

        Vector2f screenPosition;
        switch(anchor) {
            case TOP_LEFT:
                screenPosition = new Vector2f(parent.origin).add(this.origin);
                break;
            case BOTTOM_LEFT:
                screenPosition = new Vector2f(parent.bottomLeft.x, parent.bottomLeft.y - this.height);
                screenPosition.x += this.origin.x;
                screenPosition.y -= this.origin.y;
                break;
            case BOTTOM_RIGHT:
                screenPosition = new Vector2f(parent.bottomRight.x - this.width, parent.bottomRight.y - this.height);
                screenPosition.x -= this.origin.x;
                screenPosition.y -= this.origin.y;
                break;
            case TOP_RIGHT:
                screenPosition = new Vector2f(parent.topRight.x - this.width, parent.topRight.y);
                screenPosition.x -= this.origin.x;
                screenPosition.y += this.origin.y;
                break;
            case CENTER:
                screenPosition = new Vector2f(parent.center.x - this.width * 0.5f, parent.center.y - this.height * 0.5f);
                screenPosition.x += this.origin.x;
                screenPosition.y += this.origin.y;
                break;
            default:
                throw new SgOglRuntimeException("Invalid anchor position type: " + anchor);
        }

        return screenPosition;
    }

    //-------------------------------------------------
    // Init positions
    //-------------------------------------------------

    /**
     * Calculates the positions of the corners of the GuiQuad, depending the top left position
     *
     * @param topLeft The top left position.
     */
    private void initCorners(Vector2f topLeft) {
        bottomLeft = new Vector2f(topLeft.x, topLeft.y + height);
        topRight = new Vector2f(topLeft.x + width, topLeft.y);
        bottomRight = new Vector2f(topLeft.x + width, topLeft.y + height);
        center = new Vector2f((topLeft.x + topRight.x) * 0.5f, (topLeft.y + bottomLeft.y) * 0.5f);
    }
}
