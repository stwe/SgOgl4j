/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.state;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StateMachine {

    private final StateContext stateContext;

    private final Map<String, State> states;
    private State currentState;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public StateMachine(StateContext stateContext) {
        this.stateContext = Objects.requireNonNull(stateContext, "stateContext must not be null");

        this.states = new HashMap<>();
        this.currentState = new EmptyState(this);
        this.states.put(null, currentState);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public StateContext getStateContext() {
        return stateContext;
    }

    //-------------------------------------------------
    // Add && Change
    //-------------------------------------------------

    public void add(String name, State state) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(state, "state must not be null");

        states.put(name, state);
    }

    public void change(String name, Object... params) throws Exception {
        Objects.requireNonNull(name, "name must not be null");

        currentState.cleanUp();

        currentState = states.get(name);
        currentState.init(params);
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void init() throws Exception {
        currentState.init();
    }

    public void input() {
        currentState.input();
    }

    public void update(float dt) {
        currentState.update(dt);
    }

    public void render() {
        currentState.render();
    }

    public void cleanUp() {
        currentState.cleanUp();
    }
}
