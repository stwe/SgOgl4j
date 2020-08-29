package de.sg.ogl.ecs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

public class Signature {

    private final BitSet bitSet = new BitSet();
    private final ArrayList<Class<?>> signatureComponentTypes = new ArrayList<>();

    public Signature(Class<?>... classes) {
        signatureComponentTypes.addAll(Arrays.asList(classes));
    }

    BitSet getBitSet() {
        return bitSet;
    }

    void initBitset(ArrayList<Class<?>> allComponentTypes) {
        for (var signatureType : signatureComponentTypes) {
            bitSet.set(allComponentTypes.indexOf(signatureType));
        }
    }
}
