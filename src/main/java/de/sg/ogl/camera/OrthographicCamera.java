/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.camera;

import de.sg.ogl.Input;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.glfw.GLFW.*;

public class OrthographicCamera {

    public enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    private Vector2f position = new Vector2f(0.0f, 0.0f);
    private float cameraVelocity = 2.0f;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public OrthographicCamera() {
        LOGGER.debug("Creates OrthographicCamera object.");
    }

    public OrthographicCamera(Vector2f position) {
        LOGGER.debug("Creates OrthographicCamera object.");

        this.position = position;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public Vector2f getPosition() {
        return position;
    }

    public Matrix4f getViewMatrix() {
        Matrix4f transformMatrix = new Matrix4f();
        transformMatrix
                .identity()
                .translate(new Vector3f(position, 0.0f))
                .rotateZ((float) Math.toRadians(0.0f))
                .scale(new Vector3f(1.0f));

        return transformMatrix.invert();
    }

    //-------------------------------------------------
    // Setter
    //-------------------------------------------------

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void setCameraVelocity(float cameraVelocity) {
        this.cameraVelocity = cameraVelocity;
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void update(float dt) {
        if (Input.isKeyPressed(GLFW.GLFW_KEY_W) || Input.isKeyPressed(GLFW_KEY_UP)) {
            processKeyboard(Direction.UP, dt);
        }

        if (Input.isKeyPressed(GLFW.GLFW_KEY_S) || Input.isKeyPressed(GLFW_KEY_DOWN)) {
            processKeyboard(Direction.DOWN, dt);
        }

        if (Input.isKeyPressed(GLFW.GLFW_KEY_A) || Input.isKeyPressed(GLFW_KEY_LEFT)) {
            processKeyboard(Direction.LEFT, dt);
        }

        if (Input.isKeyPressed(GLFW.GLFW_KEY_D) || Input.isKeyPressed(GLFW_KEY_RIGHT)) {
            processKeyboard(Direction.RIGHT, dt);
        }

        if (Input.isKeyPressed(GLFW.GLFW_KEY_I)) {
            LOGGER.info("Camera x: {},  y: {}", position.x, position.y);
        }
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private void processKeyboard(Direction direction, float dt) {
        var velocity = cameraVelocity * dt;

        if (direction == Direction.UP) {
            var v = new Vector2f(new Vector2f(0.0f, 1.0f).mul(velocity));
            position.add(v);
        }

        if (direction == Direction.DOWN) {
            var v = new Vector2f(new Vector2f(0.0f, 1.0f).mul(velocity));
            position.sub(v);
        }

        if (direction == Direction.LEFT) {
            var v = new Vector2f(new Vector2f(1.0f, 0.0f).mul(velocity));
            position.sub(v);
        }

        if (direction == Direction.RIGHT) {
            var v = new Vector2f(new Vector2f(1.0f, 0.0f).mul(velocity));
            position.add(v);
        }
    }
}
