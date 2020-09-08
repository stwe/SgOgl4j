package de.sg.sgbrick;

import de.sg.ogl.SgOglEngine;

public class Main {

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
