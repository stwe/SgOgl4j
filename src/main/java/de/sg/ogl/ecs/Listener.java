/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.ecs;

import java.util.function.Consumer;

public class Listener<T extends Event> {

    private Consumer<T> consumer = onEvent();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Listener() {
    }

    public Listener(Consumer<T> consumer) {
        this.consumer = consumer;
    }

    //-------------------------------------------------
    // Can be override
    //-------------------------------------------------

    protected Consumer<T> onEvent() {
        return null;
    }

    //-------------------------------------------------
    // Invoke
    //-------------------------------------------------

    void invoke(T event){
        consumer.accept(event);
    }
}
