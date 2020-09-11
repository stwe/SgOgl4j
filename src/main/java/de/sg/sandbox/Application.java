/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.sandbox;

import de.sg.ogl.BaseApplication;
import de.sg.ogl.Input;
import de.sg.ogl.Pixel;
import de.sg.ogl.resource.Texture;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;

public class Application extends BaseApplication {

    private final Vector2i worldSize = new Vector2i(14, 10);
    private final Vector2i tileSize = new Vector2i(40 ,20);
    private final Vector2i origin = new Vector2i(5 ,1); // -> (200, 20)

    private Texture click;
    private Texture empty;
    private Texture coll;
    private Texture sand;
    private Texture tree;
    private Texture water;
    private Texture grass;

    private Pixel[] pixels;

    private SpriteRenderer renderer;

    private final int[] map = new int[worldSize.x * worldSize.y];

    public Application() throws IOException, IllegalAccessException {
    }

    @Override
    public void init() throws Exception {
        empty = getEngine().getResourceManager().loadTextureResource("/texture/tiles/empty.png");
        sand = getEngine().getResourceManager().loadTextureResource("/texture/tiles/sand.png");
        tree = getEngine().getResourceManager().loadTextureResource("/texture/tiles/tree.png");
        water = getEngine().getResourceManager().loadTextureResource("/texture/tiles/water.png");
        grass = getEngine().getResourceManager().loadTextureResource("/texture/tiles/grass.png");

        click = getEngine().getResourceManager().loadTextureResource("/texture/tiles/click.png");

        coll = getEngine().getResourceManager().loadTextureResource("/texture/tiles/coll.png");
        pixels = createPixels();

        renderer = new SpriteRenderer(getEngine());

        Arrays.fill(map, 0);
    }

    @Override
    public void input() {
        if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(getEngine().getWindow().getWindowHandle(), true);
        }
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        renderer.prepareRendering();

        var mx = (int)Input.getCurrentMouseX();
        var my = (int)Input.getCurrentMouseY();

        var cell = new Vector2i(mx / tileSize.x, my / tileSize.y);
        var offset = new Vector2i( mx % tileSize.x, my % tileSize.y);
        var pixel = pixels[offset.y * tileSize.x + offset.x];

        var selected = getSelected(cell);
        if (pixel.equals(Pixel.RED)) selected.x += -1;
        if (pixel.equals(Pixel.BLUE)) selected.y += -1;
        if (pixel.equals(Pixel.GREEN)) selected.y += 1;
        if (pixel.equals(Pixel.YELLOW)) selected.x += 1;

        if (Input.isMouseButtonReleased(0)) {
            if (selected.x >= 0 && selected.x < worldSize.x && selected.y >= 0 && selected.y < worldSize.y) {
                map[selected.y * worldSize.x + selected.x]++;

                if (map[selected.y * worldSize.x + selected.x] > 4) {
                    map[selected.y * worldSize.x + selected.x] = 0;
                }

                System.out.println("Texture: " + map[selected.y * worldSize.x + selected.x]);
            }
            Input.update(0.0f); // reset mouse
        }

        for (int y = 0; y < worldSize.y; y++) {
            for (int x = 0; x < worldSize.x; x++) {

                var world = toScreen(x, y);

                switch (map[y * worldSize.x + x]) {
                    case 0: renderer.render(empty.getId(), new Vector2f(world), 0.0f, new Vector2f(tileSize)); break;
                    case 1: renderer.render(grass.getId(), new Vector2f(world), 0.0f, new Vector2f(tileSize)); break;
                    case 2: renderer.render(sand.getId(), new Vector2f(world), 0.0f, new Vector2f(tileSize)); break;
                    case 3: renderer.render(water.getId(), new Vector2f(world), 0.0f, new Vector2f(tileSize)); break;
                    case 4: renderer.render(tree.getId(), new Vector2f(world.x, world.y - tileSize.y), 0.0f, new Vector2f(tileSize.x, tileSize.y * 2.0f)); break;
                }
            }
        }

        var selectedWorld = toScreen(selected.x, selected.y);
        renderer.render(click.getId(), new Vector2f(selectedWorld.x, selectedWorld.y), 0.0f, new Vector2f(tileSize));

        renderer.finishRendering();
    }

    @Override
    public void cleanUp() {

    }

    private Vector2i toScreen(int x, int y) {
        return new Vector2i(
                (origin.x * tileSize.x) + (x - y) * (tileSize.x / 2),
                (origin.y * tileSize.y) + (x + y) * (tileSize.y / 2)
                );
    }

    private Vector2i getSelected(Vector2i cell) {
        return new Vector2i(
                (cell.y - origin.y) + (cell.x - origin.x),
                (cell.y - origin.y) - (cell.x - origin.x)
        );
    }

    public Pixel[] createPixels() {
        Texture.bind(coll.getId());

        int width = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH);
        int height = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT);
        int colorFormat = glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_INTERNAL_FORMAT);
        int channels = colorFormat == GL_RGBA ? 4 : 3;

        var bb = BufferUtils.createByteBuffer(width * height * channels);
        glGetTexImage(GL_TEXTURE_2D, 0, colorFormat, GL_UNSIGNED_BYTE, bb);

        Pixel[] pixels = new Pixel[width * height];

        for (int i = 0; i < pixels.length; i++) {
            int r = bb.get() & 0xFF;
            int g = bb.get() & 0xFF;
            int b = bb.get() & 0xFF;

            int a = 0xFF;
            if(channels == 4) {
                a = bb.get() & 0xFF;
            }

            pixels[i] = new Pixel(r, g, b, a);
        }

        Texture.unbind();

        return pixels;
    }
}
