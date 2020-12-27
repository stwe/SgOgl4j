/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.physics;

import org.joml.Vector2f;

public class Aabb {

    public Vector2f position;
    public Vector2f size;

    public Aabb() {}

    public Aabb(Vector2f position, Vector2f size) {
        this.position = position;
        this.size = size;
    }

    public static boolean pointVsAabb(Vector2f point, Aabb aabb) {
        return
            point.x >= aabb.position.x &&
            point.y >= aabb.position.y &&
            point.x < aabb.position.x + aabb.size.x &&
            point.y < aabb.position.y + aabb.size.y;
    }

    public static boolean aabbVsAabb(Aabb a, Aabb b) {
        return
            a.position.x < b.position.x + b.size.x &&
            a.position.x + a.size.x > b.position.x &&
            a.position.y < b.position.y + b.size.y &&
            a.position.y + a.size.y > b.position.y;
    }
}
