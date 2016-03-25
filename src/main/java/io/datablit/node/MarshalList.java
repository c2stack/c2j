package io.datablit.node;

import io.datablit.meta.MetaList;

import java.lang.reflect.Array;
import java.util.List;

/**
 *
 */
public class MarshalList {
    public OnNewItem onNew;
    public OnSelectItem onSelectItem;
    public Node node(Reflect reflect, Object l) {
        MyNode n = new MyNode();
        n.onNext = (ListRequest r) -> {
            Value[] key = r.key;
            Object child = null;
            if (r.create) {
                if (onNew != null) {
                    child = onNew.newItem(key);
                } else {
                    child = reflect.newInstance();
                    // TODO: set key values
                }
            } else {
                if (l instanceof Array) {
                    child = Array.get(l, (int) r.row);
                } else if (l instanceof List) {
                    child = ((List)l).get((int) r.row);
                }
            }
            if (child == null) {
                return null;
            }
            if (key == null && r.meta.getKey() != null) {
                key = new Value[r.meta.getKey().length];
                for (int i = 0; i < key.length; i++) {
                    Reflect keyReflect = reflect.property(r.meta.getKey()[i]);
                    key[i] = keyReflect.getValue(l);
                    if (key[i] == null) {
                        throw new RuntimeException("Could not read key " + r.meta.getKey()[i].getIdent());
                    }
                }
            }
            Node childNode = null;
            if (onSelectItem != null) {
                onSelectItem.selectItem(child);
            } else {
                childNode = Marshal.container(reflect, child);
            }
            return new NodeKeyPair(childNode, key);
        };
        return n;
    }
}
