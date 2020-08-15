package de.sg.sandbox;

import de.sg.ogl.SgOglEngine;

public class Main {
    public static void main(String[] args) {
        var sandbox = new Sandbox();
        var engine = new SgOglEngine(sandbox);
        engine.run();
    }
}
