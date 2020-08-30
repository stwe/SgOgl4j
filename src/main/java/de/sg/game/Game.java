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

    private Manager manager;

    private PlayfieldRenderSystem playfieldRenderSystem;
    private PlayerUpdateSystem playerUpdateSystem;

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

        // signatures
        var signatures = new HashMap<String, Signature>();
        signatures.put("brickSig", brickSig);
        signatures.put("paddleSig", paddleSig);

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
    // Create entity
    //-------------------------------------------------

    private void createPlayerEntity() throws Exception {
        var size = new Vector2f(100.0f, 20.0f);
        var color = new Vector3f(1.0f);
        var velocity = 500.0f;

        Texture paddleTexture = getEngine().getResourceManager().loadTextureResource("/texture/paddle.png");

        var position = new Vector2f(
                getEngine().getWindow().getWidth() / 2.0f - size.x / 2.0f,
                getEngine().getWindow().getHeight() - size.y
        );

        var rotation = 0.0f;

        var playerEntity = manager.createEntity();

        var colorTextureOpt= manager.addComponent(playerEntity, ColorTextureComponent.class);
        var transformOpt= manager.addComponent(playerEntity, TransformComponent.class);
        var physicsOpt = manager.addComponent(playerEntity, PhysicsComponent.class);

        var colorTexture = colorTextureOpt.get();
        colorTexture.setColor(color);
        colorTexture.setTexture(paddleTexture);

        var transform = transformOpt.get();
        transform.setPosition(position);
        transform.setSize(size);
        transform.setRotation(rotation);

        var physics = physicsOpt.get();
        physics.setVelocity(velocity);
    }
}
