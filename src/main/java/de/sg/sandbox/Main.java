package de.sg.sandbox;

import de.sg.ogl.SgOglEngine;

public class Main {
    public static void main(String[] args) {
        try {
            var sandbox = new Sandbox();
            var engine = new SgOglEngine("Sandbox", 800, 600, false, sandbox);
            engine.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
