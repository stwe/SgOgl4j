/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.game;

import de.sg.ogl.SgOglEngine;
import de.sg.ogl.SgOglRuntimeException;
import de.sg.ogl.ecs.Manager;
import de.sg.ogl.ecs.System;

import static de.sg.game.Game.BRICK_SIGNATURE;
import static de.sg.ogl.Log.LOGGER;

public class CollisionSystem implements System {

    private final SgOglEngine engine;
    private final Manager manager;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public CollisionSystem(SgOglEngine engine, Manager manager) {
        this.engine = engine;
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

        // there is only one player here
        if (playerEntities.size() > 1) {
            throw new SgOglRuntimeException("Invalid number of player entities.");
        }

        var player = playerEntities.get(0);

        // check collisions between bricks and ball
        for (var brick : brickEntities) {
            var brickAabbComp = manager.getComponent(brick.id, AabbComponent.class).orElseThrow();
            var brickSolidComp = manager.getComponent(brick.id, SolidComponent.class).orElseThrow();
            var healthComp = manager.getComponent(brick.id, HealthComponent.class).orElseThrow();

            var ballAabbComp = manager.getComponent(ball.id, AabbComponent.class).orElseThrow();

            if (aabbIntersectsAabb(brickAabbComp, ballAabbComp)) {
                // todo: collision event: destroy brick
                if (!brickSolidComp.isSolid()) {
                    healthComp.setDestroyed(true);
                }
            }
        }

        // check collisions between player and ball
        var ballComp = manager.getComponent(ball.id, BallComponent.class).orElseThrow();
        var ballAabbComp = manager.getComponent(ball.id, AabbComponent.class).orElseThrow();
        var paddleAabbComp = manager.getComponent(player.id, AabbComponent.class).orElseThrow();
        if (!ballComp.isStuck()) {
            if (aabbIntersectsAabb(ballAabbComp, paddleAabbComp)) {
                // todo: collision event: print msg
                LOGGER.debug("Paddle Collision");
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

    private static boolean aabbIntersectsAabb(AabbComponent one, AabbComponent two) {
        if (one.getMax().x < two.getMin().x || one.getMin().x > two.getMax().x) {
            return false;
        }

        if (one.getMax().y < two.getMin().y || one.getMin().y > two.getMax().y) {
            return false;
        }

        return true;
    }
}
