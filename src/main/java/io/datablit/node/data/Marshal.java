package org.conf2.data;

import org.conf2.schema.MetaUtil;

/**
 *
 */
public class Marshal {
    public static Node container(Reflect reflect, Object o) {
        MyNode n = new MyNode();
        n.onSelect = (ContainerRequest r) -> {
            Reflect p = reflect.property(o, r.meta);
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
            Reflect p = reflect.property(o, r.meta);
            p.setValue(o, v);
        };
        n.onRead = (FieldRequest r) -> {
            Reflect p = reflect.property(o, r.meta);
            return p.getValue(o);
        };
        return n;
    }
}

