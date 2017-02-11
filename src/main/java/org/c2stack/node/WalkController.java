package org.c2stack.node;

import org.c2stack.meta.Meta;
import org.c2stack.meta.MetaCollection;
import org.c2stack.meta.Rpc;

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
