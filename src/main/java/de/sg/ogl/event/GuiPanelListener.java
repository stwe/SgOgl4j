package de.sg.ogl.event;

import java.util.EventListener;

public interface GuiPanelListener extends EventListener {

    void onClick(GuiPanelEvent event);
    void onHover(GuiPanelEvent event);
    void onRelease(GuiPanelEvent event);
}
