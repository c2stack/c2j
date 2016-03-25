package io.datablit.node;

import io.datablit.meta.MetaCollection;
import io.datablit.meta.MetaList;

public class Path {
    MetaCollection meta;
    Value[] key;
    Path parent;

    public Path(MetaCollection meta) {
        this.meta = meta;
    }

    public Path(Path parent, MetaList meta, Value[] key) {
        this.parent = parent;
        this.meta = meta;
        this.key = key;

    }

    public Path(Path parent, MetaCollection meta) {
        this.parent = parent;
        this.meta = meta;
    }

    public Path getParent() {
        return this.parent;
    }

    public MetaCollection getMeta() {
        return this.meta;
    }

    public Value[] getKey() {
        return this.key;
    }

    void str(StringBuilder sb) {
        if (parent != null) {
            parent.str(sb);
        }
        if (sb.length() > 0) {
            sb.append('/');
        }
        sb.append(meta.getIdent());
        if (key != null) {
            sb.append('=');
            for (int i = 0; i < key.length; i++) {
                if (i != 0) {
                    sb.append(',');
                }
                sb.append(key[i].toString());
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        str(sb);
        return sb.toString();
    }

    public int length() {
        int length = 0;
        Path p = this;
        while (p != null) {
            length++;
            p = p.parent;
        }
        return length;
    }
}

