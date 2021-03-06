/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.math;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class Transform {

    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public Matrix4f getModelMatrix() {
        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix
                .identity()
                .translate(position)
                .rotateX((float)Math.toRadians(-rotation.x))
                .rotateY((float)Math.toRadians(-rotation.y))
                .rotateZ((float)Math.toRadians(-rotation.z))
                .scale(scale);

        return modelMatrix;
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }
}
