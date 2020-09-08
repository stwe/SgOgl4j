/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.sgbrick;

import de.sg.ogl.Input;
import de.sg.ogl.SgOglEngine;
import de.sg.ogl.SgOglRuntimeException;
import de.sg.ogl.ecs.Dispatcher;
import de.sg.ogl.ecs.Manager;
import de.sg.ogl.ecs.System;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class UpdatePlayerSystem implements System {

    private final SgOglEngine engine;
    private final Manager manager;
    private final Dispatcher dispatcher;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public UpdatePlayerSystem(SgOglEngine engine, Manager manager, Dispatcher dispatcher) {
        this.engine = engine;
        this.manager = manager;
        this.dispatcher = dispatcher;
    }

    //-------------------------------------------------
    // Implement System
    //-------------------------------------------------

    @Override
    public void init() throws Exception {

    }

    @Override
    public void update(float dt) {
        var playerEntities = manager.getEntities(PlayerComponent.class);

        // there is only one player here
        if (playerEntities.size() > 1) {
            throw new SgOglRuntimeException("Invalid number of player entities.");
        }

        var player = playerEntities.get(0);

        var transformComp = manager.getComponent(player.id, TransformComponent.class).orElseThrow();
        var physicsComp = manager.getComponent(player.id, PhysicsComponent.class).orElseThrow();
        var aabbComp = manager.getComponent(player.id, AabbComponent.class).orElseThrow();

        var velocity = physicsComp.getVelocity().x * dt;

        // move paddle left
        if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT)) {
            if (transformComp.getPosition().x >= 0.0f) {
                transformComp.getPosition().x -= velocity;

                // update paddle aabb
                aabbComp.setMin(new Vector2f(transformComp.getPosition()));
                aabbComp.setMax(new Vector2f(transformComp.getPosition()).add(transformComp.getSize()));

                // dispatch update player position event
                // if the position of the player changes, the position of the ball should also change
                dispatcher.dispatch(new UpdatePlayerEvent(UpdatePlayerEvent.DIRECTION.LEFT, velocity));
            }
        }

        // move paddle right
        if (Input.isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
            if (transformComp.getPosition().x <= engine.getWindow().getWidth() - transformComp.getSize().x) {
                transformComp.getPosition().x += velocity;

                // update paddle aabb
                aabbComp.setMin(new Vector2f(transformComp.getPosition()));
                aabbComp.setMax(new Vector2f(transformComp.getPosition()).add(transformComp.getSize()));

                // dispatch update player position event
                // if the position of the player changes, the position of the ball should also change
                dispatcher.dispatch(new UpdatePlayerEvent(UpdatePlayerEvent.DIRECTION.RIGHT, velocity));
            }
        }
    }

    @Override
    public void render() {

    }

    @Override
    public void cleanUp() {

    }
}
