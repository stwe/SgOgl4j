/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.ecs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

public class Signature {

    /**
     * A BitSet representing the required components.
     */
    private final BitSet bitSet = new BitSet();

    /**
     * Stores the components required to initialize the BitSet.
     */
    private final ArrayList<Class<?>> signatureComponentTypes = new ArrayList<>();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Signature(Class<?>... componentTypes) {
        signatureComponentTypes.addAll(Arrays.asList(componentTypes));
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    BitSet getBitSet() {
        return bitSet;
    }

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    void initBitset(ArrayList<Class<?>> allComponentTypes) {
        for (var type : signatureComponentTypes) {
            bitSet.set(allComponentTypes.indexOf(type));
        }
    }
}
