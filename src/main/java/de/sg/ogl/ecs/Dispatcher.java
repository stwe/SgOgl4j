/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.ecs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static de.sg.ogl.Log.LOGGER;

public class Dispatcher {

    private final ConcurrentHashMap<
                Class<? extends Event>,         // each type of Event (key)
                List<Listener<? extends Event>> // can even have more than one Listener (List as value)
                > listenerMap = new ConcurrentHashMap<>();

    //-------------------------------------------------
    // Dispatch
    //-------------------------------------------------

    @SuppressWarnings("unchecked")
    public <T extends Event> T dispatch(T event) {
        var listenerList = listenerMap.get(event.getClass());

        if (listenerList != null) {
            for (Listener listener : listenerList) {
                listener.invoke(event);
            }
        } else {
            LOGGER.warn("No registered Listeners exists.");
        }

        // returns the Event after running all Listener
        return event;
    }

    //-------------------------------------------------
    // Getter
    //-------------------------------------------------

    public ConcurrentHashMap<Class<? extends Event>, List<Listener<? extends Event>>> getListenerMap() {
        return listenerMap;
    }

    //-------------------------------------------------
    // Add && remove
    //-------------------------------------------------

    public void addListener(Class<? extends Event> event, Listener<? extends Event> listener) {
        var list = listenerMap.computeIfAbsent(event, x -> new ArrayList<>());
        if (list.isEmpty()) {
            list.add(listener);
        } else {
            for (var entry : list) {
                if (entry.getClass() != listener.getClass()) {
                    list.add(listener);
                } else {
                    LOGGER.warn("A Listener of type {} is already registered to the Event.", entry.getClass());
                }
            }
        }
    }
}
