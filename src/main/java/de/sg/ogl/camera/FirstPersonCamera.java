package de.sg.ogl.camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class FirstPersonCamera {

    private Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);

    private float yaw = 20.0f;
    private float pitch = 0.0f;

    private Vector3f front = new Vector3f(0.0f, 0.0f, 1.0f);
    private Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
    private Vector3f worldUp = new Vector3f(0.0f, 1.0f, 0.0f);
    private Vector3f right = new Vector3f(1.0f, 0.0f, 0.0f);

    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(position, position.add(front), up);
    }

    public void input() {
        processMouse();
    }

    public void update() {

    }

    private void processKeyboard() {

    }

    private void processMouse() {

    }
}
