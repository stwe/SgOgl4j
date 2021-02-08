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

public class StateMachine implements State {

    private final StateContext stateContext;

    private final Map<String, State> states;
    private State currentState;

    public StateMachine(StateContext stateContext) {
        this.stateContext = stateContext;

        this.states = new HashMap<>();
        this.currentState = new EmptyState(this);
        this.states.put(null, currentState);
    }

    public StateContext getStateContext() {
        return stateContext;
    }

    public void add(String name, State state) {
        states.put(name, state);
    }

    public void change(String name) throws Exception {
        currentState.cleanUp();
        currentState = states.get(name);
        currentState.init();
    }

    @Override
    public void init() throws Exception {
        currentState.init();
    }

    @Override
    public void input() {
        currentState.input();
    }

    @Override
    public void update(float dt) {
        currentState.update(dt);
    }

    @Override
    public void render() {
        currentState.render();
    }

    @Override
    public void cleanUp() {
        currentState.cleanUp();
    }
}
