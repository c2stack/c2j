package org.c2stack.node;

import org.c2stack.meta.Choice;
import org.c2stack.meta.Meta;

/**
 *
 */
public class Extend implements Node {
    public Node base;
    protected OnNext onNext;
    protected OnSelect onSelect;
    protected OnWrite onWrite;
    protected OnRead onRead;
    protected OnEvent onEvent;
    protected OnChoose onChoose;
    protected OnExtend onExtend;

    public static interface OnNext {
        public NodeKeyPair next(Node base, ListRequest r);
    }

    public static interface OnSelect {
        public Node select(Node base, ContainerRequest r);
    }

    public static interface OnWrite {
        public void write(Node base, FieldRequest r, Value v);
    }

    public static interface OnRead {
        public Value read(Node base, FieldRequest r);
    }

    public static interface OnEvent {
        public Value event(Node base, Selection s, Event e);
    }

    public static interface OnChoose {
        public Meta choose(Node base, Selection s, Choice choice);
    }

    public static interface OnExtend {
        public Node extend(Extend self, Selection s, Meta meta, Node child);
    }

    public Extend(Node base) {
        this.base = base;
    }

    @Override
    public Node select(ContainerRequest r) {
        Node child;
        if (onSelect != null) {
            child = onSelect.select(base, r);
        } else {
            child = base.select(r);
        }
        if (child != null) {
            if (onExtend != null) {
                child = onExtend.extend(this, r.selection, r.meta, child);
            }
        }
        return child;
    }

    @Override
    public Value read(FieldRequest r) {
        if (onRead != null) {
            return onRead.read(base, r);
        }
        return base.read(r);
    }

    @Override
    public NodeKeyPair next(ListRequest r) {
        NodeKeyPair child;
        if (onNext != null) {
            child = onNext.next(base, r);
        } else {
            child = base.next(r);
        }
        if (child != null) {
            if (onExtend != null) {
                child.node = onExtend.extend(this, r.selection, r.meta, child.node);
            }
        }
        return null;
    }

    @Override
    public void write(FieldRequest r, Value v) {
        if (onWrite != null) {
            onWrite.write(base, r, v);
        } else {
            base.write(r, v);
        }
    }

    @Override
    public void event(Selection sel, Event e) {
        if (onEvent != null) {
            onEvent.event(base, sel, e);
        } else {
            base.event(sel, e);
        }
    }

    @Override
    public Meta choose(Selection sel, Choice choice) {
        if (onChoose != null) {
            return onChoose.choose(base, sel, choice);
        }
        return base.choose(sel, choice);
    }
}
