/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.sgbrick;

import de.sg.ogl.SgOglEngine;
import de.sg.ogl.SgOglRuntimeException;
import de.sg.ogl.ecs.Entity;
import de.sg.ogl.ecs.Listener;
import de.sg.ogl.ecs.Manager;
import de.sg.ogl.ecs.System;
import de.sg.ogl.resource.Mesh;
import org.joml.Vector2f;

import java.util.function.Consumer;

import static de.sg.sgbrick.Game.*;

public class ResetSystem extends Listener<GameOverEvent> implements System {

    private final SgOglEngine engine;
    private final Manager manager;
    private final Mesh mesh;
    private final String levelPath;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public ResetSystem(SgOglEngine engine, Manager manager, Mesh mesh, String levelPath) {
        this.engine = engine;
        this.manager = manager;
        this.mesh = mesh;
        this.levelPath = levelPath;
    }

    //-------------------------------------------------
    // Override onEvent
    //-------------------------------------------------

    @Override
    protected Consumer<GameOverEvent> onEvent() {
        return (event) -> {
            try {
                resetLevel();

                var playerEntities = manager.getEntities(PlayerComponent.class);
                if (playerEntities.size() > 1) {
                    throw new SgOglRuntimeException("Invalid number of player entities.");
                }

                var ballEntities = manager.getEntities(BallComponent.class);
                if (ballEntities.size() > 1) {
                    throw new SgOglRuntimeException("Invalid number of ball entities.");
                }

                var player = playerEntities.get(0);
                var ball = ballEntities.get(0);

                resetPlayerAndBall(player, ball);
            } catch (Exception e) {
                e.printStackTrace();
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

    }

    @Override
    public void render() {

    }

    @Override
    public void cleanUp() {

    }

    //-------------------------------------------------
    // Reset
    //-------------------------------------------------

    private void resetLevel() throws Exception {
        new Level(levelPath, engine, manager, mesh);
    }

    private void resetPlayerAndBall(Entity player, Entity ball) {
        // reset player
        var playerTransformComp = manager.getComponent(player.id, TransformComponent.class).orElseThrow();
        playerTransformComp.setPosition(new Vector2f(
                engine.getWindow().getWidth() / 2.0f - PLAYER_SIZE.x / 2.0f,
                engine.getWindow().getHeight() - PLAYER_SIZE.y
        ));

        // reset ball
        var ballComp = manager.getComponent(ball.id, BallComponent.class).orElseThrow();
        ballComp.setStuck(true);

        var ballTransformComp = manager.getComponent(ball.id, TransformComponent.class).orElseThrow();
        var ballPhysicsComp = manager.getComponent(ball.id, PhysicsComponent.class).orElseThrow();

        ballTransformComp.setPosition(new Vector2f(playerTransformComp.getPosition())
                .add(new Vector2f(PLAYER_SIZE.x / 2.0f - BALL_RADIUS, -(BALL_RADIUS * 2.0f))));

        ballPhysicsComp.setVelocity(BALL_VELOCITY);
    }
}
