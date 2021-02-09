/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.state;

import java.util.Objects;

public abstract class ApplicationState implements State {

    /**
     * The parent StateMachine.
     */
    private final StateMachine stateMachine;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public ApplicationState(StateMachine stateMachine) {
        this.stateMachine = Objects.requireNonNull(stateMachine, "stateMachine must not be null");
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public StateMachine getStateMachine() {
        return stateMachine;
    }
}
