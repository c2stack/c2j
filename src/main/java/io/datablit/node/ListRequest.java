package io.datablit.node;

import io.datablit.meta.MetaList;

public class ListRequest extends BaseRequest {
    public MetaList meta;
    public boolean create;
    public Value[] key;
    public long row;
    public boolean isFirst;

    public ListRequest() {
    }

    public ListRequest(MetaList meta, boolean isFirst) {
        this.meta = meta;
        this.isFirst = isFirst;
    }

    public ListRequest copy() {
        ListRequest copy = new ListRequest();
        copy.meta = meta;
        copy.create = create;
        copy.key = key;
        copy.isFirst = isFirst;
        return copy;
    }

    public String getRequestPath(Selection sel) {
        String p = sel.path.toString() + "/" + meta.getIdent();
        if (key != null && key.length > 0) {
            // TODO: support compound keys
            p = p + "=" + key.toString();
        }
        return p;
    }
}
