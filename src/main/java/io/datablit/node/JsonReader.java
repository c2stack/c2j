package io.datablit.node;

import io.datablit.meta.MetaUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Reader;

/**
 *
 */
public class JsonReader {
    private Node root;

    public JsonReader(Reader in) {
        JSONTokener toks = new JSONTokener(in);
        Object rootObj = toks.nextValue();
        this.root = container((JSONObject) rootObj);
    }

    public Node getNode() {
        return this.root;
    }

    Node container(JSONObject o) {
        MyNode n = new MyNode();
        n.onSelect = (ContainerRequest r) -> {
            if (o.has(r.meta.getIdent())) {
                Object child = o.get(r.meta.getIdent());
                if (child != null) {
                    if (MetaUtil.isList(r.meta)) {
                        return list((JSONArray)child);
                    } else {
                        return container((JSONObject)child);
                    }
                }
            }
            return null;
        };
        n.onRead = (FieldRequest r) -> {
            if (o.has(r.meta.getIdent())) {
                Object child = o.get(r.meta.getIdent());
                if (child != null) {
                    return Value.coerse(r.meta.getDataType(), child);
                }
            }
            return null;
        };
        return n;
    }

    Node list(JSONArray a) {
        MyNode n = new MyNode();
        n.onNext = (ListRequest r) -> {
            int row = (int)r.row;
            if (a.length() > row) {
                Object record = a.get(row);
                if (record != null) {
                    return new NodeKeyPair(container((JSONObject)record));
                }
            }
            return null;
        };
        return n;
    }
}
