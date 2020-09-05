/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.ecs;

import de.sg.ogl.SgOglRuntimeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static de.sg.ogl.Log.LOGGER;

public class Settings {

    /**
     * Stores component class objects.
     */
    private final ArrayList<Class<?>> componentTypes;

    /**
     * Stores signatures.
     */
    private final HashMap<String, Signature> signatures;

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    public Settings(ArrayList<Class<?>> componentTypes, HashMap<String, Signature> signatures) {
        LOGGER.debug("Creates Settings object.");

        this.componentTypes = Objects.requireNonNull(componentTypes, "componentTypes must not be null");
        this.signatures = signatures;

        if (componentTypes.isEmpty()) {
            throw new SgOglRuntimeException("Components missing");
        }

        LOGGER.debug("{} components can be used.", componentTypes.size());

        if (!signatures.isEmpty()) {
            LOGGER.debug("Initialize {} signatures.", signatures.size());

            for (var signature : this.signatures.values()) {
                signature.initBitset(componentTypes);
            }
        } else {
            LOGGER.warn("There are no predefined Signatures.");
        }
    }

    public Settings(ArrayList<Class<?>> componentTypes) {
        this(componentTypes, null);
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    int getComponentIdByType(Class<?> componentType) {
        return componentTypes.indexOf(componentType);
    }

    Signature getSignatureByKey(String signatureKey) {
        return signatures.get(signatureKey);
    }

    ArrayList<Class<?>> getComponentTypes() {
        return componentTypes;
    }

    HashMap<String, Signature> getSignatures() {
        return signatures;
    }
}
