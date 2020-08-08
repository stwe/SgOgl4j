package de.sg.ogl;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public final class Log {

    //-------------------------------------------------
    // Public member
    //-------------------------------------------------

    static public final Logger LOGGER = LogManager.getRootLogger();

    //-------------------------------------------------
    // Ctors.
    //-------------------------------------------------

    private Log() {}
}
