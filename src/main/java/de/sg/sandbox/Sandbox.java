package de.sg.sandbox;

import de.sg.ogl.BaseApplication;

import java.io.File;
import java.io.FileNotFoundException;

public class Sandbox extends BaseApplication {

    @Override
    public void init() throws Exception {
        var textureFile = getClass().getResource("/texture/Grass.jpg");
        if (textureFile == null) {
            throw new FileNotFoundException("File not found");
        }

        getEngine().getResourceManager().LoadTextureResource(new File(textureFile.getFile()).getPath());

        getEngine().getResourceManager().LoadShaderResource("");
    }

    @Override
    public void input() {

    }

    @Override
    public void update() {

    }

    @Override
    public void render() {

    }

    @Override
    public void cleanUp() {

    }
}
