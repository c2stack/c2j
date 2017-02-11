package org.c2stack.node;

import org.c2stack.meta.MetaUtil;

/**
 *
 */
public class Marshal {
    public static Node container(Reflect reflect, Object o) {
        MyNode n = new MyNode();
        n.onSelect = (ContainerRequest r) -> {
            Reflect p = reflect.property(r.meta);
            Object child;
            if (r.create) {
                child = p.newInstance();
                p.set(o, child);
            } else {
                child = p.get(o);
            }
            if (child == null) {
                return null;
            }
            if (MetaUtil.isList(r.meta)) {
                return new MarshalList().node(p, child);
            }

            return container(p, child);
        };
        n.onWrite = (FieldRequest r, Value v) -> {
            Reflect p = reflect.property(r.meta);
            p.setValue(o, v);
        };
        n.onRead = (FieldRequest r) -> {
            Reflect p = reflect.property(r.meta);
            return p.getValue(o);
        };
        return n;
    }
}

