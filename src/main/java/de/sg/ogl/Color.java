/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Color {

    public static final Color WHITE = new Color(1.0f, 1.0f, 1.0f);
    public static final Color BLACK = new Color(0.0f, 0.0f, 0.0f);
    public static final Color RED = new Color(1.0f, 0.0f, 0.0f);
    public static final Color GREEN = new Color(0.0f, 1.0f, 0.0f);
    public static final Color BLUE = new Color(0.0f, 0.0f, 1.0f);
    public static final Color YELLOW = new Color(1.0f, 1.0f, 0.0f);
    public static final Color MAGENTA = new Color(1.0f, 0.0f, 1.0f);
    public static final Color CYAN = new Color(0.0f, 1.0f, 1.0f);
    public static final Color CORNFLOWER_BLUE = new Color(0.392f, 0.584f, 0.929f);

    private float red;
    private float green;
    private float blue;
    private float alpha;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    /**
     * Default constructor. The default color is black.
     */
    public Color() {
        this(0.0f, 0.0f, 0.0f);
    }

    /**
     * Creates a RGB-Color with an alpha value of 1.0.
     *
     * @param red   The red component. Range [0.0f .. 1.0f].
     * @param green The green component. Range [0.0f .. 1.0f].
     * @param blue  The blue component. Range [0.0f .. 1.0f].
     */
    public Color(float red, float green, float blue) {
        this(red, green, blue, 1.0f);
    }

    /**
     * Creates a RGBA-Color.
     *
     * @param red   The red component. Range [0.0f .. 1.0f].
     * @param green The green component. Range [0.0f .. 1.0f].
     * @param blue  The blue component. Range [0.0f .. 1.0f].
     * @param alpha The transparency. Range [0.0f .. 1.0f].
     */
    public Color(float red, float green, float blue, float alpha) {
        setRed(red);
        setGreen(green);
        setBlue(blue);
        setAlpha(alpha);
    }

    /**
     * Creates a RGB-Color with an alpha value of 1.
     *
     * @param red   The red component. Range [0 .. 255].
     * @param green The green component. Range [0 .. 255].
     * @param blue  The blue component. Range [0 .. 255].
     */
    public Color(int red, int green, int blue) {
        this(red, green, blue, 255);
    }

    /**
     * Creates a RGBA-Color.
     *
     * @param red   The red component. Range [0 .. 255].
     * @param green The green component. Range [0 .. 255].
     * @param blue  The blue component. Range [0 .. 255].
     * @param alpha The transparency. Range [0 .. 255].
     */
    public Color(int red, int green, int blue, int alpha) {
        setRed(red);
        setGreen(green);
        setBlue(blue);
        setAlpha(alpha);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public float getAlpha() {
        return alpha;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setRed(float red) {
        this.red = clamp(red, 0.0f, 1.0f);
    }

    public void setGreen(float green) {
        this.green = clamp(green, 0.0f, 1.0f);
    }

    public void setBlue(float blue) {
        this.blue = clamp(blue, 0.0f, 1.0f);
    }

    public void setAlpha(float alpha) {
        this.alpha = clamp(alpha, 0.0f, 1.0f);
    }

    public void setRed(int red) {
        setRed(clamp(red, 0, 255) / 255.0f);
    }

    public void setGreen(int green) {
        setGreen(clamp(green, 0, 255) / 255.0f);
    }

    public void setBlue(int blue) {
        setBlue(clamp(blue, 0, 255) / 255.0f);
    }

    public void setAlpha(int alpha) {
        setAlpha(clamp(alpha, 0, 255) / 255.0f);
    }

    //-------------------------------------------------
    // Type casting
    //-------------------------------------------------

    public Vector3f toVector3f() {
        return new Vector3f(red, green, blue);
    }

    public Vector4f toVector4f() {
        return new Vector4f(red, green, blue, alpha);
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    private static float clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
