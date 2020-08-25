package de.sg.ogl;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import static de.sg.ogl.Log.LOGGER;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL32.GL_FIRST_VERTEX_CONVENTION;
import static org.lwjgl.opengl.GL32.glProvokingVertex;

public final class OpenGL {

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    private OpenGL() {}

    //-------------------------------------------------
    // Init
    //-------------------------------------------------

    public static void init(boolean enableDepthAndStencilTesting) {
        LOGGER.debug("Initializing OpenGL.");

        GL.createCapabilities();
        printContextInitInfo();

        if (enableDepthAndStencilTesting) {
            enableDepthAndStencilTesting();
        }

        // enabled by default on some drivers, but not all so always enable to make sure
        glEnable(GL_MULTISAMPLE);
    }

    //-------------------------------------------------
    // OpenGL states
    //-------------------------------------------------

    public static void setClearColor(float r, float g, float b, float a) {
        glClearColor(r, g, b, a);
    }

    public static void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    public static void enableDepthAndStencilTesting() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);
        glProvokingVertex(GL_FIRST_VERTEX_CONVENTION);
    }

    public static void enableAlphaBlending() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void disableBlending() {
        glDisable(GL_BLEND);
    }

    //-------------------------------------------------
    // Helper
    //-------------------------------------------------

    private static void printContextInitInfo() {
        LOGGER.info("OpenGL version: {}", GL11.glGetString(GL11.GL_VERSION));
        LOGGER.info("GLSL version: {}", GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));
        LOGGER.info("Vendor: {}", GL11.glGetString(GL11.GL_VENDOR));
        LOGGER.info("Renderer: {}", GL11.glGetString(GL11.GL_RENDERER));
    }
}
