package de.sg.ogl;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public final class Log {

    private Log() {}

    public static final Logger LOGGER = LogManager.getRootLogger();
}
