/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.ecs;

import java.util.BitSet;

public class Entity {

    /**
     * The entity Id or index of data in the components storage.
     */
    public int id = 0;

    /**
     * Entities can be either "alive" or "dead".
     */
    public boolean alive = false;

    /**
     * Components of the entity.
     */
    public BitSet bitSet = new BitSet();
}
