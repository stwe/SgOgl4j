package de.sg.sandbox;

import de.sg.ogl.BaseApplication;

public class Sandbox extends BaseApplication {

    @Override
    public void init() throws Exception {
        var texture0 = getEngine().getResourceManager().loadTextureResource("/texture/Grass.jpg");
        var texture1 = getEngine().getResourceManager().loadTextureResource("/texture/Grass.jpg");

        var shader0 = getEngine().getResourceManager().loadShaderResource("simple");
        var shader1 = getEngine().getResourceManager().loadShaderResource("simple");
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
