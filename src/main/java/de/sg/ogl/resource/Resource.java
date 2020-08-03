package de.sg.ogl.resource;

public interface Resource {
    void load() throws Exception;
    void cleanUp();
}
