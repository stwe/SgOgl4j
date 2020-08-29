package de.sg.sandbox;

import org.joml.Vector2f;

public interface Entity {
    Vector2f update(float dt, int width);
    void render(Renderer renderer);
}
