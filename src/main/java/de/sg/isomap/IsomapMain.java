/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.isomap;

import de.sg.ogl.SgOglEngine;

public class IsomapMain {

    public static void main(String[] args) {
        Isomap application = null;
        try {
            application = new Isomap();
        } catch (Exception e) {
            e.printStackTrace();
        }

        var engine = new SgOglEngine(application);
        engine.run();
    }
}
