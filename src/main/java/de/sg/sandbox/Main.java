package de.sg.sandbox;

import de.sg.ogl.SgOglEngine;

public class Main {

    public static void main(String[] args) {
        Sandbox sandbox = null;
        try {
            sandbox = new Sandbox();
        } catch (Exception e) {
            e.printStackTrace();
        }

        var engine = new SgOglEngine(sandbox);
        engine.run();
    }
}
