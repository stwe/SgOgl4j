/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.islands;

import java.io.IOException;

public interface FileInterface {
    void readChunksFromFile() throws IOException;
    void readDataFromChunks();
}
