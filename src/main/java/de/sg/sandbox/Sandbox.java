package de.sg.sandbox;

import de.sg.ogl.BaseApplication;
import de.sg.ogl.Input;
import de.sg.ogl.resource.Texture;
import de.sg.sandbox.physics.Intersect;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Sandbox extends BaseApplication {

    private static final Vector2f PLAYER_SIZE = new Vector2f(100.0f, 20.0f);
    private static final float PLAYER_VELOCITY = 500.0f;

    private static final Vector2f INITIAL_BALL_VELOCITY = new Vector2f(100.0f, -350.0f);
    private static final float BALL_RADIUS = 12.5f;

    private SpriteRenderer spriteRenderer;
    private Texture backgroundTexture;
    private Texture paddleTexture;
    private Texture ballTexture;
    private Level level;
    private GameObject player;
    private Ball ball;

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

        backgroundTexture = getEngine().getResourceManager().loadTextureResource("/texture/background.jpg");
        paddleTexture = getEngine().getResourceManager().loadTextureResource("/texture/paddle.png");
        ballTexture = getEngine().getResourceManager().loadTextureResource("/texture/awesomeface.png");

        level = new Level("/level/level.lvl", getEngine());

        var playerPosition = new Vector2f(
                getEngine().getWindow().getWidth() / 2.0f - PLAYER_SIZE.x / 2.0f,
                getEngine().getWindow().getHeight() - PLAYER_SIZE.y
        );

        player = new GameObject(playerPosition, PLAYER_SIZE, paddleTexture);

        var ballPosition = new Vector2f(playerPosition).add(new Vector2f(PLAYER_SIZE.x / 2.0f - BALL_RADIUS, -BALL_RADIUS * 2.0f));

        ball = new Ball(ballPosition, BALL_RADIUS, INITIAL_BALL_VELOCITY, ballTexture);
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

        if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT)) {
            if (player.getPosition().x >= 0.0f) {
                player.getPosition().x -= velocity;
                if (ball.isStuck()) {
                    ball.getPosition().x -= velocity;
                }
            }
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_RIGHT)) {
            if (player.getPosition().x <= getEngine().getWindow().getWidth() - player.getSize().x) {
                player.getPosition().x += velocity;
                if (ball.isStuck()) {
                    ball.getPosition().x += velocity;
                }
            }
        }

        if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE)) {
            ball.setStuck(false);
        }

        ball.update(dt, getEngine().getWindow().getWidth());
        doCollisions();

        spriteRenderer.update(dt);
    }

    @Override
    public void render() {
        spriteRenderer.prepareRendering();

        spriteRenderer.render(
                backgroundTexture,
                new Vector2f(0.0f),
                new Vector2f(getEngine().getWindow().getWidth(), getEngine().getWindow().getHeight()),
                0.0f,
                new Vector3f(1.0f)
        );

        level.render(spriteRenderer);
        player.render(spriteRenderer);
        ball.render(spriteRenderer);

        spriteRenderer.finishRendering();
    }

    @Override
    public void cleanUp() {
        spriteRenderer.cleanUp();
    }

    //-------------------------------------------------
    // Collisions
    //-------------------------------------------------

    private boolean checkCollision(GameObject brick, Ball ball) {
        return Intersect.circleIntersectsAabb(ball.getCircle(), brick.aabb);
    }

    private void doCollisions() {
        for (var brick : level.getBricks()) {
            if (!brick.isDestroyed()) {
                if (checkCollision(brick, ball)) {
                    if (!brick.isSolid()) {
                        brick.setDestroyed(true);
                    }
                }
            }
        }
    }
}
