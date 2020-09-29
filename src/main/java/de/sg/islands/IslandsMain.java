/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.islands;

import de.sg.ogl.SgOglEngine;

public class IslandsMain {
    public static void main(String[] args) {
        Islands application = null;
        try {
            application = new Islands();
        } catch (Exception e) {
            e.printStackTrace();
        }

        var engine = new SgOglEngine(application);
        engine.run();
    }
}
