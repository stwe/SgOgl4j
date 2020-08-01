package de.sg.ogl;

public interface Application {
    void init() throws Exception;
    void input();
    void update();
    void render();
    void cleanUp();
}
