package io.datablit.node;

import io.datablit.meta.Meta;
import io.datablit.meta.MetaCollection;
import io.datablit.meta.Rpc;

import java.util.Iterator;

/**
 *
 */
public interface WalkController {
    Iterator<Meta> containerIterator(Selection sel);
    Selection visitList(ListRequest r);
    Selection visitContainer(ContainerRequest r);
    Selection visitAction(ActionRequest r);
    Value visitField(FieldRequest r);
}
