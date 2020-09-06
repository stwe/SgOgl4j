package de.sg.game;

import de.sg.ogl.BaseApplication;
import de.sg.ogl.Input;
import de.sg.ogl.buffer.BufferLayout;
import de.sg.ogl.buffer.VertexAttribute;
import de.sg.ogl.ecs.Dispatcher;
import de.sg.ogl.ecs.Manager;
import de.sg.ogl.ecs.Settings;
import de.sg.ogl.ecs.Signature;
import de.sg.ogl.resource.Mesh;
import de.sg.ogl.resource.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static de.sg.ogl.buffer.VertexAttribute.VertexAttributeType.POSITION_2D;
import static de.sg.ogl.buffer.VertexAttribute.VertexAttributeType.UV;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Game extends BaseApplication {

    //-------------------------------------------------
    // Const
    //-------------------------------------------------

    private static final Vector3f DEFAULT_COLOR = new Vector3f(1.0f);
    private static final float NO_ROTATION = 0.0f;

    private static final Vector2f PLAYER_SIZE = new Vector2f(100.0f, 20.0f);
    private static final float PLAYER_VELOCITY = 500.0f;

    private static final float BALL_RADIUS = 12.5f;
    private static final Vector2f BALL_VELOCITY = new Vector2f(100.0f, -350.0f);

    public static final String BRICK_SIGNATURE = "BRICK_SIGNATURE";

    //-------------------------------------------------
    // Private member
    //-------------------------------------------------

    private Manager manager;
    private Dispatcher dispatcher;

    private Vector2f initialPlayerPosition;

    private Mesh mesh;

    private SpriteRenderSystem spriteRenderSystem;
    private UpdatePlayerSystem updatePlayerSystem;
    private UpdateBallSystem updateBallSystem;

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
        // create Mesh
        createMesh();

        // init Ecs / Dispatcher
        initEcs();

        // load level - create brick entities
        new Level("/level/level.lvl", getEngine(), manager);

        // create player
        createPlayerEntity();

        // create ball
        createBallEntity();

        // create sprite render system
        spriteRenderSystem = new SpriteRenderSystem(getEngine(), manager, mesh);
        spriteRenderSystem.init();

        // create update system for the player
        updatePlayerSystem = new UpdatePlayerSystem(getEngine(), manager, dispatcher);
        updatePlayerSystem.init();

        // create update system for the ball
        updateBallSystem = new UpdateBallSystem(getEngine(), manager);
        updateBallSystem.init();

        // the UpdateBallSystem listen to UpdatePlayerEvents
        dispatcher.addListener(UpdatePlayerEvent.class, updateBallSystem);
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

        spriteRenderSystem.update(dt);
        updatePlayerSystem.update(dt);
        updateBallSystem.update(dt);
    }

    @Override
    public void render() {
        spriteRenderSystem.prepareRendering();
        spriteRenderSystem.render();
        spriteRenderSystem.finishRendering();

        updatePlayerSystem.render();
        updateBallSystem.render();
    }

    @Override
    public void cleanUp() {
        mesh.cleanUp();

        spriteRenderSystem.cleanUp();
        updatePlayerSystem.cleanUp();
        updateBallSystem.cleanUp();
    }

    //-------------------------------------------------
    // Init Ecs
    //-------------------------------------------------

    void createMesh() {
        float[] vertices = new float[] {
                // pos      // tex
                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f,

                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 0.0f
        };

        BufferLayout bufferLayout = new BufferLayout(
                new ArrayList<>(){{
                    add(new VertexAttribute(POSITION_2D, "aPosition"));
                    add(new VertexAttribute(UV, "aUv"));
                }}
        );

        mesh = new Mesh();
        mesh.getVao().addVerticesVbo(vertices, 6, bufferLayout);
    }

    void initEcs() throws Exception {
        // components
        ArrayList<Class<?>> componentTypes = new ArrayList<>();
        componentTypes.add(MeshComponent.class);
        componentTypes.add(ColorTextureComponent.class);
        componentTypes.add(HealthComponent.class);
        componentTypes.add(PhysicsComponent.class);
        componentTypes.add(TransformComponent.class);
        componentTypes.add(SolidComponent.class);
        componentTypes.add(BallComponent.class);
        componentTypes.add(PlayerComponent.class);
        componentTypes.add(AabbComponent.class);

        // brick signature
        Signature brickSig = new Signature(
                MeshComponent.class,         // the Mesh of the brick
                ColorTextureComponent.class, // the color or texture of a brick
                HealthComponent.class,       // a brick can be destroyed
                SolidComponent.class,        // a brick can be solid
                TransformComponent.class,    // position, size and rotation of a brick
                AabbComponent.class          // the brick has an Aabb
        );

        // signatures
        var signatures = new HashMap<String, Signature>();
        signatures.put(BRICK_SIGNATURE, brickSig);

        // create ecs manager
        manager = new Manager(new Settings(componentTypes, signatures));

        // create dispatcher
        dispatcher = new Dispatcher();
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

        var meshOpt = manager.addComponent(playerEntity, MeshComponent.class);
        var colorTextureOpt= manager.addComponent(playerEntity, ColorTextureComponent.class);
        var transformOpt= manager.addComponent(playerEntity, TransformComponent.class);
        var physicsOpt = manager.addComponent(playerEntity, PhysicsComponent.class);
        manager.addComponent(playerEntity, PlayerComponent.class);
        var aabbOpt = manager.addComponent(playerEntity, AabbComponent.class);

        var mesh = meshOpt.orElseThrow();
        mesh.setMesh(this.mesh);

        var colorTexture = colorTextureOpt.orElseThrow();
        colorTexture.setColor(DEFAULT_COLOR);
        colorTexture.setTexture(paddleTexture);

        var transform = transformOpt.orElseThrow();
        transform.setPosition(initialPlayerPosition);
        transform.setSize(PLAYER_SIZE);
        transform.setRotation(NO_ROTATION);

        var physics = physicsOpt.orElseThrow();
        physics.setVelocity(new Vector2f(PLAYER_VELOCITY));

        var aabb = aabbOpt.orElseThrow();
        aabb.setMin(new Vector2f(initialPlayerPosition));
        aabb.setMax(new Vector2f(initialPlayerPosition).add(PLAYER_SIZE));
    }

    private void createBallEntity() throws Exception {
        Texture ballTexture = getEngine().getResourceManager().loadTextureResource("/texture/awesomeface.png");

        var initialBallPosition = new Vector2f(initialPlayerPosition).add(PLAYER_SIZE.x / 2.0f - BALL_RADIUS, -BALL_RADIUS * 2.0f);

        var ballEntity = manager.createEntity();

        var meshOpt = manager.addComponent(ballEntity, MeshComponent.class);
        var colorTextureOpt= manager.addComponent(ballEntity, ColorTextureComponent.class);
        var transformOpt= manager.addComponent(ballEntity, TransformComponent.class);
        var physicsOpt = manager.addComponent(ballEntity, PhysicsComponent.class);
        var ballOpt = manager.addComponent(ballEntity, BallComponent.class);
        var aabbOpt = manager.addComponent(ballEntity, AabbComponent.class);

        var mesh = meshOpt.orElseThrow();
        mesh.setMesh(this.mesh);

        var colorTexture = colorTextureOpt.orElseThrow();
        colorTexture.setColor(DEFAULT_COLOR);
        colorTexture.setTexture(ballTexture);

        var transform = transformOpt.orElseThrow();
        transform.setPosition(initialBallPosition);
        transform.setSize(new Vector2f(BALL_RADIUS * 2.0f, BALL_RADIUS * 2.0f));
        transform.setRotation(NO_ROTATION);

        var physics = physicsOpt.orElseThrow();
        physics.setVelocity(BALL_VELOCITY);

        var ball = ballOpt.orElseThrow();
        ball.setRadius(BALL_RADIUS);
        ball.setStuck(true);

        var aabb = aabbOpt.orElseThrow();
        aabb.setMin(new Vector2f(initialBallPosition));
        aabb.setMax(new Vector2f(initialBallPosition).add(new Vector2f(BALL_RADIUS * 2.0f, BALL_RADIUS * 2.0f)));
    }
}
