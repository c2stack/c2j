package org.conf2.data;

import org.conf2.schema.MetaCollection;

public class ContainerRequest extends BaseRequest {
    public MetaCollection meta;
    public boolean create;

    public ContainerRequest() {
    }

    public ContainerRequest(MetaCollection meta) {
        this.meta = meta;
    }

    public ContainerRequest copy() {
        ContainerRequest copy = new ContainerRequest();
        copy.meta = meta;
        copy.create = create;
        return copy;
    }

    public String getRequestPath(Selection sel) {
        return sel.path.toString() + "/" + meta.getIdent();
    }
}
