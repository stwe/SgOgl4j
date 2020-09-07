package de.sg.game;

import de.sg.ogl.SgOglEngine;
import de.sg.ogl.ecs.Manager;
import de.sg.ogl.resource.Mesh;
import de.sg.ogl.resource.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.Scanner;

public class Level {

    public static final String TEXTURE_SOLID = "/texture/sgbrick/solid.png";
    public static final String TEXTURE_BLOCK = "/texture/sgbrick/block.png";

    private final SgOglEngine engine;
    private final Manager manager;
    private final Mesh mesh;

    private final Texture solidTexture;
    private final Texture blockTexture;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Level(String path, SgOglEngine engine, Manager manager, Mesh mesh) throws Exception {
        this.engine = engine;
        this.manager = manager;
        this.mesh = mesh;

        this.solidTexture = engine.getResourceManager().loadTextureResource(TEXTURE_SOLID);
        this.blockTexture = engine.getResourceManager().loadTextureResource(TEXTURE_BLOCK);

        load(path);
    }

    //-------------------------------------------------
    // Init / load level data
    //-------------------------------------------------

    private void load(String path) throws Exception {
        var res = loadResource(path);

        try (Scanner sc = new Scanner(new BufferedReader(new FileReader(res)))) {

            var rows = sc.nextInt();
            var columns = sc.nextInt();
            int[][] values = new int[rows][columns];

            sc.nextLine();

            while(sc.hasNextLine()) {
                for (var i = 0; i < values.length; i++) {

                    String[] line = sc.nextLine().trim().split(" ");

                    for (var j = 0; j < line.length; j++) {
                        values[i][j] = Integer.parseInt(line[j]);
                    }
                }
            }

            init(values, rows, columns);
        }
    }

    private void init(int[][] values, int rows, int columns) throws Exception {
        var unitWidth = (float) engine.getWindow().getWidth() / columns;
        var unitHeight = 0.5f * engine.getWindow().getHeight() / rows;

        for (var y = 0; y < rows; y++) {
            for (var x = 0; x < columns; x++) {

                //System.out.print(values[y][x] + " ");

                var position = new Vector2f(unitWidth * x, unitHeight * y);
                var size = new Vector2f(unitWidth, unitHeight);

                if (values[y][x] == 1) {
                    createSolidBrickEntity(position, size);
                }

                if (values[y][x] > 1) {
                    createBrickEntity(position, size, values[y][x]);
                }
            }

            //System.out.println();
        }
    }

    //-------------------------------------------------
    // Create entity
    //-------------------------------------------------

    private void createSolidBrickEntity(Vector2f position, Vector2f size) throws Exception {
        var color = new Vector3f(0.8f, 0.8f, 0.7f);
        var rotation = 0.0f;

        var solidBrick = manager.createEntity();

        var meshOpt = manager.addComponent(solidBrick, MeshComponent.class);
        var colorTextureOpt= manager.addComponent(solidBrick, ColorTextureComponent.class);
        var healthOpt= manager.addComponent(solidBrick, HealthComponent.class);
        var solidOpt= manager.addComponent(solidBrick, SolidComponent.class);
        var transformOpt= manager.addComponent(solidBrick, TransformComponent.class);
        var aabbOpt = manager.addComponent(solidBrick, AabbComponent.class);

        var mesh = meshOpt.orElseThrow();
        mesh.setMesh(this.mesh);

        var colorTexture = colorTextureOpt.orElseThrow();
        colorTexture.setColor(color);
        colorTexture.setTexture(solidTexture);

        var health = healthOpt.orElseThrow();
        health.setDestroyed(false);

        var solid = solidOpt.orElseThrow();
        solid.setSolid(true);

        var transform = transformOpt.orElseThrow();
        transform.setPosition(position);
        transform.setSize(size);
        transform.setRotation(rotation);

        var aabb = aabbOpt.orElseThrow();
        aabb.setMin(new Vector2f(position));
        aabb.setMax(new Vector2f(position).add(size));
    }

    private void createBrickEntity(Vector2f position, Vector2f size, int levelValue) throws Exception {
        Vector3f color;
        var rotation = 0.0f;

        switch (levelValue) {
            case 2 : color = new Vector3f(0.2f, 0.6f, 1.0f); break;
            case 3 : color = new Vector3f(0.0f, 0.7f, 0.0f); break;
            case 4 : color = new Vector3f(0.8f, 0.8f, 0.4f); break;
            case 5 : color = new Vector3f(1.0f, 0.5f, 0.0f); break;
            default: color = new Vector3f(1.0f);
        }

        var brick = manager.createEntity();

        var meshOpt = manager.addComponent(brick, MeshComponent.class);
        var colorTextureOpt= manager.addComponent(brick, ColorTextureComponent.class);
        var healthOpt= manager.addComponent(brick, HealthComponent.class);
        var solidOpt= manager.addComponent(brick, SolidComponent.class);
        var transformOpt= manager.addComponent(brick, TransformComponent.class);
        var aabbOpt = manager.addComponent(brick, AabbComponent.class);

        var mesh = meshOpt.orElseThrow();
        mesh.setMesh(this.mesh);

        var colorTexture = colorTextureOpt.orElseThrow();
        colorTexture.setColor(color);
        colorTexture.setTexture(blockTexture);

        var health = healthOpt.orElseThrow();
        health.setDestroyed(false);

        var solid = solidOpt.orElseThrow();
        solid.setSolid(false);

        var transform = transformOpt.orElseThrow();
        transform.setPosition(position);
        transform.setSize(size);
        transform.setRotation(rotation);

        var aabb = aabbOpt.orElseThrow();
        aabb.setMin(new Vector2f(position));
        aabb.setMax(new Vector2f(position).add(size));
    }

    //-------------------------------------------------
    // Get resource
    //-------------------------------------------------

    private String loadResource(String resource) throws FileNotFoundException {
        return loadResourceByUrl(getClass().getResource(resource), resource);
    }

    private String loadResourceByUrl(URL url, String resource) throws FileNotFoundException {
        if (url != null) {
            return url.getPath().replaceFirst("^/(.:/)", "$1");
        } else {
            throw new FileNotFoundException("Resource " + resource + " not found.");
        }
    }
}
