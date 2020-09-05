/*
 * This file is part of the SgOgl4j project.
 *
 * Copyright (c) 2020. stwe <https://github.com/stwe/SgOgl4j>
 *
 * License: MIT
 */

package de.sg.ogl.ecs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public abstract class System {
    ArrayList<Listener> listeners = new ArrayList<>();

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void emit(Event event) throws InvocationTargetException, IllegalAccessException {
        for (var listener : listeners) {
            var eventClass = event.getClass().getName();
            boolean bb = false;
            if (eventClass == listener.getEventType().getName()) {
                bb = true;
            }
            if (bb) {
                Method method = null;
                try {
                    method = listener.getClass().getDeclaredMethod(listener.getName(), listener.getEventType());
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

                if (method != null) // todo listener -> über alle events nicht umgedreht
                    method.invoke(listener, event);
            }
        }
    }

    public abstract void update(float dt) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}
