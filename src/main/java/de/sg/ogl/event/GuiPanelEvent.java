package de.sg.ogl.event;

import de.sg.ogl.gui.GuiPanel;

import java.util.EventObject;

public class GuiPanelEvent extends EventObject {

    public GuiPanelEvent(GuiPanel source) {
        super(source);
    }
}
