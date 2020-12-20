/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl;

public class Pixel {
    public int r;
    public int g;
    public int b;
    public int a = 255;

    public Pixel(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Pixel(int r, int g, int b, int a) {
        this(r, g, b);
        this.a = a;
    }

    public static final Pixel BLACK = new Pixel(0, 0, 0);
    public static final Pixel WHITE = new Pixel(255, 255, 255);
    public static final Pixel RED = new Pixel(255, 0, 0);
    public static final Pixel GREEN = new Pixel(0, 255, 0);
    public static final Pixel BLUE = new Pixel(0, 0, 255);
    public static final Pixel YELLOW = new Pixel(255, 255, 0);
    public static final Pixel CYAN = new Pixel(0, 255, 255);
    public static final Pixel MAGENTA = new Pixel(255, 0, 255);

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Pixel)) {
            return false;
        }

        Pixel p = (Pixel) o;

        return r == p.r && g == p.g && b == p.b && a == p.a;
    }
}
