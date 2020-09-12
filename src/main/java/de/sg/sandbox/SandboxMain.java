/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.sandbox;

import de.sg.ogl.SgOglEngine;

public class SandboxMain {
    public static void main(String[] args) {
        Sandbox application = null;
        try {
            application = new Sandbox();
        } catch (Exception e) {
            e.printStackTrace();
        }

        var engine = new SgOglEngine(application);
        engine.run();
    }
}
