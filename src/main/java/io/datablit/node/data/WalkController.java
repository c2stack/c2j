package org.conf2.data;

import org.conf2.schema.Meta;
import org.conf2.schema.MetaCollection;
import org.conf2.schema.Rpc;

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
