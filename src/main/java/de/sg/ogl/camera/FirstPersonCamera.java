/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.camera;

import de.sg.ogl.input.KeyInput;
import de.sg.ogl.input.MouseInput;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import static de.sg.ogl.Log.LOGGER;
import static org.joml.Math.*;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

public class FirstPersonCamera {

    public enum Direction {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    private Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
    private float yaw = 90.0f;
    private float pitch = 0.0f;

    private Vector3f front = new Vector3f(0.0f, 0.0f, 1.0f);
    private Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
    private final Vector3f worldUp = new Vector3f(0.0f, 1.0f, 0.0f);
    private Vector3f right = new Vector3f(1.0f, 0.0f, 0.0f);

    private float cameraVelocity = 2.0f;
    private float mouseSensitivity = 0.025f;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public FirstPersonCamera() {
        LOGGER.debug("Creates FirstPersonCamera object.");
    }

    public FirstPersonCamera(Vector3f position, float yaw, float pitch) {
        LOGGER.debug("Creates FirstPersonCamera object.");

        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public Vector3f getPosition() {
        return position;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getCameraVelocity() {
        return cameraVelocity;
    }

    public float getMouseSensitivity() {
        return mouseSensitivity;
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(position, new Vector3f(position).add(front), up);
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setCameraVelocity(float cameraVelocity) {
        this.cameraVelocity = cameraVelocity;
    }

    public void setMouseSensitivity(float mouseSensitivity) {
        this.mouseSensitivity = mouseSensitivity;
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void input() {
        processMouse();
    }

    public void update(float dt) {
        if (KeyInput.isKeyDown(GLFW.GLFW_KEY_W)) {
            processKeyboard(Direction.FORWARD, dt);
        }

        if (KeyInput.isKeyDown(GLFW.GLFW_KEY_S)) {
            processKeyboard(Direction.BACKWARD, dt);
        }

        if (KeyInput.isKeyDown(GLFW.GLFW_KEY_A)) {
            processKeyboard(Direction.LEFT, dt);
        }

        if (KeyInput.isKeyDown(GLFW.GLFW_KEY_D)) {
            processKeyboard(Direction.RIGHT, dt);
        }

        if (KeyInput.isKeyDown(GLFW.GLFW_KEY_O)) {
            processKeyboard(Direction.UP, dt);
        }

        if (KeyInput.isKeyDown(GLFW.GLFW_KEY_U)) {
            processKeyboard(Direction.DOWN, dt);
        }

        updateFront();
        updateRightAndUp();

        if (KeyInput.isKeyDown(GLFW.GLFW_KEY_I)) {
            LOGGER.info("Camera x: {}  y: {}  z: {}", position.x, position.y, position.z);
            LOGGER.info("Camera yaw: {}  pitch: {}", yaw, pitch);
        }
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private void processKeyboard(Direction direction, float dt) {
        var velocity = cameraVelocity * dt;

        if (direction == Direction.FORWARD) {
            var v = new Vector3f(front.mul(velocity));
            position.add(v);
        }

        if (direction == Direction.BACKWARD) {
            var v = new Vector3f(front.mul(velocity));
            position.sub(v);
        }

        if (direction == Direction.LEFT) {
            var v = new Vector3f(right.mul(velocity));
            position.sub(v);
        }

        if (direction == Direction.RIGHT) {
            var v = new Vector3f(right.mul(velocity));
            position.add(v);
        }

        if (direction == Direction.UP) {
            var v = new Vector3f(up.mul(velocity));
            position.add(v);
        }

        if (direction == Direction.DOWN) {
            var v = new Vector3f(up.mul(velocity));
            position.sub(v);
        }
    }

    private void processMouse() {
        if (MouseInput.isMouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT)) {
            yaw += MouseInput.getDx() * mouseSensitivity;
            pitch += MouseInput.getDy() * mouseSensitivity;
        }
    }

    private void updateFront() {
        Vector3f newFront = new Vector3f();
        newFront.x = cos(toRadians(yaw)) * cos(toRadians(pitch));
        newFront.y = sin(toRadians(pitch));
        newFront.z = sin(toRadians(yaw)) * cos(toRadians(pitch));

        this.front = new Vector3f(newFront.normalize());
    }

    private void updateRightAndUp() {
        // update right
        var frontVec = new Vector3f(this.front);
        this.right = new Vector3f(frontVec.cross(this.worldUp));
        this.right.normalize();

        // update up
        var rightVec = new Vector3f(this.right);
        this.up = rightVec.cross(this.front);
        this.up.normalize();
    }
}
