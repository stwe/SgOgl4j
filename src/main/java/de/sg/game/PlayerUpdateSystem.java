package de.sg.game;

import de.sg.ogl.Input;
import de.sg.ogl.SgOglEngine;
import de.sg.ogl.ecs.Manager;
import org.lwjgl.glfw.GLFW;

// todo rename
public class PlayerUpdateSystem {

    private final SgOglEngine engine;
    private final Manager manager;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public PlayerUpdateSystem(SgOglEngine engine, Manager manager) {
        this.engine = engine;
        this.manager = manager;
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void update(float dt) {
        var paddleEntities = manager.getEntities("paddleSig");

        for (var e : paddleEntities) {
            var transformComp = manager.getComponent(e.id, TransformComponent.class).orElseThrow();
            var physicsComp = manager.getComponent(e.id, PhysicsComponent.class).orElseThrow();

            var velocity = physicsComp.getVelocity().x * dt;

            if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT)) {
                if (transformComp.getPosition().x >= 0.0f) {
                    transformComp.getPosition().x -= velocity;
                }
            }

            if (Input.isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
                if (transformComp.getPosition().x <= engine.getWindow().getWidth() - transformComp.getSize().x) {
                    transformComp.getPosition().x += velocity;
                }
            }
        }
    }
}
