package de.sg.game;

import de.sg.ogl.Input;
import de.sg.ogl.SgOglEngine;
import de.sg.ogl.ecs.Manager;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class UpdateSystem {

    private final SgOglEngine engine;
    private final Manager manager;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public UpdateSystem(SgOglEngine engine, Manager manager) {
        this.engine = engine;
        this.manager = manager;
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void update(float dt) {
        var paddleEntities = manager.getEntities("paddleSig");
        var ballEntities = manager.getEntities("ballSig");

        for (var paddle : paddleEntities) {
            var transformComp = manager.getComponent(paddle.id, TransformComponent.class).orElseThrow();
            var physicsComp = manager.getComponent(paddle.id, PhysicsComponent.class).orElseThrow();
            var aabbComp = manager.getComponent(paddle.id, AabbComponent.class).orElseThrow();

            var velocity = physicsComp.getVelocity().x * dt;

            // move paddle left
            if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT)) {
                if (transformComp.getPosition().x >= 0.0f) {
                    transformComp.getPosition().x -= velocity;

                    // move ball left
                    for (var ball : ballEntities) {
                        var ballComp = manager.getComponent(ball.id, BallComponent.class).orElseThrow();
                        if (ballComp.isStuck()) {
                            var ballTransformComp = manager.getComponent(ball.id, TransformComponent.class).orElseThrow();
                            ballTransformComp.getPosition().x -= velocity;
                        }
                    }

                    // update paddle aabb
                    aabbComp.setMin(new Vector2f(transformComp.getPosition()));
                    aabbComp.setMax(new Vector2f(transformComp.getPosition()).add(transformComp.getSize()));
                }
            }

            // move paddle right
            if (Input.isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
                if (transformComp.getPosition().x <= engine.getWindow().getWidth() - transformComp.getSize().x) {
                    transformComp.getPosition().x += velocity;

                    // move ball right
                    for (var ball : ballEntities) {
                        var ballComp = manager.getComponent(ball.id, BallComponent.class).orElseThrow();
                        if (ballComp.isStuck()) {
                            var ballTransformComp = manager.getComponent(ball.id, TransformComponent.class).orElseThrow();
                            ballTransformComp.getPosition().x += velocity;
                        }
                    }

                    // update paddle aabb
                    aabbComp.setMin(new Vector2f(transformComp.getPosition()));
                    aabbComp.setMax(new Vector2f(transformComp.getPosition()).add(transformComp.getSize()));
                }
            }

            // release the ball from the paddle
            if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
                for (var ball : ballEntities) {
                    var ballComp = manager.getComponent(ball.id, BallComponent.class).orElseThrow();
                    ballComp.setStuck(false);
                }
            }
        }

        // move the ball at its speed
        for (var ball : ballEntities) {
            var ballComp = manager.getComponent(ball.id, BallComponent.class).orElseThrow();
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

                // update ball aabb
                ballAabbComp.setMin(new Vector2f(ballTransformComp.getPosition()));
                ballAabbComp.setMax(new Vector2f(ballTransformComp.getPosition()).add(ballTransformComp.getSize()));
            }
        }

        // check collisions
        var brickEntities = manager.getEntities("brickSig");
        for (var brick : brickEntities) {
            var brickAabbComp = manager.getComponent(brick.id, AabbComponent.class).orElseThrow();
            var brickSolidComp = manager.getComponent(brick.id, SolidComponent.class).orElseThrow();
            var healthComp = manager.getComponent(brick.id, HealthComponent.class).orElseThrow();

            for (var ball : ballEntities) {
                var ballAabbComp = manager.getComponent(ball.id, AabbComponent.class).orElseThrow();

                if (aabbIntersectsAabb(brickAabbComp, ballAabbComp)) {
                    if (!brickSolidComp.isSolid()) {
                        healthComp.setDestroyed(true);
                    }
                }
            }
        }

        for (var ball : ballEntities) {
            var ballAabbComp = manager.getComponent(ball.id, AabbComponent.class).orElseThrow();
            var ballComp = manager.getComponent(ball.id, BallComponent.class).orElseThrow();

            for (var paddle : paddleEntities) {
                var paddleAabbComp = manager.getComponent(paddle.id, AabbComponent.class).orElseThrow();

                if (!ballComp.isStuck()) {
                    if (aabbIntersectsAabb(ballAabbComp, paddleAabbComp)) {
                        System.out.println("Paddle Collision");
                    }
                }
            }
        }
    }

    public static boolean aabbIntersectsAabb(AabbComponent one, AabbComponent two) {
        if (one.getMax().x < two.getMin().x || one.getMin().x > two.getMax().x) {
            return false;
        }

        if (one.getMax().y < two.getMin().y || one.getMin().y > two.getMax().y) {
            return false;
        }

        return true;
    }
}
