package de.sg.ogl;

public interface Application {
    void init() throws Exception;
    void input();
    void update(float dt);
    void render();
    void cleanUp();
}
