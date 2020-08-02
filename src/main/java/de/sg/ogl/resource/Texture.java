package de.sg.ogl.resource;

public class Texture {

    String path;

    int width;
    int height;
    int nrChannels;

    int resourceId;

    boolean loadVerticalFlipped;

    private int LoadTexture() {
        return 0;
    }

    public int getResourceId() {
        return resourceId;
    }
}
