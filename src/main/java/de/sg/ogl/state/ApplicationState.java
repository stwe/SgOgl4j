/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.state;

public abstract class ApplicationState implements State {

    private final StateMachine stateMachine;

    public ApplicationState(StateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    public StateMachine getStateMachine() {
        return stateMachine;
    }
}
