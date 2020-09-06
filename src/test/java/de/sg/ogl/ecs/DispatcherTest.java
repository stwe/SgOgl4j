/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.ecs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class DispatcherTest {

    private static Dispatcher dispatcher;

    @BeforeEach
    void setUp() {
        dispatcher = new Dispatcher();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void dispatch() {
        dispatcher.addListener(PositionChangedEvent.class, new PositionChangedListener());
        var pe = new PositionChangedEvent();
        dispatcher.dispatch(pe);
        assertThat(pe.x, is(999));

        // no listener registered -> warning
        var co = new CollisionEvent();
        dispatcher.dispatch(co);
        assertThat(co.isCollision(), is(false));
    }

    @Test
    void addListener() {
        // duplicate entries are skipped
        dispatcher.addListener(PositionChangedEvent.class, new PositionChangedListener());
        dispatcher.addListener(PositionChangedEvent.class, new PositionChangedListener());
        dispatcher.addListener(CollisionEvent.class, new CollisionListener());
        dispatcher.addListener(CollisionEvent.class, new CollisionListener());

        assertThat(dispatcher.getListenerMap().size(), is(2));
    }
}
