package de.sg.sandbox;

import de.sg.ogl.SgOglEngine;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Level {

    private final ArrayList<GameObject> bricks = new ArrayList<>();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Level(String path, SgOglEngine engine) throws FileNotFoundException {
        load(path, engine);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public ArrayList<GameObject> getBricks() {
        return bricks;
    }

    //-------------------------------------------------
    // Logic
    //-------------------------------------------------

    public void render(SpriteRenderer renderer) {
        for (var brick : bricks) {
            if (!brick.isDestroyed()) {
                brick.render(renderer);
            }
        }
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    private void load(String path, SgOglEngine engine) throws FileNotFoundException {
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

            init(values, rows, columns, engine);
        }
    }

    private void init(int[][] values, int rows, int columns, SgOglEngine engine) throws FileNotFoundException {
        var unitWidth = (float) engine.getWindow().getWidth() / columns;
        var unitHeight = 0.5f * engine.getWindow().getHeight() / rows;

        var solidTexture = engine.getResourceManager().loadTextureResource("/texture/block_solid.png");
        var blockTexture = engine.getResourceManager().loadTextureResource("/texture/block.png");

        for (var y = 0; y < rows; y++) {
            for (var x = 0; x < columns; x++) {

                //System.out.print(values[y][x] + " ");

                var position = new Vector2f(unitWidth * x, unitHeight * y);
                var size = new Vector2f(unitWidth, unitHeight);

                if (values[y][x] == 1) {
                    var gameObject = new GameObject(position, size, solidTexture, new Vector3f(0.8f, 0.8f, 0.7f));
                    gameObject.setSolid(true);
                     bricks.add(gameObject);
                }

                if (values[y][x] > 1) {
                    var color = new Vector3f(1.0f);

                    if (values[y][x] == 2) {
                        color = new Vector3f(0.2f, 0.6f, 1.0f);
                    }

                    if (values[y][x] == 3) {
                        color = new Vector3f(0.0f, 0.7f, 0.0f);
                    }

                    if (values[y][x] == 4) {
                        color = new Vector3f(0.8f, 0.8f, 0.4f);
                    }

                    if (values[y][x] == 5) {
                        color = new Vector3f(1.0f, 0.5f, 0.0f);
                    }

                    var gameObject = new GameObject(position, size, blockTexture, color);
                    bricks.add(gameObject);
                }
            }

            //System.out.println();
        }
    }

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
