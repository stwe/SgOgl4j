package de.sg.ogl.event;

import java.util.EventListener;

public interface GuiButtonListener extends EventListener {

    void onClick(GuiButtonEvent event);
    void onHover(GuiButtonEvent event);
}
