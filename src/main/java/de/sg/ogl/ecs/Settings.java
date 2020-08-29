package de.sg.ogl.ecs;

import java.util.ArrayList;
import java.util.HashMap;

public class Settings {

    private final ArrayList<Class<?>> componentTypes;
    private final HashMap<String, Signature> signatures;

    public Settings(ArrayList<Class<?>> componentTypes, HashMap<String, Signature> signatures) {
        this.componentTypes = componentTypes;
        this.signatures = signatures;

        for (var signature : this.signatures.values()) {
            signature.initBitset(componentTypes);
        }
    }

    int getComponentId(Class<?> componentType) {
        return componentTypes.indexOf(componentType);
    }

    Signature getSignature(String signatureId) {
        return signatures.get(signatureId);
    }

    ArrayList<Class<?>> getComponentTypes() {
        return componentTypes;
    }

    HashMap<String, Signature> getSignatures() {
        return signatures;
    }
}
