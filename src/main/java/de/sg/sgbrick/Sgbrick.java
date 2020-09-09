/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.sgbrick;

import de.sg.ogl.SgOglEngine;

public class Sgbrick {

    public static void main(String[] args) {
        Game game = null;
        try {
            game = new Game();
        } catch (Exception e) {
            e.printStackTrace();
        }

        var engine = new SgOglEngine(game);
        engine.run();
    }
}
