/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2021. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.test;

import de.sg.ogl.SgOglEngine;

public class TestMain {

    public static void main(String[] args) {
        TestApp application = null;
        try {
            application = new TestApp();
        } catch (Exception e) {
            e.printStackTrace();
        }

        var engine = new SgOglEngine(application);
        engine.run();
    }
}
