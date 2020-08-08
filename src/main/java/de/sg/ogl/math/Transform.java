package de.sg.ogl.math;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

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
}
