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
import de.sg.ogl.gui.event.GuiEvent;
import de.sg.ogl.gui.event.GuiListener;
import de.sg.ogl.input.MouseInput;
import de.sg.ogl.physics.Aabb;
import de.sg.ogl.renderer.TileRenderer;
import de.sg.ogl.resource.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;

public class GuiQuad {

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
     * Multiple listeners can be added.
     */
    private final ArrayList<GuiListener> listeners = new ArrayList<>();

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

    Vector2f getOrigin() {
        return origin;
    }

    float getWidth() {
        return width;
    }

    float getHeight() {
        return height;
    }

    public Vector2f getSize() {
        return new Vector2f(getWidth(), getHeight());
    }

    Vector2f getBottomLeft() {
        return bottomLeft;
    }

    Vector2f getBottomRight() {
        return bottomRight;
    }

    Vector2f getTopRight() {
        return topRight;
    }

    Vector2f getCenter() {
        return center;
    }

    GuiQuad getParent() {
        return parent;
    }

    ArrayList<GuiQuad> getChildren() {
        return children;
    }

    Anchor getAnchor() {
        return anchor;
    }

    Aabb getAabb() {
        return aabb;
    }

    Color getColor() {
        return color;
    }

    Texture getTexture() {
        return texture;
    }

    boolean isMouseOver() {
        return Aabb.pointVsAabb(MouseInput.getXY(), aabb);
    }

    boolean isRenderMe() {
        return renderMe;
    }

    ArrayList<GuiListener> getListeners() {
        return listeners;
    }

    public String getName() {
        return name;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    void setColor(Color color) {
        this.color = color;
    }

    void setTexture(Texture texture) {
        this.texture = texture;
    }

    void setRenderMe(boolean renderMe) {
        this.renderMe = renderMe;
    }

    public void setName(String name) {
        this.name = name;
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    void input() {
        inputTree(this);
    }

    void update() {}

    void render(TileRenderer tileRenderer) {
        renderTree(this, tileRenderer);
    }

    //-------------------------------------------------
    // Logic Helper
    //-------------------------------------------------

    private void inputTree(GuiQuad guiQuad) {
        inputGuiQuad(guiQuad);

        for (var child : guiQuad.children) {
            inputTree(child);
        }
    }

    private void renderTree(GuiQuad guiQuad, TileRenderer tileRenderer) {
        guiQuad.renderGuiQuad(tileRenderer);

        for (var child : guiQuad.children) {
            renderTree(child, tileRenderer);
        }
    }

    private static void inputGuiQuad(GuiQuad guiQuad) {
        if (guiQuad.listeners.isEmpty()) {
            return;
        }

        if (guiQuad.isMouseOver()) {
            if (MouseInput.isMouseButtonPressed(GLFW_MOUSE_BUTTON_LEFT) ||
                    MouseInput.isMouseButtonPressed(GLFW_MOUSE_BUTTON_RIGHT) ||
                    MouseInput.isMouseButtonPressed(GLFW_MOUSE_BUTTON_MIDDLE)
            ) {
                guiQuad.runOnClickListeners();
            }

            guiQuad.runOnHoverListeners();
        } else if (guiQuad.mouseOverFlag) {
            guiQuad.runOnReleaseListeners();
        }
    }

    public void renderGuiQuad(TileRenderer tileRenderer) {
        if (renderMe) {
            if (getTexture() != null) {
                tileRenderer.render(getTexture(), getOrigin(), getSize());
            } else {
                tileRenderer.render(getColor(), getOrigin(), getSize());
            }
        }
    }

    //-------------------------------------------------
    // Add
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
    // Listener
    //-------------------------------------------------

    public void addListener(GuiListener listener) {
        Objects.requireNonNull(listener, "listener must not be null");

        if (listeners.contains(listener)) {
            return;
        }

        listeners.add(listener);
    }

    public void removeListener(GuiListener listener) {
        Objects.requireNonNull(listener, "listener must not be null");
        listeners.remove(listener);
    }

    private void runOnClickListeners() {
        if (getListeners().isEmpty()) {
            return;
        }

        mouseOverFlag = true;
        var event = new GuiEvent(this);
        for (var listener : getListeners()) {
            listener.onClick(event);
        }
    }

    private void runOnHoverListeners() {
        if (getListeners().isEmpty()) {
            return;
        }

        mouseOverFlag = true;
        var event = new GuiEvent(this);
        for (var listener : getListeners()) {
            listener.onHover(event);
        }
    }

    private void runOnReleaseListeners() {
        if (getListeners().isEmpty()) {
            return;
        }

        mouseOverFlag = false;
        var event = new GuiEvent(this);
        for (var listener : getListeners()) {
            listener.onRelease(event);
        }
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
    // Init
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
