package org.conf2.data;

/**
 *
 */
public enum EventType {
    NEW(1),

    START_TREE_EDIT(2),
    END_TREE_EDIT(3),

    LEAVE_EDIT(4),
    LEAVE(5),
    DELETE(6);

    private int id;

    EventType(int id) {
        this.id = id;
    }

    public Event create() {
        Event e = new Event();
        e.type = this;
        return e;
    }

    public boolean isBubble() {
        return this == START_TREE_EDIT || this == END_TREE_EDIT;
    }
}
