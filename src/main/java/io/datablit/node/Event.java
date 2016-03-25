package io.datablit.node;

import java.util.Map;

/**
 *
 */
public class Event {
    EventType type;
    Map details;
    public boolean propagationStopped;

    public EventType getType() {
        return type;
    }

    public Map getDetails() {
        return details;
    }
}