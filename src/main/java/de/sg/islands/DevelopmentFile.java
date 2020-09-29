/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.islands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static de.sg.ogl.Log.LOGGER;

public class DevelopmentFile {

    private static final int START_INDEX = 101;
    private static final int END_INDEX = 2822;

    /*
        Map<Integer, Info> infoMap = new HashMap<>();

               _________ ID                                                                     - the key in the map
              / ________ width                                                                  | -- \
             / / _______ height                                                                 |    |
            / / / ______ directions (1 or 4)                                                    |    |
           / / / / _____ animation steps                                                        |    class Info
          / / / / / ____ base height (0 or 1)                                                   |    |
         / / / / / / ___ construction height (0 or 1)                                           |    /
        / / / / / / / __ stocks                                                                 | --
       / / / / / / / / _ category (0 = NONE, 1 = TERRAIN, 2 = FIELD, 3 = HOUSE, 4 = BUSINESS) - | - enum Category
      / / / / / / / / /                                                                         |
      | | | | | | | | |                                                                         |
    101 1 1 4 1 1 0 1 1 gras0_gruen                                                           - name in class Info
    */

    private enum Category {
        NONE(0),
        TERRAIN(1),
        FIELD(2),
        HOUSE(3),
        BUSINESS(4);

        private static final Map<Integer, Category> typesByValue = new HashMap<>();

        static {
            for (var type : Category.values()) {
                typesByValue.put(type.value, type);
            }
        }

        private final int value;

        Category(int value) {
            this.value = value;
        }

        public static Category forValue(int value) {
            return typesByValue.get(value);
        }
    }

    public static class Info {
        public int width;              // width of the area or building
        public int height;             // height of the area or building
        public int directions;         // 1 or 4
        public int animationSteps;
        public int baseHeight;         // 0 or 1
        public int constructionHeight; // 0 or 1
        public int stocks;
        public Category category;
        public String name;

        public Info(
                int width,
                int height,
                int directions,
                int animationSteps,
                int baseHeight,
                int constructionHeight,
                int stocks,
                Category category,
                String name
        ) {
            this.width = width;
            this.height = height;
            this.directions = directions;
            this.animationSteps = animationSteps;
            this.baseHeight = baseHeight;
            this.constructionHeight = constructionHeight;
            this.stocks = stocks;
            this.category = category;
            this.name = name;
        }
    }

    private final Map<Integer, Info> infoMap = new HashMap<>();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public DevelopmentFile(String filePath) throws IOException {
        LOGGER.debug("Creates DevelopmentFile object.");

        loadFile(Util.loadResource(Objects.requireNonNull(filePath, "filePath must not be null")));
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public Info getMeta(int index) {
        if (index < START_INDEX || index > END_INDEX) {
            throw new RuntimeException("Invalid index: " + index);
        }

        return Optional.ofNullable(infoMap.get(index))
                .orElseThrow( () -> new NoSuchElementException("Element " + index + " not found") );
    }

    //-------------------------------------------------
    // Load
    //-------------------------------------------------

    private void loadFile(String fileUrl) throws IOException {
        var br = new BufferedReader(new FileReader(Objects.requireNonNull(fileUrl, "filePath must not be null")));

        String line;
        while ((line = br.readLine()) != null) {
            if (!line.isEmpty() && line.charAt(0) != ';') {
                String[] values = line.split(" ");
                var id = Integer.parseInt(values[0]);
                infoMap.put(id, new Info(                               // id as key
                        Integer.parseInt(values[1]),                    // width
                        Integer.parseInt(values[2]),                    // height
                        Integer.parseInt(values[3]),                    // directions
                        Integer.parseInt(values[4]),                    // animationSteps
                        Integer.parseInt(values[5]),                    // baseHeight
                        Integer.parseInt(values[6]),                    // constructionHeight
                        Integer.parseInt(values[7]),                    // stocks
                        Category.forValue(Integer.parseInt(values[8])), // category
                        values[9]                                       // name
                ));
            }
        }
    }
}
