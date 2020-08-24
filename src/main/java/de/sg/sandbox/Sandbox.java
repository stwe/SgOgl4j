package de.sg.sandbox;

import de.sg.ogl.BaseApplication;
import de.sg.ogl.Input;
import de.sg.ogl.resource.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Sandbox extends BaseApplication {

    private static final Vector2f PLAYER_SIZE = new Vector2f(100.0f, 20.0f);
    private static final float PLAYER_VELOCITY = 500.0f;

    private SpriteRenderer spriteRenderer;
    private Texture background;
    private Texture paddle;
    private Level level;
    private GameObject player;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Sandbox() throws IOException, IllegalAccessException {
    }

    //-------------------------------------------------
    // Override
    //-------------------------------------------------

    @Override
    public void init() throws Exception {
        spriteRenderer = new SpriteRenderer(getEngine());
        spriteRenderer.init();

        background = getEngine().getResourceManager().loadTextureResource("/texture/background.jpg");
        paddle = getEngine().getResourceManager().loadTextureResource("/texture/paddle.png");

        level = new Level("/level/level.lvl", getEngine());

        var playerPosition = new Vector2f(
                getEngine().getWindow().getWidth() / 2.0f - PLAYER_SIZE.x / 2.0f,
                getEngine().getWindow().getHeight() - PLAYER_SIZE.y
        );

        player = new GameObject(playerPosition, PLAYER_SIZE, paddle, new Vector3f(1.0f));
    }

    @Override
    public void input() {
        if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(getEngine().getWindow().getWindowHandle(), true);
        }

        spriteRenderer.input();
    }

    @Override
    public void update(float dt) {
        var velocity = PLAYER_VELOCITY * dt;

        if (Input.isKeyDown(GLFW.GLFW_KEY_A)) {
            if (player.getPosition().x >= 0.0f) {
                player.getPosition().x -= velocity;
            }
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_D)) {
            if (player.getPosition().x <= getEngine().getWindow().getWidth() - player.getSize().x) {
                player.getPosition().x += velocity;
            }
        }

        spriteRenderer.update(dt);
    }

    @Override
    public void render() {
        spriteRenderer.prepareRendering();

        spriteRenderer.render(
                background,
                new Vector2f(0.0f),
                new Vector2f(getEngine().getWindow().getWidth(), getEngine().getWindow().getHeight()),
                0.0f,
                new Vector3f(1.0f)
        );

        level.render(spriteRenderer);
        player.render(spriteRenderer);

        spriteRenderer.finishRendering();
    }

    @Override
    public void cleanUp() {
        spriteRenderer.cleanUp();
    }
}
