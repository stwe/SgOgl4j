/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.state;

import java.util.Objects;

public class EmptyState extends ApplicationState {

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public EmptyState(StateMachine stateMachine) {
        super(Objects.requireNonNull(stateMachine, "stateMachine must not be null"));
    }

    //-------------------------------------------------
    // Implement State
    //-------------------------------------------------

    @Override
    public void init(Object... params) throws Exception {

    }

    @Override
    public void input() {

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
}
