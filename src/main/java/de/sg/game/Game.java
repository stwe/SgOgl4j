package de.sg.game;

import de.sg.ogl.BaseApplication;
import de.sg.ogl.Input;
import de.sg.ogl.ecs.Manager;
import de.sg.ogl.ecs.Settings;
import de.sg.ogl.ecs.Signature;
import de.sg.ogl.resource.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Game extends BaseApplication {

    private static final Vector3f DEFAULT_COLOR = new Vector3f(1.0f);
    private static final float NO_ROTATION = 0.0f;

    private static final Vector2f PLAYER_SIZE = new Vector2f(100.0f, 20.0f);
    private static final float PLAYER_VELOCITY = 500.0f;

    private static final float BALL_RADIUS = 12.5f;
    private static final Vector2f BALL_VELOCITY = new Vector2f(100.0f, -350.0f);

    private Manager manager;

    private PlayfieldRenderSystem playfieldRenderSystem;
    private PlayerUpdateSystem playerUpdateSystem;

    private Vector2f initialPlayerPosition;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Game() throws IOException, IllegalAccessException {
    }

    //-------------------------------------------------
    // Override
    //-------------------------------------------------

    @Override
    public void init() throws Exception {
        // components
        ArrayList<Class<?>> componentTypes = new ArrayList<>();
        componentTypes.add(ColorTextureComponent.class);
        componentTypes.add(HealthComponent.class);
        componentTypes.add(PhysicsComponent.class);
        componentTypes.add(TransformComponent.class);
        componentTypes.add(SolidComponent.class);
        componentTypes.add(BallComponent.class);

        // brick signature
        Signature brickSig = new Signature(
                ColorTextureComponent.class, // the color or texture of a brick
                HealthComponent.class,       // a brick can be destroyed
                SolidComponent.class,        // a brick can be solid
                TransformComponent.class     // position, size and rotation of a brick
        );

        // paddle signature
        Signature paddleSig = new Signature(
                ColorTextureComponent.class, // the color or texture of the paddle
                TransformComponent.class,    // position, size and rotation of the paddle
                PhysicsComponent.class       // the paddle has a velocity
        );

        // ball signature
        Signature ballSig = new Signature(
                ColorTextureComponent.class, // the color or texture of the ball
                TransformComponent.class,    // position, size and rotation of the ball
                PhysicsComponent.class,      // the ball has a velocity
                BallComponent.class          // the ball has a radius and a stuck state
        );

        // signatures
        var signatures = new HashMap<String, Signature>();
        signatures.put("brickSig", brickSig);
        signatures.put("paddleSig", paddleSig);
        signatures.put("ballSig", ballSig);

        // create ecs manager
        manager = new Manager(new Settings(componentTypes, signatures));

        // load level - create brick entities
        new Level("/level/level.lvl", getEngine(), manager);

        // create render system
        playfieldRenderSystem = new PlayfieldRenderSystem(getEngine(), manager);
        playfieldRenderSystem.init();

        // create paddle update system
        playerUpdateSystem = new PlayerUpdateSystem(getEngine(), manager);

        // create player
        createPlayerEntity();

        // create ball
        createBallEntity();
    }

    @Override
    public void input() {
        if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(getEngine().getWindow().getWindowHandle(), true);
        }
    }

    @Override
    public void update(float dt) {
        manager.update();
        playerUpdateSystem.update(dt);
    }

    @Override
    public void render() {
        playfieldRenderSystem.prepareRendering();
        playfieldRenderSystem.render();
        playfieldRenderSystem.finishRendering();
    }

    @Override
    public void cleanUp() {
        playfieldRenderSystem.cleanUp();
    }

    //-------------------------------------------------
    // Create entities
    //-------------------------------------------------

    private void createPlayerEntity() throws Exception {
        Texture paddleTexture = getEngine().getResourceManager().loadTextureResource("/texture/paddle.png");

        initialPlayerPosition = new Vector2f(
                getEngine().getWindow().getWidth() / 2.0f - PLAYER_SIZE.x / 2.0f,
                getEngine().getWindow().getHeight() - PLAYER_SIZE.y
        );

        var playerEntity = manager.createEntity();

        var colorTextureOpt= manager.addComponent(playerEntity, ColorTextureComponent.class);
        var transformOpt= manager.addComponent(playerEntity, TransformComponent.class);
        var physicsOpt = manager.addComponent(playerEntity, PhysicsComponent.class);

        var colorTexture = colorTextureOpt.get();
        colorTexture.setColor(DEFAULT_COLOR);
        colorTexture.setTexture(paddleTexture);

        var transform = transformOpt.get();
        transform.setPosition(initialPlayerPosition);
        transform.setSize(PLAYER_SIZE);
        transform.setRotation(NO_ROTATION);

        var physics = physicsOpt.get();
        physics.setVelocity(new Vector2f(PLAYER_VELOCITY));
    }

    private void createBallEntity() throws Exception {
        Texture ballTexture = getEngine().getResourceManager().loadTextureResource("/texture/awesomeface.png");

        var initialBallPosition = new Vector2f(initialPlayerPosition).add(PLAYER_SIZE.x / 2.0f - BALL_RADIUS, -BALL_RADIUS * 2.0f);

        var ballEntity = manager.createEntity();

        var colorTextureOpt= manager.addComponent(ballEntity, ColorTextureComponent.class);
        var transformOpt= manager.addComponent(ballEntity, TransformComponent.class);
        var physicsOpt = manager.addComponent(ballEntity, PhysicsComponent.class);
        var ballOpt = manager.addComponent(ballEntity, BallComponent.class);

        var colorTexture = colorTextureOpt.get();
        colorTexture.setColor(DEFAULT_COLOR);
        colorTexture.setTexture(ballTexture);

        var transform = transformOpt.get();
        transform.setPosition(initialBallPosition);
        transform.setSize(new Vector2f(BALL_RADIUS * 2.0f, BALL_RADIUS * 2.0f));
        transform.setRotation(NO_ROTATION);

        var physics = physicsOpt.get();
        physics.setVelocity(BALL_VELOCITY);

        var ball = ballOpt.get();
        ball.setRadius(BALL_RADIUS);
        ball.setStuck(true);
    }
}
