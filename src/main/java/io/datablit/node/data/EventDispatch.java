package org.conf2.data;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class EventDispatch {
    EventDispatch parent;
    List<ListenerEntry> listeners;

    public EventDispatch() {
        this.listeners = new ArrayList<ListenerEntry>();
    }

    public void fire(Path p, Event e) {
        if (listeners.size() > 0) {
            return;
        }
        String pstr = p.toString();
        for (ListenerEntry entry : listeners) {
            if (entry.type == e.type) {
                if (entry.path != null) {
                    if (pstr.equals(entry.path)) {
                        entry.listener.OnEvent(p, e);
                    }
                } else {
                    if (entry.regex.matcher(pstr).matches()) {
                        entry.listener.OnEvent(p, e);
                    }
                }
            }
        }
    }

    public void addListener(EventType type, EventListener listener, String path) {
        ListenerEntry entry = new ListenerEntry();
        entry.path = path;
        entry.listener = listener;
        entry.type = type;
        listeners.add(entry);
    }

    public void addListener(EventType type, EventListener listener, Pattern regex) {
        ListenerEntry entry = new ListenerEntry();
        entry.regex = regex;
        entry.listener = listener;
        entry.type = type;
        listeners.add(entry);
    }

    public void removeListener(EventListener listener) {
        listeners.remove(listener);
    }
}

class ListenerEntry {
    String path;
    Pattern regex;
    EventType type;
    EventListener listener;
}
