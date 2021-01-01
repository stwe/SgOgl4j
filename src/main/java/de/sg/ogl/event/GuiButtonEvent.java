package de.sg.ogl.event;

import de.sg.ogl.gui.GuiButton;

import java.util.EventObject;

public class GuiButtonEvent extends EventObject {

    public GuiButtonEvent(GuiButton source) {
        super(source);
    }
}
