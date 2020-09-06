/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.ecs;

import java.util.function.Consumer;

public class CollisionListener extends Listener<CollisionEvent> {

    @Override
    protected Consumer<CollisionEvent> onEvent() {
        return (e) -> {
            java.lang.System.out.println("The Collision Event can be handled here.");
            e.setCollision(true);
        };
    }
}
