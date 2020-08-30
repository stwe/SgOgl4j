package de.sg.game;

import de.sg.ogl.Input;
import de.sg.ogl.SgOglEngine;
import de.sg.ogl.ecs.Manager;
import org.lwjgl.glfw.GLFW;

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
        var entities = manager.getEntities();

        for (int i = 0; i < manager.getEntityCount(); i++) {
            var e = entities.get(i);

            if (manager.matchesSignature(e.id, "paddleSig")) {
                var transformComp = manager.getComponent(e.id, TransformComponent.class).get();
                var physicsComp = manager.getComponent(e.id, PhysicsComponent.class).get();

                var velocity = physicsComp.getVelocity() * dt;

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
}
