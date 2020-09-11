/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.sandbox;

import de.sg.ogl.SgOglEngine;

public class Sandbox {

    public static void main(String[] args) {
        Application application = null;
        try {
            application = new Application();
        } catch (Exception e) {
            e.printStackTrace();
        }

        var engine = new SgOglEngine(application);
        engine.run();
    }
}
