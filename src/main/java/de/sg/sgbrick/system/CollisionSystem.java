/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.sgbrick.system;

import de.sg.ogl.SgOglRuntimeException;
import de.sg.ogl.ecs.Manager;
import de.sg.ogl.ecs.System;
import de.sg.sgbrick.Collision;
import de.sg.sgbrick.component.*;
import org.joml.Vector2f;

import static de.sg.sgbrick.Game.BALL_VELOCITY;
import static de.sg.sgbrick.Game.BRICK_SIGNATURE;
import static org.joml.Math.abs;

public class CollisionSystem implements System {

    private final Manager manager;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public CollisionSystem(Manager manager) {
        this.manager = manager;
    }

    //-------------------------------------------------
    // Implement System
    //-------------------------------------------------

    @Override
    public void init() throws Exception {

    }

    @Override
    public void update(float dt) {
        var brickEntities = manager.getEntities(BRICK_SIGNATURE);
        var ballEntities = manager.getEntities(BallComponent.class);
        var playerEntities = manager.getEntities(PlayerComponent.class);

        // there is only one ball here
        if (ballEntities.size() > 1) {
            throw new SgOglRuntimeException("Invalid number of ball entities.");
        }

        // get the ball entity
        var ball = ballEntities.get(0);

        // get ball components
        var ballCircleComp = manager.getComponent(ball.id, CircleComponent.class).orElseThrow();
        var ballPhysicsComp = manager.getComponent(ball.id, PhysicsComponent.class).orElseThrow();
        var ballTransformComp = manager.getComponent(ball.id, TransformComponent.class).orElseThrow();
        var ballComp = manager.getComponent(ball.id, BallComponent.class).orElseThrow();

        // there is only one player here
        if (playerEntities.size() > 1) {
            throw new SgOglRuntimeException("Invalid number of player entities.");
        }

        var player = playerEntities.get(0);

        // get player components
        var paddleAabbComp = manager.getComponent(player.id, AabbComponent.class).orElseThrow();
        var paddleTransformComp = manager.getComponent(player.id, TransformComponent.class).orElseThrow();

        // check collisions between bricks and ball
        for (var brick : brickEntities) {
            var brickHealthComp = manager.getComponent(brick.id, HealthComponent.class).orElseThrow();
            if (!brickHealthComp.isDestroyed()) {
                var brickAabbComp = manager.getComponent(brick.id, AabbComponent.class).orElseThrow();
                var brickSolidComp = manager.getComponent(brick.id, SolidComponent.class).orElseThrow();

                var collision = circleIntersectsAabb(ballCircleComp, brickAabbComp);
                if (collision.isCollision()) {
                    // todo: collision event
                    if (!brickSolidComp.isSolid()) {
                        brickHealthComp.setDestroyed(true);
                    }

                    var direction = collision.getDirection();
                    var difference = collision.getDifference();

                    if (direction == Collision.Direction.LEFT || direction == Collision.Direction.RIGHT) {
                        ballPhysicsComp.getVelocity().x = -ballPhysicsComp.getVelocity().x;

                        var penetration = (ballComp.getRadius() - abs(difference.x)) * 4.0f;

                        if (direction == Collision.Direction.LEFT) {
                            ballTransformComp.getPosition().x += penetration;
                        } else {
                            ballTransformComp.getPosition().x -= penetration;
                        }
                    } else {
                        ballPhysicsComp.getVelocity().y = -ballPhysicsComp.getVelocity().y;

                        var penetration = (ballComp.getRadius() - abs(difference.y)) * 4.0f;

                        if (direction == Collision.Direction.UP) {
                            ballTransformComp.getPosition().y -= penetration;
                        } else {
                            ballTransformComp.getPosition().y += penetration;
                        }
                    }
                }
            }
        }

        // check collisions between player and ball
        if (!ballComp.isStuck()) {
            var collision = circleIntersectsAabb(ballCircleComp, paddleAabbComp);
            if (collision.isCollision()) {
                // todo: collision event
                if (!ballComp.isStuck()) {
                    var centerBoard = paddleTransformComp.getPosition().x + paddleTransformComp.getSize().x / 2.0f;
                    var distance = (ballTransformComp.getPosition().x + ballComp.getRadius()) - centerBoard;
                    var percentage = distance / (paddleTransformComp.getSize().x / 2.0f);
                    var strength = 2.0f;
                    var oldVelocity = new Vector2f(ballPhysicsComp.getVelocity());
                    ballPhysicsComp.getVelocity().x = BALL_VELOCITY.x * percentage * strength;
                    ballPhysicsComp.getVelocity().normalize().mul(oldVelocity.length());
                    ballPhysicsComp.getVelocity().y = -1 * abs(ballPhysicsComp.getVelocity().y);
                }
            }
        }
    }

    @Override
    public void render() {

    }

    @Override
    public void cleanUp() {

    }

    //-------------------------------------------------
    // Intersect
    //-------------------------------------------------

    private static Vector2f closestPointOnAabb(Vector2f givenPoint, AabbComponent aabb) {
        var result = new Vector2f();

        var x = givenPoint.x;
        var y = givenPoint.y;

        if (x < aabb.getMin().x) {
            x = aabb.getMin().x;
        }

        if (x > aabb.getMax().x) {
            x = aabb.getMax().x;
        }

        if (y < aabb.getMin().y) {
            y = aabb.getMin().y;
        }

        if (y > aabb.getMax().y) {
            y = aabb.getMax().y;
        }

        result.x = x;
        result.y = y;

        return result;
    }

    private static Collision.Direction vectorDirection(Vector2f target) {
        Vector2f[] compass = new Vector2f[]{
                new Vector2f(0.0f, 1.0f),
                new Vector2f(1.0f, 0.0f),
                new Vector2f(0.0f, -1.0f),
                new Vector2f(-1.0f, 0.0f)
        };

        var max = 0.0f;
        var best = -1;

        for (int i = 0; i < 4; i++) {
            var normalTarget = new Vector2f(target).normalize();
            var dot = new Vector2f(normalTarget).dot(compass[i]);
            if (dot > max) {
                max = dot;
                best = i;
            }
        }

        return Collision.Direction.fromInt(best);
    }

    private static boolean aabbIntersectsAabb(AabbComponent one, AabbComponent two) {
        if (one.getMax().x < two.getMin().x || one.getMin().x > two.getMax().x) {
            return false;
        }

        if (one.getMax().y < two.getMin().y || one.getMin().y > two.getMax().y) {
            return false;
        }

        return true;
    }

    private static Collision circleIntersectsAabb(CircleComponent circle, AabbComponent aabb) {
        var closestPoint = closestPointOnAabb(circle.getCenter(), aabb);

        var v = new Vector2f(closestPoint).sub(circle.getCenter());
        var diff = new Vector2f(v);
        var dot = v.dot(v);

        if (dot <= circle.getRadius() * circle.getRadius()) {
            return new Collision(
                    true,
                    vectorDirection(diff),
                    diff
            );
        }

        return new Collision(
                false,
                Collision.Direction.UP,
                new Vector2f(0.0f)
        );
    }
}
