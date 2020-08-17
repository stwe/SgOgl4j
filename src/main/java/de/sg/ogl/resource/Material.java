package de.sg.ogl.resource;

import org.joml.Vector3f;

public class Material {

    //-------------------------------------------------
    // Public member
    //-------------------------------------------------

    /**
     * The material name statement.
     */
    String newmtl;

    /**
     * The ambient color of the material.
     */
    Vector3f ka = new Vector3f(0.0f, 0.0f, 0.0f);

    /**
     * The diffuse color of the material.
     */
    Vector3f kd = new Vector3f(0.0f, 0.0f, 0.0f);

    /**
     * The specular color of the material.
     */
    Vector3f ks = new Vector3f(0.0f, 0.0f, 0.0f);

    /**
     * Can be a number from 0 to 10 which represents various material lighting and shading effects.
     * illum = 1 indicates a flat material with no specular highlights, so the value of Ks is not used.
     * illum = 2 denotes the presence of specular highlights, and so a specification for Ks is required.
     */
    int illum = 2;

    /**
     * Specifies the specular exponent (shininess) for the material.
     * Ns values normally range from 0 to 1000.
     */
    float ns = 0.0f;

    /**
     * The transparency of the material.
     * A factor of 1.0 is fully opaque.
     * A factor of 0.0 is fully dissolved (completely transparent).
     */
    float d = 1.0f;

    /**
     * The ambientmap texture Id.
     */
    int mapKa = 0;

    /**
     * The diffusemap texture Id.
     * Most of time, it will be the same as ambient texture map.
     */
    int mapKd = 0;

    /**
     * The specularmap texture Id.
     */
    int mapKs = 0;

    /**
     * The bumpmap texture Id.
     */
    int mapBump = 0;

    /**
     * The normalmap texture Id.
     */
    int mapKn = 0;

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    boolean hasAmbientMap() {
        return mapKa != 0;
    }

    boolean hasDiffuseMap() {
        return mapKd != 0;
    }

    boolean hasSpecularMap() {
        return mapKs != 0;
    }

    boolean hasBumpMap() {
        return mapBump != 0;
    }

    boolean hasNormalMap() {
        return mapKn != 0;
    }
}
