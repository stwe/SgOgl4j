/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.sgbrick.system;

import de.sg.ogl.ecs.Listener;
import de.sg.ogl.ecs.System;
import de.sg.sgbrick.Game;
import de.sg.sgbrick.event.GameOverEvent;
import de.sg.sgbrick.Level;

import java.util.function.Consumer;

public class ResetSystem extends Listener<GameOverEvent> implements System {

    private final Game game;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public ResetSystem(Game game) {
        this.game = game;
    }

    //-------------------------------------------------
    // Override onEvent
    //-------------------------------------------------

    @Override
    protected Consumer<GameOverEvent> onEvent() {
        return (event) -> {
            try {
                resetLevel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    //-------------------------------------------------
    // Implement System
    //-------------------------------------------------

    @Override
    public void init() throws Exception {

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

    //-------------------------------------------------
    // Reset
    //-------------------------------------------------

    private void resetLevel() throws Exception {
        game.getManager().clear();

        // create background
        game.createBgEntity();

        // load level - create brick entities
        new Level(Game.LEVEL, game.getEngine(), game.getManager(), game.getMesh());

        // create player
        game.createPlayerEntity();

        // create ball
        game.createBallEntity();

        game.getManager().update();
    }
}
