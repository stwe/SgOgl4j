/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.game;

import de.sg.ogl.Input;
import de.sg.ogl.SgOglEngine;
import de.sg.ogl.SgOglRuntimeException;
import de.sg.ogl.ecs.Dispatcher;
import de.sg.ogl.ecs.Listener;
import de.sg.ogl.ecs.Manager;
import de.sg.ogl.ecs.System;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class UpdateBallSystem extends Listener<UpdatePlayerEvent> implements System {

    private final SgOglEngine engine;
    private final Manager manager;
    private final Dispatcher dispatcher;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public UpdateBallSystem(SgOglEngine engine, Manager manager, Dispatcher dispatcher) {
        this.engine = engine;
        this.manager = manager;
        this.dispatcher = dispatcher;
    }

    //-------------------------------------------------
    // Override onEvent
    //-------------------------------------------------

    @Override
    protected Consumer<UpdatePlayerEvent> onEvent() {
        // if the position of the player changes, the position of the ball should also change
        return (event) -> {
            var ballEntities = manager.getEntities(BallComponent.class);

            // there is only one ball here
            if (ballEntities.size() > 1) {
                throw new SgOglRuntimeException("Invalid number of ball entities.");
            }

            // get the ball entity
            var ball = ballEntities.get(0);

            // update ball position
            var ballComp = manager.getComponent(ball.id, BallComponent.class).orElseThrow();
            if (ballComp.isStuck()) {
                var transformComp = manager.getComponent(ball.id, TransformComponent.class).orElseThrow();

                if (event.getDirection() == UpdatePlayerEvent.DIRECTION.LEFT) {
                    transformComp.getPosition().x -= event.getVelocity();
                }

                if (event.getDirection() == UpdatePlayerEvent.DIRECTION.RIGHT) {
                    transformComp.getPosition().x += event.getVelocity();
                }
            }
        };
    }

    //-------------------------------------------------
    // Implement System
    //-------------------------------------------------

    @Override
    public void init() throws Exception {

    }

    @Override
    public void update(float dt) {
        var ballEntities = manager.getEntities(BallComponent.class);

        // there is only one ball here
        if (ballEntities.size() > 1) {
            throw new SgOglRuntimeException("Invalid number of ball entities.");
        }

        // get the ball entity
        var ball = ballEntities.get(0);

        // get ball component
        var ballComp = manager.getComponent(ball.id, BallComponent.class).orElseThrow();

        // release the ball from the paddle
        if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
            ballComp.setStuck(false);
        }

        // move the ball
        if (!ballComp.isStuck()) {
            var ballPhysicsComp = manager.getComponent(ball.id, PhysicsComponent.class).orElseThrow();
            var ballTransformComp = manager.getComponent(ball.id, TransformComponent.class).orElseThrow();
            var ballAabbComp = manager.getComponent(ball.id, AabbComponent.class).orElseThrow();

            var ballVelocity = new Vector2f(ballPhysicsComp.getVelocity()).mul(dt);
            ballTransformComp.getPosition().add(ballVelocity);

            if (ballTransformComp.getPosition().x <= 0.0f) {
                ballPhysicsComp.getVelocity().x = -ballPhysicsComp.getVelocity().x;
                ballTransformComp.getPosition().x = 0.0f;
            } else if (ballTransformComp.getPosition().x + ballTransformComp.getSize().x >= engine.getWindow().getWidth()) {
                ballPhysicsComp.getVelocity().x = -ballPhysicsComp.getVelocity().x;
                ballTransformComp.getPosition().x = engine.getWindow().getWidth() - ballTransformComp.getSize().x;
            }

            if (ballTransformComp.getPosition().y <= 0.0f) {
                ballPhysicsComp.getVelocity().y = -ballPhysicsComp.getVelocity().y;
                ballTransformComp.getPosition().y = 0.0f;
            }

            // the game was lost
            if (ballTransformComp.getPosition().y >= engine.getWindow().getHeight()) {
                // dispatch game over event
                dispatcher.dispatch(new GameOverEvent());
            }

            // update ball aabb
            ballAabbComp.setMin(new Vector2f(ballTransformComp.getPosition()));
            ballAabbComp.setMax(new Vector2f(ballTransformComp.getPosition()).add(ballTransformComp.getSize()));
        }
    }

    @Override
    public void render() {

    }

    @Override
    public void cleanUp() {

    }
}
