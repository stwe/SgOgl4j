/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.ecs;

import java.util.function.Consumer;

public class PositionChangedListener extends Listener<PositionChangedEvent> {

    @Override
    protected Consumer<PositionChangedEvent> onEvent() {
        return (e) -> {
            java.lang.System.out.println("Do something with the new position.");
            e.x = 999;
            e.y = 999;
        };
    }
}
