package de.sg.sandbox;

import de.sg.ogl.resource.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

public interface Renderer {
    void init() throws Exception;

    void input();
    void update(float dt);
    void render(Texture texture, Vector2f position, Vector2f size, float rotate, Vector3f color);

    void prepareRendering();
    void finishRendering();

    void cleanUp();
}
