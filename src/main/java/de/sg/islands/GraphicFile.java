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

public class GraphicFile {

    private static final int START_INDEX = 101;
    private static final int END_INDEX = 2822;

    private Map<Integer, Integer> graphicMap = new HashMap<>();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public GraphicFile(String filePath) throws IOException {
        loadFile(Util.loadResource(Objects.requireNonNull(filePath, "filePath must not be null")));
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public int getGraphicIndexByDevId(int developmentId) {
        if (developmentId < START_INDEX || developmentId > END_INDEX) {
            throw new RuntimeException("Invalid developmentId: " + developmentId);
        }

        return Optional.ofNullable(graphicMap.get(developmentId))
                .orElseThrow( () -> new NoSuchElementException("Element " + developmentId + " not found") );
    }

    //-------------------------------------------------
    // Load
    //-------------------------------------------------

    private void loadFile(String fileUrl) throws IOException {
        var br = new BufferedReader(new FileReader(Objects.requireNonNull(fileUrl, "filePath must not be null")));

        LOGGER.debug("Start reading graphic index from {}...", fileUrl);

        String line;
        while ((line = br.readLine()) != null) {
            if (!line.isEmpty()) {
                String[] values = line.split(" ");
                var id = Integer.parseInt(values[0]);
                var graphicNumber = Integer.parseInt(values[1]);
                graphicMap.put(id, graphicNumber);
            }
        }

        LOGGER.debug("Graphic index read successfully. {} entries were found", graphicMap.size());
    }
}
